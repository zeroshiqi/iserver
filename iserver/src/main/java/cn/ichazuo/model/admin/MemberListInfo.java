package cn.ichazuo.model.admin;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.ichazuo.commons.base.BaseModel;
import cn.ichazuo.commons.util.DateUtils;

/**
 * @ClassName: MemberListInfo
 * @Description: (用户列表信息)
 * @author ZhaoXu
 * @date 2015年7月16日 上午11:51:55
 * @version V1.0
 */
public class MemberListInfo extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String mobile; // 手机号
	private String nickName; // 昵称
	private String avatar; // 头像
	private String gender; // 性别
	private Date birthday; // 生日
	private Long companyId; // 所属公司
	private String companyName; // 公司名称
	private String jobName; // 职位名称
	private Integer jobYear; // 入职时间
	private Long coreCapacityId; // 核心能力Id
	private String coreCapaciyuName; // 核心能力名称
	private Date createAt; // 创建时间
	private Integer status; // 状态

	private String statusStr; // 页面显示状态
	private String jobYearStr; // 显示入职时间

	// get set
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

	@JsonFormat(pattern = DateUtils.DATE_FORMAT_NORMAL, timezone = "GMT+8")
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
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

	public Long getCoreCapacityId() {
		return coreCapacityId;
	}

	public void setCoreCapacityId(Long coreCapacityId) {
		this.coreCapacityId = coreCapacityId;
	}

	public String getCoreCapaciyuName() {
		return coreCapaciyuName;
	}

	public void setCoreCapaciyuName(String coreCapaciyuName) {
		this.coreCapaciyuName = coreCapaciyuName;
	}

	@JsonFormat(pattern = DateUtils.TIME_FORMAT_NORMAL, timezone = "GMT+8")
	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Integer getJobYear() {
		return jobYear;
	}

	public void setJobYear(Integer jobYear) {
		this.jobYear = jobYear;
	}

	public String getJobYearStr() {
		return jobYearStr;
	}

	public void setJobYearStr(String jobYearStr) {
		this.jobYearStr = jobYearStr;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
}
