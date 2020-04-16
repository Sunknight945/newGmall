package com.atguigu.gmall.item.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 * 异步编排需要线程池
 */
@Configuration
public class ThreadPool {

	@Bean
	public ThreadPoolExecutor threadPoolExecutor(){
		return new ThreadPoolExecutor(50,500,50, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000));
	}

}


