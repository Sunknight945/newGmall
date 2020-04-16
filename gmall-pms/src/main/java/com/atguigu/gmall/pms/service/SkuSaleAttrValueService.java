package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.entity.SkuSaleAttrValueEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;

import java.util.List;


/**
 * sku销售属性&值
 *
 * @author uiys
 * @email uiys@Gmall.com
 * @date 2020-03-19 01:51:34
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

	PageVo queryPage(QueryCondition params);


	/**
	 * 根据skuId 查询他的销售属性和值
	 */
	List<SkuSaleAttrValueEntity> querySaleAttrValueBySkuId(Long skuId);
}

