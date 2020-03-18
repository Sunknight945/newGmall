package com.atguigu.gmall.oms.dao;

import com.atguigu.gmall.oms.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author uiys
 * @email uiys@gmall.com
 * @date 2020-03-18 17:53:27
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
