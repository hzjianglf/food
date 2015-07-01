package com.sniper.springmvc.security;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 获取spring bean
 * 
 * @author sniper
 * 
 */
public class SpringContextUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		SpringContextUtil.applicationContext = context;
		
		System.out.println(applicationContext);
		

	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}

	public static Object getBean(Class<?> class1) {
		return applicationContext.getBean(class1);
	}

}
