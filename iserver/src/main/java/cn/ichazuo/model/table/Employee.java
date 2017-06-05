package cn.ichazuo.model.table;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: Employee
 * @Description: (会员表)
 * @author ZhaoXu
 * @date 2015年7月18日 下午2:28:06
 * @version V1.0
 */
@Table(name = "t_business_employee")
public class Employee extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id; // id
	private String mobile; // 手机号
	private String password; // 密码
	private String name; // 名称
	private String sex; // 性别
	private String position;	//职位
	private String businessId; // 所在企业ID
	private String businessName; // 所在企业名称
	private String province; // 省份
	private String city; // 区县
	private Date createAt; // 创建时间
	private Date updateAt; // 修改时间
	private Integer status; // 状态
	private String accessToken; // token
	private String avatar;//头像
	private Long ifBusiness;//是否为企业用户 0：企业用户，1：个人注册用户
	private String commodityId;//苹果内购用产品ID
	private Long expiryDate;//会员有效期天数
	
	private int remainingDate;//年费会员剩余天数
	private String DeviceId;//设备Id
	//完成的课程数量
	private int finishedCount;
	//收藏的课程数量
	private int commentCount;
	//学习时长排名百分比
	private float studyTimePersent;
	//学习时长排名名次
	private int studyTimeRanking;
	//收藏的课程包数量
	private int collectionCatalogCount;
	private int studyTime;
	private Long employeeId;
	private Long buyCount;//购买数量
	private Long updateCount;//更新数量
	private String unionid;//绑定的微信id
	private String openid;
	private String wxName;//绑定的微信昵称
	private String wxAvatar;//绑定的微信头像
	
	public String getDeviceId() {
		return DeviceId;
	}
	public void setDeviceId(String deviceId) {
		DeviceId = deviceId;
	}

	private String overdueDate;//会员过期日期
	
	public String getOverdueDate() {
		return overdueDate;
	}
	public void setOverdueDate(String overdueDate) {
		this.overdueDate = overdueDate;
	}
	public String getCommodityId() {
		return commodityId;
	}
	public void setCommodityId(String commodityId) {
		this.commodityId = commodityId;
	}
	public Long getIfBusiness() {
		return ifBusiness;
	}
	public void setIfBusiness(Long ifBusiness) {
		this.ifBusiness = ifBusiness;
	}

	private int studyPlanCount;
	private int studyTimeCount;
	
	public int getStudyPlanCount() {
		return studyPlanCount;
	}
	public void setStudyPlanCount(int studyPlanCount) {
		this.studyPlanCount = studyPlanCount;
	}
	public int getStudyTimeCount() {
		return studyTimeCount;
	}
	public void setStudyTimeCount(int studyTimeCount) {
		this.studyTimeCount = studyTimeCount;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
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
	
	

	public int getFinishedCount() {
		return finishedCount;
	}
	public void setFinishedCount(int finishedCount) {
		this.finishedCount = finishedCount;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Employee){
			Employee temp = (Employee)obj;
			if(temp.accessToken.equals(this.accessToken)){
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.mobile.hashCode() + this.id.hashCode() + this.accessToken.hashCode();
	}

	@Override
	public String toString() {
		return super.toString();
	}
	public float getStudyTimePersent() {
		return studyTimePersent;
	}
	public void setStudyTimePersent(float studyTimePersent) {
		this.studyTimePersent = studyTimePersent;
	}
	public int getStudyTime() {
		return studyTime;
	}
	public void setStudyTime(int studyTime) {
		this.studyTime = studyTime;
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public int getStudyTimeRanking() {
		return studyTimeRanking;
	}
	public void setStudyTimeRanking(int studyTimeRanking) {
		this.studyTimeRanking = studyTimeRanking;
	}
	public Long getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Long expiryDate) {
		this.expiryDate = expiryDate;
	}
	public int getCollectionCatalogCount() {
		return collectionCatalogCount;
	}
	public void setCollectionCatalogCount(int collectionCatalogCount) {
		this.collectionCatalogCount = collectionCatalogCount;
	}
	public Long getBuyCount() {
		return buyCount;
	}
	public void setBuyCount(Long buyCount) {
		this.buyCount = buyCount;
	}
	public Long getUpdateCount() {
		return updateCount;
	}
	public void setUpdateCount(Long updateCount) {
		this.updateCount = updateCount;
	}
	public int getRemainingDate() {
		return remainingDate;
	}
	public void setRemainingDate(int remainingDate) {
		this.remainingDate = remainingDate;
	}
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getWxName() {
		return wxName;
	}
	public void setWxName(String wxName) {
		this.wxName = wxName;
	}
	public String getWxAvatar() {
		return wxAvatar;
	}
	public void setWxAvatar(String wxAvatar) {
		this.wxAvatar = wxAvatar;
	}
	
}
