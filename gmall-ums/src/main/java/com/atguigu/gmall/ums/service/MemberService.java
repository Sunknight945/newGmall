package com.atguigu.gmall.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.ums.entity.MemberEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * 会员
 *
 * @author uiys
 * @email uiys@gmall.com
 * @date 2020-03-18 17:57:10
 */
public interface MemberService extends IService<MemberEntity> {

	PageVo queryPage(QueryCondition params);

	/**
	 * 实现用户数据的校验，主要包括对：手机号、用户名的唯一性校验。
	 */
	Boolean checkIfExist(String data, Integer type);

	/**
	 * 实现用户注册功能，需要对用户密码进行加密存储，使用MD5加密，加密过程中使用随机码作为salt加盐。另外还需要对用户输入的短信验证码进行校验。
	 */
	int register(MemberEntity memberEntity, String code);

	/**
	 * 查询功能，根据参数中的用户名和密码查询指定用户
	 */
	MemberEntity queryMember(String username, String password);
}

