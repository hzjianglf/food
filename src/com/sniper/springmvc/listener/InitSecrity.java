package com.sniper.springmvc.listener;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

import com.sniper.springmvc.coder.RSACoder;

@SuppressWarnings("rawtypes")
// @Component
/**
 * 不可用页面
 * @author sniper
 *
 */
public class InitSecrity implements ApplicationListener,
		ApplicationContextAware {

	private ServletContext context;

	/**
	 * spring中已经内置的几种事件：
	 * 
	 * ContextClosedEvent 、ContextRefreshedEvent 、ContextStartedEvent
	 * 、ContextStoppedEvent 、RequestHandleEvent
	 */
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ContextStartedEvent) {
			System.out.println("ContextStartedEvent");
		}
		if (event instanceof ContextStoppedEvent) {
			System.out.println("ContextStoppedEvent");
		}
		if (event instanceof ContextClosedEvent) {
			System.out.println("ContextClosedEvent");
		}

		if (event instanceof ContextRefreshedEvent) {
			System.out.println("ContextRefreshedEvent");
		}

		if (this.context != null) {

			try {
				Map<String, Object> ketMap = RSACoder.initKey();
				byte[] priKey = RSACoder.getPrivateKey(ketMap);
				byte[] pubKey = RSACoder.getPublicKey(ketMap);

				String prikeys = Base64.encodeBase64String(priKey);
				String pubkeys = Base64.encodeBase64String(pubKey);
				// System.out.println(this.context.getInitParameter("publicKey"));
				System.out.println(pubkeys);
				this.context.setInitParameter("publicKey", "ssssssssssss"
						+ pubkeys);
				System.out.println(this.context.getInitParameter("publicKey"));
				// System.out.println(this.context.getInitParameter("privateKey"));
				// System.out.println(prikeys);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		context = applicationContext.getBean(ServletContext.class);

	}

}
