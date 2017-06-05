package cn.ichazuo.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.ichazuo.commons.base.BaseService;
import cn.ichazuo.dao.FeedBackDao;
import cn.ichazuo.model.table.FeedBack;

/**
 * @ClassName: FeedBackService 
 * @Description: (意见反馈Service) 
 * @author ZhaoXu
 * @date 2015年7月18日 下午10:28:42 
 * @version V1.0
 */
@Service("feedBackService")
public class FeedBackService extends BaseService{
	private static final long serialVersionUID = 1L;
	
	@Resource
	private FeedBackDao feedBackDao;
	
	/**
	 * @Title: saveFeedBack 
	 * @Description: (保存意见反馈) 
	 * @param feedBack
	 */
	public boolean saveFeedBack(FeedBack feedBack){
		return feedBackDao.saveFeedBack(feedBack) > 0;
	}
}
