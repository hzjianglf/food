package com.sniper.springmvc.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ProjectWorldFieldUtil {
	private static Map<Integer, Map<String, String>> projectWorldField = new HashMap<>();
	static {
		addProjectWF(1, "projectName", "项目名称");
		addProjectWF(1, "provinceName|cityName|areaName", "项目所在地");
		addProjectWF(1, "note", "项目单位简介");
		addProjectWF(1, "content", "项目内容");
		addProjectWF(1, "industryType", "所属产业");
		addProjectWF(1, "industry", "所属行业");
		addProjectWF(1, "conditions", "建设条件");
		addProjectWF(1, "totalInvestment", "预计总投资");
		addProjectWF(1, "forecast", "市场分析预测");
		addProjectWF(1, "cooperType", "合作方式(独资,合资,合作)");
		addProjectWF(1, "chargeName", "联系人");
		addProjectWF(1, "address", "地址");
		addProjectWF(1, "zipCode", "邮编");
		addProjectWF(1, "tel", "电话");
		addProjectWF(1, "email", "电子邮件");
		addProjectWF(1, "createTime", "生成时间");

		addProjectWF(2, "projectName", "项目名称");
		addProjectWF(2, "provinceName|cityName|areaName", "项目所在地");
		addProjectWF(2, "signStartTime", "洽谈起始时间");
		addProjectWF(2, "investCountry", "外资来源地");
		addProjectWF(2, "chineseSideName", "中方名称");
		addProjectWF(2, "sideName", "外方名称");
		addProjectWF(2, "content", "项目内容及建设规模");
		addProjectWF(2, "industryType", "所属产业");
		addProjectWF(3, "industry", "所属行业");
		addProjectWF(2, "totalInvestment", "预计总投资");
		addProjectWF(2, "foreignInverstment", "预计外资");
		addProjectWF(2, "status", "项目进度");
		addProjectWF(2, "signType", "签约方式");
		addProjectWF(2, "progressNotes", "进度说明");
		addProjectWF(2, "signExpected", "预计签约时间");
		addProjectWF(2, "stopNotes", "存在问题");
		addProjectWF(2, "createTime", "生成时间");

		addProjectWF(3, "depName", "申报单位");
		addProjectWF(3, "provinceName|cityName|areaName", "地区");
		addProjectWF(3, "developmentName", "开发区名称");
		addProjectWF(3, "projectName", "项目名称");
		addProjectWF(3, "signDate", "签约时间");
		addProjectWF(3, "investCountry", "外资国别/地区");
		addProjectWF(3, "totalInvestment", "合同外资");
		addProjectWF(3, "sideName", "外方名称");
		addProjectWF(3, "sideParent", "外方母公司");
		addProjectWF(3, "chineseSideName", "中方名称");
		addProjectWF(3, "chineseParent", "中方母公司");
		addProjectWF(3, "industryType", "所属产业");
		addProjectWF(3, "industry", "所属行业");
		addProjectWF(3, "signLevel", "省签/市签");
		addProjectWF(3, "signApprovalTime", "批准时间");
		addProjectWF(3, "foreInvest", "合同外资");
		addProjectWF(3, "arrivedFI", "实际到帐外资");
		addProjectWF(3, "intoDomestic", "转内资投入资金");
		addProjectWF(3, "newInvestAdd", "合同外资增资累计");
		addProjectWF(3, "status", "项目进度");
		addProjectWF(3, "progressNotes", "进度说明");
		addProjectWF(3, "stopNotes", "终止、转内资、推进慢的原因");
		addProjectWF(3, "createTime", "生成时间");

		addProjectWF(4, "projectName", "项目名称");
		addProjectWF(4, "cooperType", "合作方式");
		addProjectWF(4, "chineseSideName", "中方名称");
		addProjectWF(4, "sideName", "外方名称");
		addProjectWF(4, "investCountry", "国家/地区");
		addProjectWF(4, "totalInvestment", "合同总外资");
		addProjectWF(4, "signLevel", "省签/市签");
		addProjectWF(4, "industry", "行业分类");
		addProjectWF(4, "foreInvest", "合同外资");
		addProjectWF(4, "arrivedFI", "实际到账外资");
		addProjectWF(4, "intoDomestic", "转内资投入资金");
		addProjectWF(4, "status", "项目进度");
		addProjectWF(4, "progressNotes", "进展情况");
		addProjectWF(4, "stopNotes", "原因");
		addProjectWF(4, "exportTime", "导入日期");
		addProjectWF(4, "exportYear", "项目年份");
		addProjectWF(4, "newInvest", "增资实际到账累计");
		addProjectWF(4, "newInvestAdd", "合同外资增资累计");
		addProjectWF(4, "createTime", "生成时间");
	}

	public static Map<Integer, Map<String, String>> getProjectWorldField() {
		return projectWorldField;
	}

	public static Map<String, String> getProjectWorldField(int key) {
		return projectWorldField.get(key);
	}

	private static void addProjectWF(Integer key, String name, String value) {
		Map<String, String> map = new LinkedHashMap<>();
		map.put(name, value);
		if (projectWorldField.get(key) != null) {
			projectWorldField.get(key).putAll(map);
		} else {
			projectWorldField.put(key, map);
		}
	}

}
