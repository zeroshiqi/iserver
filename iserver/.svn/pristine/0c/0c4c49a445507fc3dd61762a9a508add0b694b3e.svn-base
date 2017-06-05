package cn.ichazuo.dao;

import java.util.Map;

import cn.ichazuo.model.table.Code;

/**
 * @ClassName: CodeDao 
 * @Description: (验证码DAO) 
 * @author ZhaoXu
 * @date 2015年7月20日 下午10:41:07 
 * @version V1.0
 */
public interface CodeDao {

	/**
	 * @Title: saveCode 
	 * @Description: (保存验证码) 
	 * @param code 验证码
	 * @return
	 */
	public int saveCode(Code code);
	
	/**
	 * @Title: updateCode 
	 * @Description: (修改验证码状态) 
	 * @param code 验证码
	 * @return
	 */
	public int updateCode(Code code);
	
	/**
	 * @Title: findCodeByMobile 
	 * @Description: (根据验证码和手机号查询验证码对象) 
	 * @param params 参数
	 * @return
	 */
	public Code findCodeByParams(Map<String,Object> params);
}
