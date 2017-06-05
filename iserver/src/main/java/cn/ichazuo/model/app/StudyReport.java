package cn.ichazuo.model.app;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.ichazuo.commons.base.BaseResult;
import cn.ichazuo.commons.util.DateUtils;

/**
 * @ClassName: StudyDetail
 * @Description: (学习明细)
 * @author ZhaoXu
 * @date 2015年7月20日 上午11:43:05
 * @version V1.0
 */
public class StudyReport extends BaseResult {
	private static final long serialVersionUID = 1L;
	private Long id; // 明细ID
	private Long employeeId; // 企业用户Id
	private Long studyTime; // 学习时长
	private Long type; // 1、包含所有的学习时间；2、只包括学习完成的学习时间；1、只包括学习中的学习时间
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
	public Long getStudyTime() {
		return studyTime;
	}
	public void setStudyTime(Long studyTime) {
		this.studyTime = studyTime;
	}
	public Long getType() {
		return type;
	}
	public void setType(Long type) {
		this.type = type;
	}
}
