package com.sniper.springmvc.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

public class QuartzManagerUtil {

	private static SchedulerFactory factory = new StdSchedulerFactory();
	private static SchedulerFactoryBean factoryBean;
	/**
	 * scheduler来自spring
	 */
	private static Scheduler scheduler;
	private static String JOB_GROUP_NAME = "EXTJWEB_JOBGROUP_NAME";
	private static String TRIGGRT_GROUP_NAME = "EXTEEB_TRIGGERGROUP_NAME";

	public static void setFactoryBean(SchedulerFactoryBean factoryBean) {
		QuartzManagerUtil.factoryBean = factoryBean;
	}

	public static SchedulerFactoryBean getFactoryBean() {
		return QuartzManagerUtil.factoryBean;
	}

	public static void setScheduler(Scheduler scheduler) {
		QuartzManagerUtil.scheduler = scheduler;
	}

	/**
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	public static Scheduler getScheduler() throws SchedulerException {
		if (QuartzManagerUtil.scheduler == null) {
			QuartzManagerUtil.scheduler = QuartzManagerUtil.factory
					.getScheduler();
		}
		return QuartzManagerUtil.scheduler;
	}

	public static void start() throws SchedulerException {
		try {
			if (!getScheduler().isStarted()) {
				getScheduler().start();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static void stop() throws SchedulerException {
		try {
			if (!getScheduler().isShutdown()) {
				getScheduler().shutdown();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 添加即时计划任务
	 * 
	 * @param jobName
	 * @param jobClass
	 * @param sce
	 *            有效时间 秒数
	 * @param repeat
	 *            重复次数
	 * @param startTime
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void addSimpleJob(String jobName, Class jobClass, int sce,
			int repeat, Date startTime) {
		try {
			Scheduler scheduler = getScheduler();
			JobDetail job = JobBuilder.newJob(jobClass)
					.withIdentity(jobName, JOB_GROUP_NAME).build();

			SimpleScheduleBuilder builder = SimpleScheduleBuilder
					.simpleSchedule();
			builder.withIntervalInSeconds(sce);
			if (repeat > 0) {
				builder.withRepeatCount(repeat);
			}

			Trigger trigger = TriggerBuilder.newTrigger()
					.withIdentity(jobName, TRIGGRT_GROUP_NAME)
					.startAt(startTime).withSchedule(builder).build();
			scheduler.scheduleJob(job, trigger);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 添加cron计划任务
	 * 
	 * @param jobName
	 * @param jobClass
	 * @param cron
	 * @param startTime
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void addCronJob(String jobName, Class jobClass, String cron) {
		try {
			Scheduler scheduler = getScheduler();
			JobDetail job = JobBuilder.newJob(jobClass)
					.withIdentity(jobName, JOB_GROUP_NAME).build();

			Trigger trigger = TriggerBuilder.newTrigger()
					.withIdentity(jobName, TRIGGRT_GROUP_NAME).startNow()
					.withSchedule(CronScheduleBuilder.cronSchedule(cron))
					.build();
			if (!scheduler.checkExists(job.getKey())) {
				scheduler.scheduleJob(job, trigger);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 修改一个任务 删除久的任务 添加新的任务
	 * 
	 * @param jobName
	 * @param jobClass
	 * @param cron
	 */
	public static void modifyCronJob(String jobName, String cron) {
		try {
			Scheduler scheduler = getScheduler();
			TriggerKey key = new TriggerKey(jobName, TRIGGRT_GROUP_NAME);
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(key);
			if (trigger == null) {
				return;
			}
			String oldCron = trigger.getCronExpression();
			if (!oldCron.equalsIgnoreCase(cron)) {
				JobKey arg0 = new JobKey(jobName, JOB_GROUP_NAME);
				JobDetail detail = scheduler.getJobDetail(arg0);
				@SuppressWarnings("rawtypes")
				Class oldJobClass = detail.getJobClass();
				removeCronJob(jobName);
				addCronJob(jobName, oldJobClass, cron);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 暂停触发器
	 * 
	 * @param jobName
	 */
	public static void pauseCronTrigger(String jobName) {
		try {
			Scheduler scheduler = getScheduler();
			TriggerKey arg0 = new TriggerKey(jobName, TRIGGRT_GROUP_NAME);
			scheduler.pauseTrigger(arg0);// 停止触发器
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static void pauseCronJob(String jobName) {
		try {
			Scheduler scheduler = getScheduler();
			JobKey jobKey = new JobKey(jobName, JOB_GROUP_NAME);
			scheduler.pauseJob(jobKey);// 停止任务
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 继续触发器
	 * 
	 * @param jobName
	 */
	public static void resumeTrigger(String jobName) {
		try {
			Scheduler scheduler = getScheduler();
			TriggerKey arg0 = new TriggerKey(jobName, TRIGGRT_GROUP_NAME);
			scheduler.resumeTrigger(arg0);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 回复任务
	 * 
	 * @param jobName
	 */
	public static void resumeJob(String jobName) {
		try {
			JobKey jobKey = JobKey.jobKey(jobName, JOB_GROUP_NAME);
			getScheduler().resumeJob(jobKey);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	/**
	 * 
	 * @param job
	 */
	public static void resumeJob(ScheduleJob job) {
		try {
			JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
			getScheduler().resumeJob(jobKey);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	/**
	 * 运行一次任务
	 * @param jobName
	 */
	public static void runOne(String jobName) {
		try {
			Scheduler scheduler = getScheduler();
			JobKey jobKey = new JobKey(jobName, JOB_GROUP_NAME);
			scheduler.triggerJob(jobKey);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param jobName
	 * @return
	 */
	public static CronTrigger getCronTrigger(String jobName) {
		try {
			Scheduler scheduler = getScheduler();
			TriggerKey key = new TriggerKey(jobName, TRIGGRT_GROUP_NAME);
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(key);
			return trigger;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 删除一个任务
	 * 
	 * @param jobName
	 */
	public static void removeCronJob(String jobName) {
		try {
			Scheduler scheduler = getScheduler();
			JobKey jobKey = new JobKey(jobName, JOB_GROUP_NAME);
			if (scheduler.checkExists(jobKey)) {
				scheduler.deleteJob(jobKey);// 删除任务
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取计划中的任务
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	public static List<ScheduleJob> getCronJobs() throws SchedulerException {

		GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
		Set<JobKey> jobKeys = getScheduler().getJobKeys(matcher);
		List<ScheduleJob> schedulers = new ArrayList<>();
		for (JobKey jobKey : jobKeys) {

			List<? extends Trigger> triggers = getScheduler().getTriggersOfJob(
					jobKey);

			for (Trigger trigger : triggers) {
				ScheduleJob job = new ScheduleJob();
				job.setJobName(jobKey.getName());
				job.setJobGroup(jobKey.getGroup());
				job.setDesc("触发器:" + trigger.getKey());
				Trigger.TriggerState state = getScheduler().getTriggerState(
						trigger.getKey());
				job.setJobStatus(state.name());

				if (trigger instanceof CronTrigger) {
					CronTrigger cronTrigger = (CronTrigger) trigger;
					String cronExpress = cronTrigger.getCronExpression();
					job.setCronExpression(cronExpress);
				}
				schedulers.add(job);
			}
		}
		return schedulers;
	}

	/**
	 * 获取运行中的任务
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	public static List<ScheduleJob> getCronExecutingJobs()
			throws SchedulerException {

		List<JobExecutionContext> executionContexts = getScheduler()
				.getCurrentlyExecutingJobs();

		List<ScheduleJob> schedulers = new ArrayList<>(executionContexts.size());
		for (JobExecutionContext context : executionContexts) {
			ScheduleJob job = new ScheduleJob();
			JobDetail detail = context.getJobDetail();
			JobKey jobKey = detail.getKey();
			Trigger trigger = context.getTrigger();
			job.setJobName(jobKey.getName());
			job.setJobGroup(jobKey.getGroup());
			job.setDesc("触发器:" + trigger.getKey());

			Trigger.TriggerState state = getScheduler().getTriggerState(
					trigger.getKey());
			job.setJobStatus(state.name());
			if (trigger instanceof CronTrigger) {
				CronTrigger cronTrigger = (CronTrigger) trigger;
				String cronExpress = cronTrigger.getCronExpression();
				job.setCronExpression(cronExpress);
			}

			schedulers.add(job);
		}

		return schedulers;
	}

	public static ScheduleJob getScheduleJob(String jobName)
			throws SchedulerException {

		ScheduleJob job = new ScheduleJob();
		JobKey jobKey = new JobKey(jobName, JOB_GROUP_NAME);

		TriggerKey triggerKey = new TriggerKey(jobName, TRIGGRT_GROUP_NAME);
		Trigger trigger = getScheduler().getTrigger(triggerKey);
		job.setJobName(jobKey.getName());
		job.setJobGroup(jobKey.getGroup());
		Trigger.TriggerState state = getScheduler().getTriggerState(triggerKey);
		job.setJobStatus(state.name());
		job.setDesc("触发器:" + triggerKey.getName());
		if (trigger instanceof CronTrigger) {
			job.setCronExpression(((CronTrigger) trigger).getCronExpression());
		}

		return job;

	}

	public static String getCron(int s) {
		String cron = "0 0 * * * ?";
		if (s < 60) {

		}
		return cron;
	}

}
