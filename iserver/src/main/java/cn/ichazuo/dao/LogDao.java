package cn.ichazuo.dao;

import java.util.List;
import java.util.Map;

import cn.ichazuo.model.admin.UserLogListInfo;
import cn.ichazuo.model.log.ArticleClickLog;
import cn.ichazuo.model.log.CourseClickLog;
import cn.ichazuo.model.log.PushLog;
import cn.ichazuo.model.log.UserLog;

/**
 * @ClassName: LogDao 
 * @Description: (日志DAO) 
 * @author ZhaoXu
 * @date 2015年7月24日 上午10:39:17 
 * @version V1.0
 */
public interface LogDao {

	/**
	 * @Title: saveUserLog 
	 * @Description: (保存用户日志) 
	 * @param log
	 * @return
	 */
	public int saveUserLog(UserLog log);
	
	/**
	 * @Title: findUserLog 
	 * @Description: (查询用户日志) 
	 * @param params
	 * @return
	 */
	public List<UserLogListInfo> findUserLog(Map<String,Object> params);
	
	/**
	 * @Title: findUserLogCount 
	 * @Description: (查询用户日志总数) 
	 * @param params
	 * @return
	 */
	public Long findUserLogCount(Map<String,Object> params);
	
	/**
	 * @Title: saveCourseClickLog 
	 * @Description: (保存课程点击日志) 
	 * @param log
	 * @return
	 */
	public int saveCourseClickLog(CourseClickLog log);
	
	/**
	 * @Title: saveArticleClickLog 
	 * @Description: (保存文章点击日志) 
	 * @param log
	 * @return
	 */
	public int saveArticleClickLog(ArticleClickLog log);
	
	/**
	 * @Title: saveWebListLog 
	 * @Description: (保存web列表页面访问日志) 
	 * @param ip
	 * @return
	 */
	public int saveWebListLog(String ip);
	
	/**
	 * @Title: savePushLog 
	 * @Description: (保存推送日志) 
	 * @param log
	 * @return
	 */
	public int savePushLog(PushLog log);
}
