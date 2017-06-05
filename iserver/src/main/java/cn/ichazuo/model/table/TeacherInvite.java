package cn.ichazuo.model.table;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;
import cn.ichazuo.commons.util.StringUtils;

/**
 * @ClassName: TeacherInvite 
 * @Description: (邀请老师信息) 
 * @author ZhaoXu
 * @date 2015年10月16日 下午3:45:26 
 * @version V1.0
 */
@Table(name="t_teacher_invite")
public class TeacherInvite extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id; // id
	private String name; // 老师姓名
	private String content; // 老师介绍
	private String yourName; // 你的名字
	private String yourWeixin; // 你的微信
	private String yourPhone; // 你的手机号
	private Date createAt; // 创建时间

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getYourName() {
		return yourName;
	}

	public void setYourName(String yourName) {
		this.yourName = yourName;
	}

	public String getYourWeixin() {
		return yourWeixin;
	}

	public void setYourWeixin(String yourWeixin) {
		this.yourWeixin = yourWeixin;
	}

	public String getYourPhone() {
		return yourPhone;
	}

	public void setYourPhone(String yourPhone) {
		this.yourPhone = yourPhone;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	
	public boolean check(){
		return StringUtils.isNotNull(this.content,this.name,this.yourName,this.yourPhone,this.yourWeixin);
	}
}
