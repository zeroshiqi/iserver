package cn.ichazuo.dao;

import java.util.List;
import java.util.Map;

import cn.ichazuo.model.app.TeacherContent;
import cn.ichazuo.model.app.TeacherInfo;
import cn.ichazuo.model.table.TeacherInvite;

public interface TeacherDao {
	
	/**
	 * @Title: findTeacherList 
	 * @Description: (查询老师信息) 
	 * @param map
	 * @return
	 */
	public List<TeacherInfo> findTeacherList(Map<String,Object> map);
	
	/**
	 * @Title: findTeacherCount 
	 * @Description: (查询老师数量) 
	 * @param map
	 * @return
	 */
	public Long findTeacherCount(Map<String,Object> map);
	
	/**
	 * @Title: findTeacherText 
	 * @Description: (查询老师介绍信息) 
	 * @param id
	 * @return
	 */
	public TeacherContent findTeacherText(Long id);
	
	/**
	 * @Title: saveTeacherInvite 
	 * @Description: (保存邀请信息) 
	 * @param invite
	 * @return
	 */
	public int saveTeacherInvite(TeacherInvite invite);
}
