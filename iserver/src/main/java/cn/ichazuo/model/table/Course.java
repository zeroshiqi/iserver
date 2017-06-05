package cn.ichazuo.model.table;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: Course
 * @Description: (课程表)
 * @author ZhaoXu
 * @date 2015年7月18日 下午3:12:24
 * @version V1.0
 */
@Table(name = "t_course")
public class Course extends BaseModel {
	private static final long serialVersionUID = 1L;

	public static final int offlineType = 0;
	public static final int onlineType = 1;

	private Long id; // id
	private String courseName; // 课程名称
	private Integer type; // 课程类型 //0:线下课程 1:线上课程
	private Date beginTime; // 课程开始时间
	private String cover; // 封面图片
	private String avatar; // 头图

	private Date createAt; // 创建时间
	private Date updateAt; // 更新时间
	private Integer status; // 状态
	private Long version; // 版本
	private Long isSell; // 是否售卖
	public Long getIsSell() {
		return isSell;
	}

	public void setIsSell(Long isSell) {
		this.isSell = isSell;
	}

	//get set
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}
