package com.atguigu.gmall.order.service;

import com.atguigu.gmall.oms.entity.OrderEntity;
import com.atguigu.gmall.order.vo.OrderConfirmVo;
import com.atguigu.gmall.oms.vo.OrderSubmitVo;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
public interface OrderService {
	/**
	 * 获取订单
	 */
	OrderConfirmVo getOrderConfirmVo();

	/**
	 * 提交订单
	 * @param submitVo 订单信息
	 * @return
	 */
	OrderEntity postOrderSubmitVo(OrderSubmitVo submitVo);
}
