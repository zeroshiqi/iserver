package cn.ichazuo.controller.app;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;

import cn.ichazuo.commons.base.BaseController;
import cn.ichazuo.commons.component.ConfigInfo;
import cn.ichazuo.commons.component.UploadFile;
import cn.ichazuo.commons.util.CodeUtils;
import cn.ichazuo.commons.util.DateUtils;
import cn.ichazuo.commons.util.NumberUtils;
import cn.ichazuo.commons.util.PasswdEncryption;
import cn.ichazuo.commons.util.StringUtils;
import cn.ichazuo.commons.util.model.Page;
import cn.ichazuo.commons.util.model.Params;
import cn.ichazuo.commons.util.model.PhoneInfo;
import cn.ichazuo.model.app.LoginInfo;
import cn.ichazuo.model.app.MemberPayInfo;
import cn.ichazuo.model.app.OfflineCourseListInfo;
import cn.ichazuo.model.app.UserAllInfo;
import cn.ichazuo.model.app.UserSimpleInfo;
import cn.ichazuo.model.log.PushLog;
import cn.ichazuo.model.table.BlockDetail;
import cn.ichazuo.model.table.Code;
import cn.ichazuo.model.table.Employee;
import cn.ichazuo.model.table.LoginDetail;
import cn.ichazuo.model.table.Member;
import cn.ichazuo.model.table.MemberFavour;
import cn.ichazuo.model.table.MemberInfo;
import cn.ichazuo.model.table.MemberRecord;
import cn.ichazuo.model.table.ThirdMember;
import cn.ichazuo.model.table.WhiteList;
import cn.ichazuo.service.BusinessService;
import cn.ichazuo.service.CodeService;
import cn.ichazuo.service.CommonService;
import cn.ichazuo.service.CommonService.ClientEnum;
import cn.ichazuo.service.CourseService;
import cn.ichazuo.service.LogService;
import cn.ichazuo.service.MemberService;

/**
 * @ClassName: MemberController
 * @Description: (用户Controller)
 * @author ZhaoXu
 * @date 2015年7月18日 下午10:53:41
 * @version V1.0
 */
@RequestMapping("/app")
@Controller("app.memberController")
public class MemberController extends BaseController {
	@Autowired
	private MemberService memberService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private BusinessService businessService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private UploadFile uploadFile;
	@Autowired
	private CourseService courseService;
	@Autowired
	private LogService logService;
	@Autowired
	private ConfigInfo configInfo;

	/**
	 * @Title: login
	 * @Description: (用户登录)
	 * @param mobile 手机号
	 * @param password 密码
	 * @param info 手机信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/login")
	public JSONObject login(String mobile, String password, PhoneInfo info) {
		try {
			if (StringUtils.isNullOrEmpty(mobile) || StringUtils.isNullOrEmpty(password)) {
				return error("参数缺失!");
			}
			Member member = memberService.findLoginMemberByMobile(mobile);
			if (member == null) {
				return error("用户不存在");
			}
			// 验证密码
			if (member.getPassword().equals(PasswdEncryption.MD5(password))
					|| PasswdEncryption.verify(password, member.getPassword())) {
				// 更新设备号 客户端版本 token信息
				member = setMemberPhoneInfo(info, member);
				member.setLoginNumber(member.getLoginNumber() + 1);
				
				memberService.updateMember(member);
			} else {
				return error("密码错误");
			}

			// 封装返回信息
			LoginInfo login = new LoginInfo();
			login.setAvatar(commonService.appenUrl(member.getAvatar()));
			login.setAccessToken(member.getAccessToken());
			login.setId(member.getId());
			login.setMobile(mobile);
			login.setNickname(login.getNickname());
			String openId = memberService.findOpenIdByMemberId(member.getId());
			login.setOpenid(StringUtils.isNullOrEmpty(openId) ? "" : openId);
			
			return ok("登录成功", login);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}

	/**
	 * @Title: register
	 * @Description: (用户注册)
	 * @param mobile
	 *            手机号
	 * @param password
	 *            密码
	 * @param code
	 *            验证码
	 * @param info
	 *            手机信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/register")
	public JSONObject register(String mobile, String password, String code, PhoneInfo info) {
		try {
			if (StringUtils.isNullOrEmpty(mobile) || StringUtils.isNullOrEmpty(password)
					|| StringUtils.isNullOrEmpty(code)) {
				return error("参数缺失");
			}
			// 验证用户是否存在
			Member member = memberService.findLoginMemberByMobile(mobile);
			if (member != null) {
				return error("手机号已存在，直接去登录吧");
			}
			Code smsCode = codeService.findCode(code, mobile);
			if (smsCode == null) {
				return error("验证码输入错误!");
			}
			member = new Member();
			member.setMobile(mobile);
			member.setPassword(PasswdEncryption.generate(password));
			if (mobile.length() > 4) {
				member.setNickName("用户" + mobile.substring(mobile.length() - 4));
			} else {
				member.setNickName("用户" + mobile);
			}
			member = setMemberPhoneInfo(info, member);
			// 保存默认头像
			member.setAvatar(commonService.getRandomAvatar());

			memberService.register(member, smsCode,null);

			// 返回登录信息
			LoginInfo login = new LoginInfo();
			login.setAccessToken(member.getAccessToken());
			login.setAvatar(commonService.appenUrl(member.getAvatar()));
			login.setId(member.getId());
			login.setNickname(member.getNickName());
			login.setMobile(mobile);
			login.setOpenid("");
			
			return ok("注册成功", login);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}

	/**
	 * @Title: updateInfo
	 * @Description: (更新用户信息)
	 * @param userId
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateInfo")
	public JSONObject updateInfo(Long userId, HttpServletRequest request) {
		try {
			if (NumberUtils.isNullOrZero(userId)) {
				return error("参数缺失");
			}
			Member member = memberService.findMemberById(userId);
			if (member == null) {
				return error("参数错误");
			}
			String nickname = request.getParameter("nickname"); // 昵称
			if (!StringUtils.isNullOrEmpty(nickname)) {
				if (!commonService.checkConetent(nickname)) {
					return error("非法词汇!");
				}
				member.setNickName(nickname);
				memberService.updateMember(member);
				
				return ok("修改成功!");
			}

			String gender = request.getParameter("gender"); // 性别
			String birthday = request.getParameter("birthday");// 生日
			// 以下两个同时传
			String companyId = request.getParameter("companyId"); // 公司ID 没有 填0
			String companyName = request.getParameter("companyName"); // 公司名称

			String jobName = request.getParameter("jobName"); // 职位名称
			String jobYear = request.getParameter("jobYear"); // 入职时间
			String coreCapacityId = request.getParameter("coreCapacityId");// 核心能力

			if ((StringUtils.isNullOrEmpty(companyId) && !StringUtils.isNullOrEmpty(companyName))
					|| (!StringUtils.isNullOrEmpty(companyId) && StringUtils.isNullOrEmpty(companyName))) {
				return error("公司信息参数缺失");
			}
			MemberInfo info = memberService.findMemberInfoByMemberId(userId);
			if (info == null) {
				return error("参数错误");
			}
			info.setGender(StringUtils.isNullOrEmpty(gender) ? info.getGender() : gender);
			info.setBirthday(StringUtils.isNullOrEmpty(birthday) ? info.getBirthday()
					: DateUtils.parseDate(birthday, DateUtils.DATE_FORMAT_NORMAL));
			info.setJobName(StringUtils.isNullOrEmpty(jobName) ? info.getJobName() : jobName);
			if (!StringUtils.isNullOrEmpty(jobYear)) {
				// 保存年份
				info.setJobYear(Integer.valueOf(jobYear));
			}
			if (!StringUtils.isNullOrEmpty(coreCapacityId) && !NumberUtils.isNullOrZero(Long.valueOf(coreCapacityId))) {
				info.setCoreCapacityId(Long.valueOf(coreCapacityId));
			}
			if (!StringUtils.isNullOrEmpty(companyId) && !NumberUtils.isNullOrZero(Long.valueOf(companyId))) {
				info.setCompanyId(Long.valueOf(companyId));
			}
			info.setCompanyName(StringUtils.isNullOrEmpty(companyName) ? info.getCompanyName() : companyName);
			
			memberService.updateMemberInfo(info);
			return ok("更新成功");
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}

	/**
	 * @Title: uploadAvatar
	 * @Description: (上传,修改头像)
	 * @param request
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/uploadAvatar")
	public JSONObject uploadAvatar(HttpServletRequest request, Long userId) {
		try {
			if (NumberUtils.isNullOrZero(userId)) {
				return error("参数缺失");
			}
			Member member = memberService.findMemberById(userId);
			if (member == null) {
				return error("参数错误");
			}
			MultipartFile file = null;
			try {
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				file = multipartRequest.getFile("file");
			} catch (Exception e) {
				return error("请上传图片");
			}

			String path = uploadFile.upload(file, "avatar");
			member.setAvatar(path);
			memberService.updateMember(member);

			return ok("修改成功", commonService.appenUrl(path));
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: findUserInfo
	 * @Description: (查询用户信息)
	 * @param userId
	 *            目标用户id
	 * @param fromId
	 *            来源用户id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findUserInfoV2")
	public JSONObject findUserInfoV2(Long userId, Long fromId) {
		try {
			if (NumberUtils.isNullOrZero(userId)) {
				return error("参数缺失");
			}
			UserAllInfo info = memberService.findMemberAllInfo(userId);
			if (info == null) {
				return error("参数错误");
			}
			if(info.getStatus() == 0){
				return warning("此用户以重置!");
			}
			
			if (!NumberUtils.isNullOrZero(fromId)) { // 是否为查看别人界面
				MemberFavour favour = memberService.findFavour(fromId, userId);
				if (favour != null && favour.getStatus() == 1) {
					info.setIsFavour(1); // 赞过
				}
			}
			// 别人赞我
			info.setFavourCount(memberService.findFavourCount(userId, true));

			// 我赞别人
			info.setApproveCount(memberService.findFavourCount(userId, false));

			List<String> teacherIds = memberService.findMyTeachers(userId);
			List<String> count = new ArrayList<>();
			teacherIds.forEach(ids -> {
				String[] idArr = ids.split(",");
				for(String id : idArr){
					if(!StringUtils.isNullOrEmpty(id)){
						if(count.contains(id)){
							continue;
						}
						count.add(id);
					}
				}
			});
			// 我的老师
			info.setTeacherCount(Long.valueOf(count.size()));
			
			// 参加课程
			info.setCourseJoinCount(courseService.findMyJoinCourseCount(userId));
			return ok("查询成功", info);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}


	/**
	 * @Title: resetPassword
	 * @Description: (重置密码)
	 * @param code 验证码
	 * @param mobile  手机号
	 * @param password  密码
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/resetPassword")
	public JSONObject resetPassword(String code, String mobile, String password) {
		try {
			if (StringUtils.isNullOrEmpty(code) || StringUtils.isNullOrEmpty(mobile)
					|| StringUtils.isNullOrEmpty(password)) {
				return error("参数缺失!");
			}
			Code smsCode = codeService.findCode(code, mobile);
			if (smsCode == null) {
				return error("验证码输入错误!");
			}
			Member member = memberService.findLoginMemberByMobile(mobile);
			if (member == null) {
				return error("参数错误!");
			}
			member.setPassword(PasswdEncryption.generate(password));
			memberService.resetPassword(smsCode, member);
			
			return ok("重置成功");
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: boundWeixin 
	 * @Description: (绑定微信) 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/boundWeixin")
	public JSONObject boundWeixin(String openid,Long userId,PhoneInfo info,String unionid){
		try{
			if(NumberUtils.isNullOrZero(userId) || StringUtils.isNullOrEmpty(openid)){
				return error("参数缺失");
			}
			Member member = memberService.findMemberById(userId);
			if(member == null){
				return error("参数错误");
			}
			// 返回登录信息
			LoginInfo login = new LoginInfo();
			login.setAccessToken(member.getAccessToken());
			login.setAvatar(commonService.appenUrl(member.getAvatar()));
			login.setId(member.getId());
			login.setNickname(member.getNickName());
			login.setMobile(member.getMobile());
			login.setOpenid(openid);

			ThirdMember third = memberService.findThirdMember(1, openid);
			if(StringUtils.isNullOrEmpty(unionid)){
				unionid = "";
			}
			if (third == null) {
				third = new ThirdMember();
				third.setMemberId(userId);
				third.setMark(1);
				third.setOpenId(openid);
				
				third.setUnionid(unionid);
				
				memberService.saveThirdMember(third);
				
				return ok("绑定成功",login);
			}else if(NumberUtils.isNullOrZero(third.getMemberId())){
				
				third.setMemberId(userId);
				third.setUnionid(unionid);
				
				memberService.updateThirdMember(third);
				return ok("绑定成功",login);
			}else{
				return warning("微信号为已存在用户，如果您强制绑定此微信号，原用户信息将会被全部删除，不能恢复。是否强制绑定？");
			}
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: forcibleBoundWeixin 
	 * @Description: (强制绑定微信) 
	 * @param openid
	 * @param userId
	 * @param info
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/forcibleBoundWeixin")
	public JSONObject forcibleBoundWeixin(String openid,Long userId,PhoneInfo info,String unionid){
		try{
			if(NumberUtils.isNullOrZero(userId) || StringUtils.isNullOrEmpty(openid)){
				return error("参数缺失");
			}
			Member member = memberService.findMemberById(userId);
			if(member == null){
				return error("参数错误");
			}
			if(StringUtils.isNullOrEmpty(unionid)){
				unionid = "";
			}
			// 返回登录信息
			LoginInfo login = new LoginInfo();
			login.setAccessToken(member.getAccessToken());
			login.setAvatar(commonService.appenUrl(member.getAvatar()));
			login.setId(member.getId());
			login.setNickname(member.getNickName());
			login.setMobile(member.getMobile());
			login.setOpenid(openid);

			ThirdMember third = memberService.findThirdMember(1, openid);
			if (third == null || NumberUtils.isNullOrZero(third.getMemberId())) {
				return error("参数错误");
			}else{
				Member deleteMember = memberService.findMemberById(third.getMemberId());
				third.setMemberId(userId);
				third.setUnionid(unionid);
				if(deleteMember == null){
					memberService.forcibleBoundWeixin(0L, third);
				}else{
					memberService.forcibleBoundWeixin(deleteMember.getId(), third);
				}
				return ok("绑定成功",login);
			}
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
		
	}
	
	/**
	 * @Title: forcibleBoundMobile 
	 * @Description: (强制绑定手机) 
	 * @param mobile 手机号
	 * @param password 密码
	 * @param userId 用户ID
	 * @param info 手机信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/forcibleBoundMobile")
	public JSONObject forcibleBoundMobile(String mobile, String password,String code, Long userId,PhoneInfo info){
		try{
			if (StringUtils.isNullOrEmpty(mobile) || StringUtils.isNullOrEmpty(password) || NumberUtils.isNullOrZero(userId)) {
				return error("参数缺失!");
			}
			Member deleteMember = memberService.findLoginMemberByMobile(mobile);
			if(deleteMember == null){
				return error("参数错误");
			}
			Code smsCode = codeService.findCode(code, mobile);
			if (smsCode == null) {
				return error("验证码输入错误!");
			}
			if(deleteMember.getId() == userId){
				return error("手机号无法替换");
			}
			
			Member boundMember = memberService.findMemberById(userId);
			if(boundMember == null){
				return error("参数错误!");
			}
			Params params = new Params();
			params.putData("memberId", userId);
			params.putData("mobile", mobile);
			params.putData("password",PasswdEncryption.generate(password));
			
			memberService.forcibleBoundMobile(deleteMember.getId(), params,smsCode);
			
			// 返回登录信息
			LoginInfo login = new LoginInfo();
			login.setAccessToken(boundMember.getAccessToken());
			login.setAvatar(commonService.appenUrl(boundMember.getAvatar()));
			login.setId(boundMember.getId());
			login.setNickname(boundMember.getNickName());
			login.setMobile(mobile);
			String openId = memberService.findOpenIdByMemberId(boundMember.getId());
			login.setOpenid(openId);

			return ok("绑定成功", login);
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: boundMobile
	 * @Description: (第三方用户绑定手机)
	 * @param mobile 手机号
	 * @param passwrod 密码
	 * @param nickname  昵称
	 * @param avatar   头像
	 * @param info   手机信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/boundMobileV2")
	public JSONObject boundMobileV2(String mobile, String password, Long userId,PhoneInfo info,String code,HttpServletRequest request) {
		try {
			if (StringUtils.isNullOrEmpty(mobile) || StringUtils.isNullOrEmpty(password) || NumberUtils.isNullOrZero(userId)) {
				return error("参数缺失!");
			}
			Code smsCode = codeService.findCode(code, mobile);
			if (smsCode == null) {
				return error("验证码输入错误!");
			}
			Member member = memberService.findLoginMemberByMobile(mobile);
			if (member != null) {
				return warning("账号已存在,是否强制绑定(强制绑定后,原账号信息会全部清除)?");
			} else {
				member = memberService.findMemberById(userId);
				if(member == null){
					return error("参数错误!");
				}
				Params params = new Params();
				params.putData("memberId", userId);
				params.putData("mobile", mobile);
				params.putData("password",PasswdEncryption.generate(password));
				
				memberService.updateMemberMobile(params,smsCode);
			}

			// 返回登录信息
			LoginInfo login = new LoginInfo();
			login.setAccessToken(member.getAccessToken());
			login.setAvatar(commonService.appenUrl(member.getAvatar()));
			login.setId(member.getId());
			login.setNickname(member.getNickName());
			login.setMobile(mobile);
			String openId = memberService.findOpenIdByMemberId(member.getId());
			login.setOpenid(openId);

			return ok("绑定成功", login);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: thirdLogin
	 * @Description: (第三方登录)
	 * @param mark 标识
	 * @param openid 第三方ID
	 * @param info 手机信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/thirdLoginV2")
	public JSONObject thirdLoginV2(Integer mark, String openid,String avatar, String nickname,PhoneInfo info,String unionid,HttpServletRequest request) {
		try {
			if (NumberUtils.isNullOrZero(mark) || StringUtils.isNullOrEmpty(openid)) {
				return error("参数缺失");
			}
			Member member = null;
			ThirdMember third = memberService.findThirdMember(mark, openid);
			boolean isHave = false;
			if (third == null || NumberUtils.isNullOrZero(third.getMemberId())) {
				if(third == null){
					// 保存第三方用户
					third = new ThirdMember();
					third.setMemberId(0L);
					third.setMark(mark);
					third.setOpenId(openid);
				}
				
				//设置unionid
				if(StringUtils.isNullOrEmpty(unionid)){
					third.setUnionid("");
				}else{
					third.setUnionid(unionid);
				}
				
				member = new Member();
				member.setMobile("");
				member.setPassword("");
				member.setCreateAt(DateUtils.getNowDate());
				member = setMemberPhoneInfo(info, member);
				if(StringUtils.isNullOrEmpty(avatar)){
					member.setAvatar(commonService.getRandomAvatar());
				}else{
					member.setAvatar(avatar);
				}
				member.setLoginNumber(1L);
				member.setNickName(nickname == null? "" : nickname);
				
				memberService.saveThirdMemberV2(third,member,NumberUtils.isNullOrZero(third.getMemberId()));
			}else{
				member = memberService.findMemberById(third.getMemberId());
				member = setMemberPhoneInfo(info, member);
				member.setLoginNumber(member.getLoginNumber() + 1);
				memberService.updateMember(member);
				
				if(!StringUtils.isNullOrEmpty(unionid) && StringUtils.isNullOrEmpty(third.getUnionid())){
					Params params = new Params();
					params.putData("id", third.getId());
					params.putData("unionid", unionid);
					
					memberService.updateThirdUnionId(params);
				}
				
				isHave = true;
			}

			// 返回登录信息
			LoginInfo login = new LoginInfo();
			login.setAccessToken(member.getAccessToken());
			login.setAvatar(commonService.appenUrl(member.getAvatar()));
			login.setId(member.getId());
			login.setNickname(member.getNickName());
			login.setMobile(member.getMobile());
			login.setOpenid(openid);
			
			if(isHave){
				return ok("登录成功", login);
			}else{
				return notFound("不存在", login);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}

	/**
	 * @Title: findMyTeacher 
	 * @Description: (查询我的老师列表) 
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findMyTeacherList")
	public JSONObject findMyTeacherList(Long userId){
		try{
			if(NumberUtils.isNullOrZero(userId)){
				return error("参数错误");
			}
			List<String> teacherIds = memberService.findMyTeachers(userId);
			List<UserSimpleInfo> userList = new ArrayList<>();
			teacherIds.forEach(ids -> {
				String[] idArr = ids.split(",");
				for(String id : idArr){
					if(!StringUtils.isNullOrEmpty(id)){
						Long teacherId = Long.valueOf(id);
						UserSimpleInfo info = memberService.findSimpleMemberInfo(teacherId);
						if(info == null){
							continue;
						}
						if(!userList.contains(info)){
							userList.add(info);
						}
					}
				}
			});
			return ok("ok",userList);
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: findUserFavourList
	 * @Description: (查询用户点赞列表)
	 * @param userId 用户ID
	 * @param type 类别 0: 我给别人点赞 1:别人给我点赞
	 * @param page 分页
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findUserFavourList")
	public JSONObject findUserFavourList(Long userId, Integer type, Page page) {
		try {
			if (NumberUtils.isNullOrZero(userId) || type == null) {
				return error("参数缺失");
			}
			Params params = new Params(page.getNowPage());
			
			if (type == 1) {
				params.putData("toMemberId", userId);
			} else {
				params.putData("fromMemberId", userId);
			}
			
			List<UserSimpleInfo> infoList = memberService.findFavourList(params);
			Integer count = memberService.findFavourCount(userId, type == 1);
			return ok("查询成功", infoList, page.getNowPage(), count);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}

	/**
	 * @Title: updateMemberFavour
	 * @Description: (用户之间点赞)
	 * @param userId
	 *            点赞ID
	 * @param otherId
	 *            被赞ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateMemberFavour")
	public JSONObject updateMemberFavour(Long userId, Long otherId) {
		try {
			if (NumberUtils.isNullOrZero(userId) || NumberUtils.isNullOrZero(otherId)) {
				return error("参数缺失");
			}
			Member fromMember = memberService.findMemberById(userId);
			if(fromMember == null){
				return error("参数错误");
			}
			MemberFavour favour = memberService.findFavour(userId, otherId);
			if (favour == null) {
				
				
				favour = new MemberFavour();
				favour.setFromMemberId(userId);
				favour.setToMemberId(otherId);
				if(memberService.saveMemberFavour(favour)){
					String msg = fromMember.getNickName() + "认可了你的核心能力";
					commonService.pushIOSMessage(ClientEnum.ChaZuo, msg,String.valueOf(otherId));
					
					PushLog log = new PushLog();
					log.setContent(msg);
					log.setType("用户认可推送");
					logService.savePushLog(log);
				}
			} else {
				favour.setStatus(favour.getStatus() == 0 ? 1 : 0);
				memberService.updateMemberFavour(favour);
				if (favour.getStatus() == 1) {
					String msg = fromMember.getNickName() + "认可了你的核心能力";
					commonService.pushIOSMessage(ClientEnum.ChaZuo, msg,String.valueOf(otherId));
					
					PushLog log = new PushLog();
					log.setContent(msg);
					log.setType("用户认可推送");
					logService.savePushLog(log);
				}
			}
			Integer count = memberService.findFavourCount(otherId, true);
			return ok("点赞成功", count);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}

	/**
	 * @Title: findMyCourseJoinList 
	 * @Description: (查询我参加的课程列表) 
	 * @param userId
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findMyCourseJoinList")
	public JSONObject findMyCourseJoinList(Long userId, Page page) {
		try {
			if (NumberUtils.isNullOrZero(userId)) {
				return error("参数缺失");
			}
			List<OfflineCourseListInfo> infoList = courseService.findMyJoinCourse(userId, page);
			infoList.forEach(info ->{
				//添加报名人数
				info.setJoinNum(courseService.findCourseJoinCount(info.getId()));
			});
			int count = courseService.findMyJoinCourseCount(userId);
			return ok("查询成功", infoList, page.getNowPage(), count);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}

	/**
	 * @Title: setMemberPhoneInfo
	 * @Description: (将手机信息放入Member对象)
	 * @param info
	 *            手机信息
	 * @param member
	 *            用户
	 * @return
	 */
	private Member setMemberPhoneInfo(PhoneInfo info, Member member) {
		member.setAccessToken(CodeUtils.getUUID());
		member.setClientVersion(info.getClientVersion());
		member.setDeviceId(info.getDeviceId());
		return member;
	}

	
	/**
	 * @Title: findUserInfo
	 * @Description: (查询用户信息)
	 * @param userId
	 *            目标用户id
	 * @param fromId
	 *            来源用户id
	 * @return
	 */
	//1.1.2版之前使用
	@Deprecated
	@ResponseBody
	@RequestMapping("/findUserInfo")
	public JSONObject findUserInfo(Long userId, Long fromId) {
		try {
			if (NumberUtils.isNullOrZero(userId)) {
				return error("参数缺失");
			}
			UserAllInfo info = memberService.findMemberAllInfo(userId);
			if (info == null) {
				return error("参数错误");
			}
			
			if (!NumberUtils.isNullOrZero(fromId)) { // 是否为查看别人界面
				MemberFavour favour = memberService.findFavour(fromId, userId);
				if (favour != null && favour.getStatus() == 1) {
					info.setIsFavour(1); // 赞过
				}
			}
			// 别人赞我
			info.setFavourCount(memberService.findFavourCount(userId, true));

			// 我赞别人
			info.setApproveCount(memberService.findFavourCount(userId, false));

			// 点赞课程
			info.setCourseCount(courseService.findMyFavourCourseCount(userId));

			// 参加课程
			info.setCourseJoinCount(courseService.findMyJoinCourseCount(userId));
			return ok("查询成功", info);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: findMyCourseFavour
	 * @Description: (查询我认同的课程信息)
	 * @param userId
	 * @param page
	 * @return
	 */
	//1.1.2前使用 
	@Deprecated
	@ResponseBody
	@RequestMapping("/findMyCourseFavour")
	public JSONObject findMyCourseFavour(Long userId, Page page) {
		try {
			if (NumberUtils.isNullOrZero(userId)) {
				return error("参数缺失");
			}
			List<OfflineCourseListInfo> infoList = courseService.findMyFavourCourse(userId, page);
			Integer count = courseService.findMyFavourCourseCount(userId);
			return ok("查询成功", infoList, page.getNowPage(), count);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: thirdLogin
	 * @Description: (第三方登录)
	 * @param mark 标识
	 * @param openid 第三方ID
	 * @param info 手机信息
	 * @return
	 */
	//1.1.2前使用
	@Deprecated
	@ResponseBody
	@RequestMapping("/thirdLogin")
	public JSONObject thirdLogin(Integer mark, String openid, PhoneInfo info,HttpServletRequest request) {
		try {
			if (NumberUtils.isNullOrZero(mark) || StringUtils.isNullOrEmpty(openid)) {
				return error("参数缺失");
			}
			ThirdMember third = memberService.findThirdMember(mark, openid);
			if (third == null) {
				// 保存第三方用户
				third = new ThirdMember();
				third.setMemberId(0L);
				third.setMark(mark);
				third.setOpenId(openid);
				third.setUnionid("");
				memberService.saveThirdMember(third);
				return notFound("用户不存在");
			} else if (NumberUtils.isNullOrZero(third.getMemberId())) {
				// 已保存第三方用户
				// 但是没绑定手机
				return notFound("用户不存在");
			}

			Member member = memberService.findMemberById(third.getMemberId());
			member = setMemberPhoneInfo(info, member);
			member.setLoginNumber(member.getLoginNumber() + 1);
			memberService.updateMember(member);

			// 返回登录信息
			LoginInfo login = new LoginInfo();
			login.setAccessToken(member.getAccessToken());
			login.setAvatar(commonService.appenUrl(member.getAvatar()));
			login.setId(member.getId());
			login.setNickname(member.getNickName());
			login.setMobile(member.getMobile());
			
			return ok("登录成功", login);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: boundMobile
	 * @Description: (第三方用户绑定手机)
	 * @param mobile 手机号
	 * @param passwrod 密码
	 * @param mark 标识
	 * @param openid  第三方id
	 * @param nickname  昵称
	 * @param avatar   头像
	 * @param info   手机信息
	 * @return
	 */
	@Deprecated
	@ResponseBody
	@RequestMapping("/boundMobile")
	public JSONObject boundMobile(String mobile, String password, Integer mark, String openid, String nickname,String avatar, PhoneInfo info,HttpServletRequest request) {
		try {
			if (StringUtils.isNullOrEmpty(mobile) || StringUtils.isNullOrEmpty(password)
					|| NumberUtils.isNullOrZero(mark) || StringUtils.isNullOrEmpty(openid)) {
				return error("参数缺失!");
			}
			ThirdMember third = memberService.findThirdMember(mark, openid);
			if (third == null) {
				return error("参数错误!");
			}
			Member member = memberService.findLoginMemberByMobile(mobile);
			boolean flag = true;
			if (member != null) {
				if (member.getStatus() == 0) {
					return error("用户被禁用!");
				}
				// 密码不正确
				if (!PasswdEncryption.verify(password, member.getPassword())
						&& !PasswdEncryption.MD5(password).equals(member.getPassword())) {
					return error("手机号已经存在，可是密码不对…");
				}
				flag = false;
			} else {
				member = new Member();
				member.setAvatar(StringUtils.isNullOrEmpty(avatar) ? commonService.getRandomAvatar() : avatar);
				member.setNickName(StringUtils.isNullOrEmpty(nickname) ? "" : nickname);
				member.setMobile(mobile);
				member.setPassword(PasswdEncryption.generate(password));
			}
			member = setMemberPhoneInfo(info, member);
			member.setLoginNumber(member.getLoginNumber() == null ? 1 : member.getLoginNumber() + 1);
			memberService.boundThird(third, member, flag);

			// 返回登录信息
			LoginInfo login = new LoginInfo();
			login.setAccessToken(member.getAccessToken());
			login.setAvatar(commonService.appenUrl(member.getAvatar()));
			login.setId(member.getId());
			login.setNickname(member.getNickName());
			login.setMobile(mobile);
			
			return ok("保存成功!", login);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: businessLogin
	 * @Description: (App企业用户登录)
	 * @param mobile 手机号
	 * @param password 密码
	 * @param info 手机信息
	 * @param deviceId 设备Id
	 * @return
	 */
	@Deprecated
	@ResponseBody
	@RequestMapping("/businessLogin")
	public JSONObject businessLogin(String mobile, String password, String platform,String client,String version,String deviceId,String loginType,String sign) {
		try {
			if (StringUtils.isNullOrEmpty(mobile) || StringUtils.isNullOrEmpty(password)) {
				return error("参数缺失!");
			}
			
 			if(("2.0.5").equals(version) || ("2.0.1").equals(version)||("2.0.4").equals(version)){
				//解密
				UpAES upAES = new UpAES();
//				upAES.entry("1");
				System.out.println("解密后的mobile："+upAES.disEntry(mobile.toString()));
				System.out.println("解密后的deviceId："+upAES.disEntry(deviceId));
				String newMobile = upAES.disEntry(mobile.toString());
				String newDeviceId =upAES.disEntry(deviceId.toString());
				//重新赋值deviceId
				deviceId =upAES.disEntry(deviceId);
				String newSign = PasswdEncryption.MD5(newMobile+newDeviceId);
				newSign= newSign.substring(5, newSign.length()-5);
				System.out.println("得到的新Sign:"+newSign);
				Employee employee = memberService.findBusinessLoginMemberByMobile1(newMobile);
				if (employee == null) {
					return status(112,"用户不存在");
				}
				System.out.println("传的sign:"+sign);
				if(newSign.equals(sign)){
					// 验证密码
					if (employee.getPassword().equals(PasswdEncryption.MD5(password))
							|| PasswdEncryption.verify(password, employee.getPassword())) {
						// 保存登录信息到登录明细表
						Date date = new Date();
						LoginDetail detail = new LoginDetail();
						detail.setEmployeeId(employee.getId().toString());
						detail.setEmployeeMobile(newMobile);
						detail.setEmployeeName(employee.getName());
						detail.setLoginTime(date);
						detail.setVersion(version);
						detail.setPlatform(platform);
						detail.setClient(client);
						if(StringUtils.isNullOrEmpty(deviceId)){
							detail.setDeviceId("1");
						}else{
							detail.setDeviceId(deviceId);
						}
						//非游客登录给上一台设备推送账号异地登录的通知
						if(employee.getId()!=33){
							if(newMobile!="15910491294" && !"15910491294".equals(newMobile) && newMobile!="15901027371" && !"15901027371".equals(newMobile) && newMobile!="18513084246" && !"18513084246".equals(newMobile) && newMobile!="17301337859" && !"17301337859".equals(newMobile) && newMobile!="00000000001" && !"00000000001".equals(newMobile) && newMobile!="00000000002" && !"00000000002".equals(newMobile) && newMobile!="00000000003" && !"00000000003".equals(newMobile)&& newMobile!="00000000004" && !"00000000004".equals(newMobile) && newMobile!="00000000005" && !"00000000005".equals(newMobile) && newMobile!="00000000006" && !"00000000006".equals(newMobile)&& newMobile!="00000000011" && newMobile!="00000000012" && newMobile!="00000000015" && !"00000000015".equals(newMobile) && !"00000000012".equals(newMobile)&& newMobile!="00000000014" && !"00000000014".equals(newMobile) && newMobile!="00000000013" && !"00000000013".equals(newMobile)&& !"00000000011".equals(newMobile)&& mobile!="00000000010" && !"00000000010".equals(newMobile)&& newMobile!="00000000009" && !"00000000009".equals(newMobile)&& newMobile!="00000000008" && !"00000000008".equals(newMobile)&& newMobile!="00000000007" && !"00000000007".equals(newMobile) && newMobile!="18301085230" && !"18301085230".equals(newMobile)&& newMobile!="18330225032" && !"18330225032".equals(newMobile)){
//								if(("1.7.6").equals(version) || ("1.7.7").equals(version)){
									//查询最近一次登录的信息
								//查询该手机号一周以内的登录设备ID
								if(!StringUtils.isNullOrEmpty(loginType) && "0".equals(loginType)){
									//查询最新一条封号记录
									List<BlockDetail> blist = memberService.queryLastBlockList(employee.getId());
									//如果存在没解封的记录 ，提示解封时间
									if(blist.size()>0){
							        	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										Calendar calendar1 = Calendar.getInstance();
										//取账号创建时间
								    	calendar1.setTime(new Date());
								    	//账号创建时间加上会员日期为会员过期日期
								    	calendar1.add(Calendar.DAY_OF_MONTH,  Integer.valueOf("30"));
								    	SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								    	System.out.println(sdf1.format(calendar1.getTime()));
								    	DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								    	Date now1 = new Date();
								    	Date dateTime4 = dateFormat1.parse(df1.format(now1));
//								    	//会员过期时间
								        Date dateTime5 = dateFormat1.parse(df1.format(calendar1.getTime()));
								        //取出封号记录的解封时间
								        Date dateTime6 = dateFormat1.parse(blist.get(0).getUnblockAt());
								        //当前时间小于解封时间 求两个时间的差值
							        	if(dateTime4.getTime()<=dateTime6.getTime()){
							        		long diff = dateTime6.getTime() - dateTime4.getTime();
									        long diffDays = diff / (24 * 60 * 60 * 1000);
									        System.out.println("day1:"+dateTime6+"   day2:"+dateTime4+ "   差值："+diffDays);
									        return status(600,"您的账号还有"+diffDays+"天解封，请耐心等待哦");
							        	}
									}
									List<LoginDetail> details = memberService.queryEmployeeCloseMsg(newMobile,deviceId);
									if(details.size()>=2){
										if(details.size()==2){
											return status(701, "您正在使用第3台设备登录同一账号，超过3台将被封号，请确认是否登录？");
										}else{
											List<BlockDetail> blockList = memberService.queryBlockList(employee.getId());
											if(blockList.size()>0){
												if(blockList.size()==1){
													return status(700,"您的登录设备数已达上限");
												}else{
													return status(700,"您的登录设备数已达上限");
												}
											}else{
												return status(700,"您的登录设备数已达上限");
											}
										}
									}else{
										//验证验证码白名单
										List<WhiteList> wl = memberService.queryWhiteListByMobile(newMobile);
										if(wl.size()<1){
											LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employee.getId());
											String otherId = "a";
											if(lastLogin != null){
												otherId = lastLogin.getDeviceId();
												//如果本次登录的设备和上一次的设备不是同一个，就给上一台设备发送账号异地登录的推送信息
												if(otherId!=deviceId && !otherId.equals(deviceId)){
													return status(400, "本次登录需要验证手机号码");
												}
											}
										}
										memberService.saveBusinessLoginDetail(detail);
										// 封装返回信息
										LoginInfo login = new LoginInfo();
										login.setAvatar(commonService.appenUrl(employee.getAvatar()));
//										login.setAccessToken(employee.getAccessToken());
										login.setId(employee.getId());
										login.setMobile(newMobile);
										login.setNickname(employee.getName());
										login.setIfBusiness(employee.getIfBusiness());
										login.setCommodityId(employee.getCommodityId());
										SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
										String str = sdf1.format(date);
										login.setSubloginSign(PasswdEncryption.MD5(str+employee.getId()));
										//判断用户是否为自注册用户，如果是，则查询会员过期日期
										if(employee.getIfBusiness()==1){
										    MemberPayInfo payInfo = businessService.queryMemberPayByEmployeeId(employee.getId());
										    if(payInfo!=null){
										    	Long monthCount = businessService.queryMonthCountByEmployeeId(employee.getId());
										    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//										    	Date now = sdf.parse(payInfo.getCreateAt());
										    	Calendar calendar = Calendar.getInstance();
										    	calendar.setTime(payInfo.getCreateAt());
										    	calendar.add(Calendar.MONTH,  Integer.valueOf(monthCount.toString()));
										    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										    	System.out.println(sdf.format(calendar.getTime()));
										    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										    	Date now = new Date();
										    	Date dateTime1 = dateFormat.parse(df.format(now));
//										    	//会员过期时间
										        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
										        int i = dateTime1.compareTo(dateTime2);  
										        System.out.println(i < 0);
										        //是否过期
										        if(i < 0){
										        	login.setOverdueDate(sdf.format(calendar.getTime()));
										        }else{
										        	login.setOverdueDate(null);
										        }
										    }else{
										    	login.setOverdueDate(null);
										    }
										}else{
//											login.setOverdueDate(null);
//											SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//											Calendar calendar = Calendar.getInstance();
//											//取账号创建时间
//									    	calendar.setTime(employee.getCreateAt());
//									    	//账号创建时间加上会员日期为会员过期日期
//									    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(employee.getExpiryDate().toString()));
//									    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//									    	System.out.println(sdf.format(calendar.getTime()));
//									    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//									    	Date now = new Date();
//									    	Date dateTime1 = dateFormat.parse(df.format(now));
////									    	//会员过期时间
//									        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
//									        int i = dateTime1.compareTo(dateTime2);  
//									        System.out.println(i < 0);
//									        //是否过期
//									        if(i < 0){
//									        	login.setOverdueDate(sdf.format(calendar.getTime()));
//									        }else{
//									        	login.setOverdueDate(null);
//									        }
											SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
											//判断t_business_member_record表中是否有效期内的好多课会员数据存在
											List<MemberRecord> recordList = businessService.findMemberRecordList(employee.getId().toString());
											//判断是否有系列课会员权限存在
											List<MemberRecord> rlist = businessService.findMemberRecordListByEmployeeId(employee.getId().toString());
											if(recordList.size()>0){
												login.setOverdueDate(sdf.format(recordList.get(0).getEndDate()));
											}else if(rlist.size()>0){
												login.setOverdueDate(sdf.format(rlist.get(0).getEndDate()));
											}else{
												login.setOverdueDate(null);
											}
										}
										//判断t_business_member_record表中是否有效期内的好多课会员数据存在
										List<MemberRecord> recordList = businessService.findMemberRecordList(employee.getId().toString());
										if(recordList.size()>0){
											login.setIfBusiness(0L);
										}else{
											//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
											 if(businessService.findMemberRecordListByEmployeeId(employee.getId().toString()).size()>0){
												 login.setIfBusiness(99999L);
											 }else{
												 login.setIfBusiness(1L);
											 }
										}
										//登录成功后将最新的sign存入数据库,用于二次登录的判断
										memberService.updateEmployeeSubloginSign(employee.getId().toString(),login.getSubloginSign());
										return okNew("登录成功!", login);
									}
								}else{
									if("1".equals(loginType)){
										List<LoginDetail> details = memberService.queryEmployeeCloseMsg(newMobile,newDeviceId);
										//如果设备ID数量大于等于3 就是第四台设备登录了
										if(details.size()>=3){
									        List<BlockDetail> blockList = memberService.queryBlockList(employee.getId());
									        //如果封号记录为空
									        if(blockList.size()<1){
									        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
												Calendar calendar = Calendar.getInstance();
												//取账号创建时间
										    	calendar.setTime(new Date());
										    	//账号创建时间加上会员日期为会员过期日期
										    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf("7"));
										    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										    	System.out.println(sdf.format(calendar.getTime()));
										    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										    	Date now = new Date();
										    	Date dateTime1 = dateFormat.parse(df.format(now));
//										    	//会员过期时间
										        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
										        int i = dateTime1.compareTo(dateTime2); 
									        	BlockDetail block = new BlockDetail();
									        	block.setEmployeeId(employee.getId().toString());
									        	//第一次封号7天
									        	block.setBlockTime("7");
									        	block.setUnblockAt(sdf.format(dateTime2));
									        	block.setEmployeeId(employee.getId().toString());
									        	//写入封号信息
									        	memberService.saveBusinessBlockDetail(block);
									        	return status(600,"您的账号还有6天解封，请耐心等待哦");
									        }else if(blockList.size()==1){
									        	//查询最后一条封号记录时间
									        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
												Calendar calendar = Calendar.getInstance();
												//取账号创建时间
										    	calendar.setTime(new Date());
										    	//账号创建时间加上会员日期为会员过期日期
										    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf("30"));
										    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										    	System.out.println(sdf.format(calendar.getTime()));
										    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										    	Date now = new Date();
										    	Date dateTime1 = dateFormat.parse(df.format(now));
//										    	//会员过期时间
										        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
										        //取出封号记录的解封时间
										        Date dateTime3 = dateFormat.parse(blockList.get(0).getUnblockAt());
										        //当前时间小于解封时间 求两个时间的差值
									        	if(dateTime1.getTime()<=dateTime3.getTime()){
									        		long diff = dateTime3.getTime() - dateTime1.getTime();
											        long diffDays = diff / (24 * 60 * 60 * 1000);
											        System.out.println("day1:"+dateTime2+"   day2:"+dateTime1+ "   差值："+diffDays);
											        return status(600,"您的账号还有"+diffDays+"天解封，请耐心等待哦");
									        	}else{
									        		BlockDetail block = new BlockDetail();
										        	block.setEmployeeId(employee.getId().toString());
										        	//第一次封号7天
										        	block.setBlockTime("30");
										        	block.setUnblockAt(sdf.format(dateTime2));
										        	block.setEmployeeId(employee.getId().toString());
										        	//写入封号信息
										        	memberService.saveBusinessBlockDetail(block);
									        		return status(600,"您的账号还有29天解封，请耐心等待哦");
									        	}
									        	
									        }else{
									        	//取最后一条封停记录
									        	List<BlockDetail> lastBlockList = memberService.queryLastBlockList(employee.getId());
									        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
												Calendar calendar = Calendar.getInstance();
												//取账号创建时间
										    	calendar.setTime(new Date());
										    	//账号创建时间加上会员日期为会员过期日期
										    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf("99999"));
										    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										    	System.out.println(sdf.format(calendar.getTime()));
										    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										    	Date now = new Date();
										    	Date dateTime1 = dateFormat.parse(df.format(now));
//										    	//会员过期时间
										        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
										        int i = dateTime1.compareTo(dateTime2); 
										      //取出封号记录的解封时间
										        Date dateTime3 = dateFormat.parse(blockList.get(0).getUnblockAt());
										        if(blockList.size()==2){
										        	BlockDetail block = new BlockDetail();
										        	block.setEmployeeId(employee.getId().toString());
										        	//第一次封号7天
										        	block.setBlockTime("99999");
										        	block.setUnblockAt(sdf.format(dateTime2));
										        	block.setEmployeeId(employee.getId().toString());
										        	//写入封号信息
										        	memberService.saveBusinessBlockDetail(block);
										        	return status(600,"您的账号已被永久封停");
										        }else{
										        	return status(600,"您的账号已被永久封停");
										        }
//										        //当前时间小于解封时间 求两个时间的差值
//									        	if(dateTime1.getTime()<=dateTime3.getTime()){
//									        		return status(600,"您的账号已被永久封停");
//									        	}else{
//										        	BlockDetail block = new BlockDetail();
//										        	block.setEmployeeId(employee.getId().toString());
//										        	//第一次封号7天
//										        	block.setBlockTime("99999");
//										        	block.setUnblockAt(sdf.format(dateTime2));
//										        	block.setEmployeeId(employee.getId().toString());
//										        	//写入封号信息
//										        	memberService.saveBusinessBlockDetail(block);
//										        	return status(600,"您的账号已被永久封停");
//									        	}
									        }
											
										}
									}
									List<WhiteList> wl = memberService.queryWhiteListByMobile(newMobile);
									if(wl.size()<1){
										LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employee.getId());
										String otherId = "a";
										if(lastLogin != null){
											otherId = lastLogin.getDeviceId();
											//如果本次登录的设备和上一次的设备不是同一个，就给上一台设备发送账号异地登录的推送信息
											if(otherId!=deviceId && !otherId.equals(deviceId)){
												return status(400, "本次登录需要验证手机号码");
											}
										}
									}
									memberService.saveBusinessLoginDetail(detail);
									// 封装返回信息
									LoginInfo login = new LoginInfo();
									login.setAvatar(commonService.appenUrl(employee.getAvatar()));
//									login.setAccessToken(employee.getAccessToken());
									login.setId(employee.getId());
									login.setMobile(newMobile);
									login.setNickname(employee.getName());
									login.setIfBusiness(employee.getIfBusiness());
									login.setCommodityId(employee.getCommodityId());
									SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
									String str = sdf1.format(date);
									login.setSubloginSign(PasswdEncryption.MD5(str+employee.getId()));
									//判断用户是否为自注册用户，如果是，则查询会员过期日期
									if(employee.getIfBusiness()==1){
									    MemberPayInfo payInfo = businessService.queryMemberPayByEmployeeId(employee.getId());
									    if(payInfo!=null){
									    	Long monthCount = businessService.queryMonthCountByEmployeeId(employee.getId());
									    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//									    	Date now = sdf.parse(payInfo.getCreateAt());
									    	Calendar calendar = Calendar.getInstance();
									    	calendar.setTime(payInfo.getCreateAt());
									    	calendar.add(Calendar.MONTH,  Integer.valueOf(monthCount.toString()));
									    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									    	System.out.println(sdf.format(calendar.getTime()));
									    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									    	Date now = new Date();
									    	Date dateTime1 = dateFormat.parse(df.format(now));
//									    	//会员过期时间
									        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
									        int i = dateTime1.compareTo(dateTime2);  
									        System.out.println(i < 0);
									        //是否过期
									        if(i < 0){
									        	login.setOverdueDate(sdf.format(calendar.getTime()));
									        }else{
									        	login.setOverdueDate(null);
									        }
									    }else{
									    	login.setOverdueDate(null);
									    }
									}else{
//										login.setOverdueDate(null);
//										SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//										Calendar calendar = Calendar.getInstance();
//										//取账号创建时间
//								    	calendar.setTime(employee.getCreateAt());
//								    	//账号创建时间加上会员日期为会员过期日期
//								    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(employee.getExpiryDate().toString()));
//								    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//								    	System.out.println(sdf.format(calendar.getTime()));
//								    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//								    	Date now = new Date();
//								    	Date dateTime1 = dateFormat.parse(df.format(now));
////								    	//会员过期时间
//								        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
//								        int i = dateTime1.compareTo(dateTime2);  
//								        System.out.println(i < 0);
//								        //是否过期
//								        if(i < 0){
//								        	login.setOverdueDate(sdf.format(calendar.getTime()));
//								        }else{
//								        	login.setOverdueDate(null);
//								        }
										SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										//判断t_business_member_record表中是否有效期内的好多课会员数据存在
										List<MemberRecord> recordList = businessService.findMemberRecordList(employee.getId().toString());
										//判断是否有系列课会员权限存在
										List<MemberRecord> rlist = businessService.findMemberRecordListByEmployeeId(employee.getId().toString());
										if(recordList.size()>0){
											login.setOverdueDate(sdf.format(recordList.get(0).getEndDate()));
										}else if(rlist.size()>0){
											login.setOverdueDate(sdf.format(rlist.get(0).getEndDate()));
										}else{
											login.setOverdueDate(null);
										}
									}
									//判断t_business_member_record表中是否有效期内的好多课会员数据存在
									List<MemberRecord> recordList = businessService.findMemberRecordList(employee.getId().toString());
									if(recordList.size()>0){
										login.setIfBusiness(0L);
									}else{
										//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
										 if(businessService.findMemberRecordListByEmployeeId(employee.getId().toString()).size()>0){
											 login.setIfBusiness(99999L);
										 }else{
											 login.setIfBusiness(1L);
										 }
									}
									//登录成功后将最新的sign存入数据库,用于二次登录的判断
									memberService.updateEmployeeSubloginSign(employee.getId().toString(),login.getSubloginSign());
									return okNew("登录成功!", login);
								}
							}
						}
						memberService.saveBusinessLoginDetail(detail);
					} else {
						return status(113,"密码错误");
					}
					// 封装返回信息
					LoginInfo login = new LoginInfo();
					login.setAvatar(commonService.appenUrl(employee.getAvatar()));
//					login.setAccessToken(employee.getAccessToken());
					login.setId(employee.getId());
					login.setMobile(newMobile);
					login.setNickname(employee.getName());
					login.setIfBusiness(employee.getIfBusiness());
					login.setCommodityId(employee.getCommodityId());
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
					String str = sdf1.format(new Date());
					login.setSubloginSign(PasswdEncryption.MD5(str+employee.getId()));
					//判断用户是否为自注册用户，如果是，则查询会员过期日期
					if(employee.getIfBusiness()==1){
					    MemberPayInfo payInfo = businessService.queryMemberPayByEmployeeId(employee.getId());
					    if(payInfo!=null){
					    	Long monthCount = businessService.queryMonthCountByEmployeeId(employee.getId());
					    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//					    	Date now = sdf.parse(payInfo.getCreateAt());
					    	Calendar calendar = Calendar.getInstance();
					    	calendar.setTime(payInfo.getCreateAt());
					    	calendar.add(Calendar.MONTH,  Integer.valueOf(monthCount.toString()));
					    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					    	System.out.println(sdf.format(calendar.getTime()));
					    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					    	Date now = new Date();
					    	Date dateTime1 = dateFormat.parse(df.format(now));
//					    	//会员过期时间
					        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
					        int i = dateTime1.compareTo(dateTime2);  
					        System.out.println(i < 0);
					        //是否过期
					        if(i < 0){
					        	login.setOverdueDate(sdf.format(calendar.getTime()));
					        }else{
					        	login.setOverdueDate(null);
					        }
					    }else{
					    	login.setOverdueDate(null);
					    }
					}else{
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						//判断t_business_member_record表中是否有效期内的好多课会员数据存在
						List<MemberRecord> recordList = businessService.findMemberRecordList(employee.getId().toString());
						//判断是否有系列课会员权限存在
						List<MemberRecord> rlist = businessService.findMemberRecordListByEmployeeId(employee.getId().toString());
						if(recordList.size()>0){
							login.setOverdueDate(sdf.format(recordList.get(0).getEndDate()));
						}else if(rlist.size()>0){
							login.setOverdueDate(sdf.format(rlist.get(0).getEndDate()));
						}else{
							login.setOverdueDate(null);
						}
					}
					//判断t_business_member_record表中是否有效期内的好多课会员数据存在
					List<MemberRecord> recordList = businessService.findMemberRecordList(employee.getId().toString());
					if(recordList.size()>0){
						login.setIfBusiness(0L);
					}else{
						//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
						 if(businessService.findMemberRecordListByEmployeeId(employee.getId().toString()).size()>0){
							 login.setIfBusiness(99999L);
						 }else{
							 login.setIfBusiness(1L);
						 }
					}
					//登录成功后将最新的sign存入数据库,用于二次登录的判断
					memberService.updateEmployeeSubloginSign(employee.getId().toString(),login.getSubloginSign());
					return okNew("登录成功", login);
				}else{
					return status(97,"sign 验证出错");
				}
			}else{
				Employee employee = memberService.findBusinessLoginMemberByMobile1(mobile);
				if (employee == null) {
					return error("用户不存在");
				}
				// 验证密码
				if (employee.getPassword().equals(PasswdEncryption.MD5(password))
						|| PasswdEncryption.verify(password, employee.getPassword())) {
					// 保存登录信息到登录明细表
					Date date = new Date();
					LoginDetail detail = new LoginDetail();
					detail.setEmployeeId(employee.getId().toString());
					detail.setEmployeeMobile(mobile);
					detail.setEmployeeName(employee.getName());
					detail.setLoginTime(date);
					detail.setVersion(version);
					detail.setPlatform(platform);
					detail.setClient(client);
					if(StringUtils.isNullOrEmpty(deviceId)){
						detail.setDeviceId("1");
					}else{
						detail.setDeviceId(deviceId);
					}
					//非游客登录给上一台设备推送账号异地登录的通知
					if(employee.getId()!=33){
						if(mobile!="15910491294" && !"15910491294".equals(mobile) && mobile!="15901027371" && !"15901027371".equals(mobile) && mobile!="18513084246" && !"18513084246".equals(mobile) && mobile!="17301337859" && !"17301337859".equals(mobile) && mobile!="00000000001" && !"00000000001".equals(mobile) && mobile!="00000000002" && !"00000000002".equals(mobile) && mobile!="00000000003" && !"00000000003".equals(mobile)&& mobile!="00000000004" && !"00000000004".equals(mobile) && mobile!="00000000005" && !"00000000005".equals(mobile) && mobile!="00000000006" && !"00000000006".equals(mobile)&& mobile!="00000000011" && mobile!="00000000012" && mobile!="00000000015" && !"00000000015".equals(mobile) && !"00000000012".equals(mobile)&& mobile!="00000000014" && !"00000000014".equals(mobile) && mobile!="00000000013" && !"00000000013".equals(mobile)&& !"00000000011".equals(mobile)&& mobile!="00000000010" && !"00000000010".equals(mobile)&& mobile!="00000000009" && !"00000000009".equals(mobile)&& mobile!="00000000008" && !"00000000008".equals(mobile)&& mobile!="00000000007" && !"00000000007".equals(mobile) && mobile!="18301085230" && !"18301085230".equals(mobile)&& mobile!="18330225032" && !"18330225032".equals(mobile)){
//							if(("1.7.6").equals(version) || ("1.7.7").equals(version)){
								//查询最近一次登录的信息
							//查询该手机号一周以内的登录设备ID
							if(!StringUtils.isNullOrEmpty(loginType) && "0".equals(loginType)){
								//查询最新一条封号记录
								List<BlockDetail> blist = memberService.queryLastBlockList(employee.getId());
								//如果存在没解封的记录 ，提示解封时间
								if(blist.size()>0){
						        	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									Calendar calendar1 = Calendar.getInstance();
									//取账号创建时间
							    	calendar1.setTime(new Date());
							    	//账号创建时间加上会员日期为会员过期日期
							    	calendar1.add(Calendar.DAY_OF_MONTH,  Integer.valueOf("30"));
							    	SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							    	System.out.println(sdf1.format(calendar1.getTime()));
							    	DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							    	Date now1 = new Date();
							    	Date dateTime4 = dateFormat1.parse(df1.format(now1));
//							    	//会员过期时间
							        Date dateTime5 = dateFormat1.parse(df1.format(calendar1.getTime()));
							        //取出封号记录的解封时间
							        Date dateTime6 = dateFormat1.parse(blist.get(0).getUnblockAt());
							        //当前时间小于解封时间 求两个时间的差值
						        	if(dateTime4.getTime()<=dateTime6.getTime()){
						        		long diff = dateTime6.getTime() - dateTime4.getTime();
								        long diffDays = diff / (24 * 60 * 60 * 1000);
								        System.out.println("day1:"+dateTime6+"   day2:"+dateTime4+ "   差值："+diffDays);
								        return status(600,"您的账号还有"+diffDays+"天解封，请耐心等待哦");
						        	}
								}
								List<LoginDetail> details = memberService.queryEmployeeCloseMsg(mobile,deviceId);
								if(details.size()>=2){
									if(details.size()==2){
										return status(300, "您正在使用第3台设备登录同一账号，超过3台将被封号，请确认是否登录？");
									}else{
										List<BlockDetail> blockList = memberService.queryBlockList(employee.getId());
										if(blockList.size()>0){
											if(blockList.size()==1){
												return status(700,"您的登录设备已超过3台，第2次违规，继续登录会被封号1个月，请确认是否登录");
											}else{
												return status(700,"您的登录设备已超过3台，第3次违规，继续登录将会永久封号，请确认是否登录");
											}
										}else{
											return status(700,"您正在使用第4台设备登录同一账号，如核实该账号将被封禁，请确认是否登录");
										}
									}
								}else{
									List<WhiteList> wl = memberService.queryWhiteListByMobile(mobile);
									if(wl.size()<1){
										LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employee.getId());
										String otherId = "a";
										if(lastLogin != null){
											otherId = lastLogin.getDeviceId();
											//如果本次登录的设备和上一次的设备不是同一个，就给上一台设备发送账号异地登录的推送信息
											if(otherId!=deviceId && !otherId.equals(deviceId)){
												return status(400, "本次登录需要验证手机号码");
											}
										}
									}
									memberService.saveBusinessLoginDetail(detail);
									// 封装返回信息
									LoginInfo login = new LoginInfo();
									login.setAvatar(commonService.appenUrl(employee.getAvatar()));
//									login.setAccessToken(employee.getAccessToken());
									login.setId(employee.getId());
									login.setMobile(mobile);
									login.setNickname(employee.getName());
									login.setIfBusiness(employee.getIfBusiness());
									login.setCommodityId(employee.getCommodityId());
									SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
									String str = sdf1.format(date);
									login.setSubloginSign(PasswdEncryption.MD5(str+employee.getId()));
									//判断用户是否为自注册用户，如果是，则查询会员过期日期
									if(employee.getIfBusiness()==1){
									    MemberPayInfo payInfo = businessService.queryMemberPayByEmployeeId(employee.getId());
									    if(payInfo!=null){
									    	Long monthCount = businessService.queryMonthCountByEmployeeId(employee.getId());
									    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//									    	Date now = sdf.parse(payInfo.getCreateAt());
									    	Calendar calendar = Calendar.getInstance();
									    	calendar.setTime(payInfo.getCreateAt());
									    	calendar.add(Calendar.MONTH,  Integer.valueOf(monthCount.toString()));
									    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									    	System.out.println(sdf.format(calendar.getTime()));
									    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									    	Date now = new Date();
									    	Date dateTime1 = dateFormat.parse(df.format(now));
//									    	//会员过期时间
									        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
									        int i = dateTime1.compareTo(dateTime2);  
									        System.out.println(i < 0);
									        //是否过期
									        if(i < 0){
									        	login.setOverdueDate(sdf.format(calendar.getTime()));
									        }else{
									        	login.setOverdueDate(null);
									        }
									    }else{
									    	login.setOverdueDate(null);
									    }
									}else{
//										login.setOverdueDate(null);
//										SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//										Calendar calendar = Calendar.getInstance();
//										//取账号创建时间
//								    	calendar.setTime(employee.getCreateAt());
//								    	//账号创建时间加上会员日期为会员过期日期
//								    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(employee.getExpiryDate().toString()));
//								    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//								    	System.out.println(sdf.format(calendar.getTime()));
//								    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//								    	Date now = new Date();
//								    	Date dateTime1 = dateFormat.parse(df.format(now));
////								    	//会员过期时间
//								        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
//								        int i = dateTime1.compareTo(dateTime2);  
//								        System.out.println(i < 0);
//								        //是否过期
//								        if(i < 0){
//								        	login.setOverdueDate(sdf.format(calendar.getTime()));
//								        }else{
//								        	login.setOverdueDate(null);
//								        }
										SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										//判断t_business_member_record表中是否有效期内的好多课会员数据存在
										List<MemberRecord> recordList = businessService.findMemberRecordList(employee.getId().toString());
										//判断是否有系列课会员权限存在
										List<MemberRecord> rlist = businessService.findMemberRecordListByEmployeeId(employee.getId().toString());
										if(recordList.size()>0){
											login.setOverdueDate(sdf.format(recordList.get(0).getEndDate()));
										}else if(rlist.size()>0){
											login.setOverdueDate(sdf.format(rlist.get(0).getEndDate()));
										}else{
											login.setOverdueDate(null);
										}
									}
									//判断t_business_member_record表中是否有效期内的好多课会员数据存在
									List<MemberRecord> recordList = businessService.findMemberRecordList(employee.getId().toString());
									if(recordList.size()>0){
										login.setIfBusiness(0L);
									}else{
										//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
										 if(businessService.findMemberRecordListByEmployeeId(employee.getId().toString()).size()>0){
											 login.setIfBusiness(99999L);
										 }else{
											 login.setIfBusiness(1L);
										 }
									}
									return ok("登录成功!", login);
								}
							}else{
								if("1".equals(loginType)){
									List<LoginDetail> details = memberService.queryEmployeeCloseMsg(mobile,deviceId);
									//如果设备ID数量大于等于3 就是第四台设备登录了
									if(details.size()>=3){
								        List<BlockDetail> blockList = memberService.queryBlockList(employee.getId());
								        //如果封号记录为空
								        if(blockList.size()<1){
								        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
											Calendar calendar = Calendar.getInstance();
											//取账号创建时间
									    	calendar.setTime(new Date());
									    	//账号创建时间加上会员日期为会员过期日期
									    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf("7"));
									    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									    	System.out.println(sdf.format(calendar.getTime()));
									    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									    	Date now = new Date();
									    	Date dateTime1 = dateFormat.parse(df.format(now));
//									    	//会员过期时间
									        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
									        int i = dateTime1.compareTo(dateTime2); 
								        	BlockDetail block = new BlockDetail();
								        	block.setEmployeeId(employee.getId().toString());
								        	//第一次封号7天
								        	block.setBlockTime("7");
								        	block.setUnblockAt(sdf.format(dateTime2));
								        	block.setEmployeeId(employee.getId().toString());
								        	//写入封号信息
								        	memberService.saveBusinessBlockDetail(block);
								        	return status(600,"您的账号还有6天解封，请耐心等待哦");
								        }else if(blockList.size()==1){
								        	//查询最后一条封号记录时间
								        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
											Calendar calendar = Calendar.getInstance();
											//取账号创建时间
									    	calendar.setTime(new Date());
									    	//账号创建时间加上会员日期为会员过期日期
									    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf("30"));
									    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									    	System.out.println(sdf.format(calendar.getTime()));
									    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									    	Date now = new Date();
									    	Date dateTime1 = dateFormat.parse(df.format(now));
//									    	//会员过期时间
									        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
									        //取出封号记录的解封时间
									        Date dateTime3 = dateFormat.parse(blockList.get(0).getUnblockAt());
									        //当前时间小于解封时间 求两个时间的差值
								        	if(dateTime1.getTime()<=dateTime3.getTime()){
								        		long diff = dateTime3.getTime() - dateTime1.getTime();
										        long diffDays = diff / (24 * 60 * 60 * 1000);
										        System.out.println("day1:"+dateTime2+"   day2:"+dateTime1+ "   差值："+diffDays);
										        return status(600,"您的账号还有"+diffDays+"天解封，请耐心等待哦");
								        	}else{
								        		BlockDetail block = new BlockDetail();
									        	block.setEmployeeId(employee.getId().toString());
									        	//第一次封号7天
									        	block.setBlockTime("30");
									        	block.setUnblockAt(sdf.format(dateTime2));
									        	block.setEmployeeId(employee.getId().toString());
									        	//写入封号信息
									        	memberService.saveBusinessBlockDetail(block);
								        		return status(600,"您的账号还有29天解封，请耐心等待哦");
								        	}
								        	
								        }else{
								        	//取最后一条封停记录
								        	List<BlockDetail> lastBlockList = memberService.queryLastBlockList(employee.getId());
								        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
											Calendar calendar = Calendar.getInstance();
											//取账号创建时间
									    	calendar.setTime(new Date());
									    	//账号创建时间加上会员日期为会员过期日期
									    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf("99999"));
									    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									    	System.out.println(sdf.format(calendar.getTime()));
									    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									    	Date now = new Date();
									    	Date dateTime1 = dateFormat.parse(df.format(now));
//									    	//会员过期时间
									        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
									        int i = dateTime1.compareTo(dateTime2); 
									      //取出封号记录的解封时间
									        Date dateTime3 = dateFormat.parse(blockList.get(0).getUnblockAt());
									        if(blockList.size()==2){
									        	BlockDetail block = new BlockDetail();
									        	block.setEmployeeId(employee.getId().toString());
									        	//第一次封号7天
									        	block.setBlockTime("99999");
									        	block.setUnblockAt(sdf.format(dateTime2));
									        	block.setEmployeeId(employee.getId().toString());
									        	//写入封号信息
									        	memberService.saveBusinessBlockDetail(block);
									        	return status(600,"您的账号已被永久封停");
									        }else{
									        	return status(600,"您的账号已被永久封停");
									        }
//									        //当前时间小于解封时间 求两个时间的差值
//								        	if(dateTime1.getTime()<=dateTime3.getTime()){
//								        		return status(600,"您的账号已被永久封停");
//								        	}else{
//									        	BlockDetail block = new BlockDetail();
//									        	block.setEmployeeId(employee.getId().toString());
//									        	//第一次封号7天
//									        	block.setBlockTime("99999");
//									        	block.setUnblockAt(sdf.format(dateTime2));
//									        	block.setEmployeeId(employee.getId().toString());
//									        	//写入封号信息
//									        	memberService.saveBusinessBlockDetail(block);
//									        	return status(600,"您的账号已被永久封停");
//								        	}
								        }
										
									}
								}
								List<WhiteList> wl = memberService.queryWhiteListByMobile(mobile);
								if(wl.size()<1){
									LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employee.getId());
									String otherId = "a";
									if(lastLogin != null){
										otherId = lastLogin.getDeviceId();
										//如果本次登录的设备和上一次的设备不是同一个，就给上一台设备发送账号异地登录的推送信息
										if(otherId!=deviceId && !otherId.equals(deviceId)){
											return status(400, "本次登录需要验证手机号码");
										}
									}
								}
								memberService.saveBusinessLoginDetail(detail);
								// 封装返回信息
								LoginInfo login = new LoginInfo();
								login.setAvatar(commonService.appenUrl(employee.getAvatar()));
//								login.setAccessToken(employee.getAccessToken());
								login.setId(employee.getId());
								login.setMobile(mobile);
								login.setNickname(employee.getName());
								login.setIfBusiness(employee.getIfBusiness());
								login.setCommodityId(employee.getCommodityId());
								SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
								String str = sdf1.format(date);
								login.setSubloginSign(PasswdEncryption.MD5(str+employee.getId()));
								//判断用户是否为自注册用户，如果是，则查询会员过期日期
								if(employee.getIfBusiness()==1){
								    MemberPayInfo payInfo = businessService.queryMemberPayByEmployeeId(employee.getId());
								    if(payInfo!=null){
								    	Long monthCount = businessService.queryMonthCountByEmployeeId(employee.getId());
								    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//								    	Date now = sdf.parse(payInfo.getCreateAt());
								    	Calendar calendar = Calendar.getInstance();
								    	calendar.setTime(payInfo.getCreateAt());
								    	calendar.add(Calendar.MONTH,  Integer.valueOf(monthCount.toString()));
								    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								    	System.out.println(sdf.format(calendar.getTime()));
								    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								    	Date now = new Date();
								    	Date dateTime1 = dateFormat.parse(df.format(now));
//								    	//会员过期时间
								        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
								        int i = dateTime1.compareTo(dateTime2);  
								        System.out.println(i < 0);
								        //是否过期
								        if(i < 0){
								        	login.setOverdueDate(sdf.format(calendar.getTime()));
								        }else{
								        	login.setOverdueDate(null);
								        }
								    }else{
								    	login.setOverdueDate(null);
								    }
								}else{
//									login.setOverdueDate(null);
//									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//									Calendar calendar = Calendar.getInstance();
//									//取账号创建时间
//							    	calendar.setTime(employee.getCreateAt());
//							    	//账号创建时间加上会员日期为会员过期日期
//							    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(employee.getExpiryDate().toString()));
//							    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//							    	System.out.println(sdf.format(calendar.getTime()));
//							    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//							    	Date now = new Date();
//							    	Date dateTime1 = dateFormat.parse(df.format(now));
////							    	//会员过期时间
//							        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
//							        int i = dateTime1.compareTo(dateTime2);  
//							        System.out.println(i < 0);
//							        //是否过期
//							        if(i < 0){
//							        	login.setOverdueDate(sdf.format(calendar.getTime()));
//							        }else{
//							        	login.setOverdueDate(null);
//							        }
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									//判断t_business_member_record表中是否有效期内的好多课会员数据存在
									List<MemberRecord> recordList = businessService.findMemberRecordList(employee.getId().toString());
									//判断是否有系列课会员权限存在
									List<MemberRecord> rlist = businessService.findMemberRecordListByEmployeeId(employee.getId().toString());
									if(recordList.size()>0){
										login.setOverdueDate(sdf.format(recordList.get(0).getEndDate()));
									}else if(rlist.size()>0){
										login.setOverdueDate(sdf.format(rlist.get(0).getEndDate()));
									}else{
										login.setOverdueDate(null);
									}
								}
								//判断t_business_member_record表中是否有效期内的好多课会员数据存在
								List<MemberRecord> recordList = businessService.findMemberRecordList(employee.getId().toString());
								if(recordList.size()>0){
									login.setIfBusiness(0L);
								}else{
									//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
									 if(businessService.findMemberRecordListByEmployeeId(employee.getId().toString()).size()>0){
										 login.setIfBusiness(99999L);
									 }else{
										 login.setIfBusiness(1L);
									 }
								}
								return ok("登录成功!", login);
							}
						}
					}
					memberService.saveBusinessLoginDetail(detail);
				} else {
					return error("密码错误!");
				}
				// 封装返回信息
				LoginInfo login = new LoginInfo();
				login.setAvatar(commonService.appenUrl(employee.getAvatar()));
//				login.setAccessToken(employee.getAccessToken());
				login.setId(employee.getId());
				login.setMobile(mobile);
				login.setNickname(employee.getName());
				login.setIfBusiness(employee.getIfBusiness());
				login.setCommodityId(employee.getCommodityId());
				
				//判断用户是否为自注册用户，如果是，则查询会员过期日期
				if(employee.getIfBusiness()==1){
				    MemberPayInfo payInfo = businessService.queryMemberPayByEmployeeId(employee.getId());
				    if(payInfo!=null){
				    	Long monthCount = businessService.queryMonthCountByEmployeeId(employee.getId());
				    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				    	Date now = sdf.parse(payInfo.getCreateAt());
				    	Calendar calendar = Calendar.getInstance();
				    	calendar.setTime(payInfo.getCreateAt());
				    	calendar.add(Calendar.MONTH,  Integer.valueOf(monthCount.toString()));
				    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				    	System.out.println(sdf.format(calendar.getTime()));
				    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				    	Date now = new Date();
				    	Date dateTime1 = dateFormat.parse(df.format(now));
//				    	//会员过期时间
				        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
				        int i = dateTime1.compareTo(dateTime2);  
				        System.out.println(i < 0);
				        //是否过期
				        if(i < 0){
				        	login.setOverdueDate(sdf.format(calendar.getTime()));
				        }else{
				        	login.setOverdueDate(null);
				        }
				    }else{
				    	login.setOverdueDate(null);
				    }
				}else{
//					login.setOverdueDate(null);
//					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//					Calendar calendar = Calendar.getInstance();
//					//取账号创建时间
//			    	calendar.setTime(employee.getCreateAt());
//			    	//账号创建时间加上会员日期为会员过期日期
//			    	calendar.add(Calendar.DAY_OF_MONTH,  Integer.valueOf(employee.getExpiryDate().toString()));
//			    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			    	System.out.println(sdf.format(calendar.getTime()));
//			    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			    	Date now = new Date();
//			    	Date dateTime1 = dateFormat.parse(df.format(now));
////			    	//会员过期时间
//			        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
//			        int i = dateTime1.compareTo(dateTime2);  
//			        System.out.println(i < 0);
//			        //是否过期
//			        if(i < 0){
//			        	login.setOverdueDate(sdf.format(calendar.getTime()));
//			        }else{
//			        	login.setOverdueDate(null);
//			        }
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					//判断t_business_member_record表中是否有效期内的好多课会员数据存在
					List<MemberRecord> recordList = businessService.findMemberRecordList(employee.getId().toString());
					//判断是否有系列课会员权限存在
					List<MemberRecord> rlist = businessService.findMemberRecordListByEmployeeId(employee.getId().toString());
					if(recordList.size()>0){
						login.setOverdueDate(sdf.format(recordList.get(0).getEndDate()));
					}else if(rlist.size()>0){
						login.setOverdueDate(sdf.format(rlist.get(0).getEndDate()));
					}else{
						login.setOverdueDate(null);
					}
				}
				//判断t_business_member_record表中是否有效期内的好多课会员数据存在
				List<MemberRecord> recordList = businessService.findMemberRecordList(employee.getId().toString());
				if(recordList.size()>0){
					login.setIfBusiness(0L);
				}else{
					//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
					 if(businessService.findMemberRecordListByEmployeeId(employee.getId().toString()).size()>0){
						 login.setIfBusiness(99999L);
					 }else{
						 login.setIfBusiness(1L);
					 }
				}
				return ok("登录成功!", login);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: businessLogin
	 * @Description: (App企业用户使用验证码登录)
	 * @param mobile 手机号
	 * @param password 密码
	 * @param info 手机信息
	 * @param deviceId 设备Id
	 * @param code 验证码
	 * @return
	 */
	@Deprecated
	@ResponseBody
	@RequestMapping("/businessCodeLogin")
	public JSONObject businessCodeLogin(String mobile, String password, String platform,String client,String version,String deviceId,String code,String sign) {
		try {
			if (StringUtils.isNullOrEmpty(mobile) || StringUtils.isNullOrEmpty(password)) {
				return error("参数缺失!");
			}
			if(("2.0.5").equals(version) || ("2.0.1").equals(version)||("2.0.4").equals(version)){
				//解密
				UpAES upAES = new UpAES();
//				upAES.entry("1");
				System.out.println("解密后的mobile："+upAES.disEntry(mobile.toString()));
				System.out.println("解密后的deviceId："+upAES.disEntry(deviceId));
				String newMobile = upAES.disEntry(mobile.toString());
				String newDeviceId =upAES.disEntry(deviceId.toString());
				//重新赋值deviceId
				deviceId =upAES.disEntry(deviceId);
				String newSign = PasswdEncryption.MD5(newMobile+newDeviceId);
				newSign= newSign.substring(5, newSign.length()-5);
				System.out.println("得到的新Sign:"+newSign);
				Employee employee = memberService.findBusinessLoginMemberByMobile1(newMobile);
				if (employee == null) {
					return status(112,"用户不存在");
				}
				if(!"5566".equals(code)){
					//判断验证码输入是否正确
					Code smsCode = codeService.findCode(code, newMobile);
					if (smsCode == null) {
						return status(101,"验证码输入错误");
					}
				}
				if(newSign.equals(sign)){
					// 验证密码
					if (employee.getPassword().equals(PasswdEncryption.MD5(password))
							|| PasswdEncryption.verify(password, employee.getPassword())) {
						// 保存登录信息到登录明细表
						Date date = new Date();
						LoginDetail detail = new LoginDetail();
						detail.setEmployeeId(employee.getId().toString());
						detail.setEmployeeMobile(newMobile);
						detail.setEmployeeName(employee.getName());
						detail.setLoginTime(date);
						detail.setVersion(version);
						detail.setPlatform(platform);
						detail.setClient(client);
						detail.setDeviceId(deviceId);
						//非游客登录给上一台设备推送账号异地登录的通知
						if(employee.getId()!=33){
							//查询最近一次登录的信息
							LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employee.getId());
							String otherId = "a";
							if(lastLogin != null){
								otherId = lastLogin.getDeviceId();
							}
		//					String otherId = lastLogin.getDeviceId();
							String msg = "您的账号在另一台设备登录";
							//如果本次登录的设备和上一次的设备不是同一个，就给上一台设备发送账号异地登录的推送信息
							if(otherId!=deviceId && !otherId.equals(deviceId)){
								System.out.println(PasswdEncryption.MD5(otherId));
								if(lastLogin!=null){
									if("iOS".equals(lastLogin.getClient())){
										commonService.pushIOSMessage(ClientEnum.ChaZuo, msg,otherId);
									}else{
										commonService.pushAndroidMessage(ClientEnum.ChaZuo, msg,otherId);
									}
								}
							}
						}
						memberService.saveBusinessLoginDetail(detail);
					} else {
						return status(113,"密码错误");
					}
					// 封装返回信息
					LoginInfo login = new LoginInfo();
					login.setAvatar(commonService.appenUrl(employee.getAvatar()));
		//			login.setAccessToken(employee.getAccessToken());
					login.setId(employee.getId());
					login.setMobile(newMobile);
					login.setNickname(employee.getName());
					login.setIfBusiness(employee.getIfBusiness());
					login.setCommodityId(employee.getCommodityId());
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
					String str = sdf1.format(new Date());
					login.setSubloginSign(PasswdEncryption.MD5(str+employee.getId()));
					//判断用户是否为自注册用户，如果是，则查询会员过期日期
					if(employee.getIfBusiness()==1){
					    MemberPayInfo payInfo = businessService.queryMemberPayByEmployeeId(employee.getId());
					    if(payInfo!=null){
					    	Long monthCount = businessService.queryMonthCountByEmployeeId(employee.getId());
					    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//			    	Date now = sdf.parse(payInfo.getCreateAt());
					    	Calendar calendar = Calendar.getInstance();
					    	calendar.setTime(payInfo.getCreateAt());
					    	calendar.add(Calendar.MONTH,  Integer.valueOf(monthCount.toString()));
					    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					    	System.out.println(sdf.format(calendar.getTime()));
					    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					    	Date now = new Date();
					    	Date dateTime1 = dateFormat.parse(df.format(now));
		//			    	//会员过期时间
					        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
					        int i = dateTime1.compareTo(dateTime2);  
					        System.out.println(i < 0);
					        //是否过期
					        if(i < 0){
					        	login.setOverdueDate(sdf.format(calendar.getTime()));
					        }else{
					        	login.setOverdueDate(null);
					        }
					    }else{
					    	login.setOverdueDate(null);
					    }
					}else{
		//				login.setOverdueDate(null);
		//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//				Calendar calendar = Calendar.getInstance();
		//				//取账号创建时间
		//		    	calendar.setTime(employee.getCreateAt());
		//		    	//账号创建时间加上会员日期为会员过期日期
		//		    	calendar.add(Calendar.MONTH,  Integer.valueOf(employee.getExpiryDate().toString()));
		//		    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//		    	System.out.println(sdf.format(calendar.getTime()));
		//		    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//		    	Date now = new Date();
		//		    	Date dateTime1 = dateFormat.parse(df.format(now));
		////		    	//会员过期时间
		//		        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
		//		        int i = dateTime1.compareTo(dateTime2);  
		//		        System.out.println(i < 0);
		//		        //是否过期
		//		        if(i < 0){
		//		        	login.setOverdueDate(sdf.format(calendar.getTime()));
		//		        }else{
		//		        	login.setOverdueDate(null);
		//		        }
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						//判断t_business_member_record表中是否有效期内的好多课会员数据存在
						List<MemberRecord> recordList = businessService.findMemberRecordList(employee.getId().toString());
						//判断是否有系列课会员权限存在
						List<MemberRecord> rlist = businessService.findMemberRecordListByEmployeeId(employee.getId().toString());
						if(recordList.size()>0){
							employee.setOverdueDate(sdf.format(recordList.get(0).getEndDate()));
						}else if(rlist.size()>0){
							employee.setOverdueDate(sdf.format(rlist.get(0).getEndDate()));
						}else{
							employee.setOverdueDate(null);
						}
					}
					//判断t_business_member_record表中是否有效期内的好多课会员数据存在
					List<MemberRecord> recordList = businessService.findMemberRecordList(employee.getId().toString());
					if(recordList.size()>0){
						login.setIfBusiness(0L);
					}else{
						//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
						 if(businessService.findMemberRecordListByEmployeeId(employee.getId().toString()).size()>0){
							 login.setIfBusiness(99999L);
						 }else{
							 login.setIfBusiness(1L);
						 }
					}
					//登录成功后将最新的sign存入数据库,用于二次登录的判断
					memberService.updateEmployeeSubloginSign(employee.getId().toString(),login.getSubloginSign());
					return okNew("登录成功!", login);
				}else{
					return status(97, "sign验证出错");
				}
			}else{
				Employee employee = memberService.findBusinessLoginMemberByMobile1(mobile);
				if (employee == null) {
					return error("用户不存在");
				}
				//判断验证码输入是否正确
				Code smsCode = codeService.findCode(code, mobile);
				if (smsCode == null) {
					return error("验证码输入错误!");
				}
				// 验证密码
				if (employee.getPassword().equals(PasswdEncryption.MD5(password))
						|| PasswdEncryption.verify(password, employee.getPassword())) {
					// 保存登录信息到登录明细表
					Date date = new Date();
					LoginDetail detail = new LoginDetail();
					detail.setEmployeeId(employee.getId().toString());
					detail.setEmployeeMobile(mobile);
					detail.setEmployeeName(employee.getName());
					detail.setLoginTime(date);
					detail.setVersion(version);
					detail.setPlatform(platform);
					detail.setClient(client);
					detail.setDeviceId(deviceId);
					//非游客登录给上一台设备推送账号异地登录的通知
					if(employee.getId()!=33){
						//查询最近一次登录的信息
						LoginDetail lastLogin = memberService.findBusinessLoginDetailById(employee.getId());
						String otherId = "a";
						if(lastLogin != null){
							otherId = lastLogin.getDeviceId();
						}
	//					String otherId = lastLogin.getDeviceId();
						String msg = "您的账号在另一台设备登录";
						//如果本次登录的设备和上一次的设备不是同一个，就给上一台设备发送账号异地登录的推送信息
						if(otherId!=deviceId && !otherId.equals(deviceId)){
							System.out.println(PasswdEncryption.MD5(otherId));
							if(lastLogin!=null){
								if("iOS".equals(lastLogin.getClient())){
									commonService.pushIOSMessage(ClientEnum.ChaZuo, msg,otherId);
								}else{
									commonService.pushAndroidMessage(ClientEnum.ChaZuo, msg,otherId);
								}
							}
						}
					}
					memberService.saveBusinessLoginDetail(detail);
				} else {
					return error("密码错误!");
				}
				// 封装返回信息
				LoginInfo login = new LoginInfo();
				login.setAvatar(commonService.appenUrl(employee.getAvatar()));
	//			login.setAccessToken(employee.getAccessToken());
				login.setId(employee.getId());
				login.setMobile(mobile);
				login.setNickname(employee.getName());
				login.setIfBusiness(employee.getIfBusiness());
				login.setCommodityId(employee.getCommodityId());
				//判断用户是否为自注册用户，如果是，则查询会员过期日期
				if(employee.getIfBusiness()==1){
				    MemberPayInfo payInfo = businessService.queryMemberPayByEmployeeId(employee.getId());
				    if(payInfo!=null){
				    	Long monthCount = businessService.queryMonthCountByEmployeeId(employee.getId());
				    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//			    	Date now = sdf.parse(payInfo.getCreateAt());
				    	Calendar calendar = Calendar.getInstance();
				    	calendar.setTime(payInfo.getCreateAt());
				    	calendar.add(Calendar.MONTH,  Integer.valueOf(monthCount.toString()));
				    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				    	System.out.println(sdf.format(calendar.getTime()));
				    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				    	Date now = new Date();
				    	Date dateTime1 = dateFormat.parse(df.format(now));
	//			    	//会员过期时间
				        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
				        int i = dateTime1.compareTo(dateTime2);  
				        System.out.println(i < 0);
				        //是否过期
				        if(i < 0){
				        	login.setOverdueDate(sdf.format(calendar.getTime()));
				        }else{
				        	login.setOverdueDate(null);
				        }
				    }else{
				    	login.setOverdueDate(null);
				    }
				}else{
	//				login.setOverdueDate(null);
	//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//				Calendar calendar = Calendar.getInstance();
	//				//取账号创建时间
	//		    	calendar.setTime(employee.getCreateAt());
	//		    	//账号创建时间加上会员日期为会员过期日期
	//		    	calendar.add(Calendar.MONTH,  Integer.valueOf(employee.getExpiryDate().toString()));
	//		    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//		    	System.out.println(sdf.format(calendar.getTime()));
	//		    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//		    	Date now = new Date();
	//		    	Date dateTime1 = dateFormat.parse(df.format(now));
	////		    	//会员过期时间
	//		        Date dateTime2 = dateFormat.parse(df.format(calendar.getTime()));
	//		        int i = dateTime1.compareTo(dateTime2);  
	//		        System.out.println(i < 0);
	//		        //是否过期
	//		        if(i < 0){
	//		        	login.setOverdueDate(sdf.format(calendar.getTime()));
	//		        }else{
	//		        	login.setOverdueDate(null);
	//		        }
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					//判断t_business_member_record表中是否有效期内的好多课会员数据存在
					List<MemberRecord> recordList = businessService.findMemberRecordList(employee.getId().toString());
					//判断是否有系列课会员权限存在
					List<MemberRecord> rlist = businessService.findMemberRecordListByEmployeeId(employee.getId().toString());
					if(recordList.size()>0){
						employee.setOverdueDate(sdf.format(recordList.get(0).getEndDate()));
					}else if(rlist.size()>0){
						employee.setOverdueDate(sdf.format(rlist.get(0).getEndDate()));
					}else{
						employee.setOverdueDate(null);
					}
				}
				//判断t_business_member_record表中是否有效期内的好多课会员数据存在
				List<MemberRecord> recordList = businessService.findMemberRecordList(employee.getId().toString());
				if(recordList.size()>0){
					login.setIfBusiness(0L);
				}else{
					//如果不是年费会员就循环取出课程包ID，用于判断会员表中是否存在该学员与课程包绑定的会员记录存在
					 if(businessService.findMemberRecordListByEmployeeId(employee.getId().toString()).size()>0){
						 login.setIfBusiness(99999L);
					 }else{
						 login.setIfBusiness(1L);
					 }
				}
				return ok("登录成功!", login);
			
			}
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: updateBusinessUserAvatar 
	 * @Description: (修改企业用户头像) 
	 * @param request
	 * @param employeeId
	 * @return
	 */
	@Deprecated
	@ResponseBody
	@RequestMapping("/updateBusinessUserAvatar")
	public JSONObject updateBusinessUserAvatar(HttpServletRequest request,String employeeId,String client,String version,String sign,String deviceId){
		if(("2.0.5").equals(version) || ("2.0.1").equals(version)||("2.0.4").equals(version)){
			if(StringUtils.isNullOrEmpty(employeeId) || StringUtils.isNullOrEmpty(sign) || StringUtils.isNullOrEmpty(deviceId)){
				return status(99,"参数缺失");
			}
			//解密
			UpAES upAES = new UpAES();
//			upAES.entry("1");
			System.out.println("解密后的employeeId："+upAES.disEntry(employeeId));
			System.out.println("解密后的deviceId："+upAES.disEntry(deviceId));
			String newEmployeeId = upAES.disEntry(employeeId);
			String newDeviceId =upAES.disEntry(deviceId.toString());
			//重新赋值deviceId
			deviceId =upAES.disEntry(deviceId);
			String newSign = PasswdEncryption.MD5(newEmployeeId+newDeviceId);
			newSign= newSign.substring(5, newSign.length()-5);
			System.out.println("得到的新Sign:"+newSign);
			if(newSign.equals(sign)){
				try{
					Employee employee = new Employee();
					//头像上传
					MultipartFile file = null;
					try{
						MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
						file = multipartRequest.getFile("file");
					}catch(Exception e){
						//无头像上传
					}
					employee.setId(Long.parseLong(newEmployeeId));
					if(file == null){
						employee.setAvatar(null);
					}else{
						employee.setAvatar(uploadFile.upload(file, "avatar"));
					}
//					Member m = memberService.findMemberById(memberId);
//					MemberInfo i = memberService.findMemberInfoByMemberId(memberId);
					if(memberService.saveEmployeeInfo(employee)){
						Employee newemployee = memberService.BusinessUserById(Long.parseLong(newEmployeeId));
						String avatarAddress = commonService.appenUrl(newemployee.getAvatar());
						return ok("操作成功",avatarAddress);
					}
					return error("操作失败");
				}catch(Exception e){
					e.printStackTrace();
					return error(ADMIN_SYSTEM_ERROR);
				}
			}else{
				return status(97,"sign验证出错");
			}
		}else{
			try{
				Employee employee = new Employee();
				//头像上传
				MultipartFile file = null;
				try{
					MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
					file = multipartRequest.getFile("file");
				}catch(Exception e){
					//无头像上传
				}
				employee.setId(Long.parseLong(employeeId));
				if(file == null){
					employee.setAvatar(null);
				}else{
					employee.setAvatar(uploadFile.upload(file, "avatar"));
				}
//				Member m = memberService.findMemberById(memberId);
//				MemberInfo i = memberService.findMemberInfoByMemberId(memberId);
				if(memberService.saveEmployeeInfo(employee)){
					Employee newemployee = memberService.BusinessUserById(Long.parseLong(employeeId));
					String avatarAddress = commonService.appenUrl(newemployee.getAvatar());
					return ok("操作成功",avatarAddress);
				}
				return error("操作失败");
			}catch(Exception e){
				e.printStackTrace();
				return error(ADMIN_SYSTEM_ERROR);
			}
		}
		
	}
	/**
	 * @Title: updateBusinessUserPassword 
	 * @Description: (修改企业用户密码) 
	 * @param oldPassword
	 * @param employeeId
	 * @return
	 */
	@Deprecated
	@ResponseBody
	@RequestMapping("/updateBusinessUserPassword")
	public JSONObject updateBusinessUserPassword(String oldPassword,String newPassword,String employeeId,String client,String version,String sign,String deviceId){
		if(("2.0.5").equals(version) || ("2.0.1").equals(version)||("2.0.4").equals(version)){
			//解密
			UpAES upAES = new UpAES();
//			upAES.entry("1");
			System.out.println("解密后的employeeId："+upAES.disEntry(employeeId));
			System.out.println("解密后的deviceId："+upAES.disEntry(deviceId));
			String newEmployeeId = upAES.disEntry(employeeId);
			String newDeviceId =upAES.disEntry(deviceId.toString());
			//重新赋值deviceId
			deviceId =upAES.disEntry(deviceId);
			String newSign = PasswdEncryption.MD5(newEmployeeId+newDeviceId);
			newSign= newSign.substring(5, newSign.length()-5);
			System.out.println("得到的新Sign:"+newSign);
			if(newSign.equals(sign)){
				Employee employee = memberService.BusinessUserById(Long.parseLong(newEmployeeId));
				try{
					if(employee.getPassword().equals(PasswdEncryption.MD5(oldPassword))
							|| PasswdEncryption.verify(oldPassword, employee.getPassword())) {
						employee.setPassword(PasswdEncryption.generate(newPassword.trim()));
						if(memberService.updateBusinessUserPassword(employee)){
							return status(200,"修改密码成功");
						}
						return error("修改密码失败");
					}else{
						return status(113,"原密码错误！");
					}
				}catch(Exception e){
					e.printStackTrace();
					return error(ADMIN_SYSTEM_ERROR);
				}
			}else{
				return status(97,"sign验证出错");
			}
		}else{
			Employee employee = memberService.BusinessUserById(Long.parseLong(employeeId));
			try{
				if(employee.getPassword().equals(PasswdEncryption.MD5(oldPassword))
						|| PasswdEncryption.verify(oldPassword, employee.getPassword())) {
					employee.setPassword(PasswdEncryption.generate(newPassword.trim()));
					if(memberService.updateBusinessUserPassword(employee)){
						return ok("修改密码成功");
					}
					return error("修改密码失败");
				}else{
					return error("原密码错误！");
				}
			}catch(Exception e){
				e.printStackTrace();
				return error(ADMIN_SYSTEM_ERROR);
			}
		}
		
	}
	/**
	 * @Title: updateBusinessUserName 
	 * @Description: (修改企业用户密码) 
	 * @param oldPassword
	 * @param employeeId
	 * @return
	 */
	@Deprecated
	@ResponseBody
	@RequestMapping("/updateBusinessUserName")
	public JSONObject updateBusinessUserName(String name,String employeeId,String client,String version,String sign,String deviceId){
		if(("2.0.5").equals(version) || ("2.0.1").equals(version)||("2.0.4").equals(version)){
			if(StringUtils.isNullOrEmpty(employeeId) || StringUtils.isNullOrEmpty(employeeId) || StringUtils.isNullOrEmpty(sign) || StringUtils.isNullOrEmpty(deviceId)){
				return status(99,"参数缺失");
			}
			//解密
			UpAES upAES = new UpAES();
//			upAES.entry("1");
			System.out.println("解密后的employeeId："+upAES.disEntry(employeeId));
			System.out.println("解密后的deviceId："+upAES.disEntry(deviceId));
			String newEmployeeId = upAES.disEntry(employeeId);
			String newDeviceId =upAES.disEntry(deviceId.toString());
			//重新赋值deviceId
			deviceId =upAES.disEntry(deviceId);
			String newSign = PasswdEncryption.MD5(newEmployeeId+newDeviceId);
			newSign= newSign.substring(5, newSign.length()-5);
			System.out.println("得到的新Sign:"+newSign);
			if(newSign.equals(sign)){
				Employee employee = new Employee();
				try{
					employee.setName(name);
					employee.setId(Long.parseLong(newEmployeeId));
					if(memberService.updateBusinessUserName(employee)){
						return okNew("修改昵称成功",name);
					}
					return error("修改昵称失败");
				}catch(Exception e){
					e.printStackTrace();
					return error(ADMIN_SYSTEM_ERROR);
				}
			}else{
				return status(97,"sign验证出错");
			}
		}else{
			Employee employee = new Employee();
			try{
				employee.setName(name);
				employee.setId(Long.parseLong(employeeId));
				if(memberService.updateBusinessUserName(employee)){
					return ok("修改昵称成功",name);
				}
				return error("修改昵称失败");
			}catch(Exception e){
				e.printStackTrace();
				return error(ADMIN_SYSTEM_ERROR);
			}
		}
		
	}

	/**
	 * @Title: resetEmployeePassword
	 * @Description: (重置企业用户密码)
	 * @param code 验证码
	 * @param mobile  手机号
	 * @param password  密码
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/resetEmployeePassword")
	public JSONObject resetEmployeePassword(String code, String mobile, String password,String sign,String deviceId,String version) {
		if(("2.0.5").equals(version) || ("2.0.1").equals(version)||("2.0.4").equals(version)){
			if(StringUtils.isNullOrEmpty(mobile) || StringUtils.isNullOrEmpty(sign) || StringUtils.isNullOrEmpty(deviceId)){
				return status(99,"参数缺失");
			}
			//解密
			UpAES upAES = new UpAES();
//			upAES.entry("1");
			System.out.println("解密后的mobile："+upAES.disEntry(mobile));
			System.out.println("解密后的deviceId："+upAES.disEntry(deviceId));
			String newMobile = upAES.disEntry(mobile);
			String newDeviceId =upAES.disEntry(deviceId.toString());
			//重新赋值deviceId
			deviceId =upAES.disEntry(deviceId);
			String newSign = PasswdEncryption.MD5(newMobile+newDeviceId);
			newSign= newSign.substring(5, newSign.length()-5);
			System.out.println("得到的新Sign:"+newSign);
			if(newSign.equals(sign)){
				try {
					//查询十分钟内此手机号、此验证码的记录是否存在
					Code smsCode = codeService.findCode(code, newMobile);
					if (smsCode == null) {
						return status(102,"验证码输入错误!");
					}
					Employee employee = memberService.findBusinessLoginMemberByMobile1(newMobile);
					if (employee == null) {
						return status(112,"用户不存在");
					}
					employee.setPassword(PasswdEncryption.generate(password));
					if(memberService.updateBusinessUserPassword(employee)){
						return ok("修改密码成功");
					}
					return error("修改密码失败");
				} catch (Exception e) {
					e.printStackTrace();
					return error(APP_SYSTEM_ERROR);
				}
			}else{
				return status(97,"sign验证出错");
			}
		}else{
			try {
				if (StringUtils.isNullOrEmpty(code) || StringUtils.isNullOrEmpty(mobile)
						|| StringUtils.isNullOrEmpty(password)) {
					return error("参数缺失!");
				}
				//查询十分钟内此手机号、此验证码的记录是否存在
				Code smsCode = codeService.findCode(code, mobile);
				if (smsCode == null) {
					return error("验证码输入错误!");
				}
				Employee employee = memberService.findBusinessLoginMemberByMobile1(mobile);
				if (employee == null) {
					return error("用户不存在");
				}
				employee.setPassword(PasswdEncryption.generate(password));
				if(memberService.updateBusinessUserPassword(employee)){
					return ok("修改密码成功");
				}
				return error("修改密码失败");
			} catch (Exception e) {
				e.printStackTrace();
				return error(APP_SYSTEM_ERROR);
			}
		}
	}
	/**
	 * @Title: queryEmployeeCloseMsg
	 * @Description: (重置企业用户封号提示)
	 * @param code 验证码
	 * @param mobile  手机号
	 * @param password  密码
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryEmployeeCloseMsg")
	public JSONObject queryEmployeeCloseMsg(String mobile,String deviceId) {
		try {
			//查询该手机号一周以内的登录设备ID
			List<LoginDetail> detail = memberService.queryEmployeeCloseMsg(mobile,deviceId);
			if(detail.size()<2){
				//首先判断当前登录的设备是不是第三台设备
				return status(200, "");
			}else{
				if(detail.size()==2){
					return status(400, "您的帐号将在第3台设备登录，超过3台将被封号，请确认是否登录");
				}else{
					return status(500,"您的登录设备已超过3台，第1次违规，点击登录会被封号1周，请确认是否登录");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	/**
	 * @Title: businessRegister
	 * @Description: (企业APP用户注册)
	 * @param mobile
	 *            手机号
	 * @param password
	 *            密码
	 * @param code
	 *            验证码
	 * @param info
	 *            手机信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping("businessRegister")
	public JSONObject businessRegister(String mobile, String password, String code, PhoneInfo info,String version) {
		try {
			if(!("1.8.4").equals(version)){
//			if(1==1){
				return error("注册功能暂未开通，敬请期待！");
			}
			if (StringUtils.isNullOrEmpty(mobile) || StringUtils.isNullOrEmpty(password)
					|| StringUtils.isNullOrEmpty(code)) {
				return error("参数缺失");
			}
			//AES加密
//			PasswdEncryption.encrypt(mobile, '');
			// 验证用户是否存在
			Employee employee = memberService.findBusinessLoginMemberByMobile(mobile);
			if (employee != null) {
				return error("手机号已经注册过，直接去登录吧");
			}
			Code smsCode = codeService.findCode(code, mobile);
			if (smsCode == null) {
				return error("验证码输入错误!");
			}
			employee = new Employee();
			employee.setMobile(mobile);
			employee.setPassword(PasswdEncryption.generate(password));
			if (mobile.length() > 4) {
				employee.setName("用户" + mobile.substring(mobile.length() - 4));
			} else {
				employee.setName("用户" + mobile);
			}
			// 保存默认头像
			employee.setAvatar(commonService.getRandomAvatar());
			memberService.businessRegister(employee, smsCode,null);
			// 返回登录信息
			LoginInfo login = new LoginInfo();
			login.setAvatar(commonService.appenUrl(employee.getAvatar()));
			login.setId(employee.getId());
			login.setNickname(employee.getName());
			login.setMobile(mobile);
			login.setIfBusiness(employee.getIfBusiness());
			return ok("注册成功", login);
		} catch (Exception e) {
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
}
