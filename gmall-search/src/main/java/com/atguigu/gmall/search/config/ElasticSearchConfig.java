package com.atguigu.gmall.search.config;

import java.beans.BeanInfo;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.inject.BindingAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Configuration
public class ElasticSearchConfig {
	@Bean
	public RestHighLevelClient restHighLevelClient() {
		return new RestHighLevelClient(RestClient.builder(HttpHost.create("192.168.255.130:9200")));
	}

}


