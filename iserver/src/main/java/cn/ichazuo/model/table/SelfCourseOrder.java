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
public class SelfCourseOrder extends BaseModel {
	private static final long serialVersionUID = 1L;
//	private Long id; // id
//	private Integer number; // 数量
	private String orderCode; // 订单号
	private Double price; // 金额
	private Long courseId; // 课程ID
//	private Integer status; // 状态
//	private Long version; // 版本
	private String createAt; // 创建时间
//	private Date updateAt; // 修改时间
//	private String openid; // openid
//	private String unionid; // unionid
//	private String ip; // ip
//	private Integer type; // 订单类别
//	private String nickname; // 姓名
//	private String weixinSex; // 微信性别
//	private String avatar; // 头像
//	private Integer felicitate; // 祝贺数
//	private Integer from;	//来源
//	private Integer ticketStatus; //是否使用奖学金

//	private String formNickName;	//用户填写微信昵称
//	private String joinReason;	//加入原因
//	private String buyIntentions; //购买意向
//	private String job;  //职位
//	private String email; //邮箱
//	private String invoiceType;//发票类型：咨询费；服务费。
//	private String invoiceAddress;//发票收货地址
	private String invoiceTitle;//发票抬头
//	private String invoiceRemarks;//备注
//	private String invoiceName;//收件人姓名
//	private String invoiceMobile;//收件人手机号
	
//	private String isGift;//是否赠送
//	private String message;//信的内容
//	private String giftDate;
//	private String address;
	private String courseName;//课程名称
	private String teachers;//讲师
	private String teacherId;//讲师Id
	private String invoiceStatus;//发票状态
	private String station;//课程举办站点
	private String newtype;//课程类型
	private String reviewStatus;//线下课程订单审核状态
	private String liveStatus;//直播课程直播状态
	private String liveRoomNo;//直播房间号
	private String stream;//推流地址
	private String beginTime;//课程开始时间
	private int catalogId;
	private String web;//订单来源：0、WEB订单，1、App订单

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getTeachers() {
		return teachers;
	}

	public void setTeachers(String teachers) {
		this.teachers = teachers;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
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

	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	public String getNewtype() {
		return newtype;
	}

	public void setNewtype(String newtype) {
		this.newtype = newtype;
	}

	public String getCreateAt() {
		return createAt;
	}

	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}

	public String getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	public String getLiveStatus() {
		return liveStatus;
	}

	public void setLiveStatus(String liveStatus) {
		this.liveStatus = liveStatus;
	}

	public String getLiveRoomNo() {
		return liveRoomNo;
	}

	public void setLiveRoomNo(String liveRoomNo) {
		this.liveRoomNo = liveRoomNo;
	}

	public String getStream() {
		return stream;
	}

	public void setStream(String stream) {
		this.stream = stream;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public int getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(int catalogId) {
		this.catalogId = catalogId;
	}

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}
}
