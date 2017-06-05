package cn.ichazuo.model.table;

import java.util.Date;
import java.util.List;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: Catalog
 * @Description: (课程分类表)
 * @author ZhaoXu
 * @date 2015年7月18日 下午2:28:06
 * @version V1.0
 */
@Table(name = "t_business_catalog")
public class Catalog extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id; // id
	private String name; // 标题名称
	private String subtitle;	//子标题名称
	private String status; // 状态
	private String createAt; //创建时间
	private String updateAt; // 修改时间
	
	private int courseCount;//包含课程总数
	private int studyCount;//已完成学习总数
	private Long examId;//试题ID
	private String examName;//试卷名称
	private String number;//试卷题数
	
	private String ifRemoteLogin;//是否异地登录
	private Long ifCollection;//是否收藏
	
	private String avatar;//背景图
	private String flag;//展示方式标志：0一级目录形式展示，1二级目录形式展示
	
	private List bannerList; //首页banner列表
	private List catalogList; //首页最新课程列表
	private List hotCourseList;//今日热门课程列表
	private List recommendCourseList;//推荐课程列表
	private List secondCatalogList;//推荐课程列表
	private List courseList;//推荐课程列表
	
	private String teacherId;//讲师id
	private String teacherName;//讲师姓名
	private List teacherList;//讲师列表
	
	private String isChapter;//是否可以点击
	
	private String updateMark;//持续更新标识
	private String testMark;//测试参与标识
	
	private String cover;// 封面
	
	private String total;//课程包包含的课程数量
	private String ifBusiness;//会员类别
	private String isChoose;//是否选课
	
	private String type;//课程包类型
	private int counts;
	private String ifVideo;
	private String price;
	private String courseId;//关联的课程Id
	private String synopsisAddress;//课程包简章地址
	
	private String shareAddress;//分享地址
	private String listCover;//竖图
	private String catalogContent;//简章
	private String courseUpdateStatus;//是否已经更新完毕 1:未完毕 2:已经更新完毕
	
	private String payStatus;//购买状态
	
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public Long getExamId() {
		return examId;
	}
	public void setExamId(Long examId) {
		this.examId = examId;
	}
	public int getCourseCount() {
		return courseCount;
	}
	public void setCourseCount(int courseCount) {
		this.courseCount = courseCount;
	}
	public int getStudyCount() {
		return studyCount;
	}
	public void setStudyCount(int studyCount) {
		this.studyCount = studyCount;
	}
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
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getIfRemoteLogin() {
		return ifRemoteLogin;
	}
	public void setIfRemoteLogin(String ifRemoteLogin) {
		this.ifRemoteLogin = ifRemoteLogin;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public List getBannerList() {
		return bannerList;
	}
	public void setBannerList(List bannerList) {
		this.bannerList = bannerList;
	}
	public List getCatalogList() {
		return catalogList;
	}
	public void setCatalogList(List catalogList) {
		this.catalogList = catalogList;
	}
	public List getHotCourseList() {
		return hotCourseList;
	}
	public void setHotCourseList(List hotCourseList) {
		this.hotCourseList = hotCourseList;
	}
	public List getRecommendCourseList() {
		return recommendCourseList;
	}
	public void setRecommendCourseList(List recommendCourseList) {
		this.recommendCourseList = recommendCourseList;
	}
	public String getIsChapter() {
		return isChapter;
	}
	public void setIsChapter(String isChapter) {
		this.isChapter = isChapter;
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
	public List getTeacherList() {
		return teacherList;
	}
	public void setTeacherList(List teacherList) {
		this.teacherList = teacherList;
	}
	public List getSecondCatalogList() {
		return secondCatalogList;
	}
	public void setSecondCatalogList(List secondCatalogList) {
		this.secondCatalogList = secondCatalogList;
	}
	public List getCourseList() {
		return courseList;
	}
	public void setCourseList(List courseList) {
		this.courseList = courseList;
	}
	public Long getIfCollection() {
		return ifCollection;
	}
	public void setIfCollection(Long ifCollection) {
		this.ifCollection = ifCollection;
	}
	public String getUpdateMark() {
		return updateMark;
	}
	public void setUpdateMark(String updateMark) {
		this.updateMark = updateMark;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getTestMark() {
		return testMark;
	}
	public void setTestMark(String testMark) {
		this.testMark = testMark;
	}
	public String getIfBusiness() {
		return ifBusiness;
	}
	public void setIfBusiness(String ifBusiness) {
		this.ifBusiness = ifBusiness;
	}
	public int getCounts() {
		return counts;
	}
	public void setCounts(int counts) {
		this.counts = counts;
	}
	public String getIfVideo() {
		return ifVideo;
	}
	public void setIfVideo(String ifVideo) {
		this.ifVideo = ifVideo;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getShareAddress() {
		return shareAddress;
	}
	public void setShareAddress(String shareAddress) {
		this.shareAddress = shareAddress;
	}
	public String getIsChoose() {
		return isChoose;
	}
	public void setIsChoose(String isChoose) {
		this.isChoose = isChoose;
	}
	public String getSynopsisAddress() {
		return synopsisAddress;
	}
	public void setSynopsisAddress(String synopsisAddress) {
		this.synopsisAddress = synopsisAddress;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getCatalogContent() {
		return catalogContent;
	}
	public void setCatalogContent(String catalogContent) {
		this.catalogContent = catalogContent;
	}
	public String getListCover() {
		return listCover;
	}
	public void setListCover(String listCover) {
		this.listCover = listCover;
	}
	public String getCourseUpdateStatus() {
		return courseUpdateStatus;
	}
	public void setCourseUpdateStatus(String courseUpdateStatus) {
		this.courseUpdateStatus = courseUpdateStatus;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	
}
