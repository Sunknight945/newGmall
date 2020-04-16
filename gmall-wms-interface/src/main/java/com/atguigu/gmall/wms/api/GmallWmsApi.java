package com.atguigu.gmall.wms.api;

import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.wms.entity.WareSkuEntity;
import com.atguigu.gmall.wms.vo.SkuLockVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
public interface GmallWmsApi {

	/**
	 * 根据sku 查询它对应的库存
	 */
	@ApiOperation("查sku库存")
	@GetMapping("wms/waresku/{skuId}")
	public Resp<List<WareSkuEntity>> queryWareSkuEntityResp(@PathVariable("skuId") Long skuId);

	/**
	 * 锁sku的库存
	 *
	 * @param skuLockVos ff
	 * @return ff
	 */
	@PostMapping("wms/waresku/lockSkuWare")
	public Resp<Object> lockSkuWare(@RequestBody List<SkuLockVo> skuLockVos);
}
