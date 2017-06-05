package cn.ichazuo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.ichazuo.model.table.Authorize;

@Repository("authorizeDao")
public interface AuthorizeDao {
	
	/**
	 * @Title: findAllAuthorize 
	 * @Description: (查询全部权限) 
	 * @return
	 */
	public List<Authorize> findAllAuthorize();
	
	/**
	 * @Title: findAuthorizeById 
	 * @Description: (根据ID查询权限) 
	 * @param id
	 * @return
	 */
	public Authorize findAuthorizeById(Long id);
	
	/**
	 * @Title: saveAuthorize 
	 * @Description: (保存权限) 
	 * @param authorize
	 * @return
	 */
	public int saveAuthorize(Authorize authorize);
	
	/**
	 * @Title: updateAuthorize 
	 * @Description: (修改权限) 
	 * @param authorize
	 * @return
	 */
	public int updateAuthorize(Authorize authorize);
}
