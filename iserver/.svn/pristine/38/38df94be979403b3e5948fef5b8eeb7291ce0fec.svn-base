package cn.ichazuo.commons.util;

import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: CookieUtils 
 * @Description: (Cookie工具类) 
 * @author ZhaoXu
 * @date 2015年7月14日 下午4:47:24 
 * @version V1.0
 */
public class CookieUtils {
	
	/**
	 * @Title: getCookieValue 
	 * @Description: (获得Cookie中得值) 
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String cookieName) {
		try {
			if (cookieName != null) {
				Cookie cookie = getCookie(request, cookieName);
				if (cookie != null) {
					return URLDecoder.decode(cookie.getValue(), "UTF-8");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * @Title: getCookie 
	 * @Description: (获得cookie对象) 
	 * @param request
	 * @param cookieName
	 * @return
	 */
	private static Cookie getCookie(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		Cookie cookie = null;
		try {
			if (cookies != null && cookies.length > 0) {
				for (int i = 0; i < cookies.length; i++) {
					cookie = cookies[i];
					if (cookie.getName().equals(cookieName)) {
						return cookie;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cookie;
	}

	/**
	 * @Title: addCookie 
	 * @Description: (添加cookie,浏览器关闭失效) 
	 * @param response
	 * @param name
	 * @param value
	 */
	public static void addCookie(HttpServletResponse response, String name, String value) {
		try {
			Cookie cookies = new Cookie(name, URLEncoder.encode(value, "UTF-8"));
			cookies.setPath("/");
			cookies.setHttpOnly(true);
			cookies.setMaxAge(-1);// 设置cookie经过多长秒后被删除。如果0，就说明立即删除。如果是负数就表明当浏览器关闭时自动删除。
			response.addCookie(cookies);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @Title: deleteCookie 
	 * @Description: (删除Cookie) 
	 * @param response
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static boolean deleteCookie(HttpServletResponse response, HttpServletRequest request, String cookieName) {
		if (cookieName != null) {
			Cookie cookie = getCookie(request, cookieName);
			if (cookie != null) {
				cookie.setMaxAge(0);// 如果0，就说明立即删除
				cookie.setPath("/");// 不要漏掉
				response.addCookie(cookie);
				return true;
			}
		}
		return false;
	}
}
