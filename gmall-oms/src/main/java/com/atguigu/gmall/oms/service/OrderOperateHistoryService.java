package com.atguigu.gmall.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.oms.entity.OrderOperateHistoryEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * 订单操作历史记录
 *
 * @author uiys
 * @email uiys@gmall.com
 * @date 2020-03-18 17:53:27
 */
public interface OrderOperateHistoryService extends IService<OrderOperateHistoryEntity> {

    PageVo queryPage(QueryCondition params);
}

