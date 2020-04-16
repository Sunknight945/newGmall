package com.atguigu.gmall.order.feign;

import com.atguigu.gmall.ums.api.GmallUmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 *
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@FeignClient("ums-service")
public interface GmallUmsClient extends GmallUmsApi {
}
