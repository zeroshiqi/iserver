package cn.ichazuo.model.table;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: OnlineCourseComment 
 * @Description: (线上课程评论表) 
 * @author ZhaoXu
 * @date 2015年7月21日 下午5:36:15 
 * @version V1.0
 */
@Table(name="t_course_online_comment")
public class OnlineCourseComment extends BaseModel{
	private static final long serialVersionUID = 1L;
	
	private Long id;	//id
	private Long courseId;	//课程ID
	private Long memberId;	//用户ID
	private String content;	//内容
	private Date createAt;	//创建时间
	private Date updateAt;	//修改时间
	private Integer status;	//状态
	private Long version; // 版本

	private String avatar;	//头像
	private String nickName;//昵称
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
}
