package com.sniper.springmvc.datasource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * http://blog.csdn.net/liuwenbo0920/article/details/7290586/
 * 
 * @author sniper
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DataSource {
	DataSourceValue value() default DataSourceValue.MASTER;
}
