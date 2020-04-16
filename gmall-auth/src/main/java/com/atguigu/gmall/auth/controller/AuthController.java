package com.atguigu.gmall.auth.controller;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.atguigu.core.bean.Resp;
import com.atguigu.core.utils.CookieUtils;
import com.atguigu.gmall.auth.config.JwtProperties;
import com.atguigu.gmall.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@RestController
@RequestMapping("auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@Resource
	private JwtProperties jwtProperties;

	@PostMapping()
	public Resp<Object> authentication(@RequestParam("username") String userName,
	                                   @RequestParam("password") String password,
	                                   HttpServletRequest request,
	                                   HttpServletResponse response) {
		String token = this.authService.authentication(userName, password);
		if (StrUtil.isEmpty(token)) {
			return Resp.fail("用户名或密码错误");
		}
		//将token 写入cookie, 并指定httpOnly 为true , 防止通过js获取和修改
		CookieUtils.setCookie(request, response, jwtProperties.getCookieName(), token, this.jwtProperties.getExpire() * 60, CharsetUtil.UTF_8, true);
		return Resp.ok(null);
	}
}


