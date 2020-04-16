package com.atguigu.gmall.sms.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Data
public class SkuSaleVo {

	private Long skuId;

	/**
	 * 积分营销相关字段 SkuBoundsEntity
	 */
	private BigDecimal growBounds;
	private BigDecimal buyBounds;
	private List<Integer> work;

	/**
	 * 打折相关字段 SkuLadderEntity
	 */
	private Integer fullCount;
	private BigDecimal discount;
	private Integer ladderAddOther;

	/**
	 * 满减的相关字段 SkuFullReductionEntity
	 */
	private BigDecimal fullPrice;
	private BigDecimal reducePrice;
	private Integer fullAddOther;
}


