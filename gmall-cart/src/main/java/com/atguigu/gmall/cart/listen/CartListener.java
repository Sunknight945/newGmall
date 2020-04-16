package com.atguigu.gmall.cart.listen;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 * this.amqpTemplate.convertAndSend("GMALL-ORDER-EXCHANGE", "cart.delete", map);
 */
@Component
public class CartListener {

	private final StringRedisTemplate redisTemplate;

	private static final String KEY_PREFIX = "gmall:cart:";

	private static final String PRICE_PREFIX = "gmall:sku:";

	public CartListener(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}


	@RabbitListener(bindings = @QueueBinding(
		value = @Queue(value = "ORDER-CART-QUEUE", durable = "true"),
		exchange = @Exchange(value = "GMALL-ORDER-EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
		key = {"cart.delete"}
	))
	public void deleteCartListener(Map<String, Object> map) {
		Long userId = (Long) map.get("userId");
		List<Long> skuIds = (List<Long>) map.get("skuIds");
		BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(KEY_PREFIX + userId);
		String[] skuIdListStr = skuIds.stream().map(Objects::toString).toArray(String[]::new);
		hashOps.delete((Object) skuIdListStr);
	}





}


