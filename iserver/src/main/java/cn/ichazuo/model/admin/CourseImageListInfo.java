package cn.ichazuo.model.admin;

import cn.ichazuo.commons.base.BaseResult;

/**
 * @ClassName: CourseImageListInfo 
 * @Description: (课程图片列表信息) 
 * @author ZhaoXu
 * @date 2015年7月23日 下午3:21:43 
 * @version V1.0
 */
public class CourseImageListInfo extends BaseResult{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String courseName; //课程名称
	private String url;  //图片路径

	public String getImage() {
		return "<img src="+url+" width='150px' height='60px' />";
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
