package com.atguigu.gmall.order.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.atguigu.core.bean.Resp;
import com.atguigu.core.bean.UserInfo;
import com.atguigu.core.exception.OrderException;
import com.atguigu.gmall.cart.pojo.Cart;
import com.atguigu.gmall.oms.entity.OrderEntity;
import com.atguigu.gmall.oms.vo.OrderItemVo;
import com.atguigu.gmall.oms.vo.OrderSubmitVo;
import com.atguigu.gmall.order.feign.*;
import com.atguigu.gmall.order.interceptor.LoginInterceptor;
import com.atguigu.gmall.order.service.OrderService;
import com.atguigu.gmall.order.vo.OrderConfirmVo;
import com.atguigu.gmall.pms.entity.SkuInfoEntity;
import com.atguigu.gmall.pms.entity.SkuSaleAttrValueEntity;
import com.atguigu.gmall.sms.vo.SaleVo;
import com.atguigu.gmall.ums.entity.MemberEntity;
import com.atguigu.gmall.ums.entity.MemberReceiveAddressEntity;
import com.atguigu.gmall.wms.entity.WareSkuEntity;
import com.atguigu.gmall.wms.vo.SkuLockVo;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;


/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Service
public class OrderServiceImpl implements OrderService {


	private final StringRedisTemplate redisTemplate;

	private final GmallCartClient cartClient;

	private final GmallOmsClient omsClient;

	private final GmallPmsClient pmsClient;

	private final GmallSmsClient smsClient;

	private final GmallUmsClient umsClient;

	private final GmallWmsClient wmsClient;

	private final AmqpTemplate amqpTemplate;

	private final ThreadPoolExecutor threadPoolExecutor;

	private static final String PREFIX_ORDER_TOKEN = "order:token";

	public OrderServiceImpl(StringRedisTemplate redisTemplate, GmallCartClient cartClient, GmallOmsClient omsClient, GmallPmsClient pmsClient, GmallSmsClient smsClient, GmallUmsClient umsClient, GmallWmsClient wmsClient, AmqpTemplate amqpTemplate, ThreadPoolExecutor threadPoolExecutor) {
		this.redisTemplate = redisTemplate;
		this.cartClient = cartClient;
		this.omsClient = omsClient;
		this.pmsClient = pmsClient;
		this.smsClient = smsClient;
		this.umsClient = umsClient;
		this.wmsClient = wmsClient;
		this.amqpTemplate = amqpTemplate;
		this.threadPoolExecutor = threadPoolExecutor;
	}

	@Override
	public OrderConfirmVo getOrderConfirmVo() {
		UserInfo userInfo = LoginInterceptor.getUserInfo();
		Long userId = userInfo.getUserId();
		if (userId == null) {
			return null;
		}
		OrderConfirmVo orderConfirmVo = new OrderConfirmVo();
		//获取用户的收货地址列表
		CompletableFuture<Void> addressesCompletableFuture = CompletableFuture.runAsync(() -> {
			Resp<List<MemberReceiveAddressEntity>> addressesByUserId = this.umsClient.queryAddressesByUserId(userId);
			List<MemberReceiveAddressEntity> memberReceiveAddressEntityList = addressesByUserId.getData();
			orderConfirmVo.setMemberReceiveAddressEntities(memberReceiveAddressEntityList);
		}, threadPoolExecutor);
		//获取购物车中选中的商品信息 skuId

		Resp<List<Cart>> queryCarts = this.cartClient.queryCartsByFeignMemberId(userId);
		List<Cart> cartList = queryCarts.getData();
		assert cartList != null;

		CompletableFuture<Void> skuCompletableFuture = CompletableFuture.runAsync(() -> {
			List<OrderItemVo> orderItemVoList = cartList.stream().map(cart -> {
				OrderItemVo orderItemVo = new OrderItemVo();
				orderItemVo.setSkuId(cart.getSkuId());
				Resp<SkuInfoEntity> skuInfoEntityResp = this.pmsClient.querySkuInfoBySkuId(cart.getSkuId());
				SkuInfoEntity skuInfoEntity = skuInfoEntityResp.getData();
				assert skuInfoEntity != null;
				CompletableFuture<Void> orderItemCompletableFuture = CompletableFuture.runAsync(() -> {
					orderItemVo.setTitle(skuInfoEntity.getSkuTitle());
					orderItemVo.setDefaultImage(skuInfoEntity.getSkuDefaultImg());
					orderItemVo.setPrice(skuInfoEntity.getPrice());
					orderItemVo.setCount(cart.getCount());
				}, threadPoolExecutor);
				CompletableFuture<Void> wareSkuCompletableFuture = CompletableFuture.runAsync(() -> {
					Resp<List<WareSkuEntity>> wareSkuEntityResp = this.wmsClient.queryWareSkuEntityResp(cart.getSkuId());
					if (CollectionUtil.isNotEmpty(wareSkuEntityResp.getData())) {
						boolean flag = wareSkuEntityResp.getData().stream().anyMatch(resp -> resp.getStock() > 0);
						orderItemVo.setStore(flag);
					}
				}, threadPoolExecutor);
				CompletableFuture<Void> saleAttrValueCompletableFuture = CompletableFuture.runAsync(() -> {
					Resp<List<SkuSaleAttrValueEntity>> saleAttrValueBySkuId = this.pmsClient.querySaleAttrValueBySkuId(cart.getSkuId());
					if (CollectionUtil.isNotEmpty(saleAttrValueBySkuId.getData())) {
						orderItemVo.setSaleAttrValues(saleAttrValueBySkuId.getData());
						Resp<List<SaleVo>> querySaleVoBySkuId = this.smsClient.querySaleVoBySkuId(cart.getSkuId());
						orderItemVo.setSaleVos(querySaleVoBySkuId.getData());
						orderItemVo.setWeight(skuInfoEntity.getWeight());
					}
				}, threadPoolExecutor);
				CompletableFuture.allOf(orderItemCompletableFuture, wareSkuCompletableFuture, saleAttrValueCompletableFuture).join();
				return orderItemVo;
			}).collect(Collectors.toList());
			orderConfirmVo.setOrderItemVos(orderItemVoList);
		}, threadPoolExecutor);


		//查询用户信息, 获取积分 userId
		CompletableFuture<Void> memberEntityCompletableFuture = CompletableFuture.runAsync(() -> {
			Resp<MemberEntity> memberEntityResp = this.umsClient.queryMemberById(userId);
			MemberEntity memberEntity = memberEntityResp.getData();
			if (memberEntity != null) {
				Integer integration = memberEntity.getIntegration();
				if (integration != null && integration > 0) {
					orderConfirmVo.setBounds(integration);
				}
			}
		}, threadPoolExecutor);
		//生成一个未养成标志, 防止重复提交(相应到页面有一份, 有一份保存到redis中)
		CompletableFuture<Void> orderTokenCompletableFuture = CompletableFuture.runAsync(() -> {
			String orderTokenForOrderPage = IdWorker.getIdStr();
			this.redisTemplate.opsForValue().set(PREFIX_ORDER_TOKEN + orderTokenForOrderPage, orderTokenForOrderPage);
			orderConfirmVo.setOrderToken(orderTokenForOrderPage);
		}, threadPoolExecutor);

		CompletableFuture.allOf(addressesCompletableFuture, skuCompletableFuture, memberEntityCompletableFuture, orderTokenCompletableFuture).join();

		return orderConfirmVo;
	}

	/**
	 * 提交订单
	 *
	 * @param submitVo 订单信息
	 * @return ff
	 */
	@Override
	public OrderEntity postOrderSubmitVo(OrderSubmitVo submitVo) {
		UserInfo userInfo = LoginInterceptor.getUserInfo();
		//1.获取token 防止重复提交
		String orderToken = submitVo.getOrderToken();
		String script = "if redis.call('get', KEYS[1]) == ARGV[1] then redis.call('dle' , KEYS[1]) else return 0";
		Long flag = this.redisTemplate.execute(new DefaultRedisScript<>(script), Collections.singletonList(PREFIX_ORDER_TOKEN + orderToken), orderToken);
		if (0 == flag) {
			throw new OrderException("请勿重复提交订单!");
		}
		//2.校验价格
		BigDecimal totalPrice = submitVo.getTotalPrice();
		List<OrderItemVo> orderItemVoList = submitVo.getOrderItemVoList();
		BigDecimal correctTotalPrice = orderItemVoList.stream().map(orderItemVo -> {
			Resp<SkuInfoEntity> skuInfoEntityResp = this.pmsClient.querySkuInfoBySkuId(orderItemVo.getSkuId());
			if (skuInfoEntityResp.getData() != null) {
				SkuInfoEntity skuInfoEntity = skuInfoEntityResp.getData();
				return skuInfoEntity.getPrice().multiply(new BigDecimal(orderItemVo.getCount()));
			}
			return new BigDecimal(0);
		}).reduce(BigDecimal.ZERO, BigDecimal::add);
		if (totalPrice.compareTo(correctTotalPrice) == 0) {
			throw new OrderException("页面已过期, 请刷新过在提交!");
		}

		//3.校验库存
		List<SkuLockVo> skuLockVoList = orderItemVoList.stream().map(orderItemVo -> {
			SkuLockVo skuLockVo = new SkuLockVo();
			skuLockVo.setSkuName(orderItemVo.getTitle());
			skuLockVo.setOrderToken(orderToken);
			skuLockVo.setCount(orderItemVo.getCount());
			return skuLockVo;
		}).collect(Collectors.toList());
		Resp<Object> lockSkuWare = this.wmsClient.lockSkuWare(skuLockVoList);
		if (lockSkuWare.getData() != null) {
			throw new OrderException(lockSkuWare.getMsg());
		}

		//4.下单
		Resp<OrderEntity> orderEntityResp;
		try {
			submitVo.setUserId(userInfo.getUserId());
			orderEntityResp = this.omsClient.saveOrder(submitVo);
		} catch (Exception e) {
			e.printStackTrace();
			//发消息wms, 解锁对应的库存
			this.amqpTemplate.convertAndSend("GMALL-ORDER-EXCHANGE", "stock.unlock", orderToken);
			throw new OrderException("服务器错误, 创建订单失败!");
		}
		//5. 删除购物车
		Map<String, Object> map = new HashMap<>(2);
		map.put("userId", userInfo.getUserId());
		List<Long> skuIds = skuLockVoList.stream().map(SkuLockVo::getSkuId).collect(Collectors.toList());
		map.put("skuIds", skuIds);
		//发消息让 wms 删除订单
		this.amqpTemplate.convertAndSend("GMALL-ORDER-EXCHANGE", "cart.delete", map);
		if (orderEntityResp.getData() != null) {
			return orderEntityResp.getData();
		}
		return null;

	}
}



