package cn.ichazuo.model.table;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: Company
 * @Description: (公司表)
 * @author ZhaoXu
 * @date 2015年7月18日 下午1:50:16
 * @version V1.0
 */
@Table(name = "t_business")
public class Business extends BaseModel {
	private static final long serialVersionUID = 1L;

	private String id;
	private String businessName; // 企业名称
	private String businessLevel; // 企业级别
	private String businessAddress; // 企业地址
	private String businessNature; // 企业性质
	private Long businessScale; // 企业规模
	private String loginName; // 登录名
	private String password; // 密码
	private Date createAt; // 创建时间
	private Date updateAt; // 修改时间
	private Integer status; // 状态

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getBusinessLevel() {
		return businessLevel;
	}

	public void setBusinessLevel(String businessLevel) {
		this.businessLevel = businessLevel;
	}

	public String getBusinessAddress() {
		return businessAddress;
	}

	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}

	public String getBusinessNature() {
		return businessNature;
	}

	public void setBusinessNature(String businessNature) {
		this.businessNature = businessNature;
	}

	public Long getBusinessScale() {
		return businessScale;
	}

	public void setBusinessScale(Long businessScale) {
		this.businessScale = businessScale;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
	
}
