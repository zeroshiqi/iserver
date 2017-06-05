package cn.ichazuo.model.table;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: JNCourseJoin
 * @Description: (极牛直播课程报名表)
 * @author ZhaoXu
 * @date 2015年7月18日 下午4:30:46
 * @version V1.0
 */
@Table(name = "t_course_jiniu_offline_join")
public class JNCourseOnlineJoin extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id; // id
	private Long courseId; // 参加的课程
	private Long memberId; // 报名的人
	private Date createAt; // 创建时间
	private Date updateAt; // 修改时间
	private Integer status; // 状态
	private Long version; // 版本
	private Long orderId; // 订单Id

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	private int from = -1; // 来源 //-1为之前数据 0:众筹 1:百度支付 2:微信支付 3:支付宝
	private int type = 0; // 类型 1:APP 2:WEB 0: 默认 导入

	// get set
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

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
