package cn.ichazuo.controller.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.ichazuo.commons.base.BaseController;
import cn.ichazuo.commons.util.NumberUtils;
import cn.ichazuo.commons.util.model.Params;
import cn.ichazuo.model.table.TeacherInvite;
import cn.ichazuo.service.TeacherService;

@RequestMapping("/app")
@Controller("app.teacherController")
public class TeacherController extends BaseController{
	@Autowired
	private TeacherService teacherService;
	
	/**
	 * @Title: findTeacherList 
	 * @Description: (查询老师列表) 
	 * @param type 类别 1:1对1服务  2:企业内训
	 * @param page 页数
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findTeacherList")
	public JSONObject findTeacherList(Long type,Integer page){
		try{
			if(NumberUtils.isNullOrZero(type)){
				return error("参数错误");
			}
			Params params = new Params(page);
			params.putData("type", type);
			
			Long count = teacherService.findTeacherCount(params);
			return ok("查询成功",teacherService.findTeacherList(params),count);
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: findTeacherText 
	 * @Description: (查询老师简介) 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findTeacherText")
	public JSONObject findTeacherText(Long id){
		try{
			if(NumberUtils.isNullOrZero(id)){
				return error("参数错误");
			}
			return ok("查询成功",teacherService.findTeacherText(id));
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: saveTeacherInvite 
	 * @Description: (保存邀请信息) 
	 * @param invite
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveTeacherInvite")
	public JSONObject saveTeacherInvite(TeacherInvite invite){
		try{
			if(invite.check()){
				teacherService.saveTeacherInvite(invite);
			}
			return ok();
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
}
