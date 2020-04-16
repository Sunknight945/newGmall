package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.entity.SpuInfoEntity;
import com.atguigu.gmall.pms.vo.SpuInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * spu信息
 *
 * @author uiys
 * @email uiys@Gmall.com
 * @date 2020-03-19 01:51:33
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

  PageVo queryPage(QueryCondition params);

  /**
   * 根据条件 和 catId 查询 spu分页
   *
   * @param condition
   * @param catId
   * @return
   */
  PageVo querySpuPage(QueryCondition condition, Long catId);

  /**
   * 有九张表需要实现保存.
   *
   * @param spuInfoVo
   */
  void bigSave(SpuInfoVo spuInfoVo);


}

