package cn.ichazuo.dao;

import java.util.List;
import java.util.Map;

import cn.ichazuo.model.Dictionary;
import cn.ichazuo.model.admin.DictItemListInfo;
import cn.ichazuo.model.admin.DictListInfo;
import cn.ichazuo.model.table.Dict;
import cn.ichazuo.model.table.DictItem;

/**
 * @ClassName: DictDao 
 * @Description: (数据字典DAO) 
 * @author ZhaoXu
 * @date 2015年7月20日 下午11:16:19 
 * @version V1.0
 */
public interface DictDao {
	
	/**
	 * @Title: findDictItemById 
	 * @Description: (根据ID查询字典项) 
	 * @param id
	 * @return
	 */
	public DictItem findDictItemById(Long id);
	
	/**
	 * @Title: findDictionaryListByCode 
	 * @Description: (根据Code查询字典项列表) 
	 * @param code 
	 * @return
	 */
	public List<Dictionary> findDictionaryListByCode(String code);
	
	/**
	 * @Title: saveDict 
	 * @Description: (保存数据字典) 
	 * @param dict
	 * @return
	 */
	public int saveDict(Dict dict);
	
	/**
	 * @Title: saveDictItem 
	 * @Description: (保存数据字典项) 
	 * @param item
	 * @return
	 */
	public int saveDictItem(DictItem item);
	
	/**
	 * @Title: updateDictStatus 
	 * @Description: (修改数据字典状态) 
	 * @param dict
	 * @return
	 */
	public int updateDictStatus(Dict dict);
	
	/**
	 * @Title: updateDictItemStatus 
	 * @Description: (修改数据字典项状态) 
	 * @param item
	 * @return
	 */
	public int updateDictItemStatus(DictItem item);
	
	/**
	 * @Title: findDictList 
	 * @Description: (查询字典列表) 
	 * @param map
	 * @return
	 */
	public List<DictListInfo> findDictList(Map<String,Object> map);
	
	/**
	 * @Title: findDictListCount 
	 * @Description: (查询字典总数) 
	 * @param map
	 * @return
	 */
	public int findDictListCount(Map<String,Object> map);
	
	/**
	 * @Title: findDictById 
	 * @Description: (根据ID查询字典) 
	 * @param id
	 * @return
	 */
	public Dict findDictById(Long id);
	
	/**
	 * @Title: findDictCodeCount 
	 * @Description: (查询字典标识数量) 
	 * @param code
	 * @return
	 */
	public int findDictCodeCount(String code);
	
	/**
	 * @Title: findDictItemList 
	 * @Description: (查询字典项列表) 
	 * @param map
	 * @return
	 */
	public List<DictItemListInfo> findDictItemList(Map<String,Object> map);
	
	/**
	 * @Title: findDictItemListCount 
	 * @Description: (查询字典项总数) 
	 * @param map
	 * @return
	 */
	public int findDictItemListCount(Map<String,Object> map);
	
	/**
	 * @Title: updateStatusByDictId 
	 * @Description: (批量更新字典项状态) 
	 * @param dictId
	 * @return
	 */
	public int updateStatusByDictId(Long dictId);
}
