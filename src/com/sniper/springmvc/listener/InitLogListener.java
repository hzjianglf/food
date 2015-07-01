package com.sniper.springmvc.listener;

import javax.annotation.Resource;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.sniper.springmvc.hibernate.service.impl.LogService;
import com.sniper.springmvc.utils.LogUtil;

/**
 * sping监听器 初始化监听器，咋spring初始化完成后立即调用 负责完成权限的初始化
 * 
 * @author laolang
 * 
 */
@SuppressWarnings("rawtypes")
@Component
public class InitLogListener implements ApplicationListener {

	@Resource
	private LogService logService;

	@Override
	public void onApplicationEvent(ApplicationEvent event) {

		// 上下文刷新事件
		if (event instanceof ContextRefreshedEvent) {
			// 创建日志表
			String tableName = LogUtil.generateLogTableNameByYear(0);
			logService.createLogTable(tableName);
			// 下一个月
			tableName = LogUtil.generateLogTableNameByYear(1);
			logService.createLogTable(tableName);
			// 下一个月、
			tableName = LogUtil.generateLogTableNameByYear(2);
			logService.createLogTable(tableName);
		}

	}

}
