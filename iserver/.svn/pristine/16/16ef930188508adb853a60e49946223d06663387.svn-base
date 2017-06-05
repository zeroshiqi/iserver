package cn.ichazuo.model.app;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.ichazuo.commons.base.BaseResult;
import cn.ichazuo.commons.util.DateUtils;

/**
 * @ClassName: CommentListInfo
 * @Description: (评论返回信息)
 * @author ZhaoXu
 * @date 2015年7月9日 上午11:03:47
 * @version V1.0
 */
public class OfflineCourseCommentInfo extends BaseResult {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long userId; // 用户id
	private Integer anonymous; // 是否匿名 0不匿名 1:匿名
	private String content;// 评论内容
	private Integer agree; // 赞同数
	private String nickname; // 名称
	private String avatar; // 头像
	private Integer isAgree; // 是否赞同 0:未赞同 1:赞同
	private Date createAt;	//创建时间

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public Integer getAnonymous() {
		return anonymous;
	}

	public void setAnonymous(Integer anonymous) {
		this.anonymous = anonymous;
	}

	public Integer getIsAgree() {
		return isAgree;
	}

	public void setIsAgree(Integer isAgree) {
		this.isAgree = isAgree;
	}

	public Integer getAgree() {
		return agree;
	}

	public void setAgree(Integer agree) {
		this.agree = agree;
	}

	@JsonFormat(pattern=DateUtils.TIME_FORMAT_NORMAL,timezone = "GMT+8")  
	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

}
