package cn.ichazuo.commons.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;

/**
 * @ClassName: StringUtils 
 * @Description: (字符串帮助类) 
 * @author ZhaoXu
 * @date 2015年7月15日 下午12:50:56 
 * @version V1.0
 */
public class StringUtils {
	
	/**
	 * Title: isNullOrEmpty 
	 * Description: (验证字符串是否为空或null) 
	 * @param str
	 * @return
	 * @author ZhaoXu
	 */
	public static boolean isNullOrEmpty(String str){
		if(str == null || str.trim().equals("")){
			return true;
		}
		return false;
	}
	
	/**
	 * Title: isNotNull 
	 * Description: (是否全部不为null) 
	 * @param args
	 * @return
	 * @author ZhaoXu
	 */
	public static boolean isNotNull(String...args){
		if(args == null){
			return false;
		}
		for(int i=0; i < args.length; i++){
			if(args[i] == null){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @Title: subString 
	 * @Description: (截取字符串,添加...) 
	 * @param str 
	 * @param length
	 * @return
	 */
	public static String subString(String str,Integer length){
		if(isNullOrEmpty(str)){
			return "";
		}
		if(NumberUtils.isNullOrZero(length) || length < 0){
			length = 30;
		}
		if(str.trim().length() > length){
			return str.trim().substring(0,length) + "...";
		}
		return str.trim();
	}
	
	/**
	 * @Title: subString 
	 * @Description: (截取字符串,默认截取30个字符,填充...) 
	 * @param str
	 * @return
	 */
	public static String subString(String str){
		return subString(str, 30);
	}
	
	/**
	 * @Title: removeEndChar 
	 * @Description: (如果末位字符为value,移除该字符) 
	 * @param str
	 * @param value
	 * @return
	 */
	public static String removeEndChar(String str, char value) {
		if(isNullOrEmpty(str)){
			return "";
		}
		if (str.endsWith(String.valueOf(value))) {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}
	
	/**
	 * @Title: writeJSONString 
	 * @Description: (打印json串) 
	 * @param json
	 */
	public static void writeJSONString(JSON json){
		System.out.println(json.toJSONString());
	}
	/**
	 * 替换字符串
	 * 
	 * @param str 字符串
	 * @param oldChar 需要替换的值
	 * @param newChar 替换成的值
	 * @return
	 */
	public static String replacestr(String str) {
		String req = "";
		if(str==null || "".equals(str)){
			return req;
		} else {
//			Pattern pattern=Pattern.compile("(\r\n|\r|\n|\n\r)");
			//正则表达式的匹配一定要是这样，单个替换\r|\n的时候会错误
//			Matcher matcher=pattern.matcher(str);
//			req = matcher.replaceAll("<br>");
			req = str.replace("	","");
			req = req.replace(",", "，");
			req = req.replace("\"", "”");
			req = req.replace("'", "’");
			req = req.replace("\\", "/");
		}
		return req;
	}
	
	/**
	 * 替换字符串(替换回车)
	 * 
	 * @param str 字符串
	 * @param oldChar 需要替换的值
	 * @param newChar 替换成的值
	 * @return
	 */
	public static String replacestrs(String str) {
		String req = "";
		if(str==null || "".equals(str)){
			return req;
		} else {
			Pattern pattern=Pattern.compile("(\r\n|\r|\n|\n\r)");
//			正则表达式的匹配一定要是这样，单个替换\r|\n的时候会错误
			Matcher matcher=pattern.matcher(str);
			req = matcher.replaceAll("");
			req = req.replace("	","");
			req = req.replace(",", "，");
			req = req.replace("\"", "”");
			req = req.replace("'", "’");
			req = req.replace("\\", "/");
		}
		return req;
	}
}
