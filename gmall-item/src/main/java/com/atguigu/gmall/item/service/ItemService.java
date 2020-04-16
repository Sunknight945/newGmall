package com.atguigu.gmall.item.service;

import com.atguigu.gmall.item.vo.ItemVo;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
public interface ItemService {
	/**
	 * 根据skuId 查询组装 item 的数据库的信息
	 * @param skuId
	 * @return
	 */
	ItemVo queryItemVo(Long skuId);
}
