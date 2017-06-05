package cn.ichazuo.commons.util.model;

import java.io.Serializable;

/**
 * ClassName: Page 
 * Description: (分页) 
 * @author ZhaoXu
 * @date 2015年6月28日 下午4:21:17 
 * @version V1.0
 */
public class Page implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 默认显示条数
	 */
	private static int DEFAULT_ROWS = 30;

	private Integer page;
	private Integer rows;
	
	/**
	 * 
	 * Title: getNowPage 
	 * Description: (获得当前页) 
	 * @return
	 */
	public int getNowPage() {
		return page == null || page <= 0 ? 1 : page;
	}

	/**
	 * 
	 * Title: getNumber 
	 * Description: (获得每显示条数) 
	 * @return
	 */
	public int getNumber() {
		return rows == null || rows <= 0 ? DEFAULT_ROWS : rows;
	}
	
	/**
	 * 获得每页的开始记录
	 * 
	 * @return
	 */
	public int getStartNumber() {
		return (getNowPage() - 1) * getNumber();
	}

	//  get set
	public void setPage(Integer page) {
		this.page = page;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}
	
}
