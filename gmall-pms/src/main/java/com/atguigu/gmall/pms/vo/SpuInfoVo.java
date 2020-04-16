package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.SpuInfoEntity;
import lombok.Data;

import java.util.List;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Data
public class SpuInfoVo extends SpuInfoEntity {

  private List<String> spuImages;
  private List<BaseAttrVo> baseAttrs;
  private List<SkuInfoVo> skus;

}


