package com.sniper.springmvc.freemarker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

public class CreateFreemarkerHtml {

	/**
	 * ftl模板跟路径
	 */
	private static final String ftlBasePath = "/approot/www/jsp/survey/WebRoot/WEB-INF/content";

	@SuppressWarnings("rawtypes")
	public static void init(String ftl, Map map, String htmlPath)
			throws Exception {
		Version version = new Version(2, 3, 21);
		Configuration cfg = new Configuration(version);
		cfg.setDirectoryForTemplateLoading(new File(ftlBasePath));
		ObjectWrapper objectWrapper = new DefaultObjectWrapper(version);
		cfg.setObjectWrapper(objectWrapper);
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
		cfg.setIncompatibleImprovements(version);
		Template template = cfg.getTemplate(ftl);
		// 模板输出
		File file = new File(htmlPath);
		// 手册中例子
		// Writer out = new OutputStreamWriter(System.out)
		// 网上例子
		Writer out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file)));
		template.process(map, out);
		out.flush();
		out.close();
		// 输出到文件

	}

	public static void main(String[] args) throws Exception {

		init("/web/index/index.ftl", null, "index-1.html");
	}
}
