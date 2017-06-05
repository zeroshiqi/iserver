package cn.ichazuo.model.table;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: LoginDetail
 * @Description: (登录明细表)
 * @author ZhaoXu
 * @date 2015年7月18日 下午2:28:06
 * @version V1.0
 */
@Table(name = "t_business_block_detail")
public class BlockDetail extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id; // id
	private String employeeId; // 登录人Id
	private String blockAt; // 封号时间
	private String unblockAt; // 解封时间
	private String blockTime;	//封号时长
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getBlockAt() {
		return blockAt;
	}
	public void setBlockAt(String blockAt) {
		this.blockAt = blockAt;
	}
	public String getUnblockAt() {
		return unblockAt;
	}
	public void setUnblockAt(String unblockAt) {
		this.unblockAt = unblockAt;
	}
	public String getBlockTime() {
		return blockTime;
	}
	public void setBlockTime(String blockTime) {
		this.blockTime = blockTime;
	}
	
}
