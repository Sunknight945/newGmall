package com.atguigu.gmall.oms.vo;

import com.atguigu.gmall.pms.entity.SkuSaleAttrValueEntity;
import com.atguigu.gmall.sms.vo.SaleVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Data
public class OrderItemVo {

	private Long skuId;
	private String title;
	private String defaultImage;
	/**
	 * 数据库的实时价格
	 */
	private BigDecimal price;
	private Integer count;
	/**
	 * 是否有货
	 */
	private Boolean store;
	private List<SkuSaleAttrValueEntity> saleAttrValues;
	/**
	 * 营销信息是需要的
	 */
	private List<SaleVo> saleVos;
	/**
	 * 重量
	 */
	private BigDecimal weight;




}


