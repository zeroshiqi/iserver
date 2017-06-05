package cn.ichazuo.commons.util.kit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.http.HttpResponse;


/*
 * 这是个例子
 */
public class KDTApiTest {
	private static final String APP_ID = "564a841c78e2ca9adf"; //这里换成你的app_id
	private static final String APP_SECRET = "3a6ae82098884fbdba4431f7f4122bb6"; //这里换成你的app_secret
	
	public static void main(String[] args){
		sendGet();
	}
	
	/*
	 * 测试获取单个商品信息
	 */
	private static void sendGet(){
		String method = "kdt.trades.sold.get";
		HashMap<String, String> params = new HashMap<String, String>();
//		params.put("tid", "E20150819140336133996851");
		
		KdtApiClient kdtApiClient;
		HttpResponse response;
		
		try {
			kdtApiClient = new KdtApiClient(APP_ID, APP_SECRET);
			response = kdtApiClient.get(method, params);
			System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				result.append(line);
			}

			System.out.println(result.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 测试获取添加商品
	 */
//	private static void sendPost(){
//		String method = "kdt.item.add";
//		HashMap<String, String> params = new HashMap<String, String>();
//		params.put("price", "999.01");
//		params.put("title", "测试商品");
//		params.put("desc", "这是一个号商铺");
//		params.put("is_virtual", "0");
//		params.put("post_fee", "10.01");
//		params.put("sku_properties", "");
//		params.put("sku_quantities", "");
//		params.put("sku_prices", "");
//		params.put("sku_outer_ids", "");
//		String fileKey = "images[]";
//		List<String> filePaths = new ArrayList<String>();
//		filePaths.add("/Users/xuexiaozhe/Desktop/1.png");
//		filePaths.add("/Users/xuexiaozhe/Desktop/2.png");
//		
//		KdtApiClient kdtApiClient;
//		HttpResponse response;
//		
//		try {
//			kdtApiClient = new KdtApiClient(APP_ID, APP_SECRET);
//			response = kdtApiClient.post(method, params, filePaths, fileKey);
//			System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
//			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//			StringBuffer result = new StringBuffer();
//			String line = "";
//			while ((line = bufferedReader.readLine()) != null) {
//				result.append(line);
//			}
//
//			System.out.println(result.toString());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
