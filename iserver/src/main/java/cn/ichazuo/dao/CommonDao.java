package cn.ichazuo.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.ichazuo.model.app.BusinessFeedBack;
import cn.ichazuo.model.app.CatalogCourseList;
import cn.ichazuo.model.app.Comment;
import cn.ichazuo.model.app.CommentPraise;
import cn.ichazuo.model.app.DictItem;
import cn.ichazuo.model.app.FirstType;
import cn.ichazuo.model.app.GiftInfo;
import cn.ichazuo.model.app.Images;
import cn.ichazuo.model.app.KeyWords;
import cn.ichazuo.model.app.MemberPayInfo;
import cn.ichazuo.model.app.MemberSeries;
import cn.ichazuo.model.app.OnlineCourseCommentInfo;
import cn.ichazuo.model.app.OnlineCourseListInfo;
import cn.ichazuo.model.app.UserInfo;
import cn.ichazuo.model.app.WebJobInfo;
import cn.ichazuo.model.app.WebOfflineCourseInfo;
import cn.ichazuo.model.app.QuestionListInfo;
import cn.ichazuo.model.app.Recommend;
import cn.ichazuo.model.app.SecondCatalogCourseList;
import cn.ichazuo.model.app.SecondType;
import cn.ichazuo.model.app.SelfStudy;
import cn.ichazuo.model.app.StudyDetail;
import cn.ichazuo.model.app.StudyDetailHistory;
import cn.ichazuo.model.app.StudyPlan;
import cn.ichazuo.model.app.StudyPlanCatalog;
import cn.ichazuo.model.app.StudyReport;
import cn.ichazuo.model.table.ArticleCommentFavour;
import cn.ichazuo.model.table.Banner;
import cn.ichazuo.model.table.Business;
import cn.ichazuo.model.table.BusinessTicket;
import cn.ichazuo.model.table.BusinessVersion;
import cn.ichazuo.model.table.Catalog;
import cn.ichazuo.model.table.CourseWebInfo;
import cn.ichazuo.model.table.Employee;
import cn.ichazuo.model.table.FeedBack;
import cn.ichazuo.model.table.Gift;
import cn.ichazuo.model.table.MemberRecord;
import cn.ichazuo.model.table.Question;
import cn.ichazuo.model.table.RecommendInfo;
import cn.ichazuo.model.table.SecondCatalog;
import cn.ichazuo.model.table.Student;
import cn.ichazuo.model.table.Teacher;
import cn.ichazuo.model.table.Ticket;
import cn.ichazuo.model.table.Version;
import cn.ichazuo.model.table.WebCourseOrder;

/**
 * @ClassName: CommonDao 
 * @Description: (通用DAO) 
 * @author ZhaoXu
 * @date 2015年7月20日 下午10:40:44 
 * @version V1.0
 */
public interface CommonDao {

	public CourseWebInfo findCourseWebInfo(Long id);
	
	public int updateCourseWebInfo(CourseWebInfo info);
	
	public List<OnlineCourseCommentInfo> findOnlineCourseCommentLimit50(Long courseId);
	
	public int updateTicketStatus(Map<String,Object> map);
	
	public UserInfo findUserInfoByUnionId(String unionId);
	public UserInfo findUserEmailInfoByUnionId(String unionId);
	public UserInfo findInvoiceDetailByUnionId(String unionId);
	public UserInfo findInvoiceDetailByOrderNo(String orderNo);
	//根据订单编号查询赠送详情
	public GiftInfo findGiftDetailByOrderNo(String orderNo);
	
	public List<WebJobInfo> getWebJobList(Map<String,Object> params);
	
	public WebJobInfo getWebJobInfo(Long id);
	
	public String getQRCodeURL(Long id);
	

	
	 
	/**
	 * @Title: findWebCourseOfflineInfo 
	 * @Description: (查询线下课程信息) 
	 * @param courseId
	 * @return
	 */
	public WebOfflineCourseInfo findWebCourseOfflineInfo(Long courseId);
	
	/**
	 * @Title: updateWebCourseClickNumber 
	 * @Description: (修改网页课程点击数量) 
	 * @param id
	 * @return
	 */
	public int updateWebCourseClickNumber(Long id);
	
	/**
	 * @Title: findAppVersionMax 
	 * @Description: (查询最新版本) 
	 * @param client
	 * @return
	 */
	public Version findAppVersionMax(String client);
	
	/**
	 * @Title: findBusinessAppVersionByMap 
	 * @Description: (查询当前版本信息) 
	 * @param params
	 * @return
	 */
	public BusinessVersion findBusinessAppVersionByMap(Map<String,Object> params);
	
	/**
	 * @Title: findAppVersionMax 
	 * @Description: (查询最新版本) 
	 * @param client
	 * @return
	 */
	public BusinessVersion findBusinessAppVersionMax(String client);
	
	/**
	 * @Title: findAppVersionByMap 
	 * @Description: (查询当前版本信息) 
	 * @param params
	 * @return
	 */
	public Version findAppVersionByMap(Map<String,Object> params);
	
	public int updateWebOrderFelicitate(String code);
	
	public int findFelicitateCount(String code);
	
	public int updateWebOrderFelicitates(Long id);
	
	public int updateWebCrowdFelicitates(Long id);
	
	/**
	 * @Title: findAllQuestion 
	 * @Description: (查询全部问题) 
	 * @return
	 */
	public List<Question> findAllQuestion();
	public List<Question> findAllQuestionByType(int type);
	
	public int saveTicket(Ticket ticket);
	public int saveScore(Ticket ticket);
	
	public int saveBusinessScore(BusinessTicket ticket);
	
	public Ticket findTicket(Long id);
	
	public int updateTicketFavour(Long id);
	
	public Long findHaveTicket(String unionId);
	public Long findIfHaveTicket(Ticket ticket);
	public int findTicketNo(Ticket ticket);
	public int findTicketCount(Ticket ticket);
	public String findUnionIdById(Long id);
	public int findParentIdById(Long id);
	public Long findScoreMaxByParentId(int parentId);
	public Long findScoreMinByParentId(int parentId);
	
	@Deprecated
	public List<Images> findAllImages();
	
	public List<Images> findAllImagesV2(int type);
	
//	试题相关
	/**
	 * @Title: getQuestionList 
	 * @Description: (查询试题列表) 
	 * @param params
	 * @return
	 */
	public List<QuestionListInfo> getQuestionList(Map<String,Object> params); 
	/**
	 * @Title: getQuestionChildList 
	 * @Description: (查询子标题列表) 
	 * @param id
	 * @return
	 */
	public List<QuestionListInfo> getQuestionChildList(Long id);
	/**
	 * @Title: getChildQuestionsById 
	 * @Description: (查询全部问题) 
	 * @param id
	 * @return
	 */
	public List<Question> getChildQuestionsById(int id);
	
	//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓   企业APP专用   ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	
	/**
	 * @Title: findCatalogList 
	 * @Description: (查询分类列表) 
	 * @param params
	 * @return
	 */
	public List<Catalog> findCatalogList(Map<String,Object> params); 
	
	public List<Catalog> findCatalogListCount(Map<String,Object> params);
	
	/**
	 * @Title: findTeacherList 
	 * @Description: (查询讲师列表) 
	 * @param params
	 * @return
	 */
	public List<Teacher> findTeacherList(Map<String,Object> params); 
	
	/**
	 * @Title: findTeacherList 
	 * @Description: (查询讲师列表) 
	 * @param params
	 * @return
	 */
	public List<Teacher> findAppTeacherList(Map<String,Object> params); 
	
	/**
	 * @Title: findTeacherList 
	 * @Description: (查询讲师列表) 
	 * @param params
	 * @return
	 */
	public List<Teacher> getTeacherListForWeb(Map<String,Object> params); 
	/**
	 * @Title: findTeacherList 
	 * @Description: (查询讲师列表) 
	 * @param params
	 * @return
	 */
	public List<Teacher> getTeacherListForDaKe(Map<String,Object> params); 
	
	/**
	 * @Title: findTeacherList 
	 * @Description: (查询讲师列表) 
	 * @param params
	 * @return
	 */
	public List<Teacher> queryTeacherList(Map<String,Object> params); 
	
	/**
	 * @Title: findTeacherList 
	 * @Description: (查询讲师列表) 
	 * @param params
	 * @return
	 */
	public List<Student> findStudentList(Map<String,Object> params); 
	
	/**
	 * @Title: findHomePageNewCatalogList 
	 * @Description: (查询首页最新课程分类列表) 
	 * @param params
	 * @return
	 */
	public List<Catalog> findHomePageNewCatalogList(Map<String,Object> params); 
	/**
	 * @Title: findHotCatalogList 
	 * @Description: (查询首页最新课程分类列表) 
	 * @param params
	 * @return
	 */
	public List<Catalog> findHotCatalogList(Long id); 
	
	
	/**
	 * @Title: findCatalogListCourseCount 
	 * @Description: (查询分类下的课程数量) 
	 * @param params
	 * @return
	 */
	public int findCatalogListCourseCount(Map<String,Object> params); 
	
	/**
	 * @Title: findTeacherById 
	 * @Description: (根据id查询讲师) 
	 * @param params
	 * @return
	 */
	public List<Teacher> findTeacherById(Map<String,Object> params); 
	
	/**
	 * @Title: findCatalogListStudyCount 
	 * @Description: (查询分类下的完成学习人数) 
	 * @param params
	 * @return
	 */
	public int findCatalogListStudyCount(Map<String,Object> params); 
	
	/**
	 * @Title: findParentCatalogList 
	 * @Description: (查询课程一级分类列表) 
	 * @param params
	 * @return
	 */
	public List<Catalog> findParentCatalogList(Map<String,Object> params);
	public List<Catalog> findOtherParentCatalogList(Map<String,Object> params);
	public List<Catalog> findBuyParentCatalogList(Map<String,Object> params);
	public List<Catalog> findParentVideoCatalogList(Map<String,Object> params);
	public List<Catalog> findParentCatalogListCount(Map<String,Object> params);
	
	/**
	 * @Title: findParentCatalogList 
	 * @Description: (查询课程一级分类列表) 
	 * @param params
	 * @return
	 */
	public List<MemberRecord> findMemberRecordList(Map<String,Object> params);
	
	public List<Employee> queryAllEmployee();
	
	/**
	 * @Title: findRecommendList 
	 * @Description: (查询课程一级分类列表) 
	 * @param params
	 * @return
	 */
	public List<RecommendInfo> findRecommendList(Map<String,Object> params);
	/**
	 * @Title: findMemberRecordListByEmployeeId 
	 * @Description: (根据学员ID查询学员好多课会员购买记录列表) 
	 * @param params
	 * @return
	 */
	public List<MemberRecord> findMemberRecordListByEmployeeId(Map<String,Object> params);
	
	/**
	 * @Title: findMemberRecordListByEmployeeId1 
	 * @Description: (根据学员ID查询学员好多课会员购买记录列表) 
	 * @param params
	 * @return
	 */
	public List<MemberRecord> findMemberRecordListByEmployeeId1(Map<String,Object> params);
	
	/**
	 * @Title: findMemberRecordListByCatalogId 
	 * @Description: (根据课程包Id和学员ID查询学员是否报名了该课程) 
	 * @param params
	 * @return
	 */
	public List<MemberRecord> findMemberRecordListByCatalogId(Map<String,Object> params);
	
	public List<WebCourseOrder> findCourseWebOrderBymobile(Map<String,Object> params);
	
	public List<WebCourseOrder> findCourseAppOrderByEmployeeId(Map<String,Object> params);
	
	public List<WebCourseOrder> findCourseAppOrderByEmployeeId1(Map<String,Object> params);
	
	/**
	 * @Title: findParentCatalogList 
	 * @Description: (查询课程一级分类列表) 
	 * @param params
	 * @return
	 */
	public List<Catalog> findParentCatalogListByTeacher(Map<String,Object> params);
	
	/**
	 * @Title: findParentCatalogList 
	 * @Description: (查询课程一级分类列表) 
	 * @param params
	 * @return
	 */
	public List<Catalog> findParentCatalogListById(Map<String,Object> params);
	/**
	 * @Title: findParentCatalogList 
	 * @Description: (查询课程一级分类列表) 
	 * @param params
	 * @return
	 */
	public List<Catalog> findTeacherParentCatalogById(Map<String,Object> params);
	
	/**
	 * @Title: findSecondCatalogList 
	 * @Description: (查询分类列表) 
	 * @param params
	 * @return
	 */
	public List<SecondCatalog> findSecondCatalogList(Map<String,Object> params); 
	/**
	 * @Title: findPersonCatalogList 
	 * @Description: (查询个人注册分类列表) 
	 * @param params
	 * @return
	 */
	public List<Catalog> findPersonCatalogList(Map<String,Object> params); 
 
	/**
	 * @Title: getCatalogCourseList 
	 * @Description: (查询分类下课程列表) 
	 * @param params
	 * @return
	 */
	public List<CatalogCourseList> getCatalogCourseList(Long id); 
	/**
	 * 根据课程包ID和会员购买课程包时间，查询课程开始时间在购买时间之后，且未听过的课程列表
	 * @param params
	 * @return
	 */
	public List<CatalogCourseList> getCatalogCourseListByBuyDate(Map<String,Object> params);
	/**
	 * @Title: getCatalogCourseList 
	 * @Description: (查询分类下课程列表) 
	 * @param params
	 * @return
	 */
	public List<CatalogCourseList> getAllCatalogCourseList(Map<String,Object> params); 
	
	/**
	 * @Title: getCatalogCourseList 
	 * @Description: (查询分类下课程列表) 
	 * @param params
	 * @return
	 */
	public List<CatalogCourseList> getRecommendCourseList(Map<String,Object> params); 
	
	/**
	 * @Title: getCatalogCourseList 
	 * @Description: (查询分类下课程列表) 
	 * @param params
	 * @return
	 */
	public List<StudyDetail> getStudyType(Long id); 
	
	/**
	 * @Title: getStudyCourseId 
	 * @Description: (查询分类下课程列表) 
	 * @param params
	 * @return
	 */
	public List<StudyDetail> getStudyCourseId(Long id); 
	/**
	 * @Title: getHaveStudyCourseId 
	 * @Description: (查询分类下课程列表) 
	 * @param params
	 * @return
	 */
	public List<StudyDetail> getHaveStudyCourseId(Long id); 
	
	/**
	 * @Title: getCatalogCourseList 
	 * @Description: (查询分类下课程列表) 
	 * @param params
	 * @return
	 */
	public List<CatalogCourseList> getSurplusCourseList(Long id); 
	
	/**
	 * @Title: getBannerList 
	 * @Description: (查询首页Banner列表) 
	 * @param params
	 * @return
	 */
	public List<Banner> getBannerList(Long id); 
	
	/**
	 * @Title: getSecondCatalogCourseList 
	 * @Description: (查询分类下课程列表) 
	 * @param params
	 * @return
	 */
	public List<SecondCatalogCourseList> getSecondCatalogCourseList(Long id); 
	/**
	 * @Title: addStudyNumber 
	 * @Description: (修改课程评论点赞) 
	 * @param courseId
	 * @return
	 */
	public int addStudyNumber(Long courseId);
	
	/**
	 * @Title: updateStudyReportById 
	 * @Description: (修改课程评论点赞) 
	 * @param courseId
	 * @return
	 */
	public int updateStudyReportById(Map<String,Object> params);
	
	/**
	 * @Title: addStudyDetail 
	 * @Description: (添加学员学习详细记录) 
	 * @param courseId
	 * @return
	 */
	public int addStudyDetail(Map<String,Object> params);
	/**
	 * @Title: addStudyReport 
	 * @Description: (添加学员学习时间记录) 
	 * @param courseId
	 * @return
	 */
	public int addStudyReport(Map<String,Object> params);
	//保存Android调用接口记录
	public int addStudyDetailHistory(Map<String,Object> params);
	/**
	 * @Title: findStudyDetail 
	 * @Description: (查询学员是否已经完成过学习) 
	 * @param courseId
	 * @return
	 */
	public List<StudyDetail>  findStudyDetail(Map<String,Object> params);
	
	/**
	 * @Title: findStudyDetail 
	 * @Description: (查询学员是否已经完成过学习) 
	 * @param courseId
	 * @return
	 */
	public List<StudyReport>  findStudyReport(Map<String,Object> params);
	
	/**
	 * @Title: updateStudyPlan 
	 * @Description: (更新学员学习计划) 
	 * @param courseId
	 * @return
	 */
	public int updateStudyPlan(Map<String,Object> params);
	/**
	 * @Title: updateStudyPlanCatalog 
	 * @Description: (更新学员学习计划) 
	 * @param courseId
	 * @return
	 */
	public int updateStudyPlanCatalog(Map<String,Object> params);
	
	/**
	 * @Title: updateStudyPlanById 
	 * @Description: (根据ID更新学员学习计划中的学习进度) 
	 * @param courseId
	 * @param employeeId
	 * @return
	 */
	public int updateStudyPlanById(Map<String,Object> params);
	/**
	 * @Title: findCatalogCourseList 
	 * @Description: (根据条件查询满足条件的课程列表) 
	 * @param params
	 * @return
	 */
	public List<CatalogCourseList> findCatalogCourseList(Map<String,Object> params);
	/**
	 * @Title: findCatalogCourseListByType 
	 * @Description: (根据条件查询满足条件的分类下的课程列表) 
	 * @param params
	 * @return
	 */
	public List<CatalogCourseList> findCatalogCourseListByType(Map<String,Object> params);
	/**
	 * @Title: findPersonCatalogCourseList 
	 * @Description: (根据条件查询满足条件的课程列表) 
	 * @param params
	 * @return
	 */
	public List<CatalogCourseList> findPersonCatalogCourseList(Map<String,Object> params);
	
	/**
	 * @Title: findDictItemList 
	 * @Description: (查询数据字典列表) 
	 * @param params
	 * @return
	 */
	public List<DictItem> findDictItemList(Map<String,Object> params); 
	
	/**
	 * @Title: saveStudyPlan 
	 * @Description: (添加学员学习计划) 
	 * @param courseId,employeeId
	 * @return
	 */
	public int saveStudyPlan(Map<String,Object> params);
	
	/**
	 * @Title: saveStudyPlanCatalog 
	 * @Description: (添加学员学习计划) 
	 * @param courseId,employeeId
	 * @return
	 */
	public int saveStudyPlanCatalog(Map<String,Object> params);
	
	/**
	 * @Title: queryStudyPlan 
	 * @Description: (查询学员计划是否已经存在) 
	 * @param courseId,employeeId
	 * @return
	 */
	public List<StudyPlan> queryStudyPlan(Map<String,Object> params);
	
	/**
	 * @Title: queryIsChoose 
	 * @Description: (查询学员是否已经将当前课程包做为选课) 
	 * @param catalogId,employeeId
	 * @return
	 */
	public List<MemberSeries> queryIsChoose(Map<String,Object> params);
	
	/**
	 * @Title: queryAnswerHistory 
	 * @Description: (查询是否已经答过试题) 
	 * @param courseId,employeeId
	 * @return
	 */
	public List<BusinessTicket> queryAnswerHistory(Map<String,Object> params);
	/**
	 * @Title: queryStudyPlanCatalog 
	 * @Description: (查询学员计划是否已经存在选择的课程包) 
	 * @param courseId,employeeId
	 * @return
	 */
	public List<StudyPlanCatalog> queryStudyPlanCatalog(Map<String,Object> params);
	
	/**
	 * @Title: queryStudyPlan 
	 * @Description: (查询学员计划是否已经存在) 
	 * @param courseId,employeeId
	 * @return
	 */
	public List<StudyPlan> queryStudyPlanExist(Map<String,Object> params);
	
	/**
	 * @Title: queryStudyPlan 
	 * @Description: (查询学员计划是否已经存在) 
	 * @param courseId,employeeId
	 * @return
	 */
	public List<StudyDetailHistory> queryStudyDetailHistory(Map<String,Object> params);
	
	/**
	 * @Title: queryStudyPlan 
	 * @Description: (查询学员计划是否已经存在) 
	 * @param courseId,employeeId
	 * @return
	 */
	public List<StudyPlanCatalog> queryStudyPlanCatalogExist(Map<String,Object> params);
	/**
	 * @Title: queryStudyPlanList 
	 * @Description: (查询学员计划学习的课程列表) 
	 * @param employeeId
	 * @return
	 */
	public List<CatalogCourseList> queryStudyPlanList(Map<String,Object> params);
	
	/**
	 * @Title: queryStudyPlanList 
	 * @Description: (查询学员计划学习的课程列表) 
	 * @param employeeId
	 * @return
	 */
	public List<Catalog> queryCollectionCatalogList(Map<String,Object> params);
	/**
	 * @Title: queryStudyPlanList 
	 * @Description: (查询学员计划学习的课程列表) 
	 * @param employeeId
	 * @return
	 */
	public List<Catalog> queryCollectionCatalogList1(Map<String,Object> params);
	
	/**
	 * @Title: queryStudyPlanListCount 
	 * @Description: (查询学员计划学习的课程列表) 
	 * @param employeeId
	 * @return
	 */
	public List<CatalogCourseList> queryStudyPlanListCount(Map<String,Object> params);
	/**
	 * @Title: saveRecommend 
	 * @Description: (添加学员学习计划) 
	 * @param courseId,employeeId
	 * @return
	 */
	public int saveRecommend(Map<String,Object> params);
	
	/**
	 * @Title: queryRecommend 
	 * @Description: (查询学员是否已推荐此课程) 
	 * @param courseId,employeeId
	 * @return
	 */
	public List<Recommend> queryRecommend(Map<String,Object> params);
	
	/**
	 * @Title: queryRecommendExist 
	 * @Description: (查询学员是否历史已推荐此课程) 
	 * @param courseId,employeeId
	 * @return
	 */
	public List<Recommend> queryRecommendExist(Map<String,Object> params);
	
	/**
	 * @Title: updateRecommend 
	 * @Description: (更新学员课程推荐学习状态) 
	 * @param courseId
	 * @param employeeId
	 * @return
	 */
	public int updateRecommend(Map<String,Object> params);
	/**
	 * @Title: findKeyWordsList 
	 * @Description: (查询关键字列表) 
	 * @param params
	 * @return
	 */
	public List<KeyWords> findKeyWordsList(Map<String,Object> params); 
	
	/**
	 * @Title: findCourseListByKeyWords 
	 * @Description: (根据关键字查询课程列表) 
	 * @param employeeId
	 * @return
	 */
	public List<CatalogCourseList> findCourseListByKeyWords(Map<String,Object> params);
	/**
	 * @Title: findTeachersList 
	 * @Description: (查询讲师列表) 
	 * @param params
	 * @return
	 */
	public List<CatalogCourseList> findTeachersList(Map<String,Object> params); 
	/**
	 * @Title: findCourseListByTeacher 
	 * @Description: (根据讲师查询课程列表) 
	 * @param params
	 * @return
	 */
	public List<CatalogCourseList> findCourseListByTeacher(Map<String,Object> params);
	/**
	 * @Title: findEmployeeDetail 
	 * @Description: (根据ID查询企业学员信息) 
	 * @param employeeId
	 * @return
	 */
	public Employee findEmployeeDetail(Map<String,Object> params);
	
	/**
	 * @Title: findEmployeeDetailByDeviceId 
	 * @Description: (根据设备ID查询企业学员信息) 
	 * @param deviceId
	 * @return
	 */
	public Employee findEmployeeDetailByDeviceId(Map<String,Object> params);
	
	/**
	 * @Title: findEmployeePlanCount 
	 * @Description: (添加学员学习计划) 
	 * @param employeeId
	 * @return
	 */
	public int findEmployeePlanCount(Map<String,Object> params);
	/**
	 * @Title: findEmployeeStudyTimeCount 
	 * @Description: (添加学员学习计划) 
	 * @param employeeId
	 * @return
	 */
	public int findEmployeeStudyTimeCount(Map<String,Object> params);
	public int findEmployeeStudyTimeCountAll(Map<String,Object> params);
	/**
	 * 改版后的查询学习时长的接口
	 * @param params
	 * @return
	 */
	public int findEmployeeStudyTimeCountNew(Map<String,Object> params);
	public int findEmployeeStudyTimeCountAllNew(Map<String,Object> params);
	/**
	 * @Title: saveBusinessFeedBack 
	 * @Description: (保存意见反馈) 
	 * @param feedback 意见反馈
	 * @return
	 */
	public int saveBusinessFeedBack(BusinessFeedBack feedback);
	/**
	 * @Title: queryStudyFinishedList 
	 * @Description: (查询学员已经完成学习的课程列表) 
	 * @param params
	 * @return
	 */
	public List<CatalogCourseList> queryStudyFinishedList(Map<String,Object> params);
	
	/**
	 * @Title: queryStudyFinishedListCount 
	 * @Description: (查询学员已经完成学习的课程总数) 
	 * @param params
	 * @return
	 */
	public List<CatalogCourseList> queryStudyFinishedListCount(Map<String,Object> params);
	/**
	 * @Title: queryStudyIfFinished 
	 * @Description: (查询学员是否已经完成学习的课程列表) 
	 * @param params
	 * @return
	 */
	public List<CatalogCourseList> queryStudyIfFinished(Map<String,Object> params);
	/**
	 * @Title: findOnlineCourseDetailById 
	 * @Description: (根据课程ID查询课程详细信息) 
	 * @param params
	 * @return
	 */
	public CatalogCourseList findOnlineCourseDetailById(Map<String,Object> params);
	

	/**
	 * @Title: addMemberPayDetail 
	 * @Description: (添加学员购买会员的详细记录) 
	 * @param params
	 * @return
	 */
	public int addMemberPayDetail(Map<String,Object> params);
	/**
	 * 单询最后一次的订单记录
	 */
	public MemberPayInfo queryMemberPayDetail(Map<String,Object> params);
	
	/**
	 * 删除匿名用户信息
	 */
	public int updateEmployeeDetailByDeviceId(Map<String,Object> params);
	

	/**
	 * 将匿名用户信息下的会员记录转移到手机号码用户信息下
	 */
	public int updateMemberPayById(Map<String,Object> params);
	/**
	 * 根据Id更新订单装态
	 */
	public int updateMemberPay(Map<String,Object> params);
	/**
	 * 查询第一次下订单的时间
	 */
	public MemberPayInfo queryMemberPayByEmployeeId(Map<String,Object> params);
	/**
	 * 查询购买会员的月份总数
	 */
	public Long queryMonthCountByEmployeeId(Map<String,Object> params);
	
	/**
	 * @Title: findFirstTypeList 
	 * @Description: (查询课程一级目录) 
	 * @param params
	 * @return
	 */
	public List<FirstType> findFirstTypeList(Map<String,Object> params); 
	
	/**
	 * @Title: findFirstTypeList 
	 * @Description: (查询课程一级目录) 
	 * @param params
	 * @return
	 */
	public List<SelfStudy> findSelfStudyList(Map<String,Object> params); 
	
	/**
	 * @Title: findSecondTypeList 
	 * @Description: (查询课程一级目录) 
	 * @param params
	 * @return
	 */
	public List<SecondType> findSecondTypeList(Map<String,Object> params); 
	
	/**
	 * @Title: addCourseComment 
	 * @Description: (添加学员课程评论) 
	 * @param 
	 * @return
	 */
	public int addCourseComment(Map<String,Object> params);
	
	
	/**
	 * @Title: findCourseCommentList 
	 * @Description: (查询课程下的评论列表) 
	 * @param params
	 * @return
	 */
	public ArrayList<Comment> findCourseCommentList(Map<String,Object> params); 
	
	/**
	 * @Title: findCourseCommentListTop3 
	 * @Description: (查询课程下的评论列表前三的评论) 
	 * @param params
	 * @return
	 */
	public ArrayList<Comment> findCourseCommentListTop3(Map<String,Object> params); 
	/**
	 * @Title: queryCommentPraise 
	 * @Description: (查询点赞操作是否已经存在) 
	 * @param commentId,employeeId
	 * @return
	 */
	public List<CommentPraise> queryCommentPraise(Map<String,Object> params);
	
	/**
	 * @Title: queryCommentPraiseExistss 
	 * @Description: (查询点赞操作是否已经存在) 
	 * @param commentId,employeeId
	 * @return
	 */
	public List<CommentPraise> queryCommentPraiseExist(Map<String,Object> params);
	/**
	 * @Title: updateCommentPraise 
	 * @Description: (更新学员学习计划) 
	 * @param praiseId
	 * @return
	 */
	public int updateCommentPraise(Map<String,Object> params);
	
	/**
	 * @Title: saveCommentPraise 
	 * @Description: (添加学员点赞记录) 
	 * @param commenstId,employeeId
	 * @return
	 */
	public int saveCommentPraise(Map<String,Object> params);
	
	/**
	 * @Title: updateCommentPraiseNum 
	 * @Description: (更新课程评论点赞数量) 
	 * @param praiseId
	 * @return
	 */
	public int updateCommentPraiseNum(Map<String,Object> params);
	
	/**
	 * @Title: findCourseListByTitle 
	 * @Description: (根据课程名称查询课程列表) 
	 * @param params
	 * @return
	 */
	public List<CatalogCourseList> findCourseListByTitle(Map<String,Object> params);
	
	/**
	 * @Title: findEmployeeStudyTimeNo 
	 * @Description: (查询学员学习时长排名) 
	 * @param employeeId
	 * @return
	 */
	public String findEmployeeStudyTimeNo(Map<String,Object> params);
	
	public String findEmployeeStudyTimeNoNew(Map<String,Object> params);
	/**
	 * @Title: findEmployeeStudyTimeNoALl 
	 * @Description: (查询学员学习时长排名) 
	 * @param employeeId
	 * @return
	 */
	public String findEmployeeStudyTimeNoALl(Map<String,Object> params);
	public String findEmployeeStudyTimeNoALlNew(Map<String,Object> params);
	
	/**
	 * @Title: findEmployeeStudyCount 
	 * @Description: (查询学员学习时长总人数) 
	 * @param employeeId
	 * @return
	 */
	public int findEmployeeStudyCount(Map<String,Object> params);
	public int findEmployeeStudyCountNew(Map<String,Object> params);
	/**
	 * @Title: findEmployeeStudyCount 
	 * @Description: (查询学员学习时长总人数) 
	 * @param employeeId
	 * @return
	 */
	public int findEmployeeStudyCountAll(Map<String,Object> params);
	public int findEmployeeStudyCountAllNew(Map<String,Object> params);
	/**
	 * @Title: findStudyTimeMax 
	 * @Description: (查询学员学习时长总数最大值) 
	 * @param employeeId
	 * @return
	 */
	public String findStudyTimeMax(Map<String,Object> params);
	public String findStudyTimeMaxNew(Map<String,Object> params);
	/**
	 * @Title: findStudyTimeMin 
	 * @Description: (查询学员学习时长总数最小值) 
	 * @param employeeId
	 * @return
	 */
	public String findStudyTimeMin(Map<String,Object> params);
	public String findStudyTimeMinNew(Map<String,Object> params);
	
	/**
	 * @Title: findStudyTimeMin 
	 * @Description: (查询学员学习时长总数最小值) 
	 * @param employeeId
	 * @return
	 */
	public String findCatalogShareAddress(Map<String,Object> params);
	
	/**
	 * @Title: findStudyTimeRankingList 
	 * @Description: (查询学习时间排名前20名的学员列表) 
	 * @param params
	 * @return
	 */
	public List<Employee> findStudyTimeRankingList(Map<String,Object> params);
	public List<Employee> findStudyTimeRankingListAll(Map<String,Object> params);
	
	
	public List<Employee> findStudyTimeRankingListNew(Map<String,Object> params);
	public List<Employee> findStudyTimeRankingListAllNew(Map<String,Object> params);
	
	/**
	 * @Title: findBusinessList 
	 * @Description: (根据名称查询企业) 
	 * @param params
	 * @return
	 */
	public List<Business> findBusinessList(Map<String,Object> params);
	public List<Gift> findGiftByCode(Map<String,Object> params);
	/**
	 * @Title: saveCompany 
	 * @Description: (保存企业信息) 
	 * @param employee 企业版App用户
	 * @return
	 */
	public int saveCompany(Business business);
	
	public int updateEmployee(Employee employee);
	public int updateEmployee1(Employee employee);
	public int updateEmployee2(Employee employee);
	
	public int updateEmployeeXL(Employee employee);
	public int updateGiftByCode(Gift gift);
	
}
