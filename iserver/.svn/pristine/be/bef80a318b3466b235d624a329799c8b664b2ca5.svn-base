package cn.ichazuo.commons.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import cn.ichazuo.commons.util.SerializeUtils;

/**
 * @ClassName: RedisClient 
 * @Description: (Redis操作类) 
 * @author ZhaoXu
 * @date 2015年7月18日 下午10:07:22 
 * @version V1.0
 */
@Component("redisClient")
public class RedisClient {
	private static Logger logger = LoggerFactory.getLogger(RedisClient.class);
	
	@Autowired
	private ShardedJedisPool shardedJedisPool;
	
	@Autowired
	private ConfigInfo configInfo;

	/**
	 * @Title: setObject 
	 * @Description: (设置Object对象) 
	 * @param key 键
	 * @param obj 值
	 * @param time 过期时间
	 */
	public void setObject(String key, Object obj, int time) {
		if(!configInfo.getProjectCache()){
			return;
		}
		ShardedJedis jedis = null;
		try {
			jedis = shardedJedisPool.getResource();
			jedis.set(key.getBytes(), SerializeUtils.serialize(obj));
			if (time != 0) {
				jedis.expire(key.getBytes(), time);
			}
			logger.info(String.format("redis setObject info:[key:%s,value:%s,outTime:%d]", key,obj.toString(),time));
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		} finally {
			shardedJedisPool.returnResourceObject(jedis);
		}
	}

	/**
	 * @Title: getObject 
	 * @Description: (获得Object对象) 
	 * @param key 键
	 * @return
	 */
	public Object getObject(String key) {
		ShardedJedis jedis = null;
		Object obj = null;
		try {
			jedis = shardedJedisPool.getResource();
			byte[] dataByte = jedis.get(key.getBytes());
			obj = SerializeUtils.deserialize(dataByte);
			
			logger.info(String.format("redis getObject info:[key:%s,value:%s]", key,obj.toString()));
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		} finally {
			shardedJedisPool.returnResourceObject(jedis);
		}
		return obj;
	}


	/**
	 * @Title: delete 
	 * @Description: (删除对象) 
	 * @param key 键
	 * @return
	 */
	public Long delete(String key) {
		ShardedJedis jedis = null;
		Long temp = null;
		try {
			jedis = shardedJedisPool.getResource();
			temp = jedis.del(key.getBytes());
			
			logger.debug(String.format("redis deleteDate info:[key:%s]", key));
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		} finally {
			shardedJedisPool.returnResourceObject(jedis);
		}
		return temp;
	}

	/**
	 * @Title: isExist 
	 * @Description: (判断对象是否存在,若未开启缓存,返回false) 
	 * @param key 键
	 * @return
	 */
	public boolean isExist(String key) {
		if(!configInfo.getProjectCache()){
			return false;
		}
		ShardedJedis jedis = null;
		boolean flag = false;
		try {
			jedis = shardedJedisPool.getResource();
			flag = jedis.exists(key.getBytes());
			
			logger.debug(String.format("redis isExist info:[key:%s]", key));
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		} finally {
			shardedJedisPool.returnResourceObject(jedis);
		}
		return flag;
	}

	/**
	 * @Title: updateTime 
	 * @Description: (更新key的剩余时间) 
	 * @param key 键
	 * @param time 时间
	 */
	public void updateTime(String key, int time) {
		ShardedJedis jedis = null;
		try {
			jedis = shardedJedisPool.getResource();
			jedis.expire(key.getBytes(), time);
			
			logger.debug(String.format("redis updateTime info:[key:%s,time:%d]", key ,time));
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		} finally {
			shardedJedisPool.returnResourceObject(jedis);
		}
	}
}
