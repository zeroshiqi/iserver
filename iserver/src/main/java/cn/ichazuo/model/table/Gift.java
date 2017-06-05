package cn.ichazuo.model.table;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: Company
 * @Description: (公司表)
 * @author ZhaoXu
 * @date 2015年7月18日 下午1:50:16
 * @version V1.0
 */
@Table(name = "t_haoduoke_gift")
public class Gift extends BaseModel {
	private static final long serialVersionUID = 1L;

	private String id;
	private String gCode; //赠送码
	private String unionId; //领取人unionId
	private String nickName; //领取人昵称
	private String expDate; // 礼品券过期日期
	private String collectionTime; //领取时间
	private String avatar; // 领取人头像
	private String mobile; // 手机号
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getgCode() {
		return gCode;
	}
	public void setgCode(String gCode) {
		this.gCode = gCode;
	}
	public String getUnionId() {
		return unionId;
	}
	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getExpDate() {
		return expDate;
	}
	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}
	public String getCollectionTime() {
		return collectionTime;
	}
	public void setCollectionTime(String collectionTime) {
		this.collectionTime = collectionTime;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
