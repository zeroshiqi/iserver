package cn.ichazuo.controller.app;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeWapPayRequest;

import cn.ichazuo.commons.base.BaseController;
import cn.ichazuo.commons.component.ConfigInfo;
import cn.ichazuo.commons.util.CodeUtils;
import cn.ichazuo.commons.util.DateUtils;
import cn.ichazuo.commons.util.NumberUtils;
import cn.ichazuo.commons.util.PasswdEncryption;
import cn.ichazuo.commons.util.StringUtils;
import cn.ichazuo.commons.util.alipay.sign.RSA;
import cn.ichazuo.commons.util.alipay.util.AlipayConfig;
import cn.ichazuo.commons.util.alipay.util.AlipayNotify;
import cn.ichazuo.commons.util.model.Params;
import cn.ichazuo.model.admin.OfflineMsgInfo;
import cn.ichazuo.model.app.BusinessAppVersionInfo;
import cn.ichazuo.model.app.CourseWebCrowdfundingInfo;
import cn.ichazuo.model.app.LivingCourseInfo;
import cn.ichazuo.model.app.UserInfo;
import cn.ichazuo.model.app.WebCourseOrderInfo;
import cn.ichazuo.model.table.AlipayOrder;
import cn.ichazuo.model.table.Business;
import cn.ichazuo.model.table.Course;
import cn.ichazuo.model.table.CourseOfflineJoin;
import cn.ichazuo.model.table.CourseOrder;
import cn.ichazuo.model.table.CourseWebCrowdfunding;
import cn.ichazuo.model.table.CourseWebCrowdfundingLog;
import cn.ichazuo.model.table.CourseWebCrowdfundingOrder;
import cn.ichazuo.model.table.CourseWebCrowdfundingUser;
import cn.ichazuo.model.table.Employee;
import cn.ichazuo.model.table.JNCourseOnlineJoin;
import cn.ichazuo.model.table.JNWebCourseOrder;
import cn.ichazuo.model.table.JNWebCourseOrderUser;
import cn.ichazuo.model.table.LiveGiftOrder;
import cn.ichazuo.model.table.Member;
import cn.ichazuo.model.table.MemberRecord;
import cn.ichazuo.model.table.OfflineCourse;
import cn.ichazuo.model.table.SelfCourseOrder;
import cn.ichazuo.model.table.Ticket;
import cn.ichazuo.model.table.WebCourseOrder;
import cn.ichazuo.model.table.WebCourseOrderUser;
import cn.ichazuo.model.table.WeixinGift;
import cn.ichazuo.service.BusinessService;
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
@Controller("app.orderController")
public class OrderController extends BaseController {

	// 短信内容
//	public static final String msg = "【插坐学院】#name#，恭喜您成功报名插坐学院#courseName#的课程！我们将于#time#准时开课。具体上课信息，辅导员会在开课前一周发短信通知呦！";
	public static final String msg = "【插坐学院】#name#，恭喜您成功报名插坐学院的线下课程！我们将于#time#准时开课。具体上课信息，辅导员会在开课前一周发短信通知呦！";

	//带有上课地址的短信内容
//	public static final String msgAddress = "【插坐学院】#name#，恭喜您成功报名插坐学院#courseName#的课程！我们将于#time#准时开课。上课地址：#address#";
	public static final String msgAddress = "【插坐学院】#name#，恭喜您成功报名插坐学院的线下课程！我们将于#time#准时开课。上课地址：#address#。不要迟到哦！";
	public static final String msg144 = "【插坐学院】#name#，恭喜您成功购买：插坐学院2016线上课程全年通票，并获得首批内测资格！全年通票有效期为：2016年2月1日至2017年2月1日。辅导员将在2月1日之前，拉您进入插坐学院专群。希望您能在新的一年中变得更好。";
	public static final String msg611 = "【插坐学院】#name#，恭喜您成功购买：插坐学院2016线上课程全年通票，并获得首批内测资格！全年通票有效期为：2016年2月1日至2017年2月1日。辅导员将在2月1日之前，拉您进入插坐学院专群。希望您能在新的一年中变得更好。";
//	public static final String msgQRCode = "【插坐学院】#name#，恭喜您成功报名插坐学院#courseName#的课程！我们将于#time#准时开课。请点击链接：#qrcode#扫描二维码进入听课群";
	public static final String msgQRCode = "【插坐学院】#name#，恭喜您成功报名插坐学院线上课程！我们将于#time#准时开课。";
	public static final String msgHaoduoke= "【插坐学院】亲爱哒#name#，您已经成功购买好多课，现发送您账号：#mobile#，密码：#mobile#。这就开启学习之旅啦！每天比别人更用心的学习，时间都会看得见！～";
	public static final String msgBeijing = "【插坐学院】";
	public static final String msgXL = "【插坐学院】#name#，恭喜您成功报名插坐学院《#courseName#》系列课！“好多课”账号密码均为报名时所填写的手机号，工作人员会在三天内联系你，如有其他问题，可以加插坐宝宝微信号： chazuomba06";
	public static final String msgXLK = "【插坐学院】#name#，恭喜您成功报名插坐学院《#courseName#》系列课！“好多课”账号密码均为报名时所填写的手机号，10月17日后即可开始学习，如有其他问题，可以加插坐宝宝微信号： chazuomba06";
	public static final String msgCD = "【插坐学院】#name#，恭喜您成功报名插坐学院《#courseName#》系列课！“好多课”账号密码均为报名时所填写的手机号，10月1日后即可开始学习，如有其他问题，可以加插坐宝宝微信号： chazuomba06";

	
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
	private BusinessService businessService;
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
	@ResponseBody
	@RequestMapping("/getCourseOrderCode")
	public JSONObject getCourseOrderCode(Long courseId, Long userId, String simpleName, Double price) {
		try {
			if (NumberUtils.isNullOrZero(courseId) || NumberUtils.isNullOrZero(userId)
					|| StringUtils.isNullOrEmpty(simpleName) || price == null) {
				return error("参数错误!");
			}
			String code = CodeUtils.getCourseOrderCode(simpleName, courseId, userId);
			if (orderService.getCountByOrderNo(code) > 0) {
				return ok("生成成功", code);
			}
			Member member = memberService.findMemberById(userId);
			if (member == null) {
				return error("参数错误");
			}
			CourseOrder order = new CourseOrder();
			order.setMemberId(userId);
			order.setCourseId(courseId);
			order.setCash(price);
			order.setOrderNo(code);
			orderService.saveCourseOrder(order);
			return ok("生成成功", code);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	@ResponseBody
	@RequestMapping("/saveAlipayOrder")
	public JSONObject saveAlipayOrder(Long bookId,String company,String name,String mobile,String province,String city,String address,Integer number,Double price,String from,String invoiceType,String invoiceAddress,String invoiceTitle,String invoiceRemarks,String content) throws UnsupportedEncodingException{
		try{
			logger.error("买书支付宝订单参数：bookId:"+bookId+"---company："+company+"---name："+name+"---mobile："+mobile+"---province："+province+"---city："+city+"---address："+address+"---number："+number+"---price："+price+"---from："+from+"---invoiceType："+invoiceType+"---invoiceAddress："+invoiceAddress+"---invoiceTitle："+invoiceTitle+"---invoiceRemarks："+invoiceRemarks+"---content："+content);
			String charset="utf-8";
			String sign_type="RSA2";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timestamp=sdf.format(new Date());
	//		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL,AlipayConfig.APPID,AlipayConfig.RSA_PRIVATE_KEY,"json",charset,AlipayConfig.ALIPAY_PUBLIC_KEY,sign_type);
	//		System.out.println(sign);
			String code = CodeUtils.getCourseOrderCode("WebBook", bookId, Long.valueOf(number));
			if (orderService.getCountByOrderNo(code) > 0) {
				return status(200, code);
			}
			if(Integer.valueOf(number)<5){
				return status(300,"购买数量不足5本！");
			}
			Course course = courseService.findCourseById(bookId);
			OfflineCourse offline = courseService.findOfflineCourseByCourseId(bookId);
			if (offline == null || course == null) {
				return error("参数错误");
			}
			//取报名课程的课程名称
			String bookName = course.getCourseName();
			int joinNumber = courseService.findCourseJoinCount(bookId);
			if (joinNumber + Integer.valueOf(number) > offline.getStudentNum()) {
				int temp = offline.getStudentNum() - joinNumber;
				return status(800, "购买数量过多", temp >= 0 ? temp : 0);
			}
			
			if(offline.getCourseId() == 129){
				return status(800, "购买数量过多", 0);
			}
			//默认报名截止时间为课程开讲前一天
			LocalDate now = LocalDate.now();
			LocalDate begin = LocalDate.parse(DateUtils.formatDate(course.getBeginTime(), DateUtils.DATE_FORMAT_NORMAL));
			if (now.isAfter(begin)){
				return status(800, "购买时间已过", 0);
			}
			// 商户订单号，商户网站订单系统中唯一订单号，必填
		    String out_trade_no = code;
			// 订单名称，必填
		    String subject = bookName;
		    // 付款金额，必填
		    String total_amount=String.valueOf(price);
		    // 商品描述，可空
		    String body = bookName;
		    // 超时时间 可空
		    String timeout_express="2m";
		    // 销售产品码 必填
		    String product_code="QUICK_WAP_PAY";
		    /**********************/
		    // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签     
		    //调用RSA签名方式
		    AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
		    AlipayTradeWapPayRequest alipay_request=new AlipayTradeWapPayRequest();
		    // 封装请求支付信息
		    AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
		    model.setOutTradeNo(out_trade_no);
		    model.setSubject(subject);
		    model.setTotalAmount(total_amount);
		    model.setBody(body);
		    model.setTimeoutExpress(timeout_express);
		    model.setProductCode(product_code);
		    alipay_request.setBizModel(model);
		    // 设置异步通知地址
		    alipay_request.setNotifyUrl(AlipayConfig.notify_url);
		    // 设置同步地址
		    alipay_request.setReturnUrl(AlipayConfig.return_url);
		    // form表单生产
		    String form = "";
//		    AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
//		    alipayRequest.setReturnUrl("http://www.chazuomba.com/files/activity/buyBook/paySuccess.html");
//		    alipayRequest.setNotifyUrl("http://www.chazuomba.com/iserver/app/aliPayNotify");//在公共参数中设置回跳和通知地址
//		    alipayRequest.setBizModel(model);
//		    alipayRequest.setBizContent("{"+
//		        "\"out_trade_no\":\""+out_trade_no+"\","+
//		        "\"total_amount\":"+price+","+
//		        "\"subject\":\""+bookName+"\","+
//		        "\"seller_id\":\"2088521076484223\","+
//		        "\"product_code\":\"QUICK_WAP_PAY\""+
//		        "}");//填充业务参数
//		    form = client.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
			try {
				// 调用SDK生成表单
				form = client.pageExecute(alipay_request).getBody(); //调用SDK生成表单
//				form = client.pageExecute(alipay_request).getBody(); //调用SDK生成表单
			} catch (AlipayApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//返回的支付订单信息
			AlipayOrder aliOrder = new AlipayOrder();
			aliOrder.setAppid(AlipayConfig.APPID);
			aliOrder.setOrderNo(code);
			aliOrder.setBody(body);
			aliOrder.setTimestamp(timestamp);
			aliOrder.setProductCode(product_code);
			aliOrder.setPrice(price);
			aliOrder.setForm(form);
			//保存预订单
			WebCourseOrder order = new WebCourseOrder();
			order.setCourseId(bookId);
			order.setIp("127.0.0.1");
			order.setNumber(number);
			order.setOpenid("支付宝购买图书");
			order.setOrderCode(code);
			order.setPrice(price);
			order.setStatus(0);
			order.setUnionid("支付宝购买图书");
			order.setType(0);
			order.setNickname("");
			order.setWeixinSex("man");
			order.setAvatar("");
			order.setFrom(StringUtils.isNullOrEmpty(from) ? "0" : from);
			order.setTicketStatus(0);
			order.setFormNickName("");
			order.setJoinReason("");
			order.setJob("");
			order.setBuyIntentions("");
			order.setEmail("");
			//发票抬头
			order.setInvoiceTitle(invoiceTitle);
			//发票类型
			order.setInvoiceType(invoiceType);
			//发票收货地址
			order.setInvoiceAddress(invoiceAddress);
			//发票收货人
			order.setInvoiceName(name);
			//发票收货人手机号
			order.setInvoiceMobile(mobile);
			//发票备注
			order.setInvoiceRemarks(invoiceRemarks);
			//是否赠送
			order.setIsGift("0");
			order.setAddress(address);
			orderService.saveWebCourseOrder(order);
			List<WebCourseOrderUser> ulist = new ArrayList<WebCourseOrderUser>();
				Long id = 0L;
				// 查询用户
				Member member = memberService.findLoginMemberByMobile(mobile);
				if (member != null) {
					id = member.getId();
				}
				WebCourseOrderUser user = new WebCourseOrderUser();
				if (StringUtils.isNullOrEmpty(content)){
					user.setContent("");
				}else{
					user.setContent(
							StringUtils.isNullOrEmpty(content) || content.equals("") ? "" : content);
				}
				user.setMemberId(member == null ? 0L : member.getId());
				user.setMobile(mobile);
				String str = null;
				user.setCity(StringUtils.isNullOrEmpty(city) ? "" : city);
				user.setProvince(StringUtils.isNullOrEmpty(province) ? "" : province);
				user.setName(name);
				user.setOrderId(order.getId());
				user.setWeixin("");
				user.setWork(company);
				user.setSex("man");
				ulist.add(user);
				orderService.saveWebCourseOrderUser(ulist);
				return ok("生成成功",aliOrder);
			}catch(Exception e){
				e.printStackTrace();
				return error("系统异常");
			}
	}
	/**
	 * @Title: aliPayNotify
	 * @Description: (支付宝支付回调)
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/aliPayNotify")
	public String aliPayNotify(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 获取支付宝POST过来反馈信息
			Map<String,String> params = new HashMap<String,String>();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
				params.put(name, valueStr);
			}

			// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			// 商户订单号
			String outTradeNo = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

			// 支付宝交易号
			String tradeNo = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

			// 交易状态
			String tradeStatus = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
			logger.error("订单号："+outTradeNo+"-------支付回调返回结果："+tradeStatus);

			// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
			if (AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, "RSA2")){// 验证成功
				//////////////////////////////////////////////////////////////////////////////////////////
				// ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
				if (tradeStatus.equals("TRADE_SUCCESS")) {
//					CourseOrder order = orderService.findCourseOrderByOrderNo(outTradeNo);
					WebCourseOrder order = orderService.findWebCourseOrder(outTradeNo);
					// 修改订单信息
					orderService.updateWebCourseOrderStatus(order.getId(), 1, order.getCourseId());
//					CourseOrder order = orderService.findCourseOrderByOrderNo(outTradeNo);
					if (order == null) {
						return "success";
					}
					orderService.updateWebCourseOrderStatus(order.getId(), 1, order.getCourseId());
//					order.setWay(1); // 1:支付宝支付
//					order.setTradeNo(tradeNo);
//					order.setNotifyTime(new Date());
//					order.setTradeStatus(tradeStatus);
//					order.setPayTime(new Date());
//					order.setStatus(1);
//					// 修改订单信息
//					orderService.updateCourseOrder(order, 1, 3);
				}

				// ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
				return "success";

				//////////////////////////////////////////////////////////////////////////////////////////
			} else {// 验证失败
				return "fail";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	/**
	 * @Title: weixinPayNotify
	 * @Description: (微信支付回调)
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/weixinPayNotify")
	public String weixinPayNotify(HttpServletRequest request, HttpServletResponse response) {
		String resXml = "";
		try {
			InputStream inStream = request.getInputStream();
			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}
			outSteam.close();
			inStream.close();

			String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息

			Map<String, String> m = parseXmlToMap(result);

			if ("SUCCESS".equals(m.get("result_code").toString())) {
				
				String tradeNo = m.get("out_trade_no").toString();
				logger.error("微信回调成功，订单号为："+tradeNo);
				// 支付成功
				CourseOrder order = orderService.findCourseOrderByOrderNo(tradeNo);
				if (order == null) {
					// 支付成功
					resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
							+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
					BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
					out.write(resXml.getBytes());
					out.flush();
					out.close();
					return null;
				} else {
					order.setWay(2); // 2:微信支付
					order.setTradeNo(tradeNo);
					order.setNotifyTime(new Date());
					order.setTradeStatus("TRADE_SUCCESS");
					order.setPayTime(new Date());
					order.setStatus(1);

					// 修改订单信息
					orderService.updateCourseOrder(order, 1, 2);
				}

				// 支付成功
				resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
						+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
			} else {
				resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
						+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
			}
			BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
			out.write(resXml.getBytes());
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
//	 * @Title: weixinWebPayNotify
	 * @Description: (Web微信支付回调)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/weixinWebPayNotify")
	public String weixinWebPayNotify(HttpServletRequest request, HttpServletResponse response) {
		String resXml = "";
		try {
			InputStream inStream = request.getInputStream();
			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
			Long ifOldEmployee = 0L;
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}
			outSteam.close();
			inStream.close();

			String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息

			Map<String, String> m = parseXmlToMap(result);

			if ("SUCCESS".equals(m.get("result_code").toString())) {
				String tradeNo = m.get("out_trade_no").toString();
				// 支付成功
				WebCourseOrder order = orderService.findWebCourseOrder(tradeNo);
				logger.error("微信支付成功，订单号："+order.getOrderCode());
				//获取订单的Id，用于存储在offline_join表中
				Long orderId = order.getId();
				logger.error("微信支付成功，订单id："+orderId);
				if (order == null || order.getStatus() == 1) {
					// 支付成功
					resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
							+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
					BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
					out.write(resXml.getBytes());
					out.flush();
					out.close();
					return null;
				} else {

					List<WebCourseOrderUser> userList = orderService.findWebCourseOrderUserByCourseId(order.getId());

					OfflineMsgInfo offline = courseService.findOfflineMsgInfo(order.getCourseId());
					for (WebCourseOrderUser user : userList) {
						Member me = memberService.findLoginMemberByMobile(user.getMobile());
						Long memberId = (me == null ? null : me.getId());
						logger.error("memberId的值为："+memberId);
						if (NumberUtils.isNullOrZero(memberId)) {
							logger.error("memberId的值判断为NullOrZero");
							Member member = new Member();
							member.setMobile(user.getMobile());
							member.setPassword(PasswdEncryption.generate("123456"));
							member.setNickName(user.getName());
							member.setAccessToken(CodeUtils.getUUID());
							member.setClientVersion("1.0");
							member.setDeviceId("null");
							// 保存默认头像
							member.setAvatar(commonService.getRandomAvatar());
							logger.error("新增用户参数：mobile："+user.getMobile()+";userName:"+user.getName());
							memberService.register(member, null, user.getWork());
							Member member1 = memberService.findLoginMemberByMobile(user.getMobile());
							
							memberId = member.getId();
							logger.error("memberId的值:"+memberId);
							Long memberId1 = member1.getId();
							logger.error("memberId1的值:"+memberId1);
							// 更新ID
							orderService.updateWebCourseOrderUserId(memberId, user.getId());
						}
						
						CourseOfflineJoin join = courseService.findCourseJoinInfo(order.getCourseId(), memberId);

						if(join == null){
							// 同时保存报名信息
							join = new CourseOfflineJoin();
							join.setCourseId(order.getCourseId());
							join.setMemberId(memberId);
							join.setOrderId(orderId);
							join.setType(2); // WEB
							join.setFrom(2);
							courseService.saveCourseJoin(join);
							//取报名课程的课程名称
							String courseName = offline.getCourseName();
							logger.error("---------------------------订单支付成功------------------------"+courseName);
//							System.out.println("---------------------------订单支付成功------------------------"+courseName);
							String sub="在线课程";
							//判断课程名称是否包含“在线课程”
							int a=courseName.indexOf(sub);
							//如果课程名称包含“在线课程”，则该课程为好多课会员购买课程
							if(a>=0){
								logger.error("---------------------------课程名称中包含“在线课程”------------------------"+courseName);
//								System.out.println("---------------------------课程名称中包含“在线课程”------------------------"+courseName);
								//判断是否存在与课程名称一致的企业信息
								List<Business> businessList = businessService.findBusinessByName(courseName);
								if(businessList.size()>0){
									Business b= businessList.get(0);
//									System.out.println("课程Id"+b.getId());
//									System.out.println("课程名称"+b.getBusinessName());
									logger.error("---------------------------与课程名称相同的企业已存在------------------------"+courseName);
//									System.out.println("---------------------------与课程名称相同的企业已存在------------------------"+courseName);
									//将支付成功的订单填写的手机号码创建为半年期限的好多课会员账号
									for (int i = 0; i < userList.size(); i++) {
										// 验证用户是否存在
										Employee employee = memberService.findBusinessLoginMemberByMobile1(userList.get(i).getMobile());
										//根据unionId查询好多课App用户信息
										Employee employeeInfo = memberService.findBusinessLoginMemberByUnionId(userList.get(i).getUnionid());
										if (employee != null){
											logger.error("--------------验证用户已经注册过会员-------------------"+userList.get(i).getMobile());
//											System.out.println("--------------验证用户已经注册过会员-------------------"+userList.get(i).getMobile());
											ifOldEmployee = 1L;
											//如果是自注册用户，把用户信息更新
//											if(employee.getIfBusiness()==1L){
												//如果unionid没有绑定过其他用户信息并且unionId为空则更新unionid信息
												if(employeeInfo==null){
													if(employee.getUnionid()==null||("null").equals(employee.getUnionid())||("").equals(employee.getUnionid())||StringUtils.isNullOrEmpty(employee.getUnionid())){
														Employee upEm = new Employee();
														upEm.setUnionid(userList.get(i).getUnionid());
														upEm.setOpenid(userList.get(i).getOpenid());
														upEm.setWxAvatar(userList.get(i).getAvatar());
														upEm.setWxName(userList.get(i).getNickname());
														upEm.setId(employee.getId());
														businessService.updateEmployee1(upEm);
													}
												}
												//写入好多课会员记录
												MemberRecord record = new MemberRecord();
												record.setEmployeeId(employee.getId().toString());
												record.setCatalogId("0");
												record.setExpiryDate(offline.getExpiryDate());
												//好多课年费会员type传0 系列课会员信息传1
												record.setType("0");
												record.setCourseId(order.getCourseId().toString());
												SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
												Calendar calendar = Calendar.getInstance();
												//取订单创建时间
										    	calendar.setTime(new Date());
										    	//订单创建时间加上会员日期为会员过期日期
										    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(offline.getExpiryDate().toString()));
										    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//										    	System.out.println(sdf.format(calendar.getTime()));
										    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									//	    	//会员过期时间
										        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
												record.setEndDate(dateTime2);
												memberService.insertMemberRecord(record);
												businessService.refreshRedis(employee.getId());
//											}
										}else{
											Employee newE = new Employee();
											//企业Id
											newE.setBusinessId(b.getId());
											//企业名称
											newE.setBusinessName(b.getBusinessName());
											//用户手机号
											newE.setMobile(userList.get(i).getMobile());
											//用户名
											newE.setName(userList.get(i).getNickname());
											//职位
											newE.setPosition(userList.get(i).getContent());
											//数据状态
											newE.setStatus(1);
											//会员日期180天
											newE.setExpiryDate(365L);
											//会员类型：企业会员
											newE.setIfBusiness(0L);
											newE.setPassword(PasswdEncryption.generate(userList.get(i).getMobile()));
											// 保存默认头像
											newE.setAvatar(userList.get(i).getAvatar());
											if(employeeInfo==null){
												//添加微信信息
												newE.setUnionid(userList.get(i).getUnionid());
												newE.setOpenid(userList.get(i).getOpenid());
												newE.setWxAvatar(userList.get(i).getAvatar());
												newE.setWxName(userList.get(i).getNickname());
											}else{
												//添加微信信息
												newE.setUnionid("");
												newE.setOpenid("");
												newE.setWxAvatar("");
												newE.setWxName("");
											}
											logger.error("--------------验证用户没有注册过会员----------------"+userList.get(i).getMobile()+"=="+businessList.get(0).getId()+"==="+businessList.get(0).getBusinessName());
//											System.out.println("--------------验证用户没有注册过会员----------------"+userList.get(i).getMobile()+"=="+businessList.get(0).getId()+"==="+businessList.get(0).getBusinessName());
											//保存数据到数据库
											memberService.registerEmployee(newE,null);
											//写入好多课会员记录
											MemberRecord record = new MemberRecord();
											record.setEmployeeId(newE.getId().toString());
											record.setCatalogId("0");
											record.setExpiryDate(offline.getExpiryDate());
											//好多课年费会员type传0 系列课会员信息传1
											record.setType("0");
											record.setCourseId(order.getCourseId().toString());
											SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
											Calendar calendar = Calendar.getInstance();
											//取订单创建时间
									    	calendar.setTime(new Date());
									    	//订单创建时间加上会员日期为会员过期日期
									    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(offline.getExpiryDate().toString()));
									    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//									    	System.out.println(sdf.format(calendar.getTime()));
									    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								//	    	//会员过期时间
									        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
											record.setEndDate(dateTime2);
											memberService.insertMemberRecord(record);
											businessService.refreshRedis(newE.getId());
										}
									}
								}else{
									logger.error("---------------------------与课程名称相同的企业不存在！！！------------------------"+courseName);
//									System.out.println("---------------------------与课程名称相同的企业不存在！！！------------------------"+courseName);
									Business business = new Business();
									business.setBusinessName(courseName);
									business.setLoginName(courseName);
									business.setPassword(PasswdEncryption.generate("123456"));
									business.setBusinessLevel("100");
									businessService.saveCompany(business);
									//将支付成功的订单填写的手机号码创建为半年期限的好多课会员账号
									for (int i = 0; i < userList.size(); i++) {
										// 验证用户是否存在
										Employee employee = memberService.findBusinessLoginMemberByMobile1(userList.get(i).getMobile());
										//根据unionId查询好多课App用户信息
										Employee employeeInfo = memberService.findBusinessLoginMemberByUnionId(userList.get(i).getUnionid());
										if (employee != null) {
											logger.error("--------------验证用户已经注册过会员-------------------"+userList.get(i).getMobile());
//											System.out.println("--------------验证用户已经注册过会员-------------------"+userList.get(i).getMobile());
//											if(employee.getIfBusiness()==1L){
//												Employee upEm = new Employee();
//												upEm.setId(employee.getId());
//												upEm.setBusinessId(business.getId());
//												upEm.setBusinessName(business.getBusinessName());
//												businessService.updateEmployee(upEm);
												//写入好多课会员记录
												if(employeeInfo==null){
													if(employee.getUnionid()==null||("null").equals(employee.getUnionid())||("").equals(employee.getUnionid())||StringUtils.isNullOrEmpty(employee.getUnionid())){
														Employee upEm = new Employee();
														upEm.setUnionid(userList.get(i).getUnionid());
														upEm.setOpenid(userList.get(i).getOpenid());
														upEm.setWxAvatar(userList.get(i).getAvatar());
														upEm.setWxName(userList.get(i).getNickname());
														upEm.setId(employee.getId());
														businessService.updateEmployee1(upEm);
													}
												}
												ifOldEmployee = 1L;
												MemberRecord record = new MemberRecord();
												record.setEmployeeId(employee.getId().toString());
												record.setCatalogId("0");
												record.setExpiryDate(offline.getExpiryDate());
												//好多课年费会员type传0 系列课会员信息传1
												record.setType("0");
												record.setCourseId(order.getCourseId().toString());
												SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
												Calendar calendar = Calendar.getInstance();
												//取订单创建时间
										    	calendar.setTime(new Date());
										    	//订单创建时间加上会员日期为会员过期日期
										    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(offline.getExpiryDate().toString()));
										    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//										    	System.out.println(sdf.format(calendar.getTime()));
										    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									//	    	//会员过期时间
										        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
												record.setEndDate(dateTime2);
												memberService.insertMemberRecord(record);
												businessService.refreshRedis(employee.getId());
//											}
										}else{
											Employee newE = new Employee();
											//企业Id
											newE.setBusinessId(business.getId());
											//企业名称
											newE.setBusinessName(business.getBusinessName());
											//用户手机号
											newE.setMobile(userList.get(i).getMobile());
											//用户名
											newE.setName(userList.get(i).getNickname());
											//职位
											newE.setPosition(userList.get(i).getContent());
											//数据状态
											newE.setStatus(1);
											//会员日期180天
											newE.setExpiryDate(365L);
											//会员类型：企业会员
											newE.setIfBusiness(0L);
											//创建时间
											newE.setCreateAt(new Date());
											//密码
											newE.setPassword(PasswdEncryption.generate(userList.get(i).getMobile()));
											// 保存默认头像
											newE.setAvatar(userList.get(i).getAvatar());
											if(employeeInfo==null){
												//添加微信信息
												newE.setUnionid(userList.get(i).getUnionid());
												newE.setOpenid(userList.get(i).getOpenid());
												newE.setWxAvatar(userList.get(i).getAvatar());
												newE.setWxName(userList.get(i).getNickname());
											}else{
												//添加微信信息
												newE.setUnionid("");
												newE.setOpenid("");
												newE.setWxAvatar("");
												newE.setWxName("");
											}
											
											//保存数据到数据库
											logger.error("--------------验证用户没有注册过会员----------------"+userList.get(i).getMobile()+"=="+business.getId()+"==="+business.getBusinessName());
//											System.out.println("--------------验证用户没有注册过会员----------------"+userList.get(i).getMobile()+"=="+business.getId()+"==="+business.getBusinessName());
											memberService.registerEmployee(newE,null);
											//写入好多课会员记录
											MemberRecord record = new MemberRecord();
											record.setEmployeeId(newE.getId().toString());
											record.setCatalogId("0");
											record.setExpiryDate(offline.getExpiryDate());
											//好多课年费会员type传0 系列课会员信息传1
											record.setType("0");
											record.setCourseId(order.getCourseId().toString());
											SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
											Calendar calendar = Calendar.getInstance();
											//取订单创建时间
									    	calendar.setTime(new Date());
									    	//订单创建时间加上会员日期为会员过期日期
									    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(offline.getExpiryDate().toString()));
									    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//									    	System.out.println(sdf.format(calendar.getTime()));
									    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								//	    	//会员过期时间
									        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
											record.setEndDate(dateTime2);
											memberService.insertMemberRecord(record);
											businessService.refreshRedis(newE.getId());
										}
									}
								}
							}
							String sub4="系列课";
							String sub5="学霸";
							String sub6="公开课";
							//判断课程名称是否包含“系列课”
							int a1=courseName.indexOf(sub4);
							int a2=courseName.indexOf(sub5);
							int a3=courseName.indexOf(sub6);
							//如果课程名称包含“系列课”，则该课程为系列课课程
							if(a1>=0 || ("2").equals(offline.getNewtype())){
								logger.error("---------------------------课程名称中包含“系列课”------------------------"+courseName);
//								System.out.println("---------------------------课程名称中包含“系列课”------------------------"+courseName);
								//判断是否存在与课程名称一致的企业信息
								List<Business> businessList = businessService.findBusinessByName(courseName);
								if(businessList.size()>0){
									Business b= businessList.get(0);
//									System.out.println("课程Id"+b.getId());
//									System.out.println("课程名称"+b.getBusinessName());
									logger.error("---------------------------与课程名称相同的企业已存在------------------------"+courseName);
//									System.out.println("---------------------------与课程名称相同的企业已存在------------------------"+courseName);
									//将支付成功的订单填写的手机号码创建为半年期限的好多课会员账号
									for (int i = 0; i < userList.size(); i++) {
										// 验证用户是否存在
										Employee employee = memberService.findBusinessLoginMemberByMobile1(userList.get(i).getMobile());
										//根据unionId查询好多课App用户信息
										Employee employeeInfo = memberService.findBusinessLoginMemberByUnionId(userList.get(i).getUnionid());
										if (employee != null){
											logger.error("--------------验证用户已经注册过会员-------------------"+userList.get(i).getMobile());
//											System.out.println("--------------验证用户已经注册过会员-------------------"+userList.get(i).getMobile());
											//如果是自注册用户，把用户信息更新
//											if(employee.getIfBusiness()==1L){
//												Employee upEm = new Employee();
//												upEm.setId(employee.getId());
//												upEm.setBusinessId(b.getId());
//												upEm.setBusinessName(b.getBusinessName());
//												//将系列课的课程包ID作为if_business的值
////												if(a2>0){
////													upEm.setIfBusiness(58L);
////												}else{
////													upEm.setIfBusiness(200L);
////												}
//												//如果用户类型不是好多课年费会员
//												if(employee.getIfBusiness()==1){
//													if(a2>0){
//														upEm.setIfBusiness(58L);
//													}else{
//														upEm.setIfBusiness(200L);
//													}
////													upEm.setIfBusiness(Long.parseLong(offline.getCatalogId()));
//												}else{
//													upEm.setIfBusiness(employee.getIfBusiness());
//												}
//												businessService.updateEmployeeXL(upEm);
												//在t_business_member_record 表中写入会员购买记录
												if(employeeInfo==null){
													if(employee.getUnionid()==null||("null").equals(employee.getUnionid())||("").equals(employee.getUnionid())||StringUtils.isNullOrEmpty(employee.getUnionid())){
														Employee upEm = new Employee();
														upEm.setUnionid(userList.get(i).getUnionid());
														upEm.setOpenid(userList.get(i).getOpenid());
														upEm.setWxAvatar(userList.get(i).getAvatar());
														upEm.setWxName(userList.get(i).getNickname());
														upEm.setId(employee.getId());
														businessService.updateEmployee1(upEm);
													}
												}
												ifOldEmployee = 1L;
												//微信公众号运营超值礼包丨系列课
												if(order.getCourseId()==2455){
													Double price = order.getPrice();
													//如果课程Id是2455 并且支付金额为199 加入2个系列课一年权限
													//①、支付一元时只添加零基础编辑课
													if(price<199.0){
														MemberRecord record = new MemberRecord();
														record.setEmployeeId(employee.getId().toString());
														record.setCatalogId("312");
														record.setExpiryDate(offline.getExpiryDate());
														//好多课年费会员type传0 系列课会员信息传1
														record.setType("1");
														record.setCourseId(order.getCourseId().toString());
														SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
														Calendar calendar = Calendar.getInstance();
														//取订单创建时间
												    	calendar.setTime(new Date());
												    	//订单创建时间加上会员日期为会员过期日期
												    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(offline.getExpiryDate().toString()));
												    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//												    	System.out.println(sdf.format(calendar.getTime()));
												    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
											//	    	//会员过期时间
												        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
														record.setEndDate(dateTime2);
														memberService.insertMemberRecord(record);
														//将数据写入选课表
														memberService.insertMemberSeries(record);
														businessService.refreshRedis(employee.getId());
													}else{
														MemberRecord record = new MemberRecord();
														record.setEmployeeId(employee.getId().toString());
														record.setCatalogId("312");
														record.setExpiryDate(offline.getExpiryDate());
														//好多课年费会员type传0 系列课会员信息传1
														record.setType("1");
														record.setCourseId(order.getCourseId().toString());
														SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
														Calendar calendar = Calendar.getInstance();
														//取订单创建时间
												    	calendar.setTime(new Date());
												    	//订单创建时间加上会员日期为会员过期日期
												    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(offline.getExpiryDate().toString()));
												    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//												    	System.out.println(sdf.format(calendar.getTime()));
												    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
											//	    	//会员过期时间
												        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
														record.setEndDate(dateTime2);
														memberService.insertMemberRecord(record);
														//将数据写入选课表
														memberService.insertMemberSeries(record);
														//②、加入《90招野路子》课程权限
														MemberRecord record2 = new MemberRecord();
														record2.setEmployeeId(employee.getId().toString());
														record2.setCatalogId("7");
														record2.setExpiryDate("365");
														//好多课年费会员type传0 系列课会员信息传1
														record2.setType("2");
														record2.setCourseId(order.getCourseId().toString());
														//取订单创建时间
														calendar.setTime(new Date());
														//订单创建时间加上会员日期为会员过期日期
														calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(offline.getExpiryDate().toString()));
														record2.setEndDate(dateTime2);
														memberService.insertMemberRecord2(record2);
														//将数据写入选课表
														memberService.insertMemberSeries1(record2);
														businessService.refreshRedis(employee.getId());
													}
												}else{
													MemberRecord record = new MemberRecord();
													record.setEmployeeId(employee.getId().toString());
													record.setCatalogId(offline.getCatalogId().toString());
													record.setExpiryDate(offline.getExpiryDate());
													//好多课年费会员type传0 系列课会员信息传1
													record.setType("1");
													record.setCourseId(order.getCourseId().toString());
													SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
													Calendar calendar = Calendar.getInstance();
													//取订单创建时间
											    	calendar.setTime(new Date());
											    	//订单创建时间加上会员日期为会员过期日期
											    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(offline.getExpiryDate().toString()));
											    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//											    	System.out.println(sdf.format(calendar.getTime()));
											    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										//	    	//会员过期时间
											        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
													record.setEndDate(dateTime2);
													memberService.insertMemberRecord(record);
													//将数据写入选课表
													memberService.insertMemberSeries(record);
													businessService.refreshRedis(employee.getId());
												}
//											}
										}else{
											Employee newE = new Employee();
											//企业Id
											newE.setBusinessId(b.getId());
											//企业名称
											newE.setBusinessName(b.getBusinessName());
											//用户手机号
											newE.setMobile(userList.get(i).getMobile());
											//用户名
											newE.setName(userList.get(i).getNickname());
											//职位
											newE.setPosition(userList.get(i).getContent());
											//数据状态
											newE.setStatus(1);
											//会员日期为后台配置的天数
											newE.setExpiryDate(Long.parseLong(offline.getExpiryDate()));
//											newE.setExpiryDate(185L);
											//会员类型：企业会员
											if(a2>=0){
												newE.setIfBusiness(58L);
											}else{
												newE.setIfBusiness(200L);
											}
											//if_business的值为后台关联课程包的ID
											newE.setIfBusiness(Long.parseLong(offline.getCatalogId()));
											newE.setPassword(PasswdEncryption.generate(userList.get(i).getMobile()));
											// 保存默认头像
											newE.setAvatar(userList.get(i).getAvatar());
											newE.setIfBusiness(58L);
											if(employeeInfo==null){
												//添加微信信息
												newE.setUnionid(userList.get(i).getUnionid());
												newE.setOpenid(userList.get(i).getOpenid());
												newE.setWxAvatar(userList.get(i).getAvatar());
												newE.setWxName(userList.get(i).getNickname());
											}else{
												//添加微信信息
												newE.setUnionid("");
												newE.setOpenid("");
												newE.setWxAvatar("");
												newE.setWxName("");
											}
											logger.error("--------------验证用户没有注册过会员----------------"+userList.get(i).getMobile()+"=="+businessList.get(0).getId()+"==="+businessList.get(0).getBusinessName());
//											System.out.println("--------------验证用户没有注册过会员----------------"+userList.get(i).getMobile()+"=="+businessList.get(0).getId()+"==="+businessList.get(0).getBusinessName());
											//保存数据到数据库
											memberService.registerEmployeeXL(newE,null);
											if(order.getCourseId()==2455){
												Double price = order.getPrice();
												//如果课程Id是2455 并且支付金额为199 加入2个系列课一年权限
												//①、支付一元时只添加零基础编辑课
												if(price<199.0){
													MemberRecord record = new MemberRecord();
													record.setEmployeeId(newE.getId().toString());
													record.setCatalogId("312");
													record.setExpiryDate(offline.getExpiryDate());
													//好多课年费会员type传0 系列课会员信息传1
													record.setType("1");
													record.setCourseId(order.getCourseId().toString());
													SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
													Calendar calendar = Calendar.getInstance();
													//取订单创建时间
											    	calendar.setTime(new Date());
											    	//订单创建时间加上会员日期为会员过期日期
											    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(offline.getExpiryDate().toString()));
											    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//											    	System.out.println(sdf.format(calendar.getTime()));
											    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										//	    	//会员过期时间
											        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
													record.setEndDate(dateTime2);
													memberService.insertMemberRecord(record);
													//将数据写入选课表
													memberService.insertMemberSeries(record);
													businessService.refreshRedis(newE.getId());
												}else{
													MemberRecord record = new MemberRecord();
													record.setEmployeeId(newE.getId().toString());
													record.setCatalogId("312");
													record.setExpiryDate(offline.getExpiryDate());
													//好多课年费会员type传0 系列课会员信息传1
													record.setType("1");
													record.setCourseId(order.getCourseId().toString());
													SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
													Calendar calendar = Calendar.getInstance();
													//取订单创建时间
											    	calendar.setTime(new Date());
											    	//订单创建时间加上会员日期为会员过期日期
											    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(offline.getExpiryDate().toString()));
											    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//											    	System.out.println(sdf.format(calendar.getTime()));
											    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										//	    	//会员过期时间
											        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
													record.setEndDate(dateTime2);
													memberService.insertMemberRecord(record);
													//将数据写入选课表
													memberService.insertMemberSeries(record);
													//②、加入《90招野路子》课程权限
													MemberRecord record2 = new MemberRecord();
													record2.setEmployeeId(newE.getId().toString());
													record2.setCatalogId("7");
													record2.setExpiryDate("365");
													//好多课年费会员type传0 系列课会员信息传1
													record2.setType("2");
													record2.setCourseId(order.getCourseId().toString());
													//取订单创建时间
													calendar.setTime(new Date());
													//订单创建时间加上会员日期为会员过期日期
													calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(offline.getExpiryDate().toString()));
													record2.setEndDate(dateTime2);
													memberService.insertMemberRecord2(record2);
													//将数据写入选课表
													memberService.insertMemberSeries1(record2);
													businessService.refreshRedis(newE.getId());
												}
											}else{
												//在t_business_member_record 表中写入会员购买记录
												MemberRecord record = new MemberRecord();
												record.setEmployeeId(newE.getId().toString());
												record.setCatalogId(offline.getCatalogId().toString());
												record.setExpiryDate(offline.getExpiryDate());
												//好多课年费会员type传0 系列课会员信息传1
												record.setType("1");
												record.setCourseId(order.getCourseId().toString());
												SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
												Calendar calendar = Calendar.getInstance();
												//取订单创建时间
										    	calendar.setTime(new Date());
										    	//订单创建时间加上会员日期为会员过期日期
										    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(offline.getExpiryDate().toString()));
										    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//										    	System.out.println(sdf.format(calendar.getTime()));
										    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									//	    	//会员过期时间
										        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
												record.setEndDate(dateTime2);
												memberService.insertMemberRecord(record);
												//将数据写入选课表
												memberService.insertMemberSeries(record);
												businessService.refreshRedis(newE.getId());
											}
											
										}
									}
								}else{
									logger.error("---------------------------与课程名称相同的企业不存在！！！------------------------"+courseName);
//									System.out.println("---------------------------与课程名称相同的企业不存在！！！------------------------"+courseName);
									Business business = new Business();
									business.setBusinessName(courseName);
									business.setLoginName(courseName);
									business.setPassword(PasswdEncryption.generate("123456"));
									business.setBusinessLevel("100");
									businessService.saveCompany(business);
									//将支付成功的订单填写的手机号码创建为半年期限的好多课会员账号
									for (int i = 0; i < userList.size(); i++) {
										// 验证用户是否存在
										Employee employee = memberService.findBusinessLoginMemberByMobile1(userList.get(i).getMobile());
										//根据unionId查询好多课App用户信息
										Employee employeeInfo = memberService.findBusinessLoginMemberByUnionId(userList.get(i).getUnionid());
										if (employee != null) {
											logger.error("--------------验证用户已经注册过会员-------------------"+userList.get(i).getMobile());
//											System.out.println("--------------验证用户已经注册过会员-------------------"+userList.get(i).getMobile());
//											if(employee.getIfBusiness()==1L){
//												Employee upEm = new Employee();
//												upEm.setId(employee.getId());
//												upEm.setBusinessId(business.getId());
//												upEm.setBusinessName(business.getBusinessName());
//												if(employee.getIfBusiness()==1){
//													if(a2>0){
//														upEm.setIfBusiness(58L);
//													}else{
//														upEm.setIfBusiness(200L);
//													}
//												}else{
//													upEm.setIfBusiness(employee.getIfBusiness());
//												}
//												businessService.updateEmployeeXL(upEm);
												if(employeeInfo==null){
													if(employee.getUnionid()==null||("null").equals(employee.getUnionid())||("").equals(employee.getUnionid())||StringUtils.isNullOrEmpty(employee.getUnionid())){
														Employee upEm = new Employee();
														upEm.setUnionid(userList.get(i).getUnionid());
														upEm.setOpenid(userList.get(i).getOpenid());
														upEm.setWxAvatar(userList.get(i).getAvatar());
														upEm.setWxName(userList.get(i).getNickname());
														upEm.setId(employee.getId());
														businessService.updateEmployee1(upEm);
													}
												}
												//在t_business_member_record 表中写入会员购买记录
												ifOldEmployee = 1L;
												if(order.getCourseId()==2455){
													Double price = order.getPrice();
													//如果课程Id是2455 并且支付金额为199 加入2个系列课一年权限
													//①、支付一元时只添加零基础编辑课
													if(price<199.0){
														MemberRecord record = new MemberRecord();
														record.setEmployeeId(employee.getId().toString());
														record.setCatalogId("312");
														record.setExpiryDate(offline.getExpiryDate());
														//好多课年费会员type传0 系列课会员信息传1
														record.setType("1");
														record.setCourseId(order.getCourseId().toString());
														SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
														Calendar calendar = Calendar.getInstance();
														//取订单创建时间
												    	calendar.setTime(new Date());
												    	//订单创建时间加上会员日期为会员过期日期
												    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(offline.getExpiryDate().toString()));
												    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//												    	System.out.println(sdf.format(calendar.getTime()));
												    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
											//	    	//会员过期时间
												        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
														record.setEndDate(dateTime2);
														memberService.insertMemberRecord(record);
														//将数据写入选课表
														memberService.insertMemberSeries(record);
														businessService.refreshRedis(employee.getId());
													}else{
														MemberRecord record = new MemberRecord();
														record.setEmployeeId(employee.getId().toString());
														record.setCatalogId("312");
														record.setExpiryDate(offline.getExpiryDate());
														//好多课年费会员type传0 系列课会员信息传1
														record.setType("1");
														record.setCourseId(order.getCourseId().toString());
														SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
														Calendar calendar = Calendar.getInstance();
														//取订单创建时间
												    	calendar.setTime(new Date());
												    	//订单创建时间加上会员日期为会员过期日期
												    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(offline.getExpiryDate().toString()));
												    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//												    	System.out.println(sdf.format(calendar.getTime()));
												    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
											//	    	//会员过期时间
												        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
														record.setEndDate(dateTime2);
														memberService.insertMemberRecord(record);
														//将数据写入选课表
														memberService.insertMemberSeries(record);
														//②、加入《90招野路子》课程权限
														MemberRecord record2 = new MemberRecord();
														record2.setEmployeeId(employee.getId().toString());
														record2.setCatalogId("7");
														record2.setExpiryDate("365");
														//好多课年费会员type传0 系列课会员信息传1
														record2.setType("2");
														record2.setCourseId(order.getCourseId().toString());
														//取订单创建时间
														calendar.setTime(new Date());
														//订单创建时间加上会员日期为会员过期日期
														calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(offline.getExpiryDate().toString()));
														record2.setEndDate(dateTime2);
														memberService.insertMemberRecord2(record2);
														//将数据写入选课表
														memberService.insertMemberSeries1(record2);
														businessService.refreshRedis(employee.getId());
													}
												}else{
													MemberRecord record = new MemberRecord();
													record.setEmployeeId(employee.getId().toString());
													record.setCatalogId(offline.getCatalogId().toString());
													record.setExpiryDate(offline.getExpiryDate());
													//好多课年费会员type传0 系列课会员信息传1
													record.setType("1");
													record.setCourseId(order.getCourseId().toString());
													SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
													Calendar calendar = Calendar.getInstance();
													//取订单创建时间
											    	calendar.setTime(new Date());
											    	//订单创建时间加上会员日期为会员过期日期
											    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(offline.getExpiryDate().toString()));
											    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//											    	System.out.println(sdf.format(calendar.getTime()));
											    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										//	    	//会员过期时间
											        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
													record.setEndDate(dateTime2);
													memberService.insertMemberRecord(record);
													//将数据写入选课表
													memberService.insertMemberSeries(record);
													businessService.refreshRedis(employee.getId());
												}
//											}
										}else{
											Employee newE = new Employee();
											//企业Id
											newE.setBusinessId(business.getId());
											//企业名称
											newE.setBusinessName(business.getBusinessName());
											//用户手机号
											newE.setMobile(userList.get(i).getMobile());
											//用户名
											newE.setName(userList.get(i).getNickname());
											//职位
											newE.setPosition(userList.get(i).getContent());
											//数据状态
											newE.setStatus(1);
											//会员日期180天
											newE.setExpiryDate(185L);
											//会员类型：企业会员
											if(a2>0){
												newE.setIfBusiness(58L);
											}else{
												newE.setIfBusiness(200L);
											}
											
											//创建时间
											newE.setCreateAt(new Date());
											//密码
											newE.setPassword(PasswdEncryption.generate(userList.get(i).getMobile()));
											// 保存默认头像
											newE.setAvatar(userList.get(i).getAvatar());
											if(employeeInfo==null){
												//添加微信信息
												newE.setUnionid(userList.get(i).getUnionid());
												newE.setOpenid(userList.get(i).getOpenid());
												newE.setWxAvatar(userList.get(i).getAvatar());
												newE.setWxName(userList.get(i).getNickname());
											}else{
												//添加微信信息
												newE.setUnionid("");
												newE.setOpenid("");
												newE.setWxAvatar("");
												newE.setWxName("");
											}
											
											//保存数据到数据库
											logger.error("--------------验证用户没有注册过会员----------------"+userList.get(i).getMobile()+"=="+business.getId()+"==="+business.getBusinessName());
//											System.out.println("--------------验证用户没有注册过会员----------------"+userList.get(i).getMobile()+"=="+business.getId()+"==="+business.getBusinessName());
											logger.error("注册App用户时用户信息如下：①、用户昵称："+userList.get(i).getNickname()+"，②、用户手机号："+userList.get(i).getMobile()+"，③、userList.get(i).getUnionid()");
											memberService.registerEmployeeXL(newE,null);
											if(order.getCourseId()==2455){
												Double price = order.getPrice();
												//如果课程Id是2455 并且支付金额为199 加入2个系列课一年权限
												//①、支付一元时只添加零基础编辑课
												if(price<199.0){
													MemberRecord record = new MemberRecord();
													record.setEmployeeId(newE.getId().toString());
													record.setCatalogId("312");
													record.setExpiryDate(offline.getExpiryDate());
													//好多课年费会员type传0 系列课会员信息传1
													record.setType("1");
													record.setCourseId(order.getCourseId().toString());
													SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
													Calendar calendar = Calendar.getInstance();
													//取订单创建时间
											    	calendar.setTime(new Date());
											    	//订单创建时间加上会员日期为会员过期日期
											    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(offline.getExpiryDate().toString()));
											    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//											    	System.out.println(sdf.format(calendar.getTime()));
											    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										//	    	//会员过期时间
											        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
													record.setEndDate(dateTime2);
													memberService.insertMemberRecord(record);
													//将数据写入选课表
													memberService.insertMemberSeries(record);
													businessService.refreshRedis(newE.getId());
												}else{
													MemberRecord record = new MemberRecord();
													record.setEmployeeId(newE.getId().toString());
													record.setCatalogId("312");
													record.setExpiryDate(offline.getExpiryDate());
													//好多课年费会员type传0 系列课会员信息传1
													record.setType("1");
													record.setCourseId(order.getCourseId().toString());
													SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
													Calendar calendar = Calendar.getInstance();
													//取订单创建时间
											    	calendar.setTime(new Date());
											    	//订单创建时间加上会员日期为会员过期日期
											    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(offline.getExpiryDate().toString()));
											    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//											    	System.out.println(sdf.format(calendar.getTime()));
											    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										//	    	//会员过期时间
											        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
													record.setEndDate(dateTime2);
													memberService.insertMemberRecord(record);
													//将数据写入选课表
													memberService.insertMemberSeries(record);
													//②、加入《90招野路子》课程权限
													MemberRecord record2 = new MemberRecord();
													record2.setEmployeeId(newE.getId().toString());
													record2.setCatalogId("7");
													record2.setExpiryDate("365");
													//好多课年费会员type传0 系列课会员信息传1
													record2.setType("2");
													record2.setCourseId(order.getCourseId().toString());
													//取订单创建时间
													calendar.setTime(new Date());
													//订单创建时间加上会员日期为会员过期日期
													calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(offline.getExpiryDate().toString()));
													record2.setEndDate(dateTime2);
													memberService.insertMemberRecord2(record2);
													//将数据写入选课表
													memberService.insertMemberSeries1(record2);
													businessService.refreshRedis(newE.getId());
												}
											}else{
												//在t_business_member_record 表中写入会员购买记录
												MemberRecord record = new MemberRecord();
												record.setEmployeeId(newE.getId().toString());
												record.setCatalogId(offline.getCatalogId().toString());
												record.setExpiryDate(offline.getExpiryDate());
												//好多课年费会员type传0 系列课会员信息传1
												record.setType("1");
												record.setCourseId(order.getCourseId().toString());
												SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
												Calendar calendar = Calendar.getInstance();
												//取订单创建时间
										    	calendar.setTime(new Date());
										    	//订单创建时间加上会员日期为会员过期日期
										    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(offline.getExpiryDate().toString()));
										    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										    	System.out.println(sdf.format(calendar.getTime()));
										    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									//	    	//会员过期时间
										        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
												record.setEndDate(dateTime2);
												memberService.insertMemberRecord(record);
												//将数据写入选课表
												memberService.insertMemberSeries(record);
												businessService.refreshRedis(newE.getId());
											}
										}
									}
								}
							}
							if(("5").equals(offline.getNewtype())){
								logger.error("---------------------------课程名称中包含“公开课”------------------------"+courseName);
//								System.out.println("---------------------------课程名称中包含“公开课”------------------------"+courseName);
								//将支付成功的订单填写的手机号码创建为半年期限的好多课会员账号
								for (int i = 0; i < userList.size(); i++) {
									// 验证用户是否存在
									Employee employee = memberService.findBusinessLoginMemberByMobile1(userList.get(i).getMobile());
									if (employee != null) {
										logger.error("--------------验证用户已经注册过会员-------------------"+userList.get(i).getMobile());
//										System.out.println("--------------验证用户已经注册过会员-------------------"+userList.get(i).getMobile());
										//在t_business_member_record 表中写入会员购买记录
										MemberRecord record = new MemberRecord();
										record.setEmployeeId(employee.getId().toString());
										record.setCatalogId(offline.getCatalogId().toString());
										record.setExpiryDate(offline.getExpiryDate());
										//好多课年费会员type传0 系列课会员信息传1
										record.setType("2");
										record.setCourseId(order.getCourseId().toString());
										SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										Calendar calendar = Calendar.getInstance();
										//取订单创建时间
										calendar.setTime(new Date());
										//订单创建时间加上会员日期为会员过期日期
										calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(offline.getExpiryDate().toString()));
										SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//										System.out.println(sdf.format(calendar.getTime()));
										DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										//会员过期时间
										Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
										record.setEndDate(dateTime2);
										ifOldEmployee=1L;
										memberService.insertMemberRecord2(record);
										//将数据写入选课表
										memberService.insertMemberSeries1(record);
										businessService.refreshRedis(employee.getId());
//												}
									}else{
										Employee newE = new Employee();
										//企业Id
										newE.setBusinessId("");
										//企业名称
										newE.setBusinessName("");
										//用户手机号
										newE.setMobile(userList.get(i).getMobile());
										//用户名
										newE.setName(userList.get(i).getName());
										//职位
										newE.setPosition(userList.get(i).getContent());
										//数据状态
										newE.setStatus(1);
										//会员日期180天
										newE.setExpiryDate(360L);
										//会员类型：企业会员
										newE.setIfBusiness(58L);
										//创建时间
										newE.setCreateAt(new Date());
										//密码
										newE.setPassword(PasswdEncryption.generate(userList.get(i).getMobile()));
										// 保存默认头像
										newE.setAvatar(commonService.getRandomAvatar());
										newE.setIfBusiness(58L);
										//保存数据到数据库
										logger.error("--------------验证用户没有注册过会员----------------"+userList.get(i).getMobile());
//										System.out.println("--------------验证用户没有注册过会员----------------"+userList.get(i).getMobile());
										memberService.registerEmployeeXL(newE,null);
										//在t_business_member_record 表中写入会员购买记录
										MemberRecord record = new MemberRecord();
										record.setEmployeeId(newE.getId().toString());
										record.setCatalogId(offline.getCatalogId().toString());
										record.setExpiryDate(offline.getExpiryDate());
										//好多课年费会员type传0 系列课会员信息传1 公开课
										record.setType("2");
										record.setCourseId(order.getCourseId().toString());
										SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										Calendar calendar = Calendar.getInstance();
										//取订单创建时间
										calendar.setTime(new Date());
										//订单创建时间加上会员日期为会员过期日期
										calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(offline.getExpiryDate().toString()));
										SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//										System.out.println(sdf.format(calendar.getTime()));
										DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									//	//会员过期时间
										Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
										record.setEndDate(dateTime2);
										memberService.insertMemberRecord2(record);
										//将数据写入选课表
										memberService.insertMemberSeries1(record);
										businessService.refreshRedis(newE.getId());
									}
								}
							}
							
							//发短信
							//测试环境和线下课程不发送短信
							if (!configInfo.getProjectTest() && !("0").equals(offline.getNewtype())) {
								//取报名课程的课程名称
								String courseName1 = offline.getCourseName();
								logger.error("---------------------------订单支付成功------------------------"+courseName);
									logger.error("发送短信参数:手机号："+user.getMobile()+"学员姓名："+user.getName()+"课程名称："+offline.getCityName()+"课程地址："+offline.getAddress());
									if(order.getCourseId() == 144){
										commonService.send(user.getMobile(),msg144.replace("#name#", user.getName()));
									}else{
										if(offline.getTypeId() == 28){
											if(!StringUtils.isNullOrEmpty(offline.getMessage()) && !"null".equals(offline.getMessage())){
												String url = commonService.findQRCode(order.getCourseId());
												String msg = "";
												//如果是新用户
												if(ifOldEmployee==0L){
													msg = offline.getMessage().replace("#name#", user.getName())
															.replace("#time#",offline.getCourseTime()).replace("#courseName#",offline.getCourseName()).replace("#mobile#",user.getMobile());
												}else{
													String msg1 = "【插坐学院】厉害了，我的#name#，《#courseName#》已被你成功抢下前排名额，现在打开“好多课”APP，直接登录即可学习。其他不懂的微信来找我，ID：chazuomba06";
													msg = msg1.replace("#name#", user.getName())
															.replace("#time#",offline.getCourseTime()).replace("#courseName#",offline.getCourseName()).replace("#mobile#",user.getMobile());
												}
												
												commonService.sendCLMsg(user.getMobile(),msg);
												logger.error("线上课程最终发送的短信内容为："+msg);
											}
											//判断课程为线上课程
											
//											if(offline.getBeginTime()!=null && offline.getEndTime()!= null){
//												if(DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR).equals(DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY_YEAR))){
////													String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
//													String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR);
//													commonService.send(user.getMobile(),
//															msgQRCode.replace("#name#", user.getName())
//																	.replace("#time#",classDate));
//												}else{
////													String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
//													String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR);
//													commonService.send(user.getMobile(),
//															msgQRCode.replace("#name#", user.getName())
//																	.replace("#time#",classDate));
//												}
//												
//											}else{
//												commonService.send(user.getMobile(),
//														msgQRCode.replace("#name#", user.getName())
//																.replace("#time#",
//																		DateUtils.formatDate(offline.getBeginTime(),
//																				DateUtils.MONTH_DAY_YEAR)));
//											}
//													.replace("#city#", offline.getCityName()).replace("#courseName#", offline.getCourseName()).replace("#qrcode#", url))
										}else{
											if(!StringUtils.isNullOrEmpty(offline.getMessage()) && !"null".equals(offline.getMessage())){
												String msg = offline.getMessage().replace("#name#", user.getName())
														.replace("#time#",offline.getCourseTime()).replace("#courseName#",offline.getCourseName()).replace("#mobile#",user.getMobile());
												commonService.sendCLMsg(user.getMobile(),msg);
												logger.error("线下课程最终发送的短信内容为："+msg);
//												System.out.println("线下课程最终发送的短信内容为："+msg);
											}
//											if(offline.getAddress()=="" || "".equals(offline.getAddress())){
//												logger.error("课程名称："+offline.getCourseName()+"的开始地址为："+offline.getAddress()+"程序判断为非空，发送msg短信内容！");
//												if(offline.getBeginTime()!=null && offline.getEndTime()!= null){
//													if(DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR).equals(DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY_YEAR))){
//														String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
//														commonService.sendCLMsg(user.getMobile(),
//																msg.replace("#name#", user.getName())
//																		.replace("#time#",classDate));
//													}else{
//														//判断两个日期的月份是否相同
//														if(DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH).equals(DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH))){
//															String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
//															commonService.sendCLMsg(user.getMobile(),
//																	msgAddress.replace("#name#", user.getName())
//																			.replace("#time#",
//																					classDate)
//																	.replace("#address#", offline.getAddress()).replace("#address#", offline.getAddress()));
//														}else{
//															String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY_)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
//															commonService.sendCLMsg(user.getMobile(),
//																	msgAddress.replace("#name#", user.getName())
//																			.replace("#time#",
//																					classDate)
//																	.replace("#address#", offline.getAddress()).replace("#address#", offline.getAddress()));
//														}
//													}
//												}else{
//													commonService.sendCLMsg(user.getMobile(),
//															msg.replace("#name#", user.getName())
//																	.replace("#time#",
//																			DateUtils.formatDate(offline.getBeginTime(),
//																					DateUtils.MONTH_DAY_YEAR)));
//												}
////														.replace("#city#", offline.getCityName()).replace("#courseName#", offline.getCourseName()));
//												logger.error("最终发送短信的内容为："+msg);
//											}else{
//												logger.error("课程名称："+offline.getCourseName()+"的开始地址为："+offline.getAddress()+"程序判断为空，发送msgAddress短信内容！");
//												if(offline.getBeginTime()!=null && offline.getEndTime()!= null){
//													if(DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR).equals(DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY_YEAR))){
//														String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
//														commonService.sendCLMsg(user.getMobile(),
//																msgAddress.replace("#name#", user.getName())
//																		.replace("#time#",
//																				classDate)
//																.replace("#address#", offline.getAddress()).replace("#address#", offline.getAddress()));
//													}else{
//														//判断两个日期的月份是否相同
//														if(DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH).equals(DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH))){
//															String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
//															commonService.sendCLMsg(user.getMobile(),
//																	msgAddress.replace("#name#", user.getName())
//																			.replace("#time#",
//																					classDate)
//																	.replace("#address#", offline.getAddress()).replace("#address#", offline.getAddress()));
//														}else{
//															String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY_)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
//															commonService.sendCLMsg(user.getMobile(),
//																	msgAddress.replace("#name#", user.getName())
//																			.replace("#time#",
//																					classDate)
//																	.replace("#address#", offline.getAddress()).replace("#address#", offline.getAddress()));
//														}
//													}
//												}else{
//													commonService.sendCLMsg(user.getMobile(),
//															msgAddress.replace("#name#", user.getName())
//																	.replace("#time#",
//																			DateUtils.formatDate(offline.getBeginTime(),
//																					DateUtils.MONTH_DAY_YEAR))
//															.replace("#address#", offline.getAddress()).replace("#address#", offline.getAddress()));
//												}
//												logger.error("最终发送短信的内容为："+msgAddress);
//											}
										}
									}
								}else{
									//如果是线下课程并且是赠送的
									if(("0").equals(offline.getNewtype()) && ("1").equals(order.getIsGift()) && !StringUtils.isNullOrEmpty(offline.getMessage2()) && !"null".equals(offline.getMessage2())){
										String msg = offline.getMessage2().replace("#name#", user.getName())
												.replace("#time#",offline.getCourseTime()).replace("#courseName#",offline.getCourseName()).replace("#mobile#",user.getMobile());
										commonService.sendCLMsg(user.getMobile(),msg);
										logger.error("线下课程赠送最终发送的短信内容为："+msg);
									}
								}
							}
						}
						
						if(order.getTicketStatus() == 1){
							Params params = new Params();
							Long id = commonService.findHaveTicket(order.getUnionid());
							if (id != null) {
								params.putData("id", id);
								params.putData("unionId", order.getUnionid());
								params.putData("status", 3);

								commonService.updateTicketStatus(params);
							}
						}
//					}

					// 修改订单信息
					orderService.updateWebCourseOrderStatus(order.getId(), userList.size(), order.getCourseId());
				}

				// 支付成功
				resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
						+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
			} else {
				resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
						+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
			}

//			System.out.println("微信支付回调数据结束");

			BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
			out.write(resXml.getBytes());
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @Title: saveWebCourseOrder
	 * @Description: (提交预订单)
	 * @param sex
	 *            性别
	 * @param content
	 *            内容
	 * @param name
	 *            姓名
	 * @param mobile
	 *            手机号
	 * @param weixin
	 *            微信号
	 * @param courseId
	 *            课程ID
	 * @param unionid
	 *            唯一ID
	 * @param openid
	 *            openId
	 * @param number
	 *            购买数量
	 * @param ip
	 *            ip地址
	 * @param ticketStatus 是否使用奖学金 1:使用 0:不使用
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveWebCourseOrder")
	public JSONObject saveWebCourseOrder(String[] sex, String[] content, String[] name, String[] mobile, String[] work,
			String[] weixin, Long courseId, String unionid, String openid, String ip, String nickname,
			String weixinSex, String avatar, String from,Integer ticketStatus,Integer weixinType,Integer qrcode,String formNickName,String joinReason,HttpServletRequest requestm,String job,String buyIntentions,String email,String invoiceType,String invoiceAddress,String invoiceTitle,String invoiceRemarks) {
		
		if(StringUtils.isNullOrEmpty(ip)){
//			ip = request.getRemoteAddr();
			ip = "47.153.191.255";
		}
		logger.error("生成订单参数长度：sex:"+sex.length+",name:"+name.length+",mobile:"+mobile.length+",work:"+work.length+",weixin:"+weixin.length);
		logger.error("生成订单参数：sex:"+sex[0]+",name:"+name[0]+",mobile:"+mobile[0]+",work:"+work[0]+",weixin:"+weixin[0]+",courseId:"+courseId+",unionId:"+unionid+",openId:"+openid);
		logger.error("生成订单参数：nickname:"+nickname+",weixinSex:"+weixinSex+",avatar:"+avatar+",from:"+from+",ticketStatus:"+ticketStatus+",weixinType:"+weixinType+",qrcode:"+qrcode+",formNickName:"+formNickName+",job:"+job);
		logger.error("生成订单参数：buyIntentions:"+buyIntentions+",email:"+email+",invoiceType:"+invoiceType+",invoiceAddress:"+invoiceAddress+",invoiceTitle:"+invoiceTitle+",invoiceRemarks:"+invoiceRemarks);
		System.out.println("生成订单参数长度：sex:"+sex.length+",content:"+content.length+",name:"+name.length+",mobile:"+mobile.length+",work:"+work.length+",weixin:"+weixin.length);
		System.out.println("生成订单参数：sex:"+sex[0]+",name:"+name[0]+",mobile:"+mobile[0]+",work:"+work[0]+",weixin:"+weixin[0]+",courseId:"+courseId+",unionId:"+unionid+",openId:"+openid);
		System.out.println("生成订单参数：nickname:"+nickname+",weixinSex:"+weixinSex+",avatar:"+avatar+",from:"+from+",ticketStatus:"+ticketStatus+",weixinType:"+weixinType+",qrcode:"+qrcode+",formNickName:"+formNickName+",job:"+job);
		System.out.println("生成订单参数：buyIntentions:"+buyIntentions+",email:"+email+",invoiceType:"+invoiceType+",invoiceAddress:"+invoiceAddress+",invoiceTitle:"+invoiceTitle+",invoiceRemarks:"+invoiceRemarks);
		//weixinType 是否为 1 判断是JSAPI 还是 NATIVE
		try {
			if(ticketStatus == null){
				ticketStatus = 0;
			}
			if (sex.length != name.length && name.length != mobile.length && mobile.length != name.length
					&& weixin.length != name.length && name.length == 0) {
				return error("参数错误!");
			}
			int number = name.length;
			if (NumberUtils.isNullOrZero(courseId) || StringUtils.isNullOrEmpty(openid)
					 || NumberUtils.isNullOrZero(number)) {
				return error("参数错误!");
			}
			Course course = courseService.findCourseById(courseId);
			OfflineCourse offline = courseService.findOfflineCourseByCourseId(courseId);
			if (offline == null || course == null) {
				return error("参数错误");
			}
			//取报名课程的课程名称
			String courseName = course.getCourseName();
			String sub="在线课程";
			String sub1="系列课";
			//判断课程名称是否包含“好多课”
			int a=courseName.indexOf(sub);
			int b=courseName.indexOf(sub1);
			//如果课程名称包含“在线课程”，则该课程为好多课会员购买课程
			if(a>=0){
				logger.error("生成订单时判断出课程名称中包含‘在线课程’");
				//判断填写的手机号码是否已经注册了好多课App账号
				for (int i = 0; i < mobile.length; i++) {
					// 验证用户是否存在
					Employee employee = memberService.findBusinessLoginMemberByMobile(mobile[i]);
					if (employee != null) {
						logger.error("验证手机号码："+mobile[i]+"已注册过企业会员");
						List<MemberRecord> recordList = businessService.findMemberRecordList(employee.getId().toString());
						if(recordList.size()>0){
							return status(700,"您已经是好多课年票会员，直接登录App学习吧~");
						}
//							if(employee.getIfBusiness()==0){
//								return status(700,"您填写的手机号已经注册了好多课会员，直接去登录吧");
//							}
					}
					logger.error("验证手机号码："+mobile[i]+"没有注册过企业会员");
				}
			}
			int joinNumber = courseService.findCourseJoinCount(courseId);
			if (joinNumber + number > offline.getStudentNum()) {
				int temp = offline.getStudentNum() - joinNumber;
				return status(800, "购买数量过多", temp >= 0 ? temp : 0);
			}
			
			if(offline.getCourseId() == 129){
				return status(800, "购买数量过多", 0);
			}
			//默认报名截止时间为课程开讲前一天
			LocalDate now = LocalDate.now();
			LocalDate begin = LocalDate.parse(DateUtils.formatDate(course.getBeginTime(), DateUtils.DATE_FORMAT_NORMAL));
			if (now.isAfter(begin)){
				return status(800, "报名时间已过", 0);
			}
			
			Double price = 0.0d;
			if(!NumberUtils.isNullOrZero(qrcode) && courseId == 129L){
				price = NumberUtils.mul(offline.getPrice(), 0.88);
			}else{
				if(offline.getDiscount() == 100){
					//不打折
					price = NumberUtils.mul(offline.getPrice(), number); // 计算价格
					//如果课程名称包含“好多课”，则该课程为好多课会员购买课程
					if(b>=0){
						
						logger.error("生成订单时判断出课程名称中包含‘系列课’");
						//判断填写的手机号码是否已经注册了好多课App账号
						for (int i = 0; i < mobile.length; i++) {
							// 验证用户是否存在
							Employee employee = memberService.findBusinessLoginMemberByMobile(mobile[i]);
							if (employee != null) {
								logger.error("验证手机号码："+mobile[i]+"已注册过企业会员");
//								if(employee.getIfBusiness()==0){
								List<MemberRecord> recordList = businessService.findMemberRecordList(employee.getId().toString());
								if(recordList.size()>0){
									String sub5="深度拆解";
									String sub6="每日问答";
									//判断课程名称是否包含“深度拆解”
									int a2=courseName.indexOf(sub5);
									int a9=courseName.indexOf(sub6);
									//包含深度拆解就打折，否则不让购买
									if(a2>=0){
										price = NumberUtils.mul(offline.getPrice(), 0.5);
									}else if(a9>=0){
										price = offline.getPrice();
									}
									else{
										return status(700,"您已经是好多课年票会员，直接登录App学习吧~");
									}
								}else{
									List<MemberRecord> recordList1 = businessService.findMemberRecordListByCatalogId(employee.getId().toString(),offline.getCatalogId());
									if(recordList1.size()>0){
										return status(700,"您填写的手机号已经开通了该系列课程会员，直接去登录吧");
									}
								}
							}
							logger.error("验证手机号码："+mobile[i]+"没有注册过企业会员");
						}
					}
				}else{
					//计算价格
					for (int i = 0; i < mobile.length; i++) {
						// 查询用户
						Member member = memberService.findLoginMemberByMobile(mobile[i]);
						if (member == null) {
							price = NumberUtils.add(price, offline.getPrice());
						}else{
							Integer count = courseService.findMyJoinCourseCount(member.getId());
							if(count > 0){
								// 计算折扣
								price = NumberUtils.mul(offline.getPrice(), NumberUtils.div(offline.getDiscount(), 100));
							}else{
								price = NumberUtils.add(price, offline.getPrice());
							}
						}
					}
				}
			}
			
			
			if(ticketStatus == 1){
				//查询奖学金ID
				Long id = commonService.findHaveTicket(unionid);
				if (id == null) {
					return status(300, "没有奖学金");
				}
				Ticket ticket = commonService.findTicket(id);
				price = NumberUtils.sub(price, ticket.getPrice());
			}
			
			// 生成订单号
			String code = CodeUtils.getCourseOrderCode("WEB", courseId, Long.valueOf(CodeUtils.getRandomInt(100) + 1));

			String nonceStr = CodeUtils.getUUID();// 随机字符串

			SortedMap<String, String> par = new TreeMap<>();
			par.put("appid", appid);
			par.put("attach", String.valueOf(number));
			par.put("body", "ChaZuoMBA:" + code);
			par.put("device_info", "WEB");
			par.put("nonce_str", nonceStr);
			par.put("mch_id", mchId);

			StringBuffer sbWX = new StringBuffer();
			sbWX.append("<xml>").append("<appid>").append(appid).append("</appid>");

			sbWX.append("<attach><![CDATA[").append(String.valueOf(number)).append("]]></attach>");
			sbWX.append("<body><![CDATA[").append("ChaZuoMBA:" + code).append("]]></body>");

			sbWX.append("<device_info>").append("WEB").append("</device_info>");
			sbWX.append("<mch_id>").append(mchId).append("</mch_id>");
			sbWX.append("<nonce_str>").append(nonceStr).append("</nonce_str>");
			if (configInfo.getProjectTest()) {
				par.put("notify_url", configInfo.getWxTestRestUrl());
				sbWX.append("<notify_url>").append(configInfo.getWxTestRestUrl()).append("</notify_url>");
			} else {
				par.put("notify_url", configInfo.getWxProRestUrl());
				sbWX.append("<notify_url>").append(configInfo.getWxProRestUrl()).append("</notify_url>");
			}
			sbWX.append("<openid>").append(openid).append("</openid>");
			sbWX.append("<out_trade_no>").append(code).append("</out_trade_no>");
			sbWX.append("<spbill_create_ip>").append(ip).append("</spbill_create_ip>");
			int pr = (int) NumberUtils.mul(price, 100);
			sbWX.append("<total_fee>").append(pr).append("</total_fee>");
			if(NumberUtils.isNullOrZero(weixinType)){
				sbWX.append("<trade_type>").append("JSAPI").append("</trade_type>");
			}else if(weixinType == 1){
				sbWX.append("<trade_type>").append("NATIVE").append("</trade_type>");
			}
			
			sbWX.append("<attach>").append(number).append("</attach>");

			par.put("openid", openid);
			par.put("out_trade_no", code);
			par.put("spbill_create_ip", ip);
			par.put("total_fee", pr + "");
			if(NumberUtils.isNullOrZero(weixinType)){
				par.put("trade_type", "JSAPI");
			}else if(weixinType == 1){
				par.put("trade_type", "NATIVE");
			}
			
			String sign = commonService.createSign(par, apiKey);

			sbWX.append("<sign><![CDATA[").append(sign).append("]]></sign>");
			sbWX.append("</xml>");

			HttpClient client = HttpClients.createDefault();
			HttpPost post = new HttpPost("https://api.mch.weixin.qq.com/pay/unifiedorder");
			post.setHeader("Content-type", "text/xml; charset=UTF-8");
			StringEntity entity = new StringEntity(sbWX.toString());
			post.setEntity(entity);
			System.out.println("保存预订单时请求微信接口开始时间："+new Date());
			HttpResponse response = client.execute(post);
			HttpEntity entity2 = response.getEntity();
			System.out.println("保存预订单时请求微信接口结束时间："+new Date());
			if (entity2 != null) {
				String result2 = EntityUtils.toString(entity2, "UTF-8");

				Map<String, String> result = parseXmlToMap(result2);

				WebCourseOrderInfo info = new WebCourseOrderInfo();
				info.setAppid(appid);
				info.setPrepayId(result.get("prepay_id"));
				info.setNonceStr(nonceStr);
				info.setOrderCode(code);
				info.setSign(sign);
				info.setMchId(mchId);
				info.setApiKey(apiKey);
				if(weixinType == 1){
					info.setCodeUrl(result.get("code_url"));
				}
				
				WebCourseOrder order = new WebCourseOrder();
				order.setCourseId(courseId);
				order.setIp(ip);
				order.setNumber(number);
				order.setOpenid(openid);
				order.setOrderCode(code);
				order.setPrice(price);
				order.setStatus(0);
				order.setUnionid(unionid);
				order.setType(0);
				order.setNickname(nickname);
				order.setWeixinSex(weixinSex);
				order.setAvatar(avatar);
				order.setFrom(StringUtils.isNullOrEmpty(from) ? "0" : from);
				order.setTicketStatus(ticketStatus);
				order.setFormNickName(formNickName);
				order.setJoinReason(joinReason);
				order.setJob(job);
				order.setBuyIntentions(buyIntentions);
				order.setEmail(email);
				//发票抬头
				order.setInvoiceTitle(invoiceTitle);
				//发票类型
				order.setInvoiceType(invoiceType);
				//发票收货地址
				order.setInvoiceAddress(invoiceAddress);
				//发票收货人
				order.setInvoiceName(name[0]);
				//发票收货人手机号
				order.setInvoiceMobile(mobile[0]);
				//发票备注
				order.setInvoiceRemarks(invoiceRemarks);
				System.out.println("保存预订单时保存预订单记录到----t_course_web_order-----表中开始时间："+new Date());
				orderService.saveWebCourseOrder(order);
				System.out.println("保存预订单时保存预订单记录到----t_course_web_order-----表中开始时间："+new Date());

				List<WebCourseOrderUser> ulist = new ArrayList<WebCourseOrderUser>();
				for (int i = 0; i < name.length; i++) {
					Long id = 0L;
					// 查询用户
					Member member = memberService.findLoginMemberByMobile(mobile[i]);
					if (member != null) {
						id = member.getId();
					}
					if(id != 0){
						if (courseService.findCourseJoinInfo(courseId, id) != null) {
							return status(600, "已报过名..", mobile[i]);
						}	
					}
					
					WebCourseOrderUser user = new WebCourseOrderUser();
					if (content.length==0){
						user.setContent("");
					}else{
						user.setContent(
								StringUtils.isNullOrEmpty(content[i]) || content[i].equals("") ? "" : content[i]);
					}
					user.setMemberId(member == null ? 0L : member.getId());
					user.setMobile(mobile[i]);
					//获取手机号码归属地
//					user.setCity(memberService.getMobileFrom(mobile[i]));
					String str = null;
					String province="";
					String city="";
					JSONArray jsonArray = null;
					//根据手机号码查询手机号码归属地
//					str = "[" +memberService.request(mobile[i]) + "]";
//					jsonArray = new JSONArray(str);
//					int errNumResult = (int) jsonArray.getJSONObject(0).get("errNum");
//					if(errNumResult == 0){
//						org.json.JSONObject jsonresult = (org.json.JSONObject) jsonArray.getJSONObject(0).get("retData");
//						province = jsonresult.getString("province");
//						city = jsonresult.getString("city");
//					}
					user.setCity(StringUtils.isNullOrEmpty(city) ? "" : city);
					user.setProvince(StringUtils.isNullOrEmpty(province) ? "" : province);
					user.setName(name[i]);
					user.setOrderId(order.getId());
					if (sex[i].equals("man")) {
						user.setSex("男");
					} else {
						user.setSex("女");
					}
					user.setWeixin(weixin[i]);
					user.setWork(work[i]);

					ulist.add(user);
				}
				System.out.println("保存预订单时保存记录到----t_course_web_order_user-----表中开始时间："+new Date());
				orderService.saveWebCourseOrderUser(ulist);
				System.out.println("保存预订单时保存记录到----t_course_web_order_user-----表中开始时间："+new Date());
				
//				WebCourseOrder orderq = orderService.findWebCourseOrder(code);
				//获取订单的Id，用于存储在offline_join表中
//				Long orderId = order.getId();
//				CourseOfflineJoin join = new CourseOfflineJoin();
//				join.setCourseId(order.getCourseId());
//				join.setMemberId(Long.valueOf("0"));
//				join.setOrderId(orderId);
//				join.setType(2); // WEB
//				join.setFrom(2);
//				System.out.println("保存预订单时保存记录到t_course_offline_join表中开始时间："+new Date());
//				courseService.saveCourseJoin(join);
//				System.out.println("保存预订单时保存记录到t_course_offline_join表中结束时间："+new Date());
				return ok("生成成功", info);
			}

			return error("生成失败");
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: updateOrderByOrderNo 
	 * @param unionId
	 * @return status 777 : 无用户信息  200:返回成功
	 */
	@ResponseBody
	@RequestMapping("/updateOrderByOrderNo")
	public JSONObject updateOrderByOrderNo(String orderNo,String invoiceType,String invoiceAddress,String invoiceTitle,String invoiceRemarks,String name, String mobile){
		try{
			if(StringUtils.isNullOrEmpty(orderNo)){
				return error("订单编号参数错误");
			}
			WebCourseOrder order = new WebCourseOrder();
			//发票抬头
			order.setInvoiceTitle(invoiceTitle);
			//发票类型
			order.setInvoiceType(invoiceType);
			//发票收货地址
			order.setInvoiceAddress(invoiceAddress);
			//发票收货人
			order.setInvoiceName(name);
			//发票收货人手机号
			order.setInvoiceMobile(mobile);
			//发票备注
			order.setInvoiceRemarks(invoiceRemarks);
			//orderNo
			order.setOrderCode(orderNo);
			orderService.updateCourseOrderByOrderNo(order);
			return ok("索要成功", order);
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: updateOrderByOrderNo 
	 * @param unionId
	 * @return status 777 : 无用户信息  200:返回成功
	 */
	@ResponseBody
	@RequestMapping("/saveGiftDetailByOrderNo")
	public JSONObject saveGiftDetailByOrderNo(String orderNo,String message,String giftDate){
		try{
			if(StringUtils.isNullOrEmpty(orderNo)){
				return error("订单编号参数错误");
			}
			WebCourseOrder order = new WebCourseOrder();
			order.setMessage(message);
			//orderNo
			order.setOrderCode(orderNo);
			order.setGiftDate(giftDate);
			orderService.updateGiftDetailByOrderNo(order);
			return ok("保存成功", order);
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}

	/**
	 * @Title: payCrowdfunding
	 * @Description: (支付众筹)
	 * @param id
	 *            众筹ID UUID
	 * @param unionId
	 *            微信唯一ID
	 * @param price
	 *            价格
	 * @param openid
	 *            openid
	 * @param ip
	 *            ip地址
	 * @param avatar
	 *            头像
	 * @param content
	 *            说的话
	 * @param name
	 *            微信名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/payCrowdfunding")
	public JSONObject payCrowdfunding(String id, String unionId, Double price, String openid, String ip, String avatar,
			String content, String name) {
		try {
			if (StringUtils.isNullOrEmpty(id) || NumberUtils.isNullOrZero(price) || StringUtils.isNullOrEmpty(unionId)
					|| StringUtils.isNullOrEmpty(openid) || StringUtils.isNullOrEmpty(ip)
					|| StringUtils.isNullOrEmpty(avatar)) {
				return error("参数缺失");
			}

			CourseWebCrowdfunding course = crowdfundingService.findUserCrowdfundInfoByUUID(id);
			if (course == null) {
				return error("信息错误");
			}

			double p = crowdfundingService.findCrowdfundingPriceCount(id);
			double subPrice = NumberUtils.sub(course.getPrice(), p);

			if (subPrice < price) {
				return status(666, "超出金额", subPrice);
			}

			// 生成订单号
			String code = CodeUtils.getCourseOrderCode("WEB", Long.valueOf(CodeUtils.getRandomInt(50)),
					Long.valueOf(CodeUtils.getRandomInt(100) + 1));
			String nonceStr = CodeUtils.getUUID();// 随机字符串

			SortedMap<String, String> par = new TreeMap<>();
			par.put("appid", appid);
			par.put("attach", "2");
			par.put("body", "ChaZuoMBA:" + code);
			par.put("device_info", "WEB");
			par.put("nonce_str", nonceStr);
			par.put("mch_id", mchId);

			StringBuffer sbWX = new StringBuffer();
			sbWX.append("<xml>").append("<appid>").append(appid).append("</appid>");

			sbWX.append("<attach><![CDATA[").append("2").append("]]></attach>");
			sbWX.append("<body><![CDATA[").append("ChaZuoMBA:" + code).append("]]></body>");

			sbWX.append("<device_info>").append("WEB").append("</device_info>");
			sbWX.append("<mch_id>").append(mchId).append("</mch_id>");
			sbWX.append("<nonce_str>").append(nonceStr).append("</nonce_str>");
			if (configInfo.getProjectTest()) {
				par.put("notify_url", configInfo.getWxCrowdTestUrl());
				sbWX.append("<notify_url>").append(configInfo.getWxCrowdTestUrl()).append("</notify_url>");
			} else {
				par.put("notify_url", configInfo.getWxCrowdProUrl());
				sbWX.append("<notify_url>").append(configInfo.getWxCrowdProUrl()).append("</notify_url>");
			}
			sbWX.append("<openid>").append(openid).append("</openid>");
			sbWX.append("<out_trade_no>").append(code).append("</out_trade_no>");
			sbWX.append("<spbill_create_ip>").append(ip).append("</spbill_create_ip>");
			int pr = (int) NumberUtils.mul(price, 100);
			sbWX.append("<total_fee>").append(pr).append("</total_fee>");
			sbWX.append("<trade_type>").append("JSAPI").append("</trade_type>");
			sbWX.append("<attach>").append("2").append("</attach>");

			par.put("openid", openid);
			par.put("out_trade_no", code);
			par.put("spbill_create_ip", ip);
			par.put("total_fee", pr + "");
			par.put("trade_type", "JSAPI");
			String sign = commonService.createSign(par, apiKey);

			sbWX.append("<sign><![CDATA[").append(sign).append("]]></sign>");
			sbWX.append("</xml>");

			HttpClient client = HttpClients.createDefault();
			HttpPost post = new HttpPost("https://api.mch.weixin.qq.com/pay/unifiedorder");
			post.setHeader("Content-type", "text/xml; charset=UTF-8");
			StringEntity entity = new StringEntity(sbWX.toString());
			post.setEntity(entity);

			HttpResponse response = client.execute(post);
			HttpEntity entity2 = response.getEntity();
			if (entity2 != null) {
				String result2 = EntityUtils.toString(entity2, "UTF-8");
				Map<String, String> result = parseXmlToMap(result2);

				CourseWebCrowdfundingLog log = new CourseWebCrowdfundingLog();
				log.setAvatar(avatar);
				log.setCode(code);
				log.setContent(content);
				log.setCrowdfundId(course.getId());
				log.setName(name);
				log.setPrice(price);
				log.setUnionId(unionId);
				log.setStatus(0);
				log.setType(0);

				crowdfundingService.saveCrowdfundingLog(log);

				WebCourseOrderInfo info = new WebCourseOrderInfo();
				info.setAppid(appid);
				info.setPrepayId(result.get("prepay_id"));
				info.setNonceStr(nonceStr);
				info.setOrderCode(code);
				info.setSign(sign);
				info.setApiKey(apiKey);

				return ok("生成成功", info);
			}

			return error("生成失败");
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}

	/**
	 * @Title: saveCrowdfundingOrder
	 * @Description: (支付剩余)
	 * @param id
	 *            众筹ID uuid
	 * @param unionId
	 *            微信唯一ID
	 * @param openid
	 *            openid
	 * @param ip
	 *            ip地址
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveCrowdfundingOrder")
	public JSONObject saveCrowdfundingOrder(String id, String unionId, String openid, String ip) {
		try {
			if (StringUtils.isNullOrEmpty(id) || StringUtils.isNullOrEmpty(unionId) || StringUtils.isNullOrEmpty(openid)
					|| StringUtils.isNullOrEmpty(ip)) {
				return error("参数缺失");
			}

			CourseWebCrowdfunding course = crowdfundingService.findUserCrowdfundInfoByUUID(id);
			if (course == null) {
				return error("信息错误");
			}

			double p = crowdfundingService.findCrowdfundingPriceCount(id);
			double price = NumberUtils.sub(course.getPrice(), p);

			// 生成订单号
			String code = CodeUtils.getCourseOrderCode("WEB", Long.valueOf(CodeUtils.getRandomInt(50)),
					Long.valueOf(CodeUtils.getRandomInt(100) + 1));
			String nonceStr = CodeUtils.getUUID();// 随机字符串

			SortedMap<String, String> par = new TreeMap<>();
			par.put("appid", appid);
			par.put("attach", "1");
			par.put("body", "ChaZuoMBA:" + code);
			par.put("device_info", "WEB");
			par.put("nonce_str", nonceStr);
			par.put("mch_id", mchId);

			StringBuffer sbWX = new StringBuffer();
			sbWX.append("<xml>").append("<appid>").append(appid).append("</appid>");

			sbWX.append("<attach><![CDATA[").append("1").append("]]></attach>");
			sbWX.append("<body><![CDATA[").append("ChaZuoMBA:" + code).append("]]></body>");

			sbWX.append("<device_info>").append("WEB").append("</device_info>");
			sbWX.append("<mch_id>").append(mchId).append("</mch_id>");
			sbWX.append("<nonce_str>").append(nonceStr).append("</nonce_str>");
			if (configInfo.getProjectTest()) {
				par.put("notify_url", configInfo.getWxCrowdTestUrl());
				sbWX.append("<notify_url>").append(configInfo.getWxCrowdTestUrl()).append("</notify_url>");
			} else {
				par.put("notify_url", configInfo.getWxCrowdProUrl());
				sbWX.append("<notify_url>").append(configInfo.getWxCrowdProUrl()).append("</notify_url>");
			}
			sbWX.append("<openid>").append(openid).append("</openid>");
			sbWX.append("<out_trade_no>").append(code).append("</out_trade_no>");
			sbWX.append("<spbill_create_ip>").append(ip).append("</spbill_create_ip>");
			int pr = (int) NumberUtils.mul(price, 100);
			sbWX.append("<total_fee>").append(pr).append("</total_fee>");
			sbWX.append("<trade_type>").append("JSAPI").append("</trade_type>");
			sbWX.append("<attach>").append("1").append("</attach>");

			par.put("openid", openid);
			par.put("out_trade_no", code);
			par.put("spbill_create_ip", ip);
			par.put("total_fee", pr + "");
			par.put("trade_type", "JSAPI");
			String sign = commonService.createSign(par, apiKey);

			sbWX.append("<sign><![CDATA[").append(sign).append("]]></sign>");
			sbWX.append("</xml>");

			HttpClient client = HttpClients.createDefault();
			HttpPost post = new HttpPost("https://api.mch.weixin.qq.com/pay/unifiedorder");
			post.setHeader("Content-type", "text/xml; charset=UTF-8");
			StringEntity entity = new StringEntity(sbWX.toString());
			post.setEntity(entity);

			HttpResponse response = client.execute(post);
			HttpEntity entity2 = response.getEntity();
			if (entity2 != null) {
				String result2 = EntityUtils.toString(entity2, "UTF-8");
				Map<String, String> result = parseXmlToMap(result2);

				CourseWebCrowdfundingOrder order = new CourseWebCrowdfundingOrder();
				order.setCode(code);
				order.setCrowdfundingId(course.getId());
				order.setIp(ip);
				order.setPrice(price);
				crowdfundingService.saveCrowdfundingOrder(order);

				WebCourseOrderInfo info = new WebCourseOrderInfo();
				info.setAppid(appid);
				info.setPrepayId(result.get("prepay_id"));
				info.setNonceStr(nonceStr);
				info.setOrderCode(code);
				info.setSign(sign);
				info.setApiKey(apiKey);

				return ok("生成成功", info);
			}

			return error("生成失败");
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}

	/**
	 * @Title: weixinWebCrowdfundingLogPayNotify
	 * @Description: (回调)
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/weixinWebCrowdfundingLogPayNotify")
	public String weixinWebCrowdfundingLogPayNotify(HttpServletRequest request, HttpServletResponse response) {
		String resXml = "";
		try {
			InputStream inStream = request.getInputStream();
			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}
			outSteam.close();
			inStream.close();

			String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息

			Map<String, String> m = parseXmlToMap(result);

			if ("SUCCESS".equals(m.get("result_code").toString())) {
				String tradeNo = m.get("out_trade_no").toString();

				String attach = m.get("attach").toString();
				if (StringUtils.isNullOrEmpty(attach)) {
					resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
							+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
					return resXml;
				}

				Integer type = Integer.parseInt(attach);
				if (type == 2) {
					CourseWebCrowdfundingLog log = crowdfundingService.findCrowdfundingLogInfo(tradeNo);
					String uuid = crowdfundingService.findCrowdfundUUIDById(log.getCrowdfundId());

					CourseWebCrowdfundingInfo info = crowdfundingService.findCrowdfundingInfo(uuid);
					double p = crowdfundingService.findCrowdfundingPriceCount(uuid);
					p = NumberUtils.add(p, log.getPrice());
					double price = NumberUtils.sub(info.getPrice(), p);
					if (price <= 0 && info.getStatus() == 1) {
						saveUsers(info.getCourseId(), log.getCrowdfundId());
						crowdfundingService.updateCrowfundingStatus(2, log.getCrowdfundId());
					}

					crowdfundingService.updateCrowdfundingLogStatus(tradeNo);
				} else {

					CourseWebCrowdfundingOrder order = crowdfundingService.findCrowdfundingOrderInfo(tradeNo);
					String uuid = crowdfundingService.findCrowdfundUUIDById(order.getCrowdfundingId());
					CourseWebCrowdfunding info = crowdfundingService.findUserCrowdfundInfoByUUID(uuid);

					crowdfundingService.updateCrowfundingStatus(2, order.getCrowdfundingId());
					crowdfundingService.updateCrowdfundOrderStatus(order.getId());

					saveUsers(info.getCourseId(), order.getCrowdfundingId());
				}

				// 支付成功
				resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
						+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
			} else {
				resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
						+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
			}

//			System.out.println("微信支付回调数据结束");

//			BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
//			out.write(resXml.getBytes());
//			out.flush();
//			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resXml;
	}

	private void saveUsers(Long courseId, Long crowdfundingId) {
		List<CourseWebCrowdfundingUser> users = crowdfundingService.findCrowdfundingUsers(crowdfundingId);

		OfflineMsgInfo offline = courseService.findOfflineMsgInfo(courseId);

		for (CourseWebCrowdfundingUser user : users) {
			Long memberId = user.getMemberId();

			if (!configInfo.getProjectTest()) {
				if(courseId == 144){
					commonService.send(user.getMobile(),
							msg144.replace("#name#", user.getName()));
				}else{
					if(offline.getTypeId() == 28){
						String url = commonService.findQRCode(courseId);
						if(offline.getBeginTime()!=null && offline.getEndTime()!= null){
							if(DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR).equals(DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY_YEAR))){
//								String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
								String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR);
								commonService.send(user.getMobile(),
										msgQRCode.replace("#name#", user.getName())
												.replace("#time#",
														classDate));
							}else{
//								String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
								String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR);
								commonService.send(user.getMobile(),
										msgQRCode.replace("#name#", user.getName())
												.replace("#time#",
														classDate));
							}
							
						}else{
							commonService.send(user.getMobile(),
									msgQRCode.replace("#name#", user.getName())
											.replace("#time#",
													DateUtils.formatDate(offline.getBeginTime(),
															DateUtils.MONTH_DAY_YEAR)));
	//								.replace("#city#", offline.getCityName()).replace("#courseName#", offline.getCourseName()).replace("#qrcode#", url));
						}
					}else{
						if(offline.getAddress()!="" && !"".equals(offline.getAddress())){
							if(offline.getBeginTime()!=null && offline.getEndTime()!= null){
								if(DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR).equals(DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY_YEAR))){
									String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
									commonService.send(user.getMobile(),
											msg.replace("#name#", user.getName())
													.replace("#time#",
															classDate));
								}else{
									String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
									commonService.send(user.getMobile(),
											msg.replace("#name#", user.getName())
													.replace("#time#",
															classDate));
								}
							}else{
								commonService.send(user.getMobile(),
										msg.replace("#name#", user.getName())
												.replace("#time#",
														DateUtils.formatDate(offline.getBeginTime(),
																DateUtils.MONTH_DAY_YEAR)));
							}
//									.replace("#city#", offline.getCityName()).replace("#courseName#", offline.getCourseName()));
						}else{
							if(offline.getBeginTime()!=null && offline.getEndTime()!= null){
								if(DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR).equals(DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY_YEAR))){
									String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
									commonService.send(user.getMobile(),
											msgAddress.replace("#name#", user.getName())
													.replace("#time#",
															classDate)
											.replace("#address#", offline.getAddress()).replace("#address#", offline.getAddress()));
								}else{
									String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
									commonService.send(user.getMobile(),
											msgAddress.replace("#name#", user.getName())
													.replace("#time#",
															classDate)
											.replace("#address#", offline.getAddress()).replace("#address#", offline.getAddress()));
								}
							
							}else{
								commonService.send(user.getMobile(),
										msgAddress.replace("#name#", user.getName())
												.replace("#time#",
														DateUtils.formatDate(offline.getBeginTime(),
																DateUtils.MONTH_DAY_YEAR))
										.replace("#address#", offline.getAddress()).replace("#address#", offline.getAddress()));
							}
						}
					}
				}
				
			}

			if (NumberUtils.isNullOrZero(memberId)) {
				Member member = new Member();
				member.setMobile(user.getMobile());
				member.setPassword(PasswdEncryption.generate("123456"));
				member.setNickName(user.getName());
				member.setAccessToken(CodeUtils.getUUID());
				member.setClientVersion("1.0");
				member.setDeviceId("null");
				// 保存默认头像
				member.setAvatar(commonService.getRandomAvatar());
				memberService.register(member, null, user.getWork());

				memberId = member.getId();

				// 更新ID
				orderService.updateWebCourseOrderUserId(memberId, user.getId());
			}

			// 同时保存报名信息
			CourseOfflineJoin join = new CourseOfflineJoin();
			join.setCourseId(courseId);
			join.setMemberId(memberId);
			join.setType(2);
			join.setFrom(0);
			join.setOrderId(Long.valueOf("0"));
			courseService.saveCourseJoin(join);
		}
	}

	// 百度回调
	@RequestMapping("/baiduCrowdPayNotify")
	public String baiduCrowdPayNotify(String order_no, String pay_result, String extra) {
		try {
			if ("1".equals(pay_result)) {

				if ("1".equals(extra)) {
					// 众筹支付
					CourseWebCrowdfundingLog log = crowdfundingService.findCrowdfundingLogInfo(order_no);
					String uuid = crowdfundingService.findCrowdfundUUIDById(log.getCrowdfundId());

					CourseWebCrowdfundingInfo info = crowdfundingService.findCrowdfundingInfo(uuid);
					double p = crowdfundingService.findCrowdfundingPriceCount(uuid);
					p = NumberUtils.add(p, log.getPrice());
					double price = NumberUtils.sub(info.getPrice(), p);
					if (price <= 0) {
						saveUsers(info.getCourseId(), log.getCrowdfundId());
						crowdfundingService.updateCrowfundingStatus(2, log.getCrowdfundId());
					}
					crowdfundingService.updateCrowdfundingLogStatus(order_no);
				} else {
					CourseWebCrowdfundingOrder order = crowdfundingService.findCrowdfundingOrderInfo(order_no);
					String uuid = crowdfundingService.findCrowdfundUUIDById(order.getCrowdfundingId());
					CourseWebCrowdfunding info = crowdfundingService.findUserCrowdfundInfoByUUID(uuid);

					crowdfundingService.updateCrowfundingStatus(2, order.getCrowdfundingId());
					crowdfundingService.updateCrowdfundOrderStatus(order.getId());

					saveUsers(info.getCourseId(), order.getCrowdfundingId());
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/paysuccess";
	}

	@RequestMapping("/baiduPayNotify")
	public String baiduPayNotify(String order_no, String pay_result) {
		try {
			if ("1".equals(pay_result)) {
				// 支付成功
				WebCourseOrder order = orderService.findWebCourseOrder(order_no);
				Long orderId = order.getId();

				if (order != null && order.getStatus() == 1) {
					return "/paysuccess";
				}
				List<WebCourseOrderUser> userList = orderService.findWebCourseOrderUserByCourseId(order.getId());
				OfflineMsgInfo offline = courseService.findOfflineMsgInfo(order.getCourseId());
				//取报名课程的课程名称
				String courseName = offline.getCourseName();
				logger.error("---------------------------订单支付成功------------------------"+courseName);
				System.out.println("---------------------------订单支付成功------------------------"+courseName);
				String sub="在线课程";
				//判断课程名称是否包含“在线课程”
				int a=courseName.indexOf(sub);
				//如果课程名称包含“在线课程”，则该课程为好多课会员购买课程
				if(a>=0){
					logger.error("---------------------------课程名称中包含“在线课程”------------------------"+courseName);
					System.out.println("---------------------------课程名称中包含“在线课程”------------------------"+courseName);
					//判断是否存在与课程名称一致的企业信息
					List<Business> businessList = businessService.findBusinessByName(courseName);
					if(businessList.size()>0){
						Business b= businessList.get(0);
						System.out.println("课程Id"+b.getId());
						System.out.println("课程名称"+b.getBusinessName());
						logger.error("---------------------------与课程名称相同的企业已存在------------------------"+courseName);
						System.out.println("---------------------------与课程名称相同的企业已存在------------------------"+courseName);
						//将支付成功的订单填写的手机号码创建为半年期限的好多课会员账号
						for (int i = 0; i < userList.size(); i++) {
							// 验证用户是否存在
							Employee employee = memberService.findBusinessLoginMemberByMobile1(userList.get(i).getMobile());
							if (employee != null){
								logger.error("--------------验证用户已经注册过会员-------------------"+userList.get(i).getMobile());
								System.out.println("--------------验证用户已经注册过会员-------------------"+userList.get(i).getMobile());
								//如果是自注册用户，把用户信息更新
//								if(employee.getIfBusiness()==1L){
//									Employee upEm = new Employee();
//									upEm.setId(employee.getId());
//									upEm.setBusinessId(b.getId());
//									upEm.setBusinessName(b.getBusinessName());
//									businessService.updateEmployee(upEm);
									//在t_business_member_record 表中写入会员购买记录
									MemberRecord record = new MemberRecord();
									record.setEmployeeId(employee.getId().toString());
									record.setCatalogId("0");
									record.setExpiryDate(offline.getExpiryDate());
									//好多课年费会员type传0 系列课会员信息传1
									record.setType("0");
									record.setCourseId(order.getCourseId().toString());
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									Calendar calendar = Calendar.getInstance();
									//取订单创建时间
							    	calendar.setTime(new Date());
							    	//订单创建时间加上会员日期为会员过期日期
							    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(offline.getExpiryDate().toString()));
							    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							    	System.out.println(sdf.format(calendar.getTime()));
							    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						//	    	//会员过期时间
							        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
									record.setEndDate(dateTime2);
									memberService.insertMemberRecord(record);
//								}
							}else{
								Employee newE = new Employee();
								//企业Id
								newE.setBusinessId(b.getId());
								//企业名称
								newE.setBusinessName(b.getBusinessName());
								//用户手机号
								newE.setMobile(userList.get(i).getMobile());
								//用户名
								newE.setName(userList.get(i).getName());
								//职位
								newE.setPosition(userList.get(i).getContent());
								//数据状态
								newE.setStatus(1);
								//会员日期180天
								newE.setExpiryDate(185L);
								//会员类型：企业会员
								newE.setIfBusiness(0L);
								newE.setPassword(PasswdEncryption.generate(userList.get(i).getMobile()));
								// 保存默认头像
								newE.setAvatar(commonService.getRandomAvatar());
								logger.error("--------------验证用户没有注册过会员----------------"+userList.get(i).getMobile()+"=="+businessList.get(0).getId()+"==="+businessList.get(0).getBusinessName());
								System.out.println("--------------验证用户没有注册过会员----------------"+userList.get(i).getMobile()+"=="+businessList.get(0).getId()+"==="+businessList.get(0).getBusinessName());
								//保存数据到数据库
								memberService.registerEmployee(newE,null);
								//在t_business_member_record 表中写入会员购买记录
								MemberRecord record = new MemberRecord();
								record.setEmployeeId(newE.getId().toString());
								record.setCatalogId("0");
								record.setExpiryDate(offline.getExpiryDate());
								//好多课年费会员type传0 系列课会员信息传1
								record.setType("0");
								record.setCourseId(order.getCourseId().toString());
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								Calendar calendar = Calendar.getInstance();
								//取订单创建时间
						    	calendar.setTime(new Date());
						    	//订单创建时间加上会员日期为会员过期日期
						    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(offline.getExpiryDate().toString()));
						    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						    	System.out.println(sdf.format(calendar.getTime()));
						    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					//	    	//会员过期时间
						        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
								record.setEndDate(dateTime2);
								memberService.insertMemberRecord(record);
							}
						}
					}else{
						logger.error("---------------------------与课程名称相同的企业不存在！！！------------------------"+courseName);
						System.out.println("---------------------------与课程名称相同的企业不存在！！！------------------------"+courseName);
						Business business = new Business();
						business.setBusinessName(courseName);
						business.setLoginName(courseName);
						business.setPassword(PasswdEncryption.generate("123456"));
						business.setBusinessLevel("100");
						businessService.saveCompany(business);
						//将支付成功的订单填写的手机号码创建为半年期限的好多课会员账号
						for (int i = 0; i < userList.size(); i++) {
							// 验证用户是否存在
							Employee employee = memberService.findBusinessLoginMemberByMobile1(userList.get(i).getMobile());
							if (employee != null) {
								logger.error("--------------验证用户已经注册过会员-------------------"+userList.get(i).getMobile());
								System.out.println("--------------验证用户已经注册过会员-------------------"+userList.get(i).getMobile());
//								if(employee.getIfBusiness()==1L){
//									Employee upEm = new Employee();
//									upEm.setId(employee.getId());
//									upEm.setBusinessId(business.getId());
//									upEm.setBusinessName(business.getBusinessName());
//									businessService.updateEmployee(upEm);
									//在t_business_member_record 表中写入会员购买记录
									MemberRecord record = new MemberRecord();
									record.setEmployeeId(employee.getId().toString());
									record.setCatalogId("0");
									record.setExpiryDate(offline.getExpiryDate());
									//好多课年费会员type传0 系列课会员信息传1
									record.setType("0");
									record.setCourseId(order.getCourseId().toString());
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									Calendar calendar = Calendar.getInstance();
									//取订单创建时间
							    	calendar.setTime(new Date());
							    	//订单创建时间加上会员日期为会员过期日期
							    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(offline.getExpiryDate().toString()));
							    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							    	System.out.println(sdf.format(calendar.getTime()));
							    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						//	    	//会员过期时间
							        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
									record.setEndDate(dateTime2);
									memberService.insertMemberRecord(record);
//								}
							}else{
								Employee newE = new Employee();
								//企业Id
								newE.setBusinessId(business.getId());
								//企业名称
								newE.setBusinessName(business.getBusinessName());
								//用户手机号
								newE.setMobile(userList.get(i).getMobile());
								//用户名
								newE.setName(userList.get(i).getName());
								//职位
								newE.setPosition(userList.get(i).getContent());
								//数据状态
								newE.setStatus(1);
								//会员日期180天
								newE.setExpiryDate(185L);
								//会员类型：企业会员
								newE.setIfBusiness(0L);
								newE.setPassword(PasswdEncryption.generate(userList.get(i).getMobile()));
								// 保存默认头像
								newE.setAvatar(commonService.getRandomAvatar());
								//保存数据到数据库
								logger.error("--------------验证用户没有注册过会员----------------"+userList.get(i).getMobile()+"=="+business.getId()+"==="+business.getBusinessName());
								System.out.println("--------------验证用户没有注册过会员----------------"+userList.get(i).getMobile()+"=="+business.getId()+"==="+business.getBusinessName());
								memberService.registerEmployee(newE,null);
								//在t_business_member_record 表中写入会员购买记录
								MemberRecord record = new MemberRecord();
								record.setEmployeeId(newE.getId().toString());
								record.setCatalogId("0");
								record.setExpiryDate(offline.getExpiryDate());
								//好多课年费会员type传0 系列课会员信息传1
								record.setType("0");
								record.setCourseId(order.getCourseId().toString());
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								Calendar calendar = Calendar.getInstance();
								//取订单创建时间
						    	calendar.setTime(new Date());
						    	//订单创建时间加上会员日期为会员过期日期
						    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(offline.getExpiryDate().toString()));
						    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						    	System.out.println(sdf.format(calendar.getTime()));
						    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					//	    	//会员过期时间
						        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
								record.setEndDate(dateTime2);
								memberService.insertMemberRecord(record);
							}
						}
					}
				}
				String sub4="系列课";
				String sub5="深度拆解";
				//判断课程名称是否包含“系列课”
				int a1=courseName.indexOf(sub4);
				int a2=courseName.indexOf(sub5);
				//如果课程名称包含“系列课”，则该课程为系列课课程
				if(a1>=0){
					logger.error("---------------------------课程名称中包含“系列课”------------------------"+courseName);
					System.out.println("---------------------------课程名称中包含“系列课”------------------------"+courseName);
					//判断是否存在与课程名称一致的企业信息
					List<Business> businessList = businessService.findBusinessByName(courseName);
					if(businessList.size()>0){
						Business b= businessList.get(0);
						System.out.println("课程Id"+b.getId());
						System.out.println("课程名称"+b.getBusinessName());
						logger.error("---------------------------与课程名称相同的企业已存在------------------------"+courseName);
						System.out.println("---------------------------与课程名称相同的企业已存在------------------------"+courseName);
						//将支付成功的订单填写的手机号码创建为半年期限的好多课会员账号
						for (int i = 0; i < userList.size(); i++) {
							// 验证用户是否存在
							Employee employee = memberService.findBusinessLoginMemberByMobile1(userList.get(i).getMobile());
							if (employee != null){
								logger.error("--------------验证用户已经注册过会员-------------------"+userList.get(i).getMobile());
								System.out.println("--------------验证用户已经注册过会员-------------------"+userList.get(i).getMobile());
								//如果是自注册用户，把用户信息更新
//								if(employee.getIfBusiness()==1L){
//									Employee upEm = new Employee();
//									upEm.setId(employee.getId());
//									upEm.setBusinessId(b.getId());
//									upEm.setBusinessName(b.getBusinessName());
//									//将系列课的课程包ID作为if_business的值
//									if(employee.getIfBusiness()==1){
//										if(a2>0){
//											upEm.setIfBusiness(58L);
//										}else{
//											upEm.setIfBusiness(200L);
//										}
//									}else{
//										upEm.setIfBusiness(employee.getIfBusiness());
//									}
//									businessService.updateEmployeeXL(upEm);
									//在t_business_member_record 表中写入会员购买记录
									MemberRecord record = new MemberRecord();
									record.setEmployeeId(employee.getId().toString());
									record.setCatalogId(offline.getCatalogId().toString());
									record.setExpiryDate(offline.getExpiryDate());
									//好多课年费会员type传0 系列课会员信息传1
									record.setType("1");
									record.setCourseId(order.getCourseId().toString());
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									Calendar calendar = Calendar.getInstance();
									//取订单创建时间
							    	calendar.setTime(new Date());
							    	//订单创建时间加上会员日期为会员过期日期
							    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(offline.getExpiryDate().toString()));
							    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							    	System.out.println(sdf.format(calendar.getTime()));
							    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						//	    	//会员过期时间
							        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
									record.setEndDate(dateTime2);
									memberService.insertMemberRecord(record);
//								}
							}else{
								Employee newE = new Employee();
								//企业Id
								newE.setBusinessId(b.getId());
								//企业名称
								newE.setBusinessName(b.getBusinessName());
								//用户手机号
								newE.setMobile(userList.get(i).getMobile());
								//用户名
								newE.setName(userList.get(i).getName());
								//职位
								newE.setPosition(userList.get(i).getContent());
								//数据状态
								newE.setStatus(1);
								//会员日期180天
								newE.setExpiryDate(185L);
								//会员类型：企业会员
									if(a2>0){
										newE.setIfBusiness(58L);
									}else{
										newE.setIfBusiness(200L);
									}
								newE.setPassword(PasswdEncryption.generate(userList.get(i).getMobile()));
								// 保存默认头像
								newE.setAvatar(commonService.getRandomAvatar());
								newE.setIfBusiness(58L);
								logger.error("--------------验证用户没有注册过会员----------------"+userList.get(i).getMobile()+"=="+businessList.get(0).getId()+"==="+businessList.get(0).getBusinessName());
								System.out.println("--------------验证用户没有注册过会员----------------"+userList.get(i).getMobile()+"=="+businessList.get(0).getId()+"==="+businessList.get(0).getBusinessName());
								//保存数据到数据库
								memberService.registerEmployeeXL(newE,null);
								//在t_business_member_record 表中写入会员购买记录
								MemberRecord record = new MemberRecord();
								record.setEmployeeId(newE.getId().toString());
								record.setCatalogId(offline.getCatalogId().toString());
								record.setExpiryDate(offline.getExpiryDate());
								//好多课年费会员type传0 系列课会员信息传1
								record.setType("1");
								record.setCourseId(order.getCourseId().toString());
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								Calendar calendar = Calendar.getInstance();
								//取订单创建时间
						    	calendar.setTime(new Date());
						    	//订单创建时间加上会员日期为会员过期日期
						    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(offline.getExpiryDate().toString()));
						    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						    	System.out.println(sdf.format(calendar.getTime()));
						    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					//	    	//会员过期时间
						        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
								record.setEndDate(dateTime2);
								memberService.insertMemberRecord(record);
							}
						}
					}else{
						logger.error("---------------------------与课程名称相同的企业不存在！！！------------------------"+courseName);
						System.out.println("---------------------------与课程名称相同的企业不存在！！！------------------------"+courseName);
						Business business = new Business();
						business.setBusinessName(courseName);
						business.setLoginName(courseName);
						business.setPassword(PasswdEncryption.generate("123456"));
						business.setBusinessLevel("100");
						businessService.saveCompany(business);
						//将支付成功的订单填写的手机号码创建为半年期限的好多课会员账号
						for (int i = 0; i < userList.size(); i++) {
							// 验证用户是否存在
							Employee employee = memberService.findBusinessLoginMemberByMobile1(userList.get(i).getMobile());
							if (employee != null) {
								logger.error("--------------验证用户已经注册过会员-------------------"+userList.get(i).getMobile());
								System.out.println("--------------验证用户已经注册过会员-------------------"+userList.get(i).getMobile());
//								if(employee.getIfBusiness()==1L){
//									Employee upEm = new Employee();
//									upEm.setId(employee.getId());
//									upEm.setBusinessId(business.getId());
//									upEm.setBusinessName(business.getBusinessName());
//									if(employee.getIfBusiness()==1){
//										if(a2>0){
//											upEm.setIfBusiness(58L);
//										}else{
//											upEm.setIfBusiness(200L);
//										}
//									}else{
//										upEm.setIfBusiness(employee.getIfBusiness());
//									}
//									businessService.updateEmployeeXL(upEm);
									//在t_business_member_record 表中写入会员购买记录
									MemberRecord record = new MemberRecord();
									record.setEmployeeId(employee.getId().toString());
									record.setCatalogId(offline.getCatalogId().toString());
									record.setExpiryDate(offline.getExpiryDate());
									//好多课年费会员type传0 系列课会员信息传1
									record.setType("1");
									record.setCourseId(order.getCourseId().toString());
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									Calendar calendar = Calendar.getInstance();
									//取订单创建时间
							    	calendar.setTime(new Date());
							    	//订单创建时间加上会员日期为会员过期日期
							    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(offline.getExpiryDate().toString()));
							    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							    	System.out.println(sdf.format(calendar.getTime()));
							    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						//	    	//会员过期时间
							        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
									record.setEndDate(dateTime2);
									memberService.insertMemberRecord(record);
//								}
							}else{
								Employee newE = new Employee();
								//企业Id
								newE.setBusinessId(business.getId());
								//企业名称
								newE.setBusinessName(business.getBusinessName());
								//用户手机号
								newE.setMobile(userList.get(i).getMobile());
								//用户名
								newE.setName(userList.get(i).getName());
								//职位
								newE.setPosition(userList.get(i).getContent());
								//数据状态
								newE.setStatus(1);
								//会员日期180天
								newE.setExpiryDate(185L);
								//会员类型：企业会员
								if(a2>0){
									newE.setIfBusiness(58L);
								}else{
									newE.setIfBusiness(200L);
								}
								//创建时间
								newE.setCreateAt(new Date());
								//密码
								newE.setPassword(PasswdEncryption.generate(userList.get(i).getMobile()));
								// 保存默认头像
								newE.setAvatar(commonService.getRandomAvatar());
								newE.setIfBusiness(58L);
								//保存数据到数据库
								logger.error("--------------验证用户没有注册过会员----------------"+userList.get(i).getMobile()+"=="+business.getId()+"==="+business.getBusinessName());
								System.out.println("--------------验证用户没有注册过会员----------------"+userList.get(i).getMobile()+"=="+business.getId()+"==="+business.getBusinessName());
								memberService.registerEmployeeXL(newE,null);
								//在t_business_member_record 表中写入会员购买记录
								MemberRecord record = new MemberRecord();
								record.setEmployeeId(newE.getId().toString());
								record.setCatalogId(offline.getCatalogId().toString());
								record.setExpiryDate(offline.getExpiryDate());
								//好多课年费会员type传0 系列课会员信息传1
								record.setType("1");
								record.setCourseId(order.getCourseId().toString());
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								Calendar calendar = Calendar.getInstance();
								//取订单创建时间
						    	calendar.setTime(new Date());
						    	//订单创建时间加上会员日期为会员过期日期
						    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(offline.getExpiryDate().toString()));
						    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						    	System.out.println(sdf.format(calendar.getTime()));
						    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					//	    	//会员过期时间
						        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
								record.setEndDate(dateTime2);
								memberService.insertMemberRecord(record);
							}
						}
					}
				}
				for (WebCourseOrderUser user : userList) {
					Long memberId = user.getMemberId();

					if (!configInfo.getProjectTest()) {
						//取报名课程的课程名称
//						String courseName1 = offline.getCourseName();
//						logger.error("---------------------------订单支付成功------------------------"+courseName);
//						System.out.println("---------------------------订单支付成功------------------------"+courseName);
//						String sub1="在线课程";
//						String sub2="系列课";
//						String sub3="深度拆解";
//						String sub10="充电";
//						String sub11="90节";
//						//判断课程名称是否包含“在线课程”
//						int b=courseName1.indexOf(sub1);
//						//判断课程名称是否包含“系列课”
//						int c=courseName1.indexOf(sub2);
//						//判断课程名称是否包含“系列课”
//						int d=courseName1.indexOf(sub3);
//						//判断课程名称是否包含“充电”
//						int e=courseName1.indexOf(sub10);
//						//判断课程名称是否包含“充电”
//						int f=courseName1.indexOf(sub11);
//						//判断是不是好多课会员开通课程
//						if(b>0){
//							commonService.send(user.getMobile(),
//									msgHaoduoke.replace("#name#", user.getName())
//											.replace("#mobile#",user.getMobile()));
//						}else if(c>0){
//							if(d>0){
//								//如果课程名称包含“系列课”，则该课程为好多课会员购买课程
//								String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR);
//								commonService.send(user.getMobile(),
//										msgXL.replace("#name#", user.getName())
//												.replace("#courseName#",offline.getCourseName()));
//							}else if(e>0){
//								commonService.send(user.getMobile(),
//										msgCD.replace("#name#", user.getName())
//												.replace("#courseName#",offline.getCourseName()));
//							}
//							else{
//								if(f<0){
//									//如果课程名称包含“系列课”，则该课程为好多课会员购买课程
//									String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR);
//									commonService.send(user.getMobile(),
//											msgXLK.replace("#name#", user.getName())
//													.replace("#courseName#",offline.getCourseName()));
//								}
//								
//							}
//							
//						}
//						else{
//							commonService
//							.send(user.getMobile(),
//									msg.replace("#name#", user.getName())
//											.replace("#time#",
//													DateUtils.formatDate(offline.getBeginTime(),
//															DateUtils.MONTH_DAY_YEAR))
//							.replace("#city#", offline.getCityName()).replace("#courseName#", offline.getCourseName()));
							if(offline.getTypeId() == 28){
								if(!StringUtils.isNullOrEmpty(offline.getMessage()) && !"null".equals(offline.getMessage())){
									String url = commonService.findQRCode(order.getCourseId());
									System.out.println("短信参数:姓名："+user.getName()+";时间："+offline.getCourseTime()+";课程名称："+offline.getCourseTime()+";手机号："+user.getMobile());
									System.out.println("最终短信："+offline.getMessage().replace("#name#", user.getName())
											.replace("#time#",offline.getCourseTime()).replace("#courseName#",offline.getCourseName()).replace("#mobile#",user.getMobile()));
									String msg = offline.getMessage().replace("#name#", user.getName())
											.replace("#time#",offline.getCourseTime()).replace("#courseName#",offline.getCourseName()).replace("#mobile#",user.getMobile());
									commonService.sendCLMsg(user.getMobile(),msg);
									System.out.println("线上课程最终发送的短信内容为："+offline.getMessage());
								}
//								String url = commonService.findQRCode(order.getCourseId());
//								if(offline.getBeginTime()!=null && offline.getEndTime()!= null){
//									if(DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR).equals(DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY_YEAR))){
////										String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
//										String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR);
//										commonService.send(user.getMobile(),
//												msgQRCode.replace("#name#", user.getName())
//														.replace("#time#",
//																classDate)
//												.replace("#city#", offline.getCityName()).replace("#courseName#", offline.getCourseName()).replace("#qrcode#", url));
//									}else{
////										String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
//										String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR);
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
							}else{
								if(offline.getAddress()=="" || "".equals(offline.getAddress())){
									if(offline.getBeginTime()!=null && offline.getEndTime()!= null){
										if(DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR).equals(DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY_YEAR))){
											String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
											commonService.send(user.getMobile(),
													msg.replace("#name#", user.getName())
															.replace("#time#",
																	classDate)
													.replace("#city#", offline.getCityName()).replace("#courseName#", offline.getCourseName()));
										}else{
											//判断两个日期的月份是否相同
											if(DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH).equals(DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH))){
												String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
												commonService.send(user.getMobile(),
														msgAddress.replace("#name#", user.getName())
																.replace("#time#",
																		classDate)
														.replace("#address#", offline.getAddress()).replace("#address#", offline.getAddress()));
											}else{
												String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY_)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
												commonService.send(user.getMobile(),
														msgAddress.replace("#name#", user.getName())
																.replace("#time#",
																		classDate)
														.replace("#address#", offline.getAddress()).replace("#address#", offline.getAddress()));
											}
										}
									}else{
									commonService.send(user.getMobile(),
											msg.replace("#name#", user.getName())
													.replace("#time#",
															DateUtils.formatDate(offline.getBeginTime(),
																	DateUtils.MONTH_DAY_YEAR))
											.replace("#city#", offline.getCityName()).replace("#courseName#", offline.getCourseName()));
									}
								}else{
									if(offline.getBeginTime()!=null && offline.getEndTime()!= null){
										if(DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR).equals(DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY_YEAR))){
										String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
										commonService.send(user.getMobile(),
												msgAddress.replace("#name#", user.getName())
														.replace("#time#",
																classDate)
												.replace("#city#", offline.getCityName()).replace("#courseName#", offline.getCourseName())
												.replace("#address#", offline.getAddress()).replace("#address#", offline.getAddress()));
										}else{
											//判断两个日期的月份是否相同
											if(DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH).equals(DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH))){
												String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
												commonService.send(user.getMobile(),
														msgAddress.replace("#name#", user.getName())
																.replace("#time#",
																		classDate)
														.replace("#address#", offline.getAddress()).replace("#address#", offline.getAddress()));
											}else{
												String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY_)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
												commonService.send(user.getMobile(),
														msgAddress.replace("#name#", user.getName())
																.replace("#time#",
																		classDate)
														.replace("#address#", offline.getAddress()).replace("#address#", offline.getAddress()));
											}
										}
									}else{
									commonService.send(user.getMobile(),
											msgAddress.replace("#name#", user.getName())
													.replace("#time#",
															DateUtils.formatDate(offline.getBeginTime(),
																	DateUtils.MONTH_DAY_YEAR))
											.replace("#city#", offline.getCityName()).replace("#courseName#", offline.getCourseName())
											.replace("#address#", offline.getAddress()).replace("#address#", offline.getAddress()));
									}
								}
							}
//						}
						
					}

					if (NumberUtils.isNullOrZero(memberId)) {
						Member member = new Member();
						member.setMobile(user.getMobile());
						member.setPassword(PasswdEncryption.generate("123456"));
						member.setNickName(user.getName());
						member.setAccessToken(CodeUtils.getUUID());
						member.setClientVersion("1.0");
						member.setDeviceId("null");
						// 保存默认头像
						member.setAvatar(commonService.getRandomAvatar());
						memberService.register(member, null, user.getWork());

						memberId = member.getId();

						// 更新ID
						orderService.updateWebCourseOrderUserId(memberId, user.getId());
					}

					// 同时保存报名信息
					CourseOfflineJoin join = new CourseOfflineJoin();
					join.setCourseId(order.getCourseId());
					join.setMemberId(memberId);
					join.setType(2); // WEB
					join.setFrom(1);
					join.setOrderId(orderId);;
					courseService.saveCourseJoin(join);
				}

				if(order.getTicketStatus() == 1){
					Params params = new Params();
					Long id = commonService.findHaveTicket(order.getUnionid());
					if (id != null) {
						params.putData("id", id);
						params.putData("unionId", order.getUnionid());
						params.putData("status", 3);

						commonService.updateTicketStatus(params);
					}
				}
				// 修改订单信息
				orderService.updateWebCourseOrderStatus(order.getId(), userList.size(), order.getCourseId());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/paysuccess";
	}

	
	
	/**
	 * @Title: saveWebCourseOrder
	 * @Description: (提交预订单)
	 * @param sex
	 *            性别
	 * @param content
	 *            内容
	 * @param name
	 *            姓名
	 * @param mobile
	 *            手机号
	 * @param weixin
	 *            微信号
	 * @param courseId
	 *            课程ID
	 * @param unionid
	 *            唯一ID
	 * @param openid
	 *            openId
	 * @param number
	 *            购买数量
	 * @param ip
	 *            ip地址
	 * @param ticketStatus 是否使用奖学金 1:使用 0:不使用
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveLivingWebCourseOrder")
	public JSONObject saveLivingWebCourseOrder(String[] sex, String[] name, String[] mobile, String[] work,
			String[] weixin, Long courseId, String unionid, String openid, String ip, String nickname,Double price,
			String weixinSex, String avatar, Integer from,Integer weixinType,HttpServletRequest requestm,String job,String email) {
		
		if(StringUtils.isNullOrEmpty(ip)){
//			ip = request.getRemoteAddr();
			ip = "47.153.191.255";
		}
		//weixinType 是否为 1 判断是JSAPI 还是 NATIVE
		try {
			if (sex.length != name.length && name.length != mobile.length && mobile.length != name.length
					&& weixin.length != name.length && name.length == 0) {
				return error("参数错误!");
			}
			int number = name.length;
			if (NumberUtils.isNullOrZero(courseId) || StringUtils.isNullOrEmpty(openid)
					|| StringUtils.isNullOrEmpty(unionid) || NumberUtils.isNullOrZero(number)) {
				return error("参数错误!");
			}
//			Course course = courseService.findCourseById(courseId);
//			OfflineCourse offline = courseService.findOfflineCourseByCourseId(courseId);
//			if (offline == null || course == null) {
//				return error("参数错误");
//			}
//			int joinNumber = courseService.findCourseJoinCount(courseId);
//			if (joinNumber + number > offline.getStudentNum()) {
//				int temp = offline.getStudentNum() - joinNumber;
//				return status(800, "购买数量过多", temp >= 0 ? temp : 0);
//			}
			
//			if(offline.getCourseId() == 129){
//				return status(800, "购买数量过多", 0);
//			}
			List<JNWebCourseOrder> list1 = orderService.getWhetherBuyCourseByUnionid(courseId.toString(),unionid);
			if (list1 !=null && list1.size()>0) {
				return status(600, "已报过名");
			}
			//默认报名截止时间为课程开讲前一天
			LocalDate now = LocalDate.now();
//			LocalDate begin = LocalDate.parse(DateUtils.formatDate(course.getBeginTime(), DateUtils.DATE_FORMAT_NORMAL));
//			if (now.isAfter(begin)){
//				return status(800, "报名时间已过", 0);
//			}
			
			Double price1 = 0.0d;
//			if(!NumberUtils.isNullOrZero(qrcode) && courseId == 129L){
//				price = NumberUtils.mul(offline.getPrice(), 0.88);
//			}else{
//				if(offline.getDiscount() == 100){
//					//不打折
					price1 = NumberUtils.mul(price, number); // 计算价格
//				}else{
					//计算价格
//					for (int i = 0; i < mobile.length; i++) {
//						// 查询用户
//						Member member = memberService.findLoginMemberByMobile(mobile[i]);
//						if (member == null) {
//							price = NumberUtils.add(price, offline.getPrice());
//						}else{
//							Integer count = courseService.findMyJoinCourseCount(member.getId());
//							if(count > 0){
//								// 计算折扣
//								price = NumberUtils.mul(offline.getPrice(), NumberUtils.div(offline.getDiscount(), 100));
//							}else{
//								price = NumberUtils.add(price, offline.getPrice());
//							}
//						}
//					}
//				}
//			}
			
			
//			if(ticketStatus == 1){
//				//查询奖学金ID
//				Long id = commonService.findHaveTicket(unionid);
//				if (id == null) {
//					return status(300, "没有奖学金");
//				}
//				Ticket ticket = commonService.findTicket(id);
//				price = NumberUtils.sub(price, ticket.getPrice());
//			}
			// 生成订单号
			String code = CodeUtils.getCourseOrderCode("WEB", courseId, Long.valueOf(CodeUtils.getRandomInt(100) + 1));
			if(code.length()>32){
				code = code.substring(0,31);
			}
			String nonceStr = CodeUtils.getUUID();// 随机字符串

			SortedMap<String, String> par = new TreeMap<>();
			par.put("appid", appid);
			par.put("attach", String.valueOf(number));
			par.put("body", "ChaZuoMBA:" + code);
			par.put("device_info", "WEB");
			par.put("nonce_str", nonceStr);
			par.put("mch_id", mchId);

			StringBuffer sbWX = new StringBuffer();
			sbWX.append("<xml>").append("<appid>").append(appid).append("</appid>");

			sbWX.append("<attach><![CDATA[").append(String.valueOf(number)).append("]]></attach>");
			sbWX.append("<body><![CDATA[").append("ChaZuoMBA:" + code).append("]]></body>");

			sbWX.append("<device_info>").append("WEB").append("</device_info>");
			sbWX.append("<mch_id>").append(mchId).append("</mch_id>");
			sbWX.append("<nonce_str>").append(nonceStr).append("</nonce_str>");
			if (configInfo.getProjectTest()) {
				par.put("notify_url", "http://test.chazuomba.com:8080/iserver/app/weixinLivingWebPayNotify");
				sbWX.append("<notify_url>").append("http://test.chazuomba.com:8080/iserver/app/weixinLivingWebPayNotify").append("</notify_url>");
			} else {
				par.put("notify_url", "http://www.chazuomba.com/iserver/app/weixinLivingWebPayNotify");
				sbWX.append("<notify_url>").append("http://www.chazuomba.com/iserver/app/weixinLivingWebPayNotify").append("</notify_url>");
			}
			sbWX.append("<openid>").append(openid).append("</openid>");
			sbWX.append("<out_trade_no>").append(code).append("</out_trade_no>");
			sbWX.append("<spbill_create_ip>").append(ip).append("</spbill_create_ip>");
			int pr = (int) NumberUtils.mul(price1, 100);
			sbWX.append("<total_fee>").append(pr).append("</total_fee>");
			if(NumberUtils.isNullOrZero(weixinType)){
				sbWX.append("<trade_type>").append("JSAPI").append("</trade_type>");
			}else if(weixinType == 1){
				sbWX.append("<trade_type>").append("NATIVE").append("</trade_type>");
			}
			
//			sbWX.append("<attach>").append(number).append("</attach>");

			par.put("openid", openid);
			par.put("out_trade_no", code);
			par.put("spbill_create_ip", ip);
			par.put("total_fee", pr + "");
			if(NumberUtils.isNullOrZero(weixinType)){
				par.put("trade_type", "JSAPI");
			}else if(weixinType == 1){
				par.put("trade_type", "NATIVE");
			}
			
			String sign = commonService.createSign(par, apiKey);
			System.out.println(sign);
			sbWX.append("<sign><![CDATA[").append(sign).append("]]></sign>");
			sbWX.append("</xml>");
			System.out.println(sbWX);

			HttpClient client = HttpClients.createDefault();
			HttpPost post = new HttpPost("https://api.mch.weixin.qq.com/pay/unifiedorder");
			post.setHeader("Content-type", "text/xml; charset=UTF-8");
			StringEntity entity = new StringEntity(sbWX.toString());
			post.setEntity(entity);

			HttpResponse response = client.execute(post);
			HttpEntity entity2 = response.getEntity();
			if (entity2 != null) {
				String result2 = EntityUtils.toString(entity2, "UTF-8");

				Map<String, String> result = parseXmlToMap(result2);

				WebCourseOrderInfo info = new WebCourseOrderInfo();
				info.setAppid(appid);
				info.setPrepayId(result.get("prepay_id"));
				info.setNonceStr(nonceStr);
				info.setOrderCode(code);
				info.setSign(sign);
				info.setMchId(mchId);
				info.setApiKey(apiKey);
				if(weixinType == 1){
					info.setCodeUrl(result.get("code_url"));
				}
				
				JNWebCourseOrder order = new JNWebCourseOrder();
				order.setCourseId(courseId);
				order.setIp(ip);
				order.setNumber(number);
				order.setOpenid(openid);
				order.setOrderCode(code);
				order.setPrice(price);
				order.setStatus(0);
				order.setUnionid(unionid);
				order.setType(0);
				order.setNickname(nickname);
				order.setWeixinSex(weixinSex);
				order.setAvatar(avatar);
				order.setFrom(NumberUtils.isNullOrZero(from) ? 0 : from);
				order.setJob(job);
				order.setEmail(email);
				orderService.saveJNWebCourseOrder(order);

				List<JNWebCourseOrderUser> ulist = new ArrayList<JNWebCourseOrderUser>();
				for (int i = 0; i < name.length; i++) {
					Long id = 0L;
					// 查询用户
					Member member = memberService.findLoginMemberByMobile(mobile[i]);
					if (member != null) {
						id = member.getId();
					}
					if(id != 0){
						List<JNWebCourseOrder> list = orderService.getWhetherBuyCourseByUnionid(courseId.toString(),unionid);
						if (list !=null && list.size()>0) {
							return status(600, "已报过名..", mobile[i]);
						}	
					}
					
					JNWebCourseOrderUser user = new JNWebCourseOrderUser();
					user.setMemberId(member == null ? 0L : member.getId());
					user.setMobile(mobile[i]);
					//获取手机号码归属地
//					user.setCity(memberService.getMobileFrom(mobile[i]));
					String str = null;
					String province="";
					String city="";
					JSONArray jsonArray = null;
					str = "[" +memberService.request(mobile[i]) + "]";
					jsonArray = new JSONArray(str);
					int errNumResult = (int) jsonArray.getJSONObject(0).get("errNum");
					if(errNumResult == 0){
						org.json.JSONObject jsonresult = (org.json.JSONObject) jsonArray.getJSONObject(0).get("retData");
						province = jsonresult.getString("province");
						city = jsonresult.getString("city");
					}
					user.setCity(StringUtils.isNullOrEmpty(city) ? "" : city);
					user.setProvince(StringUtils.isNullOrEmpty(province) ? "" : province);
					user.setName(name[i]);
					user.setOrderId(order.getId());
					if (sex[i].equals("man")) {
						user.setSex("男");
					} else {
						user.setSex("女");
					}
					user.setWeixin(weixin[i]);
					user.setWork(work[i]);

					ulist.add(user);
				}
				orderService.saveJNWebCourseOrderUser(ulist);
//				WebCourseOrder orderq = orderService.findWebCourseOrder(code);
				//获取订单的Id，用于存储在offline_join表中
				Long orderId = order.getId();
				JNCourseOnlineJoin join = new JNCourseOnlineJoin();
				join.setCourseId(order.getCourseId());
				join.setMemberId(Long.valueOf("0"));
				join.setOrderId(orderId);
				join.setType(2); // WEB
				join.setFrom(2);
				courseService.saveJNCourseJoin(join);
				return ok("生成成功",info);
			}

			return error("生成失败");
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: weixinWebCrowdfundingLogPayNotify
	 * @Description: (回调)
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/weixinLivingWebCrowdfundingLogPayNotify")
	public String weixinWebLivingCrowdfundingLogPayNotify(HttpServletRequest request, HttpServletResponse response) {
		String resXml = "";
		try {
			InputStream inStream = request.getInputStream();
			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}
			outSteam.close();
			inStream.close();

			String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息

			Map<String, String> m = parseXmlToMap(result);

			if ("SUCCESS".equals(m.get("result_code").toString())) {
				String tradeNo = m.get("out_trade_no").toString();

				String attach = m.get("attach").toString();
				if (StringUtils.isNullOrEmpty(attach)) {
					resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
							+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
					return resXml;
				}

				Integer type = Integer.parseInt(attach);
				if (type == 2) {
					CourseWebCrowdfundingLog log = crowdfundingService.findCrowdfundingLogInfo(tradeNo);
					String uuid = crowdfundingService.findCrowdfundUUIDById(log.getCrowdfundId());

					CourseWebCrowdfundingInfo info = crowdfundingService.findCrowdfundingInfo(uuid);
					double p = crowdfundingService.findCrowdfundingPriceCount(uuid);
					p = NumberUtils.add(p, log.getPrice());
					double price = NumberUtils.sub(info.getPrice(), p);
					if (price <= 0 && info.getStatus() == 1) {
						saveUsers(info.getCourseId(), log.getCrowdfundId());
						crowdfundingService.updateCrowfundingStatus(2, log.getCrowdfundId());
					}

					crowdfundingService.updateCrowdfundingLogStatus(tradeNo);
				} else {

					CourseWebCrowdfundingOrder order = crowdfundingService.findCrowdfundingOrderInfo(tradeNo);
					String uuid = crowdfundingService.findCrowdfundUUIDById(order.getCrowdfundingId());
					CourseWebCrowdfunding info = crowdfundingService.findUserCrowdfundInfoByUUID(uuid);

					crowdfundingService.updateCrowfundingStatus(2, order.getCrowdfundingId());
					crowdfundingService.updateCrowdfundOrderStatus(order.getId());

					saveUsers(info.getCourseId(), order.getCrowdfundingId());
				}

				// 支付成功
				resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
						+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
			} else {
				resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
						+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
			}

//			System.out.println("微信支付回调数据结束");

//			BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
//			out.write(resXml.getBytes());
//			out.flush();
//			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resXml;
	}
	/**
	 * @Title: weixinWebPayNotify
	 * @Description: (Web微信支付回调)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/weixinLivingWebPayNotify")
	public String weixinLivingWebPayNotify(HttpServletRequest request, HttpServletResponse response) {
		String resXml = "";
		try {
			InputStream inStream = request.getInputStream();
			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}
			outSteam.close();
			inStream.close();

			String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息

			Map<String, String> m = parseXmlToMap(result);

			if ("SUCCESS".equals(m.get("result_code").toString())) {
				String tradeNo = m.get("out_trade_no").toString();
				// 支付成功
				JNWebCourseOrder order = orderService.findJNWebCourseOrder(tradeNo);
				logger.error("微信支付成功，订单号："+order.getOrderCode());
				//获取订单的Id，用于存储在offline_join表中
				Long orderId = order.getId();
				logger.error("微信支付成功，订单id："+orderId);
				if (order == null || order.getStatus() == 1) {
					// 支付成功
					resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
							+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
					BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
					out.write(resXml.getBytes());
					out.flush();
					out.close();
					return null;
				} else {

					List<JNWebCourseOrderUser> userList = orderService.findJNWebCourseOrderUserByCourseId(order.getId());

					OfflineMsgInfo offline = courseService.findOfflineMsgInfo(order.getCourseId());
					for (JNWebCourseOrderUser user : userList) {
						Member me = memberService.findLoginMemberByMobile(user.getMobile());
						Long memberId = (me == null ? null : me.getId());
						logger.error("memberId的值为："+memberId);
						if (NumberUtils.isNullOrZero(memberId)) {
							logger.error("memberId的值判断为NullOrZero");
							Member member = new Member();
							member.setMobile(user.getMobile());
							member.setPassword(PasswdEncryption.generate("123456"));
							member.setNickName(user.getName());
							member.setAccessToken(CodeUtils.getUUID());
							member.setClientVersion("1.0");
							member.setDeviceId("null");
							// 保存默认头像
							member.setAvatar(commonService.getRandomAvatar());
							logger.error("新增用户参数：mobile："+user.getMobile()+";userName:"+user.getName());
							memberService.register(member, null, user.getWork());
							Member member1 = memberService.findLoginMemberByMobile(user.getMobile());
							
							memberId = member.getId();
							logger.error("memberId的值:"+memberId);
							Long memberId1 = member1.getId();
							logger.error("memberId1的值:"+memberId1);
							// 更新ID
							orderService.updateJNWebCourseOrderUserId(memberId, user.getId());
						}
						
						JNCourseOnlineJoin join = courseService.findJNCourseJoinInfo(order.getCourseId(),memberId);

						if(join == null){
							// 同时保存报名信息
							join = new JNCourseOnlineJoin();
							join.setCourseId(order.getCourseId());
							join.setMemberId(memberId);
							join.setOrderId(orderId);
							join.setType(2); // WEB
							join.setFrom(2);
							courseService.saveJNCourseJoin(join);
							
							//发短信
//							if (!configInfo.getProjectTest()) {
//								logger.error("发送短信参数:手机号："+user.getMobile()+"学员姓名："+user.getName()+"课程名称："+offline.getCityName()+"课程地址："+offline.getAddress());
//								if(order.getCourseId() == 144){
//									commonService.send(user.getMobile(),msg144.replace("#name#", user.getName()));
//								}else{
//									
//									if(offline.getTypeId() == 28){
//										String url = commonService.findQRCode(order.getCourseId());
//										if(offline.getBeginTime()!=null && offline.getEndTime()!= null){
//											if(DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR).equals(DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY_YEAR))){
//												String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
//												commonService.send(user.getMobile(),
//														msgQRCode.replace("#name#", user.getName())
//																.replace("#time#",classDate));
//											}else{
//												String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
//												commonService.send(user.getMobile(),
//														msgQRCode.replace("#name#", user.getName())
//																.replace("#time#",classDate));
//											}
//											
//										}else{
//											commonService.send(user.getMobile(),
//													msgQRCode.replace("#name#", user.getName())
//															.replace("#time#",
//																	DateUtils.formatDate(offline.getBeginTime(),
//																			DateUtils.MONTH_DAY_YEAR)));
//										}
////												.replace("#city#", offline.getCityName()).replace("#courseName#", offline.getCourseName()).replace("#qrcode#", url))
//									}else{
//										if(offline.getAddress()=="" || "".equals(offline.getAddress())){
//											logger.error("课程名称："+offline.getCourseName()+"的开始地址为："+offline.getAddress()+"程序判断为非空，发送msg短信内容！");
//											if(offline.getBeginTime()!=null && offline.getEndTime()!= null){
//												if(DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR).equals(DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY_YEAR))){
//													String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
//													commonService.send(user.getMobile(),
//															msg.replace("#name#", user.getName())
//																	.replace("#time#",classDate));
//												}else{
//													String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
//													commonService.send(user.getMobile(),
//															msg.replace("#name#", user.getName())
//																	.replace("#time#",classDate));
//												}
//											}else{
//												commonService.send(user.getMobile(),
//														msg.replace("#name#", user.getName())
//																.replace("#time#",
//																		DateUtils.formatDate(offline.getBeginTime(),
//																				DateUtils.MONTH_DAY_YEAR)));
//											}
////													.replace("#city#", offline.getCityName()).replace("#courseName#", offline.getCourseName()));
//											logger.error("最终发送短信的内容为："+msg);
//										}else{
//											logger.error("课程名称："+offline.getCourseName()+"的开始地址为："+offline.getAddress()+"程序判断为空，发送msgAddress短信内容！");
//											if(offline.getBeginTime()!=null && offline.getEndTime()!= null){
//												if(DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR).equals(DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY_YEAR))){
//													String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
//													commonService.send(user.getMobile(),
//															msgAddress.replace("#name#", user.getName())
//																	.replace("#time#",
//																			classDate)
//															.replace("#address#", offline.getAddress()).replace("#address#", offline.getAddress()));
//												}else{
//													String classDate = DateUtils.formatDate(offline.getBeginTime(),DateUtils.MONTH_DAY_YEAR)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.MONTH_DAY)+"("+DateUtils.formatDate(offline.getBeginTime(),DateUtils.HOUR_MINUTE)+"-"+DateUtils.formatDate(offline.getEndTime(),DateUtils.HOUR_MINUTE)+")";
//													commonService.send(user.getMobile(),
//															msgAddress.replace("#name#", user.getName())
//																	.replace("#time#",
//																			classDate)
//															.replace("#address#", offline.getAddress()).replace("#address#", offline.getAddress()));
//												}
//											}else{
//												commonService.send(user.getMobile(),
//														msgAddress.replace("#name#", user.getName())
//																.replace("#time#",
//																		DateUtils.formatDate(offline.getBeginTime(),
//																				DateUtils.MONTH_DAY_YEAR))
//														.replace("#address#", offline.getAddress()).replace("#address#", offline.getAddress()));
//											}
//											logger.error("最终发送短信的内容为："+msgAddress);
//										}
//									}
//								}
//							}
						}
						
//						if(order.getTicketStatus() == 1){
//							Params params = new Params();
//							Long id = commonService.findHaveTicket(order.getUnionid());
//							if (id != null) {
//								params.putData("id", id);
//								params.putData("unionId", order.getUnionid());
//								params.putData("status", 3);
//
//								commonService.updateTicketStatus(params);
//							}
//						}
					}

					// 修改订单信息
					orderService.updateJNWebCourseOrderStatus(order.getId(), userList.size(), order.getCourseId());
				}

				// 支付成功
				resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
						+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
			} else {
				resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
						+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
			}

			System.out.println("微信支付回调数据结束");

			BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
			out.write(resXml.getBytes());
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * @Title: getWhetherBuyCourseByUnionid
	 * @Description: (根据unionid和课程id查询是否购买了课程)
	 * @param courseId
	 *            课程ID
	 * @param unionid
	 *            微信用户ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getWhetherBuyCourseByUnionid")
	public JSONObject getWhetherBuyCourseByUnionid(String courseId, String unionId) {
		try {
			if (StringUtils.isNullOrEmpty(courseId)) {
				return error("课程Id参数错误!");
			}
			if(StringUtils.isNullOrEmpty(unionId)){
				return error("用户Id参数错误!");
			}
			//判断uinionId是否是年票学员额unionId
			List<JNWebCourseOrder> list1 =  orderService.getWhetherYearByUnionid(unionId);
			if(list1.size()>0){
				return ok("已购买");
			}else{
				List<JNWebCourseOrder> list = orderService.getWhetherBuyCourseByUnionid(courseId,unionId);
				if (list.size() > 0) {
					return ok("已购买");
				}else{
					return status(300,"未购买");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: getWhetherBuyCourseByUnionid
	 * @Description: (根据unionid和课程id查询是否购买了大课课程名额)
	 * @param courseId
	 *            课程ID
	 * @param unionid
	 *            微信用户ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getIfBuyCourseByUnionid")
	public JSONObject getIfBuyCourseByUnionid(String courseId, String unionId) {
		try {
			if (StringUtils.isNullOrEmpty(courseId)) {
				return error("课程Id参数错误!");
			}
			if(StringUtils.isNullOrEmpty(unionId)){
				return error("用户Id参数错误!");
			}
			//判断uinionId是否是年票学员额unionId
			List<WebCourseOrder> list1 =  orderService.getIfBuyCourseByUnionid(unionId);
			if(list1.size()>0){
				return status(200,list1.get(0).getOrderCode());
			}else{
//				List<JNWebCourseOrder> list = orderService.getWhetherBuyCourseByUnionid(courseId,unionId);
//				if (list.size() > 0) {
//					return ok("已购买");
//				}else{
					return status(300,"未购买");
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	@RequestMapping("/baiduLivingPayNotify")
	public String baiduLivingPayNotify(String order_no, String pay_result) {
		try {
			if ("1".equals(pay_result)) {
				// 支付成功
				JNWebCourseOrder order = orderService.findJNWebCourseOrder(order_no);
				Long orderId = order.getId();

				if (order != null && order.getStatus() == 1) {
					return "/paysuccess";
				}
				List<JNWebCourseOrderUser> userList = orderService.findJNWebCourseOrderUserByCourseId(order.getId());
				OfflineMsgInfo offline = courseService.findOfflineMsgInfo(order.getCourseId());

				for (JNWebCourseOrderUser user : userList) {
					Long memberId = user.getMemberId();

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

					if (NumberUtils.isNullOrZero(memberId)) {
						Member member = new Member();
						member.setMobile(user.getMobile());
						member.setPassword(PasswdEncryption.generate("123456"));
						member.setNickName(user.getName());
						member.setAccessToken(CodeUtils.getUUID());
						member.setClientVersion("1.0");
						member.setDeviceId("null");
						// 保存默认头像
						member.setAvatar(commonService.getRandomAvatar());
						memberService.register(member, null, user.getWork());

						memberId = member.getId();

						// 更新ID
						orderService.updateJNWebCourseOrderUserId(memberId, user.getId());
					}

					// 同时保存报名信息
					JNCourseOnlineJoin join = new JNCourseOnlineJoin();
					join.setCourseId(order.getCourseId());
					join.setMemberId(memberId);
					join.setType(2); // WEB
					join.setFrom(1);
					join.setOrderId(orderId);;
					courseService.saveJNCourseJoin(join);
				}

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
				// 修改订单信息
				orderService.updateWebCourseOrderStatus(order.getId(), userList.size(), order.getCourseId());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/paysuccess";
	}
	/**
	 * @Title: saveWebCourseOrder
	 * @Description: (提交预订单)
	 * @param sex
	 *            性别
	 * @param content
	 *            内容
	 * @param name
	 *            姓名
	 * @param mobile
	 *            手机号
	 * @param weixin
	 *            微信号
	 * @param courseId
	 *            课程ID
	 * @param unionid
	 *            唯一ID
	 * @param openid
	 *            openId
	 * @param number
	 *            购买数量
	 * @param ip
	 *            ip地址
	 * @param ticketStatus 是否使用奖学金 1:使用 0:不使用
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveWebCourseOrderNew")
	public JSONObject saveWebCourseOrderNew(String sex, String content, String name, String mobile, String work,
			String weixin, Long courseId, String unionid, String openid, String ip, String nickname,String isGift,
			String weixinSex, String avatar, String from,Integer ticketStatus,Integer weixinType,Integer qrcode,String formNickName,String joinReason,HttpServletRequest requestm,String job,String buyIntentions,String email,String invoiceType,String invoiceAddress,String invoiceTitle,String invoiceRemarks) {
		
		if(StringUtils.isNullOrEmpty(ip)){
//			ip = request.getRemoteAddr();
			ip = "47.153.191.255";
		}
//		logger.error("生成订单参数长度：sex:"+sex+",name:"+name.length+",mobile:"+mobile.length+",work:"+work.length+",weixin:"+weixin.length);
		logger.error("新的接口生成订单参数：sex:"+sex+",name:"+name+",mobile:"+mobile+",work:"+work+",weixin:"+weixin+",courseId:"+courseId+",unionId:"+unionid+",openId:"+openid);
		logger.error("新的接口生成订单参数：nickname:"+nickname+",weixinSex:"+weixinSex+",avatar:"+avatar+",from:"+from+",ticketStatus:"+ticketStatus+",weixinType:"+weixinType+",qrcode:"+qrcode+",formNickName:"+formNickName+",job:"+job);
		logger.error("新的接口生成订单参数：buyIntentions:"+buyIntentions+",email:"+email+",invoiceType:"+invoiceType+",invoiceAddress:"+invoiceAddress+",invoiceTitle:"+invoiceTitle+",invoiceRemarks:"+invoiceRemarks+",isGift:"+isGift);
//		System.out.println("生成订单参数长度：sex:"+sex.length+",content:"+content.length+",name:"+name.length+",mobile:"+mobile.length+",work:"+work.length+",weixin:"+weixin.length);
//		System.out.println("新的接口生成订单参数：sex:"+sex+",name:"+name+",mobile:"+mobile+",work:"+work+",weixin:"+weixin+",courseId:"+courseId+",unionId:"+unionid+",openId:"+openid);
//		System.out.println("新的接口生成订单参数：nickname:"+nickname+",weixinSex:"+weixinSex+",avatar:"+avatar+",from:"+from+",ticketStatus:"+ticketStatus+",weixinType:"+weixinType+",qrcode:"+qrcode+",formNickName:"+formNickName+",job:"+job);
//		System.out.println("新的接口生成订单参数：buyIntentions:"+buyIntentions+",email:"+email+",invoiceType:"+invoiceType+",invoiceAddress:"+invoiceAddress+",invoiceTitle:"+invoiceTitle+",invoiceRemarks:"+invoiceRemarks+",isGift:"+isGift);
		//weixinType 是否为 1 判断是JSAPI 还是 NATIVE
		try {
			if(ticketStatus == null){
				ticketStatus = 0;
			}
			if (StringUtils.isNullOrEmpty(sex) || StringUtils.isNullOrEmpty(name) ||  StringUtils.isNullOrEmpty(mobile) ||
					StringUtils.isNullOrEmpty(weixin)) {
				return error("参数错误!");
			}
			int number = 1;
			if (NumberUtils.isNullOrZero(courseId) || StringUtils.isNullOrEmpty(openid)
					 || NumberUtils.isNullOrZero(number)) {
				return error("参数错误!");
			}
			Course course = courseService.findCourseById(courseId);
			OfflineCourse offline = courseService.findOfflineCourseByCourseId(courseId);
			if (offline == null || course == null) {
				return error("参数错误");
			}
			//取报名课程的课程名称
			String courseName = course.getCourseName();
			String sub="在线课程";
			String sub1="系列课";
			//判断课程名称是否包含“好多课”
			int a=courseName.indexOf(sub);
			int b=courseName.indexOf(sub1);
			//如果课程名称包含“在线课程”，则该课程为好多课会员购买课程
			if(a>=0){
				logger.error("生成订单时判断出课程名称中包含‘在线课程’");
				//判断填写的手机号码是否已经注册了好多课App账号
//				for (int i = 0; i < mobile.length; i++) {
					// 验证用户是否存在
					Employee employee = memberService.findBusinessLoginMemberByMobile1(mobile);
					if (employee != null) {
						logger.error("验证手机号码："+mobile+"已注册过企业会员");
						List<MemberRecord> recordList = businessService.findMemberRecordList(employee.getId().toString());
						if(recordList.size()>0){
							return status(700,"您已经是好多课年票会员，直接登录App学习吧~");
						}
//							if(employee.getIfBusiness()==0){
//								return status(700,"您填写的手机号已经注册了好多课会员，直接去登录吧");
//							}
					}
					logger.error("验证手机号码："+mobile+"没有注册过企业会员");
				}
//			}
			int joinNumber = courseService.findCourseJoinCount(courseId);
			if (joinNumber + number > offline.getStudentNum()) {
				int temp = offline.getStudentNum() - joinNumber;
				return status(800, "购买数量过多", temp >= 0 ? temp : 0);
			}
			
			if(offline.getCourseId() == 129){
				return status(800, "购买数量过多", 0);
			}
			//默认报名截止时间为课程开讲前一天
			LocalDate now = LocalDate.now();
			LocalDate begin = LocalDate.parse(DateUtils.formatDate(course.getBeginTime(), DateUtils.DATE_FORMAT_NORMAL));
			if (now.isAfter(begin)){
				return status(800, "报名时间已过", 0);
			}
			
			Double price = 0.0d;
			if(!NumberUtils.isNullOrZero(qrcode) && courseId == 129L){
				price = NumberUtils.mul(offline.getPrice(), 0.88);
			}else{
				if(offline.getDiscount() == 100){
					//不打折
					price = NumberUtils.mul(offline.getPrice(), number); // 计算价格
					//如果课程名称包含“好多课”，则该课程为好多课会员购买课程
					if(b>=0 || ("2").equals(offline.getNewtype()) || ("5").equals(offline.getNewtype())){
						logger.error("生成订单时判断出课程名称中包含‘系列课’");
						//判断填写的手机号码是否已经注册了好多课App账号
//						for (int i = 0; i < mobile.length; i++) {
							// 验证用户是否存在
							Employee employee = memberService.findBusinessLoginMemberByMobile1(mobile);
							if (employee != null) {
								logger.error("验证手机号码："+mobile+"已注册过企业会员");
//								if(employee.getIfBusiness()==0){
								//查询是否该用户为年费会员用户
								List<MemberRecord> recordList = businessService.findMemberRecordList(employee.getId().toString());
								if(recordList.size()>0){
//									String sub5="深度拆解";
//									String sub6="每日问答";
									//判断课程名称是否包含“深度拆解”
//									int a2=courseName.indexOf(sub5);
//									int a9=courseName.indexOf(sub6);
									//包含深度拆解就打折，否则不让购买
//									if(a2>=0){
//										price = NumberUtils.mul(offline.getPrice(), 0.5);
//									}else 
//									if(a9>=0){
//										price = offline.getPrice();
//									}else{
									if(("2".equals(offline.getNewtype()))){
										Employee employeeInfo = memberService.findBusinessLoginMemberByUnionId(unionid);
										if(employeeInfo==null){
											if(employee.getUnionid()==null||("null").equals(employee.getUnionid())||("").equals(employee.getUnionid())||StringUtils.isNullOrEmpty(employee.getUnionid())){
												Employee upEm = new Employee();
												upEm.setUnionid(unionid);
												upEm.setOpenid(openid);
												upEm.setWxAvatar(avatar);
												upEm.setWxName(nickname);
												upEm.setId(employee.getId());
												businessService.updateEmployee1(upEm);
											}
										}
									}
										return status(700,"您已经是好多课年票会员，直接登录App学习吧~");
//									}
								}else{
									List<MemberRecord> recordList1 = businessService.findMemberRecordListByCatalogId(employee.getId().toString(),offline.getCatalogId());
									if(recordList1.size()>0){
										if(("2".equals(offline.getNewtype()))){
											Employee employeeInfo = memberService.findBusinessLoginMemberByUnionId(unionid);
											if(employeeInfo==null){
												if(employee.getUnionid()==null||("null").equals(employee.getUnionid())||("").equals(employee.getUnionid())||StringUtils.isNullOrEmpty(employee.getUnionid())){
													Employee upEm = new Employee();
													upEm.setUnionid(unionid);
													upEm.setOpenid(openid);
													upEm.setWxAvatar(avatar);
													upEm.setWxName(nickname);
													upEm.setId(employee.getId());
													businessService.updateEmployee1(upEm);
												}
											}
										}
										return status(700,"您填写的手机号已经开通了该系列课程会员，直接去登录吧");
									}
									//判断购买迎新大课的小课程包时是否已购买了大课程包
									if(("231").equals(offline.getCatalogId())||("230").equals(offline.getCatalogId())||("229").equals(offline.getCatalogId())||("228").equals(offline.getCatalogId())||("227").equals(offline.getCatalogId())||("226").equals(offline.getCatalogId())||("225").equals(offline.getCatalogId())||("224").equals(offline.getCatalogId())||("223").equals(offline.getCatalogId())||("222").equals(offline.getCatalogId())||("221").equals(offline.getCatalogId())||("220").equals(offline.getCatalogId())||("219").equals(offline.getCatalogId())||("218").equals(offline.getCatalogId())||("217").equals(offline.getCatalogId())||("214").equals(offline.getCatalogId())||("213").equals(offline.getCatalogId())||("212").equals(offline.getCatalogId())||("211").equals(offline.getCatalogId())||("209").equals(offline.getCatalogId())){
										List<MemberRecord> recordList2 = businessService.findMemberRecordListByCatalogId(employee.getId().toString(),"232");
										if(recordList2.size()>0){
											return status(700,"您购买过的系列课中已包含本课程，请到好多课APP中学习");
										}
									}
									//判断是否购买了大课程包课程
									if(courseId==2455){
										//判断所填手机号是否购买了WEB《90招野路子》系列课
										List<WebCourseOrder> webOrderList = businessService.findCourseWebOrderBymobile(mobile);
										if(webOrderList.size()>0){
											price = 1.00;
										}else{
											//判断所填手机号是否购买了App《90招野路子》系列课
											List<WebCourseOrder> appOrderList = businessService.findCourseAppOrderByEmployeeId(employee.getId());
											if(appOrderList.size()>0){
												price = 1.00;
											}else{
												//判断所填手机号是否购买了App《90招野路子》系列课
												List<WebCourseOrder> appOrderList1 = businessService.findCourseAppOrderByEmployeeId1(employee.getId());
												if(appOrderList1.size()>0){
													price = 1.00;
												}
											}
										}
									}
									//如果是迎新大课的话需要计算折扣
									if(("232").equals(offline.getCatalogId())){
										List<MemberRecord> dkList = businessService.findMemberRecordListByEmployeeId1(employee.getId().toString());
										if(dkList.size()>0){
											price = Double.valueOf(new java.text.DecimalFormat("#.00").format(198-(9.8*dkList.size())));
										}
									}
								}
							}else{
								//判断是否购买了大课程包课程
								if(courseId==2455){
									//判断所填手机号是否购买了WEB《90招野路子》系列课
									List<WebCourseOrder> webOrderList = businessService.findCourseWebOrderBymobile(mobile);
									if(webOrderList.size()>0){
										price = 1.00;
									}
								}
							}
							logger.error("验证手机号码："+mobile+"没有注册过企业会员");
						}
//					}
				}else{
					//计算价格
//					for (int i = 0; i < mobile.length; i++) {
						// 查询用户
//						Member member = memberService.findLoginMemberByMobile(mobile);
//						if (member == null) {
//							price = NumberUtils.add(price, offline.getPrice());
//						}else{
//							Integer count = courseService.findMyJoinCourseCount(member.getId());
//							if(count > 0){
								// 计算折扣
								price = NumberUtils.mul(offline.getPrice(), NumberUtils.div(offline.getDiscount(), 100));
//							}else{
//								price = NumberUtils.add(price, offline.getPrice());
//							}
//						}
//					}
				}
			}
			
			
			if(ticketStatus == 1){
				//查询奖学金ID
				Long id = commonService.findHaveTicket(unionid);
				if (id == null) {
					return status(300, "没有奖学金");
				}
				Ticket ticket = commonService.findTicket(id);
				price = NumberUtils.sub(price, ticket.getPrice());
			}
			
			// 生成订单号
			String code = CodeUtils.getCourseOrderCode("WEB", courseId, Long.valueOf(CodeUtils.getRandomInt(100) + 1));

			String nonceStr = CodeUtils.getUUID();// 随机字符串

			SortedMap<String, String> par = new TreeMap<>();
			par.put("appid", appidNew);
			par.put("attach", String.valueOf(number));
			par.put("body", "ChaZuoMBA:" + code);
			par.put("device_info", "WEB");
			par.put("nonce_str", nonceStr);
			par.put("mch_id", mchIdNew);

			StringBuffer sbWX = new StringBuffer();
			sbWX.append("<xml>").append("<appid>").append(appidNew).append("</appid>");

			sbWX.append("<attach><![CDATA[").append(String.valueOf(number)).append("]]></attach>");
			sbWX.append("<body><![CDATA[").append("ChaZuoMBA:" + code).append("]]></body>");

			sbWX.append("<device_info>").append("WEB").append("</device_info>");
			sbWX.append("<mch_id>").append(mchIdNew).append("</mch_id>");
			sbWX.append("<nonce_str>").append(nonceStr).append("</nonce_str>");
			if (configInfo.getProjectTest()) {
				par.put("notify_url", configInfo.getWxTestRestUrl());
				sbWX.append("<notify_url>").append(configInfo.getWxTestRestUrl()).append("</notify_url>");
			} else {
				par.put("notify_url", configInfo.getWxProRestUrl());
				sbWX.append("<notify_url>").append(configInfo.getWxProRestUrl()).append("</notify_url>");
			}
			sbWX.append("<openid>").append(openid).append("</openid>");
			sbWX.append("<out_trade_no>").append(code).append("</out_trade_no>");
			sbWX.append("<spbill_create_ip>").append(ip).append("</spbill_create_ip>");
			int pr = (int) NumberUtils.mul(price, 100);
			sbWX.append("<total_fee>").append(pr).append("</total_fee>");
			if(NumberUtils.isNullOrZero(weixinType)){
				sbWX.append("<trade_type>").append("JSAPI").append("</trade_type>");
			}else if(weixinType == 1){
				sbWX.append("<trade_type>").append("NATIVE").append("</trade_type>");
			}
			
			sbWX.append("<attach>").append(number).append("</attach>");

			par.put("openid", openid);
			par.put("out_trade_no", code);
			par.put("spbill_create_ip", ip);
			par.put("total_fee", pr + "");
			if(NumberUtils.isNullOrZero(weixinType)){
				par.put("trade_type", "JSAPI");
			}else if(weixinType == 1){
				par.put("trade_type", "NATIVE");
			}
			
			String sign = commonService.createSign(par, apiKeyNew);

			sbWX.append("<sign><![CDATA[").append(sign).append("]]></sign>");
			sbWX.append("</xml>");

			HttpClient client = HttpClients.createDefault();
			HttpPost post = new HttpPost("https://api.mch.weixin.qq.com/pay/unifiedorder");
			post.setHeader("Content-type", "text/xml; charset=UTF-8");
			StringEntity entity = new StringEntity(sbWX.toString());
			post.setEntity(entity);
			logger.error("保存预订单时请求微信接口开始时间："+new Date());
			HttpResponse response = client.execute(post);
			HttpEntity entity2 = response.getEntity();
			logger.error("保存预订单时请求微信接口结束时间："+new Date());
			if (entity2 != null) {
				String result2 = EntityUtils.toString(entity2, "UTF-8");

				Map<String, String> result = parseXmlToMap(result2);

				WebCourseOrderInfo info = new WebCourseOrderInfo();
				info.setAppid(appidNew);
				info.setPrepayId(result.get("prepay_id"));
				info.setNonceStr(nonceStr);
				info.setOrderCode(code);
				info.setSign(sign);
				info.setMchId(mchIdNew);
				info.setApiKey(apiKeyNew);
				if(weixinType == 1){
					info.setCodeUrl(result.get("code_url"));
				}
				info.setBeginTime(offline.getBeginTime());
				
				WebCourseOrder order = new WebCourseOrder();
				order.setCourseId(courseId);
				order.setIp(ip);
				order.setNumber(number);
				order.setOpenid(openid);
				order.setOrderCode(code);
				order.setPrice(price);
				order.setStatus(0);
				order.setUnionid(unionid);
				order.setType(0);
				order.setNickname(nickname);
				order.setWeixinSex(weixinSex);
				order.setAvatar(avatar);
				order.setFrom(StringUtils.isNullOrEmpty(from) ? "0" : from);
				order.setTicketStatus(ticketStatus);
				order.setFormNickName(formNickName);
				order.setJoinReason(joinReason);
				order.setJob(job);
				order.setBuyIntentions(buyIntentions);
				order.setEmail(email);
				//发票抬头
				order.setInvoiceTitle(invoiceTitle);
				//发票类型
				order.setInvoiceType(invoiceType);
				//发票收货地址
				order.setInvoiceAddress(invoiceAddress);
				//发票收货人
				order.setInvoiceName(name);
				//发票收货人手机号
				order.setInvoiceMobile(mobile);
				//发票备注
				order.setInvoiceRemarks(invoiceRemarks);
				//是否赠送
				order.setIsGift(isGift);
				order.setAddress(invoiceAddress);
				logger.error("保存预订单时保存预订单记录到----t_course_web_order-----表中开始时间："+new Date());
				orderService.saveWebCourseOrder(order);
				logger.error("保存预订单时保存预订单记录到----t_course_web_order-----表中开始时间："+new Date());

				List<WebCourseOrderUser> ulist = new ArrayList<WebCourseOrderUser>();
//				for (int i = 0; i < name.length; i++) {
					Long id = 0L;
					// 查询用户
					Member member = memberService.findLoginMemberByMobile(mobile);
					if (member != null) {
						id = member.getId();
					}
					if(id != 0){
						if (courseService.findCourseJoinInfo(courseId, id) != null) {
							return status(600, "已报过名..", mobile);
						}	
					}
					//判断是否购买了大课席位
					if(courseId==951 || courseId==1029 || courseId==1030 || courseId==1031 || courseId==1032){
						if (orderService.getIfBuyCourseByMobile(mobile).size()>0) {
							return status(600, "已报过名..", mobile);
						}	
					}
					
					WebCourseOrderUser user = new WebCourseOrderUser();
					if (StringUtils.isNullOrEmpty(content)){
						user.setContent("");
					}else{
						user.setContent(
								StringUtils.isNullOrEmpty(content) || content.equals("") ? "" : content);
					}
					user.setMemberId(member == null ? 0L : member.getId());
					user.setMobile(mobile);
					//获取手机号码归属地
//					user.setCity(memberService.getMobileFrom(mobile[i]));
					String str = null;
					String province="";
					String city="";
					JSONArray jsonArray = null;
					//根据手机号码查询手机号码归属地
//					str = "[" +memberService.request(mobile[i]) + "]";
//					jsonArray = new JSONArray(str);
//					int errNumResult = (int) jsonArray.getJSONObject(0).get("errNum");
//					if(errNumResult == 0){
//						org.json.JSONObject jsonresult = (org.json.JSONObject) jsonArray.getJSONObject(0).get("retData");
//						province = jsonresult.getString("province");
//						city = jsonresult.getString("city");
//					}
					user.setCity(StringUtils.isNullOrEmpty(city) ? "" : city);
					user.setProvince(StringUtils.isNullOrEmpty(province) ? "" : province);
					user.setName(name);
					user.setOrderId(order.getId());
					if (sex.equals("man")) {
						user.setSex("男");
					} else {
						user.setSex("女");
					}
					user.setWeixin(weixin);
					user.setWork(work);

					ulist.add(user);
//				}
				logger.error("保存预订单时保存记录到----t_course_web_order_user-----表中开始时间："+new Date());
				orderService.saveWebCourseOrderUser(ulist);
				logger.error("保存预订单时保存记录到----t_course_web_order_user-----表中开始时间："+new Date());
				
//				WebCourseOrder orderq = orderService.findWebCourseOrder(code);
				//获取订单的Id，用于存储在offline_join表中
//				Long orderId = order.getId();
//				CourseOfflineJoin join = new CourseOfflineJoin();
//				join.setCourseId(order.getCourseId());
//				join.setMemberId(Long.valueOf("0"));
//				join.setOrderId(orderId);
//				join.setType(2); // WEB
//				join.setFrom(2);
//				System.out.println("保存预订单时保存记录到t_course_offline_join表中开始时间："+new Date());
//				courseService.saveCourseJoin(join);
//				System.out.println("保存预订单时保存记录到t_course_offline_join表中结束时间："+new Date());
				return ok("生成成功", info);
			}

			return error("生成失败");
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
		/**
		 * @Title: queryOrderListByUnionid
		 * @Description: (根据unionid查询购买的)
		 * @param courseId
		 *            课程ID
		 * @param unionid
		 *            微信用户ID
		 * @return
		 */
		@ResponseBody
		@RequestMapping("/queryOrderListByUnionid")
		public JSONObject queryOrderListByUnionid(String unionId) {
			try {
				if(StringUtils.isNullOrEmpty(unionId)){
					return error("用户Id参数错误!");
				}
				//判断uinionId是否是年票学员额unionId
				List<SelfCourseOrder> list1 =  orderService.queryOrderListByUnionid(unionId);
				if(list1.size()>0){
					for(int i=0;i<list1.size();i++){
						String teachers = "";
						String id = list1.get(i).getTeacherId();
						String arr[] = id.split(",");
						for(int j=0;j<arr.length;j++){
							Member m = memberService.findMemberById(Long.valueOf(arr[j]));
							if(m != null){
								teachers += m.getNickName();
								teachers += ",";
							}
							if(j == 1){
								teachers = StringUtils.removeEndChar(teachers, ',');
//								if(arr.length > index){
//									teachers += "等";
//								}
								break;
							}
						}
						list1.get(i).setTeachers(StringUtils.removeEndChar(teachers, ','));
						if("0".equals(list1.get(i).getNewtype())){
							list1.get(i).setNewtype("线下课程");
						}else if("1".equals(list1.get(i).getNewtype())){
							list1.get(i).setNewtype("线上课程");
							list1.get(i).setStation("");
						}else{
							list1.get(i).setNewtype("直播课程");
							list1.get(i).setStation("");
							if(!StringUtils.isNullOrEmpty(list1.get(i).getLiveRoomNo())){
								list1.get(i).setLiveStatus(getLivingStatus(list1.get(i).getStream()));
							}
							list1.get(i).setStream("");
						}
					}
					return ok("查询成功",list1);
				}else{
					return status(300,"未购买");
				}
			} catch (Exception e) {
				e.printStackTrace();
				return error(APP_SYSTEM_ERROR);
			}
		}
		/**
		 * @Title: getOrderByOrderNo
		 * @Description: (根据orderNo查询购买课程的订单信息)
		 * @param courseId
		 *            课程ID
		 * @param unionid
		 *            微信用户ID
		 * @return
		 */
		@ResponseBody
		@RequestMapping("/getOrderByOrderNo")
		public JSONObject getOrderByOrderNo(String orderNo) {
			try {
				if(StringUtils.isNullOrEmpty(orderNo)){
					return error("订单编号参数错误!");
				}
				//判断uinionId是否是年票学员额unionId
				List<WebCourseOrder> list1 =  orderService.getOrderByOrderNo(orderNo);
				if(list1.size()>0){
					for(int i=0;i<list1.size();i++){
						String teachers = "";
						String id = list1.get(i).getTeacherId();
						String arr[] = id.split(",");
						System.out.println(arr.length);
						for(int j=0;j<arr.length;j++){
							Member m = memberService.findMemberById(Long.valueOf(arr[j]));
							if(m != null){
								teachers += m.getNickName();
								teachers += ",";
							}
							if(j == 1){
								teachers = StringUtils.removeEndChar(teachers, ',');
//								if(arr.length > index){
//									teachers += "等";
//								}
								break;
							}
						}
						list1.get(i).setTeachers(StringUtils.removeEndChar(teachers, ','));
						if("0".equals(list1.get(i).getNewtype())){
							list1.get(i).setNewtype("线下课程");
						}else if("1".equals(list1.get(i).getNewtype())){
							list1.get(i).setNewtype("线上课程");
							list1.get(i).setStation("");
						}else if("2".equals(list1.get(i).getNewtype())){
							list1.get(i).setNewtype("直播课程");
							list1.get(i).setStation("");
							if(!StringUtils.isNullOrEmpty(list1.get(i).getLiveRoomNo())){
								list1.get(i).setLiveStatus(getLivingStatus(list1.get(i).getStream()));
							}
							list1.get(i).setStream("");
						}else{
							list1.get(i).setNewtype("书籍");
							list1.get(i).setStation("");
						}
					}
					return ok("查询成功",list1);
				}else{
					return status(300,"订单错误");
				}
			} catch (Exception e) {
				e.printStackTrace();
				return error(APP_SYSTEM_ERROR);
			}
		}
		/**
		 * @Title: queryOrderListByUnionid
		 * @Description: (根据unionid查询购买的)
		 * @param courseId
		 *            课程ID
		 * @param unionid
		 *            微信用户ID
		 * @return
		 */
		@ResponseBody
		@RequestMapping("/xmlToMap")
		public JSONObject xmlToMap(String xmlStr) {
			try {
				Map<String, String> m = parseXmlToMap(xmlStr);
				String result_code = m.get("result_code").toString();
				logger.error("后台线下课程报名审核退款解析xml："+xmlStr+"。结果为："+result_code);
				if((("SUCCESS").equals(result_code))){
					return ok();
				}else{
					return status(300,m.get("err_code_des").toString());
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				return error(APP_SYSTEM_ERROR);
			}
		}
		
		/**
		 * @Title: saveRewardOrder
		 * @Description: (保存打赏预订单)
		 * @param sex
		 *            性别
		 * @param content
		 *            内容
		 * @param name
		 *            姓名
		 * @param mobile
		 *            手机号
		 * @param weixin
		 *            微信号
		 * @param courseId
		 *            课程ID
		 * @param unionid
		 *            唯一ID
		 * @param openid
		 *            openId
		 * @param number
		 *            购买数量
		 * @param ip
		 *            ip地址
		 * @param ticketStatus 是否使用奖学金 1:使用 0:不使用
		 * @return
		 */
		@ResponseBody
		@RequestMapping("/saveRewardOrder")
		public JSONObject saveRewardOrder(String unionId,Double price,String ip, String employeeId,Integer weixinType, Long liveCatalogId, Long liveCourseId, String openid,String avatar, String nickName, String payWay) {
			logger.error("打赏接口生成订单参数：unionId:"+unionId+",employeeId:"+employeeId+",liveCatalogId:"+liveCatalogId+",liveCourseId:"+liveCourseId+",openid:"+openid+",avatar:"+avatar+",nickName:"+nickName+",openId:"+openid+",payWay:"+payWay);
			System.out.println("打赏接口生成订单参数：unionId:"+unionId+",employeeId:"+employeeId+",liveCatalogId:"+liveCatalogId+",liveCourseId:"+liveCourseId+",openid:"+openid+",avatar:"+avatar+",nickName:"+nickName+",openId:"+openid+",payWay:"+payWay);
			//weixinType 是否为 1 判断是JSAPI 还是 NATIVE
			try {
				int number = 1;
				if(StringUtils.isNullOrEmpty(payWay)){
					return error("渠道错误");
				}
				// 生成订单号
				String code ="";
				//支付渠道：1、PC官网，2、手机官网，3、App
				if(("3").equals(payWay)){
					if(liveCatalogId==0L||liveCatalogId==null){
						return error("支付课程错误");
					}
					code = CodeUtils.getCourseOrderCode("APP", liveCatalogId, Long.valueOf(CodeUtils.getRandomInt(100) + 1));
				}else{
					if(liveCourseId==0L||liveCourseId==null){
						return error("支付课程错误");
					}
					code = CodeUtils.getCourseOrderCode("WEB", liveCourseId, Long.valueOf(CodeUtils.getRandomInt(100) + 1));
				}
				String nonceStr = CodeUtils.getUUID();// 随机字符串

				SortedMap<String, String> par = new TreeMap<>();
				par.put("appid", appidNew);
				par.put("attach", String.valueOf(number));
				par.put("body", "ChaZuoMBA:" + code);
				par.put("device_info", "WEB");
				par.put("nonce_str", nonceStr);
				par.put("mch_id", mchIdNew);

				StringBuffer sbWX = new StringBuffer();
				sbWX.append("<xml>").append("<appid>").append(appidNew).append("</appid>");

				sbWX.append("<attach><![CDATA[").append(String.valueOf(number)).append("]]></attach>");
				sbWX.append("<body><![CDATA[").append("ChaZuoMBA:" + code).append("]]></body>");

				sbWX.append("<device_info>").append("WEB").append("</device_info>");
				sbWX.append("<mch_id>").append(mchIdNew).append("</mch_id>");
				sbWX.append("<nonce_str>").append(nonceStr).append("</nonce_str>");
				if (configInfo.getProjectTest()) {
					par.put("notify_url", configInfo.getWxTestGiftUrl());
					sbWX.append("<notify_url>").append(configInfo.getWxTestGiftUrl()).append("</notify_url>");
				} else {
					par.put("notify_url", configInfo.getWxProGiftUrl());
					sbWX.append("<notify_url>").append(configInfo.getWxProGiftUrl()).append("</notify_url>");
				}
				sbWX.append("<openid>").append(openid).append("</openid>");
				sbWX.append("<out_trade_no>").append(code).append("</out_trade_no>");
				sbWX.append("<spbill_create_ip>").append(ip).append("</spbill_create_ip>");
				int pr = (int) NumberUtils.mul(price, 100);
				sbWX.append("<total_fee>").append(pr).append("</total_fee>");
				if(NumberUtils.isNullOrZero(weixinType)){
					sbWX.append("<trade_type>").append("JSAPI").append("</trade_type>");
				}else if(weixinType == 1){
					sbWX.append("<trade_type>").append("NATIVE").append("</trade_type>");
				}
				
				sbWX.append("<attach>").append(number).append("</attach>");

				par.put("openid", openid);
				par.put("out_trade_no", code);
				par.put("spbill_create_ip", ip);
				par.put("total_fee", pr + "");
				if(NumberUtils.isNullOrZero(weixinType)){
					par.put("trade_type", "JSAPI");
				}else if(weixinType == 1){
					par.put("trade_type", "NATIVE");
				}
				
				String sign = commonService.createSign(par, apiKeyNew);

				sbWX.append("<sign><![CDATA[").append(sign).append("]]></sign>");
				sbWX.append("</xml>");

				HttpClient client = HttpClients.createDefault();
				HttpPost post = new HttpPost("https://api.mch.weixin.qq.com/pay/unifiedorder");
				post.setHeader("Content-type", "text/xml; charset=UTF-8");
				StringEntity entity = new StringEntity(sbWX.toString());
				post.setEntity(entity);
				System.out.println("保存预订单时请求微信接口开始时间："+new Date());
				HttpResponse response = client.execute(post);
				HttpEntity entity2 = response.getEntity();
				System.out.println("保存预订单时请求微信接口结束时间："+new Date());
				if (entity2 != null) {
					String result2 = EntityUtils.toString(entity2, "UTF-8");

					Map<String, String> result = parseXmlToMap(result2);

					WebCourseOrderInfo info = new WebCourseOrderInfo();
					info.setAppid(appidNew);
					info.setPrepayId(result.get("prepay_id"));
					info.setNonceStr(nonceStr);
					info.setOrderCode(code);
					info.setSign(sign);
					info.setMchId(mchIdNew);
					info.setApiKey(apiKeyNew);
					if(weixinType == 1){
						info.setCodeUrl(result.get("code_url"));
					}
					LiveGiftOrder order = new LiveGiftOrder();
					order.setCourseId(liveCourseId);
					order.setIp(ip);
					order.setOpenid(openid);
					order.setOrderSn(code);
					order.setPrice(price);
					order.setOrderStatus(0);
					order.setUnionid(unionId);
					order.setNickName(nickName);
					order.setAvatar(avatar);
					order.setPayWay(payWay);
					order.setEmployeeId(employeeId);
					order.setLiveCatalogId(liveCatalogId);
					order.setLiveCourseId(liveCourseId);
					System.out.println("保存预订单时保存预订单记录到----t_haoduoke_live_gift_order-----表中开始时间："+new Date());
					orderService.saveLiveGiftOrder(order);
					System.out.println("保存预订单时保存预订单记录到----t_haoduoke_live_gift_order-----表中结束时间："+new Date());
					return ok("生成成功", info);
				}
				return error("生成失败");
			} catch (Exception e) {
				e.printStackTrace();
				return error(APP_SYSTEM_ERROR);
			}
		}
		/**
		 * @Title: weixinGiftPayNotify
		 * @Description: (Web微信支付回调)
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping("/weixinGiftPayNotify")
		public String weixinGiftPayNotify(HttpServletRequest request, HttpServletResponse response) {
			String resXml = "";
			try {
				InputStream inStream = request.getInputStream();
				ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
				Long ifOldEmployee = 0L;
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = inStream.read(buffer)) != -1) {
					outSteam.write(buffer, 0, len);
				}
				outSteam.close();
				inStream.close();

				String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息

				Map<String, String> m = parseXmlToMap(result);

				if ("SUCCESS".equals(m.get("result_code").toString())) {
					String tradeNo = m.get("out_trade_no").toString();
					// 支付成功
					LiveGiftOrder order = orderService.findLiveGiftOrder(tradeNo);
					Long orderId = order.getId();
					logger.error("微信支付成功，订单id："+orderId);
					if (order == null || order.getOrderStatus() == 1) {
						// 支付成功
						resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
								+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
						BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
						out.write(resXml.getBytes());
						out.flush();
						out.close();
						return null;
					}else{
						// 修改订单信息
						orderService.updateLiveGiftOrderStatus(order.getId());
					}
					// 支付成功
					resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
							+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
				} else {
					resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
							+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
				}

				logger.error("微信支付回调数据结束");

				BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
				out.write(resXml.getBytes());
				out.flush();
				out.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		/**
		 * @Title: getLiveGiftOrderDetail 
		 * @Description: (根据订单号查询打赏订单支付信息) 
		 * @param unionId 微信Id
		 * @param content 评论内容
		 * @return
		 */
		@ResponseBody
		@RequestMapping("/getLiveGiftOrderDetail")
		public JSONObject getLiveGiftOrderDetail(String orderNo){
			try {
				LiveGiftOrder order = orderService.findLiveGiftOrder(orderNo);
				if(order!=null){
					return ok("查询成功",order.getOrderStatus().toString());
				}else{
					return error("无效的订单");
				}
			    
			}catch(Exception e){
				e.printStackTrace();
			}
			return error("获取课程信息失败");
		}
		
		/**
		 * @Title: queryOrderListByMobile
		 * @Description: (根据mobile查询购买的线上课程订单)
		 * @param courseId
		 *            课程ID
		 * @param mobile
		 *            手机号
		 * @return
		 */
		@ResponseBody
		@RequestMapping("/queryOrderListByMobile")
		public JSONObject queryOrderListByMobile(String mobile) {
			try {
				if(StringUtils.isNullOrEmpty(mobile)){
					return error("手机号参数错误!");
				}
//				if(courseId==0L){
//					return error("课程编号参数错误!");
//				}
//				Course course = courseService.findCourseById(courseId);
//				OfflineCourse offline = courseService.findOfflineCourseByCourseId(courseId);
//				if (offline == null || course == null) {
//					return error("参数错误");
//				}
				//判断手机号查询订单列表
				List<SelfCourseOrder> list1 =  orderService.queryOrderListByMobile(mobile);
				Double sumPrice = 0.0;
				if(list1.size()>0){
					for(int i=0;i<list1.size();i++){
						java.math.BigDecimal d1=new java.math.BigDecimal(String.valueOf(sumPrice));
						java.math.BigDecimal d2=new java.math.BigDecimal(String.valueOf(list1.get(i).getPrice()));
						sumPrice = d1.add(d2).doubleValue(); 
					}
					return ok1("查询成功",list1,String.valueOf(sumPrice));
				}else{
					return status(300,"未购买");
				}
			} catch (Exception e) {
				e.printStackTrace();
				return error(APP_SYSTEM_ERROR);
			}
		}
		/**
		 * @Title: freeAccess
		 * @Description: (线上课程购买超过598元免费获取先下课名额)
		 * @param courseId
		 *            课程ID
		 * @param mobile
		 *            手机号
		 * @return
		 */
		@ResponseBody
		@RequestMapping("/freeAccess ")
		public JSONObject freeAccess(String mobile,String name,String email,Long courseId,String unionid,String openid,String avatar,String nickName,String ip,String from,String content,String company,String work) {
			try {
				if(StringUtils.isNullOrEmpty(mobile)){
					return error("手机号参数错误!");
				}
				if(courseId==0L){
					return error("课程编号参数错误!");
				}
				Long id=0L;
				// 查询用户
				Member member = memberService.findLoginMemberByMobile(mobile);
				if (member != null) {
					id = member.getId();
				}
				if(id != 0){
					//根据课程Id和用户Id查询用户是否购买了课程
					List<SelfCourseOrder> list1 =  orderService.queryOrderListByUnionidAndCourseId(unionid,courseId);
					if(list1.size()>0){
						return status(600, list1.get(0).getOrderCode(), "已报过名");
					}
//					if (courseService.findCourseJoinInfo(courseId, id) != null) {
//						return status(600, "已报过名..", mobile);
//					}	
				}
				List<SelfCourseOrder> list1 =  orderService.queryOrderListByMobile(mobile);
				Double sumPrice = 0.0;
				if(list1.size()>0){
					for(int i=0;i<list1.size();i++){
						java.math.BigDecimal d1=new java.math.BigDecimal(String.valueOf(sumPrice));
						java.math.BigDecimal d2=new java.math.BigDecimal(String.valueOf(list1.get(i).getPrice()));
						sumPrice = d1.add(d2).doubleValue(); 
					}
				}
				if(sumPrice<598){
					return status(606,"消费金额不足以报名本课程");
				}
				Course course = courseService.findCourseById(courseId);
				OfflineCourse offline = courseService.findOfflineCourseByCourseId(courseId);
				if (offline == null || course == null) {
					return error("参数错误");
				}
				WebCourseOrder order = new WebCourseOrder();
				order.setCourseId(courseId);
				order.setIp(ip);
				order.setNumber(1);
				order.setOpenid(openid);
				order.setOrderCode(CodeUtils.getCourseOrderCode("免费", courseId, Long.valueOf(CodeUtils.getRandomInt(100) + 1)));
				order.setPrice(0.0);
				order.setStatus(1);
				order.setUnionid(unionid);
				order.setType(0);
				order.setNickname(nickName);
				order.setWeixinSex("男");
				order.setAvatar(avatar);
				order.setFrom(StringUtils.isNullOrEmpty(from) ? "0" : from);
				order.setTicketStatus(0);
				order.setFormNickName("");
				order.setJoinReason("");
				order.setJob("");
				order.setBuyIntentions("");
				order.setEmail(email);
				//发票抬头
				order.setInvoiceTitle("");
				//发票类型
				order.setInvoiceType("");
				//发票收货地址
				order.setInvoiceAddress("");
				//发票收货人
				order.setInvoiceName(name);
				//发票收货人手机号
				order.setInvoiceMobile(mobile);
				//发票备注
				order.setInvoiceRemarks("");
				//是否赠送
				order.setIsGift("0");
				order.setAddress("");
				System.out.println("保存订单记录到----t_course_web_order-----表中开始时间："+new Date());
				orderService.saveWebCourseOrder(order);
				System.out.println("保存订单记录到----t_course_web_order-----表中开始时间："+new Date());
				List<WebCourseOrderUser> ulist = new ArrayList<WebCourseOrderUser>();
				WebCourseOrderUser user = new WebCourseOrderUser();
				if (StringUtils.isNullOrEmpty(content)){
					user.setContent("");
				}else{
					user.setContent(
					StringUtils.isNullOrEmpty(content) || content.equals("") ? "" : content);
				}
				user.setMemberId(member == null ? 0L : member.getId());
				user.setMobile(mobile);
				String province="";
				String city="";
				user.setCity(StringUtils.isNullOrEmpty(city) ? "" : city);
				user.setProvince(StringUtils.isNullOrEmpty(province) ? "" : province);
				user.setName(name);
				user.setOrderId(order.getId());
				user.setSex("男");
				user.setWeixin("");
				user.setWork(company);
				ulist.add(user);
				System.out.println("保存订单记录到----t_course_web_order_user-----表中开始时间："+new Date());
				orderService.saveWebCourseOrderUser(ulist);
				System.out.println("保存订单记录到----t_course_web_order_user-----表中开始时间："+new Date());
				Member me = memberService.findLoginMemberByMobile(mobile);
				Long memberId = (me == null ? null : me.getId());
				logger.error("memberId的值为："+memberId);
				if (NumberUtils.isNullOrZero(memberId)) {
					logger.error("memberId的值判断为NullOrZero");
					Member member2 = new Member();
					member2.setMobile(mobile);
					member2.setPassword(PasswdEncryption.generate("123456"));
					member2.setNickName(name);
					member2.setAccessToken(CodeUtils.getUUID());
					member2.setClientVersion("1.0");
					member2.setDeviceId("null");
					// 保存默认头像
					member2.setAvatar(commonService.getRandomAvatar());
					logger.error("新增用户参数：mobile："+mobile+";userName:"+name);
					memberService.register(member2, null, "");
					memberId = member2.getId();
					logger.error("memberId的值:"+memberId);
					// 更新ID
					orderService.updateWebCourseOrderUserId(memberId, user.getId());
				}
				CourseOfflineJoin join = courseService.findCourseJoinInfo(order.getCourseId(), memberId);
				if(join == null){
					// 同时保存报名信息
					join = new CourseOfflineJoin();
					join.setCourseId(courseId);
					join.setMemberId(memberId);
					join.setOrderId(order.getId());
					join.setType(2); // WEB
					join.setFrom(2);
					courseService.saveCourseJoin(join);
				}
				return ok(order.getOrderCode());
			} catch (Exception e) {
				e.printStackTrace();
				return error(APP_SYSTEM_ERROR);
			}
		}
		/**
		 * @Title: weixinGift
		 * @Description: (微信公众号关注领取课程礼包)
		 * @param courseId
		 *            课程ID
		 * @param mobile
		 *            手机号
		 * @return
		 */
		@ResponseBody
		@RequestMapping("/weixinGift ")
		public JSONObject weixinGift(String mobile,String nickName,String openid,String unionid,String avatar) {
			try {
				if(StringUtils.isNullOrEmpty(mobile)){
					return error("手机号参数错误!");
				}
				if(StringUtils.isNullOrEmpty(unionid)){
					return error("微信用户信息获取异常");
				}
				//先验证此手机号是否已经领取过了该礼包
				WeixinGift gift = memberService.findWeixinGiftByMobile(mobile);
				if(gift!=null){
					return status(301,"此手机号已经领取过该礼包了");
				}
				//先验证此微信用户是否已经领取过了该礼包
				WeixinGift gift1 = memberService.findWeixinGiftByUnionid(unionid);
				if(gift1!=null){
					return status(302,"一个微信号只能领取一次礼包");
				}
				Long ifOldEmployee = 0L;
				// 验证用户是否存在
				Employee employee = memberService.findBusinessLoginMemberByMobile1(mobile);
				//根据unionId查询好多课App用户信息
				Employee employeeInfo = memberService.findBusinessLoginMemberByUnionId(unionid);
				if (employee != null){
					MemberRecord record = new MemberRecord();
					record.setEmployeeId(employee.getId().toString());
					record.setCatalogId("9");
					record.setExpiryDate("30");
					//好多课年费会员type传0 系列课会员信息传1
					record.setType("2");
					record.setCourseId("0");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Calendar calendar = Calendar.getInstance();
					//取订单创建时间
					calendar.setTime(new Date());
					//订单创建时间加上会员日期为会员过期日期
					calendar.add(Calendar.DAY_OF_MONTH, 90);
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//				System.out.println(sdf.format(calendar.getTime()));
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					//会员过期时间
					Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
					record.setEndDate(dateTime2);
					ifOldEmployee=1L;
					memberService.insertMemberRecord3(record);
					//将数据写入选课表
					memberService.insertMemberSeries1(record);
					businessService.refreshRedis(employee.getId());
					WeixinGift weixinGift = new WeixinGift();
					weixinGift.setAvatar(avatar);
					weixinGift.setMobile(mobile);
					weixinGift.setNickname(nickName);
					weixinGift.setUnionid(unionid);
					weixinGift.setOpenid(openid);
					memberService.insertWeixinGift(weixinGift);
					String msg="【插坐学院】厉害了，我的#name#，《新同学超级礼包》已被你成功领取，现在打开“好多课”APP，直接登录即可学习。其他不懂的微信找我哦，ID：chazuomba06";
					commonService.sendCLMsg(mobile,msg.replace("#name#", employee.getName()));
				}else{
					Employee newE = new Employee();
					//企业Id
					newE.setBusinessId("0");
					//企业名称
					newE.setBusinessName("");
					//用户手机号
					newE.setMobile(mobile);
					//用户名
					newE.setName(nickName);
					//职位
					newE.setPosition("");
					//数据状态
					newE.setStatus(1);
					//会员日期180天
					newE.setExpiryDate(360L);
					//会员类型：企业会员
					newE.setIfBusiness(58L);
					//创建时间
					newE.setCreateAt(new Date());
					//密码
					newE.setPassword(PasswdEncryption.generate(mobile));
					// 保存默认头像
					newE.setAvatar(commonService.getRandomAvatar());
					newE.setIfBusiness(58L);
					//保存数据到数据库
					memberService.registerEmployeeXL(newE,null);
					//在t_business_member_record 表中写入会员购买记录
					MemberRecord record = new MemberRecord();
					record.setEmployeeId(newE.getId().toString());
					record.setCatalogId("9");
					record.setExpiryDate("30");
					//好多课年费会员type传0 系列课会员信息传1 公开课
					record.setType("2");
					record.setCourseId("0");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Calendar calendar = Calendar.getInstance();
					//取订单创建时间
					calendar.setTime(new Date());
					//订单创建时间加上会员日期为会员过期日期
					calendar.add(Calendar.DAY_OF_MONTH,90);
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//				System.out.println(sdf.format(calendar.getTime()));
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				//	//会员过期时间
					Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
					record.setEndDate(dateTime2);
					memberService.insertMemberRecord3(record);
					//将数据写入选课表
					memberService.insertMemberSeries1(record);
					businessService.refreshRedis(newE.getId());
					WeixinGift weixinGift = new WeixinGift();
					weixinGift.setAvatar(avatar);
					weixinGift.setMobile(mobile);
					weixinGift.setNickname(nickName);
					weixinGift.setUnionid(unionid);
					weixinGift.setOpenid(openid);
					memberService.insertWeixinGift(weixinGift);
					String msg="【插坐学院】厉害了，我的#name#，《新同学超级礼包》已被你成功领取，现在去应用商店下载“好多课”APP吧，账号密码均为手机号：#mobile#，其他不懂的微信来找我，ID：chazuomba06";
					commonService.sendCLMsg(mobile,msg.replace("#mobile#", mobile).replace("#name#", nickName));
//					}
				}
				return ok("领取成功");
			} catch (Exception e) {
				e.printStackTrace();
				return error(APP_SYSTEM_ERROR);
			}
		}
		/**
		 * @Title: weixinGift
		 * @Description: (微信公众号关注领取课程礼包)
		 * @param courseId
		 *            课程ID
		 * @param mobile
		 *            手机号
		 * @return
		 */
		@ResponseBody
		@RequestMapping("/checkMobile ")
		public JSONObject checkMobile(String mobile) {
			try {
				String price = "0";
				Employee employee = memberService.findBusinessLoginMemberByMobile1(mobile);
				if (employee != null) {
					logger.error("验证手机号码："+mobile+"已注册过企业会员");
//					if(employee.getIfBusiness()==0){
					//查询是否该用户为年费会员用户
					List<MemberRecord> recordList = businessService.findMemberRecordList(employee.getId().toString());
					if(recordList.size()>0){
						return status(700,"您已经是好多课年票会员，直接登录App学习吧~");
					}else{
						//判断所填手机号是否购买了WEB《90招野路子》系列课
						List<WebCourseOrder> webOrderList = businessService.findCourseWebOrderBymobile(mobile);
						if(webOrderList.size()>0){
							price = "1";
						}else{
							//判断所填手机号是否购买了App《90招野路子》系列课
							List<WebCourseOrder> appOrderList = businessService.findCourseAppOrderByEmployeeId(employee.getId());
							if(appOrderList.size()>0){
								price = "1";
							}else{
								//判断所填手机号是否购买了App《90招野路子》系列课
								List<WebCourseOrder> appOrderList1 = businessService.findCourseAppOrderByEmployeeId1(employee.getId());
								if(appOrderList1.size()>0){
									price = "1";
								}
							}
						}
					}
				}else{
					//判断所填手机号是否购买了WEB《90招野路子》系列课
					List<WebCourseOrder> webOrderList = businessService.findCourseWebOrderBymobile(mobile);
					if(webOrderList.size()>0){
						price = "1";
					}else{
						price = "199";
					}
				}
				return status(200,price);
			} catch (Exception e) {
				e.printStackTrace();
				return error(APP_SYSTEM_ERROR);
			}
		}
}
