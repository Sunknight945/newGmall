package com.atguigu.gmall.ums.controller;

import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.ums.entity.MemberEntity;
import com.atguigu.gmall.ums.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


/**
 * 会员
 *
 * @author uiys
 * @email uiys@gmall.com
 * @date 2020-03-18 17:57:10
 */
@Api(tags = "会员 管理")
@RestController
@RequestMapping("ums/member")
public class MemberController {
	@Autowired
	private MemberService memberService;

	/**
	 * 查询功能，根据参数中的用户名和密码查询指定用户
	 */
	@GetMapping("query")
	public Resp<MemberEntity> queryMember(@RequestParam("username") String username,
	                                      @RequestParam("password") String password) {
		MemberEntity memberEntity = this.memberService.queryMember(username, password);
		if (memberEntity != null) {
			return Resp.ok(memberEntity);
		}
		return null;
	}

	/**
	 * 实现用户注册功能，需要对用户密码进行加密存储，使用MD5加密，加密过程中使用随机码作为salt加盐。另外还需要对用户输入的短信验证码进行校验。
	 */
	@PostMapping("register")
	public Resp<Object> register(MemberEntity memberEntity, @RequestParam("code") String code) {
		int i = this.memberService.register(memberEntity, code);
		return Resp.ok(i == 1);
	}


	/**
	 * 实现用户数据的校验，主要包括对：手机号、用户名的唯一性校验。
	 */
	@GetMapping("check/{data}/{type}")
	public Resp<Boolean> checkIfExist(@PathVariable("data") String data,
	                                  @PathVariable("type") Integer type) {
		Boolean flag = this.memberService.checkIfExist(data, type);
		return Resp.ok(flag);

	}


	/**
	 * 列表
	 */
	@ApiOperation("分页查询(排序)")
	@GetMapping("/list")
	@PreAuthorize("hasAuthority('ums:member:list')")
	public Resp<PageVo> list(QueryCondition queryCondition) {
		PageVo page = memberService.queryPage(queryCondition);

		return Resp.ok(page);
	}


	/**
	 * 信息
	 */
	@ApiOperation("详情查询")
	@GetMapping("/info/{id}")
	@PreAuthorize("hasAuthority('ums:member:info')")
	public Resp<MemberEntity> info(@PathVariable("id") Long id) {
		MemberEntity member = memberService.getById(id);

		return Resp.ok(member);
	}

	/**
	 * 保存
	 */
	@ApiOperation("保存")
	@PostMapping("/save")
	@PreAuthorize("hasAuthority('ums:member:save')")
	public Resp<Object> save(@RequestBody MemberEntity member) {
		memberService.save(member);

		return Resp.ok(null);
	}

	/**
	 * 修改
	 */
	@ApiOperation("修改")
	@PostMapping("/update")
	@PreAuthorize("hasAuthority('ums:member:update')")
	public Resp<Object> update(@RequestBody MemberEntity member) {
		memberService.updateById(member);

		return Resp.ok(null);
	}

	/**
	 * 删除
	 */
	@ApiOperation("删除")
	@PostMapping("/delete")
	@PreAuthorize("hasAuthority('ums:member:delete')")
	public Resp<Object> delete(@RequestBody Long[] ids) {
		memberService.removeByIds(Arrays.asList(ids));

		return Resp.ok(null);
	}

}
