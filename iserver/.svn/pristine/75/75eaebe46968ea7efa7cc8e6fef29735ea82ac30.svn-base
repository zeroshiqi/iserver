package cn.ichazuo.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ichazuo.commons.base.BaseService;
import cn.ichazuo.commons.component.CacheInfo;
import cn.ichazuo.commons.component.RedisClient;
import cn.ichazuo.dao.CompanyDao;
import cn.ichazuo.model.Dictionary;
import cn.ichazuo.model.table.Company;
import cn.ichazuo.model.table.CompanyImage;
import cn.ichazuo.model.table.CompanyInfo;
import cn.ichazuo.model.table.CompanyJob;

/**
 * @ClassName: CompanyService 
 * @Description: (公司Service) 
 * @author ZhaoXu
 * @date 2015年7月19日 上午12:25:19 
 * @version V1.0
 */
@Service("companyService")
public class CompanyService extends BaseService{
	private static final long serialVersionUID = 1L;
	@Resource
	private CompanyDao companyDao;
	@Autowired
	private CacheInfo cacheInfo;
	@Autowired
	private RedisClient redisClient;
	
	/**
	 * @Title: findAllCompany 
	 * @Description: (查询全部公司) 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Dictionary> findAllCompany(){
		List<Dictionary> list = null;
		
		String key = cacheInfo.getCacheCompanyAllListKey();
		if(redisClient.isExist(key)){
			list = (List<Dictionary>)redisClient.getObject(key);
		}else{
			list = companyDao.findAllCompanyName();
			redisClient.setObject(key, list, cacheInfo.getRedisCacheLevel6());
		}
		return list;
	}
	
	/**
	 * @Title: findAllCompanyJob 
	 * @Description: (查询全部公司职位) 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Dictionary> findAllCompanyJob(){
		List<Dictionary> list = null;
		String key = cacheInfo.getCacheCompanyJobAllListKey();
		if(redisClient.isExist(key)){
			list = (List<Dictionary>)redisClient.getObject(key);
		}else{
			list = companyDao.findAllCompanyJobName();
			redisClient.setObject(key, list, cacheInfo.getRedisCacheLevel6());
		}
		
		return list;
	}
	
	/**
	 * @Title: saveCompany 
	 * @Description: (保存公司) 
	 * @param company
	 * @return
	 */
	public boolean saveCompany(Company company){
		return companyDao.saveCompany(company) > 0;
	}
	
	/**
	 * @Title: saveCompanyInfo 
	 * @Description: (保存公司信息) 
	 * @param companyInfo
	 * @return
	 */
	public boolean saveCompanyInfo(CompanyInfo companyInfo){
		return companyDao.saveCompanyInfo(companyInfo) > 0;
	}
	
	/**
	 * @Title: saveCompanyImage 
	 * @Description: (保存公司照片) 
	 * @param image
	 * @return
	 */
	public boolean saveCompanyImage(CompanyImage image){
		return companyDao.saveCompanyImage(image) > 0;
	}
	
	/**
	 * @Title: saveCompanyJob 
	 * @Description: (保存公司职位) 
	 * @param job
	 * @return
	 */
	public boolean saveCompanyJob(CompanyJob job){
		return companyDao.saveCompanyJob(job) > 0;
	}
	
	/**
	 * @Title: updateCompany 
	 * @Description: (修改公司) 
	 * @param company
	 * @return
	 */
	public boolean updateCompany(Company company){
		return companyDao.updateCompany(company) > 0;
	}
}
