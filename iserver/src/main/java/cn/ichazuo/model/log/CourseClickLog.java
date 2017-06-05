package cn.ichazuo.model.log;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: CourseClickLog
 * @Description: (课程点击日志)
 * @author ZhaoXu
 * @date 2015年8月19日 上午10:12:23
 * @version V1.0
 */
@Table(name = "l_course_click_log")
public class CourseClickLog extends BaseModel {
	private static final long serialVersionUID = 1L;
	
	private Long id; // id
	private String ipAddress; // ip地址
	private Long courseId; // 课程ID
	private Integer type; // 类别 1:线上 0:线下 2:web线下课程
	private Date createAt; // 创建时间
	private Long memberId;	//用户ID

	
	// get set
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
}
