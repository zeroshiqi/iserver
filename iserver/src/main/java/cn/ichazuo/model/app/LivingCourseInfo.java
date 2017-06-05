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
public class LivingCourseInfo extends BaseResult {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String courseName; // 课程名称
	private String cover; // 封面图片

	private String avatar; // 头图
	private Double price; // 价格
	private String courseTime; // 课程时间

	private String teacherName; //讲师姓名
	private String courseContent; //课程简章
	private String showAddress; // 直播地址
	private String teacherJob; // 讲师职位
	private String stream; //查询直播间状态的stream值
	private String livingStatus;//
	private String beginTime;
	private String ifBuy;//是否购买
	private String vid;//直播频道号
	private String uid;//直播账号Id
	private String praiseNumber;//点赞数量
	private int catalogId;//课程包ID
	private String synopsis;//直播课程分享语
	private String liveNotice;//直播公告

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

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
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

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getCourseContent() {
		return courseContent;
	}

	public void setCourseContent(String courseContent) {
		this.courseContent = courseContent;
	}

	public String getShowAddress() {
		return showAddress;
	}

	public void setShowAddress(String showAddress) {
		this.showAddress = showAddress;
	}

	public String getTeacherJob() {
		return teacherJob;
	}

	public void setTeacherJob(String teacherJob) {
		this.teacherJob = teacherJob;
	}

	public String getStream() {
		return stream;
	}

	public void setStream(String stream) {
		this.stream = stream;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getIfBuy() {
		return ifBuy;
	}

	public void setIfBuy(String ifBuy) {
		this.ifBuy = ifBuy;
	}

	public String getLivingStatus() {
		return livingStatus;
	}

	public void setLivingStatus(String livingStatus) {
		this.livingStatus = livingStatus;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPraiseNumber() {
		return praiseNumber;
	}

	public void setPraiseNumber(String praiseNumber) {
		this.praiseNumber = praiseNumber;
	}

	public int getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(int catalogId) {
		this.catalogId = catalogId;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public String getLiveNotice() {
		return liveNotice;
	}

	public void setLiveNotice(String liveNotice) {
		this.liveNotice = liveNotice;
	}
}
