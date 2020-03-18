package com.atguigu.gmall.pms.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Configuration
public class MybatisPlusConfig {
  @Bean
  PaginationInterceptor paginationInterceptor() {
    PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
    // paginationInterceptor.setLimit(100);
    return paginationInterceptor;
  }

}


