package com.atguigu.gmall.order.interceptor;

import cn.hutool.core.util.StrUtil;
import com.atguigu.core.bean.UserInfo;
import com.atguigu.core.utils.CookieUtils;
import com.atguigu.core.utils.JwtUtils;
import com.atguigu.gmall.order.config.JwtProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {


	private final JwtProperties jwtProperties;

	private static final ThreadLocal<UserInfo> THREAD_LOCAL = new ThreadLocal<>();

	public LoginInterceptor(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		UserInfo userInfo = new UserInfo();
		String token = CookieUtils.getCookieValue(request, this.jwtProperties.getCookieName());
		if (StrUtil.isNotBlank(token)) {
			Map<String, Object> infoFromToken = JwtUtils.getInfoFromToken(token, this.jwtProperties.getPublicKey());
			userInfo.setUserId(new Long(infoFromToken.get("id").toString()));
		}
		THREAD_LOCAL.set(userInfo);
		return super.preHandle(request, response, handler);
	}

	public static UserInfo getUserInfo() {
		return THREAD_LOCAL.get();
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		THREAD_LOCAL.remove();
		super.afterCompletion(request, response, handler, ex);
	}
}


