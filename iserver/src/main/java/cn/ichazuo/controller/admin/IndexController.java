package cn.ichazuo.controller.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.ichazuo.commons.authorize.AuthorizeCfg;
import cn.ichazuo.commons.authorize.AuthorizeGroup;
import cn.ichazuo.commons.authorize.AuthorizeProperties;
import cn.ichazuo.commons.base.BaseController;
import cn.ichazuo.commons.component.ConfigInfo;
import cn.ichazuo.commons.util.CookieUtils;
import cn.ichazuo.commons.util.alipay.sign.Base64;
import cn.ichazuo.model.table.Authorize;
import cn.ichazuo.service.AuthorizeService;

/**
 * @ClassName: IndexController 
 * @Description: (indexController) 
 * @author ZhaoXu
 * @date 2015年7月14日 下午5:53:05 
 * @version V1.0
 */
@Controller("index.indexController")
public class IndexController extends BaseController{
	@Autowired
	private AuthorizeService authorizeService;
	@Autowired
	private ConfigInfo configInfo;
	
	/**
	 * @Title: admin 
	 * @Description: (admin,防止/admin报404) 
	 * @return
	 */
	@RequestMapping("/admin")
	public String admin(){
		return "/login";
	}
	
	/**
	 * Title: login 
	 * Description: (跳转登录action) 
	 * @return
	 */
	@RequestMapping("/admin/login")
	public String login(String message,HttpServletRequest request){
		request.setAttribute("message", message);
		return "/login";
	}
	
	/**
	 * Title: forward 
	 * Description: (重定向至登录action) 
	 * @return
	 */
	@RequestMapping("/admin/forward")
	public String forward(RedirectAttributesModelMap modelMap,HttpServletRequest request){
		modelMap.addFlashAttribute("message", "请重新登录...");
		return "redirect:login";
	}
	
	/**
	 * @Title: findMenuTree 
	 * @Description: (查询左侧树形菜单) 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/admin/json/findMenuTree")
	public JSONArray findMenuTree(HttpServletRequest request){
		JSONArray arr = new JSONArray();
		String roleCode = CookieUtils.getCookieValue(request, configInfo.getCookieRole());
		List<AuthorizeProperties> properMenu = new ArrayList<>();
		List<AuthorizeGroup> groupMenu = new ArrayList<>();
		
		// 权限帮助类,拼接json
		AuthorizeCfg util = AuthorizeCfg.getInstance();

		// 取得所有的权限
		List<Authorize> authorList = authorizeService.findAllAuthorize().stream().filter(author -> author.getRoleCode().contains(new String(Base64.decode(roleCode)))).collect(Collectors.toList());

		authorList.stream().filter(authorize->authorize.getType() == 1).forEach(author -> {
			AuthorizeGroup group = util.getGroupMap().get(author.getGroupId());
			if (!groupMenu.contains(group)) {
				groupMenu.add(group);
			}
			properMenu.add(group.getProperMap().get(author.getPropertiesId()));
		});
		
		Collections.sort(properMenu, (a,b) -> a.getWeight().compareTo(b.getWeight()));
		Collections.sort(groupMenu, (a,b) -> a.getWeight().compareTo(b.getWeight()));
		
		// 系统帮助类,取得title
		JSONObject head = new JSONObject();
		head.put("id", configInfo.getProjectName());
		head.put("text", configInfo.getProjectTitle());

		JSONArray groupArr = new JSONArray();
		
		// 遍历所有group
		groupMenu.forEach(group -> {
			JSONObject groupObj = new JSONObject();
			groupObj.put("id", group.getId());
			groupObj.put("text", group.getName());
			JSONObject nullUrl = new JSONObject();
			nullUrl.put("url", "");
			groupObj.put("attributes", nullUrl);
			JSONArray properArr = new JSONArray();
			// 遍历组中所有资源
			properMenu.stream().filter(proper->proper.getAuthorizeGroup().equals(group) && proper.isShowMenu()).forEach(proper -> {
				JSONObject properObj = new JSONObject();
				properObj.put("id", proper.getId());
				properObj.put("text", proper.getTitle());
				
				// 添加URL
				JSONObject url = new JSONObject();
				// 拼接url
				url.put("url", "/"+configInfo.getProjectName() + proper.getAction());
				properObj.put("attributes", url);
				properArr.add(properObj);
			});
			groupObj.put("children", properArr);
			groupArr.add(groupObj);
		});
		head.put("children", groupArr);
		arr.add(head);
		return arr;
	}
	
}	
