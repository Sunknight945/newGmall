package com.atguigu.gmall.auth.service;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
public interface AuthService {

	/**
	 * oss单点登录 意味  用户输入用户名和密码调用ums-模块的接口验证 如果 为真
	 * 则 把用户相应的数据写到cookie 和session 里面. 没有就 返回空, 让Controller层
	 * 去处理.
	 */
	String authentication(String userName, String password);
}
