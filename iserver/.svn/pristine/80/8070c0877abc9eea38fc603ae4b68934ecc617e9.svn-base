package cn.ichazuo.model.table;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: CouserCommentFavour
 * @Description: (课程评论点赞表)
 * @author ZhaoXu
 * @date 2015年7月19日 下午3:26:57
 * @version V1.0
 */
@Table(name = "t_course_offline_comment_favour")
public class OfflineCourseCommentFavour extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id; // id
	private Long commentId; // 课程评论
	private Long memberId; // 点赞人
	private Integer status; // 状态
	private Long version; // 版本
	private Date createAt; // 创建时间
	private Date updateAt; // 修改时间

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
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

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
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

}
