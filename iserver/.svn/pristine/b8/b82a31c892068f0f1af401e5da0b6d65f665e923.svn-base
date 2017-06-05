package cn.ichazuo.commons.util;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @ClassName: CodeUtils 
 * @Description: (验证码序列号等帮助类) 
 * @author ZhaoXu
 * @date 2015年7月4日 下午2:01:31 
 * @version V1.0
 */
public class CodeUtils {
	private static final Random random = new Random();
	
	/**
	 * @Title: getCode 
	 * @Description: (获得指定位数的随机码) 
	 * @param size
	 * @return
	 * @author ZhaoXu
	 */
	public static String getCode(int size){
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<size;i++){
        	sb.append(random.nextInt(10));
        }
        return sb.toString();
	}
	
	/**
	 * @Title: getUUID 
	 * @Description: (生成随机UUID) 
	 * @return
	 * @author ZhaoXu
	 */
	public static String getUUID(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	/**
	 * @Title: getRandomInt 
	 * @Description: (获得Number以内的随机整数) 
	 * @param number 
	 * @return
	 * @author ZhaoXu
	 */
	public static Integer getRandomInt(int number){
		return random.nextInt(number);
	}
	
	/**
	 * @Title: getCourseOrderCode 
	 * @Description: (获得课程订单号) 
	 * @param cityName
	 * @return
	 * @author ZhaoXu
	 */
	public static String getCourseOrderCode(String cityName,Long courseId,Long userId){
		StringBuffer buffer = new StringBuffer(cityName);
		buffer.append(DateUtils.formatDate(new Date(), DateUtils.TIME_FORMAT_SHORT));
		buffer.append(courseId).append(userId).append(getCode(3));
		return buffer.toString();
	}
}
