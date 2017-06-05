package cn.ichazuo.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ichazuo.commons.base.BaseService;
import cn.ichazuo.commons.component.CacheInfo;
import cn.ichazuo.commons.component.RedisClient;
import cn.ichazuo.commons.util.StringUtils;
import cn.ichazuo.dao.RoleDao;
import cn.ichazuo.model.admin.RoleListInfo;
import cn.ichazuo.model.table.Role;

/**
 * @ClassName: RoleService 
 * @Description: (角色Service) 
 * @author ZhaoXu
 * @date 2015年7月15日 下午1:47:33 
 * @version V1.0
 */
@Service("roleService")
public class RoleService extends BaseService{
	private static final long serialVersionUID = 1L;
	@Resource
	private RoleDao roleDao;
	@Autowired
	private CacheInfo cacheInfo;
	@Autowired
	private RedisClient redisClient;
	
	/**
	 * @Title: findRoleList 
	 * @Description: (查询角色列表) 
	 * @param map
	 * @return
	 */
	public List<RoleListInfo> findRoleList(Map<String,Object> map){
		List<RoleListInfo> list = roleDao.findRoleList(map);
		list.forEach(info -> {
			info.setRemark(StringUtils.subString(info.getRemark()));
		});
		return list;
	}
	
	/**
	 * @Title: findRoleListCount 
	 * @Description: (查询角色总数) 
	 * @param map
	 * @return
	 */
	public int findRoleListCount(Map<String,Object> map){
		return roleDao.findRoleListCount(map);
	}
	
	/**
	 * @Title: saveRole 
	 * @Description: (保存角色) 
	 * @param role
	 * @return
	 */
	public boolean saveRole(Role role){
		String key = cacheInfo.getCacheRoleAllListKey();
		super.deleteCache(redisClient, key);
		return roleDao.saveRole(role) > 0;
	}
	
	/**
	 * @Title: findRoleById 
	 * @Description: (根据ID查询角色) 
	 * @param id
	 * @return
	 */
	public Role findRoleById(Long id){
		return roleDao.findRoleById(id);
	}
	
	/**
	 * @Title: updateRole 
	 * @Description: (修改角色) 
	 * @param role
	 * @return
	 */
	public boolean updateRole(Role role){
		String key = cacheInfo.getCacheRoleAllListKey();
		super.deleteCache(redisClient, key);
		return roleDao.updateRole(role) > 0;
	}
	
	/**
	 * @Title: findRoleCount 
	 * @Description: (根据条件查询角色数量) 
	 * @param map
	 * @return
	 */
	public int findRoleCount(Map<String,Object> map){
		return roleDao.findRoleCount(map);
	}
	
	/**
	 * @Title: findUserCountByRole 
	 * @Description: (查询使用此角色的用户) 
	 * @param roleId
	 * @return
	 */
	public int findUserCountByRole(Long roleId){
		return roleDao.findUseRoleCount(roleId);
	}
	
	/**
	 * @Title: findAllRole 
	 * @Description: (查询全部角色) 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Role> findAllRole(){
		List<Role> list = null;
		String key = cacheInfo.getCacheRoleAllListKey();
		if(redisClient.isExist(key)){
			list = (List<Role>)redisClient.getObject(key);
		}else{
			list = roleDao.findAllRole();
			redisClient.setObject(key, list, cacheInfo.getRedisCacheLevel6());
		}
		return list;
	}
	
	/**
	 * @Title: findRoleListByNames 
	 * @Description: (根据名称查询code) 
	 * @param names
	 * @return
	 */
	public List<Role> findRoleListByNames(String names){
		return findAllRole().stream().filter(role -> names.contains(","+role.getRoleName()+",")).collect(Collectors.toList());
	}
}
