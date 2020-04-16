package com.atguigu.gmall.item.service.impl;

import cn.hutool.core.util.StrUtil;
import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.item.feign.GmallPmsClient;
import com.atguigu.gmall.item.feign.GmallSmsClient;
import com.atguigu.gmall.item.feign.GmallWmsClient;
import com.atguigu.gmall.item.service.ItemService;
import com.atguigu.gmall.item.vo.ItemVo;
import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.pms.vo.ItemGroupVo;
import com.atguigu.gmall.sms.vo.SaleVo;
import com.atguigu.gmall.wms.entity.WareSkuEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private GmallPmsClient pmsClient;
	@Autowired
	private GmallWmsClient wmsClient;
	@Autowired
	private GmallSmsClient smsClient;

	@Autowired
	private ThreadPoolExecutor threadPoolExecutor;

	/**
	 * 根据skuId 查询组装 item 的数据库的信息
	 */
	@Override
	public ItemVo queryItemVo(Long skuId) {
		ItemVo itemVo = new ItemVo();

		itemVo.setSkuId(skuId);
		CompletableFuture<Object> skuInfoCompletableFuture = CompletableFuture.supplyAsync(() -> {
			Resp<SkuInfoEntity> skuInfoEntityResp = this.pmsClient.querySkuInfoBySkuId(skuId);
			SkuInfoEntity skuInfoEntity = skuInfoEntityResp.getData();
			if (skuInfoEntity == null) {
				return itemVo; //assigment
			}
			itemVo.setSkuTitle(skuInfoEntity.getSkuTitle());
			itemVo.setPrice(skuInfoEntity.getPrice());
			itemVo.setWeight(skuInfoEntity.getWeight());
			itemVo.setSubTitle(skuInfoEntity.getSkuSubtitle());
			itemVo.setSpuId(skuInfoEntity.getSpuId());
			return skuInfoEntity;
		}, threadPoolExecutor);

		CompletableFuture<Void> spuInfoCompletableFuture = skuInfoCompletableFuture.thenAcceptAsync(sku -> {
			// 根据sku中的spuId查询spu
			Resp<SpuInfoEntity> spuInfoEntityResp = this.pmsClient.querySpuBySpuId(((SkuInfoEntity) sku).getSpuId());
			SpuInfoEntity spuInfoEntity = spuInfoEntityResp.getData();
			String spuName = spuInfoEntity.getSpuName();
			itemVo.setSpuId(((SkuInfoEntity) sku).getSpuId());
			itemVo.setSpuName(spuName);
		}, threadPoolExecutor);

		CompletableFuture<Void> categoryCompletableFuture = skuInfoCompletableFuture.thenAcceptAsync(sku -> {
			Long catalogId = ((SkuInfoEntity) (sku)).getCatalogId();
			Resp<CategoryEntity> categoryEntityResp = this.pmsClient.queryCategoryById(catalogId);
			CategoryEntity categoryEntity = categoryEntityResp.getData();
			itemVo.setCategoryEntity(categoryEntity);
		}, threadPoolExecutor);


		CompletableFuture<Void> brandEntityCompletableFuture = skuInfoCompletableFuture.thenAcceptAsync(sku -> {
			Long brandId = ((SkuInfoEntity) (sku)).getBrandId();
			Resp<BrandEntity> brandEntityResp = this.pmsClient.queryBrandById(brandId);
			BrandEntity brandEntity = brandEntityResp.getData();
			itemVo.setBrandEntity(brandEntity);
		}, threadPoolExecutor);


		CompletableFuture<Void> skuImageListCompletableFuture = CompletableFuture.runAsync(() -> {
			Resp<List<SkuImagesEntity>> skuImageListBySkuId = this.pmsClient.queryImageListBySkuId(skuId);
			List<SkuImagesEntity> skuImagesEntityList = skuImageListBySkuId.getData();
			itemVo.setPics(skuImagesEntityList);
		}, threadPoolExecutor);


		//营销信息
		CompletableFuture<Void> saleVoCompletableFuture = CompletableFuture.runAsync(() -> {
			Resp<List<SaleVo>> saleVoBySkuId = this.smsClient.querySaleVoBySkuId(skuId);
			List<SaleVo> saleVoList = saleVoBySkuId.getData();
			itemVo.setSales(saleVoList);
		}, threadPoolExecutor);


		CompletableFuture<Void> wareSkuEntityCompletableFuture = CompletableFuture.runAsync(() -> {
			Resp<List<WareSkuEntity>> wareSkuEntityResp = this.wmsClient.queryWareSkuEntityResp(skuId);
			List<WareSkuEntity> wareSkuEntityList = wareSkuEntityResp.getData();
			boolean flag = wareSkuEntityList.stream().anyMatch(wareSkuEntity -> wareSkuEntity.getStock() > 0);
			itemVo.setStore(flag);
		}, threadPoolExecutor);


		//   销售属性
		CompletableFuture<Void> saleAttrValueEntityCompletableFuture = CompletableFuture.runAsync(() -> {
			Resp<List<SkuSaleAttrValueEntity>> saleAttrValueBySkuId = this.pmsClient.querySaleAttrValueBySkuId(skuId);
			List<SkuSaleAttrValueEntity> saleAttrValueEntityList = saleAttrValueBySkuId.getData();
			itemVo.setSaleAttrs(saleAttrValueEntityList);
		}, threadPoolExecutor);


		//
		CompletableFuture<Void> spuInfoDescCompletableFuture = skuInfoCompletableFuture.thenAcceptAsync(sku -> {
			Resp<SpuInfoDescEntity> spuInfoDescEntityResp = this.pmsClient.querySpuInfoDescBySpuId(((SkuInfoEntity) sku).getSpuId());
			SpuInfoDescEntity spuInfoDescEntity = spuInfoDescEntityResp.getData();
			itemVo.setImages(Arrays.asList(StrUtil.split(spuInfoDescEntity.getDecript(), ",")));
		}, threadPoolExecutor);


		// 根据 spuId 和 cateId 查询组及组下规格参数（带值）
		CompletableFuture<Void> itemGroupVoListCompletableFuture = skuInfoCompletableFuture.thenAcceptAsync(sku -> {
			Resp<List<ItemGroupVo>> listResp = this.pmsClient.queryItemGroupVoBySpuIdAndCateId(((SkuInfoEntity) sku).getCatalogId(), ((SkuInfoEntity) sku).getSpuId());
			List<ItemGroupVo> itemGroupVoList = listResp.getData();
			itemVo.setGroups(itemGroupVoList);
		}, threadPoolExecutor);

		CompletableFuture.allOf(spuInfoCompletableFuture, categoryCompletableFuture, brandEntityCompletableFuture,
			skuImageListCompletableFuture, saleVoCompletableFuture,
			wareSkuEntityCompletableFuture, saleAttrValueEntityCompletableFuture,
			spuInfoDescCompletableFuture, itemGroupVoListCompletableFuture).join();
		return itemVo;
	}

}


