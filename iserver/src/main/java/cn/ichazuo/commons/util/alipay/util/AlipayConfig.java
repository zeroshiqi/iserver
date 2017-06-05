package cn.ichazuo.commons.util.alipay.util;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {
	
	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = "2088521076484223";
	
	// 收款支付宝账号，一般情况下收款账号就是签约账号
	public static String seller_email = "ichazuo_dev@ichazuo.cn";
	// 商户的私钥
	public static String key = "ukhcfch3s8ehxy5necwxfg1wgfuel2k6";

	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	

	// 调试用，创建TXT日志文件夹路径
//	public static String log_path = "D:\\";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
	
	// 签名方式 不需修改
	public static String sign_type = "MD5";
	
	// 商户appid
		public static String APPID = "2016101402170133";
		// 私钥 pkcs8格式的
		public static String RSA_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCCrkjqRzhuroXB5KT2rDuVjkPBr+zkM1LBFofn7IS5r6awGo5tGIQswOe3Z71ykQh2SCynmD1nzs5UJkNNf0o3ckeNqw1sGHJ3nCs6FUAtQSFPwvJawpR7AO0bTw8JJbXFR/NzGgh5sWvnp6I9babU86jNOtbkXdrXzmfDrhwWxvoKriOhEpWj4aWlAS6o2r5271GxpHr7wkKDUP71ObuoYlH+T1aIxpkPZ5vrGxS7J6bgekyGU9UJLNVbiTGKbzeGXPquRqGhxzNlUzTWQv6nKM9qxePkTKKj7UqRGrIn1gr2NC4DfAuTpwZySttJgV8hOR6Js4phoF/XTCqn+QtDAgMBAAECggEAIvb83WpG6B5b/X/uv21f1+kjqewnRvuQdcdQf7Zh4MjloZLJbtkzbEIE/aaNIt05VWSERidOrrq/q/Y85bxcIC+8g2Q0CCsWXXh0fGr5K+KURiIT9ELBtZOO3VKu5DSpyOoyciaSA/V7uJS6rRLso2e6T4chviJ6jmlUjB/GtjUOZqLgZj+hZJMkBahqRu+51hQXVHfd4WlghPGZmP1LSyoo9TRIAMRXfjd2GI6gn95RaVL0rrrz2Tiu9P3/hdEdPMlo+dFolP9Lc8MCTZBxmABCTM7n0ns5ZxJkOWOsOZk1R8N0dwCMQUlsXUBZ6hpPiN5fwApiKviPvUAkkBob8QKBgQDNG26TeHsSlA73P3/IMaGJs8nGsWvSGggRhxRn7F83TcV4ZSJpjFF2onLRa2HLGbyE8TeLCrCmut7E/yt5UavR+imxCfDa+jdES8qcACwuT2pmgDDSJsEI3W5f8xF93MZlodLl2WPnEyeYAsZSWvVCbSjWADtLlBUCptdXqJTlaQKBgQCjGztuVHokrIKfyRTXG/3vm6CQ4FGemYeXR0aVKEInYOJA7WkOuX/y5YD/suJPKA4DgAmR6H7kmw+IPbGG79v7MVMLdbpuzd9vg9boOCRnTvFIxH1STss2O4cADWqDxKrltpgGtXH0RJ2oPUGYJv/y5xq2flBXmiEdMDl3RCT5ywKBgQDGy2/BP2UJ/LcErWcMgg4viTJogk4etVxcoLAGifM/hwF+T4erBXVvpsPidlGyqJ8vuxw6h4HwF+8SwL9CNwZx3iXKkfgARcSLTWpzCTfJKu/VqSmohidSkEwKc2zgsLsahl4CqVFLBiW6D/ZonABbAbIyuMa/2icDCvJs49htAQKBgQCVYX1+wJPVPsGrgcg6coKRjiDyTt+xt779B+RW+zYLRqow+L59I9tRJGyOwtCN2otrHTqmjR6aVIFXL9mFUexRFI2QM1R54sc+JKsQp0p/60cEdCCq30sx8jbkD2V8JQS3uhFmI4uoQad6zA9lQGKtnodSmEEQ2jJ1vxMCbkDHHQKBgC1FiR0y6px4FSPxJWaD6ZKc3z7d2Z1rnzPtb/yNwoq7w8MKADY8ztI1oC73xx0lFERPcukTYUSQ2e1YO6eiYobMutr5+Y1cUqURsbgvknAw7YxgIi5d1LeFQvoZDbSyMPxphC3UqYatUd1Qlj2j4Y6M8SzsuGGPajIl0JQqr6Pe";
		// 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
		public static String notify_url = "http://www.chazuomba.com/iserver/app/aliPayNotify";
		// 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
		public static String return_url = "http://www.chazuomba.com/files/activity/buyBook/paySuccess.html";
		// 请求网关地址
		public static String URL = "https://openapi.alipay.com/gateway.do";
		// 编码
		public static String CHARSET = "UTF-8";
		// 返回格式
		public static String FORMAT = "json";
		// 支付宝公钥
		public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjF73JSVUWeUg/NxXMF7dKTLulHoyHOt7EJeKFU6XnQIW1XDLMlvzJbUSGvoOld/xBgYI2xsWjI2vkZH+tr+D+qmdg+tA+pGNVVFfsKhwITFw5CG6VvoXubXjgul6WEc5bOkMvYJA6InlWrq6bJtZV1zq/h1GMMJ6IqQyBbeyRdka8zCVLw717QP9Shd70T0+OQX+KgOOpDU3nMBbvsj1RclzTeft+LHNWJSzmyp+YkP0KRZTXDrFH+dvbYoAN+IVIGPij6l2rxxwDLchRx+7z8DJoA9/2GgoNUyCW7uhzbC9o9urHzrbaGBq2BmycfxCqDnImfdEZ8fhpKnnFlEwHQIDAQAB";
		// 日志记录目录
		public static String log_path = "/log";
		// RSA2
		public static String SIGNTYPE = "RSA2";

}
