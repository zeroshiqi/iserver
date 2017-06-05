package cn.ichazuo.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ichazuo.commons.base.BaseService;
import cn.ichazuo.commons.component.CacheInfo;
import cn.ichazuo.commons.component.RedisClient;
import cn.ichazuo.commons.util.StringUtils;
import cn.ichazuo.dao.DictDao;
import cn.ichazuo.model.Dictionary;
import cn.ichazuo.model.admin.DictItemListInfo;
import cn.ichazuo.model.admin.DictListInfo;
import cn.ichazuo.model.table.Dict;
import cn.ichazuo.model.table.DictItem;

@Service("dictService")
public class DictService extends BaseService {
	private static final long serialVersionUID = 1L;

	@Resource
	private DictDao dictDao;
	@Autowired
	private CacheInfo cacheInfo;
	@Autowired
	private RedisClient redisClient;

	/**
	 * @Title: findDictItemListByCode 
	 * @Description: (根据Code获得数据字典项) 
	 * @param code
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Dictionary> findDictItemListByCode(String code) {
		List<Dictionary> list = null;

		String key = cacheInfo.getCacheDictionaryKey() + "--code--" + code;
		if (redisClient.isExist(key)) {
			list = (List<Dictionary>) redisClient.getObject(key);
		} else {
			list = dictDao.findDictionaryListByCode(code);
			redisClient.setObject(key, list, cacheInfo.getRedisCacheLevel6());
		}
		return list;
	}
	
	/**
	 * @Title: findDictItemById 
	 * @Description: (根据ID查询数据字典项) 
	 * @param id
	 * @return
	 */
	public DictItem findDictItemById(Long id){
		return dictDao.findDictItemById(id);
	}
	
	/**
	 * @Title: saveDict 
	 * @Description: (保存数据字典) 
	 * @param dict
	 * @return
	 */
	public boolean saveDict(Dict dict){
		return dictDao.saveDict(dict) > 0;
	}
	
	/**
	 * @Title: saveDictItem 
	 * @Description: (保存数据字典项) 
	 * @param item 
	 * @return
	 */
	public boolean saveDictItem(DictItem item){
		return dictDao.saveDictItem(item) > 0;
	}
	
	/**
	 * @Title: findDictList 
	 * @Description: (查询字典列表) 
	 * @param map
	 * @return
	 */
	public List<DictListInfo> findDictList(Map<String,Object> map){
		List<DictListInfo> list = dictDao.findDictList(map);
		list.forEach(info -> {
			info.setRemark(StringUtils.subString(info.getRemark()));
		});
		return list;
	}
	
	/**
	 * @Title: findDictListCount 
	 * @Description: (查询字典总数) 
	 * @param map
	 * @return
	 */
	public int findDictListCount(Map<String,Object> map){
		return dictDao.findDictListCount(map);
	}
	
	/**
	 * @Title: findDictById 
	 * @Description: (根据ID查询字典) 
	 * @param id
	 * @return
	 */
	public Dict findDictById(Long id){
		return dictDao.findDictById(id);
	}
	
	/**
	 * @Title: deleteDict 
	 * @Description: (删除字典) 
	 * @param dict
	 * @return
	 */
	public boolean deleteDict(Dict dict){
		dictDao.updateStatusByDictId(dict.getId());
		return dictDao.updateDictStatus(dict) > 0;
	}
	
	/**
	 * @Title: findCodeCount 
	 * @Description: (查询标识数量) 
	 * @param code
	 * @return
	 */
	public int findCodeCount(String code){
		return dictDao.findDictCodeCount(code);
	}
	
	/**
	 * @Title: findDictItemList 
	 * @Description: (查询字典项列表) 
	 * @param map
	 * @return
	 */
	public List<DictItemListInfo> findDictItemList(Map<String,Object> map){
		List<DictItemListInfo> list = dictDao.findDictItemList(map);
		list.forEach(s -> {
			s.setRemark(StringUtils.subString(s.getRemark()));
		});
		return list;
	}
	
	/**
	 * @Title: findDictItemListCount 
	 * @Description: (查询字典项列表总数) 
	 * @param map
	 * @return
	 */
	public int findDictItemListCount(Map<String,Object> map){
		return dictDao.findDictItemListCount(map);
	}
	
	/**
	 * @Title: deleteDictItem 
	 * @Description: (删除字典项) 
	 * @param item
	 * @return
	 */
	public boolean deleteDictItem(DictItem item){
		return dictDao.updateDictItemStatus(item) > 0;
	}
}
