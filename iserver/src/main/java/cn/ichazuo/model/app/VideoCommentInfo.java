package cn.ichazuo.model.app;

import cn.ichazuo.commons.base.BaseResult;

/**
 * ClassName: VideoCommentInfo 
 * Description: (视频评论信息) 
 * @author ZhaoXu
 * @date 2015年7月5日 下午7:22:12 
 * @version V1.0
 */
public class VideoCommentInfo extends BaseResult {
	private static final long serialVersionUID = 1L;

	private Long id; // 主键
	private String head; // 头像
	private String nickName; // 昵称
	private String comment; // 评论内容

	// get set
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
