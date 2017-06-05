package cn.ichazuo.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.ichazuo.commons.base.BaseService;
import cn.ichazuo.commons.util.model.Params;
import cn.ichazuo.dao.PlayAddressDao;
import cn.ichazuo.model.Dictionary;
import cn.ichazuo.model.admin.PlayAddressListInfo;
import cn.ichazuo.model.table.PlayAddress;

/**
 * @ClassName: PlayAddressService 
 * @Description: (播放地址Service) 
 * @author ZhaoXu
 * @date 2015年7月23日 下午5:25:12 
 * @version V1.0
 */
@Service("playAddressService")
public class PlayAddressService extends BaseService{
	private static final long serialVersionUID = 1L;
	
	@Resource
	private PlayAddressDao playAddressDao;
	
	/**
	 * @Title: findAllPlayAddress 
	 * @Description: (查询全部播放地址) 
	 * @return
	 */
	public List<Dictionary> findAllPlayAddress(){
		return playAddressDao.findAllPlayAddress();
	}
	
	/**
	 * @Title: findPlayAddressList 
	 * @Description: (查询播放地址列表) 
	 * @param params
	 * @return
	 */
	public List<PlayAddressListInfo> findPlayAddressList(Params params){
		return playAddressDao.findPlayAddressList(params.getMap());
	}
	
	/**
	 * @Title: findPlayAddressCount 
	 * @Description: (查询播放地址总数) 
	 * @param params
	 * @return
	 */
	public int findPlayAddressCount(Params params){
		return playAddressDao.findPlayAddressListCount(params.getMap());
	}
	
	/**
	 * @Title: savePlayAddredd 
	 * @Description: (保存播放地址) 
	 * @param address
	 * @return
	 */
	public boolean savePlayAddredd(PlayAddress address){
		return playAddressDao.savePlayAddress(address) > 0;
	}
	
	/**
	 * @Title: updatePlayAddress 
	 * @Description: (修改播放地址) 
	 * @param address
	 * @return
	 */
	public boolean updatePlayAddress(PlayAddress address){
		return playAddressDao.updatePlayAddress(address) > 0;
	}
	
	/**
	 * @Title: findPlayAddressById 
	 * @Description: (根据ID查询播放地址) 
	 * @param id
	 * @return
	 */
	public PlayAddress findPlayAddressById(Long id){
		return playAddressDao.findPlayAddressById(id);
	}
}
