package cn.ichazuo.model.app;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.ichazuo.commons.base.BaseResult;
import cn.ichazuo.commons.util.DateUtils;

/**
 * @ClassName: UserAllInfo
 * @Description: (用户信息返回)
 * @author ZhaoXu
 * @date 2015年7月19日 上午12:47:24
 * @version V1.0
 */
public class UserAllInfo extends BaseResult {
	private static final long serialVersionUID = 1L;
	private Long id; // id
	private String nickname; // 昵称
	private String avatar; // 头像
	private String gender; // 性别 1男 0女-1未设置
	private Date birthday; // 生日
	private String companyName; // 公司名称
	private String jobName; // 职位名称
	private Integer jobYear; // 入职时间
	private String workYear; // 工作时间
	private String coreCapacityName;// 核心能力
	private Integer age; // 年龄
	private String constellation; // 星座
	private Integer favourCount; // 赞数量 别人给你点赞的数量
	private Integer approveCount; // 赞别人
	private Integer courseCount; // 推荐课程数量
	private Integer courseJoinCount; // 参加课程数量
	private Integer isFavour = 0; // 是否赞过 0:没赞过 1:赞过
	
	private Long teacherCount;	//我的老师 数量
	
	private Integer status;		//用户状态

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@JsonFormat(pattern=DateUtils.DATE_FORMAT_NORMAL,timezone = "GMT+8")  
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Integer getJobYear() {
		return jobYear;
	}

	public void setJobYear(Integer jobYear) {
		this.jobYear = jobYear;
	}

	public String getWorkYear() {
		return workYear;
	}

	public void setWorkYear(String workYear) {
		this.workYear = workYear;
	}

	public String getCoreCapacityName() {
		return coreCapacityName;
	}

	public void setCoreCapacityName(String coreCapacityName) {
		this.coreCapacityName = coreCapacityName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getConstellation() {
		return constellation;
	}

	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}

	public Integer getIsFavour() {
		return isFavour;
	}

	public void setIsFavour(Integer isFavour) {
		this.isFavour = isFavour;
	}

	public Integer getFavourCount() {
		return favourCount;
	}

	public void setFavourCount(Integer favourCount) {
		this.favourCount = favourCount;
	}

	public Integer getApproveCount() {
		return approveCount;
	}

	public void setApproveCount(Integer approveCount) {
		this.approveCount = approveCount;
	}

	public Integer getCourseCount() {
		return courseCount;
	}

	public void setCourseCount(Integer courseCount) {
		this.courseCount = courseCount;
	}

	public Integer getCourseJoinCount() {
		return courseJoinCount;
	}

	public void setCourseJoinCount(Integer courseJoinCount) {
		this.courseJoinCount = courseJoinCount;
	}

	public Long getTeacherCount() {
		return teacherCount;
	}

	public void setTeacherCount(Long teacherCount) {
		this.teacherCount = teacherCount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
