package cn.ichazuo.commons.authorize;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: AuthorizeGroup
 * @Description: (权限组)
 * @author ZhaoXu
 * @date 2015年6月28日 下午4:22:45
 * @version V1.0
 */
public class AuthorizeGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id; // ID
	private String name; // 名称
	private Integer weight; // 排序值
	private Map<String, AuthorizeProperties> properMap;
	private List<AuthorizeProperties> properList; // 当前资源组中的资源

	/**
	 * @Title: getAuthorizeProperties
	 * @Description: (根据资源id获得资源)
	 * @param pid
	 * @return
	 */
	public AuthorizeProperties getAuthorizeProperties(String pid) {
		return properMap.get(pid);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, AuthorizeProperties> getProperMap() {
		return properMap;
	}

	public void setProperMap(Map<String, AuthorizeProperties> properMap) {
		this.properMap = properMap;
	}

	public List<AuthorizeProperties> getProperList() {
		return properList;
	}

	public void setProperList(List<AuthorizeProperties> properList) {
		this.properList = properList;
	}

	@Override
	public int hashCode() {
		return id.hashCode() + name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AuthorizeGroup) {
			AuthorizeGroup ag = (AuthorizeGroup) obj;
			if (ag.id.equals(this.id) && ag.name.equals(this.name)) {
				return true;
			}
		}
		return false;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}
}
