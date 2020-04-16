package com.atguigu.gmall.order.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "alipay")
@Component
@Data
public class AlipayTemplate {

	//在支付宝创建的应用的id
	private String app_id = "2016101900723545";

	// 商户私钥，您的PKCS8格式RSA2私钥
	private String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCKfsQDeHuh6djlty7EyO2UQVOyP6oFuWQDZNpXX/An5S0ri+hblZAykN1Akvb8L6KwvtqXX3H8CDoG1AW5Fyi+OktlIfIX4YJYGadhrBSoNnEmeSpaUUy0eMrao44C7BTOKiIJ7SzhMqU/wptydmzILbhgG6sRAVmAKLelWz0fvr9u6364nc1Nu/0N5XLVmyYMznZRcqvV9+d+g3+TJjVCN4RpPyrDnmp1VBcNAQboJqjg5Yz3drmvrWuxOAzEO4vp4r9MImwDcQM31CA1yzWk6NWJtohPOEf9TP8dXbLnVLKIU+iQIzQ1XcnMMDYtd9H4OoxhvSp9vEzgB5H6K6HTAgMBAAECggEAOccq5TvFcEYLy8IBS+17WCds0GdI+jxeoh/YCy2mFi7BCnLCzDp4PvA6ra7iRVFmMRBeR1p1Riz0cFbR0Gb+A2Z0f8uKoTXeV/zT3Kaas5aK2/8ekTFrq7rc/hvPqh5ti5j+PF/rIkQrto7spPMM7PJOAiJo2p5ShWn3O4/1zyD5gr5mfTdiIZADeVxbdgkafPDZf+WyPvZDZiWauQqNxT53wW08AqI7XaijBis276HPF0ZkLI9aMQ6jW5Mgdu7IsWp362hHOGCKqFr6MwMQzV2YLhybJ9VBgVMwLspeWMvaphNzleyZPNBe50PFjF+iCc9c6KJAq8XssTCQB4KFEQKBgQDVvX/55FHSINcEOO0z2TIxxiVIU1MMqAJR7+pbHUgFCMEsHgvD7nV+TBgfRoyzgJtVO40vDVfbFqHT/msUKE8DaGxRDIkfXUO20lgae+JKRebMCH8QZXJ++d8CMHixuRqNPdM01vs18E3vVRUJGw/2O+R03LBQo82SR5dgtwiYyQKBgQCl4LlObrnHzMLEjbLKkb0iUu7RuG5iSMe6pnly+XJfYStka40DVxm3dNxrSooJw52A3DDBlifVG+qtTCNAvim3T4SE6JptLiIb6iMAP7LSB7rHIa+H05i8TVO89LxbiL+6BsvavuTnec8EA2hldxsCrry0xAtjMN1Re5jdPs5PuwKBgA5wJZPwy2IhW8TqP4OwGUbpdp1DG6oQM0/rn+4VtUa5x6q+QNc0ZXVpmHuMXXbGqjeAtVHHWfbvuXDDj+IlsuitBhwXd6/JsghIfzESyz9vJ18C9+pYn/pZ0KmqMoz2zwdZdMnt97w2Bl2zZp9d158vfogyXCqmVf2anmwnEQqZAoGBAKO5903qTqdefVQihLobNbLeZulMAHA0ZCfgT3njqzdnorvgyHPmkfSZfULNdQiEMz9QlzNM8Y4QmwdoYldyG5za+PNy35IqefJRnTCOrwNOK9aHckk/2r7qGGAmtJAonw2PzcDgsPGlMmdEsdOMx3VTYeGOq6rBPYJC27RtXJgnAoGANzG+/eR/uYH1vtDhDmL8S7xgQVuoLX2gD4M0f9oMx9dZj396eiJD8u8s7Xp5DzfOqdYfkEGV8XtPs0HA8KSrVkPuw7Ukyb77MirGDDFdVDHAbpLkJRD9uldqyGwNaJeSkjUBha/rNj7fq5CLFZWXMWt7ZL5HGt/pb+tOKkxX1p0=";
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
	private String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAin7EA3h7oenY5bcuxMjtlEFTsj+qBblkA2TaV1/wJ+UtK4voW5WQMpDdQJL2/C+isL7al19x/Ag6BtQFuRcovjpLZSHyF+GCWBmnYawUqDZxJnkqWlFMtHjK2qOOAuwUzioiCe0s4TKlP8KbcnZsyC24YBurEQFZgCi3pVs9H76/but+uJ3NTbv9DeVy1ZsmDM52UXKr1ffnfoN/kyY1QjeEaT8qw55qdVQXDQEG6Cao4OWM93a5r61rsTgMxDuL6eK/TCJsA3EDN9QgNcs1pOjVibaITzhH/Uz/HV2y51SyiFPokCM0NV3JzDA2LXfR+DqMYb0qfbxM4AeR+iuh0wIDAQAB";  // 服务器[异步通知]页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	// 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
	private String notify_url;

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	//同步通知，支付成功，一般跳转到成功页
	private String return_url;

	// 签名方式
	private String sign_type = "RSA2";

	// 字符编码格式
	private String charset = "utf-8";

	// 支付宝网关； https://openapi.alipaydev.com/gateway.do
	private String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

	public String pay(PayVo vo) throws AlipayApiException {

		//AlipayClient alipayClient = new DefaultAlipayClient(AlipayTemplate.gatewayUrl, AlipayTemplate.app_id, AlipayTemplate.merchant_private_key, "json", AlipayTemplate.charset, AlipayTemplate.alipay_public_key, AlipayTemplate.sign_type);
		//1、根据支付宝的配置生成一个支付客户端
		AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl,
			app_id, merchant_private_key, "json",
			charset, alipay_public_key, sign_type);

		//2、创建一个支付请求 //设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(return_url);
		alipayRequest.setNotifyUrl(notify_url);

		//商户订单号，商户网站订单系统中唯一订单号，必填
		String out_trade_no = vo.getOut_trade_no();
		//付款金额，必填
		String total_amount = vo.getTotal_amount();
		//订单名称，必填
		String subject = vo.getSubject();
		//商品描述，可空
		String body = vo.getBody();

		alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
			                            + "\"total_amount\":\"" + total_amount + "\","
			                            + "\"subject\":\"" + subject + "\","
			                            + "\"body\":\"" + body + "\","
			                            + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

		String result = alipayClient.pageExecute(alipayRequest).getBody();

		//会收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
		System.out.println("支付宝的响应：" + result);

		return result;

	}
}
