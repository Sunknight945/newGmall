package com.atguigu.gmall.wms.service;

import com.atguigu.gmall.wms.vo.SkuLockVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.wms.entity.WareSkuEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;

import java.util.List;


/**
 * 商品库存
 *
 * @author uiys
 * @date 2020-03-18 18:05:22
 */
public interface WareSkuService extends IService<WareSkuEntity> {

	PageVo queryPage(QueryCondition params);

	/**
	 * 锁sku的库存
	 */
	String lockSkuWare(List<SkuLockVo> skuLockVos);
}

