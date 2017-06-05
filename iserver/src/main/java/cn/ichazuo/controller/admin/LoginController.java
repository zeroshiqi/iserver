package cn.ichazuo.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.alibaba.fastjson.JSONObject;

import cn.ichazuo.commons.base.BaseController;
import cn.ichazuo.commons.component.ConfigInfo;
import cn.ichazuo.commons.util.CodeUtils;
import cn.ichazuo.commons.util.CookieUtils;
import cn.ichazuo.commons.util.PasswdEncryption;
import cn.ichazuo.commons.util.alipay.sign.Base64;
import cn.ichazuo.commons.util.model.LoginUser;
import cn.ichazuo.service.UserService;

/**
 * @ClassName: LoginController 
 * @Description: (后台登录controller) 
 * @author ZhaoXu
 * @date 2015年7月14日 下午5:47:02 
 * @version V1.0
 */
@RequestMapping("/admin")
@Controller("admin.loginController")
public class LoginController extends BaseController {
	
	@Autowired
	private UserService adminService;
	@Autowired
	private ConfigInfo configInfo;
	
	/**
	 * @Title: main 
	 * @Description: (重定向至后台主界面) 
	 * @return
	 */
	@RequestMapping("/main")
	public String main(HttpServletRequest request) {
		String realname = CookieUtils.getCookieValue(request, configInfo.getCookieRealName());
		if(realname == null){
			return "redirect:login";
		}
		request.setAttribute("realName", new String(Base64.decode(realname)));
		return "/main";
	}
	
	/**
	 * @Title: signin 
	 * @Description: (登录) 
	 * @param account 账号
	 * @param password 密码
	 * @return
	 */
	@RequestMapping("/signin")
	public String signin(String account, String password, RedirectAttributesModelMap modelMap,HttpServletRequest request, HttpServletResponse response) {
		LoginUser user = adminService.findUserByAccount(account);

		// redirect url
		String redirect = "";
		if (user == null) {
			modelMap.addFlashAttribute("message", "无此用户,请检查账号");
			// 重定向至登录页面
			redirect = "redirect:login";
		} if (!PasswdEncryption.verify(password, user.getPassword())) {
			modelMap.addFlashAttribute("message", "密码错误!");
			redirect = "redirect:login";
		} else {
			// 设置cookie
			String uuid = CodeUtils.getUUID();
			CookieUtils.deleteCookie(response, request, configInfo.getCookieName());
			CookieUtils.addCookie(response, configInfo.getCookieName(), uuid);
			
			CookieUtils.deleteCookie(response, request, configInfo.getCookieAccount());
			CookieUtils.addCookie(response, configInfo.getCookieAccount(), Base64.encode(user.getAccount().getBytes()));
			
			CookieUtils.deleteCookie(response, request, configInfo.getCookieRealName());
			CookieUtils.addCookie(response, configInfo.getCookieRealName(), Base64.encode(user.getRealName().getBytes()));
			
			CookieUtils.deleteCookie(response, request, configInfo.getCookieRole());
			CookieUtils.addCookie(response, configInfo.getCookieRole(), Base64.encode(user.getCode().getBytes()));
			
			CookieUtils.deleteCookie(response, request, configInfo.getCookieUser());
			CookieUtils.addCookie(response, configInfo.getCookieUser(), Base64.encode(String.valueOf(user.getId()).getBytes()));
			
			redirect = "redirect:main";
		}
		return redirect;
	}
	
	/**
	 * @Title: logout 
	 * @Description: (用户退出) 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/json/logout")
	public JSONObject logout(HttpServletRequest request,HttpServletResponse response){
		//清空session
		request.getSession().invalidate();
		//清空缓存
		CookieUtils.deleteCookie(response, request, configInfo.getCookieName());
		return ok();
	}
}
