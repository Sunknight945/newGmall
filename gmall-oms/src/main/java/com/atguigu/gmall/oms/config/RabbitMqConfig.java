package com.atguigu.gmall.oms.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Configuration
public class RabbitMqConfig {

	/**
	 * 声明一个死信队列 还未绑定到 交换机
	 *
	 * @return 声明的死信队列
	 */
	@Bean("ORDER-TTL-QUEUE")
	public Queue ttlQueue() {
		Map<String, Object> arguments = new HashMap<>();
		arguments.put("x-dead-letter-exchange", "GMALL-ORDER-EXCHANGE");
		arguments.put("x-dead-letter-routing-key", "order.ttl");
		arguments.put("x-message-ttl", 90000);
		return new Queue("ORDER-TTL-QUEUE", true, false, false, arguments);
	}


	/**
	 * 绑定的 交换机的信息 但是还没有声明 我们绑定的交换机.
	 *
	 * @return bdQueue
	 */
	@Bean("ORDER-TTL-BINDING")
	public Binding ttlBinding() {
		return new Binding("ORDER-TTL-QUEUE", Binding.DestinationType.QUEUE,
			"GMALL-ORDER-EXCHANGE", "order.ttl", null);
	}

	@Bean("ORDER-DEAD-QUEUE")
	public Queue dlQueue() {
		return new Queue("ORDER-DEAD-QUEUE", true, false, false, null);
	}

	@Bean("ORDER-DEAD-BINDING")
	public Binding deadBinding(){
		return new Binding("ORDER-DEAD-QUEUE", Binding.DestinationType.QUEUE,
			"GMALL-ORDER-EXCHANGE", "order.dead", null);
	}



}


