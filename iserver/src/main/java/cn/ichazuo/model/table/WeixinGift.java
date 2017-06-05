package cn.ichazuo.model.table;

import java.util.Date;
import java.util.List;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: MemberRecord
 * @Description: (会员记录表)
 * @author LDY
 * @date 2016-09-28 11:52:50
 * @version V1.0
 */
@Table(name = "t_business_weixin_gift")
public class WeixinGift extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id; // id
	private String unionid; // unionid
	private String openid;	//openid
	private String nickname; // 微信昵称
	private String createAt; //领取时间
	private String status; // 数据状态
	private String mobile;//手机号
	private String avatar;//头像
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getCreateAt() {
		return createAt;
	}
	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
}
