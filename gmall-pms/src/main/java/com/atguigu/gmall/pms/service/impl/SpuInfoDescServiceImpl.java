package com.atguigu.gmall.pms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.gmall.pms.dao.SpuInfoDescDao;
import com.atguigu.gmall.pms.entity.SpuInfoDescEntity;
import com.atguigu.gmall.pms.service.SpuInfoDescService;
import com.atguigu.gmall.pms.vo.SpuInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service("spuInfoDescService")
public class SpuInfoDescServiceImpl extends ServiceImpl<SpuInfoDescDao, SpuInfoDescEntity> implements SpuInfoDescService {

  @Override
  public PageVo queryPage(QueryCondition params) {
    IPage<SpuInfoDescEntity> page = this.page(
      new Query<SpuInfoDescEntity>().getPage(params),
      new QueryWrapper<SpuInfoDescEntity>()
    );

    return new PageVo(page);
  }

  /**
   * 1.2.保存spm_spu_info_desc 描述信息
   *
   * @param spuInfoVo
   * @param spuId
   */
  @Override
  @Transactional
  public void saveSpuInfoDesc(SpuInfoVo spuInfoVo, Long spuId) {
    SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
    spuInfoDescEntity.setSpuId(spuId);
    List<String> spuImages = spuInfoVo.getSpuImages();
    if (CollUtil.isNotEmpty(spuImages)) {
      String join = StrUtil.join(",", spuImages);
      spuInfoDescEntity.setDecript(join);
    }
    this.save(spuInfoDescEntity);
  }
}
