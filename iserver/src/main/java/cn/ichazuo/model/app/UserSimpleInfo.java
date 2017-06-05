package cn.ichazuo.model.app;

import cn.ichazuo.commons.base.BaseService;

/**
 * @ClassName: UserSimpleInfo
 * @Description: (用户列表信息)
 * @author ZhaoXu
 * @date 2015年7月19日 上午11:07:18
 * @version V1.0
 */
public class UserSimpleInfo extends BaseService {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String nickname; // 名称
	private String avatar; // 头像
	private String companyName; // 公司名称
	private String jobName; // 职位名称
	private Integer jobYear; // 入职时间
	private String workYear; // 工作时间

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

}
