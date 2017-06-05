package cn.ichazuo.controller.admin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.ichazuo.commons.authorize.AuthorizeButton;
import cn.ichazuo.commons.authorize.AuthorizeCfg;
import cn.ichazuo.commons.authorize.AuthorizeCfg.AuthorizeEnum;
import cn.ichazuo.commons.authorize.AuthorizeProperties;
import cn.ichazuo.commons.base.BaseController;
import cn.ichazuo.commons.component.ConfigInfo;
import cn.ichazuo.commons.util.NumberUtils;
import cn.ichazuo.commons.util.StringUtils;
import cn.ichazuo.model.table.Authorize;
import cn.ichazuo.model.table.Role;
import cn.ichazuo.service.AuthorizeService;
import cn.ichazuo.service.RoleService;

/**
 * @ClassName: AuthorizeController
 * @Description: (权限Controller)
 * @author ZhaoXu
 * @date 2015年7月15日 下午8:09:17
 * @version V1.0
 */
@RequestMapping("/admin")
@Controller("admin.authorizeController")
public class AuthorizeController extends BaseController {
	@Autowired
	private AuthorizeService authorizeService;
	@Autowired
	private ConfigInfo configInfo;
	@Autowired
	private RoleService roleService;

	/**
	 * @Title: authorize
	 * @Description: (跳转权限管理页面)
	 * @return
	 */
	@RequestMapping("/authorize")
	public String authorize() {
		return "/authorize/authorize";
	}

	/**
	 * @Title: findAllAuthorize 
	 * @Description: (获得所有资源) 
	 * @return
	 */
	@RequestMapping("/json/findAllAuthorize")
	@ResponseBody
	public JSONArray findAllAuthorize() {
		List<Role> roleList = roleService.findAllRole();
		List<Authorize> authorList = authorizeService.findAllAuthorize();

		AuthorizeCfg util = AuthorizeCfg.getInstance();
		JSONArray arr = new JSONArray();
		JSONObject head = new JSONObject();
		// 设置顶级文本为系统标题
		head.put("id", configInfo.getProjectName());
		head.put("authorizeName", configInfo.getProjectTitle());
		head.put("groupId", "");
		head.put("propertiesId", "");
		head.put("buttonId", "");
		// 根节点,0
		head.put("level", 0);
		head.put("index", 0);
		JSONArray groupArr = new JSONArray();
		// 遍历所有group
		util.getGroupList().forEach(group -> {
			JSONObject groupObj = new JSONObject();
			groupObj.put("id", group.getId());
			groupObj.put("authorizeName", group.getName());
			groupObj.put("groupId", group.getId());
			groupObj.put("propertiesId", "");
			groupObj.put("buttonId", "");
			// group节点,1
			groupObj.put("level", 1);
			groupObj.put("index", 1);
			JSONArray properArr = new JSONArray();
			// 遍历组中所有资源
			group.getProperList().forEach(proper -> {
				JSONObject properObj = new JSONObject();
				properObj.put("id", group.getId() + "-" + proper.getId() + "-null");
				properObj.put("authorizeName", proper.getName());
				properObj.put("iconCls", "tree-file");
				properObj.put("groupId", group.getId());
				Authorize properAuthor = authorizeService.findAuthorize(authorList, group.getId(), proper.getId(),AuthorizeEnum.Properties);
				properObj.put("authorId", properAuthor == null ? 0 : properAuthor.getId());

				properObj.put("propertiesId", proper.getId());
				properObj.put("buttonId", "");

				StringBuffer proStr = new StringBuffer("");
				roleList.stream().filter(role -> properAuthor != null && properAuthor.getRoleCode().contains(role.getCode()))
						.forEach(r -> proStr.append(r.getRoleName()).append(","));
				properObj.put("roleName", StringUtils.removeEndChar(proStr.toString(), ','));

				properObj.put("index", 2);
				// properties节点,2
				properObj.put("level", 2);
				JSONArray buttonArr = new JSONArray();
				// 遍历资源中的button
				proper.getButtonList().forEach(button -> {
					JSONObject buttonObj = new JSONObject();
					buttonObj.put("id", group.getId() + "-" + proper.getId() + "-" + button.getId());
					buttonObj.put("authorizeName", button.getName());
					buttonObj.put("iconCls", "icon-" + button.getIcon());
					buttonObj.put("groupId", group.getId());
					buttonObj.put("buttonId", button.getId());
					buttonObj.put("propertiesId", proper.getId());
					Authorize buttonAuthor = authorizeService.findAuthorize(authorList, button.getId(), proper.getId(),AuthorizeEnum.Button);
					buttonObj.put("authorId", buttonAuthor == null ? 0 : buttonAuthor.getId());

					StringBuffer btnStr = new StringBuffer("");
					roleList.stream().filter(role -> buttonAuthor != null && buttonAuthor.getRoleCode().contains(role.getCode()))
							.forEach(r -> btnStr.append(r.getRoleName()).append(","));

					buttonObj.put("roleName", StringUtils.removeEndChar(btnStr.toString(), ','));

					buttonObj.put("index", 3);
					// button节点,3
					buttonObj.put("level", 3);
					buttonArr.add(buttonObj);
				});
				properObj.put("children", buttonArr);
				properArr.add(properObj);
			});
			groupObj.put("children", properArr);
			groupArr.add(groupObj);
		});
		head.put("children", groupArr);
		arr.add(head);
		System.out.println(arr.toJSONString());
		return arr;
	}

	/**
	 * @Title: saveAuthorList 
	 * @Description: (权限批量授权) 
	 * @param ids
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value = "/json/saveAuthorList")
	@ResponseBody
	public JSONObject saveAuthorList(String ids, Long roleId) {
		try {
			if(StringUtils.isNullOrEmpty(ids) || NumberUtils.isNullOrZero(roleId)){
				return error("参数缺失");
			}
			Role r = roleService.findRoleById(roleId);
			if(r == null){
				return error("保存失败");
			}
			Arrays.asList(ids.split(",")).stream().filter(id -> id != null && !id.equals("") && !id.equals("0")).forEach(id -> {
						String[] idArr = id.split("-");
						if (idArr[2].equals("null") && idArr.length != 0) {
							// 添加资源权限
							saveOrUpdateProperties(idArr[0], idArr[1], r.getCode());
						} else {
							// 添加button权限
							saveOrUpdateButton(idArr[0], idArr[1], idArr[2], r.getCode());
							// 给properties配置权限
							saveOrUpdateProperties(idArr[0], idArr[1], r.getCode());
						}
					});
			return ok("批量授权成功");
		} catch (Exception e) {
			e.printStackTrace();
			return error(ADMIN_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: cancelSaveList 
	 * @Description: (取消批量授权) 
	 * @param ids
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value = "/json/cancelSaveList")
	@ResponseBody
	public JSONObject cancelSaveList(String ids, Long roleId) {
		AuthorizeCfg util = AuthorizeCfg.getInstance();
		try {
			Role r = roleService.findRoleById(roleId);
			if(r == null){
				return error("操作失败");
			}
			List<Authorize> authorizeList = authorizeService.findAllAuthorize();
			Arrays.asList(ids.split(",")).stream().filter(id -> id != null && !id.equals("") && !id.equals("0")).forEach(id -> {
						// 取得author
						Authorize author = authorizeService.findAuthorizeById(Long.parseLong(id));
						if(author != null){
							String code = author.getRoleCode();
							// 如果包含要取消的角色
							if (code != null && code.contains(r.getCode())) {
								// 删除角色的code
								author.setRoleCode(code.replace("," + r.getCode(), ""));
								authorizeService.updateAuthorize(author);

								if (author.getType() == 1) {
									AuthorizeProperties proper = util.getAuthorizePropertiesById(author.getGroupId(),author.getPropertiesId());
									// 取消资源下button的权限
									List<AuthorizeButton> buttonList = proper.getButtonList();
									buttonList.forEach(btn -> {
										Authorize authorizationBtn = authorizeService.findAuthorize(authorizeList,btn.getId(), proper.getId(), AuthorizeEnum.Button);
										// 如果button有当前角色权限,则取消
										if (authorizationBtn != null && authorizationBtn.getRoleCode() != null && authorizationBtn.getRoleCode().contains(r.getCode())) {
											// 删除code
											authorizationBtn.setRoleCode(authorizationBtn.getRoleCode().replaceAll("," + r.getCode(), ""));
											authorizeService.updateAuthorize(authorizationBtn);
										}
									});
								}
							}
						}
					});
			return ok("取消授权成功!");
		} catch (Exception e) {
			e.printStackTrace();
			return error(ADMIN_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: saveAuthorize 
	 * @Description: (保存权限) 
	 * @param authorize
	 * @param roleNames
	 * @return
	 */
	@RequestMapping(value = "/json/saveAuthorize")
	@ResponseBody
	public JSONObject saveAuthorize(Authorize authorize, String roleNames) {
		try {
			// 判断是否为资源
			if (authorize.getButtonId() == null || authorize.getButtonId().length() == 0) {
				// 为资源置为true
				authorize.setType(1);
			} else {
				// 非资源置为false
				authorize.setType(0);
			}
			// 将角色名换成code
			authorize.setRoleCode(getAllCodeByNames(roleNames));
			// 保存或修改
			if (NumberUtils.isNullOrZero(authorize.getId())) {
				authorize.setId(null);
				authorizeService.saveAuthorize(authorize);
			} else {
				Authorize temp = authorizeService.findAuthorizeById(authorize.getId());
				if(temp == null){
					return error("保存失败");
				}
				authorize.setVersion(temp.getVersion());
				authorizeService.updateAuthorize(authorize);
			}
			List<Authorize> list = authorizeService.findAllAuthorize();
			// 添加权限信息
			if (authorize.getType() == 1) {
				AuthorizeProperties proper = AuthorizeCfg.getInstance().getAuthorizePropertiesById(authorize.getGroupId(),authorize.getPropertiesId());
				// 为proper下button重新配置权限
				if (proper.getButtonList() != null && proper.getButtonList().size() != 0) {
					proper.getButtonList().forEach(btn -> {
						Authorize authorTemp = authorizeService.findAuthorize(list, btn.getId(),proper.getId(), AuthorizeEnum.Button);
						if (authorTemp != null && authorTemp.getRoleCode() != null) {
							StringBuffer btnCode = new StringBuffer(",");
							Arrays.asList(authorTemp.getRoleCode().split(",")).stream().filter(code -> code != null && code.length() > 1&& authorize.getRoleCode().contains(code)).distinct().forEach(code -> btnCode.append(code).append(","));
							authorTemp.setRoleCode(btnCode.toString());
							authorizeService.updateAuthorize(authorTemp);
						}
					});
				}
			} else {
				// 为button,给properties配置权限
				saveOrUpdateProperties(authorize.getGroupId(), authorize.getPropertiesId(), authorize.getRoleCode());
			}
			return ok("保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			return error(ADMIN_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: saveOrUpdateButton 
	 * @Description: (保存或修改按钮) 
	 * @param groupId
	 * @param properId
	 * @param buttonId
	 * @param roleCode
	 * @return
	 */
	private Authorize saveOrUpdateButton(String groupId, String properId,String buttonId, String roleCode) {
		// 是button
		Authorize authorize = authorizeService.findAuthorize(authorizeService.findAllAuthorize(),buttonId, properId,AuthorizeEnum.Button);
		if (authorize == null || NumberUtils.isNullOrZero(authorize.getId())) {
			authorize = new Authorize();
			// 添加author
			authorize.setButtonId(buttonId);
			authorize.setGroupId(groupId);
			authorize.setPropertiesId(properId);
			authorize.setRoleCode("," + roleCode + ",");
			authorize.setType(0);
			authorizeService.saveAuthorize(authorize);
		} else {
			// 修改author
			authorize.setButtonId(buttonId);
			authorize.setGroupId(groupId);
			authorize.setPropertiesId(properId);
			authorize.setType(0);
			if (authorize.getRoleCode() != null) {
				if (!authorize.getRoleCode().contains(roleCode)) {
					authorize.setRoleCode(authorize.getRoleCode() + roleCode + ",");
				}
			} else {
				authorize.setRoleCode("," + roleCode + ",");
			}
			authorizeService.updateAuthorize(authorize);
		}
		return authorize;
	}
	
	/**
	 * @Title: saveOrUpdateProperties 
	 * @Description: (保存或修改资源) 
	 * @param groupId
	 * @param properId
	 * @param roleCode
	 * @return
	 */
	private Authorize saveOrUpdateProperties(String groupId, String properId,String roleCode) {
		// 是资源
		Authorize authorize = authorizeService.findAuthorize(authorizeService.findAllAuthorize(),groupId, properId,AuthorizeEnum.Properties);
		if (authorize == null || NumberUtils.isNullOrZero(authorize.getId())) {
			authorize = new Authorize();
			// 添加author
			authorize.setButtonId("");
			authorize.setGroupId(groupId);
			authorize.setPropertiesId(properId);
			authorize.setRoleCode("," + roleCode + ",");
			authorize.setType(1);
			authorizeService.saveAuthorize(authorize);
		} else {
			// 修改author
			authorize.setButtonId("");
			authorize.setGroupId(groupId);
			authorize.setPropertiesId(properId);
			authorize.setType(1);
			List<String> codeList = Arrays.asList(roleCode.split(",")).stream().filter(code-> code != null && code.length() != 0).distinct().collect(Collectors.toList());
			if (authorize.getRoleCode() != null) {
				for (String code : codeList) {
					if (!authorize.getRoleCode().contains(code)) {
						authorize.setRoleCode(authorize.getRoleCode() + roleCode+ ",");
					}
				}
			} else {
				authorize.setRoleCode("," + roleCode + ",");
			}
			authorizeService.updateAuthorize(authorize);
		}
		return authorize;
	}

	/**
	 * @Title: getAllCodeByNames 
	 * @Description: (根据角色名称查询对应角色Code) 
	 * @param names
	 * @return
	 */
	private String getAllCodeByNames(String names) {
		StringBuffer sb = new StringBuffer("");
		StringBuffer name = new StringBuffer(",");

		Arrays.asList(names.split(",")).stream().filter(s->s != null && s.length() > 0).forEach(s->name.append(s).append(","));
		roleService.findRoleListByNames(name.toString()).forEach(role -> sb.append(role.getCode()).append(","));
		return sb.toString();
	}
}
