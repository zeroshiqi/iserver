package cn.ichazuo.model.table;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: ArticleCommentFavour
 * @Description: (文章评论点赞)
 * @author ZhaoXu
 * @date 2015年8月5日 上午10:30:39
 * @version V1.0
 */
@Table(name = "t_article_comment_favour")
public class ArticleCommentFavour extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id; // id
	private Long commentId; // 评论id
	private Long memberId; // 用户id

	private Date createAt; // 创建时间
	private Date updateAt; // 修改时间
	private Integer status; // 状态
	private Long version; // 版本

	// get set
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
