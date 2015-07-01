package com.sniper.springmvc.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Criteria查询助手
 * 
 * @author sniper
 * 
 */
public class QueryUtil<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(QueryUtil.class);

	private Criteria criteria;
	private Class<T> clazz;

	private Session session;

	public QueryUtil() {
	}

	public QueryUtil(Class<?> clazz) {
		super();
		this.criteria = session.createCriteria(clazz);
		
	}

	public void eq(String propertyName, Object object) {

		Criterion criterion = Restrictions.eq(propertyName, object);
		criteria.add(criterion);
	}

	public void idEq(Object object) {

		Criterion criterion = Restrictions.idEq(object);
		criteria.add(criterion);
		
		List<T> lists = criteria.list();
		
		
	}

}
