package com.atguigu.gmall.sms.api;

import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.sms.vo.SaleVo;
import com.atguigu.gmall.sms.vo.SkuSaleVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
public interface GmallSmsApi {
	@PostMapping("sms/skubounds/sku/sale/save")
	public Resp<Object> saveSale(@RequestBody SkuSaleVo skuSaleVo);

	/**
	 * 查询组itemVo 所需要的音效信息 (两个字段) type 与 bounds )
	 */
	@GetMapping("sms/skubounds/querySaleVoBySkuId/{skuId}")
	public Resp<List<SaleVo>> querySaleVoBySkuId(@PathVariable("skuId") Long skuId);
}


