package cn.ichazuo.model.table;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: CourseOnlineJoin
 * @Description: (线上课程参加表)
 * @author ZhaoXu
 * @date 2015年7月21日 上午11:37:44
 * @version V1.0
 */
@Table(name="t_course_online_join")
public class CourseOnlineJoin extends BaseModel {
	private static final long serialVersionUID = 1L;
	private Long id; // 主键
	private Long courseId;// 课程id
	private Long memberId; // 用户id
	private Integer status; // 状态
	private Date createAt; // 创建时间
	private Date updateAt; // 修改时间
	private Long version; // 版本

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}
