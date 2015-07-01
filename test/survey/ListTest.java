package survey;

import java.util.Arrays;

import org.junit.Test;

import com.sniper.springmvc.utils.RedisUtil;
import com.sniper.springmvc.utils.TreeZTreeUtil;

public class ListTest {

	@Test
	public void test() {
		int[] test = {1,6,3,8,9,2,5};
        Arrays.sort(test); //首先对数组排序
        
        System.out.println(Arrays.binarySearch(test, 10));
        int result = Arrays.binarySearch(test, 5); //在数组中搜索是否含有5
        System.out.println(result);
        System.out.println(result>=0&&result<test.length); //这里的结果是 3 
        
        System.out.println(RedisUtil.getKeyName(TreeZTreeUtil.class,
							"treeNodes"));
	}
}
