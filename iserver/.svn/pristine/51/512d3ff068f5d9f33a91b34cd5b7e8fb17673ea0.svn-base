package cn.ichazuo.commons.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: AppSessionListener 
 * @Description: (Session监听器) 
 * @author ZhaoXu
 * @date 2015年6月28日 下午4:20:44 
 * @version V1.0
 */
public class AppSessionListener implements HttpSessionListener {
	private static Logger logger = LoggerFactory.getLogger(AppSessionListener.class);
	
	
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		logger.info("session创建...");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		logger.info("session销毁...");
	}

}
