package com.atguigu.gmall.sms.service.impl;

import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.gmall.sms.dao.SkuBoundsDao;
import com.atguigu.gmall.sms.dao.SkuFullReductionDao;
import com.atguigu.gmall.sms.dao.SkuLadderDao;
import com.atguigu.gmall.sms.entity.SkuBoundsEntity;
import com.atguigu.gmall.sms.entity.SkuFullReductionEntity;
import com.atguigu.gmall.sms.entity.SkuLadderEntity;
import com.atguigu.gmall.sms.service.SkuBoundsService;
import com.atguigu.gmall.sms.service.SkuFullReductionService;
import com.atguigu.gmall.sms.service.SkuLadderService;
import com.atguigu.gmall.sms.vo.SaleVo;
import com.atguigu.gmall.sms.vo.SkuSaleVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service("skuBoundsService")
public class SkuBoundsServiceImpl extends ServiceImpl<SkuBoundsDao, SkuBoundsEntity> implements SkuBoundsService {


	@Autowired
	private SkuLadderService skuLadderService;

	@Autowired
	private SkuFullReductionService skuFullReductionService;

	@Autowired
	private SkuBoundsDao skuBoundsDao;

	@Autowired
	private SkuFullReductionDao skuFullReductionDao;

	@Autowired
	private SkuLadderDao skuLadderDao;

	@Override
	public PageVo queryPage(QueryCondition params) {
		IPage<SkuBoundsEntity> page = this.page(
			new Query<SkuBoundsEntity>().getPage(params),
			new QueryWrapper<SkuBoundsEntity>()
		);

		return new PageVo(page);
	}

	/**
	 * 利用 feign 实现接口远程调用, 然后 这是被实现的接口, 在 pms 模块中.
	 *
	 * @param skuSaleVo
	 */
	@Override
	@Transactional
	public void saveSale(SkuSaleVo skuSaleVo) {
		//这里有三张表
		//3.保存营销信息的三张表
		//3.1 保存sms_sku_bounds
		SkuBoundsEntity skuBoundsEntity = new SkuBoundsEntity();
		List<Integer> workList = skuSaleVo.getWork();
		int work = 0;
		for (int i = 0; i < workList.size(); i++) {
			work += workList.get(i) * Math.pow(2, workList.size() - i - 1);
		}
		skuBoundsEntity.setWork(work);
		skuBoundsEntity.setBuyBounds(skuSaleVo.getBuyBounds());
		skuBoundsEntity.setGrowBounds(skuSaleVo.getGrowBounds());
		skuBoundsEntity.setSkuId(skuSaleVo.getSkuId());
		this.save(skuBoundsEntity);
		//3.2 保存sms_sku_ladder
		SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
		skuLadderEntity.setDiscount(skuSaleVo.getDiscount());
		skuLadderEntity.setAddOther(skuSaleVo.getLadderAddOther());
		skuLadderEntity.setSkuId(skuSaleVo.getSkuId());
		skuLadderEntity.setFullCount(skuSaleVo.getFullCount());
		this.skuLadderService.save(skuLadderEntity);
		//3.3 保存sms_sku_fullReduction
		SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
		skuFullReductionEntity.setSkuId(skuSaleVo.getSkuId());
		skuFullReductionEntity.setAddOther(skuSaleVo.getFullAddOther());
		skuFullReductionEntity.setFullPrice(skuSaleVo.getFullPrice());
		skuFullReductionEntity.setReducePrice(skuSaleVo.getReducePrice());
		this.skuFullReductionService.save(skuFullReductionEntity);
	}

	/**
	 * 查询组itemVo 所需要的营销信息 (两个字段) type 与 bounds )
	 */
	@Override
	public List<SaleVo> querySaleVoBySkuId(Long skuId) {
		List<SaleVo> saleVoList = new ArrayList<>();
		SaleVo boundsVo = new SaleVo();
		SkuBoundsEntity skuBoundsEntity = this.skuBoundsDao.selectOne(new QueryWrapper<SkuBoundsEntity>().eq("sku_id", skuId));
		boundsVo.setType("积分");
		if (skuBoundsEntity != null) {
			BigDecimal buyBounds = skuBoundsEntity.getBuyBounds();
			BigDecimal growBounds = skuBoundsEntity.getGrowBounds();
			StringBuilder sb = new StringBuilder();
			if (growBounds != null && growBounds.intValue() > 0) {
				sb.append("赠送成长积分:").append(growBounds);
			} else {
				sb.append("赠送成长积分:").append("无");
			}
			if (buyBounds != null && buyBounds.intValue() > 0) {
				sb.append("赠送购买积分:").append(buyBounds);
			} else {
				sb.append("赠送购买积分:").append("无");
			}
			boundsVo.setDesc(sb.toString());
			saleVoList.add(boundsVo);
		}

		//(
		//     id           bigint auto_increment comment 'id'
		//         primary key,
		//     sku_id       bigint     null comment 'spu_id',
		//     full_price   decimal    null comment '满多少',
		//     reduce_price decimal    null comment '减多少',
		//     add_other    tinyint(1) null comment '是否参与其他优惠'
		// )
		//     comment '商品满减信息';
		SkuFullReductionEntity skuFullReductionEntity = this.skuFullReductionDao.selectOne(new QueryWrapper<SkuFullReductionEntity>().eq("sku_id", skuId));
		SaleVo reductionVo = new SaleVo();
		if (skuFullReductionEntity != null) {
			BigDecimal fullPrice = skuFullReductionEntity.getFullPrice();
			BigDecimal reducePrice = skuFullReductionEntity.getReducePrice();
			StringBuffer sb = new StringBuffer();
			if (fullPrice != null && fullPrice.intValue() > 0 && reducePrice != null && reducePrice.intValue() > 0) {
				reductionVo.setType("满减");
				sb.append("满:").append(fullPrice).append("减:").append(reducePrice);
			} else {
				sb.append("无满减");
			}
			reductionVo.setDesc(sb.toString());
		}
		saleVoList.add(reductionVo);
		//(
		//     id         bigint auto_increment comment 'id'
		//         primary key,
		//     sku_id     bigint     null comment 'spu_id',
		//     full_count int        null comment '满几件',
		//     discount   decimal    null comment '打几折',
		//     price      decimal    null comment '折后价',
		//     add_other  tinyint(1) null comment '是否叠加其他优惠[0-不可叠加，1-可叠加]'
		// )
		//     comment '商品阶梯价格';
		SkuLadderEntity ladderEntity = this.skuLadderDao.selectOne(new QueryWrapper<SkuLadderEntity>().eq("sku_id", skuId));
		SaleVo ladderVo = new SaleVo();
		ladderVo.setType("打折");
		if (ladderEntity != null) {
			StringBuffer sb = new StringBuffer();
			Integer fullCount = ladderEntity.getFullCount();
			BigDecimal discount = ladderEntity.getDiscount();
			if (fullCount != null && fullCount > 0 && discount != null && discount.intValue() > 0) {
				sb.append("满:").append(fullCount).append("打").append(discount.intValue());
			} else {
				sb.append("无打折");
			}
			ladderVo.setDesc(sb.toString());
		}
		saleVoList.add(ladderVo);
		return saleVoList;
	}
}
