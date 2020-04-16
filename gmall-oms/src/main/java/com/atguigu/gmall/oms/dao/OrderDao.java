package com.atguigu.gmall.oms.dao;

import com.atguigu.gmall.oms.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 订单
 *
 * @author uiys
 * @date 2020-03-18 17:53:27
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {

	/**
	 * 关单 操作 更改orderEntity字段状态(逻辑删除)
	 *
	 * @param orderToken ff
	 * @return ff
	 */
	int closeOrder(@Param("orderToken") String orderToken);

	/**
	 *  支付 操作 . (逻辑)
	 * @param orderToken ff
	 * @return ff
	 */
	int payOrder(@Param("orderToken") String orderToken);
}
