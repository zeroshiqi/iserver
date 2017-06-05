package cn.ichazuo.model.table;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: CourseWebInfo
 * @Description: (课程web信息)
 * @author ZhaoXu
 * @date 2015年7月25日 下午6:04:25
 * @version V1.0
 */
@Table(name = "t_course_web_info")
public class CourseWebInfo extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String url; // url
	private Long number; // 点击数
	private Long courseId; // 课程ID

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
}
