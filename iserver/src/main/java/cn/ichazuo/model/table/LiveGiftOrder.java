package cn.ichazuo.model.table;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: WebCourseOrder
 * @Description: (Web订单信息)
 * @author ZhaoXu
 * @date 2015年9月1日 下午1:49:57
 * @version V1.0
 */
@Table(name = "t_haoduoke_live_gift_order")
public class LiveGiftOrder extends BaseModel {
	private static final long serialVersionUID = 1L;
	private Long id; // id
	private String orderSn; // 订单号
	private Double price; // 金额
	private Long courseId; // 课程ID
	private Integer orderStatus; // 状态
	private Long liveCatalogId; // 系列课ID
	private Long liveCourseId; // 课程ID
	private Date payAt; // 支付时间
	private String openid; // openid
	private String unionid; // unionid
	private String ip; // ip
	private Integer type; // 订单类别
	private String nickName; // 姓名
	private String payWay; // 支付渠道：1、PC官网，2、手机官网，3、App
	private String avatar; // 头像
	private String employeeId;//App用户Id
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOrderSn() {
		return orderSn;
	}
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
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
	public Integer getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Long getLiveCatalogId() {
		return liveCatalogId;
	}
	public void setLiveCatalogId(Long liveCatalogId) {
		this.liveCatalogId = liveCatalogId;
	}
	public Long getLiveCourseId() {
		return liveCourseId;
	}
	public void setLiveCourseId(Long liveCourseId) {
		this.liveCourseId = liveCourseId;
	}
	public Date getPayAt() {
		return payAt;
	}
	public void setPayAt(Date payAt) {
		this.payAt = payAt;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPayWay() {
		return payWay;
	}
	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
}
