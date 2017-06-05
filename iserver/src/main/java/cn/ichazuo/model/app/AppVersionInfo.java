package cn.ichazuo.model.app;

import cn.ichazuo.commons.base.BaseResult;

/**
 * @ClassName: AppVersionInfo
 * @Description: (版本更新返回信息)
 * @author ZhaoXu
 * @date 2015年8月20日 上午11:01:06
 * @version V1.0
 */
public class AppVersionInfo extends BaseResult {
	private static final long serialVersionUID = 1L;

	private Integer status; // 1 : 更新 0 : 不更新
	private Integer force; // 1 : 强制 0: 不强制更新
	private String msg;	//提示消息

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getForce() {
		return force;
	}

	public void setForce(Integer force) {
		this.force = force;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
