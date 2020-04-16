package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.atguigu.gmall.pms.vo.CategoryEntityVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;

import java.util.List;


/**
 * 商品三级分类
 *
 * @author uiys
 * @email uiys@Gmall.com
 * @date 2020-03-19 01:51:33
 */
public interface CategoryService extends IService<CategoryEntity> {

  PageVo queryPage(QueryCondition params);

	/**
	 * 根据pid 查询 二级分类和它下面的三级分类的list
	 */
	List<CategoryEntityVo> querySubCategoriesByPid(Long pid);
}

