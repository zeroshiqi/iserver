package cn.ichazuo.model.table;

import java.util.Date;
import java.util.List;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: MemberRecord
 * @Description: (会员记录表)
 * @author LDY
 * @date 2016-09-28 11:52:50
 * @version V1.0
 */
@Table(name = "t_haoduoke_member_series")
public class MemberSeries extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id; // id
	private String employeeId; // 客户ID
	private String catalogId;	//课程包ID
	private String createAt; //创建时间
	private String status; // 数据状态

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}

	public String getCreateAt() {
		return createAt;
	}

	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
