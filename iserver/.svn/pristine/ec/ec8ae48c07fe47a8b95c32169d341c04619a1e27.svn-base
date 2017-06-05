package cn.ichazuo.model.admin;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.ichazuo.commons.base.BaseResult;
import cn.ichazuo.commons.util.DateUtils;

/**
 * @ClassName: DictListInfo
 * @Description: (字典列表信息)
 * @author ZhaoXu
 * @date 2015年7月16日 下午5:14:26
 * @version V1.0
 */
public class DictListInfo extends BaseResult {
	private static final long serialVersionUID = 1L;
	
	private Long id; // id
	private String code; // 标识
	private String name; // 字典名称
	private Date createAt; // 创建时间
	private String remark;	//备注

	// get set
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@JsonFormat(pattern=DateUtils.TIME_FORMAT_NORMAL,timezone = "GMT+8")  
	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
