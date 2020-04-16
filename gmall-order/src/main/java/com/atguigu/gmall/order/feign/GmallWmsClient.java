package com.atguigu.gmall.order.feign;

import com.atguigu.gmall.wms.api.GmallWmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@FeignClient("wms-service")
public interface GmallWmsClient extends GmallWmsApi {
}
