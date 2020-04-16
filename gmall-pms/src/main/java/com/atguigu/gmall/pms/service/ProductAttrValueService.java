package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.entity.ProductAttrValueEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;

import java.util.List;


/**
 * spu属性值
 *
 * @author uiys
 * @email uiys@Gmall.com
 * @date 2020-03-19 01:51:34
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

	PageVo queryPage(QueryCondition params);

	/**
	 * 根据spuId 查询该商品对应的搜索属性及值( pms_attr pms_product_attr_value)
	 *
	 * @param spuId
	 * @return
	 */
	List<ProductAttrValueEntity> querySearchAttrValueListBySpuId(Long spuId);

	/**
	 * 根据 spuId 和 cateId 查询组及组下规格参数（带值）
	 */
	List<ProductAttrValueEntity> queryProductAttrValueListBy(Long spuId, Long cateId);
}

