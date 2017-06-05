package cn.ichazuo.model.app;

import java.util.Date;
import java.util.List;

import cn.ichazuo.commons.base.BaseResult;

/**
 * @ClassName: OfflineCourseInfo
 * @Description: (线下课程信息接口返回)
 * @author ZhaoXu
 * @date 2015年7月19日 下午2:37:37
 * @version V1.0
 */
public class OfflineCourseInfo extends BaseResult {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String courseName; // 课程名称

	private String cover; // 封面图片
	private List<String> images; // 课程图片

	private String avatar; // 头图
	private String cityName; // 城市名称
	private Double price; // 价格
	private String courseTime; // 课程时间

	private Integer studentNum; // 招生总数
	private Integer joinNum; // 已报名人数
	private String address; // 讲课地址
	private Double lon; // 上课地点经度
	private Double lat; // 上课地点维度

	private Integer commentCount; // 评论数量
	private Double star; // 评分
	private Integer favourCount; // 赞数量
	private Integer isFavour; // 是否赞 1:赞 0 未赞
	private List<UserSimpleInfo> userList;// 点赞列表
	private Integer isComment; // 是否可以评论 0:可以评论 1:还不能评论
	private String simpleName; // 简称
	private List<UserSimpleInfo> teacherList; // 讲师列表
	
	private String ids;	//教师ID
	private Date beginTime;
	
	private Integer newType;

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

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
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

	public Integer getStudentNum() {
		return studentNum;
	}

	public void setStudentNum(Integer studentNum) {
		this.studentNum = studentNum;
	}

	public Integer getJoinNum() {
		return joinNum;
	}

	public void setJoinNum(Integer joinNum) {
		this.joinNum = joinNum;
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

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	public Double getStar() {
		return star;
	}

	public void setStar(Double star) {
		this.star = star;
	}

	public Integer getFavourCount() {
		return favourCount;
	}

	public void setFavourCount(Integer favourCount) {
		this.favourCount = favourCount;
	}

	public Integer getIsFavour() {
		return isFavour;
	}

	public void setIsFavour(Integer isFavour) {
		this.isFavour = isFavour;
	}

	public List<UserSimpleInfo> getUserList() {
		return userList;
	}

	public void setUserList(List<UserSimpleInfo> userList) {
		this.userList = userList;
	}

	public Integer getIsComment() {
		return isComment;
	}

	public void setIsComment(Integer isComment) {
		this.isComment = isComment;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public List<UserSimpleInfo> getTeacherList() {
		return teacherList;
	}

	public void setTeacherList(List<UserSimpleInfo> teacherList) {
		this.teacherList = teacherList;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public Integer getNewType() {
		return newType;
	}

	public void setNewType(Integer newType) {
		this.newType = newType;
	}

}
