package com.atguigu.gmall.auth;

import com.atguigu.core.utils.JwtUtils;
import com.atguigu.core.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

// import org.aspectj.lang.annotation.Before;

public class JwtTest {
	private static final String pubKeyPath = "C:\\tmp\\rsa\\rsa.pub";

	private static final String priKeyPath = "C:\\tmp\\rsa\\rsa.pri";

	private PublicKey publicKey;

	private PrivateKey privateKey;

	@Test
	public void testRsa() throws Exception {
		RsaUtils.generateKey(pubKeyPath, priKeyPath, "dfasdfgsfsa4564hasdfkj89safyashrwqkjth984yhakshf9py");
	}


	@Before
	public void testGetRsa() throws Exception {
		this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
		this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
	}

	@Test
	public void testGenerateToken() throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("id", "11");
		map.put("username", "liuyan");
		// 生成token
		String token = JwtUtils.generateToken(map, privateKey, 1);
		System.out.println("token = " + token);
	}

	@Test
	public void testParseToken() throws Exception {
		String token = " eyJhbGciOiJSUzI1NiJ9.eyJpZCI6IjExIiwidXNlcm5hbWUiOiJsaXV5YW4iLCJleHAiOjE1ODU4OTc5NDh9" +
			               ".bMMJsW0s3W-Rwe5VOIsQzuvHhKv63HIgib71cZj--DIXm6FeIVu8VlQbYiGazJVcCAUPFHsJMyWB0BuTGR6yFknQWPl9zEK1JyIKIdxXIzMpAtQcMpGN2slc3bqdhuKn9YRZVFeCIIH0ITKLaVM8YpCMrvqEAF3s5v-l3TF-SPY7cghWkdP7BDa6bJ8fftyT-uZlhD3-TfjMmkYbcamJKdN8gkQZ_2ba5M0A32X6F3ZoGCWzn6OxdL-h5yDxFBIsg3tOQ35q4afkx-plyI9oJromNcUuJ-H5ir-KGpLMMzSPHMNW3TeTExSmMDiNmsZN_SzTWHYEwGNB0EYCAX8vYw";
		// 解析token
		Map<String, Object> map = JwtUtils.getInfoFromToken(token, publicKey);
		System.out.println("id: " + map.get("id"));
		System.out.println("userName: " + map.get("username"));
	}
}
