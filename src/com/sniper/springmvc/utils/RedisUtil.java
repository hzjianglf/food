package com.sniper.springmvc.utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisUtil {

	private static Map<String, Object> redisConfig = new HashMap<>();
	// 非切片客户端链接
	private Jedis jedis;
	// 非切片连接池
	private JedisPool jedisPool;
	// 切片客户端
	private static ShardedJedis shardedJedis;
	// 切片连接池
	private static ShardedJedisPool shardedPool;
	private static List<JedisShardInfo> hosts = new ArrayList<>();

	/**
	 * 返回所有的链接
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	private static void initHost() {

		InputStream in = RedisUtil.class.getClassLoader().getResourceAsStream(
				"properties/redis.properties");
		PropertiesUtil pu = null;
		pu = new PropertiesUtil(in);
		Map<String, String> map = pu.getValues();

		for (Map.Entry<String, String> entry : map.entrySet()) {

			if (entry.getKey().startsWith("host")) {
				String host = entry.getValue();
				String[] hosts = host.split(":");
				JedisShardInfo info = new JedisShardInfo(hosts[0], hosts[1]);
				if (hosts.length == 3) {
					info.setPassword(hosts[2]);
				}
				RedisUtil.hosts.add(info);
			} else {
				RedisUtil.redisConfig.put(entry.getKey(), entry.getValue());
			}
		}

	}

	private void initJedisPool() {
		if (null == jedisPool) {
			initHost();
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxIdle(5000);
			config.setMaxWaitMillis(0);
			config.setTestOnBorrow(false);
			if (hosts.size() > 0) {
				jedisPool = new JedisPool(config, hosts.get(0).getHost());
			}
		}
	}

	/**
	 * 获取切片连接池
	 * 
	 * @return
	 */
	public void initShardePool() {

		if (null == shardedPool) {
			// 池基本配置
			initHost();
			JedisPoolConfig config = new JedisPoolConfig();
			// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
			config.setMaxIdle(5000);
			// 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
			config.setMaxWaitMillis(0);
			// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
			config.setTestOnBorrow(false);

			shardedPool = new ShardedJedisPool(config, hosts);

		}
	}

	/**
	 * 设置配置
	 * 
	 * @param redisConfig
	 */
	public void setRedisConfig(Map<String, Object> redisConfig) {
		for (Map.Entry<String, Object> entry : redisConfig.entrySet()) {
			if (RedisUtil.redisConfig.get(entry.getKey()) != null) {
				RedisUtil.redisConfig.put(entry.getKey(), entry.getValue());
			}
		}
	}

	public static Map<String, Object> getRedisConfig() {
		return redisConfig;
	}

	/**
	 * 从连接池里面获取一个资源链接
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	public ShardedJedis getShardedJedis() {
		if (shardedJedis == null) {
			initShardePool();
			shardedJedis = shardedPool.getResource();

		}
		return shardedJedis;
	}

	/**
	 * 返还到连接池
	 * 
	 * @param pool
	 * @param redis
	 */
	public void returnResource(Jedis redis) {
		if (redis != null) {
			jedisPool.returnResource(redis);
		}
	}

	public void returnBrokenResource(Jedis redis) {
		if (redis != null) {
			jedisPool.returnResource(redis);
		}
	}

	public Jedis getJedis() {
		if (null == jedis) {
			initJedisPool();
			jedis = jedisPool.getResource();
		}

		if (!jedis.isConnected()) {
			jedis.connect();
		}
		return jedis;
	}

	public Jedis getJedis(int num) throws Exception {
		if (null == jedis) {
			if (hosts.get(num) != null) {
				jedis = new Jedis(hosts.get(num));
			} else {
				throw new Exception("找不到相关服务器");
			}
		}
		if (!jedis.isConnected()) {
			jedis.connect();
		}
		return jedis;
	}

	/**
	 * 获取缓存前缀
	 * 
	 * @param key
	 * @return
	 */
	public static String getKeyName(String key) {
		initHost();
		return redisConfig.get("prefix") + key;
	}

	/**
	 * 返回
	 * 
	 * @param object
	 * @param method
	 * @return
	 */
	public static String getKeyName(Class<?> object, String method) {
		initHost();
		return redisConfig.get("prefix") + object.getName() + ":" + method;
	}

	/**
	 * 获取数组的前缀
	 * 
	 * @param key
	 * @return
	 */
	public static String[] getKeyName(String[] key) {

		String[] newName = new String[key.length];
		for (int i = 0; i < key.length; i++) {
			newName[i] = redisConfig.get("prefix") + key[i];
		}
		return newName;
	}

	public static void main(String[] args) {
	}
}
