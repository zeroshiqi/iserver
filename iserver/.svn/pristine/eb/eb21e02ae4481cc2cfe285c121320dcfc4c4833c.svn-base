package cn.ichazuo.model.app;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.ichazuo.commons.base.BaseResult;
import cn.ichazuo.commons.util.DateUtils;

/**
 * @ClassName: ArticleCommentInfo
 * @Description: (文章评论内容)
 * @author ZhaoXu
 * @date 2015年8月5日 上午10:52:23
 * @version V1.0
 */
public class ArticleCommentInfo extends BaseResult {
	private static final long serialVersionUID = 1L;

	private Long id; // id
	private Long memberId; // 用户ID
	private String avatar; // 头像
	private String nickName; // 名称
	private String content; // 内容
	private Long favourCount; // 点赞数量

	private Integer isFavour; // 是否赞过 0:未赞 1:赞过
	private Date createAt;	//创建时间

	
	// get set
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getFavourCount() {
		return favourCount;
	}

	public void setFavourCount(Long favourCount) {
		this.favourCount = favourCount;
	}

	public Integer getIsFavour() {
		return isFavour;
	}

	public void setIsFavour(Integer isFavour) {
		this.isFavour = isFavour;
	}

	@JsonFormat(pattern=DateUtils.TIME_FORMAT_NORMAL,timezone = "GMT+8")  
	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
}
