package cn.ichazuo.model.app;

import java.io.Serializable;
import java.util.Date;

public class GiftInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	//课程名称
	private String courseName;
	//内容
	private String message;
	//姓名
	private String name;
	//月份
	private String month;
	//日期
	private String day;	
	//购买人微信昵称
	private String weixinNickName;
	//购买人头像
	private String avatar;
	//购买人用户Id
	private String unionId;
	//是否购买人
	private String ifPurchaser;
	//开课时间
	private Date beginTime;
	private String giftDate;
	private String synopsis;
	private String isGift;
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getWeixinNickName() {
		return weixinNickName;
	}
	public void setWeixinNickName(String weixinNickName) {
		this.weixinNickName = weixinNickName;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getUnionId() {
		return unionId;
	}
	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}
	public String getIfPurchaser() {
		return ifPurchaser;
	}
	public void setIfPurchaser(String ifPurchaser) {
		this.ifPurchaser = ifPurchaser;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public String getSynopsis() {
		return synopsis;
	}
	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}
	public String getGiftDate() {
		return giftDate;
	}
	public void setGiftDate(String giftDate) {
		this.giftDate = giftDate;
	}
	public String getIsGift() {
		return isGift;
	}
	public void setIsGift(String isGift) {
		this.isGift = isGift;
	}
	
}
