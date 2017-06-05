package cn.ichazuo.model.log;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: ArticleClickLog
 * @Description: (文章点击日志)
 * @author ZhaoXu
 * @date 2015年8月19日 上午11:21:44
 * @version V1.0
 */
@Table(name = "l_article_click_log")
public class ArticleClickLog extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id; // id
	private Long articleId; // 文章ID
	private Long memberId; // 用户Id
	private String ipAddress; // 请求地址
	private Date createAt; // 创建时间

	
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

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
}
