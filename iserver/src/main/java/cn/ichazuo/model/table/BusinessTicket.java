package cn.ichazuo.model.table;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

@Table(name = "t_business_ticket")
public class BusinessTicket extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id; // id
	private Long memberId; // 用户唯一ID
	private Double price; // price
	private Date createAt; // 创建时间
	private Integer status; // 状态
	private String level;	//级别
	private Integer favour;	//点赞数
	private String avatar;	//头像
	private String nickName;	//头像
	private Date lastTime;	//过期时间
	private Integer score; 	//分数
	private Long parentId; //二级目录Id
	private Long topParentId; //一级目录Id
	private String parentName; //二级目录名称
	private Long examType;//试卷类型：0、目录试卷，1、课程试卷
	
	public Long getExamType() {
		return examType;
	}

	public void setExamType(Long examType) {
		this.examType = examType;
	}

	//以下字段不对应数据库
	private int countNo;//参考人员总数
	private int rowNo; //该考生在所有参考人员中的排名
	private float percent;//击败了百分之多少的学员
	
	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Long getTopParentId() {
		return topParentId;
	}

	public void setTopParentId(Long topParentId) {
		this.topParentId = topParentId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public int getCountNo() {
		return countNo;
	}

	public void setCountNo(int countNo) {
		this.countNo = countNo;
	}

	public int getRowNo() {
		return rowNo;
	}

	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}

	public float getPercent() {
		return percent;
	}

	public void setPercent(float percent) {
		this.percent = percent;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Integer getFavour() {
		return favour;
	}

	public void setFavour(Integer favour) {
		this.favour = favour;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

}
