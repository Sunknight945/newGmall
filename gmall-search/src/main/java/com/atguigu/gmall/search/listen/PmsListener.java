package com.atguigu.gmall.search.listen;

import cn.hutool.core.collection.CollUtil;
import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.search.feign.GmallPmsClient;
import com.atguigu.gmall.search.feign.GmallWmsClient;
import com.atguigu.gmall.search.pojo.Goods;
import com.atguigu.gmall.search.pojo.SearchAttr;
import com.atguigu.gmall.search.repository.GoodsRepository;
import com.atguigu.gmall.wms.entity.WareSkuEntity;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Component
public class PmsListener {

	@Autowired
	private GmallPmsClient pmsClient;
	@Autowired
	private GmallWmsClient wmsClient;
	@Autowired
	private GoodsRepository goodsRepository;

	@RabbitListener(bindings = @QueueBinding(
		value = @Queue(value = "gmall-search-queue", durable = "true"),
		exchange = @Exchange(value = "GMALL-PMS-EXCHANGES", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
		key = {"item.update", "item.insert"}
	))
	public void listener(Long spuId) {
		Resp<SpuInfoEntity> spuInfoEntityResp = this.pmsClient.querySpuBySpuId(spuId);
		SpuInfoEntity spuInfoEntity = spuInfoEntityResp.getData();
		if (spuInfoEntity != null) {
			Resp<List<SkuInfoEntity>> skuPageBySpuId = this.pmsClient.querySkuPageBySpuId(spuId);
			List<SkuInfoEntity> skuInfoEntityList = skuPageBySpuId.getData();
			if (CollUtil.isNotEmpty(skuInfoEntityList)) {
				//把sku转化成goods对象
				List<Goods> goodsList = skuInfoEntityList.stream().map(skuInfoEntity -> {
					Goods good = new Goods();
					good.setSkuId(skuInfoEntity.getSkuId());
					good.setPic(skuInfoEntity.getSkuDefaultImg());
					good.setTitle(skuInfoEntity.getSkuTitle());
					good.setPrice(skuInfoEntity.getPrice().doubleValue());
					//销量里面
					good.setSale(0L);
					//wms里面
					Resp<List<WareSkuEntity>> wareSkuEntityResp = this.wmsClient.queryWareSkuEntityResp(skuInfoEntity.getSkuId());
					List<WareSkuEntity> wareSkuEntityList = wareSkuEntityResp.getData();
					if (CollUtil.isNotEmpty(wareSkuEntityList)) {
						boolean flag = wareSkuEntityList.stream().anyMatch(wareSkuEntity -> wareSkuEntity.getStock() > 0);
						good.setStore(flag);
					}
					good.setCreateTime(spuInfoEntity.getCreateTime());
					//接口里面
					Resp<BrandEntity> brandEntityResp = this.pmsClient.queryBrandById(skuInfoEntity.getBrandId());
					BrandEntity brandEntity = brandEntityResp.getData();
					if (brandEntity != null) {
						good.setBrandId(skuInfoEntity.getBrandId());
						good.setBrandName(brandEntity.getName());
					}
					//接口里面
					Resp<CategoryEntity> categoryEntityResp = this.pmsClient.queryCategoryById(skuInfoEntity.getCatalogId());
					CategoryEntity categoryEntity = categoryEntityResp.getData();
					if (categoryEntity != null) {
						good.setCategoryId(skuInfoEntity.getCatalogId());
						good.setCategoryName(categoryEntity.getName());
					}
					//接口里面
					Resp<List<ProductAttrValueEntity>> searchAttrValueListBySpuId = this.pmsClient.querySearchAttrValueListBySpuId(skuInfoEntity.getSpuId());
					List<ProductAttrValueEntity> searchAttrValueListBySpuIdData = searchAttrValueListBySpuId.getData();
					if (CollUtil.isNotEmpty(searchAttrValueListBySpuIdData)) {
						List<SearchAttr> searchAttrList = searchAttrValueListBySpuIdData.stream().map(searchAttrValue -> {
							SearchAttr searchAttr = new SearchAttr();
							searchAttr.setAttrId(searchAttrValue.getAttrId());
							searchAttr.setAttrName(searchAttrValue.getAttrName());
							searchAttr.setAttrValue(searchAttrValue.getAttrValue());
							return searchAttr;
						}).collect(Collectors.toList());
						good.setAttrs(searchAttrList);
					}
					return good;
				}).collect(Collectors.toList());
				//导入索引库
				this.goodsRepository.saveAll(goodsList);
			}
		}
	}

}




