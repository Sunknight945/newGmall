package com.atguigu.gmall.pms.service.impl;

import com.atguigu.gmall.pms.entity.SkuInfoEntity;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.atguigu.gmall.pms.dao.SkuInfoDao;
import com.atguigu.gmall.pms.service.SkuInfoService;

import java.util.List;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

  @Override
  public PageVo queryPage(QueryCondition params) {
    IPage<SkuInfoEntity> page = this.page(
      new Query<SkuInfoEntity>().getPage(params),
      new QueryWrapper<SkuInfoEntity>()
    );

    return new PageVo(page);
  }

  /**
   * 根据spuId 检索它对应的skuList列表
   *
   * @param spuId
   * @return
   */
  @Override
  public List<SkuInfoEntity> querySkuPageBySpuId(Long spuId) {
    QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("spu_id", spuId);
    return this.list(queryWrapper);
  }
}
