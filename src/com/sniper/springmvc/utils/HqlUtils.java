package com.sniper.springmvc.utils;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * 用户组装hql
 * 
 * @author laolang
 * 
 */
public class HqlUtils {

	private String entityName;
	private String entityAsName = "ean";
	private Set<String> where = new LinkedHashSet<>();
	private String join;
	private String having;
	private String group;
	private Set<String> order = new LinkedHashSet<>();
	private boolean distinct = false;
	private String hql = "";
	private String hqlCount = "";
	private boolean debug = false;
	private Map<String, Object> params = new LinkedHashMap<>();

	public HqlUtils() {
	}

	public HqlUtils(String entityName) {
		super();
		this.entityName = entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getEntityName() {
		return entityName;
	}

	public String getEntityAsName() {
		return entityAsName;
	}

	public void setEntityAsName(String entityAsName) {
		this.entityAsName = entityAsName;
	}

	public String getWhere() {
		if (this.where.size() > 0) {
			String whereHql = "";
			whereHql = StringUtils.join(where, " ").trim();
			if (whereHql.trim().startsWith("and")
					|| whereHql.trim().startsWith("or")) {
				// 送第一个空格开始截取
				whereHql = whereHql.substring(whereHql.indexOf(" "));
			}
			return " WHERE " + whereHql;
		}
		return "";
	}

	public HqlUtils where(String where) {
		if (!"".equalsIgnoreCase(where)) {
			this.where.add(where.trim());
		}
		return this;
	}

	public HqlUtils orWhere(String where) {
		if (!"".equalsIgnoreCase(where)) {
			this.where.add(" or " + where.trim());
		}
		return this;
	}

	public HqlUtils andWhere(String where) {
		if (!"".equalsIgnoreCase(where)) {
			this.where.add(" and " + where.trim());
		}
		return this;
	}

	public HqlUtils startBracket() {
		this.where.add("(");
		return this;
	}

	public HqlUtils endBracket() {
		this.where.add(")");
		return this;
	}

	public HqlUtils setWhere(String where) {
		if (!"".equalsIgnoreCase(where)) {
			this.where.add(where.trim());
		}
		return this;
	}

	public void clearWhere() {
		if (this.where.size() > 0) {
			this.where.clear();
		}
	}

	public String getJoin() {
		if (ValidateUtil.isValid(join)) {
			return " " + join;
		}
		return "";
	}

	public void setJoin(String join) {
		this.join = join.trim();
	}

	public String getHaving() {
		if (ValidateUtil.isValid(having)) {
			return " HAVING " + having;
		}
		return "";
	}

	public void setHaving(String having) {
		this.having = having;
	}

	public String getGroup() {
		if (ValidateUtil.isValid(group)) {
			return " GROUP By " + group;
		}
		return "";
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getOrder() {

		if (this.order.size() > 0) {
			String orderHql = "";
			orderHql = StringUtils.join(order, ",");
			return " ORDER BY " + orderHql;
		}

		return "";
	}

	public HqlUtils setOrder(String order) {
		if (!"".equalsIgnoreCase(order)) {
			this.order.add(order.trim());
		}
		return this;
	}

	public void clearOrder() {
		if (this.order.size() > 0) {
			this.order.clear();
		}
	}

	public boolean isDistinct() {
		return distinct;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	public String getHql() {

		if (!"".equals(this.hql)) {
			return this.hql;
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT ");
		if (isDistinct()) {
			buffer.append(" DISTINCT ");
		}
		buffer.append(getEntityAsName());
		buffer.append(" FROM ");
		buffer.append(getEntityName());
		buffer.append(" ");
		buffer.append(getEntityAsName());
		buffer.append(getJoin());
		buffer.append(getWhere());
		buffer.append(getHaving());
		buffer.append(getGroup());
		buffer.append(getOrder());

		this.hql = buffer.toString();

		if (isDebug()) {
			System.out.println(this.hql);
		}
		return hql;
	}

	public void setHql(String hql) {
		this.hql = hql;
	}

	public String getHqlCount() {

		if (!"".equals(this.hqlCount)) {
			return this.hqlCount;
		}

		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT ");
		buffer.append(" count(");
		if (isDistinct()) {
			buffer.append("DISTINCT ");
		}
		buffer.append(getEntityAsName());
		buffer.append(")");
		buffer.append(" FROM ");
		buffer.append(getEntityName());
		buffer.append(" ");
		buffer.append(getEntityAsName());
		buffer.append(getJoin());
		buffer.append(getWhere());
		buffer.append(getHaving());
		buffer.append(getGroup());
		buffer.append(getOrder());

		this.hqlCount = buffer.toString();
		if (isDebug()) {
			System.out.println(this.hqlCount);
		}
		return this.hqlCount;
	}

	public void setHqlCount(String hqlCount) {
		this.hqlCount = hqlCount;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	@Override
	public String toString() {
		return this.hqlCount + "\t" + this.hql;
	}

}
