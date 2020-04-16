package com.atguigu.gmall.oms.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.oms.dao.OrderDao;
import com.atguigu.gmall.oms.dao.OrderItemDao;
import com.atguigu.gmall.oms.entity.OrderEntity;
import com.atguigu.gmall.oms.entity.OrderItemEntity;
import com.atguigu.gmall.oms.feign.GmallPmsClient;
import com.atguigu.gmall.oms.feign.GmallUmsClient;
import com.atguigu.gmall.oms.service.OrderService;
import com.atguigu.gmall.oms.vo.OrderItemVo;
import com.atguigu.gmall.oms.vo.OrderSubmitVo;
import com.atguigu.gmall.pms.entity.BrandEntity;
import com.atguigu.gmall.pms.entity.SkuInfoEntity;
import com.atguigu.gmall.pms.entity.SpuInfoEntity;
import com.atguigu.gmall.ums.entity.MemberEntity;
import com.atguigu.gmall.ums.entity.MemberReceiveAddressEntity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * @author ovo
 */
@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {


	public OrderServiceImpl(GmallUmsClient umsClient, GmallPmsClient pmsClient, OrderItemDao orderItemDao, RabbitTemplate rabbitTemplate) {
		this.umsClient = umsClient;
		this.pmsClient = pmsClient;
		this.orderItemDao = orderItemDao;
		this.rabbitTemplate = rabbitTemplate;
	}

	private final GmallUmsClient umsClient;

	private final GmallPmsClient pmsClient;

	private final OrderItemDao orderItemDao;

	private final RabbitTemplate rabbitTemplate;


	@Override
	public PageVo queryPage(QueryCondition params) {
		IPage<OrderEntity> page = this.page(
			new Query<OrderEntity>().getPage(params),
			new QueryWrapper<>()
		);

		return new PageVo(page);
	}

	/**
	 * 保存订单信息
	 *
	 * @param orderSubmitVo 提交的订单信息
	 * @return 创建好的保存的订单的表一条数据数据
	 */
	@Override
	public OrderEntity saveOrder(OrderSubmitVo orderSubmitVo) {

		//订单总览 (不含订单商品单项)
		OrderEntity orderEntity = new OrderEntity();
		// orderEntity.setId(0L);
		MemberReceiveAddressEntity addressEntity = orderSubmitVo.getAddress();
		orderEntity.setMemberId(addressEntity.getMemberId());
		//收货人姓名
		orderEntity.setReceiverName(addressEntity.getName());
		//收货人电话
		orderEntity.setReceiverPhone(addressEntity.getPhone());
		//收货人邮编
		orderEntity.setReceiverPostCode(addressEntity.getPostCode());
		//省份/直辖市
		orderEntity.setReceiverProvince(addressEntity.getProvince());
		//城市
		orderEntity.setReceiverCity(addressEntity.getCity());
		//区
		orderEntity.setReceiverRegion(addressEntity.getRegion());
		//详细地址
		orderEntity.setReceiverDetailAddress(addressEntity.getDetailAddress());
		//订单号
		orderEntity.setOrderSn(orderSubmitVo.getOrderToken());
		//create_time
		orderEntity.setCreateTime(new Date());
		// 需要member
		Resp<MemberEntity> memberEntityResp = this.umsClient.queryMemberById(addressEntity.getMemberId());
		if (memberEntityResp.getData() != null) {
			orderEntity.setMemberUsername(memberEntityResp.getData().getUsername());
		}
		BigDecimal totalPrice = orderSubmitVo.getTotalPrice();
		//订单总额
		orderEntity.setTotalAmount(totalPrice);
		//应付总额
		orderEntity.setPayAmount(new BigDecimal("0"));
		//计算邮费
		orderEntity.setFreightAmount(new BigDecimal("0"));
		//促销金额
		orderEntity.setPromotionAmount(new BigDecimal("0"));
		//积分金额
		orderEntity.setIntegrationAmount(new BigDecimal("0"));
		//优惠券金额
		orderEntity.setCouponAmount(new BigDecimal("0"));
		//折扣金额
		orderEntity.setDiscountAmount(new BigDecimal("0"));
		orderEntity.setPayType(orderSubmitVo.getPayType());
		//订单来源[0->PC订单；1->app订单]
		orderEntity.setSourceType(1);
		//订单状态【0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单】
		orderEntity.setStatus(0);
		//物流公司(配送方式)
		orderEntity.setDeliveryCompany(orderSubmitVo.getDeliveryCompany());
		//物流单号
		orderEntity.setDeliverySn("");
		//自动确认时间（天)
		orderEntity.setAutoConfirmDay(15);
		//可以获得的积分
		orderEntity.setIntegration(0);
		// 可以获得的成长值
		orderEntity.setGrowth(0);
		//发票类型[0->不开发票；1->电子发票；2->纸质发票]
		orderEntity.setBillType(0);

		//确认收货状态[0->未确认；1->已确认]
		orderEntity.setConfirmStatus(0);
		//删除状态【0->未删除；1->已删除】
		orderEntity.setDeleteStatus(0);
		//支付时间
		orderEntity.setPaymentTime(new Date());
		//修改时间
		orderEntity.setModifyTime(orderEntity.getCreateTime());

		this.save(orderEntity);
		Long orderEntityId = orderEntity.getId();
		//订单商品单项
		List<OrderItemVo> orderItemVoList = orderSubmitVo.getOrderItemVoList();
		orderItemVoList.forEach(orderItemVo -> {
			OrderItemEntity orderItemEntity = new OrderItemEntity();
			orderItemEntity.setOrderId(orderEntityId);
			orderItemEntity.setOrderSn(orderSubmitVo.getOrderToken());
			Resp<SkuInfoEntity> skuInfoEntityResp = this.pmsClient.querySkuInfoBySkuId(orderItemVo.getSkuId());
			SkuInfoEntity skuInfoEntity = skuInfoEntityResp.getData();
			Long spuId = skuInfoEntity.getSpuId();
			orderItemEntity.setSpuId(spuId);
			Resp<SpuInfoEntity> spuInfoEntityResp = this.pmsClient.querySpuBySpuId(spuId);
			SpuInfoEntity spuInfoEntity = spuInfoEntityResp.getData();
			String spuName = spuInfoEntity.getSpuName();
			orderItemEntity.setSpuName(spuName);
			// todo: delete or correct this field
			//这个字段是错误的. spu 没有price这个字段
			// orderItemEntity.setSpuPic("0");
			Resp<BrandEntity> brandEntityResp = this.pmsClient.queryBrandById(skuInfoEntity.getBrandId());
			BrandEntity brandEntity = brandEntityResp.getData();
			orderItemEntity.setSpuBrand(brandEntity.getName());
			orderItemEntity.setCategoryId(spuInfoEntity.getCatalogId());
			orderItemEntity.setSkuId(skuInfoEntity.getSkuId());
			orderItemEntity.setSkuName(skuInfoEntity.getSkuName());
			orderItemEntity.setSkuPic(skuInfoEntity.getSkuDefaultImg());
			orderItemEntity.setSkuPrice(skuInfoEntity.getPrice());
			orderItemEntity.setSkuQuantity(orderItemVo.getCount());
			orderItemEntity.setSkuAttrsVals(JSON.toJSONString(orderItemVo.getSaleAttrValues()));
			this.orderItemDao.insert(orderItemEntity);
		});

		//订单创建之后, 在相应之前发送延时消息, 达到定时关闭订单的效果
		this.rabbitTemplate.convertAndSend("GMALL-ORDER-EXCHANGE", "order.ttl", orderSubmitVo.getOrderToken());

		// todo: list
		// 这个是每一种商品的优惠的小计. (小)
		//商品促销分解金额
		// orderItemEntity.setPromotionAmount(new BigDecimal("0"));
		//优惠券优惠分解金额
		// orderItemEntity.setCouponAmount(new BigDecimal("0"));
		//积分优惠分解金额
		// orderItemEntity.setIntegrationAmount(new BigDecimal("0"));
		//该商品经过优惠后的分解金额
		// orderItemEntity.setRealAmount(new BigDecimal("0"));
		//赠送积分
		// orderItemEntity.setGiftIntegration(0);
		//赠送成长值
		// orderItemEntity.setGiftGrowth(0);
		// 使用的优惠券 (大)
		// orderEntity.setCouponId(0L);
		//下单时使用的积分
		// orderEntity.setUseIntegration(0);
		//发票抬头
		// orderEntity.setBillHeader("");
		//发票内容
		// orderEntity.setBillContent("");
		//收票人电话
		// orderEntity.setBillReceiverPhone("");
		//收票人邮箱
		// orderEntity.setBillReceiverEmail("");
		//订单备注
		// orderEntity.setNote("");
		//发货时间
		// orderEntity.setDeliveryTime(new Date());
		//确认收货时间
		// orderEntity.setReceiveTime(new Date());
		//评价时间
		// orderEntity.setCommentTime(new Date());

		return orderEntity;
	}
}
