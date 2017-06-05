package cn.ichazuo.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.ichazuo.commons.base.BaseController;
import cn.ichazuo.commons.util.model.Page;
import cn.ichazuo.commons.util.model.Params;
import cn.ichazuo.service.LogService;

/**
 * @ClassName: LogController 
 * @Description: (日志Controller) 
 * @author ZhaoXu
 * @date 2015年7月24日 下午12:56:32 
 * @version V1.0
 */
@RequestMapping("/admin")
@Controller("admin.logController")
public class LogController extends BaseController{
	
	@Autowired
	private LogService logService;
	
	/**
	 * @Title: userLog 
	 * @Description: (跳转用户操作日志) 
	 * @return
	 */
	@RequestMapping("/userLog")
	public String userLog(){
		return "/log/userlog";
	}
	
	@ResponseBody
	@RequestMapping("/json/findUserLog")
	public JSONObject findUserLog(Page page){
		JSONObject obj = new JSONObject();
		try{
			Params params = new Params(page.getNowPage(),page.getNumber());
			obj.put("page", page.getNowPage());
			obj.put("rows", logService.findUserLog(params));
			obj.put("total", logService.findUserLogCount(params));
		}catch(Exception e){
			e.printStackTrace();
		}
		super.writeJSONString(obj);
		return obj;
	}
}
