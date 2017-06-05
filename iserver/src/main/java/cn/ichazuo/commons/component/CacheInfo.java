package cn.ichazuo.commons.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName: CacheInfo 
 * @Description: (缓存信息) 
 * @author ZhaoXu
 * @date 2015年7月18日 下午10:05:49 
 * @version V1.0
 */
@Component("cacheInfo")
public class CacheInfo {
	/**
	 * redis 缓存分页数
	 */
	@Value("${redis.cache.page.count}")
	private String pageCacheCount;

	/**
	 * redis缓存 级别1 1分钟
	 */
	@Value("${redis.time.level1}")
	private String redisCacheLevel1;

	/**
	 * redis缓存 级别2 2分钟
	 */
	@Value("${redis.time.level2}")
	private String redisCacheLevel2;

	/**
	 * redis缓存 级别3 5分钟
	 */
	@Value("${redis.time.level3}")
	private String redisCacheLevel3;

	/**
	 * redis缓存 级别4 10分钟
	 */
	@Value("${redis.time.level4}")
	private String redisCacheLevel4;

	/**
	 * redis缓存 级别5 30分钟
	 */
	@Value("${redis.time.level5}")
	private String redisCacheLevel5;

	/**
	 * redis缓存 级别6 60分钟
	 */
	@Value("${redis.time.level6}")
	private String redisCacheLevel6;

	/**
	 * redis缓存 级别最高 无失效时间
	 */
	@Value("${redis.time.max}")
	private String redisCacheLevelMax;
	
	/**
	 * 数据字典项列表缓存key 
	 */
	@Value("${reids.cache.dict}")
	private String cacheDictionaryKey;
	
	/**
	 * 线上课程评论数量缓存Key
	 */
	@Value("${redis.cache.courseonlinecommentcount}")
	private String cacheOnlineCommentCount;
	
	/**
	 * 全部角色缓存key
	 */
	@Value("${redis.cache.rolealllist}")
	private String cacheRoleAllListKey;
	
	/**
	 * 全部权限缓存列表key
	 */
	@Value("${redis.cache.authorizealllistkey}")
	private String cacheAuthorizeAllListKey;
	
	/**
	 * 全部公司缓存列表key
	 */
	@Value("${redis.cache.companyalllist}")
	private String cacheCompanyAllListKey;
	
	/**
	 * 全部公司职位缓存列表key
	 */
	@Value("${redis.cache.companyjoballlist}")
	private String cacheCompanyJobAllListKey;
	
	/**
	 * 微信token缓存key
	 */
	@Value("${redis.cache.weixinToken}")
	private String cacheWeixinTokenKey;
	
	/**
	 * 微信ticketKey
	 */
	@Value("${redis.cache.weixinTicket}")
	private String cacheWeixinTicketKey;
	
	// -------- get set --------
	/**
	 * @Title: getRedisCacheLevel1
	 * @Description: (获得redis缓存,级别1,1分钟)
	 * @return
	 * @author ZhaoXu
	 */
	public Integer getRedisCacheLevel1() {
		return Integer.valueOf(redisCacheLevel1);
	}

	/**
	 * @Title: getRedisCacheLevel2
	 * @Description: (获得redis缓存,级别2,2分钟)
	 * @return
	 * @author ZhaoXu
	 */
	public Integer getRedisCacheLevel2() {
		return Integer.valueOf(redisCacheLevel2);
	}

	/**
	 * @Title: getRedisCacheLevel3
	 * @Description: (获得redis缓存,级别3,5分钟)
	 * @return
	 * @author ZhaoXu
	 */
	public Integer getRedisCacheLevel3() {
		return Integer.valueOf(redisCacheLevel3);
	}

	/**
	 * @Title: getRedisCacheLevel4
	 * @Description: (获得redis缓存,级别4,10分钟)
	 * @return
	 * @author ZhaoXu
	 */
	public Integer getRedisCacheLevel4() {
		return Integer.valueOf(redisCacheLevel4);
	}

	/**
	 * @Title: getRedisCacheLevel5
	 * @Description: (获得redis缓存,级别5,30分钟)
	 * @return
	 * @author ZhaoXu
	 */
	public Integer getRedisCacheLevel5() {
		return Integer.valueOf(redisCacheLevel5);
	}

	/**
	 * @Title: getRedisCacheLevel6
	 * @Description: (获得redis缓存,级别6,60分钟)
	 * @return
	 * @author ZhaoXu
	 */
	public Integer getRedisCacheLevel6() {
		return Integer.valueOf(redisCacheLevel6);
	}

	/**
	 * @Title: getPageCacheCount
	 * @Description: (获得缓存最大分页数,默认5)
	 * @return
	 * @author ZhaoXu
	 */
	public Integer getPageCacheCount() {
		try {
			return Integer.valueOf(pageCacheCount);
		} catch (Exception e) {
			return 5;
		}
	}

	/**
	 * @Title: getRedisCacheLevelMax 
	 * @Description: (获得redis缓存,级别最高,无限) 
	 * @return
	 */
	public String getRedisCacheLevelMax() {
		return redisCacheLevelMax;
	}

	/**
	 * @Title: getCacheDictionaryKey 
	 * @Description: (获得数据字典项列表缓存key) 
	 * @return
	 */
	public String getCacheDictionaryKey() {
		return cacheDictionaryKey;
	}

	/**
	 * @Title: getCacheOnlineCommentCount 
	 * @Description: (获得线上课程评论数量缓存Key) 
	 * @return
	 */
	public String getCacheOnlineCommentCount() {
		return cacheOnlineCommentCount;
	}

	/**
	 * @Title: getCacheRoleAllListKey 
	 * @Description: (获得全部角色缓存key) 
	 * @return
	 */
	public String getCacheRoleAllListKey() {
		return cacheRoleAllListKey;
	}

	/**
	 * @Title: getCacheAuthorizeAllListKey 
	 * @Description: (获得全部权限缓存列表key) 
	 * @return
	 */
	public String getCacheAuthorizeAllListKey() {
		return cacheAuthorizeAllListKey;
	}

	/**
	 * @Title: getCacheCompanyAllListKey 
	 * @Description: (获得全部公司缓存列表key) 
	 * @return
	 */
	public String getCacheCompanyAllListKey() {
		return cacheCompanyAllListKey;
	}

	/**
	 * @Title: getCacheCompanyJobAllListKey 
	 * @Description: (获得全部公司职位缓存列表key) 
	 * @return
	 */
	public String getCacheCompanyJobAllListKey() {
		return cacheCompanyJobAllListKey;
	}

	/**
	 * @Title: getCacheWeixinTokenKey 
	 * @Description: (获得微信token缓存key) 
	 * @return
	 */
	public String getCacheWeixinTokenKey() {
		return cacheWeixinTokenKey;
	}

	/**
	 * @Title: getCacheWeixinTicketKey 
	 * @Description: (获得微信ticketKey) 
	 * @return
	 */
	public String getCacheWeixinTicketKey() {
		return cacheWeixinTicketKey;
	}

}
