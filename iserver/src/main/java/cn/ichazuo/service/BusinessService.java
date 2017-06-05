package cn.ichazuo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ichazuo.commons.base.BaseService;
import cn.ichazuo.commons.component.CacheInfo;
import cn.ichazuo.commons.component.ConfigInfo;
import cn.ichazuo.commons.component.RedisClient;
import cn.ichazuo.commons.util.model.Page;
import cn.ichazuo.commons.util.model.Params;
import cn.ichazuo.dao.CommonDao;
import cn.ichazuo.model.app.BusinessFeedBack;
import cn.ichazuo.model.app.CatalogCourseList;
import cn.ichazuo.model.app.Comment;
import cn.ichazuo.model.app.CommentPraise;
import cn.ichazuo.model.app.DictItem;
import cn.ichazuo.model.app.FirstType;
import cn.ichazuo.model.app.KeyWords;
import cn.ichazuo.model.app.MemberPayInfo;
import cn.ichazuo.model.app.MemberSeries;
import cn.ichazuo.model.app.Recommend;
import cn.ichazuo.model.app.SecondCatalogCourseList;
import cn.ichazuo.model.app.SecondType;
import cn.ichazuo.model.app.SelfStudy;
import cn.ichazuo.model.app.StudyDetail;
import cn.ichazuo.model.app.StudyDetailHistory;
import cn.ichazuo.model.app.StudyPlan;
import cn.ichazuo.model.app.StudyPlanCatalog;
import cn.ichazuo.model.app.StudyReport;
import cn.ichazuo.model.table.Banner;
import cn.ichazuo.model.table.Business;
import cn.ichazuo.model.table.BusinessTicket;
import cn.ichazuo.model.table.Catalog;
import cn.ichazuo.model.table.Employee;
import cn.ichazuo.model.table.Gift;
import cn.ichazuo.model.table.MemberRecord;
import cn.ichazuo.model.table.RecommendInfo;
import cn.ichazuo.model.table.SecondCatalog;
import cn.ichazuo.model.table.Student;
import cn.ichazuo.model.table.Teacher;
import cn.ichazuo.model.table.WebCourseOrder;

/**
 * @ClassName: BusinessService
 * @Description: (通用Service)
 * @author ZhaoXu
 * @date 2015年7月18日 下午10:13:58
 * @version V1.0
 */
@Service("businessService")
public class BusinessService extends BaseService {
	private static final long serialVersionUID = 1L;
	@Resource
	private CommonDao commonDao;
	@Autowired
	private CodeService codeService;
	@Autowired
	private ConfigInfo configInfo;
	@Autowired
	private RedisClient redisClient;
	@Autowired
	private CacheInfo cacheInfo;

	/**
	 * 查询课程分类列表
	 * @param 当前查询页数 page
	 * @return
	 */
	public List<Catalog> findCatalogList(Page page){
		Params par = new Params(page.getNowPage());
		List<Catalog> infos = commonDao.findCatalogList(par.getMap());
        // 截取时间字符串
//		infos.forEach(info -> {
//			if(info.getCreateAt() != "" && !("").equals(info.getCreateAt())){
//				info.setCreateAt(info.getCreateAt().substring(0, 19));;
//			}
//			if(info.getUpdateAt() != "" && !("").equals(info.getUpdateAt())){
//				info.setUpdateAt(info.getUpdateAt().substring(0, 19));;
//			}		
//			});
		return infos;
	}
	public List<Catalog> findCatalogListCount(){
		Params par = new Params();
		List<Catalog> infos = commonDao.findCatalogListCount(par.getMap());
        // 截取时间字符串
//		infos.forEach(info -> {
//			if(info.getCreateAt() != "" && !("").equals(info.getCreateAt())){
//				info.setCreateAt(info.getCreateAt().substring(0, 19));;
//			}
//			if(info.getUpdateAt() != "" && !("").equals(info.getUpdateAt())){
//				info.setUpdateAt(info.getUpdateAt().substring(0, 19));;
//			}		
//			});
		return infos;
	}
	/**
	 * 查询讲师列表
	 * @param 当前查询页数 page
	 * @return
	 */
	public List<Teacher> findTeacherList(Page page){
		Params par = new Params(page.getNowPage());
		List<Teacher> infos = commonDao.findTeacherList(par.getMap());
		return infos;
	}
	
	/**
	 * 查询讲师列表
	 * @param 当前查询页数 page
	 * @return
	 */
	public List<Teacher> findAppTeacherList(Page page){
		Params par = new Params(page.getNowPage());
		List<Teacher> infos = commonDao.findAppTeacherList(par.getMap());
		return infos;
	}
	
	/**
	 * 查询讲师列表
	 * @param 当前查询页数 page
	 * @return
	 */
	public List<Teacher> getTeacherListForWeb(Page page){
		Params par = new Params(page.getNowPage());
		List<Teacher> infos = commonDao.getTeacherListForWeb(par.getMap());
		return infos;
	}
	/**
	 * 查询讲师列表
	 * @param 当前查询页数 page
	 * @return
	 */
	public List<Teacher> getTeacherListForDake(Page page){
		Params par = new Params(page.getNowPage());
		List<Teacher> infos = commonDao.getTeacherListForDaKe(par.getMap());
		return infos;
	}
	/**
	 * 查询讲师列表
	 * @param 当前查询页数 page
	 * @return
	 */
	public List<Teacher> queryTeacherList(Page page){
		Params par = new Params(page.getNowPage());
		List<Teacher> infos = commonDao.queryTeacherList(par.getMap());
		return infos;
	}
	
	/**
	 * 查询讲师列表
	 * @param 当前查询页数 page
	 * @return
	 */
	public List<Student> findStudentList(Page page){
		Params par = new Params(page.getNowPage());
		List<Student> infos = commonDao.findStudentList(par.getMap());
		return infos;
	}
	/**
	 * 查询首页最新课程分类列表
	 * @param 当前查询页数 page
	 * @return
	 */
	public List<Catalog> findHomePageNewCatalogList(){
		Page page = new Page();
		Params par = new Params(page.getNowPage());
		List<Catalog> infos = commonDao.findHomePageNewCatalogList(par.getMap());
        // 截取时间字符串
//		infos.forEach(info -> {
//			if(info.getCreateAt() != "" && !("").equals(info.getCreateAt())){
//				info.setCreateAt(info.getCreateAt().substring(0, 19));;
//			}
//			if(info.getUpdateAt() != "" && !("").equals(info.getUpdateAt())){
//				info.setUpdateAt(info.getUpdateAt().substring(0, 19));;
//			}		
//			});
		return infos;
	}
	
	/**
	 * 查询首页最新课程分类列表
	 * @param 当前查询页数 page
	 * @return
	 */
	public List<Catalog> findHotCatalogList(Long id){
		List<Catalog> infos = commonDao.findHotCatalogList(id);
        // 截取时间字符串
//		infos.forEach(info -> {
//			if(info.getCreateAt() != "" && !("").equals(info.getCreateAt())){
//				info.setCreateAt(info.getCreateAt().substring(0, 19));;
//			}
//			if(info.getUpdateAt() != "" && !("").equals(info.getUpdateAt())){
//				info.setUpdateAt(info.getUpdateAt().substring(0, 19));;
//			}		
//			});
		return infos;
	}
	/**
	 * 查询课程一级分类下课程总数
	 * @param 当前查询页数 page
	 * @return
	 */
	public int findCatalogListCourseCount(Long catalogId){
		Params params = new Params();
		params.putData("catalogId", catalogId);
        // 截取时间字符串
//		infos.forEach(info -> {
//			if(info.getCreateAt() != "" && !("").equals(info.getCreateAt())){
//				info.setCreateAt(info.getCreateAt().substring(0, 19));;
//			}
//			if(info.getUpdateAt() != "" && !("").equals(info.getUpdateAt())){
//				info.setUpdateAt(info.getUpdateAt().substring(0, 19));;
//			}		
//			});
		return commonDao.findCatalogListCourseCount(params.getMap());
	}
	
	/**
	 * 查询课程一级分类下课程总数
	 * @param 当前查询页数 page
	 * @return
	 */
	public List<Teacher> findTeacherById(Long id){
		Params params = new Params();
		params.putData("id", id);
        // 截取时间字符串
//		infos.forEach(info -> {
//			if(info.getCreateAt() != "" && !("").equals(info.getCreateAt())){
//				info.setCreateAt(info.getCreateAt().substring(0, 19));;
//			}
//			if(info.getUpdateAt() != "" && !("").equals(info.getUpdateAt())){
//				info.setUpdateAt(info.getUpdateAt().substring(0, 19));;
//			}		
//			});
		return commonDao.findTeacherById(params.getMap());
	}
	
	/**
	 * 查询课程一级分类下课程总数
	 * @param 当前查询页数 page
	 * @return
	 */
	public int findCatalogListStudyCount(Long catalogId){
		Params params = new Params();
		params.putData("catalogId", catalogId);
        // 截取时间字符串
//		infos.forEach(info -> {
//			if(info.getCreateAt() != "" && !("").equals(info.getCreateAt())){
//				info.setCreateAt(info.getCreateAt().substring(0, 19));;
//			}
//			if(info.getUpdateAt() != "" && !("").equals(info.getUpdateAt())){
//				info.setUpdateAt(info.getUpdateAt().substring(0, 19));;
//			}		
//			});
		return commonDao.findCatalogListStudyCount(params.getMap());
	}
	/**
	 * 查询课程分类列表
	 * @param 当前查询页数 page
	 * @return
	 */
	public List<Catalog> findParentCatalogList(Page page){
		Params par = new Params(page.getNowPage());
		List<Catalog> infos = commonDao.findParentCatalogList(par.getMap());
        // 截取时间字符串
//		infos.forEach(info -> {
//			if(info.getCreateAt() != "" && !("").equals(info.getCreateAt())){
//				info.setCreateAt(info.getCreateAt().substring(0, 19));;
//			}
//			if(info.getUpdateAt() != "" && !("").equals(info.getUpdateAt())){
//				info.setUpdateAt(info.getUpdateAt().substring(0, 19));;
//			}		
//			});
		return infos;
	}
	/**
	 * 查询课程分类列表
	 * @param 当前查询页数 page
	 * @return
	 */
	public List<Catalog> findBuyParentCatalogList(Long employeeId){
		Params par = new Params();
		par.putData("employeeId", employeeId);
		List<Catalog> infos = commonDao.findBuyParentCatalogList(par.getMap());
        // 截取时间字符串
//		infos.forEach(info -> {
//			if(info.getCreateAt() != "" && !("").equals(info.getCreateAt())){
//				info.setCreateAt(info.getCreateAt().substring(0, 19));;
//			}
//			if(info.getUpdateAt() != "" && !("").equals(info.getUpdateAt())){
//				info.setUpdateAt(info.getUpdateAt().substring(0, 19));;
//			}		
//			});
		return infos;
	}
	/**
	 * 查询课程分类列表
	 * @param 当前查询页数 page
	 * @return
	 */
	public List<Catalog> findOtherParentCatalogList(Long employeeId){
		Params par = new Params();
		par.putData("employeeId", employeeId);
		List<Catalog> infos = commonDao.findOtherParentCatalogList(par.getMap());
        // 截取时间字符串
//		infos.forEach(info -> {
//			if(info.getCreateAt() != "" && !("").equals(info.getCreateAt())){
//				info.setCreateAt(info.getCreateAt().substring(0, 19));;
//			}
//			if(info.getUpdateAt() != "" && !("").equals(info.getUpdateAt())){
//				info.setUpdateAt(info.getUpdateAt().substring(0, 19));;
//			}		
//			});
		return infos;
	}
	/**
	 * 查询课程分类列表
	 * @param 当前查询页数 page
	 * @return
	 */
	public List<Catalog> findParentVideoCatalogList(Page page){
		Params par = new Params(page.getNowPage());
		List<Catalog> infos = commonDao.findParentVideoCatalogList(par.getMap());
        // 截取时间字符串
//		infos.forEach(info -> {
//			if(info.getCreateAt() != "" && !("").equals(info.getCreateAt())){
//				info.setCreateAt(info.getCreateAt().substring(0, 19));;
//			}
//			if(info.getUpdateAt() != "" && !("").equals(info.getUpdateAt())){
//				info.setUpdateAt(info.getUpdateAt().substring(0, 19));;
//			}		
//			});
		return infos;
	}
	public List<Catalog> findParentCatalogListCount(){
		Params par = new Params();
		List<Catalog> infos = commonDao.findParentCatalogListCount(par.getMap());
        // 截取时间字符串
//		infos.forEach(info -> {
//			if(info.getCreateAt() != "" && !("").equals(info.getCreateAt())){
//				info.setCreateAt(info.getCreateAt().substring(0, 19));;
//			}
//			if(info.getUpdateAt() != "" && !("").equals(info.getUpdateAt())){
//				info.setUpdateAt(info.getUpdateAt().substring(0, 19));;
//			}		
//			});
		return infos;
	}
	//根据学员ID查询学员是否有状态正常存在的会员记录存在
	public List<MemberRecord> findMemberRecordList(String employeeId){
		Params par = new Params();
		par.putData("employeeId", employeeId);
		List<MemberRecord> infos = commonDao.findMemberRecordList(par.getMap());
		return infos;
	}
	
	public List<Employee> queryAllEmployee(){
		List<Employee> infos = commonDao.queryAllEmployee();
		return infos;
	}
	//根据课程包Id查询课程包是否是被推荐的免费课程包
	public List<RecommendInfo> findRecommendList(Long targetId,String type){
		Params par = new Params();
		par.putData("targetId", targetId);
		par.putData("targetType", type);
		List<RecommendInfo> infos = commonDao.findRecommendList(par.getMap());
		return infos;
	}
	//根据学员ID查询学员是否有状态正常存在的系列课会员记录存在
	public List<MemberRecord> findMemberRecordListByEmployeeId(String employeeId){
		Params par = new Params();
		par.putData("employeeId", employeeId);
		List<MemberRecord> infos = commonDao.findMemberRecordListByEmployeeId(par.getMap());
		return infos;
	}
	//根据学员ID查询学员是否有状态正常存在的系列课会员记录存在
	public List<MemberRecord> findMemberRecordListByEmployeeId1(String employeeId){
		Params par = new Params();
		par.putData("employeeId", employeeId);
		List<MemberRecord> infos = commonDao.findMemberRecordListByEmployeeId1(par.getMap());
		return infos;
	}
	//根据学员ID查询学员是否有状态正常存在的会员记录存在
		public List<MemberRecord> findMemberRecordListByCatalogId(String employeeId,String catalogId){
			Params par = new Params();
			par.putData("employeeId", employeeId);
			par.putData("catalogId", catalogId);
			List<MemberRecord> infos = commonDao.findMemberRecordListByCatalogId(par.getMap());
			return infos;
		}
		
		public List<WebCourseOrder> findCourseWebOrderBymobile(String mobile){
			Params par = new Params();
			par.putData("mobile", mobile);
			List<WebCourseOrder> infos = commonDao.findCourseWebOrderBymobile(par.getMap());
			return infos;
		}
		public List<WebCourseOrder> findCourseAppOrderByEmployeeId(Long employeeId){
			Params par = new Params();
			par.putData("employeeId", employeeId);
			List<WebCourseOrder> infos = commonDao.findCourseAppOrderByEmployeeId(par.getMap());
			return infos;
		}
		public List<WebCourseOrder> findCourseAppOrderByEmployeeId1(Long employeeId){
			Params par = new Params();
			par.putData("employeeId", employeeId);
			List<WebCourseOrder> infos = commonDao.findCourseAppOrderByEmployeeId1(par.getMap());
			return infos;
		}
	/**
	 * 查询课程分类列表
	 * @param 当前查询页数 page
	 * @return
	 */
	public List<Catalog> findParentCatalogListByTeacher(Page page,String teacherName){
		Params par = new Params(page.getNowPage());
		par.putData("teacherName", super.fuzzy(teacherName));
		List<Catalog> infos = commonDao.findParentCatalogListByTeacher(par.getMap());
        // 截取时间字符串
//		infos.forEach(info -> {
//			if(info.getCreateAt() != "" && !("").equals(info.getCreateAt())){
//				info.setCreateAt(info.getCreateAt().substring(0, 19));;
//			}
//			if(info.getUpdateAt() != "" && !("").equals(info.getUpdateAt())){
//				info.setUpdateAt(info.getUpdateAt().substring(0, 19));;
//			}		
//			});
		return infos;
	}
	/**
	 * 查询课程分类列表
	 * @param 当前查询页数 page
	 * @return
	 */
	public List<Catalog> findParentCatalogById(Long id){
		Params params = new Params();
		params.putData("id", id);
		List<Catalog> infos = commonDao.findParentCatalogListById(params.getMap());
        // 截取时间字符串
//		infos.forEach(info -> {
//			if(info.getCreateAt() != "" && !("").equals(info.getCreateAt())){
//				info.setCreateAt(info.getCreateAt().substring(0, 19));;
//			}
//			if(info.getUpdateAt() != "" && !("").equals(info.getUpdateAt())){
//				info.setUpdateAt(info.getUpdateAt().substring(0, 19));;
//			}		
//			});
		return infos;
	}
	/**
	 * 查询课程分类列表
	 * @param 当前查询页数 page
	 * @return
	 */
	public List<Catalog> findTeacherParentCatalogById(Long id){
		Params params = new Params();
		params.putData("id", id);
		List<Catalog> infos = commonDao.findTeacherParentCatalogById(params.getMap());
        // 截取时间字符串
//		infos.forEach(info -> {
//			if(info.getCreateAt() != "" && !("").equals(info.getCreateAt())){
//				info.setCreateAt(info.getCreateAt().substring(0, 19));;
//			}
//			if(info.getUpdateAt() != "" && !("").equals(info.getUpdateAt())){
//				info.setUpdateAt(info.getUpdateAt().substring(0, 19));;
//			}		
//			});
		return infos;
	}
	/**
	 * 根据一级分类Id查询课程二级分类分类列表
	 * @param 当前查询页数 page
	 * @return
	 */
	public List<SecondCatalog> findSecondCatalogList(Page page,Long parentId){
		Params par = new Params(page.getNowPage());
		par.putData("parentId", parentId);
		List<SecondCatalog> infos = commonDao.findSecondCatalogList(par.getMap());
        // 截取时间字符串
//		infos.forEach(info -> {
//			if(info.getCreateAt() != "" && !("").equals(info.getCreateAt())){
//				info.setCreateAt(info.getCreateAt().substring(0, 19));;
//			}
//			if(info.getUpdateAt() != "" && !("").equals(info.getUpdateAt())){
//				info.setUpdateAt(info.getUpdateAt().substring(0, 19));;
//			}		
//			});
		return infos;
	}
	/**
	 * 查询课程分类列表
	 * @param 当前查询页数 page
	 * @return
	 */
	public List<Catalog> findPersonCatalogList(Page page){
		Params par = new Params(page.getNowPage());
		List<Catalog> infos = commonDao.findPersonCatalogList(par.getMap());
        // 截取时间字符串
//		infos.forEach(info -> {
//			if(info.getCreateAt() != "" && !("").equals(info.getCreateAt())){
//				info.setCreateAt(info.getCreateAt().substring(0, 19));;
//			}
//			if(info.getUpdateAt() != "" && !("").equals(info.getUpdateAt())){
//				info.setUpdateAt(info.getUpdateAt().substring(0, 19));;
//			}		
//			});
		return infos;
	}
	
	/**
	 * 查询试题列表的子目录标题
	 * @param  父级标题 id
	 * @return 子标题目录
	 */
	public List<CatalogCourseList> getCatalogCourseList(Long id){
		List<CatalogCourseList> infos = commonDao.getCatalogCourseList(id);
		 // 截取时间字符串
//		infos.forEach(info -> {
//			if(info.getCreateAt() != "" && !("").equals(info.getCreateAt())){
//				info.setCreateAt(info.getCreateAt().substring(0, 19));;
//			}
//			if(info.getUpdateAt() != "" && !("").equals(info.getUpdateAt())){
//				info.setUpdateAt(info.getUpdateAt().substring(0, 19));;
//			}		
//			});
		return infos;
	}
	
	/**
	 * 查询试题列表的子目录标题
	 * @param  父级标题 id
	 * @return 子标题目录
	 */
	public List<CatalogCourseList> getCatalogCourseListByBuyDate(String catalogId,String time,String courseIds){
		Params par = new Params();
		par.putData("catalogId", catalogId);
		par.putData("time", time);
		par.putData("courseIds", courseIds);
		List<CatalogCourseList> infos = commonDao.getCatalogCourseListByBuyDate(par.getMap());
		return infos;
	}
	/**
	 * 查询试题列表的子目录标题
	 * @param  父级标题 id
	 * @return 子标题目录
	 */
	public List<CatalogCourseList> getAllCatalogCourseList(Long catalogId,Long parentId){
		Params par = new Params();
		par.putData("catalogId", catalogId);
		if(parentId==99999){
			par.putData("parentId", null);
		}else{
			par.putData("parentId", parentId);
		}
		List<CatalogCourseList> infos = commonDao.getAllCatalogCourseList(par.getMap());
		return infos;
	}
	/**
	 * 查询试题列表的子目录标题
	 * @param  父级标题 id
	 * @return 子标题目录
	 */
	public List<CatalogCourseList> getRecommendCourseList(Long id,String resulta,String resultb){
		Params params = new Params();
		params.putData("resulta", resulta);
		params.putData("resultb", resultb);
		List<CatalogCourseList> infos = commonDao.getRecommendCourseList(params.getMap());
		 // 截取时间字符串
//		infos.forEach(info -> {
//			if(info.getCreateAt() != "" && !("").equals(info.getCreateAt())){
//				info.setCreateAt(info.getCreateAt().substring(0, 19));;
//			}
//			if(info.getUpdateAt() != "" && !("").equals(info.getUpdateAt())){
//				info.setUpdateAt(info.getUpdateAt().substring(0, 19));;
//			}		
//			});
		return infos;
	}
	
	/**
	 * 查询试题列表的子目录标题
	 * @param  父级标题 id
	 * @return 子标题目录
	 */
	public List<StudyDetail> getStudyType(Long id){
		List<StudyDetail> infos = commonDao.getStudyType(id);
		 // 截取时间字符串
//		infos.forEach(info -> {
//			if(info.getCreateAt() != "" && !("").equals(info.getCreateAt())){
//				info.setCreateAt(info.getCreateAt().substring(0, 19));;
//			}
//			if(info.getUpdateAt() != "" && !("").equals(info.getUpdateAt())){
//				info.setUpdateAt(info.getUpdateAt().substring(0, 19));;
//			}		
//			});
		return infos;
	}
	
	/**
	 * 查询学员学习完成的课程id 
	 * @param  学员ID id
	 * @return 课程Id列表
	 */
	public List<StudyDetail> getStudyCourseId(Long id){
		List<StudyDetail> infos = commonDao.getStudyCourseId(id);
		 // 截取时间字符串
//		infos.forEach(info -> {
//			if(info.getCreateAt() != "" && !("").equals(info.getCreateAt())){
//				info.setCreateAt(info.getCreateAt().substring(0, 19));;
//			}
//			if(info.getUpdateAt() != "" && !("").equals(info.getUpdateAt())){
//				info.setUpdateAt(info.getUpdateAt().substring(0, 19));;
//			}		
//			});
		return infos;
	}
	/**
	 * 根据学员ID查询学员听过的课程
	 * @param  父级标题 id
	 * @return 子标题目录
	 */
	public List<StudyDetail> getHaveStudyCourseId(Long id){
		List<StudyDetail> infos = commonDao.getHaveStudyCourseId(id);
		 // 截取时间字符串
//		infos.forEach(info -> {
//			if(info.getCreateAt() != "" && !("").equals(info.getCreateAt())){
//				info.setCreateAt(info.getCreateAt().substring(0, 19));;
//			}
//			if(info.getUpdateAt() != "" && !("").equals(info.getUpdateAt())){
//				info.setUpdateAt(info.getUpdateAt().substring(0, 19));;
//			}		
//			});
		return infos;
	}
	
	/**
	 * 查询试题列表的子目录标题
	 * @param  父级标题 id
	 * @return 子标题目录
	 */
	public List<CatalogCourseList> getSurplusCourseList(Long id){
		List<CatalogCourseList> infos = commonDao.getSurplusCourseList(id);
		 // 截取时间字符串
//		infos.forEach(info -> {
//			if(info.getCreateAt() != "" && !("").equals(info.getCreateAt())){
//				info.setCreateAt(info.getCreateAt().substring(0, 19));;
//			}
//			if(info.getUpdateAt() != "" && !("").equals(info.getUpdateAt())){
//				info.setUpdateAt(info.getUpdateAt().substring(0, 19));;
//			}		
//			});
		return infos;
	}
	/**
	 * 查询首页banner列表
	 * @param  父级标题 id
	 * @return 子标题目录
	 */
	public List<Banner> getBannerList(Long id){
		List<Banner> infos = commonDao.getBannerList(id);
		 // 截取时间字符串
//		infos.forEach(info -> {
//			if(info.getCreateAt() != "" && !("").equals(info.getCreateAt())){
//				info.setCreateAt(info.getCreateAt().substring(0, 19));;
//			}
//			if(info.getUpdateAt() != "" && !("").equals(info.getUpdateAt())){
//				info.setUpdateAt(info.getUpdateAt().substring(0, 19));;
//			}		
//			});
		return infos;
	}
	
	/**
	 * 查询试题列表的子目录标题
	 * @param  父级标题 id
	 * @return 子标题目录
	 */
	public List<SecondCatalogCourseList> getSecondCatalogCourseList(Long id){
		List<SecondCatalogCourseList> infos = commonDao.getSecondCatalogCourseList(id);
		 // 截取时间字符串
//		infos.forEach(info -> {
//			if(info.getCreateAt() != "" && !("").equals(info.getCreateAt())){
//				info.setCreateAt(info.getCreateAt().substring(0, 19));;
//			}
//			if(info.getUpdateAt() != "" && !("").equals(info.getUpdateAt())){
//				info.setUpdateAt(info.getUpdateAt().substring(0, 19));;
//			}		
//			});
		return infos;
	}
	/**
	 * @Title: addStudyNumber 
	 * @Description: (更新文章评论点赞) 
	 * @param favour
	 * @return
	 */
	public boolean addStudyNumber(Long courseId){
		return commonDao.addStudyNumber(courseId) > 0;
	}
	
	/**
	 * @Title: addStudyNumber 
	 * @Description: (更新文章评论点赞) 
	 * @param favour
	 * @return
	 */
	public boolean updateStudyReportById(Long id,Long studyTime){
		Params params = new Params();
		params.putData("id", id);
		params.putData("studyTime", studyTime);
		return commonDao.updateStudyReportById(params.getMap()) > 0;
	}
	/**
	 * @Title: addStudyDetail 
	 * @Description: (添加学员学习的详细记录)
	 * @param favour
	 * @return
	 */
	public boolean addStudyDetail(Long courseId,Long employeeId,Long studyTime,Long isFinished,String status){
		Params params = new Params();
		params.putData("courseId", courseId);
		params.putData("employeeId", employeeId);
		params.putData("studyTime", studyTime);
		params.putData("isFinished", isFinished);
		params.putData("status", "1");
		return commonDao.addStudyDetail(params.getMap()) > 0;
	}
	/**
	 * @Title: addStudyReport 
	 * @Description: (添加学员学习时间到t_business_study_report表)
	 * @param favour
	 * @return
	 */
	public boolean addStudyReport(Long employeeId,Long studyTime,Long type){
		Params params = new Params();
		params.putData("employeeId", employeeId);
		params.putData("studyTime", studyTime);
		params.putData("type", type);
		return commonDao.addStudyReport(params.getMap()) > 0;
	}
	/**
	 * @Title: addStudyDetail 
	 * @Description: (添加学员学习的详细记录)
	 * @param favour
	 * @return
	 */
	public boolean addStudyDetailHistory(Long courseId,Long employeeId,Long studyTime,Long isFinished,String status,String client,String version){
		Params params = new Params();
		params.putData("courseId", courseId);
		params.putData("employeeId", employeeId);
		params.putData("studyTime", studyTime);
		params.putData("isFinished", isFinished);
		params.putData("client", client);
		params.putData("version", version);
		params.putData("status", "1");
		return commonDao.addStudyDetailHistory(params.getMap()) > 0;
	}
	/**
	 * @Title: findStudyDetail 
	 * @Description: (查询学员是否已经完成过此课程)
	 * @param favour
	 * @return
	 */
	public List<StudyDetail>  findStudyDetail(Long courseId,Long employeeId,Long isFinished){
		Params params = new Params();
		params.putData("courseId", courseId);
		params.putData("employeeId", employeeId);
		params.putData("isFinished", isFinished);
		return commonDao.findStudyDetail(params.getMap());
	}
	/**
	 * @Title: findStudyDetail 
	 * @Description: (查询学员是否已经完成过此课程)
	 * @param favour
	 * @return
	 */
	public List<StudyReport>  findStudyReport(Long employeeId,Long type){
		Params params = new Params();
		params.putData("type", type);
		params.putData("employeeId", employeeId);
		return commonDao.findStudyReport(params.getMap());
	}
	/**
	 * 查询试题列表的子目录标题
	 * @param  父级标题 id
	 * @return 子标题目录
	 */
	public List<CatalogCourseList> findCatalogCourseList(int keyWords,int timeLength,int level,Page page){
		Params params = new Params(page.getNowPage());
		int max=timeLength+10;
//		if(timeLength!=0){
//			max = timeLength+10;
//		}
		if(keyWords==999){
			params.putData("keyWords", null);
		}else{
			params.putData("keyWords", keyWords);
		}
		if(level==999){
			params.putData("level", null);
		}else{
			params.putData("level", level);
		}
//		params.putData("keyWords", keyWords);
		if(timeLength==999){
			params.putData("timeLength", null);
		}else{
			if(timeLength==5){
				params.putData("max", timeLength);
				params.putData("timeLength", 0);
			}
			if(timeLength==10){
				params.putData("max", timeLength);
				params.putData("timeLength", 5);
			}
			if(timeLength==20){
				params.putData("max", timeLength);
				params.putData("timeLength", 10);
			}
			if(timeLength==30){
				params.putData("max", 10000);
				params.putData("timeLength", 20);
			}
		}
//		params.putData("level", level);
		List<CatalogCourseList> infos = commonDao.findCatalogCourseList(params.getMap());
		// 截取时间字符串
		return infos;
	}
/**
 * 查询试题列表的子目录标题
 * @param  父级标题 id
 * @return 子标题目录
 */
public List<CatalogCourseList> findCatalogCourseListByType(int firstTypeId,int secondTypeId,int timeLength,int level,Page page){
	Params params = new Params(page.getNowPage());
	int max=timeLength+10;
//	if(timeLength!=0){
//		max = timeLength+10;
//	}
	//课程一级分类
	if(firstTypeId==999){
		params.putData("firstTypeId", null);
	}else{
		params.putData("firstTypeId", firstTypeId);
	}
	//课程二级分类
	if(secondTypeId==999){
		params.putData("secondTypeId", null);
	}else{
		params.putData("secondTypeId", secondTypeId);
	}
	//课程级别
	if(level==999){
		params.putData("level", null);
	}else{
		params.putData("level", level);
	}
//	params.putData("keyWords", keyWords);
	//课程时长
	if(timeLength==999){
		params.putData("timeLength", null);
	}else{
		if(timeLength==5){
			params.putData("max", timeLength);
			params.putData("timeLength", 0);
		}
		if(timeLength==10){
			params.putData("max", timeLength);
			params.putData("timeLength", 5);
		}
		if(timeLength==20){
			params.putData("max", timeLength);
			params.putData("timeLength", 10);
		}
		if(timeLength==30){
			params.putData("max", 10000);
			params.putData("timeLength", 20);
		}
	}
//	params.putData("level", level);
	List<CatalogCourseList> infos = commonDao.findCatalogCourseListByType(params.getMap());
	// 截取时间字符串
	return infos;
}
	
	/**
	 * 查询试题列表的子目录标题
	 * @param  父级标题 id
	 * @return 子标题目录
	 */
	public List<CatalogCourseList> findPersonCatalogCourseList(int keyWords,int timeLength,int level,Page page){
		Params params = new Params(page.getNowPage());
		int max=timeLength+10;
//		if(timeLength!=0){
//			max = timeLength+10;
//		}
		if(keyWords==999){
			params.putData("keyWords", null);
		}else{
			params.putData("keyWords", keyWords);
		}
		if(level==999){
			params.putData("level", null);
		}else{
			params.putData("level", level);
		}
//		params.putData("keyWords", keyWords);
		if(timeLength==999){
			params.putData("timeLength", null);
		}else{
			if(timeLength==5){
				params.putData("max", timeLength);
				params.putData("timeLength", 0);
			}
			if(timeLength==10){
				params.putData("max", timeLength);
				params.putData("timeLength", 5);
			}
			if(timeLength==20){
				params.putData("max", timeLength);
				params.putData("timeLength", 10);
			}
			if(timeLength==30){
				params.putData("max", 10000);
				params.putData("timeLength", 20);
			}
		}
//		params.putData("level", level);
		List<CatalogCourseList> infos = commonDao.findPersonCatalogCourseList(params.getMap());
		// 截取时间字符串
		return infos;
	}
	/**
	 * 查询数据字典
	 * @param 当前查询页数 page
	 * @return
	 */
	public List<DictItem> findDictItemList(Page page){
		Params par = new Params(page.getNowPage());
		List<DictItem> infos = commonDao.findDictItemList(par.getMap());
		return infos;
	}
	
	/**
	 * @Title: 查询学习计划是否已存在
	 * @Description: (查询学习计划是否已存在 )
	 * @param favour
	 * @return
	 */
	public List<StudyPlan> queryStudyPlan(Long courseId,Long employeeId){
		Params params = new Params();
		params.putData("courseId", courseId);
		params.putData("employeeId", employeeId);
		return commonDao.queryStudyPlan(params.getMap());
	}
	/**
	 * @Title: 查询学习计划是否已被选为选课
	 * @Description: (查询学习计划是否已被选课 )
	 * @param favour
	 * @return
	 */
	public List<MemberSeries> queryIsChoose(Long catalogId,Long employeeId){
		Params params = new Params();
		params.putData("catalogId", catalogId);
		params.putData("employeeId", employeeId);
		return commonDao.queryIsChoose(params.getMap());
	}
	
	/**
	 * @Title: 根据试卷ID和学员ID查询学员是否已经答过题
	 * @Description: (查询学习计划是否已存在 )
	 * @param favour
	 * @return
	 */
	public List<BusinessTicket> queryAnswerHistory(Long examId,Long employeeId){
		Params params = new Params();
		params.putData("examId", examId);
		params.putData("employeeId", employeeId);
		return commonDao.queryAnswerHistory(params.getMap());
	}
	/**
	 * @Title: 查询是否已收藏课程包
	 * @Description: (查询学习计划是否已存在 )
	 * @param favour
	 * @return
	 */
	public List<StudyPlanCatalog> queryStudyPlanCatalog(Long catalogId,Long employeeId){
		Params params = new Params();
		params.putData("courseId", catalogId);
		params.putData("employeeId", employeeId);
		return commonDao.queryStudyPlanCatalog(params.getMap());
	}
	/**
	 * @Title: 查询学习计划是否之前存在过
	 * @Description: (查询学习计划是否已存在 )
	 * @param favour
	 * @return
	 */
	public List<StudyPlan> queryStudyPlanExist(Long courseId,Long employeeId){
		Params params = new Params();
		params.putData("courseId", courseId);
		params.putData("employeeId", employeeId);
		return commonDao.queryStudyPlanExist(params.getMap());
	}
	/**
	 * @Title: 查询学习计划是否之前存在过
	 * @Description: (查询学习计划是否已存在 )
	 * @param favour
	 * @return
	 */
	public List<StudyDetailHistory> queryStudyDetailHistory(Long employeeId){
		Params params = new Params();
//		params.putData("courseId", courseId);
		params.putData("employeeId", employeeId);
		return commonDao.queryStudyDetailHistory(params.getMap());
	}
	
	/**
	 * @Title: 查询学习计划是否之前存在过
	 * @Description: (查询学习计划是否已存在 )
	 * @param favour
	 * @return
	 */
	public List<StudyPlanCatalog> queryStudyPlanCatalogExist(Long courseId,Long employeeId){
		Params params = new Params();
		params.putData("courseId", courseId);
		params.putData("employeeId", employeeId);
		return commonDao.queryStudyPlanCatalogExist(params.getMap());
	}
	/**
	 * @Title: 添加学习计划 
	 * @Description: (添加学习计划 )
	 * @param favour
	 * @return
	 */
	public boolean saveStudyPlan(Long courseId,Long employeeId){
		Params params = new Params();
		params.putData("courseId", courseId);
		params.putData("employeeId", employeeId);
		params.putData("status", "1");
		return commonDao.saveStudyPlan(params.getMap()) > 0;
	}
	
	/**
	 * @Title: 添加学习计划 
	 * @Description: (添加学习计划 )
	 * @param favour
	 * @return
	 */
	public boolean saveStudyPlanCatalog(Long courseId,Long employeeId){
		Params params = new Params();
		params.putData("courseId", courseId);
		params.putData("employeeId", employeeId);
		params.putData("status", "1");
		return commonDao.saveStudyPlanCatalog(params.getMap()) > 0;
	}
	
	/**
	 * @Title: 更新学习计划 
	 * @Description: (更新学习计划 )
	 * @param favour
	 * @return
	 */
	public boolean updateStudyPlan(Long studyPlanId){
		Params params = new Params();
		params.putData("id", studyPlanId);
		return commonDao.updateStudyPlan(params.getMap()) > 0;
	}
	/**
	 * @Title: 更新学习计划 
	 * @Description: (更新学习计划 )
	 * @param favour
	 * @return
	 */
	public boolean updateStudyPlanCatalog(Long studyPlanId){
		Params params = new Params();
		params.putData("id", studyPlanId);
		return commonDao.updateStudyPlanCatalog(params.getMap()) > 0;
	}
	/**
	 * @Title: 更新学习计划 
	 * @Description: (更新学习计划 )
	 * @param favour
	 * @return
	 */
	public boolean updateStudyPlan(Long studyPlanId,Long isFinished){
		Params params = new Params();
		params.putData("id", studyPlanId);
		params.putData("isFinished", isFinished);
		return commonDao.updateStudyPlanById(params.getMap()) > 0;
	}
	/**
	 * @Title: 根据学员ID查询学习计划
	 * @Description: (根据学员ID查询学习计划)
	 * @param favour
	 * @return
	 */
	public List<CatalogCourseList> queryStudyPlanList(Page page,Long employeeId){
//		Params params = new Params();
		Params par = new Params(page.getNowPage());
		par.putData("employeeId", employeeId);
		return commonDao.queryStudyPlanList(par.getMap());
	}
	/**
	 * @Title: 根据学员ID查询收藏的课程包列表
	 * @Description: (根据学员ID查询收藏的课程包列表)
	 * @param favour
	 * @return
	 */
	public List<Catalog> queryCollectionCatalogList(Page page,Long employeeId){
//		Params params = new Params();
		Params par = new Params(page.getNowPage());
		par.putData("employeeId", employeeId);
		return commonDao.queryCollectionCatalogList(par.getMap());
	}
	/**
	 * @Title: 根据学员ID查询收藏的课程包列表
	 * @Description: (根据学员ID查询收藏的课程包列表)
	 * @param favour
	 * @return
	 */
	public List<Catalog> queryCollectionCatalogList(Long employeeId){
//		Params params = new Params();
		Params par = new Params();
		par.putData("employeeId", employeeId);
		return commonDao.queryCollectionCatalogList1(par.getMap());
	}
	/**
	 * @Title: 根据学员ID查询学习计划
	 * @Description: (根据学员ID查询学习计划)
	 * @param favour
	 * @return
	 */
	public List<CatalogCourseList> queryStudyPlanList(Long employeeId){
		Params params = new Params();
//		Params par = new Params(page.getNowPage());
		params.putData("employeeId", employeeId);
		return commonDao.queryStudyPlanListCount(params.getMap());
	}
	/**
	 * @Title: 查询学习计划是否已存在
	 * @Description: (查询学习计划是否已存在 )
	 * @param favour
	 * @return
	 */
	public List<Recommend> queryRecommend(Long courseId,Long employeeId){
		Params params = new Params();
		params.putData("courseId", courseId);
		params.putData("employeeId", employeeId);
		return commonDao.queryRecommend(params.getMap());
	}
	/**
	 * @Title: 查询学习计划是否已存在
	 * @Description: (查询学习计划是否已存在 )
	 * @param favour
	 * @return
	 */
	public List<Recommend> queryRecommendExist(Long courseId,Long employeeId){
		Params params = new Params();
		params.putData("courseId", courseId);
		params.putData("employeeId", employeeId);
		return commonDao.queryRecommendExist(params.getMap());
	}
	/**
	 * @Title: 添加学习计划 
	 * @Description: (添加学习计划 )
	 * @param favour
	 * @return
	 */
	public boolean saveRecommend(Long courseId,Long employeeId){
		Params params = new Params();
		params.putData("courseId", courseId);
		params.putData("employeeId", employeeId);
		params.putData("status", "1");
		return commonDao.saveRecommend(params.getMap()) > 0;
	}
	
	/**
	 * @Title: 更新课程推荐状态
	 * @Description: (更新课程推荐状态)
	 * @param favour
	 * @return
	 */
	public boolean updateRecommend(Long recommendId){
		Params params = new Params();
		params.putData("id", recommendId);
		return commonDao.updateRecommend(params.getMap()) > 0;
	}
	
	/**
	 * 查询数据字典
	 * @param 当前查询页数 page
	 * @return
	 */
	public List<KeyWords> findKeyWordsList(Page page){
		Params par = new Params(page.getNowPage());
		List<KeyWords> infos = commonDao.findKeyWordsList(par.getMap());
		return infos;
	}
	
	/**
	 * 查询试题列表的子目录标题
	 * @param  父级标题 id
	 * @return 子标题目录
	 */
	public List<CatalogCourseList> findCourseListByKeyWords(String keyWords,Page page){
		Params params = new Params(page.getNowPage());
		params.putData("keyWords", super.fuzzy(keyWords));
		List<CatalogCourseList> infos = commonDao.findCourseListByKeyWords(params.getMap());
		// 截取时间字符串
		return infos;
	}
	/**
	 * 查询课程讲师列表
	 * @param 当前查询页数 page
	 * @return
	 */
	public List<CatalogCourseList> findTeachersList(){
		Params params = new Params();
		List<CatalogCourseList> infos = commonDao.findTeachersList(params.getMap());
		return infos;
	}
	/**
	 * 根据讲师查询课程列表
	 * @param  父级标题 id
	 * @return 子标题目录
	 */
	public List<CatalogCourseList> findCourseListByTeacher(String teacher,Page page){
		Params params = new Params(page.getNowPage());
		params.putData("teacher", teacher);
		List<CatalogCourseList> infos = commonDao.findCourseListByTeacher(params.getMap());
		// 截取时间字符串
		return infos;
	}
	/**
	 * @Title: findOnlineCourseContent 
	 * @Description: (查询线上课程简介) 
	 * @param id
	 * @return
	 */
	public Employee findEmployeeDetail(Long employeeId){
		Params params = new Params();
		params.putData("employeeId", employeeId);
		Employee infos = commonDao.findEmployeeDetail(params.getMap());
		// 截取时间字符串
		return infos;
	}
	
	/**
	 * @Title: findEmployeeDetailByDeviceId 
	 * @Description: (根据是设备号查询用户信息) 
	 * @param id
	 * @return
	 */
	public Employee findEmployeeDetailByDeviceId(String deviceId){
		Params params = new Params();
		params.putData("deviceId", deviceId);
		Employee infos = commonDao.findEmployeeDetailByDeviceId(params.getMap());
		// 截取时间字符串
		return infos;
	}
	
	/**
	 * @Title: findOnlineCourseContent 
	 * @Description: (查询线上课程简介) 
	 * @param id
	 * @return
	 */
	public int findEmployeePlanCount(Long employeeId){
		Params params = new Params();
		params.putData("employeeId", employeeId);
		int count = commonDao.findEmployeePlanCount(params.getMap());
		// 截取时间字符串
		return count;
	}
	/**
	 * @Title: findEmployeeStudyTimeCount 
	 * @Description: (查询学员学习总时长) 
	 * @param id
	 * @return
	 */
	public int findEmployeeStudyTimeCount(Long employeeId){
		Params params = new Params();
		params.putData("employeeId", employeeId);
		int count = commonDao.findEmployeeStudyTimeCount(params.getMap());
		return count;
	}
	public int findEmployeeStudyTimeCountNew(Long employeeId){
		Params params = new Params();
		params.putData("employeeId", employeeId);
		int count = commonDao.findEmployeeStudyTimeCountNew(params.getMap());
		return count;
	}
	
	public int findEmployeeStudyTimeCountAll(Long employeeId){
		Params params = new Params();
		params.putData("employeeId", employeeId);
		int count = commonDao.findEmployeeStudyTimeCountAll(params.getMap());
		return count;
	}
	public int findEmployeeStudyTimeCountAllNew(Long employeeId){
		Params params = new Params();
		params.putData("employeeId", employeeId);
		int count = commonDao.findEmployeeStudyTimeCountAllNew(params.getMap());
		return count;
	}
	/**
	 * @Title: saveBusinessFeedBack 
	 * @Description: (保存企业用户意见反馈) 
	 * @param feedBack
	 */
	public boolean saveBusinessFeedBack(BusinessFeedBack feedBack){
		return commonDao.saveBusinessFeedBack(feedBack) > 0;
	}
	/**
	 * @Title: 查询已经完成学习的课程列表
	 * @Description: (查询已经完成学习的课程列表 )
	 * @param employeeId 学员ID
	 * @return
	 */
	public List<CatalogCourseList> queryStudyFinishedList(Page page,Long employeeId,String type){
		Params params = new Params(page.getNowPage());
		params.putData("employeeId", employeeId);
		params.putData("type", type);
		return commonDao.queryStudyFinishedList(params.getMap());
	}
	/**
	 * @Title: 查询已经完成学习的课程列表
	 * @Description: (查询已经完成学习的课程列表 )
	 * @param employeeId 学员ID
	 * @return
	 */
	public List<CatalogCourseList> queryStudyFinishedList(Long employeeId){
		Params params = new Params();
		params.putData("employeeId", employeeId);
		return commonDao.queryStudyFinishedListCount(params.getMap());
	}
	/**
	 * @Title: 查询已经完成学习的课程列表
	 * @Description: (查询已经完成学习的课程列表 )
	 * @param employeeId 学员ID
	 * @return
	 */
	public List<CatalogCourseList> queryStudyIfFinished(Long courseId,Long employeeId){
		Params params = new Params();
		params.putData("courseId", courseId);
		params.putData("employeeId", employeeId);
		return commonDao.queryStudyIfFinished(params.getMap());
	}
	/**
	 * @Title: findOnlineCourseDetailById 
	 * @Description: (查询线上课程简介) 
	 * @param id
	 * @return
	 */
	public CatalogCourseList findOnlineCourseDetailById(Long id){
		Params params = new Params();
		params.putData("id", id);
		CatalogCourseList online = commonDao.findOnlineCourseDetailById(params.getMap());
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(online.getPlayStartTime());
//		calendar.add(Calendar.MINUTE, online.getTimeLength());
//		
////		Date nowTime = DateUtils.getNowDate();
//		
//		String content =courseDao.findOnlineCoursePPT(id);
//		if(online.getPlayStartTime().before(nowTime)  && calendar.getTime().after(nowTime)){
//			//直播
//			content = courseDao.findOnlineCoursePPT(id);
//			if(StringUtils.isNullOrEmpty(content)){
//				content = courseDao.findOnlineCourseContent(id);
//			}
//			
//		}else if(online.getPlayStartTime().after(nowTime)){
//			//预告
//			content = courseDao.findOnlineCourseContent(id);
//		}else{
//			//往期
//			content = courseDao.findOnlineCourseBack(id);
//			if(StringUtils.isNullOrEmpty(content)){
//				content = courseDao.findOnlineCoursePPT(id);
//			}
//			if(StringUtils.isNullOrEmpty(content)){
//				content = courseDao.findOnlineCourseContent(id);
//			}
//		}
		return online;
	}
	
	/**
	 * @Title: addMemberPayDetail 
	 * @Description: (添加会员购买的详细记录)
	 * @param favour
	 * @return
	 */
	public boolean addMemberPayDetail(Long price,Long employeeId,Long memberType){
		Params params = new Params();
		params.putData("price", price);
		params.putData("employeeId", employeeId);
		params.putData("memberType", memberType);
		if(memberType==2){
			params.putData("month", 12);
		}else if(memberType==1){
			params.putData("month", 6);
		}else{
			params.putData("month", 1);
		}
		return commonDao.addMemberPayDetail(params.getMap()) > 0;
	}
	public MemberPayInfo queryMemberPayDetail(Long employeeId){
		Params params = new Params();
		params.putData("employeeId", employeeId);
		return commonDao.queryMemberPayDetail(params.getMap());
	}
	
	/**
	 * @Title: updateEmployeeDetailByDeviceId 
	 * @Description: (删除匿名会员用户信息)
	 * @param favour
	 * @return
	 */
	public boolean updateEmployeeDetailByDeviceId(Long id){
		Params params = new Params();
		params.putData("id", id);
		return commonDao.updateEmployeeDetailByDeviceId(params.getMap()) > 0;
	}
	
	/**
	 * @Title: updateMemberPayById 
	 * @Description: (将匿名会员用户信息下的会员记录转移到手机号码用户名下)
	 * @param favour
	 * @return
	 */
	public boolean updateMemberPayById(Long employeeId,Long oldEmployeeId){
		Params params = new Params();
		params.putData("employeeId", employeeId);
		params.putData("oldEmployeeId", oldEmployeeId);
		return commonDao.updateMemberPayById(params.getMap()) > 0;
	}
	/**
	 * @Title: updateMemberPay 
	 * @Description: (更新购买会员的订单状态)
	 * @param favour
	 * @return
	 */
	public boolean updateMemberPay(Long id){
		Params params = new Params();
		params.putData("id", id);
		return commonDao.updateMemberPay(params.getMap()) > 0;
	}
	/**
	 * @Title: queryMemberPayByEmployeeId 
	 * @Description: (根据用户ID查询会员开始时间)
	 * @param favour
	 * @return
	 */
	public MemberPayInfo queryMemberPayByEmployeeId(Long employeeId){
		Params params = new Params();
		params.putData("employeeId", employeeId);
		return commonDao.queryMemberPayByEmployeeId(params.getMap());
	}
	
	/**
	 * @Title: queryMonthCountByEmployeeId 
	 * @Description: (根据用户ID查询会员开始时间)
	 * @param favour
	 * @return
	 */
	public Long queryMonthCountByEmployeeId(Long employeeId){
		Params params = new Params();
		params.putData("employeeId", employeeId);
		return commonDao.queryMonthCountByEmployeeId(params.getMap());
	}
	/**
	 * 查询数据字典
	 * @param 当前查询页数 page
	 * @return
	 */
	public List<FirstType> findFirstTypeList(Page page){
		Params par = new Params(page.getNowPage());
		List<FirstType> infos = commonDao.findFirstTypeList(par.getMap());
		return infos;
	}
	
	/**
	 * 查询数据字典
	 * @param 当前查询页数 page
	 * @return
	 */
	public List<SelfStudy> findSelfStudyList(Page page){
		Params par = new Params(page.getNowPage());
		List<SelfStudy> infos = commonDao.findSelfStudyList(par.getMap());
		return infos;
	}
	/**
	 * 查询数据字典
	 * @param 当前查询页数 page
	 * @return
	 */
	public List<SecondType> findSecondTypeList(Long parentId){
		Params par = new Params();
		par.putData("parentId", parentId);
		List<SecondType> infos = commonDao.findSecondTypeList(par.getMap());
		return infos;
	}
	
	/**
	 * @Title: addCourseComment 
	 * @Description: (添加学员课程评论)
	 * @param 
	 * @return
	 */
	public boolean addCourseComment(Long courseId,Long employeeId,String comment,String deviceId,String platform){
		Params params = new Params();
		params.putData("courseId", courseId);
		params.putData("employeeId", employeeId);
		params.putData("comment", comment);
		params.putData("deviceId", deviceId);
		params.putData("platform", platform);
		return commonDao.addCourseComment(params.getMap()) > 0;
	}
	
	/**
	 * @Title: addCourseComment 
	 * @Description: (添加学员课程评论)
	 * @param 
	 * @return
	 */
	public ArrayList<Comment> findCourseCommentList(Long courseId,Page page){
		Params params = new Params(page.getNowPage());
		params.putData("courseId", courseId);
		return commonDao.findCourseCommentList(params.getMap());
	}
	
	/**
	 * @Title: addCourseComment 
	 * @Description: (添加学员课程评论)
	 * @param 
	 * @return
	 */
	public ArrayList<Comment> findCourseCommentListTop3(Long courseId){
		Params params = new Params();
		params.putData("courseId", courseId);
		return commonDao.findCourseCommentListTop3(params.getMap());
	}
	/**
	 * @Title: 查询是否已经点赞过
	 * @Description: (查询是否已经点赞过 )
	 * @param favour
	 * @return
	 */
	public List<CommentPraise> queryCommentPraise(Long commentId,Long employeeId){
		Params params = new Params();
		params.putData("commentId", commentId);
		params.putData("employeeId", employeeId);
		return commonDao.queryCommentPraise(params.getMap());
	}
	/**
	 * @Title: 查询学习计划是否之前存在过
	 * @Description: (查询学习计划是否已存在 )
	 * @param favour
	 * @return
	 */
	public List<CommentPraise> queryCommentPraiseExist(Long commentId,Long employeeId){
		Params params = new Params();
		params.putData("commentId", commentId);
		params.putData("employeeId", employeeId);
		return commonDao.queryCommentPraiseExist(params.getMap());
	}
	/**
	 * @Title: 更新课程推荐状态
	 * @Description: (更新课程推荐状态)
	 * @param favour
	 * @return
	 */
	public boolean updateCommentPraise(Long praiseId){
		Params params = new Params();
		params.putData("id", praiseId);
		return commonDao.updateCommentPraise(params.getMap()) > 0;
	}
	
	/**
	 * @Title: 添加学习计划 
	 * @Description: (添加学习计划 )
	 * @param favour
	 * @return
	 */
	public boolean saveCommentPraise(Long commentId,Long employeeId){
		Params params = new Params();
		params.putData("commentId", commentId);
		params.putData("employeeId", employeeId);
		params.putData("status", "1");
		return commonDao.saveCommentPraise(params.getMap()) > 0;
	}
	/**
	 * @Title: 更新评论点赞数量
	 * @Description: (更新评论点赞数量)
	 * @param favour
	 * @return
	 */
	public boolean updateCommentPraiseNum(String addNum,Long commentId){
		Params params = new Params();
		params.putData("id", commentId);
		params.putData("addNum", addNum);
		return commonDao.updateCommentPraiseNum(params.getMap()) > 0;
	}
	/**
	 * 根据课程名称查询课程列表
	 * @param  父级标题 id
	 * @return 子标题目录
	 */
	public List<CatalogCourseList> findCourseListByTitle(String title,Page page){
		Params params = new Params(page.getNowPage());
		params.putData("title", super.fuzzy(title));
		List<CatalogCourseList> infos = commonDao.findCourseListByTitle(params.getMap());
		// 截取时间字符串
		return infos;
	}
	
	/**
	 * @Title: findEmployeeStudyTimeCount 
	 * @Description: (查询学员学习总时长在排名中的位置) 
	 * @param id
	 * @return
	 */
	public String findEmployeeStudyTimeNo(Long employeeId){
		Params params = new Params();
		params.putData("employeeId", employeeId);
		String count = commonDao.findEmployeeStudyTimeNo(params.getMap());
		return count;
	}
	public String findEmployeeStudyTimeNoALl(Long employeeId){
		Params params = new Params();
		params.putData("employeeId", employeeId);
		String count = commonDao.findEmployeeStudyTimeNoALl(params.getMap());
		return count;
	}
	/**
	 * 改版后的
	 * @param employeeId
	 * @return
	 */
	public String findEmployeeStudyTimeNoNew(Long employeeId){
		Params params = new Params();
		params.putData("employeeId", employeeId);
		String count = commonDao.findEmployeeStudyTimeNoNew(params.getMap());
		return count;
	}
	public String findEmployeeStudyTimeNoALlNew(Long employeeId){
		Params params = new Params();
		params.putData("employeeId", employeeId);
		String count = commonDao.findEmployeeStudyTimeNoALlNew(params.getMap());
		return count;
	}
	/**
	 * @Title: findEmployeeStudyCount 
	 * @Description: (查询学员学习时长总学习人数) 
	 * @param id
	 * @return
	 */
	public int findEmployeeStudyCount(Long employeeId){
		Params params = new Params();
		params.putData("employeeId", employeeId);
		int count = commonDao.findEmployeeStudyCount(params.getMap());
		return count;
	}
	public int findEmployeeStudyCountNew(Long employeeId){
		Params params = new Params();
		params.putData("employeeId", employeeId);
		int count = commonDao.findEmployeeStudyCountNew(params.getMap());
		return count;
	}
	/**
	 * @Title: findEmployeeStudyCount 
	 * @Description: (查询学员学习时长总学习人数) 
	 * @param id
	 * @return
	 */
	public int findEmployeeStudyCountAll(Long employeeId){
		Params params = new Params();
		params.putData("employeeId", employeeId);
		int count = commonDao.findEmployeeStudyCountAll(params.getMap());
		return count;
	}
	public int findEmployeeStudyCountAllNew(Long employeeId){
		Params params = new Params();
		params.putData("employeeId", employeeId);
		int count = commonDao.findEmployeeStudyCountAllNew(params.getMap());
		return count;
	}
	/**
	 * @Title: findStudyTimeMax 
	 * @Description: (查询学员学习时长最长的时间) 
	 * @param id
	 * @return
	 */
	public String findStudyTimeMax(Long employeeId){
		Params params = new Params();
		params.putData("employeeId", employeeId);
		String count = commonDao.findStudyTimeMax(params.getMap());
		return count;
	}
	public String findStudyTimeMaxNew(Long employeeId){
		Params params = new Params();
		params.putData("employeeId", employeeId);
		String count = commonDao.findStudyTimeMaxNew(params.getMap());
		return count;
	}
	
	/**
	 * @Title: findStudyTimeMin 
	 * @Description: (查询学员学习时长最短的时间) 
	 * @param id
	 * @return
	 */
	public String findStudyTimeMin(Long employeeId){
		Params params = new Params();
		params.putData("employeeId", employeeId);
		String count = commonDao.findStudyTimeMin(params.getMap());
		return count;
	}
	public String findStudyTimeMinNew(Long employeeId){
		Params params = new Params();
		params.putData("employeeId", employeeId);
		String count = commonDao.findStudyTimeMinNew(params.getMap());
		return count;
	}
	public String findShareAddress(String name){
		Params params = new Params();
		params.putData("name", name);
		String count = commonDao.findCatalogShareAddress(params.getMap());
		return count;
	}
	
	/**
	 * 根据讲师查询课程列表
	 * @param  父级标题 id
	 * @return 子标题目录
	 */
	public List<Employee> findStudyTimeRankingList(){
		Params params = new Params();
		params.putData("employeeId", "20");
		List<Employee> infos = commonDao.findStudyTimeRankingList(params.getMap());
		// 截取时间字符串
		return infos;
	}
	public List<Employee> findStudyTimeRankingListNew(){
		Params params = new Params();
		params.putData("employeeId", "20");
		List<Employee> infos = commonDao.findStudyTimeRankingListNew(params.getMap());
		// 截取时间字符串
		return infos;
	}
	/**
	 * 根据讲师查询课程列表
	 * @param  父级标题 id
	 * @return 子标题目录
	 */
	public List<Employee> findStudyTimeRankingListALl(){
		Params params = new Params();
		params.putData("employeeId", "20");
		List<Employee> infos = commonDao.findStudyTimeRankingListAll(params.getMap());
		// 截取时间字符串
		return infos;
	}
	public List<Employee> findStudyTimeRankingListALlNew(){
		Params params = new Params();
		params.putData("employeeId", "20");
		List<Employee> infos = commonDao.findStudyTimeRankingListAllNew(params.getMap());
		// 截取时间字符串
		return infos;
	}
	/**
	 * 查询是否存在搜索名称的企业
	 * @param 当前查询页数 page
	 * @return
	 */
	public List<Business> findBusinessByName(String name){
		Params par = new Params();
		par.putData("name", name);
		List<Business> infos = commonDao.findBusinessList(par.getMap());
		return infos;
	}
	/**
	 * 查询是否存在搜索名称的企业
	 * @param 当前查询页数 page
	 * @return
	 */
	public List<Gift> findGiftByCode(String gCode){
		Params par = new Params();
		par.putData("gCode", gCode);
		List<Gift> infos = commonDao.findGiftByCode(par.getMap());
		return infos;
	}
	/**
	 * @Title: businessRegisterEmployee 
	 * @Description: (注册企业App用户,保存用户信息) 
	 * @param employee 企业用户
	 */
	public boolean saveCompany(Business business){
		//保存用户主信息
		if(commonDao.saveCompany(business) > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * @Title: updateEmployee 
	 * @Description: (注册企业App用户,保存用户信息) 
	 * @param employee 企业用户
	 */
	public boolean updateEmployee(Employee employee){
		//保存用户主信息
		if(commonDao.updateEmployee(employee) > 0){
			return true;
		}
		return false;
	}
	/**
	 * @Title: updateEmployee 
	 * @Description: (注册企业App用户,保存用户信息) 
	 * @param employee 企业用户
	 */
	public boolean updateEmployee1(Employee employee){
		//保存用户主信息
		if(commonDao.updateEmployee1(employee) > 0){
			return true;
		}
		return false;
	}
	public boolean updateEmployee2(Employee employee){
		//保存用户主信息
		if(commonDao.updateEmployee2(employee) > 0){
			return true;
		}
		return false;
	}
	/**
	 * @Title: updateEmployee 
	 * @Description: (注册企业App用户,保存用户信息) 
	 * @param employee 企业用户
	 */
	public boolean updateEmployeeXL(Employee employee){
		//保存用户主信息
		if(commonDao.updateEmployeeXL(employee) > 0){
			return true;
		}
		return false;
	}
	/**
	 * @Title: updateEmployee 
	 * @Description: (注册企业App用户,保存用户信息) 
	 * @param employee 企业用户
	 */
	public boolean updateGiftByCode(Gift gift){
		//保存用户主信息
		if(commonDao.updateGiftByCode(gift) > 0){
			return true;
		}
		return false;
	}
	/**
     * 刷新缓存
     */
	public void refreshRedis(Long employeeId){
		try {
			PrintWriter out = null;
	        BufferedReader in = null;
	        String result = "";
	        String param="employeeId="+employeeId;
	        try {
	            URL realUrl = new URL("https://sslapi.chazuomba.com/System/setSeriesCookie");
	            // 打开和URL之间的连接
	            URLConnection conn = realUrl.openConnection();
	            // 设置通用的请求属性
	            conn.setRequestProperty("accept", "*/*");
	            conn.setRequestProperty("connection", "Keep-Alive");
	            conn.setRequestProperty("user-agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            // 发送POST请求必须设置如下两行
	            conn.setDoOutput(true);
	            conn.setDoInput(true);
	            // 获取URLConnection对象对应的输出流
	            out = new PrintWriter(conn.getOutputStream());
	            // 发送请求参数
	            out.print(param);
	            // flush输出流的缓冲
	            out.flush();
	            // 定义BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(
	                    new InputStreamReader(conn.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	        } catch (Exception e) {
	            System.out.println("发送 POST 请求出现异常！"+e);
	            e.printStackTrace();
	        }
	        //使用finally块来关闭输出流、输入流
	        finally{
	            try{
	                if(out!=null){
	                    out.close();
	                }
	                if(in!=null){
	                    in.close();
	                }
	            }
	            catch(IOException ex){
	                ex.printStackTrace();
	            }
	        }
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
