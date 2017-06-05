package cn.ichazuo.model.app;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.ichazuo.commons.base.BaseResult;
import cn.ichazuo.commons.util.DateUtils;

/**
 * @ClassName: OnlineCourseListInfo
 * @Description: (线上课程列表信息)
 * @author ZhaoXu
 * @date 2015年7月20日 上午11:43:05
 * @version V1.0
 */
public class DictItem extends BaseResult {
	private static final long serialVersionUID = 1L;
	private Long id; // 字典ID
	private String value; // 字典名称
	private String dictId; // 父级分类Id
	private String weight; // 优先级
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDictId() {
		return dictId;
	}
	public void setDictId(String dictId) {
		this.dictId = dictId;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	

}
