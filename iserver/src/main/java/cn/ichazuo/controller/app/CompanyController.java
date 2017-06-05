package cn.ichazuo.controller.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.ichazuo.commons.base.BaseController;
import cn.ichazuo.model.Dictionary;
import cn.ichazuo.service.CompanyService;

/**
 * @ClassName: CompanyController 
 * @Description: (公司Controller) 
 * @author ZhaoXu
 * @date 2015年7月19日 上午11:42:27 
 * @version V1.0
 */
@RequestMapping("/app")
@Controller("app.companyController")
public class CompanyController extends BaseController{
	@Autowired
	private CompanyService companyService;
	
	/**
	 * @Title: findAllCompanyName 
	 * @Description: (查询全部公司) 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findAllCompanyName")
	public JSONObject findAllCompanyName(){
		try{
			List<Dictionary> companyList = companyService.findAllCompany();
			return ok("查询成功",companyList);
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: findAllCompanyJobName 
	 * @Description: (查询全部公司职位) 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findAllCompanyJobName")
	public JSONObject findAllCompanyJobName(){
		try{
			List<Dictionary> jobList = companyService.findAllCompanyJob();
			return ok("查询成功",jobList);
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
}
