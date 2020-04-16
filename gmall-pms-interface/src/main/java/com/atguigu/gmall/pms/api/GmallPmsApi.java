package com.atguigu.gmall.pms.api;

import com.atguigu.core.bean.QueryCondition;
import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.pms.vo.CategoryEntityVo;
import com.atguigu.gmall.pms.vo.ItemGroupVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
public interface GmallPmsApi {
	/**
	 * 列表
	 * 分页查询 spu的列表
	 */
	@PostMapping("pms/spuinfo/page")
	public Resp<List<SpuInfoEntity>> querySpuListByPage(@RequestBody QueryCondition queryCondition);


	/**
	 * 根据spuId 检索它对应的skuList列表
	 * 分页查询(根据spuId 检索它对应的skuList列表)
	 */
	@GetMapping("pms/skuinfo/{spuId}")
	public Resp<List<SkuInfoEntity>> querySkuPageBySpuId(@PathVariable("spuId") Long spuId);

	/**
	 * 根据品牌id查询品牌
	 */
	@GetMapping("pms/brand/info/{brandId}")
	public Resp<BrandEntity> queryBrandById(@PathVariable("brandId") Long brandId);

	/**
	 * 根据品牌id查询分类
	 */
	@GetMapping("pms/category/info/{catId}")
	public Resp<CategoryEntity> queryCategoryById(@PathVariable("catId") Long catId);

	/**
	 * 根据spuId 查询该商品对应的搜索属性及值( pms_attr pms_product_attr_value)
	 */
	@GetMapping("pms/productattrvalue/{spuId}")
	public Resp<List<ProductAttrValueEntity>> querySearchAttrValueListBySpuId(@PathVariable("spuId") Long spuId);

	/**
	 * 在 es的listen中 新增 spu时 同时在es中添加数据
	 */
	@ApiOperation("详情查询")
	@GetMapping("pms/spuinfo/info/{id}")
	public Resp<SpuInfoEntity> querySpuBySpuId(@PathVariable("id") Long id);

	/**
	 * 在index页面 查找一级分类
	 */
	@GetMapping("pms/category")
	public Resp<List<CategoryEntity>> queryCategoryListByParenCidAndLevel(@RequestParam(value = "level", defaultValue = "0") Integer level,
	                                                                      @RequestParam(value = "parentCid", required = false) Long parentCid);

	/**
	 * 在index 页面查询一级分类下面的子分类
	 */
	@GetMapping("pms/category/{pid}")
	public Resp<List<CategoryEntityVo>> querySubCategoriesByPid(@PathVariable("pid") Long pid);

	/**
	 * 根据skuId 查询skuInfo
	 */
	@GetMapping("pms/skuinfo/info/{skuId}")
	public Resp<SkuInfoEntity> querySkuInfoBySkuId(@PathVariable("skuId") Long skuId);

	/**
	 * 通过skuId获取 对应的skuImageList
	 */
	@GetMapping("pms/skuimages/imageList/{skuId}")
	public Resp<List<SkuImagesEntity>> queryImageListBySkuId(@PathVariable("skuId") Long skuId);


	/**
	 * 根据skuId 查询他的销售属性和值
	 */
	@GetMapping("pms/skusaleattrvalue/saleAttrValue/{skuId}")
	public Resp<List<SkuSaleAttrValueEntity>> querySaleAttrValueBySkuId(@PathVariable("skuId") Long skuId);

	/**
	 * 根据 spuId 查询spu的详细信息 例如(海报)
	 */
	@GetMapping("pms/spuinfodesc/info/{spuId}")
	public Resp<SpuInfoDescEntity> querySpuInfoDescBySpuId(@PathVariable("spuId") Long spuId);

	/**
	 * 据categoryId查 attrGroup 属性分组
	 */
	@GetMapping("pms/attrgroup/queryAttrGroupList/{cid}")
	public Resp<List<AttrGroupEntity>> queryAttrGroupListByCategoryId(@PathVariable("cid") Long cid);

	/**
	 * 据spuId 和 cateId 查询组及组下规格参数（带值）
	 */
	@GetMapping("pms/attrgroup/itemGroupVo/{cid}/{spuId}")
	public Resp<List<ItemGroupVo>> queryItemGroupVoBySpuIdAndCateId(@PathVariable("cid") Long cid, @PathVariable("spuId") Long spuId);
}


