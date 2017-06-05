package cn.ichazuo.model.table;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: Question
 * @Description: (问题表 Model)
 * @author ZhaoXu
 * @date 2015年10月8日 下午5:39:53
 * @version V1.0
 */
@Table(name = "t_question")
public class Question extends BaseModel {
	private static final long serialVersionUID = 1L;
	private Long id; // id
	private String title; // 标题
	private String a; // a
	private String b; // b
	private String c; // c
	private String d; // d
	private String answer; // 正确答案
	private Integer status; // 状态
	private String userName;	//出题人姓名
	private String type; // 试题类型  1：基础 2、增强
    private Long parentId; // 父级id
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

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

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public String getB() {
		return b;
	}

	public void setB(String b) {
		this.b = b;
	}

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
}
