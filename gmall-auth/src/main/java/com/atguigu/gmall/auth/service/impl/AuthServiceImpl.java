package com.atguigu.gmall.auth.service.impl;

import com.atguigu.core.bean.Resp;
import com.atguigu.core.utils.JwtUtils;
import com.atguigu.gmall.auth.config.JwtProperties;
import com.atguigu.gmall.auth.feign.GmallUmsFeign;
import com.atguigu.gmall.auth.service.AuthService;
import com.atguigu.gmall.ums.entity.MemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Service
public class AuthServiceImpl implements AuthService {


	@Autowired
	private GmallUmsFeign gmallUmsFeign;

	@Resource
	private JwtProperties jwtProperties;

	/**
	 * oss单点登录 意味  用户输入用户名和密码调用ums-模块的接口验证 如果 为真
	 * 则 把用户相应的数据写到cookie 和session 里面. 没有就 返回空, 让Controller层
	 * 去处理.
	 */
	@Override
	public String authentication(String userName, String password) {
		Resp<MemberEntity> memberEntityResp = this.gmallUmsFeign.queryMember(userName, password);
		MemberEntity memberEntity = memberEntityResp.getData();
		//没有返回空就可以了  交给Controller处理
		if (memberEntity == null) {
			return null;
		}
		//有的话就只做自己的token (jwt)
		try {
			Map<String, Object> claims = new HashMap<>(); //claims 要求
			claims.put("id", memberEntity.getId());
			claims.put("username", memberEntity.getUsername());
			return JwtUtils.generateToken(claims, this.jwtProperties.getPrivateKey(), jwtProperties.getExpire() * 60);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}


