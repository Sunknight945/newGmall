package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;

import java.util.List;


/**
 * 属性&属性分组关联
 *
 * @author uiys
 * @email uiys@Gmall.com
 * @date 2020-03-19 01:51:33
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageVo queryPage(QueryCondition params);

  /**
   * 根据 attrId  与 attrGroupId  删除中间表relation的关联关系
   * @param relationEntity
   */
  void deleteRelation(List<AttrAttrgroupRelationEntity> relationEntityList);
}

