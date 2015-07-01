package com.sniper.springmvc.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 利用本地线程设置request,并在普通类中获取
 * @author sniper
 *
 */
public class RequestFilter implements Filter {

	// 创建线程
	public static ThreadLocal<HttpServletRequest> threadLocalRequest = new ThreadLocal<HttpServletRequest>();
	public static ThreadLocal<HttpServletResponse> threadLocalResponse = new ThreadLocal<HttpServletResponse>();

	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		threadLocalRequest.set((HttpServletRequest) arg0);
		threadLocalResponse.set((HttpServletResponse) arg1);
		arg2.doFilter(arg0, arg1);
	}

	public void destroy() {
	}

	public void init(FilterConfig arg0) throws ServletException {
	}

}



/*<filter> <filter-name>RequestFilter</filter-name> <filter-class>包名.RequestFilter</filter-class> </filter> <filter-mapping> <filter-name>RequestFilter</filter-name> <url-pattern></url-pattern> </filter-mapping> <filter-mapping>   <filter-name>RequestFilter</filter-name>   <url-pattern>*.do</url-pattern> </filter-mapping>

注册好后就可以在Java类中轻松获取自己在Action或页面上保存在Session中值，具体调用
//获取request
HttpServletRequest request = RequestFilter.threadLocal.get();

request.getSession().getAttribute("所保存的名称");

HttpServletRequest request = RequestFilter.threadLocal.get();这句话一定要放在方法里面，不能放在方法外面。
*/