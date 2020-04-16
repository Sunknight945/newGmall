package com.atguigu.gmall.pms.dao;

import com.atguigu.gmall.pms.entity.ProductAttrValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * spu属性值
 *
 * @author uiys
 * @email uiys@Gmall.com
 * @date 2020-03-19 01:51:34
 */
@Mapper
public interface ProductAttrValueDao extends BaseMapper<ProductAttrValueEntity> {

  /**
   * 根据spuId 查询该商品对应的搜索属性及值( pms_attr pms_product_attr_value)
   *
   * @param spuId
   * @return
   */
  List<ProductAttrValueEntity> querySearchAttrValueListBySpuId(@Param("spuId") Long spuId);
}
