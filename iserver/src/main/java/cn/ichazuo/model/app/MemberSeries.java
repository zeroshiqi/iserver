package cn.ichazuo.model.app;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.ichazuo.commons.base.BaseResult;
import cn.ichazuo.commons.util.DateUtils;

/**
 * @ClassName: MemberSeries
 * @Description: (学员选课记录表)
 * @author LiDongYang
 * @date 2016年11月07日14:42:10
 * @version V1.0
 */
public class MemberSeries extends BaseResult {
	private static final long serialVersionUID = 1L;
	private Long id; // 字典ID
	private Long employeeId; // 企业用户Id
	private Long catalogId; // 课程包Id
	private Long status; // 状态
	private Date createAt; // 创建时间
	private Date catalogName; // 课程包名称
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
	public Long getCatalogId() {
		return catalogId;
	}
	public void setCatalogId(Long catalogId) {
		this.catalogId = catalogId;
	}
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	public Date getCatalogName() {
		return catalogName;
	}
	public void setCatalogName(Date catalogName) {
		this.catalogName = catalogName;
	}
	
}
