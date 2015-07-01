package com.sniper.springmvc.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	/**
	 * 获取之前几天的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateBefore(Date d, int day) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - day);
		return calendar.getTime();
	}

	/**
	 * 获取之后几天的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateAfter(Date d, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + day);
		return calendar.getTime();
	}
}
