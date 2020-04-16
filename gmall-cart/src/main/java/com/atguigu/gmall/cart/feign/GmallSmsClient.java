package com.atguigu.gmall.cart.feign;

import com.atguigu.gmall.sms.api.GmallSmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@FeignClient("sms-service")
public interface GmallSmsClient extends GmallSmsApi {
}
