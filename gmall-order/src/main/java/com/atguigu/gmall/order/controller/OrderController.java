package com.atguigu.gmall.order.controller;


import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.oms.entity.OrderEntity;
import com.atguigu.gmall.oms.vo.OrderSubmitVo;
import com.atguigu.gmall.order.pay.AlipayTemplate;
import com.atguigu.gmall.order.pay.PayAsyncVo;
import com.atguigu.gmall.order.pay.PayVo;
import com.atguigu.gmall.order.service.OrderService;
import com.atguigu.gmall.order.vo.OrderConfirmVo;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@RestController
@RequestMapping("order")
public class OrderController {

	private final OrderService orderService;


	private final AmqpTemplate amqpTemplate;

	private final AlipayTemplate alipayTemplate;

	public OrderController(OrderService orderService, AmqpTemplate amqpTemplate, AlipayTemplate alipayTemplate) {
		this.orderService = orderService;
		this.amqpTemplate = amqpTemplate;
		this.alipayTemplate = alipayTemplate;
	}

	/**
	 * 获取确认订单
	 */
	@GetMapping("confirm")
	public Resp<OrderConfirmVo> getOrderConfirmVo() {
		OrderConfirmVo orderConfirmVo = this.orderService.getOrderConfirmVo();
		return Resp.ok(orderConfirmVo);
	}

	/**
	 * 提交订单主备支付
	 */
	@PostMapping("submit")
	public Resp<OrderSubmitVo> postOrderSubmitVo(@RequestBody OrderSubmitVo submitVo) {
		OrderEntity orderEntity = this.orderService.postOrderSubmitVo(submitVo);
		try {
			PayVo payVo = new PayVo();
			payVo.setOut_trade_no(orderEntity.getOrderSn());
			payVo.setTotal_amount(orderEntity.getTotalAmount().toString());
			payVo.setSubject("谷粒商城");
			payVo.setSubject("支付平台");
			// 这一步正式调用支付宝内部的支付接口完成支付
			String pay = this.alipayTemplate.pay(payVo);
			System.out.println(pay);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Resp.ok(null);
	}

	/**
	 * 支付成功之后回调的接口
	 */

	@PostMapping("pay/sucess")
	public Resp<Object> paySuccess(PayAsyncVo payAsyncVo) {
		//订单号 payAsyncVo.getOut_trade_no()
		this.amqpTemplate.convertAndSend("GMALL-ORDER-EXCHANGE", "order.pay", payAsyncVo.getOut_trade_no());
		return Resp.ok(null);
	}


}


