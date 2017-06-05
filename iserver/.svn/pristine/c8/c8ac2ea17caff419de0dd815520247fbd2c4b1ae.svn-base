package cn.ichazuo.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.ichazuo.commons.base.BaseService;
import cn.ichazuo.commons.util.model.Params;
import cn.ichazuo.dao.CrowdfundingDao;
import cn.ichazuo.model.app.CourseWebCrowdfundingInfo;
import cn.ichazuo.model.table.CourseWebCrowdfunding;
import cn.ichazuo.model.table.CourseWebCrowdfundingLog;
import cn.ichazuo.model.table.CourseWebCrowdfundingOrder;
import cn.ichazuo.model.table.CourseWebCrowdfundingUser;

/**
 * @ClassName: CrowdfundingService 
 * @Description: (众筹Service) 
 * @author ZhaoXu
 * @date 2015年9月14日 下午12:57:05 
 * @version V1.0
 */
@Service("crowdfundingService")
public class CrowdfundingService extends BaseService{
	private static final long serialVersionUID = 1L;
	@Resource
	private CrowdfundingDao crowdfundingDao;
	
	/**
	 * @Title: findUserCrowdfundCount 
	 * @Description: (查询众筹数量) 
	 * @param unionId
	 * @param courseId
	 * @return
	 */
	public CourseWebCrowdfunding findUserCrowdfundInfo(String unionId,Long courseId){
		Params params = new Params();
		params.putData("unionId", unionId);
		params.putData("courseId", courseId);
		
		return crowdfundingDao.findUserCrowdfundInfo(params.getMap());
	}
	
	/**
	 * @Title: saveCrowdfundInfo 
	 * @Description: (保存众筹信息) 
	 * @param info
	 * @return
	 */
	public void saveCrowdfundInfo(CourseWebCrowdfunding info,List<CourseWebCrowdfundingUser> list){
		crowdfundingDao.saveCrowdfundInfo(info);
		list.forEach(user -> {
			user.setCrowdfundId(info.getId());
			
			crowdfundingDao.saveCrowdfundUser(user);
		});
	}
	
	/**
	 * @Title: updateCrowdfundingContent 
	 * @Description: (修改众筹信息 content) 
	 * @param uuid
	 * @param content
	 */
	public void updateCrowdfundingContent(String uuid,String content){
		Params params = new Params();
		params.putData("content", content);
		params.putData("uuid", uuid);
		crowdfundingDao.updateCrowdfundingContent(params.getMap());
	}
	
	/**
	 * @Title: findCrowdfundingLogList 
	 * @Description: (查询众筹日志) 
	 * @param id
	 * @return
	 */
	public List<CourseWebCrowdfundingLog> findCrowdfundingLogList(Long id){
		return crowdfundingDao.findCrowdfundingLogList(id);
	}
	
	/**
	 * @Title: findUserCrowdfundInfoByUUID 
	 * @Description: (查询众筹信息) 
	 * @param uuid
	 * @return
	 */
	public CourseWebCrowdfunding findUserCrowdfundInfoByUUID(String uuid){
		return crowdfundingDao.findUserCrowdfundInfoByUUID(uuid);
	}
	
	/**
	 * @Title: findCrowdfundingPriceCount 
	 * @Description: (查询意见众筹的钱数) 
	 * @param uuid
	 * @return
	 */
	public double findCrowdfundingPriceCount(String uuid){
		return crowdfundingDao.findCrowdfundingPriceCount(uuid);
	}
	
	/**
	 * @Title: updateCrowdfundOrderStatus 
	 * @Description: (修改订单状态) 
	 * @param id
	 * @return
	 */
	public boolean updateCrowdfundOrderStatus(Long id){
		return crowdfundingDao.updateCrowdfundOrderStatus(id) > 0;
	}
	
	/**
	 * @Title: findCrowdfundingInfo 
	 * @Description: (查询众筹信息) 
	 * @param uuid
	 * @return
	 */
	public CourseWebCrowdfundingInfo findCrowdfundingInfo(String uuid){
		return crowdfundingDao.findCrowdfundingInfo(uuid);
	}
	
	/**
	 * @Title: updateCrowfundingStatus 
	 * @Description: (修改众筹状态) 
	 * @param status
	 * @param uuid
	 * @return
	 */
	public boolean updateCrowfundingStatus(Integer status,Long id){
		Params params = new Params();
		params.putData("status", status);
		params.putData("id", id);
		return crowdfundingDao.updateCrowfundingStatus(params.getMap()) > 0;
	}
	
	/**
	 * @Title: findCrowdfundUUIDById 
	 * @Description: (查询众筹UUId) 
	 * @param id
	 * @return
	 */
	public String findCrowdfundUUIDById(Long id){
		return crowdfundingDao.findCrowdfundUUIDById(id);
	}
	
	/**
	 * @Title: findCrowdfundingIdByUUID 
	 * @Description: (根据UUID查询ID) 
	 * @param uuid
	 * @return
	 */
	public Long findCrowdfundingIdByUUID(String uuid){
		return crowdfundingDao.findCrowdfundIdByUUId(uuid);
	}
	
	/**
	 * @Title: saveCrowdfundingLog 
	 * @Description: (保存众筹日志) 
	 * @param log
	 * @return
	 */
	public boolean saveCrowdfundingLog(CourseWebCrowdfundingLog log){
		return crowdfundingDao.saveCrowdfundingLog(log) > 0;
	}
	
	/**
	 * @Title: updateCrowdfundingLogStatus 
	 * @Description: (修改状态) 
	 * @param code
	 * @return
	 */
	public boolean updateCrowdfundingLogStatus(String code){
		return crowdfundingDao.updateCrowdfundingLogStatus(code) > 0;
	}
	
	/**
	 * @Title: saveCrowdfundingOrder 
	 * @Description: (保存众筹订单) 
	 * @param order
	 * @return
	 */
	public boolean saveCrowdfundingOrder(CourseWebCrowdfundingOrder order){
		return crowdfundingDao.saveCrowdfundingOrder(order) > 0;
	}
	
	/**
	 * @Title: findCrowdfundingOrderInfo 
	 * @Description: (查询众筹订单) 
	 * @param code
	 * @return
	 */
	public CourseWebCrowdfundingOrder findCrowdfundingOrderInfo(String code){
		return crowdfundingDao.findCrowdfundingOrderInfo(code);
	}
	
	/**
	 * @Title: findCrowdfundingLogInfo 
	 * @Description: (查询众筹日志信息) 
	 * @param code
	 * @return
	 */
	public CourseWebCrowdfundingLog findCrowdfundingLogInfo(String code){
		return crowdfundingDao.findCrowdfundingLogInfo(code);
	}
	
	/**
	 * @Title: findCrowdfundingUsers 
	 * @Description: (查询用户) 
	 * @param id
	 * @return
	 */
	public List<CourseWebCrowdfundingUser> findCrowdfundingUsers(Long id){
		return crowdfundingDao.findCrowdfundingUsers(id);
	}
	
	public void updateUserCrowdfundWeixinInfo(Params params){
		crowdfundingDao.updateUserCrowdfundWeixinInfo(params.getMap());
	}
}
