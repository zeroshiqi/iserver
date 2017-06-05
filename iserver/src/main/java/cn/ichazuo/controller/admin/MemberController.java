package cn.ichazuo.controller.admin;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.ichazuo.commons.base.BaseController;
import cn.ichazuo.commons.component.UploadFile;
import cn.ichazuo.commons.util.CodeUtils;
import cn.ichazuo.commons.util.NumberUtils;
import cn.ichazuo.commons.util.PasswdEncryption;
import cn.ichazuo.commons.util.StringUtils;
import cn.ichazuo.commons.util.model.Page;
import cn.ichazuo.commons.util.model.Params;
import cn.ichazuo.model.Dictionary;
import cn.ichazuo.model.table.Member;
import cn.ichazuo.model.table.MemberInfo;
import cn.ichazuo.service.CommonService;
import cn.ichazuo.service.MemberService;

/**
 * @ClassName: MemberController 
 * @Description: (注册Controller) 
 * @author ZhaoXu
 * @date 2015年7月16日 下午1:55:06 
 * @version V1.0
 */
@RequestMapping("/admin")
@Controller("admin.memberController")
public class MemberController extends BaseController{
	@Autowired
	private MemberService memberService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private UploadFile uploadFile;
	
	/**
	 * @Title: member 
	 * @Description: (跳转会员管理页面) 
	 * @return
	 */
	@RequestMapping("/member")
	public String member(){
		return "/member/member";
	}
	
	/**
	 * @Title: findMemberList 
	 * @Description: (查询注册会员列表) 
	 * @param page 分页
	 * @param mobile 手机号  
	 * @param nickname 昵称
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/json/findMemberList")
	public JSONObject findMemberList(Page page,String mobile,String nickname){
		JSONObject obj = new JSONObject();
		try{
			Params params = new Params(page.getNowPage(),page.getNumber());
			if(!StringUtils.isNullOrEmpty(mobile)){
				params.putData("mobile", super.fuzzy(mobile));
			}
			if(!StringUtils.isNullOrEmpty(nickname)){
				params.putData("nickname", super.fuzzy(nickname));
			}
			obj.put("page", page.getNowPage());
			obj.put("rows", memberService.findMemberList(params.getMap()));
			obj.put("total", memberService.findMemberListCount(params.getMap()));
		}catch(Exception e){
			e.printStackTrace();
		}
		super.writeJSONString(obj);
		return obj;
	}
	
	/**
	 * @Title: saveUserInfo 
	 * @Description: (添加用户) 
	 * @param request
	 * @param user
	 * @param info
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/json/saveUserInfo")
	public JSONObject saveUserInfo(HttpServletRequest request,Member member,MemberInfo info,Long memberId){
		try{
			if(StringUtils.isNullOrEmpty(member.getMobile())){
				return warning("请输入手机号");
			}
			if(StringUtils.isNullOrEmpty(member.getNickName())){
				return warning("请输入昵称");
			}
			//设置默认值
			if(info.getBirthday() == null){
				info.setBirthday(new Date());
			}
			if(StringUtils.isNullOrEmpty(info.getCompanyName())){
				info.setCompanyName("");
			}
			//头像上传
			MultipartFile file = null;
			try{
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				file = multipartRequest.getFile("file");
			}catch(Exception e){
				//无头像上传
			}
			//添加
			if(NumberUtils.isNullOrZero(memberId)){
				if(StringUtils.isNullOrEmpty(member.getPassword())){
					return warning("请输入密码");
				}
				//密码加密
				member.setPassword(PasswdEncryption.generate(member.getPassword()));
				if(file == null){
					member.setAvatar(commonService.getRandomAvatar());
				}else{
					member.setAvatar(uploadFile.upload(file, "avatar"));
				}
				info.setCompanyId(0L);
			}else{  //修改
				member.setPassword(null);
				if(file == null){
					member.setAvatar(null);
				}else{
					member.setAvatar(uploadFile.upload(file, "avatar"));
				}
				Member m = memberService.findMemberById(memberId);
				MemberInfo i = memberService.findMemberInfoByMemberId(memberId);
				
				member.setId(m.getId());
				member.setVersion(m.getVersion());
				info.setVersion(i.getVersion());
				info.setId(i.getId());
			}
			member.setAccessToken(CodeUtils.getUUID());
			member.setClientVersion("null");
			member.setDeviceId("null");
			if(memberService.saveMemberInfo(member, info, NumberUtils.isNullOrZero(memberId))){
				return ok("操作成功");
			}
			return error("操作失败");
		}catch(Exception e){
			e.printStackTrace();
			return error(ADMIN_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: updateMemberStatus 
	 * @Description: (修改用户状态,禁用,启用) 
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/json/updateMemberStatus")
	public JSONObject updateMemberStatus(Long userId){
		try{
			if(NumberUtils.isNullOrZero(userId)){
				return error("参数缺失");
			}
			if(memberService.updateMemberStatus(userId)){
				return ok("操作成功");
			}
			return error("操作失败");
		}catch(Exception e){
			e.printStackTrace();
			return error(ADMIN_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: findAllMember 
	 * @Description: (查询全部注册用户) 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/json/findAllMember")
	public JSONArray findAllMember(){
		JSONArray arr = new JSONArray();
		try{
			List<Dictionary> list = memberService.findAllMember();
			arr.addAll(list);
		}catch(Exception e){
			e.printStackTrace();
		}
		return arr;
	}
}
