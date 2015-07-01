package com.sniper.springmvc.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Table;

public class AnnotationsUtil {

	/**
	 * 获取字段
	 * 
	 * @param classtype
	 */
	@SuppressWarnings(value = { "rawtypes" })
	public static Map<String, String> getColumns(Class classtype) {
		Field[] fields = classtype.getDeclaredFields();
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < fields.length; i++) {
			Annotation[] annotations = fields[i].getAnnotations();
			for (int j = 0; j < annotations.length; j++) {
				if (annotations[j] instanceof Column) {
					Column column = (Column) annotations[j];
					System.out.print(fields[i].getName() + "、");
					System.out.println(column.name());
					map.put(fields[i].getName(), column.name());
				}
			}
		}
		return map;
	}

	/**
	 * 获取表名
	 * 
	 * @param classtype
	 * @return
	 */
	@SuppressWarnings(value = { "rawtypes" })
	public static String getTableName(Class classtype) {
		Annotation[] anno = classtype.getAnnotations();
		String tableName = "";
		for (int i = 0; i < anno.length; i++) {
			if (anno[i] instanceof Table) {
				Table table = (Table) anno[i];
				System.out.println(table.name());
				tableName = table.name();
			}
		}
		return tableName;
	}
}
