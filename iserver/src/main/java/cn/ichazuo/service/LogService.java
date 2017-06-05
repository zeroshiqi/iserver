package cn.ichazuo.service;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ichazuo.commons.base.BaseService;
import cn.ichazuo.commons.component.ConfigInfo;
import cn.ichazuo.commons.util.CookieUtils;
import cn.ichazuo.commons.util.alipay.sign.Base64;
import cn.ichazuo.commons.util.model.Params;
import cn.ichazuo.dao.LogDao;
import cn.ichazuo.model.admin.UserLogListInfo;
import cn.ichazuo.model.log.ArticleClickLog;
import cn.ichazuo.model.log.CourseClickLog;
import cn.ichazuo.model.log.PushLog;
import cn.ichazuo.model.log.UserLog;

/**
 * @ClassName: LogService 
 * @Description: (日志Service) 
 * @author ZhaoXu
 * @date 2015年7月24日 上午10:43:29 
 * @version V1.0
 */
@Service("logService")
public class LogService extends BaseService{
	private static final long serialVersionUID = 1L;

	@Resource
	private LogDao logDao;
	@Autowired
	private ConfigInfo configInfo;
	
	/**
	 * @Title: saveUserLog 
	 * @Description: (保存用户日志) 
	 * @param user 登录用户
	 * @param uri 访问地址
	 * @param ip ip地址
	 * @param description 描述
	 * @return
	 */
	public boolean saveUserLog(HttpServletRequest request,String uri,String ip,String description){
		UserLog log = new UserLog();
		String account = CookieUtils.getCookieValue(request, configInfo.getCookieAccount());
		String realName = CookieUtils.getCookieValue(request, configInfo.getCookieRealName());
		String userid = CookieUtils.getCookieValue(request, configInfo.getCookieUser());
		log.setAccount(new String(Base64.decode(account)));
		log.setUserName(new String(Base64.decode(realName)));
		log.setUserId(Long.valueOf(new String(Base64.decode(userid))));
		log.setIpAddress(ip);
		log.setUri(uri);
		log.setDescription(description);
		
		return logDao.saveUserLog(log) > 0;
	}
	
	/**
	 * @Title: findUserLog 
	 * @Description: (查询用户日志) 
	 * @param params
	 * @return
	 */
	public List<UserLogListInfo> findUserLog(Params params){
		return logDao.findUserLog(params.getMap());
	}
	
	/**
	 * @Title: findUserLogCount 
	 * @Description: (查询用户日志总数) 
	 * @param params
	 * @return
	 */
	public Long findUserLogCount(Params params){
		return logDao.findUserLogCount(params.getMap());
	}
	
	/**
	 * @Title: saveCourseClickLog 
	 * @Description: (保存课程点击日志) 
	 * @param log
	 * @return
	 */
	public boolean saveCourseClickLog(CourseClickLog log){
		return logDao.saveCourseClickLog(log) > 0;
	}
	
	/**
	 * @Title: saveArticleClickLog 
	 * @Description: (保存文章点击日志) 
	 * @param log
	 * @return
	 */
	public boolean saveArticleClickLog(ArticleClickLog log){
		return logDao.saveArticleClickLog(log) > 0;
	}
	
	/**
	 * @Title: saveWebListLog 
	 * @Description: (保存web列表日志) 
	 * @param ip
	 * @return
	 */
	public boolean saveWebListLog(String ip){
		return logDao.saveWebListLog(ip) > 0;
	}
	
	/**
	 * @Title: savePushLog 
	 * @Description: (保存推送日志) 
	 * @param log
	 * @return
	 */
	public boolean savePushLog(PushLog log){
		return logDao.savePushLog(log) > 0;
	}
}
