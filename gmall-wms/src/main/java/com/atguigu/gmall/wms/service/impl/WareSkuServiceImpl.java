package com.atguigu.gmall.wms.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.gmall.wms.dao.WareSkuDao;
import com.atguigu.gmall.wms.entity.WareSkuEntity;
import com.atguigu.gmall.wms.service.WareSkuService;
import com.atguigu.gmall.wms.vo.SkuLockVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author ovo
 */
@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

	private final RedissonClient redissonClient;

	private final StringRedisTemplate redisTemplate;

	private final AmqpTemplate amqpTemplate;

	private static final String WARE_PREFIX_KEY = "stock:lock";

	@Resource
	private WareSkuDao wareSkuDao;

	public WareSkuServiceImpl(RedissonClient redissonClient, StringRedisTemplate redisTemplate, AmqpTemplate amqpTemplate) {
		this.redissonClient = redissonClient;
		this.redisTemplate = redisTemplate;
		this.amqpTemplate = amqpTemplate;
	}


	@Override
	public PageVo queryPage(QueryCondition params) {
		IPage<WareSkuEntity> page = this.page(
			new Query<WareSkuEntity>().getPage(params),
			new QueryWrapper<>()
		);
		return new PageVo(page);
	}

	/**
	 * 锁sku的库存
	 *
	 * @param skuLockVos ff
	 */
	@Override
	public String lockSkuWare(List<SkuLockVo> skuLockVos) {
		if (CollectionUtil.isEmpty(skuLockVos)) {
			return "没有选中的商品";
		}
		// 检验并锁定库存 (锁住了库存充足的, 当缺少库存的 没有锁住 , 所以算作下单失败, 并且需要报锁住的库存释放掉.
		skuLockVos.forEach(this::lockSku);
		List<SkuLockVo> unLockedList = skuLockVos.stream().filter(skuLockVo -> !skuLockVo.getLock()).collect(Collectors.toList());
		if (CollectionUtil.isNotEmpty(unLockedList)) {
			StringBuffer skuNames = new StringBuffer();
			List<String> skuNameList = unLockedList.stream().map(SkuLockVo::getSkuName).collect(Collectors.toList());
			skuNameList.forEach(skuName -> skuNames.append(skuName).append(","));
			// 解锁
			skuLockVos.forEach(this::setLockSkuToUnlock);
			return "下单失败, 商品库存不足:" + skuNames.toString();
		}
		String orderToken = skuLockVos.get(0).getOrderToken();
		this.redisTemplate.opsForValue().set(WARE_PREFIX_KEY + orderToken, orderToken);
		//锁定成功, 发送延时消息, 定时解锁  // 这个就是死信队列了.  然后这个死信队列好像有点特殊.. 没有找到他的监听者. 但是貌似好像
		// 是 com.atguigu.gmall.oms.listener.OrderListener  @RabbitListener(queues = {"ORDER-DEAD-QUEUE"})    public void closeOrder(String orderToken){
		this.amqpTemplate.convertAndSend("GMALL-ORDER-EXCHANGE", "stock.ttl", orderToken);
		return null;
	}

	/**
	 * 解锁
	 *
	 * @param skuLockVo 被解锁的wareSku需要的信息
	 */
	private void setLockSkuToUnlock(SkuLockVo skuLockVo) {
		this.wareSkuDao.setLockSkuToUnlock(skuLockVo.getSkuWareId(), skuLockVo.getSkuId(), skuLockVo.getCount());
	}


	private void lockSku(SkuLockVo skuLockVo) {
		RLock lockStock = this.redissonClient.getLock("lockStock" + skuLockVo.getSkuId());
		lockStock.lock();
		// 查询剩余库存够不过
		List<WareSkuEntity> wareSkuEntityList = this.wareSkuDao.checkStock(skuLockVo.getSkuId(), skuLockVo.getCount());
		if (CollectionUtil.isEmpty(wareSkuEntityList)) {
			skuLockVo.setLock(false);
			lockStock.unlock();
		}
		// 锁定库存信息
		Long id = wareSkuEntityList.get(0).getId();
		this.wareSkuDao.lockStock(skuLockVo.getSkuId(), skuLockVo.getCount(), id);
		skuLockVo.setSkuWareId(id);
		skuLockVo.setLock(true);
		lockStock.unlock();
	}
}
