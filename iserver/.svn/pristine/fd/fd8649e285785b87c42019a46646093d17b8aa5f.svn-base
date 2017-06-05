package cn.ichazuo.model.log;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: PushLog 
 * @Description: (推送日志) 
 * @author ZhaoXu
 * @date 2015年8月19日 下午7:32:02 
 * @version V1.0
 */
@Table(name = "l_push_log")
public class PushLog extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id; // id
	private String content; // 推送内容
	private String type;// 推送类型
	private Date createAt; // 创建时间

	
	//get set
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
}
