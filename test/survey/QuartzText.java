package survey;

import static org.quartz.DateBuilder.evenMinuteDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.sniper.springmvc.scheduler.BackDBToFile;

public class QuartzText {

	public void run() throws SchedulerException {

		// 创建调度器
		SchedulerFactory factory = new StdSchedulerFactory();
		Date runTime = evenMinuteDate(new Date());  
		Scheduler scheduler = null;
		
		try {
			scheduler = factory.getScheduler();

			JobDetail job = newJob(BackDBToFile.class)
					.withIdentity("job1","group1")
					.build();
			Trigger trigger = newTrigger()
					.withIdentity("trigger1", "group1")
					.startAt(runTime)
					.build();
			
			scheduler.scheduleJob(job, trigger);
			scheduler.start();
			
			Thread.sleep(65l * 1000l);
		} catch (Exception e) {
			
		}
		
		scheduler.shutdown(true);
		
	}
	
	public static void main(String[] args) throws SchedulerException {
		QuartzText quartzText = new QuartzText();
		quartzText.run();
	}

	
}
