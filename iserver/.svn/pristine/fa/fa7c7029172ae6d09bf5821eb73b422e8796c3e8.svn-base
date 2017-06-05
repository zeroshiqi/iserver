package cn.ichazuo.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.ichazuo.commons.base.BaseService;
import cn.ichazuo.dao.CityDao;
import cn.ichazuo.model.Dictionary;

/**
 * @ClassName: CityService 
 * @Description: (城市service) 
 * @author ZhaoXu
 * @date 2015年7月18日 下午12:41:19 
 * @version V1.0
 */
@Service("cityService")
public class CityService extends BaseService{
	private static final long serialVersionUID = 1L;
	@Resource
	private CityDao cityDao;
	
	/**
	 * @Title: findAllCity 
	 * @Description: (查询全部城市) 
	 * @return
	 */
	public List<Dictionary> findAllCity(){
		return cityDao.findAllCity();
	}
}
