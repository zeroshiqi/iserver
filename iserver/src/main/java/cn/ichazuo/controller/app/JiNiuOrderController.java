package cn.ichazuo.controller.app;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.ichazuo.commons.base.BaseController;
import cn.ichazuo.commons.component.ConfigInfo;
import cn.ichazuo.commons.util.CodeUtils;
import cn.ichazuo.commons.util.DateUtils;
import cn.ichazuo.commons.util.NumberUtils;
import cn.ichazuo.commons.util.PasswdEncryption;
import cn.ichazuo.commons.util.StringUtils;
import cn.ichazuo.commons.util.alipay.util.AlipayNotify;
import cn.ichazuo.commons.util.model.Params;
import cn.ichazuo.model.admin.OfflineMsgInfo;
import cn.ichazuo.model.app.CourseWebCrowdfundingInfo;
import cn.ichazuo.model.app.WebCourseOrderInfo;
import cn.ichazuo.model.table.Course;
import cn.ichazuo.model.table.CourseOfflineJoin;
import cn.ichazuo.model.table.CourseOrder;
import cn.ichazuo.model.table.CourseWebCrowdfunding;
import cn.ichazuo.model.table.CourseWebCrowdfundingLog;
import cn.ichazuo.model.table.CourseWebCrowdfundingOrder;
import cn.ichazuo.model.table.CourseWebCrowdfundingUser;
import cn.ichazuo.model.table.JNCourseOnlineJoin;
import cn.ichazuo.model.table.JNWebCourseOrder;
import cn.ichazuo.model.table.JNWebCourseOrderUser;
import cn.ichazuo.model.table.Member;
import cn.ichazuo.model.table.OfflineCourse;
import cn.ichazuo.model.table.Ticket;
import cn.ichazuo.model.table.WebCourseOrder;
import cn.ichazuo.model.table.WebCourseOrderUser;
import cn.ichazuo.service.CommonService;
import cn.ichazuo.service.CourseService;
import cn.ichazuo.service.CrowdfundingService;
import cn.ichazuo.service.MemberService;
import cn.ichazuo.service.OrderService;

/**
 * @ClassName: OrderController
 * @Description: (订单Controller)
 * @author ZhaoXu
 * @date 2015年7月19日 上午2:28:03
 * @version V1.0
 */
@RequestMapping("/app")
@Controller("app.jiniuOrderController")
public class JiNiuOrderController extends BaseController {

	// 短信内容
//	public static final String msg = "【插坐学院】#name#，恭喜您成功报名插坐学院#courseName#的课程！我们将于#time#准时开课。具体上课信息，辅导员会在开课前一周发短信通知呦！";
	public static final String msg = "【插坐学院】#name#，恭喜您成功报名插坐学院的线下课程！我们将于#time#准时开课。具体上课信息，辅导员会在开课前一周发短信通知呦！";

	//带有上课地址的短信内容
//	public static final String msgAddress = "【插坐学院】#name#，恭喜您成功报名插坐学院#courseName#的课程！我们将于#time#准时开课。上课地址：#address#";
	public static final String msgAddress = "【插坐学院】#name#，恭喜您成功报名插坐学院的线下课程！我们将于#time#准时开课。上课地址：#address#。不要迟到哦！";
	public static final String msg144 = "【插坐学院】#name#，恭喜您成功购买：插坐学院2016线上课程全年通票，并获得首批内测资格！全年通票有效期为：2016年2月1日至2017年2月1日。辅导员将在2月1日之前，拉您进入插坐学院专群。希望您能在新的一年中变得更好。";
//	public static final String msgQRCode = "【插坐学院】#name#，恭喜您成功报名插坐学院#courseName#的课程！我们将于#time#准时开课。请点击链接：#qrcode#扫描二维码进入听课群";
	public static final String msgQRCode = "【插坐学院】#name#，恭喜您成功报名插坐学院线上课程！我们将于#time#准时开课。";
	public static final String msgBeijing = "【插坐学院】";
	
	@Autowired
	private CommonService commonService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private ConfigInfo configInfo;
	@Autowired
	private CourseService courseService;
	@Autowired
	private CrowdfundingService crowdfundingService;
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * @Title: getCourseOrderCode
	 * @Description: (获得课程订单号)
	 * @param courseId
	 *            课程ID
	 * @param userId
	 *            用户ID
	 * @param simpleName
	 *            城市简称
	 * @return
	 */
//	@ResponseBody
//	@RequestMapping("/getLivingCourseOrderCode")
//	public JSONObject getLivingCourseOrderCode(Long courseId, Long userId, String simpleName, Double price) {
//		try {
//			if (NumberUtils.isNullOrZero(courseId) || NumberUtils.isNullOrZero(userId)
//					|| StringUtils.isNullOrEmpty(simpleName) || price == null) {
//				return error("参数错误!");
//			}
//			String code = CodeUtils.getCourseOrderCode(simpleName, courseId, userId);
//			if (orderService.getCountByOrderNo(code) > 0) {
//				return ok("生成成功", code);
//			}
//			Member member = memberService.findMemberById(userId);
//			if (member == null) {
//				return error("参数错误");
//			}
//			CourseOrder order = new CourseOrder();
//			order.setMemberId(userId);
//			order.setCourseId(courseId);
//			order.setCash(price);
//			order.setOrderNo(code);
//			orderService.saveCourseOrder(order);
//			return ok("生成成功", code);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return error(APP_SYSTEM_ERROR);
//		}
//	}
//
//	/**
//	 * @Title: aliPayNotify
//	 * @Description: (支付宝支付回调)
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping("/aliPayLivingNotify")
//	public String aliPayLivingNotify(HttpServletRequest request, HttpServletResponse response) {
//		try {
//			// 获取支付宝POST过来反馈信息
//			Map<String, String> params = new HashMap<String, String>();
//			Map<String, String[]> requestParams = request.getParameterMap();
//			for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
//				String name = iter.next();
//				String[] values = (String[]) requestParams.get(name);
//				String valueStr = "";
//				for (int i = 0; i < values.length; i++) {
//					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
//				}
//				// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
//				// valueStr = new String(valueStr.getBytes("ISO-8859-1"),
//				// "gbk");
//				params.put(name, valueStr);
//			}
//
//			// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
//			// 商户订单号
//			String outTradeNo = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
//
//			// 支付宝交易号
//			String tradeNo = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
//
//			// 交易状态
//			String tradeStatus = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
//
//			// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
//			if (AlipayNotify.verify(params, configInfo.getAliPayPartner())) {// 验证成功
//				//////////////////////////////////////////////////////////////////////////////////////////
//				// ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
//				if (tradeStatus.equals("TRADE_SUCCESS")) {
//					CourseOrder order = orderService.findCourseOrderByOrderNo(outTradeNo);
//					if (order == null) {
//						return "success";
//					}
//					order.setWay(1); // 1:支付宝支付
//					order.setTradeNo(tradeNo);
//					order.setNotifyTime(new Date());
//					order.setTradeStatus(tradeStatus);
//					order.setPayTime(new Date());
//					order.setStatus(1);
//					// 修改订单信息
//					orderService.updateCourseOrder(order, 1, 3);
//				}
//
//				// ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
//				return "success";
//
//				//////////////////////////////////////////////////////////////////////////////////////////
//			} else {// 验证失败
//				return "fail";
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "fail";
//		}
//	}
//
//	/**
//	 * @Title: weixinPayNotify
//	 * @Description: (微信支付回调)
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping("/weixinLivingPayNotify")
//	public String weixinLivingPayNotify(HttpServletRequest request, HttpServletResponse response) {
//		String resXml = "";
//		try {
//			InputStream inStream = request.getInputStream();
//			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
//			byte[] buffer = new byte[1024];
//			int len = 0;
//			while ((len = inStream.read(buffer)) != -1) {
//				outSteam.write(buffer, 0, len);
//			}
//			outSteam.close();
//			inStream.close();
//
//			String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息
//
//			Map<String, String> m = parseXmlToMap(result);
//
//			if ("SUCCESS".equals(m.get("result_code").toString())) {
//				
//				String tradeNo = m.get("out_trade_no").toString();
//				logger.error("微信回调成功，订单号为："+tradeNo);
//				// 支付成功
//				CourseOrder order = orderService.findCourseOrderByOrderNo(tradeNo);
//				if (order == null) {
//					// 支付成功
//					resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
//							+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
//					BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
//					out.write(resXml.getBytes());
//					out.flush();
//					out.close();
//					return null;
//				} else {
//					order.setWay(2); // 2:微信支付
//					order.setTradeNo(tradeNo);
//					order.setNotifyTime(new Date());
//					order.setTradeStatus("TRADE_SUCCESS");
//					order.setPayTime(new Date());
//					order.setStatus(1);
//
//					// 修改订单信息
//					orderService.updateCourseOrder(order, 1, 2);
//				}
//
//				// 支付成功
//				resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
//						+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
//			} else {
//				resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
//						+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
//			}
//			BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
//			out.write(resXml.getBytes());
//			out.flush();
//			out.close();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	/**
//	 * @Title: weixinWebPayNotify
//	 * @Description: (Web微信支付回调)
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping("/weixinLivingWebPayNotify")
//	public String weixinLivingWebPayNotify(HttpServletRequest request, HttpServletResponse response) {
//		String resXml = "";
//		try {
//			InputStream inStream = request.getInputStream();
//			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
//			byte[] buffer = new byte[1024];
//			int len = 0;
//			while ((len = inStream.read(buffer)) != -1) {
//				outSteam.write(buffer, 0, len);
//			}
//			outSteam.close();
//			inStream.close();
//
//			String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息
//
//			Map<String, String> m = parseXmlToMap(result);
//
//			if ("SUCCESS".equals(m.get("result_code").toString())) {
//				String tradeNo = m.get("out_trade_no").toString();
//				// 支付成功
//				JNWebCourseOrder order = orderService.findJNWebCourseOrder(tradeNo);
//				logger.error("微信支付成功，订单号："+order.getOrderCode());
//				//获取订单的Id，用于存储在offline_join表中
//				Long orderId = order.getId();
//				logger.error("微信支付成功，订单id："+orderId);
//				if (order == null || order.getStatus() == 1) {
//					// 支付成功
//					resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
//							+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
//					BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
//					out.write(resXml.getBytes());
//					out.flush();
//					out.close();
//					return null;
//				} else {
//
//					List<JNWebCourseOrderUser> userList = orderService.findJNWebCourseOrderUserByCourseId(order.getId());
//
//					OfflineMsgInfo offline = courseService.findOfflineMsgInfo(order.getCourseId());
//					for (JNWebCourseOrderUser user : userList) {
//						Member me = memberService.findLoginMemberByMobile(user.getMobile());
//						Long memberId = (me == null ? null : me.getId());
//						logger.error("memberId的值为："+memberId);
//						if (NumberUtils.isNullOrZero(memberId)) {
//							logger.error("memberId的值判断为NullOrZero");
//							Member member = new Member();
//							member.setMobile(user.getMobile());
//							member.setPassword(PasswdEncryption.generate("123456"));
//							member.setNickName(user.getName());
//							member.setAccessToken(CodeUtils.getUUID());
//							member.setClientVersion("1.0");
//							member.setDeviceId("null");
//							// 保存默认头像
//							member.setAvatar(commonService.getRandomAvatar());
//							logger.error("新增用户参数：mobile："+user.getMobile()+";userName:"+user.getName());
//							memberService.register(member, null, user.getWork());
//							Member member1 = memberService.findLoginMemberByMobile(user.getMobile());
//							
//							memberId = member.getId();
//							logger.error("memberId的值:"+memberId);
//							Long memberId1 = member1.getId();
//							logger.error("memberId1的值:"+memberId1);
//							// 更新ID
//							orderService.updateJNWebCourseOrderUserId(memberId, user.getId());
//						}
//						
//						JNCourseOnlineJoin join = courseService.findJNCourseJoinInfo(order.getCourseId(), memberId);
//
//						if(join == null){
//							// 同时保存报名信息
//							join = new JNCourseOnlineJoin();
//							join.setCourseId(order.getCourseId());
//							join.setMemberId(memberId);
//							join.setOrderId(orderId);
//							join.setType(2); // WEB
//							join.setFrom(2);
//							courseService.saveJNCourseJoin(join);
//							
//							//发短信
////							if (!configInfo.getProjectTest()) {
////								logger.error("发送短信参数:手机号："+user.getMobile()+"学员姓名："+user.getName()+"课程名称："+offline.getCityName()+"课程地址："+offline.getAddress());
////								if(order.getCourseId() == 144){
////									commonService.send(user.getMobile(),msg144.replace("#name#", user.getName()));
////								}else{
////									
////									if(offline.getTypeId() == 28){
////										String url = commonService.findQRCode(order.getCourseId());
////										if(offline.getBeginTime()!=null && offline.getEndTime()!= null){
////											if(DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR).equals(DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY_YEAR))){
////												String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
////												commonService.send(user.getMobile(),
////														msgQRCode.replace("#name#", user.getName())
////																.replace("#time#",classDate));
////											}else{
////												String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
////												commonService.send(user.getMobile(),
////														msgQRCode.replace("#name#", user.getName())
////																.replace("#time#",classDate));
////											}
////											
////										}else{
////											commonService.send(user.getMobile(),
////													msgQRCode.replace("#name#", user.getName())
////															.replace("#time#",
////																	DateUtils.formatDate(offline.getBeginTime(),
////																			DateUtils.MONTH_DAY_YEAR)));
////										}
//////												.replace("#city#", offline.getCityName()).replace("#courseName#", offline.getCourseName()).replace("#qrcode#", url))
////									}else{
////										if(offline.getAddress()=="" || "".equals(offline.getAddress())){
////											logger.error("课程名称："+offline.getCourseName()+"的开始地址为："+offline.getAddress()+"程序判断为非空，发送msg短信内容！");
////											if(offline.getBeginTime()!=null && offline.getEndTime()!= null){
////												if(DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR).equals(DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY_YEAR))){
////													String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
////													commonService.send(user.getMobile(),
////															msg.replace("#name#", user.getName())
////																	.replace("#time#",classDate));
////												}else{
////													String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
////													commonService.send(user.getMobile(),
////															msg.replace("#name#", user.getName())
////																	.replace("#time#",classDate));
////												}
////											}else{
////												commonService.send(user.getMobile(),
////														msg.replace("#name#", user.getName())
////																.replace("#time#",
////																		DateUtils.formatDate(offline.getBeginTime(),
////																				DateUtils.MONTH_DAY_YEAR)));
////											}
//////													.replace("#city#", offline.getCityName()).replace("#courseName#", offline.getCourseName()));
////											logger.error("最终发送短信的内容为："+msg);
////										}else{
////											logger.error("课程名称："+offline.getCourseName()+"的开始地址为："+offline.getAddress()+"程序判断为空，发送msgAddress短信内容！");
////											if(offline.getBeginTime()!=null && offline.getEndTime()!= null){
////												if(DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR).equals(DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY_YEAR))){
////													String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
////													commonService.send(user.getMobile(),
////															msgAddress.replace("#name#", user.getName())
////																	.replace("#time#",
////																			classDate)
////															.replace("#address#", offline.getAddress()).replace("#address#", offline.getAddress()));
////												}else{
////													String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
////													commonService.send(user.getMobile(),
////															msgAddress.replace("#name#", user.getName())
////																	.replace("#time#",
////																			classDate)
////															.replace("#address#", offline.getAddress()).replace("#address#", offline.getAddress()));
////												}
////											}else{
////												commonService.send(user.getMobile(),
////														msgAddress.replace("#name#", user.getName())
////																.replace("#time#",
////																		DateUtils.formatDate(offline.getBeginTime(),
////																				DateUtils.MONTH_DAY_YEAR))
////														.replace("#address#", offline.getAddress()).replace("#address#", offline.getAddress()));
////											}
////											logger.error("最终发送短信的内容为："+msgAddress);
////										}
////									}
////								}
////							}
//						}
//						
////						if(order.getTicketStatus() == 1){
////							Params params = new Params();
////							Long id = commonService.findHaveTicket(order.getUnionid());
////							if (id != null) {
////								params.putData("id", id);
////								params.putData("unionId", order.getUnionid());
////								params.putData("status", 3);
////
////								commonService.updateTicketStatus(params);
////							}
////						}
//					}
//
//					// 修改订单信息
//					orderService.updateJNWebCourseOrderStatus(order.getId(), userList.size(), order.getCourseId());
//				}
//
//				// 支付成功
//				resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
//						+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
//			} else {
//				resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
//						+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
//			}
//
//			System.out.println("微信支付回调数据结束");
//
//			BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
//			out.write(resXml.getBytes());
//			out.flush();
//			out.close();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	/**
//	 * @Title: saveWebCourseOrder
//	 * @Description: (提交预订单)
//	 * @param sex
//	 *            性别
//	 * @param content
//	 *            内容
//	 * @param name
//	 *            姓名
//	 * @param mobile
//	 *            手机号
//	 * @param weixin
//	 *            微信号
//	 * @param courseId
//	 *            课程ID
//	 * @param unionid
//	 *            唯一ID
//	 * @param openid
//	 *            openId
//	 * @param number
//	 *            购买数量
//	 * @param ip
//	 *            ip地址
//	 * @param ticketStatus 是否使用奖学金 1:使用 0:不使用
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping("/saveLivingWebCourseOrder")
//	public JSONObject saveLivingWebCourseOrder(String[] sex, String[] content, String[] name, String[] mobile, String[] work,
//			String[] weixin, Long courseId, String unionid, String openid, String ip, String nickname,
//			String weixinSex, String avatar, Integer from,Integer ticketStatus,Integer weixinType,Integer qrcode,String formNickName,String joinReason,HttpServletRequest requestm,String job,String buyIntentions,String email) {
//		
//		if(StringUtils.isNullOrEmpty(ip)){
////			ip = request.getRemoteAddr();
//			ip = "47.153.191.255";
//		}
//		//weixinType 是否为 1 判断是JSAPI 还是 NATIVE
//		try {
//			if(ticketStatus == null){
//				ticketStatus = 0;
//			}
//			if (sex.length != name.length && name.length != mobile.length && mobile.length != name.length
//					&& weixin.length != name.length && name.length == 0) {
//				return error("参数错误!");
//			}
//			int number = name.length;
//			if (NumberUtils.isNullOrZero(courseId) || StringUtils.isNullOrEmpty(openid)
//					|| StringUtils.isNullOrEmpty(unionid) || NumberUtils.isNullOrZero(number)) {
//				return error("参数错误!");
//			}
////			Course course = courseService.findCourseById(courseId);
////			OfflineCourse offline = courseService.findOfflineCourseByCourseId(courseId);
////			if (offline == null || course == null) {
////				return error("参数错误");
////			}
////			int joinNumber = courseService.findCourseJoinCount(courseId);
////			if (joinNumber + number > offline.getStudentNum()) {
////				int temp = offline.getStudentNum() - joinNumber;
////				return status(800, "购买数量过多", temp >= 0 ? temp : 0);
////			}
//			
////			if(offline.getCourseId() == 129){
////				return status(800, "购买数量过多", 0);
////			}
//			//默认报名截止时间为课程开讲前一天
//			LocalDate now = LocalDate.now();
////			LocalDate begin = LocalDate.parse(DateUtils.formatDate(course.getBeginTime(), DateUtils.DATE_FORMAT_NORMAL));
////			if (now.isAfter(begin)){
////				return status(800, "报名时间已过", 0);
////			}
//			
//			Double price = 0.0d;
////			if(!NumberUtils.isNullOrZero(qrcode) && courseId == 129L){
////				price = NumberUtils.mul(offline.getPrice(), 0.88);
////			}else{
////				if(offline.getDiscount() == 100){
////					//不打折
////					price = NumberUtils.mul(offline.getPrice(), number); // 计算价格
////				}else{
//					//计算价格
////					for (int i = 0; i < mobile.length; i++) {
////						// 查询用户
////						Member member = memberService.findLoginMemberByMobile(mobile[i]);
////						if (member == null) {
////							price = NumberUtils.add(price, offline.getPrice());
////						}else{
////							Integer count = courseService.findMyJoinCourseCount(member.getId());
////							if(count > 0){
////								// 计算折扣
////								price = NumberUtils.mul(offline.getPrice(), NumberUtils.div(offline.getDiscount(), 100));
////							}else{
////								price = NumberUtils.add(price, offline.getPrice());
////							}
////						}
////					}
////				}
////			}
//			
//			
////			if(ticketStatus == 1){
////				//查询奖学金ID
////				Long id = commonService.findHaveTicket(unionid);
////				if (id == null) {
////					return status(300, "没有奖学金");
////				}
////				Ticket ticket = commonService.findTicket(id);
////				price = NumberUtils.sub(price, ticket.getPrice());
////			}
//			
//			// 生成订单号
//			String code = CodeUtils.getCourseOrderCode("JN", courseId, Long.valueOf(CodeUtils.getRandomInt(100) + 1));
//
//			String nonceStr = CodeUtils.getUUID();// 随机字符串
//
//			SortedMap<String, String> par = new TreeMap<>();
//			par.put("appid", appid);
//			par.put("attach", String.valueOf(number));
//			par.put("body", "ChaZuoMBA:" + code);
//			par.put("device_info", "WEB");
//			par.put("nonce_str", nonceStr);
//			par.put("mch_id", mchId);
//
//			StringBuffer sbWX = new StringBuffer();
//			sbWX.append("<xml>").append("<appid>").append(appid).append("</appid>");
//
//			sbWX.append("<attach><![CDATA[").append(String.valueOf(number)).append("]]></attach>");
//			sbWX.append("<body><![CDATA[").append("ChaZuoMBA:" + code).append("]]></body>");
//
//			sbWX.append("<device_info>").append("WEB").append("</device_info>");
//			sbWX.append("<mch_id>").append(mchId).append("</mch_id>");
//			sbWX.append("<nonce_str>").append(nonceStr).append("</nonce_str>");
//			if (configInfo.getProjectTest()) {
//				par.put("notify_url", configInfo.getWxTestRestUrl());
//				sbWX.append("<notify_url>").append("http://123.57.84.121:8080/iserver/app/weixinLivingWebPayNotify").append("</notify_url>");
//			} else {
//				par.put("notify_url", configInfo.getWxProRestUrl());
//				sbWX.append("<notify_url>").append("http://112.124.13.195/iserver/live/weixinLivingWebPayNotify").append("</notify_url>");
//			}
//			sbWX.append("<openid>").append(openid).append("</openid>");
//			sbWX.append("<out_trade_no>").append(code).append("</out_trade_no>");
//			sbWX.append("<spbill_create_ip>").append(ip).append("</spbill_create_ip>");
//			int pr = (int) NumberUtils.mul(price, 100);
//			sbWX.append("<total_fee>").append(pr).append("</total_fee>");
//			if(NumberUtils.isNullOrZero(weixinType)){
//				sbWX.append("<trade_type>").append("JSAPI").append("</trade_type>");
//			}else if(weixinType == 1){
//				sbWX.append("<trade_type>").append("NATIVE").append("</trade_type>");
//			}
//			
//			sbWX.append("<attach>").append(number).append("</attach>");
//
//			par.put("openid", openid);
//			par.put("out_trade_no", code);
//			par.put("spbill_create_ip", ip);
//			par.put("total_fee", pr + "");
//			if(NumberUtils.isNullOrZero(weixinType)){
//				par.put("trade_type", "JSAPI");
//			}else if(weixinType == 1){
//				par.put("trade_type", "NATIVE");
//			}
//			
//			String sign = commonService.createSign(par, apiKey);
//
//			sbWX.append("<sign><![CDATA[").append(sign).append("]]></sign>");
//			sbWX.append("</xml>");
//
//			HttpClient client = HttpClients.createDefault();
//			HttpPost post = new HttpPost("https://api.mch.weixin.qq.com/pay/unifiedorder");
//			post.setHeader("Content-type", "text/xml; charset=UTF-8");
//			StringEntity entity = new StringEntity(sbWX.toString());
//			post.setEntity(entity);
//
//			HttpResponse response = client.execute(post);
//			HttpEntity entity2 = response.getEntity();
//			if (entity2 != null) {
//				String result2 = EntityUtils.toString(entity2, "UTF-8");
//
//				Map<String, String> result = parseXmlToMap(result2);
//
//				WebCourseOrderInfo info = new WebCourseOrderInfo();
//				info.setAppid(appid);
//				info.setPrepayId(result.get("prepay_id"));
//				info.setNonceStr(nonceStr);
//				info.setOrderCode(code);
//				info.setSign(sign);
//				info.setMchId(mchId);
//				info.setApiKey(apiKey);
//				if(weixinType == 1){
//					info.setCodeUrl(result.get("code_url"));
//				}
//				
//				JNWebCourseOrder order = new JNWebCourseOrder();
//				order.setCourseId(courseId);
//				order.setIp(ip);
//				order.setNumber(number);
//				order.setOpenid(openid);
//				order.setOrderCode(code);
//				order.setPrice(price);
//				order.setStatus(0);
//				order.setUnionid(unionid);
//				order.setType(0);
//				order.setNickname(nickname);
//				order.setWeixinSex(weixinSex);
//				order.setAvatar(avatar);
//				order.setFrom(NumberUtils.isNullOrZero(from) ? 0 : from);
//				order.setTicketStatus(ticketStatus);
//				order.setFormNickName(formNickName);
//				order.setJoinReason(joinReason);
//				order.setJob(job);
//				order.setBuyIntentions(buyIntentions);
//				order.setEmail(email);
//				orderService.saveJNWebCourseOrder(order);
//
//				List<JNWebCourseOrderUser> ulist = new ArrayList<JNWebCourseOrderUser>();
//				for (int i = 0; i < name.length; i++) {
//					Long id = 0L;
//					// 查询用户
//					Member member = memberService.findLoginMemberByMobile(mobile[i]);
//					if (member != null) {
//						id = member.getId();
//					}
//					if(id != 0){
//						if (courseService.findCourseJoinInfo(courseId, id) != null) {
//							return status(600, "已报过名..", mobile[i]);
//						}	
//					}
//					
//					JNWebCourseOrderUser user = new JNWebCourseOrderUser();
//					if (content.length==0){
//						user.setContent("");
//					}else{
//						user.setContent(
//								StringUtils.isNullOrEmpty(content[i]) || content[i].equals("") ? "" : content[i]);
//					}
//					user.setMemberId(member == null ? 0L : member.getId());
//					user.setMobile(mobile[i]);
//					//获取手机号码归属地
////					user.setCity(memberService.getMobileFrom(mobile[i]));
//					String str = null;
//					String province="";
//					String city="";
//					JSONArray jsonArray = null;
//					str = "[" +memberService.request(mobile[i]) + "]";
//					jsonArray = new JSONArray(str);
//					int errNumResult = (int) jsonArray.getJSONObject(0).get("errNum");
//					if(errNumResult == 0){
//						org.json.JSONObject jsonresult = (org.json.JSONObject) jsonArray.getJSONObject(0).get("retData");
//						province = jsonresult.getString("province");
//						city = jsonresult.getString("city");
//					}
//					user.setCity(StringUtils.isNullOrEmpty(city) ? "" : city);
//					user.setProvince(StringUtils.isNullOrEmpty(province) ? "" : province);
//					user.setName(name[i]);
//					user.setOrderId(order.getId());
//					if (sex[i].equals("man")) {
//						user.setSex("男");
//					} else {
//						user.setSex("女");
//					}
//					user.setWeixin(weixin[i]);
//					user.setWork(work[i]);
//
//					ulist.add(user);
//				}
//				orderService.saveJNWebCourseOrderUser(ulist);
////				WebCourseOrder orderq = orderService.findWebCourseOrder(code);
//				//获取订单的Id，用于存储在offline_join表中
//				Long orderId = order.getId();
//				JNCourseOnlineJoin join = new JNCourseOnlineJoin();
//				join.setCourseId(order.getCourseId());
//				join.setMemberId(Long.valueOf("0"));
//				join.setOrderId(orderId);
//				join.setType(2); // WEB
//				join.setFrom(2);
//				courseService.saveJNCourseJoin(join);
//				return ok("生成成功", info);
//			}
//
//			return error("生成失败");
//		} catch (Exception e) {
//			e.printStackTrace();
//			return error(APP_SYSTEM_ERROR);
//		}
//	}
//
//	/**
//	 * @Title: payCrowdfunding
//	 * @Description: (支付众筹)
//	 * @param id
//	 *            众筹ID UUID
//	 * @param unionId
//	 *            微信唯一ID
//	 * @param price
//	 *            价格
//	 * @param openid
//	 *            openid
//	 * @param ip
//	 *            ip地址
//	 * @param avatar
//	 *            头像
//	 * @param content
//	 *            说的话
//	 * @param name
//	 *            微信名称
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping("/payLivingCrowdfunding")
//	public JSONObject payLivingCrowdfunding(String id, String unionId, Double price, String openid, String ip, String avatar,
//			String content, String name) {
//		try {
//			if (StringUtils.isNullOrEmpty(id) || NumberUtils.isNullOrZero(price) || StringUtils.isNullOrEmpty(unionId)
//					|| StringUtils.isNullOrEmpty(openid) || StringUtils.isNullOrEmpty(ip)
//					|| StringUtils.isNullOrEmpty(avatar)) {
//				return error("参数缺失");
//			}
//
//			CourseWebCrowdfunding course = crowdfundingService.findUserCrowdfundInfoByUUID(id);
//			if (course == null) {
//				return error("信息错误");
//			}
//
//			double p = crowdfundingService.findCrowdfundingPriceCount(id);
//			double subPrice = NumberUtils.sub(course.getPrice(), p);
//
//			if (subPrice < price) {
//				return status(666, "超出金额", subPrice);
//			}
//
//			// 生成订单号
//			String code = CodeUtils.getCourseOrderCode("WEB", Long.valueOf(CodeUtils.getRandomInt(50)),
//					Long.valueOf(CodeUtils.getRandomInt(100) + 1));
//			String nonceStr = CodeUtils.getUUID();// 随机字符串
//
//			SortedMap<String, String> par = new TreeMap<>();
//			par.put("appid", appid);
//			par.put("attach", "2");
//			par.put("body", "ChaZuoMBA:" + code);
//			par.put("device_info", "WEB");
//			par.put("nonce_str", nonceStr);
//			par.put("mch_id", mchId);
//
//			StringBuffer sbWX = new StringBuffer();
//			sbWX.append("<xml>").append("<appid>").append(appid).append("</appid>");
//
//			sbWX.append("<attach><![CDATA[").append("2").append("]]></attach>");
//			sbWX.append("<body><![CDATA[").append("ChaZuoMBA:" + code).append("]]></body>");
//
//			sbWX.append("<device_info>").append("WEB").append("</device_info>");
//			sbWX.append("<mch_id>").append(mchId).append("</mch_id>");
//			sbWX.append("<nonce_str>").append(nonceStr).append("</nonce_str>");
//			if (configInfo.getProjectTest()) {
//				par.put("notify_url", configInfo.getWxCrowdTestUrl());
//				sbWX.append("<notify_url>").append(configInfo.getWxCrowdTestUrl()).append("</notify_url>");
//			} else {
//				par.put("notify_url", configInfo.getWxCrowdProUrl());
//				sbWX.append("<notify_url>").append(configInfo.getWxCrowdProUrl()).append("</notify_url>");
//			}
//			sbWX.append("<openid>").append(openid).append("</openid>");
//			sbWX.append("<out_trade_no>").append(code).append("</out_trade_no>");
//			sbWX.append("<spbill_create_ip>").append(ip).append("</spbill_create_ip>");
//			int pr = (int) NumberUtils.mul(price, 100);
//			sbWX.append("<total_fee>").append(pr).append("</total_fee>");
//			sbWX.append("<trade_type>").append("JSAPI").append("</trade_type>");
//			sbWX.append("<attach>").append("2").append("</attach>");
//
//			par.put("openid", openid);
//			par.put("out_trade_no", code);
//			par.put("spbill_create_ip", ip);
//			par.put("total_fee", pr + "");
//			par.put("trade_type", "JSAPI");
//			String sign = commonService.createSign(par, apiKey);
//
//			sbWX.append("<sign><![CDATA[").append(sign).append("]]></sign>");
//			sbWX.append("</xml>");
//
//			HttpClient client = HttpClients.createDefault();
//			HttpPost post = new HttpPost("https://api.mch.weixin.qq.com/pay/unifiedorder");
//			post.setHeader("Content-type", "text/xml; charset=UTF-8");
//			StringEntity entity = new StringEntity(sbWX.toString());
//			post.setEntity(entity);
//
//			HttpResponse response = client.execute(post);
//			HttpEntity entity2 = response.getEntity();
//			if (entity2 != null) {
//				String result2 = EntityUtils.toString(entity2, "UTF-8");
//				Map<String, String> result = parseXmlToMap(result2);
//
//				CourseWebCrowdfundingLog log = new CourseWebCrowdfundingLog();
//				log.setAvatar(avatar);
//				log.setCode(code);
//				log.setContent(content);
//				log.setCrowdfundId(course.getId());
//				log.setName(name);
//				log.setPrice(price);
//				log.setUnionId(unionId);
//				log.setStatus(0);
//				log.setType(0);
//
//				crowdfundingService.saveCrowdfundingLog(log);
//
//				WebCourseOrderInfo info = new WebCourseOrderInfo();
//				info.setAppid(appid);
//				info.setPrepayId(result.get("prepay_id"));
//				info.setNonceStr(nonceStr);
//				info.setOrderCode(code);
//				info.setSign(sign);
//				info.setApiKey(apiKey);
//
//				return ok("生成成功", info);
//			}
//
//			return error("生成失败");
//		} catch (Exception e) {
//			e.printStackTrace();
//			return error(APP_SYSTEM_ERROR);
//		}
//	}
//
//	/**
//	 * @Title: saveCrowdfundingOrder
//	 * @Description: (支付剩余)
//	 * @param id
//	 *            众筹ID uuid
//	 * @param unionId
//	 *            微信唯一ID
//	 * @param openid
//	 *            openid
//	 * @param ip
//	 *            ip地址
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping("/saveLivingCrowdfundingOrder")
//	public JSONObject saveLivingCrowdfundingOrder(String id, String unionId, String openid, String ip) {
//		try {
//			if (StringUtils.isNullOrEmpty(id) || StringUtils.isNullOrEmpty(unionId) || StringUtils.isNullOrEmpty(openid)
//					|| StringUtils.isNullOrEmpty(ip)) {
//				return error("参数缺失");
//			}
//
//			CourseWebCrowdfunding course = crowdfundingService.findUserCrowdfundInfoByUUID(id);
//			if (course == null) {
//				return error("信息错误");
//			}
//
//			double p = crowdfundingService.findCrowdfundingPriceCount(id);
//			double price = NumberUtils.sub(course.getPrice(), p);
//
//			// 生成订单号
//			String code = CodeUtils.getCourseOrderCode("WEB", Long.valueOf(CodeUtils.getRandomInt(50)),
//					Long.valueOf(CodeUtils.getRandomInt(100) + 1));
//			String nonceStr = CodeUtils.getUUID();// 随机字符串
//
//			SortedMap<String, String> par = new TreeMap<>();
//			par.put("appid", appid);
//			par.put("attach", "1");
//			par.put("body", "ChaZuoMBA:" + code);
//			par.put("device_info", "WEB");
//			par.put("nonce_str", nonceStr);
//			par.put("mch_id", mchId);
//
//			StringBuffer sbWX = new StringBuffer();
//			sbWX.append("<xml>").append("<appid>").append(appid).append("</appid>");
//
//			sbWX.append("<attach><![CDATA[").append("1").append("]]></attach>");
//			sbWX.append("<body><![CDATA[").append("ChaZuoMBA:" + code).append("]]></body>");
//
//			sbWX.append("<device_info>").append("WEB").append("</device_info>");
//			sbWX.append("<mch_id>").append(mchId).append("</mch_id>");
//			sbWX.append("<nonce_str>").append(nonceStr).append("</nonce_str>");
//			if (configInfo.getProjectTest()) {
//				par.put("notify_url", configInfo.getWxCrowdTestUrl());
//				sbWX.append("<notify_url>").append(configInfo.getWxCrowdTestUrl()).append("</notify_url>");
//			} else {
//				par.put("notify_url", configInfo.getWxCrowdProUrl());
//				sbWX.append("<notify_url>").append(configInfo.getWxCrowdProUrl()).append("</notify_url>");
//			}
//			sbWX.append("<openid>").append(openid).append("</openid>");
//			sbWX.append("<out_trade_no>").append(code).append("</out_trade_no>");
//			sbWX.append("<spbill_create_ip>").append(ip).append("</spbill_create_ip>");
//			int pr = (int) NumberUtils.mul(price, 100);
//			sbWX.append("<total_fee>").append(pr).append("</total_fee>");
//			sbWX.append("<trade_type>").append("JSAPI").append("</trade_type>");
//			sbWX.append("<attach>").append("1").append("</attach>");
//
//			par.put("openid", openid);
//			par.put("out_trade_no", code);
//			par.put("spbill_create_ip", ip);
//			par.put("total_fee", pr + "");
//			par.put("trade_type", "JSAPI");
//			String sign = commonService.createSign(par, apiKey);
//
//			sbWX.append("<sign><![CDATA[").append(sign).append("]]></sign>");
//			sbWX.append("</xml>");
//
//			HttpClient client = HttpClients.createDefault();
//			HttpPost post = new HttpPost("https://api.mch.weixin.qq.com/pay/unifiedorder");
//			post.setHeader("Content-type", "text/xml; charset=UTF-8");
//			StringEntity entity = new StringEntity(sbWX.toString());
//			post.setEntity(entity);
//
//			HttpResponse response = client.execute(post);
//			HttpEntity entity2 = response.getEntity();
//			if (entity2 != null) {
//				String result2 = EntityUtils.toString(entity2, "UTF-8");
//				Map<String, String> result = parseXmlToMap(result2);
//
//				CourseWebCrowdfundingOrder order = new CourseWebCrowdfundingOrder();
//				order.setCode(code);
//				order.setCrowdfundingId(course.getId());
//				order.setIp(ip);
//				order.setPrice(price);
//				crowdfundingService.saveCrowdfundingOrder(order);
//
//				WebCourseOrderInfo info = new WebCourseOrderInfo();
//				info.setAppid(appid);
//				info.setPrepayId(result.get("prepay_id"));
//				info.setNonceStr(nonceStr);
//				info.setOrderCode(code);
//				info.setSign(sign);
//				info.setApiKey(apiKey);
//
//				return ok("生成成功", info);
//			}
//
//			return error("生成失败");
//		} catch (Exception e) {
//			e.printStackTrace();
//			return error(APP_SYSTEM_ERROR);
//		}
//	}
//
//	/**
//	 * @Title: weixinWebCrowdfundingLogPayNotify
//	 * @Description: (回调)
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping("/weixinLivingWebCrowdfundingLogPayNotify")
//	public String weixinWebLivingCrowdfundingLogPayNotify(HttpServletRequest request, HttpServletResponse response) {
//		String resXml = "";
//		try {
//			InputStream inStream = request.getInputStream();
//			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
//			byte[] buffer = new byte[1024];
//			int len = 0;
//			while ((len = inStream.read(buffer)) != -1) {
//				outSteam.write(buffer, 0, len);
//			}
//			outSteam.close();
//			inStream.close();
//
//			String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息
//
//			Map<String, String> m = parseXmlToMap(result);
//
//			if ("SUCCESS".equals(m.get("result_code").toString())) {
//				String tradeNo = m.get("out_trade_no").toString();
//
//				String attach = m.get("attach").toString();
//				if (StringUtils.isNullOrEmpty(attach)) {
//					resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
//							+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
//					return resXml;
//				}
//
//				Integer type = Integer.parseInt(attach);
//				if (type == 2) {
//					CourseWebCrowdfundingLog log = crowdfundingService.findCrowdfundingLogInfo(tradeNo);
//					String uuid = crowdfundingService.findCrowdfundUUIDById(log.getCrowdfundId());
//
//					CourseWebCrowdfundingInfo info = crowdfundingService.findCrowdfundingInfo(uuid);
//					double p = crowdfundingService.findCrowdfundingPriceCount(uuid);
//					p = NumberUtils.add(p, log.getPrice());
//					double price = NumberUtils.sub(info.getPrice(), p);
//					if (price <= 0 && info.getStatus() == 1) {
//						saveUsers(info.getCourseId(), log.getCrowdfundId());
//						crowdfundingService.updateCrowfundingStatus(2, log.getCrowdfundId());
//					}
//
//					crowdfundingService.updateCrowdfundingLogStatus(tradeNo);
//				} else {
//
//					CourseWebCrowdfundingOrder order = crowdfundingService.findCrowdfundingOrderInfo(tradeNo);
//					String uuid = crowdfundingService.findCrowdfundUUIDById(order.getCrowdfundingId());
//					CourseWebCrowdfunding info = crowdfundingService.findUserCrowdfundInfoByUUID(uuid);
//
//					crowdfundingService.updateCrowfundingStatus(2, order.getCrowdfundingId());
//					crowdfundingService.updateCrowdfundOrderStatus(order.getId());
//
//					saveUsers(info.getCourseId(), order.getCrowdfundingId());
//				}
//
//				// 支付成功
//				resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
//						+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
//			} else {
//				resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
//						+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
//			}
//
//			System.out.println("微信支付回调数据结束");
//
////			BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
////			out.write(resXml.getBytes());
////			out.flush();
////			out.close();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return resXml;
//	}
//
//	private void saveUsers(Long courseId, Long crowdfundingId) {
//		List<CourseWebCrowdfundingUser> users = crowdfundingService.findCrowdfundingUsers(crowdfundingId);
//
//		OfflineMsgInfo offline = courseService.findOfflineMsgInfo(courseId);
//
//		for (CourseWebCrowdfundingUser user : users) {
//			Long memberId = user.getMemberId();
//
//			if (!configInfo.getProjectTest()) {
//				if(courseId == 144){
//					commonService.send(user.getMobile(),
//							msg144.replace("#name#", user.getName()));
//				}else{
//					if(offline.getTypeId() == 28){
//						String url = commonService.findQRCode(courseId);
//						if(offline.getBeginTime()!=null && offline.getEndTime()!= null){
//							if(DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR).equals(DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY_YEAR))){
//								String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
//								commonService.send(user.getMobile(),
//										msgQRCode.replace("#name#", user.getName())
//												.replace("#time#",
//														classDate));
//							}else{
//								String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
//								commonService.send(user.getMobile(),
//										msgQRCode.replace("#name#", user.getName())
//												.replace("#time#",
//														classDate));
//							}
//							
//						}else{
//							commonService.send(user.getMobile(),
//									msgQRCode.replace("#name#", user.getName())
//											.replace("#time#",
//													DateUtils.formatDate(offline.getBeginTime(),
//															DateUtils.MONTH_DAY_YEAR)));
//	//								.replace("#city#", offline.getCityName()).replace("#courseName#", offline.getCourseName()).replace("#qrcode#", url));
//						}
//					}else{
//						if(offline.getAddress()!="" && !"".equals(offline.getAddress())){
//							if(offline.getBeginTime()!=null && offline.getEndTime()!= null){
//								if(DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR).equals(DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY_YEAR))){
//									String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
//									commonService.send(user.getMobile(),
//											msg.replace("#name#", user.getName())
//													.replace("#time#",
//															classDate));
//								}else{
//									String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
//									commonService.send(user.getMobile(),
//											msg.replace("#name#", user.getName())
//													.replace("#time#",
//															classDate));
//								}
//							}else{
//								commonService.send(user.getMobile(),
//										msg.replace("#name#", user.getName())
//												.replace("#time#",
//														DateUtils.formatDate(offline.getBeginTime(),
//																DateUtils.MONTH_DAY_YEAR)));
//							}
////									.replace("#city#", offline.getCityName()).replace("#courseName#", offline.getCourseName()));
//						}else{
//							if(offline.getBeginTime()!=null && offline.getEndTime()!= null){
//								if(DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR).equals(DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY_YEAR))){
//									String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
//									commonService.send(user.getMobile(),
//											msgAddress.replace("#name#", user.getName())
//													.replace("#time#",
//															classDate)
//											.replace("#address#", offline.getAddress()).replace("#address#", offline.getAddress()));
//								}else{
//									String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
//									commonService.send(user.getMobile(),
//											msgAddress.replace("#name#", user.getName())
//													.replace("#time#",
//															classDate)
//											.replace("#address#", offline.getAddress()).replace("#address#", offline.getAddress()));
//								}
//							
//							}else{
//								commonService.send(user.getMobile(),
//										msgAddress.replace("#name#", user.getName())
//												.replace("#time#",
//														DateUtils.formatDate(offline.getBeginTime(),
//																DateUtils.MONTH_DAY_YEAR))
//										.replace("#address#", offline.getAddress()).replace("#address#", offline.getAddress()));
//							}
//						}
//					}
//				}
//				
//			}
//
//			if (NumberUtils.isNullOrZero(memberId)) {
//				Member member = new Member();
//				member.setMobile(user.getMobile());
//				member.setPassword(PasswdEncryption.generate("123456"));
//				member.setNickName(user.getName());
//				member.setAccessToken(CodeUtils.getUUID());
//				member.setClientVersion("1.0");
//				member.setDeviceId("null");
//				// 保存默认头像
//				member.setAvatar(commonService.getRandomAvatar());
//				memberService.register(member, null, user.getWork());
//
//				memberId = member.getId();
//
//				// 更新ID
//				orderService.updateWebCourseOrderUserId(memberId, user.getId());
//			}
//
//			// 同时保存报名信息
//			CourseOfflineJoin join = new CourseOfflineJoin();
//			join.setCourseId(courseId);
//			join.setMemberId(memberId);
//			join.setType(2);
//			join.setFrom(0);
//			join.setOrderId(Long.valueOf("0"));
//			courseService.saveCourseJoin(join);
//		}
//	}
//
//	// 百度回调
//	@RequestMapping("/baiduLivingCrowdPayNotify")
//	public String baiduLivingCrowdPayNotify(String order_no, String pay_result, String extra) {
//		try {
//			if ("1".equals(pay_result)) {
//
//				if ("1".equals(extra)) {
//					// 众筹支付
//					CourseWebCrowdfundingLog log = crowdfundingService.findCrowdfundingLogInfo(order_no);
//					String uuid = crowdfundingService.findCrowdfundUUIDById(log.getCrowdfundId());
//
//					CourseWebCrowdfundingInfo info = crowdfundingService.findCrowdfundingInfo(uuid);
//					double p = crowdfundingService.findCrowdfundingPriceCount(uuid);
//					p = NumberUtils.add(p, log.getPrice());
//					double price = NumberUtils.sub(info.getPrice(), p);
//					if (price <= 0) {
//						saveUsers(info.getCourseId(), log.getCrowdfundId());
//						crowdfundingService.updateCrowfundingStatus(2, log.getCrowdfundId());
//					}
//					crowdfundingService.updateCrowdfundingLogStatus(order_no);
//				} else {
//					CourseWebCrowdfundingOrder order = crowdfundingService.findCrowdfundingOrderInfo(order_no);
//					String uuid = crowdfundingService.findCrowdfundUUIDById(order.getCrowdfundingId());
//					CourseWebCrowdfunding info = crowdfundingService.findUserCrowdfundInfoByUUID(uuid);
//
//					crowdfundingService.updateCrowfundingStatus(2, order.getCrowdfundingId());
//					crowdfundingService.updateCrowdfundOrderStatus(order.getId());
//
//					saveUsers(info.getCourseId(), order.getCrowdfundingId());
//				}
//
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return "/paysuccess";
//	}
//
//	@RequestMapping("/baiduLivingPayNotify")
//	public String baiduLivingPayNotify(String order_no, String pay_result) {
//		try {
//			if ("1".equals(pay_result)) {
//				// 支付成功
//				WebCourseOrder order = orderService.findWebCourseOrder(order_no);
//				Long orderId = order.getId();
//
//				if (order != null && order.getStatus() == 1) {
//					return "/paysuccess";
//				}
//				List<WebCourseOrderUser> userList = orderService.findWebCourseOrderUserByCourseId(order.getId());
//				OfflineMsgInfo offline = courseService.findOfflineMsgInfo(order.getCourseId());
//
//				for (WebCourseOrderUser user : userList) {
//					Long memberId = user.getMemberId();
//
//					if (!configInfo.getProjectTest()) {
//						if(order.getCourseId() == 144){
//							commonService.send(user.getMobile(),
//									msg144.replace("#name#", user.getName()));
//						}else{
////							commonService
////							.send(user.getMobile(),
////									msg.replace("#name#", user.getName())
////											.replace("#time#",
////													DateUtils.formatDate(offline.getBeginTime(),
////															DateUtils.MONTH_DAY_YEAR))
////							.replace("#city#", offline.getCityName()).replace("#courseName#", offline.getCourseName()));
//							if(offline.getTypeId() == 28){
//								String url = commonService.findQRCode(order.getCourseId());
//								if(offline.getBeginTime()!=null && offline.getEndTime()!= null){
//									if(DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR).equals(DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY_YEAR))){
//										String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
//										commonService.send(user.getMobile(),
//												msgQRCode.replace("#name#", user.getName())
//														.replace("#time#",
//																classDate)
//												.replace("#city#", offline.getCityName()).replace("#courseName#", offline.getCourseName()).replace("#qrcode#", url));
//									}else{
//										String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
//										commonService.send(user.getMobile(),
//												msgQRCode.replace("#name#", user.getName())
//														.replace("#time#",
//																classDate)
//												.replace("#city#", offline.getCityName()).replace("#courseName#", offline.getCourseName()).replace("#qrcode#", url));
//									}
//								}else{
//									commonService.send(user.getMobile(),
//											msgQRCode.replace("#name#", user.getName())
//													.replace("#time#",
//															DateUtils.formatDate(offline.getBeginTime(),
//																	DateUtils.MONTH_DAY_YEAR))
//											.replace("#city#", offline.getCityName()).replace("#courseName#", offline.getCourseName()).replace("#qrcode#", url));
//								}
//							}else{
//								if(offline.getAddress()=="" || "".equals(offline.getAddress())){
//									if(offline.getBeginTime()!=null && offline.getEndTime()!= null){
//										if(DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR).equals(DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY_YEAR))){
//											String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
//											commonService.send(user.getMobile(),
//													msg.replace("#name#", user.getName())
//															.replace("#time#",
//																	classDate)
//													.replace("#city#", offline.getCityName()).replace("#courseName#", offline.getCourseName()));
//										}else{
//											String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
//											commonService.send(user.getMobile(),
//													msg.replace("#name#", user.getName())
//															.replace("#time#",
//																	classDate)
//													.replace("#city#", offline.getCityName()).replace("#courseName#", offline.getCourseName()));
//										}
//									}else{
//									commonService.send(user.getMobile(),
//											msg.replace("#name#", user.getName())
//													.replace("#time#",
//															DateUtils.formatDate(offline.getBeginTime(),
//																	DateUtils.MONTH_DAY_YEAR))
//											.replace("#city#", offline.getCityName()).replace("#courseName#", offline.getCourseName()));
//									}
//								}else{
//									if(offline.getBeginTime()!=null && offline.getEndTime()!= null){
//										if(DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR).equals(DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY_YEAR))){
//										String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
//										commonService.send(user.getMobile(),
//												msgAddress.replace("#name#", user.getName())
//														.replace("#time#",
//																classDate)
//												.replace("#city#", offline.getCityName()).replace("#courseName#", offline.getCourseName())
//												.replace("#address#", offline.getAddress()).replace("#address#", offline.getAddress()));
//										}else{
//											String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
//											commonService.send(user.getMobile(),
//													msgAddress.replace("#name#", user.getName())
//															.replace("#time#",
//																	classDate)
//													.replace("#city#", offline.getCityName()).replace("#courseName#", offline.getCourseName())
//													.replace("#address#", offline.getAddress()).replace("#address#", offline.getAddress()));
//										}
//									}else{
//									commonService.send(user.getMobile(),
//											msgAddress.replace("#name#", user.getName())
//													.replace("#time#",
//															DateUtils.formatDate(offline.getBeginTime(),
//																	DateUtils.MONTH_DAY_YEAR))
//											.replace("#city#", offline.getCityName()).replace("#courseName#", offline.getCourseName())
//											.replace("#address#", offline.getAddress()).replace("#address#", offline.getAddress()));
//									}
//								}
//							}
//						}
//						
//					}
//
//					if (NumberUtils.isNullOrZero(memberId)) {
//						Member member = new Member();
//						member.setMobile(user.getMobile());
//						member.setPassword(PasswdEncryption.generate("123456"));
//						member.setNickName(user.getName());
//						member.setAccessToken(CodeUtils.getUUID());
//						member.setClientVersion("1.0");
//						member.setDeviceId("null");
//						// 保存默认头像
//						member.setAvatar(commonService.getRandomAvatar());
//						memberService.register(member, null, user.getWork());
//
//						memberId = member.getId();
//
//						// 更新ID
//						orderService.updateWebCourseOrderUserId(memberId, user.getId());
//					}
//
//					// 同时保存报名信息
//					CourseOfflineJoin join = new CourseOfflineJoin();
//					join.setCourseId(order.getCourseId());
//					join.setMemberId(memberId);
//					join.setType(2); // WEB
//					join.setFrom(1);
//					join.setOrderId(orderId);;
//					courseService.saveCourseJoin(join);
//				}
//
//				if(order.getTicketStatus() == 1){
//					Params params = new Params();
//					Long id = commonService.findHaveTicket(order.getUnionid());
//					if (id != null) {
//						params.putData("id", id);
//						params.putData("unionId", order.getUnionid());
//						params.putData("status", 3);
//
//						commonService.updateTicketStatus(params);
//					}
//				}
//				// 修改订单信息
//				orderService.updateWebCourseOrderStatus(order.getId(), userList.size(), order.getCourseId());
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return "/paysuccess";
//	}

	
}
