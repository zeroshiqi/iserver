package cn.ichazuo.model.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.ichazuo.commons.base.BaseResult;
import cn.ichazuo.commons.util.DateUtils;
import cn.ichazuo.commons.util.StringUtils;

public class WebJobInfo extends BaseResult {
	private static final long serialVersionUID = 1L;

	private Long id; // jobid
	private String jobName; // 职位名称
	private String pay; // 薪资
	private String tag; // 标签

	private List<String> bottomTag; // 底部标签
	private String content; // 内容
	private String typeName; // 类别名称
	private String typeId; // 类别Id
	private String cover; // 封面

	private Date createAt; // 创建时间
	private String address; // 地址
	private String email; // 邮箱
	private String company; // 公司
	private String avatar; // 头图

	private String tags;	//底部标签
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getPay() {
		return pay;
	}

	public void setPay(String pay) {
		this.pay = pay;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public List<String> getBottomTag() {
		if(StringUtils.isNullOrEmpty(tags)){
			return new ArrayList<>(0);
		}
		String[] temp = tags.split("-");
		bottomTag = Arrays.asList(temp);
		if(bottomTag.size() > 4){
			bottomTag = bottomTag.subList(0, 4);
		}
		return bottomTag;
	}

	public void setBottomTag(List<String> bottomTag) {
		this.bottomTag = bottomTag;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	@JsonFormat(pattern = DateUtils.TIME_FORMAT_NORMAL, timezone = "GMT+8")
	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

}
