package com.atguigu.gmall.gateway.config;

import cn.hutool.core.collection.CollectionUtil;
import com.atguigu.core.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Component
public class AuthGatewayFilter implements GatewayFilter {

	@Autowired
	private JwtProperties jwtProperties;

	/**
	 * 第一步 编写一个 AuthGatewayFilter 过滤器实现 GatewayFilter 这个过滤器接口
	 * 处理Web请求，并（可选）委托给下一个{@code WebFilter}
	 * 通过给定的{@link GatewayFilterChain}。
	 *
	 * @param exchange 当前服务器交流
	 * @param chain    链提供了一种委托给下一个过滤器的方法
	 * @return {@code Mono <Void>}表示请求处理何时完成
	 */
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		//1.获取jwt 类型的token信息
		ServerHttpRequest request = exchange.getRequest();
		ServerHttpResponse response = exchange.getResponse();
		MultiValueMap<String, HttpCookie> cookies = request.getCookies();
		if (CollectionUtil.isEmpty(cookies)) {
			//拦截  //这被搞事情或用户故意在浏览器上禁用了cookie 所以为空
			//告诉浏览器被拦截 设置一个状态码 HttpStatus.UNAUTHORIZED  (401 身份未认证)
			response.setStatusCode(HttpStatus.UNAUTHORIZED);
			return response.setComplete();
		}
		HttpCookie cookie = cookies.getFirst(this.jwtProperties.getCookieName());
		//2.判断jwt类型的token是否为空
		if (cookie == null) {
			//拦截
			response.setStatusCode(HttpStatus.UNAUTHORIZED);
			return response.setComplete();
		}

		//3.解析jwt,若果正常解析就放行
		try {
			JwtUtils.getInfoFromToken(cookie.getValue(), this.jwtProperties.getPublicKey());
		} catch (Exception e) {
			e.printStackTrace();
			//拦截
			response.setStatusCode(HttpStatus.UNAUTHORIZED);
			response.setComplete();
		}
		//放行
		return chain.filter(exchange);
	}
}



