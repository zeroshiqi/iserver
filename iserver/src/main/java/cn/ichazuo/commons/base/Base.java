package cn.ichazuo.commons.base;

import cn.ichazuo.commons.component.RedisClient;

/**
 * @ClassName: Base 
 * @Description: (Base抽象类) 
 * @author ZhaoXu
 * @date 2015年7月15日 下午12:46:07 
 * @version V1.0
 */
public abstract class Base {
	
	/**
	 * @Title: deleteCaches
	 * @Description: (删除缓存) 
	 * @param redis
	 * @param keys
	 * @author ZhaoXu
	 */
	protected void deleteCache(RedisClient redis,String...keys){
		for(String key : keys){
			if(redis.isExist(key)){
				redis.delete(key);
				System.out.println("deleteKey-------------->"+key);
			}
		}
	}
	
	/**
	 * Title: fuzzy 
	 * Description: (模糊字符串) 
	 * @param strSource
	 * @return
	 */
	protected String fuzzy(String strSource) {
		String strResult = null;
		if (strSource == null || strSource.trim().length() == 0) {
			strResult = "%";
		} else {
			strResult = "%" + strSource.replace(' ', '%') + "%";
			strResult = strResult.replaceAll("%+", "%");
		}
		return strResult;
	}
	/**
	 * Title: fuzzy 
	 * Description: (in字符串) 
	 * @param strSource
	 * @return
	 */
	protected String inzfc(String strSource) {
		String strResult = null;
		if (strSource == null || strSource.trim().length() == 0) {
			strResult = "("+"''"+")";
		} else {
			strResult = "(" + strSource + ")";
		}
		return strResult;
	}
}
