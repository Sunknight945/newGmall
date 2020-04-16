package com.atguigu.gmall.wms.dao;

import com.atguigu.gmall.wms.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品库存
 *
 * @author uiys
 * @date 2020-03-18 18:05:22
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {


	int lockStock(@Param("skuId") Long skuId, @Param("count") Integer count, @Param("id") Long id);

	/**
	 * 这里把我搞混了 没有 就是他这里有 表的id 与 ware_id 有一个冗余了,
	 * 我傻了都.
	 *
	 * @param skuId ff
	 * @param count ff
	 * @return ff
	 */
	List<WareSkuEntity> checkStock(@Param("skuId") Long skuId, @Param("count") Integer count);

	/**
	 * 按id skuId count 解锁 mysql的库存.
	 *
	 * @param id    ff
	 * @param skuId ff
	 * @param count ff
	 */
	void setLockSkuToUnlock(@Param("id") Long id, @Param("skuId") Long skuId, @Param("count") Integer count);

	/**
	 * 下单成功减库存
	 * @param skuWareId ff
	 * @param count ff
	 */
	void minusStore(@Param("skuWareId") Long skuWareId, @Param("count") Integer count);
}
