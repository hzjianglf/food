package com.sniper.springmvc.interceptions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

public class MyLocaleChangeInterceptor extends LocaleChangeInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object object)
			throws ServletException {

		return super.preHandle(request, response, object);
	}

}
