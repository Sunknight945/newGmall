package com.atguigu.gmall.order.feign;


import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.oms.api.GmallOmsApi;
import com.atguigu.gmall.oms.entity.OrderEntity;
import com.atguigu.gmall.oms.vo.OrderSubmitVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@FeignClient("oms-service")
public interface GmallOmsClient extends GmallOmsApi {

	@PostMapping("oms/order/saveOrder")
	public Resp<OrderEntity> saveOrder(@RequestBody OrderSubmitVo orderSubmitVo);
}


