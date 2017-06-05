package cn.ichazuo.model.table;

import java.util.Date;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: OnlineWebOrderUser
 * @Description: (线上课程报名用户信息)
 * @author ZhaoXu
 * @date 2015年11月2日 下午1:51:39
 * @version V1.0
 */
public class OnlineWebOrderUser extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id; // id
	private String name; // 姓名
	private String sex; // 性别
	private String weixin; // 微信
	private String phone; // 手机号
	private String company; // 公司
	private Long memberId; // 用户ID
	private Long orderId; // 所属订单
	private Integer status; // 状态
	private Long version;// 版本
	private Date createAt; // 创建时间
	private Date updateAt; // 修改时间

	
	//----------------------
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
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
