package com.atguigu.gmall.auth.config;

import com.atguigu.core.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@ConfigurationProperties(prefix = "gmall.jwt")
@Data
@Slf4j
@Component
public class JwtProperties {
	private String secret; // 密钥

	private String pubKeyPath;// 公钥

	private String priKeyPath;// 私钥

	private int expire;// token过期时间

	private String cookieName; // cookie名称

	private PublicKey publicKey; // 公钥

	private PrivateKey privateKey; // 私钥

	//是不是发现一个类似于bug 的存在就是在起初的路径没有 将公钥和私钥准备好的话 那么就获取公钥和私钥就成了一个笑话
	// 甚至会发生错误. 这应该是设计的时候几里须要避免的 刚好 我们在这里就可以利用一个注解 来解决这要能的情况.
	//这个注解就表示该类执行构造方法之后执行 init() 方法  刚好这个时候可以检查有没有公钥和私钥 如果没有那么直接生成就好了.
	@PostConstruct
	public void init() {
		try {
			File pub = new File(pubKeyPath);
			File pri = new File(priKeyPath);
			if (!pub.exists() || !pri.exists()) {
				// 生成公钥和私钥
				RsaUtils.generateKey(pubKeyPath, priKeyPath, secret);
			}
			// 获取公钥和私钥
			this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
			this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
		} catch (Exception e) {
			log.error("初始化公钥私钥失败!", e);
			throw new RuntimeException();
		}
	}
}


