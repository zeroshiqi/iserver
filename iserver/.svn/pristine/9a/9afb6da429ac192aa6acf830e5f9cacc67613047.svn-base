/**
 * @author Glan.duanyj
 * @date 2014-05-12
 * @project rest_demo
 */
package cn.ichazuo.commons.util.sms;
import java.io.ByteArrayInputStream;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import cn.ichazuo.commons.component.ConfigInfo;
import cn.ichazuo.commons.util.DateUtils;
import cn.ichazuo.commons.util.EncryptUtils;

/**
 * ClassName: JsonReqClient 
 * Description: (短信发送) 
 * @author ZhaoXu
 * @date 2015年7月4日 下午1:43:33 
 * @version V1.0
 */
@Component("jsonReqClient")
public class JsonReqClient {
	private static Logger logger=Logger.getLogger(JsonReqClient.class);
	
	@Autowired
	private ConfigInfo configInfo;
	
	/**
	 * Title: templateSMS 
	 * Description: (发送短信) 
	 * @param to 电话号码
	 * @param param 参数,验证码
	 * @return
	 * @author ZhaoXu
	 */
	public String templateSMS(String to, String param) {
		String result = "";
		HttpClient httpclient= HttpClients.createDefault();
		
		try {
			//MD5加密
			EncryptUtils encryptUtil = new EncryptUtils();
			// 构造请求URL内容
			String timestamp = DateUtils.formatDate(new Date(), DateUtils.TIME_FORMAT_SHORT);
			String signature =getSignature(configInfo.getSmsAccountSid(),configInfo.getSmsToken(),timestamp,encryptUtil);
			String url = getStringBuffer(configInfo.getSmsRestUrl()).append("/").append(configInfo.getSmsVersion())
					.append("/Accounts/").append(configInfo.getSmsAccountSid())
					.append("/Messages/templateSMS")
					.append("?sig=").append(signature).toString();
			TemplateSMS templateSMS=new TemplateSMS();
			templateSMS.setAppId(configInfo.getSmsAppId());
			templateSMS.setTemplateId(configInfo.getSmsTemplateId());
			templateSMS.setTo(to);
			templateSMS.setParam(param);
			Gson gson = new Gson();
			String body = gson.toJson(templateSMS);
			body="{\"templateSMS\":"+body+"}";
			logger.info(body);
			HttpResponse response=post("application/json",configInfo.getSmsAccountSid(), configInfo.getSmsToken(), timestamp, url, httpclient, encryptUtil, body);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
			}
			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			// 关闭连接
			HttpClientUtils.closeQuietly(httpclient);
		}
		return result;
	}
	//加密
	private String getSignature(String accountSid, String authToken,String timestamp,EncryptUtils encryptUtil) throws Exception{
		String sig = accountSid + authToken + timestamp;
		String signature = encryptUtil.md5Digest(sig);
		return signature;
	}
	//拼接server地址
	private StringBuffer getStringBuffer(String url) {
		StringBuffer sb = new StringBuffer("https://");
		sb.append(url);
		return sb;
	}
	
	//发送请求
	private HttpResponse post(String cType,String accountSid,String authToken,String timestamp,String url,HttpClient httpclient,EncryptUtils encryptUtil,String body) throws Exception{
		HttpPost httppost = new HttpPost(url);
		httppost.setHeader("Accept", cType);
		httppost.setHeader("Content-Type", cType+";charset=utf-8");
		String src = accountSid + ":" + timestamp;
		String auth = encryptUtil.base64Encoder(src);
		httppost.setHeader("Authorization", auth);
		BasicHttpEntity requestBody = new BasicHttpEntity();
        requestBody.setContent(new ByteArrayInputStream(body.getBytes("UTF-8")));
        requestBody.setContentLength(body.getBytes("UTF-8").length);
        httppost.setEntity(requestBody);
        // 执行客户端请求
		HttpResponse response = httpclient.execute(httppost);
		return response;
	}
	
}
