package cn.ichazuo.model.table;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: CourseClickDetail 
 * @Description: (课程点击明细表) 
 * @author ZhaoXu
 * @date 2015年7月21日 下午5:36:15 
 * @version V1.0
 */
@Table(name="t_course_click_detail")
public class CourseClickDetail extends BaseModel{
	private static final long serialVersionUID = 1L;
	
	private Long id;	//id
	private Long courseId;	//课程ID
	private String ip;	//用户ID
	private String type;	//1：点击课程封面进入课程简章；2、点击简章页面的立即报名按钮
	private Date createAt;	//创建时间
	private String city;	//城市
	private String from1;//来源
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCourseId() {
		return courseId;
	}
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
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
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getFrom1() {
		return from1;
	}
	public void setFrom1(String from1) {
		this.from1 = from1;
	}
	
}
