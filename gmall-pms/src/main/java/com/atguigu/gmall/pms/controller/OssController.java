package com.atguigu.gmall.pms.controller;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.atguigu.core.bean.Resp;
import com.google.common.base.Charsets;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 * http://127.0.0.1:8888/pms/oss/policy?t=1584628876191
 */
@RestController
@ApiOperation("阿里云对象存储服务")
@RequestMapping("pms/oss")
public class OssController {

  @GetMapping("policy")
  public Resp<Object> getPolicyString() {
    String accessId = "LTAI4FfW4UaeZyTbVZeUEppb"; // 请填写您的AccessKeyId。
    String accessKey = "2GEMweTKOpFmAOPCYkta1HDqPiRGWV"; // 请填写您的AccessKeySecret。
    String endpoint = "oss-cn-beijing.aliyuncs.com"; // 请填写您的 endpoint。
    String bucket = "uiys"; // 请填写您的 bucketname 。
    String host = "https://" + bucket + "." + endpoint; // host的格式为 bucketname.endpoint
    // callbackUrl为 上传回调服务器的URL，请将下面的IP和Port配置为您自己的真实信息。
    String callbackUrl = "http://88.88.88.88:8888";
    // String dir = "user-dir-prefix/"; // 用户上传文件时指定的前缀。
    SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
    String dir = dateFormat.format(new Date()); // 用户上传文件时指定的前缀。

    OSSClient client = new OSSClient(endpoint, accessId, accessKey);
    try {
      long expireTime = 30;
      long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
      Date expiration = new Date(expireEndTime);
      PolicyConditions policyConds = new PolicyConditions();
      policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
      policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
      String postPolicy = client.generatePostPolicy(expiration, policyConds);
      byte[] binaryData = postPolicy.getBytes(Charsets.UTF_8);
      String encodedPolicy = BinaryUtil.toBase64String(binaryData);
      String postSignature = client.calculatePostSignature(postPolicy);
      Map<String, String> respMap = new LinkedHashMap<String, String>();
      respMap.put("accessid", accessId);
      respMap.put("policy", encodedPolicy);
      respMap.put("signature", postSignature);
      respMap.put("dir", dir);
      respMap.put("host", host);
      respMap.put("expire", String.valueOf(expireEndTime / 1000));
      System.out.println(respMap);
      return Resp.ok(respMap);
    } catch (Exception e) {
      // Assert.fail(e.getMessage());
      System.out.println(e.getMessage());
    }
    return Resp.ok(null);
  }
}


