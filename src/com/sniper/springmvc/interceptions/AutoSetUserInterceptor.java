package com.sniper.springmvc.interceptions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.util.AssertionHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sniper.springmvc.security.SpringContextUtil;
import com.sniper.springmvc.utils.ValidateUtil;

/**
 * 用户登录系统 cas 客户端处理
 * 
 * @author sniper
 * 
 */
public class AutoSetUserInterceptor implements HandlerInterceptor {

	HttpSession httpSession;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		this.httpSession = request.getSession();
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		// cas 提交过来的用户名
		String username = "";
		SecurityContext securityContext = null;
		
		
		boolean login = false;
		try {
			username = AssertionHolder.getAssertion().getPrincipal().getName();
			securityContext = (SecurityContext) httpSession
					.getAttribute("SPRING_SECURITY_CONTEXT");
			
			System.out.println("cas:" + username);
			System.out.println("securityContext:" + securityContext);
			// 当cas 服务器没有登录,二本地登录的时候 清除本地登录
			if (securityContext != null) {
				login = securityContext.getAuthentication().isAuthenticated();
				if (login && !ValidateUtil.isValid(username)) {
					httpSession.invalidate();
					securityContext.setAuthentication(null);
					SecurityContextHolder.clearContext();
					return;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("错误信息" + e.getMessage());
			// 清除登录信息
			httpSession.invalidate();
			SecurityContext context = SecurityContextHolder.getContext();
			context.setAuthentication(null);
			SecurityContextHolder.clearContext();
			return;
		}

		System.out.println("执行了用户登录");
		try {

			if (login == false) {

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

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
