package com.atguigu.gmall.pms.service.impl;

import com.atguigu.gmall.pms.entity.SkuImagesEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.atguigu.gmall.pms.dao.SkuImagesDao;
import com.atguigu.gmall.pms.service.SkuImagesService;

import java.util.List;


@Service("skuImagesService")
public class SkuImagesServiceImpl extends ServiceImpl<SkuImagesDao, SkuImagesEntity> implements SkuImagesService {
	@Autowired
	private SkuImagesDao skuImagesDao;

	@Override
	public PageVo queryPage(QueryCondition params) {
		IPage<SkuImagesEntity> page = this.page(
			new Query<SkuImagesEntity>().getPage(params),
			new QueryWrapper<SkuImagesEntity>()
		);

		return new PageVo(page);
	}

	/**
	 * 通过skuId获取 对应的skuImageList
	 *
	 * @param skuId
	 */
	@Override
	public List<SkuImagesEntity> queryImageListBySkuId(Long skuId) {
		QueryWrapper<SkuImagesEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("sku_id", skuId);
		return this.skuImagesDao.selectList(queryWrapper);
	}
}
