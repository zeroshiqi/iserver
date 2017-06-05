package cn.ichazuo.model.table;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: DictItem
 * @Description: (数据字典项表)
 * @author ZhaoXu
 * @date 2015年7月18日 下午1:31:57
 * @version V1.0
 */
@Table(name = "s_dict_item")
public class DictItem extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id; // id
	private String value; // 值
	private Long dictId; // 所属字典
	private Integer weight; // 权重 排序
	private String remark; // 备注
	private Integer status; // 状态
	private Date createAt; // 创建时间
	private Date updateAt; // 修改时间
	private Long version; // 版本

	
	// get set 
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getDictId() {
		return dictId;
	}

	public void setDictId(Long dictId) {
		this.dictId = dictId;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
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

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}
