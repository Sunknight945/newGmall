package com.atguigu.gmall.sms.service;

import com.atguigu.gmall.sms.vo.SaleVo;
import com.atguigu.gmall.sms.vo.SkuSaleVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.sms.entity.SkuBoundsEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;

import java.util.List;


/**
 * 商品sku积分设置
 *
 * @author uiys
 * @email uiys@gmall.com
 * @date 2020-03-18 17:48:12
 */
public interface SkuBoundsService extends IService<SkuBoundsEntity> {

    PageVo queryPage(QueryCondition params);

  /**
   * 利用 feign 实现接口远程调用, 然后 这是被实现的接口, 在 pms 模块中.
   * @param skuSaleVo
   */
  void saveSale(SkuSaleVo skuSaleVo);

  /**
   * 查询组itemVo 所需要的音效信息 (两个字段) type 与 bounds )
   */
  List<SaleVo> querySaleVoBySkuId(Long skuId);
}

