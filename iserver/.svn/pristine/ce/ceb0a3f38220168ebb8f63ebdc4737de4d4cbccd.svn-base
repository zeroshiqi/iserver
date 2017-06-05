package cn.ichazuo.model.table;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: MemberFavour
 * @Description: (用户点赞表)
 * @author ZhaoXu
 * @date 2015年7月18日 下午9:55:22
 * @version V1.0
 */
@Table(name = "t_member_favour")
public class MemberFavour extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id; // id
	private Long fromMemberId; // 点赞人
	private Long toMemberId; // 被赞人
	private Integer status; // 状态
	private Long version; // 版本
	private Date createAt; // 创建时间
	private Date updateAt; // 修改时间

	// get set
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getFromMemberId() {
		return fromMemberId;
	}

	public void setFromMemberId(Long fromMemberId) {
		this.fromMemberId = fromMemberId;
	}

	public Long getToMemberId() {
		return toMemberId;
	}

	public void setToMemberId(Long toMemberId) {
		this.toMemberId = toMemberId;
	}

}
