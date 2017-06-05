package cn.ichazuo.commons.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import cn.ichazuo.commons.Result;
import cn.ichazuo.commons.authorize.AuthorizeButton;
import cn.ichazuo.commons.authorize.AuthorizeCfg;
import cn.ichazuo.commons.authorize.AuthorizeCfg.AuthorizeEnum;
import cn.ichazuo.commons.authorize.AuthorizeProperties;
import cn.ichazuo.commons.component.ConfigInfo;
import cn.ichazuo.commons.util.CookieUtils;
import cn.ichazuo.commons.util.alipay.sign.Base64;
import cn.ichazuo.commons.util.model.PhoneInfo;
import cn.ichazuo.model.table.Authorize;
import cn.ichazuo.service.AuthorizeService;
import cn.ichazuo.service.LogService;

/**
 * ClassName: AuthorizeFilter Description: (权限过滤器)
 * 
 * @author ZhaoXu
 * @date 2015年6月28日 下午4:19:45
 * @version V1.0
 */
public class AuthorizeFilter implements Filter {
	private static Logger logger = LoggerFactory.getLogger(AuthorizeFilter.class);

	@Autowired
	private ConfigInfo configInfo;
	@Autowired
	private AuthorizeService authorizeService;
	@Autowired
	private LogService logService;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		AuthorizeCfg.getInstance();
		logger.info("init Filter....");
	}

	@Override
	public void destroy() {
		AuthorizeCfg.destory();
		logger.info("destroy Filter....");
	}

	/**
	 * Title: getPhoneInfo Description: (获得手机信息)
	 * 
	 * @param request
	 * @return
	 * @author ZhaoXu
	 */
	@SuppressWarnings("unused")
	private PhoneInfo getPhoneInfo(ServletRequest request) {
		PhoneInfo info = new PhoneInfo();
		info.setAccess_token(request.getParameter("access_token"));// token
		info.setClient_version(request.getParameter("client_version"));// 版本信息
		info.setDevice_id(request.getParameter("device_id")); // 设备号
		info.setPlatform(request.getParameter("platform"));// 客户端信息 android or
															// ios
		return info;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// 获得httpRequest httpResponse方便使用
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		request.setAttribute("app", "/" + configInfo.getProjectName());

		// IP地址
		String ipAddress = request.getRemoteAddr();

		// 获得访问uri
		String uri = httpRequest.getRequestURI();
		if (uri.contains(";")) {
			uri = uri.substring(0, uri.indexOf(";"));
		}

		// 过滤静态资源
		if (uri.contains("/js/") || uri.contains("/css/") || uri.contains("/images/") || uri.contains("/easyui/")
				|| uri.contains("/ueditor/") || uri.endsWith(".jpg") || uri.endsWith(".JPG") || uri.endsWith(".png")
				|| uri.endsWith(".gif")) {
			chain.doFilter(request, response);
			return;
		} else {
//			System.out.println("ip:" + ipAddress + "-->" + uri);
		}

		// 设置不需要登录访问的页面,action等..
		if (uri.endsWith("/index.jsp") || uri.endsWith(configInfo.getProjectName() + "/") || uri.endsWith("/fail.jsp")
				|| uri.endsWith("/forward.jsp") || uri.endsWith("/login") || uri.endsWith("/signin")
				|| uri.endsWith("/forward")) {
			chain.doFilter(request, response);
			return;
		}

		// 支付回调不做拦截
		if (uri.endsWith("/app/aliPayOnlineCourseNotify") || uri.endsWith("/app/aliPayNotify") || uri.endsWith("/app/baiduPayNotify")
				|| uri.endsWith("/app/weixinWebPayNotify") || uri.endsWith("/app/weixinWebCrowdfundingLogPayNotify")
				|| uri.endsWith("/app/weixinPayNotify") || uri.endsWith("/app/baiduCrowdPayNotify") || uri.endsWith("/app/weixinOnlineWebPayNotify")) {
			chain.doFilter(request, response);
			return;
		}

		// app端处理
		if (uri.contains("/app/")) {
			if (uri.endsWith("/app/uploadAvatar")) {
				chain.doFilter(request, response);
				return;
			}

			// 获得手机信息
//			PhoneInfo info = getPhoneInfo(request);
			// 验证参数
//			if (StringUtils.isNullOrEmpty(info.getDeviceId()) || StringUtils.isNullOrEmpty(info.getClientVersion())
//					|| StringUtils.isNullOrEmpty(info.getPlatform())) {
//				writerErrorJSON(httpResponse, Result.ERROR_STATUS, "参数缺失");
//				return;
//			}

			chain.doFilter(request, response);
			return;
		}

		// 后台处理
		if (uri.contains("/admin/")) {
			// 登录用户
			String uuid = CookieUtils.getCookieValue(httpRequest, configInfo.getCookieName());
			String roleCode = CookieUtils.getCookieValue(httpRequest, configInfo.getCookieRole());
			// 用户未登录
			if (roleCode == null || uuid == null || uuid.equals("")) {
				// 如果是json请求
				if (uri.contains("/json/")) {
					writerErrorJSON(httpResponse, Result.NOTLOGIN_STATUS, "未登录");
					return;
				} else {
					httpResponse.sendRedirect("/" + configInfo.getProjectName() + "/forward.jsp");
					return;
				}
			}

			// 登录后访问main和请求json 不拦截
			if (uri.endsWith("/main") || uri.contains("/json/")) {
				chain.doFilter(request, response);
				return;
			}

			// 权限认证
			AuthorizeCfg authorize = AuthorizeCfg.getInstance();
			// 根据action获得properties
			AuthorizeProperties proper = authorize
					.getAuthorizePropertiesByAction(uri.substring(("/" + configInfo.getProjectName()).length()));
			if (proper == null) {
				// 说明访问地址有问题
				httpRequest.getRequestDispatcher("/fail.jsp").forward(httpRequest, httpResponse);
				return;
			}
			List<Authorize> authorList = authorizeService.findAllAuthorize();

			// 获得当前资源的权限
			Authorize propertiesAuthor = authorizeService.findAuthorize(authorList, proper.getAuthorizeGroup().getId(),
					proper.getId(), AuthorizeEnum.Properties);
			// 404 无权限
			if (proper == null || propertiesAuthor == null || propertiesAuthor.getRoleCode() == null) {
				httpRequest.getRequestDispatcher("/fail.jsp").forward(httpRequest, httpResponse);
				return;
			}
			// 添加页面按钮
			List<AuthorizeButton> buttonList = new ArrayList<>();
			proper.getButtonList().forEach(button -> {
				Authorize author = authorizeService.findAuthorize(authorList, button.getId(), proper.getId(),
						AuthorizeEnum.Button);
				if (author != null && author.getRoleCode().contains(new String(Base64.decode(roleCode)))) {
					buttonList.add(button);
				}
			});
			httpRequest.setAttribute("buttonList", buttonList);

			// 保存访问日志
			logService.saveUserLog(httpRequest, uri, ipAddress,
					proper.getAuthorizeGroup().getName() + "->" + proper.getName());

			chain.doFilter(request, response);
			return;
		}

		writerErrorJSON(httpResponse, Result.ERROR_STATUS, "请求错误");
	}

	/**
	 * Title: error Description: (返回错误信息)
	 * 
	 * @return
	 * @author ZhaoXu
	 */
	private JSONObject error(Integer status, String msg) {
		JSONObject obj = new JSONObject();
		obj.put("status", status);
		obj.put("msg", msg);
//		System.out.println(obj.toJSONString());
		return obj;
	}

	/**
	 * Title: writerErrorJSON Description: (输出错误JSON信息)
	 * 
	 * @param response
	 * @author ZhaoXu
	 */
	private void writerErrorJSON(ServletResponse response, Integer status, String msg) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");

		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.append(error(status, msg).toJSONString());
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
}
