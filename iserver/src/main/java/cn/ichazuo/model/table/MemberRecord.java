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
@Table(name = "t_business_member_record")
public class MemberRecord extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id; // id
	private String employeeId; // 客户ID
	private String catalogId;	//课程包ID
	private String type; // 会员类型 0：年票会员 1是系列课会员
	private String createAt; //创建时间
	private String status; // 数据状态
	private String courseId;//课程ID
	private String expiryDate;//有效期
	private Date endDate;//会员结束日期
	private String gainWay;//权限获得途径：0、购买，1赠送

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getGainWay() {
		return gainWay;
	}

	public void setGainWay(String gainWay) {
		this.gainWay = gainWay;
	}
}
