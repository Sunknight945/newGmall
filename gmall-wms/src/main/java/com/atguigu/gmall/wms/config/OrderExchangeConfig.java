package com.atguigu.gmall.wms.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Component
public class OrderExchangeConfig {


	@Bean("WMS-TTL-QUEUE")
	public Queue ttlQueue() {
		Map<String, Object> arguments = new HashMap<>();
		arguments.put("x-dead-letter-exchange", "GMALL-ORDER-EXCHANGE");
		arguments.put("x-dead-letter-routing-key", "stock.unlock");
		arguments.put("x-message-ttl", 90 * 1000);
		return new Queue("WMS-TTL-QUEUE", true, false, false, arguments);
	}


	@Bean("WMS-TTL-BINDING")
	public Binding ttlBinding() {
		return new Binding("WMS-TTL-QUEUE", Binding.DestinationType.QUEUE,
			"GMALL-ORDER-EXCHANGE", "stock.ttl", null);
	}


}


