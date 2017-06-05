package cn.ichazuo.model.table;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: CompanyImage
 * @Description: (公司图片表)
 * @author ZhaoXu
 * @date 2015年7月18日 下午1:46:34
 * @version V1.0
 */
@Table(name = "t_company_image")
public class CompanyImage extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id; // id
	private Long companyId; // 所属公司
	private String imageUrl; // 图片URL
	private Date createAt; // 创建时间
	private Date updateAt; // 更新时间
	private Integer status; // 状态
	private Long version; // 版本

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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
