package com.atguigu.gmall.pms.controller;

import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.pms.entity.ProductAttrValueEntity;
import com.atguigu.gmall.pms.service.ProductAttrValueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


/**
 * spu属性值
 *
 * @author uiys
 * @email uiys@Gmall.com
 * @date 2020-03-19 01:51:34
 */
@Api(tags = "spu属性值 管理")
@RestController
@RequestMapping("pms/productattrvalue")
public class ProductAttrValueController {
	@Autowired
	private ProductAttrValueService productAttrValueService;



	/**
	 * 根据 spuId 和 cateId 查询组及组下规格参数（带值）
	 */
	@GetMapping("queryProductAttrValueListBy/{spuId}/{cateId}")
	public Resp<List<ProductAttrValueEntity>> queryProductAttrValueListBy(@PathVariable("spuId") Long spuId,
	                                                                      @PathVariable("cateId") Long cateId) {
		List<ProductAttrValueEntity> productAttrValueEntityList = this.productAttrValueService.queryProductAttrValueListBy(spuId, cateId);
		return Resp.ok(productAttrValueEntityList);
	}


	/**
	 * 根据spuId 查询该商品对应的搜索属性及值( pms_attr pms_product_attr_value)
	 */
	@GetMapping("{spuId}")
	public Resp<List<ProductAttrValueEntity>> querySearchAttrValueListBySpuId(@PathVariable("spuId") Long spuId) {
		List<ProductAttrValueEntity> searchAttrValueList = this.productAttrValueService.querySearchAttrValueListBySpuId(spuId);
		return Resp.ok(searchAttrValueList);
	}

	/**
	 * 列表
	 */
	@ApiOperation("分页查询(排序)")
	@GetMapping("/list")
	@PreAuthorize("hasAuthority('pms:productattrvalue:list')")
	public Resp<PageVo> list(QueryCondition queryCondition) {
		PageVo page = productAttrValueService.queryPage(queryCondition);

		return Resp.ok(page);
	}


	/**
	 * 信息
	 */
	@ApiOperation("详情查询")
	@GetMapping("/info/{id}")
	@PreAuthorize("hasAuthority('pms:productattrvalue:info')")
	public Resp<ProductAttrValueEntity> info(@PathVariable("id") Long id) {
		ProductAttrValueEntity productAttrValue = productAttrValueService.getById(id);

		return Resp.ok(productAttrValue);
	}

	/**
	 * 保存
	 */
	@ApiOperation("保存")
	@PostMapping("/save")
	@PreAuthorize("hasAuthority('pms:productattrvalue:save')")
	public Resp<Object> save(@RequestBody ProductAttrValueEntity productAttrValue) {
		productAttrValueService.save(productAttrValue);

		return Resp.ok(null);
	}

	/**
	 * 修改
	 */
	@ApiOperation("修改")
	@PostMapping("/update")
	@PreAuthorize("hasAuthority('pms:productattrvalue:update')")
	public Resp<Object> update(@RequestBody ProductAttrValueEntity productAttrValue) {
		productAttrValueService.updateById(productAttrValue);

		return Resp.ok(null);
	}

	/**
	 * 删除
	 */
	@ApiOperation("删除")
	@PostMapping("/delete")
	@PreAuthorize("hasAuthority('pms:productattrvalue:delete')")
	public Resp<Object> delete(@RequestBody Long[] ids) {
		productAttrValueService.removeByIds(Arrays.asList(ids));

		return Resp.ok(null);
	}

}
