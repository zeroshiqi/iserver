package cn.ichazuo.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.ichazuo.commons.base.BaseController;
import cn.ichazuo.commons.util.NumberUtils;
import cn.ichazuo.commons.util.StringUtils;
import cn.ichazuo.commons.util.model.Page;
import cn.ichazuo.commons.util.model.Params;
import cn.ichazuo.model.table.PlayAddress;
import cn.ichazuo.service.PlayAddressService;

/**
 * @ClassName: PlayAddressController 
 * @Description: (直播地址controller) 
 * @author ZhaoXu
 * @date 2015年7月23日 下午5:19:59 
 * @version V1.0
 */
@RequestMapping("/admin")
@Controller("admin.playAddressController")
public class PlayAddressController extends BaseController{
	
	@Autowired
	private PlayAddressService playAddressService;
	
	/**
	 * @Title: address 
	 * @Description: (跳转播放地址管理) 
	 * @return
	 */
	@RequestMapping("/playaddress")
	public String address(){
		return "/playaddress/address";
	}
	
	/**
	 * @Title: findPlayAddressList 
	 * @Description: (查询播放地址列表) 
	 * @param page
	 * @param roleName
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/json/findPlayAddressList")
	public JSONObject findPlayAddressList(Page page,String roleName){
		JSONObject obj = new JSONObject();
		try{
			Params params = new Params(page.getNowPage(),page.getNumber());
			if(!StringUtils.isNullOrEmpty(roleName)){
				params.putData("roleName", super.fuzzy(roleName));
			}
			obj.put("page", page.getNowPage());
			obj.put("rows", playAddressService.findPlayAddressList(params));
			obj.put("total", playAddressService.findPlayAddressCount(params));
		}catch(Exception e){
			e.printStackTrace();
		}
		super.writeJSONString(obj);
		return obj;
	}
	
	/**
	 * @Title: savePlayAddress 
	 * @Description: (保存地址列表) 
	 * @param address
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/json/savePlayAddress")
	public JSONObject savePlayAddress(PlayAddress address){
		try{
			if(StringUtils.isNullOrEmpty(address.getAddressName())){
				return warning("请填写播放地址名称!");
			}
			if(StringUtils.isNullOrEmpty(address.getAddressUrl())){
				return warning("请填写播放地址URL!");
			}
			if(StringUtils.isNullOrEmpty(address.getAddressWebUrl())){
				return warning("请填写Web播放地址URL!");
			}
			if(NumberUtils.isNullOrZero(address.getId())){
				if(playAddressService.savePlayAddredd(address)){
					return ok("保存成功");
				}
			}else{
				PlayAddress temp = playAddressService.findPlayAddressById(address.getId());
				address.setVersion(temp.getVersion());
				if(playAddressService.updatePlayAddress(address)){
					return ok("保存成功");
				}
			}
			return error("保存失败");
		}catch(Exception e){
			e.printStackTrace();
			return error(ADMIN_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: deletePlayAddress 
	 * @Description: (删除播放地址) 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/json/deletePlayAddress")
	public JSONObject deletePlayAddress(Long id){
		try{
			if(NumberUtils.isNullOrZero(id)){
				return error("参数缺失");
			}
			PlayAddress address = playAddressService.findPlayAddressById(id);
			if(address == null){
				return error("参数错误");
			}
			address.setStatus(0);
			if(playAddressService.updatePlayAddress(address)){
				return ok("删除成功");
			}
			return error("删除失败");
		}catch(Exception e){
			e.printStackTrace();
			return error(ADMIN_SYSTEM_ERROR);
		}
	}
	
	
	/**
	 * @Title: findAllPlayAddress 
	 * @Description: (查询全部播放地址) 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/json/findAllPlayAddress")
	public JSONArray findAllPlayAddress(){
		JSONArray arr = new JSONArray();
		try{
			arr.addAll(playAddressService.findAllPlayAddress());
		}catch(Exception e){
			e.printStackTrace();
		}
		return arr;
	}
	
}
