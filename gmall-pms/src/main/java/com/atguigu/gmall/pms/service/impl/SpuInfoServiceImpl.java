package com.atguigu.gmall.pms.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.gmall.pms.dao.SkuInfoDao;
import com.atguigu.gmall.pms.dao.SpuInfoDao;
import com.atguigu.gmall.pms.dao.SpuInfoDescDao;
import com.atguigu.gmall.pms.entity.ProductAttrValueEntity;
import com.atguigu.gmall.pms.entity.SkuImagesEntity;
import com.atguigu.gmall.pms.entity.SkuSaleAttrValueEntity;
import com.atguigu.gmall.pms.entity.SpuInfoEntity;
import com.atguigu.gmall.pms.feign.GmallSmsClient;
import com.atguigu.gmall.pms.service.*;
import com.atguigu.gmall.pms.vo.BaseAttrVo;
import com.atguigu.gmall.pms.vo.SkuInfoVo;
import com.atguigu.gmall.pms.vo.SpuInfoVo;
import com.atguigu.gmall.sms.vo.SkuSaleVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {


	@Autowired
	private SpuInfoDescDao spuInfoDescDao;

	@Autowired
	private ProductAttrValueService attrValueService;

	@Autowired
	private SkuInfoDao skuInfoDao;

	@Autowired
	private SkuImagesService skuImagesService;

	@Autowired
	private SkuSaleAttrValueService skuSaleAttrValueService;

	@Autowired
	private SpuInfoDescService spuInfoDescService;


	@Autowired
	private GmallSmsClient gmallSmsClient;

	@Autowired
	private AmqpTemplate amqpTemplate;

	@Value("${item.rabbitmq.exchange}")
	private String EXCHANGE_NAME;


	@Override
	public PageVo queryPage(QueryCondition params) {
		IPage<SpuInfoEntity> page = this.page(new Query<SpuInfoEntity>().getPage(params), new QueryWrapper<SpuInfoEntity>());
		return new PageVo(page);
	}

	/**
	 * 根据条件 和 catId 查询 spu分页
	 *
	 * @param condition
	 * @param catId
	 * @return
	 */
	@Override
	public PageVo querySpuPage(QueryCondition condition, Long catId) {
		QueryWrapper<SpuInfoEntity> queryWrapper = new QueryWrapper<>();
		if (catId != 0) {
			queryWrapper.eq("catalog_id", catId);
		}
		String key = condition.getKey();
		if (StrUtil.isNotBlank(key)) {
			queryWrapper.and(t -> t.eq("id", key).or().like("spu_name", key));
		}
		IPage<SpuInfoEntity> page = this.page(new Query<SpuInfoEntity>().getPage(condition), queryWrapper);
		return new PageVo(page);
	}


	/**
	 * 有九张表需要实现保存.
	 *
	 * @param spuInfoVo
	 */
	@Override
	// @Transactional  这个不能实现分布式事务的控制, 只能完成本地事物的控制
	// GlobalTransactional 借助seata 可以完成分布式事务的控制
	@GlobalTransactional
	public void bigSave(SpuInfoVo spuInfoVo) {
		//1. 保存spu相关的三张表
		//1.1. 保存pms_spu_info信息
		Long spuId = saveSpuInfo(spuInfoVo);
		//1.2.保存spm_spu_info_desc 描述信息
		this.spuInfoDescService.saveSpuInfoDesc(spuInfoVo, spuId);
		//1.3 保存pms_productAttrValue
		saveBaseAttrValue(spuInfoVo, spuId);
		//2. 保存sku相关的三张表
		saveSkuAndSale(spuInfoVo, spuId);
		this.sendMsgUpdatedEs("insert", spuId);
	}

	private void sendMsgUpdatedEs(String type, Long spuId) {
		this.amqpTemplate.convertAndSend(EXCHANGE_NAME, "item." + type, spuId);
	}

	/**
	 * 保存 sku 的三张表
	 *
	 * @param spuInfoVo
	 * @param spuId
	 */
	private void saveSkuAndSale(SpuInfoVo spuInfoVo, Long spuId) {
		List<SkuInfoVo> skus = spuInfoVo.getSkus();
		if (CollUtil.isEmpty(skus)) {
			return;
		}
		skus.forEach(skuInfoVo -> {
			//2. 保存sku相关的三张表
			//2.1. 保存pms_sku_info
			skuInfoVo.setSpuId(spuId);
			skuInfoVo.setSkuCode(UUID.randomUUID().toString());
			skuInfoVo.setBrandId(spuInfoVo.getBrandId());
			skuInfoVo.setCatalogId(spuInfoVo.getCatalogId());
			List<String> images = skuInfoVo.getImages();
			if (CollUtil.isNotEmpty(images)) {
				skuInfoVo.setSkuDefaultImg(skuInfoVo.getSkuDefaultImg() == null ? skuInfoVo.getImages().get(0) : skuInfoVo.getSkuDefaultImg());
			}
			this.skuInfoDao.insert(skuInfoVo);
			Long skuId = skuInfoVo.getSkuId();
			//2.2. 保存pms_sku_images
			if (CollUtil.isNotEmpty(images)) {
				List<SkuImagesEntity> skuImagesEntityList = images.stream().map(image -> {
					SkuImagesEntity imagesEntity = new SkuImagesEntity();
					imagesEntity.setImgUrl(image);
					imagesEntity.setSkuId(skuId);
					imagesEntity.setDefaultImg(StrUtil.equals(image, skuInfoVo.getSkuDefaultImg()) ? 1 : 0);
					return imagesEntity;
				}).collect(Collectors.toList());
				this.skuImagesService.saveBatch(skuImagesEntityList);
			}
			//2.3. 保存pms_sale_attrValue
			List<SkuSaleAttrValueEntity> skuSaleAttrValueEntityList = skuInfoVo.getSaleAttrs();
			if (CollectionUtil.isNotEmpty(skuSaleAttrValueEntityList)) {
				//设置skuId
				skuSaleAttrValueEntityList.forEach(skuSaleAttrValueEntity -> skuSaleAttrValueEntity.setSkuId(skuId));
				//批量保存销售属性
				skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntityList);

				System.out.println("????");
			}

			//3.保存营销信息的三张表
			SkuSaleVo skuSaleVo = new SkuSaleVo();
			BeanUtil.copyProperties(skuInfoVo, skuSaleVo);
			skuSaleVo.setSkuId(skuId);
			this.gmallSmsClient.saveSale(skuSaleVo);
			//3.1 保存sms_sku_bounds
			//3.2 保存sms_sku_ladder
			//3.3 保存sms_sku_fullReduction
		});
	}

	/**
	 * 1.3 保存pms_productAttrValue
	 *
	 * @param spuInfoVo
	 * @param spuId
	 */
	private void saveBaseAttrValue(SpuInfoVo spuInfoVo, Long spuId) {
		List<BaseAttrVo> baseAttrs = spuInfoVo.getBaseAttrs();
		if (CollUtil.isNotEmpty(baseAttrs)) {
			List<ProductAttrValueEntity> valueEntityList = baseAttrs.stream().map(baseAttrVo -> {
				baseAttrVo.setSpuId(spuId);
				return (ProductAttrValueEntity) baseAttrVo;
			}).collect(Collectors.toList());
			this.attrValueService.saveBatch(valueEntityList);
		}
	}

	/**
	 * 1.1. 保存pms_spu_info信息
	 *
	 * @param spuInfoVo
	 * @return
	 */
	private Long saveSpuInfo(SpuInfoVo spuInfoVo) {
		spuInfoVo.setCreateTime(new Date());
		spuInfoVo.setUodateTime(spuInfoVo.getCreateTime());
		this.save(spuInfoVo);
		return spuInfoVo.getId();
	}
}
