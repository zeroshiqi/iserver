package cn.ichazuo.model.app;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.ichazuo.commons.base.BaseResult;
import cn.ichazuo.commons.util.DateUtils;

/**
 * @ClassName: CourseListInfo
 * @Description: (课程列表返回信息)
 * @author ZhaoXu
 * @date 2015年7月19日 下午12:19:39
 * @version V1.0
 */
public class OfflineCourseListInfo extends BaseResult {
	private static final long serialVersionUID = 1L;
	private Long id; // 课程ID
	private String cover; // 封面图片
	private String avatar; // 头图
	private String courseName; // 课程名称
	private Integer userCount; // 赞数量
	private Integer isFavour; // 是否赞 1:赞 0 未赞
	private Integer studentNum; // 招生总数
	private Integer joinNum; // 已报名人数
	private String courseTime; // 课程时间
	private String cityName; // 城市名称
	private Date beginTime;	//开始时间
	private Integer isOver;	//是否结束 1:报名结束  0:未结束
	private String teacherIds;//老师的ID
	private String teachers; //老师的名字
	private Integer isnew;	//是否标新    0:不  1:标新
	private Integer newType;//线上线下
	private String showAddress;//线上课程播放地址
	private float price;//课程价格
	private String teacherJob;//讲师职位
	private String station;//站点
	private String synopsis;
	private String content;
	//是否售卖
	private String isSell;
	//是否招满
	private String isfull;
	
	private Integer DeLate;	//是否过期:报名结束  0:未结束;
	
	//系列课Id
	private Long catalogId;
	//系列课展示形式：1用二级分类的形式展现，0：用一级分类形式展现
	private String flag;
	//目录类型：0：课程音频包 1：老师音频包 2课程视频包 3 老师视频包
	private String ifVideo;
	
	//系列课购买状态
	private String payStatus;
	
	
	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getShowAddress() {
		return showAddress;
	}

	public void setShowAddress(String showAddress) {
		this.showAddress = showAddress;
	}

	private List<UserSimpleInfo> userList;// 点赞列表

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Integer getIsFavour() {
		return isFavour;
	}

	public void setIsFavour(Integer isFavour) {
		this.isFavour = isFavour;
	}

	public Integer getStudentNum() {
		return studentNum;
	}

	public void setStudentNum(Integer studentNum) {
		this.studentNum = studentNum;
	}

	public String getCourseTime() {
		return courseTime;
	}

	public void setCourseTime(String courseTime) {
		this.courseTime = courseTime;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public List<UserSimpleInfo> getUserList() {
		return userList;
	}

	public void setUserList(List<UserSimpleInfo> userList) {
		this.userList = userList;
	}

	public Integer getUserCount() {
		return userCount;
	}

	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}

	public Integer getJoinNum() {
		return joinNum;
	}

	public void setJoinNum(Integer joinNum) {
		this.joinNum = joinNum;
	}
	
	@JsonFormat(pattern = DateUtils.TIME_FORMAT_NORMAL, timezone = "GMT+8")
	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Integer getIsOver() {
		return isOver;
	}

	public void setIsOver(Integer isOver) {
		this.isOver = isOver;
	}

	public String getTeacherIds() {
		return teacherIds;
	}

	public void setTeacherIds(String teacherIds) {
		this.teacherIds = teacherIds;
	}

	public String getTeachers() {
		return teachers;
	}

	public void setTeachers(String teachers) {
		this.teachers = teachers;
	}

	public Integer getIsnew() {
		return isnew;
	}

	public void setIsnew(Integer isnew) {
		this.isnew = isnew;
	}

	public Integer getNewType() {
		return newType;
	}

	public void setNewType(Integer newType) {
		this.newType = newType;
	}

	public String getTeacherJob() {
		return teacherJob;
	}

	public void setTeacherJob(String teacherJob) {
		this.teacherJob = teacherJob;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public Integer getDeLate() {
		return DeLate;
	}

	public void setDeLate(Integer deLate) {
		DeLate = deLate;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public String getIsSell() {
		return isSell;
	}

	public void setIsSell(String isSell) {
		this.isSell = isSell;
	}

	public String getIsfull() {
		return isfull;
	}

	public void setIsfull(String isfull) {
		this.isfull = isfull;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(Long catalogId) {
		this.catalogId = catalogId;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getIfVideo() {
		return ifVideo;
	}

	public void setIfVideo(String ifVideo) {
		this.ifVideo = ifVideo;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	
}
