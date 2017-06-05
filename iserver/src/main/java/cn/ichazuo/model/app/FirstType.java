package cn.ichazuo.model.app;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.ichazuo.commons.base.BaseResult;
import cn.ichazuo.commons.util.DateUtils;

/**
 * @ClassName: OnlineCourseListInfo
 * @Description: (线上课程列表信息)
 * @author ZhaoXu
 * @date 2015年7月20日 上午11:43:05
 * @version V1.0
 */
public class FirstType extends BaseResult {
	public List getTypeTwo() {
		return typeTwo;
	}
	public void setTypeTwo(List typeTwo) {
		this.typeTwo = typeTwo;
	}
	private static final long serialVersionUID = 1L;
	private Long id; // 字典ID
	private String name; // 一级目录名称
	private Date createAt; // 创建时间
	private Date updateAt; // 优先级
	private Long weight; // 权重
	private Long status;//一级分类状态
	private String cover;//封面
	private String courseId;//课程id
	private String courseName;//课程名称
	private String teacherId;//讲师Id
	private String teacherName;//讲师名称
	private String typeId;//课程包Id
	private String typeName;//课程包名称
	private String showType;//展示类型：0：课程，1讲师
	private String ifBusiness;
	private List typeTwo;
	//讲师列表
	private List teacher;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	public Date getUpdateAt() {
		return updateAt;
	}
	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}
	public Long getWeight() {
		return weight;
	}
	public void setWeight(Long weight) {
		this.weight = weight;
	}
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	public List getTeacher() {
		return teacher;
	}
	public void setTeacher(List teacher) {
		this.teacher = teacher;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getShowType() {
		return showType;
	}
	public void setShowType(String showType) {
		this.showType = showType;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getIfBusiness() {
		return ifBusiness;
	}
	public void setIfBusiness(String ifBusiness) {
		this.ifBusiness = ifBusiness;
	}
	
}
