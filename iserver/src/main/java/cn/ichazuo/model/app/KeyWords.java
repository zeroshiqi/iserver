package cn.ichazuo.model.app;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.ichazuo.commons.base.BaseResult;
import cn.ichazuo.commons.util.DateUtils;

/**
 * @ClassName: KeyWords
 * @Description: (课程搜索关键字)
 * @author ZhaoXu
 * @date 2016-5-11 10:10:45
 * @version V1.0
 */
public class KeyWords extends BaseResult {
	private static final long serialVersionUID = 1L;
	private Long id; // 字典ID
	private String keywords; // 关键字
	private Long status; // 状态
	private Date createAt; // 创建时间
	private Date updateAt; // 最后更新时间
	
	private String ifBusiness;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
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
	public String getIfBusiness() {
		return ifBusiness;
	}
	public void setIfBusiness(String ifBusiness) {
		this.ifBusiness = ifBusiness;
	}
	
}
