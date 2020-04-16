package com.atguigu.gmall.pms.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.gmall.pms.dao.AttrAttrgroupRelationDao;
import com.atguigu.gmall.pms.dao.AttrDao;
import com.atguigu.gmall.pms.dao.AttrGroupDao;
import com.atguigu.gmall.pms.dao.ProductAttrValueDao;
import com.atguigu.gmall.pms.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gmall.pms.entity.AttrEntity;
import com.atguigu.gmall.pms.entity.AttrGroupEntity;
import com.atguigu.gmall.pms.entity.ProductAttrValueEntity;
import com.atguigu.gmall.pms.service.AttrGroupService;
import com.atguigu.gmall.pms.vo.GroupVo;
import com.atguigu.gmall.pms.vo.ItemGroupVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {


	@Autowired
	private AttrGroupDao attrGroupDao;

	@Autowired
	private AttrAttrgroupRelationDao attrAttrgroupRelationDao;

	@Autowired
	private AttrDao attrDao;

	@Autowired
	private ProductAttrValueDao productAttrValueDao;

	@Override
	public PageVo queryPage(QueryCondition params) {
		IPage<AttrGroupEntity> page = this.page(
			new Query<AttrGroupEntity>().getPage(params),
			new QueryWrapper<AttrGroupEntity>()
		);
		return new PageVo(page);
	}

	@Override
	public PageVo queryGroupByPage(QueryCondition queryCondition, Long catId) {
		QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<>();
		if (catId != null) {
			queryWrapper.eq("catelog_id", catId);
		}
		IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(queryCondition), queryWrapper);
		return new PageVo(page);
	}

	@Override
	public GroupVo queryGroupWithAttrsByGid(Long gid) {
		GroupVo groupVo = new GroupVo();
		// g - r  - a attrGroup  attr-attrGroup 中间表 attr 表 靠中间表 获取对应关系 然后查询
		AttrGroupEntity attrGroupEntity = this.getById(gid);
		BeanUtil.copyProperties(attrGroupEntity, groupVo);
		Long attrGroupId = attrGroupEntity.getAttrGroupId();
		QueryWrapper<AttrAttrgroupRelationEntity> attrgroupRelationEntityQueryWrapper = new QueryWrapper<>();
		attrgroupRelationEntityQueryWrapper.eq("attr_group_id", attrGroupId);
		List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntityList = this.attrAttrgroupRelationDao.selectList(attrgroupRelationEntityQueryWrapper);
		if (CollectionUtil.isEmpty(attrAttrgroupRelationEntityList)) {
			return groupVo;
		}
		groupVo.setRelations(attrAttrgroupRelationEntityList);
		List<Long> attrIds = attrAttrgroupRelationEntityList.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
		List<AttrEntity> attrEntityList = this.attrDao.selectBatchIds(attrIds);
		groupVo.setAttrEntities(attrEntityList);
		return groupVo;
	}

	/**
	 * 根据cid 分类id查找 attrGroup 录入spu属性信息步骤
	 *
	 * @param cid
	 * @return
	 */
	@Override
	public List<GroupVo> queryAttrGroupWithAttrByCid(Long cid) {
		List<AttrGroupEntity> attrGroupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", cid));
		return attrGroupEntities.stream().map(attrGroupEntity -> this.queryGroupWithAttrsByGid(attrGroupEntity.getAttrGroupId())).collect(Collectors.toList());

		// List<GroupVo> groupVoList = new ArrayList<>();
		// List<AttrGroupEntity> attrGroupEntityList = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", cid));
		// for (int i = 0; i < attrGroupEntityList.size(); i++) {
		//   GroupVo groupVo = new GroupVo();
		//   BeanUtil.copyProperties(groupVo, attrGroupEntityList.get(i));
		//   attrGroupEntityList.forEach(attrGroupEntity -> {
		//     List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities = this.attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrGroupEntity.getAttrGroupId()));
		//     List<Long> attrIdList = attrAttrgroupRelationEntities.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
		//     List<AttrEntity> attrEntityList = this.attrDao.selectBatchIds(attrIdList);
		//     groupVo.setAttrEntities(attrEntityList);
		//     groupVoList.add(groupVo);
		//   });
		// }
	}

	/**
	 * 据categoryId查 attrGroup 属性分组
	 */
	@Override
	public List<AttrGroupEntity> queryAttrGroupListByCategoryId(Long cid) {
		QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("catelog_id", cid);
		return this.attrGroupDao.selectList(queryWrapper);
	}

	/**
	 * 据 spuId 和 cateId 查询组及组下规格参数（带值）
	 */
	@Override
	public List<ItemGroupVo> queryItemGroupVoBySpuIdAndCateId(Long cid, Long spuId) {
		List<AttrGroupEntity> attrGroupEntityList = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", cid));
		return attrGroupEntityList.stream().map(attrGroupEntity -> {
			ItemGroupVo itemGroupVo = new ItemGroupVo();
			List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntityList = this.attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrGroupEntity.getAttrGroupId()));
			List<Long> attrIdList = attrAttrgroupRelationEntityList.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
			// 查询规格参数及值
			List<ProductAttrValueEntity> productAttrValueEntityList = this.productAttrValueDao.selectList(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId).in("attr_id", attrIdList));
			itemGroupVo.setName(attrGroupEntity.getAttrGroupName());
			itemGroupVo.setBaseAttrs(productAttrValueEntityList);
			return itemGroupVo;
		}).collect(Collectors.toList());
	}
}
