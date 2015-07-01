package com.sniper.springmvc.interceptions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 调用过程 preHandle-> handlerAdadter#handle ->postHandle->DispatchServlet@render
 * ->afterCompletion
 * 
 * @author sniper
 * 
 */
public class RunTimeInterceptor implements HandlerInterceptor {

	/**
	 * 3 在渲染试图之后调用 可以做一些释放资源
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object object, Exception exception)
			throws Exception {

	}

	/**
	 * 2 调用方法之后渲染试图之前 可以对请求玉中的属性,或试图做出修改
	 */
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object object, ModelAndView andView)
			throws Exception {

		long startTime = (long) request.getAttribute("startTime");
		request.setAttribute("endTime", System.currentTimeMillis() - startTime);

	}

	/**
	 * 1 当返回false的时候他后面的拦截器就不会执行调用 返回true会继续调用后面的拦截器 调用 目标方法之前, 可以写一些事务,日志,权限等等
	 */

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object object) throws Exception {

		request.setAttribute("startTime", System.currentTimeMillis());

		return true;
	}

}
