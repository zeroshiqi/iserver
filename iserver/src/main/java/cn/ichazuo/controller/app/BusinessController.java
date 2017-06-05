package cn.ichazuo.controller.app;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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

import cn.ichazuo.commons.base.BaseController;
import cn.ichazuo.commons.component.ConfigInfo;
import cn.ichazuo.commons.util.CodeUtils;
import cn.ichazuo.commons.util.DateUtils;
import cn.ichazuo.commons.util.NumberUtils;
import cn.ichazuo.commons.util.PasswdEncryption;
import cn.ichazuo.commons.util.StringUtils;
import cn.ichazuo.commons.util.alipay.util.AlipayNotify;
import cn.ichazuo.commons.util.model.EModel;
import cn.ichazuo.commons.util.model.Page;
import cn.ichazuo.commons.util.model.Params;
import cn.ichazuo.commons.util.model.PhoneInfo;
import cn.ichazuo.model.admin.OfflineMsgInfo;
import cn.ichazuo.model.app.BusinessFeedBack;
import cn.ichazuo.model.app.CatalogCourseList;
import cn.ichazuo.model.app.Comment;
import cn.ichazuo.model.app.CommentPraise;
import cn.ichazuo.model.app.CourseWebCrowdfundingInfo;
import cn.ichazuo.model.app.DictItem;
import cn.ichazuo.model.app.FirstType;
import cn.ichazuo.model.app.KeyWords;
import cn.ichazuo.model.app.LoginInfo;
import cn.ichazuo.model.app.MemberPayInfo;
import cn.ichazuo.model.app.OfflineCourseListInfo;
import cn.ichazuo.model.app.Recommend;
import cn.ichazuo.model.app.SecondCatalogCourseList;
import cn.ichazuo.model.app.SecondType;
import cn.ichazuo.model.app.SelfStudy;
import cn.ichazuo.model.app.StudyDetail;
import cn.ichazuo.model.app.StudyDetailHistory;
import cn.ichazuo.model.app.StudyPlan;
import cn.ichazuo.model.app.StudyPlanCatalog;
import cn.ichazuo.model.app.StudyReport;
import cn.ichazuo.model.app.WebCourseOrderInfo;
import cn.ichazuo.model.table.ArticleCommentFavour;
import cn.ichazuo.model.table.Banner;
import cn.ichazuo.model.table.BlockDetail;
import cn.ichazuo.model.table.BusinessTicket;
import cn.ichazuo.model.table.Catalog;
import cn.ichazuo.model.table.Code;
import cn.ichazuo.model.table.Course;
import cn.ichazuo.model.table.CourseOfflineJoin;
import cn.ichazuo.model.table.CourseOrder;
import cn.ichazuo.model.table.CourseWebCrowdfunding;
import cn.ichazuo.model.table.CourseWebCrowdfundingLog;
import cn.ichazuo.model.table.CourseWebCrowdfundingOrder;
import cn.ichazuo.model.table.CourseWebCrowdfundingUser;
import cn.ichazuo.model.table.Employee;
import cn.ichazuo.model.table.FeedBack;
import cn.ichazuo.model.table.LoginDetail;
import cn.ichazuo.model.table.Member;
import cn.ichazuo.model.table.MemberRecord;
import cn.ichazuo.model.table.OfflineCourse;
import cn.ichazuo.model.table.RecommendInfo;
import cn.ichazuo.model.table.SecondCatalog;
import cn.ichazuo.model.table.Student;
import cn.ichazuo.model.table.Teacher;
import cn.ichazuo.model.table.Ticket;
import cn.ichazuo.model.table.WebCourseOrder;
import cn.ichazuo.model.table.WebCourseOrderUser;
import cn.ichazuo.service.BusinessService;
import cn.ichazuo.service.CodeService;
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
@Controller("app.businessController")
public class BusinessController extends BaseController {
	@Autowired
	private MemberService memberService;
	@Autowired
	private BusinessService businessService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private CrowdfundingService crowdfundingService;
	protected Logger logger = LoggerFactory.getLogger(getClass());
	public static final String css = "<link rel='stylesheet' href='https://res.wx.qq.com/mpres/htmledition/ueditor/themes/iframe.css' /><link rel='stylesheet' type='text/css' href='http://www.chazuomba.com/files/bg.css'>";
	public static final String sign="53b0f682ae5170a3184fa0";
	public static final String AESKEY = "483322d1ae220826ea76f2689";
	/**
	 * @Title: getCatalogList
	 * @Description: (获得课程分类列表)
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getCatalogList")
	public JSONObject getCatalogList(Page page,Long employeeId,String client,String version,String deviceId) {
		try {
			List<Catalog> catalogList = null;
//			Employee employee = businessService.findEmployeeDetail(employeeId);
//			Long ifBusiness = employee.getIfBusiness();
			//判断用户是否为苹果端注册用户
			//1:苹果端注册用户 不为1：后来录入用户
//			if(ifBusiness==1){
//				//查询个人用户分类列表
////				catalogList=businessService.findPersonCatalogList(page); 
//				//以下是判断用户会员是否过期的
//				MemberPayInfo payInfo = businessService.queryMemberPayByEmployeeId(employeeId);
//			    if(payInfo!=null){
//			    	Long monthCount = businessService.queryMonthCountByEmployeeId(employeeId);
//			    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
//			    	Date now = new Date();
//			    	Calendar calendar = Calendar.getInstance();
//			    	calendar.setTime(payInfo.getCreateAt());
//			    	calendar.add(Calendar.MONTH,  Integer.valueOf(monthCount.toString()));
//			    	System.out.println(sdf.format(calendar.getTime()));
//			    	//当前时间
//			    	Date dateTime1 = dateFormat.parse(sdf.format(now));
//			    	//会员过期时间
//			        Date dateTime2 = dateFormat.parse(sdf.format(calendar.getTime()));
//			        int i = dateTime1.compareTo(dateTime2);  
//			        System.out.println(i < 0);
//			    }
//			}else{
				if(employeeId!=33){
					//查询最近一次登录的信息
					LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employeeId);
					//查询学员此种设备、此个设备ID上次登录的明细
					LoginDetail loginDetail = memberService.findBusinessLoginDetail(employeeId,client,deviceId);
					if(loginDetail!=null){
						Long detailId = loginDetail.getId();
						Long lastId = lastLogin.getId();
						//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，，如果最后一次的大于本机登陆的，那么就是在异地登录了
						if(lastId>detailId){
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
						}
					}
				}
				catalogList=businessService.findCatalogList(page);
				for(int i=0;i<catalogList.size();i++){
//					String content = catalogList.get(i).getAvatar();
//					if(!StringUtils.isNullOrEmpty(content)){
//						//随机生成字母和数字的组合
//						String pwd = PasswdEncryption.getStringRandom();
//						StringBuffer stringBuffer = new StringBuffer(commonService.appenUrl(catalogList.get(i).getAvatar()));
//						stringBuffer.insert(50, pwd).toString();
//						catalogList.get(i).setAvatar(stringBuffer.toString().toUpperCase());
//					}else{
//						catalogList.get(i).setAvatar(content);
//					}
					catalogList.get(i).setAvatar(commonService.appenUrl(catalogList.get(i).getAvatar()));
				}
//			}
			return ok("查询成功", catalogList);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: getParentCatalogList
	 * @Description: (获得课程一级分类列表)
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getParentCatalogList")
	public JSONObject getParentCatalogList(Page page,Long employeeId,String client,String version,String deviceId) {
		try {
			List<Catalog> catalogList = null;
			List<Catalog> buyList = null;
			List<Catalog> otherList = null;
				if(employeeId!=33){
					//查询最近一次登录的信息
					LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employeeId);
					//查询学员此种设备、此个设备ID上次登录的明细
					LoginDetail loginDetail = memberService.findBusinessLoginDetail(employeeId,client,deviceId);
					if(loginDetail!=null){
						Long detailId = loginDetail.getId();
						Long lastId = lastLogin.getId();
						//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，，如果最后一次的大于本机登陆的，那么就是在异地登录了
						if(lastId>detailId){
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
						}
					}
				}
				if(page.getNowPage()>=2){
					page.setPage(2);
				}
				List<MemberRecord> recordList = businessService.findMemberRecordList(employeeId.toString());
				//判断用户是否购买了年费会员，如果购买了则课程包列表排列顺序为后台维护的排列顺序
				//①、购买了年费会员
				if(recordList.size()>0){
					catalogList=businessService.findParentCatalogList(page);
				}else{
					//②、未购买年费会员
					//用户不是年费会员，根据employeeId判断用户是否有有效期内的系列课会员记录存在
					List<MemberRecord> seriesList =  businessService.findMemberRecordListByEmployeeId(employeeId.toString());
					if(seriesList.size()>0){
						//如果存在查询会员买的所有课程
						catalogList=businessService.findBuyParentCatalogList(employeeId);
						otherList = businessService.findOtherParentCatalogList(employeeId);
						catalogList.addAll(otherList);
					}else{
						catalogList=businessService.findParentCatalogList(page);
					}
				}
				Employee employee1 = businessService.findEmployeeDetail(employeeId);
				int start = (page.getNowPage()-1)*20;
				int mid = page.getNowPage()*20;
				int end =page.getNowPage()*20-1;
				if(catalogList.size()>=mid){
					end = page.getNowPage()*20-1;
				}else{
					end= catalogList.size();
				}
				for(int i=start;i<end;i++){
					//判断t_business_member_record表中是否有效期内的好多课会员数据存在
//					List<MemberRecord> recordList = businessService.findMemberRecordList(employeeId.toString());
					if(recordList.size()>0){
						catalogList.get(i).setIsChapter("1");
					}else{
						//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
						 if(businessService.findMemberRecordListByCatalogId(employeeId.toString(),catalogList.get(i).getId().toString()).size()>0){
							 catalogList.get(i).setIsChapter("1");
						 }else{
							 catalogList.get(i).setIsChapter("0");
						 }
					}
					
					//获取teacherId 的集合
//					String ids = catalogList.get(i).getTeacherId();
//					List<Teacher> teacherList = new ArrayList<Teacher>();
//					StringBuffer sb = new StringBuffer();
//					for (String s : ids.split(",")) {
//						if (StringUtils.isNullOrEmpty(s)) {
//							continue;
//						}
//						List<Teacher> list=businessService.findTeacherById(Long.parseLong(s));
//						if(list.size()>0){
//							teacherList.add(list.get(0));
//						}
////						sb.append(service.findTeacherById(Long.parseLong(s)).getStr("name")).append(",");
//					}
//					catalogList.get(i).setTeacherList(teacherList);
					catalogList.get(i).setAvatar(commonService.appenUrl(catalogList.get(i).getAvatar()));
					catalogList.get(i).getFlag();
					//判断是以什么形式展现课程列表
					if(catalogList.get(i).getFlag()=="0" || ("0").equals(catalogList.get(i).getFlag())){
						//修改课程数量
						catalogList.get(i).setCourseCount(businessService.findCatalogListCourseCount(catalogList.get(i).getId()));
						//修改学习人数
						catalogList.get(i).setStudyCount(businessService.findCatalogListStudyCount(catalogList.get(i).getId()));
					}
				}
				List<Catalog> last = new ArrayList<Catalog>();
				for(int i=start;i<end;i++){
					last.add(catalogList.get(i));
				}
				catalogList = last;
//			}
			return ok("查询成功", catalogList);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: getCatalogList
	 * @Description: (获得课程分类列表)
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getSecondCatalogList")
	public JSONObject getSecondCatalogList(Page page,Long employeeId,Long parentId,String client,String version,String deviceId) {
		try {
			List<SecondCatalog> catalogList = null;
//			Employee employee = businessService.findEmployeeDetail(employeeId);
//			Long ifBusiness = employee.getIfBusiness();
			//判断用户是否为苹果端注册用户
			//1:苹果端注册用户 不为1：后来录入用户
//			if(ifBusiness==1){
//				//查询个人用户分类列表
////				catalogList=businessService.findPersonCatalogList(page); 
//				//以下是判断用户会员是否过期的
//				MemberPayInfo payInfo = businessService.queryMemberPayByEmployeeId(employeeId);
//			    if(payInfo!=null){
//			    	Long monthCount = businessService.queryMonthCountByEmployeeId(employeeId);
//			    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
//			    	Date now = new Date();
//			    	Calendar calendar = Calendar.getInstance();
//			    	calendar.setTime(payInfo.getCreateAt());
//			    	calendar.add(Calendar.MONTH,  Integer.valueOf(monthCount.toString()));
//			    	System.out.println(sdf.format(calendar.getTime()));
//			    	//当前时间
//			    	Date dateTime1 = dateFormat.parse(sdf.format(now));
//			    	//会员过期时间
//			        Date dateTime2 = dateFormat.parse(sdf.format(calendar.getTime()));
//			        int i = dateTime1.compareTo(dateTime2);  
//			        System.out.println(i < 0);
//			    }
//			}else{
				if(employeeId!=33){
					//查询最近一次登录的信息
					LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employeeId);
					//查询学员此种设备、此个设备ID上次登录的明细
					LoginDetail loginDetail = memberService.findBusinessLoginDetail(employeeId,client,deviceId);
					if(loginDetail!=null){
						Long detailId = loginDetail.getId();
						Long lastId = lastLogin.getId();
						//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，，如果最后一次的大于本机登陆的，那么就是在异地登录了
						if(lastId>detailId){
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
						}
					}
				}
				SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				System.out.println("查询二级分类列表开始时间："+df1.format(new Date()));
				catalogList=businessService.findSecondCatalogList(page,parentId);
				System.out.println("查询二级分类列表结束时间："+df1.format(new Date()));
				for(int i=0;i<catalogList.size();i++){
					catalogList.get(i).setAvatar(commonService.appenUrl(catalogList.get(i).getAvatar()));
					List<SecondCatalogCourseList> catalogCourseList = null;
					System.out.println("查询二级分类下课程列表开始时间："+df1.format(new Date()));
					catalogCourseList=businessService.getSecondCatalogCourseList(catalogList.get(i).getId());
					System.out.println("查询二级分类下课程列表结束时间："+df1.format(new Date()));
					//循环遍历集合查询是否是学员的推荐课程和计划学习课程
					if(catalogCourseList!=null){
						for(int j=0;j<catalogCourseList.size();j++){
							//是否推荐
							if(businessService.queryRecommend(catalogCourseList.get(j).getId(),employeeId).size()>0){
								Long ifRecommend = (long) 1;
								catalogCourseList.get(j).setIfRecommend(ifRecommend);
							}else{
								Long ifRecommend = (long) 0;
								catalogCourseList.get(j).setIfRecommend(ifRecommend);
							}
							//是否在学习计划
							if(businessService.queryStudyPlan(catalogCourseList.get(j).getId(),employeeId).size()>0){
								Long ifStudyPlan = (long) 1;
								catalogCourseList.get(j).setIfStudyPlan(ifStudyPlan);
							}else{
								Long ifStudyPlan = (long) 0;
								catalogCourseList.get(j).setIfStudyPlan(ifStudyPlan);
							}
							catalogCourseList.get(j).setAvatar(commonService.appenUrl(catalogCourseList.get(j).getAvatar()));
							//根据版本号判断是否加密PDF地址
//							if(!("1.6.0").equals(version) && !("1.5.9").equals(version)){
//								catalogCourseList.get(j).setPdfAddress(commonService.appenUrl(catalogCourseList.get(j).getPdfAddress()));
//							}else{
								String content =catalogCourseList.get(j).getPdfAddress();
								String playAddress = catalogCourseList.get(j).getPlayAddress();
								//随机生成字母和数字的组合加密录音文件地址
								String pwd1 = PasswdEncryption.getStringRandom();
								StringBuffer stringBuffer1 = new StringBuffer(commonService.appenUrl(playAddress));
								stringBuffer1.insert(50, pwd1).toString();
								catalogCourseList.get(j).setPlayAddress(stringBuffer1.toString().toUpperCase());
								//加密PlayAddressM3u8
//								String playAddress1 = catalogCourseList.get(j).getPlayAddressM3u8();
//								String pwd2 = PasswdEncryption.getStringRandom();
//								StringBuffer stringBuffer2 = new StringBuffer(commonService.appenUrl(playAddress1));
//								stringBuffer2.insert(50, pwd2).toString();
//								catalogCourseList.get(j).setPlayAddressM3u8(stringBuffer2.toString().toUpperCase());
								if(!StringUtils.isNullOrEmpty(content)){
									//随机生成字母和数字的组合加密PDF地址
									String pwd = PasswdEncryption.getStringRandom();
									StringBuffer stringBuffer = new StringBuffer(commonService.appenUrl(catalogCourseList.get(j).getPdfAddress()));
									stringBuffer.insert(50, pwd).toString();
									catalogCourseList.get(j).setPdfAddress(stringBuffer.toString().toUpperCase());
//									catalogList.get(i).setAvatar(stringBuffer.toString().toUpperCase());
								}else{
									catalogCourseList.get(j).setPdfAddress(commonService.appenUrl(content));
								}
							}
//						}
					}
					catalogList.get(i).setCatalogCourseList(catalogCourseList);
				}
				System.out.println("遍历结束时间："+df1.format(new Date()));
//			}
			return ok("查询成功", catalogList);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: getCatalogCourseList
	 * @Description: (获得课程分类下课程列表)
	 * @param catalogId
	 *            分类ID
	 * @param employeeId
	 *            用户ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getSecondCatalogCourseList")
	public JSONObject getSecondCatalogCourseList(Long catalogId,Long employeeId,String client,String version,String deviceId) {
		try {
			List<SecondCatalogCourseList> catalogCourseList = null;
			if(employeeId!=33){
				LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employeeId);
				//查询学员此种设备、此个设备ID上次登录的明细
				LoginDetail loginDetail = memberService.findBusinessLoginDetail(employeeId,client,deviceId);
				if(loginDetail!=null){
					Long detailId = loginDetail.getId();
					Long lastId = lastLogin.getId();
					//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，，如果最后一次的大于本机登陆的，那么就是在异地登录了
					if(lastId>detailId){
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
					}
				}
			}
			catalogCourseList=businessService.getSecondCatalogCourseList(catalogId);
			//循环遍历集合查询是否是学员的推荐课程和计划学习课程
			for(int i=0;i<catalogCourseList.size();i++){
				//是否推荐
				if(businessService.queryRecommend(catalogCourseList.get(i).getId(),employeeId).size()>0){
					Long ifRecommend = (long) 1;
					catalogCourseList.get(i).setIfRecommend(ifRecommend);
				}else{
					Long ifRecommend = (long) 0;
					catalogCourseList.get(i).setIfRecommend(ifRecommend);
				}
				//是否在学习计划
				if(businessService.queryStudyPlan(catalogCourseList.get(i).getId(),employeeId).size()>0){
					Long ifStudyPlan = (long) 1;
					catalogCourseList.get(i).setIfStudyPlan(ifStudyPlan);
				}else{
					Long ifStudyPlan = (long) 0;
					catalogCourseList.get(i).setIfStudyPlan(ifStudyPlan);
				}
				catalogCourseList.get(i).setAvatar(commonService.appenUrl(catalogCourseList.get(i).getAvatar()));
				catalogCourseList.get(i).setPdfAddress(commonService.appenUrl(catalogCourseList.get(i).getPdfAddress()));
//				String content =catalogCourseList.get(i).getPdfAddress();
//				if(!StringUtils.isNullOrEmpty(content)){
//					//随机生成字母和数字的组合
//					String pwd = PasswdEncryption.getStringRandom();
//					StringBuffer stringBuffer = new StringBuffer(commonService.appenUrl(catalogCourseList.get(i).getPdfAddress()));
//					stringBuffer.insert(50, pwd).toString();
//					catalogCourseList.get(i).setPdfAddress(stringBuffer.toString().toUpperCase());
////					catalogList.get(i).setAvatar(stringBuffer.toString().toUpperCase());s
//				}else{
//					catalogCourseList.get(i).setAvatar(content);
//				}
//				//判断音频地址
//				if(!StringUtils.isNullOrEmpty(catalogCourseList.get(i).getPlayAddress())){
//					//随机生成字母和数字的组合
//					String pwd = PasswdEncryption.getStringRandom();
//					StringBuffer stringBuffer = new StringBuffer(commonService.appenUrl(catalogCourseList.get(i).getPlayAddress()));
//					stringBuffer.insert(50, pwd).toString();
//					catalogCourseList.get(i).setPlayAddress(stringBuffer.toString().toUpperCase());
//				}
				//根据版本号判断是否加密PDF地址,只加密正在审核的iOS版本地址
//				if(!("1.6.0").equals(version) && !("1.5.9").equals(version)){
//					catalogCourseList.get(i).setPdfAddress(commonService.appenUrl(catalogCourseList.get(i).getPdfAddress()));
//				}else{
					String content =catalogCourseList.get(i).getPdfAddress();
					String playAddress = catalogCourseList.get(i).getPlayAddress();
					//随机生成字母和数字的组合加密录音文件地址
					String pwd1 = PasswdEncryption.getStringRandom();
					StringBuffer stringBuffer1 = new StringBuffer(commonService.appenUrl(playAddress));
					stringBuffer1.insert(50, pwd1).toString();
					catalogCourseList.get(i).setPlayAddress(stringBuffer1.toString().toUpperCase());
					//加密PlayAddressM3u8
//					String playAddress1 = catalogCourseList.get(i).getPlayAddressM3u8();
//					String pwd2 = PasswdEncryption.getStringRandom();
//					StringBuffer stringBuffer2 = new StringBuffer(commonService.appenUrl(playAddress1));
//					stringBuffer2.insert(50, pwd2).toString();
//					catalogCourseList.get(i).setPlayAddressM3u8(stringBuffer2.toString().toUpperCase());
					if(!StringUtils.isNullOrEmpty(content)){
						//随机生成字母和数字的组合加密PDF地址
						String pwd = PasswdEncryption.getStringRandom();
						StringBuffer stringBuffer = new StringBuffer(commonService.appenUrl(catalogCourseList.get(i).getPdfAddress()));
						stringBuffer.insert(50, pwd).toString();
						catalogCourseList.get(i).setPdfAddress(stringBuffer.toString().toUpperCase());
//						catalogList.get(i).setAvatar(stringBuffer.toString().toUpperCase());
					}else{
						catalogCourseList.get(i).setPdfAddress(commonService.appenUrl(content));
					}
				}
//			}
			return ok("查询成功", catalogCourseList);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: getCatalogCourseList
	 * @Description: (获得课程分类下课程列表)
	 * @param catalogId
	 *            分类ID
	 * @param employeeId
	 *            用户ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getCatalogCourseList")
	public JSONObject getCatalogCourseList(Long catalogId,Long employeeId,String client,String version,String deviceId) {
		try {
			List<CatalogCourseList> catalogCourseList = null;
			if(employeeId!=33){
				LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employeeId);
				//查询学员此种设备、此个设备ID上次登录的明细
				LoginDetail loginDetail = memberService.findBusinessLoginDetail(employeeId,client,deviceId);
				if(loginDetail!=null){
					Long detailId = loginDetail.getId();
					Long lastId = lastLogin.getId();
					//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，，如果最后一次的大于本机登陆的，那么就是在异地登录了
					if(lastId>detailId){
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
					}
				}
			}
			catalogCourseList=businessService.getCatalogCourseList(catalogId);
			//循环遍历集合查询是否是学员的推荐课程和计划学习课程
			for(int i=0;i<catalogCourseList.size();i++){
				//是否推荐
				if(businessService.queryRecommend(catalogCourseList.get(i).getId(),employeeId).size()>0){
					Long ifRecommend = (long) 1;
					catalogCourseList.get(i).setIfRecommend(ifRecommend);
				}else{
					Long ifRecommend = (long) 0;
					catalogCourseList.get(i).setIfRecommend(ifRecommend);
				}
				//是否在学习计划
				if(businessService.queryStudyPlan(catalogCourseList.get(i).getId(),employeeId).size()>0){
					Long ifStudyPlan = (long) 1;
					catalogCourseList.get(i).setIfStudyPlan(ifStudyPlan);
				}else{
					Long ifStudyPlan = (long) 0;
					catalogCourseList.get(i).setIfStudyPlan(ifStudyPlan);
				}
				catalogCourseList.get(i).setAvatar(commonService.appenUrl(catalogCourseList.get(i).getAvatar()));
				catalogCourseList.get(i).setPdfAddress(commonService.appenUrl(catalogCourseList.get(i).getPdfAddress()));
//				String content =catalogCourseList.get(i).getPdfAddress();
//				if(!StringUtils.isNullOrEmpty(content)){
//					//随机生成字母和数字的组合
//					String pwd = PasswdEncryption.getStringRandom();
//					StringBuffer stringBuffer = new StringBuffer(commonService.appenUrl(catalogCourseList.get(i).getPdfAddress()));
//					stringBuffer.insert(50, pwd).toString();
//					catalogCourseList.get(i).setPdfAddress(stringBuffer.toString().toUpperCase());
////					catalogList.get(i).setAvatar(stringBuffer.toString().toUpperCase());s
//				}else{
//					catalogCourseList.get(i).setAvatar(content);
//				}
				//根据版本号判断是否加密PDF地址
//				if(!("1.6.0").equals(version) && !("1.5.9").equals(version)){
//					catalogCourseList.get(i).setPdfAddress(commonService.appenUrl(catalogCourseList.get(i).getPdfAddress()));
//				}else{
					String content =catalogCourseList.get(i).getPdfAddress();
					String playAddress = catalogCourseList.get(i).getPlayAddress();
					//随机生成字母和数字的组合加密录音文件地址
					String pwd1 = PasswdEncryption.getStringRandom();
					StringBuffer stringBuffer1 = new StringBuffer(commonService.appenUrl(playAddress));
					stringBuffer1.insert(50, pwd1).toString();
					catalogCourseList.get(i).setPlayAddress(stringBuffer1.toString().toUpperCase());
					//加密PlayAddressM3u8
//					String playAddress1 = catalogCourseList.get(i).getPlayAddressM3u8();
//					String pwd2 = PasswdEncryption.getStringRandom();
//					StringBuffer stringBuffer2 = new StringBuffer(commonService.appenUrl(playAddress1));
//					stringBuffer2.insert(50, pwd2).toString();
//					catalogCourseList.get(i).setPlayAddressM3u8(stringBuffer2.toString().toUpperCase());
					if(!StringUtils.isNullOrEmpty(content)){
						//随机生成字母和数字的组合加密PDF地址
						String pwd = PasswdEncryption.getStringRandom();
						StringBuffer stringBuffer = new StringBuffer(commonService.appenUrl(catalogCourseList.get(i).getPdfAddress()));
						stringBuffer.insert(50, pwd).toString();
						catalogCourseList.get(i).setPdfAddress(stringBuffer.toString().toUpperCase());
//						catalogList.get(i).setAvatar(stringBuffer.toString().toUpperCase());
					}else{
						catalogCourseList.get(i).setPdfAddress(commonService.appenUrl(content));
					}
				}
//			}
			return ok("查询成功", catalogCourseList);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: addStudyDetail 
	 * @Description: (记录学员学习记录) 
	 * @param courseId 课程ID
	 * @param employeeId 学员ID
	 * @param addNum 课程学习完成标识，2：已完成，1：未完成；0未开始
	 * @param studyTime 课程学习时长，单位秒
	 * @param courseTime 课程时长，只有学习完成时才传，大小为录音课件的时间长度，单位秒
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addStudyDetail")
	public JSONObject addStudyDetail(Long courseId,String employeeId,Long addNum,Long studyTime,Long courseTime,String client,String version,String deviceId,String sign){
		if(("2.0.5").equals(version) || ("2.0.1").equals(version)||("2.0.4").equals(version)){
			if(NumberUtils.isNullOrZero(courseId) || StringUtils.isNullOrEmpty(employeeId) || NumberUtils.isNullOrZero(studyTime)){
				return status(99,"参数缺失");
			}
			if(NumberUtils.isNullOrZero(addNum)){
				return status(99,"addNum参数传值错误");
			}
			//解密
			UpAES upAES = new UpAES();
//			upAES.entry("1");
			String newEmployeeId = upAES.disEntry(employeeId);
			String newDeviceId =upAES.disEntry(deviceId.toString());
			System.out.println("解密后的employeeId："+newEmployeeId);
			System.out.println("解密后的deviceId："+newDeviceId);
			//重新赋值deviceId
			String newSign = PasswdEncryption.MD5(newEmployeeId+newDeviceId);
			newSign= newSign.substring(5, newSign.length()-5);
			System.out.println("得到的新Sign:"+newSign);
			System.out.println("传的sign:"+sign);
			if(newSign.equals(sign)){
				try{
						String status = "";
						//根据客户端筛选错误数据
						if("android".equals(client) && addNum==2){
							List<StudyDetailHistory> historyList = null;
							historyList = businessService.queryStudyDetailHistory(Long.parseLong(newEmployeeId));
							if(historyList.size()>0){
								return status(200,"保存成功");
							}
							//客户端是android 并且学习进度为已完成存储接口请求记录历史
							businessService.addStudyDetailHistory(courseId,Long.parseLong(newEmployeeId),courseTime,addNum,status,client,version);
							
						}
						if(addNum==2){
							if(courseTime>5400){
								return status(200,"保存成功");
							}
//							if(businessService.findStudyDetail(courseId,employeeId,addNum).size()<1){
								status = "2";
								businessService.addStudyDetail(courseId,Long.parseLong(newEmployeeId),courseTime,addNum,status);
//							}
							businessService.addStudyNumber(courseId);
							if(!"33".equals(employeeId)){
								//查询在t_study_report表中是否有学员学习完成的数据存在
								List<StudyReport> report = businessService.findStudyReport(Long.parseLong(newEmployeeId),2L);
								List<StudyReport> reportAll = businessService.findStudyReport(Long.parseLong(newEmployeeId),1L);
								List<StudyReport> reportWeek = businessService.findStudyReport(Long.parseLong(newEmployeeId),4L);
								List<StudyReport> reportAllWeek = businessService.findStudyReport(Long.parseLong(newEmployeeId),5L);
								if(report.size()>0){
									//存在记录就更新学习时间
									businessService.updateStudyReportById(report.get(0).getId(),courseTime);
								}else{
									//不存在记录就新增记录
									businessService.addStudyReport(Long.parseLong(newEmployeeId),courseTime,2L);
								}
								if(reportAll.size()>0){
									//存在记录就更新学习时间
									//存在记录就更新学习时间
									businessService.updateStudyReportById(reportAll.get(0).getId(),courseTime);
								}else{
									//不存在记录就新增记录
									businessService.addStudyReport(Long.parseLong(newEmployeeId),courseTime,1L);
								}
								if(reportAllWeek.size()>0){
									//存在记录就更新学习时间
									//存在记录就更新学习时间
									businessService.updateStudyReportById(reportAllWeek.get(0).getId(),courseTime);
								}else{
									//不存在记录就新增记录
									businessService.addStudyReport(Long.parseLong(newEmployeeId),courseTime,5L);
								}
								if(reportWeek.size()>0){
									//存在记录就更新学习时间
									//存在记录就更新学习时间
									businessService.updateStudyReportById(reportWeek.get(0).getId(),courseTime);
								}else{
									//不存在记录就新增记录
									businessService.addStudyReport(Long.parseLong(newEmployeeId),courseTime,4L);
								}
								List<StudyPlan> planList = null;
								planList = businessService.queryStudyPlanExist(courseId,Long.parseLong(newEmployeeId));
	//							判断完成的课程是否在计划学习列表中，如果在则更新此条记录的学习进度
								if(planList.size()>0){ 
									//如果计划中的课程学习进度低于本次进度，则把本次进度当作最新进度更新到计划列表中
									if(planList.get(0).getIsFinished()<addNum){
										businessService.updateStudyPlan(planList.get(0).getId(),addNum);
									}
								}
							}
							return status(200,"保存成功");
						}
						if(addNum==1){
//							if(businessService.findStudyDetail(courseId,employeeId,addNum).size()<1){
								status = "1";
								businessService.addStudyDetail(courseId,Long.parseLong(newEmployeeId),studyTime,addNum,status);
//							}
							if(!"33".equals(employeeId)){
								List<StudyPlan> planList = null;
								planList = businessService.queryStudyPlanExist(courseId,Long.parseLong(newEmployeeId));
								//查询在t_study_report表中是否有学员学习完成的数据存在
								List<StudyReport> report = businessService.findStudyReport(Long.parseLong(newEmployeeId),3L);
								List<StudyReport> reportAll = businessService.findStudyReport(Long.parseLong(newEmployeeId),1L);
								List<StudyReport> reportAllWeek = businessService.findStudyReport(Long.parseLong(newEmployeeId),5L);
								if(report.size()>0){
									//存在记录就更新学习时间
									businessService.updateStudyReportById(report.get(0).getId(),courseTime);
								}else{
									//不存在记录就新增记录
									businessService.addStudyReport(Long.parseLong(newEmployeeId),courseTime,3L);
								}
								if(reportAll.size()>0){
									//存在记录就更新学习时间
									//存在记录就更新学习时间
									businessService.updateStudyReportById(reportAll.get(0).getId(),courseTime);
								}else{
									//不存在记录就新增记录
									businessService.addStudyReport(Long.parseLong(newEmployeeId),courseTime,1L);
								}
								if(reportAllWeek.size()>0){
									//存在记录就更新学习时间
									//存在记录就更新学习时间
									businessService.updateStudyReportById(reportAllWeek.get(0).getId(),courseTime);
								}else{
									//不存在记录就新增记录
									businessService.addStudyReport(Long.parseLong(newEmployeeId),courseTime,5L);
								}
	//							判断正在学习的课程是否在计划学习列表中，如果在则更新此条记录的学习进度
								if(planList.size()>0){
									//如果计划中的课程学习进度低于本次进度，则把本次进度当作最新进度更新到计划列表中
									if(planList.get(0).getIsFinished()<addNum){
										businessService.updateStudyPlan(planList.get(0).getId(),addNum);
									}
								}
							}
							return status(200,"保存成功");
						}
//					if(studyTime>0){
//						status = "1";
//						businessService.addStudyDetail(courseId,employeeId,studyTime,addNum,status);
//						return ok("保存成功");
//					}	
					return error("保存失败");
				}catch(Exception e){
					e.printStackTrace();
					return error(APP_SYSTEM_ERROR);
				}
			}else{
				return status(97,"sign验证出错");
			}
		}else{
			try{
				if(NumberUtils.isNullOrZero(courseId) || StringUtils.isNullOrEmpty(employeeId) || NumberUtils.isNullOrZero(studyTime)){
					return error("参数缺失");
				}
				if(NumberUtils.isNullOrZero(addNum)){
					return error("addNum参数传值错误");
				}
				
					String status = "";
					//根据客户端筛选错误数据
					if("android".equals(client) && addNum==2){
						List<StudyDetailHistory> historyList = null;
						historyList = businessService.queryStudyDetailHistory(Long.parseLong(employeeId));
						if(historyList.size()>0){
							return ok("保存成功");
						}
						//客户端是android 并且学习进度为已完成存储接口请求记录历史
						businessService.addStudyDetailHistory(courseId,Long.parseLong(employeeId),courseTime,addNum,status,client,version);
						
					}
					if(addNum==2){
						if(courseTime>5400){
							return ok("保存成功");
						}
//						if(businessService.findStudyDetail(courseId,employeeId,addNum).size()<1){
							status = "2";
							businessService.addStudyDetail(courseId,Long.parseLong(employeeId),courseTime,addNum,status);
//						}
						businessService.addStudyNumber(courseId);
						if(!"33".equals(employeeId)){
							List<StudyPlan> planList = null;
							planList = businessService.queryStudyPlanExist(courseId,Long.parseLong(employeeId));
							//查询在t_study_report表中是否有学员学习完成的数据存在
							List<StudyReport> report = businessService.findStudyReport(Long.parseLong(employeeId),2L);
							List<StudyReport> reportAll = businessService.findStudyReport(Long.parseLong(employeeId),1L);
							List<StudyReport> reportWeek = businessService.findStudyReport(Long.parseLong(employeeId),4L);
							List<StudyReport> reportAllWeek = businessService.findStudyReport(Long.parseLong(employeeId),5L);
							if(report.size()>0){
								//存在记录就更新学习时间
								businessService.updateStudyReportById(report.get(0).getId(),courseTime);
							}else{
								//不存在记录就新增记录
								businessService.addStudyReport(Long.parseLong(employeeId),courseTime,2L);
							}
							if(reportAll.size()>0){
								//存在记录就更新学习时间
								//存在记录就更新学习时间
								businessService.updateStudyReportById(reportAll.get(0).getId(),courseTime);
							}else{
								//不存在记录就新增记录
								businessService.addStudyReport(Long.parseLong(employeeId),courseTime,1L);
							}
							if(reportAllWeek.size()>0){
								//存在记录就更新学习时间
								//存在记录就更新学习时间
								businessService.updateStudyReportById(reportAllWeek.get(0).getId(),courseTime);
							}else{
								//不存在记录就新增记录
								businessService.addStudyReport(Long.parseLong(employeeId),courseTime,5L);
							}
							if(reportWeek.size()>0){
								//存在记录就更新学习时间
								//存在记录就更新学习时间
								businessService.updateStudyReportById(reportWeek.get(0).getId(),courseTime);
							}else{
								//不存在记录就新增记录
								businessService.addStudyReport(Long.parseLong(employeeId),courseTime,4L);
							}
	//						判断完成的课程是否在计划学习列表中，如果在则更新此条记录的学习进度
							if(planList.size()>0){ 
								//如果计划中的课程学习进度低于本次进度，则把本次进度当作最新进度更新到计划列表中
								if(planList.get(0).getIsFinished()<addNum){
									businessService.updateStudyPlan(planList.get(0).getId(),addNum);
								}
							}
						}
						return ok("保存成功");
					}
					if(addNum==1){
//						if(businessService.findStudyDetail(courseId,employeeId,addNum).size()<1){
							status = "1";
							businessService.addStudyDetail(courseId,Long.parseLong(employeeId),studyTime,addNum,status);
//						}
							
						List<StudyPlan> planList = null;
						planList = businessService.queryStudyPlanExist(courseId,Long.parseLong(employeeId));
						if(!"33".equals(employeeId)){
							//查询在t_study_report表中是否有学员学习完成的数据存在
							List<StudyReport> report = businessService.findStudyReport(Long.parseLong(employeeId),3L);
							List<StudyReport> reportAll = businessService.findStudyReport(Long.parseLong(employeeId),1L);
							List<StudyReport> reportAllWeek = businessService.findStudyReport(Long.parseLong(employeeId),5L);
							if(report.size()>0){
								//存在记录就更新学习时间
								businessService.updateStudyReportById(report.get(0).getId(),courseTime);
							}else{
								//不存在记录就新增记录
								businessService.addStudyReport(Long.parseLong(employeeId),courseTime,3L);
							}
							if(reportAll.size()>0){
								//存在记录就更新学习时间
								//存在记录就更新学习时间
								businessService.updateStudyReportById(reportAll.get(0).getId(),courseTime);
							}else{
								//不存在记录就新增记录
								businessService.addStudyReport(Long.parseLong(employeeId),courseTime,1L);
							}
							if(reportAllWeek.size()>0){
								//存在记录就更新学习时间
								//存在记录就更新学习时间
								businessService.updateStudyReportById(reportAllWeek.get(0).getId(),courseTime);
							}else{
								//不存在记录就新增记录
								businessService.addStudyReport(Long.parseLong(employeeId),courseTime,5L);
							}
						}
//						判断正在学习的课程是否在计划学习列表中，如果在则更新此条记录的学习进度
						if(planList.size()>0){
							//如果计划中的课程学习进度低于本次进度，则把本次进度当作最新进度更新到计划列表中
							if(planList.get(0).getIsFinished()<addNum){
								businessService.updateStudyPlan(planList.get(0).getId(),addNum);
							}
						}
						return ok("保存成功");
					}
//				if(studyTime>0){
//					status = "1";
//					businessService.addStudyDetail(courseId,employeeId,studyTime,addNum,status);
//					return ok("保存成功");
//				}	
				return error("保存失败");
			}catch(Exception e){
				e.printStackTrace();
				return error(APP_SYSTEM_ERROR);
			}
		}
		
	}
	/**
	 * @Title: findCatalogCourseListHot
	 * @Description: (根据播放时间等查询条件查询课程分类下热门课程列表)
	 * @param keyWordsId 关键字ID
	 * @param level 级别         
	 * @param employeeId  企业用户ID
	 * @param timeLength 时长
	 * @param page 分页
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findCatalogCourseListHot")
	public JSONObject findCatalogCourseListHot(int keyWordsId,Long employeeId,int timeLength,int level,Page page,String client,String version) {
		try {
			if(NumberUtils.isNullOrZero(timeLength) && NumberUtils.isNullOrZero(keyWordsId) && NumberUtils.isNullOrZero(level)){
				return error("参数缺失");
			}
			List<CatalogCourseList> catalogCourseList = null;
			Employee employee = businessService.findEmployeeDetail(employeeId);
			Long ifBusiness = employee.getIfBusiness();
			//判断用户是否为苹果端注册用户
			//1:苹果端注册用户 不为1：后来录入用户
//			if(ifBusiness==1){
//				catalogCourseList =businessService.findPersonCatalogCourseList(keyWordsId,timeLength,level,page);
//			}else{
				catalogCourseList=businessService.findCatalogCourseList(keyWordsId,timeLength,level,page);
//			}
			//循环遍历集合查询是否是学员的推荐课程和计划学习课程
			for(int i=0;i<catalogCourseList.size();i++){
				//是否推荐
				if(businessService.queryRecommend(catalogCourseList.get(i).getId(),employeeId).size()>0){
					Long ifRecommend = (long) 1;
					catalogCourseList.get(i).setIfRecommend(ifRecommend);
				}else{
					Long ifRecommend = (long) 0;
					catalogCourseList.get(i).setIfRecommend(ifRecommend);
				}
				//是否在学习计划
				if(businessService.queryStudyPlan(catalogCourseList.get(i).getId(),employeeId).size()>0){
					Long ifStudyPlan = (long) 1;
					catalogCourseList.get(i).setIfStudyPlan(ifStudyPlan);
				}else{
					Long ifStudyPlan = (long) 0;
					catalogCourseList.get(i).setIfStudyPlan(ifStudyPlan);
				}
				catalogCourseList.get(i).setAvatar(commonService.appenUrl(catalogCourseList.get(i).getAvatar()));
				//根据版本号判断是否加密PDF地址
//				if(!("1.6.0").equals(version) && !("1.5.9").equals(version)){
//					catalogCourseList.get(i).setPdfAddress(commonService.appenUrl(catalogCourseList.get(i).getPdfAddress()));
//				}else{
					String content =catalogCourseList.get(i).getPdfAddress();
					String playAddress = catalogCourseList.get(i).getPlayAddress();
					//随机生成字母和数字的组合加密录音文件地址
					String pwd1 = PasswdEncryption.getStringRandom();
					StringBuffer stringBuffer1 = new StringBuffer(commonService.appenUrl(playAddress));
					stringBuffer1.insert(50, pwd1).toString();
					catalogCourseList.get(i).setPlayAddress(stringBuffer1.toString().toUpperCase());
					//加密PlayAddressM3u8
//					String playAddress1 = catalogCourseList.get(i).getPlayAddressM3u8();
//					String pwd2 = PasswdEncryption.getStringRandom();
//					StringBuffer stringBuffer2 = new StringBuffer(commonService.appenUrl(playAddress1));
//					stringBuffer2.insert(50, pwd2).toString();
//					catalogCourseList.get(i).setPlayAddressM3u8(stringBuffer2.toString().toUpperCase());
					if(!StringUtils.isNullOrEmpty(content)){
						//随机生成字母和数字的组合加密PDF地址
						String pwd = PasswdEncryption.getStringRandom();
						StringBuffer stringBuffer = new StringBuffer(commonService.appenUrl(catalogCourseList.get(i).getPdfAddress()));
						stringBuffer.insert(50, pwd).toString();
						catalogCourseList.get(i).setPdfAddress(stringBuffer.toString().toUpperCase());
//						catalogList.get(i).setAvatar(stringBuffer.toString().toUpperCase());
					}else{
						catalogCourseList.get(i).setPdfAddress(commonService.appenUrl(content));
					}
				}
//			}
			return ok("查询成功", catalogCourseList);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: findDictItemList
	 * @Description: (查询全部数据字典项用于热门中用分类查询)
	 * @param page 分页
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findDictItemList")
	public JSONObject findDictItemList(Page page,String client,String version) {
		try {
			List<DictItem> dictList = null;
			dictList=businessService.findDictItemList(page);
			return ok("查询成功", dictList);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: updateStudyPlan
	 * @Description: (更改学习计划状态)
	 * @param employeeId 学员ID
	 * @param courseId 课程ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateStudyPlan")
	public JSONObject updateStudyPlan(Long employeeId,Long courseId,String client,String version) {
		try {
			List<StudyPlan> planList = null;
			planList = businessService.queryStudyPlan(courseId,employeeId);
			if(planList.size()>0){
				businessService.updateStudyPlan(planList.get(0).getId());
//				return ok("取消收藏成功");
				return status(300, "取消收藏成功");
			}else{
				List<StudyPlan> planListExist = null;
				planListExist = businessService.queryStudyPlanExist(courseId,employeeId);
				//判断学员历史是否收藏过此课程，如果收藏过并且已经取消收藏，则把数据还原
				if(planListExist.size()>0){
					businessService.updateStudyPlan(planListExist.get(0).getId());
					return ok("收藏成功");
				}else{
					businessService.saveStudyPlan(courseId,employeeId);
					return ok("收藏成功");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: collectionCatalog
	 * @Description: (收藏/取消收藏课程包)
	 * @param employeeId 学员ID
	 * @param catalogId 课程包ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/collectionCatalog")
	public JSONObject collectionCatalog(Long employeeId,Long catalogId,String client,String version) {
		try {
			List<StudyPlanCatalog> planList = null;
			planList = businessService.queryStudyPlanCatalog(catalogId,employeeId);
			if(planList.size()>0){
				businessService.updateStudyPlanCatalog(planList.get(0).getId());
//				return ok("取消收藏成功");
				return status(300, "取消收藏成功");
			}else{
				List<StudyPlanCatalog> planListExist = null;
				planListExist = businessService.queryStudyPlanCatalogExist(catalogId,employeeId);
				//判断学员历史是否收藏过此课程，如果收藏过并且已经取消收藏，则把数据还原
				if(planListExist.size()>0){
					businessService.updateStudyPlanCatalog(planListExist.get(0).getId());
					return ok("收藏成功");
				}else{
					businessService.saveStudyPlanCatalog(catalogId,employeeId);
					return ok("收藏成功");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: findStudyPlanList
	 * @Description: (查询学员学习计划列表)
	 * @param employeeId 学员ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findStudyPlanList")
	public JSONObject findStudyPlanList(Page page,Long employeeId,String client,String version) {
		try {
			List<CatalogCourseList> planList = null;
			planList = businessService.queryStudyPlanList(page,employeeId);
			//循环遍历集合查询是否是学员的推荐课程和计划学习课程
			for(int i=0;i<planList.size();i++){
				//是否推荐
				if(businessService.queryRecommend(planList.get(i).getId(),employeeId).size()>0){
					Long ifRecommend = (long) 1;
					planList.get(i).setIfRecommend(ifRecommend);
				}else{
					Long ifRecommend = (long) 0;
					planList.get(i).setIfRecommend(ifRecommend);
				}
				//是否在学习计划
				if(businessService.queryStudyPlan(planList.get(i).getId(),employeeId).size()>0){
					Long ifStudyPlan = (long) 1;
					planList.get(i).setIfStudyPlan(ifStudyPlan);
				}else{
					Long ifStudyPlan = (long) 0;
					planList.get(i).setIfStudyPlan(ifStudyPlan);
				}
				//判断课程包管理的试卷是否已经回答过
				if(businessService.queryAnswerHistory(planList.get(i).getExamId(),employeeId).size()>0){
					planList.get(i).setTestMark("1");
				}else{
					planList.get(i).setTestMark("0");
				}
				//根据版本号判断是否加密PDF地址
//				if(!("1.6.0").equals(version) && !("1.5.9").equals(version)){
//					planList.get(i).setAvatar(commonService.appenUrl(planList.get(i).getAvatar()));
//					planList.get(i).setPdfAddress(commonService.appenUrl(planList.get(i).getPdfAddress()));
//				}else{
					String content =planList.get(i).getPdfAddress();
					String playAddress = planList.get(i).getPlayAddress();
					//随机生成字母和数字的组合加密录音文件地址
					String pwd1 = PasswdEncryption.getStringRandom();
					StringBuffer stringBuffer1 = new StringBuffer(commonService.appenUrl(playAddress));
					stringBuffer1.insert(50, pwd1).toString();
					planList.get(i).setPlayAddress(stringBuffer1.toString().toUpperCase());
					//加密PlayAddressM3u8
//					String playAddress1 = planList.get(i).getPlayAddressM3u8();
//					String pwd2 = PasswdEncryption.getStringRandom();
//					StringBuffer stringBuffer2 = new StringBuffer(commonService.appenUrl(playAddress1));
//					stringBuffer2.insert(50, pwd2).toString();
//					planList.get(i).setPlayAddressM3u8(stringBuffer2.toString().toUpperCase());
					planList.get(i).setAvatar(commonService.appenUrl(planList.get(i).getAvatar()));
					if(!StringUtils.isNullOrEmpty(content)){
						//随机生成字母和数字的组合加密PDF地址
						String pwd = PasswdEncryption.getStringRandom();
						StringBuffer stringBuffer = new StringBuffer(commonService.appenUrl(planList.get(i).getPdfAddress()));
						stringBuffer.insert(50, pwd).toString();
						planList.get(i).setPdfAddress(stringBuffer.toString().toUpperCase());
//						catalogList.get(i).setAvatar(stringBuffer.toString().toUpperCase());
					}else{
						planList.get(i).setPdfAddress(commonService.appenUrl(content));
					}
				}
//			}
			return ok("查询成功",planList);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: queryStudyFinishedList
	 * @Description: (查询学员已经完成学习的课程列表)
	 * @param employeeId 学员ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryStudyFinishedList")
	public JSONObject queryStudyFinishedList(Page page,String employeeId,String client,String version,String type,String sign,String deviceId) {
		try {
			List<CatalogCourseList> planList = null;
			if(("2.0.5").equals(version) || ("2.0.1").equals(version)||("2.0.4").equals(version)){
				if(StringUtils.isNullOrEmpty(employeeId) || StringUtils.isNullOrEmpty(deviceId) || StringUtils.isNullOrEmpty(sign)){
					return status(99,"参数错误");
				}
				//解密
				UpAES upAES = new UpAES();
//				upAES.entry("1");
//				String newEmployeeId = upAES.disEntry(employeeId);
//				String newDeviceId =upAES.disEntry(deviceId);
				//重新赋值deviceId
				String newEmployeeId = upAES.disEntry(employeeId);
				String newDeviceId =upAES.disEntry(deviceId);
				System.out.println("解密后的employeeId："+newEmployeeId);
				System.out.println("解密后的deviceId："+newDeviceId);
				Long employeeId1 = Long.parseLong(newEmployeeId);
				String newSign = PasswdEncryption.MD5(newEmployeeId+newDeviceId);
				newSign= newSign.substring(5, newSign.length()-5);
				System.out.println("得到的新Sign"+newSign);
				if(newSign.equals(sign)){
					planList = businessService.queryStudyFinishedList(page,employeeId1,type);
					//循环遍历集合查询是否是学员的推荐课程和计划学习课程
					for(int i=0;i<planList.size();i++){
						//是否推荐
						if(businessService.queryRecommend(planList.get(i).getId(),employeeId1).size()>0){
							Long ifRecommend = (long) 1;
							planList.get(i).setIfRecommend(ifRecommend);
						}else{
							Long ifRecommend = (long) 0;
							planList.get(i).setIfRecommend(ifRecommend);
						}
						//是否在学习计划
						if(businessService.queryStudyPlan(planList.get(i).getId(),employeeId1).size()>0){
							Long ifStudyPlan = (long) 1;
							planList.get(i).setIfStudyPlan(ifStudyPlan);
						}else{
							Long ifStudyPlan = (long) 0;
							planList.get(i).setIfStudyPlan(ifStudyPlan);
						}
						//判断课程包管理的试卷是否已经回答过
						if(businessService.queryAnswerHistory(planList.get(i).getExamId(),employeeId1).size()>0){
							planList.get(i).setTestMark("1");
						}else{
							planList.get(i).setTestMark("0");
						}
						planList.get(i).setAvatar(commonService.appenUrl(planList.get(i).getAvatar()));
						planList.get(i).setPlayAddress(commonService.appenUrl(planList.get(i).getPlayAddress()));
						planList.get(i).setPdfAddress(commonService.appenUrl(planList.get(i).getPdfAddress()));
						}
//					}
					return okNew("查询成功",planList);
				}else{
					return status(97,"sign验证出错");
				}
			}else{
				planList = businessService.queryStudyFinishedList(page,Long.parseLong(employeeId),"0");
				//循环遍历集合查询是否是学员的推荐课程和计划学习课程
				for(int i=0;i<planList.size();i++){
					//是否推荐
					if(businessService.queryRecommend(planList.get(i).getId(),Long.parseLong(employeeId)).size()>0){
						Long ifRecommend = (long) 1;
						planList.get(i).setIfRecommend(ifRecommend);
					}else{
						Long ifRecommend = (long) 0;
						planList.get(i).setIfRecommend(ifRecommend);
					}
					//是否在学习计划
					if(businessService.queryStudyPlan(planList.get(i).getId(),Long.parseLong(employeeId)).size()>0){
						Long ifStudyPlan = (long) 1;
						planList.get(i).setIfStudyPlan(ifStudyPlan);
					}else{
						Long ifStudyPlan = (long) 0;
						planList.get(i).setIfStudyPlan(ifStudyPlan);
					}
					//判断课程包管理的试卷是否已经回答过
					if(businessService.queryAnswerHistory(planList.get(i).getExamId(),Long.parseLong(employeeId)).size()>0){
						planList.get(i).setTestMark("1");
					}else{
						planList.get(i).setTestMark("0");
					}
					planList.get(i).setAvatar(commonService.appenUrl(planList.get(i).getAvatar()));
//					planList.get(i).setPdfAddress(commonService.appenUrl(planList.get(i).getPdfAddress()));
					//根据版本号判断是否加密PDF地址
//					if(!("1.6.0").equals(version) && !("1.5.9").equals(version)){
//						planList.get(i).setAvatar(commonService.appenUrl(planList.get(i).getAvatar()));
//						planList.get(i).setPdfAddress(commonService.appenUrl(planList.get(i).getPdfAddress()));
//					}else{
						String content =planList.get(i).getPdfAddress();
						String playAddress = planList.get(i).getPlayAddress();
						//随机生成字母和数字的组合加密录音文件地址
						String pwd1 = PasswdEncryption.getStringRandom();
						StringBuffer stringBuffer1 = new StringBuffer(commonService.appenUrl(playAddress));
						stringBuffer1.insert(50, pwd1).toString();
						planList.get(i).setPlayAddress(stringBuffer1.toString().toUpperCase());
						//加密PlayAddressM3u8
//						String playAddress1 = planList.get(i).getPlayAddressM3u8();
//						String pwd2 = PasswdEncryption.getStringRandom();
//						StringBuffer stringBuffer2 = new StringBuffer(commonService.appenUrl(playAddress1));
//						stringBuffer2.insert(50, pwd2).toString();
//						planList.get(i).setPlayAddressM3u8(stringBuffer2.toString().toUpperCase());
						if(!StringUtils.isNullOrEmpty(content)){
							//随机生成字母和数字的组合加密PDF地址
							String pwd = PasswdEncryption.getStringRandom();
							StringBuffer stringBuffer = new StringBuffer(commonService.appenUrl(planList.get(i).getPdfAddress()));
							stringBuffer.insert(50, pwd).toString();
							planList.get(i).setPdfAddress(stringBuffer.toString().toUpperCase());
//							catalogList.get(i).setAvatar(stringBuffer.toString().toUpperCase());
						}else{
							planList.get(i).setPdfAddress(commonService.appenUrl(content));
						}
					}
//				}
				return ok("查询成功",planList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: updateRecommend
	 * @Description: (修改课程推荐状态)
	 * @param employeeId 学员ID
	 * @param courseId 课程ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateRecommend")
	public JSONObject updateRecommend(Long employeeId,Long courseId,String client,String version) {
		try {
			List<Recommend> recommendList = null;
			recommendList = businessService.queryRecommend(courseId,employeeId);
			if(recommendList.size()>0){
				businessService.updateRecommend(recommendList.get(0).getId());
//				return ok("取消推荐成功");
				return status(300, "取消推荐成功");
			}else{
				List<Recommend> recommendListExist = null;
				recommendListExist = businessService.queryRecommendExist(courseId,employeeId);
				if(recommendListExist.size()>0){
					businessService.updateRecommend(recommendListExist.get(0).getId());
					return ok("推荐成功");
				}else{
					businessService.saveRecommend(courseId,employeeId);
					return ok("推荐成功");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: findKeyWordsList
	 * @Description: (查询所有搜索关键字)
	 * @param page 分页
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findKeyWordsList")
	public JSONObject findKeyWordsList(Page page,String client,String version,String employeeId) {
		try {
			try {
				List<KeyWords> keyWordsList = null;
				String ifBusiness = "1";
				keyWordsList=businessService.findKeyWordsList(page);
				if(!"null".equals(employeeId) && !StringUtils.isNullOrEmpty(employeeId)){
					List<MemberRecord> recordList = businessService.findMemberRecordList(employeeId.toString());
					if(recordList.size()>0){
						ifBusiness="0";
					}else{
						//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
						 if(businessService.findMemberRecordListByEmployeeId(employeeId.toString()).size()>0){
							 ifBusiness="99999";
						 }else{
							 ifBusiness="1";
						 }
					}
					for(int i=0;i<keyWordsList.size();i++){
						keyWordsList.get(i).setIfBusiness(ifBusiness);
					}
				}
				return ok("查询成功", keyWordsList);
			} catch (Exception e) {
				e.printStackTrace();
				return error(APP_SYSTEM_ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: findCourseListByKeyWords
	 * @Description: (根据关键字查询课程)
	 * @param keyWords 关键字
	 * @param employeeId 企业学员ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findCourseListByKeyWords")
	public JSONObject findCourseListByKeyWords(String keyWords,String employeeId,Page page,String client,String version,String deviceId,String sign) {
		if(("2.0.5").equals(version) || ("2.0.1").equals(version)||("2.0.4").equals(version)){
			if(StringUtils.isNullOrEmpty(employeeId) || StringUtils.isNullOrEmpty(sign) || StringUtils.isNullOrEmpty(deviceId)){
				return status(99,"参数缺失");
			}
			//解密
			UpAES upAES = new UpAES();
//			upAES.entry("1");
			
			String newEmployeeId = upAES.disEntry(employeeId);
			String newDeviceId =upAES.disEntry(deviceId.toString());
			System.out.println("解密后的employeeId："+newEmployeeId);
			System.out.println("解密后的deviceId："+newDeviceId);
			//重新赋值deviceId
			String newSign = PasswdEncryption.MD5(newEmployeeId+newDeviceId);
			newSign= newSign.substring(5, newSign.length()-5);
			System.out.println("得到的新Sign:"+newSign);
			if(newSign.equals(sign)){
				try {
					List<CatalogCourseList> courseList = null;
					courseList=businessService.findCourseListByKeyWords(keyWords,page);
					//循环遍历集合查询是否是学员的推荐课程和计划学习课程
					for(int i=0;i<courseList.size();i++){
						//是否推荐
						if(businessService.queryRecommend(courseList.get(i).getId(),Long.parseLong(newEmployeeId)).size()>0){
							Long ifRecommend = (long) 1;
							courseList.get(i).setIfRecommend(ifRecommend);
						}else{
							Long ifRecommend = (long) 0;
							courseList.get(i).setIfRecommend(ifRecommend);
						}
						//是否在学习计划
						if(businessService.queryStudyPlan(courseList.get(i).getId(),Long.parseLong(newEmployeeId)).size()>0){
							Long ifStudyPlan = (long) 1;
							courseList.get(i).setIfStudyPlan(ifStudyPlan);
						}else{
							Long ifStudyPlan = (long) 0;
							courseList.get(i).setIfStudyPlan(ifStudyPlan);
						}
						//判断课程包管理的试卷是否已经回答过
						if(businessService.queryAnswerHistory(courseList.get(i).getExamId(),Long.parseLong(newEmployeeId)).size()>0){
							courseList.get(i).setTestMark("1");
						}else{
							courseList.get(i).setTestMark("0");
						}
						courseList.get(i).setAvatar(commonService.appenUrl(courseList.get(i).getAvatar()));
						String content =courseList.get(i).getPdfAddress();
						String playAddress = courseList.get(i).getPlayAddress();
						courseList.get(i).setPlayAddress(commonService.appenUrl(playAddress));
						courseList.get(i).setPdfAddress(commonService.appenUrl(courseList.get(i).getPdfAddress()));
						}
					if(courseList.size()>0){
						//判断t_business_member_record表中是否有效期内的好多课会员数据存在
						List<MemberRecord> recordList = businessService.findMemberRecordList(newEmployeeId);
						if(recordList.size()>0){
							courseList.get(0).setIfBusiness("0");
						}else{
							//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
							 if(businessService.findMemberRecordListByEmployeeId(newEmployeeId).size()>0){
								 courseList.get(0).setIfBusiness("999999");
							 }else{
								 courseList.get(0).setIfBusiness("1");
							 }
						}
					}
					return okNew("查询成功", courseList);
			} catch (Exception e) {
				e.printStackTrace();
				return error(APP_SYSTEM_ERROR);
			}
			}else{
				return status(97,"sign验证出错");
			}
		}else{
			try {
				List<CatalogCourseList> courseList = null;
				courseList=businessService.findCourseListByKeyWords(keyWords,page);
				//循环遍历集合查询是否是学员的推荐课程和计划学习课程
				for(int i=0;i<courseList.size();i++){
					//是否推荐
					if(businessService.queryRecommend(courseList.get(i).getId(),Long.parseLong(employeeId)).size()>0){
						Long ifRecommend = (long) 1;
						courseList.get(i).setIfRecommend(ifRecommend);
					}else{
						Long ifRecommend = (long) 0;
						courseList.get(i).setIfRecommend(ifRecommend);
					}
					//是否在学习计划
					if(businessService.queryStudyPlan(courseList.get(i).getId(),Long.parseLong(employeeId)).size()>0){
						Long ifStudyPlan = (long) 1;
						courseList.get(i).setIfStudyPlan(ifStudyPlan);
					}else{
						Long ifStudyPlan = (long) 0;
						courseList.get(i).setIfStudyPlan(ifStudyPlan);
					}
					//判断课程包管理的试卷是否已经回答过
					if(businessService.queryAnswerHistory(courseList.get(i).getExamId(),Long.parseLong(employeeId)).size()>0){
						courseList.get(i).setTestMark("1");
					}else{
						courseList.get(i).setTestMark("0");
					}
					courseList.get(i).setAvatar(commonService.appenUrl(courseList.get(i).getAvatar()));
					//根据版本号判断是否加密PDF地址
//					if(!("1.6.0").equals(version) && !("1.5.9").equals(version)){
//						courseList.get(i).setAvatar(commonService.appenUrl(courseList.get(i).getAvatar()));
//						courseList.get(i).setPdfAddress(commonService.appenUrl(courseList.get(i).getPdfAddress()));
//					}else{
						String content =courseList.get(i).getPdfAddress();
						String playAddress = courseList.get(i).getPlayAddress();
						//随机生成字母和数字的组合加密录音文件地址
						String pwd1 = PasswdEncryption.getStringRandom();
						StringBuffer stringBuffer1 = new StringBuffer(commonService.appenUrl(playAddress));
						stringBuffer1.insert(50, pwd1).toString();
						courseList.get(i).setPlayAddress(stringBuffer1.toString().toUpperCase());
						//加密PlayAddressM3u8
//						String playAddress1 = courseList.get(i).getPlayAddressM3u8();
//						String pwd2 = PasswdEncryption.getStringRandom();
//						StringBuffer stringBuffer2 = new StringBuffer(commonService.appenUrl(playAddress1));
//						stringBuffer2.insert(50, pwd2).toString();
//						courseList.get(i).setPlayAddressM3u8(stringBuffer2.toString().toUpperCase());

						if(!StringUtils.isNullOrEmpty(content)){
							//随机生成字母和数字的组合加密PDF地址
							String pwd = PasswdEncryption.getStringRandom();
							StringBuffer stringBuffer = new StringBuffer(commonService.appenUrl(courseList.get(i).getPdfAddress()));
							stringBuffer.insert(50, pwd).toString();
							courseList.get(i).setPdfAddress(stringBuffer.toString().toUpperCase());
//							catalogList.get(i).setAvatar(stringBuffer.toString().toUpperCase());
						}else{
							courseList.get(i).setPdfAddress(commonService.appenUrl(content));
						}
					}
				if(courseList.size()>0){
					//判断t_business_member_record表中是否有效期内的好多课会员数据存在
					List<MemberRecord> recordList = businessService.findMemberRecordList(employeeId.toString());
					if(recordList.size()>0){
						courseList.get(0).setIfBusiness("0");
					}else{
						//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
						 if(businessService.findMemberRecordListByEmployeeId(employeeId.toString()).size()>0){
							 courseList.get(0).setIfBusiness("999999");
						 }else{
							 courseList.get(0).setIfBusiness("1");
						 }
					}
				}
//				}
				return ok("查询成功", courseList);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
		}
		
	}
	/**
	 * @Title: findTeachersList
	 * @Description: (查询所有讲师)
	 * @param page 分页
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findTeachersList")
	public JSONObject findTeachersList(String client,String version) {
		try {
			try {
				List<CatalogCourseList> teachersList = null;
				teachersList=businessService.findTeachersList();
				return ok("查询成功", teachersList);
			} catch (Exception e) {
				e.printStackTrace();
				return error(APP_SYSTEM_ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: findCourseListByTeacher
	 * @Description: (根据讲师姓名查询课程)
	 * @param teacher 讲师姓名
	 * @param employeeId 企业学员ID
	 * @param page 分页
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findCourseListByTeacher")
	public JSONObject findCourseListByTeacher(String teacher,String employeeId,Page page,String client,String version,String sign,String deviceId) {
		if(("2.0.5").equals(version) || ("2.0.1").equals(version)||("2.0.4").equals(version)){
			if(StringUtils.isNullOrEmpty(employeeId) || StringUtils.isNullOrEmpty(sign) || StringUtils.isNullOrEmpty(deviceId)){
				return status(99,"参数缺失");
			}
			//解密
			UpAES upAES = new UpAES();
//			upAES.entry("1");
			String newEmployeeId = upAES.disEntry(employeeId);
			String newDeviceId =upAES.disEntry(deviceId.toString());
			System.out.println("解密后的employeeId："+newEmployeeId);
			System.out.println("解密后的deviceId："+newDeviceId);
			//重新赋值deviceId
			String newSign = PasswdEncryption.MD5(newEmployeeId+newDeviceId);
			newSign= newSign.substring(5, newSign.length()-5);
			System.out.println("得到的新Sign:"+newSign);
			if(newSign.equals(sign)){
				try {
					List<CatalogCourseList> courseList = null;
					courseList=businessService.findCourseListByTeacher(teacher,page);
					//循环遍历集合查询是否是学员的推荐课程和计划学习课程
					for(int i=0;i<courseList.size();i++){
						//是否推荐
						if(businessService.queryRecommend(courseList.get(i).getId(),Long.parseLong(newEmployeeId)).size()>0){
							Long ifRecommend = (long) 1;
							courseList.get(i).setIfRecommend(ifRecommend);
						}else{
							Long ifRecommend = (long) 0;
							courseList.get(i).setIfRecommend(ifRecommend);
						}
						//是否在学习计划
						if(businessService.queryStudyPlan(courseList.get(i).getId(),Long.parseLong(newEmployeeId)).size()>0){
							Long ifStudyPlan = (long) 1;
							courseList.get(i).setIfStudyPlan(ifStudyPlan);
						}else{
							Long ifStudyPlan = (long) 0;
							courseList.get(i).setIfStudyPlan(ifStudyPlan);
						}
						//判断课程包管理的试卷是否已经回答过
						if(businessService.queryAnswerHistory(courseList.get(i).getExamId(),Long.parseLong(newEmployeeId)).size()>0){
							courseList.get(i).setTestMark("1");
						}else{
							courseList.get(i).setTestMark("0");
						}
						courseList.get(i).setAvatar(commonService.appenUrl(courseList.get(i).getAvatar()));
						courseList.get(i).setAvatar(commonService.appenUrl(courseList.get(i).getAvatar()));
						courseList.get(i).setPdfAddress(commonService.appenUrl(courseList.get(i).getPdfAddress()));
					}
					if(courseList.size()>0){
						//判断t_business_member_record表中是否有效期内的好多课会员数据存在
						List<MemberRecord> recordList = businessService.findMemberRecordList(newEmployeeId);
						if(recordList.size()>0){
							courseList.get(0).setIfBusiness("0");
						}else{
							//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
							 if(businessService.findMemberRecordListByEmployeeId(newEmployeeId).size()>0){
								 courseList.get(0).setIfBusiness("999999");
							 }else{
								 courseList.get(0).setIfBusiness("1");
							 }
						}
					}
					return okNew("查询成功", courseList);
				} catch (Exception e) {
					e.printStackTrace();
					return error(APP_SYSTEM_ERROR);
				}
			}else{
				return status(97,"sign验证出错");
			}
		}else{
			try {
				List<CatalogCourseList> courseList = null;
				courseList=businessService.findCourseListByTeacher(teacher,page);
				//循环遍历集合查询是否是学员的推荐课程和计划学习课程
				for(int i=0;i<courseList.size();i++){
					//是否推荐
					if(businessService.queryRecommend(courseList.get(i).getId(),Long.parseLong(employeeId)).size()>0){
						Long ifRecommend = (long) 1;
						courseList.get(i).setIfRecommend(ifRecommend);
					}else{
						Long ifRecommend = (long) 0;
						courseList.get(i).setIfRecommend(ifRecommend);
					}
					//是否在学习计划
					if(businessService.queryStudyPlan(courseList.get(i).getId(),Long.parseLong(employeeId)).size()>0){
						Long ifStudyPlan = (long) 1;
						courseList.get(i).setIfStudyPlan(ifStudyPlan);
					}else{
						Long ifStudyPlan = (long) 0;
						courseList.get(i).setIfStudyPlan(ifStudyPlan);
					}
					//判断课程包管理的试卷是否已经回答过
					if(businessService.queryAnswerHistory(courseList.get(i).getExamId(),Long.parseLong(employeeId)).size()>0){
						courseList.get(i).setTestMark("1");
					}else{
						courseList.get(i).setTestMark("0");
					}
					courseList.get(i).setAvatar(commonService.appenUrl(courseList.get(i).getAvatar()));
					//根据版本号判断是否加密PDF地址
//					if(!("1.6.0").equals(version) && !("1.5.9").equals(version)){
//						courseList.get(i).setAvatar(commonService.appenUrl(courseList.get(i).getAvatar()));
//						courseList.get(i).setPdfAddress(commonService.appenUrl(courseList.get(i).getPdfAddress()));
//					}else{
						String content =courseList.get(i).getPdfAddress();
						String playAddress = courseList.get(i).getPlayAddress();
						//随机生成字母和数字的组合加密录音文件地址
						String pwd1 = PasswdEncryption.getStringRandom();
						StringBuffer stringBuffer1 = new StringBuffer(commonService.appenUrl(playAddress));
						stringBuffer1.insert(50, pwd1).toString();
						courseList.get(i).setPlayAddress(stringBuffer1.toString().toUpperCase());
						//加密PlayAddressM3u8
//						String playAddress1 = courseList.get(i).getPlayAddressM3u8();
//						String pwd2 = PasswdEncryption.getStringRandom();
//						StringBuffer stringBuffer2 = new StringBuffer(commonService.appenUrl(playAddress1));
//						stringBuffer2.insert(50, pwd2).toString();
//						courseList.get(i).setPlayAddressM3u8(stringBuffer2.toString().toUpperCase());
						if(!StringUtils.isNullOrEmpty(content)){
							//随机生成字母和数字的组合加密PDF地址
							String pwd = PasswdEncryption.getStringRandom();
							StringBuffer stringBuffer = new StringBuffer(commonService.appenUrl(courseList.get(i).getPdfAddress()));
							stringBuffer.insert(50, pwd).toString();
							courseList.get(i).setPdfAddress(stringBuffer.toString().toUpperCase());
//							catalogList.get(i).setAvatar(stringBuffer.toString().toUpperCase());
						}else{
							courseList.get(i).setPdfAddress(commonService.appenUrl(content));
						}
					}
				if(courseList.size()>0){
					//判断t_business_member_record表中是否有效期内的好多课会员数据存在
					List<MemberRecord> recordList = businessService.findMemberRecordList(employeeId.toString());
					if(recordList.size()>0){
						courseList.get(0).setIfBusiness("0");
					}else{
						//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
						 if(businessService.findMemberRecordListByEmployeeId(employeeId.toString()).size()>0){
							 courseList.get(0).setIfBusiness("999999");
						 }else{
							 courseList.get(0).setIfBusiness("1");
						 }
					}
				}
				
//				}
				return ok("查询成功", courseList);
			} catch (Exception e) {
				e.printStackTrace();
				return error(APP_SYSTEM_ERROR);
			}
		}
			
	}
	/**
	 * @Title: findEmployeeDetail 
	 * @Description: (查询学员详细信息)
	 * @param employeeId 企业学员ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findEmployeeDetail")
	public JSONObject findEmployeeDetail(String employeeId,String client,String version,String deviceId,String sign){
		try{
			if(("2.0.5").equals(version) || ("2.0.1").equals(version)||("2.0.4").equals(version)){
				if(StringUtils.isNullOrEmpty(employeeId) || StringUtils.isNullOrEmpty(deviceId) || StringUtils.isNullOrEmpty(sign)){
					return status(99,"参数错误");
				}
				//解密
				UpAES upAES = new UpAES();
				String newEmployeeId = upAES.disEntry(employeeId);
				String newDeviceId =upAES.disEntry(deviceId.toString());
				System.out.println("解密后的employeeId："+newEmployeeId);
				System.out.println("解密后的deviceId："+newDeviceId);
				//重新赋值deviceId
				String newSign = PasswdEncryption.MD5(newEmployeeId+newDeviceId);
				newSign= newSign.substring(5, newSign.length()-5);
				System.out.println("得到的新Sign"+newSign);
				if(newSign.equals(sign)){
					Long employeeId1 = Long.parseLong(newEmployeeId);
					Employee employee1 = businessService.findEmployeeDetail(employeeId1);
					if(employee1==null){
						return status(800, "用户账号已失效");
					}
					if(employeeId1!=33){
						//查询最近一次登录的信息
						LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employeeId1);
						//查询学员此种设备、此个设备ID上次登录的明细
						LoginDetail loginDetail = memberService.findBusinessLoginDetail(employeeId1,client,newDeviceId);
						if(loginDetail!=null){
							Long detailId = loginDetail.getId();
							Long lastId = lastLogin.getId();
							//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，如果最后一次的大于本机登陆的，那么就是在异地登录了
							if(lastId>detailId){
								SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
							}
						}
					}
					//判断t_business_member_record表中是否有效期内的好多课会员数据存在
					List<MemberRecord> recordList = businessService.findMemberRecordList(employeeId1.toString());
					if(recordList.size()>0){
						employee1.setIfBusiness(0L);
					}else{
						List<MemberRecord> recordCatalogList = businessService.findMemberRecordListByEmployeeId(employeeId1.toString());
						//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
						 if(recordCatalogList.size()>0){
							 //不是年费会员取出购买的系列课ID
							 for(int k=0;k<recordCatalogList.size();k++){
								 //循环遍历取出课程包ID
								 String catalogId = recordCatalogList.get(k).getCatalogId();
								 //购买时间
								 String buyDate = recordCatalogList.get(k).getCreateAt();
								 //根据课程包ID查询课程包信息
								 List<Catalog> bCatalogList =businessService.findParentCatalogById(Long.parseLong(catalogId));
								 if(bCatalogList.size()>0){
									 //取出bCatalogList展示形式
									 String flag = bCatalogList.get(0).getFlag();
									 if("0".equals(flag)){
											//查询学习过的课程Id
											List<StudyDetail> b = businessService.getHaveStudyCourseId(employeeId1);
											String resultb = "";
											if(b.size()>0){
												for(int i=0;i<b.size();i++){
													StudyDetail ob =  b.get(i);
													resultb += "'"+ob.getCourseId().toString()+"',";
												}
												resultb = resultb.substring(1,resultb.length()-2);
											}
//											recommendCourseList=businessService.getRecommendCourseList(employeeId,resulta,resultb);
											//根据包ID和学员购买时间判断课程开始时间在购买时间之后的课程查询课程包下面包含的课程列表
											employee1.setBuyCount(2L);
											employee1.setUpdateCount(employeeId1);
//											List<CatalogCourseList> courseList = businessService.getCatalogCourseListByBuyDate(catalogId,buyDate,resultb);
//											employee1.setUpdateCount(courseList.size());
									 }else{
										//查询学习过的课程Id集合
										List<StudyDetail> b = businessService.getHaveStudyCourseId(employeeId1);
										String resultb = "";
										if(b.size()>0){
											for(int i=0;i<b.size();i++){
												StudyDetail ob =  b.get(i);
												resultb += "'"+ob.getCourseId().toString()+"',";
											}
											resultb = resultb.substring(1,resultb.length()-2);
										}
										employee1.setBuyCount(2L);
										employee1.setUpdateCount(employeeId1);
										 //根据课程包ID查询二级目录下的课程数量
//										List<CatalogCourseList>  courseList = businessService.getCatalogCourseListByBuyDate(catalogId,buyDate,resultb);
//										employee1.setUpdateCount(courseList.size());
									 }
								 }
							 }
							 employee1.setIfBusiness(999999L);
						 }else{
							 employee1.setIfBusiness(1L);
						 }
					}
					//完成的课程数量
//					List<CatalogCourseList> list=businessService.queryStudyFinishedList(employeeId1);
					//收藏的课程数量
//					List<CatalogCourseList> planlist = businessService.queryStudyPlanList(employeeId1);
					//收藏的课程包数量
//					List<Catalog> collectionCatalogList = businessService.queryCollectionCatalogList(employeeId);
					//学习总时长
//					int studyLength = businessService.findEmployeeStudyTimeCount(employeeId1);
					int studyLength = businessService.findEmployeeStudyTimeCountNew(employeeId1);
					//查询学员学习时长目前所在的名次位置
					float persent =0;
					//获取学员成绩的排名
//					String rowNo1 = businessService.findEmployeeStudyTimeNo(employeeId1);
					String rowNo1 = businessService.findEmployeeStudyTimeNoNew(employeeId1);
					if(StringUtils.isNullOrEmpty(rowNo1)){
						rowNo1="0";
					}
					int rowNo = Integer.parseInt(rowNo1) + 0;
					//获取试题总的参考人数
//					int ticketCount = businessService.findEmployeeStudyCount(employeeId1);
					int ticketCount = businessService.findEmployeeStudyCountNew(employeeId1);
					//取学习时长的最长和最短记录
					//取系列考题的最高分数和低分
//					String timeMax1 = businessService.findStudyTimeMax(employeeId1);
					String timeMax1 = businessService.findStudyTimeMaxNew(employeeId1);
					int timeMax=0;
					if(!StringUtils.isNullOrEmpty(timeMax1)){
						timeMax = Integer.parseInt(timeMax1);
					}
					
//					String  timeMin1 = businessService.findStudyTimeMin(employeeId1);
					String  timeMin1 = businessService.findStudyTimeMinNew(employeeId1);
					int timeMin = 0;
					if(!StringUtils.isNullOrEmpty(timeMin1)){
						timeMin = Integer.parseInt(timeMin1);
					}
					if(rowNo == ticketCount){
						persent = 100;
					}else{
						//获取此学员成绩所在的范围
						if(timeMax == studyLength){
							persent = 100;
						}else if(timeMin == studyLength){
							persent = 0;
						}else{
							persent = (rowNo*100/ticketCount);
						}
					}
					Employee employee = new Employee();
					employee.setStudyPlanCount(businessService.findEmployeePlanCount(employeeId1));
					employee.setStudyTimeCount(studyLength);
					employee.setId(employee1.getId());
					employee.setEmployeeId(employee1.getId());
					employee.setAvatar(commonService.appenUrl(employee1.getAvatar()));
					employee.setMobile(employee1.getMobile());
					employee.setName(employee1.getName());
					employee.setSex(employee1.getSex());
					employee.setBusinessId(employee1.getBusinessId());
					employee.setBusinessName(employee1.getBusinessName());
					employee.setCommodityId(employee1.getCommodityId());
					employee.setIfBusiness(employee1.getIfBusiness());
					employee.setStudyTimePersent(persent);
					if(rowNo==0){
						employee.setStudyTimeRanking(rowNo);
					}else{
						employee.setStudyTimeRanking(ticketCount-rowNo+1);
					}
					//完成的课程数量
//					employee.setFinishedCount(list.size());
					//收藏的课程数量
//					employee.setCommentCount(planlist.size());
					//收藏的课程包数量
//					employee.setCollectionCatalogCount(collectionCatalogList.size());
					//判断用户是否为自注册用户，如果是，则查询会员过期日期
					if(employee1.getIfBusiness()==1){
					    MemberPayInfo payInfo = businessService.queryMemberPayByEmployeeId(employeeId1);
					    if(payInfo!=null){
					    	Long monthCount = businessService.queryMonthCountByEmployeeId(employeeId1);
					    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//					    	Date now = sdf.parse(payInfo.getCreateAt());
					    	Calendar calendar = Calendar.getInstance();
					    	calendar.setTime(payInfo.getCreateAt());
					    	calendar.add(Calendar.MONTH,  Integer.valueOf(monthCount.toString()));
					    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					    	System.out.println(sdf.format(calendar.getTime()));
					    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					    	Date now = new Date();
					    	Date dateTime1 = dateFormat.parse(df.format(now));
//					    	//会员过期时间
					        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
					        int i = dateTime1.compareTo(dateTime2);  
					        System.out.println(i < 0);
					        //是否过期
					        if(i < 0){
					        	employee.setOverdueDate(sdf.format(calendar.getTime()));
					        }else{
					        	employee.setOverdueDate(null);
					        }
					    }else{
					    	employee.setOverdueDate(null);
					    }
					}else{
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						List<MemberRecord> rlist = businessService.findMemberRecordListByEmployeeId(employeeId1.toString());
						if(recordList.size()>0){
							employee.setOverdueDate(sdf.format(recordList.get(0).getEndDate()));
						}else if(rlist.size()>0){
							employee.setOverdueDate(sdf.format(rlist.get(0).getEndDate()));
						}else{
							employee.setOverdueDate(null);
						}
			        }
					int remainingDate  = 0;
					//求年费会员剩余期限
					if(employee1.getIfBusiness()==0){
						Calendar aCalendar = Calendar.getInstance();
						Calendar bCalendar = Calendar.getInstance();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");                
					    aCalendar.setTime(sdf.parse(employee.getOverdueDate()));
					    Long time1 = aCalendar.getTimeInMillis();
					    int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
					    bCalendar.setTime(new Date());
					    int day2 = bCalendar.get(Calendar.DAY_OF_YEAR);
					    Long time2 = bCalendar.getTimeInMillis();
					    long between_days=(time1-time2)/(1000*3600*24);  
//					       return Integer.parseInt(String.valueOf(between_days));     
					    remainingDate  = Integer.parseInt(String.valueOf(between_days));
					}
					employee.setRemainingDate(remainingDate);
					return okNew("查询成功",employee);
				}else{
					return status(97,"sign验证出错");
				}
			}else{
				Employee employee1 = businessService.findEmployeeDetail(Long.parseLong(employeeId));
				if(employee1==null){
					return status(800, "用户账号已失效");
				}
				if(Long.parseLong(employeeId)!=33){
					//查询最近一次登录的信息
					LoginDetail lastLogin = memberService.findBusinessLoginDetailById(Long.parseLong(employeeId));
					//查询学员此种设备、此个设备ID上次登录的明细
					LoginDetail loginDetail = memberService.findBusinessLoginDetail(Long.parseLong(employeeId),client,deviceId);
					if(loginDetail!=null){
						Long detailId = loginDetail.getId();
						Long lastId = lastLogin.getId();
						//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，如果最后一次的大于本机登陆的，那么就是在异地登录了
						if(lastId>detailId){
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
						}
					}
				}
				//判断t_business_member_record表中是否有效期内的好多课会员数据存在
				List<MemberRecord> recordList = businessService.findMemberRecordList(employeeId);
				if(recordList.size()>0){
					employee1.setIfBusiness(0L);
				}else{
					List<MemberRecord> recordCatalogList = businessService.findMemberRecordListByEmployeeId(employeeId);
					//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
					 if(recordCatalogList.size()>0){
						 //不是年费会员取出购买的系列课ID
						 for(int k=0;k<recordCatalogList.size();k++){
							 //循环遍历取出课程包ID
							 String catalogId = recordCatalogList.get(k).getCatalogId();
							 //购买时间
							 String buyDate = recordCatalogList.get(k).getCreateAt();
							 //根据课程包ID查询课程包信息
							 List<Catalog> bCatalogList =businessService.findParentCatalogById(Long.parseLong(catalogId));
							 if(bCatalogList.size()>0){
								 //取出bCatalogList展示形式
								 String flag = bCatalogList.get(0).getFlag();
								 if("0".equals(flag)){
										//查询学习过的课程Id
										List<StudyDetail> b = businessService.getHaveStudyCourseId(Long.parseLong(employeeId));
										String resultb = "";
										if(b.size()>0){
											for(int i=0;i<b.size();i++){
												StudyDetail ob =  b.get(i);
												resultb += "'"+ob.getCourseId().toString()+"',";
											}
											resultb = resultb.substring(1,resultb.length()-2);
										}
//										recommendCourseList=businessService.getRecommendCourseList(employeeId,resulta,resultb);
										//根据包ID和学员购买时间判断课程开始时间在购买时间之后的课程查询课程包下面包含的课程列表
										employee1.setBuyCount(2L);
										employee1.setUpdateCount(Long.parseLong(employeeId));
//										List<CatalogCourseList> courseList = businessService.getCatalogCourseListByBuyDate(catalogId,buyDate,resultb);
//										employee1.setUpdateCount(courseList.size());
								 }else{
									//查询学习过的课程Id集合
									List<StudyDetail> b = businessService.getHaveStudyCourseId(Long.parseLong(employeeId));
									String resultb = "";
									if(b.size()>0){
										for(int i=0;i<b.size();i++){
											StudyDetail ob =  b.get(i);
											resultb += "'"+ob.getCourseId().toString()+"',";
										}
										resultb = resultb.substring(1,resultb.length()-2);
									}
									employee1.setBuyCount(2L);
									employee1.setUpdateCount(Long.parseLong(employeeId));
									 //根据课程包ID查询二级目录下的课程数量
//									List<CatalogCourseList>  courseList = businessService.getCatalogCourseListByBuyDate(catalogId,buyDate,resultb);
//									employee1.setUpdateCount(courseList.size());
								 }
							 }
						 }
						 employee1.setIfBusiness(999999L);
					 }else{
						 employee1.setIfBusiness(1L);
					 }
				}
				//完成的课程数量
				List<CatalogCourseList> list=businessService.queryStudyFinishedList(Long.parseLong(employeeId));
				//收藏的课程数量
				List<CatalogCourseList> planlist = businessService.queryStudyPlanList(Long.parseLong(employeeId));
				//收藏的课程包数量
//				List<Catalog> collectionCatalogList = businessService.queryCollectionCatalogList(employeeId);
				//学习总时长
				int studyLength = businessService.findEmployeeStudyTimeCount(Long.parseLong(employeeId));
				//查询学员学习时长目前所在的名次位置
				float persent =0;
				//获取学员成绩的排名
				String rowNo1 = businessService.findEmployeeStudyTimeNo(Long.parseLong(employeeId));
				if(StringUtils.isNullOrEmpty(rowNo1)){
					rowNo1="0";
				}
				int rowNo = Integer.parseInt(rowNo1) + 0;
				//获取试题总的参考人数
				int ticketCount = businessService.findEmployeeStudyCount(Long.parseLong(employeeId));
				//取学习时长的最长和最短记录
				//取系列考题的最高分数和低分
				String timeMax1 = businessService.findStudyTimeMax(Long.parseLong(employeeId));
				int timeMax =0;
				if(!StringUtils.isNullOrEmpty(timeMax1)){
					timeMax = Integer.parseInt(timeMax1);
				}
				String timeMin1 = businessService.findStudyTimeMin(Long.parseLong(employeeId));
				int timeMin =0;
				if(!StringUtils.isNullOrEmpty(timeMin1)){
					timeMin = Integer.parseInt(timeMin1);
				}
				if(rowNo == ticketCount){
					persent = 100;
				}else{
					//获取此学员成绩所在的范围
					if(timeMax == studyLength){
						persent = 100;
					}else if(timeMin == studyLength){
						persent = 0;
					}else{
						persent = (rowNo*100/ticketCount);
					}
				}
				Employee employee = new Employee();
				employee.setStudyPlanCount(businessService.findEmployeePlanCount(Long.parseLong(employeeId)));
				employee.setStudyTimeCount(studyLength);
				employee.setId(employee1.getId());
				employee.setAvatar(commonService.appenUrl(employee1.getAvatar()));
				employee.setMobile(employee1.getMobile());
				employee.setName(employee1.getName());
				employee.setSex(employee1.getSex());
				employee.setBusinessId(employee1.getBusinessId());
				employee.setBusinessName(employee1.getBusinessName());
				employee.setCommodityId(employee1.getCommodityId());
				employee.setIfBusiness(employee1.getIfBusiness());
				employee.setStudyTimePersent(persent);
				if(rowNo==0){
					employee.setStudyTimeRanking(rowNo);
				}else{
					employee.setStudyTimeRanking(ticketCount-rowNo+1);
				}
				//完成的课程数量
				employee.setFinishedCount(list.size());
				//收藏的课程数量
				employee.setCommentCount(planlist.size());
				//收藏的课程包数量
//				employee.setCollectionCatalogCount(collectionCatalogList.size());
				//判断用户是否为自注册用户，如果是，则查询会员过期日期
				if(employee1.getIfBusiness()==1){
				    MemberPayInfo payInfo = businessService.queryMemberPayByEmployeeId(Long.parseLong(employeeId));
				    if(payInfo!=null){
				    	Long monthCount = businessService.queryMonthCountByEmployeeId(Long.parseLong(employeeId));
				    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				    	Date now = sdf.parse(payInfo.getCreateAt());
				    	Calendar calendar = Calendar.getInstance();
				    	calendar.setTime(payInfo.getCreateAt());
				    	calendar.add(Calendar.MONTH,  Integer.valueOf(monthCount.toString()));
				    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				    	System.out.println(sdf.format(calendar.getTime()));
				    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				    	Date now = new Date();
				    	Date dateTime1 = dateFormat.parse(df.format(now));
//				    	//会员过期时间
				        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
				        int i = dateTime1.compareTo(dateTime2);  
				        System.out.println(i < 0);
				        //是否过期
				        if(i < 0){
				        	employee.setOverdueDate(sdf.format(calendar.getTime()));
				        }else{
				        	employee.setOverdueDate(null);
				        }
				    }else{
				    	employee.setOverdueDate(null);
				    }
				}else{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					List<MemberRecord> rlist = businessService.findMemberRecordListByEmployeeId(employeeId.toString());
					if(recordList.size()>0){
						employee.setOverdueDate(sdf.format(recordList.get(0).getEndDate()));
					}else if(rlist.size()>0){
						employee.setOverdueDate(sdf.format(rlist.get(0).getEndDate()));
					}else{
						employee.setOverdueDate(null);
					}
					
//					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//					Calendar calendar = Calendar.getInstance();
//					//取账号创建时间
//			    	calendar.setTime(employee1.getCreateAt());
//			    	//账号创建时间加上会员日期为会员过期日期
//			    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(employee1.getExpiryDate().toString()));
//			    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			    	System.out.println(sdf.format(calendar.getTime()));
//			    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			    	Date now = new Date();
//			    	Date dateTime1 = dateFormat.parse(df.format(now));
//		//	    	//会员过期时间
//			        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
//			        int i = dateTime1.compareTo(dateTime2);  
//			        System.out.println(i < 0);
//			        //是否过期
//			        if(i < 0){
//			        	employee.setOverdueDate(sdf.format(calendar.getTime()));
//			        }else{
//			        	employee.setOverdueDate(null);
//			        }
		        }
				return ok("ok",employee);
			}
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * Title: saveBusinessFeedBack 
	 * Description: (保存意见反馈)
	 * 
	 * @return
	 * @author ZhaoXu
	 */
	@ResponseBody
	@RequestMapping("/saveBusinessFeedBack")
	public JSONObject saveBusinessFeedBack(String content, Long employeeId,String client,String version,String platform) {
		try {
			if (StringUtils.isNullOrEmpty(content) || NumberUtils.isNullOrZero(employeeId)) {
				return error("参数缺失");
			}

			BusinessFeedBack feedBack = new BusinessFeedBack();
			feedBack.setEmployeeId(employeeId);
			feedBack.setContent(content);
			feedBack.setVersion(version);
			//设备型号
			feedBack.setPlatform(platform);
			if (businessService.saveBusinessFeedBack(feedBack)) {
				return ok("保存成功");
			}
			return error("保存失败");
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: findOnlineCourseDetailById 
	 * @Description: (根据线上课程ID查询线上课程内容)
	 * @param courseId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findOnlineCourseDetailById")
	public JSONObject findOnlineCourseDetailById(Long courseId,Long employeeId,Integer type,String client,String deviceId,String version){
		try{
			if(NumberUtils.isNullOrZero(courseId)){
				return error("参数缺失");
			}
			if(employeeId!=33){
				//查询最近一次登录的信息
				LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employeeId);
				//查询学员此种设备、此个设备ID上次登录的明细
				LoginDetail loginDetail = memberService.findBusinessLoginDetail(employeeId,client,deviceId);
				if(loginDetail!=null){
					Long detailId = loginDetail.getId();
					Long lastId = lastLogin.getId();
					//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，如果最后一次的大于本机登陆的，那么就是在异地登录了
					if(lastId>detailId){	
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
					}
				}
			}
			CatalogCourseList info = businessService.findOnlineCourseDetailById(courseId);
			if(info!=null){
				if(NumberUtils.isNullOrZero(type)){
					info.setCoursePpt(info.getCoursePpt()+css);
				}
				//是否推荐
				if(businessService.queryRecommend(info.getId(),employeeId).size()>0){
					Long ifRecommend = (long) 1;
					info.setIfRecommend(ifRecommend);
				}else{
					Long ifRecommend = (long) 0;
					info.setIfRecommend(ifRecommend);
				}
				//是否在学习计划
				if(businessService.queryStudyPlan(info.getId(),employeeId).size()>0){
					Long ifStudyPlan = (long) 1;
					info.setIfStudyPlan(ifStudyPlan);
				}else{
					Long ifStudyPlan = (long) 0;
					info.setIfStudyPlan(ifStudyPlan);
				}
				//学习进度
				if(businessService.queryStudyIfFinished(courseId,employeeId).size()>0){
					info.setIsFinished(businessService.queryStudyIfFinished(courseId,employeeId).get(0).getIsFinished());
				}else{
					Long isFinished = (long) 0;
					info.setIfStudyPlan(isFinished);
				}
				info.setAvatar(commonService.appenUrl(info.getAvatar()));
				info.setPdfAddress(commonService.appenUrl(info.getPdfAddress()));
//				if(!StringUtils.isNullOrEmpty(info.getPdfAddress())){
////					info.setPdfAddress(commonService.appenUrl(info.getPdfAddress()));
//					//随机生成字母和数字的组合
//					String pwd = PasswdEncryption.getStringRandom();
//					StringBuffer stringBuffer = new StringBuffer(commonService.appenUrl(info.getPdfAddress()));
//					stringBuffer.insert(50, pwd).toString();
//					info.setPdfAddress(stringBuffer.toString().toUpperCase());
//				}
				//根据版本号判断是否加密PDF地址
//				if(!("1.6.0").equals(version) && !("1.5.9").equals(version)){
//					info.setAvatar(commonService.appenUrl(info.getAvatar()));
//					info.setPdfAddress(commonService.appenUrl(info.getPdfAddress()));
//				}else{
					String content =info.getPdfAddress();
					String playAddress = info.getPlayAddress();
					//随机生成字母和数字的组合加密录音文件地址
					String pwd1 = PasswdEncryption.getStringRandom();
					StringBuffer stringBuffer1 = new StringBuffer(commonService.appenUrl(playAddress));
					stringBuffer1.insert(50, pwd1).toString();
					info.setPlayAddress(stringBuffer1.toString().toUpperCase());
					//加密PlayAddressM3u8
//					String playAddress1 = info.getPlayAddressM3u8();
//					String pwd2 = PasswdEncryption.getStringRandom();
//					StringBuffer stringBuffer2 = new StringBuffer(commonService.appenUrl(playAddress1));
//					stringBuffer2.insert(50, pwd2).toString();
//					info.setPlayAddressM3u8(stringBuffer2.toString().toUpperCase());
					if(!StringUtils.isNullOrEmpty(content)){
						//随机生成字母和数字的组合加密PDF地址
						String pwd = PasswdEncryption.getStringRandom();
						StringBuffer stringBuffer = new StringBuffer(commonService.appenUrl(info.getPdfAddress()));
						stringBuffer.insert(50, pwd).toString();
						info.setPdfAddress(stringBuffer.toString().toUpperCase());
//						catalogList.get(i).setAvatar(stringBuffer.toString().toUpperCase());
					}else{
						info.setPdfAddress(commonService.appenUrl(content));
					}
//				}
				return ok("ok",info);
			}
			return ok("error","查询失败");
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: addMemberPayDetail 
	 * @Description: (添加会员购买记录)
	 * @param employeeId 客户ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addMemberPayDetail")
	public JSONObject addMemberPayDetail(Long price,Long employeeId,Long memberType,String deviceId,String version){
		try{
			if(NumberUtils.isNullOrZero(employeeId)){
				return error("客户ID参数缺失");
			}
			if(NumberUtils.isNullOrZero(price)){
				return error("价格参数缺失");
			}
//			if(!("1.8.4").equals(version)){
//				return error("会员购买功能暂未开通，敬请期待！");
//			}
			if(1==1){
				return error("会员购买功能暂未开通，敬请期待！");
			}
			//如果是游客购买会员，根据设备Id生成用户信息
			if(employeeId==33){
				//根据设备Id查询是否已经注册过
				Employee employee =businessService.findEmployeeDetailByDeviceId(deviceId);
				//如果已经购买过会员，则累加会员时间
				if(employee!=null){
					businessService.addMemberPayDetail(price,employee.getId(),memberType);
					//查询最后一次的订单信息
					MemberPayInfo memberPayInfo = businessService.queryMemberPayDetail(employee.getId());
					return ok("保存订单成功",memberPayInfo);
				}else{
					employee = new Employee();
					employee.setMobile("");
					employee.setPassword(PasswdEncryption.generate("123456"));
					employee.setName("匿名用户");
					employee.setDeviceId(deviceId);
					// 保存默认头像
					employee.setAvatar(commonService.getRandomAvatar());
					memberService.businessRegisterNiMing(employee);
					// 返回登录信息
					businessService.addMemberPayDetail(price,employee.getId(),memberType);
					//查询最后一次的订单信息
					MemberPayInfo memberPayInfo = businessService.queryMemberPayDetail(employee.getId());
					return ok("保存订单成功",memberPayInfo);
				}
			}
			Employee employee = businessService.findEmployeeDetail(employeeId);
			if(employee.getIfBusiness()!=1){
				return error("非注册用户不能购买会员");
			}
			businessService.addMemberPayDetail(price,employeeId,memberType);
			//查询最后一次的订单信息
			MemberPayInfo memberPayInfo = businessService.queryMemberPayDetail(employeeId);
			return ok("保存订单成功",memberPayInfo);
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: updateMemberPay
	 * @Description: (添加会员购买记录)
	 * @param payId 订单ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateMemberPay")
	public JSONObject updateMemberPay(Long payId,Long employeeId){
		try{
			if(businessService.updateMemberPay(payId)){
				//查询最后一次的订单信息
				Employee employee = businessService.findEmployeeDetail(employeeId);
				//判断用户是否为自注册用户，如果是，则查询会员过期日期
				if(employee.getIfBusiness()==1){
				    MemberPayInfo payInfo = businessService.queryMemberPayByEmployeeId(employeeId);
				    if(payInfo!=null){
				    	Long monthCount = businessService.queryMonthCountByEmployeeId(employeeId);
				    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				    	Date now = sdf.parse(payInfo.getCreateAt());
				    	Calendar calendar = Calendar.getInstance();
				    	calendar.setTime(payInfo.getCreateAt());
				    	calendar.add(Calendar.MONTH,  Integer.valueOf(monthCount.toString()));
				    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				    	System.out.println(sdf.format(calendar.getTime()));
				    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				    	Date now = new Date();
				    	Date dateTime1 = dateFormat.parse(df.format(now));
//				    	//会员过期时间
				        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
				        int i = dateTime1.compareTo(dateTime2);  
				        System.out.println(i < 0);
				        //是否过期
				        if(i < 0){
				        	employee.setOverdueDate(sdf.format(calendar.getTime()));
				        }else{
				        	employee.setOverdueDate(null);
					        }
					    }else{
					    	employee.setOverdueDate(null);
					    }
					return ok("更新订单状态成功",employee);
				}else{
					return error("非注册用户");
				}
			}
			return error("更新订单状态失败");
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: saveBusinessScore
	 * @Description: (保存分数)
	 * @param score
	 *            分数
	 * @param unionId
	 *            唯一ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveBusinessScore")
	public JSONObject saveBusinessScore(Integer score, Long employeeId,Long parentId,String parentName,Long topParentId,Long examType) {
		try {
			if (score == null || parentId == null ) {
				return error("参数错误!");
			}
			Employee employee = businessService.findEmployeeDetail(employeeId);
			BusinessTicket ticket = new BusinessTicket();
			ticket.setMemberId(employeeId);
			ticket.setAvatar(commonService.appenUrl(employee.getAvatar()));
			ticket.setNickName(employee.getName());
			ticket.setScore(score);
			ticket.setParentId(parentId);
			ticket.setParentName(parentName);
			ticket.setTopParentId(topParentId);
			ticket.setExamType(examType);
			commonService.saveBusinessScore(ticket);
			return ok("ok", ticket);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: findFirstTypeList
	 * @Description: (查询全部课程一级目录)
	 * @param page 分页
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findFirstTypeList")
	public JSONObject findFirstTypeList(Page page,String client,String version) {
		try {
			List<FirstType> firstTypeList = null;
			firstTypeList=businessService.findFirstTypeList(page);
			if(firstTypeList.size()>0){
				for(int i=0;i<firstTypeList.size();i++){
					Long id = firstTypeList.get(i).getId();
					List<SecondType> secondTypeList=businessService.findSecondTypeList(id);
					firstTypeList.get(i).setTypeTwo(secondTypeList);
				}
			}
			
			return ok("查询成功", firstTypeList);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: findSecondTypeList
	 * @Description: (查询全部课程一级目录)
	 * @param page 分页
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findSecondTypeList")
	public JSONObject findSecondTypeList(Long parentId,String client,String version) {
		if(NumberUtils.isNullOrZero(parentId)){
			return error("parentId参数有误");
		}
		try {
			List<SecondType> secondTypeList = null;
			secondTypeList=businessService.findSecondTypeList(parentId);
			return ok("查询成功", secondTypeList);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: findCatalogCourseListHotByType
	 * @Description: (根据播放时间等查询条件查询课程分类下热门课程列表)
	 * @param typeId 关键字ID
	 * @param level 级别         
	 * @param employeeId  企业用户ID
	 * @param timeLength 时长
	 * @param page 分页
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findCatalogCourseListHotByType")
	public JSONObject findCatalogCourseListHotByType(int firstTypeId,int secondTypeId,Long employeeId,int timeLength,int level,Page page,String client,String version,String deviceId) {
		try {
			if(NumberUtils.isNullOrZero(timeLength) && NumberUtils.isNullOrZero(firstTypeId) && NumberUtils.isNullOrZero(secondTypeId) && NumberUtils.isNullOrZero(level)){
				return error("参数缺失");
			}
			List<CatalogCourseList> catalogCourseList = null;
			if(employeeId!=33){
				//查询最近一次登录的信息
				LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employeeId);
				//查询学员此种设备、此个设备ID上次登录的明细
				LoginDetail loginDetail = memberService.findBusinessLoginDetail(employeeId,client,deviceId);
				if(loginDetail!=null){
					Long detailId = loginDetail.getId();
					Long lastId = lastLogin.getId();
					//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，如果最后一次的大于本机登陆的，那么就是在异地登录了
					if(lastId>detailId){	
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
					}
				}
			}
			
//			Employee employee = businessService.findEmployeeDetail(employeeId);
//			Long ifBusiness = employee.getIfBusiness();
			//判断用户是否为苹果端注册用户
			//1:苹果端注册用户 不为1：后来录入用户
//			if(ifBusiness==1){
//				catalogCourseList =businessService.findPersonCatalogCourseList(keyWordsId,timeLength,level,page);
//			}else{
				catalogCourseList=businessService.findCatalogCourseListByType(firstTypeId,secondTypeId,timeLength,level,page);
//			}
			//循环遍历集合查询是否是学员的推荐课程和计划学习课程
			for(int i=0;i<catalogCourseList.size();i++){
				//是否推荐
				if(businessService.queryRecommend(catalogCourseList.get(i).getId(),employeeId).size()>0){
					Long ifRecommend = (long) 1;
					catalogCourseList.get(i).setIfRecommend(ifRecommend);
				}else{
					Long ifRecommend = (long) 0;
					catalogCourseList.get(i).setIfRecommend(ifRecommend);
				}
				//是否在学习计划
				if(businessService.queryStudyPlan(catalogCourseList.get(i).getId(),employeeId).size()>0){
					Long ifStudyPlan = (long) 1;
					catalogCourseList.get(i).setIfStudyPlan(ifStudyPlan);
				}else{
					Long ifStudyPlan = (long) 0;
					catalogCourseList.get(i).setIfStudyPlan(ifStudyPlan);
				}
				catalogCourseList.get(i).setAvatar(commonService.appenUrl(catalogCourseList.get(i).getAvatar()));
//				catalogCourseList.get(i).setPdfAddress(commonService.appenUrl(catalogCourseList.get(i).getPdfAddress()));
				//根据版本号判断是否加密PDF地址
//				if(!("1.6.0").equals(version) && !("1.5.9").equals(version)){
//					catalogCourseList.get(i).setPdfAddress(commonService.appenUrl(catalogCourseList.get(i).getPdfAddress()));
//				}else{
					String content =catalogCourseList.get(i).getPdfAddress();
					String playAddress = catalogCourseList.get(i).getPlayAddress();
					//随机生成字母和数字的组合加密录音文件地址
					String pwd1 = PasswdEncryption.getStringRandom();
					StringBuffer stringBuffer1 = new StringBuffer(commonService.appenUrl(playAddress));
					stringBuffer1.insert(50, pwd1).toString();
					catalogCourseList.get(i).setPlayAddress(stringBuffer1.toString().toUpperCase());
					//加密PlayAddressM3u8
//					String playAddress1 = catalogCourseList.get(i).getPlayAddressM3u8();
//					String pwd2 = PasswdEncryption.getStringRandom();
//					StringBuffer stringBuffer2 = new StringBuffer(commonService.appenUrl(playAddress1));
//					stringBuffer2.insert(50, pwd2).toString();
//					catalogCourseList.get(i).setPlayAddressM3u8(stringBuffer2.toString().toUpperCase());
					if(!StringUtils.isNullOrEmpty(content)){
						//随机生成字母和数字的组合加密PDF地址
						String pwd = PasswdEncryption.getStringRandom();
						StringBuffer stringBuffer = new StringBuffer(commonService.appenUrl(catalogCourseList.get(i).getPdfAddress()));
						stringBuffer.insert(50, pwd).toString();
						catalogCourseList.get(i).setPdfAddress(stringBuffer.toString().toUpperCase());
//						catalogList.get(i).setAvatar(stringBuffer.toString().toUpperCase());
					}else{
						catalogCourseList.get(i).setPdfAddress(commonService.appenUrl(content));
					}
				}
//			}
			return ok("查询成功", catalogCourseList);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title:bundingMobile
	 * @Description: (企业APP匿名用户绑定手机号)
	 * @param mobile
	 *            手机号
	 * @param password
	 *            密码
	 * @param code
	 *            验证码
	 * @param info
	 *            手机信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping("bundingMobile")
	public JSONObject bundingMobile(String mobile, String password, String code, String deviceId,String version) {
		try {
			if (StringUtils.isNullOrEmpty(mobile) || StringUtils.isNullOrEmpty(password)
					|| StringUtils.isNullOrEmpty(code)) {
				return error("参数缺失");
			}
//			if(!("1.8.4").equals(version)){
			if(1==1){
				return error("绑定功能暂未开通，敬请期待！");
			}
			
			Code smsCode = codeService.findCode(code, mobile);
			if (smsCode == null) {
				return error("验证码输入错误!");
			}
			// 验证用户是否存在
			Employee employee = memberService.findBusinessLoginMemberByMobile(mobile);
			//根据设备Id查询是否已经注册过
			Employee employee1 =businessService.findEmployeeDetailByDeviceId(deviceId);
			if(employee!=null){
				//验证密码是否正确
				if (employee.getPassword().equals(PasswdEncryption.MD5(password)) || PasswdEncryption.verify(password, employee.getPassword())){
					//将匿名用户下的会员信息绑定到手机号码所在的用户
					businessService.updateMemberPayById(employee.getId(),employee1.getId());
					//删除现有的用户信息
					businessService.updateEmployeeDetailByDeviceId(employee1.getId());
					LoginInfo login = new LoginInfo();
					login.setAvatar(commonService.appenUrl(employee.getAvatar()));
					login.setId(employee.getId());
					login.setNickname(employee.getName());
					login.setMobile(mobile);
					login.setIfBusiness(employee.getIfBusiness());
					return ok("绑定成功",login);
				}else{
					return error("手机号码已经被注册过，但是绑定时输入的密码不正确");
				}
			}
			employee = new Employee();
			employee.setMobile(mobile);
			employee.setPassword(PasswdEncryption.generate(password));
			if (mobile.length() > 4) {
				employee.setName("用户" + mobile.substring(mobile.length() - 4));
			} else {
				employee.setName("用户" + mobile);
			}
			// 保存默认头像
			employee.setAvatar(commonService.getRandomAvatar());
			memberService.businessRegister(employee, smsCode,null);
			// 返回登录信息
			LoginInfo login = new LoginInfo();
			login.setAvatar(commonService.appenUrl(employee.getAvatar()));
			login.setId(employee.getId());
			login.setNickname(employee.getName());
			login.setMobile(mobile);
			login.setIfBusiness(employee.getIfBusiness());
			return ok("注册成功", login);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: addCourseComment 
	 * @Description: (记录学员课程评论) 
	 * @param courseId 课程ID
	 * @param employeeId 学员ID
	 * @param comment 课程评论内容
	 * @param studyTime 课程学习时长，单位秒
	 * @param courseTime 课程时长，只有学习完成时才传，大小为录音课件的时间长度，单位秒
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addCourseComment")
	public JSONObject addCourseComment(Long courseId,String employeeId,String comment,Page page,String client,String version,String deviceId,String platform,String sign){
		if(("2.0.5").equals(version) || ("2.0.1").equals(version)||("2.0.4").equals(version)){
			if(StringUtils.isNullOrEmpty(employeeId)|| StringUtils.isNullOrEmpty(deviceId) || NumberUtils.isNullOrZero(courseId)){
				return status(99,"参数错误");
			}
			//解密
			UpAES upAES = new UpAES();
			String newEmployeeId = upAES.disEntry(employeeId);
			String newDeviceId =upAES.disEntry(deviceId.toString());
			System.out.println("解密后的employeeId："+newEmployeeId);
			System.out.println("解密后的deviceId："+newDeviceId);
			//重新赋值deviceId
			String newSign = PasswdEncryption.MD5(newEmployeeId+newDeviceId);
			newSign= newSign.substring(5, newSign.length()-5);
			System.out.println("得到的新Sign"+newSign);
			if(newSign.equals(sign)){
				try{
					businessService.addCourseComment(courseId,Long.parseLong(newEmployeeId),java.net.URLEncoder.encode(comment,"utf-8"),newDeviceId,platform);
					List<Comment> commentList = null;
					List<Comment> commentList1 = null;
					String str = null;
					//查询评论点赞数前三的评论列表
					commentList = businessService.findCourseCommentListTop3(courseId);
					commentList1 = businessService.findCourseCommentList(courseId,page);
					commentList.addAll(commentList1);
					for(int i=0;i<commentList1.size();i++){
						commentList1.get(i).setCreateAt(commentList1.get(i).getCreateAt().substring(0, 19));
						commentList1.get(i).setAvatar(commonService.appenUrl(commentList1.get(i).getAvatar()));
						//解密
						commentList1.get(i).setComment(java.net.URLDecoder.decode(commentList1.get(i).getComment(),"utf-8"));
						//查询是否点赞
						List<CommentPraise> praiseList = null;
						praiseList = businessService.queryCommentPraise(commentList1.get(i).getId(),Long.parseLong(newEmployeeId));
						if(praiseList.size()>0){
							commentList1.get(i).setIfPraise(1L);
						}else{
							commentList1.get(i).setIfPraise(0L);
						}
					}
//					if(commentList.size()>0){
//						for(int i=0;i<commentList.size();i++){
//							commentList.get(i).setCreateAt(commentList.get(i).getCreateAt().substring(0, 19));
//							commentList.get(i).setAvatar(commonService.appenUrl(commentList.get(i).getAvatar()));
//							//查询是否点赞
//							List<CommentPraise> praiseList = null;
//							praiseList = businessService.queryCommentPraise(commentList.get(i).getId(),employeeId);
//							if(praiseList.size()>0){
//								commentList.get(i).setIfPraise(1L);
//							}else{
//								commentList.get(i).setIfPraise(0L);
//							}
//							if(commentList.size()>0){
//								for(int j=0;j<commentList.size();j++){
//									if(commentList1.get(i).getId() == commentList1.get(j).getId()){
//										commentList1.remove(i);
//									}
//								}
//							}
//						}
//					}
					return okNew("评论成功",commentList);
				}catch(Exception e){
					e.printStackTrace();
					return error(APP_SYSTEM_ERROR);
				}
			}else{
				return status(97,"sign验证出错");
			}
		}else{
			try{
				if(NumberUtils.isNullOrZero(courseId) ||StringUtils.isNullOrEmpty(employeeId) || StringUtils.isNullOrEmpty(comment)){
					return error("参数缺失");
				}
				businessService.addCourseComment(courseId,Long.parseLong(employeeId),java.net.URLEncoder.encode(comment,"utf-8"),deviceId,platform);
				List<Comment> commentList = null;
				List<Comment> commentList1 = null;
				String str = null;
				//查询评论点赞数前三的评论列表
				commentList = businessService.findCourseCommentListTop3(courseId);
				commentList1 = businessService.findCourseCommentList(courseId,page);
				commentList.addAll(commentList1);
				for(int i=0;i<commentList1.size();i++){
					commentList1.get(i).setCreateAt(commentList1.get(i).getCreateAt().substring(0, 19));
					commentList1.get(i).setAvatar(commonService.appenUrl(commentList1.get(i).getAvatar()));
					//解密
					commentList1.get(i).setComment(java.net.URLDecoder.decode(commentList1.get(i).getComment(),"utf-8"));
					//查询是否点赞
					List<CommentPraise> praiseList = null;
					praiseList = businessService.queryCommentPraise(commentList1.get(i).getId(),Long.parseLong(employeeId));
					if(praiseList.size()>0){
						commentList1.get(i).setIfPraise(1L);
					}else{
						commentList1.get(i).setIfPraise(0L);
					}
				}
//				if(commentList.size()>0){
//					for(int i=0;i<commentList.size();i++){
//						commentList.get(i).setCreateAt(commentList.get(i).getCreateAt().substring(0, 19));
//						commentList.get(i).setAvatar(commonService.appenUrl(commentList.get(i).getAvatar()));
//						//查询是否点赞
//						List<CommentPraise> praiseList = null;
//						praiseList = businessService.queryCommentPraise(commentList.get(i).getId(),employeeId);
//						if(praiseList.size()>0){
//							commentList.get(i).setIfPraise(1L);
//						}else{
//							commentList.get(i).setIfPraise(0L);
//						}
//						if(commentList.size()>0){
//							for(int j=0;j<commentList.size();j++){
//								if(commentList1.get(i).getId() == commentList1.get(j).getId()){
//									commentList1.remove(i);
//								}
//							}
//						}
//					}
//				}
				return ok("评论成功",commentList);
			}catch(Exception e){
				e.printStackTrace();
				return error(APP_SYSTEM_ERROR);
			}
		}
		
	}
	/**
	 * @Title: findCourseCommentList 
	 * @Description: (记录学员课程评论) 
	 * @param courseId 课程ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findCourseCommentList")
	public JSONObject findCourseCommentList(Long courseId,String employeeId,Page page,String client,String version,String deviceId,String sign){
		try{
			if(("2.0.5").equals(version) || ("2.0.1").equals(version)||("2.0.4").equals(version)){
				if(StringUtils.isNullOrEmpty(employeeId)|| StringUtils.isNullOrEmpty(deviceId) || NumberUtils.isNullOrZero(courseId)){
					return status(99,"参数错误");
				}
				//解密
				UpAES upAES = new UpAES();
				System.out.println("解密前的employeeId："+employeeId);
				System.out.println("解密前的deviceId："+deviceId);
				String newEmployeeId = upAES.disEntry(employeeId);
				String newDeviceId =upAES.disEntry(deviceId.toString());
				System.out.println("解密后的employeeId："+newEmployeeId);
				System.out.println("解密后的deviceId："+newDeviceId);
				//重新赋值deviceId
				String newSign = PasswdEncryption.MD5(newEmployeeId+newDeviceId);
				newSign= newSign.substring(5, newSign.length()-5);
				System.out.println("得到的新Sign"+newSign);
				if(newSign.equals(sign)){
					ArrayList<Comment> commentList = new ArrayList<Comment>();
					ArrayList<Comment> commentList1 = new ArrayList<Comment>();
					ArrayList<Comment> commentList2 = new ArrayList<Comment>();
					String str = "";
					//查询评论点赞数前三的评论列表
					commentList = businessService.findCourseCommentListTop3(courseId);
					commentList1 = businessService.findCourseCommentList(courseId,page);
//					for(int i=0;i<commentList1.size();i++){
//						if(commentList.size()>0){
//							for(int j=0;j<commentList.size();j++){
//								if(commentList1.get(i).getId()!=commentList.get(j).getId()){
//									commentList2.add(commentList1.get(i));
//									for(int k=0;k<commentList2.size();k++){
//										if(commentList2.get(k).getId()!=commentList1.get(i).getId()){
//											commentList2.add(commentList1.get(i));
//										}
//									}
//								}
//							}
//						}
//					}
//					if(commentList.size()>0){
//						commentList.addAll(commentList2);
//					}else{
//						commentList = commentList1;
//					}
					
					for(int i=0;i<commentList1.size();i++){
						commentList1.get(i).setCreateAt(commentList1.get(i).getCreateAt().substring(0, 19));
						commentList1.get(i).setAvatar(commonService.appenUrl(commentList1.get(i).getAvatar()));
						//解密
						commentList1.get(i).setComment(java.net.URLDecoder.decode(commentList1.get(i).getComment(),"utf-8"));
						//查询是否点赞
						List<CommentPraise> praiseList = null;
						praiseList = businessService.queryCommentPraise(Long.parseLong(commentList1.get(i).getId().toString()),Long.parseLong(newEmployeeId));
						if(praiseList.size()>0){
							commentList1.get(i).setIfPraise(1L);
						}else{
							commentList1.get(i).setIfPraise(0L);
						}
					}
					return okNew("查询成功",commentList1);
				}
				return status(97,"sign验证出错");
				
			}else{
				if(NumberUtils.isNullOrZero(courseId)){
					return error("参数缺失");
				}
				ArrayList<Comment> commentList = new ArrayList<Comment>();
				ArrayList<Comment> commentList1 = new ArrayList<Comment>();
				ArrayList<Comment> commentList2 = new ArrayList<Comment>();
				String str = "";
				//查询评论点赞数前三的评论列表
				commentList = businessService.findCourseCommentListTop3(courseId);
				commentList1 = businessService.findCourseCommentList(courseId,page);
//				for(int i=0;i<commentList1.size();i++){
//					if(commentList.size()>0){
//						for(int j=0;j<commentList.size();j++){
//							if(commentList1.get(i).getId()!=commentList.get(j).getId()){
//								commentList2.add(commentList1.get(i));
//								for(int k=0;k<commentList2.size();k++){
//									if(commentList2.get(k).getId()!=commentList1.get(i).getId()){
//										commentList2.add(commentList1.get(i));
//									}
//								}
//							}
//						}
//					}
//				}
//				if(commentList.size()>0){
//					commentList.addAll(commentList2);
//				}else{
//					commentList = commentList1;
//				}
				
				for(int i=0;i<commentList1.size();i++){
					commentList1.get(i).setCreateAt(commentList1.get(i).getCreateAt().substring(0, 19));
					commentList1.get(i).setAvatar(commonService.appenUrl(commentList1.get(i).getAvatar()));
					//解密
					commentList1.get(i).setComment(java.net.URLDecoder.decode(commentList1.get(i).getComment(),"utf-8"));
					//查询是否点赞
					List<CommentPraise> praiseList = null;
					praiseList = businessService.queryCommentPraise(Long.parseLong(commentList1.get(i).getId().toString()),Long.parseLong(employeeId));
					if(praiseList.size()>0){
						commentList1.get(i).setIfPraise(1L);
					}else{
						commentList1.get(i).setIfPraise(0L);
					}
				}
				return ok("查询成功",commentList1);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: updateStudyPlan
	 * @Description: (更改学习计划状态)
	 * @param employeeId 学员ID
	 * @param courseId 课程ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/commentPraise")
	public JSONObject commentPraise(String employeeId,Long commentId,String client,String version,String deviceId,String sign) {
		if(("2.0.5").equals(version) || ("2.0.1").equals(version)||("2.0.4").equals(version)){
			if(StringUtils.isNullOrEmpty(employeeId)|| StringUtils.isNullOrEmpty(deviceId) || NumberUtils.isNullOrZero(commentId)){
				return status(99,"参数错误");
			}
			//解密
			UpAES upAES = new UpAES();
			String newEmployeeId = upAES.disEntry(employeeId);
			String newDeviceId =upAES.disEntry(deviceId.toString());
			System.out.println("解密后的employeeId："+newEmployeeId);
			System.out.println("解密后的deviceId："+newDeviceId);
			//重新赋值deviceId
			String newSign = PasswdEncryption.MD5(newEmployeeId+newDeviceId);
			newSign= newSign.substring(5, newSign.length()-5);
			System.out.println("得到的新Sign"+newSign);
			if(newSign.equals(sign)){
				try {
					List<CommentPraise> praiseList = null;
					praiseList = businessService.queryCommentPraise(commentId,Long.parseLong(newEmployeeId));
					if(praiseList.size()>0){
						businessService.updateCommentPraise(praiseList.get(0).getId());
						//修改评论点赞数量-1
						businessService.updateCommentPraiseNum("-1",commentId);
//						return ok("取消收藏成功");
						return status(324, "取消赞成功");
					}else{
						List<CommentPraise> praiseListExist = null;
						praiseList = businessService.queryCommentPraiseExist(commentId,Long.parseLong(newEmployeeId));
						//判断学员历史是否收藏过此课程，如果收藏过并且已经取消收藏，则把数据还原
						if(praiseList.size()>0){
							businessService.updateCommentPraise(praiseList.get(0).getId());
							//修改评论点赞数量+1
							businessService.updateCommentPraiseNum("1",commentId);
							return ok("赞成功");
						}else{
							businessService.saveCommentPraise(commentId,Long.parseLong(newEmployeeId));
							//修改评论点赞数量+1
							businessService.updateCommentPraiseNum("1",commentId);
							return ok("赞成功");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					return error(APP_SYSTEM_ERROR);
				}
			}else{
				return status(97,"sign验证出错");
			}
		}else{
			try {
				List<CommentPraise> praiseList = null;
				praiseList = businessService.queryCommentPraise(commentId,Long.parseLong(employeeId));
				if(praiseList.size()>0){
					businessService.updateCommentPraise(praiseList.get(0).getId());
					//修改评论点赞数量-1
					businessService.updateCommentPraiseNum("-1",commentId);
//					return ok("取消收藏成功");
					return status(300, "取消赞成功");
				}else{
					List<CommentPraise> praiseListExist = null;
					praiseList = businessService.queryCommentPraiseExist(commentId,Long.parseLong(employeeId));
					//判断学员历史是否收藏过此课程，如果收藏过并且已经取消收藏，则把数据还原
					if(praiseList.size()>0){
						businessService.updateCommentPraise(praiseList.get(0).getId());
						//修改评论点赞数量+1
						businessService.updateCommentPraiseNum("1",commentId);
						return ok("赞成功");
					}else{
						businessService.saveCommentPraise(commentId,Long.parseLong(employeeId));
						//修改评论点赞数量+1
						businessService.updateCommentPraiseNum("1",commentId);
						return ok("赞成功");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return error(APP_SYSTEM_ERROR);
			}
		}
		
	}
	/**
	 * @Title: batchCollection
	 * @Description: (批量收藏)
	 * @param employeeId 学员ID
	 * @param courseId 课程ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/batchCollection")
	public JSONObject batchCollection(Long employeeId,String courselist,String client,String version) {
		try {
			
			if(courselist!=null && !StringUtils.isNullOrEmpty(courselist)){
				String[] arr = courselist.split(",");
			    List<String> list = Arrays.asList(arr);
				if(list.size()>0){
					List<StudyPlan> planList = null;
					for(int i=0;i<list.size();i++){
						//判断课程现在是否已经收藏了
						planList = businessService.queryStudyPlan(Long.parseLong(list.get(i)),employeeId);
						//如果没收藏则执行收藏操作
						if(planList.size()<=0){
							List<StudyPlan> planListExist = null;
							planListExist = businessService.queryStudyPlanExist(Long.parseLong(list.get(i)),employeeId);
							//判断学员历史是否收藏过此课程，如果收藏过并且已经取消收藏，则把数据还原
							if(planListExist.size()>0){
								businessService.updateStudyPlan(planListExist.get(0).getId());
							}else{
								businessService.saveStudyPlan(Long.parseLong(list.get(i)),employeeId);
							}
						}
					}
					return ok("收藏成功");
				}
			}
			
			return ok("收藏成功");
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: findCourseListByTitle
	 * @Description: (根据课程名称查询课程)
	 * @param title 课程名称
	 * @param employeeId 企业学员ID
	 * @param page 分页
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findCourseListByTitle")
	public JSONObject findCourseListByTitle(String title,String employeeId,Page page,String client,String version,String sign,String deviceId) {
		if(("2.0.5").equals(version) || ("2.0.1").equals(version)||("2.0.4").equals(version)){
			if(StringUtils.isNullOrEmpty(employeeId) || StringUtils.isNullOrEmpty(deviceId) || StringUtils.isNullOrEmpty(sign)){
				return status(99,"参数错误");
			}
			//解密
			UpAES upAES = new UpAES();
//			upAES.entry("1");
			String newEmployeeId = upAES.disEntry(employeeId);
			String newDeviceId =upAES.disEntry(deviceId.toString());
			System.out.println("解密后的employeeId："+newEmployeeId);
			System.out.println("解密后的deviceId："+newDeviceId);
			//重新赋值deviceId
			String newSign = PasswdEncryption.MD5(newEmployeeId+newDeviceId);
			newSign= newSign.substring(5, newSign.length()-5);
			System.out.println("得到的新Sign"+newSign);
			if(newSign.equals(sign)){
				try {
					List<CatalogCourseList> courseList = null;
					courseList=businessService.findCourseListByTitle(title,page);
					//循环遍历集合查询是否是学员的推荐课程和计划学习课程
					for(int i=0;i<courseList.size();i++){
						//是否推荐
						if(businessService.queryRecommend(courseList.get(i).getId(),Long.parseLong(newEmployeeId)).size()>0){
							Long ifRecommend = (long) 1;
							courseList.get(i).setIfRecommend(ifRecommend);
						}else{
							Long ifRecommend = (long) 0;
							courseList.get(i).setIfRecommend(ifRecommend);
						}
						//是否在学习计划
						if(businessService.queryStudyPlan(courseList.get(i).getId(),Long.parseLong(newEmployeeId)).size()>0){
							Long ifStudyPlan = (long) 1;
							courseList.get(i).setIfStudyPlan(ifStudyPlan);
						}else{
							Long ifStudyPlan = (long) 0;
							courseList.get(i).setIfStudyPlan(ifStudyPlan);
						}
						courseList.get(i).setAvatar(commonService.appenUrl(courseList.get(i).getAvatar()));
						courseList.get(i).setPlayAddress(commonService.appenUrl(courseList.get(i).getPlayAddress()));
						courseList.get(i).setPdfAddress(commonService.appenUrl(courseList.get(i).getPdfAddress()));
					}
					return okNew("查询成功", courseList);
				} catch (Exception e) {
					e.printStackTrace();
					return error(APP_SYSTEM_ERROR);
				}
			}else{
				return status(97,"sign验证出错");
			}
		}else{
			try {
				List<CatalogCourseList> courseList = null;
				courseList=businessService.findCourseListByTitle(title,page);
				//循环遍历集合查询是否是学员的推荐课程和计划学习课程
				for(int i=0;i<courseList.size();i++){
					//是否推荐
					if(businessService.queryRecommend(courseList.get(i).getId(),Long.parseLong(employeeId)).size()>0){
						Long ifRecommend = (long) 1;
						courseList.get(i).setIfRecommend(ifRecommend);
					}else{
						Long ifRecommend = (long) 0;
						courseList.get(i).setIfRecommend(ifRecommend);
					}
					//是否在学习计划
					if(businessService.queryStudyPlan(courseList.get(i).getId(),Long.parseLong(employeeId)).size()>0){
						Long ifStudyPlan = (long) 1;
						courseList.get(i).setIfStudyPlan(ifStudyPlan);
					}else{
						Long ifStudyPlan = (long) 0;
						courseList.get(i).setIfStudyPlan(ifStudyPlan);
					}
					courseList.get(i).setAvatar(commonService.appenUrl(courseList.get(i).getAvatar()));
					//根据版本号判断是否加密PDF地址
//					if(!("1.6.0").equals(version) && !("1.5.9").equals(version)){
//						courseList.get(i).setPdfAddress(commonService.appenUrl(courseList.get(i).getPdfAddress()));
//					}else{
						String content =courseList.get(i).getPdfAddress();
						String playAddress = courseList.get(i).getPlayAddress();
						//随机生成字母和数字的组合加密录音文件地址
						String pwd1 = PasswdEncryption.getStringRandom();
						StringBuffer stringBuffer1 = new StringBuffer(commonService.appenUrl(playAddress));
						stringBuffer1.insert(50, pwd1).toString();
						courseList.get(i).setPlayAddress(stringBuffer1.toString().toUpperCase());
						//加密PlayAddressM3u8
//						String playAddress1 = courseList.get(i).getPlayAddressM3u8();
//						String pwd2 = PasswdEncryption.getStringRandom();
//						StringBuffer stringBuffer2 = new StringBuffer(commonService.appenUrl(playAddress1));
//						stringBuffer2.insert(50, pwd2).toString();
//						courseList.get(i).setPlayAddressM3u8(stringBuffer2.toString().toUpperCase());
						if(!StringUtils.isNullOrEmpty(content)){
							//随机生成字母和数字的组合加密PDF地址
							String pwd = PasswdEncryption.getStringRandom();
							StringBuffer stringBuffer = new StringBuffer(commonService.appenUrl(courseList.get(i).getPdfAddress()));
							stringBuffer.insert(50, pwd).toString();
							courseList.get(i).setPdfAddress(stringBuffer.toString().toUpperCase());
//							catalogList.get(i).setAvatar(stringBuffer.toString().toUpperCase());
						}else{
							courseList.get(i).setPdfAddress(commonService.appenUrl(content));
						}
					}
//				}
				return ok("查询成功", courseList);
			} catch (Exception e) {
				e.printStackTrace();
				return error(APP_SYSTEM_ERROR);
			}
		}
	}
	/**
	 * @Title: findStudyTimeRankingList
	 * @Description: (查询学习时长排行榜)
	 * @param employeeId 企业学员ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findStudyTimeRankingList")
	public JSONObject findStudyTimeRankingList(String employeeId,String client,String version,String deviceId,String rankingType,String sign){
		try{
			if(("2.0.5").equals(version) || ("2.0.1").equals(version)||("2.0.4").equals(version)){
				if(StringUtils.isNullOrEmpty(employeeId)|| StringUtils.isNullOrEmpty(deviceId) || StringUtils.isNullOrEmpty(sign) || StringUtils.isNullOrEmpty(rankingType)){
					return status(99,"参数错误");
				}
				//解密
				UpAES upAES = new UpAES();
//				upAES.entry("1");
				String newEmployeeId = upAES.disEntry(employeeId);
				String newDeviceId =upAES.disEntry(deviceId.toString());
				System.out.println("解密后的employeeId："+newEmployeeId);
				System.out.println("解密后的deviceId："+newDeviceId);
				//重新赋值deviceId
				String newSign = PasswdEncryption.MD5(newEmployeeId+newDeviceId);
				newSign= newSign.substring(5, newSign.length()-5);
				System.out.println("得到的新Sign"+newSign);
				if(newSign.equals(sign)){
					Long employeeId1 = Long.parseLong(newEmployeeId);
					Employee employee1 = businessService.findEmployeeDetail(employeeId1);
					if(employee1==null){
						return status(800, "用户账号已失效");
					}
					if(employeeId1!=33){
						//查询最近一次登录的信息
						LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employeeId1);
						//查询学员此种设备、此个设备ID上次登录的明细
						LoginDetail loginDetail = memberService.findBusinessLoginDetail(employeeId1,client,newDeviceId);
						if(loginDetail!=null){
							Long detailId = loginDetail.getId();
							Long lastId = lastLogin.getId();
							//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，如果最后一次的大于本机登陆的，那么就是在异地登录了
							if(lastId>detailId){
								SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
							}
						}
					}
					if("0".equals(rankingType)){
						//完成的课程数量
//						List<CatalogCourseList> list=businessService.queryStudyFinishedList(employeeId1);
						//收藏的课程数量
//						List<CatalogCourseList> planlist = businessService.queryStudyPlanList(employeeId1);
						//学习总时长
						int studyLength = businessService.findEmployeeStudyTimeCountNew(employeeId1);
						//查询学员学习时长目前所在的名次位置
//						float persent =0;
						//获取学员学习时长的排名
						String rowNo1 = businessService.findEmployeeStudyTimeNoNew(employeeId1);
						if(StringUtils.isNullOrEmpty(rowNo1)){
							rowNo1="0";
						}
						int rowNo = Integer.parseInt(rowNo1) + 0;
						//获取试题总的参考人数
						int ticketCount = businessService.findEmployeeStudyCountNew(employeeId1);
						//取学习时长的最长和最短记录
						//取系列考题的最高分数和低分
//						String timeMax1 = businessService.findStudyTimeMax(employeeId1);
//						int timeMax =0;
//						if(!StringUtils.isNullOrEmpty(timeMax1)){
//							timeMax = Integer.parseInt(timeMax1);
//						}
//						String timeMin1 = businessService.findStudyTimeMin(employeeId1);
//						int timeMin =0;
//						if(!StringUtils.isNullOrEmpty(timeMin1)){
//							timeMin = Integer.parseInt(timeMin1);
//						}
//						if(rowNo == ticketCount){
//							persent = 100;
//						}else{
//							//获取此学员成绩所在的范围
//							if(timeMax == studyLength){
//								persent = 100;
//							}else if(timeMin == studyLength){
//								persent = 0;
//							}else{
//								persent = (rowNo*100/ticketCount);
//							}
//						}
						
						employee1.setAvatar(commonService.appenUrl(employee1.getAvatar()));
						employee1.setStudyTime(studyLength);
						if(rowNo==0){
							employee1.setStudyTimeRanking(rowNo);
						}else{
							employee1.setStudyTimeRanking(ticketCount-rowNo+1);
						}
//						employee1.setStudyTimeRanking(rowNo);
						employee1.setEmployeeId(employee1.getId());
						//查询学习时长排名前20的学员信息
						List<Employee> employeeList = businessService.findStudyTimeRankingListNew();
						for(int i=0;i<employeeList.size();i++){
							employeeList.get(i).setStudyPlanCount(businessService.findEmployeePlanCount(employeeList.get(i).getId()));
							employeeList.get(i).setStudyTimeCount(businessService.findEmployeeStudyTimeCountNew(employeeList.get(i).getId()));
							employeeList.get(i).setId(employeeList.get(i).getId());
							employeeList.get(i).setAvatar(commonService.appenUrl(employeeList.get(i).getAvatar()));
							employeeList.get(i).setName(employeeList.get(i).getName());
							//完成的课程数量
							List<CatalogCourseList> listi=businessService.queryStudyFinishedList(employeeList.get(i).getEmployeeId());
							employeeList.get(i).setFinishedCount(listi.size());
						}
						EModel model = new EModel();
						model.setPerson(employee1);
						model.setList(employeeList);
						return okNew("ok",model);
					}else{
						//学习总人数
						int studyLength = businessService.findEmployeeStudyTimeCountAllNew(employeeId1);
						//查询学员学习时长目前所在的名次位置
						float persent =0;
						//获取学员学习时长的排名
						String rowNo1 = businessService.findEmployeeStudyTimeNoALlNew(employeeId1);
						if(StringUtils.isNullOrEmpty(rowNo1)){
							rowNo1="0";
						}
						int rowNo = Integer.parseInt(rowNo1) + 0;
//						//获取试题总的参考人数
						int ticketCount = businessService.findEmployeeStudyCountAllNew(employeeId1);
//						//取学习时长的最长和最短记录
//						//取系列考题的最高分数和低分
//						String timeMax1 = businessService.findStudyTimeMax(employeeId1);
//						int timeMax =0;
//						if(!StringUtils.isNullOrEmpty(timeMax1)){
//							timeMax = Integer.parseInt(timeMax1);
//						}
//						String timeMin1 = businessService.findStudyTimeMin(employeeId1);
//						int timeMin =0;
//						if(!StringUtils.isNullOrEmpty(timeMin1)){
//							timeMin = Integer.parseInt(timeMin1);
//						}
//						if(rowNo == ticketCount){
//							persent = 100;
//						}else{
//							//获取此学员成绩所在的范围
//							if(timeMax == studyLength){
//								persent = 100;
//							}else if(timeMin == studyLength){
//								persent = 0;
//							}else{
//								persent = (rowNo*100/ticketCount);
//							}
//						}
						employee1.setAvatar(commonService.appenUrl(employee1.getAvatar()));
						employee1.setStudyTime(studyLength);
						if(rowNo==0){
							employee1.setStudyTimeRanking(rowNo);
						}else{
							employee1.setStudyTimeRanking(ticketCount-rowNo+1);
						}
//						employee1.setStudyTimeRanking(rowNo);
						employee1.setEmployeeId(employee1.getId());
						//查询学习时长排名前20的学员信息
						List<Employee> employeeList = businessService.findStudyTimeRankingListALlNew();
						for(int i=0;i<employeeList.size();i++){
							employeeList.get(i).setStudyPlanCount(businessService.findEmployeePlanCount(employeeList.get(i).getId()));
							employeeList.get(i).setStudyTimeCount(businessService.findEmployeeStudyTimeCount(employeeList.get(i).getId()));
							employeeList.get(i).setId(employeeList.get(i).getId());
							employeeList.get(i).setAvatar(commonService.appenUrl(employeeList.get(i).getAvatar()));
							employeeList.get(i).setName(employeeList.get(i).getName());
							//完成的课程数量
							List<CatalogCourseList> listi=businessService.queryStudyFinishedList(employeeList.get(i).getEmployeeId());
							employeeList.get(i).setFinishedCount(listi.size());
						}
						EModel model = new EModel();
						model.setPerson(employee1);
						model.setList(employeeList);
						return okNew("ok",model);
					}
					
				}
				else{
					return status(97,"sign验证出错");
				}
			}else{
				Employee employee1 = businessService.findEmployeeDetail(Long.parseLong(employeeId));
				if(employee1==null){
					return status(800, "用户账号已失效");
				}
				if(Long.parseLong(employeeId)!=33){
					//查询最近一次登录的信息
					LoginDetail lastLogin = memberService.findBusinessLoginDetailById(Long.parseLong(employeeId));
					//查询学员此种设备、此个设备ID上次登录的明细
					LoginDetail loginDetail = memberService.findBusinessLoginDetail(Long.parseLong(employeeId),client,deviceId);
					if(loginDetail!=null){
						Long detailId = loginDetail.getId();
						Long lastId = lastLogin.getId();
						//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，如果最后一次的大于本机登陆的，那么就是在异地登录了
						if(lastId>detailId){
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
						}
					}
				}
				//完成的课程数量
				List<CatalogCourseList> list=businessService.queryStudyFinishedList(Long.parseLong(employeeId));
				//收藏的课程数量
				List<CatalogCourseList> planlist = businessService.queryStudyPlanList(Long.parseLong(employeeId));
				//学习总时长
				int studyLength = businessService.findEmployeeStudyTimeCount(Long.parseLong(employeeId));
				//查询学员学习时长目前所在的名次位置
				float persent =0;
				//获取学员成绩的排名
				String rowNo1 = businessService.findEmployeeStudyTimeNo(Long.parseLong(employeeId));
				if(StringUtils.isNullOrEmpty(rowNo1)){
					rowNo1="0";
				}
				int rowNo = Integer.parseInt(rowNo1) + 0;
				//获取试题总的参考人数
				int ticketCount = businessService.findEmployeeStudyCount(Long.parseLong(employeeId));
				//取学习时长的最长和最短记录
				//取系列考题的最高分数和低分
				String timeMax1 = businessService.findStudyTimeMax(Long.parseLong(employeeId));
				int timeMax =0;
				if(!StringUtils.isNullOrEmpty(timeMax1)){
					timeMax = Integer.parseInt(timeMax1);
				}
				String timeMin1 = businessService.findStudyTimeMin(Long.parseLong(employeeId));
				int timeMin =0;
				if(!StringUtils.isNullOrEmpty(timeMin1)){
					timeMin = Integer.parseInt(timeMin1);
				}
				if(rowNo == ticketCount){
					persent = 100;
				}else{
					//获取此学员成绩所在的范围
					if(timeMax == studyLength){
						persent = 100;
					}else if(timeMin == studyLength){
						persent = 0;
					}else{
						persent = (rowNo*100/ticketCount);
					}
				}
				//查询学习时长排名前20的学员信息
				List<Employee> employeeList = businessService.findStudyTimeRankingList();
				for(int i=0;i<employeeList.size();i++){
					employeeList.get(i).setStudyPlanCount(businessService.findEmployeePlanCount(employeeList.get(i).getId()));
					employeeList.get(i).setStudyTimeCount(businessService.findEmployeeStudyTimeCount(employeeList.get(i).getId()));
					employeeList.get(i).setId(employeeList.get(i).getId());
					employeeList.get(i).setAvatar(commonService.appenUrl(employeeList.get(i).getAvatar()));
					employeeList.get(i).setName(employeeList.get(i).getName());
					//完成的课程数量
					List<CatalogCourseList> listi=businessService.queryStudyFinishedList(employeeList.get(i).getEmployeeId());
					employeeList.get(i).setFinishedCount(listi.size());
				}
			
				return ok("ok",employeeList);
			}
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: getCatalogList
	 * @Description: (获得课程分类列表)
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getHomePage")
	public JSONObject getHomePage(Page page,Long employeeId,String client,String version,String deviceId) {
		try {
			List<Catalog> catalogList = null;
//			Employee employee = businessService.findEmployeeDetail(employeeId);
//			Long ifBusiness = employee.getIfBusiness();
			//判断用户是否为苹果端注册用户
			//1:苹果端注册用户 不为1：后来录入用户
//			if(ifBusiness==1){
//				//查询个人用户分类列表
////				catalogList=businessService.findPersonCatalogList(page); 
//				//以下是判断用户会员是否过期的
//				MemberPayInfo payInfo = businessService.queryMemberPayByEmployeeId(employeeId);
//			    if(payInfo!=null){
//			    	Long monthCount = businessService.queryMonthCountByEmployeeId(employeeId);
//			    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
//			    	Date now = new Date();
//			    	Calendar calendar = Calendar.getInstance();
//			    	calendar.setTime(payInfo.getCreateAt());
//			    	calendar.add(Calendar.MONTH,  Integer.valueOf(monthCount.toString()));
//			    	System.out.println(sdf.format(calendar.getTime()));
//			    	//当前时间
//			    	Date dateTime1 = dateFormat.parse(sdf.format(now));
//			    	//会员过期时间
//			        Date dateTime2 = dateFormat.parse(sdf.format(calendar.getTime()));
//			        int i = dateTime1.compareTo(dateTime2);  
//			        System.out.println(i < 0);
//			    }
//			}else{
			//生成八位数的随机数作为访问接口的唯一标识
			String code = CodeUtils.getCode(8);
			SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if(employeeId!=33){
					//查询最近一次登录的信息
					LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employeeId);
					//查询学员此种设备、此个设备ID上次登录的明细
					LoginDetail loginDetail = memberService.findBusinessLoginDetail(employeeId,client,deviceId);
					if(loginDetail!=null){
						Long detailId = loginDetail.getId();
						Long lastId = lastLogin.getId();
						//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，，如果最后一次的大于本机登陆的，那么就是在异地登录了
						if(lastId>detailId){
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
						}
					}
				}

				//查询最新一条封号记录
				List<BlockDetail> blist = memberService.queryLastBlockList(employeeId);
				//如果存在没解封的记录 ，提示解封时间
				if(blist.size()>0){
		        	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Calendar calendar1 = Calendar.getInstance();
					//取账号创建时间
			    	calendar1.setTime(new Date());
			    	//账号创建时间加上会员日期为会员过期日期
			    	calendar1.add(Calendar.DAY_OF_MONTH,  Integer.valueOf("30"));
			    	System.out.println(sdf1.format(calendar1.getTime()));
			    	DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    	Date now1 = new Date();
			    	Date dateTime4 = dateFormat1.parse(df1.format(now1));
//			    	//会员过期时间
			        Date dateTime5 = dateFormat1.parse(df1.format(calendar1.getTime()));
			        //取出封号记录的解封时间
			        Date dateTime6 = dateFormat1.parse(blist.get(0).getUnblockAt());
			        //当前时间小于解封时间 求两个时间的差值
		        	if(dateTime4.getTime()<=dateTime6.getTime()){
		        		long diff = dateTime6.getTime() - dateTime4.getTime();
				        long diffDays = diff / (24 * 60 * 60 * 1000);
				        System.out.println("day1:"+dateTime6+"   day2:"+dateTime4+ "   差值："+diffDays);
				        return status(600,"您的账号还有"+diffDays+"天解封，请耐心等待哦");
		        	}
				}
				//新版本首页最新课程
//				catalogList=businessService.findHomePageNewCatalogList();
//				for(int i=0;i<catalogList.size();i++){
//					catalogList.get(i).setAvatar(commonService.appenUrl(catalogList.get(i).getAvatar()));
//				}
				logger.error(code+"客户Id："+employeeId+"，开始查询首页最新课程时间："+df1.format(new Date()));
				catalogList=businessService.findHomePageNewCatalogList();
				logger.error(code+"客户Id："+employeeId+"，结束查询首页最新课程时间："+df1.format(new Date()));
				logger.error(code+"客户Id："+employeeId+"，开始循环遍历首页最新课程时间："+df1.format(new Date()));
				Employee employee1 = businessService.findEmployeeDetail(employeeId);
				for(int i=0;i<catalogList.size();i++){
					//判断是否是只购买了系列课程
//					if(employee1.getIfBusiness()!=0L && employee1.getIfBusiness()!=1L){
//						if(employee1.getIfBusiness()==catalogList.get(i).getId()){
//							catalogList.get(i).setIsChapter("1");
//						}else{
//							catalogList.get(i).setIsChapter("0");
//						}
//					}
					catalogList.get(i).setAvatar(commonService.appenUrl(catalogList.get(i).getAvatar()));
					catalogList.get(i).getFlag();
					//判断是以什么形式展现课程列表
					if(catalogList.get(i).getFlag()=="0" || ("0").equals(catalogList.get(i).getFlag())){
						//修改课程数量
						catalogList.get(i).setCourseCount(businessService.findCatalogListCourseCount(catalogList.get(i).getId()));
						//修改学习人数
						catalogList.get(i).setStudyCount(businessService.findCatalogListStudyCount(catalogList.get(i).getId()));
					}
					//判断t_business_member_record表中是否有效期内的好多课会员数据存在
					List<MemberRecord> recordList = businessService.findMemberRecordList(employeeId.toString());
					if(recordList.size()>0){
						catalogList.get(i).setIsChapter("1");
					}else{
						//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
						 if(businessService.findMemberRecordListByCatalogId(employeeId.toString(),catalogList.get(i).getId().toString()).size()>0){
							 catalogList.get(i).setIsChapter("1");
						 }else{
							 catalogList.get(i).setIsChapter("0");
						 }
					}
					//判断课程包管理的试卷是否已经回答过
					if(businessService.queryAnswerHistory(catalogList.get(i).getExamId(),employeeId).size()>0){
						catalogList.get(i).setTestMark("1");
					}else{
						catalogList.get(i).setTestMark("0");
					}
				}
				logger.error(code+"客户Id："+employeeId+"，结束循环遍历首页最新课程时间："+df1.format(new Date()));
				Long va = 1L;
				List<CatalogCourseList> catalogCourseList = null;
				//查询今日热门课程所在的分类
				Catalog hotCatalog = businessService.findHotCatalogList(va).get(0);
				//新版本热门课程(分类Id是1的课程);
				catalogCourseList=businessService.getCatalogCourseList(va);
				//循环遍历集合查询是否是学员的推荐课程和计划学习课程
				
//				if(employeeId!=33){
					logger.error(code+"客户Id："+employeeId+"，循环遍历首页***热门课程***----是否推荐和收藏----开始时间："+df1.format(new Date()));
					for(int i=0;i<catalogCourseList.size();i++){
						//是否推荐
						if(businessService.queryRecommend(catalogCourseList.get(i).getId(),employeeId).size()>0){
							Long ifRecommend = (long) 1;
							catalogCourseList.get(i).setIfRecommend(ifRecommend);
						}else{
							Long ifRecommend = (long) 0;
							catalogCourseList.get(i).setIfRecommend(ifRecommend);
						}
						//是否在学习计划
						if(businessService.queryStudyPlan(catalogCourseList.get(i).getId(),employeeId).size()>0){
							Long ifStudyPlan = (long) 1;
							catalogCourseList.get(i).setIfStudyPlan(ifStudyPlan);
						}else{
							Long ifStudyPlan = (long) 0;
							catalogCourseList.get(i).setIfStudyPlan(ifStudyPlan);
						}
						catalogCourseList.get(i).setAvatar(commonService.appenUrl(catalogCourseList.get(i).getAvatar()));
						catalogCourseList.get(i).setPdfAddress(commonService.appenUrl(catalogCourseList.get(i).getPdfAddress()));
							String content =catalogCourseList.get(i).getPdfAddress();
							String playAddress = catalogCourseList.get(i).getPlayAddress();
							//随机生成字母和数字的组合加密录音文件地址
							String pwd1 = PasswdEncryption.getStringRandom();
							StringBuffer stringBuffer1 = new StringBuffer(commonService.appenUrl(playAddress));
							stringBuffer1.insert(50, pwd1).toString();
							catalogCourseList.get(i).setPlayAddress(stringBuffer1.toString().toUpperCase());
							//加密PlayAddressM3u8
							String playAddress1 = catalogCourseList.get(i).getPlayAddressM3u8();
							String pwd2 = PasswdEncryption.getStringRandom();
							StringBuffer stringBuffer2 = new StringBuffer(commonService.appenUrl(playAddress1));
							stringBuffer2.insert(50, pwd2).toString();
							catalogCourseList.get(i).setPlayAddressM3u8(stringBuffer2.toString().toUpperCase());
							if(!StringUtils.isNullOrEmpty(content)){
								//随机生成字母和数字的组合加密PDF地址
								String pwd = PasswdEncryption.getStringRandom();
								StringBuffer stringBuffer = new StringBuffer(commonService.appenUrl(catalogCourseList.get(i).getPdfAddress()));
								stringBuffer.insert(50, pwd).toString();
								catalogCourseList.get(i).setPdfAddress(stringBuffer.toString().toUpperCase());
	//							catalogList.get(i).setAvatar(stringBuffer.toString().toUpperCase());
							}else{
								catalogCourseList.get(i).setPdfAddress(commonService.appenUrl(content));
							}
							//判断课程包管理的试卷是否已经回答过
							if(businessService.queryAnswerHistory(catalogCourseList.get(i).getExamId(),employeeId).size()>0){
								catalogCourseList.get(i).setTestMark("1");
							}else{
								catalogCourseList.get(i).setTestMark("0");
							}
						}
					logger.error(code+"客户Id："+employeeId+"，循环遍历首页***热门课程***----是否推荐和收藏---结束时间："+df1.format(new Date()));
//				}
				//首页Banner列表
				List<Banner> bannerList = null;
				//首页banner课程
				bannerList=businessService.getBannerList(va);
				for(int i=0;i<bannerList.size();i++){
					bannerList.get(i).setCover(commonService.appenUrl(bannerList.get(i).getCover()));
					//如果Banner展示形式是系列课
					if(bannerList.get(i).getShowType()=="2" || "2".equals(bannerList.get(i).getShowType())){
						//判断t_business_member_record表中是否有效期内的好多课会员数据存在
						List<MemberRecord> recordList = businessService.findMemberRecordList(employeeId.toString());
						if(recordList.size()>0){
							bannerList.get(i).setIsChapter("1");
						}else{
							//判断试听专区
							if("94".equals(bannerList.get(i).getTypeId())){
								bannerList.get(i).setIsChapter("1");
							}else{
								//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
								 if(businessService.findMemberRecordListByCatalogId(employeeId.toString(),bannerList.get(i).getTypeId().toString()).size()>0){
									 bannerList.get(i).setIsChapter("1");
								 }else{
									 bannerList.get(i).setIsChapter("0");
								 }
							}
						}
					}else{
						 bannerList.get(i).setIsChapter("0");
					}
					//如果是讲师展示形式
					if(bannerList.get(i).getShowType()=="1" || ("1").equals(bannerList.get(i).getShowType())){
						List<Teacher> teachersList = null;
						teachersList=businessService.findTeacherById(Long.parseLong(bannerList.get(i).getTeacherId()));
						if(teachersList.size()>0){
							bannerList.get(i).setCatalogId(teachersList.get(0).getCatalogId());
							bannerList.get(i).setFlag(teachersList.get(0).getFlag());
						}
					}
				}
				List<CatalogCourseList> recommendCourseList = null;
				//新版本推荐课程(根据学员学习过的课程推荐);
				if(employeeId!=33){
					//查询学习完的课程类型
					List<StudyDetail> a = businessService.getStudyType(employeeId);
					String resulta = "";
					if(a.size()>0){
						for(int i=0;i<a.size();i++){
							StudyDetail oa =  a.get(i);
							resulta += "'"+oa.getId().toString()+"',";
						}
						resulta = resulta.substring(1,resulta.length()-2);
					}
					//查询学习完的课程Id
					List<StudyDetail> b = businessService.getStudyCourseId(employeeId);
					String resultb = "";
					if(b.size()>0){
						for(int i=0;i<b.size();i++){
							StudyDetail ob =  b.get(i);
							resultb += "'"+ob.getCourseId().toString()+"',";
						}
						resultb = resultb.substring(1,resultb.length()-2);
					}
					recommendCourseList=businessService.getRecommendCourseList(employeeId,resulta,resultb);
				}else{
					List<StudyDetail> a = businessService.getStudyType(1L);
					String resulta = "";
					if(a.size()>0){
						for(int i=0;i<a.size();i++){
							StudyDetail oa =  a.get(i);
							resulta += "'"+oa.getId().toString()+"',";
						}
						resulta = resulta.substring(1,resulta.length()-2);
					}
					//查询学习完的课程Id
					List<StudyDetail> b = businessService.getStudyCourseId(1L);
					String resultb = "";
					if(b.size()>0){
						for(int i=0;i<b.size();i++){
							StudyDetail ob =  b.get(i);
							resultb += "'"+ob.getCourseId().toString()+"',";
						}
						resultb = resultb.substring(1,resultb.length()-2);
					}
					recommendCourseList=businessService.getRecommendCourseList(1L,resulta,resultb);
				}
				if(recommendCourseList.size()<5){
					//如果推荐课程不足5个，则推荐学习人数在前五的课程
					List<CatalogCourseList> surplusCourseList = null;
					Long surplus = 5L-recommendCourseList.size();
					surplusCourseList = businessService.getSurplusCourseList(surplus);
					recommendCourseList.addAll(surplusCourseList);
				}
				//循环遍历集合查询是否是学员的推荐课程和计划学习课程
//				if(employeeId!=33){
					logger.error(code+"客户Id："+employeeId+"，循环遍历首页***推荐课程列表***----是否推荐和收藏---开始时间："+df1.format(new Date()));
					for(int i=0;i<recommendCourseList.size();i++){
						//是否推荐
						if(businessService.queryRecommend(recommendCourseList.get(i).getId(),employeeId).size()>0){
							Long ifRecommend = (long) 1;
							recommendCourseList.get(i).setIfRecommend(ifRecommend);
						}else{
							Long ifRecommend = (long) 0;
							recommendCourseList.get(i).setIfRecommend(ifRecommend);
						}
						//是否在学习计划
						if(businessService.queryStudyPlan(recommendCourseList.get(i).getId(),employeeId).size()>0){
							Long ifStudyPlan = (long) 1;
							recommendCourseList.get(i).setIfStudyPlan(ifStudyPlan);
						}else{
							Long ifStudyPlan = (long) 0;
							recommendCourseList.get(i).setIfStudyPlan(ifStudyPlan);
						}
						//判断课程包管理的试卷是否已经回答过
						if(businessService.queryAnswerHistory(recommendCourseList.get(i).getExamId(),employeeId).size()>0){
							recommendCourseList.get(i).setTestMark("1");
						}else{
							recommendCourseList.get(i).setTestMark("0");
						}
						recommendCourseList.get(i).setAvatar(commonService.appenUrl(recommendCourseList.get(i).getAvatar()));
						recommendCourseList.get(i).setPdfAddress(commonService.appenUrl(recommendCourseList.get(i).getPdfAddress()));
						String content =recommendCourseList.get(i).getPdfAddress();
						String playAddress = recommendCourseList.get(i).getPlayAddress();
						//随机生成字母和数字的组合加密录音文件地址
						String pwd1 = PasswdEncryption.getStringRandom();
						StringBuffer stringBuffer1 = new StringBuffer(commonService.appenUrl(playAddress));
						stringBuffer1.insert(50, pwd1).toString();
						recommendCourseList.get(i).setPlayAddress(stringBuffer1.toString().toUpperCase());
						//加密PlayAddressM3u8
						String playAddress1 = recommendCourseList.get(i).getPlayAddressM3u8();
						String pwd2 = PasswdEncryption.getStringRandom();
						StringBuffer stringBuffer2 = new StringBuffer(commonService.appenUrl(playAddress1));
						stringBuffer2.insert(50, pwd2).toString();
						recommendCourseList.get(i).setPlayAddressM3u8(stringBuffer2.toString().toUpperCase());
						if(!StringUtils.isNullOrEmpty(content)){
							//随机生成字母和数字的组合加密PDF地址
							String pwd = PasswdEncryption.getStringRandom();
							StringBuffer stringBuffer = new StringBuffer(commonService.appenUrl(recommendCourseList.get(i).getPdfAddress()));
							stringBuffer.insert(50, pwd).toString();
							recommendCourseList.get(i).setPdfAddress(stringBuffer.toString().toUpperCase());
	//						catalogList.get(i).setAvatar(stringBuffer.toString().toUpperCase());
						}else{
							recommendCourseList.get(i).setPdfAddress(commonService.appenUrl(content));
						}
					}
					logger.error(code+"客户Id："+employeeId+"，循环遍历首页***推荐课程列表***----是否推荐和收藏---结束时间："+df1.format(new Date()));
//				}
				hotCatalog.setHotCourseList(catalogCourseList);
				hotCatalog.setCatalogList(catalogList);
				hotCatalog.setRecommendCourseList(recommendCourseList);
				hotCatalog.setBannerList(bannerList);
				hotCatalog.setAvatar(commonService.appenUrl(hotCatalog.getAvatar()));
				List<MemberRecord> recordList = businessService.findMemberRecordList(employeeId.toString());
				if(recordList.size()>0){
					hotCatalog.setIfBusiness("0");
				}else{
					//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
					 if(businessService.findMemberRecordListByEmployeeId(employeeId.toString()).size()>0){
						 hotCatalog.setIfBusiness("99999");
					 }else{
						 hotCatalog.setIfBusiness("1");
					 }
				}
			return ok("查询成功", hotCatalog);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: getTeacherList
	 * @Description: (查询讲师列表)
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getTeacherList")
	public JSONObject getTeacherList(Page page,Long employeeId,String client,String version,String deviceId) {
		try {
			List<Teacher> teacherList = null;
				if(employeeId!=33){
					//查询最近一次登录的信息
					LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employeeId);
					//查询学员此种设备、此个设备ID上次登录的明细
					LoginDetail loginDetail = memberService.findBusinessLoginDetail(employeeId,client,deviceId);
					if(loginDetail!=null){
						Long detailId = loginDetail.getId();
						Long lastId = lastLogin.getId();
						//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，，如果最后一次的大于本机登陆的，那么就是在异地登录了
						if(lastId>detailId){
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
						}
					}
				}
				String ifBusiness="1";
					List<MemberRecord> recordList = businessService.findMemberRecordList(employeeId.toString());
					if(recordList.size()>0){
						ifBusiness="0";
					}else{
						//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
						 if(businessService.findMemberRecordListByEmployeeId(employeeId.toString()).size()>0){
							 ifBusiness="99999";
						 }else{
							 ifBusiness="1";
						 }
					}
				teacherList=businessService.findAppTeacherList(page);
				for(int i=0;i<teacherList.size();i++){
					teacherList.get(i).setAvatar(commonService.appenUrl(teacherList.get(i).getAvatar()));
					teacherList.get(i).setCover(commonService.appenUrl(teacherList.get(i).getCover()));
					teacherList.get(i).setIfBusiness(ifBusiness);
				}
//			}
			return ok("查询成功", teacherList);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: getTeacherList
	 * @Description: (查询讲师列表)
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getTeacherListForWeb")
	public JSONObject getTeacherListForWeb(Page page,Long employeeId,String client,String version,String deviceId) {
		try {
			List<Teacher> teacherList = null;
				if(employeeId!=33){
					//查询最近一次登录的信息
					LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employeeId);
					//查询学员此种设备、此个设备ID上次登录的明细
					LoginDetail loginDetail = memberService.findBusinessLoginDetail(employeeId,client,deviceId);
					if(loginDetail!=null){
						Long detailId = loginDetail.getId();
						Long lastId = lastLogin.getId();
						//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，，如果最后一次的大于本机登陆的，那么就是在异地登录了
						if(lastId>detailId){
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
						}
					}
				}
				teacherList=businessService.getTeacherListForWeb(page);
				for(int i=0;i<teacherList.size();i++){
					teacherList.get(i).setAvatar(commonService.appenUrl(teacherList.get(i).getAvatar()));
					teacherList.get(i).setCover(commonService.appenUrl(teacherList.get(i).getCover()));
				}
//			}
			return ok("查询成功", teacherList);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: getTeacherListForDaKe
	 * @Description: (查询大课讲师列表)
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getTeacherListForDaKe")
	public JSONObject getTeacherListForDaKe(Page page) {
		try {
			List<Teacher> teacherList = null;
				teacherList=businessService.getTeacherListForDake(page);
				for(int i=0;i<teacherList.size();i++){
					teacherList.get(i).setAvatar(commonService.appenUrl(teacherList.get(i).getAvatar()));
					teacherList.get(i).setCover(commonService.appenUrl(teacherList.get(i).getCover()));
				}
//			}
			return ok("查询成功", teacherList);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: getTeacherList
	 * @Description: (查询讲师列表)
	 * @return
	 */
//	@ResponseBody
//	@RequestMapping("/findTeacherList")
//	public JSONObject findTeacherList(Page page,Long employeeId,String client,String version,String deviceId) {
//		try {
//			List<Teacher> teacherList = null;
//				if(employeeId!=33){
//					//查询最近一次登录的信息
//					LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employeeId);
//					//查询学员此种设备、此个设备ID上次登录的明细
//					LoginDetail loginDetail = memberService.findBusinessLoginDetail(employeeId,client,deviceId);
//					if(loginDetail!=null){
//						Long detailId = loginDetail.getId();
//						Long lastId = lastLogin.getId();
//						//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，，如果最后一次的大于本机登陆的，那么就是在异地登录了
//						if(lastId>detailId){
//							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//							return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
//						}
//					}
//				}
//				teacherList=businessService.findTeacherList(page);
//				for(int i=0;i<teacherList.size();i++){
//					teacherList.get(i).setAvatar(commonService.appenUrl(teacherList.get(i).getAvatar()));
//					teacherList.get(i).setCover(commonService.appenUrl(teacherList.get(i).getCover()));
//				}
////			}
//			return ok("查询成功", teacherList);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return error(APP_SYSTEM_ERROR);
//		}
//	}
	/**
	 * @Title: getTeacherList
	 * @Description: (查询讲师列表)
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getStudentsList")
	public JSONObject getStudentsList(Page page,Long employeeId,String client,String version,String deviceId) {
		try {
			List<Student> studentList = null;
				if(employeeId!=33){
					//查询最近一次登录的信息
					LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employeeId);
					//查询学员此种设备、此个设备ID上次登录的明细
					LoginDetail loginDetail = memberService.findBusinessLoginDetail(employeeId,client,deviceId);
					if(loginDetail!=null){
						Long detailId = loginDetail.getId();
						Long lastId = lastLogin.getId();
						//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，，如果最后一次的大于本机登陆的，那么就是在异地登录了
						if(lastId>detailId){
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
						}
					}
				}
				studentList=businessService.findStudentList(page);
				for(int i=0;i<studentList.size();i++){
					studentList.get(i).setCover(commonService.appenUrl(studentList.get(i).getCover()));
				}
//			}
			return ok("查询成功", studentList);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: findFirstTypeList
	 * @Description: (查询全部课程一级目录)
	 * @param page 分页
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findNewFirstTypeList")
	public JSONObject findNewFirstTypeList(Page page,String client,String version,Long employeeId,String deviceId) {
		try {
			List<FirstType> firstTypeList = null;
			if(employeeId!=33){
				//查询最近一次登录的信息
				LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employeeId);
				//查询学员此种设备、此个设备ID上次登录的明细
				LoginDetail loginDetail = memberService.findBusinessLoginDetail(employeeId,client,deviceId);
				if(loginDetail!=null){
					Long detailId = loginDetail.getId();
					Long lastId = lastLogin.getId();
					//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，，如果最后一次的大于本机登陆的，那么就是在异地登录了
					if(lastId>detailId){
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
					}
				}
			}
			firstTypeList=businessService.findFirstTypeList(page);
			if(firstTypeList.size()>0){
				for(int i=0;i<firstTypeList.size();i++){
					Long id = firstTypeList.get(i).getId();
					List<SecondType> secondTypeList=businessService.findSecondTypeList(id);
					firstTypeList.get(i).setTypeTwo(secondTypeList);
					firstTypeList.get(i).setCover(commonService.appenUrl(firstTypeList.get(i).getCover()));
				}
			}
			List<Teacher> teacherList = null;
			teacherList=businessService.findTeacherList(page);
			for(int i=0;i<teacherList.size();i++){
				teacherList.get(i).setAvatar(commonService.appenUrl(teacherList.get(i).getAvatar()));
			}
			List<MemberRecord> recordList = businessService.findMemberRecordList(employeeId.toString());
			if(recordList.size()>0){
				firstTypeList.get(0).setIfBusiness("0");
			}else{
				//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
				 if(businessService.findMemberRecordListByEmployeeId(employeeId.toString()).size()>0){
					 firstTypeList.get(0).setIfBusiness("99999");
				 }else{
					 firstTypeList.get(0).setIfBusiness("1");
				 }
			}
			firstTypeList.get(0).setTeacher(teacherList);
			return ok("查询成功", firstTypeList);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: deleteImage 
	 * @Description: (删除图片) 
	 * @param id
	 * @return 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getOnlineCourseCode")
	public JSONObject getOnlineCourseCode(Long id,String courseName) throws UnsupportedEncodingException {
//		Long id = getParaToLong("id", 0L);
//		String url = "http://www.chazuomba.com/files/webApp/PaymentPage.html?courseId=" + id+"&course_name="+courseName;
		String url = URLEncoder.encode("http://api.chazuomba.com/manage/Web/PaymentPage/courseId/"+ id,"utf-8");		
		String params = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxa3069b403f0a23af&redirect_uri="
				+ url
				+ "&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
//		render(new QRCodeUtil(120, 120, generateShortUrl(params)));
		return status(200,params);
	}
	/**
	 * @Title: findFirstTypeList
	 * @Description: (查询全部课程一级目录)
	 * @param page 分页
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findSelfStudyList")
	public JSONObject findSelfStudyList(Page page) {
		try {
			List<SelfStudy> selfStudyList = null;
			selfStudyList=businessService.findSelfStudyList(page);
			if(selfStudyList.size()>0){
				for(int i=0;i<selfStudyList.size();i++){
					selfStudyList.get(i).setCover(commonService.appenUrl(selfStudyList.get(i).getCover()));
				}
			}
			return ok("查询成功", selfStudyList);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	@ResponseBody
	@RequestMapping("/queryTeacherList")
	public JSONObject queryTeacherList(Page page,Long employeeId,String client,String version,String deviceId) {
		try {
			List<Teacher> teacherList = null;
				if(employeeId!=33){
					//查询最近一次登录的信息
					LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employeeId);
					//查询学员此种设备、此个设备ID上次登录的明细
					LoginDetail loginDetail = memberService.findBusinessLoginDetail(employeeId,client,deviceId);
					if(loginDetail!=null){
						Long detailId = loginDetail.getId();
						Long lastId = lastLogin.getId();
						//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，，如果最后一次的大于本机登陆的，那么就是在异地登录了
						if(lastId>detailId){
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
						}
					}
				}
				teacherList=businessService.queryTeacherList(page);
				for(int i=0;i<teacherList.size();i++){
					teacherList.get(i).setAvatar(commonService.appenUrl(teacherList.get(i).getAvatar()));
					teacherList.get(i).setCover(commonService.appenUrl(teacherList.get(i).getCover()));
				}
//			}
			return ok("查询成功", teacherList);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: getCatalogList
	 * @Description: (获得课程分类列表)
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getSecondCatalogDetailById")
	public JSONObject getSecondCatalogDetailById(Page page,String employeeId,Long catalogId,String client,String version,String deviceId,String sign) {
		try {
			List<Catalog> catalogList1 = null;
			List<SecondCatalog> catalogList = null;
			if(("2.0.5").equals(version) || ("2.0.1").equals(version)||("2.0.4").equals(version)){
				if(StringUtils.isNullOrEmpty(employeeId) || catalogId==null || catalogId==0 || StringUtils.isNullOrEmpty(deviceId) || StringUtils.isNullOrEmpty(sign)){
					return status(99,"参数错误");
				}
				//解密
				UpAES upAES = new UpAES();
//				upAES.entry("1");
				String newEmployeeId = upAES.disEntry(employeeId);
				String newDeviceId =upAES.disEntry(deviceId.toString());
				System.out.println("解密后的employeeId："+newEmployeeId);
				System.out.println("解密后的deviceId："+newDeviceId);
				//重新赋值deviceId
				String newSign = PasswdEncryption.MD5(newEmployeeId+newDeviceId);
				newSign= newSign.substring(5, newSign.length()-5);
				System.out.println("得到的新Sign"+newSign);
				if(newSign.equals(sign)){
					Long employeeId1 = Long.parseLong(newEmployeeId);
					if(employeeId1!=33){
						//查询最近一次登录的信息
						LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employeeId1);
						//查询学员此种设备、此个设备ID上次登录的明细
						LoginDetail loginDetail = memberService.findBusinessLoginDetail(employeeId1,client,newDeviceId);
						if(loginDetail!=null){
							Long detailId = loginDetail.getId();
							Long lastId = lastLogin.getId();
							//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，，如果最后一次的大于本机登陆的，那么就是在异地登录了
							if(lastId>detailId){
								SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
							}
						}
					}
					catalogList1=businessService.findParentCatalogById(catalogId);
					Employee employee1 = businessService.findEmployeeDetail(employeeId1);
					for(int i=0;i<catalogList1.size();i++){
						//判断t_business_member_record表中是否有效期内的好多课会员数据存在
						List<MemberRecord> recordList = businessService.findMemberRecordList(employeeId1.toString());
						//根据课程包Id查询推荐列表
						List<RecommendInfo> recommendList = businessService.findRecommendList(catalogList1.get(i).getId(),"2");
						String os = "0";
						if("iOS".equals(client)){
							os = "1";
						}else if("android".equals(client)){
							os = "2";
						}
						//先判断课程是不是被推荐的课程
						if(recommendList.size()>0 && (recommendList.get(0).getOs().equals("0") || recommendList.get(0).getOs().equals(os))){
							catalogList1.get(i).setIsChapter("1");
						}else{
							if(recordList.size()>0){
								catalogList1.get(i).setIsChapter("1");
							}else{
								//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
								 if(businessService.findMemberRecordListByCatalogId(employeeId1.toString(),catalogList1.get(i).getId().toString()).size()>0){
									 catalogList1.get(i).setIsChapter("1");
								 }else{
									 catalogList1.get(i).setIsChapter("0");
								 }
							}
						}
						catalogList1.get(i).setIfVideo("0");
						//是否收藏了课程包去
						if(businessService.queryStudyPlanCatalog(catalogList1.get(i).getId(),employeeId1).size()>0){
							Long ifStudyPlan = (long) 1;
							catalogList1.get(i).setIfCollection(ifStudyPlan);
						}else{
							Long ifStudyPlan = (long) 0;
							catalogList1.get(i).setIfCollection(ifStudyPlan);
						}
						//判断课程包管理的试卷是否已经回答过
						if(businessService.queryAnswerHistory(catalogList1.get(i).getExamId(),employeeId1).size()>0){
							catalogList1.get(i).setTestMark("1");
						}else{
							catalogList1.get(i).setTestMark("0");
						}
						//获取teacherId 的集合
						String ids = catalogList1.get(i).getTeacherId();
						List<Teacher> teacherList = new ArrayList<Teacher>();
						StringBuffer sb = new StringBuffer();
						for (String s : ids.split(",")) {
							if (StringUtils.isNullOrEmpty(s)) {
								continue;
							}
							List<Teacher> list=businessService.findTeacherById(Long.parseLong(s));
							if(list.size()>0){
								list.get(0).setAvatar(commonService.appenUrl(list.get(i).getAvatar()));
								list.get(0).setCover(commonService.appenUrl(list.get(i).getCover()));
								teacherList.add(list.get(0));
							}
//							sb.append(service.findTeacherById(Long.parseLong(s)).getStr("name")).append(",");
						}
						catalogList1.get(i).setTeacherList(teacherList);
						catalogList1.get(i).setAvatar(commonService.appenUrl(catalogList1.get(i).getAvatar()));
						catalogList1.get(i).setCover(commonService.appenUrl(catalogList1.get(i).getCover()));
						catalogList1.get(i).setListCover(commonService.appenUrl(catalogList1.get(i).getListCover()));
						catalogList1.get(i).getFlag();
						SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						System.out.println("查询二级分类列表开始时间："+df1.format(new Date()));
						catalogList=businessService.findSecondCatalogList(page,catalogId);
						System.out.println("查询二级分类列表结束时间："+df1.format(new Date()));
						for(int j=0;j<catalogList.size();j++){
							catalogList.get(j).setAvatar(commonService.appenUrl(catalogList.get(j).getAvatar()));
							List<SecondCatalogCourseList> catalogCourseList = null;
							System.out.println("查询二级分类下课程列表开始时间："+df1.format(new Date()));
							catalogCourseList=businessService.getSecondCatalogCourseList(catalogList.get(j).getId());
							System.out.println("查询二级分类下课程列表结束时间："+df1.format(new Date()));
							//循环遍历集合查询是否是学员的推荐课程和计划学习课程
							if(catalogCourseList!=null){
								for(int k=0;k<catalogCourseList.size();k++){
									//是否推荐
									if(businessService.queryRecommend(catalogCourseList.get(k).getId(),employeeId1).size()>0){
										Long ifRecommend = (long) 1;
										catalogCourseList.get(k).setIfRecommend(ifRecommend);
									}else{
										Long ifRecommend = (long) 0;
										catalogCourseList.get(k).setIfRecommend(ifRecommend);
									}
									//是否在学习计划
									if(businessService.queryStudyPlan(catalogCourseList.get(k).getId(),employeeId1).size()>0){
										Long ifStudyPlan = (long) 1;
										catalogCourseList.get(k).setIfStudyPlan(ifStudyPlan);
									}else{
										Long ifStudyPlan = (long) 0;
										catalogCourseList.get(k).setIfStudyPlan(ifStudyPlan);
									}
									//学习进度
									if(businessService.queryStudyIfFinished(catalogCourseList.get(k).getId(),employeeId1).size()>0){
										catalogCourseList.get(k).setIsFinished(businessService.queryStudyIfFinished(catalogCourseList.get(k).getId(),employeeId1).get(0).getIsFinished());
									}else{
										Long isFinished = (long) 0;
										catalogCourseList.get(k).setIsFinished(isFinished);
									}
									//判断课程包管理的试卷是否已经回答过
									if(businessService.queryAnswerHistory(catalogCourseList.get(k).getExamId(),employeeId1).size()>0){
										catalogCourseList.get(k).setTestMark("1");
									}else{
										catalogCourseList.get(k).setTestMark("0");
									}
									catalogCourseList.get(k).setAvatar(commonService.appenUrl(catalogCourseList.get(k).getAvatar()));
									//根据版本号判断是否加密PDF地址
//									if(!("1.6.0").equals(version) && !("1.5.9").equals(version)){
										catalogCourseList.get(k).setPdfAddress(commonService.appenUrl(catalogCourseList.get(k).getPdfAddress()));
										catalogCourseList.get(k).setPlayAddress(commonService.appenUrl(catalogCourseList.get(k).getPlayAddress()));
//									}else{
//										String content =catalogCourseList.get(k).getPdfAddress();
//										String playAddress = catalogCourseList.get(k).getPlayAddress();
//										//随机生成字母和数字的组合加密录音文件地址
//										String pwd1 = PasswdEncryption.getStringRandom();
//										StringBuffer stringBuffer1 = new StringBuffer(commonService.appenUrl(playAddress));
//										stringBuffer1.insert(50, pwd1).toString();
//										catalogCourseList.get(k).setPlayAddress(stringBuffer1.toString().toUpperCase());
//										//加密PlayAddressM3u8
//										String playAddress1 = catalogCourseList.get(k).getPlayAddressM3u8();
//										String pwd2 = PasswdEncryption.getStringRandom();
//										StringBuffer stringBuffer2 = new StringBuffer(commonService.appenUrl(playAddress1));
//										stringBuffer2.insert(50, pwd2).toString();
//										catalogCourseList.get(k).setPlayAddressM3u8(stringBuffer2.toString().toUpperCase());
//										if(!StringUtils.isNullOrEmpty(content)){
//											//随机生成字母和数字的组合加密PDF地址
//											String pwd = PasswdEncryption.getStringRandom();
//											StringBuffer stringBuffer = new StringBuffer(commonService.appenUrl(catalogCourseList.get(k).getPdfAddress()));
//											stringBuffer.insert(50, pwd).toString();
//											catalogCourseList.get(k).setPdfAddress(stringBuffer.toString().toUpperCase());
////											catalogList.get(i).setAvatar(stringBuffer.toString().toUpperCase());
//										}else{
//											catalogCourseList.get(k).setPdfAddress(commonService.appenUrl(content));
//										}
									}
//								}
							}
							catalogList.get(j).setCatalogCourseList(catalogCourseList);
						}
						System.out.println("遍历结束时间："+df1.format(new Date()));
						catalogList1.get(i).setSecondCatalogList(catalogList);
						//判断是以什么形式展现课程列表
						if(catalogList1.get(i).getFlag()=="0" || ("0").equals(catalogList1.get(i).getFlag())){
							//修改课程数量
							catalogList1.get(i).setCourseCount(businessService.findCatalogListCourseCount(catalogList1.get(i).getId()));
							//修改学习人数
							catalogList1.get(i).setStudyCount(businessService.findCatalogListStudyCount(catalogList1.get(i).getId()));
						}
						//根据课程包ID查询分享链接地址
						String shareAddress = businessService.findShareAddress("h5_course_share_url");
						if(!StringUtils.isNullOrEmpty(shareAddress)){
							shareAddress+="&id="+catalogList1.get(i).getCourseId();
						}
						catalogList1.get(i).setShareAddress(shareAddress);
						//根据课程简章链接地址
						String synopsisAddress = businessService.findShareAddress("h5_course_synopsis_url");
						if(!StringUtils.isNullOrEmpty(synopsisAddress)){
							synopsisAddress+="&catalogId="+catalogList1.get(i).getId();
						}
						catalogList1.get(i).setSynopsisAddress(synopsisAddress);
						//查询是否已经被选课
						if(businessService.queryIsChoose(catalogList1.get(i).getId(),employeeId1).size()>0){
							catalogList1.get(i).setIsChoose("1");
						}else{
							catalogList1.get(i).setIsChoose("0");
						}
						//判断课程实际数量是否大于
						if(Integer.parseInt(catalogList1.get(i).getTotal())<catalogList1.get(i).getCourseCount()){
							catalogList1.get(i).setTotal(String.valueOf(catalogList1.get(i).getCourseCount()));
						}
					}
//				}
//				return ok("查询成功", catalogList);
					
//				}
				return okNew("查询成功", catalogList1);
			
				}else{
					return status(97,"sign验证失败");
				}
			}else{
				if(Long.parseLong(employeeId)!=33){
					//查询最近一次登录的信息
					LoginDetail lastLogin = memberService.findBusinessLoginDetailById(Long.parseLong(employeeId));
					//查询学员此种设备、此个设备ID上次登录的明细
					LoginDetail loginDetail = memberService.findBusinessLoginDetail(Long.parseLong(employeeId),client,deviceId);
					if(loginDetail!=null){
						Long detailId = loginDetail.getId();
						Long lastId = lastLogin.getId();
						//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，，如果最后一次的大于本机登陆的，那么就是在异地登录了
						if(lastId>detailId){
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
						}
					}
				}
				catalogList1=businessService.findParentCatalogById(catalogId);
				Employee employee1 = businessService.findEmployeeDetail(Long.parseLong(employeeId));
				for(int i=0;i<catalogList1.size();i++){
					//判断t_business_member_record表中是否有效期内的好多课会员数据存在
					List<MemberRecord> recordList = businessService.findMemberRecordList(employeeId);
					if(recordList.size()>0){
						catalogList1.get(i).setIsChapter("1");
					}else{
						//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
						 if(businessService.findMemberRecordListByCatalogId(employeeId,catalogList1.get(i).getId().toString()).size()>0){
							 catalogList1.get(i).setIsChapter("1");
						 }else{
							 catalogList1.get(i).setIsChapter("0");
						 }
					}
					catalogList1.get(i).setIfVideo("0");
					//是否收藏了课程包去
					if(businessService.queryStudyPlanCatalog(catalogList1.get(i).getId(),Long.parseLong(employeeId)).size()>0){
						Long ifStudyPlan = (long) 1;
						catalogList1.get(i).setIfCollection(ifStudyPlan);
					}else{
						Long ifStudyPlan = (long) 0;
						catalogList1.get(i).setIfCollection(ifStudyPlan);
					}
					//判断课程包管理的试卷是否已经回答过
					if(businessService.queryAnswerHistory(catalogList1.get(i).getExamId(),Long.parseLong(employeeId)).size()>0){
						catalogList1.get(i).setTestMark("1");
					}else{
						catalogList1.get(i).setTestMark("0");
					}
					//获取teacherId 的集合
					String ids = catalogList1.get(i).getTeacherId();
					List<Teacher> teacherList = new ArrayList<Teacher>();
					StringBuffer sb = new StringBuffer();
					for (String s : ids.split(",")) {
						if (StringUtils.isNullOrEmpty(s)) {
							continue;
						}
						List<Teacher> list=businessService.findTeacherById(Long.parseLong(s));
						if(list.size()>0){
							list.get(0).setAvatar(commonService.appenUrl(list.get(i).getAvatar()));
							list.get(0).setCover(commonService.appenUrl(list.get(i).getCover()));
							teacherList.add(list.get(0));
						}
//						sb.append(service.findTeacherById(Long.parseLong(s)).getStr("name")).append(",");
					}
					catalogList1.get(i).setTeacherList(teacherList);
					catalogList1.get(i).setAvatar(commonService.appenUrl(catalogList1.get(i).getAvatar()));
					catalogList1.get(i).setCover(commonService.appenUrl(catalogList1.get(i).getCover()));
					catalogList1.get(i).getFlag();
					SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					System.out.println("查询二级分类列表开始时间："+df1.format(new Date()));
					catalogList=businessService.findSecondCatalogList(page,catalogId);
					System.out.println("查询二级分类列表结束时间："+df1.format(new Date()));
					for(int j=0;j<catalogList.size();j++){
						catalogList.get(j).setAvatar(commonService.appenUrl(catalogList.get(j).getAvatar()));
						List<SecondCatalogCourseList> catalogCourseList = null;
						System.out.println("查询二级分类下课程列表开始时间："+df1.format(new Date()));
						catalogCourseList=businessService.getSecondCatalogCourseList(catalogList.get(j).getId());
						System.out.println("查询二级分类下课程列表结束时间："+df1.format(new Date()));
						//循环遍历集合查询是否是学员的推荐课程和计划学习课程
						if(catalogCourseList!=null){
							for(int k=0;k<catalogCourseList.size();k++){
								//是否推荐
								if(businessService.queryRecommend(catalogCourseList.get(k).getId(),Long.parseLong(employeeId)).size()>0){
									Long ifRecommend = (long) 1;
									catalogCourseList.get(k).setIfRecommend(ifRecommend);
								}else{
									Long ifRecommend = (long) 0;
									catalogCourseList.get(k).setIfRecommend(ifRecommend);
								}
								//是否在学习计划
								if(businessService.queryStudyPlan(catalogCourseList.get(k).getId(),Long.parseLong(employeeId)).size()>0){
									Long ifStudyPlan = (long) 1;
									catalogCourseList.get(k).setIfStudyPlan(ifStudyPlan);
								}else{
									Long ifStudyPlan = (long) 0;
									catalogCourseList.get(k).setIfStudyPlan(ifStudyPlan);
								}
								//学习进度
								if(businessService.queryStudyIfFinished(catalogCourseList.get(k).getId(),Long.parseLong(employeeId)).size()>0){
									catalogCourseList.get(k).setIsFinished(businessService.queryStudyIfFinished(catalogCourseList.get(k).getId(),Long.parseLong(employeeId)).get(0).getIsFinished());
								}else{
									Long isFinished = (long) 0;
									catalogCourseList.get(k).setIsFinished(isFinished);
								}
								//判断课程包管理的试卷是否已经回答过
								if(businessService.queryAnswerHistory(catalogCourseList.get(k).getExamId(),Long.parseLong(employeeId)).size()>0){
									catalogCourseList.get(k).setTestMark("1");
								}else{
									catalogCourseList.get(k).setTestMark("0");
								}
								catalogCourseList.get(k).setAvatar(commonService.appenUrl(catalogCourseList.get(k).getAvatar()));
								//根据版本号判断是否加密PDF地址
//								if(!("1.6.0").equals(version) && !("1.5.9").equals(version)){
//									catalogCourseList.get(j).setPdfAddress(commonService.appenUrl(catalogCourseList.get(j).getPdfAddress()));
//								}else{
									String content =catalogCourseList.get(k).getPdfAddress();
//									String playAddress = catalogCourseList.get(k).getPlayAddress();
//									//随机生成字母和数字的组合加密录音文件地址
//									String pwd1 = PasswdEncryption.getStringRandom();
//									StringBuffer stringBuffer1 = new StringBuffer(commonService.appenUrl(playAddress));
//									stringBuffer1.insert(50, pwd1).toString();
//									catalogCourseList.get(k).setPlayAddress(stringBuffer1.toString().toUpperCase());
//									//加密PlayAddressM3u8
//									String playAddress1 = catalogCourseList.get(k).getPlayAddressM3u8();
//									String pwd2 = PasswdEncryption.getStringRandom();
//									StringBuffer stringBuffer2 = new StringBuffer(commonService.appenUrl(playAddress1));
//									stringBuffer2.insert(50, pwd2).toString();
//									catalogCourseList.get(k).setPlayAddressM3u8(stringBuffer2.toString().toUpperCase());
									if(!StringUtils.isNullOrEmpty(content)){
										//随机生成字母和数字的组合加密PDF地址
										String pwd = PasswdEncryption.getStringRandom();
										StringBuffer stringBuffer = new StringBuffer(commonService.appenUrl(catalogCourseList.get(k).getPdfAddress()));
										stringBuffer.insert(50, pwd).toString();
										catalogCourseList.get(k).setPdfAddress(stringBuffer.toString().toUpperCase());
//										catalogList.get(i).setAvatar(stringBuffer.toString().toUpperCase());
									}else{
										catalogCourseList.get(k).setPdfAddress(commonService.appenUrl(content));
									}
								}
//							}
						}
						catalogList.get(j).setCatalogCourseList(catalogCourseList);
					}
					System.out.println("遍历结束时间："+df1.format(new Date()));
					catalogList1.get(i).setSecondCatalogList(catalogList);
					//判断是以什么形式展现课程列表
					if(catalogList1.get(i).getFlag()=="0" || ("0").equals(catalogList1.get(i).getFlag())){
						//修改课程数量
						catalogList1.get(i).setCourseCount(businessService.findCatalogListCourseCount(catalogList1.get(i).getId()));
						//修改学习人数
						catalogList1.get(i).setStudyCount(businessService.findCatalogListStudyCount(catalogList1.get(i).getId()));
					}
					//判断课程实际数量是否大于
					if(Integer.parseInt(catalogList1.get(i).getTotal())<catalogList1.get(i).getCourseCount()){
						catalogList1.get(i).setTotal(String.valueOf(catalogList1.get(i).getCourseCount()));
					}
				}
//			}
//			return ok("查询成功", catalogList);
				
//			}
			return ok("查询成功", catalogList1);
		}
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: getCatalogCourseList
	 * @Description: (获得课程分类下课程列表)
	 * @param catalogId
	 *            分类ID
	 * @param employeeId
	 *            用户ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getCatalogDetailById1")
	public JSONObject getCatalogDetailById(Long catalogId,String employeeId,String client,String version,String deviceId,String sign) {
		try {
			List<CatalogCourseList> catalogCourseList = null;
			List<Catalog> catalogList = null;
			if("2.0.5".equals(version) || ("2.0.1").equals(version)||("2.0.4").equals(version)){
				UpAES upAES = new UpAES();
//				upAES.entry("1");
				
				String newEmployeeId = upAES.disEntry(employeeId);
				String newCatalogId =upAES.disEntry(deviceId);
				System.out.println("解密后的employeeID："+newEmployeeId);
				System.out.println("解密后的deviceId："+newCatalogId);
				//重新赋值deviceId
				String newSign = PasswdEncryption.MD5(newEmployeeId+newCatalogId);
				newSign= newSign.substring(5, newSign.length()-5);
				System.out.println("得到的新Sign"+newSign);
			//解密
			
				if(newSign.equals(sign)){
					Long employeeId1 = Long.parseLong(upAES.disEntry(employeeId));
					if(employeeId1!=33){
						LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employeeId1);
						//查询学员此种设备、此个设备ID上次登录的明细
						LoginDetail loginDetail = memberService.findBusinessLoginDetail(employeeId1,client,newCatalogId);
						if(loginDetail!=null){
							Long detailId = loginDetail.getId();
							Long lastId = lastLogin.getId();
							//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，，如果最后一次的大于本机登陆的，那么就是在异地登录了
							if(lastId>detailId){
								SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
							}
						}
					}
					catalogList=businessService.findParentCatalogById(catalogId);
					Employee employee1 = businessService.findEmployeeDetail(employeeId1);
					for(int i=0;i<catalogList.size();i++){
						//是否收藏了课程包去
						if(businessService.queryStudyPlanCatalog(catalogList.get(i).getId(),employeeId1).size()>0){
							Long ifStudyPlan = (long) 1;
							catalogList.get(i).setIfCollection(ifStudyPlan);
						}else{
							Long ifStudyPlan = (long) 0;
							catalogList.get(i).setIfCollection(ifStudyPlan);
						}
						//设置课程包的类型为非视频
						catalogList.get(i).setIfVideo("0");
						//判断t_business_member_record表中是否有效期内的好多课会员数据存在
						List<MemberRecord> recordList = businessService.findMemberRecordList(employeeId1.toString());
						//根据课程包Id查询推荐列表
						List<RecommendInfo> recommendList = businessService.findRecommendList(catalogList.get(i).getId(),"2");
						String os = "0";
						if("iOS".equals(client)){
							os = "1";
						}else if("android".equals(client)){
							os = "2";
						}
						//先判断课程是不是被推荐的课程
						if(recommendList.size()>0 && (recommendList.get(0).getOs().equals("0") || recommendList.get(0).getOs().equals(os))){
							catalogList.get(i).setIsChapter("1");
						}else{
							if(recordList.size()>0){
								catalogList.get(i).setIsChapter("1");
							}else{
								//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
								 if(businessService.findMemberRecordListByCatalogId(employeeId1.toString(),catalogList.get(i).getId().toString()).size()>0){
									 catalogList.get(i).setIsChapter("1");
								 }else{
									 catalogList.get(i).setIsChapter("0");
								 }
							}
						}
						//判断课程包管理的试卷是否已经回答过
						if(businessService.queryAnswerHistory(catalogList.get(i).getExamId(),employeeId1).size()>0){
							catalogList.get(i).setTestMark("1");
						}else{
							catalogList.get(i).setTestMark("0");
						}
						//获取teacherId 的集合
						String ids = catalogList.get(i).getTeacherId();
						List<Teacher> teacherList = new ArrayList<Teacher>();
						StringBuffer sb = new StringBuffer();
						for (String s : ids.split(",")) {
							if (StringUtils.isNullOrEmpty(s)) {
								continue;
							}
							List<Teacher> list=businessService.findTeacherById(Long.parseLong(s));
							if(list.size()>0){
								list.get(0).setAvatar(commonService.appenUrl(list.get(i).getAvatar()));
								list.get(0).setCover(commonService.appenUrl(list.get(i).getCover()));
								teacherList.add(list.get(0));
							}
		//					sb.append(service.findTeacherById(Long.parseLong(s)).getStr("name")).append(",");
						}
						catalogList.get(i).setTeacherList(teacherList);
						catalogList.get(i).setAvatar(commonService.appenUrl(catalogList.get(i).getAvatar()));
						catalogList.get(i).setCover(commonService.appenUrl(catalogList.get(i).getCover()));
						catalogList.get(i).setListCover(commonService.appenUrl(catalogList.get(i).getListCover()));
						catalogList.get(i).getFlag();
						catalogCourseList=businessService.getCatalogCourseList(catalogId);
						//循环遍历集合查询是否是学员的推荐课程和计划学习课程
						for(int j=0;j<catalogCourseList.size();j++){
							//是否推荐
							if(businessService.queryRecommend(catalogCourseList.get(j).getId(),employeeId1).size()>0){
								Long ifRecommend = (long) 1;
								catalogCourseList.get(j).setIfRecommend(ifRecommend);
							}else{
								Long ifRecommend = (long) 0;
								catalogCourseList.get(j).setIfRecommend(ifRecommend);
							}
							//是否在学习计划
							if(businessService.queryStudyPlan(catalogCourseList.get(j).getId(),employeeId1).size()>0){
								Long ifStudyPlan = (long) 1;
								catalogCourseList.get(i).setIfStudyPlan(ifStudyPlan);
							}else{
								Long ifStudyPlan = (long) 0;
								catalogCourseList.get(j).setIfStudyPlan(ifStudyPlan);
							}
							//判断课程包管理的试卷是否已经回答过
							if(businessService.queryAnswerHistory(catalogCourseList.get(j).getExamId(),employeeId1).size()>0){
								catalogCourseList.get(j).setTestMark("1");
							}else{
								catalogCourseList.get(j).setTestMark("0");
							}
							//学习进度
							if(businessService.queryStudyIfFinished(catalogCourseList.get(j).getId(),employeeId1).size()>0){
								catalogCourseList.get(j).setIsFinished(businessService.queryStudyIfFinished(catalogCourseList.get(j).getId(),employeeId1).get(0).getIsFinished());
							}else{
								Long isFinished = (long) 0;
								catalogCourseList.get(j).setIsFinished(isFinished);
							}
							catalogCourseList.get(j).setAvatar(commonService.appenUrl(catalogCourseList.get(j).getAvatar()));
							catalogCourseList.get(j).setPdfAddress(commonService.appenUrl(catalogCourseList.get(j).getPdfAddress()));
			//				String content =catalogCourseList.get(i).getPdfAddress();
			//				if(!StringUtils.isNullOrEmpty(content)){
			//					//随机生成字母和数字的组合
			//					String pwd = PasswdEncryption.getStringRandom();
			//					StringBuffer stringBuffer = new StringBuffer(commonService.appenUrl(catalogCourseList.get(i).getPdfAddress()));
			//					stringBuffer.insert(50, pwd).toString();
			//					catalogCourseList.get(i).setPdfAddress(stringBuffer.toString().toUpperCase());
			////					catalogList.get(i).setAvatar(stringBuffer.toString().toUpperCase());s
			//				}else{
			//					catalogCourseList.get(i).setAvatar(content);
			//				}
							//根据版本号判断是否加密PDF地址
			//				if(!("1.6.0").equals(version) && !("1.5.9").equals(version)){
								catalogCourseList.get(j).setPlayAddress(commonService.appenUrl(catalogCourseList.get(j).getPlayAddress()));
			//				}else{
//								String content =catalogCourseList.get(j).getPdfAddress();
//								String playAddress = catalogCourseList.get(j).getPlayAddress();
//								//随机生成字母和数字的组合加密录音文件地址
//								String pwd1 = PasswdEncryption.getStringRandom();
//								StringBuffer stringBuffer1 = new StringBuffer(commonService.appenUrl(playAddress));
//								stringBuffer1.insert(50, pwd1).toString();
//								catalogCourseList.get(j).setPlayAddress(stringBuffer1.toString().toUpperCase());
//								//加密PlayAddressM3u8
//								String playAddress1 = catalogCourseList.get(j).getPlayAddressM3u8();
//								String pwd2 = PasswdEncryption.getStringRandom();
//								StringBuffer stringBuffer2 = new StringBuffer(commonService.appenUrl(playAddress1));
//								stringBuffer2.insert(50, pwd2).toString();
//								catalogCourseList.get(j).setPlayAddressM3u8(stringBuffer2.toString().toUpperCase());
//								if(!StringUtils.isNullOrEmpty(content)){
//									//随机生成字母和数字的组合加密PDF地址
//									String pwd = PasswdEncryption.getStringRandom();
//									StringBuffer stringBuffer = new StringBuffer(commonService.appenUrl(catalogCourseList.get(j).getPdfAddress()));
//									stringBuffer.insert(50, pwd).toString();
//									catalogCourseList.get(j).setPdfAddress(stringBuffer.toString().toUpperCase());
//			//						catalogList.get(i).setAvatar(stringBuffer.toString().toUpperCase());
//								}else{
//									catalogCourseList.get(j).setPdfAddress(commonService.appenUrl(content));
//								}
							}
						catalogList.get(i).setCourseList(catalogCourseList);
						//判断是以什么形式展现课程列表
						if(catalogList.get(i).getFlag()=="0" || ("0").equals(catalogList.get(i).getFlag())){
							//修改课程数量
							catalogList.get(i).setCourseCount(businessService.findCatalogListCourseCount(catalogList.get(i).getId()));
							//修改学习人数
							catalogList.get(i).setStudyCount(businessService.findCatalogListStudyCount(catalogList.get(i).getId()));
						}
						//根据课程包ID查询分享链接地址
						String shareAddress = businessService.findShareAddress("h5_course_share_url");
						if(!StringUtils.isNullOrEmpty(shareAddress)){
							shareAddress+="&id="+catalogList.get(i).getCourseId();
						}
						//根据课程简章链接地址
						String synopsisAddress = businessService.findShareAddress("h5_course_synopsis_url");
						if(!StringUtils.isNullOrEmpty(synopsisAddress)){
							synopsisAddress+="&catalogId="+catalogList.get(i).getId();
						}
						catalogList.get(i).setSynopsisAddress(synopsisAddress);
						catalogList.get(i).setShareAddress(shareAddress);
						//查询是否已经被选课
						if(businessService.queryIsChoose(catalogList.get(i).getId(),employeeId1).size()>0){
							catalogList.get(i).setIsChoose("1");
						}else{
							catalogList.get(i).setIsChoose("0");
						}
						//判断课程实际数量是否大于
						if(Integer.parseInt(catalogList.get(i).getTotal())<catalogList.get(i).getCourseCount()){
							catalogList.get(i).setTotal(String.valueOf(catalogList.get(i).getCourseCount()));
						}
					}
		//			}
						return okNew("查询成功", catalogList);
					}else{
						return status(97,"sign验证出错");
					}
			}else{
				if(Long.parseLong(employeeId)!=33){
					LoginDetail lastLogin = memberService.findBusinessLoginDetailById(Long.parseLong(employeeId));
					//查询学员此种设备、此个设备ID上次登录的明细
					LoginDetail loginDetail = memberService.findBusinessLoginDetail(Long.parseLong(employeeId),client,deviceId);
					if(loginDetail!=null){
						Long detailId = loginDetail.getId();
						Long lastId = lastLogin.getId();
						//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，，如果最后一次的大于本机登陆的，那么就是在异地登录了
						if(lastId>detailId){
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
						}
					}
				}
				catalogList=businessService.findParentCatalogById(catalogId);
				Employee employee1 = businessService.findEmployeeDetail(Long.parseLong(employeeId));
				for(int i=0;i<catalogList.size();i++){
					//是否收藏了课程包去
					if(businessService.queryStudyPlanCatalog(catalogList.get(i).getId(),Long.parseLong(employeeId)).size()>0){
						Long ifStudyPlan = (long) 1;
						catalogList.get(i).setIfCollection(ifStudyPlan);
					}else{
						Long ifStudyPlan = (long) 0;
						catalogList.get(i).setIfCollection(ifStudyPlan);
					}
					catalogList.get(i).setIfVideo("0");
					//判断t_business_member_record表中是否有效期内的好多课会员数据存在
					List<MemberRecord> recordList = businessService.findMemberRecordList(employeeId.toString());
					if(recordList.size()>0){
						catalogList.get(i).setIsChapter("1");
					}else{
						//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
						 if(businessService.findMemberRecordListByCatalogId(employeeId.toString(),catalogList.get(i).getId().toString()).size()>0){
							 catalogList.get(i).setIsChapter("1");
						 }else{
							 catalogList.get(i).setIsChapter("0");
						 }
					}
					//判断课程包管理的试卷是否已经回答过
					if(businessService.queryAnswerHistory(catalogList.get(i).getExamId(),Long.parseLong(employeeId)).size()>0){
						catalogList.get(i).setTestMark("1");
					}else{
						catalogList.get(i).setTestMark("0");
					}
					//获取teacherId 的集合
					String ids = catalogList.get(i).getTeacherId();
					List<Teacher> teacherList = new ArrayList<Teacher>();
					StringBuffer sb = new StringBuffer();
					for (String s : ids.split(",")) {
						if (StringUtils.isNullOrEmpty(s)) {
							continue;
						}
						List<Teacher> list=businessService.findTeacherById(Long.parseLong(s));
						if(list.size()>0){
							list.get(0).setAvatar(commonService.appenUrl(list.get(i).getAvatar()));
							list.get(0).setCover(commonService.appenUrl(list.get(i).getCover()));
							teacherList.add(list.get(0));
						}
//						sb.append(service.findTeacherById(Long.parseLong(s)).getStr("name")).append(",");
					}
					catalogList.get(i).setTeacherList(teacherList);
					catalogList.get(i).setAvatar(commonService.appenUrl(catalogList.get(i).getAvatar()));
					catalogList.get(i).setCover(commonService.appenUrl(catalogList.get(i).getCover()));
					catalogList.get(i).getFlag();
					catalogCourseList=businessService.getCatalogCourseList(catalogId);
					//循环遍历集合查询是否是学员的推荐课程和计划学习课程
					for(int j=0;j<catalogCourseList.size();j++){
						//是否推荐
						if(businessService.queryRecommend(catalogCourseList.get(j).getId(),Long.parseLong(employeeId)).size()>0){
							Long ifRecommend = (long) 1;
							catalogCourseList.get(j).setIfRecommend(ifRecommend);
						}else{
							Long ifRecommend = (long) 0;
							catalogCourseList.get(j).setIfRecommend(ifRecommend);
						}
						//是否在学习计划
						if(businessService.queryStudyPlan(catalogCourseList.get(j).getId(),Long.parseLong(employeeId)).size()>0){
							Long ifStudyPlan = (long) 1;
							catalogCourseList.get(i).setIfStudyPlan(ifStudyPlan);
						}else{
							Long ifStudyPlan = (long) 0;
							catalogCourseList.get(j).setIfStudyPlan(ifStudyPlan);
						}
						//判断课程包管理的试卷是否已经回答过
						if(businessService.queryAnswerHistory(catalogCourseList.get(j).getExamId(),Long.parseLong(employeeId)).size()>0){
							catalogCourseList.get(j).setTestMark("1");
						}else{
							catalogCourseList.get(j).setTestMark("0");
						}
						//学习进度
						if(businessService.queryStudyIfFinished(catalogCourseList.get(j).getId(),Long.parseLong(employeeId)).size()>0){
							catalogCourseList.get(j).setIsFinished(businessService.queryStudyIfFinished(catalogCourseList.get(j).getId(),Long.parseLong(employeeId)).get(0).getIsFinished());
						}else{
							Long isFinished = (long) 0;
							catalogCourseList.get(j).setIsFinished(isFinished);
						}
						catalogCourseList.get(j).setAvatar(commonService.appenUrl(catalogCourseList.get(j).getAvatar()));
						catalogCourseList.get(j).setPdfAddress(commonService.appenUrl(catalogCourseList.get(j).getPdfAddress()));
		//				String content =catalogCourseList.get(i).getPdfAddress();
		//				if(!StringUtils.isNullOrEmpty(content)){
		//					//随机生成字母和数字的组合
		//					String pwd = PasswdEncryption.getStringRandom();
		//					StringBuffer stringBuffer = new StringBuffer(commonService.appenUrl(catalogCourseList.get(i).getPdfAddress()));
		//					stringBuffer.insert(50, pwd).toString();
		//					catalogCourseList.get(i).setPdfAddress(stringBuffer.toString().toUpperCase());
		////					catalogList.get(i).setAvatar(stringBuffer.toString().toUpperCase());s
		//				}else{
		//					catalogCourseList.get(i).setAvatar(content);
		//				}
						//根据版本号判断是否加密PDF地址
		//				if(!("1.6.0").equals(version) && !("1.5.9").equals(version)){
		//					catalogCourseList.get(i).setPdfAddress(commonService.appenUrl(catalogCourseList.get(i).getPdfAddress()));
		//				}else{
							String content =catalogCourseList.get(j).getPdfAddress();
							String playAddress = catalogCourseList.get(j).getPlayAddress();
							//随机生成字母和数字的组合加密录音文件地址
							String pwd1 = PasswdEncryption.getStringRandom();
							StringBuffer stringBuffer1 = new StringBuffer(commonService.appenUrl(playAddress));
							stringBuffer1.insert(50, pwd1).toString();
							catalogCourseList.get(j).setPlayAddress(stringBuffer1.toString().toUpperCase());
							//加密PlayAddressM3u8
							String playAddress1 = catalogCourseList.get(j).getPlayAddressM3u8();
							String pwd2 = PasswdEncryption.getStringRandom();
							StringBuffer stringBuffer2 = new StringBuffer(commonService.appenUrl(playAddress1));
							stringBuffer2.insert(50, pwd2).toString();
							catalogCourseList.get(j).setPlayAddressM3u8(stringBuffer2.toString().toUpperCase());
							if(!StringUtils.isNullOrEmpty(content)){
								//随机生成字母和数字的组合加密PDF地址
								String pwd = PasswdEncryption.getStringRandom();
								StringBuffer stringBuffer = new StringBuffer(commonService.appenUrl(catalogCourseList.get(j).getPdfAddress()));
								stringBuffer.insert(50, pwd).toString();
								catalogCourseList.get(j).setPdfAddress(stringBuffer.toString().toUpperCase());
		//						catalogList.get(i).setAvatar(stringBuffer.toString().toUpperCase());
							}else{
								catalogCourseList.get(j).setPdfAddress(commonService.appenUrl(content));
							}
						}
					catalogList.get(i).setCourseList(catalogCourseList);
					//判断是以什么形式展现课程列表
					if(catalogList.get(i).getFlag()=="0" || ("0").equals(catalogList.get(i).getFlag())){
						//修改课程数量
						catalogList.get(i).setCourseCount(businessService.findCatalogListCourseCount(catalogList.get(i).getId()));
						//修改学习人数
						catalogList.get(i).setStudyCount(businessService.findCatalogListStudyCount(catalogList.get(i).getId()));
					}
					//判断课程实际数量是否大于
					if(Integer.parseInt(catalogList.get(i).getTotal())<catalogList.get(i).getCourseCount()){
						catalogList.get(i).setTotal(String.valueOf(catalogList.get(i).getCourseCount()));
					}
				}
//				}
				return ok("查询成功", catalogList);
			
			}
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: findCollectionCatalogList
	 * @Description: (查询学员收藏的系列课列表)
	 * @param employeeId 学员ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findCollectionCatalogList")
	public JSONObject findCollectionCatalogList(Page page,Long employeeId,String client,String version,String deviceId) {
		try {
			List<Catalog> planList = null;
			if(employeeId!=33){
				//查询最近一次登录的信息
				LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employeeId);
				//查询学员此种设备、此个设备ID上次登录的明细
				LoginDetail loginDetail = memberService.findBusinessLoginDetail(employeeId,client,deviceId);
				if(loginDetail!=null){
					Long detailId = loginDetail.getId();
					Long lastId = lastLogin.getId();
					//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，，如果最后一次的大于本机登陆的，那么就是在异地登录了
					if(lastId>detailId){
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
					}
				}
			}
			Employee employee1 = businessService.findEmployeeDetail(employeeId);
			planList = businessService.queryCollectionCatalogList(page,employeeId);
			//循环遍历集合查询是否是学员的推荐课程和计划学习课程
			for(int i=0;i<planList.size();i++){
				//判断是否是只购买了系列课程
				if(employee1.getIfBusiness()!=0L && employee1.getIfBusiness()!=1L){
					if(employee1.getIfBusiness()==planList.get(i).getId()){
						planList.get(i).setIsChapter("1");
					}else{
						planList.get(i).setIsChapter("0");
					}
				}
				//判断课程包管理的试卷是否已经回答过
				if(businessService.queryAnswerHistory(planList.get(i).getExamId(),employeeId).size()>0){
					planList.get(i).setTestMark("1");
				}else{
					planList.get(i).setTestMark("0");
				}
				//获取teacherId 的集合
//				String ids = catalogList.get(i).getTeacherId();
//				List<Teacher> teacherList = new ArrayList<Teacher>();
//				StringBuffer sb = new StringBuffer();
//				for (String s : ids.split(",")) {
//					if (StringUtils.isNullOrEmpty(s)) {
//						continue;
//					}
//					List<Teacher> list=businessService.findTeacherById(Long.parseLong(s));
//					if(list.size()>0){
//						teacherList.add(list.get(0));
//					}
////					sb.append(service.findTeacherById(Long.parseLong(s)).getStr("name")).append(",");
//				}
//				catalogList.get(i).setTeacherList(teacherList);
				planList.get(i).setAvatar(commonService.appenUrl(planList.get(i).getAvatar()));
				planList.get(i).setCover(commonService.appenUrl(planList.get(i).getCover()));
				planList.get(i).getFlag();
				
				//判断是以什么形式展现课程列表
				if(planList.get(i).getFlag()=="0" || ("0").equals(planList.get(i).getFlag())){
					//修改课程数量
					planList.get(i).setCourseCount(businessService.findCatalogListCourseCount(planList.get(i).getId()));
					//修改学习人数
					planList.get(i).setStudyCount(businessService.findCatalogListStudyCount(planList.get(i).getId()));
				}
				}
//			}
			return ok("查询成功",planList);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: findTeachersList
	 * @Description: (查询所有讲师)
	 * @param page 分页
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findTeacherFirstTypeCourse")
	public JSONObject findTeacherFirstTypeCourse(Long teacherId,Long catalogId,String employeeId,String client,String version,String deviceId,String sign) {
		try {
			try {
				List<Teacher> teachersList = null;
				if(("2.0.5").equals(version) || ("2.0.1").equals(version)||("2.0.4").equals(version)){
					if(StringUtils.isNullOrEmpty(employeeId) || StringUtils.isNullOrEmpty(deviceId) || StringUtils.isNullOrEmpty(sign)){
						return status(99,"参数错误");
					}
					//解密
					UpAES upAES = new UpAES();
//					upAES.entry("1");
					String newDeviceId =upAES.disEntry(deviceId);
					String newEmployeeId =upAES.disEntry(employeeId);
					System.out.println("解密后的employeeId："+newEmployeeId);
					System.out.println("解密后的deviceId："+newDeviceId);
					//重新赋值deviceId
					String newSign = PasswdEncryption.MD5(newEmployeeId+newDeviceId);
					newSign= newSign.substring(5, newSign.length()-5);
					System.out.println("得到的新Sign"+newSign);
					Long employeeId1 = Long.parseLong(newEmployeeId);
					if(newSign.equals(sign)){
						if(employeeId1!=33){
							//查询最近一次登录的信息
							LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employeeId1);
							//查询学员此种设备、此个设备ID上次登录的明细
							LoginDetail loginDetail = memberService.findBusinessLoginDetail(employeeId1,client,newDeviceId);
							if(loginDetail!=null){
								Long detailId = loginDetail.getId();
								Long lastId = lastLogin.getId();
								//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，，如果最后一次的大于本机登陆的，那么就是在异地登录了
								if(lastId>detailId){
									SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
								}
							}
						}
						teachersList=businessService.findTeacherById(teacherId);
						for(int z=0;z<teachersList.size();z++){
							List<CatalogCourseList> catalogCourseList = null;
							List<Catalog> catalogList = null;
							catalogList=businessService.findTeacherParentCatalogById(catalogId);
							Employee employee1 = businessService.findEmployeeDetail(employeeId1);
							for(int i=0;i<catalogList.size();i++){
								//是否收藏了课程包去
								if(businessService.queryStudyPlanCatalog(catalogList.get(i).getId(),employeeId1).size()>0){
									Long ifStudyPlan = (long) 1;
									catalogList.get(i).setIfCollection(ifStudyPlan);
								}else{
									Long ifStudyPlan = (long) 0;
									catalogList.get(i).setIfCollection(ifStudyPlan);
								}
								//判断是否是只购买了系列课程
								if(employee1.getIfBusiness()!=0L && employee1.getIfBusiness()!=1L){
									if(employee1.getIfBusiness()==catalogList.get(i).getId()){
										catalogList.get(i).setIsChapter("1");
									}else{
										catalogList.get(i).setIsChapter("0");
									}
								}
								catalogList.get(i).setAvatar(commonService.appenUrl(catalogList.get(i).getAvatar()));
								catalogList.get(i).getFlag();
								catalogCourseList=businessService.getCatalogCourseList(catalogId);
								//循环遍历集合查询是否是学员的推荐课程和计划学习课程
								for(int j=0;j<catalogCourseList.size();j++){
									//是否推荐
									if(businessService.queryRecommend(catalogCourseList.get(j).getId(),employeeId1).size()>0){
										Long ifRecommend = (long) 1;
										catalogCourseList.get(j).setIfRecommend(ifRecommend);
									}else{
										Long ifRecommend = (long) 0;
										catalogCourseList.get(j).setIfRecommend(ifRecommend);
									}
									//是否在学习计划
									if(businessService.queryStudyPlan(catalogCourseList.get(j).getId(),employeeId1).size()>0){
										Long ifStudyPlan = (long) 1;
										catalogCourseList.get(i).setIfStudyPlan(ifStudyPlan);
									}else{
										Long ifStudyPlan = (long) 0;
										catalogCourseList.get(j).setIfStudyPlan(ifStudyPlan);
									}
									//学习进度
									if(businessService.queryStudyIfFinished(catalogCourseList.get(j).getId(),employeeId1).size()>0){
										catalogCourseList.get(j).setIsFinished(businessService.queryStudyIfFinished(catalogCourseList.get(j).getId(),employeeId1).get(0).getIsFinished());
									}else{
										Long isFinished = (long) 0;
										catalogCourseList.get(j).setIsFinished(isFinished);
									}
									catalogCourseList.get(j).setAvatar(commonService.appenUrl(catalogCourseList.get(j).getAvatar()));
									catalogCourseList.get(j).setPdfAddress(commonService.appenUrl(catalogCourseList.get(j).getPdfAddress()));
					//				String content =catalogCourseList.get(i).getPdfAddress();
					//				if(!StringUtils.isNullOrEmpty(content)){
					//					//随机生成字母和数字的组合
					//					String pwd = PasswdEncryption.getStringRandom();
					//					StringBuffer stringBuffer = new StringBuffer(commonService.appenUrl(catalogCourseList.get(i).getPdfAddress()));
					//					stringBuffer.insert(50, pwd).toString();
					//					catalogCourseList.get(i).setPdfAddress(stringBuffer.toString().toUpperCase());
					////					catalogList.get(i).setAvatar(stringBuffer.toString().toUpperCase());s
					//				}else{
					//					catalogCourseList.get(i).setAvatar(content);
					//				}
									//根据版本号判断是否加密PDF地址
					//				if(!("1.6.0").equals(version) && !("1.5.9").equals(version)){
					//					catalogCourseList.get(i).setPdfAddress(commonService.appenUrl(catalogCourseList.get(i).getPdfAddress()));
					//				}else{
											catalogCourseList.get(j).setPdfAddress(commonService.appenUrl(catalogCourseList.get(j).getPdfAddress()));
										//判断课程包管理的试卷是否已经回答过
										if(businessService.queryAnswerHistory(catalogCourseList.get(j).getExamId(),employeeId1).size()>0){
											catalogCourseList.get(j).setTestMark("1");
										}else{
											catalogCourseList.get(j).setTestMark("0");
										}
									}
								catalogList.get(i).setCourseList(catalogCourseList);
								//判断是以什么形式展现课程列表
								if(catalogList.get(i).getFlag()=="0" || ("0").equals(catalogList.get(i).getFlag())){
									//修改课程数量
									catalogList.get(i).setCourseCount(businessService.findCatalogListCourseCount(catalogList.get(i).getId()));
									//修改学习人数
									catalogList.get(i).setStudyCount(businessService.findCatalogListStudyCount(catalogList.get(i).getId()));
								}
								//判断t_business_member_record表中是否有效期内的好多课会员数据存在
								List<MemberRecord> recordList = businessService.findMemberRecordList(employeeId1.toString());
								if(recordList.size()>0){
									catalogList.get(i).setIsChapter("1");
								}else{
									//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
									 if(businessService.findMemberRecordListByCatalogId(employeeId1.toString(),catalogList.get(i).getId().toString()).size()>0){
										 catalogList.get(i).setIsChapter("1");
									 }else{
										 catalogList.get(i).setIsChapter("0");
									 }
								}
							}
							teachersList.get(z).setCover(commonService.appenUrl(teachersList.get(z).getCover()));
							teachersList.get(z).setCover1(commonService.appenUrl(teachersList.get(z).getCover1()));
							teachersList.get(z).setAvatar(commonService.appenUrl(teachersList.get(z).getAvatar()));
							teachersList.get(z).setCatalogList(catalogList);
							//根据课程包ID查询分享链接地址
							String shareAddress = businessService.findShareAddress("h5_teacher_share_url");
							if(!StringUtils.isNullOrEmpty(shareAddress)){
								shareAddress+="&teacherId="+teachersList.get(z).getId()+"&teacherName="+teachersList.get(z).getName();
							}
							teachersList.get(z).setShareAddress(shareAddress);
						}
						return okNew("查询成功", teachersList);
					}else{
						return status(97,"sign验证出错");
					}
				}else{
					teachersList=businessService.findTeacherById(teacherId);
					for(int z=0;z<teachersList.size();z++){
						List<CatalogCourseList> catalogCourseList = null;
						List<Catalog> catalogList = null;
						catalogList=businessService.findTeacherParentCatalogById(catalogId);
						Employee employee1 = businessService.findEmployeeDetail(Long.parseLong(employeeId));
						for(int i=0;i<catalogList.size();i++){
							//是否收藏了课程包去
							if(businessService.queryStudyPlanCatalog(catalogList.get(i).getId(),Long.parseLong(employeeId)).size()>0){
								Long ifStudyPlan = (long) 1;
								catalogList.get(i).setIfCollection(ifStudyPlan);
							}else{
								Long ifStudyPlan = (long) 0;
								catalogList.get(i).setIfCollection(ifStudyPlan);
							}
							//判断是否是只购买了系列课程
							if(employee1.getIfBusiness()!=0L && employee1.getIfBusiness()!=1L){
								if(employee1.getIfBusiness()==catalogList.get(i).getId()){
									catalogList.get(i).setIsChapter("1");
								}else{
									catalogList.get(i).setIsChapter("0");
								}
							}
							catalogList.get(i).setAvatar(commonService.appenUrl(catalogList.get(i).getAvatar()));
							catalogList.get(i).getFlag();
							catalogCourseList=businessService.getCatalogCourseList(catalogId);
							//循环遍历集合查询是否是学员的推荐课程和计划学习课程
							for(int j=0;j<catalogCourseList.size();j++){
								//是否推荐
								if(businessService.queryRecommend(catalogCourseList.get(j).getId(),Long.parseLong(employeeId)).size()>0){
									Long ifRecommend = (long) 1;
									catalogCourseList.get(j).setIfRecommend(ifRecommend);
								}else{
									Long ifRecommend = (long) 0;
									catalogCourseList.get(j).setIfRecommend(ifRecommend);
								}
								//是否在学习计划
								if(businessService.queryStudyPlan(catalogCourseList.get(j).getId(),Long.parseLong(employeeId)).size()>0){
									Long ifStudyPlan = (long) 1;
									catalogCourseList.get(i).setIfStudyPlan(ifStudyPlan);
								}else{
									Long ifStudyPlan = (long) 0;
									catalogCourseList.get(j).setIfStudyPlan(ifStudyPlan);
								}
								//学习进度
								if(businessService.queryStudyIfFinished(catalogCourseList.get(j).getId(),Long.parseLong(employeeId)).size()>0){
									catalogCourseList.get(j).setIsFinished(businessService.queryStudyIfFinished(catalogCourseList.get(j).getId(),Long.parseLong(employeeId)).get(0).getIsFinished());
								}else{
									Long isFinished = (long) 0;
									catalogCourseList.get(j).setIsFinished(isFinished);
								}
								catalogCourseList.get(j).setAvatar(commonService.appenUrl(catalogCourseList.get(j).getAvatar()));
								catalogCourseList.get(j).setPdfAddress(commonService.appenUrl(catalogCourseList.get(j).getPdfAddress()));
				//				String content =catalogCourseList.get(i).getPdfAddress();
				//				if(!StringUtils.isNullOrEmpty(content)){
				//					//随机生成字母和数字的组合
				//					String pwd = PasswdEncryption.getStringRandom();
				//					StringBuffer stringBuffer = new StringBuffer(commonService.appenUrl(catalogCourseList.get(i).getPdfAddress()));
				//					stringBuffer.insert(50, pwd).toString();
				//					catalogCourseList.get(i).setPdfAddress(stringBuffer.toString().toUpperCase());
				////					catalogList.get(i).setAvatar(stringBuffer.toString().toUpperCase());s
				//				}else{
				//					catalogCourseList.get(i).setAvatar(content);
				//				}
								//根据版本号判断是否加密PDF地址
				//				if(!("1.6.0").equals(version) && !("1.5.9").equals(version)){
				//					catalogCourseList.get(i).setPdfAddress(commonService.appenUrl(catalogCourseList.get(i).getPdfAddress()));
				//				}else{
									String content =catalogCourseList.get(j).getPdfAddress();
									String playAddress = catalogCourseList.get(j).getPlayAddress();
									//随机生成字母和数字的组合加密录音文件地址
									String pwd1 = PasswdEncryption.getStringRandom();
									StringBuffer stringBuffer1 = new StringBuffer(commonService.appenUrl(playAddress));
									stringBuffer1.insert(50, pwd1).toString();
									catalogCourseList.get(i).setPlayAddress(stringBuffer1.toString().toUpperCase());
									//加密PlayAddressM3u8
				//					String playAddress1 = catalogCourseList.get(i).getPlayAddressM3u8();
				//					String pwd2 = PasswdEncryption.getStringRandom();
				//					StringBuffer stringBuffer2 = new StringBuffer(commonService.appenUrl(playAddress1));
				//					stringBuffer2.insert(50, pwd2).toString();
				//					catalogCourseList.get(i).setPlayAddressM3u8(stringBuffer2.toString().toUpperCase());
									if(!StringUtils.isNullOrEmpty(content)){
										//随机生成字母和数字的组合加密PDF地址
										String pwd = PasswdEncryption.getStringRandom();
										StringBuffer stringBuffer = new StringBuffer(commonService.appenUrl(catalogCourseList.get(j).getPdfAddress()));
										stringBuffer.insert(50, pwd).toString();
										catalogCourseList.get(j).setPdfAddress(stringBuffer.toString().toUpperCase());
				//						catalogList.get(i).setAvatar(stringBuffer.toString().toUpperCase());
									}else{
										catalogCourseList.get(j).setPdfAddress(commonService.appenUrl(content));
									}
									//判断课程包管理的试卷是否已经回答过
									if(businessService.queryAnswerHistory(catalogCourseList.get(j).getExamId(),Long.parseLong(employeeId)).size()>0){
										catalogCourseList.get(j).setTestMark("1");
									}else{
										catalogCourseList.get(j).setTestMark("0");
									}
								}
							catalogList.get(i).setCourseList(catalogCourseList);
							//判断是以什么形式展现课程列表
							if(catalogList.get(i).getFlag()=="0" || ("0").equals(catalogList.get(i).getFlag())){
								//修改课程数量
								catalogList.get(i).setCourseCount(businessService.findCatalogListCourseCount(catalogList.get(i).getId()));
								//修改学习人数
								catalogList.get(i).setStudyCount(businessService.findCatalogListStudyCount(catalogList.get(i).getId()));
							}
							//判断t_business_member_record表中是否有效期内的好多课会员数据存在
							List<MemberRecord> recordList = businessService.findMemberRecordList(employeeId);
							if(recordList.size()>0){
								catalogList.get(i).setIsChapter("1");
							}else{
								//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
								 if(businessService.findMemberRecordListByCatalogId(employeeId,catalogList.get(i).getId().toString()).size()>0){
									 catalogList.get(i).setIsChapter("1");
								 }else{
									 catalogList.get(i).setIsChapter("0");
								 }
							}
						}
						teachersList.get(z).setCover(commonService.appenUrl(teachersList.get(z).getCover()));
						teachersList.get(z).setCover1(commonService.appenUrl(teachersList.get(z).getCover1()));
						teachersList.get(z).setAvatar(commonService.appenUrl(teachersList.get(z).getAvatar()));
						teachersList.get(z).setCatalogList(catalogList);
						//根据课程包ID查询分享链接地址
						String shareAddress = businessService.findShareAddress("h5_teacher_share_url");
						if(!StringUtils.isNullOrEmpty(shareAddress)){
							shareAddress+="&teacherId="+teachersList.get(z).getId()+"&teacherName="+teachersList.get(z).getName();
						}
						teachersList.get(z).setShareAddress(shareAddress);
					}
					return ok("查询成功", teachersList);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return error(APP_SYSTEM_ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: findTeachersList
	 * @Description: (查询所有讲师)
	 * @param page 分页
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findTeacherSecondTypeCourse")
	public JSONObject findTeacherSecondTypeCourse(Page page,Long teacherId,Long catalogId,String employeeId,Long parentId,String client,String version,String deviceId,String sign) {
		try {
				List<Teacher> teachersList = null;
				if(("2.0.5").equals(version) || ("2.0.1").equals(version)||("2.0.4").equals(version)){
					if(StringUtils.isNullOrEmpty(employeeId) || StringUtils.isNullOrEmpty(deviceId) || StringUtils.isNullOrEmpty(sign)){
						return status(99,"参数错误");
					}
					//解密
					UpAES upAES = new UpAES();
					String newDeviceId =upAES.disEntry(deviceId.toString());
					String newEmployeeId =upAES.disEntry(employeeId);
					System.out.println("解密后的employeeId："+newEmployeeId);
					System.out.println("解密后的deviceId："+newDeviceId);
					//重新赋值deviceId
					String newSign = PasswdEncryption.MD5(newEmployeeId+newDeviceId);
					newSign= newSign.substring(5, newSign.length()-5);
					System.out.println("得到的新Sign"+newSign);
					Long employeeId1 = Long.parseLong(newEmployeeId);
					if(newSign.equals(sign)){
						if(employeeId1!=33){
							//查询最近一次登录的信息
							LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employeeId1);
							//查询学员此种设备、此个设备ID上次登录的明细
							LoginDetail loginDetail = memberService.findBusinessLoginDetail(employeeId1,client,newDeviceId);
							if(loginDetail!=null){
								Long detailId = loginDetail.getId();
								Long lastId = lastLogin.getId();
								//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，，如果最后一次的大于本机登陆的，那么就是在异地登录了
								if(lastId>detailId){
									SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
								}
							}
						}
						teachersList=businessService.findTeacherById(teacherId);
						List<Catalog> catalogList1 = null;
						List<SecondCatalog> catalogList = null;
						for(int z=0;z<teachersList.size();z++){
								catalogList1=businessService.findTeacherParentCatalogById(catalogId);
								Employee employee1 = businessService.findEmployeeDetail(employeeId1);
								for(int i=0;i<catalogList1.size();i++){
									//判断是否是只购买了系列课程
									if(employee1.getIfBusiness()!=0L && employee1.getIfBusiness()!=1L){
										if(employee1.getIfBusiness()==catalogList1.get(i).getId()){
											catalogList1.get(i).setIsChapter("1");
										}else{
											catalogList1.get(i).setIsChapter("0");
										}
									}
									//是否收藏了课程包
									if(businessService.queryStudyPlanCatalog(catalogList1.get(i).getId(),employeeId1).size()>0){
										Long ifStudyPlan = (long) 1;
										catalogList1.get(i).setIfCollection(ifStudyPlan);
									}else{
										Long ifStudyPlan = (long) 0;
										catalogList1.get(i).setIfCollection(ifStudyPlan);
									}
									catalogList1.get(i).setAvatar(commonService.appenUrl(catalogList1.get(i).getAvatar()));
									catalogList1.get(i).getFlag();
									//查询一级目录下的二级目录列表
									catalogList=businessService.findSecondCatalogList(page,catalogId);
									catalogList1.get(i).setSecondCatalogList(catalogList);
									List<CatalogCourseList> catalogCourseList = null;
									catalogCourseList=businessService.getAllCatalogCourseList(catalogList1.get(i).getId(),parentId);
									for(int k=0;k<catalogCourseList.size();k++){
										catalogCourseList.get(k).setPdfAddress(commonService.appenUrl(catalogCourseList.get(k).getPdfAddress()));
										catalogCourseList.get(k).setAvatar(commonService.appenUrl(catalogCourseList.get(k).getAvatar()));
										//判断课程包管理的试卷是否已经回答过
										if(businessService.queryAnswerHistory(catalogCourseList.get(k).getExamId(),employeeId1).size()>0){
											catalogCourseList.get(k).setTestMark("1");
										}else{
											catalogCourseList.get(k).setTestMark("0");
										}
									}
									
									catalogList1.get(i).setCourseList(catalogCourseList);
									catalogList1.get(i).setSecondCatalogList(catalogList);
									//判断是以什么形式展现课程列表
									if(catalogList1.get(i).getFlag()=="0" || ("0").equals(catalogList1.get(i).getFlag())){
										//修改课程数量
										catalogList1.get(i).setCourseCount(businessService.findCatalogListCourseCount(catalogList1.get(i).getId()));
										//修改学习人数
										catalogList1.get(i).setStudyCount(businessService.findCatalogListStudyCount(catalogList1.get(i).getId()));
									}
									//判断t_business_member_record表中是否有效期内的好多课会员数据存在
									List<MemberRecord> recordList = businessService.findMemberRecordList(employeeId1.toString());
									if(recordList.size()>0){
										catalogList1.get(i).setIsChapter("1");
									}else{
										//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
										 if(businessService.findMemberRecordListByCatalogId(employeeId1.toString(),catalogList.get(i).getId().toString()).size()>0){
											 catalogList1.get(i).setIsChapter("1");
										 }else{
											 catalogList1.get(i).setIsChapter("0");
										 }
									}
								}
								teachersList.get(z).setCatalogList(catalogList1);
								teachersList.get(z).setCover(commonService.appenUrl(teachersList.get(z).getCover()));
								teachersList.get(z).setCover1(commonService.appenUrl(teachersList.get(z).getCover1()));
								teachersList.get(z).setAvatar(commonService.appenUrl(teachersList.get(z).getAvatar()));
								//根据课程包ID查询分享链接地址
								String shareAddress = businessService.findShareAddress("h5_teacher_share_url");
								if(!StringUtils.isNullOrEmpty(shareAddress)){
									shareAddress+="&teacherId="+teachersList.get(z).getId()+"&teacherName="+teachersList.get(z).getName();
								}
								teachersList.get(z).setShareAddress(shareAddress);
							}
						return okNew("查询成功", teachersList);
					}else{
						return status(97,"sign验证出错");
					}
				}else{
					teachersList=businessService.findTeacherById(teacherId);
					List<Catalog> catalogList1 = null;
					List<SecondCatalog> catalogList = null;
					for(int z=0;z<teachersList.size();z++){
							if(Long.parseLong(employeeId)!=33){
								//查询最近一次登录的信息
								LoginDetail lastLogin = memberService.findBusinessLoginDetailById(Long.parseLong(employeeId));
								//查询学员此种设备、此个设备ID上次登录的明细
								LoginDetail loginDetail = memberService.findBusinessLoginDetail(Long.parseLong(employeeId),client,deviceId);
								if(loginDetail!=null){
									Long detailId = loginDetail.getId();
									Long lastId = lastLogin.getId();
									//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，，如果最后一次的大于本机登陆的，那么就是在异地登录了
									if(lastId>detailId){
										SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
									}
								}
							}
							catalogList1=businessService.findTeacherParentCatalogById(catalogId);
							Employee employee1 = businessService.findEmployeeDetail(Long.parseLong(employeeId));
							for(int i=0;i<catalogList1.size();i++){
								//判断是否是只购买了系列课程
								if(employee1.getIfBusiness()!=0L && employee1.getIfBusiness()!=1L){
									if(employee1.getIfBusiness()==catalogList1.get(i).getId()){
										catalogList1.get(i).setIsChapter("1");
									}else{
										catalogList1.get(i).setIsChapter("0");
									}
								}
								//是否收藏了课程包
								if(businessService.queryStudyPlanCatalog(catalogList1.get(i).getId(),Long.parseLong(employeeId)).size()>0){
									Long ifStudyPlan = (long) 1;
									catalogList1.get(i).setIfCollection(ifStudyPlan);
								}else{
									Long ifStudyPlan = (long) 0;
									catalogList1.get(i).setIfCollection(ifStudyPlan);
								}
								catalogList1.get(i).setAvatar(commonService.appenUrl(catalogList1.get(i).getAvatar()));
								catalogList1.get(i).getFlag();
								//查询一级目录下的二级目录列表
								catalogList=businessService.findSecondCatalogList(page,catalogId);
								catalogList1.get(i).setSecondCatalogList(catalogList);
								List<CatalogCourseList> catalogCourseList = null;
								catalogCourseList=businessService.getAllCatalogCourseList(catalogList1.get(i).getId(),parentId);
								for(int k=0;k<catalogCourseList.size();k++){
									String content =catalogCourseList.get(k).getPdfAddress();
									String playAddress = catalogCourseList.get(k).getPlayAddress();
									//随机生成字母和数字的组合加密录音文件地址
									String pwd1 = PasswdEncryption.getStringRandom();
									StringBuffer stringBuffer1 = new StringBuffer(commonService.appenUrl(playAddress));
									stringBuffer1.insert(50, pwd1).toString();
									catalogCourseList.get(k).setPlayAddress(stringBuffer1.toString().toUpperCase());
									//加密PlayAddressM3u8
									String playAddress1 = catalogCourseList.get(k).getPlayAddressM3u8();
									String pwd2 = PasswdEncryption.getStringRandom();
									StringBuffer stringBuffer2 = new StringBuffer(commonService.appenUrl(playAddress1));
									stringBuffer2.insert(50, pwd2).toString();
									catalogCourseList.get(k).setPlayAddressM3u8(stringBuffer2.toString().toUpperCase());
									if(!StringUtils.isNullOrEmpty(content)){
										//随机生成字母和数字的组合加密PDF地址
										String pwd = PasswdEncryption.getStringRandom();
										StringBuffer stringBuffer = new StringBuffer(commonService.appenUrl(catalogCourseList.get(k).getPdfAddress()));
										stringBuffer.insert(50, pwd).toString();
										catalogCourseList.get(k).setPdfAddress(stringBuffer.toString().toUpperCase());
//										catalogList.get(i).setAvatar(stringBuffer.toString().toUpperCase());
									}else{
										catalogCourseList.get(k).setPdfAddress(commonService.appenUrl(content));
									}
									catalogCourseList.get(k).setAvatar(commonService.appenUrl(catalogCourseList.get(k).getAvatar()));
									//判断课程包管理的试卷是否已经回答过
									if(businessService.queryAnswerHistory(catalogCourseList.get(k).getExamId(),Long.parseLong(employeeId)).size()>0){
										catalogCourseList.get(k).setTestMark("1");
									}else{
										catalogCourseList.get(k).setTestMark("0");
									}
								}
								
								catalogList1.get(i).setCourseList(catalogCourseList);
								catalogList1.get(i).setSecondCatalogList(catalogList);
								//判断是以什么形式展现课程列表
								if(catalogList1.get(i).getFlag()=="0" || ("0").equals(catalogList1.get(i).getFlag())){
									//修改课程数量
									catalogList1.get(i).setCourseCount(businessService.findCatalogListCourseCount(catalogList1.get(i).getId()));
									//修改学习人数
									catalogList1.get(i).setStudyCount(businessService.findCatalogListStudyCount(catalogList1.get(i).getId()));
								}
								//判断t_business_member_record表中是否有效期内的好多课会员数据存在
								List<MemberRecord> recordList = businessService.findMemberRecordList(employeeId.toString());
								if(recordList.size()>0){
									catalogList1.get(i).setIsChapter("1");
								}else{
									//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
									 if(businessService.findMemberRecordListByCatalogId(employeeId,catalogList.get(i).getId().toString()).size()>0){
										 catalogList1.get(i).setIsChapter("1");
									 }else{
										 catalogList1.get(i).setIsChapter("0");
									 }
								}
							}
							teachersList.get(z).setCatalogList(catalogList1);
							teachersList.get(z).setCover(commonService.appenUrl(teachersList.get(z).getCover()));
							teachersList.get(z).setCover1(commonService.appenUrl(teachersList.get(z).getCover1()));
							teachersList.get(z).setAvatar(commonService.appenUrl(teachersList.get(z).getAvatar()));
							//根据课程包ID查询分享链接地址
							String shareAddress = businessService.findShareAddress("h5_teacher_share_url");
							if(!StringUtils.isNullOrEmpty(shareAddress)){
								shareAddress+="&teacherId="+teachersList.get(z).getId()+"&teacherName="+teachersList.get(z).getName();
							}
							teachersList.get(z).setShareAddress(shareAddress);
						}
					return ok("查询成功", teachersList);
				}
						
						
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: findCourseListByTeacher
	 * @Description: (根据讲师姓名查询课程)
	 * @param teacher 讲师姓名
	 * @param employeeId 企业学员ID
	 * @param page 分页
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findCourseListByTeacherName")
	public JSONObject findCourseListByTeacherName(String teacher,Long employeeId,Page page,String client,String version) {
		try {
			try {
				List<CatalogCourseList> courseList = null;
				courseList=businessService.findCourseListByTeacher(teacher,page);
				//循环遍历集合查询是否是学员的推荐课程和计划学习课程
				for(int i=0;i<courseList.size();i++){
					//是否推荐
					if(businessService.queryRecommend(courseList.get(i).getId(),employeeId).size()>0){
						Long ifRecommend = (long) 1;
						courseList.get(i).setIfRecommend(ifRecommend);
					}else{
						Long ifRecommend = (long) 0;
						courseList.get(i).setIfRecommend(ifRecommend);
					}
					//是否在学习计划
					if(businessService.queryStudyPlan(courseList.get(i).getId(),employeeId).size()>0){
						Long ifStudyPlan = (long) 1;
						courseList.get(i).setIfStudyPlan(ifStudyPlan);
					}else{
						Long ifStudyPlan = (long) 0;
						courseList.get(i).setIfStudyPlan(ifStudyPlan);
					}
					courseList.get(i).setAvatar(commonService.appenUrl(courseList.get(i).getAvatar()));
					//根据版本号判断是否加密PDF地址
//					if(!("1.6.0").equals(version) && !("1.5.9").equals(version)){
//						courseList.get(i).setAvatar(commonService.appenUrl(courseList.get(i).getAvatar()));
//						courseList.get(i).setPdfAddress(commonService.appenUrl(courseList.get(i).getPdfAddress()));
//					}else{
						String content =courseList.get(i).getPdfAddress();
						String playAddress = courseList.get(i).getPlayAddress();
						//随机生成字母和数字的组合加密录音文件地址
						String pwd1 = PasswdEncryption.getStringRandom();
						StringBuffer stringBuffer1 = new StringBuffer(commonService.appenUrl(playAddress));
						stringBuffer1.insert(50, pwd1).toString();
						courseList.get(i).setPlayAddress(stringBuffer1.toString().toUpperCase());
						//加密PlayAddressM3u8
//						String playAddress1 = courseList.get(i).getPlayAddressM3u8();
//						String pwd2 = PasswdEncryption.getStringRandom();
//						StringBuffer stringBuffer2 = new StringBuffer(commonService.appenUrl(playAddress1));
//						stringBuffer2.insert(50, pwd2).toString();
//						courseList.get(i).setPlayAddressM3u8(stringBuffer2.toString().toUpperCase());
						if(!StringUtils.isNullOrEmpty(content)){
							//随机生成字母和数字的组合加密PDF地址
							String pwd = PasswdEncryption.getStringRandom();
							StringBuffer stringBuffer = new StringBuffer(commonService.appenUrl(courseList.get(i).getPdfAddress()));
							stringBuffer.insert(50, pwd).toString();
							courseList.get(i).setPdfAddress(stringBuffer.toString().toUpperCase());
//							catalogList.get(i).setAvatar(stringBuffer.toString().toUpperCase());
						}else{
							courseList.get(i).setPdfAddress(commonService.appenUrl(content));
						}
					}
//				}
				return ok("查询成功", courseList);
			} catch (Exception e) {
				e.printStackTrace();
				return error(APP_SYSTEM_ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: getParentCatalogListByTeacher
	 * @Description: (查询讲师所在的课程一级分类列表)
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getParentCatalogListByTeacher")
	public JSONObject getParentCatalogListByTeacher(Page page,String employeeId,String teacherName,String client,String version,String deviceId,String sign) {
		try {
			List<Catalog> catalogList = null;
			if(("2.0.5").equals(version)||("2.0.1").equals(version)||("2.0.4").equals(version)){
				if(StringUtils.isNullOrEmpty(employeeId) || StringUtils.isNullOrEmpty(teacherName) || StringUtils.isNullOrEmpty(deviceId) || StringUtils.isNullOrEmpty(sign)){
					return status(99,"参数错误");
				}
				//解密
				UpAES upAES = new UpAES();
//				upAES.entry("1");
				String newDeviceId =upAES.disEntry(deviceId.toString());
				String newEmployeeId =upAES.disEntry(employeeId.toString());
				System.out.println("解密后的employeeId："+newEmployeeId);
				System.out.println("解密后的deviceId："+newDeviceId);
				//重新赋值deviceId
				String newSign = PasswdEncryption.MD5(newEmployeeId+newDeviceId);
				newSign= newSign.substring(5, newSign.length()-5);
				System.out.println("得到的新Sign"+newSign);
					Long employeeId1 = Long.parseLong(newEmployeeId);
					if(newSign.equals(sign)){
					if(employeeId1!=33){
						//查询最近一次登录的信息
						LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employeeId1);
						//查询学员此种设备、此个设备ID上次登录的明细
						LoginDetail loginDetail = memberService.findBusinessLoginDetail(employeeId1,client,newDeviceId);
						if(loginDetail!=null){
							Long detailId = loginDetail.getId();
							Long lastId = lastLogin.getId();
							//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，，如果最后一次的大于本机登陆的，那么就是在异地登录了
							if(lastId>detailId){
								SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
							}
						}
					}
					Employee employee1 = businessService.findEmployeeDetail(employeeId1);
	//				catalogList=businessService.findParentCatalogList(page);
					catalogList=businessService.findParentCatalogListByTeacher(page,teacherName);
					for(int i=0;i<catalogList.size();i++){
						//判断是否是只购买了系列课程
						if(employee1.getIfBusiness()!=0L && employee1.getIfBusiness()!=1L){
							if(employee1.getIfBusiness()==catalogList.get(i).getId()){
								catalogList.get(i).setIsChapter("1");
							}else{
								catalogList.get(i).setIsChapter("0");
							}
						}
						//如果课程包type=2则为视频课程包
						if("2".equals(catalogList.get(i).getType())){
							catalogList.get(i).setIfVideo("1");
						}else{
							catalogList.get(i).setIfVideo("0");
						}
						//是否收藏了课程包去
						if(businessService.queryStudyPlanCatalog(catalogList.get(i).getId(),employeeId1).size()>0){
							Long ifStudyPlan = (long) 1;
							catalogList.get(i).setIfCollection(ifStudyPlan);
						}else{
							Long ifStudyPlan = (long) 0;
							catalogList.get(i).setIfCollection(ifStudyPlan);
						}
						catalogList.get(i).setAvatar(commonService.appenUrl(catalogList.get(i).getAvatar()));
						catalogList.get(i).setCover(commonService.appenUrl(catalogList.get(i).getCover()));
						catalogList.get(i).setListCover(commonService.appenUrl(catalogList.get(i).getListCover()));
						catalogList.get(i).getFlag();
						//判断是以什么形式展现课程列表
						if(catalogList.get(i).getFlag()=="0" || ("0").equals(catalogList.get(i).getFlag())){
							//修改课程数量
							catalogList.get(i).setCourseCount(businessService.findCatalogListCourseCount(catalogList.get(i).getId()));
							//修改学习人数
							catalogList.get(i).setStudyCount(businessService.findCatalogListStudyCount(catalogList.get(i).getId()));
						}
						//判断t_business_member_record表中是否有效期内的好多课会员数据存在
						List<MemberRecord> recordList = businessService.findMemberRecordList(employeeId1.toString());
						//根据课程包Id查询推荐列表
						List<RecommendInfo> recommendList = businessService.findRecommendList(catalogList.get(i).getId(),"2");
						String os = "0";
						if("iOS".equals(client)){
							os = "1";
						}else if("android".equals(client)){
							os = "2";
						}
						//先判断课程是不是被推荐的课程
						if(recommendList.size()>0 && (recommendList.get(0).getOs().equals("0") || recommendList.get(0).getOs().equals(os))){
							catalogList.get(i).setIsChapter("1");
							catalogList.get(i).setPrice("0");
						}else{
							if(recordList.size()>0){
								catalogList.get(i).setIsChapter("1");
								catalogList.get(i).setPrice("0");
							}else{
								//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
								 if(businessService.findMemberRecordListByCatalogId(employeeId1.toString(),catalogList.get(i).getId().toString()).size()>0){
									 catalogList.get(i).setIsChapter("1");
								 }else{
									 catalogList.get(i).setIsChapter("0");
								 }
							}
						}
						//查询是否已经被选课
						if(businessService.queryIsChoose(catalogList.get(i).getId(),employeeId1).size()>0){
							catalogList.get(i).setIsChoose("1");
						}else{
							catalogList.get(i).setIsChoose("0");
						}
						//判断是否已经更新完毕
						if(Integer.parseInt(catalogList.get(i).getTotal())>catalogList.get(i).getCourseCount()){
							catalogList.get(i).setCourseUpdateStatus("1");
						}else{
							catalogList.get(i).setCourseUpdateStatus("2");
						}
					}
						return okNew("查询成功", catalogList);
					}else{
						return status(97, "sigh验证出错");
					}
			}else{
				
				if(Long.parseLong(employeeId)!=33){
					//查询最近一次登录的信息
					LoginDetail lastLogin = memberService.findBusinessLoginDetailById(Long.parseLong(employeeId));
					//查询学员此种设备、此个设备ID上次登录的明细
					LoginDetail loginDetail = memberService.findBusinessLoginDetail(Long.parseLong(employeeId),client,deviceId);
					if(loginDetail!=null){
						Long detailId = loginDetail.getId();
						Long lastId = lastLogin.getId();
						//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，，如果最后一次的大于本机登陆的，那么就是在异地登录了
						if(lastId>detailId){
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
						}
					}
				}
				Employee employee1 = businessService.findEmployeeDetail(Long.parseLong(employeeId));
//				catalogList=businessService.findParentCatalogList(page);
				catalogList=businessService.findParentCatalogListByTeacher(page,teacherName);
				for(int i=0;i<catalogList.size();i++){
					//判断是否是只购买了系列课程
					if(employee1.getIfBusiness()!=0L && employee1.getIfBusiness()!=1L){
						if(employee1.getIfBusiness()==catalogList.get(i).getId()){
							catalogList.get(i).setIsChapter("1");
						}else{
							catalogList.get(i).setIsChapter("0");
						}
					}
					//如果课程包type=2则为视频课程包
					if("2".equals(catalogList.get(i).getType())){
						catalogList.get(i).setIfVideo("1");
					}else{
						catalogList.get(i).setIfVideo("0");
					}
					//是否收藏了课程包去
					if(businessService.queryStudyPlanCatalog(catalogList.get(i).getId(),Long.parseLong(employeeId)).size()>0){
						Long ifStudyPlan = (long) 1;
						catalogList.get(i).setIfCollection(ifStudyPlan);
					}else{
						Long ifStudyPlan = (long) 0;
						catalogList.get(i).setIfCollection(ifStudyPlan);
					}
					catalogList.get(i).setAvatar(commonService.appenUrl(catalogList.get(i).getAvatar()));
					catalogList.get(i).setCover(commonService.appenUrl(catalogList.get(i).getCover()));
					catalogList.get(i).getFlag();
					//判断是以什么形式展现课程列表
					if(catalogList.get(i).getFlag()=="0" || ("0").equals(catalogList.get(i).getFlag())){
						//修改课程数量
						catalogList.get(i).setCourseCount(businessService.findCatalogListCourseCount(catalogList.get(i).getId()));
						//修改学习人数
						catalogList.get(i).setStudyCount(businessService.findCatalogListStudyCount(catalogList.get(i).getId()));
					}
					//判断t_business_member_record表中是否有效期内的好多课会员数据存在
					List<MemberRecord> recordList = businessService.findMemberRecordList(employeeId.toString());
					if(recordList.size()>0){
						catalogList.get(i).setIsChapter("1");
					}else{
						//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
						 if(businessService.findMemberRecordListByCatalogId(employeeId.toString(),catalogList.get(i).getId().toString()).size()>0){
							 catalogList.get(i).setIsChapter("1");
						 }else{
							 catalogList.get(i).setIsChapter("0");
						 }
					}
				}
//			}
			return ok("查询成功", catalogList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	//////////////////////////////////////////以下是视频接口信息//////////////////////////////////////
	///////      ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓      //////
	
	
	/**
	 * @Title: getParentCatalogList
	 * @Description: (获得课程一级分类列表)
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getVideoParentCatalogList")
	public JSONObject getVideoParentCatalogList(Page page,String employeeId,String client,String version,String deviceId,String sign) {
		try {
			List<Catalog> catalogList = null;
			if(StringUtils.isNullOrEmpty(employeeId) || StringUtils.isNullOrEmpty(deviceId) || StringUtils.isNullOrEmpty(sign)){
				return status(99,"参数错误");
			}
//			//解密
			UpAES upAES = new UpAES();
//			upAES.entry("1");
			System.out.println("解密后的employeeID："+upAES.disEntry(employeeId));
			System.out.println("解密后的deviceId："+upAES.disEntry(deviceId));
			String newEmployeeId = upAES.disEntry(employeeId);
			String newDeviceId =upAES.disEntry(deviceId);
			String newSign = PasswdEncryption.MD5(newEmployeeId+newDeviceId);
			System.out.println(PasswdEncryption.MD5("1"));
			newSign= newSign.substring(5, newSign.length()-5);
			System.out.println("得到的新Sign"+newSign);
			if(newSign.equals(sign)){
				Long employeeId1 = Long.parseLong(newEmployeeId);
				if(employeeId1!=33){
				//查询最近一次登录的信息
				LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employeeId1);
				//查询学员此种设备、此个设备ID上次登录的明细
				LoginDetail loginDetail = memberService.findBusinessLoginDetail(employeeId1,client,newDeviceId);
				if(loginDetail!=null){
					Long detailId = loginDetail.getId();
					Long lastId = lastLogin.getId();
					//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，，如果最后一次的大于本机登陆的，那么就是在异地登录了
					if(lastId>detailId){
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
					}
				}
			}
			Employee employee1 = businessService.findEmployeeDetail(employeeId1);
			catalogList=businessService.findParentVideoCatalogList(page);
			for(int i=0;i<catalogList.size();i++){
				//判断t_business_member_record表中是否有效期内的好多课会员数据存在
				List<MemberRecord> recordList = businessService.findMemberRecordList(employeeId1.toString());
				if(recordList.size()>0){
					catalogList.get(i).setIsChapter("1");
				}else{
					//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
					 if(businessService.findMemberRecordListByCatalogId(employeeId1.toString(),catalogList.get(i).getId().toString()).size()>0){
						 catalogList.get(i).setIsChapter("1");
					 }else{
						 catalogList.get(i).setIsChapter("0");
					 }
				}
				catalogList.get(i).setIfVideo("1");
				if(employee1.getIfBusiness()!=0L && employee1.getIfBusiness()!=1L){
					if(employee1.getIfBusiness()==catalogList.get(i).getId()){
						catalogList.get(i).setIsChapter("1");
					}else{
						catalogList.get(i).setIsChapter("0");
					}
				}
				String ids = catalogList.get(i).getTeacherId();
				List<Teacher> teacherList = new ArrayList<Teacher>();
				StringBuffer sb = new StringBuffer();
				for (String s : ids.split(",")) {
					if (StringUtils.isNullOrEmpty(s)) {
						continue;
					}
					List<Teacher> list=businessService.findTeacherById(Long.parseLong(s));
					if(list.size()>0){
						teacherList.add(list.get(0));
					}
//					sb.append(service.findTeacherById(Long.parseLong(s)).getStr("name")).append(",");
				}
				catalogList.get(i).setTeacherList(teacherList);
				String http = "http";
				catalogList.get(i).setAvatar(commonService.appenUrl(catalogList.get(i).getAvatar()));
				catalogList.get(i).getFlag();
				//判断是以什么形式展现课程列表
				if(catalogList.get(i).getFlag()=="0" || ("0").equals(catalogList.get(i).getFlag())){
					//修改课程数量
					catalogList.get(i).setCourseCount(businessService.findCatalogListCourseCount(catalogList.get(i).getId()));
					//修改学习人数
					catalogList.get(i).setStudyCount(businessService.findCatalogListStudyCount(catalogList.get(i).getId()));
				}
			}
			return okNew("查询成功", catalogList);
//				return status(200, "校验通过");
			}else
			{
				return status(97, "验证出错");
			}
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: getCatalogCourseList
	 * @Description: (获得课程分类下课程列表)
	 * @param catalogId
	 *            分类ID
	 * @param employeeId
	 *            用户ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getVideoCatalogDetailById")
	public JSONObject getVideoCatalogDetailById(Long catalogId,String employeeId,String client,String version,String deviceId,String sign) {
		try {
			List<CatalogCourseList> catalogCourseList = null;
			List<Catalog> catalogList = null;
			if(employeeId==null || catalogId==null || catalogId==0L || StringUtils.isNullOrEmpty(employeeId) || StringUtils.isNullOrEmpty(deviceId) || StringUtils.isNullOrEmpty(sign)){
				return status(99,"参数错误");
			}
//			//解密
			UpAES upAES = new UpAES();
//			upAES.entry("1");
			System.out.println("解密后的employeeID："+upAES.disEntry(employeeId.toString()));
			System.out.println("解密后的catalogId："+upAES.disEntry(deviceId));
			String newEmployeeId = upAES.disEntry(employeeId.toString());
			String newCatalogId =upAES.disEntry(deviceId);
			//重新赋值deviceId
			deviceId =upAES.disEntry(deviceId);
			String newSign = PasswdEncryption.MD5(newEmployeeId+newCatalogId);
			System.out.println(PasswdEncryption.MD5("1"));
			newSign= newSign.substring(5, newSign.length()-5);
			System.out.println("得到的新Sign"+newSign);
			if(newSign.equals(sign)){
				Long employeeId1 = Long.parseLong(newEmployeeId);
				if(employeeId1!=33){
					//查询最近一次登录的信息
					LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employeeId1);
					//查询学员此种设备、此个设备ID上次登录的明细
					LoginDetail loginDetail = memberService.findBusinessLoginDetail(employeeId1,client,deviceId);
					if(loginDetail!=null){
						Long detailId = loginDetail.getId();
						Long lastId = lastLogin.getId();
						//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，，如果最后一次的大于本机登陆的，那么就是在异地登录了
						if(lastId>detailId){
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
						}
					}
				}
			catalogList=businessService.findParentCatalogById(catalogId);
			Employee employee1 = businessService.findEmployeeDetail(employeeId1);
			for(int i=0;i<catalogList.size();i++){
				catalogList.get(i).setIfVideo("1");
				//是否收藏了课程包去
				if(businessService.queryStudyPlanCatalog(catalogList.get(i).getId(),employeeId1).size()>0){
					Long ifStudyPlan = (long) 1;
					catalogList.get(i).setIfCollection(ifStudyPlan);
				}else{
					Long ifStudyPlan = (long) 0;
					catalogList.get(i).setIfCollection(ifStudyPlan);
				}
				//判断t_business_member_record表中是否有效期内的好多课会员数据存在
				List<MemberRecord> recordList = businessService.findMemberRecordList(employeeId1.toString());
				//根据课程包Id查询推荐列表
				List<RecommendInfo> recommendList = businessService.findRecommendList(catalogList.get(i).getId(),"2");
				String os = "0";
				if("iOS".equals(client)){
					os = "1";
				}else if("android".equals(client)){
					os = "2";
				}
				//先判断课程是不是被推荐的课程
				if(recommendList.size()>0 && (recommendList.get(0).getOs().equals("0") || recommendList.get(0).getOs().equals(os))){
					catalogList.get(i).setIsChapter("1");
				}else{
					if(recordList.size()>0){
						catalogList.get(i).setIsChapter("1");
					}else{
						//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
						 if(businessService.findMemberRecordListByCatalogId(employeeId1.toString(),catalogList.get(i).getId().toString()).size()>0){
							 catalogList.get(i).setIsChapter("1");
						 }else{
							 catalogList.get(i).setIsChapter("0");
						 }
					}
				}
				
				//判断课程包管理的试卷是否已经回答过
				if(businessService.queryAnswerHistory(catalogList.get(i).getExamId(),employeeId1).size()>0){
					catalogList.get(i).setTestMark("1");
				}else{
					catalogList.get(i).setTestMark("0");
				}
				//获取teacherId 的集合
				String ids = catalogList.get(i).getTeacherId();
				List<Teacher> teacherList = new ArrayList<Teacher>();
				StringBuffer sb = new StringBuffer();
				for (String s : ids.split(",")) {
					if (StringUtils.isNullOrEmpty(s)) {
						continue;
					}
					List<Teacher> list=businessService.findTeacherById(Long.parseLong(s));
					if(list.size()>0){
						list.get(0).setAvatar(commonService.appenUrl(list.get(i).getAvatar()));
						list.get(0).setCover(commonService.appenUrl(list.get(i).getCover()));
						teacherList.add(list.get(0));
					}
//					sb.append(service.findTeacherById(Long.parseLong(s)).getStr("name")).append(",");
				}
				catalogList.get(i).setTeacherList(teacherList);
				catalogList.get(i).setAvatar(commonService.appenUrl(catalogList.get(i).getAvatar()));
				catalogList.get(i).setCover(commonService.appenUrl(catalogList.get(i).getCover()));
				catalogList.get(i).setListCover(commonService.appenUrl(catalogList.get(i).getListCover()));
				catalogList.get(i).getFlag();
				catalogCourseList=businessService.getCatalogCourseList(catalogId);
				//循环遍历集合查询是否是学员的推荐课程和计划学习课程
				for(int j=0;j<catalogCourseList.size();j++){
					//是否推荐
					if(businessService.queryRecommend(catalogCourseList.get(j).getId(),employeeId1).size()>0){
						Long ifRecommend = (long) 1;
						catalogCourseList.get(j).setIfRecommend(ifRecommend);
					}else{
						Long ifRecommend = (long) 0;
						catalogCourseList.get(j).setIfRecommend(ifRecommend);
					}
					//是否在学习计划
					if(businessService.queryStudyPlan(catalogCourseList.get(j).getId(),employeeId1).size()>0){
						Long ifStudyPlan = (long) 1;
						catalogCourseList.get(i).setIfStudyPlan(ifStudyPlan);
					}else{
						Long ifStudyPlan = (long) 0;
						catalogCourseList.get(j).setIfStudyPlan(ifStudyPlan);
					}
					//判断课程包管理的试卷是否已经回答过
					if(businessService.queryAnswerHistory(catalogCourseList.get(j).getExamId(),employeeId1).size()>0){
						catalogCourseList.get(j).setTestMark("1");
					}else{
						catalogCourseList.get(j).setTestMark("0");
					}
					//学习进度
					if(businessService.queryStudyIfFinished(catalogCourseList.get(j).getId(),employeeId1).size()>0){
						catalogCourseList.get(j).setIsFinished(businessService.queryStudyIfFinished(catalogCourseList.get(j).getId(),employeeId1).get(0).getIsFinished());
					}else{
						Long isFinished = (long) 0;
						catalogCourseList.get(j).setIsFinished(isFinished);
					}
					catalogCourseList.get(j).setAvatar(commonService.appenUrl(catalogCourseList.get(j).getAvatar()));
					catalogCourseList.get(j).setPdfAddress(commonService.appenUrl(catalogCourseList.get(j).getPdfAddress()));
					catalogCourseList.get(j).setPlayAddress(commonService.appenUrl(catalogCourseList.get(j).getPlayAddress()));
					}
				catalogList.get(i).setCourseList(catalogCourseList);
				//判断是以什么形式展现课程列表
				if(catalogList.get(i).getFlag()=="0" || ("0").equals(catalogList.get(i).getFlag())){
					//修改课程数量
					catalogList.get(i).setCourseCount(businessService.findCatalogListCourseCount(catalogList.get(i).getId()));
					//修改学习人数
					catalogList.get(i).setStudyCount(businessService.findCatalogListStudyCount(catalogList.get(i).getId()));
				}
				//根据课程包ID查询分享链接地址
				String shareAddress = businessService.findShareAddress("h5_course_share_url");
				if(!StringUtils.isNullOrEmpty(shareAddress)){
					shareAddress+="&id="+catalogList.get(i).getCourseId();
				}
				catalogList.get(i).setShareAddress(shareAddress);
				//根据课程简章链接地址
				String synopsisAddress = businessService.findShareAddress("h5_course_synopsis_url");
				if(!StringUtils.isNullOrEmpty(synopsisAddress)){
					synopsisAddress+="&catalogId="+catalogList.get(i).getId();
				}
				catalogList.get(i).setSynopsisAddress(synopsisAddress);
				//查询是否已经被选课
				if(businessService.queryIsChoose(catalogList.get(i).getId(),employeeId1).size()>0){
					catalogList.get(i).setIsChoose("1");
				}else{
					catalogList.get(i).setIsChoose("0");
				}
				//判断课程实际数量是否大于
				if(Integer.parseInt(catalogList.get(i).getTotal())<catalogList.get(i).getCourseCount()){
					catalogList.get(i).setTotal(String.valueOf(catalogList.get(i).getCourseCount()));
				}
			}
			return okNew("查询成功", catalogList);
			}
			return status(97, "sign验证出错");
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: getCatalogList
	 * @Description: (获得课程分类列表)
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getVideoSecondCatalogDetailById")
	public JSONObject getVideoSecondCatalogDetailById(Page page,String employeeId,Long catalogId,String client,String version,String deviceId,String sign) {
		try {
			List<Catalog> catalogList1 = null;
			List<SecondCatalog> catalogList = null;
			//解密
			UpAES upAES = new UpAES();
//			upAES.entry("1");
			System.out.println("解密后的employeeID："+upAES.disEntry(employeeId.toString()));
			System.out.println("解密后的deviceId："+upAES.disEntry(deviceId));
			String newEmployeeId = upAES.disEntry(employeeId.toString());
			String newDeviceId =upAES.disEntry(deviceId);
			//重新赋值deviceId
			deviceId =upAES.disEntry(deviceId);
			String newSign = PasswdEncryption.MD5(newEmployeeId+newDeviceId);
			newSign= newSign.substring(5, newSign.length()-5);
			System.out.println("得到的新Sign"+newSign);
			if(newSign.equals(sign)){
				Long employeeId1 = Long.parseLong(newEmployeeId);
				if(employeeId1!=33){
					//查询最近一次登录的信息
					LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employeeId1);
					//查询学员此种设备、此个设备ID上次登录的明细
					LoginDetail loginDetail = memberService.findBusinessLoginDetail(employeeId1,client,deviceId);
					if(loginDetail!=null){
						Long detailId = loginDetail.getId();
						Long lastId = lastLogin.getId();
						//如果最近一次登录的记录ID与此设备最后一次登录的记录Id是否相符，，如果最后一次的大于本机登陆的，那么就是在异地登录了
						if(lastId>detailId){
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							return status(300, "此账号于"+df.format(lastLogin.getLoginTime()).substring(0,19)+"在其他设备登录！");
						}
					}
				}
				catalogList1=businessService.findParentCatalogById(catalogId);
				Employee employee1 = businessService.findEmployeeDetail(employeeId1);
				for(int i=0;i<catalogList1.size();i++){
					//判断t_business_member_record表中是否有效期内的好多课会员数据存在
					List<MemberRecord> recordList = businessService.findMemberRecordList(employeeId1.toString());
					catalogList1.get(i).setIfVideo("1");
					//根据课程包Id查询推荐列表
					List<RecommendInfo> recommendList = businessService.findRecommendList(catalogList1.get(i).getId(),"2");
					String os = "0";
					if("iOS".equals(client)){
						os = "1";
					}else if("android".equals(client)){
						os = "2";
					}
					//先判断课程是不是被推荐的课程
					if(recommendList.size()>0 && (recommendList.get(0).getOs().equals("0") || recommendList.get(0).getOs().equals(os))){
						catalogList1.get(i).setIsChapter("1");
					}else{
						if(recordList.size()>0){
							catalogList1.get(i).setIsChapter("1");
						}else{
							//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
							 if(businessService.findMemberRecordListByCatalogId(employeeId1.toString(),catalogList1.get(i).getId().toString()).size()>0){
								 catalogList1.get(i).setIsChapter("1");
							 }else{
								 catalogList1.get(i).setIsChapter("0");
							 }
						}
					}
					//是否收藏了课程包去
					if(businessService.queryStudyPlanCatalog(catalogList1.get(i).getId(),employeeId1).size()>0){
						Long ifStudyPlan = (long) 1;
						catalogList1.get(i).setIfCollection(ifStudyPlan);
					}else{
						Long ifStudyPlan = (long) 0;
						catalogList1.get(i).setIfCollection(ifStudyPlan);
					}
					//判断课程包管理的试卷是否已经回答过
					if(businessService.queryAnswerHistory(catalogList1.get(i).getExamId(),employeeId1).size()>0){
						catalogList1.get(i).setTestMark("1");
					}else{
						catalogList1.get(i).setTestMark("0");
					}
					//获取teacherId 的集合
					String ids = catalogList1.get(i).getTeacherId();
					List<Teacher> teacherList = new ArrayList<Teacher>();
					StringBuffer sb = new StringBuffer();
					for (String s : ids.split(",")) {
						if (StringUtils.isNullOrEmpty(s)) {
							continue;
						}
						List<Teacher> list=businessService.findTeacherById(Long.parseLong(s));
						if(list.size()>0){
							list.get(0).setAvatar(commonService.appenUrl(list.get(i).getAvatar()));
							list.get(0).setCover(commonService.appenUrl(list.get(i).getCover()));
							teacherList.add(list.get(0));
						}
//						sb.append(service.findTeacherById(Long.parseLong(s)).getStr("name")).append(",");
					}
					catalogList1.get(i).setTeacherList(teacherList);
					catalogList1.get(i).setAvatar(commonService.appenUrl(catalogList1.get(i).getAvatar()));
					catalogList1.get(i).setCover(commonService.appenUrl(catalogList1.get(i).getCover()));
					catalogList1.get(i).setListCover(commonService.appenUrl(catalogList1.get(i).getListCover()));
					catalogList1.get(i).getFlag();
					SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					System.out.println("查询二级分类列表开始时间："+df1.format(new Date()));
					catalogList=businessService.findSecondCatalogList(page,catalogId);
					System.out.println("查询二级分类列表结束时间："+df1.format(new Date()));
					for(int j=0;j<catalogList.size();j++){
						catalogList.get(j).setAvatar(commonService.appenUrl(catalogList.get(j).getAvatar()));
						List<SecondCatalogCourseList> catalogCourseList = null;
						System.out.println("查询二级分类下课程列表开始时间："+df1.format(new Date()));
						catalogCourseList=businessService.getSecondCatalogCourseList(catalogList.get(j).getId());
						System.out.println("查询二级分类下课程列表结束时间："+df1.format(new Date()));
						//循环遍历集合查询是否是学员的推荐课程和计划学习课程
						if(catalogCourseList!=null){
							for(int k=0;k<catalogCourseList.size();k++){
								//是否推荐
								if(businessService.queryRecommend(catalogCourseList.get(k).getId(),employeeId1).size()>0){
									Long ifRecommend = (long) 1;
									catalogCourseList.get(k).setIfRecommend(ifRecommend);
								}else{
									Long ifRecommend = (long) 0;
									catalogCourseList.get(k).setIfRecommend(ifRecommend);
								}
								//是否在学习计划
								if(businessService.queryStudyPlan(catalogCourseList.get(k).getId(),employeeId1).size()>0){
									Long ifStudyPlan = (long) 1;
									catalogCourseList.get(k).setIfStudyPlan(ifStudyPlan);
								}else{
									Long ifStudyPlan = (long) 0;
									catalogCourseList.get(k).setIfStudyPlan(ifStudyPlan);
								}
								//学习进度
								if(businessService.queryStudyIfFinished(catalogCourseList.get(k).getId(),employeeId1).size()>0){
									catalogCourseList.get(k).setIsFinished(businessService.queryStudyIfFinished(catalogCourseList.get(k).getId(),employeeId1).get(0).getIsFinished());
								}else{
									Long isFinished = (long) 0;
									catalogCourseList.get(k).setIsFinished(isFinished);
								}
								//判断课程包管理的试卷是否已经回答过
								if(businessService.queryAnswerHistory(catalogCourseList.get(k).getExamId(),employeeId1).size()>0){
									catalogCourseList.get(k).setTestMark("1");
								}else{
									catalogCourseList.get(k).setTestMark("0");
								}
								catalogCourseList.get(k).setAvatar(commonService.appenUrl(catalogCourseList.get(k).getAvatar()));
								//根据版本号判断是否加密PDF地址
//								if(!("1.6.0").equals(version) && !("1.5.9").equals(version)){
									catalogCourseList.get(k).setPdfAddress(commonService.appenUrl(catalogCourseList.get(k).getPdfAddress()));
									catalogCourseList.get(k).setPlayAddress(commonService.appenUrl(catalogCourseList.get(k).getPlayAddress()));
//								}else{
									String content =catalogCourseList.get(k).getPdfAddress();
									String playAddress = catalogCourseList.get(k).getPlayAddress();
									//随机生成字母和数字的组合加密录音文件地址
//									String pwd1 = PasswdEncryption.getStringRandom();
//									StringBuffer stringBuffer1 = new StringBuffer(commonService.appenUrl(playAddress));
//									stringBuffer1.insert(50, pwd1).toString();
//									catalogCourseList.get(k).setPlayAddress(stringBuffer1.toString().toUpperCase());
//									//加密PlayAddressM3u8
//									String playAddress1 = catalogCourseList.get(k).getPlayAddressM3u8();
//									String pwd2 = PasswdEncryption.getStringRandom();
//									StringBuffer stringBuffer2 = new StringBuffer(commonService.appenUrl(playAddress1));
//									stringBuffer2.insert(50, pwd2).toString();
//									catalogCourseList.get(k).setPlayAddressM3u8(stringBuffer2.toString().toUpperCase());
//									if(!StringUtils.isNullOrEmpty(content)){
//										//随机生成字母和数字的组合加密PDF地址
//										String pwd = PasswdEncryption.getStringRandom();
//										StringBuffer stringBuffer = new StringBuffer(commonService.appenUrl(catalogCourseList.get(k).getPdfAddress()));
//										stringBuffer.insert(50, pwd).toString();
//										catalogCourseList.get(k).setPdfAddress(stringBuffer.toString().toUpperCase());
////										catalogList.get(i).setAvatar(stringBuffer.toString().toUpperCase());
//									}else{
//										catalogCourseList.get(k).setPdfAddress(commonService.appenUrl(content));
//									}
								}
//							}
						}
						catalogList.get(j).setCatalogCourseList(catalogCourseList);
					}
					System.out.println("遍历结束时间："+df1.format(new Date()));
					catalogList1.get(i).setSecondCatalogList(catalogList);
					//判断是以什么形式展现课程列表
					if(catalogList1.get(i).getFlag()=="0" || ("0").equals(catalogList1.get(i).getFlag())){
						//修改课程数量
						catalogList1.get(i).setCourseCount(businessService.findCatalogListCourseCount(catalogList1.get(i).getId()));
						//修改学习人数
						catalogList1.get(i).setStudyCount(businessService.findCatalogListStudyCount(catalogList1.get(i).getId()));
					}
					//根据课程包ID查询分享链接地址
					String shareAddress = businessService.findShareAddress("h5_course_share_url");
					if(!StringUtils.isNullOrEmpty(shareAddress)){
						shareAddress+="&id="+catalogList1.get(i).getCourseId();
					}
					catalogList1.get(i).setShareAddress(shareAddress);
					//根据课程简章链接地址
					String synopsisAddress = businessService.findShareAddress("h5_course_synopsis_url");
					if(!StringUtils.isNullOrEmpty(synopsisAddress)){
						synopsisAddress+="&catalogId="+catalogList1.get(i).getId();
					}
					catalogList1.get(i).setSynopsisAddress(synopsisAddress);
					//查询是否已经被选课
					if(businessService.queryIsChoose(catalogList.get(i).getId(),employeeId1).size()>0){
						catalogList1.get(i).setIsChoose("1");
					}else{
						catalogList1.get(i).setIsChoose("0");
					}
					//判断课程实际数量是否大于
					if(Integer.parseInt(catalogList1.get(i).getTotal())<catalogList1.get(i).getCourseCount()){
						catalogList1.get(i).setTotal(String.valueOf(catalogList1.get(i).getCourseCount()));
					}
				}
				return okNew("查询成功", catalogList1);
			}
//			}
//			return ok("查询成功", catalogList);
				
//			}
			return status(97,"sign 验证出错");
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: getLivingCourseDetail 
	 * @Description: (根据unionId和课程Id查询直播课程详细信息，包含：是否购买、课程简章、封面图) 
	 * @param unionId 微信Id
	 * @param content 评论内容
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/test1")
	public JSONObject test1(){
		try {
			List<Employee> list =  businessService.queryAllEmployee();
			for(int i=0;i<list.size();i++){
				String str = null;
				String province="";
				String city="";
				JSONArray jsonArray = null;
				//根据手机号码查询手机号码归属地
				str = "[" +memberService.request(list.get(i).getMobile()) + "]";
				jsonArray = new JSONArray(str);
				String errNumResult = (String) jsonArray.getJSONObject(0).get("status");
				if(("0").equals(errNumResult)){
					org.json.JSONObject jsonresult = (org.json.JSONObject) jsonArray.getJSONObject(0).get("result");
					province = jsonresult.getString("province");
					city = jsonresult.getString("city");
					list.get(i).setProvince(province);
					list.get(i).setCity(city);
					businessService.updateEmployee2(list.get(i));
				}
			}
		    return ok("查询成功");
		}catch(Exception e){
			e.printStackTrace();
		}
		return error("获取课程信息失败");
	}
}