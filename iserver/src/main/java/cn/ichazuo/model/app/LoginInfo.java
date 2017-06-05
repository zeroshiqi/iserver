package cn.ichazuo.model.app;

import cn.ichazuo.commons.base.BaseResult;

/**
 * @ClassName: LoginInfo 
 * @Description: (返回登录信息) 
 * @author ZhaoXu
 * @date 2015年7月19日 上午12:07:24 
 * @version V1.0
 */
public class LoginInfo extends BaseResult {
	private static final long serialVersionUID = 1L;

	private Long id; // 主键 
	private String mobile; // 手机号 
	private String nickname; // 昵称
	private String avatar; // 头像url
	private String accessToken; // token
	private Long ifBusiness; // 是否企业用户 0：企业用户；1自注册用户
	private String commodityId;//苹果内购用产品ID	
	private String subloginSign;//用户登录时产生的sign用于调用二次登录接口
	public String getCommodityId() {
		return commodityId;
	}

	public void setCommodityId(String commodityId) {
		this.commodityId = commodityId;
	}

	private String overdueDate;//会员过期日期
	
	public String getOverdueDate() {
		return overdueDate;
	}

	public void setOverdueDate(String overdueDate) {
		this.overdueDate = overdueDate;
	}

	public Long getIfBusiness() {
		return ifBusiness;
	}

	public void setIfBusiness(Long ifBusiness) {
		this.ifBusiness = ifBusiness;
	}

	// 1.1.2后添加
	private String openid;	//openid
	
	// get set
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getSubloginSign() {
		return subloginSign;
	}

	public void setSubloginSign(String subloginSign) {
		this.subloginSign = subloginSign;
	}
	
}
