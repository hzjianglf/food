package com.sniper.springmvc.utils;

import java.io.File;
import java.util.Map;

/**
 * 用户前台图片大小的展示
 * 
 * @author sniper
 * 
 */
public class ImageHelpUtil {

	public static String show(String path, int width, int height) {

		if (!ValidateUtil.isValid(path)) {
			return path;
		}

		Map<String, String> config = SystemConfigUtil.getSystemConfig();
		String rootPath = "";
		if (ValidateUtil.isValid(config.get("imagePath"))) {
			rootPath = config.get("imagePath");
		}
		// 图片要改成的高度,宽度
		// 组装新图片地址
		String suffix = path.substring(path.lastIndexOf("."));
		String newPath = path.substring(0, path.lastIndexOf(".")) + "_"
				+ String.valueOf(width) + "_" + String.valueOf(height) + suffix;

		File file = new File(rootPath + newPath);
		if (file.isFile()) {
			return newPath;
		}

		File oldFile = new File(rootPath + path);
		if (!oldFile.isFile()) {
			return newPath;
		}

		// 复制文件
		try {
			FilesUtil.Copy(rootPath + path, rootPath + newPath);
			ImageUtils.resize(rootPath + newPath, height, width, false);
		} catch (Exception e) {
		}

		return newPath;

	}
}
