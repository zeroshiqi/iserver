package cn.ichazuo.model.table;

import java.util.Date;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: Version 
 * @Description: (版本更新) 
 * @author ZhaoXu
 * @date 2015年8月20日 上午10:49:56 
 * @version V1.0
 */
@Table(name="t_version")
public class Version extends BaseModel{
	private static final long serialVersionUID = 1L;
	private Long id;
	private String version;	//版本
	private Integer weight;	 //权重
	private Date createAt;	//创建时间
	private String client; // 客户端
	private Integer status;	//0:非强制更新  1:强制更新

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
