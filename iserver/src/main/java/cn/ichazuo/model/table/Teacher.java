package cn.ichazuo.model.table;

import java.util.Date;
import java.util.List;

import javax.persistence.Table;

import cn.ichazuo.commons.base.BaseModel;

/**
 * @ClassName: Catalog
 * @Description: (课程分类表)
 * @author ZhaoXu
 * @date 2015年7月18日 下午2:28:06
 * @version V1.0
 */
@Table(name = "t_teacher")
public class Teacher extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id; // id
	private String name; // 讲师姓名
	private String title;	//标题
	private String type; // 服务类型
	private String createAt; //创建时间
	private String updateAt; // 修改时间
	
	private String content;//简介
	private String avatar;//头像
	private String cover;//封面
	private String cover1;
	private String info;//课程定义
	private String weight;//显示权重
	private String job;//职位
	private String bewrite;//描述
	
	private String catalogId;
	private String catalogName;
	private String flag;
	
	private String ifBusiness;
	
	private String shareAddress;
	//课程列表
	private List catalogList;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCreateAt() {
		return createAt;
	}
	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}
	public String getUpdateAt() {
		return updateAt;
	}
	public void setUpdateAt(String updateAt) {
		this.updateAt = updateAt;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getBewrite() {
		return bewrite;
	}
	public void setBewrite(String bewrite) {
		this.bewrite = bewrite;
	}
	public String getCatalogId() {
		return catalogId;
	}
	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}
	public String getCatalogName() {
		return catalogName;
	}
	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public List getCatalogList() {
		return catalogList;
	}
	public void setCatalogList(List catalogList) {
		this.catalogList = catalogList;
	}
	public String getCover1() {
		return cover1;
	}
	public void setCover1(String cover1) {
		this.cover1 = cover1;
	}
	public String getIfBusiness() {
		return ifBusiness;
	}
	public void setIfBusiness(String ifBusiness) {
		this.ifBusiness = ifBusiness;
	}
	public String getShareAddress() {
		return shareAddress;
	}
	public void setShareAddress(String shareAddress) {
		this.shareAddress = shareAddress;
	}
	
}
