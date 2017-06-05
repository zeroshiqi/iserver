package cn.ichazuo.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.ichazuo.commons.base.BaseController;
import cn.ichazuo.commons.util.NumberUtils;
import cn.ichazuo.commons.util.StringUtils;
import cn.ichazuo.commons.util.model.Page;
import cn.ichazuo.commons.util.model.Params;
import cn.ichazuo.model.table.Role;
import cn.ichazuo.service.RoleService;

/**
 * @ClassName: RoleController 
 * @Description: (角色Controller) 
 * @author ZhaoXu
 * @date 2015年7月15日 下午2:49:23 
 * @version V1.0
 */
@RequestMapping("/admin")
@Controller("admin.roleController")
public class RoleController extends BaseController{
	@Autowired
	private RoleService roleService;
	
	/**
	 * @Title: role 
	 * @Description: (跳转至角色页面) 
	 * @return
	 */
	@RequestMapping("/role")
	public String role(){
		return "/role/role";
	}
	
	/**
	 * @Title: findRoleList 
	 * @Description: (查询角色列表) 
	 * @param page
	 * @param roleName
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/json/findRoleList")
	public JSONObject findRoleList(Page page,String roleName){
		JSONObject obj = new JSONObject();
		try{
			Params params = new Params(page.getNowPage(),page.getNumber());
			if(!StringUtils.isNullOrEmpty(roleName)){
				params.putData("roleName", super.fuzzy(roleName));
			}
			obj.put("page", page.getNowPage());
			obj.put("rows", roleService.findRoleList(params.getMap()));
			obj.put("total", roleService.findRoleListCount(params.getMap()));
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println(obj.toJSONString());
		return obj;
	}
	
	/**
	 * @Title: saveRole 
	 * @Description: (保存角色) 
	 * @param role
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/json/saveRole")
	public JSONObject saveRole(Role role){
		try{
			if(StringUtils.isNullOrEmpty(role.getRoleName())){
				return warning("请输入角色名称!");
			}
			if(StringUtils.isNullOrEmpty(role.getCode())){
				return warning("请输入Code!");
			}
			Params params = new Params();
			params.putData("code", role.getCode());
			params.putData("id", role.getId());
			
			if(roleService.findRoleCount(params.getMap()) > 0){
				return warning("Code已存在!");
			}
			
			params.removeData("code");
			params.putData("roleName", role.getRoleName());
			if(roleService.findRoleCount(params.getMap()) > 0){
				return warning("角色已存在");
			}
			
			// 新增 修改
			if(NumberUtils.isNullOrZero(role.getId())){
				if(roleService.saveRole(role)){
					return ok("保存成功!");
				}
				return error("保存失败!");
			}else{
				Role r = roleService.findRoleById(role.getId());
				if(r == null){
					return error("保存失败!");
				}
				role.setVersion(r.getVersion());
				
				if(roleService.updateRole(role)){
					return ok("修改成功!");
				}
				return error("修改失败!");
			}
		}catch(Exception e){
			e.printStackTrace();
			return error(ADMIN_SYSTEM_ERROR);
		}
	}
	
	@ResponseBody
	@RequestMapping("/json/deleteRole")
	public JSONObject deleteRole(Long roleId){
		try{
			if(NumberUtils.isNullOrZero(roleId)){
				return error("参数缺失");
			}
			if(roleService.findUserCountByRole(roleId) > 0){
				return error("有用户使用此角色,请先解除使用!");
			}
			
			Role role = roleService.findRoleById(roleId);
			role.setStatus(0);
			if(roleService.updateRole(role)){
				return ok("删除成功");
			}
			return error("删除失败");
		}catch(Exception e){
			e.printStackTrace();
			return error(ADMIN_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: findAllRole 
	 * @Description: (查询全部角色) 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/json/findAllRole")
	public JSONArray findAllRole(){
		JSONArray arr = new JSONArray();
		arr.addAll(roleService.findAllRole());
		System.out.println(arr.toJSONString());
		return arr;
	}
}
