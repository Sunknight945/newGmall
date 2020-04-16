package com.atguigu.gmall.item.vo;

import com.atguigu.gmall.pms.entity.BrandEntity;
import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.atguigu.gmall.pms.entity.SkuImagesEntity;
import com.atguigu.gmall.pms.entity.SkuSaleAttrValueEntity;
import com.atguigu.gmall.pms.vo.ItemGroupVo;
import com.atguigu.gmall.sms.vo.SaleVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 * 组成 item的必要信息
 */
@Data
public class ItemVo {
	private Long skuId;
	private CategoryEntity categoryEntity;
	private BrandEntity brandEntity;
	private Long spuId;
	private String spuName;
	private String skuTitle;
	private String subTitle;
	private BigDecimal price;
	private BigDecimal weight;
	//sku图片列表
	private List<SkuImagesEntity> pics;
	//营销信息
	private List<SaleVo> sales;
	//是否有货
	private Boolean store;
	// 销售属性
	private List<SkuSaleAttrValueEntity> saleAttrs;
	//spu的海报
	private List<String> images;
  // 规格参数组 pms_attr_group 及组下的规格参数 pms_product_attr_value （带值）
	private List<ItemGroupVo> groups;
}


