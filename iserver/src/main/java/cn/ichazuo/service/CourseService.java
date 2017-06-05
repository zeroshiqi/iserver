package cn.ichazuo.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ichazuo.commons.base.BaseService;
import cn.ichazuo.commons.util.DateUtils;
import cn.ichazuo.commons.util.NumberUtils;
import cn.ichazuo.commons.util.StringUtils;
import cn.ichazuo.commons.util.model.Page;
import cn.ichazuo.commons.util.model.Params;
import cn.ichazuo.dao.CourseDao;
import cn.ichazuo.model.admin.CourseImageListInfo;
import cn.ichazuo.model.admin.CourseListInfo;
import cn.ichazuo.model.admin.OfflineMsgInfo;
import cn.ichazuo.model.app.LivingCourseInfo;
import cn.ichazuo.model.app.OfflineCourseCommentInfo;
import cn.ichazuo.model.app.OfflineCourseInfo;
import cn.ichazuo.model.app.OfflineCourseListInfo;
import cn.ichazuo.model.app.OfflineJoinMember;
import cn.ichazuo.model.app.OfflineSigned;
import cn.ichazuo.model.app.OnlineCourseCommentInfo;
import cn.ichazuo.model.app.OnlineCourseListInfo;
import cn.ichazuo.model.app.OnlineCourseResultInfo;
import cn.ichazuo.model.app.SelfStudy;
import cn.ichazuo.model.app.UserSimpleInfo;
import cn.ichazuo.model.table.Catalog;
import cn.ichazuo.model.table.Course;
import cn.ichazuo.model.table.CourseClickDetail;
import cn.ichazuo.model.table.CourseOfflineJoin;
import cn.ichazuo.model.table.CourseOnlineJoin;
import cn.ichazuo.model.table.JNCourseOnlineJoin;
import cn.ichazuo.model.table.OfflineCourse;
import cn.ichazuo.model.table.OfflineCourseComment;
import cn.ichazuo.model.table.OfflineCourseCommentFavour;
import cn.ichazuo.model.table.OfflineCourseFavour;
import cn.ichazuo.model.table.OfflineCourseImage;
import cn.ichazuo.model.table.OnlineCourse;
import cn.ichazuo.model.table.OnlineCourseComment;
import cn.ichazuo.model.table.OnlineCourseFavour;

/**
 * @ClassName: CourseService 
 * @Description: (课程Service) 
 * @author ZhaoXu
 * @date 2015年7月19日 上午1:28:19 
 * @version V1.0
 */
@Service("courseService")
public class CourseService extends BaseService{
	private static final long serialVersionUID = 1L;
	@Resource
	private CourseDao courseDao;
	@Autowired
	private CommonService commonService;
	
	public OfflineMsgInfo findOfflineMsgInfo(Long id){
		return courseDao.findOfflineMsgInfo(id);
	}
	/**
	 * @Title: saveOnlineCourseFavour 
	 * @Description: (保存线上课程点赞信息) 
	 * @param favour
	 */
	public void saveOnlineCourseFavour(OnlineCourseFavour favour){
		courseDao.saveOnlineCourseFavour(favour);
	}
	
	/**
	 * @Title: updateOnlineCourseFavour 
	 * @Description: (修改线上课程点赞信息) 
	 * @param favour
	 */
	public void updateOnlineCourseFavour(OnlineCourseFavour favour){
		courseDao.updateOnlineCourseFavour(favour);
	}
	
	/**
	 * @Title: findOnlineCourseFavour 
	 * @Description: (查询线上课程点赞信息) 
	 * @param courseId
	 * @param memberId
	 * @return
	 */
	public OnlineCourseFavour findOnlineCourseFavour(Long courseId,Long memberId){
		Params params = new Params();
		params.putData("courseId", courseId);
		params.putData("memberId", memberId);
		return courseDao.findOnlineCourseFavour(params.getMap());
	}
	
	/**
	 * @Title: findOfflineCourseList 
	 * @Description: (查询线下课程列表) 
	 * @param page 分页
	 * @param type 是否按最新课程排序
	 * @param searchName 名称查询
	 * @param courseType 线下课程类别
	 * @return
	 */
	public List<OfflineCourseListInfo> findOfflineCourseListV2(String time,Page page,Long courseType,int from,int newtype){
		Params params = new Params(page.getNowPage());
		params.putData("from", from);
		params.putData("newtype", newtype);
		List<OfflineCourseListInfo> list = new ArrayList<>();
		if(!NumberUtils.isNullOrZero(courseType) && courseType!=3L){
			params.putData("type", "1");
			params.putData("courseType", courseType);	
			list = courseDao.findOfflineCourseListV2(params.getMap());
		}else{
			Date nowDate = DateUtils.parseDate(DateUtils.formatDate(DateUtils.getNowDate()));
			Date findDate = null;
			if(StringUtils.isNullOrEmpty(time)){
				findDate = DateUtils.getNowDate();
			}else{
				findDate = DateUtils.parseDate(time);
			}
			if(findDate.after(nowDate)){
				//判断是否是网站展示用的线上课程列表
				//如果courseType是3 查询官网线上课程
				if(courseType==3L){
					params.putData("time", findDate);
					params.putData("orderBy", 1);
					params.putData("number", 100);
					//查询未开始的课程
					list = courseDao.findOfflineCourseListOffical(params.getMap());
					
					if(list.size() < Params.PAGE_COUNT){
						//查询预播
						params.removeData("time");
						params.putData("time", DateUtils.formatDate(nowDate));
						params.removeData("orderBy");
						params.putData("orderBy", 2);
						params.putData("number", Params.PAGE_COUNT - list.size());
						list.addAll(courseDao.findOfflineCourseListOffical(params.getMap()));
					}
				}else{
					params.putData("time", findDate);
					params.putData("orderBy", 1);
					params.putData("number", Params.PAGE_COUNT);
					//查询未开始的课程
					list = courseDao.findOfflineCourseListV2(params.getMap());
					
					if(list.size() < Params.PAGE_COUNT){
						//查询预播
						params.removeData("time");
						params.putData("time", DateUtils.formatDate(nowDate));
						params.removeData("orderBy");
						params.putData("orderBy", 2);
						params.putData("number", Params.PAGE_COUNT - list.size());
						list.addAll(courseDao.findOfflineCourseListV2(params.getMap()));
					}
				}
				
			}else{
				//查询已结束的课程
				params.putData("time", findDate);
				params.putData("orderBy", 2);
				params.putData("number", Params.PAGE_COUNT);
				list = courseDao.findOfflineCourseListV2(params.getMap());
			}
		}
		
		list.forEach(info -> {
			info.setAvatar(commonService.appenUrl(info.getAvatar()));
			info.setCover(commonService.appenUrl(info.getCover()));
		});
		return list;
	}
	/**
	 * @Title: findOfflineCourseCount 
	 * @Description: (查询线下课程数量) 
	 * @param searchName
	 * @param courseType
	 * @return
	 */
	public Long findOfflineCourseCount(Long courseType){
		Params params = new Params();
		if(!NumberUtils.isNullOrZero(courseType)){
			params.putData("courseType", courseType);
		}
		return courseDao.findOfflineCourseListCountV2(params.getMap());
	}
	

	
	/**
	 * @Title: findOfflineCourseFavourCount 
	 * @Description: (查询线下课程点赞人数) 
	 * @param courseId
	 * @return
	 */
	public Integer findOfflineCourseFavourCount(Long courseId){
		return courseDao.findOfflineCourseFavourCount(courseId);
	}
	
	/**
	 * @Title: findOfflineCourseCount 
	 * @Description: (查询线下课程数量) 
	 * @param searchName
	 * @param courseType
	 * @return
	 */
	@Deprecated
	public Integer findOfflineCourseCount(String searchName,Long courseType){
		Params params = new Params();
		if(!StringUtils.isNullOrEmpty(searchName)){
			params.putData("searchName", super.fuzzy(searchName));
		}
		if(!NumberUtils.isNullOrZero(courseType)){
			params.putData("courseType", courseType);
		}
		return courseDao.findOfflineCourseListCount(params.getMap());
	}
	
	/**
	 * @Title: findMyFavourCourseCount 
	 * @Description: (查询我点赞的课程数量) 
	 * @param memberId
	 * @return
	 */
	public Integer findMyFavourCourseCount(Long memberId){
		return courseDao.findMyFavourCourseCount(memberId);
	}
	
	/**
	 * @Title: findMyJoinCourseCount 
	 * @Description: (查询我参加的课程数量) 
	 * @param memberId
	 * @return
	 */
	public Integer findMyJoinCourseCount(Long memberId){
		return courseDao.findMyJoinCourseCount(memberId);
	}
	
	/**
	 * @Title: findMyFavourCourse 
	 * @Description: (查询我认可的课程) 
	 * @param memberId
	 * @param page
	 * @return
	 */
	public List<OfflineCourseListInfo> findMyFavourCourse(Long memberId,Page page){
		Params params = new Params(page.getNowPage());
		params.putData("memberId", memberId);
		List<OfflineCourseListInfo> list = courseDao.findMyFavourCourseList(params.getMap());
		list.forEach(info -> {
			info.setAvatar(commonService.appenUrl(info.getAvatar()));
			info.setCover(commonService.appenUrl(info.getCover()));
		});
		return list;
	}
	
	/**
	 * @Title: findMyJoinCourse 
	 * @Description: (查询我报名的课程) 
	 * @param memberId
	 * @param page
	 * @return
	 */
	public List<OfflineCourseListInfo> findMyJoinCourse(Long memberId,Page page){
		Params params = new Params(page.getNowPage());
		params.putData("memberId", memberId);
		List<OfflineCourseListInfo> list = courseDao.findMyJoinCourseList(params.getMap());
		list.forEach(info -> {
			info.setAvatar(commonService.appenUrl(info.getAvatar()));
			info.setCover(commonService.appenUrl(info.getCover()));
		});
		return list;
	}
	
	/**
	 * @Title: findCourseJoinCount 
	 * @Description: (查询课程报名人数) 
	 * @param offlineCourseId 线下课程ID
	 * @return
	 */
	public Integer findCourseJoinCount(Long courseId){
		return courseDao.findCourseJoinNumber(courseId);
	}
	
	/**
	 * @Title: saveCourseJoin 
	 * @Description: (保存报名信息) 
	 * @param join
	 */
	public boolean saveCourseJoin(CourseOfflineJoin join){
		return courseDao.saveCourseJoin(join) > 0;
	}
	
	/**
	 * @Title: findOfflineCourseListInfoByCourseId 
	 * @Description: (根据课程id查询线下课程单条信息) 
	 * @param courseId
	 * @return
	 */
	public OfflineCourseListInfo findOfflineCourseListInfoByCourseId(Long courseId){
		OfflineCourseListInfo info = courseDao.findOfflineCourseListInfoByCourseId(courseId);
		info.setAvatar(commonService.appenUrl(info.getAvatar()));
		info.setCover(commonService.appenUrl(info.getCover()));
		return info;
	}
	
	public Catalog findCatalogInfoById(Long id){
		Catalog catalog = courseDao.findCatalogInfoById(id);
		return catalog;
	}
	/**
	 * @Title: findOfflineCourseListInfoByCourseId 
	 * @Description: (根据课程id查询线下课程单条信息) 
	 * @param courseId
	 * @return
	 */
	public LivingCourseInfo findLivingeCourseInfoByCourseId(Long courseId){
		LivingCourseInfo info = courseDao.findLivingeCourseInfoByCourseId(courseId);
		info.setAvatar(commonService.appenUrl(info.getAvatar()));
		info.setCover(commonService.appenUrl(info.getCover()));
		if(StringUtils.isNullOrEmpty(info.getPraiseNumber())){
			info.setPraiseNumber("0");
		}
		return info;
	}
	
	/**
	 * @Title: findOfflineCourseInfo 
	 * @Description: (查询线下课程简介) 
	 * @param courseId
	 * @return
	 */
	public String findOfflineCourseInfo(Long courseId){
		return courseDao.findOfflineCourseInfo(courseId);
	}
	
	public List<Catalog> findCatalogListByCourseId(Long courseId){
		return courseDao.findCatalogListByCourseId(courseId);
	}
	/**
	 * @Title: findCatalogContent
	 * @Description: (查询线下课程简介) 
	 * @param courseId
	 * @return
	 */
	public String findCatalogContent(Long catalogId){
		return courseDao.findCatalogContent(catalogId);
	}
	
	/**
	 * @Title: findOfflineCourseAllInfo 
	 * @Description: (查询线下课程全部信息) 
	 * @param courseId
	 * @return
	 */
	public OfflineCourseInfo findOfflineCourseAllInfo(Long courseId){
		OfflineCourseInfo info = courseDao.findOfflineCourseAllInfo(courseId);
		if(info == null){
			return null;
		}
		info.setAvatar(commonService.appenUrl(info.getAvatar()));
		info.setCover(commonService.appenUrl(info.getCover()));
		return info;
	}
	
	/**
	 * @Title: findCourseImages 
	 * @Description: (查询课程图片) 
	 * @param courseId
	 * @return
	 */
	public List<OfflineCourseImage> findOfflineCourseImages(Long courseId){
		List<OfflineCourseImage> list = courseDao.findOfflineCourseImages(courseId);
		list.forEach(image -> {
			image.setImageUrl(commonService.appenUrl(image.getImageUrl()));
		});
		return list;
	}
	
	/**
	 * @Title: findCourseFavourByOfflineId 
	 * @Description: (查询课程点赞信息) 
	 * @param courseId
	 * @return
	 */
	public List<UserSimpleInfo> findCourseFavourListByOfflineId(Long courseId){
		List<UserSimpleInfo> list = courseDao.findCourseFavourUserInfo(courseId);
		list.forEach(info -> {
			info.setAvatar(commonService.appenUrl(info.getAvatar()));
		});
		return list;
	}
	
	/**
	 * @Title: findCourseJoin 
	 * @Description: (查询课程报名信息) 
	 * @param courseId 课程ID
	 * @param memberId 用户id
	 * @return
	 */
	public CourseOfflineJoin findCourseJoinInfo(Long courseId,Long memberId){
		Params params = new Params();
		params.putData("courseId", courseId);
		params.putData("memberId", memberId);
		return courseDao.findCourseJoin(params.getMap());
	}
	
	/**
	 * @Title: findCourseFavour 
	 * @Description: (查询点赞) 
	 * @param courseId 线下课程id
	 * @param memberId 用户id
	 * @return
	 */
	public OfflineCourseFavour findCourseFavourInfo(Long courseId,Long memberId){
		Params params = new Params();
		params.putData("courseId", courseId);
		params.putData("memberId", memberId);
		return courseDao.findCourseFavour(params.getMap());
	}
	
	/**
	 * @Title: updateOfflineCourseJoinNumber 
	 * @Description: (修改线下课程报名人数) 
	 * @param params
	 * @return
	 */
	public boolean updateOfflineCourseJoinNumber(Params params){
		return courseDao.updateOfflineCourseJoinNumber(params.getMap()) > 0;
	}
	
	/**
	 * @Title: saveCourseFavour 
	 * @Description: (保存课程点赞信息) 
	 * @param favour
	 */
	public boolean saveCourseFavour(OfflineCourseFavour favour){
		return courseDao.saveCourseFavour(favour) > 0;
	}
	
	/**
	 * @Title: updateCourseFavour 
	 * @Description: (修改点赞信息) 
	 * @param favour
	 */
	public boolean updateCourseFavour(OfflineCourseFavour favour){
		return courseDao.updateCourseFavourStatus(favour) > 0;
	}
	
	/**
	 * @Title: findCourseJoinList 
	 * @Description: (查询课程参加信息列表) 
	 * @param page 分页
	 * @param courseId 课程ID
	 * @return
	 */
	public List<UserSimpleInfo> findCourseJoinList(Page page,Long courseId){
		Params params = new Params(page.getNowPage());
		params.putData("courseId", courseId);
		
		LocalDate now = LocalDate.now();
		List<UserSimpleInfo> list = courseDao.findCourseJoinUserList(params.getMap());
		list.forEach(info -> {
			// 工作年限
			if (now.getYear() - info.getJobYear() == 0) {
				info.setWorkYear("半年");
			} else {
				info.setWorkYear((now.getYear() - info.getJobYear()) + "年");
			}
			info.setAvatar(commonService.appenUrl(info.getAvatar()));
		});
		return list;
	}
	
	/**
	 * @Title: findCourseCommentByCourseId 
	 * @Description: (根据线下课程ID查询评论) 
	 * @param courseId 课程ID
	 * @return
	 */
	public List<OfflineCourseCommentInfo> findCourseCommentByCourseId(Long courseId){
		Params params = new Params();
		params.putData("courseId", courseId);
		return courseDao.findCourseCommentByCourseId(params.getMap());
	}
	
	/**
	 * @Title: findCourseCommenById 
	 * @Description: (根据课程评论ID查询评论信息) 
	 * @param commentId
	 * @return
	 */
	public OfflineCourseCommentInfo findCourseCommenById(Long commentId){
		return courseDao.findCourseCommentById(commentId);
	}
	
	/**
	 * @Title: findCommentFavour 
	 * @Description: (查询评论点赞信息) 
	 * @param commentId 课程ID
	 * @param memberId 用户ID
	 * @return
	 */
	public OfflineCourseCommentFavour findCommentFavour(Long commentId,Long memberId){
		Params params = new Params();
		params.putData("commentId", commentId);
		params.putData("memberId", memberId);
		return courseDao.findCourseCommentFavour(params.getMap());
	}
	
	/**
	 * @Title: saveCommentFavour 
	 * @Description: (添加评论点赞信息) 
	 * @param favour
	 */
	public boolean saveCommentFavour(OfflineCourseCommentFavour favour){
		return courseDao.saveCourseCommentFavour(favour) > 0;
	}
	
	/**
	 * @Title: updateCommentFavour 
	 * @Description: (修改评论点赞信息) 
	 * @param favour
	 */
	public boolean updateCommentFavour(OfflineCourseCommentFavour favour){
		return courseDao.updateCourseCommentFavourStatus(favour) > 0;
	}
	
	/**
	 * @Title: findCommentFavourCount 
	 * @Description: (查询评论点赞数量) 
	 * @param commentId 评论ID
	 * @return
	 */
	public Integer findCommentFavourCount(Long commentId){
		return courseDao.findCourseCommentFavourCount(commentId);
	}
	
	/**
	 * @Title: saveOfflineCourseComment 
	 * @Description: (保存线下课程评论) 
	 * @param comment
	 */
	public boolean saveOfflineCourseComment(OfflineCourseComment comment){
		Params params = new Params();
		params.putData("memberId", comment.getMemberId());
		params.putData("courseId", comment.getCourseId());
		OfflineCourse course = courseDao.findOfflineCourseByCourseId(comment.getCourseId());
		//查询当天用户评论课程数量
		if(courseDao.findMemberCommnetCount(params.getMap()) == 0){
			course.setStar(course.getStar() == 0 ? comment.getStar() : NumberUtils.div(NumberUtils.add(course.getStar(), comment.getStar()), 2));
			
		}
		course.setCommentCount(course.getCommentCount() + 1);
		courseDao.updateOfflineCourse(course);
		return courseDao.saveCourseComment(comment) > 0;
	}
	/**
	 * @Title: findOnlineCourseFavourCount 
	 * @Description: (查询课程点赞数量) 
	 * @param courseId
	 * @return
	 */
	public Long findOnlineCourseFavourCount(Long courseId){
		return courseDao.findOnlineCourseFavourCount(courseId);
	}
	
	/**
	 * @Title: findOnlineCourseInfoByCourseId 
	 * @Description: (根据ID查询线上课程信息) 
	 * @param courseId
	 * @return
	 */
	public OnlineCourseListInfo findOnlineCourseInfoByCourseId(Long courseId){
		OnlineCourseListInfo course = courseDao.findOnlineCourseInfoByCourseId(courseId);
		Date nowTime = DateUtils.getNowDate();
		
		course.setAvatar(commonService.appenUrl(course.getAvatar()));
		course.setCover(commonService.appenUrl(course.getCover()));
		LocalDate date = LocalDate.parse(DateUtils.formatDate(course.getPlayStartTime(),DateUtils.DATE_FORMAT_NORMAL));
		
		LocalTime startTime = LocalTime.parse(DateUtils.formatDate(course.getPlayStartTime(),DateUtils.HOUR_MINUTE));
//		LocalTime endTime = LocalTime.parse(DateUtils.formatDate(course.getPlayEndTime(),DateUtils.HOUR_MINUTE));
		//根据开始时间 + 时长
//		LocalTime endTime = LocalTime.parse(DateUtils.formatDate(course.getPlayStartTime(),DateUtils.HOUR_MINUTE));
//		endTime = endTime.plusMinutes(course.getTimeLength());
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(course.getPlayStartTime());
		calendar.add(Calendar.MINUTE, course.getTimeLength());
		
		course.setPlayTime(date.toString()+"");
		if(course.getPlayStartTime().before(nowTime)  && calendar.getTime().after(nowTime)){
			course.setType(1);
			
			course.setPlayAddress(commonService.appenUrl(course.getPlayAddress()));
			course.setPlayAddressM3u8(commonService.appenUrl(course.getPlayAddressM3u8()));
		}else if(course.getPlayStartTime().after(nowTime)){
			course.setType(2);
			String startHour = startTime.getHour() + "";
			if(startHour.length() == 1){
				startHour = "0" + startHour;
			}
			String startMinute = startTime.getMinute() + "";
			if(startMinute .length() == 1){
				startMinute = "0" + startMinute;
			}
			
//			String endHour = endTime.getHour() + "";
//			if(endHour.length() == 1){
//				endHour = "0" + endHour;
//			}
//			String endMinute = endTime.getMinute() + "";
//			if(endMinute.length() == 1){
//				endMinute = "0" + endMinute;
//			}

			course.setPlayTime(date.getMonthValue() + "月" + date.getDayOfMonth()+"日 "+startHour +":"+startMinute+"开始");
//			course.setPlayTime(date.getMonthValue() + "月" + date.getDayOfMonth()+"日 "+startHour +":"+startMinute+"-"+endHour+":"+endMinute);
			course.setPlayAddress(commonService.appenUrl(course.getPlayAddress()));
			course.setPlayAddressM3u8(commonService.appenUrl(course.getPlayAddressM3u8()));
		}else{
			course.setPlayTime(date.getMonthValue() + "月" + date.getDayOfMonth()+"日");
			course.setType(3);
			course.setPlayAddress(commonService.appenUrl(course.getPlayAddress()));
			course.setPlayAddressM3u8(commonService.appenUrl(course.getPlayAddressM3u8()));
		}
		return course;
	}
	
	public List<OnlineCourseListInfo> findOnlineCourseListV2(Integer page){
		Params params = new Params(page);
		List<OnlineCourseListInfo> courseList = courseDao.findOnlineCourseListV2(params.getMap());
		if(courseList != null){
			courseList.forEach(course -> {
				course.setAvatar(commonService.appenUrl(course.getAvatar()));
				course.setCover(commonService.appenUrl(course.getCover()));
				LocalDate date = LocalDate.parse(DateUtils.formatDate(course.getPlayStartTime(),DateUtils.DATE_FORMAT_NORMAL));
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(course.getPlayStartTime());
				calendar.add(Calendar.MINUTE, course.getTimeLength());
				
				course.setPlayTime(date.toString()+""); 
				course.setPlayTime(date.getMonthValue() + "月" + date.getDayOfMonth()+"日");
				course.setType(3);
				course.setPlayAddress(commonService.appenUrl(course.getPlayAddress()));
				course.setPlayAddressM3u8(commonService.appenUrl(course.getPlayAddressM3u8()));
			});
		}
		return courseList;
	}
	
	public Long findOnlineCourseCountV2(){
		return courseDao.findOnlineCourseCountV2();
	}
	
	/**
	 * @Title: findOnlineCourseList 
	 * @Description: (查询线上课程列表) 
	 * @param time 时间
	 * @param type 类别 1:正在直播  2:预告 3:点播
	 * @return
	 */
	@Deprecated
	public List<OnlineCourseListInfo> findOnlineCourseList(String time,int type,int from){
		if(StringUtils.isNullOrEmpty(time)){
			time = DateUtils.formatDate(DateUtils.getNowDate());
		}
		Date now = DateUtils.getNowDate();
		Params params = new Params();
		params.putData("time", time);
		params.putData("from", from);
		List<OnlineCourseListInfo> courseList = null;
		if(type == 1){
			params.putData("type", 1);
			//查询正在直播列表
			courseList = courseDao.findOnlineCourseList(params.getMap());
			
			if(courseList.size() < Params.PAGE_COUNT){
				//查询预播
				params.removeData("time");
				params.putData("time", DateUtils.formatDate(now));
				params.removeData("type");
				params.putData("type", 2);
				params.putData("limit", Params.PAGE_COUNT - courseList.size());
				courseList.addAll(courseDao.findOnlineCourseList(params.getMap()));
			}
			if(courseList.size() < Params.PAGE_COUNT){
				//查询点播的
				params.removeData("time");
				params.putData("time", DateUtils.formatDate(now));
				params.removeData("type");
				params.putData("type", 3);
				params.putData("limit", Params.PAGE_COUNT - courseList.size());
				courseList.addAll(courseDao.findOnlineCourseList(params.getMap()));
			}
		}else if(type == 2){
			params.putData("type", 2);
			//查询正在直播列表
			courseList = courseDao.findOnlineCourseList(params.getMap());
			
			if(courseList.size() < Params.PAGE_COUNT){
				//查询点播的
				params.removeData("time");
				params.putData("time", DateUtils.formatDate(now));
				params.removeData("type");
				params.putData("type", 3);
				params.putData("limit", Params.PAGE_COUNT - courseList.size());
				courseList.addAll(courseDao.findOnlineCourseList(params.getMap()));
			}
		}else{
			params.putData("type", 3);
			params.putData("limit", Params.PAGE_COUNT);
			//查询正在直播列表
			courseList = courseDao.findOnlineCourseList(params.getMap());
		}
		
		if(courseList != null){
			courseList.forEach(course -> {
				course.setAvatar(commonService.appenUrl(course.getAvatar()));
				course.setCover(commonService.appenUrl(course.getCover()));
				LocalDate date = LocalDate.parse(DateUtils.formatDate(course.getPlayStartTime(),DateUtils.DATE_FORMAT_NORMAL));
				
//				LocalTime startTime = LocalTime.parse(DateUtils.formatDate(course.getPlayStartTime(),DateUtils.HOUR_MINUTE));
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(course.getPlayStartTime());
				calendar.add(Calendar.MINUTE, course.getTimeLength());
				
				course.setPlayTime(date.toString()+""); 
//				if(course.getPlayStartTime().before(nowTime)  && calendar.getTime().after(nowTime)){
//					course.setType(1);
//					
//					course.setPlayAddress(commonService.appenUrl(course.getPlayAddress()));
//					course.setPlayAddressM3u8(commonService.appenUrl(course.getPlayAddressM3u8()));
//				}else if(course.getPlayStartTime().after(nowTime)){
//					course.setType(2);
//					String startHour = startTime.getHour() + "";
//					if(startHour.length() == 1){
//						startHour = "0" + startHour;
//					}
//					String startMinute = startTime.getMinute() + "";
//					if(startMinute .length() == 1){
//						startMinute = "0" + startMinute;
//					}
//					
//					course.setPlayTime(date.getMonthValue() + "月" + date.getDayOfMonth()+"日 "+startHour +":"+startMinute+"准时开始");
////					course.setPlayTime(date.getMonthValue() + "月" + date.getDayOfMonth()+"日 "+startHour +":"+startMinute+"-"+endHour+":"+endMinute);
//					course.setPlayAddress(commonService.appenUrl(course.getPlayAddress()));
//					course.setPlayAddressM3u8(commonService.appenUrl(course.getPlayAddressM3u8()));
//				}else{
					course.setPlayTime(date.getMonthValue() + "月" + date.getDayOfMonth()+"日");
					course.setType(3);
					course.setPlayAddress(commonService.appenUrl(course.getPlayAddress()));
					course.setPlayAddressM3u8(commonService.appenUrl(course.getPlayAddressM3u8()));
//				}
			});
		}
		return courseList;
	}
	
	
	/**
	 * @Title: findCourseOnlineJoinInfo 
	 * @Description: (查询线上课程参加信息) 
	 * @param params
	 * @return
	 */
	public CourseOnlineJoin findCourseOnlineJoinInfo(Params params){
		return courseDao.findOnlineJoinInfo(params.getMap());
	}
	
	/**
	 * @Title: saveCourseOnlineInfo 
	 * @Description: (保存线上课程参加信息) 
	 * @param join
	 * @return
	 */
	public boolean saveCourseOnlineInfo(CourseOnlineJoin join){
		OnlineCourse online = courseDao.findOnlineCourseByCourseId(join.getCourseId());
		if(online == null){
			return false;
		}
		online.setJoinNumber(online.getJoinNumber() + 1);
		return courseDao.saveOnlineJoin(join) > 0 && courseDao.updateOnlineCourse(online) > 0;
	}
	
	/**
	 * @Title: updateCourseOnlineInfoStatus 
	 * @Description: (修改线上课程参加信息状态) 
	 * @param join
	 * @return
	 */
	public boolean updateCourseOnlineInfoStatus(CourseOnlineJoin join){
		OnlineCourse online = courseDao.findOnlineCourseByCourseId(join.getCourseId());
		if(online == null){
			return false;
		}
		if(join.getStatus() == 1){
			online.setJoinNumber(online.getJoinNumber() + 1);
		}else{
			online.setJoinNumber(online.getJoinNumber() == 0 ? 0 : online.getJoinNumber() - 1);
		}
		return courseDao.updateOnlineJoinStatus(join) > 0 && courseDao.updateOnlineCourse(online) > 0;
	}
	
	/**
	 * @Title: saveOfflineCourse 
	 * @Description: (保存线下课程信息) 
	 * @param offline
	 * @return
	 */
	public boolean saveOfflineCourse(OfflineCourse offline){
		return courseDao.saveOfflineCourse(offline) > 0;
	}
	
	/**
	 * @Title: saveCourse 
	 * @Description: (保存课程) 
	 * @param course
	 * @return
	 */
	public boolean saveCourse(Course course){
		return courseDao.saveCourse(course) > 0;
	}
	
	/**
	 * @Title: saveOnlineCourse 
	 * @Description: (保存线上课程信息) 
	 * @param online
	 * @return
	 */
	public boolean saveOnlineCourse(OnlineCourse online){
		return courseDao.saveOnlineCourse(online) > 0;
	}
	
	/**
	 * @Title: saveOnlineCourseComment 
	 * @Description: (保存线上课程评论) 
	 * @param comment
	 * @return
	 */
	public boolean saveOnlineCourseComment(OnlineCourseComment comment){
		return courseDao.saveOnlineCourseComment(comment) > 0;
	}
	/**
	 * @Title: saveCourseClickDetail
	 * @Description: (保存线上课程评论) 
	 * @param comment
	 * @return
	 */
	public boolean saveCourseClickDetail(CourseClickDetail detail){
		return courseDao.saveCourseClickDetail(detail) > 0;
	}
	
	/**
	 * @Title: findOnlineCommentList 
	 * @Description: (查询线上课程评论列表) 
	 * @param params
	 * @return
	 */
	public List<OnlineCourseCommentInfo> findOnlineCommentList(Params params){
		List<OnlineCourseCommentInfo> list = courseDao.findOnlineCourseCommentList(params.getMap());
		list.forEach(info->{
			info.setAvatar(commonService.appenUrl(info.getAvatar()));
		});
		return list;
	}
	
	/**
	 * @Title: findOnlineCommentCount 
	 * @Description: (查询线上课程评论数量) 
	 * @param params
	 * @return
	 */
	public int findOnlineCommentCount(Params params){
		return courseDao.findOnlineCourseCommentCount(params.getMap());
	}
	
	/**
	 * @Title: findCourseById 
	 * @Description: (根据ID查询课程基本信息) 
	 * @param id
	 * @return
	 */
	public Course findCourseById(Long id){
		Course course = courseDao.findCourseById(id);
		if(course == null){
			return null;
		}
		course.setAvatar(commonService.appenUrl(course.getAvatar()));
		course.setCover(commonService.appenUrl(course.getCover()));
		return course;
	}
	
	/**
	 * @Title: findCourseIsSell
	 * @Description: (根据ID查询课程是否售卖) 
	 * @param id
	 * @return
	 */
	public Course findCourseIsSell(Long id){
		Course course = courseDao.findCourseIsSell(id);
		if(course == null){
			return null;
		}
		return course;
	}
	/**
	 * @Title: findOfflineCourseById 
	 * @Description: (根据ID查询线下课程信息) 
	 * @param courseId
	 * @return
	 */
	public OfflineCourse findOfflineCourseByCourseId(Long courseId){
		return courseDao.findOfflineCourseByCourseId(courseId);
	}
	
	/**
	 * @Title: findOnlineCourseByCourseId 
	 * @Description: (根据课程ID查询线上课程信息) 
	 * @param courseId
	 * @return
	 */
	public OnlineCourse findOnlineCourseByCourseId(Long courseId){
		return courseDao.findOnlineCourseByCourseId(courseId);
	}
	
	/**
	 * @Title: findOnlineWebCourseByCourseId 
	 * @Description: (查询Web线上课程信息) 
	 * @param id
	 * @return
	 */
	public OnlineCourseResultInfo findOnlineWebCourseByCourseId(Long id){
		return courseDao.findOnlineWebCourseByCourseId(id);
	}
	
	/**
	 * @Title: findOnlineWebCourseByCourseSell 
	 * @Description: (查询Web线上课程信息) 
	 * @param id
	 * @return
	 */
	public OnlineCourseResultInfo findOnlineWebCourseByCourseSell(Long id){
		return courseDao.findOnlineWebCourseByCourseSell(id);
	}
	
	public Long findBuyCourseCount(String unionId,Long courseId){
		Params params = new Params();
		params.putData("unionId", unionId);
		params.putData("courseId", courseId);
		
		return courseDao.findBuyCourseCount(params.getMap());
	}
	
	/**
	 * @Title: saveOfflineCourse 
	 * @Description: (保存线下课程信息) 
	 * @param course
	 * @param offline
	 * @return
	 */
	public boolean saveOfflineCourse(Course course,OfflineCourse offline){
		if(NumberUtils.isNullOrZero(course.getId())){
			if(courseDao.saveCourse(course) > 0){
				offline.setCourseId(course.getId());
				return courseDao.saveOfflineCourse(offline) > 0;
			}
			return false;
		}else{
			offline.setFavourCount(null);
			return courseDao.updateCourse(course) > 0 && courseDao.updateOfflineCourse(offline) > 0;
		}
	}
	
	/**
	 * @Title: saveOnlineCourse 
	 * @Description: (保存线下课程) 
	 * @param course
	 * @param online
	 * @return
	 */
	public boolean saveOnlineCourse(Course course,OnlineCourse online){
		if(NumberUtils.isNullOrZero(course.getId())){
			if(courseDao.saveCourse(course) > 0){
				online.setCourseId(course.getId());
				return courseDao.saveOnlineCourse(online) > 0;
			}
			return false;
		}else{
			online.setJoinNumber(null);
			return courseDao.updateCourse(course) > 0 && courseDao.updateOnlineCourse(online) > 0;
		}
	}
	
	/**
	 * @Title: updateOfflineCourse 
	 * @Description: (修改线下课程信息) 
	 * @param course
	 * @return
	 */
	public boolean updateOfflineCourse(OfflineCourse course){
		return courseDao.updateOfflineCourse(course) > 0;
	}
	
	/**
	 * @Title: deleteCourse 
	 * @Description: (删除课程) 
	 * @param id
	 * @param type
	 * @return
	 */
	public boolean deleteCourse(Long id,Integer type){
		if(courseDao.updateCourseStatus(id) > 0){
			if(type == 0){
				return courseDao.updateOfflineCourseStatus(id) > 0;
			}else{
				return courseDao.updateOnlineCourseStatus(id) > 0;
			}
		}
		return false;
	}
	
	/**
	 * @Title: findCourseInfoList
	 * @Description: (查询课程信息列表) 
	 * @param params
	 * @return
	 */
	public List<CourseListInfo> findCourseInfoList(Params params){
		List<CourseListInfo> list = courseDao.findCourseInfoList(params.getMap());
		list.forEach(info -> {
			info.setAvatar(commonService.appenUrl(info.getAvatar()));
			info.setCover(commonService.appenUrl(info.getCover()));
			
		});
		return list;
	}
	
	/**
	 * @Title: fincCourseInfoListCount 
	 * @Description: (查询课程信息总数) 
	 * @param params
	 * @return
	 */
	public Integer fincCourseInfoListCount(Params params){
		return courseDao.findCourseInfoListCount(params.getMap());
	}
	
	/**
	 * @Title: updateOnlineCourseInfo 
	 * @Description: (修改线上课程) 
	 * @param course
	 * @return
	 */
	public boolean updateOnlineCourseInfo(OnlineCourse course){
		return courseDao.updateOnlineCourse(course) > 0;
	}
	
	/**
	 * @Title: saveOfflineCourseImage 
	 * @Description: (保存课程图片) 
	 * @param image
	 * @return
	 */
	public boolean saveOfflineCourseImage(OfflineCourseImage image){
		return courseDao.saveOfflineCourseImage(image) > 0;
	}
	
	/**
	 * @Title: findOfflineCourseImage 
	 * @Description: (查询课程图片) 
	 * @param params
	 * @return
	 */
	public List<CourseImageListInfo> findOfflineCourseImage(Params params){
		List<CourseImageListInfo> list = courseDao.findOfflineCourseImage(params.getMap());
		list.forEach(info -> {
			info.setUrl(commonService.appenUrl(info.getUrl()));
		});
		return list;
	}
	
	/**
	 * @Title: findOfflineCourseImageCount 
	 * @Description: (查询课程图片数量) 
	 * @param params
	 * @return
	 */
	public int findOfflineCourseImageCount(Params params){
		return courseDao.findOfflineCourseImageCount(params.getMap());
	}
	
	/**
	 * @Title: updateCourseImage 
	 * @Description: (修改课程图片) 
	 * @param image
	 * @return
	 */
	public boolean updateCourseImage(OfflineCourseImage image){
		return courseDao.updateCourseImage(image) > 0;
	}
	
	/**
	 * @Title: findOfflineCourseImageById 
	 * @Description: (根据Id查询课程图片) 
	 * @param id
	 * @return
	 */
	public OfflineCourseImage findOfflineCourseImageById(Long id){
		OfflineCourseImage image = courseDao.findOfflineCourseImageById(id);
		image.setImageUrl(commonService.appenUrl(image.getImageUrl()));
		return image;
	}
	
	/**
	 * @Title: findOnlineCourseCount 
	 * @Description: (查询线上课程数量) 
	 * @return
	 */
	public Long findOnlineCourseCount(){
		return courseDao.findOnlineCourseCount() + courseDao.findPlayCourseCount();
	}
	
	/**
	 * @Title: findOnlineCourseContent 
	 * @Description: (查询线上课程简介) 
	 * @param id
	 * @return
	 */
	public String findOnlineCourseContent(Long id){
		OnlineCourseListInfo online = courseDao.findOnlineCourseInfoByCourseId(id);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(online.getPlayStartTime());
		calendar.add(Calendar.MINUTE, online.getTimeLength());
		
//		Date nowTime = DateUtils.getNowDate();
		
		String content =courseDao.findOnlineCoursePPT(id);
//		if(online.getPlayStartTime().before(nowTime)  && calendar.getTime().after(nowTime)){
//			//直播
//			content = courseDao.findOnlineCoursePPT(id);
//			if(StringUtils.isNullOrEmpty(content)){
//				content = courseDao.findOnlineCourseContent(id);
//			}
//			
//		}else if(online.getPlayStartTime().after(nowTime)){
//			//预告
//			content = courseDao.findOnlineCourseContent(id);
//		}else{
//			//往期
//			content = courseDao.findOnlineCourseBack(id);
//			if(StringUtils.isNullOrEmpty(content)){
//				content = courseDao.findOnlineCoursePPT(id);
//			}
//			if(StringUtils.isNullOrEmpty(content)){
//				content = courseDao.findOnlineCourseContent(id);
//			}
//		}
		return content;
	}
	/**
	 * @Title: findSelfStudyContent 
	 * @Description: (查询线上课程简介) 
	 * @param id
	 * @return
	 */
	public SelfStudy findSelfStudyContent(Long id){
		SelfStudy content =courseDao.findSelfStudyContent(id);
		return content;
	}
	
	/**
	 * @Title: findOnlineJoinMember 
	 * @Description: (查询线上课程在线用户) 
	 * @param params
	 * @return
	 */
	public List<UserSimpleInfo> findOnlineJoinMember(Params params){
		LocalDate now = LocalDate.now();
		List<UserSimpleInfo> infoList = courseDao.findOnlineJoinMember(params.getMap());
		infoList.forEach(info -> {
			// 工作年限
			if (now.getYear() - info.getJobYear() == 0) {
				info.setWorkYear("半年");
			} else {
				info.setWorkYear((now.getYear() - info.getJobYear()) + "年");
			}
			info.setAvatar(commonService.appenUrl(info.getAvatar()));
			info.setAvatar(commonService.appenUrl(info.getAvatar()));
		});
		return infoList;
	}
	
	/**
	 * @Title: findOnlineJoinMemberLimit10 
	 * @Description: (查询线上课程在线用户列表) 
	 * @param courseId
	 * @return
	 */
	public List<UserSimpleInfo> findOnlineJoinMemberLimit10(Long courseId){
		LocalDate now = LocalDate.now();
		List<UserSimpleInfo> infoList = courseDao.findOnlineJoinMemberLimit10(courseId);
		infoList.forEach(info -> {
			// 工作年限
			if (now.getYear() - info.getJobYear() == 0) {
				info.setWorkYear("半年");
			} else {
				info.setWorkYear((now.getYear() - info.getJobYear()) + "年");
			}
			info.setAvatar(commonService.appenUrl(info.getAvatar()));
			info.setAvatar(commonService.appenUrl(info.getAvatar()));
		});
		return infoList;
	}
	
	/**
	 * @Title: findOnlineJoinNumber 
	 * @Description: (查询线上课程在线用户数) 
	 * @param id
	 * @return
	 */
	public Long findOnlineJoinNumber(Long id){
		return courseDao.findOnlineJoinNumber(id);
	}
	
	/**
	 * @Title: updateAppCourseClickNumber 
	 * @Description: (修改客户端点击数量) 
	 * @param id
	 * @return
	 */
	public boolean updateAppCourseClickNumber(Long id){
		return courseDao.updateAppCourseClickNumber(id) > 0;
	}
	public boolean updateZuoWei(Long memberId,Long courseId,Long pai,Long hao){
		Params params = new Params();
		params.putData("memberId", memberId);
		params.putData("pai", pai);
		params.putData("hao", hao);
		params.putData("courseId", courseId);
		return courseDao.updateZuoWei(params.getMap()) > 0;
	}
	
	/**
	 * @Title: findOfflineCourseList 
	 * @Description: (查询线下课程列表) 
	 * @param page 分页
	 * @param type 是否按最新课程排序
	 * @param searchName 名称查询
	 * @param courseType 线下课程类别
	 * @return
	 */
	@Deprecated
	public List<OfflineCourseListInfo> findOfflineCourseList(Page page,boolean type,String searchName,Long courseType,Integer number){
		Params params = new Params(page.getNowPage());
		if(!NumberUtils.isNullOrZero(number)){
			params.putData("number", number);
		}
		if(!StringUtils.isNullOrEmpty(searchName)){
			params.putData("searchName", super.fuzzy(searchName));
		}
		if(!NumberUtils.isNullOrZero(courseType)){
			params.putData("courseType", courseType);
		}
		if(type){
			params.putData("type", "1");
		}else{
			params.putData("favour", "1");
		}
		List<OfflineCourseListInfo> list = courseDao.findOfflineCourseList(params.getMap());
		list.forEach(info -> {
			info.setAvatar(commonService.appenUrl(info.getAvatar()));
			info.setCover(commonService.appenUrl(info.getCover()));
		});
		return list;
	}
	
	/**
	 * @Title: findOfflineCourseV2ForSigned 
	 * @Description: (查询线下课程列表供签到使用) 
	 * @param page 分页
	 * @param type 是否按最新课程排序
	 * @param searchName 名称查询
	 * @param courseType 线下课程类别
	 * @return
	 */
	public List<OfflineCourseListInfo> findOfflineCourseV2ForSigned(){
		List<OfflineCourseListInfo> list = new ArrayList<OfflineCourseListInfo>();
		Params params = new Params();
		params.putData("max", "<=3");
		params.putData("min", ">=0");
		list = courseDao.findOfflineCourseListForSign(params.getMap());
		return list;
	}
	
	/**
	 * @Title: findOfflineJoinForSigned 
	 * @Description: (查询线下课程列表供签到使用) 
	 * @param page 分页
	 * @param type 是否按最新课程排序
	 * @param searchName 名称查询
	 * @param courseType 线下课程类别
	 * @return
	 */
	public List<OfflineJoinMember> findOfflineJoinForSigned(String mobile,Long courseId){
		List<OfflineJoinMember> list = new ArrayList<OfflineJoinMember>();
		Params params = new Params();
		params.putData("mobile", mobile);
		params.putData("courseId", courseId);
		list = courseDao.findOfflineJoinForSigned(params.getMap());
		return list;
	}
	/**
	 * @Title: findOfflineJoinForSignedDaKe
	 * @Description: (查询线下课程列表供签到使用) 
	 * @param page 分页
	 * @param type 是否按最新课程排序
	 * @param searchName 名称查询
	 * @param courseType 线下课程类别
	 * @return
	 */
	public List<OfflineJoinMember> findOfflineJoinForSignedDaKe(String mobile,Long courseId){
		List<OfflineJoinMember> list = new ArrayList<OfflineJoinMember>();
		Params params = new Params();
		params.putData("mobile", mobile);
		params.putData("courseId", courseId);
		list = courseDao.findOfflineJoinForSignedDaKe(params.getMap());
		return list;
	}
	
	/**
	 * @Title: findOfflineJoinForSignedAll 
	 * @Description: (查询线下课程列表供签到使用) 
	 * @param page 分页
	 * @param type 是否按最新课程排序
	 * @param searchName 名称查询
	 * @param courseType 线下课程类别
	 * @return
	 */
	public List<OfflineJoinMember> findOfflineJoinForSignedAll(String mobile,Long courseId){
		List<OfflineJoinMember> list = new ArrayList<OfflineJoinMember>();
		Params params = new Params();
		params.putData("mobile", mobile);
		params.putData("courseId", courseId);
		list = courseDao.findOfflineJoinForSignedAll(params.getMap());
		return list;
	}
	
	/**
	 * @Title: saveOnlineCourseFavour 
	 * @Description: (保存线下课程签到信息) 
	 * @param favour
	 */
	public boolean saveOfflineSignedInfo(OfflineSigned signed){
		return courseDao.saveOfflineSignedInfo(signed) > 0;
	}
	
	/**
	 * @Title: findOfflineJoinMobileForSigned 
	 * @Description: (查询完整手机号供签到使用) 
	 * @param page 分页
	 * @param type 是否按最新课程排序
	 * @param searchName 名称查询
	 * @param courseType 线下课程类别
	 * @return
	 */
	public String findOfflineJoinMobileForSigned(String mobile,Long courseId){
		Params params = new Params();
		params.putData("mobile", mobile);
		params.putData("courseId", courseId);
		return courseDao.findOfflineJoinMobileForSigned(params.getMap());
	}
	public String findOfflineJoinMobileForSignedDaKe(String mobile,Long courseId){
		Params params = new Params();
		params.putData("mobile", mobile);
		params.putData("courseId", courseId);
		return courseDao.findOfflineJoinMobileForSignedDaKe(params.getMap());
	}
	
	/**
	 * @Title: findOfflineJoinNameForSigned 
	 * @Description: (查询姓名供签到使用) 
	 * @param page 分页
	 * @param type 是否按最新课程排序
	 * @param searchName 名称查询
	 * @param courseType 线下课程类别
	 * @return
	 */
	public String findOfflineJoinNameForSigned(String mobile,Long courseId){
		Params params = new Params();
		params.putData("mobile", mobile);
		params.putData("courseId", courseId);
		return courseDao.findOfflineJoinNameForSigned(params.getMap());
	}
	public String findOfflineJoinNameForSignedDaKe(String mobile,Long courseId){
		Params params = new Params();
		params.putData("mobile", mobile);
		params.putData("courseId", courseId);
		return courseDao.findOfflineJoinNameForSignedDaKe(params.getMap());
	}
	/**
	 * @Title: findOfflineJoinCompanyForSigned 
	 * @Description: (查询姓名供签到使用) 
	 * @param page 分页
	 * @param type 是否按最新课程排序
	 * @param searchName 名称查询
	 * @param courseType 线下课程类别
	 * @return
	 */
	public String findOfflineJoinCompanyForSigned(String mobile,Long courseId){
		Params params = new Params();
		params.putData("mobile", mobile);
		params.putData("courseId", courseId);
		return courseDao.findOfflineJoinCompanyForSigned(params.getMap());
	}
	public String findOfflineJoinCompanyForSignedDaKe(String mobile,Long courseId){
		Params params = new Params();
		params.putData("mobile", mobile);
		params.putData("courseId", courseId);
		return courseDao.findOfflineJoinCompanyForSignedDaKe(params.getMap());
	}
	/**
	 * @Title: findOfflineJoinCompanyForSigned 
	 * @Description: (查询姓名供签到使用) 
	 * @param page 分页
	 * @param type 是否按最新课程排序
	 * @param searchName 名称查询
	 * @param courseType 线下课程类别
	 * @return
	 */
	public String findOfflineJoinJobForSigned(String mobile,Long courseId){
		Params params = new Params();
		params.putData("mobile", mobile);
		params.putData("courseId", courseId);
		return courseDao.findOfflineJoinJobForSigned(params.getMap());
	}
	public String findOfflineJoinJobForSignedDaKe(String mobile,Long courseId){
		Params params = new Params();
		params.putData("mobile", mobile);
		params.putData("courseId", courseId);
		return courseDao.findOfflineJoinJobForSignedDaKe(params.getMap());
	}
	/**
	 * @Title: findOfflineJoinNameForSignedAll 
	 * @Description: (查询姓名供签到使用) 
	 * @param page 分页
	 * @param type 是否按最新课程排序
	 * @param searchName 名称查询
	 * @param courseType 线下课程类别
	 * @return
	 */
	public String findOfflineJoinNameForSignedAll(String mobile,Long courseId){
		Params params = new Params();
		params.putData("mobile", mobile);
		params.putData("courseId", courseId);
		return courseDao.findOfflineJoinNameForSignedAll(params.getMap());
	}
	
	/**
	 * @Title: findSignedListByMobile 
	 * @Description: (根据手机号码后四位查询线下课程签到列表供签到使用) 
	 * @param page 分页
	 * @param type 是否按最新课程排序
	 * @param searchName 名称查询
	 * @param courseType 线下课程类别
	 * @return
	 */
	public String findSignedListByMobile(String mobile,Long courseId){
		Params params = new Params();
		params.putData("mobile", mobile);
		params.putData("courseId", courseId);
		return courseDao.OfflineSigned(params.getMap());
	}
	public String findSignedListByMobileDaKe(String mobile){
		Params params = new Params();
		params.putData("mobile", mobile);
		return courseDao.OfflineSignedDaKe(params.getMap());
	}
	/**
	 * @Title: findSignedListByMobile 
	 * @Description: (根据手机号码查询线下课程签到列表供签到使用) 
	 * @param page 分页
	 * @param type 是否按最新课程排序
	 * @param searchName 名称查询
	 * @param courseType 线下课程类别
	 * @return
	 */
	public String findSignedListByMobileAll(String mobile,Long courseId){
		Params params = new Params();
		params.putData("mobile", mobile);
		params.putData("courseId", courseId);
		return courseDao.OfflineSignedAll(params.getMap());
	}
	public String findSignedListByMobileAllDaKe(String mobile,Long courseId){
		Params params = new Params();
		params.putData("mobile", mobile);
		params.putData("courseId", courseId);
		return courseDao.OfflineSignedAllDaKe(params.getMap());
	}
	
	/**
	 * @Title: findOfflineSignedJoinListShang 
	 * @Description: (根据手机号码查询线下课程签到列表供签到使用) 
	 * @param page 分页
	 * @param type 是否按最新课程排序
	 * @param searchName 名称查询
	 * @param courseType 线下课程类别
	 * @return
	 */
	public List<OfflineSigned> findOfflineSignedJoinListShang(Long courseId,String sign){
		Params params = new Params();
		params.putData("courseId", courseId);
		params.putData("sign", sign);
		return courseDao.findOfflineSignedJoinListShang(params.getMap());
	}
	public List<OfflineSigned> findOfflineSignedJoinListShangDaKe(Long courseId,String sign){
		Params params = new Params();
		params.putData("courseId", courseId);
		params.putData("sign", sign);
		return courseDao.findOfflineSignedJoinListShangDaKe(params.getMap());
	}
	public List<OfflineJoinMember> findOfflineJoinMemberByMobile(String mobile){
		Params params = new Params();
		params.putData("mobile", mobile);
		return courseDao.findOfflineJoinMemberByMobile(params.getMap());
	}
	/**
	 * @Title: findOfflineSignedJoinListShang 
	 * @Description: (根据手机号码查询线下课程签到列表供签到使用) 
	 * @param page 分页
	 * @param type 是否按最新课程排序
	 * @param searchName 名称查询
	 * @param courseType 线下课程类别
	 * @return
	 */
	public List<OfflineSigned> findOfflineSignedJoinListXia(Long courseId,String sign){
		Params params = new Params();
		params.putData("courseId", courseId);
		params.putData("sign", sign);
		return courseDao.findOfflineSignedJoinListXia(params.getMap());
	}
	public List<OfflineSigned> findOfflineSignedJoinListXiaDaKe(Long courseId,String sign){
		Params params = new Params();
		params.putData("courseId", courseId);
		params.putData("sign", sign);
		return courseDao.findOfflineSignedJoinListXiaDaKe(params.getMap());
	}
	
	/**
	 * @Title: findSignedListByMobile 
	 * @Description: (根据手机号码查询线下课程签到列表供签到使用) 
	 * @param page 分页
	 * @param type 是否按最新课程排序
	 * @param searchName 名称查询
	 * @param courseType 线下课程类别
	 * @return
	 */
	public List findOfflineSignedMobileListShang(Long courseId,String sign){
		Params params = new Params();
		params.putData("courseId", courseId);
		params.putData("sign", sign);
		return courseDao.findOfflineSignedMobileListShang(params.getMap());
	}
	public List findOfflineSignedMobileListShangDaKe(Long courseId,String sign){
		Params params = new Params();
		params.putData("courseId", courseId);
		params.putData("sign", sign);
		return courseDao.findOfflineSignedMobileListShangDaKe(params.getMap());
	}
	/**
	 * @Title: findSignedListByMobile 
	 * @Description: (根据手机号码查询线下课程签到列表供签到使用) 
	 * @param page 分页
	 * @param type 是否按最新课程排序
	 * @param searchName 名称查询
	 * @param courseType 线下课程类别
	 * @return
	 */
	public List findOfflineSignedMobileListXia(Long courseId,String sign){
		Params params = new Params();
		params.putData("courseId", courseId);
		params.putData("sign", sign);
		return courseDao.findOfflineSignedMobileListXia(params.getMap());
	}
	//根据课程Id查询审核通过的线下课程学员信息列表，用于发送结业证书
	public List queryMemberListByCourseIdForEmail(Long courseId){
		Params params = new Params();
		params.putData("courseId", courseId);
		return courseDao.queryMemberListByCourseIdForEmail(params.getMap());
	}
	
	public List findOfflineSignedMobileListXiaDaKe(Long courseId,String sign){
		Params params = new Params();
		params.putData("courseId", courseId);
		params.putData("sign", sign);
		return courseDao.findOfflineSignedMobileListXiaDaKe(params.getMap());
	}
	
	/**
	 * @Title: findSignedListByMobile 
	 * @Description: (根据手机号码查询线下课程签到列表供签到使用) 
	 * @param page 分页
	 * @param type 是否按最新课程排序
	 * @param searchName 名称查询
	 * @param courseType 线下课程类别
	 * @return
	 * @throws ParseException 
	 */
	public List findOfflineNotSignedMobileList(Long courseId,String sign) throws ParseException{
		Date date = new Date();
		DateFormat fmt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		List<OfflineJoinMember> list = new ArrayList();
		String now=fmt1.format(date);
		Params params = new Params();
		params.putData("courseId", courseId);
		params.putData("sign",sign);
		if(sdf.parse(sign).after(sdf.parse(now))){
			list = courseDao.findOfflineNotSignedMobileListShang(params.getMap());
		}else{
			list = courseDao.findOfflineNotSignedMobileListXia(params.getMap());
		}
		return list;
	}
	public List findOfflineNotSignedMobileListDaKe(Long courseId,String sign) throws ParseException{
		Date date = new Date();
		DateFormat fmt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		List<OfflineJoinMember> list = new ArrayList();
		String now=fmt1.format(date);
		Params params = new Params();
		params.putData("courseId", courseId);
		params.putData("sign",sign);
		if(sdf.parse(sign).after(sdf.parse(now))){
			list = courseDao.findOfflineNotSignedMobileListShangDaKe(params.getMap());
		}else{
			list = courseDao.findOfflineNotSignedMobileListXiaDaKe(params.getMap());
		}
		return list;
	}
	
	/**
	 * @Title: findOfflineNotSignedList 
	 * @Description: (根据手机号码查询线下课程签到列表供签到使用) 
	 * @param page 分页
	 * @param type 是否按最新课程排序
	 * @param searchName 名称查询
	 * @param courseType 线下课程类别
	 * @return
	 * @throws ParseException 
	 */
	public List findOfflineNotSignedList(Long courseId){
		Params params = new Params();
		params.putData("courseId", courseId);
		return courseDao.findOfflineNotSignedList(params.getMap());
	}
	public List findList(){
		Params params = new Params();
		params.putData("courseId", 1);
		return courseDao.findList(params.getMap());
	}
	public List findOfflineNotSignedListDaKe(Long courseId){
		Params params = new Params();
		params.putData("courseId", courseId);
		return courseDao.findOfflineNotSignedListDaKe(params.getMap());
	}
	
	/**
	 * @Title: saveJNCourseJoin 
	 * @Description: (保存极牛线上直播报名信息) 
	 * @param join
	 */
	public boolean saveJNCourseJoin(JNCourseOnlineJoin join){
		return courseDao.saveJNCourseJoin(join) > 0;
	}
	
	/**
	 * @Title: findCourseJoin 
	 * @Description: (查询课程报名信息) 
	 * @param courseId 课程ID
	 * @return
	 */
	public JNCourseOnlineJoin findJNCourseJoinInfo(Long courseId,Long memberId){
		Params params = new Params();
		params.putData("courseId", courseId);
		params.putData("memberId", memberId);
		return courseDao.findJNCourseJoin(params.getMap());
	}
	
}
