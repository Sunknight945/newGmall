package com.atguigu.gmall.wms.vo;

import lombok.Data;

/**
 *
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Data
public class SkuLockVo {

	private String skuName;
	private Long skuId;
	private Integer count;
	private Long skuWareId;
	/**
	 * 锁定状态
	 */
	private Boolean lock;
	private String orderToken;

}


