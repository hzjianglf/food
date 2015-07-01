package com.sniper.springmvc.utils;



/**
 * 
 * 
 * 
 * java类获取web应用的根目录
 * 
 */
public class PathUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		
		
		PathUtil p = new PathUtil();
		System.out.println(p.getWebClassesPath());
		System.out.println(p.getWebRoot());
		System.out.println(p.getWebInfPath());
		
		
	}

	/**
	 * 获取webclass路径
	 * 
	 * @return
	 */
	public String getWebClassesPath() {
		String path = getClass().getProtectionDomain().getCodeSource()
				.getLocation().getPath();
		return path;

	}

	public String getWebInfPath() throws IllegalAccessException {
		String path = getWebClassesPath();
		if (path.indexOf("WEB-INF") > 0) {
			path = path.substring(0, path.indexOf("WEB-INF") + 8);
		} else {
			throw new IllegalAccessException("路径获取错误");
		}
		return path;
	}

	public String getWebRoot() throws IllegalAccessException {
		String path = getWebClassesPath();
		if (path.indexOf("WEB-INF") > 0) {
			path = path.substring(0, path.indexOf("WEB-INF/classes"));
		} else {
			throw new IllegalAccessException("路径获取错误");
		}
		return path;
	}
}