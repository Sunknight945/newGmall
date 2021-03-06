package com.atguigu.gmall.ums.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import io.seata.rm.datasource.DataSourceProxy;
import javax.sql.DataSource;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Configuration
public class DatasourceConfig {

  /**
   * 需要将 DataSourceProxy 设置为主数据源，否则事务无法回滚
   *
   */
  @Primary
  @Bean("dataSource")
  public DataSource dataSource(@Value("${spring.datasource.driver-class-name}") String classname,
                               @Value("${spring.datasource.url}") String url,
                               @Value("${spring.datasource.username}") String username,
                               @Value("${spring.datasource.password}") String password ) {
    HikariDataSource hikariDataSource = new HikariDataSource();
    hikariDataSource.setDriverClassName(classname);
    hikariDataSource.setJdbcUrl(url);
    hikariDataSource.setUsername(username);
    hikariDataSource.setPassword(password);
    return new DataSourceProxy(hikariDataSource);
  }
}


