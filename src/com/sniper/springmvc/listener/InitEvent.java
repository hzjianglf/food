package com.sniper.springmvc.listener;

import org.springframework.context.ApplicationEvent;

public class InitEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InitEvent(Object source) {
		super(source);
	}

}
