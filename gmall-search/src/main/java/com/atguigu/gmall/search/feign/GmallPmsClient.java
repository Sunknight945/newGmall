package com.atguigu.gmall.search.feign;

import com.atguigu.gmall.pms.api.GmallPmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@FeignClient("pms-service")
public interface GmallPmsClient extends GmallPmsApi {

}


