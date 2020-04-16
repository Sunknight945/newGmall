package com.atguigu.gmall.order.vo;

// import com.atguigu.gmall.ums.entity.MemberReceiveAddressEntity;

import com.atguigu.gmall.oms.vo.OrderItemVo;
import com.atguigu.gmall.ums.entity.MemberReceiveAddressEntity;
import lombok.Data;

import java.util.List;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 * 订单vo
 */
@Data
public class OrderConfirmVo {


	private List<MemberReceiveAddressEntity> memberReceiveAddressEntities;

	/**
	 * 两面就是单个购物车Cart数据的列表
	 */
	private List<OrderItemVo> orderItemVos;

	/**
	 * 积分
	 */
	private Integer bounds;

	/**
	 * 在获取确认订单时为每一个订单生成的唯一token
	 */
	private String orderToken;

}


