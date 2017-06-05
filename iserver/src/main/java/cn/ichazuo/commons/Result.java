package cn.ichazuo.commons;

public interface Result {
	/** 返回正确代码: 200 */
	public static final int SUCCESS_STATUS = 200;
	/** 返回未登录代码 300 */
	public static final int NOTLOGIN_STATUS = 300;
	/** 返回未找到代码: 404 */
	public static final int NOTFOUND_STATUS = 404;
	/** 返回错误代码: 500 */
	public static final int ERROR_STATUS = 500;
	/** 返回警告代码: 600 */
	public static final int WARN_STATUS = 600;
	
	/** 是最后一页  */
	public static final int LAST = 1;
	/** 不是最后一页 */
	public static final int NOT_LAST = 0;
	
	/** app端错误返回信息 */
	public static final String APP_SYSTEM_ERROR = "系统异常";
	/** 后台错误返回信息*/
	public static final String ADMIN_SYSTEM_ERROR = "系统错误";
	
	/** 修改失败返回信息 */
	public static final String UPDATE_ERROR = "状态异常,请重试!";
}
