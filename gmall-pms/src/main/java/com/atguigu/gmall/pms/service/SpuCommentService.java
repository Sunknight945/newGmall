package com.atguigu.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.pms.entity.SpuCommentEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * 商品评价
 *
 * @author uiys
 * @email uiys@Gmall.com
 * @date 2020-03-18 03:23:02
 */
public interface SpuCommentService extends IService<SpuCommentEntity> {

  PageVo queryPage(QueryCondition params);
}

