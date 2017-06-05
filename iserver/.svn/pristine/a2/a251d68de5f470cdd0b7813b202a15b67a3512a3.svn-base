package cn.ichazuo.model.table;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: ThirdUser
 * @Description: (第三方用户)
 * @author ZhaoXu
 * @date 2015年7月18日 下午2:37:27
 * @version V1.0
 */
@Table(name = "t_third_member")
public class ThirdMember extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id; // id
	private Long memberId; // 注册用户ID
	private String openId; // 第三方id
	private Integer mark; // 第三方标识
	private Date createAt; // 创建时间
	private Date updateAt; // 修改时间
	private Integer status; // 状态
	private Long version; // 版本
	private String unionid;	//unionid

	
	// get set
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public Integer getMark() {
		return mark;
	}

	public void setMark(Integer mark) {
		this.mark = mark;
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

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

}
