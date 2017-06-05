package cn.ichazuo.model.app;

import java.util.Date;

import cn.ichazuo.commons.base.BaseResult;
import cn.ichazuo.commons.util.DateUtils;

public class OnlineCourseResultInfo extends BaseResult {
	private static final long serialVersionUID = 1L;

	private Long courseId; // id
	private String content; // 课程内容或简介
	private String title; // 课程标题
	private Date createAt; // 课程时间
	private Double price; // 课程价格
	private String status; // 是否报名  true/false true:为购买过
	private Long isSell; // 课程是否售卖 0售卖，1停止售卖

	public Long getIsSell() {
		return isSell;
	}

	public void setIsSell(Long isSell) {
		this.isSell = isSell;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreateAt() {
		return DateUtils.formatDate(createAt);
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
