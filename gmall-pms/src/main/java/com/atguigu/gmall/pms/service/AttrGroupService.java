package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.entity.AttrGroupEntity;
import com.atguigu.gmall.pms.vo.GroupVo;
import com.atguigu.gmall.pms.vo.ItemGroupVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;

import java.util.List;


/**
 * 属性分组
 *
 * @author uiys
 * @email uiys@Gmall.com
 * @date 2020-03-19 01:51:33
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

	PageVo queryPage(QueryCondition params);


	/**
	 * 分页查询attrGroup
	 *
	 * @param queryCondition
	 * @param catId
	 * @return
	 */
	PageVo queryGroupByPage(QueryCondition queryCondition, Long catId);

	/**
	 * 根据groupId 查询 GroupVo 的 list
	 *
	 * @param gid
	 * @return
	 */
	GroupVo queryGroupWithAttrsByGid(Long gid);

	/**
	 * 根据cid 分类id查找 attrGroup 录入spu属性信息步骤
	 *
	 * @param cid
	 * @return
	 */
	List<GroupVo> queryAttrGroupWithAttrByCid(Long cid);

	/**
	 * 据categoryId查 attrGroup 属性分组
	 */
	List<AttrGroupEntity> queryAttrGroupListByCategoryId(Long cid);

	/**
	 * 据 spuId 和 cateId 查询组及组下规格参数（带值）
	 */
	List<ItemGroupVo> queryItemGroupVoBySpuIdAndCateId(Long cid, Long spuId);
}

