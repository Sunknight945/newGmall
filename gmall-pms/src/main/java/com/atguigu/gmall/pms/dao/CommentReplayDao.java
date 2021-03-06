package com.atguigu.gmall.pms.dao;

import com.atguigu.gmall.pms.entity.CommentReplayEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品评价回复关系
 *
 * @author uiys
 * @email uiys@Gmall.com
 * @date 2020-03-19 01:51:34
 */
@Mapper
public interface CommentReplayDao extends BaseMapper<CommentReplayEntity> {

}
