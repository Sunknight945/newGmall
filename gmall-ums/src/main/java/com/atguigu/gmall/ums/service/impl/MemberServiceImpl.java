package com.atguigu.gmall.ums.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.core.exception.MemberException;
import com.atguigu.gmall.ums.dao.MemberDao;
import com.atguigu.gmall.ums.entity.MemberEntity;
import com.atguigu.gmall.ums.service.MemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static cn.hutool.core.collection.CollUtil.isNotEmpty;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

	@Autowired
	private MemberDao memberDao;

	@Override
	public PageVo queryPage(QueryCondition params) {
		IPage<MemberEntity> page = this.page(
			new Query<MemberEntity>().getPage(params),
			new QueryWrapper<MemberEntity>()
		);

		return new PageVo(page);
	}

	/**
	 * 实现用户数据的校验，主要包括对：手机号、用户名的唯一性校验。
	 * 要校验的数据类型：1，用户名；2，手机；3，邮箱
	 */
	@Override
	public Boolean checkIfExist(String data, Integer type) {
		String field = null;
		if (type == 1) {
			field = "username";
		} else if (type == 2) {
			field = "mobile";
		} else if (type == 3) {
			field = "email";
		}
		return this.getOne(new QueryWrapper<MemberEntity>().eq(field, data)) == null;
	}

	/**
	 * 实现用户注册功能，需要对用户密码进行加密存储，使用MD5加密，加密过程中使用随机码作为salt加盐。另外还需要对用户输入的短信验证码进行校验。
	 */
	@Override
	public int register(MemberEntity memberEntity, String code) {
		//校验手机验证码 Todo

		//生成盐

		//加盐加密

		//注册用户

		//删除redis的验证码

		if (memberEntity == null) {
			return 0;
		}
		if (4 <= memberEntity.getUsername().length() && 40 >= memberEntity.getUsername().length()) {
			memberEntity.setUsername(memberEntity.getUsername());
		}
		String slat = UUID.randomUUID().toString().substring(0, 6);
		memberEntity.setSalt(slat);
		memberEntity.setPassword(DigestUtil.md5Hex(memberEntity.getPassword() + slat));
		memberEntity.setUsername(memberEntity.getUsername());
		memberEntity.setGrowth(0);
		memberEntity.setIntegration(0);
		memberEntity.setLevelId(0L);
		memberEntity.setStatus(1);
		memberEntity.setCreateTime(new Date());
		//删除redis的验证码 todo
		return this.save(memberEntity) ? 1 : 0;
	}

	/**
	 * 查询功能，根据参数中的用户名和密码查询指定用户
	 */
	@Override
	public MemberEntity queryMember(String username, String password) {
		List<MemberEntity> memberEntityList = this.list(new QueryWrapper<MemberEntity>().eq("username", username));
		if (isNotEmpty(memberEntityList)) {
			MemberEntity memberEntity = memberEntityList.get(0);
			if (StrUtil.equals(DigestUtil.md5Hex(password + memberEntity.getSalt()), memberEntity.getPassword())) {
				return memberEntity;
			} else {
				throw new MemberException("用户名或密码错误!");
			}
		}
		throw new MemberException("用户名或密码错误!");
	}

}
