package com.atguigu.gmall.cart.config;

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
@Data
@Slf4j
@Component
public class JwtProperties {
	/**
	 * cookie名称
	 */
	private String cookieName;
	/**
	 * 公钥
	 */
	private String pubKeyPath;
	/**
	 * 公钥
	 */
	private PublicKey publicKey;
	/**
	 * 游客id
	 */
	private String userKey;

	@PostConstruct
	public void init() {
		try {
			// 获取公钥和私钥
			this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
		} catch (Exception e) {
			log.error("初始化公钥私钥失败!", e);
			throw new RuntimeException();
		}
	}
}


