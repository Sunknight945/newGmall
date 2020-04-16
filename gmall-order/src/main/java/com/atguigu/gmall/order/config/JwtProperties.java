package com.atguigu.gmall.order.config;

import com.atguigu.core.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@ConfigurationProperties(prefix = "gmall.jwt")
@Component
@Slf4j
@Data
public class JwtProperties {


	/**
	 * cookie名称
	 */
	private String cookieName;
	/**
	 * 公钥 路径
	 */
	private String pubKeyPath;

	private PublicKey publicKey;
 


	@PostConstruct
	public void init() {
		try {
			this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
		} catch (Exception e) {
			log.error("初始化公钥私钥失败");
			e.printStackTrace();
		}

	}

}


