package cn.ichazuo.commons.base;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.ichazuo.commons.Result;
import cn.ichazuo.commons.util.StringUtils;
import cn.ichazuo.commons.util.model.Params;
import cn.ichazuo.controller.app.UpAES;


/**
 * @ClassName: BaseController 
 * @Description: (基本Controller) 
 * @author ZhaoXu
 * @date 2015年6月28日 下午4:21:58 
 * @version V1.0
 */
public abstract class BaseController extends Base implements Result{
	public static final String css = "<link rel='stylesheet' href='https://res.wx.qq.com/mpres/htmledition/ueditor/themes/iframe.css' /><link rel='stylesheet' type='text/css' href='http://www.chazuomba.com/files/bg.css'>";
	public static final String appid = "wx1541ccc024a8e057"; // 服务号appid
	public static final String secret = "75d268f0c231302e9d70755116e16347"; // 服务号
																			// secret
	public static final String mchId = "1267340301"; // 商户ID
	public static final String apiKey = "1UHk2jFzPaX1CVFxv4BB2nCDFMNM7eD3"; // apiKey
																			// 生成签名使用
	
	public static final String appidNew = "wxa3069b403f0a23af"; // 服务号appid
	public static final String secretNew = "80bc66a37943bad3ae60aa33f2bec2fe"; // 服务号
																			// secret
	public static final String mchIdNew = "1401360402"; // 商户ID
	public static final String apiKeyNew = "5df41cd6bbd63cf58da9bab43c86610d"; // apiKey
	
	/**
	 * @Title: ok 
	 * @Description: (返回正确) 
	 * @return
	 */
	public JSONObject ok(){
		JSONObject obj = new JSONObject();
		obj.put("status", SUCCESS_STATUS);
		
		return obj;
	}
	
	/**
	 * @Title: ok 
	 * @Description: (返回正确,有消息) 
	 * @param msg 消息
	 * @return
	 */
	public JSONObject ok(String msg){
		JSONObject obj = new JSONObject();
		obj.put("status", SUCCESS_STATUS);
		if(msg != null){
			obj.put("msg", msg);
		}
		
		return obj;
	}
	
	/**
	 * @Title: ok 
	 * @Description: (返回正确,有数据) 
	 * @param data 数据
	 * @param msg 消息
	 * @return
	 */
	public JSONObject ok(String msg,Object data){
		JSONObject obj = new JSONObject();
		obj.put("status", SUCCESS_STATUS);
		if(msg != null){
			obj.put("msg", msg);
		}
		obj.put("data", data);
		
		return obj;
	}
	/**
	 * @Title: ok 
	 * @Description: (返回正确,有数据) 
	 * @param data 数据
	 * @param msg 消息
	 * @return
	 */
	public JSONObject ok1(String msg,Object data,String code){
		JSONObject obj = new JSONObject();
		obj.put("status", SUCCESS_STATUS);
		if(msg != null){
			obj.put("msg", msg);
		}
		if(code != null){
			obj.put("code", code);
		}
		obj.put("data", data);
		
		return obj;
	}
	/**
	 * @Title: ok 
	 * @Description: (返回正确,有数据) 
	 * @param data 数据
	 * @param msg 消息
	 * @return
	 */
	public JSONObject ok2(String msg,Object data,String code,Date date){
		JSONObject obj = new JSONObject();
		obj.put("status", SUCCESS_STATUS);
		if(msg != null){
			obj.put("msg", msg);
		}
		if(code != null){
			obj.put("code", code);
		}
		obj.put("data", data);
		obj.put("date", date);
		return obj;
	}
	/**
	 * @Title: ok 
	 * @Description: (返回正确,有数据) 
	 * @param data 数据
	 * @param msg 消息
	 * @return
	 */
	public JSONObject okNew(String msg,Object data){
		UpAES upAES = new UpAES();
		JSONObject obj = new JSONObject();
		obj.put("status", SUCCESS_STATUS);
		if(msg != null){
			obj.put("msg", msg);
		}
		obj.put("data",data);
//		JSONObject json = JSONObject.fromObject(data);
		String str =  JSON.toJSONString(data);
		obj.put("data", upAES.entry(str));
		return obj;
	}
	/**
	 * @Title: ok 
	 * @Description: (返回正确,有数据) 
	 * @param data 数据
	 * @param msg 消息
	 * @return
	 */
	public JSONObject okCounts(String msg,Object data,String counts){
		JSONObject obj = new JSONObject();
		obj.put("status", SUCCESS_STATUS);
		if(msg != null){
			obj.put("msg", msg);
		}
		if(counts != null){
			obj.put("counts", counts);
		}
		obj.put("data", data);
		
		return obj;
	}
	
	/**
	 * @Title: ok 
	 * @Description: (返回正确,有数据,带count数量) 
	 * @param msg 消息
	 * @param data 数据
	 * @param count 总数
	 * @return
	 */
	public JSONObject ok(String msg,Object data,Long count){
		JSONObject obj = new JSONObject();
		obj.put("status", SUCCESS_STATUS);
		if(msg != null){
			obj.put("msg", msg);
		}
		obj.put("data", data);
		obj.put("count", count);
		
		return obj;
	}
	
	/**
	 * @Title: ok 
	 * @Description: (返回正确,有数据,包含是否为最后一页) 
	 * @param msg 信息
	 * @param data 数据
	 * @param page 当前页
	 * @param count 总条数
	 * @return
	 */
	public JSONObject ok(String msg,Object data,Integer page,Integer count){
		JSONObject obj = new JSONObject();
		if(page == null || page <= 0){
		    page = 1;
		}
		obj.put("status", SUCCESS_STATUS);
		if(msg != null){
			obj.put("msg", msg);
		}
		obj.put("data", data);
		obj.put("page",page + 1);
		if(page * Params.PAGE_COUNT >= count){
			obj.put("isLast", LAST);
		}else{
			obj.put("isLast", NOT_LAST);
		}
		
		return obj;
	}
	
	/**
	 * @Title: ok 
	 * @Description: (返回正确,有数据,包含是否为最后一页) 
	 * @param msg 信息
	 * @param data 数据
	 * @param page 当前页
	 * @param count 总条数
	 * @return
	 */
	public JSONObject ok(String msg,Object data,Integer page,Long count){
		JSONObject obj = new JSONObject();
		if(page == null || page <= 0){
		    page = 1;
		}
		obj.put("status", SUCCESS_STATUS);
		if(msg != null){
			obj.put("msg", msg);
		}
		obj.put("data", data);
		obj.put("page",page + 1);
		if(page * Params.PAGE_COUNT >= count){
			obj.put("isLast", LAST);
		}else{
			obj.put("isLast", NOT_LAST);
		}
		
		return obj;
	}
	
	/**
	 * @Title: error 
	 * @Description: (返回错误,带消息) 
	 * @param msg 消息
	 * @return
	 */
	public JSONObject error(String msg){
		JSONObject obj = new JSONObject();
		obj.put("status", ERROR_STATUS);
		obj.put("msg", msg);
		
		return obj;
	}
	
	/**
	 * @Title: status 
	 * @Description: (返回状态,带消息) 
	 * @param status 状态值
	 * @param msg 消息
	 * @return
	 */
	public JSONObject status(Integer status,String msg){
		JSONObject obj = new JSONObject();
		obj.put("status", status);
		obj.put("msg", msg);
		
		return obj;
	}
	public JSONObject statusNew(Integer status,Object data){
		JSONObject obj = new JSONObject();
		obj.put("status", status);
		obj.put("data", data);
		return obj;
	}
	
	/**
	 * @Title: status 
	 * @Description: (返回状态,带数据) 
	 * @param status 状态值
	 * @param msg 消息
	 * @param data 数据
	 * @return
	 */
	public JSONObject status(Integer status,String msg,Object data){
		JSONObject obj = new JSONObject();
		obj.put("status", status);
		obj.put("msg", msg);
		obj.put("data", data);
		
		return obj;
	}
	/**
	 * @Title: updateException 
	 * @Description: (更新异常,乐观锁控制) 
	 * @return
	 */
	public JSONObject updateException(){
		JSONObject obj = new JSONObject();
		obj.put("status", ERROR_STATUS);
		obj.put("msg", UPDATE_ERROR);
		
		return obj;
	}
	
	/**
	 * @Title: warning 
	 * @Description: (返回警告) 
	 * @param msg
	 * @return
	 */
	public JSONObject warning(String msg){
		JSONObject obj = new JSONObject();
		obj.put("status", WARN_STATUS);
		obj.put("msg", msg);
		
		return obj;
	}
	
	/**
	 * @Title: notFound 
	 * @Description: (未找到) 
	 * @param msg 消息
	 * @return
	 */
	public JSONObject notFound(String msg){
		JSONObject obj = new JSONObject();
		obj.put("status", NOTFOUND_STATUS);
		obj.put("msg", msg);
		
		return obj;
	}
	
	/**
	 * @Title: notFound 
	 * @Description: (未找到) 
	 * @param msg 消息
	 * @param data 数据
	 * @return
	 */
	public JSONObject notFound(String msg,Object data){
		JSONObject obj = new JSONObject();
		obj.put("status", NOTFOUND_STATUS);
		if(msg != null){
			obj.put("msg", msg);
		}
		obj.put("data", data);
		
		return obj;
	}
	
	/**
	 * @Title: notLogin 
	 * @Description: (未登录) 
	 * @param msg
	 * @param data
	 * @return
	 */
	public JSONObject notLogin(String msg,Object data){
		JSONObject obj = new JSONObject();
		obj.put("status", NOTLOGIN_STATUS);
		if(msg != null){
			obj.put("msg", msg);
		}
		obj.put("data", data);
		
		return obj;
	}
	
	/**
	 * @Title: notLogin 
	 * @Description: (未登录) 
	 * @param msg
	 * @return
	 */
	public JSONObject notLogin(String msg){
		JSONObject obj = new JSONObject();
		obj.put("status", NOTLOGIN_STATUS);
		if(msg != null){
			obj.put("msg", msg);
		}
		return obj;
	}
	
	/**
	 * @Title: writeJSONString 
	 * @Description: (输出json字符串) 
	 * @param json
	 */
	protected void writeJSONString(JSON json){
		StringUtils.writeJSONString(json);
	}
	
	protected static Map<String, String> parseXmlToMap(String xml) {
		Map<String, String> retMap = new HashMap<>();
		try {
//			System.out.println(xml);
			// 通过输入源构造一个Document
			Document doc = DocumentHelper.parseText(xml);
			Element root = doc.getRootElement();// 指向根节点
			@SuppressWarnings("unchecked")
			List<Element> es = root.elements();
			if (es != null && es.size() != 0) {
				for (Element element : es) {
					retMap.put(element.getName(), element.getTextTrim());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retMap;
	}
	 /**
     * 根据stream获取保利威视直播房间直播状态
     */
	public String getLivingStatus(String stream){
		try {
			PrintWriter out = null;
	        String result = "";
	        String param="stream="+stream;
	        try {
	        	URL U = new URL("http://api.live.polyv.net/live_status/query?stream="+stream);  
	        	URLConnection connection = U.openConnection();  
	        	   connection.connect();  
	        	   BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));  
	        	   String line;  
	        	   while ((line = in.readLine())!= null)  
	        	   {  
	        	    result += line;  
	        	   }  
	        	   in.close(); 
	        	   if("end".equals(result)){
	        		   result="已结束";
	        	   }else if("live".equals(result)){
	        		   result="直播中";
	        	   }else{
	        		   result="暂无直播状态";
	        	   }
	            return result;
	        } catch (Exception e) {
	            System.out.println("发送 POST 请求出现异常！"+e);
	            e.printStackTrace();
	        }
		}catch(Exception e){
			e.printStackTrace();
			return "暂无信息";
		}
		return "暂无信息";
	}
}
