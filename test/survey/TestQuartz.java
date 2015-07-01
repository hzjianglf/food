package survey;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.sniper.springmvc.scheduler.BackDBToFile;
import com.sniper.springmvc.utils.QuartzManagerUtil;
import com.sniper.springmvc.utils.ScheduleJob;

public class TestQuartz {

	ApplicationContext ctx;
	ComboPooledDataSource dataSource;
	SchedulerFactoryBean bean;

	@Before
	public void before() {
		ctx = new ClassPathXmlApplicationContext("beans.xml");
		dataSource = (ComboPooledDataSource) ctx.getBean("dataSource_main");
	}

	@Test
	public void test() throws SchedulerException, InterruptedException {

		BackDBToFile back = new BackDBToFile();
		back.setDataSource(dataSource);
		QuartzManagerUtil.start();
		QuartzManagerUtil
				.addSimpleJob("db", back.getClass(), 40, 1, new Date());
		
		Thread.sleep(10L * 1000L);
		QuartzManagerUtil.stop();
		
		List<ScheduleJob> jobs = QuartzManagerUtil.getCronJobs();
		System.out.println(jobs.size());
		List<ScheduleJob> jobs2 = QuartzManagerUtil.getCronExecutingJobs();
		System.out.println(jobs2.size());

	}

	public void test1() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("/usr/bin/mysqldump -h");
		buffer.append("192.168.190.5");
		buffer.append(" -p");
		buffer.append("3306");
		buffer.append(" -u");
		buffer.append("sniper");
		buffer.append(" -p");
		buffer.append("sniper");
		buffer.append(" ");
		buffer.append("schedu");
		buffer.append(" | gzip > ");
		buffer.append("WebRoot/data/dbback/aaaa");
		buffer.append(".sql.gz");
		System.out.println(buffer.toString());
		try {
			Runtime.getRuntime().exec(
					new String[] { "sh", "-c", buffer.toString() });
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
