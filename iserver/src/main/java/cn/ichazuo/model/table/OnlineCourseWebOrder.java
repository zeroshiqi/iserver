package cn.ichazuo.model.table;

import java.util.Date;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: OnlineCourseWebOrder 
 * @Description: (线上课程订单) 
 * @author ZhaoXu
 * @date 2015年11月2日 下午2:09:04 
 * @version V1.0
 */
public class OnlineCourseWebOrder extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id; // id
	private String code; // 订单号
	private Double price; // 价格
	private Long courseId; // 课程Id
	private String unionId; // 唯一微信号
	private String weixin;	//微信号
	private Integer status; // 状态
	private Long version; // 版本
	private Date createAt; // 创建时间
	private Date updateAt; // 修改时间
	private Integer at;	//订单来源

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

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
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

	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public Integer getAt() {
		return at;
	}

	public void setAt(Integer at) {
		this.at = at;
	}
}
