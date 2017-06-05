package cn.ichazuo.model.log;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: UserLog
 * @Description: (用户日志)
 * @author ZhaoXu
 * @date 2015年7月24日 下午12:29:54
 * @version V1.0
 */
@Table(name = "l_user_log")
public class UserLog extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String ipAddress; // ip
	private String uri; // uri
	private Long userId; // userId
	private String account; // 账号
	private String userName;// 用户姓名
	private String description; // 描述
	private Date createAt; // 创建时间

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
}
