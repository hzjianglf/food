package survey;

import java.io.FileNotFoundException;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

import com.sniper.springmvc.utils.RedisUtil;
import com.sniper.springmvc.utils.TreeZTreeUtil;

public class TestRedis {

	@Test
	public void test() {

		JedisShardInfo info = new JedisShardInfo("192.168.190.173", 6379);

		Jedis jedis = new Jedis(info);
		jedis.connect();
		System.out.println(jedis.isConnected());

		Jedis jedis2 = new Jedis("127.0.0.1", 6379);
		jedis2.connect();
		System.out.println(jedis2.isConnected());
	}

	@Test
	public void test1() throws FileNotFoundException {
		Jedis jedis = new RedisUtil().getJedis();
		Set<String> set = jedis.keys(RedisUtil.getKeyName(TreeZTreeUtil.class,
				"treeNodes*"));

		for (String s : set) {
			System.out.println(s);
		}
	}

	public void test2() throws FileNotFoundException {

		ShardedJedis jedis = new RedisUtil().getShardedJedis();

		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			String result = jedis.set("sn" + i, "n" + i);
			// System.out.println(result);
		}

		long end = System.currentTimeMillis();
		System.out.println("Simple@Sharing SET: " + ((end - start) / 1000.0)
				+ " seconds");

		jedis.disconnect();
		jedis.close();

	}
}
