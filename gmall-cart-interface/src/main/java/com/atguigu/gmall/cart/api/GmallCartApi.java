package com.atguigu.gmall.cart.api;

import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.cart.pojo.Cart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
public interface GmallCartApi {


	@GetMapping("cart")
	public Resp<List<Cart>> queryCarts();

	/**
	 * order模块的远程调用CartApi带memberId参数查询用户的购物车列表
	 */
	@GetMapping("cart/memberId/{memberId}")
	public Resp<List<Cart>> queryCartsByFeignMemberId(@PathVariable("memberId") Long memberId);
}
