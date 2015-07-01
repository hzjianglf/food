package com.sniper.springmvc.interceptions;

import java.util.Enumeration;

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
public class FristInterceptor implements HandlerInterceptor {

	/**
	 * 3 在渲染试图之后调用 可以做一些释放资源
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object object, Exception exception)
			throws Exception {
		System.out.println("拦截器收尾工作");

	}

	/**
	 * 2 调用方法之后渲染试图之前 可以对请求玉中的属性,或试图做出修改
	 */
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object object, ModelAndView andView)
			throws Exception {

		System.out.println("第一个拦截器 : 之前执行");

		long startTime = (long) request.getAttribute("startTime");
		request.setAttribute("endTime", System.currentTimeMillis() - startTime);
		request.setAttribute("me", "我是拦截器穿的值");

	}

	/**
	 * 1 当返回false的时候他后面的拦截器就不会执行调用 返回true会继续调用后面的拦截器 调用 目标方法之前, 可以写一些事务,日志,权限等等
	 */

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object object) throws Exception {
		System.out.println("拦截器,程序之后执行");

		request.setAttribute("startTime", System.currentTimeMillis());

		Enumeration<String> headers = request.getHeaderNames();

		while (headers.hasMoreElements()) {
			String string = (String) headers.nextElement();
			//System.out.println(string + ">>>><<<<" + request.getHeader(string));

		}

		return true;
	}

}
