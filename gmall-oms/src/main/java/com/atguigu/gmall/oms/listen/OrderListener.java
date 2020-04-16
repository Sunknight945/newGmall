package com.atguigu.gmall.oms.listen;

import com.atguigu.gmall.oms.dao.OrderDao;
import com.atguigu.gmall.oms.entity.OrderEntity;
import com.atguigu.gmall.ums.ov.UserBoundsVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Component
public class OrderListener {

	private final OrderDao orderDao;
	private final AmqpTemplate amqpTemplate;

	public OrderListener(OrderDao orderDao, AmqpTemplate amqpTemplate) {
		this.orderDao = orderDao;
		this.amqpTemplate = amqpTemplate;
	}

	@RabbitListener(queues = {"ORDER-DEAD-QUEUE"})
	public void closeOrder(String orderToken) {
		//如果执行了关闭订单的操作
		if (this.orderDao.closeOrder(orderToken) == 1) {
			//解锁库存  发消息wms, 解锁对应的库存
			this.amqpTemplate.convertAndSend("gmall-order-exchange", "stock.unlock", orderToken);
		}
	}

	//		this.amqpTemplate.convertAndSend("GMALL-ORDER-EXCHANGE","order.pay",payAsyncVo.getOut_trade_no());
	@RabbitListener(bindings = @QueueBinding(
		value = @Queue(value = "GMALL-ORDER-PAY", durable = "true"),
		exchange = @Exchange(value = "GMALL-ORDER-EXCHANGE", type = ExchangeTypes.TOPIC, ignoreDeclarationExceptions = "true"),
		key = {"order.pay"}
	))
	public void payOrder(String orderToken) {
		//减库存
		this.amqpTemplate.convertAndSend("GMALL-ORDER-EXCHANGE", "stock.minus", orderToken);
		//加积分
		OrderEntity orderEntity = this.orderDao.selectOne(new QueryWrapper<OrderEntity>().eq("order_sn", orderToken));
		UserBoundsVo boundsVo = new UserBoundsVo();
		boundsVo.setMemberId(orderEntity.getMemberId());
		boundsVo.setGrowth(orderEntity.getGrowth());
		boundsVo.setIntegration(orderEntity.getIntegration());
		this.amqpTemplate.convertAndSend("GMALL-ORDER-EXCHANGE", "user.bounds", boundsVo);
	}

}


