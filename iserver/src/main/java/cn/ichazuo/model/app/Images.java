package cn.ichazuo.model.app;

import cn.ichazuo.commons.base.BaseResult;

public class Images extends BaseResult {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String url;
	private String imageUrl;
	private String title;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
