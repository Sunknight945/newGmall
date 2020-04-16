package com.atguigu.gmall.pms.controller;

import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.pms.dao.CategoryDao;
import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.atguigu.gmall.pms.service.CategoryService;
import com.atguigu.gmall.pms.vo.CategoryEntityVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


/**
 * 商品三级分类
 *
 * @author uiys
 * @email uiys@Gmall.com
 * @date 2020-03-19 01:51:33
 */
@Api(tags = "商品三级分类 管理")
@RestController
@RequestMapping("pms/category")
public class CategoryController {
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private CategoryDao categoryDao;


	@GetMapping
	public Resp<List<CategoryEntity>> queryCategoryListByParenCidAndLevel(@RequestParam(value = "level", defaultValue = "0") Integer level,
	                                                                      @RequestParam(value = "parentCid", required = false) Long parentCid) {
		// List<CategoryEntity> categoryEntityList = this.categoryDao.queryCategoryListByParenCidAndLevel(level, parentCid);
		QueryWrapper<CategoryEntity> queryWrapper = new QueryWrapper<>();
		if (parentCid != null) {
			queryWrapper.eq("parent_cid", parentCid);
		}
		if (level != 0) {
			queryWrapper.eq("cat_level", level);
		}
		List<CategoryEntity> categoryEntityList = categoryService.list(queryWrapper);
		return Resp.ok(categoryEntityList);
	}

	/**
	 * 查询 一级分类下面的子分类
	 * @param pid
	 * @return
	 */
	@GetMapping("{pid}")
	public Resp<List<CategoryEntityVo>> querySubCategoriesByPid(@PathVariable("pid") Long pid) {
		List<CategoryEntityVo> categoryEntityVoList = this.categoryService.querySubCategoriesByPid(pid);
		return Resp.ok(categoryEntityVoList);
	}


	/**
	 * 列表
	 */
	@ApiOperation("分页查询(排序)")
	@GetMapping("/list")
	@PreAuthorize("hasAuthority('pms:category:list')")
	public Resp<PageVo> list(QueryCondition queryCondition) {
		PageVo page = categoryService.queryPage(queryCondition);

		return Resp.ok(page);
	}


	/**
	 * 信息
	 */
	@ApiOperation("详情查询")
	@GetMapping("/info/{catId}")
	@PreAuthorize("hasAuthority('pms:category:info')")
	public Resp<CategoryEntity> info(@PathVariable("catId") Long catId) {
		CategoryEntity category = categoryService.getById(catId);

		return Resp.ok(category);
	}

	/**
	 * 保存
	 */
	@ApiOperation("保存")
	@PostMapping("/save")
	@PreAuthorize("hasAuthority('pms:category:save')")
	public Resp<Object> save(@RequestBody CategoryEntity category) {
		categoryService.save(category);

		return Resp.ok(null);
	}

	/**
	 * 修改
	 */
	@ApiOperation("修改")
	@PostMapping("/update")
	@PreAuthorize("hasAuthority('pms:category:update')")
	public Resp<Object> update(@RequestBody CategoryEntity category) {
		categoryService.updateById(category);

		return Resp.ok(null);
	}

	/**
	 * 删除
	 */
	@ApiOperation("删除")
	@PostMapping("/delete")
	@PreAuthorize("hasAuthority('pms:category:delete')")
	public Resp<Object> delete(@RequestBody Long[] catIds) {
		categoryService.removeByIds(Arrays.asList(catIds));

		return Resp.ok(null);
	}

}
