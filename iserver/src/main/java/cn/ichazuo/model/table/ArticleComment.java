package cn.ichazuo.model.table;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: ArticleComment
 * @Description: (文章评论)
 * @author ZhaoXu
 * @date 2015年8月5日 上午10:36:36
 * @version V1.0
 */
@Table(name = "t_article_comment")
public class ArticleComment extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id; // id
	private Long articleId; // 文章ID
	private Long memberId; // 用户ID
	private String content; // 评论内容
	private Date createAt; // 创建时间
	private Date updateAt; // 修改时间
	private Long version; // 版本
	private Integer status; // 状态
	
	private Double star;	//评分

	
	// get set
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getStar() {
		return star;
	}

	public void setStar(Double star) {
		this.star = star;
	}

}
