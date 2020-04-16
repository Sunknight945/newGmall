package com.atguigu.gmall.oms.vo;

import com.atguigu.gmall.ums.entity.MemberReceiveAddressEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Data
public class OrderSubmitVo {

	private Long userId;
	/**
	 * 订单防重
	 */
	private String orderToken;

	private MemberReceiveAddressEntity address;

	/**
	 * 支付方式
	 */
	private Integer payType;

	private String deliveryCompany;

	/**
	 * 商品列表
	 */
	private List<OrderItemVo> orderItemVoList;

	private Integer bounds;

	/**
	 * 校验总价格
	 */
	private BigDecimal totalPrice;

}


