package com.atguigu.gmall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Configuration
public class GmallCorsConfig {
  @Bean
  public CorsWebFilter corsWebFilter() {
    //配置cors跨域请求
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowCredentials(true);
    configuration.addAllowedOrigin("http://localhost:1000");
    configuration.addAllowedMethod("*");
    configuration.addAllowedHeader("*");
    //配置源对象
    UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
    corsConfigurationSource.registerCorsConfiguration("/**", configuration);
    //cors过滤器对象, 返回
    return new CorsWebFilter(corsConfigurationSource);
  }
}
