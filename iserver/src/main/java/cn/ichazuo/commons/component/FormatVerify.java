package cn.ichazuo.commons.component;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

/**
 * @ClassName: FormatVerify 
 * @Description: (格式验证) 
 * @author ZhaoXu
 * @date 2015年7月18日 下午10:06:49 
 * @version V1.0
 */
@Component("formatVerify")
public class FormatVerify {
	
	/**
	 * @Title: verifyEmail 
	 * @Description: (验证Email是否符合格式) 
	 * @param email
	 * @return
	 */
	public boolean verifyEmail(String email){
		Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		if(pattern.matcher(email).matches()){
			return true;
		}
		return false;
	}
	
	/**
	 * @Title: verifyMobile 
	 * @Description: (验证手机号是否符合格式) 
	 * @param mobile
	 * @return
	 */
	public boolean verifyMobile(String mobile) {
		Pattern pattern = Pattern.compile("^((13[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
		if(pattern.matcher(mobile).matches()){
			return true;
		}
		return false;
	}
	
}
