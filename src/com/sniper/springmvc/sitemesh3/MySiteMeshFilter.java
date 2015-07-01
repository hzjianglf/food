package com.sniper.springmvc.sitemesh3;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

public class MySiteMeshFilter extends ConfigurableSiteMeshFilter {

	@Override
	protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {

		builder.addDecoratorPath("/admin/**", "/WEB-INF/template/admin/main.jsp")
				.addExcludedPath("/admin/login**")
				.addExcludedPath("/csrf/**")
				.addExcludedPath("/nsm/**")
				.addExcludedPath("/admin/admin-print**")
				.addExcludedPath("/admin/file-upload**")
				.addDecoratorPath("/*", "/WEB-INF/template/home/main.jsp")
				.addDecoratorPath("/user*", "/WEB-INF/template/user/main.jsp")
				.addDecoratorPath("/admin/admin-images/", "/WEB-INF/template/admin/main-images.jsp")
				.addExcludedPath("/myfiles/*");

	}

}
