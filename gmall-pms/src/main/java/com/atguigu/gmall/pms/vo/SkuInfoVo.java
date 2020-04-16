package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.SkuInfoEntity;
import com.atguigu.gmall.pms.entity.SkuSaleAttrValueEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Data
public class SkuInfoVo extends SkuInfoEntity {

  //积分营销相关字段 SkuBoundsEntity
  private BigDecimal growBounds;
  private BigDecimal buyBounds;
  private List<Integer> work;

  //打折相关字段 SkuLadderEntity
  private Integer fullCount;
  private BigDecimal discount;
  private Integer ladderAddOther;

  //满减的相关字段 SkuFullReductionEntity
  private BigDecimal fullPrice;
  private BigDecimal reducePrice;
  private Integer fullAddOther;

  //销售属性和他的值 SkuSaleAttrValueEntity
  private List<SkuSaleAttrValueEntity> saleAttrs;

  //图片的列表, 在sku里面需要设置的.
  //sku的图片
  private List<String> images;
}


