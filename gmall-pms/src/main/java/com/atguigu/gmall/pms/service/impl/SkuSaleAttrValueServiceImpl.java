package com.atguigu.gmall.pms.service.impl;

import com.atguigu.gmall.pms.entity.SkuSaleAttrValueEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.atguigu.gmall.pms.dao.SkuSaleAttrValueDao;
import com.atguigu.gmall.pms.service.SkuSaleAttrValueService;

import java.util.List;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {


	@Autowired
	private SkuSaleAttrValueDao skuSaleAttrValueDao;

	@Override
	public PageVo queryPage(QueryCondition params) {
		IPage<SkuSaleAttrValueEntity> page = this.page(
			new Query<SkuSaleAttrValueEntity>().getPage(params),
			new QueryWrapper<SkuSaleAttrValueEntity>()
		);

		return new PageVo(page);
	}

	/**
	 * 根据skuId 查询他的销售属性和值
	 */
	@Override
	public List<SkuSaleAttrValueEntity> querySaleAttrValueBySkuId(Long skuId) {
		QueryWrapper<SkuSaleAttrValueEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("sku_id", skuId);
		return this.skuSaleAttrValueDao.selectList(queryWrapper);
	}
}
