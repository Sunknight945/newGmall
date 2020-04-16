package com.atguigu.gmall.pms.service.impl;

import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.gmall.pms.dao.ProductAttrValueDao;
import com.atguigu.gmall.pms.entity.ProductAttrValueEntity;
import com.atguigu.gmall.pms.service.ProductAttrValueService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Autowired
    private ProductAttrValueDao productAttrValueDao;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageVo(page);
    }


    /**
     * 根据spuId 查询该商品对应的搜索属性及值( pms_attr pms_product_attr_value)
     */
    @Override
    public List<ProductAttrValueEntity> querySearchAttrValueListBySpuId(Long spuId) {

        return this.productAttrValueDao.querySearchAttrValueListBySpuId(spuId);
    }

    /**
     * 根据 spuId 和 cateId 查询组及组下规格参数（带值）
     */
    @Override
    public List<ProductAttrValueEntity> queryProductAttrValueListBy(Long spuId, Long cateId) {


        return null;

    }
}
