package cn.ichazuo.commons.util.im;

import org.glassfish.jersey.client.JerseyWebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * ClassName: EasemobIMUsers 
 * Description: (环信IM 用户注册) 
 * @author ZhaoXu
 * @date 2015年7月7日 上午10:23:06 
 * @version V1.0
 */
public class EasemobIMUsers {
	
	private static final Logger logger = LoggerFactory.getLogger(EasemobIMUsers.class);
	private static final JsonNodeFactory factory = new JsonNodeFactory(false);

	/**
	 * 注册IM用户[单个]
	 * 
	 * 给指定AppKey创建一个新的用户
	 * 
	 * @return
	 */
	public static ObjectNode createNewIMUserSingle(ObjectNode dataNode, String appKey, String appClientId,String appClientSecret) {

		ObjectNode objectNode = factory.objectNode();

		// check appKey format
		if (!JerseyUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", appKey)) {
			logger.error("Bad format of Appkey: " + appKey);

			objectNode.put("message", "Bad format of Appkey");

			return objectNode;
		}

		objectNode.removeAll();

		// check properties that must be provided
		if (null != dataNode && !dataNode.has("username")) {
			logger.error("Property that named username must be provided .");

			objectNode.put("message", "Property that named username must be provided .");

			return objectNode;
		}
		if (null != dataNode && !dataNode.has("password")) {
			logger.error("Property that named password must be provided .");

			objectNode.put("message", "Property that named password must be provided .");

			return objectNode;
		}
		try {
			JerseyWebTarget webTarget = EndPoints.USERS_TARGET.resolveTemplate("org_name", appKey.split("#")[0])
					.resolveTemplate("app_name", appKey.split("#")[1]);
			Credential credential = new ClientSecretCredential(appClientId, appClientSecret, appKey);
			objectNode = JerseyUtils.sendRequest(webTarget, dataNode, credential, HTTPMethod.METHOD_POST, null);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}
}
