package cn.ichazuo.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;

import cn.ichazuo.commons.base.BaseController;
import cn.ichazuo.service.CityService;

/**
 * @ClassName: CityController 
 * @Description: (城市controller) 
 * @author ZhaoXu
 * @date 2015年7月22日 下午12:42:28 
 * @version V1.0
 */
@RequestMapping("/admin")
@Controller("admin.cityController")
public class CityController extends BaseController{
	
	@Autowired
	private CityService cityService;
	
	/**
	 * @Title: findAllCity 
	 * @Description: (查询全部城市) 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/json/findAllCity")
	public JSONArray findAllCity(){
		JSONArray arr = new JSONArray();
		arr.addAll(cityService.findAllCity());
		super.writeJSONString(arr);
		return arr;
	}
}
