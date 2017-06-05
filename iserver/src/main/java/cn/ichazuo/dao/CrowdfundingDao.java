package cn.ichazuo.dao;

import java.util.List;
import java.util.Map;

import cn.ichazuo.model.app.CourseWebCrowdfundingInfo;
import cn.ichazuo.model.table.CourseWebCrowdfunding;
import cn.ichazuo.model.table.CourseWebCrowdfundingLog;
import cn.ichazuo.model.table.CourseWebCrowdfundingOrder;
import cn.ichazuo.model.table.CourseWebCrowdfundingUser;

/**
 * @ClassName: CrowdfundingDao 
 * @Description: (众筹DAO) 
 * @author ZhaoXu
 * @date 2015年9月14日 下午1:00:46 
 * @version V1.0
 */
public interface CrowdfundingDao {
	/**
	 * @Title: findCrowdfundIdByUUId 
	 * @Description: (UUID换取Id) 
	 * @param uuid
	 * @return
	 */
	public Long findCrowdfundIdByUUId(String uuid);
	
	/**
	 * @Title: findUserCrowdfundInfo 
	 * @Description: (查询用户众筹信息) 
	 * @param params
	 * @return
	 */
	public CourseWebCrowdfunding findUserCrowdfundInfo(Map<String,Object> params);
	
	/**
	 * @Title: saveCrowdfundInfo 
	 * @Description: (保存众筹信息) 
	 * @param info
	 * @return
	 */
	public int saveCrowdfundInfo(CourseWebCrowdfunding info);
	
	/**
	 * @Title: saveCrowdfundUser 
	 * @Description: (保存要报名的用户) 
	 * @param user
	 * @return
	 */
	public int saveCrowdfundUser(CourseWebCrowdfundingUser user);
	
	/**
	 * @Title: updateCrowdfundingContent 
	 * @Description: (修改众筹信息) 
	 * @param map
	 * @return
	 */
	public int updateCrowdfundingContent(Map<String,Object> map);
	
	/**
	 * @Title: findCrowdfundingLogList 
	 * @Description: (查询日志) 
	 * @param id
	 * @return
	 */
	public List<CourseWebCrowdfundingLog> findCrowdfundingLogList(Long id);
	
	/**
	 * @Title: findCrowdfundingInfo 
	 * @Description: (查询众筹信息) 
	 * @param uuid
	 * @return
	 */
	public CourseWebCrowdfundingInfo findCrowdfundingInfo(String uuid);
	
	/**
	 * @Title: updateCrowfundingStatus 
	 * @Description: (修改众筹状态) 
	 * @param params
	 * @return
	 */
	public int updateCrowfundingStatus(Map<String,Object> params);
	
	/**
	 * @Title: findCrowdfundingPriceCount 
	 * @Description: (查询已经众筹的总金额) 
	 * @param uuid
	 * @return
	 */
	public double findCrowdfundingPriceCount(String uuid);
	
	/**
	 * @Title: findUserCrowdfundInfoByUUID 
	 * @Description: (查询众筹信息) 
	 * @param uuid
	 * @return
	 */
	public CourseWebCrowdfunding findUserCrowdfundInfoByUUID(String uuid);
	
	/**
	 * @Title: updateCrowdfundOrderStatus 
	 * @Description: (修改订单状态) 
	 * @param id
	 * @return
	 */
	public int updateCrowdfundOrderStatus(Long id);
	
	/**
	 * @Title: saveCrowdfundingLog 
	 * @Description: (保存众筹日志) 
	 * @param log
	 * @return
	 */
	public int saveCrowdfundingLog(CourseWebCrowdfundingLog log);
	
	/**
	 * @Title: updateCrowdfundingLogStatus 
	 * @Description: (根据订单号修改状态) 
	 * @param code
	 * @return
	 */
	public int updateCrowdfundingLogStatus(String code);
	
	/**
	 * @Title: saveCrowdfundingOrder 
	 * @Description: (保存众筹订单) 
	 * @param order
	 * @return
	 */
	public int saveCrowdfundingOrder(CourseWebCrowdfundingOrder order);
	
	/**
	 * @Title: findCrowdfundingOrderInfo 
	 * @Description: (查询众筹订单) 
	 * @param code
	 * @return
	 */
	public CourseWebCrowdfundingOrder findCrowdfundingOrderInfo(String code);
	
	/**
	 * @Title: findCrowdfundingLogInfo 
	 * @Description: (查询众筹日志信息) 
	 * @param code
	 * @return
	 */
	public CourseWebCrowdfundingLog findCrowdfundingLogInfo(String code);
	
	/**
	 * @Title: findCrowdfundUUIDById 
	 * @Description: (根据ID查询UUID) 
	 * @param id
	 * @return
	 */
	public String findCrowdfundUUIDById(Long id);
	
	/**
	 * @Title: findCrowdfundingUsers 
	 * @Description: (查询用户们) 
	 * @param id
	 * @return
	 */
	public List<CourseWebCrowdfundingUser> findCrowdfundingUsers(Long id);
	
	public int updateUserCrowdfundWeixinInfo(Map<String,Object> params);
}
