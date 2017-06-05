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
@Table(name="t_chazuo_seat")
public class DaKe extends BaseModel{
	private static final long serialVersionUID = 1L;
	
	private Long id;	//id
	private Long courseId;	//课程ID
	private Long pai;	//用户ID
	private Long hao;	//1：点击课程封面进入课程简章；2、点击简章页面的立即报名按钮
	private String mobile;	//创建时间
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
	public Long getPai() {
		return pai;
	}
	public void setPai(Long pai) {
		this.pai = pai;
	}
	public Long getHao() {
		return hao;
	}
	public void setHao(Long hao) {
		this.hao = hao;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
