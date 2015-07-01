package com.sniper.springmvc.freemarker;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tools.ant.Project;

public class ViewProjectImport {

	public static String getFiledValue(Object project, String filed) {

		if (project == null || filed == null) {
			return "";
		}

		if (filed.indexOf("|") > 0) {
			String[] fileds = filed.split("\\|");
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < fileds.length; i++) {
				buffer.append(FieldValue(project, fileds[i]));
			}

			return buffer.toString();
		} else {
			return FieldValue(project, filed);
		}

	}

	private static String FieldValue(Object project, String filed) {
		if (project == null || filed == null) {
			return "";
		}
		filed = filed.substring(0, 1).toUpperCase() + filed.substring(1);
		filed = "get" + filed;
		String result = "";
		try {
			Method method = Project.class.getMethod(filed);
			Type type = method.getGenericReturnType();
			Object object = method.invoke(project);
			if (type.equals(String.class)) {
				result = (String) method.invoke(project);
			} else if (type.equals(Integer.class)) {
				if (object != null) {
					result = String.valueOf(object);
				}
			} else if (type.equals(Date.class)) {
				Date result2 = (Date) method.invoke(project);
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				result = dateFormat.format(result2);
			} else if (type.equals(Boolean.class)) {
				Boolean result4 = (boolean) method.invoke(project);
				result = String.valueOf(result4);
			}

			return result;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static String getFiledValue(Object project, String filed,
			boolean target) {

		if (project == null || filed == null) {
			return "";
		}
		filed = filed.substring(0, 1).toUpperCase() + filed.substring(1);
		filed = "get" + filed;
		String result = "";

		try {
			Method method = Project.class.getMethod(filed);
			Type type = method.getGenericReturnType();
			Object object = method.invoke(project);
			if (type.equals(String.class)) {
				result = (String) method.invoke(project);
				if (target && filed.equals("getProjectName")) {
					result = "<a href=\"admin/admin-projects/updateSign?id="
							+ "\" target=\"_blank\">" + result + "</a>";
				}
			} else if (type.equals(Integer.class)) {
				if (object != null) {
					result = String.valueOf(object);
				}
			} else if (type.equals(Date.class)) {
				Date result2 = (Date) method.invoke(project);
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				result = dateFormat.format(result2);
			} else if (type.equals(Boolean.class)) {
				Boolean result4 = (boolean) method.invoke(project);
				result = String.valueOf(result4);
			}

			return result;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}

	public static void main(String[] args) {
		String a = "as|asdsa|aaa";
		System.out.println(a.indexOf("|"));
		if (a.indexOf("|") > 0) {
			System.out.println(a.split("\\|").length);
		}
	}
}
