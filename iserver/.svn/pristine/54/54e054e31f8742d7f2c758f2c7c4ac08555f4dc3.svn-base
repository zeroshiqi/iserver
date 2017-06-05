package cn.ichazuo.commons.util.model;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: Params 
 * Description: (查询参数类) 
 * @author ZhaoXu
 * @date 2015年7月7日 下午5:29:16 
 * @version V1.0
 */
public class Params {
	private Map<String,Object> data = new HashMap<>();
	public static final int PAGE_COUNT = 20;
	
	public Params(Integer page){
		if(page == null || page <= 1){
			page = 0;
		}else{
			page = page - 1;
		}
		data.put("page", page * PAGE_COUNT);
		data.put("pageCount", PAGE_COUNT);
	}
	
	public Params(Integer page,Integer count){
		if(page == null || page <= 1){
			page = 0;
		}else{
			page = page - 1;
		}
		data.put("page", page * count);
		data.put("pageCount", count);
	}
	
	public Params(){
		
	}
	
	/**
	 * Title: putData 
	 * Description: (放入数据) 
	 * @param key
	 * @param value
	 * @author ZhaoXu
	 */
	public void putData(String key,Object value){
		data.put(key, value);
	}
	
	/**
	 * Title: getData 
	 * Description: (取得数据) 
	 * @param key
	 * @return
	 * @author ZhaoXu
	 */
	public Object getData(String key){
		if(data.containsKey(key)){
			return data.get(key);
		}
		return null;
	}
	
	/**
	 * Title: getMap 
	 * Description: (获得Map信息) 
	 * @return
	 * @author ZhaoXu
	 */
	public Map<String,Object> getMap(){
		return this.data;
	}
	
	/**
	 * @Title: remove 
	 * @Description: (移除元素) 
	 * @param key
	 */
	public void removeData(String key){
		this.data.remove(key);
	}
	
	/**
	 * @Title: clear 
	 * @Description: (清空数据)
	 */
	public void clear(){
		this.data.clear();
	}
}
