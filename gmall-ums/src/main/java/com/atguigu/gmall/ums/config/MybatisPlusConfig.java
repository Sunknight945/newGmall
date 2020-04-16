package com.atguigu.gmall.ums.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Configuration
public class MybatisPlusConfig {
	@Bean
	public PaginationInterceptor paginationInterceptor(){

		return new PaginationInterceptor();
	}

}


