package com.sniper.springmvc.datasource;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * 数据源切换 对于内部调用的不管用 比如你写了一个扩展了一个接口,类在调用的时候,虽然在内部调用父接口的方法,但是注解无法获取
 * 
 * @author sniper
 * 
 */
public class DataSourceAspect {
	public void before(JoinPoint point) {
		Object target = point.getTarget();
		String method = point.getSignature().getName();

		// Class<?>[] superClassz =
		// target.getClass().getSuperclass().getInterfaces();
		Class<?>[] classz = target.getClass().getInterfaces();

		// System.out.println(superClassz[0].getName());
		//System.out.println("数据源切换");
		//System.out.println(classz[0].getName());
		//System.out.println(method);

		Class<?>[] parameterTypes = ((MethodSignature) point.getSignature())
				.getMethod().getParameterTypes();
		try {
			Method m = classz[0].getMethod(method, parameterTypes);

			if (m != null && m.isAnnotationPresent(DataSource.class)) {
				DataSource data = m.getAnnotation(DataSource.class);
				DataSourceSwitch.setDataSource(data.value().toString());
				//System.out.println(data.value().toString());
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
