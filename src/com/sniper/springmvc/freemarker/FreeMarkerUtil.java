package com.sniper.springmvc.freemarker;

import org.springframework.ui.ModelMap;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;
import freemarker.template.Version;

public class FreeMarkerUtil {

	private static Class<?>[] defaultStaticClasses = {};

	protected void setDefaultStaticModel(ModelMap map) {
		for (Class<?> clz : defaultStaticClasses) {
			String name = clz.getSimpleName();
			map.addAttribute(name, getStaticModel(clz));
		}
	}

	private static Object getStaticModel(Class<?> clz) {
		Version version = new Version(2, 3, 22);
		BeansWrapper beansWrapper = new BeansWrapper(version);
		try {
			return beansWrapper.getStaticModels().get(clz.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 静态方法调用
	 * 
	 * @param clz
	 * @return
	 */
	public static TemplateHashModel getFreeMarkerStaticModel(Class<?> clz) {
		TemplateHashModel dataUtil = null;
		Version version = new Version(2, 3, 22);
		BeansWrapper beansWrapper = new BeansWrapper(version);
		TemplateHashModel model = beansWrapper.getStaticModels();
		try {
			dataUtil = (TemplateHashModel) model.get(clz.getName());
		} catch (TemplateModelException e) {
			e.printStackTrace();
		}

		return dataUtil;
	}

}
