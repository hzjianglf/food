package survey;

import java.util.UUID;

import org.apache.tools.ant.taskdefs.condition.Os;
import org.junit.Test;

public class TestMem {

	public void test() {

		Runtime r = Runtime.getRuntime();
		long first = r.freeMemory();
		int num[] = new int[100000];
		for (int i = 0; i < 100000; i++)
			num[i] = i;
		long second = r.freeMemory();
		System.out.println(first - second);
		
		System.out.println(r.maxMemory());
		System.out.println(r.freeMemory());
		System.out.println(r.totalMemory());
	}
	
	@Test
	public void test1() {
		System.out.println(Os.isName("Linux"));
		System.out.println(Os.FAMILY_UNIX);
		System.out.println(UUID.randomUUID().toString());
		System.out.println("ssssss");
	}
}
