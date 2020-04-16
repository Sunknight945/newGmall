package com.atguigu.gmall.cart.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.cart.feign.GmallPmsClient;
import com.atguigu.gmall.cart.feign.GmallSmsClient;
import com.atguigu.gmall.cart.feign.GmallWmsClient;
import com.atguigu.gmall.cart.interceptors.LoginInterceptor;
import com.atguigu.gmall.cart.pojo.Cart;
import com.atguigu.gmall.cart.pojo.UserInfo;
import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.pms.entity.SkuInfoEntity;
import com.atguigu.gmall.pms.entity.SkuSaleAttrValueEntity;
import com.atguigu.gmall.sms.vo.SaleVo;
import com.atguigu.gmall.wms.entity.WareSkuEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private GmallPmsClient pmsClient;

	@Autowired
	private GmallWmsClient wmsClient;

	@Autowired
	private GmallSmsClient smsClient;

	@Autowired
	private ThreadPoolExecutor threadPoolExecutor;

	@Autowired
	private StringRedisTemplate redisTemplate;

	private static String CART_PRE_KEY = "gmall:cart";


	@Override
	public void addCart(Cart cart) {
		String key = getKey();
		//获取购物车
		assert key != null;
		BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
		String skuId = cart.getSkuId().toString();
		Integer count = cart.getCount();

		//判断购物车中是否有该商品
		if (hashOps.hasKey(skuId)) {
			//有 则更新数量
			String cartStrFromRedis = hashOps.get(skuId).toString();
			Cart cartFromRedis = JSON.parseObject(cartStrFromRedis, Cart.class);
			cartFromRedis.setCount(cartFromRedis.getCount() + count);
			hashOps.put(skuId, JSON.toJSONString(cartFromRedis));
		} else {
			//没有的添加到购物车
			cart.setSkuId(Long.valueOf(skuId));
			cart.setCheck(true);
			cart.setCount(cart.getCount());
			CompletableFuture<Void> skuInfoEntityCompletableFuture = CompletableFuture.runAsync(() -> {
				Resp<SkuInfoEntity> skuInfoEntityResp = this.pmsClient.querySkuInfoBySkuId(Long.valueOf(skuId));
				SkuInfoEntity skuInfoEntity = skuInfoEntityResp.getData();
				if (skuInfoEntity != null) {
					cart.setTitle(skuInfoEntity.getSkuTitle());
					cart.setDefaultImage(skuInfoEntity.getSkuDefaultImg());
					cart.setPrice(skuInfoEntity.getPrice());
				}
			}, threadPoolExecutor);
			CompletableFuture<Void> wareSkuEntityCompletableFuture = CompletableFuture.runAsync(() -> {
				Resp<List<WareSkuEntity>> wareSkuEntityResp = this.wmsClient.queryWareSkuEntityResp(Long.valueOf(skuId));
				List<WareSkuEntity> wareSkuEntityList = wareSkuEntityResp.getData();
				if (CollectionUtil.isNotEmpty(wareSkuEntityList)) {
					boolean store = wareSkuEntityList.stream().anyMatch(wareSkuEntity -> wareSkuEntity.getStock() > 0);
					cart.setStore(store);
				}
			}, threadPoolExecutor);
			CompletableFuture<Void> saleVoCompletableFuture = CompletableFuture.runAsync(() -> {
				Resp<List<SaleVo>> saleVoBySkuId = this.smsClient.querySaleVoBySkuId(Long.valueOf(skuId));
				List<SaleVo> saleVoList = saleVoBySkuId.getData();
				if (CollectionUtil.isNotEmpty(saleVoList)) {
					cart.setSaleVos(saleVoList);
				}
			}, threadPoolExecutor);
			CompletableFuture<Void> saleAttrValueCompletableFuture = CompletableFuture.runAsync(() -> {
				Resp<List<SkuSaleAttrValueEntity>> saleAttrValueBySkuId = this.pmsClient.querySaleAttrValueBySkuId(Long.valueOf(skuId));
				List<SkuSaleAttrValueEntity> skuSaleAttrValueEntityList = saleAttrValueBySkuId.getData();
				if (CollectionUtil.isNotEmpty(skuSaleAttrValueEntityList)) {
					cart.setSaleAttrValues(skuSaleAttrValueEntityList);
				}
			}, threadPoolExecutor);
			CompletableFuture.allOf(skuInfoEntityCompletableFuture, wareSkuEntityCompletableFuture, saleVoCompletableFuture, saleAttrValueCompletableFuture).join();

			hashOps.put(skuId, JSON.toJSONString(cart));
		}
	}

	private String getKey() {
		UserInfo userInfo = LoginInterceptor.getUserInfo();
		String key = null;
		if (userInfo != null) {
			if (userInfo.getId() != null) {
				key = CART_PRE_KEY + userInfo.getId();
			} else {
				key = CART_PRE_KEY + userInfo.getUserKey();
			}
		}
		return key;
	}

	/**
	 * 查询获取 用户 的购物车列表
	 */
	@Override
	public List<Cart> queryCarts() {
		//1. 变量提升
		//2. 状态有四种  (记住这两点就够了啊)
		UserInfo userInfo = LoginInterceptor.getUserInfo();
		Long id = userInfo.getId();
		String userKey = userInfo.getUserKey();
		//主备放回的数据 变量提升 哈哈
		List<Cart> cartList = null;

		//1.全都是空那么 剩下三种 (因为全为空的话 那么就是他自己把key弄丢了 但是这不能能 因为我们在
		// 拦截器里面设置了一个userKey 所以这个时候因该是拦截器除了问题, 直接放回null 是可取的.
		if (userKey == null && id == null) {
			return null;
		}
		// 这个时候用户没有登录被拦截器那边拦截设置了一个useKey
		if (userKey != null) {
			BoundHashOperations<String, Object, Object> unLoginHashOps = this.redisTemplate.boundHashOps(CART_PRE_KEY + userKey);
			List<Object> unLoginCartList = unLoginHashOps.values();
			assert unLoginCartList != null;
			cartList = unLoginCartList.stream().map(cartUnCast -> {
				Cart cart = JSON.parseObject(cartUnCast.toString(), Cart.class);
				Resp<SkuInfoEntity> skuInfoEntityResp = this.pmsClient.querySkuInfoBySkuId(cart.getSkuId());
				SkuInfoEntity skuInfoEntity = skuInfoEntityResp.getData();
				cart.setWeight(skuInfoEntity.getWeight());
				cart.setPrice(skuInfoEntity.getPrice());
				return cart;
			}).collect(Collectors.toList());
			if (id == null) {
				return cartList;
			}
		}
		//一直都是登录 状态当然没有拦截器给的userKey 所以
		if (userKey == null) {
			BoundHashOperations<String, Object, Object> loginHashOps = this.redisTemplate.boundHashOps(CART_PRE_KEY + id);
			List<Object> loginCartList = loginHashOps.values();
			assert loginCartList != null;
			cartList = loginCartList.stream().map(cartUnCast -> {
				Cart cart = JSON.parseObject(cartUnCast.toString(), Cart.class);
				Resp<SkuInfoEntity> skuInfoEntityResp = this.pmsClient.querySkuInfoBySkuId(cart.getSkuId());
				cart.setPrice(skuInfoEntityResp.getData().getPrice());
				return cart;
			}).collect(Collectors.toList());
			return cartList;
		}
		//前面没有登录但是在redis中有购物车, 并且根据实际情况redis里面未登录的购物车保存的时间是很久的所以极有可能当用户没有清理cookie的时候
		//两面的cookie中仍然有我们的userKey. so 可以无伤获取第二部 未登陆的购物车.
		BoundHashOperations<String, Object, Object> loginHashOps = this.redisTemplate.boundHashOps(CART_PRE_KEY + id);
		List<Object> loginCartList = loginHashOps.values();
		if (CollectionUtil.isNotEmpty(loginCartList)) {
			cartList = cartList.stream().map(cart -> {
				Integer count = cart.getCount();
				if (loginHashOps.hasKey(cart.getSkuId().toString())) {
					Object cartUnCast = this.redisTemplate.opsForHash().get(CART_PRE_KEY + id, cart.getSkuId().toString());
					cart = JSON.parseObject(cartUnCast.toString(), Cart.class);
					cart.setCount(cart.getCount() + count);
				}
				loginHashOps.put(cart.getSkuId().toString(), JSON.toJSONString(cart));
				return cart;
			}).collect(Collectors.toList());
			List<Object> cartListUnCast = loginHashOps.values();
			cartList = cartListUnCast.stream().map(cartUnCast -> {
				Cart cart = JSON.parseObject(cartUnCast.toString(), Cart.class);
				Resp<SkuInfoEntity> skuInfoEntityResp = this.pmsClient.querySkuInfoBySkuId(cart.getSkuId());
				cart.setPrice(skuInfoEntityResp.getData().getPrice());
				return cart;
			}).collect(Collectors.toList());
			this.redisTemplate.delete(CART_PRE_KEY + userKey);
			return cartList;
		}
		return null;
	}

	/**
	 * 更新购物中某个商品的数量
	 *
	 * @param cart
	 */
	@Override
	public void update(Cart cart) {
		String key = this.getKey();
		BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
		Object cartUnCast = this.redisTemplate.opsForHash().get(key, cart.getSkuId().toString());
		assert cartUnCast != null;
		Cart cartCast = JSON.parseObject(cartUnCast.toString(), Cart.class);
		cartCast.setCount(cart.getCount());
		hashOps.put(cart.getSkuId().toString(), JSON.toJSONString(cartCast));
	}


	/**
	 * 删除购物车
	 *
	 * @param skuIdList
	 */
	@Override
	public void deleteCart(List<Long> skuIdList) {
		String key = this.getKey();
		BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
		skuIdList.forEach(skuId -> {
			if (hashOps.hasKey(skuId.toString())) {
				hashOps.delete(skuId);
			}
		});
	}

	/**
	 * order模块的远程调用CartApi带memberId参数查询用户的购物车列表
	 *
	 * @param memberId
	 */
	@Override
	public List<Cart> queryCartsByFeignMemberId(Long memberId) {
		if (this.redisTemplate.hasKey(CART_PRE_KEY + memberId)) {
			BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(CART_PRE_KEY + memberId);
			if (CollectionUtil.isNotEmpty(hashOps.values())) {
				List<Object> cartListUnCast = hashOps.values();
				assert cartListUnCast != null;
				return cartListUnCast.stream().map(cartUnCast -> {
					return JSON.parseObject(cartUnCast.toString(), Cart.class);
				}).collect(Collectors.toList());
			}
		}
		return null;
	}
}


