package cn.ichazuo.commons.authorize;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: AuthorizeCfg
 * @Description: (权限操作类)
 * @author ZhaoXu
 * @date 2015年6月28日 下午4:22:54
 * @version V1.0
 */
public final class AuthorizeCfg {
	private static Logger logger = LoggerFactory.getLogger(AuthorizeCfg.class);

	/**
	 * @ClassName: AuthorizeEnum
	 * @Description: (资源类型枚举)
	 * @author ZhaoXu
	 * @date 2015年6月28日 下午4:24:05
	 * @version V1.0
	 */
	public enum AuthorizeEnum {
		Button, Properties;
	}

	// 权限文件名称
	private static final String authorXmlName = "authorize.xml";
	private static AuthorizeCfg author = null;

	private Map<String, AuthorizeGroup> groupMap = null;
	private Map<String, AuthorizeProperties> actionMap = null;
	private List<AuthorizeGroup> groupList;

	/**
	 * @Title: getInstance
	 * @Description: (获得AuthorCfg实例)
	 * @return
	 * @author ZhaoXu
	 */
	public static AuthorizeCfg getInstance() {
		if (author == null) {
			init();
			logger.info("init Authorize...");
		}
		return author;
	}

	/**
	 * @Title: destory
	 * @Description: (销毁AuthorCfg实例)
	 * @author ZhaoXu
	 */
	public static void destory() {
		if (author != null) {
			// 清空groupMap
			if (author.actionMap != null) {
				author.actionMap.clear();
				author.actionMap = null;
			}
			// 清空groupList
			if (author.groupList != null) {
				author.groupList.clear();
				author.groupList = null;
			}

			if (author.actionMap != null) {
				author.actionMap.clear();
				author.actionMap = null;
			}
			author = null;
		}
		logger.info("destory Authorize...");
	}

	/**
	 * @Title: getAuthorizeGroupById
	 * @Description: (根据资源组id获得资源组)
	 * @param gid
	 *            资源组
	 * @return
	 */
	public AuthorizeGroup getAuthorizeGroupById(String gid) {
		if (groupMap == null) {
			return null;
		}
		return groupMap.get(gid);
	}

	/**
	 * @Title: getAuthorizePropertiesById
	 * @Description: (根据资源组id,资源id获得资源)
	 * @param gid
	 *            组id
	 * @param pid
	 *            资源id
	 * @return
	 */
	public AuthorizeProperties getAuthorizePropertiesById(String gid, String pid) {
		AuthorizeProperties proper = null;
		// 根据groupId获得group
		AuthorizeGroup group = getAuthorizeGroupById(gid);
		if (group != null) {
			// 根据propertiesId 获得 properties
			proper = group.getAuthorizeProperties(pid);
		}
		return proper;
	}

	/**
	 * @Title: getAuthorizeButtonById
	 * @Description: (根据资源组Id,资源Id,buttonId获得button)
	 * @param gid
	 *            资源组id
	 * @param pid
	 *            资源id
	 * @param bid
	 *            按钮id
	 * @return
	 */
	public AuthorizeButton getAuthorizeButtonById(String gid, String pid, String bid) {
		AuthorizeButton button = null;
		// 取得Properties
		AuthorizeProperties proper = getAuthorizePropertiesById(gid, pid);
		if (proper != null) {
			// 根据buttonId获得button
			button = proper.getAuthorizeButtonById(bid);
		}
		return button;
	}

	/**
	 * @Title: getAuthorizePropertiesByAction
	 * @Description: (根据action 获得对应资源)
	 * @param action
	 * @return
	 */
	public AuthorizeProperties getAuthorizePropertiesByAction(String action) {
		if (actionMap == null) {
			return null;
		}
		return actionMap.get(action);
	}

	/**
	 * @Title: init
	 * @Description: (初始化类型)
	 */
	private static void init() {
		if (author == null) {
			author = new AuthorizeCfg();
		}
		Map<String, AuthorizeGroup> groupMap = new HashMap<String, AuthorizeGroup>();
		Map<String, AuthorizeProperties> actionMap = new HashMap<String, AuthorizeProperties>();
		// 读取配置文件获得所有资源组
		List<AuthorizeGroup> groupList = initGroup(authorXmlName);

		groupList.forEach(group -> {
			// 将资源map放入group中,方便取出
			HashMap<String, AuthorizeProperties> properMap = new HashMap<String, AuthorizeProperties>();
			group.getProperList().forEach(proper -> {
				properMap.put(proper.getId(), proper);
				actionMap.put(proper.getAction(), proper);

				// 将buttonMap放入properties中,方便取出
				HashMap<String, AuthorizeButton> buttonMap = new HashMap<String, AuthorizeButton>();
				proper.getButtonList().forEach(button -> buttonMap.put(button.getId(), button));
				proper.setButtonMap(buttonMap);

			});
			group.setProperMap(properMap);
			groupMap.put(group.getId(), group);
		});
		author.setActionMap(actionMap);
		author.setGroupMap(groupMap);
		author.setGroupList(groupList);
	}

	/**
	 * @Title: initGroup
	 * @Description: (读取配置文件,初始化资源组)
	 * @param xmlFileName
	 *            配置文件名称
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static List<AuthorizeGroup> initGroup(String xmlFileName) {
		List<AuthorizeGroup> groupList = new ArrayList<AuthorizeGroup>();
		// 获得xml文件的根节点
		Element root = getRootElement(xmlFileName);
		if (root == null) {
			return groupList;
		}
		// 获得配置文件中所有节点
		root.elements().forEach(e -> {
			Element groupElement = (Element) e;
			// 判断此节点是否为group节点
			if (groupElement.getName().equals("group")) {
				// 添加group
				AuthorizeGroup group = new AuthorizeGroup();
				group.setId(groupElement.attributeValue("id"));
				group.setName(groupElement.attributeValue("name"));
				String weight = groupElement.attributeValue("weight");

				if (weight == null || weight.equals("")) {
					group.setWeight(Integer.MAX_VALUE);
				} else {
					group.setWeight(Integer.parseInt(weight));
				}
				// 盛放此节点下的所有资源
				List<AuthorizeProperties> properList = new ArrayList<AuthorizeProperties>();

				// 获得当前组下所有节点
				groupElement.elements().stream().filter(ele -> ((Element) ele).getName().equals("properties"))
						.forEach(ele -> {
					Element properElement = (Element) ele;
					AuthorizeProperties proper = getAuthorizeProperties(properElement);
					proper.setAuthorizeGroup(group); // 添加所属组

					properList.add(proper);
				});
				group.setProperList(properList);
				groupList.add(group);
			} else if (groupElement.getName().equals("include")) { // 判断是否为include节点
				// 取得include的文件名
				String xmlName = groupElement.attributeValue("file");

				// 添加到组列表中
				groupList.addAll(initGroup(xmlName));
			}
		});
		return groupList;
	}

	/**
	 * @Title: getAuthorizeProperties
	 * @Description: (从节点中取得properties对象)
	 * @param element
	 *            节点
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static AuthorizeProperties getAuthorizeProperties(Element element) {
		AuthorizeProperties proper = new AuthorizeProperties();

		// 从节点中取数据放入对象中
		proper.setAction(element.attributeValue("action"));
		proper.setId(element.attributeValue("id"));
		proper.setShowMenu("true".equals(element.attributeValue("showMenu")));
		proper.setName(element.attributeValue("name"));
		proper.setTitle(element.attributeValue("title"));
		String weight = element.attributeValue("weight");
		if (weight == null || weight.equals("")) {
			proper.setWeight(Integer.MAX_VALUE);
		} else {
			proper.setWeight(Integer.parseInt(weight));
		}

		// 盛放此节点下所有button
		List<AuthorizeButton> buttonList = new ArrayList<AuthorizeButton>();

		// 获得当前节点下所有子节点
		element.elements().stream().filter(ele -> ((Element) ele).getName().equals("button")).forEach(buttonElement -> {
			Element btnElement = (Element) buttonElement;
			AuthorizeButton btn = getAuthorizeButton(btnElement);
			btn.setProper(proper); // 设置所属资源

			// 添加button对象
			buttonList.add(btn);
		});

		proper.setButtonList(buttonList);
		return proper;
	}

	/**
	 * @Title: getAuthorizeButton
	 * @Description: (从节点中取得button对象)
	 * @param element
	 *            节点
	 * @return
	 */
	private static AuthorizeButton getAuthorizeButton(Element element) {
		AuthorizeButton button = new AuthorizeButton();
		// 从节点取出数据放入对象中
		button.setId(element.attributeValue("id"));
		button.setIcon(element.attributeValue("icon"));
		button.setName(element.attributeValue("name"));
		button.setFunction(element.attributeValue("function"));
		return button;
	}

	/**
	 * @Title: getRootElement
	 * @Description: (获得xml文件中的根节点)
	 * @param xmlFileName
	 *            xml名称
	 * @return
	 */
	private static Element getRootElement(String xmlFileName) {
		SAXReader reader = new SAXReader();
		Document doc = null;
		// 获得当前路径
		String path = AuthorizeCfg.class.getResource("/").getPath();
		try {
			File file = null;
			try {
				file = new File(path, xmlFileName);
			} catch (Exception e) {
				file = new File(path, File.pathSeparator + xmlFileName);
			}
			// 将xml文件读取进来
//			System.out.println(file);
			doc = reader.read(file);

		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		if (doc == null) {
			return null;
		} else {
			// 返回根节点
			return doc.getRootElement();
		}
	}
	
	// 构造
	private AuthorizeCfg() {
	}

	/* get set */
	public Map<String, AuthorizeGroup> getGroupMap() {
		return groupMap;
	}

	public void setGroupMap(Map<String, AuthorizeGroup> groupMap) {
		this.groupMap = groupMap;
	}

	public Map<String, AuthorizeProperties> getActionMap() {
		return actionMap;
	}

	public void setActionMap(Map<String, AuthorizeProperties> actionMap) {
		this.actionMap = actionMap;
	}

	public List<AuthorizeGroup> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<AuthorizeGroup> groupList) {
		this.groupList = groupList;
	}
	
}
