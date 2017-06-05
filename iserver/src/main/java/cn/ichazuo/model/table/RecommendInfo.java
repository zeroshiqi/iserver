package cn.ichazuo.model.table;

import java.util.Date;
import java.util.List;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: RecommendInfo
 * @Description: (首页推荐列表记录表)
 * @author LDY
 * @date 2016-09-28 11:52:50
 * @version V1.0
 */
@Table(name = "t_haoduoke_recommend_info")
public class RecommendInfo extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id; // id
	private String postion; // 推荐位置 1:banner 2:热门 3:讲师推荐 4:精选推荐 5:免费体验 6线下实录
	private String targetId;	//推荐Id
	private String targetName; // 推荐名称
	private String targetType; //推荐类型1:普通课程2:课程包3:教师4:无功能5:h5
	private String targetImg; // 推荐图
	private String createAt;
	private String updateAt;
	private String version;
	private String status;
	private String os;
	private String weight;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPostion() {
		return postion;
	}
	public void setPostion(String postion) {
		this.postion = postion;
	}
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public String getTargetName() {
		return targetName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	public String getTargetType() {
		return targetType;
	}
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	public String getTargetImg() {
		return targetImg;
	}
	public void setTargetImg(String targetImg) {
		this.targetImg = targetImg;
	}
	public String getCreateAt() {
		return createAt;
	}
	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}
	public String getUpdateAt() {
		return updateAt;
	}
	public void setUpdateAt(String updateAt) {
		this.updateAt = updateAt;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
}
