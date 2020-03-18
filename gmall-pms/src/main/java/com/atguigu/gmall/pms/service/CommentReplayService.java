package com.atguigu.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.pms.entity.CommentReplayEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * 商品评价回复关系
 *
 * @author uiys
 * @email uiys@Gmall.com
 * @date 2020-03-18 03:23:02
 */
public interface CommentReplayService extends IService<CommentReplayEntity> {

  PageVo queryPage(QueryCondition params);
}

