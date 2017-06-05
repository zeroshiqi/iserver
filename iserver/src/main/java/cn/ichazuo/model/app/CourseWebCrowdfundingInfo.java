package cn.ichazuo.model.app;

import java.util.Date;
import java.util.List;

import cn.ichazuo.commons.base.BaseResult;
import cn.ichazuo.model.table.CourseWebCrowdfundingLog;

/**
 * @ClassName: CourseWebCrowdfundingInfo
 * @Description: (众筹信息)
 * @author ZhaoXu
 * @date 2015年9月14日 下午3:29:36
 * @version V1.0
 */
public class CourseWebCrowdfundingInfo extends BaseResult {
	private static final long serialVersionUID = 1L;
	private Long id; // id
	private Long courseId; // 课程ID
	private String title;	//标题
	private Integer number; // 数量
	private Double price; // 总价格
	private String content; // 内容
	private String uuid; // 随机字符串
	private String unionId; // 唯一ID
	private String nickname; // 发起人姓名
	private String avatar;	//头像
	private Integer count;	//参与人数量
	private Double money;	//筹集金额
	private Integer day;	//剩余天数
	private Date createAt;	//开始时间
	private Integer status;	//状态  0:未发起 1: 发起状态 2:成功 3:放弃
	private List<CourseWebCrowdfundingLog> logs;	//日志..
	private Integer refund;	//退款
	
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

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public List<CourseWebCrowdfundingLog> getLogs() {
		return logs;
	}

	public void setLogs(List<CourseWebCrowdfundingLog> logs) {
		this.logs = logs;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
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

	public Integer getRefund() {
		return refund;
	}

	public void setRefund(Integer refund) {
		this.refund = refund;
	}
}
