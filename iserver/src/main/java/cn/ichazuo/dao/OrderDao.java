package cn.ichazuo.dao;

import java.util.List;
import java.util.Map;

import cn.ichazuo.model.table.CourseOrder;
import cn.ichazuo.model.table.JNWebCourseOrder;
import cn.ichazuo.model.table.JNWebCourseOrderUser;
import cn.ichazuo.model.table.LiveGiftOrder;
import cn.ichazuo.model.table.MemberRecord;
import cn.ichazuo.model.table.OnlineCourseWebOrder;
import cn.ichazuo.model.table.OnlineWebOrderUser;
import cn.ichazuo.model.table.SelfCourseOrder;
import cn.ichazuo.model.table.WebCourseOrder;
import cn.ichazuo.model.table.WebCourseOrderUser;

/**
 * @ClassName: OrderDao 
 * @Description: (订单DAO) 
 * @author ZhaoXu
 * @date 2015年7月21日 上午10:51:48 
 * @version V1.0
 */
public interface OrderDao {
	
	/**
	 * @Title: findOrderCountByOrderNo 
	 * @Description: (根据订单号查询订单数量) 
	 * @param orderNo
	 * @return
	 */
	public int findOrderCountByOrderNo(String orderNo);
	
	/**
	 * @Title: saveWebCourseOrder 
	 * @Description: (保存WEB课程订单) 
	 * @param order
	 * @return
	 */
	public int saveWebCourseOrder(WebCourseOrder order);
	
	/**
	 * @Title: updateCourseOrderByOrderNo 
	 * @Description: (更新WEB课程订单) 
	 * @param order
	 * @return
	 */
	public int updateCourseOrderByOrderNo(WebCourseOrder order);
	
	/**
	 * @Title: saveWebCourseOrder 
	 * @Description: (保存WEB课程订单) 
	 * @param order
	 * @return
	 */
	public int saveLiveGiftOrder(LiveGiftOrder order);
	
	/**
	 * @Title: updateCourseOrderByOrderNo 
	 * @Description: (更新WEB课程订单) 
	 * @param order
	 * @return
	 */
	public int updateLiveGiftOrderByOrderNo(LiveGiftOrder order);
	
	/**
	 * @Title: updateGiftDetailByOrderNo 
	 * @Description: (更新WEB课程订单) 
	 * @param order
	 * @return
	 */
	public int updateGiftDetailByOrderNo(WebCourseOrder order);
	
	/**
	 * @Title: findWebCourseOrder 
	 * @Description: (根据订单号查询WEB订单) 
	 * @param code
	 * @return
	 */
	public WebCourseOrder findWebCourseOrder(String code);
	
	/**
	 * @Title: findWebCourseOrder 
	 * @Description: (根据订单号查询WEB订单) 
	 * @param code
	 * @return
	 */
	public LiveGiftOrder findLiveGiftOrder(String code);
	
	/**
	 * @Title: updateWebCourseOrderStatus 
	 * @Description: (修改WEB课程订单状态) 
	 * @param id
	 * @return
	 */
	public int updateWebCourseOrderStatus(Long id);
	/**
	 * @Title: updateLiveGiftOrderStatus 
	 * @Description: (修改WEB课程订单状态) 
	 * @param id
	 * @return
	 */
	public int updateLiveGiftOrderStatus(Long id);
	
	/**
	 * @Title: findOrderInfoByOrderNo 
	 * @Description: (根据订单号查询订单信息) 
	 * @param orderNo 
	 * @return
	 */
	public CourseOrder findOrderInfoByOrderNo(String orderNo);
	
	/**
	 * @Title: saveCourseOrder 
	 * @Description: (保存课程订单) 
	 * @param order
	 * @return
	 */
	public int saveCourseOrder(CourseOrder order);
	
	/**
	 * @Title: updateCourseOrder 
	 * @Description: (修改课程订单) 
	 * @param order
	 * @return
	 */
	public int updateCourseOrder(CourseOrder order);
	
	/**
	 * @Title: saveWebCourseOrderUser 
	 * @Description: (保存web订单用户) 
	 * @param user
	 * @return
	 */
	public int saveWebCourseOrderUser(WebCourseOrderUser user);
	
	/**
	 * @Title: findWebCourseOrderUserByCourseId 
	 * @Description: (根据订单ID查询订单报名用户) 
	 * @param orderId
	 * @return
	 */
	public List<WebCourseOrderUser> findWebCourseOrderUserByCourseId(Long orderId);
	
	/**
	 * @Title: updateWebCourseOrderUserStatus 
	 * @Description: (修改web课程订单用户状态) 
	 * @param params
	 * @return
	 */
	public int updateWebCourseOrderUserStatus(Map<String,Object> params);
	
	/**
	 * @Title: updateWebCourseOrderUserId 
	 * @Description: (修改memberID) 
	 * @param params
	 * @return
	 */
	public int updateWebCourseOrderUserId(Map<String,Object> params);
	
	public Integer findWebOrderByUnionId(Map<String,Object> params);
	
	public String findWebOrderReviewStatus(Map<String,Object> params);
	
	public String findWebOrderCode(Map<String,Object> params);
	
	public WebCourseOrder findWebOrderByCourseIdUnionId(Map<String,Object> params);
	
	public int updateWebOrderWeixinInfo(Map<String,Object> params);
	
	/**
	 * @Title: saveOnlineWebOrderUser 
	 * @Description: (保存线上课程订单用户) 
	 * @param user
	 * @return
	 */
	public int saveOnlineWebOrderUser(OnlineWebOrderUser user);
	/**
	 * @Title: saveOnlineWebOrder 
	 * @Description: (保存线上课程订单) 
	 * @param order
	 * @return
	 */
	public int saveOnlineWebOrder(OnlineCourseWebOrder order);
	
	/**
	 * @Title: findOnlineWebOrderInfoByCode 
	 * @Description: (根据Code查询订单信息) 
	 * @param code
	 * @return
	 */
	public OnlineCourseWebOrder findOnlineWebOrderInfoByCode(String code);
	
	/**
	 * @Title: findOnlineWebOrderUserList 
	 * @Description: (查询线上课程报名用户信息列表) 
	 * @param orderId
	 * @return
	 */
	public List<OnlineWebOrderUser> findOnlineWebOrderUserList(Long orderId);
	
	/**
	 * @Title: updateMemberIdByOrderUserId 
	 * @Description: (线上课程支付成功后修改memberId) 
	 * @param map
	 * @return
	 */
	public int updateMemberIdByOrderUserId(Map<String,Object> map);
	
	/**
	 * @Title: updateOnlineOrderStatus 
	 * @Description: (修改线上课程订单状态) 
	 * @param id
	 * @return
	 */
	public int updateOnlineOrderStatus(Long id);
	
	/**
	 * @Title: findOnlineCourseBugCount 
	 * @Description: (查询用户购买数量) 
	 * @param map
	 * @return
	 */
	public int findOnlineCourseBugCount(Map<String,Object> map);
	/**
	 * @Title: saveJNWebCourseOrder 
	 * @Description: (保存WEB课程订单) 
	 * @param order
	 * @return
	 */
	public int saveJNWebCourseOrder(JNWebCourseOrder order);
	
	/**
	 * @Title: saveJNWebCourseOrderUser 
	 * @Description: (保存极牛web订单用户) 
	 * @param user
	 * @return
	 */
	public int saveJNWebCourseOrderUser(JNWebCourseOrderUser user);
	
	/**
	 * @Title: findJNWebCourseOrder 
	 * @Description: (根据订单号查询WEB订单) 
	 * @param code
	 * @return
	 */
	public JNWebCourseOrder findJNWebCourseOrder(String code);
	
	/**
	 * @Title: findJNWebCourseOrderUserByCourseId 
	 * @Description: (根据订单ID查询订单报名用户) 
	 * @param orderId
	 * @return
	 */
	public List<JNWebCourseOrderUser> findJNWebCourseOrderUserByCourseId(Long orderId);
	
	/**
	 * @Title: updateWebCourseOrderUserId 
	 * @Description: (修改memberID) 
	 * @param params
	 * @return
	 */
	public int updateJNWebCourseOrderUserId(Map<String,Object> params);
	
	/**
	 * @Title: updateJNWebCourseOrderStatus 
	 * @Description: (修改WEB课程订单状态) 
	 * @param id
	 * @return
	 */
	public int updateJNWebCourseOrderStatus(Long id);
	/**
	 * 根据课程Id和微信用户unionid去查询微信用户是否购买了课程
	 * @param params
	 * @return
	 */
	public List<JNWebCourseOrder> getWhetherBuyCourseByUnionid(Map<String,Object> params);
	
	/**
	 * 根据课程Id和微信用户unionid去查询微信用户是否购买了课程
	 * @param params
	 * @return
	 */
	public List<JNWebCourseOrder> getWhetherYearByUnionid(Map<String,Object> params);
	
	/**
	 * 根据课程Id和微信用户unionid去查询微信用户是否购买了大课课程名额
	 * @param params
	 * @return
	 */
	public List<WebCourseOrder> getIfBuyCourseByUnionid(Map<String,Object> params);
	public List<WebCourseOrder> getIfBuyCourseByMobile(Map<String,Object> params);
	/**
	 * 根据unionId查询学员购买记录
	 * @param params
	 * @return
	 */
	public List<SelfCourseOrder> queryOrderListByUnionid(Map<String,Object> params);
	/**
	 * 根据手机号查询学员购买线上课程订单记录
	 * @param params
	 * @return
	 */
	public List<SelfCourseOrder> queryOrderListByMobile(Map<String,Object> params);
	
	/**
	 * 根据unionId查询学员购买记录
	 * @param params
	 * @return
	 */
	public List<SelfCourseOrder> queryOrderListByUnionidAndCourseId(Map<String,Object> params);
	public List<MemberRecord> queryMemberRecordByUnionId(Map<String,Object> params);
	public List<MemberRecord> queryMemberRecordByUnionIdAndCatalogId(Map<String,Object> params);
	
	/**
	 * 根据orderNo查询学员购买记录
	 * @param params
	 * @return
	 */
	public List<WebCourseOrder> getOrderByOrderNo(Map<String,Object> params);
	
}
