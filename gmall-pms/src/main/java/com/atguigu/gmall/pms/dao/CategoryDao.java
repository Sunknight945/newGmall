package com.atguigu.gmall.pms.dao;

import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 *
 * @author uiys
 * @email uiys@Gmall.com
 * @date 2020-03-18 03:23:02
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {

}
