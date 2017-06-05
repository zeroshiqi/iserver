package cn.ichazuo.model.app;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.ichazuo.commons.base.BaseResult;
import cn.ichazuo.commons.util.DateUtils;

/**
 * @ClassName: SecondType
 * @Description: (线上课程二级目录列表信息)
 * @author ZhaoXu
 * @date 2015年7月20日 上午11:43:05
 * @version V1.0
 */
public class SecondType extends BaseResult {
	private static final long serialVersionUID = 1L;
	private Long id; // 字典ID
	private String name; // 一级目录名称
	private Date createAt; // 创建时间
	private Date updateAt; // 优先级
	private Long weight; // 权重
	private Long status;//一级分类状态
	private Long parentId;//父级ID
	private String parentName;//父级名称
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
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
	public Long getWeight() {
		return weight;
	}
	public void setWeight(Long weight) {
		this.weight = weight;
	}
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	
}
