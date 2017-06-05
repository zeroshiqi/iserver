package cn.ichazuo.model.app;

import java.util.Date;

import cn.ichazuo.commons.base.BaseService;

/**
 * @ClassName: OfflineSigned
 * @Description: (签到列表信息)
 * @author LiDongYang
 * @date 2015年7月19日 上午11:07:18
 * @version V1.0
 */
public class OfflineSigned extends BaseService {
	private static final long serialVersionUID = 1L;
	private Long id;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private String mobile; // 手机号
	private String name; // 名字
	private Long courseId; // 课程Id
	private String courseName; // 课程名称
	private String createAt; // 签到时间
	private String company;
	private String job;
	//座位行号
	private String row;
	//座位列号
	private String column;
	
	//座位
	private String seat;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Long getCourseId() {
		return courseId;
	}
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getCreateAt() {
		return createAt;
	}
	public void setCreateAt(String createAt) {
		this.createAt = createAt;
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
	public String getSeat() {
		return seat;
	}
	public void setSeat(String seat) {
		this.seat = seat;
	}
	public String getRow() {
		return row;
	}
	public void setRow(String row) {
		this.row = row;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
}
