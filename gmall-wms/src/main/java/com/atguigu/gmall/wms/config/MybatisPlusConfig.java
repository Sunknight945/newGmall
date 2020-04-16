package com.atguigu.gmall.wms.config;

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
    PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
    return paginationInterceptor;
  }

}


