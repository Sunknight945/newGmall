package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.entity.AttrEntity;
import com.atguigu.gmall.pms.vo.AttrVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * 商品属性
 *
 * @author uiys
 * @email uiys@Gmall.com
 * @date 2020-03-19 01:51:33
 */
public interface AttrService extends IService<AttrEntity> {

  PageVo queryPage(QueryCondition params);

  /**
   * 根据cid获取attrs的分页
   * @param condition
   * @param cid
   * @param type
   * @return
   */
  PageVo queryAttrsByCid(QueryCondition condition, Long cid, Integer type);

  /**
   * 保存 attrVo 多表操作
   * @param attrVo
   */
  void saveAttr(AttrVo attrVo);
}

