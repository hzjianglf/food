package com.sniper.springmvc.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 商务网新闻拉取
 * 
 * @author sniper
 * 
 */
@Component
public class GetSDPostImages {
	/**
	 * 提交基本地址
	 */
	String postBaseUrl = "http://shandongbusiness.gov.cn/apii/sendPostImages/";

	// String postUrl =
	// "http://shandongbusiness.gov.cn/apii/sendPostImages/0/2015-06-25/0";

	@Scheduled(cron = "0 0 0 15 * ?")
	protected void init() {

	}

}
