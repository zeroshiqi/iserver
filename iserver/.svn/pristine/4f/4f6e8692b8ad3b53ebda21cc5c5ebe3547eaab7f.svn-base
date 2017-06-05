package cn.ichazuo.controller.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.ichazuo.commons.base.BaseController;
import cn.ichazuo.commons.component.ConfigInfo;
import cn.ichazuo.commons.util.CookieUtils;
import cn.ichazuo.commons.util.NumberUtils;
import cn.ichazuo.commons.util.PasswdEncryption;
import cn.ichazuo.commons.util.StringUtils;
import cn.ichazuo.commons.util.alipay.sign.Base64;
import cn.ichazuo.commons.util.model.Page;
import cn.ichazuo.commons.util.model.Params;
import cn.ichazuo.model.table.User;
import cn.ichazuo.service.UserService;

/**
 * @ClassName: UserController 
 * @Description: (后台用户Controller) 
 * @author ZhaoXu
 * @date 2015年7月14日 下午7:35:01 
 * @version V1.0
 */
@RequestMapping("/admin")
@Controller("admin.userController")
public class UserController extends BaseController{
	@Autowired
	private UserService userService;
	@Autowired
	private ConfigInfo configInfo;
	
	/**
	 * @Title: user 
	 * @Description: (跳转user.jsp) 
	 * @return
	 */
	@RequestMapping("/user")
	public String user(){
		return "/user/user";
	}
	
	/**
	 * @Title: findUserList 
	 * @Description: (查询后台用户列表) 
	 * @param page 分页
	 * @param realName 真实姓名
	 * @param account 账号
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/json/findUserList")
	public JSONObject findUserList(Page page,String realName,String account){
		JSONObject obj = new JSONObject();
		try{
			Params params = new Params(page.getNowPage(),page.getNumber());
			if(!StringUtils.isNullOrEmpty(account)){
				params.putData("account", super.fuzzy(account));
			}
			if(!StringUtils.isNullOrEmpty(realName)){
				params.putData("realName", super.fuzzy(realName));
			}
			obj.put("page", page.getNowPage());
			obj.put("rows", userService.findUserList(params.getMap()));
			obj.put("total", userService.findUserListCount(params.getMap()));
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println(obj.toJSONString());
		return obj;
	}
	
	/**
	 * @Title: saveUser 
	 * @Description: (保存后台用户) 
	 * @param user
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/json/saveUser")
	public JSONObject saveUser(User user){
		try{
			if(StringUtils.isNullOrEmpty(user.getAccount())){
				return warning("请输入账号!");
			}
			if(StringUtils.isNullOrEmpty(user.getRealName())){
				return warning("请输入真实姓名!");
			}
			// 新增 修改
			if(NumberUtils.isNullOrZero(user.getId())){
				if(StringUtils.isNullOrEmpty(user.getPassword())){
					return error("请输入密码!");
				}
				if(userService.findAccountCount(user.getAccount()) > 0){
					return warning("账号已存在,请重新输入!");
				}
				user.setAccount(user.getAccount().trim());
				user.setPassword(PasswdEncryption.generate(user.getPassword().trim()));
				if(userService.saveUser(user)){
					return ok("保存成功!");
				}
				return error("保存失败!");
			}else{
				user.setPassword(null);
				user.setAccount(null);
				User temp = userService.findUserById(user.getId());
				if(temp == null){
					return error("修改失败!");
				}
				user.setVersion(temp.getVersion());
				if(userService.updateUserInfo(user)){
					return ok("修改成功!");
				}
				return error("修改失败!");
			}
		}catch(Exception e){
			e.printStackTrace();
			return error(ADMIN_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: deleteUser 
	 * @Description: (删除用户) 
	 * @param adminId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/json/deleteUser")
	public JSONObject deleteUser(Long userId){
		try{
			if(NumberUtils.isNullOrZero(userId)){
				return error("参数缺失");
			}
			User admin = userService.findUserById(userId);
			admin.setStatus(0);
			if(userService.updateUserInfo(admin)){
				return ok("删除成功!");
			}
			return error("删除失败");
		}catch(Exception e){
			e.printStackTrace();
			return error(ADMIN_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: updatePassword 
	 * @Description: (修改管理员密码) 
	 * @param oldPassword 旧密码
	 * @param newPasswrod 新密码
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/json/updatePassword")
	public JSONObject updatePassword(String oldPassword,String newPasswrod,HttpServletRequest request){
		try{
			if(StringUtils.isNullOrEmpty(oldPassword) || StringUtils.isNullOrEmpty(newPasswrod)){
				return error("参数缺失");
			}
			String userid = CookieUtils.getCookieValue(request, configInfo.getCookieUser());
			User admin = userService.findUserById(Long.valueOf(new String(Base64.decode(userid))));
			if(admin == null){
				return notLogin("未登录");
			}
			//验证密码是否正确
			if(PasswdEncryption.verify(oldPassword.trim(), admin.getPassword())){
				
				admin.setPassword(PasswdEncryption.generate(newPasswrod.trim()));
				
				if(userService.updateUserInfo(admin)){
					return ok("修改成功");
				}
			}else{
				return warning("原密码输入错误");
			}
			return error("修改失败");
		}catch(Exception e){
			e.printStackTrace();
			return error(ADMIN_SYSTEM_ERROR);
		}
	}
}
