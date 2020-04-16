package com.atguigu.gmall.oms.service;

import com.atguigu.gmall.oms.vo.OrderSubmitVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.oms.entity.OrderEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * 订单
 *
 * @author uiys
 * @email uiys@gmall.com
 * @date 2020-03-18 17:53:27
 */
public interface OrderService extends IService<OrderEntity> {

	PageVo queryPage(QueryCondition params);

	/**
	 * 保存订单信息
	 *
	 * @param orderSubmitVo 提交的订单信息
	 * @return 创建好的保存的订单的表一条数据数据
	 */
	OrderEntity saveOrder(OrderSubmitVo orderSubmitVo);
}

