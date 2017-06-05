package cn.ichazuo.model.app;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.ichazuo.commons.base.BaseResult;
import cn.ichazuo.commons.util.DateUtils;

/**
 * @ClassName: Recommend
 * @Description: (课程推荐信息)
 * @author ZhaoXu
 * @date 2015年7月20日 上午11:43:05
 * @version V1.0
 */
public class Recommend extends BaseResult {
	private static final long serialVersionUID = 1L;
	private Long id; // 字典ID
	private Long employeeId; // 企业用户Id
	private Long courseId; // 课程Id
	private Long status; // 状态
	private Date createAt; // 创建时间
	private Date updateAt; // 最后更新时间
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
	public Long getCourseId() {
		return courseId;
	}
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
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
	public Date getUpdateAt() {
		return updateAt;
	}
	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}
	
}
