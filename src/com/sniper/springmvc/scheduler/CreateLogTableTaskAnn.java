package com.sniper.springmvc.scheduler;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sniper.springmvc.hibernate.service.impl.LogService;
import com.sniper.springmvc.utils.LogUtil;

@Component
public class CreateLogTableTaskAnn {

	@Resource
	private LogService logService;

	@Scheduled(cron = "0 0 0 15 * ?")
	protected void init() {
		// 创建日志表
		System.out.println("cron ....... " + new Date());
		String tableName = LogUtil.generateLogTableNameByYear(0);
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
