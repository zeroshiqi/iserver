package cn.ichazuo.model.table;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: CourseWebCrowdfundingOrder 
 * @Description: (众筹支付订单) 
 * @author ZhaoXu
 * @date 2015年9月15日 下午1:05:36 
 * @version V1.0
 */
@Table(name="t_course_web_crowdfunding_order")
public class CourseWebCrowdfundingOrder extends BaseModel{
	private static final long serialVersionUID = 1L;
	
	private Long id;	//id
	private String code;	//订单号
	private Long crowdfundingId;//众筹ID
	private String ip;	//ip
	private Double price;	//价格
	private Date createAt;	//创建时间
	private Date updateAt;	//修改时间
	private Integer status;	//状态
	private Long version; // 版本

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

	public Long getCrowdfundingId() {
		return crowdfundingId;
	}

	public void setCrowdfundingId(Long crowdfundingId) {
		this.crowdfundingId = crowdfundingId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}
