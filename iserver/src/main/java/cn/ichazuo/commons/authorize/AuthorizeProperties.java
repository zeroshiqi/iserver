package cn.ichazuo.commons.authorize;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: AuthorizeProperties
 * @Description: (权限资源)
 * @author ZhaoXu
 * @date 2015年6月28日 下午4:22:35
 * @version V1.0
 */
public class AuthorizeProperties implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id; // id
	private String name; // 名称
	private String action; // action
	private String title; // 菜单显示的名称
	private boolean showMenu; // 是否显示为菜单
	private Integer weight; // 权重,排序用.越小越靠前
	private AuthorizeGroup authorizeGroup;// 所属权限组

	private Map<String, AuthorizeButton> buttonMap;
	private List<AuthorizeButton> buttonList; // 当前资源中的button

	/**
	 * @Title: getAuthorizeButtonById
	 * @Description: (根据buttonId获得Button)
	 * @param bid
	 * @return
	 */
	public AuthorizeButton getAuthorizeButtonById(String bid) {
		if (buttonMap == null) {
			return null;
		}
		return buttonMap.get(bid);
	}

	/* get set */
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
		this.name = name == null ? null : name.trim();
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action == null ? null : action.trim();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isShowMenu() {
		return showMenu;
	}

	public void setShowMenu(boolean showMenu) {
		this.showMenu = showMenu;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Map<String, AuthorizeButton> getButtonMap() {
		return buttonMap;
	}

	public void setButtonMap(Map<String, AuthorizeButton> buttonMap) {
		this.buttonMap = buttonMap;
	}

	public List<AuthorizeButton> getButtonList() {
		return buttonList;
	}

	public void setButtonList(List<AuthorizeButton> buttonList) {
		this.buttonList = buttonList;
	}

	public AuthorizeGroup getAuthorizeGroup() {
		return authorizeGroup;
	}

	public void setAuthorizeGroup(AuthorizeGroup authorizeGroup) {
		this.authorizeGroup = authorizeGroup;
	}

}
