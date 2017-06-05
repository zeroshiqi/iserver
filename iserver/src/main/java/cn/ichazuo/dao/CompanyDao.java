package cn.ichazuo.dao;

import java.util.List;

import cn.ichazuo.model.Dictionary;
import cn.ichazuo.model.table.Company;
import cn.ichazuo.model.table.CompanyImage;
import cn.ichazuo.model.table.CompanyInfo;
import cn.ichazuo.model.table.CompanyJob;

/**
 * @ClassName: CompanyDao 
 * @Description: (公司DAO) 
 * @author ZhaoXu
 * @date 2015年7月21日 上午11:19:58 
 * @version V1.0
 */
public interface CompanyDao {
	
	/**
	 * @Title: findAllCompanyName 
	 * @Description: (查询全部公司名称) 
	 * @return
	 */
	public List<Dictionary> findAllCompanyName();
	
	/**
	 * @Title: findAllCompanyJobName 
	 * @Description: (查询全部职位信息) 
	 * @return
	 */
	public List<Dictionary> findAllCompanyJobName();
	
	/**
	 * @Title: saveCompany 
	 * @Description: (保存公司) 
	 * @param company
	 * @return
	 */
	public int saveCompany(Company company);
	
	/**
	 * @Title: saveCompanyInfo 
	 * @Description: (保存公司信息) 
	 * @param companyInfo
	 * @return
	 */
	public int saveCompanyInfo(CompanyInfo companyInfo);
	
	/**
	 * @Title: saveCompanyImage 
	 * @Description: (保存公司图片) 
	 * @param image
	 * @return
	 */
	public int saveCompanyImage(CompanyImage image);
	
	/**
	 * @Title: saveCompanyJob 
	 * @Description: (保存公司职位) 
	 * @param job
	 * @return
	 */
	public int saveCompanyJob(CompanyJob job);
	
	/**
	 * @Title: updateCompany 
	 * @Description: (修改公司) 
	 * @param company
	 * @return
	 */
	public int updateCompany(Company company);
}
