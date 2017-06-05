package cn.ichazuo.commons.util.model;

import java.io.Serializable;
import java.util.List;

import cn.ichazuo.model.table.Employee;

/**
 * @ClassName: PhoneInfo
 * @Description: (手机信息)
 * @author ZhaoXu
 * @date 2015年7月4日 下午12:22:35
 * @version V1.0
 */
public class EModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private List list; // 前20
	private Employee person; // 自己
	public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
	public Employee getPerson() {
		return person;
	}
	public void setPerson(Employee person) {
		this.person = person;
	}
	
}
