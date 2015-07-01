package com.sniper.springmvc.scheduler;

import java.util.Date;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.sniper.springmvc.hibernate.service.impl.LogService;
import com.sniper.springmvc.utils.LogUtil;

public class CreateLogTableTask extends QuartzJobBean {

	@Resource
	private LogService logService;

	/**
	 * 注入 logService
	 * 
	 * @param logService
	 */
	public void setLogService(LogService logService) {
		this.logService = logService;
	}

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		// 创建日志表
		
		String tableName = LogUtil.generateLogTableNameByYear(0);
		System.out.println("cron ....... " + new Date() + tableName);
		logService.createLogTable(tableName);
		System.out.println(tableName + " create");
		// 下一个月
		tableName = LogUtil.generateLogTableNameByYear(1);
		logService.createLogTable(tableName);
		System.out.println(tableName + " create");
		// 下一个月、
		tableName = LogUtil.generateLogTableNameByYear(2);
		logService.createLogTable(tableName);
		System.out.println(tableName + " create");
	}

}
