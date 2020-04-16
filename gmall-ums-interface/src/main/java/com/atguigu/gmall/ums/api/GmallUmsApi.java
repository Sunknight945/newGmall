package com.atguigu.gmall.ums.api;

import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.ums.entity.MemberEntity;
import com.atguigu.gmall.ums.entity.MemberReceiveAddressEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */

public interface GmallUmsApi {

	/**
	 * 查询功能，根据参数中的用户名和密码查询指定用户
	 */
	@GetMapping("/ums/member/query")
	public Resp<MemberEntity> queryMember(@RequestParam("username") String username,
	                                      @RequestParam("password") String password);

	/**
	 * 实现用户注册功能，需要对用户密码进行加密存储，使用MD5加密，加密过程中使用随机码作为salt加盐。另外还需要对用户输入的短信验证码进行校验。
	 */
	@PostMapping("ums/member/register")
	public Resp<Object> register(MemberEntity memberEntity, @RequestParam("code") String code);

	/**
	 * 实现用户数据的校验，主要包括对：手机号、用户名的唯一性校验。
	 */
	@GetMapping("ums/member/check/{data}/{type}")
	public Resp<Boolean> checkIfExist(@PathVariable("data") String data,
	                                  @PathVariable("type") Integer type);

	@GetMapping("ums/memberreceiveaddress/memberId/{memberId}")
	public Resp<List<MemberReceiveAddressEntity>> queryAddressesByUserId(@PathVariable("memberId") Long memberId);


	/**
	 * 根据 member的id获取会员的信息
	 * @param id
	 * @return
	 */
	@GetMapping("ums/member/info/{id}")
	public Resp<MemberEntity> queryMemberById(@PathVariable("id") Long id);
}
