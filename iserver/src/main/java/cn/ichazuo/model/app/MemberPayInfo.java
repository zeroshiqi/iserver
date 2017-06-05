package cn.ichazuo.model.app;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: MemberPayInfo
 * @Description: (会员购买记录表)
 * @author ZhaoXu
 * @date 2015年7月18日 下午2:45:23
 * @version V1.0
 */
@Table(name = "t_business_member_pay")
public class MemberPayInfo extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id; // id
	private Long employeeId; // 留言用户
	private Date createAt; // 创建时间
	private Date updateAt; // 修改时间
	private Long price;// 价格
	private Integer status; // 状态
	private Long memberType;//会员类型
	private Long month;//购买了几个月的会员
	
	public Long getMonth() {
		return month;
	}
	public void setMonth(Long month) {
		this.month = month;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
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
	public Long getPrice() {
		return price;
	}
	public void setPrice(Long price) {
		this.price = price;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getMemberType() {
		return memberType;
	}
	public void setMemberType(Long memberType) {
		this.memberType = memberType;
	}

	
}
