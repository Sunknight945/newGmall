package com.atguigu.gmall.cart.service;

import com.atguigu.gmall.cart.pojo.Cart;

import java.util.List;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
public interface CartService {
	/**
	 * 添加购物车
	 * @param cart
	 */
	void addCart(Cart cart);

		/**
	 * 查询获取 用户 的购物车列表
	 */
	List<Cart> queryCarts();

	/**
	 * 更新购物中某个商品的数量
	 */
	void update(Cart cart);

	/**
	 * 删除购物车
	 * @param skuIdList
	 */
	void deleteCart(List<Long> skuIdList);

	/**
	 * order模块的远程调用CartApi带memberId参数查询用户的购物车列表
	 */
	List<Cart> queryCartsByFeignMemberId(Long memberId);
}
