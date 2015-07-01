package com.sniper.springmvc.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = -2788075811594663611L;

	@SuppressWarnings({ "rawtypes" })
	public String toString() {
		try {
			StringBuffer buffer = new StringBuffer();

			Class clazz = this.getClass();
			String simpleName = clazz.getSimpleName();
			buffer.append(simpleName);
			buffer.append("{");

			//
			Field[] fs = clazz.getDeclaredFields();
			Class ftype = null;
			String fname = null;
			Object fvalue = null;
			for (Field f : fs) {
				ftype = f.getType();
				fname = f.getName();
				// 设置私有属性可读可访问
				f.setAccessible(true);
				fvalue = f.get(this);
				// 判断基本数据类型,过滤静态
				if ((ftype.isPrimitive() || ftype == Integer.class
						|| ftype == Long.class || ftype == Short.class
						|| ftype == Boolean.class || ftype == Character.class
						|| ftype == Double.class || ftype == String.class)
						&& !Modifier.isStatic(f.getModifiers())) {
					buffer.append(fname);
					buffer.append(":");
					buffer.append(ftype);
					buffer.append(",");
					buffer.append(fvalue);
				}
			}

			buffer.append("{}");
			return buffer.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}
}
