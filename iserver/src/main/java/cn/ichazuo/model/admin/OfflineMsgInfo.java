package cn.ichazuo.model.admin;

import java.util.Date;

import cn.ichazuo.commons.base.BaseResult;

public class OfflineMsgInfo extends BaseResult {
	private static final long serialVersionUID = 1L;
	private String type; // 类型值
	private String cityName; // 城市名称
	private Date beginTime; // 开始时间
	private Date endTime; // 结束时间
	private String catalogId;//课程包ID
	private String catalogName;//课程包名称
	private String expiryDate;//会员期限
	private String endDate;//会员过期时间
	private String message;//短信
	private String courseTime;//课程时间
	private String newtype;
	private String message2;//
//	private String message5;//
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	private String courseName;	//课程名称
	private Long typeId;	//类别Id
	private Long cityId;//城市Id
	private String address;//地址

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public String getCourseName() {
		return courseName == null ? "" : courseName.replace("「", "-").replace("」", "");
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public String getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}

	public String getCatalogName() {
		return catalogName;
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCourseTime() {
		return courseTime;
	}

	public void setCourseTime(String courseTime) {
		this.courseTime = courseTime;
	}

	public String getNewtype() {
		return newtype;
	}

	public void setNewtype(String newtype) {
		this.newtype = newtype;
	}

	public String getMessage2() {
		return message2;
	}

	public void setMessage2(String message2) {
		this.message2 = message2;
	}

//	public String getMessage5() {
//		return message5;
//	}
//
//	public void setMessage5(String message5) {
//		this.message5 = message5;
//	}
}
