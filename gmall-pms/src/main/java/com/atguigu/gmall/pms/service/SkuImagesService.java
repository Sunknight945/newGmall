package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.entity.SkuImagesEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;

import java.util.List;


/**
 * sku图片
 *
 * @author uiys
 * @email uiys@Gmall.com
 * @date 2020-03-19 01:51:34
 */
public interface SkuImagesService extends IService<SkuImagesEntity> {

	PageVo queryPage(QueryCondition params);

	/**
	 * 通过skuId获取 对应的skuImageList
	 */
	List<SkuImagesEntity> queryImageListBySkuId(Long skuId);
}

