package cn.ichazuo.model.table;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: CourseComment
 * @Description: (课程评论表)
 * @author ZhaoXu
 * @date 2015年7月19日 下午12:03:00
 * @version V1.0
 */
@Table(name = "t_course_offline_comment")
public class OfflineCourseComment extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id; // id
	private Long courseId; // 所属课程
	private Long memberId; // 评论人
	private String content; // 评论内容
	private Double star; // 星
	private Integer anonymous; // 是否匿名 0:不匿名 1:匿名
	private String nickName; // 名称
	private Date createAt; // 创建时间
	private Date updateAt; // 修改时间
	private Integer status; // 状态
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Double getStar() {
		return star;
	}

	public void setStar(Double star) {
		this.star = star;
	}

	public Integer getAnonymous() {
		return anonymous;
	}

	public void setAnonymous(Integer anonymous) {
		this.anonymous = anonymous;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
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
