package cn.ichazuo.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.ichazuo.model.admin.RoleListInfo;
import cn.ichazuo.model.table.Role;

@Repository("roleDao")
public interface RoleDao {
	/**
	 * @Title: findRoleList 
	 * @Description: (查询角色列表) 
	 * @param map
	 * @return
	 */
	public List<RoleListInfo> findRoleList(Map<String,Object> map);
	
	/**
	 * @Title: findRoleListCount 
	 * @Description: (查询角色总数) 
	 * @param map
	 * @return
	 */
	public int findRoleListCount(Map<String,Object> map);
	
	/**
	 * @Title: saveRole 
	 * @Description: (保存角色) 
	 * @param role
	 * @return
	 */
	public int saveRole(Role role);
	
	/**
	 * @Title: findRoleById 
	 * @Description: (根据ID查询角色) 
	 * @param id
	 * @return
	 */
	public Role findRoleById(Long id);
	
	/**
	 * @Title: updateRole 
	 * @Description: (修改角色) 
	 * @param role
	 * @return
	 */
	public int updateRole(Role role);
	
	/**
	 * @Title: findRoleCount 
	 * @Description: (根据条件查询角色数量) 
	 * @param map
	 * @return
	 */
	public int findRoleCount(Map<String,Object> map);
	
	/**
	 * @Title: findUseRoleCount 
	 * @Description: (查询使用此角色的用户人数) 
	 * @param userId
	 * @return
	 */
	public int findUseRoleCount(Long userId);
	
	/**
	 * @Title: findAllRole 
	 * @Description: (查询全部角色) 
	 * @return
	 */
	public List<Role> findAllRole();
}
