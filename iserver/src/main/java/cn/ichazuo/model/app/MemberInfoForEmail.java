package cn.ichazuo.model.app;

import java.util.Date;

import cn.ichazuo.commons.base.BaseService;

/**
 * @ClassName: MemberInfoForEmail
 * @Description: (线下课程结业证书邮件信息)
 * @author LiDongYang
 * @date 2015年7月19日 上午11:07:18
 * @version V1.0
 */
public class MemberInfoForEmail extends BaseService {
	private static final long serialVersionUID = 1L;
	private String name; //姓名
	private String email; // 邮箱
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
