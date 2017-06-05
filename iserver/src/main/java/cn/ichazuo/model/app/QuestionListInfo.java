package cn.ichazuo.model.app;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.ichazuo.commons.base.BaseResult;
import cn.ichazuo.commons.util.DateUtils;

/**
 * @ClassName: OnlineCourseListInfo
 * @Description: (试题列表信息)
 * @author FenDou
 * @version V1.0
 */
public class QuestionListInfo extends BaseResult {
	private static final long serialVersionUID = 1L;
	private Long     id; // 试题ID
	private String   title; // 试题名称
	private String   createAt;	//创建时间
	private String   updateAt;	//修改时间
	private String   status;      // 状态
	private Long number; //答题数量
	
public Long getNumber() {
		return number;
	}
	public void setNumber(Long number) {
		this.number = number;
	}
	//	试题的子目录模型
	private Long   parentId;    // 父级 Id
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCreateAt() {
		return createAt;
	}
	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}
	public String getUpdateAt() {
		return updateAt;
	}
	public void setUpdateAt(String updateAt) {
		this.updateAt = updateAt;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	 
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	 
	 
}
