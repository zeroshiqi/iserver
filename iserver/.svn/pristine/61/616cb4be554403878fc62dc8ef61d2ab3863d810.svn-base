package cn.ichazuo.model.app;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.ichazuo.commons.base.BaseResult;
import cn.ichazuo.commons.util.DateUtils;

/**
 * @ClassName: OnlineCourseCommentInfo
 * @Description: (线上课程返回信息)
 * @author ZhaoXu
 * @date 2015年7月21日 下午5:47:58
 * @version V1.0
 */
public class OnlineCourseCommentInfo extends BaseResult {
	private static final long serialVersionUID = 1L;

	private Long id; // 评论ID
	private Long userId;	//用户ID
	private String nickName; // 昵称
	private String avatar; // 头像
	private String content; // 评论内容
	private Date createAt; // 创建时间

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	@JsonFormat(pattern=DateUtils.TIME_FORMAT_NORMAL,timezone = "GMT+8")  
	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
