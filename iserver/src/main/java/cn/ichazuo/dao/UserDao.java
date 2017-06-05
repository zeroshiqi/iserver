package cn.ichazuo.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.ichazuo.commons.util.model.LoginUser;
import cn.ichazuo.model.admin.UserListInfo;
import cn.ichazuo.model.table.User;

/**
 * @ClassName: UserDao 
 * @Description: (后台用户DAO) 
 * @author ZhaoXu
 * @date 2015年7月22日 上午10:33:26 
 * @version V1.0
 */
@Repository("adminDao")
public interface UserDao {
	
	/**
	 * @Title: findAdminByAccount 
	 * @Description: (根据账号查询后台用户) 
	 * @param account
	 * @return
	 */
	public LoginUser findUserByAccount(String account);
	
	/**
	 * @Title: updateUserInfo 
	 * @Description: (修改后台用户信息) 
	 * @return
	 */
	public int updateUserInfo(User user);
	
	/**
	 * @Title: findUserInfoById 
	 * @Description: (根据ID查询后台用户信息) 
	 * @param id
	 * @return
	 */
	public User findUserInfoById(Long id);
	
	/**
	 * @Title: findUserList 
	 * @Description: (查询后台用户列表) 
	 * @param map
	 * @return
	 */
	public List<UserListInfo> findUserList(Map<String,Object> map);
	
	/**
	 * @Title: findUserListCount 
	 * @Description: (查询后台用户数量) 
	 * @param map
	 * @return
	 */
	public int findUserListCount(Map<String,Object> map);
	
	/**
	 * @Title: saveUser 
	 * @Description: (保存后台用户) 
	 * @param user
	 * @return
	 */
	public int saveUser(User user);
	
	/**
	 * @Title: findAccountCount 
	 * @Description: (查询账号数量) 
	 * @param account
	 * @return
	 */
	public int findAccountCount(String account);
}
