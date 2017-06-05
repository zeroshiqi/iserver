package cn.ichazuo.service;

import java.util.List;
import java.util.NoSuchElementException;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ichazuo.commons.authorize.AuthorizeCfg.AuthorizeEnum;
import cn.ichazuo.commons.base.BaseService;
import cn.ichazuo.commons.component.CacheInfo;
import cn.ichazuo.commons.component.RedisClient;
import cn.ichazuo.dao.AuthorizeDao;
import cn.ichazuo.model.table.Authorize;

/**
 * @ClassName: AuthorizeService
 * @Description: (权限service)
 * @author ZhaoXu
 * @date 2015年7月14日 下午8:35:43
 * @version V1.0
 */
@Service("authorizeService")
public class AuthorizeService extends BaseService {
	private static final long serialVersionUID = 1L;
	@Resource
	private AuthorizeDao authorizeDao;
	@Autowired
	private CacheInfo cacheInfo;
	@Autowired
	private RedisClient redisClient;

	/**
	 * @Title: findAllAuthorize
	 * @Description: (获得全部权限列表)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Authorize> findAllAuthorize() {
		List<Authorize> list = null;
		String key = cacheInfo.getCacheAuthorizeAllListKey();
		if (redisClient.isExist(key)) {
			list = (List<Authorize>) redisClient.getObject(key);
		} else {
			list = authorizeDao.findAllAuthorize();
			redisClient.setObject(key, list, cacheInfo.getRedisCacheLevel6());
		}
		return list;
	}
	
	/**
	 * @Title: saveAuthorize 
	 * @Description: (保存权限) 
	 * @param authorize
	 * @return
	 */
	public boolean saveAuthorize(Authorize authorize){
		super.deleteCache(redisClient, cacheInfo.getCacheAuthorizeAllListKey());
		return authorizeDao.saveAuthorize(authorize) > 0;
	}
	
	/**
	 * @Title: findAuthorizeById 
	 * @Description: (根据ID查询权限) 
	 * @param id
	 * @return
	 */
	public Authorize findAuthorizeById(Long id){
		return authorizeDao.findAuthorizeById(id);
	}
	
	/**
	 * @Title: updateAuthorize 
	 * @Description: (修改权限) 
	 * @param authorize
	 * @return
	 */
	public boolean updateAuthorize(Authorize authorize){
		super.deleteCache(redisClient, cacheInfo.getCacheAuthorizeAllListKey());
		return authorizeDao.updateAuthorize(authorize) > 0;
	}

	/**
	 * @Title: findAuthorize
	 * @Description: (查询权限)
	 * @param authorizeList 全部权限列表
	 * @param id groupId
	 * @param propertiesId
	 * @param authorEnum
	 * @return
	 */
	public Authorize findAuthorize(List<Authorize> authorizeList, String id, String propertiesId, AuthorizeEnum authorEnum) {
		Authorize author = null;
		try {
			if (authorEnum == AuthorizeEnum.Button) {
				author = authorizeList.stream().filter(temp -> temp.getButtonId().equals(id) && temp.getType() == 0 && temp.getPropertiesId().equals(propertiesId)).findFirst().get();
			} else {
				author = authorizeList.stream().filter(temp -> temp.getGroupId().equals(id) && temp.getType() == 1 && temp.getPropertiesId().equals(propertiesId)).findFirst().get();
			}
		} catch (NullPointerException e) {
			author = null;
		} catch (NoSuchElementException e) {
			// 权限未在数据库中
			author = null;
		}
		return author;
	}
}
