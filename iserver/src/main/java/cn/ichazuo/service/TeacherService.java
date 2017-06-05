package cn.ichazuo.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ichazuo.commons.base.BaseService;
import cn.ichazuo.commons.util.model.Params;
import cn.ichazuo.dao.TeacherDao;
import cn.ichazuo.model.app.TeacherContent;
import cn.ichazuo.model.app.TeacherInfo;
import cn.ichazuo.model.table.TeacherInvite;

@Service("teacherService")
public class TeacherService extends BaseService {
	private static final long serialVersionUID = 1L;

	@Resource
	private TeacherDao teacherDao;
	@Autowired
	private CommonService commonService;

	/**
	 * @Title: findTeacherList
	 * @Description: (查询老师列表)
	 * @param params
	 * @return
	 */
	public List<TeacherInfo> findTeacherList(Params params) {
		List<TeacherInfo> list = teacherDao.findTeacherList(params.getMap());
		list.forEach(info -> {
			info.setAvatar(commonService.appenUrl(info.getAvatar()));
		});
		return list;
	}

	/**
	 * @Title: findTeacherCount
	 * @Description: (查询老师数量)
	 * @param params
	 * @return
	 */
	public Long findTeacherCount(Params params) {
		return teacherDao.findTeacherCount(params.getMap());
	}
	
	/**
	 * @Title: findTeacherText 
	 * @Description: (查询老师简介) 
	 * @param id
	 * @return
	 */
	public TeacherContent findTeacherText(Long id){
		return teacherDao.findTeacherText(id);
	}
	
	public void saveTeacherInvite(TeacherInvite invite){
		teacherDao.saveTeacherInvite(invite);
	}

}
