package com.atguigu.gmall.sms.controller;

import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.sms.entity.SkuBoundsEntity;
import com.atguigu.gmall.sms.service.SkuBoundsService;
import com.atguigu.gmall.sms.vo.SaleVo;
import com.atguigu.gmall.sms.vo.SkuSaleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


/**
 * 商品sku积分设置
 *
 * @author uiys
 * @email uiys@gmall.com
 * @date 2020-03-18 17:48:12
 */
@Api(tags = "商品sku积分设置 管理")
@RestController
@RequestMapping("sms/skubounds")
public class SkuBoundsController {
	@Autowired
	private SkuBoundsService skuBoundsService;


	/**
	 * 查询组itemVo 所需要的音效信息 (两个字段) type 与 bounds )
	 * @param skuId
	 * @return
	 */
	@GetMapping("querySaleVoBySkuId/{skuId}")
	public Resp<List<SaleVo>> querySaleVoBySkuId(@PathVariable("skuId") Long skuId) {
		List<SaleVo> saleVoList = this.skuBoundsService.querySaleVoBySkuId(skuId);
		return Resp.ok(saleVoList);
	}


	/**
	 * 在pms使用feign 远程调用 保存.
	 *
	 * @param skuSaleVo
	 * @return
	 */
	@PostMapping("sku/sale/save")
	public Resp<Object> saveSale(@RequestBody SkuSaleVo skuSaleVo) {
		skuBoundsService.saveSale(skuSaleVo);
		return Resp.ok(null);
	}

	/**
	 * 列表
	 */
	@ApiOperation("分页查询(排序)")
	@GetMapping("/list")
	@PreAuthorize("hasAuthority('sms:skubounds:list')")
	public Resp<PageVo> list(QueryCondition queryCondition) {
		PageVo page = skuBoundsService.queryPage(queryCondition);

		return Resp.ok(page);
	}


	/**
	 * 信息
	 */
	@ApiOperation("详情查询")
	@GetMapping("/info/{id}")
	@PreAuthorize("hasAuthority('sms:skubounds:info')")
	public Resp<SkuBoundsEntity> info(@PathVariable("id") Long id) {
		SkuBoundsEntity skuBounds = skuBoundsService.getById(id);

		return Resp.ok(skuBounds);
	}

	/**
	 * 保存
	 */
	@ApiOperation("保存")
	@PostMapping("/save")
	@PreAuthorize("hasAuthority('sms:skubounds:save')")
	public Resp<Object> save(@RequestBody SkuBoundsEntity skuBounds) {
		skuBoundsService.save(skuBounds);

		return Resp.ok(null);
	}

	/**
	 * 修改
	 */
	@ApiOperation("修改")
	@PostMapping("/update")
	@PreAuthorize("hasAuthority('sms:skubounds:update')")
	public Resp<Object> update(@RequestBody SkuBoundsEntity skuBounds) {
		skuBoundsService.updateById(skuBounds);

		return Resp.ok(null);
	}

	/**
	 * 删除
	 */
	@ApiOperation("删除")
	@PostMapping("/delete")
	@PreAuthorize("hasAuthority('sms:skubounds:delete')")
	public Resp<Object> delete(@RequestBody Long[] ids) {
		skuBoundsService.removeByIds(Arrays.asList(ids));

		return Resp.ok(null);
	}

}
