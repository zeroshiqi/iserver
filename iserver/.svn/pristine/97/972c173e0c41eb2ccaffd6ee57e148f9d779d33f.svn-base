package cn.ichazuo.model.table;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: Authorize
 * @Description: (权限类)
 * @author ZhaoXu
 * @date 2015年7月14日 下午8:12:29
 * @version V1.0
 */
@Table(name="s_authorize")
public class Authorize extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id; // 主键
	private Integer type; // 类型 0:按钮 1:资源
	private String buttonId; // 按钮id
	private String groupId; // 组id
	private String propertiesId; // 资源id
	private String roleCode; // 角色Code
	private Date createAt; // 创建时间
	private Date updateAt; // 修改时间
	private Long version; // 版本

	//get set
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getButtonId() {
		return buttonId;
	}

	public void setButtonId(String buttonId) {
		this.buttonId = buttonId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getPropertiesId() {
		return propertiesId;
	}

	public void setPropertiesId(String propertiesId) {
		this.propertiesId = propertiesId;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
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

}
