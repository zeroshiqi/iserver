package cn.ichazuo.controller.app;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
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
import cn.ichazuo.commons.util.StringUtils;
import cn.ichazuo.commons.util.alipay.util.AlipayNotify;
import cn.ichazuo.model.app.OnlineCourseResultInfo;
import cn.ichazuo.model.app.WebCourseOrderInfo;
import cn.ichazuo.model.table.Course;
import cn.ichazuo.model.table.OnlineCourse;
import cn.ichazuo.model.table.OnlineCourseWebOrder;
import cn.ichazuo.model.table.ThirdMember;
import cn.ichazuo.service.CommonService;
import cn.ichazuo.service.CourseService;
import cn.ichazuo.service.MemberService;
import cn.ichazuo.service.OrderService;

/**
 * @ClassName: OnlineCourseOrderController
 * @Description: (线上课程报名)
 * @author ZhaoXu
 * @date 2015年11月2日 下午1:44:41
 * @version V1.0
 */
@RequestMapping("/app")
@Controller("app.onlineCourseOrderController")
public class OnlineCourseOrderController extends BaseController {
	@Autowired
	private CourseService courseService;
	@Autowired
	private ConfigInfo configInfo;
	@Autowired
	private CommonService commonService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private MemberService memberService;

	/**
	 * @Title: findOnlineBuy
	 * @Description: (查询线上课程是否购买过)
	 * @param unionid
	 *            唯一ID
	 * @param courseId
	 *            课程ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findOnlineBuy")
	public JSONObject findOnlineBuy(String unionid, Long courseId) {
		try {
			if (StringUtils.isNullOrEmpty(unionid) || NumberUtils.isNullOrZero(courseId)) {
				return error("参数错误");
			}
			
			int count = orderService.findOnlineCourseBugCount(courseId, unionid);

			// 线上课程信息
//			OnlineCourseResultInfo info = courseService.findOnlineWebCourseByCourseId(courseId);
			OnlineCourseResultInfo info = courseService.findOnlineWebCourseByCourseSell(courseId);
			if (info == null) {
				return error("参数错误");
			}
			if (count == 0) {
				Long count144 = courseService.findBuyCourseCount(unionid, 144L);
				if(count144 > 0){
					info.setStatus("true");
				}else{
					Course course = courseService.findCourseIsSell(courseId);
					if(course.getIsSell() == 1){
						info.setStatus("true");
					}
					info.setStatus("false");
				}
			} else {
				info.setStatus("true");
			}
			info.setCourseId(courseId);

			return ok("查询成功", info);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	

	/**
	 * @Title: saveOnlineWebOrder
	 * @Description: (保存线上课程订单信息)
	 * @param courseId
	 *            课程ID
	 * @param unionid
	 *            唯一ID
	 * @param openid
	 *            openid
	 * @param ip
	 *            ip地址
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveOnlineWebOrder")
	public JSONObject saveOnlineWebOrder(Long courseId, String unionid, String openid, String ip,String weixin) {
		try {
			if (NumberUtils.isNullOrZero(courseId) || StringUtils.isNullOrEmpty(openid)
					|| StringUtils.isNullOrEmpty(unionid)) {
				return error("参数错误!");
			}
			OnlineCourse course = courseService.findOnlineCourseByCourseId(courseId);
			Course course1 = courseService.findCourseIsSell(courseId);
			if (course == null) {
				return error("参数错误");
			}
			if (course1 == null) {
				return error("参数错误");
			}
			if(course1.getIsSell() == 1){
				return status(400, "停止售卖！");
			}
			
			Double price = course.getPrice(); // 计算价格

			// 生成订单号
			String code = CodeUtils.getCourseOrderCode("WEB", courseId, Long.valueOf(CodeUtils.getRandomInt(100) + 1));

			String nonceStr = CodeUtils.getUUID();// 随机字符串

			SortedMap<String, String> par = new TreeMap<>();
			par.put("appid", OrderController.appid);
			par.put("attach", String.valueOf(1));
			par.put("body", "ChaZuoMBA:" + code);
			par.put("device_info", "WEB");
			par.put("nonce_str", nonceStr);
			par.put("mch_id", OrderController.mchId);

			StringBuffer sbWX = new StringBuffer();
			sbWX.append("<xml>").append("<appid>").append(OrderController.appid).append("</appid>");

			sbWX.append("<attach><![CDATA[").append(String.valueOf(1)).append("]]></attach>");
			sbWX.append("<body><![CDATA[").append("ChaZuoMBA:" + code).append("]]></body>");

			sbWX.append("<device_info>").append("WEB").append("</device_info>");
			sbWX.append("<mch_id>").append(OrderController.mchId).append("</mch_id>");
			sbWX.append("<nonce_str>").append(nonceStr).append("</nonce_str>");
			if (configInfo.getProjectTest()) {
				par.put("notify_url", configInfo.getWeixinTestOnlineOrderUrl());
				sbWX.append("<notify_url>").append(configInfo.getWeixinTestOnlineOrderUrl()).append("</notify_url>");
			} else {
				par.put("notify_url", configInfo.getWeixinProOnlineOrderUrl());
				sbWX.append("<notify_url>").append(configInfo.getWeixinProOnlineOrderUrl()).append("</notify_url>");
			}
			sbWX.append("<openid>").append(openid).append("</openid>");
			sbWX.append("<out_trade_no>").append(code).append("</out_trade_no>");
			sbWX.append("<spbill_create_ip>").append(ip).append("</spbill_create_ip>");
			int pr = (int) NumberUtils.mul(price, 100);
			sbWX.append("<total_fee>").append(pr).append("</total_fee>");
			sbWX.append("<trade_type>").append("JSAPI").append("</trade_type>");
			sbWX.append("<attach>").append(1).append("</attach>");

			par.put("openid", openid);
			par.put("out_trade_no", code);
			par.put("spbill_create_ip", ip);
			par.put("total_fee", pr + "");
			par.put("trade_type", "JSAPI");
			String sign = commonService.createSign(par, OrderController.apiKey);

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

				WebCourseOrderInfo info = new WebCourseOrderInfo();
				info.setAppid(appid);
				info.setPrepayId(result.get("prepay_id"));
				info.setNonceStr(nonceStr);
				info.setOrderCode(code);
				info.setSign(sign);
				info.setApiKey(apiKey);

				OnlineCourseWebOrder order = new OnlineCourseWebOrder();
				order.setCode(code);
				order.setCourseId(courseId);
				order.setCreateAt(DateUtils.getNowDate());
				order.setPrice(price);
				order.setUnionId(unionid);
				order.setAt(0);
				order.setWeixin(StringUtils.isNullOrEmpty(weixin) ? "" : weixin);
				orderService.saveOnlineWebOrder(order);

				return ok("生成成功", info);
			}
			return error("生成失败");
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}

	/**
	 * @Title: weixinOnlineWebPayNotify
	 * @Description: (Web微信支付回调)
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/weixinOnlineWebPayNotify")
	public String weixinWebPayNotify(HttpServletRequest request, HttpServletResponse response) {
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
				OnlineCourseWebOrder order = orderService.findOnlineWebOrderInfoByCode(tradeNo);
				if (order == null || order.getStatus() == 1) {
					// 支付成功
					resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
							+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";

					return resXml;
				} else {
					// 修改订单状态
					orderService.updateOnlineOrderStatus(order.getId());
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
		return resXml;
	}
	

	/**
	 * @Title: aliPayOnlineCourseNotify 
	 * @Description: (支付宝回调) 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/aliPayOnlineCourseNotify")
	public String aliPayOnlineCourseNotify(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 获取支付宝POST过来反馈信息
			Map<String, String> params = new HashMap<String, String>();
			Map<String, String[]> requestParams = request.getParameterMap();
			for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				// valueStr = new String(valueStr.getBytes("ISO-8859-1"),
				// "gbk");
				params.put(name, valueStr);
			}

			// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			// 商户订单号
			String outTradeNo = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

			// 交易状态
			String tradeStatus = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");

			// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
			if (AlipayNotify.verify(params, configInfo.getAliPayPartner())) {// 验证成功
				//////////////////////////////////////////////////////////////////////////////////////////
				// ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
				if (tradeStatus.equals("TRADE_SUCCESS")) {
					// 支付成功
					OnlineCourseWebOrder order = orderService.findOnlineWebOrderInfoByCode(outTradeNo);
					if (order == null || order.getStatus() == 1) {
						return "success";
					} else {
						// 修改订单状态
						orderService.updateOnlineOrderStatus(order.getId());
					}
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
	 * @Title: saveOnlineAppOrder 
	 * @Description: (保存APP用户订单) 
	 * @param courseId 课程ID
	 * @param weixin 微信号
	 * @param userId 用户ID
	 * @param price 订单价格
	 * @return status: 800(unionId 不存在,重新使用微信登录获取)   900(未绑定微信) 200(生成成功,data数据为订单号如:WEB2015110216302012663567)
	 * 500(异常)
	 */
	@ResponseBody
	@RequestMapping("/saveOnlineAppOrder")
	public JSONObject saveOnlineAppOrder(Long courseId, String weixin,Long userId,Double price){
		try{
			if(NumberUtils.isNullOrZero(userId) || NumberUtils.isNullOrZero(courseId)){
				return error("参数错误");
			}
			
			OnlineCourse course = courseService.findOnlineCourseByCourseId(courseId);
			if (course == null) {
				return error("参数错误");
			}

			ThirdMember third = memberService.findThirdMemberByMemberId(userId);
			if(third == null){
				return status(900, "未绑定微信");
			}
			if(StringUtils.isNullOrEmpty(third.getUnionid())){
				return status(800, "未使用微信登录");
			}

			// 生成订单号
			String code = CodeUtils.getCourseOrderCode("WEB", courseId, Long.valueOf(CodeUtils.getRandomInt(100) + 1));
			OnlineCourseWebOrder order = new OnlineCourseWebOrder();
			order.setCode(code);
			order.setCourseId(courseId);
			order.setCreateAt(DateUtils.getNowDate());
			order.setPrice(price);
			order.setUnionId(third.getUnionid());
			order.setAt(1);
			order.setWeixin(StringUtils.isNullOrEmpty(weixin) ? "" : weixin);
			orderService.saveOnlineWebOrder(order);

			return ok("保存成功",code);
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
}
