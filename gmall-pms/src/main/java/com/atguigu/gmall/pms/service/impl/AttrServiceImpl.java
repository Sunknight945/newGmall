package com.atguigu.gmall.pms.service.impl;

import com.atguigu.gmall.pms.dao.AttrAttrgroupRelationDao;
import com.atguigu.gmall.pms.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gmall.pms.entity.AttrEntity;
import com.atguigu.gmall.pms.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.atguigu.gmall.pms.dao.AttrDao;
import com.atguigu.gmall.pms.service.AttrService;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
  @Autowired
  private AttrAttrgroupRelationDao attrAttrgroupRelationDao;

  @Override
  public PageVo queryPage(QueryCondition params) {
    IPage<AttrEntity> page = this.page(
      new Query<AttrEntity>().getPage(params),
      new QueryWrapper<AttrEntity>()
    );

    return new PageVo(page);
  }

  @Override
  public PageVo queryAttrsByCid(QueryCondition condition, Long cid, Integer type) {
    QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
    if (cid != null) {
      queryWrapper.eq("catelog_id", cid);
    }
    if (type != null) {
      queryWrapper.eq("attr_type", type);
    }
    IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(condition), queryWrapper);
    return new PageVo(page);
  }

  /**
   * 保存 attrVo 多表操作
   *
   * @param attrVo
   */
  @Override
  public void saveAttr(AttrVo attrVo) {
    // attr  与 relation  关系表(中间表)
    this.save(attrVo);
    Long attrId = attrVo.getAttrId();
    AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
    attrAttrgroupRelationEntity.setAttrId(attrId);
    attrAttrgroupRelationEntity.setAttrGroupId(attrVo.getAttrGroupId());
    this.attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
  }
}
