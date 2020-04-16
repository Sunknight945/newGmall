package com.atguigu.gmall.wms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  /**
   * Override this method to configure the {@link HttpSecurity}. Typically subclasses
   * should not invoke this method by calling super as it may override their
   * configuration. The default configuration is:
   *
   * <pre>
   * http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic();
   * </pre>
   *
   * @param http the {@link HttpSecurity} to modify
   * @throws Exception if an error occurs
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests().antMatchers("/**").permitAll();
    //跨站请求伪造（英语：Cross-site request forgery） 总是忘记
    http.csrf().disable();
  }
}


