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
@Table(name = "t_course_web_order")
public class WebCourseOrder extends BaseModel {
	private static final long serialVersionUID = 1L;
	private Long id; // id
	private Integer number; // 数量
	private String orderCode; // 订单号
	private Double price; // 金额
	private Long courseId; // 课程ID
	private Integer status; // 状态
	private Long version; // 版本
	private Date createAt; // 创建时间
	private Date updateAt; // 修改时间
	private String openid; // openid
	private String unionid; // unionid
	private String ip; // ip
	private Integer type; // 订单类别
	private String nickname; // 姓名
	private String weixinSex; // 微信性别
	private String avatar; // 头像
	private Integer felicitate; // 祝贺数
	private String from;	//来源
	private Integer ticketStatus; //是否使用奖学金
	private String name;
	private String mobile;
	private String work;
	private String content;
	private String province;
	private String city;	

	private String formNickName;	//用户填写微信昵称
	private String joinReason;	//加入原因
	private String buyIntentions; //购买意向
	private String job;  //职位
	private String email; //邮箱
	private String invoiceType;//发票类型：咨询费；服务费。
	private String invoiceAddress;//发票收货地址
	private String invoiceTitle;//发票抬头
	private String invoiceRemarks;//备注
	private String invoiceName;//收件人姓名
	private String invoiceMobile;//收件人手机号
	
	private String isGift;//是否赠送
	private String message;//信的内容
	private String giftDate;
	private String address;
	private String courseName;//课程名称
	private String teachers;//讲师
	private String teacherId;//讲师Id
	private String invoiceStatus;//发票状态
	private String station;//课程举办站点
	private String newtype;//课程类型
	private String reviewStatus;//订单审核状态
	private String beginTime;//开始时间
	private String stream;//直播流
	private String liveRoomNo;//直播房间号
	private String liveStatus;//直播状态
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getInvoiceAddress() {
		return invoiceAddress;
	}

	public void setInvoiceAddress(String invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}

	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	public String getInvoiceRemarks() {
		return invoiceRemarks;
	}

	public void setInvoiceRemarks(String invoiceRemarks) {
		this.invoiceRemarks = invoiceRemarks;
	}

	public String getInvoiceName() {
		return invoiceName;
	}

	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}

	public String getInvoiceMobile() {
		return invoiceMobile;
	}

	public void setInvoiceMobile(String invoiceMobile) {
		this.invoiceMobile = invoiceMobile;
	}

	public String getBuyIntentions() {
		return buyIntentions;
	}

	public void setBuyIntentions(String buyIntentions) {
		this.buyIntentions = buyIntentions;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	// get set
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getWeixinSex() {
		return weixinSex;
	}

	public void setWeixinSex(String weixinSex) {
		this.weixinSex = weixinSex;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Integer getFelicitate() {
		return felicitate;
	}

	public void setFelicitate(Integer felicitate) {
		this.felicitate = felicitate;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public Integer getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(Integer ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public String getFormNickName() {
		return formNickName;
	}

	public void setFormNickName(String formNickName) {
		this.formNickName = formNickName;
	}

	public String getJoinReason() {
		return joinReason;
	}

	public void setJoinReason(String joinReason) {
		this.joinReason = joinReason;
	}

	public String getIsGift() {
		return isGift;
	}

	public void setIsGift(String isGift) {
		this.isGift = isGift;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getGiftDate() {
		return giftDate;
	}

	public void setGiftDate(String giftDate) {
		this.giftDate = giftDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

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

	public String getNewtype() {
		return newtype;
	}

	public void setNewtype(String newtype) {
		this.newtype = newtype;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getStream() {
		return stream;
	}

	public void setStream(String stream) {
		this.stream = stream;
	}

	public String getLiveRoomNo() {
		return liveRoomNo;
	}

	public void setLiveRoomNo(String liveRoomNo) {
		this.liveRoomNo = liveRoomNo;
	}

	public String getLiveStatus() {
		return liveStatus;
	}

	public void setLiveStatus(String liveStatus) {
		this.liveStatus = liveStatus;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
}
