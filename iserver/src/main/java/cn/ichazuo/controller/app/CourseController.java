package cn.ichazuo.controller.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.ichazuo.commons.base.BaseController;
import cn.ichazuo.commons.component.CacheInfo;
import cn.ichazuo.commons.component.ConfigInfo;
import cn.ichazuo.commons.component.RedisClient;
import cn.ichazuo.commons.util.CodeUtils;
import cn.ichazuo.commons.util.DateUtils;
import cn.ichazuo.commons.util.NumberUtils;
import cn.ichazuo.commons.util.PasswdEncryption;
import cn.ichazuo.commons.util.StringUtils;
import cn.ichazuo.commons.util.model.Page;
import cn.ichazuo.commons.util.model.Params;
import cn.ichazuo.model.app.LivingCourseInfo;
import cn.ichazuo.model.app.MemberInfoForEmail;
import cn.ichazuo.model.app.MemberSeries;
import cn.ichazuo.model.app.OfflineCourseCommentInfo;
import cn.ichazuo.model.app.OfflineCourseInfo;
import cn.ichazuo.model.app.OfflineCourseListInfo;
import cn.ichazuo.model.app.OfflineEnlargedClass;
import cn.ichazuo.model.app.OfflineJoinMember;
import cn.ichazuo.model.app.OfflineSigned;
import cn.ichazuo.model.app.OnlineCourseCommentInfo;
import cn.ichazuo.model.app.OnlineCourseListInfo;
import cn.ichazuo.model.app.SelfStudy;
import cn.ichazuo.model.app.UserSimpleInfo;
import cn.ichazuo.model.log.CourseClickLog;
import cn.ichazuo.model.table.Business;
import cn.ichazuo.model.table.Catalog;
import cn.ichazuo.model.table.CourseClickDetail;
import cn.ichazuo.model.table.CourseOfflineJoin;
import cn.ichazuo.model.table.CourseOnlineJoin;
import cn.ichazuo.model.table.CourseWebInfo;
import cn.ichazuo.model.table.DaKe;
import cn.ichazuo.model.table.Employee;
import cn.ichazuo.model.table.Gift;
import cn.ichazuo.model.table.HaoduokeLog;
import cn.ichazuo.model.table.LoginDetail;
import cn.ichazuo.model.table.Member;
import cn.ichazuo.model.table.MemberRecord;
import cn.ichazuo.model.table.OfflineCourseComment;
import cn.ichazuo.model.table.OfflineCourseCommentFavour;
import cn.ichazuo.model.table.OfflineCourseFavour;
import cn.ichazuo.model.table.OfflineCourseImage;
import cn.ichazuo.model.table.OnlineCourseComment;
import cn.ichazuo.model.table.OnlineCourseFavour;
import cn.ichazuo.model.table.SelfCourseOrder;
import cn.ichazuo.model.table.ThirdMember;
import cn.ichazuo.service.BusinessService;
import cn.ichazuo.service.CommonService;
import cn.ichazuo.service.CourseService;
import cn.ichazuo.service.LogService;
import cn.ichazuo.service.MemberService;
import cn.ichazuo.service.OrderService;

/**
 * @ClassName: CourseController 
 * @Description: (课程Controller) 
 * @author ZhaoXu
 * @date 2015年7月19日 下午12:09:28 
 * @version V1.0
 */
@RequestMapping("/app")
@Controller("app.courseController")
public class CourseController extends BaseController{
	@Autowired
	private CourseService courseService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private LogService logService;
	@Autowired
	private CacheInfo cacheInfo;
	@Autowired
	private ConfigInfo configInfo;
	@Autowired
	private RedisClient redisClient;
	@Autowired
	private BusinessService businessService;
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final int index = 1;
	public static final String msg = "【插坐学院】#name#同学，课程马上就要开始啦，请抓紧时间签到入场。入场后将手机调至静音，全心投入插坐学院精彩课程哦！回复TD退订";
	
	
	@ResponseBody
	@RequestMapping("/findOfflineCourseV2ForWeb")
	public JSONObject findOfflineCourseV2ForWeb(String time,Page page,Long userId,Long courseType,Integer newtype){
		try{
			if(NumberUtils.isNullOrZero(newtype)){
				newtype = 0;
			}
			LocalDate now = LocalDate.now();
//			String countKey = "findOfflineCourseV2ForWeb";
//			String key = "findOfflineCourseV2ForWeb---page:"+page.getNowPage()+"--time--"+time;
//			long start = System.currentTimeMillis();
			
			List<OfflineCourseListInfo> offlineList = null;
//			if(redisClient.isExist(key)){
//				offlineList = (List<OfflineCourseListInfo>)redisClient.getObject(key);
//			}else{
				offlineList = courseService.findOfflineCourseListV2(time,page,courseType,1,newtype);
//				redisClient.setObject(key, offlineList, cacheInfo.getRedisCacheLevel3());
//			}
			
			offlineList.forEach(offline -> {
				String teachers = "";
				String id = offline.getTeacherIds();
				String arr[] = id.split(",");
				
				for(int i=0;i<arr.length;i++){
					Member m = memberService.findMemberById(Long.valueOf(arr[i]));
					if(m != null){
						teachers += m.getNickName();
						teachers += ",";
					}
					if(i == index){
						teachers = StringUtils.removeEndChar(teachers, ',');
						if(arr.length > index){
							teachers += "等";
						}
						break;
					}
				}
				offline.setTeachers(StringUtils.removeEndChar(teachers, ','));
				//添加报名人数
				offline.setJoinNum(courseService.findCourseJoinCount(offline.getId()));
				LocalDate begin = LocalDate.parse(DateUtils.formatDate(offline.getBeginTime(),DateUtils.DATE_FORMAT_NORMAL));
				if(now.isAfter(begin) || now.isEqual(begin) || offline.getJoinNum() >= offline.getStudentNum()){
					//报名时间截止,报名人数已经足够  停止报名  
					offline.setIsOver(1);
				}else{
					offline.setIsOver(0);
				}
				if(now.isAfter(begin) || now.isEqual(begin)){
					//报名时间截止,报名人数已经足够  停止报名  
					offline.setDeLate(1);;
				}else{
					offline.setDeLate(0);
				}
				
				if(offline.getId() == 129){
					offline.setIsOver(1);
				}
			});
			
			Long count = 0L;
//			if(redisClient.isExist(countKey)){
//				count = (Long)redisClient.getObject(countKey);
//			}else{
				count = courseService.findOfflineCourseCount(courseType);
//				redisClient.setObject(countKey, count,cacheInfo.getRedisCacheLevel3());
//			}
			
			if(!NumberUtils.isNullOrZero(courseType)){
				return ok("查询成功",offlineList,page.getNowPage(),count);
			}else{
				return ok("查询成功",offlineList,count);
			}
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * 查询2017年迎新大课各个区域信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findAreaInfoForEnlargedClass")
	public JSONObject findAreaInfoForEnlargedClass(){
		try{
			
			List<OfflineCourseListInfo> offlineList = null;
			OfflineEnlargedClass course = new OfflineEnlargedClass(); 
			//vip区
			OfflineCourseListInfo vip = new OfflineCourseListInfo();
			//A区
			OfflineCourseListInfo a = new OfflineCourseListInfo();
			//B区
			OfflineCourseListInfo b = new OfflineCourseListInfo();
			//C区
			OfflineCourseListInfo c = new OfflineCourseListInfo();
			//D区
			OfflineCourseListInfo d = new OfflineCourseListInfo();
			Long vipId=1029L;
			Long AId=1030L;
			Long BId=1031L;
			Long CId=951L;
			Long DId=1032L;
			course.setVipId(vipId.toString());
			course.setAId(AId.toString());
			course.setBId(BId.toString());
			course.setCId(CId.toString());
			course.setDId(DId.toString());
			//vip区课程信息
			vip = courseService.findOfflineCourseListInfoByCourseId(vipId);
			//A区课程信息
			a = courseService.findOfflineCourseListInfoByCourseId(AId);
			//B区课程信息
			b = courseService.findOfflineCourseListInfoByCourseId(BId);
			//C区课程信息
			c = courseService.findOfflineCourseListInfoByCourseId(CId);
			//D区课程信息
			d = courseService.findOfflineCourseListInfoByCourseId(DId);
			//是否开启售卖
			course.setVipIsOpen(vip.getIsSell());
			course.setAIsOpen(a.getIsSell());
			course.setBIsOpen(b.getIsSell());
			course.setCIsOpen(c.getIsSell());
			course.setDIsOpen(d.getIsSell());
			//各个区域对应票价
			course.setVipPrice(vip.getPrice());
			course.setAPrice(a.getPrice());
			course.setBPrice(b.getPrice());
			course.setCPrice(c.getPrice());
			course.setDPrice(d.getPrice());
			//根据课程当前报名人数判断是否可以购买
			if(courseService.findCourseJoinCount(vipId)>=vip.getStudentNum()){
				course.setVipIsOver("1");
			}else{
				course.setVipIsOver("0");
			}
			//根据课程当前报名人数判断是否可以购买
			if(courseService.findCourseJoinCount(AId)>=a.getStudentNum()){
				course.setAIsOver("1");
			}else{
				course.setAIsOver("0");
			}
			//根据课程当前报名人数判断是否可以购买
			if(courseService.findCourseJoinCount(BId)>=b.getStudentNum()){
				course.setBIsOver("1");
			}else{
				course.setBIsOver("0");
			}
			//根据课程当前报名人数判断是否可以购买
			if(courseService.findCourseJoinCount(CId)>=c.getStudentNum()){
				course.setCIsOver("1");
			}else{
				course.setCIsOver("0");
			}
			//根据课程当前报名人数判断是否可以购买
			if(courseService.findCourseJoinCount(DId)>=d.getStudentNum()){
				course.setDIsOver("1");
			}else{
				course.setDIsOver("0");
			}
			return ok("查询成功",course);
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	
	/**
	 * @Title: findOfflineCourse 
	 * @Description: (查询线下课程列表) 
	 * @param page 页数
	 * @param userId 当前登录用户
	 * @param courseType 线下课程类别
	 * @param time 时间
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findOfflineCourseV2")
	public JSONObject findOfflineCourseV2(String time,Page page,Long userId,Long courseType,Integer newtype){
		try{
			if(NumberUtils.isNullOrZero(newtype)){
				newtype = 0;
			}
			LocalDate now = LocalDate.now();
			
			List<OfflineCourseListInfo> offlineList = null;
				offlineList = courseService.findOfflineCourseListV2(time,page,courseType,0,newtype);
			
			offlineList.forEach(offline -> {
				
				String teachers = "";
				String id = offline.getTeacherIds();
				String arr[] = id.split(",");
				for(int i=0;i<arr.length;i++){
					Member m = memberService.findMemberById(Long.valueOf(arr[i]));
					if(m != null){
						teachers += m.getNickName();
						teachers += ",";
					}
					if(i == index){
						teachers = StringUtils.removeEndChar(teachers, ',');
						if(arr.length > index){
							teachers += "等";
						}
						break;
					}
				}
				offline.setTeachers(StringUtils.removeEndChar(teachers, ','));
				
				if(NumberUtils.isNullOrZero(userId)){
					offline.setIsFavour(0);
				}else{
					//设置是否点赞过
					OfflineCourseFavour favour  = courseService.findCourseFavourInfo(offline.getId(), userId);
					offline.setIsFavour(favour == null || favour.getStatus() == 0 ? 0 : 1);
				}
				int	userCount = courseService.findOfflineCourseFavourCount(offline.getId());
				offline.setUserCount(userCount);
				
				List<UserSimpleInfo> simpleList = courseService.findCourseFavourListByOfflineId(offline.getId());
				//添加报名人数
				offline.setJoinNum(courseService.findCourseJoinCount(offline.getId()));
				//添加点赞用户列表
				offline.setUserList(simpleList);
				
				
				LocalDate begin = LocalDate.parse(DateUtils.formatDate(offline.getBeginTime(),DateUtils.DATE_FORMAT_NORMAL));
				if(now.isAfter(begin) || now.isEqual(begin) || offline.getJoinNum() >= offline.getStudentNum()){
					//报名时间截止,报名人数已经足够  停止报名  
					offline.setIsOver(1);
				}else{
					offline.setIsOver(0);
				}
				if(now.isAfter(begin) || now.isEqual(begin)){
					//报名时间截止,报名人数已经足够  停止报名  
					offline.setDeLate(1);;
				}else{
					offline.setDeLate(0);
				}
				if(offline.getId() == 129){
					offline.setIsOver(1);
				}
			});
			
			Long count = 0L;
				count = courseService.findOfflineCourseCount( courseType);
			
			
			// 获取结束时间
			if(!NumberUtils.isNullOrZero(courseType)){
				return ok("查询成功",offlineList,page.getNowPage(),count);
			}else{
				return ok("查询成功",offlineList,count);
			}
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: findOfflineCourseListInfo 
	 * @Description: (查询课程列表中得单条信息) 
	 * @param courseId 课程ID
	 * @param userId 用户id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findOfflineCourseListInfo")
	public JSONObject findCourseListInfo(Long courseId,Long userId){
		try{
			if(NumberUtils.isNullOrZero(courseId)){
				return error("参数缺失");
			}
			OfflineCourseListInfo info = courseService.findOfflineCourseListInfoByCourseId(courseId);
			if(info == null){
				return error("参数错误");
			}
			if(NumberUtils.isNullOrZero(userId)){
				info.setIsFavour(0);
			}else{
				//设置是否点赞过
				OfflineCourseFavour favour = courseService.findCourseFavourInfo(courseId, userId);
				if(favour == null || favour.getStatus() == 0){
					info.setIsFavour(0);
				}else{
					info.setIsFavour(1);
				}
			}
			info.setUserCount(courseService.findOfflineCourseFavourCount(info.getId()));
			
			//添加点赞用户列表
			List<UserSimpleInfo> simpleList = courseService.findCourseFavourListByOfflineId(info.getId());
			info.setUserList(simpleList);
			
			//添加报名人数
			info.setJoinNum(courseService.findCourseJoinCount(info.getId()));
			
			return ok("查询成功",info);
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: findOfflineCourseInfo 
	 * @Description: (查询课程信息) 
	 * @param courseId 课程ID
	 * @param userId 用户id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findOfflineCourseInfo")
	public JSONObject findOfflineCourseInfo(Long courseId,Long userId){
		try{
			if(NumberUtils.isNullOrZero(courseId)){
				return error("参数缺失");
			}
			OfflineCourseInfo offline = courseService.findOfflineCourseAllInfo(courseId);
			if(offline == null){
				return error("参数错误");
			}
			
			//设置图片
			List<OfflineCourseImage> images = courseService.findOfflineCourseImages(courseId);
			List<String> imageList = new ArrayList<>();
			if(images == null || images.isEmpty()){
				imageList.add(commonService.appenUrl("/") + configInfo.getCourseDefaultImage());
			}else{
				images.forEach(image -> {
					imageList.add(image.getImageUrl());
				});
			}
			//设置返回信息
			if(NumberUtils.isNullOrZero(userId)){
				offline.setIsFavour(0);
			}else{
				//设置是否点赞过
				OfflineCourseFavour favour = courseService.findCourseFavourInfo(courseId, userId);
				offline.setIsFavour(favour == null || favour.getStatus() == 0 ? 0 : 1);
			}
			List<UserSimpleInfo> favourList = courseService.findCourseFavourListByOfflineId(courseId);
			//添加报名人数
			offline.setJoinNum(courseService.findCourseJoinCount(offline.getId()));
			//添加点赞用户列表
			offline.setUserList(favourList);
			offline.setImages(imageList);
			
			LocalDate now = LocalDate.now();
			LocalDate begin = LocalDate.parse(DateUtils.formatDate(offline.getBeginTime(),DateUtils.DATE_FORMAT_NORMAL));
			if(now.isAfter(begin) || offline.getJoinNum() >= offline.getStudentNum()){
				//报名时间截止,报名人数已经足够  停止报名  
				offline.setIsComment(0);
			}else{
				if(NumberUtils.isNullOrZero(userId)){
					offline.setIsComment(1);
				}else{
					//查询user是否报过名
					CourseOfflineJoin join = courseService.findCourseJoinInfo(offline.getId(),userId);
					//报过名就显示评论
					offline.setIsComment(join == null ? 1 : 0);
				}
			}
			
			List<UserSimpleInfo> teacherList = new ArrayList<>();
			try{
				String[] arr = offline.getIds().split(",");
				for(String s : arr){
					UserSimpleInfo info = memberService.findSimpleMemberInfo(Long.valueOf(s));
					if(info == null){
						continue;
					}
					teacherList.add(info);
				}
			}catch(Exception e){
				teacherList = new ArrayList<>();
			}
			
			offline.setFavourCount(courseService.findOfflineCourseFavourCount(offline.getId()));
			offline.setTeacherList(teacherList);
			return ok("查询成功",offline);
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: findOfflineCourseJoinUser 
	 * @Description: (查询线下课程报名用户) 
	 * @param courseId 课程ID
	 * @param page 分页
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findOfflineCourseJoinUser")
	public JSONObject findOfflineCourseJoinUser(Long courseId,Page page){
		try{
			if(NumberUtils.isNullOrZero(courseId)){
				return error("参数缺失");
			}
			//查询参加用户列表
			List<UserSimpleInfo> joinList = courseService.findCourseJoinList(page, courseId);
			int count = courseService.findCourseJoinCount(courseId);
			return ok("查询成功",joinList,page.getNowPage(),count);
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: findOfflineCourseComment 
	 * @Description: (查询线下课程评论列表) 
	 * @param otherId 课程ID
	 * @param userId 用户id
	 * @param page 分页
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findOfflineCourseComment")
	public JSONObject findOfflineCourseComment(Long otherId,Long userId,Page page){
		try{
			if(NumberUtils.isNullOrZero(otherId)){
				return error("参数缺失");
			}
			List<OfflineCourseCommentInfo> infoList = courseService.findCourseCommentByCourseId(otherId);
			infoList.forEach(info -> {
				if(NumberUtils.isNullOrZero(userId)){
					info.setIsAgree(0);
				}else{
					//设置是否赞过
					info.setIsAgree(courseService.findCommentFavour(info.getId(), userId) == null ? 0 : 1);
				}
				//设置评论点赞数量
				info.setAgree(courseService.findCommentFavourCount(info.getId()));
			});
			return ok("查询成功",infoList);
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: updateCommentFavour 
	 * @Description: (线下课程评论点赞) 
	 * @param commentId  评论ID
	 * @param userId	用户ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateCommentFavour")
	public JSONObject updateCommentFavour(Long commentId,Long userId){
		try{
			if(NumberUtils.isNullOrZero(commentId) || NumberUtils.isNullOrZero(userId) ){
				return error("参数缺失");
			}
			
			OfflineCourseCommentFavour favour = courseService.findCommentFavour(commentId, userId);
			if(favour == null){
				Member member = memberService.findMemberById(userId);
				if(member == null){
					return error("参数错误");
				}
				favour = new OfflineCourseCommentFavour();
				favour.setCommentId(commentId);
				favour.setMemberId(userId);
				if(courseService.saveCommentFavour(favour)){
					return ok("操作成功");
				}
			}else{
				favour.setStatus(favour.getStatus() == 0 ? 1 : 0);
				if(courseService.updateCommentFavour(favour)){
					return ok("操作成功");
				}
			}
			return error("操作失败");
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: updateCourseFavour 
	 * @Description: (线下课程点赞) 
	 * @param courseId 课程ID
	 * @param userId 用户id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateCourseFavour")
	public JSONObject updateCourseFavour(Long courseId,Long userId){
		try{
			if(NumberUtils.isNullOrZero(courseId) || NumberUtils.isNullOrZero(userId)){
				return error("参数缺失");
			}
			OfflineCourseFavour favour = courseService.findCourseFavourInfo(courseId, userId);
			if(favour == null){
				Member member = memberService.findMemberById(userId);
				if(member == null){
					return error("参数错误");
				}
				favour = new OfflineCourseFavour();
				favour.setCourseId(courseId);
				favour.setMemberId(userId);
				if(courseService.saveCourseFavour(favour)){
					return ok("操作成功");
				}
			}else{
				favour.setStatus(favour.getStatus() == 1 ? 0 : 1);
				if(courseService.updateCourseFavour(favour)){
					return ok("操作成功");
				}
			}
			return error("操作失败");
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: saveOfflineComment 
	 * @Description: (保存评论) 
	 * @param userId 用户id
	 * @param courseId 课程ID
	 * @param star 评分
	 * @param anonymous 是否匿名 0:不匿名 1:匿名
	 * @param content 内容
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveOfflineComment")
	public JSONObject saveOfflineComment(Long userId,Long courseId,Double star,Integer anonymous,String content){
		try{
			if(NumberUtils.isNullOrZero(userId) || NumberUtils.isNullOrZero(courseId) || NumberUtils.isNullOrZero(star) || anonymous == null){
				return error("参数缺失");
			}
			Member member = memberService.findMemberById(userId);
			if(member == null){
				return error("参数错误");
			}
			if(StringUtils.isNullOrEmpty(content)){
				content = null;
			}
			OfflineCourseComment comment = new OfflineCourseComment();
			comment.setAnonymous(anonymous);
			comment.setContent(content);
			comment.setCourseId(courseId);
			comment.setMemberId(userId);
			comment.setNickName(anonymous == 1 ? commonService.getRandomNickName(userId) : member.getNickName());
			comment.setStar(star);
			if(courseService.saveOfflineCourseComment(comment)){
				return ok("保存成功");
			}
			return error("保存失败");
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: findOfflineCourseContent 
	 * @Description: (查询线下课程简介) 
	 * @param courseId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findOfflineCourseContent")
	public JSONObject findOfflineCourseContent(Long courseId,String type){
		try{
			if(NumberUtils.isNullOrZero(courseId)){
				return error("参数缺失");
			}
			String info = courseService.findOfflineCourseInfo(courseId);
			//根据课程Id查询对应的系列课
//			List<Catalog> cList = courseService.findCatalogListByCourseId(courseId);
			if("1".equals(type)){
				OfflineCourseListInfo courseInfo = courseService.findOfflineCourseListInfoByCourseId(courseId);
				courseInfo.setContent(info+css);
				if(!NumberUtils.isNullOrZero(courseInfo.getCatalogId())){
					Catalog catalog = courseService.findCatalogInfoById(courseInfo.getCatalogId());
					if(catalog!=null){
						courseInfo.setCatalogId(catalog.getId());
						courseInfo.setFlag(catalog.getFlag());
						courseInfo.setIfVideo(catalog.getType());
						courseInfo.setPayStatus(catalog.getPayStatus());
					}
				}
//				if(cList.size()>0){
//					courseInfo.setCatalogId(cList.get(0).getId());
//					courseInfo.setFlag(cList.get(0).getFlag());
//					courseInfo.setIfVideo(cList.get(0).getType());
//					courseInfo.setPayStatus(cList.get(0).getPayStatus());
//				}
				return ok("查询成功",courseInfo);
			}else{
				return ok("查询成功",info+css);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: findCatalogContent 
	 * @Description: (查询线下课程简介) 
	 * @param courseId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findCatalogContent")
	public JSONObject findCatalogContent(Long catalogId){
		try{
			if(NumberUtils.isNullOrZero(catalogId)){
				return error("参数缺失");
			}
			String info = courseService.findCatalogContent(catalogId);
			return ok("查询成功",info+css);
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: findOnlineCourseByCourseId 
	 * @Description: (根据课程ID查询线上课程信息) 
	 * @param courseId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findOnlineCourseByCourseId")
	public JSONObject findOnlineCourseByCourseId(Long courseId){
		try{
			if(NumberUtils.isNullOrZero(courseId)){
				return error("参数错误");
			}
			OnlineCourseListInfo info = courseService.findOnlineCourseInfoByCourseId(courseId);
			CourseWebInfo web = commonService.findCourseWebInfo(info.getId());
			if(web != null){
				info.setUrl(web.getUrl());
				info.setNumber(web.getNumber());
			}
			
			info.setWeek(DateUtils.getWeekOfDate(info.getPlayStartTime()));
			
//			if(info.getType() == 2){
//				info.setPlayAddress("");
//			}
			return ok("查询成功",info);
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: updateOnlineCourseFavour 
	 * @Description: (线上课程点赞) 
	 * @param courseId 课程id
	 * @param userId 用户id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateOnlineCourseFavour")
	public JSONObject updateOnlineCourseFavour(Long courseId,Long userId){
		try{
			if(NumberUtils.isNullOrZero(userId) || NumberUtils.isNullOrZero(courseId)){
				return error("参数错误");
			}
			OnlineCourseFavour favour = courseService.findOnlineCourseFavour(courseId, userId);
			if(favour == null){
				favour = new OnlineCourseFavour();
				favour.setCourseId(courseId);
				favour.setMemberId(userId);
				courseService.saveOnlineCourseFavour(favour);
			}else{
				favour.setStatus(favour.getStatus() == 0 ? 1 : 0);
				courseService.updateOnlineCourseFavour(favour);
			}
			return ok();
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: findOnlineCourseList 
	 * @Description: (查询线上课程列表) 
	 * @param time 时间
	 * @param type 类别 1:正在直播  2:预告 3:点播
	 * @param userId 用户id
	 * @return
	 */
	@Deprecated
	@ResponseBody
	@RequestMapping("/findOnlineCourseList")
	public JSONObject findOnlineCourseList(String time,Integer type,Long userId,Integer from){
		try{
			if(NumberUtils.isNullOrZero(type)){
				type = 1;
			}
			if(from == null){
				from = 1;
			}
			ThirdMember third = memberService.findThirdMemberByMemberId(userId);
			
			List<OnlineCourseListInfo> onlineList  = new ArrayList<>();
			List<OnlineCourseListInfo> infoList = courseService.findOnlineCourseList(time, type,from);
			infoList.forEach(info -> {
				info.setUserCount(courseService.findOnlineCourseFavourCount(info.getId()));
				info.setUserList(courseService.findOnlineJoinMemberLimit10(info.getId()));
				
				if(!configInfo.getProjectTest()){
					info.setShareUrl("http://www.chazuomba.com/files/courseWeb/offline.html");
					info.setHtmlUrl("http://www.chazuomba.com/files/courseWeb/onlineShow.html?courseId="+info.getId());
				}else{
					info.setShareUrl("http://www.chazuomba.com/files/hehe/offline.html");
					info.setHtmlUrl("http://www.chazuomba.com/files/hehe/onlineShow.html?courseId="+info.getId());
				}
				
				
				CourseWebInfo web = commonService.findCourseWebInfo(info.getId());
				if(web != null){
					info.setUrl(web.getUrl());
					info.setNumber(web.getNumber());
				}
				
				if(third == null){
					info.setIsBuy("false");
				}else if(StringUtils.isNullOrEmpty(third.getUnionid())){
					info.setIsBuy("false");
				}else{
					int count = orderService.findOnlineCourseBugCount(info.getId(), third.getUnionid());
					if(count == 0){
						info.setIsBuy("false");
					}else{
						info.setIsBuy("true");
					}
				}
				
				info.setWeek(DateUtils.getWeekOfDate(info.getPlayStartTime()));
				
				if(StringUtils.isNullOrEmpty( info.getPlayAddress())){
					info.setPlayAddress("http://www.chazuomba.com/files/courseFile/2015-07-29/28eeaefc34304f53915318498dfdddf9.mp3");
				}
				
				//判断是否点赞
				OnlineCourseFavour favour = courseService.findOnlineCourseFavour(info.getId(), userId);
				if(favour == null || favour.getStatus() == 0){
					info.setIsFavour("false");
				}else{
					info.setIsFavour("true");
				}
				info.setType(3);
				
				onlineList.add(info);
			});
			Long count = courseService.findOnlineCourseCount();
			return ok("查询成功",onlineList,count);
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	@ResponseBody
	@RequestMapping("/findOnlineCourseListV2")
	public JSONObject findOnlineCourseListV2(Page page){
		try{
			List<OnlineCourseListInfo> list = courseService.findOnlineCourseListV2(page.getNowPage());
			
			return ok("查询成功",list,page.getNowPage(),courseService.findOnlineCourseCountV2());
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: joinOnlineCourse 
	 * @Description: (参加/离开线上课程) 
	 * @param userId
	 * @param courseId
	 * @param type
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/joinOnlineCourse")
	public JSONObject joinOnlineCourse(Long userId,Long courseId,Integer type){
		try{
			if(NumberUtils.isNullOrZero(userId) || NumberUtils.isNullOrZero(courseId) || type == null){
				return error("参数缺失");
			}
			Params params = new Params();
			params.putData("memberId", userId);
			params.putData("courseId", courseId);
			
			CourseOnlineJoin join = courseService.findCourseOnlineJoinInfo(params);
			if(join == null && type == 1){
				join = new CourseOnlineJoin();
				join.setCourseId(courseId);
				join.setMemberId(userId);
				if(courseService.saveCourseOnlineInfo(join)){
					return ok("操作成功");
				}
			}else{
				if(type == 0 && join == null){
					return ok("操作成功");
				}
				if(join.getStatus() == type){
					return ok("操作成功");
				}
				join.setStatus(type);
				if(courseService.updateCourseOnlineInfoStatus(join)){
					return ok("操作成功");
				}
			}
			return error("操作失败");
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: saveOnlineComment 
	 * @Description: (保存线上课程评论) 
	 * @param userId 用户id
	 * @param courseId 课程id
	 * @param content 评论内容
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveOnlineComment")
	public JSONObject saveOnlineComment(Long userId,Long courseId,String content){
		try{
			if(NumberUtils.isNullOrZero(courseId) || userId == null || content == null){
				return error("参数缺失");
			}
			OnlineCourseComment comment = new OnlineCourseComment();
			Member member = memberService.findMemberById(userId);
			if(member == null){
				comment.setMemberId(0L);
				comment.setNickName(commonService.getRandomNickName(Long.valueOf(CodeUtils.getRandomInt(20))));
				comment.setAvatar(commonService.getRandomAvatar());
			}else{
				comment.setMemberId(userId);
				comment.setNickName(member.getNickName());
				comment.setAvatar(member.getAvatar());
			}
			comment.setContent(content);
			comment.setCourseId(courseId);
			
			
			if(courseService.saveOnlineCourseComment(comment)){
				String key = cacheInfo.getCacheOnlineCommentCount() + "courseId--"+courseId;
				super.deleteCache(redisClient, key);
				return ok("保存成功");
			}
			
			return error("保存失败");
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: findOnlineComment 
	 * @Description: (查询线上课程评论列表 ) 
	 * @param courseId 课程ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findOnlineComment")
	public JSONObject findOnlineComment(Long courseId,String time){
		try{
			if(NumberUtils.isNullOrZero(courseId)){
				return error("参数缺失");
			}
			if(StringUtils.isNullOrEmpty(time)){
				time = DateUtils.formatDate(DateUtils.getNowDate());
			}
			Params params = new Params();
			params.putData("time", time);
			params.putData("courseId", courseId);
			List<OnlineCourseCommentInfo> infoList = courseService.findOnlineCommentList(params);
			return ok("查询成功",infoList);
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	
	/**
	 * @Title: findOnlineCommentCount 
	 * @Description: (查询线上课程评论数量) 
	 * @param courseId 课程ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findOnlineCommentCount")
	public JSONObject findOnlineCommentCount(Long courseId){
		try{
			if(NumberUtils.isNullOrZero(courseId)){
				return error("参数缺失");
			}
			Integer count = 0;
			String key = cacheInfo.getCacheOnlineCommentCount() + "courseId--"+courseId;
			if(redisClient.isExist(key)){
				count = (Integer)redisClient.getObject(key);
			}else{
				Params params = new Params();
				params.putData("courseId", courseId);
				count = courseService.findOnlineCommentCount(params);
				redisClient.setObject(key, count, cacheInfo.getRedisCacheLevel4());
			}
			return ok("查询成功",count);
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: findOnlineCourseContent 
	 * @Description: (查询线上课程内容)
	 * @param courseId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findOnlineCourseContent")
	public JSONObject findOnlineCourseContent(Long courseId,Integer type){
		try{
			if(NumberUtils.isNullOrZero(courseId)){
				return error("参数缺失");
			}
			String info = courseService.findOnlineCourseContent(courseId);
			if(NumberUtils.isNullOrZero(type)){
				return ok("ok",info+css);
			}else{
				return ok("ok",info);
			}
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: findOnlinJoinMember 
	 * @Description: (查询房间在线用户) 
	 * @param courseId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findOnlinJoinMember")
	public JSONObject findOnlinJoinMember(Long courseId){
		try{
			
			if(NumberUtils.isNullOrZero(courseId)){
				return error("参数缺失");
			}
			Params params = new Params();
			params.putData("courseId", courseId);
			
			List<UserSimpleInfo> infoList = courseService.findOnlineJoinMember(params);
			Long count = courseService.findOnlineJoinNumber(courseId);
			return ok("查询成功",infoList,count);
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: updateAppClickNumber 
	 * @Description: (修改客户端课程点击数量) 
	 * @param courseId 课程ID
	 * @param userId 用户ID
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateAppClickNumber")
	public JSONObject updateAppClickNumber(Long courseId,Long userId,HttpServletRequest request){
		try{
			if(NumberUtils.isNullOrZero(courseId)){
				return error("参数错误");
			}
			if(NumberUtils.isNullOrZero(userId)){
				userId = 0L;
			}
			String ipAddress = request.getRemoteAddr();
			courseService.updateAppCourseClickNumber(courseId);
			
			//保存点击日志
			CourseClickLog log = new CourseClickLog();
			log.setCourseId(courseId);
			log.setCreateAt(DateUtils.getNowDate());
			log.setIpAddress(ipAddress);
			log.setType(1);
			log.setMemberId(userId);
			
			logService.saveCourseClickLog(log);
			return ok("ok");
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: findOfflineCourse 
	 * @Description: (查询线下课程列表) 
	 * @param page 页数
	 * @param type 类别 0:最新课程  1:热门课程
	 * @param searchName 搜索
	 * @param userId 当前登录用户
	 * @param courseType 线下课程类别
	 * @param number 条数
	 * @return
	 */
	@Deprecated
	@ResponseBody
	@RequestMapping("/findOfflineCourse")
	public JSONObject findOfflineCourse(Page page,Integer type,String searchName,Long userId,Long courseType,Integer number){
		try{
			if(NumberUtils.isNullOrZero(type)){
				type = 0;
			}
			List<OfflineCourseListInfo> offlineList = courseService.findOfflineCourseList(page, type == 0, searchName, courseType,number);
			offlineList.forEach(offline -> {
				if(NumberUtils.isNullOrZero(userId)){
					offline.setIsFavour(0);
				}else{
					//设置是否点赞过
					OfflineCourseFavour favour  = courseService.findCourseFavourInfo(offline.getId(), userId);
					offline.setIsFavour(favour == null || favour.getStatus() == 0 ? 0 : 1);
				}
				
				offline.setUserCount(courseService.findOfflineCourseFavourCount(offline.getId()));
				List<UserSimpleInfo> simpleList = courseService.findCourseFavourListByOfflineId(offline.getId());
				//添加报名人数
				offline.setJoinNum(courseService.findCourseJoinCount(offline.getId()));
				//添加点赞用户列表
				offline.setUserList(simpleList);
			});
			
			Integer count = courseService.findOfflineCourseCount(searchName, courseType);
			return ok("查询成功",offlineList,page.getNowPage(),count);
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	
	@ResponseBody
	@RequestMapping("/findOfflineCourseV2ForSigned")
	public JSONObject findOfflineCourseV2ForSigned(){
		try{
			LocalDate now = LocalDate.now();
			List<OfflineCourseListInfo> offlineList = null;
			offlineList = courseService.findOfflineCourseV2ForSigned();
			offlineList.forEach(offline -> {
				String teachers = "";
				String id = offline.getTeacherIds();
				String arr[] = id.split(",");
				
				for(int i=0;i<arr.length;i++){
					Member m = memberService.findMemberById(Long.valueOf(arr[i]));
					if(m != null){
						teachers += m.getNickName();
						teachers += ",";
					}
					if(i == index){
						teachers = StringUtils.removeEndChar(teachers, ',');
						if(arr.length > index){
							teachers += "等";
						}
						break;
					}
				}
				offline.setTeachers(StringUtils.removeEndChar(teachers, ','));
				//添加报名人数
				offline.setJoinNum(courseService.findCourseJoinCount(offline.getId()));
				LocalDate begin = LocalDate.parse(DateUtils.formatDate(offline.getBeginTime(),DateUtils.DATE_FORMAT_NORMAL));
			});
			
			Long count = 0L;
			return ok("查询成功",offlineList);
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: saveSignedInfo
	 * @Description: (保存签到信息) 
	 * @param mobile 用户id
	 * @param courseId 课程ID
	 * @param courseName 课程名称
	 * @param createAt 签到时间
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveSignedInfo")
	public JSONObject saveSignedInfo(String mobile,Long courseId,String courseName,String createAt){
		try{
			if(mobile==null || NumberUtils.isNullOrZero(courseId) || courseName==null || createAt == null){
				return error("参数缺失");
			}
			Date date = new Date();
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
			DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat fmt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String sign = fmt.format(date)+" 12:00";
			String now=fmt1.format(date);
			List<OfflineJoinMember> list = new ArrayList<OfflineJoinMember>();
			List<OfflineSigned> signedList = new ArrayList<OfflineSigned>();
			//查询手机号在课程下是否报过名
			//判断mobile的位数，4位为模糊查询，12位为精准查询
			if(mobile.length()==4){
				//判断是否为迎新大课
				if(courseId==951){
					//查询是否报名迎新大课
					list = courseService.findOfflineJoinMemberByMobile(mobile);
				}else{
					list = courseService.findOfflineJoinForSigned(mobile,courseId);
				}
				if(list== null || list.size()==0){
					return status(500,"没有报名信息");
				}else if(list.size()== 1){
					if(courseId==951){
						if(courseService.findSignedListByMobileDaKe(mobile)==null || courseService.findSignedListByMobileDaKe(mobile)==""){
							//根据手机号码后四位查询报名大课详细信息
//							List<OfflineJoinMember> mlist = courseService.findOfflineJoinMemberByMobile(mobile);
//							//查询详细手机号
//							String lmobile=courseService.findOfflineJoinMobileForSignedDaKe(mobile,courseId);
//							//查询姓名
//							String name=courseService.findOfflineJoinNameForSignedDaKe(mobile,courseId);
//							//查询公司
//							String company = courseService.findOfflineJoinCompanyForSignedDaKe(lmobile,courseId);
//							//查询职位
//							String job = courseService.findOfflineJoinJobForSignedDaKe(lmobile,courseId);
							//将报名数据存入数据库
							OfflineSigned offlinesigned = new OfflineSigned();
							offlinesigned.setMobile(list.get(0).getMobile());
							offlinesigned.setName(list.get(0).getNickName());
							offlinesigned.setCourseId(list.get(0).getCourseId());
							offlinesigned.setCourseName(list.get(0).getCourseName());
							offlinesigned.setCreateAt(createAt);
							offlinesigned.setCompany(list.get(0).getCompany());
							offlinesigned.setJob(list.get(0).getJob());
							offlinesigned.setRow(list.get(0).getRowi());
							offlinesigned.setColumn(list.get(0).getColumni());
							list.add(list.get(0));
							if(courseService.saveOfflineSignedInfo(offlinesigned)){
								return status(200,"签到成功",list);
							}
						}else{
							//查询最后一条签到记录的创建时间
							String lastDate = fmt1.format(sdf.parse(courseService.findSignedListByMobileDaKe(mobile)));
							if(sdf.parse(now).after(sdf.parse(sign)) && sdf.parse(lastDate).after(sdf.parse(sign))){ 
								//起始日期大于结束日期 
								return status(300,"下午只能签到一次");
							}else if(sdf.parse(sign).after(sdf.parse(now)) && sdf.parse(sign).after(sdf.parse(lastDate))){ 
								//起始日期大于结束日期 
								return status(600,"上午只能签到一次");
							}else{
								List<OfflineJoinMember> mlist = courseService.findOfflineJoinMemberByMobile(mobile);
//								//查询详细手机号
//								String lmobile=courseService.findOfflineJoinMobileForSignedDaKe(mobile,courseId);
//								//查询姓名
//								String name=courseService.findOfflineJoinNameForSignedDaKe(mobile,courseId);
//								//查询公司
//								String company = courseService.findOfflineJoinCompanyForSignedDaKe(lmobile,courseId);
//								//查询职位
//								String job = courseService.findOfflineJoinJobForSignedDaKe(lmobile,courseId);
								//将报名数据存入数据库
								OfflineSigned offlinesigned = new OfflineSigned();
								offlinesigned.setMobile(mlist.get(0).getMobile());
								offlinesigned.setName(mlist.get(0).getNickName());
								offlinesigned.setCourseId(mlist.get(0).getCourseId());
								offlinesigned.setCourseName(mlist.get(0).getCourseName());
								offlinesigned.setCreateAt(createAt);
								offlinesigned.setCompany(mlist.get(0).getCompany());
								offlinesigned.setJob(mlist.get(0).getJob());
								offlinesigned.setRow(mlist.get(0).getRowi());
								offlinesigned.setColumn(mlist.get(0).getColumni());
								if(courseService.saveOfflineSignedInfo(offlinesigned)){
									return status(200,"签到成功",mlist);
								}
							}
						}
					}else{
						//判断是否审核通过
						if(("1").equals(list.get(0).getReviewStatus())){
							//根据手机号查询当天在课程的签到记录
							String signAt = courseService.findSignedListByMobile(mobile,courseId);
							if(signAt==null || signAt =="" || StringUtils.isNullOrEmpty(signAt)){
//								//查询详细手机号
//								String lmobile=courseService.findOfflineJoinMobileForSigned(mobile,courseId);
//								//查询姓名
//								String name=courseService.findOfflineJoinNameForSigned(mobile,courseId);
//								//查询公司
//								String company = courseService.findOfflineJoinCompanyForSigned(lmobile,courseId);
//								//查询职位
//								String job = courseService.findOfflineJoinJobForSigned(lmobile,courseId);
								//将报名数据存入数据库
								OfflineSigned offlinesigned = new OfflineSigned();
								offlinesigned.setMobile(list.get(0).getMobile());
								offlinesigned.setName(list.get(0).getNickName());
								offlinesigned.setCourseId(courseId);
								offlinesigned.setCourseName(courseName);
								offlinesigned.setCreateAt(createAt);
								offlinesigned.setCompany(list.get(0).getCompany());
								offlinesigned.setJob(list.get(0).getJob());
								if(courseService.saveOfflineSignedInfo(offlinesigned)){
									return status(200,"签到成功",list.get(0));
								}
							}else{
								//查询最后一条签到记录的创建时间
								String lastDate = fmt1.format(sdf.parse(courseService.findSignedListByMobile(mobile,courseId)));
								if(sdf.parse(now).after(sdf.parse(sign)) && sdf.parse(lastDate).after(sdf.parse(sign))){ 
									//起始日期大于结束日期 
									return status(300,"下午只能签到一次");
								}else if(sdf.parse(sign).after(sdf.parse(now)) && sdf.parse(sign).after(sdf.parse(lastDate))){ 
									//起始日期大于结束日期 
									return status(600,"上午只能签到一次");
								}else{
//									//查询详细手机号
//									String lmobile=courseService.findOfflineJoinMobileForSigned(mobile,courseId);
//									//查询姓名
//									String name=courseService.findOfflineJoinNameForSigned(mobile,courseId);
//									//查询公司
//									String company = courseService.findOfflineJoinCompanyForSigned(lmobile,courseId);
//									//查询职位
//									String job = courseService.findOfflineJoinJobForSigned(lmobile,courseId);
									//将报名数据存入数据库
									OfflineSigned offlinesigned = new OfflineSigned();
									offlinesigned.setMobile(list.get(0).getMobile());
									offlinesigned.setCourseId(courseId);
									offlinesigned.setCourseName(courseName);
									offlinesigned.setCreateAt(createAt);
									offlinesigned.setName(list.get(0).getNickName());
									offlinesigned.setCompany(list.get(0).getCompany());
									offlinesigned.setJob(list.get(0).getJob());
									if(courseService.saveOfflineSignedInfo(offlinesigned)){
										return status(200,"签到成功",list.get(0));
									}
								}
							}
						}else{
							//审核中
							if("0".equals(list.get(0).getReviewStatus())){
								list.get(0).setReviewStatus("审核中");
								return status(333,"审核中",list.get(0));
							}else if("2".equals(list.get(0).getReviewStatus())){
								list.get(0).setReviewStatus("审核未通过");
								return status(334,"审核未通过",list.get(0));
							}else{
								list.get(0).setReviewStatus("没有审核信息");
								return status(335,"没有审核信息",list.get(0));
							}
							
						}
					}
					
				}else{
					return statusNew(400,list);
//					return status(400,list);
				}
			}else{
				list = courseService.findOfflineJoinForSignedAll(mobile,courseId);
				if(list== null || list.size()==0){
					return status(500,"没有报名信息");
				}else if(list.size()== 1){
					if(courseId==951){
						if(courseService.findSignedListByMobileAllDaKe(mobile,courseId)==null || courseService.findSignedListByMobileAllDaKe(mobile,courseId)==""){
//							List<OfflineJoinMember> mlist = courseService.findOfflineJoinMemberByMobileAll(mobile);
							//查询姓名
							String name=courseService.findOfflineJoinNameForSignedAll(mobile,courseId);
							//查询公司
							String company = courseService.findOfflineJoinCompanyForSigned(mobile,courseId);
							//查询职位
							String job = courseService.findOfflineJoinJobForSigned(mobile,courseId);
							//将报名数据存入数据库
							OfflineSigned offlinesigned = new OfflineSigned();
							offlinesigned.setMobile(mobile);
							offlinesigned.setName(name);
							offlinesigned.setCourseId(courseId);
							offlinesigned.setCourseName(courseName);
							offlinesigned.setCreateAt(createAt);
							offlinesigned.setCompany(company);
							offlinesigned.setJob(job);
							if(courseService.saveOfflineSignedInfo(offlinesigned)){
								return status(200,"签到成功",list);
							}
						}else{
							String lastDate = fmt1.format(sdf.parse(courseService.findSignedListByMobileAll(mobile,courseId).toString()));
							if(sdf.parse(now).after(sdf.parse(sign)) && sdf.parse(lastDate).after(sdf.parse(sign))){ 
								//起始日期大于结束日期 
								return status(300,"下午只能签到一次");
							}else if(sdf.parse(sign).after(sdf.parse(now)) && sdf.parse(sign).after(sdf.parse(lastDate))){ 
								//起始日期大于结束日期 
								return status(600,"上午只能签到一次");
							}else{
								//查询姓名
								String name=courseService.findOfflineJoinNameForSignedAll(mobile,courseId);
								//查询公司
								String company = courseService.findOfflineJoinCompanyForSigned(mobile,courseId);
								//查询职位
								String job = courseService.findOfflineJoinJobForSigned(mobile,courseId);
								//将报名数据存入数据库
								OfflineSigned offlinesigned = new OfflineSigned();
								offlinesigned.setMobile(mobile);
								offlinesigned.setName(name);
								offlinesigned.setCourseId(courseId);
								offlinesigned.setCourseName(courseName);
								offlinesigned.setCreateAt(createAt);
								offlinesigned.setCompany(company);
								offlinesigned.setJob(job);
								if(courseService.saveOfflineSignedInfo(offlinesigned)){
									return status(200,"签到成功",list);
							}
						}
						}
					}else{
						//判断是否审核通过
						if(("1").equals(list.get(0).getReviewStatus())){
								if(courseService.findSignedListByMobileAll(mobile,courseId)==null || courseService.findSignedListByMobileAll(mobile,courseId)==""){
//									//查询姓名
//									String name=courseService.findOfflineJoinNameForSignedAll(mobile,courseId);
//									//查询公司
//									String company = courseService.findOfflineJoinCompanyForSigned(mobile,courseId);
//									//查询职位
//									String job = courseService.findOfflineJoinJobForSigned(mobile,courseId);
									//将报名数据存入数据库
									OfflineSigned offlinesigned = new OfflineSigned();
									offlinesigned.setMobile(mobile);
									offlinesigned.setName(list.get(0).getNickName());
									offlinesigned.setCourseId(courseId);
									offlinesigned.setCourseName(courseName);
									offlinesigned.setCreateAt(createAt);
									offlinesigned.setCompany(list.get(0).getCompany());
									offlinesigned.setJob(list.get(0).getJob());
									if(courseService.saveOfflineSignedInfo(offlinesigned)){
										return status(200,"签到成功",list.get(0));
									}
								}else{
									String lastDate = fmt1.format(sdf.parse(courseService.findSignedListByMobileAll(mobile,courseId).toString()));
									if(sdf.parse(now).after(sdf.parse(sign)) && sdf.parse(lastDate).after(sdf.parse(sign))){ 
										//起始日期大于结束日期 
										return status(300,"下午只能签到一次");
									}else if(sdf.parse(sign).after(sdf.parse(now)) && sdf.parse(sign).after(sdf.parse(lastDate))){ 
										//起始日期大于结束日期 
										return status(600,"上午只能签到一次");
									}else{
//										//查询姓名
//										String name=courseService.findOfflineJoinNameForSignedAll(mobile,courseId);
//										//查询公司
//										String company = courseService.findOfflineJoinCompanyForSigned(mobile,courseId);
//										//查询职位
//										String job = courseService.findOfflineJoinJobForSigned(mobile,courseId);
										//将报名数据存入数据库
										OfflineSigned offlinesigned = new OfflineSigned();
										offlinesigned.setMobile(mobile);
										offlinesigned.setName(list.get(0).getNickName());
										offlinesigned.setCourseId(courseId);
										offlinesigned.setCourseName(courseName);
										offlinesigned.setCreateAt(createAt);
										offlinesigned.setCompany(list.get(0).getCompany());
										offlinesigned.setJob(list.get(0).getJob());
										if(courseService.saveOfflineSignedInfo(offlinesigned)){
											return status(200,"签到成功",list.get(0));
									}
								}
							}
						}else{
							//审核中
							if("0".equals(list.get(0).getReviewStatus())){
								list.get(0).setReviewStatus("审核中");
								return status(333,"审核中",list.get(0));
							}else if("2".equals(list.get(0).getReviewStatus())){
								list.get(0).setReviewStatus("审核未通过");
								return status(334,"审核未通过",list.get(0));
							}else{
								list.get(0).setReviewStatus("没有审核信息");
								return status(335,"没有审核信息",list.get(0));
							}
						}
					}
				}else{
//					return status(400,"此手机号码报名信息有重复");
					//判断是否审核通过
					if(("1").equals(list.get(0).getReviewStatus())){
							if(courseService.findSignedListByMobileAll(mobile,courseId)==null || courseService.findSignedListByMobileAll(mobile,courseId)==""){
//								//查询姓名
//								String name=courseService.findOfflineJoinNameForSignedAll(mobile,courseId);
//								//查询公司
//								String company = courseService.findOfflineJoinCompanyForSigned(mobile,courseId);
//								//查询职位
//								String job = courseService.findOfflineJoinJobForSigned(mobile,courseId);
								//将报名数据存入数据库
								OfflineSigned offlinesigned = new OfflineSigned();
								offlinesigned.setMobile(mobile);
								offlinesigned.setName(list.get(0).getNickName());
								offlinesigned.setCourseId(courseId);
								offlinesigned.setCourseName(courseName);
								offlinesigned.setCreateAt(createAt);
								offlinesigned.setCompany(list.get(0).getCompany());
								offlinesigned.setJob(list.get(0).getJob());
								String names = "";
								String msg = "";
								if(courseService.saveOfflineSignedInfo(offlinesigned)){
									for(int i=0;i<list.size();i++){
										names +=list.get(i).getNickName()+"，";
									}
									msg="欢迎："+names.substring(0, names.length()-1)+list.size()+"位同学";
//									return status(200,"签到成功",list.get(0));
									return status(201,msg);
								}
							}else{
								String lastDate = fmt1.format(sdf.parse(courseService.findSignedListByMobileAll(mobile,courseId).toString()));
								if(sdf.parse(now).after(sdf.parse(sign)) && sdf.parse(lastDate).after(sdf.parse(sign))){ 
									//起始日期大于结束日期 
									return status(300,"下午只能签到一次");
								}else if(sdf.parse(sign).after(sdf.parse(now)) && sdf.parse(sign).after(sdf.parse(lastDate))){ 
									//起始日期大于结束日期 
									return status(600,"上午只能签到一次");
								}else{
//									//查询姓名
//									String name=courseService.findOfflineJoinNameForSignedAll(mobile,courseId);
//									//查询公司
//									String company = courseService.findOfflineJoinCompanyForSigned(mobile,courseId);
//									//查询职位
//									String job = courseService.findOfflineJoinJobForSigned(mobile,courseId);
									//将报名数据存入数据库
									OfflineSigned offlinesigned = new OfflineSigned();
									offlinesigned.setMobile(mobile);
									offlinesigned.setName(list.get(0).getNickName());
									offlinesigned.setCourseId(courseId);
									offlinesigned.setCourseName(courseName);
									offlinesigned.setCreateAt(createAt);
									offlinesigned.setCompany(list.get(0).getCompany());
									offlinesigned.setJob(list.get(0).getJob());
//									if(courseService.saveOfflineSignedInfo(offlinesigned)){
//										return status(200,"签到成功",list.get(0));
//									}
									String names = "";
									String msg = "";
									if(courseService.saveOfflineSignedInfo(offlinesigned)){
										for(int i=0;i<list.size();i++){
											names +=list.get(i).getNickName()+"，";
										}
										msg="欢迎："+names.substring(0, names.length()-1)+list.size()+"位同学";
//										return status(200,"签到成功",list.get(0));
										return status(201,msg);
									}
							}
						}
					}
				}
			}
			return error(APP_SYSTEM_ERROR);
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	@ResponseBody
	@RequestMapping("/findOfflineSignedJoin")
	public JSONObject findOfflineSignedJoin(Long courseId){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat fmt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String sign = fmt.format(date)+" 12:00";
		String now=fmt1.format(date);
		try{
			List<OfflineSigned> signedList = null;
			if(sdf.parse(sign).after(sdf.parse(now))){
				if(courseId==951){
					signedList = courseService.findOfflineSignedJoinListShangDaKe(courseId,sign);
				}else{
					signedList = courseService.findOfflineSignedJoinListShang(courseId,sign);
				}
				return ok("查询成功",signedList);
			}else{
				if(courseId==951){
					signedList = courseService.findOfflineSignedJoinListXiaDaKe(courseId,sign);
				}else{
					signedList = courseService.findOfflineSignedJoinListXia(courseId,sign);
				}
				return ok("查询成功",signedList);
			}
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	@ResponseBody
	@RequestMapping("/findOfflineNotSignedList")
	public JSONObject findOfflineNotSignedList(Long courseId){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat fmt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String sign = fmt.format(date)+" 12:00";
		String now=fmt1.format(date); 
		try{
			List signedMobileList = new ArrayList();
			List<OfflineJoinMember> notSignList = new ArrayList();
			//判断时间段是上午还是下午
			if(sdf.parse(sign).after(sdf.parse(now))){
				if(courseId==951){
					signedMobileList = courseService.findOfflineSignedMobileListShangDaKe(courseId,sign);
					if(signedMobileList.size()==0){
						notSignList = courseService.findOfflineNotSignedListDaKe(courseId);
					}else{
						notSignList = courseService.findOfflineNotSignedMobileListDaKe(courseId,sign);
					}
				}else{
					signedMobileList = courseService.findOfflineSignedMobileListShang(courseId,sign);
					if(signedMobileList.size()==0){
						notSignList = courseService.findOfflineNotSignedList(courseId);
					}else{
						notSignList = courseService.findOfflineNotSignedMobileList(courseId,sign);
					}
				}
				
				return ok("查询成功",notSignList);
			}else{
				if(courseId==951){
					signedMobileList = courseService.findOfflineSignedMobileListXia(courseId,sign);
					if(signedMobileList.size()==0){
						notSignList=courseService.findOfflineNotSignedListDaKe(courseId);
					}else{
						notSignList = courseService.findOfflineNotSignedMobileListDaKe(courseId,sign);
					}
				}else{
					signedMobileList = courseService.findOfflineSignedMobileListXia(courseId,sign);
					if(signedMobileList.size()==0){
						notSignList=courseService.findOfflineNotSignedList(courseId);
					}else{
						notSignList = courseService.findOfflineNotSignedMobileList(courseId,sign);
					}
				}
				return ok("查询成功",notSignList);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * 大课未签到名单
	 * @param courseId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findOfflineNotSignedListForDaKe")
	public JSONObject findOfflineNotSignedListForDaKe(Long courseId){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat fmt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String sign = fmt.format(date)+" 12:00";
		String now=fmt1.format(date); 
		try{
			List signedMobileList = new ArrayList();
			List<OfflineJoinMember> notSignList = new ArrayList();
			//判断时间段是上午还是下午
			if(sdf.parse(sign).after(sdf.parse(now))){
				signedMobileList = courseService.findOfflineSignedMobileListShang(courseId,sign);
				if(signedMobileList.size()==0){
					notSignList = courseService.findOfflineNotSignedList(courseId);
				}else{
					notSignList = courseService.findOfflineNotSignedMobileList(courseId,sign);
				}
				return ok("查询成功",notSignList);
			}else{
				signedMobileList = courseService.findOfflineSignedMobileListXia(courseId,sign);
				if(signedMobileList.size()==0){
					notSignList=courseService.findOfflineNotSignedList(courseId);
				}else{
					notSignList = courseService.findOfflineNotSignedMobileList(courseId,sign);
				}
				return ok("查询成功",notSignList);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	@ResponseBody
	@RequestMapping("/offlineNotSignedSendMessage")
	public JSONObject offlineNotSignedSendMessage(Long courseId){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat fmt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String sign = fmt.format(date)+" 12:00";
		String now=fmt1.format(date); 
		//创蓝短信
		ChuanglanSMS client = new ChuanglanSMS("M3667756","0927196a");
		CloseableHttpResponse response = null;
		StringBuffer errStr = new StringBuffer();
		try{
			List signedMobileList = new ArrayList();
			List<OfflineJoinMember> notSignList = new ArrayList();
			String result = "";
			//判断时间段是上午还是下午
			if(sdf.parse(sign).after(sdf.parse(now))){
				signedMobileList = courseService.findOfflineSignedMobileListShang(courseId,sign);
				if(signedMobileList.size()==0){
					notSignList = courseService.findOfflineNotSignedList(courseId);
					for(OfflineJoinMember member:notSignList){
						//发送短信
						response = client.sendMessage(member.getMobile(),msg.replace("#name#", member.getNickName()));
						String a = EntityUtils.toString(response.getEntity());
						org.json.JSONObject jsObject = new org.json.JSONObject(a);
						Map<String, Object> jsonMap = (Map<String, Object>) JSON.parse(jsObject.toString());
						if(!(jsonMap.get("success").toString().equals("true") || jsonMap.get("success").toString()=="true")){
							errStr.append(member.getMobile()).append(",");
						}
					}
				}else{
					notSignList = courseService.findOfflineNotSignedMobileList(courseId,sign);
					for(OfflineJoinMember member:notSignList){
						//发送短信
						response = client.sendMessage(member.getMobile(),msg.replace("#name#", member.getNickName()));
						String a = EntityUtils.toString(response.getEntity());
						org.json.JSONObject jsObject = new org.json.JSONObject(a);
						Map<String, Object> jsonMap = (Map<String, Object>) JSON.parse(jsObject.toString());
						if(!(jsonMap.get("success").toString().equals("true") || jsonMap.get("success").toString()=="true")){
							errStr.append(member.getMobile()).append(",");
						}
					}
				}
				return status(200,errStr.toString());
			}else{
				signedMobileList = courseService.findOfflineSignedMobileListXia(courseId,sign);
				if(signedMobileList.size()==0){
					notSignList=courseService.findOfflineNotSignedList(courseId);
					for(OfflineJoinMember member:notSignList){
						//发送短信
						response = client.sendMessage(member.getMobile(),msg.replace("#name#", member.getNickName()));
						String a = EntityUtils.toString(response.getEntity());
						org.json.JSONObject jsObject = new org.json.JSONObject(a);
						Map<String, Object> jsonMap = (Map<String, Object>) JSON.parse(jsObject.toString());
						if(!(jsonMap.get("success").toString().equals("true") || jsonMap.get("success").toString()=="true")){
							errStr.append(member.getMobile()).append(",");
						}
					}
				}else{
					notSignList = courseService.findOfflineNotSignedMobileList(courseId,sign);
					for(OfflineJoinMember member:notSignList){
						//发送短信
						response = client.sendMessage(member.getMobile(),msg.replace("#name#", member.getNickName()));
						String a = EntityUtils.toString(response.getEntity());
						org.json.JSONObject jsObject = new org.json.JSONObject(a);
						Map<String, Object> jsonMap = (Map<String, Object>) JSON.parse(jsObject.toString());
						if(!(jsonMap.get("success").toString().equals("true") || jsonMap.get("success").toString()=="true")){
							errStr.append(member.getMobile()).append(",");
						}
					}
				}
			}
			return status(200,errStr.toString());
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: findOnlineCourseContent 
	 * @Description: (查询线上课程内容)
	 * @param courseId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findSelfStudyContent")
	public JSONObject findSelfStudyContent(Long id){
		try{
			if(NumberUtils.isNullOrZero(id)){
				return error("参数缺失");
			}
			SelfStudy info = courseService.findSelfStudyContent(id);
			info.setCover(commonService.appenUrl(info.getCover()));
//			if(NumberUtils.isNullOrZero(type)){
//				return ok("ok",info+css);
//			}else{
				return ok("ok",info);
//			}
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: saveOnlineComment 
	 * @Description: (保存线上课程评论) 
	 * @param userId 用户id
	 * @param courseId 课程id
	 * @param content 评论内容
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveClickDetail")
	public JSONObject saveClickDetail(Long courseId,String ip,String city,String type,String from){
		try{
			CourseClickDetail click = new CourseClickDetail();
			
			click.setCourseId(courseId);
			click.setIp(ip);
			click.setCity(city);
			click.setType(type);
			click.setFrom1(from);
			if(courseService.saveCourseClickDetail(click)){
				return ok("保存成功");
			}
			return error("保存失败");
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: receiveGift 
	 * @Description: (领取赠品) 
	 * @param gCode 赠品码
	 * @param nickName 微信昵称
	 * @param avatar 微信头像
	 * @param unionId 微信UnionId
	 * @param mobile 手机号
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/receiveGift")
	public JSONObject receiveGift(String gCode,String nickName,String avatar,String unionId,String mobile){
		try{
			//先根据赠送码查询码的状态
			//判断是否存在与课程名称一致的企业信息
			List<Gift> giftList = businessService.findGiftByCode(gCode);
			String msg = "";
			if(giftList.size()>0){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
				Date date = sdf.parse(giftList.get(0).getExpDate());
				Date date1 = new Date();
				SimpleDateFormat f = new SimpleDateFormat("yyMMddHHmm"); //格式化为 hhmmss
				int d1Number = Integer.parseInt(f.format(date).toString()); //将第一个时间格式化后转为int
				int d2Number = Integer.parseInt(f.format(date1).toString()); //将第二个时间格式化后转为int
				if(d1Number<d2Number){
					return status(911,"课程礼品券已过期");
				}
				if(!StringUtils.isNullOrEmpty(giftList.get(0).getCollectionTime())){
					if(!(unionId).equals(giftList.get(0).getUnionId())){
						return status(901,"手慢了，礼品券已被其他人领取");
					}else{
						return status(902,"您已经领取过了");
					}
				}
				if(!"18610500240".endsWith(mobile)){
					//声明Gift对象，添加要更新的参数
					Gift gift = new Gift();
					gift.setAvatar(avatar);
					gift.setNickName(nickName);
					gift.setgCode(gCode);
					gift.setMobile(mobile);
					gift.setUnionId(unionId);
					String courseName = "赠送";
					logger.error("---------------------------课程名称中包含“系列课”------------------------"+courseName);
					System.out.println("---------------------------课程名称中包含“系列课”------------------------"+courseName);
					List<Business> businessList = businessService.findBusinessByName(courseName);
					if(businessList.size()>0){
						Business b= businessList.get(0);
						System.out.println("课程Id"+b.getId());
						System.out.println("课程名称"+b.getBusinessName());
						logger.error("---------------------------与课程名称相同的企业已存在------------------------"+courseName);
						System.out.println("---------------------------与课程名称相同的企业已存在------------------------"+courseName);
						// 验证用户是否存在
						Employee employee = memberService.findBusinessLoginMemberByMobile1(mobile);
						if (employee != null){
							logger.error("--------------验证用户已经注册过会员-------------------"+mobile);
							System.out.println("--------------验证用户已经注册过会员-------------------"+mobile);
							List<MemberRecord> recordList = businessService.findMemberRecordList(employee.getId().toString());
							if(recordList.size()>0){
								List<MemberSeries> seriesList = businessService.queryIsChoose(employee.getId(),71L);
								if(seriesList.size()>0){
									return status(913,"您已经拥有此课程了，送给其他好友吧");
								}else{
									MemberRecord record = new MemberRecord();
									record.setEmployeeId(employee.getId().toString());
									record.setCatalogId("71");
									record.setExpiryDate("365");
									//好多课年费会员type传0 系列课会员信息传1
									record.setType("1");
									record.setCourseId("20161215");
//									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									Calendar calendar = Calendar.getInstance();
									//取订单创建时间
							    	calendar.setTime(new Date());
							    	//订单创建时间加上会员日期为会员过期日期
							    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf("365"));
							    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							    	System.out.println(sdf.format(calendar.getTime()));
							    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						//	    	//会员过期时间
							        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
									record.setEndDate(dateTime2);
									//将数据写入选课表
									memberService.insertMemberSeries(record);
									//更新券的信息
									businessService.updateGiftByCode(gift);
									//发短信
									msg="【插坐学院】厉害了，我的"+nickName+"，您已成功领取充电学习包：《新媒体编辑必学的90招野路子》，它已经静静的躺在你的学习计划里啦！快去打开“好多课”APP学习吧，其他不懂的微信来找我，ID：chazuomba06";
									commonService.sendCLMsg(mobile,msg);
									return status(932,"好巧！您已经注册了好多课，课程已经加入学习计划，快去看看吧");
								}
							}else{
								//判断是否已经拥有权限
								List<MemberRecord> recordList1 = businessService.findMemberRecordListByCatalogId(employee.getId().toString(),"71");
								if(recordList1.size()>0){
									return status(913,"您已经拥有此课程了，送给其他好友吧");
								}else{
									MemberRecord record = new MemberRecord();
									record.setEmployeeId(employee.getId().toString());
									record.setCatalogId("71");
									record.setExpiryDate("365");
									//好多课年费会员type传0 系列课会员信息传1
									record.setType("1");
									record.setCourseId("20161215");
//									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									Calendar calendar = Calendar.getInstance();
									//取订单创建时间
							    	calendar.setTime(new Date());
							    	//订单创建时间加上会员日期为会员过期日期
							    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf("365"));
							    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							    	System.out.println(sdf.format(calendar.getTime()));
							    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						//	    	//会员过期时间
							        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
									record.setEndDate(dateTime2);
									memberService.insertMemberRecord(record);
									//将数据写入选课表
									memberService.insertMemberSeries(record);
									//更新券的信息
									businessService.updateGiftByCode(gift);
									//发短信
									msg="【插坐学院】厉害了，我的"+nickName+"，您已成功领取充电学习包：《新媒体编辑必学的90招野路子》，它已经静静的躺在你的已购列表里啦！快去打开“好多课”APP学习吧，其他不懂的微信来找我，ID：chazuomba06";
									commonService.sendCLMsg(mobile,msg);
									return status(200,"领取成功");
								}
							}
						}else{
								Employee newE = new Employee();
								//企业Id
								newE.setBusinessId(b.getId());
								//企业名称
								newE.setBusinessName(b.getBusinessName());
								//用户手机号
								newE.setMobile(mobile);
								//用户名
								newE.setName(nickName);
								//职位
								newE.setPosition("");
								//数据状态
								newE.setStatus(1);
								//会员日期为后台配置的天数
								newE.setExpiryDate(Long.parseLong("365"));
								//if_business的值为后台关联课程包的ID
								newE.setIfBusiness(Long.parseLong("71"));
								newE.setPassword(PasswdEncryption.generate(mobile));
								// 保存默认头像
								newE.setAvatar(commonService.getRandomAvatar());
								newE.setIfBusiness(58L);
								logger.error("--------------验证用户没有注册过会员----------------"+mobile+"=="+businessList.get(0).getId()+"==="+businessList.get(0).getBusinessName());
								System.out.println("--------------验证用户没有注册过会员----------------"+mobile+"=="+businessList.get(0).getId()+"==="+businessList.get(0).getBusinessName());
								//保存数据到数据库
								memberService.registerEmployeeXL(newE,null);
								//在t_business_member_record 表中写入会员购买记录
								MemberRecord record = new MemberRecord();
								record.setEmployeeId(newE.getId().toString());
								record.setCatalogId("71");
								record.setExpiryDate("365");
								//好多课年费会员type传0 系列课会员信息传1
								record.setType("1");
								record.setCourseId("20161215");
//								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								Calendar calendar = Calendar.getInstance();
								//取订单创建时间
						    	calendar.setTime(new Date());
						    	//订单创建时间加上会员日期为会员过期日期
						    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf("365"));
						    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						    	System.out.println(sdf.format(calendar.getTime()));
						    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					//	    	//会员过期时间
						        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
								record.setEndDate(dateTime2);
								memberService.insertMemberRecord(record);
								//将数据写入选课表
								memberService.insertMemberSeries(record);
								//更新券的信息
								businessService.updateGiftByCode(gift);
								//发短信
								msg="【插坐学院】厉害了，我的"+nickName+"，您已成功领取插坐学院的充电学习包：《新媒体编辑必学的90招野路子》，现在去下载“好多课”APP学习吧，账号密码均为手机号："+mobile+"，其他不懂的微信来找我，ID：chazuomba06";
								commonService.sendCLMsg(mobile,msg);
								return status(200,"领取成功");
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
							// 验证用户是否存在
							Employee employee = memberService.findBusinessLoginMemberByMobile1(mobile);
							if (employee != null) {
								List<MemberRecord> recordList = businessService.findMemberRecordList(employee.getId().toString());
								if(recordList.size()>0){
									List<MemberSeries> seriesList = businessService.queryIsChoose(employee.getId(),71L);
									if(seriesList.size()>0){
										return status(932,"好巧啊，你已经是好多课的用户了，课程包现在已经放在选课里了，快去看看吧");
									}else{
										MemberRecord record = new MemberRecord();
										record.setEmployeeId(employee.getId().toString());
										record.setCatalogId("71");
										record.setExpiryDate("365");
										//好多课年费会员type传0 系列课会员信息传1
										record.setType("1");
										record.setCourseId("20161215");
//										SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										Calendar calendar = Calendar.getInstance();
										//取订单创建时间
								    	calendar.setTime(new Date());
								    	//订单创建时间加上会员日期为会员过期日期
								    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf("365"));
								    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								    	System.out.println(sdf.format(calendar.getTime()));
								    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							//	    	//会员过期时间
								        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
										record.setEndDate(dateTime2);
										//将数据写入选课表
										memberService.insertMemberSeries(record);
										//更新券的信息
										businessService.updateGiftByCode(gift);
										//发短信
										commonService.sendCLMsg(mobile,msg);
										return status(932,"好巧啊，你已经是好多课的用户了，课程包现在已经为您放在选课里了，快去看看吧");
									}
								}else{
									List<MemberRecord> recordList1 = businessService.findMemberRecordListByCatalogId(employee.getId().toString(),"71");
									if(recordList1.size()>0){
										return status(913,"您填写的手机号已经开通了该系列课程会员，直接去登录吧");
									}else{
											MemberRecord record = new MemberRecord();
											record.setEmployeeId(employee.getId().toString());
											record.setCatalogId("71");
											record.setExpiryDate("365");
											//好多课年费会员type传0 系列课会员信息传1
											record.setType("1");
											record.setCourseId("20161215");
//											SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
											Calendar calendar = Calendar.getInstance();
											//取订单创建时间
									    	calendar.setTime(new Date());
									    	//订单创建时间加上会员日期为会员过期日期
									    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf("365"));
									    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									    	System.out.println(sdf.format(calendar.getTime()));
									    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								//	    	//会员过期时间
									        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
											record.setEndDate(dateTime2);
											memberService.insertMemberRecord(record);
											//将数据写入选课表
											memberService.insertMemberSeries(record);
											//更新券的信息
											businessService.updateGiftByCode(gift);
											//发短信
											msg="【插坐学院】厉害了，我的"+nickName+"，您已成功领取充电学习包：《新媒体编辑必学的90招野路子》，它已经静静的躺在你的已购列表里啦！快去打开“好多课”APP学习吧，其他不懂的微信来找我，ID：chazuomba06";
											commonService.sendCLMsg(mobile,msg);
											return status(200,"领取成功");
										}
									}
							}else{
								Employee newE = new Employee();
								//企业Id
								newE.setBusinessId(business.getId());
								//企业名称
								newE.setBusinessName(business.getBusinessName());
								//用户手机号
								newE.setMobile(mobile);
								//用户名
								newE.setName(nickName);
								//职位
								newE.setPosition("");
								//数据状态
								newE.setStatus(1);
								//会员日期180天
								newE.setExpiryDate(185L);
								//会员类型：企业会员
								newE.setIfBusiness(99999L);
								//创建时间
								newE.setCreateAt(new Date());
								//密码
								newE.setPassword(PasswdEncryption.generate(mobile));
								// 保存默认头像
								newE.setAvatar(commonService.getRandomAvatar());
								
								//保存数据到数据库
								logger.error("--------------验证用户没有注册过会员----------------"+mobile+"=="+business.getId()+"==="+business.getBusinessName());
								System.out.println("--------------验证用户没有注册过会员----------------"+mobile+"=="+business.getId()+"==="+business.getBusinessName());
								memberService.registerEmployeeXL(newE,null);
								//在t_business_member_record 表中写入会员购买记录
								MemberRecord record = new MemberRecord();
								record.setEmployeeId(newE.getId().toString());
								record.setCatalogId("71");
								record.setExpiryDate("365");
								//好多课年费会员type传0 系列课会员信息传1
								record.setType("1");
								record.setCourseId("0");
//								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								Calendar calendar = Calendar.getInstance();
								//取订单创建时间
						    	calendar.setTime(new Date());
						    	//订单创建时间加上会员日期为会员过期日期
						    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf("365"));
						    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						    	System.out.println(sdf.format(calendar.getTime()));
						    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					//	    	//会员过期时间
						        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
								record.setEndDate(dateTime2);
								memberService.insertMemberRecord1(record);
								//将数据写入选课表
								memberService.insertMemberSeries(record);
								//更新券的信息
								businessService.updateGiftByCode(gift);
								//发短信
								commonService.sendCLMsg(mobile,msg);
								return ok("领取成功");
							}
					}
				}
			}
			return ok("您使用的礼品券无效");
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: saveOnlineComment 
	 * @Description: (保存线上课程评论) 
	 * @param userId 用户id
	 * @param courseId 课程id
	 * @param content 评论内容
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/test")
	public JSONObject test(){
		try{
			List<DaKe> click = courseService.findList();
			for(int i=0;i<click.size();i++){
				Member me = memberService.findLoginMemberByMobile(click.get(i).getMobile());
				Long memberId = (me == null ? null : me.getId());
				courseService.updateZuoWei(memberId,click.get(i).getCourseId(),click.get(i).getPai(),click.get(i).getHao());
			}
			return error("更新成功"+click.size()+"条数据");
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: getWatchtoken 
	 * @Description: (获取保利威视直播用户登录进入房间的token) 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getWatchtoken")
	public JSONObject getWatchtoken(){
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
			//十位时间戳
			String ts = sdf.format(date);
			String sign =PasswdEncryption.MD5(ts+"polyvsign");
			try {
				PrintWriter out = null;
		        BufferedReader in = null;
		        String result = "";
		        String param="ts="+ts+"&sign="+sign;
		        try {
		            URL realUrl = new URL("http://api.live.polyv.net/watchtoken/gettoken");
		            // 打开和URL之间的连接
		            URLConnection conn = realUrl.openConnection();
		            // 设置通用的请求属性
		            conn.setRequestProperty("accept", "*/*");
		            conn.setRequestProperty("connection", "Keep-Alive");
		            conn.setRequestProperty("user-agent",
		                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		            // 发送POST请求必须设置如下两行
		            conn.setDoOutput(true);
		            conn.setDoInput(true);
		            // 获取URLConnection对象对应的输出流
		            out = new PrintWriter(conn.getOutputStream());
		            // 发送请求参数
		            out.print(param);
		            // flush输出流的缓冲
		            out.flush();
		            // 定义BufferedReader输入流来读取URL的响应
		            in = new BufferedReader(
		                    new InputStreamReader(conn.getInputStream()));
		            String line;
		            while ((line = in.readLine()) != null) {
		                result += line;
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
		        return ok(result);
			}catch(Exception e){
				e.printStackTrace();
			}
		return error("获取token失败");
	}
	/**
	 * @Title: getLivingWhatchNumber 
	 * @Description: (根据频道号查询直播观看人数) 
	 * @param unionId 微信Id
	 * @param content 评论内容
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getLivingWhatchNumber")
	public String getLivingWhatchNumber(String channelId){
		if(StringUtils.isNullOrEmpty(channelId)){
			return "channelId无效";
		}
		try {
			PrintWriter out = null;
	        String result = "";
			String appId = "eo3gj1d49l";
			SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMddHHmmss");
			String timestamp = Long.toString(df1.parse(df1.format(new Date())).getTime()).substring(0, 10);
			String userId = "edwi2f34r5";
			String appSecret = "9cd54bdb99244997b3f5a175c763c7a1";
			String str = appSecret+"appId"+appId+"timestamp"+timestamp+"userId"+userId+appSecret;
			String sign = PasswdEncryption.MD5Encode(str).toUpperCase();

	        try {
	        	URL U = new URL("http://api.live.polyv.net/v1/statistics/"+channelId+"/realtime?appId="+appId+"&timestamp="+timestamp+"&userId="+userId+"&sign="+sign);  
	        	URLConnection connection = U.openConnection();  
	        	   connection.connect();  
	        	   BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));  
	        	   String line;  
	        	   while ((line = in.readLine())!= null)  
	        	   {  
	        	    result += line;  
	        	   }  
	        	   in.close(); 
	            return result;
	        } catch (Exception e) {
	            System.out.println("发送 POST 请求出现异常！"+e);
	            e.printStackTrace();
	        }
		}catch(Exception e){
			e.printStackTrace();
			return "查询失败";
		}
		return "查询失败";
	}
	
	/**
	 * @Title: getLivingCourseDetail 
	 * @Description: (根据unionId和课程Id查询直播课程详细信息，包含：是否购买、课程简章、封面图) 
	 * @param unionId 微信Id
	 * @param content 评论内容
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getLivingCourseDetail")
	public JSONObject getLivingCourseDetail(String unionId,Long courseId){
		try {
			//查询直播课详细信息
			LivingCourseInfo info = courseService.findLivingeCourseInfoByCourseId(courseId);
			if(info==null){
				return status(301,"课程无效");
			}else{
				//根据课程Id和用户Id查询用户是否购买了课程
				List<SelfCourseOrder> list1 =  orderService.queryOrderListByUnionidAndCourseId(unionId,courseId);
				if(list1.size()>0){
					info.setIfBuy("1");
				}else{
					//根据unionid查询绑定的App账号是否为年票会员
					List<MemberRecord> list =  orderService.queryMemberRecordByUnionId(unionId);
					if(list.size()>0){
						info.setIfBuy("1");
					}else{
						if(info.getCatalogId()==0){
							info.setIfBuy("0");
						}else{
							//根据unionid查询绑定的App账号是否为年票会员
							List<MemberRecord> list2 =  orderService.queryMemberRecordByUnionIdAndCatalogId(unionId,info.getCatalogId());
							if(list2.size()>0){
								info.setIfBuy("1");
							}else{
								info.setIfBuy("0");
							}
						}
					}
					
				}
				info.setLivingStatus(getLivingStatus(info.getStream()));
			}
		    return ok("查询成功",info);
		}catch(Exception e){
			e.printStackTrace();
		}
		return error("获取课程信息失败");
	}
	/**
	 * 根据课程Id查询通过审核的学员名单用于发送结业证书邮件
	 * @param courseId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryMemberListByCourseIdForEmail")
	public JSONObject queryMemberListByCourseIdForEmail(Long courseId){
		if(courseId==0L){
			return status(300,"课程Id无效");
		}
		try{
			List<MemberInfoForEmail> memberInfoList = new ArrayList();
			memberInfoList = courseService.queryMemberListByCourseIdForEmail(courseId);
			return ok("查询成功",memberInfoList);
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: validatePwd 
	 * @Description: (验证密码) 
	 * @param pwd
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/validatePwd")
	public JSONObject validatePwd(String pwd){
		try{
			if(StringUtils.isNullOrEmpty(pwd)){
				return error("请输入密码");
			}
			if("973137".equals(pwd)){
				return ok("验证成功");
			}else{
				return status(300,"密码错误");
			}
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: joinOnlineCourse 
	 * @Description: (根据手机号查询用户的登录记录) 
	 * @param userId
	 * @param courseId
	 * @param type
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryLoginDetailByMobile")
	public JSONObject queryLoginDetailByMobile(String mobile){
		try{
			if(StringUtils.isNullOrEmpty(mobile)){
				List<LoginDetail> list = memberService.findBusinessLoginDetails();
				return ok("查询成功",list);
			}
			// 验证用户是否存在
			Employee employee = memberService.findBusinessLoginMemberByMobile1(mobile);
			if(employee==null){
				return status(400,"用户不存在");
			}else{
				List<LoginDetail> list = memberService.findBusinessLoginDetailsByEmployeeId(employee.getId());
				return ok("查询成功",list);
			}
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: queryEmployeeReponseLogByMobile 
	 * @Description: (根据手机号查询用户接口请求记录) 
	 * @param mobile
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryEmployeeReponseLogByMobile")
	public JSONObject queryEmployeeReponseLogByMobile(String mobile){
		try{
			if(StringUtils.isNullOrEmpty(mobile)){
				return error("参数缺失");
			}
			// 验证用户是否存在
			Employee employee = memberService.findBusinessLoginMemberByMobile1(mobile);
			if(employee==null){
				return status(400,"用户不存在");
			}else{
				List<HaoduokeLog> list = memberService.queryEmployeeReponseLogByEmployeeId(employee.getId());
				return ok("查询成功",list);
			}
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
}
