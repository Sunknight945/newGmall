package com.atguigu.gmall.pms.service.impl;

import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.gmall.pms.dao.AttrAttrgroupRelationDao;
import com.atguigu.gmall.pms.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gmall.pms.service.AttrAttrgroupRelationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

  @Override
  public PageVo queryPage(QueryCondition params) {
    IPage<AttrAttrgroupRelationEntity> page = this.page(
      new Query<AttrAttrgroupRelationEntity>().getPage(params),
      new QueryWrapper<AttrAttrgroupRelationEntity>()
    );

    return new PageVo(page);
  }


  /**
   * 根据 attrId  与 attrGroupId  删除中间表relation的关联关系
   *
   * @param relationEntityList
   */
  @Override
  public void deleteRelation(List<AttrAttrgroupRelationEntity> relationEntityList) {
    relationEntityList.forEach(relationEntity -> {
      QueryWrapper<AttrAttrgroupRelationEntity> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq("attr_id", relationEntity.getAttrId());
      queryWrapper.eq("attr_group_id",relationEntity.getAttrGroupId());
      this.remove(queryWrapper);
    });
  }
}
