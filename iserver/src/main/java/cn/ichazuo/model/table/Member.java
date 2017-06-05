package cn.ichazuo.model.table;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: Member
 * @Description: (会员表)
 * @author ZhaoXu
 * @date 2015年7月18日 下午2:28:06
 * @version V1.0
 */
@Table(name = "t_member")
public class Member extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id; // id
	private String mobile; // 手机号
	private String password; // 密码
	private String nickName; // 昵称
	private String avatar; // 头像
	private Long loginNumber;	//登录次数
	private String accessToken; // token
	private String clientVersion; // 客户端版本
	private String deviceId; // 设备号
	private Date createAt; // 创建时间
	private Date updateAt; // 修改时间
	private Integer status; // 状态
	private Long version; // 版本
	
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
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

	public String getClientVersion() {
		return clientVersion;
	}

	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
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

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Member){
			Member temp = (Member)obj;
			if(temp.accessToken.equals(this.accessToken)){
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.mobile.hashCode() + this.id.hashCode() + this.accessToken.hashCode();
	}

	@Override
	public String toString() {
		return super.toString();
	}

	public Long getLoginNumber() {
		return loginNumber;
	}

	public void setLoginNumber(Long loginNumber) {
		this.loginNumber = loginNumber;
	}

}
