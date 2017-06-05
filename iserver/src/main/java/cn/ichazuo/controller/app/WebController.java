package cn.ichazuo.controller.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.ichazuo.commons.base.BaseController;
import cn.ichazuo.commons.component.CacheInfo;
import cn.ichazuo.commons.component.RedisClient;
import cn.ichazuo.commons.util.CodeUtils;
import cn.ichazuo.commons.util.DateUtils;
import cn.ichazuo.commons.util.NumberUtils;
import cn.ichazuo.commons.util.SHA1;
import cn.ichazuo.commons.util.StringUtils;
import cn.ichazuo.commons.util.model.Page;
import cn.ichazuo.commons.util.model.Params;
import cn.ichazuo.model.app.OfflineCourseInfo;
import cn.ichazuo.model.app.OnlineCourseCommentInfo;
import cn.ichazuo.model.app.WebOfflineCourseInfo;
import cn.ichazuo.model.log.CourseClickLog;
import cn.ichazuo.model.table.Code;
import cn.ichazuo.model.table.CourseOfflineJoin;
import cn.ichazuo.model.table.CourseWebCrowdfunding;
import cn.ichazuo.model.table.CourseWebInfo;
import cn.ichazuo.model.table.Member;
import cn.ichazuo.model.table.WebCourseOrder;
import cn.ichazuo.service.CodeService;
import cn.ichazuo.service.CommonService;
import cn.ichazuo.service.CourseService;
import cn.ichazuo.service.CrowdfundingService;
import cn.ichazuo.service.LogService;
import cn.ichazuo.service.MemberService;
import cn.ichazuo.service.OrderService;

/**
 * @ClassName: WebController
 * @Description: (webController)
 * @author ZhaoXu
 * @date 2015年8月19日 上午10:33:03
 * @version V1.0
 */
@RequestMapping("/app")
@Controller("app.webController")
public class WebController extends BaseController {
	@Autowired
	private CommonService commonService;
	@Autowired
	private LogService logService;
	@Autowired
	private RedisClient redisClient;
	@Autowired
	private CacheInfo cacheInfo;
	@Autowired
	private CourseService courseService;
	@Autowired
	private CrowdfundingService crowdfundingService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private MemberService memberService;
	
	@ResponseBody
	@RequestMapping("/getWebJobList")
	public JSONObject getWebJobList(Page page){
		try{

			return ok("查询成功",commonService.getWebJobList(page));
			
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	@ResponseBody
	@RequestMapping("/getWebJobInfo")
	public JSONObject getWebJobInfo(Long id){
		try{
			if(NumberUtils.isNullOrZero(id)){
				return error("参数错误");
			}
			return ok("查询成功", commonService.getWebJobInfo(id));
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	

	/**
	 * @Title: findWebOfflineCourseInfo
	 * @Description: (Web查询线下课程信息)
	 * @param courseId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findWebOfflineCourseInfo")
	public JSONObject findWebOfflineCourseInfo(HttpServletRequest request, Long courseId) {
		try {
			if (NumberUtils.isNullOrZero(courseId)) {
				return error("参数错误");
			}
			OfflineCourseInfo info = courseService.findOfflineCourseAllInfo(courseId);
			if (info == null) {
				return error("参数错误");
			}
			WebOfflineCourseInfo offline = commonService.findWebOfflineCourseInfo(courseId);
			// 已报名数量
			info.setJoinNum(courseService.findCourseJoinCount(courseId));
			
			LocalDate now = LocalDate.now();
			LocalDate begin = LocalDate.parse(DateUtils.formatDate(info.getBeginTime(), DateUtils.DATE_FORMAT_NORMAL));
			if (now.isAfter(begin) || info.getJoinNum() >= info.getStudentNum()) {
				// 报名时间截止,报名人数已经足够 停止报名
				offline.setIsOver(1);
			} else {
				offline.setIsOver(0);
			}
			
			if(info.getId() == 129){
				offline.setIsOver(1);
			}
			offline.setCover(commonService.appenUrl(offline.getCover()));
			offline.setAvatar(commonService.appenUrl(offline.getAvatar()));
			offline.setJoin(courseService.findCourseJoinCount(courseId));
			offline.setNewType(info.getNewType());
			// LocalDate calc =
			// LocalDate.parse(DateUtils.formatDate(info.getBeginTime(),DateUtils.DATE_FORMAT_NORMAL));
			// LocalDate nowDate = LocalDate.now();

			Calendar calendar = Calendar.getInstance();
			Calendar nowDate = Calendar.getInstance();
			calendar.setTime(info.getBeginTime());
			calendar.set(Calendar.HOUR, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);

			nowDate.setTime(new Date());
			nowDate.add(Calendar.DAY_OF_YEAR, 6);

			if (nowDate.after(calendar)) {
				offline.setIsHidden(1);
			} else {
				offline.setIsHidden(0);
			}

			CourseClickLog log = new CourseClickLog();
			log.setCourseId(courseId);
			log.setCreateAt(new Date());
			log.setIpAddress(request.getRemoteAddr());
			log.setMemberId(-1L);
			log.setType(2);
			logService.saveCourseClickLog(log);
			return ok("查询成功", offline);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: updateWebWeixinInfo 
	 * @Description: (更新微信信息) 
	 * @param avatar 头像
	 * @param nickName 名称
	 * @param sex 性别
	 * @param type 类别 1/2
	 * @param courseId 课程ID
	 * @param unionId 唯一ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateWebWeixinInfo")
	public JSONObject updateWebWeixinInfo(String avatar,String nickName,String sex,Integer type,Long courseId,String unionId){
		try{
			if(!StringUtils.isNotNull(avatar,nickName,sex,unionId) || type == null || NumberUtils.isNullOrZero(courseId)){
				return error("参数错误");
			}
			Params params = new Params();
			params.putData("avatar", avatar);
			params.putData("nickName", nickName);
			params.putData("sex", sex);
			
			if (type == 1) {
				WebCourseOrder order = orderService.findWebOrderByCourseIdUnionId(courseId, unionId);
				if(order != null && StringUtils.isNullOrEmpty(order.getAvatar())){
					params.putData("id", order.getId());
					orderService.updateWebOrderWeixinInfo(params);
				}
			} else {
				CourseWebCrowdfunding info = crowdfundingService.findUserCrowdfundInfo(unionId, courseId);
				if(info != null && StringUtils.isNullOrEmpty(info.getAvatar())){
					params.putData("id", info.getId());
					crowdfundingService.updateUserCrowdfundWeixinInfo(params);
				}
			}
			return ok();
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: findUserJoinCourse 
	 * @Description: (查询用户报名课程) 
	 * @param mobile 手机号
	 * @param code 验证码
	 * @param courseId 课程ID
	 * @param unionId 唯一ID
	 * @param avatar 头像
	 * @param nickName 名称
	 * @param sex 性别
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findUserJoinCourse")
	public JSONObject findUserJoinCourse(String mobile,String code,Long courseId,String unionId,String avatar,String nickName,String sex){
		try{
			if(StringUtils.isNullOrEmpty(code) || StringUtils.isNullOrEmpty(mobile) || StringUtils.isNullOrEmpty(unionId) || StringUtils.isNullOrEmpty(avatar) || StringUtils.isNullOrEmpty(nickName) || StringUtils.isNullOrEmpty(sex)){
				return error("参数错误");
			}
			Code sms = codeService.findCode(code, mobile);
			if (sms == null) {
				return status(400,"验证码输入错误!");
			}
			codeService.updateCodeStatus(sms);
			
			Member member = memberService.findLoginMemberByMobile(mobile);
			if(member == null){
				return status(600,"手机号无效!");
			}
			CourseOfflineJoin join = courseService.findCourseJoinInfo(courseId, member.getId());
			if(join == null || join.getStatus() == 0){
				return status(555, "未报名!");
			}
			WebCourseOrder order = orderService.findWebOrderByCourseIdUnionId(courseId, unionId);
			if(order == null){
				order = new WebCourseOrder();
				order.setCourseId(courseId);
				order.setIp("111111");
				order.setNumber(1);
				order.setOpenid("openidWeb");
				order.setOrderCode(CodeUtils.getUUID());
				order.setPrice(0D);
				order.setStatus(1);
				order.setUnionid(unionId);
				order.setType(-1);
				order.setNickname(nickName);
				order.setWeixinSex(sex);
				order.setAvatar(avatar);
				order.setFrom("-1"); 	//自己添加
				order.setTicketStatus(0);
				orderService.saveWebCourseOrder(order);
			}
			return ok("msg",order);
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}

	/**
	 * @Title: findOnlineCommentLimit50
	 * @Description: (查询前50条评论)
	 * @param courseId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findOnlineCommentLimit50")
	public JSONObject findOnlineCommentLimit50(Long courseId) {
		try {
			if (NumberUtils.isNullOrZero(courseId)) {
				return error("参数缺失");
			}
			List<OnlineCourseCommentInfo> list = commonService.findOnlineCourseCommentLimit50(courseId);
			return ok("查询成功", list);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}

	/**
	 * @Title: findCourseWebUrl
	 * @Description: (查询课程URL)
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findCourseWebUrl")
	public JSONObject findCourseWebUrl(Long id) {
		try {
			if (NumberUtils.isNullOrZero(id)) {
				return error("参数缺失");
			}
			CourseWebInfo info = commonService.findCourseWebInfo(id);
			if (info == null) {
				return error("参数错误");
			}
			return ok("请求成功", info);
		} catch (Exception e) {
			e.printStackTrace();
			return error(ADMIN_SYSTEM_ERROR);
		}
	}

	/**
	 * @Title: getAccessToken
	 * @Description: (获得微信token,分享用)
	 * @param url
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getAccessToken")
	public JSONObject getAccessToken(String url) {
		try {
			String ticket = "";
			HttpClient httpclient = HttpClients.createDefault();

			String token = commonService.getAccessToken("wx46b392bf3bf15522", "e407de9e5f7ad53ccd8082665fd2119a");
			String ticketKey = cacheInfo.getCacheWeixinTicketKey();
			if (redisClient.isExist(ticketKey)) {
				ticket = redisClient.getObject(ticketKey).toString();
			} else {
				StringBuffer sb2 = new StringBuffer("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=");
				sb2.append(token).append("&type=jsapi");

				HttpGet httpGet = new HttpGet(sb2.toString());
				httpGet.setHeader("Accept", "application/json");
				httpGet.setHeader("Content-Type", "application/json;charset=utf-8");

				HttpResponse response2 = httpclient.execute(httpGet);
				HttpEntity entity2 = response2.getEntity();
				if (entity2 != null) {
					String result2 = EntityUtils.toString(entity2, "UTF-8");
					JSONObject obj2 = JSONObject.parseObject(result2);
					ticket = obj2.get("ticket").toString();
					redisClient.setObject(ticketKey, ticket, 7200);
				}
			}

			Long time = DateUtils.getNowDate().getTime() / 1000;
			String uuid = CodeUtils.getUUID();
			StringBuffer buffer = new StringBuffer();
			buffer.append("jsapi_ticket=").append(ticket).append("&noncestr=").append(uuid).append("&timestamp=")
					.append(time).append("&url=").append(url);
			String digest = new SHA1().getDigestOfString(buffer.toString().getBytes());

			JSONObject resultObj = new JSONObject();
			resultObj.put("time", time);
			resultObj.put("uuid", uuid);
			resultObj.put("digest", digest.toLowerCase());

			return ok("msg", resultObj);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}

	/**
	 * @Title: updateClickNumber
	 * @Description: (修改点击数量)
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateClickNumber")
	public JSONObject updateClickNumber(Long id) {
		try {
			if (NumberUtils.isNullOrZero(id)) {
				return error("参数缺失");
			}
			CourseWebInfo info = commonService.findCourseWebInfo(id);
			if (info == null) {
				return error("参数错误");
			}
			info.setNumber(info.getNumber() + 1);
			commonService.updateCourseWebInfo(info);
			return ok();
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}

	/**
	 * @Title: updateCourseClickNumber
	 * @Description: (修改网页课程点击数量)
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateWebCourseClickNumber")
	public JSONObject updateCourseClickNumber(Long id, String ip) {
		try {
			if (NumberUtils.isNullOrZero(id)) {
				return error("参数缺失");
			}
			commonService.updateWebCourseClickNumber(id);

			// 保存点击日志
			CourseClickLog log = new CourseClickLog();
			log.setCourseId(id);
			log.setCreateAt(DateUtils.getNowDate());
			log.setIpAddress(ip);
			log.setType(0);
			log.setMemberId(0L);

			logService.saveCourseClickLog(log);
			return ok();
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}

	/**
	 * @Title: saveWebListLog
	 * @Description: (保存web页面列表日志)
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveWebListLog")
	public JSONObject saveWebListLog(String ip) {
		try {
			logService.saveWebListLog(ip);
			return ok();
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}

	/**
	 * @Title: getLoginAccessTokenByCode
	 * @Description: (根据Code 获得Token)
	 * @param code
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getLoginAccessTokenByCode")
	public JSONObject getLoginAccessTokenByCode(String code) {
		try {
			HttpClient httpclient = HttpClients.createDefault();
			StringBuffer bf = new StringBuffer();
			bf.append("https://api.weixin.qq.com/sns/oauth2/access_token?appid=").append(OrderController.appid)
					.append("&secret=");
			bf.append(OrderController.secret).append("&code=").append(code).append("&grant_type=authorization_code");
			HttpGet httpGet = new HttpGet(bf.toString());
			httpGet.setHeader("Accept", "application/json");
			httpGet.setHeader("Content-Type", "application/json;charset=utf-8");

			HttpResponse response2 = httpclient.execute(httpGet);
			HttpEntity entity2 = response2.getEntity();
			if (entity2 != null) {
				String result2 = EntityUtils.toString(entity2, "UTF-8");
				JSONObject obj2 = JSONObject.parseObject(result2);

				HttpGet getInfo = new HttpGet("https://api.weixin.qq.com/sns/userinfo?access_token="
						+ obj2.getString("access_token") + "&openid=" + obj2.getString("openid") + "&lang=zh_CN");

				HttpResponse response = httpclient.execute(getInfo);
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String result = EntityUtils.toString(entity, "UTF-8");
					JSONObject resultObj = JSONObject.parseObject(result);
					return ok("返回成功", resultObj);
				}
			}
			return error("请求失败");
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	// 调用微信客服发送消息 勿动
	@Deprecated
	// @ResponseBody
	// @RequestMapping("/postMessage")
	public JSONObject postMessage() {
		try {
			HttpClient httpclient = HttpClients.createDefault();
			String token = getAccessToken();
			String next = "";
			int i = 1;
			Integer total = 0;
			Integer count = 0;
			while (total == 0 || total > count) {
				HttpGet get = new HttpGet(
						"https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + token + "&next_openid=" + next);

				HttpResponse response = httpclient.execute(get);
				HttpEntity entity = response.getEntity();
				String result = "";
				if (entity != null) {
					result = EntityUtils.toString(entity, "UTF-8");
					System.out.println(result);

					JSONObject jsonObject = JSONObject.parseObject(result);
					System.out.println(jsonObject.toJSONString());

					total = jsonObject.getInteger("total");
					count += jsonObject.getInteger("count");

					next = jsonObject.getString("next_openid");

					JSONObject data = jsonObject.getJSONObject("data");
					JSONArray arr = data.getJSONArray("openid");

					for (Object obj : arr) {
						JSONObject json = new JSONObject();
						json.put("content", "关注插坐学院公众号：chazuomba");
						HttpPost post = new HttpPost(
								"https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + token);

						// List <NameValuePair> params = new
						// ArrayList<NameValuePair>();
						// params.add(new BasicNameValuePair("openid",
						// obj.toString()));
						// params.add(new BasicNameValuePair("msgtype",
						// "text"));
						//
						// params.add(new BasicNameValuePair("text",
						// json.toJSONString()));

						// 接收参数json列表
						JSONObject jsonParam = new JSONObject();
						jsonParam.put("touser", obj.toString());
						jsonParam.put("msgtype", "text");
						jsonParam.put("text", json);

						StringEntity entityStr = new StringEntity(jsonParam.toString(), "utf-8");// 解决中文乱码问题
						entityStr.setContentEncoding("UTF-8");
						entityStr.setContentType("application/json");
						post.setEntity(entityStr);

						HttpResponse httpResponse = httpclient.execute(post);
						if (httpResponse.getStatusLine().getStatusCode() == 200) {
							HttpEntity httpEntity = httpResponse.getEntity();
							result = EntityUtils.toString(httpEntity);// 取出应答字符串
							System.out.println("------> " + i);
							System.out.println(result);
						}

						i++;
					}
				}
			}
			return ok();
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}

	/**
	 * @Title: felicitateUser
	 * @Description: (这里用一句话描述这个方法的作用)
	 * @param type
	 *            类型
	 * @param id
	 *            id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/felicitateUser")
	public JSONObject felicitateUser(Integer type, Long id) {
		try {
			if (type == null || NumberUtils.isNullOrZero(id)) {
				return error("参数错误");
			}
			commonService.updateWebOrderFelicitates(type, id);
			return ok();
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}

	/**
	 * @Title: findPaySuccessInfo
	 * @Description: (查询信息)
	 * @param type
	 *            类型
	 * @param courseId
	 *            课程ID
	 * @param unionId
	 *            唯一ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findPaySuccessInfo")
	public JSONObject findPaySuccessInfo(Integer type, Long courseId, String unionId) {
		try {
			if (type == null || NumberUtils.isNullOrZero(courseId) || StringUtils.isNullOrEmpty(unionId)) {
				return error("参数错误");
			}
			if (type == 1) {
				WebCourseOrder order = orderService.findWebOrderByCourseIdUnionId(courseId, unionId);
				return ok("msg", order);
			} else {
				CourseWebCrowdfunding info = crowdfundingService.findUserCrowdfundInfo(unionId, courseId);
				return ok("msg", info);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}

	}

	// 这是公司精选的 key value
	private String getAccessToken() {
		String token = "";
		try {
			HttpClient httpclient = HttpClients.createDefault();
			String tokenKey = cacheInfo.getCacheWeixinTokenKey() + "asdsad";
			if (redisClient.isExist(tokenKey)) {
				token = redisClient.getObject(tokenKey).toString();
			} else {
				StringBuffer urlStr = new StringBuffer("https://api.weixin.qq.com/cgi-bin/token?");
				urlStr.append("grant_type=").append("client_credential").append("&appid=").append("wx2e94ba4f5afcf31b")
						.append("&secret=").append("04250bcbef29c01b3c2ad1aec0f5f02c");

				HttpGet get = new HttpGet(urlStr.toString());
				get.setHeader("Accept", "application/json");
				get.setHeader("Content-Type", "application/json;charset=utf-8");

				HttpResponse response = httpclient.execute(get);
				HttpEntity entity = response.getEntity();
				String result = "";
				if (entity != null) {
					result = EntityUtils.toString(entity, "UTF-8");
					System.out.println(result);
				}
				JSONObject obj = JSONObject.parseObject(result);
				token = obj.get("access_token").toString();

				redisClient.setObject(tokenKey, token, 7200);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return token;
	}
	
	/** 
     * 向指定 URL 发送POST方法的请求 
     *  
     * @param url 
     *            发送请求的 URL 
     * @param param 
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。 
     * @return 所代表远程资源的响应结果 
     */  
    public static String sendPost(String url, String param) {  
        PrintWriter out = null;  
        BufferedReader in = null;  
        String result = "";  
        try {  
            URL realUrl = new URL(url);  
            // 打开和URL之间的连接  
            URLConnection conn = realUrl.openConnection();  
            //设置通用的请求属性  
            conn.setRequestProperty("user-agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0)");  
            // 发送POST请求必须设置如下两行  
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            // 获取URLConnection对象对应的输出流  
            OutputStreamWriter outWriter = new OutputStreamWriter(conn.getOutputStream(), "utf-8");  
            out = new PrintWriter(outWriter);  
            // 发送请求参数  
            out.print(param);  
            // flush输出流的缓冲  
            out.flush();  
            // 定义BufferedReader输入流来读取URL的响应  
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));  
            String line;  
            while ((line = in.readLine()) != null) {  
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));result += line;  
            }  
        } catch (Exception e) {  
            System.out.println("发送 POST 请求出现异常！"+e);  
            e.printStackTrace();  
        }  
        //使用finally块来关闭输出流、输入流  
        finally{  
            try{  
                if(out!=null){  
                    out.close();  
                }  
                if(in!=null){  
                    in.close();  
                }  
            }  
            catch(IOException ex){  
                ex.printStackTrace();  
            }  
        }  
        return result;  
    }
    
    /** 
     * 普通文本消息，需用户在48h与公共帐号有互动 
     * 微信公共账号发送给账号 
     * @param content 文本内容 
     * @param toUser(OPENID) 微信用户   
     * @return 
     */  
	@ResponseBody
	@RequestMapping("/sendTextMessageToUser")
    public JSONObject sendTextMessageToUser(HttpServletRequest request,String content,String toUser){  
        String json = "{\"touser\": \""+toUser+"\",\"msgtype\": \"text\", \"text\": {\"content\": \""+content+"\"}}";  
  
        //获取access_token  
        String token = commonService.getAccessToken("wx46b392bf3bf15522", "e407de9e5f7ad53ccd8082665fd2119a");
  
        //发送模版消息给指定用户  
        String action = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+token;  
  
        System.out.println("json:"+json);  
        try {  
            String result = sendPost(action, json);  
            System.out.println(result);  
            return ok("发送成功");
        } catch (Exception e) { 
            e.printStackTrace();  
            return error("系统异常");
        }
    }  

	
}
