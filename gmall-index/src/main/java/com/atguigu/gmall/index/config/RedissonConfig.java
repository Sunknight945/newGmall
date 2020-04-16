package com.atguigu.gmall.index.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Configuration
public class RedissonConfig {
	@Bean
	public RedissonClient redissonClient() {
		Config config = new Config();
		config.useSingleServer().setAddress("redis://192.168.255.130:6379");
		return Redisson.create(config);
	}

}


