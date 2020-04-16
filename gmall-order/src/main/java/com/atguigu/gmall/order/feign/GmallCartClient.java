package com.atguigu.gmall.order.feign;

import com.atguigu.gmall.cart.api.GmallCartApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@FeignClient("cart-service")
public interface GmallCartClient extends GmallCartApi {
}
