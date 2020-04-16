package com.atguigu.gmall.wms.listen;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.wms.dao.WareSkuDao;
import com.atguigu.gmall.wms.vo.SkuLockVo;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Component
public class WmsListener {


	private final StringRedisTemplate redisTemplate;

	private final WareSkuDao wareSkuDao;

	private static final String KEY_PREFIX = "stock:lock";


	public WmsListener(StringRedisTemplate redisTemplate, WareSkuDao wareSkuDao) {
		this.redisTemplate = redisTemplate;
		this.wareSkuDao = wareSkuDao;
	}

	@RabbitListener(bindings = @QueueBinding(
		value = @Queue(value = "WMS-UNLOCK-QUEUE", durable = "true"),
		exchange = @Exchange(value = "GMALL-ORDER-EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
		key = {"stock.unlock"}
	))
	public void unlockListener(String orderToken) {
		String lockJson = this.redisTemplate.opsForValue().get(KEY_PREFIX + orderToken);
		if (StrUtil.isEmpty(lockJson)) {
			return;
		}
		List<SkuLockVo> skuLockVos = JSON.parseArray(lockJson, SkuLockVo.class);
		skuLockVos.forEach(skuLockVo -> this.wareSkuDao.setLockSkuToUnlock(skuLockVo.getSkuWareId(), skuLockVo.getSkuId(), skuLockVo.getCount()));
		this.redisTemplate.delete(KEY_PREFIX + orderToken);
	}

	@RabbitListener(bindings = @QueueBinding(
		value = @Queue(value = "WMS-MINUS-QUEUE", durable = "true"),
		exchange = @Exchange(value = "GMALL-ORDER-EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
		key = {"stock.minus"}
	))
	public void minusStock(String orderToken) {
		String lockJson = this.redisTemplate.opsForValue().get(KEY_PREFIX + orderToken);
		List<SkuLockVo> skuLockVos = JSON.parseArray(lockJson, SkuLockVo.class);
		assert skuLockVos != null;
		skuLockVos.forEach(skuLockVo -> this.wareSkuDao.minusStore(skuLockVo.getSkuWareId(), skuLockVo.getCount()));
	}



}


