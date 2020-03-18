package com.atguigu.gmall.ums.dao;

import com.atguigu.gmall.ums.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author uiys
 * @email uiys@gmall.com
 * @date 2020-03-18 17:57:10
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
