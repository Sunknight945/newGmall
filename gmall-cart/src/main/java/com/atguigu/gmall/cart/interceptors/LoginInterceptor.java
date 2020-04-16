package com.atguigu.gmall.cart.interceptors;

import cn.hutool.core.util.StrUtil;
import com.atguigu.core.utils.CookieUtils;
import com.atguigu.core.utils.JwtUtils;
import com.atguigu.gmall.cart.config.JwtProperties;
import com.atguigu.gmall.cart.pojo.UserInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {


	@Resource
	private JwtProperties jwtProperties;


	private static final ThreadLocal<UserInfo> THREAD_LOCAL = new ThreadLocal<>();


	/**
	 * 统一获取登陆状态
	 *
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		UserInfo userInfo = new UserInfo();

		//获取cookie中的token信息(jwt) 及userKey信息
		String token = CookieUtils.getCookieValue(request, this.jwtProperties.getCookieName());
		String userKey = CookieUtils.getCookieValue(request, this.jwtProperties.getUserKey());
		//判断有没有userKey, 没有制作一个放入token中
		if (StrUtil.isEmpty(userKey)) {
			userKey = UUID.randomUUID().toString();
			CookieUtils.setCookie(request, response, jwtProperties.getUserKey(), userKey, 60 * 60 * 24 * 180);
		}
		userInfo.setUserKey(userKey);
		//判断有没有token
		if (StrUtil.isNotBlank(token)) {
			//解析token
			Map<String, Object> infoFromToken = JwtUtils.getInfoFromToken(token, this.jwtProperties.getPublicKey());
			userInfo.setId(new Long(infoFromToken.get("id").toString()));
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
	}


}


