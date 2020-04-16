package com.atguigu.gmall.cart.pojo;

import com.atguigu.gmall.pms.entity.SkuSaleAttrValueEntity;
import com.atguigu.gmall.sms.vo.SaleVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Data
public class Cart {
	private Long skuId;
	private String title;
	private String defaultImage;
	private BigDecimal price;
	private BigDecimal currentPrice;
	private Integer count;
	private Boolean store;
	private List<SkuSaleAttrValueEntity> saleAttrValues;
	private List<SaleVo> saleVos;
	private Boolean check;
	private BigDecimal weight;
}


