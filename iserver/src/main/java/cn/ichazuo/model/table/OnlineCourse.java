package cn.ichazuo.model.table;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: OnlineCourse
 * @Description: (线上课程信息表)
 * @author ZhaoXu
 * @date 2015年7月18日 下午3:33:29
 * @version V1.0
 */
@Table(name = "t_course_online")
public class OnlineCourse extends BaseModel {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long courseId; // 所属课程
	private Date playBeginTime; // 直播开始时间
	private Date playEndTime; // 直播结束时间
	private Long playAddressId; // 直播地址Id
	private String playAddress;	//播放地址
	private Long joinNumber; // 参加人数
	private Integer isVideo; // 是否为视频 1:为视频 0:为音频
	private Integer timeLength; // 时长

	private Date createAt; // 创建时间
	private Date updateAt; // 更新时间
	private Integer status; // 状态
	private Long version; // 版本
	
	private Double price;	//价格
	private String introduction;	//介绍

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

	public Date getPlayBeginTime() {
		return playBeginTime;
	}

	public void setPlayBeginTime(Date playBeginTime) {
		this.playBeginTime = playBeginTime;
	}

	public Date getPlayEndTime() {
		return playEndTime;
	}

	public void setPlayEndTime(Date playEndTime) {
		this.playEndTime = playEndTime;
	}

	public Long getJoinNumber() {
		return joinNumber;
	}

	public void setJoinNumber(Long joinNumber) {
		this.joinNumber = joinNumber;
	}

	public Integer getIsVideo() {
		return isVideo;
	}

	public void setIsVideo(Integer isVideo) {
		this.isVideo = isVideo;
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

	public Integer getTimeLength() {
		return timeLength;
	}

	public void setTimeLength(Integer timeLength) {
		this.timeLength = timeLength;
	}

	public Long getPlayAddressId() {
		return playAddressId;
	}

	public void setPlayAddressId(Long playAddressId) {
		this.playAddressId = playAddressId;
	}

	public String getPlayAddress() {
		return playAddress;
	}

	public void setPlayAddress(String playAddress) {
		this.playAddress = playAddress;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

}
