package cn.ichazuo.model.app;

import java.util.Date;

import cn.ichazuo.commons.base.BaseResult;
import cn.ichazuo.commons.util.DateUtils;

/**
 * @ClassName: TeacherContent
 * @Description: (老师详情)
 * @author ZhaoXu
 * @date 2015年11月2日 上午11:35:25
 * @version V1.0
 */
public class TeacherContent extends BaseResult {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String content; // 简介
	private Date createAt; // 创建时间
	private String title; // 标题
	private Double price; // 价格

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

	public String getCreateAt() {
		return DateUtils.formatDate(createAt);
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
}
