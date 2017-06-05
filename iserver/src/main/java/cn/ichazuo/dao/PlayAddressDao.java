package cn.ichazuo.dao;

import java.util.List;
import java.util.Map;

import cn.ichazuo.model.Dictionary;
import cn.ichazuo.model.admin.PlayAddressListInfo;
import cn.ichazuo.model.table.PlayAddress;

/**
 * @ClassName: PlayAddressDao 
 * @Description: (播放地址DAO) 
 * @author ZhaoXu
 * @date 2015年7月23日 下午5:24:02 
 * @version V1.0
 */
public interface PlayAddressDao {
	
	/**
	 * @Title: findAllPlayAddress 
	 * @Description: (查询全部播放地址) 
	 * @return
	 */
	public List<Dictionary> findAllPlayAddress();
	
	/**
	 * @Title: findPlayAddressList 
	 * @Description: (查询播放地址列表) 
	 * @param params
	 * @return
	 */
	public List<PlayAddressListInfo> findPlayAddressList(Map<String,Object> params);
	
	/**
	 * @Title: findPlayAddressListCount 
	 * @Description: (查询播放地址列表总数) 
	 * @param params
	 * @return
	 */
	public int findPlayAddressListCount(Map<String,Object> params);
	
	/**
	 * @Title: findPlayAddressById 
	 * @Description: (根据ID查询播放地址) 
	 * @param id
	 * @return
	 */
	public PlayAddress findPlayAddressById(Long id);
	
	/**
	 * @Title: savePlayAddress 
	 * @Description: (保存播放地址) 
	 * @param address
	 * @return
	 */
	public int savePlayAddress(PlayAddress address);
	
	/**
	 * @Title: updatePlayAddress 
	 * @Description: (修改播放地址) 
	 * @param address
	 * @return
	 */
	public int updatePlayAddress(PlayAddress address);
}
