package cn.ichazuo.model.app;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.ichazuo.commons.base.BaseResult;
import cn.ichazuo.commons.util.DateUtils;

/**
 * @ClassName: ArticleListInfo
 * @Description: (文章列表信息)
 * @author ZhaoXu
 * @date 2015年8月5日 上午10:06:28
 * @version V1.0
 */
public class ArticleListInfo extends BaseResult {
	private static final long serialVersionUID = 1L;

	private Long id; // id
	private String title; // 标题
	private String cover; // 封面
	private Integer level; // 级别 1:初级 2:中级 3:高级
	private Long clickNumber; // 点击数
	
	private Date createAt;	//创建时间
	private String tag;	//标签

	
	// get set
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Long getClickNumber() {
		return clickNumber;
	}

	public void setClickNumber(Long clickNumber) {
		this.clickNumber = clickNumber;
	}

	@JsonFormat(pattern = DateUtils.TIME_FORMAT_NORMAL, timezone = "GMT+8")
	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

}
