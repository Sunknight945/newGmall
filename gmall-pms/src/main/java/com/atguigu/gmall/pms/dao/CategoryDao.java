package com.atguigu.gmall.pms.dao;

import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.atguigu.gmall.pms.vo.CategoryEntityVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品三级分类
 *
 * @author uiys
 * @email uiys@Gmall.com
 * @date 2020-03-19 01:51:33
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {

	List<CategoryEntity> queryCategoryListByParenCidAndLevel(@Param("level") Integer level, @Param("parentCid") Long parentCid);

	/**
	 * 根据pid 查询 二级分类和它下面的三级分类的list
	 *
	 * @param pid
	 * @return
	 */
	List<CategoryEntityVo> querySubCategoriesByPid(@Param("pid") Long pid);
}
