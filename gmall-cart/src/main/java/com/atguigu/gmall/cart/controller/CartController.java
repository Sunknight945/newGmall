package com.atguigu.gmall.cart.controller;

import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.cart.interceptors.LoginInterceptor;
import com.atguigu.gmall.cart.pojo.Cart;
import com.atguigu.gmall.cart.pojo.UserInfo;
import com.atguigu.gmall.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@RestController
@RequestMapping("cart")
public class CartController {
	@Autowired
	private CartService cartService;

	/**
	 * 删除购物车
	 */
	@DeleteMapping("deleteCart")
	public Resp<Object> deleteCart(List<Long> skuIdList){
		this.cartService.deleteCart(skuIdList);
		return Resp.ok(null);
	}

	/**
	 * 更新购物中某个商品的数量
	 */
	@PostMapping("update")
	public Resp<Object> update(@RequestBody Cart cart) {
		this.cartService.update(cart);
		return Resp.ok(null);
	}

	/**
	 * 添加购物车
	 *
	 * @param cart
	 * @return
	 */
	@PostMapping
	public Resp<Object> addCart(@RequestBody Cart cart) {
		this.cartService.addCart(cart);
		return Resp.ok(null);
	}

	/**
	 * 查询获取 用户 的购物车列表
	 */
	@GetMapping
	public Resp<List<Cart>> queryCarts() {
		List<Cart> cartList = this.cartService.queryCarts();
		return Resp.ok(cartList);
	}

	/**
	 * order模块的远程调用CartApi带memberId参数查询用户的购物车列表
	 */
	@GetMapping("memberId/{memberId}")
	public Resp<List<Cart>> queryCartsByFeignMemberId(@PathVariable("memberId")Long memberId){
		List<Cart> cartList =	this.cartService.queryCartsByFeignMemberId(memberId);
		return Resp.ok(cartList);
	}


	@GetMapping("test")
	public String getTestStr() {
		UserInfo userInfo = LoginInterceptor.getUserInfo();
		return "hello cart!";
	}

}


