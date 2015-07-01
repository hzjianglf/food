package com.sniper.springmvc.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.util.AssertionHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.sniper.springmvc.security.SpringContextUtil;
import com.sniper.springmvc.utils.ValidateUtil;

public class AutoSetUserAdapterFilter implements Filter {

	protected final Logger logger = LoggerFactory
			.getLogger(AutoSetUserAdapterFilter.class);

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpSession httpSession = null;

		try {
			// 现获取之前的session
			httpSession = httpRequest.getSession(false);
		} catch (Exception e) {
			// 不存在泽新建session
			httpSession = httpRequest.getSession();
		}

		// 放行指定一些路径

		String[] urls = new String[4];
		urls[0] = "/myfiles/";
		urls[1] = "/csrf";
		urls[2] = "/myfiles/";
		urls[3] = "/attachments/";

		String url = httpRequest.getRequestURI().replaceFirst(
				httpRequest.getContextPath(), "");
		
		for (int i = 0; i < urls.length; i++) {
			if (url.indexOf(urls[i]) == 0) {
				chain.doFilter(request, response);
				return;
			}
		}
		// 处理逻辑
		SecurityContext securityContext = null;
		boolean login = false;
		// 获取cas的用户名
		String username = "";

		try {
			username = AssertionHolder.getAssertion().getPrincipal().getName();
		} catch (Exception e) {
			logger.info("获取cas客户端的用户名失败");
		}

		try {

			securityContext = (SecurityContext) httpSession
					.getAttribute("SPRING_SECURITY_CONTEXT");
			if (securityContext != null) {
				login = securityContext.getAuthentication().isAuthenticated();
			}
		} catch (Exception e) {
			logger.info("获取本地session中的用户管理失败");
		}

		try {

			if (login == false && ValidateUtil.isValid(username)) {
				UserDetailsService detail = (UserDetailsService) SpringContextUtil
						.getBean("myUserDetail");
				UserDetails details = null;
				try {
					details = detail.loadUserByUsername(username);
				} catch (Exception e) {
					e.printStackTrace();
				}

				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						details, null, details.getAuthorities());

				SecurityContextHolder.getContext().setAuthentication(
						authenticationToken);

				httpSession.setAttribute("SPRING_SECURITY_CONTEXT",
						SecurityContextHolder.getContext());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		chain.doFilter(request, response);

	}

	@Override
	public void init(FilterConfig config) throws ServletException {

	}

}
