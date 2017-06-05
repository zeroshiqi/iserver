package cn.ichazuo.model.app;

import cn.ichazuo.commons.base.BaseResult;

public class PaySuccessInfo extends BaseResult {
	private static final long serialVersionUID = 1L;

	private Long courseId; // 课程ID
	private String unionId; // 微信ID
	private Integer type; // 类型

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
