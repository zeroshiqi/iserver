package cn.ichazuo.model.table;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: CourseImage
 * @Description: (课程图片表)
 * @author ZhaoXu
 * @date 2015年7月18日 下午4:09:41
 * @version V1.0
 */
@Table(name = "t_course_offline_image")
public class OfflineCourseImage extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id; // id
	private Long courseId; // 所属课程
	private String imageUrl; // 图片路径
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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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
