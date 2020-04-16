package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gmall.pms.entity.AttrEntity;
import com.atguigu.gmall.pms.entity.AttrGroupEntity;
import lombok.Data;

import java.util.List;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Data
public class GroupVo extends AttrGroupEntity {
  private List<AttrEntity> attrEntities;
  private List<AttrAttrgroupRelationEntity> relations;

}


