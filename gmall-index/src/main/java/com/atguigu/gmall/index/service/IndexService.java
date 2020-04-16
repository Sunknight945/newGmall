package com.atguigu.gmall.index.service;

import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.atguigu.gmall.pms.vo.CategoryEntityVo;

import java.util.List;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
public interface IndexService {
	/**
	 * 查询一级分类
	 *
	 * @return
	 */
	List<CategoryEntity> queryLv1Categories();

	/**
	 * 查询一级分类下的子分类
	 * @param pid 父级分类的id
	 * @return
	 */
	List<CategoryEntityVo> querySubCategories(Long pid);

	/**
	 * 测试锁
	 */
	void testLock();

	String testRead();

	String testWrite();
}
