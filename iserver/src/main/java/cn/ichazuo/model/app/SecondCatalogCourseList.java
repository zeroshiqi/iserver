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
public class SecondCatalogCourseList extends BaseResult {
	private static final long serialVersionUID = 1L;
	private Long id; // 课程ID
	private String cover; // 封面图片
	private String avatar; // 头图
	private String courseName; // 课程名称
	private List<UserSimpleInfo> userList; // 在线用户列表阿
	private Long userCount; // 在线人数
	private String playAddress; // 播放地址
	private String playAddressM3u8; // web播放地址
	private Date playStartTime; // 播放开始时间
	private Date playEndTime; // 播放结束时间
	private Long examId; // 试卷Id
	private String examName;//试卷名称
	

	private String playTime; // 播放时间
	private Integer timeLength;// 时长
	private Integer isVideo; // 是否视频 1:视频 0:音频
	private Integer type; // 类别 1:正在直播 2:预告 3:点播
	private String teacher;	//讲师

	private Double price;	//价格
	private Integer playStatus; // 播放状态
	private String shareUrl; // 分享地址
	
	private String isBuy; //是否购买   true / false
	private String isFavour;	//是否点赞  true/false
	private Date createAt;	//创建时间
	private String tag;	//标签
	private String htmlUrl;	//Html连接
	
	private Long studyNum;	//完成学习数量
	private Long level;	//课程等级：0、初级，1中级，3高级
	private Long keyWords;	//关键字
	private String subtitle; //子标题
	private Long isFinished; //是否完成学习 0:未开始，1进行中，2已完成
	
	private String coursePpt;//课程大纲
	private String pdfAddress;//课程大纲PDF文件地址
	
	//是否需要添加片头片尾
	private String ifTitle;
	//课程标新
	private String isnew;
	//参加测试标识
	private String testMark;
	
	public String getPdfAddress() {
		return pdfAddress;
	}

	public void setPdfAddress(String pdfAddress) {
		this.pdfAddress = pdfAddress;
	}

	public Long getIsFinished() {
		return isFinished;
	}

	public void setIsFinished(Long isFinished) {
		this.isFinished = isFinished;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	private Long ifRecommend;//是否推荐。0：未推荐，1推荐
	private Long ifStudyPlan;//是否已加入学习计划。0：未加入，1已加入
	
	
	public Long getIfRecommend() {
		return ifRecommend;
	}

	public void setIfRecommend(Long ifRecommend) {
		this.ifRecommend = ifRecommend;
	}

	public Long getIfStudyPlan() {
		return ifStudyPlan;
	}

	public void setIfStudyPlan(Long ifStudyPlan) {
		this.ifStudyPlan = ifStudyPlan;
	}

	public Long getStudyNum() {
		return studyNum;
	}

	public void setStudyNum(Long studyNum) {
		this.studyNum = studyNum;
	}

	public Long getLevel() {
		return level;
	}

	public void setLevel(Long level) {
		this.level = level;
	}

	public Long getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(Long keyWords) {
		this.keyWords = keyWords;
	}

	private Long catalogId;

	public Long getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(Long catalogId) {
		this.catalogId = catalogId;
	}

	// ----
	private String url;
	private Long number;
	private String week; // 周几
	// ----

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public List<UserSimpleInfo> getUserList() {
		return userList;
	}

	public void setUserList(List<UserSimpleInfo> userList) {
		this.userList = userList;
	}

	public String getPlayAddress() {
		return playAddress;
	}

	public void setPlayAddress(String playAddress) {
		this.playAddress = playAddress;
	}

	@JsonFormat(pattern = DateUtils.TIME_FORMAT_NORMAL, timezone = "GMT+8")
	public Date getPlayStartTime() {
		return playStartTime;
	}

	public void setPlayStartTime(Date playStartTime) {
		this.playStartTime = playStartTime;
	}

	public Integer getIsVideo() {
		return isVideo;
	}

	public void setIsVideo(Integer isVideo) {
		this.isVideo = isVideo;
	}

	public Long getUserCount() {
		return userCount;
	}

	public void setUserCount(Long userCount) {
		this.userCount = userCount;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getPlayTime() {
		return playTime;
	}

	public void setPlayTime(String playTime) {
		this.playTime = playTime;
	}

	public Integer getTimeLength() {
		return timeLength;
	}

	public void setTimeLength(Integer timeLength) {
		this.timeLength = timeLength;
	}

	@JsonFormat(pattern = DateUtils.TIME_FORMAT_NORMAL, timezone = "GMT+8")
	public Date getPlayEndTime() {
		return playEndTime;
	}

	public void setPlayEndTime(Date playEndTime) {
		this.playEndTime = playEndTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getPlayAddressM3u8() {
		return playAddressM3u8;
	}

	public void setPlayAddressM3u8(String playAddressM3u8) {
		this.playAddressM3u8 = playAddressM3u8;
	}

	public Integer getPlayStatus() {
		return playStatus;
	}

	public void setPlayStatus(Integer playStatus) {
		this.playStatus = playStatus;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getIsBuy() {
		return isBuy;
	}

	public void setIsBuy(String isBuy) {
		this.isBuy = isBuy;
	}

	public String getIsFavour() {
		return isFavour;
	}

	public void setIsFavour(String isFavour) {
		this.isFavour = isFavour;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	@JsonFormat(pattern = DateUtils.TIME_FORMAT_NORMAL, timezone = "GMT+8")
	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getHtmlUrl() {
		return htmlUrl;
	}

	public void setHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
	}

	public Long getExamId() {
		return examId;
	}

	public void setExamId(Long examId) {
		this.examId = examId;
	}

	public String getExamName() {
		return examName;
	}

	public void setExamName(String examName) {
		this.examName = examName;
	}

	public String getCoursePpt() {
		return coursePpt;
	}

	public void setCoursePpt(String coursePpt) {
		this.coursePpt = coursePpt;
	}

	public String getIfTitle() {
		return ifTitle;
	}

	public void setIfTitle(String ifTitle) {
		this.ifTitle = ifTitle;
	}

	public String getIsnew() {
		return isnew;
	}

	public void setIsnew(String isnew) {
		this.isnew = isnew;
	}

	public String getTestMark() {
		return testMark;
	}

	public void setTestMark(String testMark) {
		this.testMark = testMark;
	}
	
}
