package com.atguigu.gmall.oms.dao;

import com.atguigu.gmall.oms.entity.OrderSettingEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单配置信息
 * 
 * @author uiys
 * @email uiys@gmall.com
 * @date 2020-03-18 17:53:27
 */
@Mapper
public interface OrderSettingDao extends BaseMapper<OrderSettingEntity> {
	
}
