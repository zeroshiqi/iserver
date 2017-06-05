package cn.ichazuo.controller.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.ichazuo.commons.base.BaseController;
import cn.ichazuo.commons.component.CacheInfo;
import cn.ichazuo.commons.component.RedisClient;
import cn.ichazuo.commons.util.NumberUtils;
import cn.ichazuo.commons.util.StringUtils;
import cn.ichazuo.commons.util.model.Page;
import cn.ichazuo.commons.util.model.Params;
import cn.ichazuo.model.table.Dict;
import cn.ichazuo.model.table.DictItem;
import cn.ichazuo.service.DictService;

/**
 * @ClassName: DictController 
 * @Description: (字典controller) 
 * @author ZhaoXu
 * @date 2015年7月16日 下午5:11:44 
 * @version V1.0
 */
@RequestMapping("/admin")
@Controller("admin.dictController")
public class DictController extends BaseController{
	@Autowired
	private DictService dictService;
	@Autowired
	private CacheInfo cacheInfo;
	@Autowired
	private RedisClient redisClient;
	
	/**
	 * @Title: dict 
	 * @Description: (跳转字典管理) 
	 * @return
	 */
	@RequestMapping("/dict")
	public String dict(){
		return "/dict/dict";
	}
	
	/**
	 * @Title: dictItem 
	 * @Description: (跳转字典项) 
	 * @return
	 */
	@RequestMapping("/dictitem")
	public String dictItem(Long dictId,HttpServletRequest request){
		request.setAttribute("dictId", dictId);
		return "/dict/dictitem";
	}
	
	/**
	 * @Title: findDictList 
	 * @Description: (查询字典列表) 
	 * @param page
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/json/findDictList")
	public JSONObject findDictList(Page page,String name){
		JSONObject obj = new JSONObject();
		try{
			Params params = new Params(page.getNowPage(),page.getNumber());
			if(!StringUtils.isNullOrEmpty(name)){
				params.putData("name", super.fuzzy(name));
			}
			obj.put("page", page.getNowPage());
			obj.put("rows", dictService.findDictList(params.getMap()));
			obj.put("total", dictService.findDictListCount(params.getMap()));
		}catch(Exception e){
			e.printStackTrace();
		}
		super.writeJSONString(obj);
		return obj;
	}
	
	/**
	 * @Title: saveDict 
	 * @Description: (保存字典) 
	 * @param dict
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/json/saveDict")
	public JSONObject saveDict(Dict dict){
		try{
			if(StringUtils.isNullOrEmpty(dict.getCode()) || StringUtils.isNullOrEmpty(dict.getDictName())){
				return error("参数缺失!");
			}
			if(dictService.findCodeCount(dict.getCode()) > 0){
				return warning("字典标识已存在!");
			}
			if(dictService.saveDict(dict)){
				return ok("保存成功");
			}
			return error("保存失败");
		}catch(Exception e){
			e.printStackTrace();
			return error(ADMIN_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: deleteDict 
	 * @Description: (删除字典) 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/json/deleteDict")
	public JSONObject deleteDict(Long id){
		try{
			if(NumberUtils.isNullOrZero(id)){
				return error("参数缺失");
			}
			Dict dict = dictService.findDictById(id);
			if(dict == null){
				return error("删除失败");
			}
			if(dictService.deleteDict(dict)){
				return ok("删除成功");
			}
			return error("删除失败");
		}catch(Exception e){
			e.printStackTrace();
			return error(ADMIN_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: findDictItemList 
	 * @Description: (查询字典项) 
	 * @param dictId
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/json/findDictItemList")
	public JSONObject findDictItemList(Long dictId,Page page){
		JSONObject obj = new JSONObject();
		try{
			if(NumberUtils.isNullOrZero(dictId)){
				return error("参数错误");
			}
			Params params = new Params(page.getNowPage(),page.getNumber());
			params.putData("dictId", dictId);
			
			obj.put("page", page.getNowPage());
			obj.put("rows", dictService.findDictItemList(params.getMap()));
			obj.put("total", dictService.findDictItemListCount(params.getMap()));
		}catch(Exception e){
			e.printStackTrace();
		}
		super.writeJSONString(obj);
		return obj;
	}
	
	/**
	 * @Title: saveDictItem 
	 * @Description: (保存字典项) 
	 * @param item
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/json/saveDictItem")
	public JSONObject saveDictItem(DictItem item){
		try{
			if(StringUtils.isNullOrEmpty(item.getValue()) || item.getWeight() == null || NumberUtils.isNullOrZero(item.getDictId())){
				return error("参数缺失");
			}
			if(dictService.saveDictItem(item)){
				
				Dict dict = dictService.findDictById(item.getDictId());
				String key = cacheInfo.getCacheDictionaryKey() + "--code--" + dict.getCode();
				super.deleteCache(redisClient, key);
				
				return ok("保存成功");
			}
			return error("保存失败");
		}catch(Exception e){
			e.printStackTrace();
			return error(ADMIN_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: deleteDictItem 
	 * @Description: (删除字典项) 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/json/deleteDictItem")
	public JSONObject deleteDictItem(Long itemId){
		try{
			if(NumberUtils.isNullOrZero(itemId)){
				return error("参数错误");
			}
			DictItem item = dictService.findDictItemById(itemId);
			if(item == null){
				return error("删除失败");
			}
			if(dictService.deleteDictItem(item)){
				
				Dict dict = dictService.findDictById(item.getDictId());
				String key = cacheInfo.getCacheDictionaryKey() + "--code--" + dict.getCode();
				super.deleteCache(redisClient, key);
				
				return ok("删除成功");
			}
			return error("删除失败");
		}catch(Exception e){
			e.printStackTrace();
			return error(ADMIN_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: findDictByCode 
	 * @Description: (根据code查询字典项) 
	 * @param code
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/json/findDictByCode")
	public JSONArray findDictByCode(String code){
		JSONArray arr = new JSONArray();
		if(StringUtils.isNullOrEmpty(code)){
			return arr;
		}
		arr.addAll(dictService.findDictItemListByCode(code));
		return arr;
	}
	
}
