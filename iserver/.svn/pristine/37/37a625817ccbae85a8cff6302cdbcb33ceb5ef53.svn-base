package cn.ichazuo.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.ichazuo.commons.base.BaseService;
import cn.ichazuo.commons.util.StringUtils;
import cn.ichazuo.commons.util.model.LoginUser;
import cn.ichazuo.dao.UserDao;
import cn.ichazuo.model.admin.UserListInfo;
import cn.ichazuo.model.table.User;

/**
 * @ClassName: UserService 
 * @Description: (用户Service) 
 * @author ZhaoXu
 * @date 2015年7月14日 下午5:03:47 
 *
 */
@Service("UserService")
public class UserService extends BaseService{
	private static final long serialVersionUID = 1L;
	
	@Resource
	private UserDao UserDao;
	
	/**
	 * @Title: findUserByAccount 
	 * @Description: (根据账号查询后台用户) 
	 * @param account
	 * @return
	 */
	public LoginUser findUserByAccount(String account){
		return UserDao.findUserByAccount(account);
	}
	
	/**
	 * @Title: updateUserInfo 
	 * @Description: (修改用户信息) 
	 * @param user
	 * @return
	 */
	public boolean updateUserInfo(User user){
		return UserDao.updateUserInfo(user) > 0;
	}
	
	/**
	 * @Title: findUserById 
	 * @Description: (根据ID查询后台用户信息) 
	 * @param id
	 * @return
	 */
	public User findUserById(Long id){
		return UserDao.findUserInfoById(id);
	}
	
	/**
	 * @Title: findUserList 
	 * @Description: (查询后台用户列表) 
	 * @param map
	 * @return
	 */
	public List<UserListInfo> findUserList(Map<String,Object> map){
		List<UserListInfo> infoList = UserDao.findUserList(map);
		infoList.forEach(info -> {
			info.setRemark(StringUtils.subString(info.getRemark()));
		});
		return infoList;
	}
	
	/**
	 * @Title: findUserListCount 
	 * @Description: (查询后台用户数量) 
	 * @param map
	 * @return
	 */
	public int findUserListCount(Map<String,Object> map){
		return UserDao.findUserListCount(map);
	}
	
	/**
	 * @Title: saveUser 
	 * @Description: (保存后台用户) 
	 * @param User
	 * @return
	 */
	public boolean saveUser(User user){
		return UserDao.saveUser(user) > 0;
	}
	
	/**
	 * @Title: findAccountCount 
	 * @Description: (查询账号数量) 
	 * @param account
	 * @return
	 */
	public int findAccountCount(String account){
		return UserDao.findAccountCount(account);
	}
}
