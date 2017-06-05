package cn.ichazuo.model.admin;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.ichazuo.commons.base.BaseResult;
import cn.ichazuo.commons.util.DateUtils;

/**
 * @ClassName: UserLogListInfo
 * @Description: (用户日志列表信息)
 * @author ZhaoXu
 * @date 2015年7月24日 下午12:46:49
 * @version V1.0
 */
public class UserLogListInfo extends BaseResult {
	private static final long serialVersionUID = 1L;

	private Long id; // id
	private Long userId; // userId
	private String account; // 账号
	private String uri; // uri
	private String ipAddress; // ip
	private String userName;// 用户名
	private String description; // 操作
	private Date createAt; // 创建时间

	// get set 
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
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

	@JsonFormat(pattern=DateUtils.TIME_FORMAT_NORMAL,timezone = "GMT+8")  
	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
}
