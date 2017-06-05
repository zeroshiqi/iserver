package cn.ichazuo.model.admin;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.ichazuo.commons.base.BaseResult;
import cn.ichazuo.commons.util.DateUtils;

/**
 * @ClassName: DictItemListInfo
 * @Description: (字典项列表信息)
 * @author ZhaoXu
 * @date 2015年7月16日 下午7:40:42
 * @version V1.0
 */
public class DictItemListInfo extends BaseResult {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String value; // 值
	private String name;	//所属字典
	private Integer weight;	//权重
	private String remark; // 备注
	private Date createAt; // 创建时间

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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@JsonFormat(pattern=DateUtils.TIME_FORMAT_NORMAL,timezone = "GMT+8")  
	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}
}
