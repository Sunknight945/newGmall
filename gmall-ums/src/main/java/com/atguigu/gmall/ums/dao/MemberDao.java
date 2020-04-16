package com.atguigu.gmall.ums.dao;

import com.atguigu.gmall.ums.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 会员
 *
 * @author uiys
 * @date 2020-03-18 17:57:10
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {

	/**
	 * 购物之后更新积分与成长值
	 * @param memberId ff
	 * @param growth ff
	 * @param integration  ff
	 */
	void updateBoundsById(@Param("memberId") Long memberId, @Param("growth") Integer growth, @Param("integration") Integer integration);
}
