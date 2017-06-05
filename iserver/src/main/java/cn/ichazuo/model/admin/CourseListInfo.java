package cn.ichazuo.model.admin;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.ichazuo.commons.base.BaseResult;
import cn.ichazuo.commons.util.DateUtils;

/**
 * @ClassName: CourseListInfo
 * @Description: (课程信息返回)
 * @author ZhaoXu
 * @date 2015年7月22日 下午5:08:51
 * @version V1.0
 */
public class CourseListInfo extends BaseResult {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String courseName; // 课程名称
	private Integer type; // 类型 1:线上课程 0:线下课程
	private Date beginTime; // 课程开始时间
	private String cover; // 封面
	private String avatar; // 头图

	// 线下课程
	private Long offlineTypeId; // 线下课程类型id
	private String typeStr;	//线下课程类型
	private String courseContent; // 课程信息
	private Double price; // 价格
	private String courseTime;// 上课时间
	private Long cityId; // 城市ID
	private String cityName;	//城市名称
	private String teacherId; // 讲师
	private String teacherNames;	//讲师名称
	private Integer studentNum; // 招生总数
	private String address; // 地址
	private Double lon; // 上课地点经度
	private Double lat; // 上课地点纬度
	private Long commentCount; // 评论数量
	private Double star; // 评分
	private Long favourCount; // 点赞数量

	// 线上课程
	private Date playBeginTime; // 直播开始时间
	private Date playEndTime; // 直播结束时间
	private Integer isVideo; // 是否为视频 1:视频 0:音频
	private String playAddress; // 播放地址
	private Long playAddressId;	//播放地址Id
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@JsonFormat(pattern = DateUtils.DATE_FORMAT_NORMAL, timezone = "GMT+8")
	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
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

	public Long getOfflineTypeId() {
		return offlineTypeId;
	}

	public void setOfflineTypeId(Long offlineTypeId) {
		this.offlineTypeId = offlineTypeId;
	}

	public String getCourseContent() {
		return courseContent;
	}

	public void setCourseContent(String courseContent) {
		this.courseContent = courseContent;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getCourseTime() {
		return courseTime;
	}

	public void setCourseTime(String courseTime) {
		this.courseTime = courseTime;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public Integer getStudentNum() {
		return studentNum;
	}

	public void setStudentNum(Integer studentNum) {
		this.studentNum = studentNum;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Long getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Long commentCount) {
		this.commentCount = commentCount;
	}

	public Double getStar() {
		return star;
	}

	public void setStar(Double star) {
		this.star = star;
	}

	public Long getFavourCount() {
		return favourCount;
	}

	public void setFavourCount(Long favourCount) {
		this.favourCount = favourCount;
	}

	@JsonFormat(pattern = DateUtils.TIME_FORMAT_NORMAL, timezone = "GMT+8")
	public Date getPlayBeginTime() {
		return playBeginTime;
	}

	public void setPlayBeginTime(Date playBeginTime) {
		this.playBeginTime = playBeginTime;
	}

	@JsonFormat(pattern = DateUtils.TIME_FORMAT_NORMAL, timezone = "GMT+8")
	public Date getPlayEndTime() {
		return playEndTime;
	}

	public void setPlayEndTime(Date playEndTime) {
		this.playEndTime = playEndTime;
	}

	public Integer getIsVideo() {
		return isVideo;
	}

	public void setIsVideo(Integer isVideo) {
		this.isVideo = isVideo;
	}

	public String getPlayAddress() {
		return playAddress;
	}

	public void setPlayAddress(String playAddress) {
		this.playAddress = playAddress;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getTeacherNames() {
		return teacherNames;
	}

	public void setTeacherNames(String teacherNames) {
		this.teacherNames = teacherNames;
	}

	public String getTypeStr() {
		if(type == 0){
			typeStr = "线下课程";
		}else{
			typeStr = "线上课程";
		}
		return typeStr;
	}

	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}

	public Long getPlayAddressId() {
		return playAddressId;
	}

	public void setPlayAddressId(Long playAddressId) {
		this.playAddressId = playAddressId;
	}

}
