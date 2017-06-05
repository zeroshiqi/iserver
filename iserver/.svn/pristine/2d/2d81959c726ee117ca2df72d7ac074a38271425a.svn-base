package cn.ichazuo.model.app;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.ichazuo.commons.base.BaseResult;
import cn.ichazuo.commons.util.DateUtils;

/**
 * @ClassName: ArticleInfo
 * @Description: (文章信息)
 * @author ZhaoXu
 * @date 2015年8月5日 上午10:39:32
 * @version V1.0
 */
public class ArticleInfo extends BaseResult {
	private static final long serialVersionUID = 1L;

	private Long id; // 文章id
	private String title; // 标题
	private String url;	//url
	private String content; // 内容
	private String type;	//类别
	private String css;	//css样式
	private Double star;	//评分
	
	private Long number;	//评分人数
	
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCss() {
		return css;
	}

	public void setCss(String css) {
		this.css = css;
	}

	public Double getStar() {
		return star;
	}

	public void setStar(Double star) {
		this.star = star;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
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
