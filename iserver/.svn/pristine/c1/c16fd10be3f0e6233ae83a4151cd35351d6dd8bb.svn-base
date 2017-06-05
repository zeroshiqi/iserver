package cn.ichazuo.model.table;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: FeedBack
 * @Description: (意见反馈表)
 * @author ZhaoXu
 * @date 2015年7月18日 下午2:45:23
 * @version V1.0
 */
@Table(name = "t_feedback")
public class FeedBack extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id; // id
	private String content; // 留言内容
	private Long memberId; // 留言用户
	private Date createAt; // 创建时间
	private Date updateAt; // 修改时间
	private Long version;// 版本
	private Integer status; // 状态

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

}
