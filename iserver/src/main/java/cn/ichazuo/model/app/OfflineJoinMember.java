package cn.ichazuo.model.app;

import java.util.Date;

import cn.ichazuo.commons.base.BaseService;

/**
 * @ClassName: OfflineJoinMember
 * @Description: (签到用学员报名列表信息)
 * @author LiDongYang
 * @date 2015年7月19日 上午11:07:18
 * @version V1.0
 */
public class OfflineJoinMember extends BaseService {
	private static final long serialVersionUID = 1L;
	private String id;
	private String mobile; // 手机号
	private String nickName; // 学员姓名
	private String company;//公司
	private String job;//职位
	private Long courseId; // 课程Id
	private Long memberId; // 学员Id
	private String courseName;
	private String reviewStatus;//审核状态
	private String rowi;
	private String columni;
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Long getCourseId() {
		return courseId;
	}
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getRowi() {
		return rowi;
	}
	public void setRowi(String rowi) {
		this.rowi = rowi;
	}
	public String getColumni() {
		return columni;
	}
	public void setColumni(String columni) {
		this.columni = columni;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReviewStatus() {
		return reviewStatus;
	}
	public void setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
	}
}
