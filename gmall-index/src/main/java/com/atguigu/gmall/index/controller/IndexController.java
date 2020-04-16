package com.atguigu.gmall.index.controller;

import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.index.service.IndexService;
import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.atguigu.gmall.pms.vo.CategoryEntityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@RestController
@RequestMapping("index")
public class IndexController {
	@Autowired
	private IndexService indexService;


	/**
	 * 查询一级分类
	 *
	 * @return
	 */
	@GetMapping("cates")
	public Resp<List<CategoryEntity>> queryLv1Categories() {
		List<CategoryEntity> lv1CategoryEntityList = this.indexService.queryLv1Categories();
		return Resp.ok(lv1CategoryEntityList);
	}

	/**
	 * 查询一级分类
	 *
	 * @return
	 */
	@GetMapping("cates/{cid}")
	public Resp<List<CategoryEntityVo>> querySubCategories(@PathVariable("cid") Long pid) {
		List<CategoryEntityVo> subCategoryEntityVoList = this.indexService.querySubCategories(pid);
		return Resp.ok(subCategoryEntityVoList);
	}

	@GetMapping("test")
	public String testLock() {
		this.indexService.testLock();
		return "ok";
	}


	@GetMapping("test/read")
	public String testRead() {
		return this.indexService.testRead();
	}

		@GetMapping("test/write")
	public String testWrite() {
		return this.indexService.testWrite();
	}


}


