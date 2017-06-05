package cn.ichazuo.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.ichazuo.commons.base.BaseService;
import cn.ichazuo.commons.util.model.Params;
import cn.ichazuo.dao.CodeDao;
import cn.ichazuo.model.table.Code;

/**
 * @ClassName: CodeService 
 * @Description: (短信验证码Service) 
 * @author ZhaoXu
 * @date 2015年7月18日 下午10:49:27 
 * @version V1.0
 */
@Service("codeService")
public class CodeService extends BaseService{
	private static final long serialVersionUID = 1L;

	@Resource
	private CodeDao codeDao;
	
	/**
	 * @Title: saveCode 
	 * @Description: (保存验证码) 
	 * @param mobile 手机号
	 * @param code 验证码
	 * @return
	 */
	public boolean saveCode(String mobile,String code){
		Code smsCode = new Code();
		smsCode.setCode(code);
		smsCode.setMobile(mobile);
		
		return codeDao.saveCode(smsCode) > 0;
	}
	
	/**
	 * @Title: updateCode 
	 * @Description: (更新验证码状态) 
	 * @param code 验证码
	 */
	public boolean updateCodeStatus(Code code){
		if(code == null){
			return true;
		}
		return codeDao.updateCode(code) > 0;
	}
	
	/**
	 * @Title: findCode 
	 * @Description: (根据验证码,手机号查询code对象) 
	 * @param code
	 * @param mobile
	 * @return
	 */
	public Code findCode(String code, String mobile) {
		Params params = new Params();
		params.putData("code", code);
		params.putData("mobile", mobile);
		return codeDao.findCodeByParams(params.getMap());
	}
}
