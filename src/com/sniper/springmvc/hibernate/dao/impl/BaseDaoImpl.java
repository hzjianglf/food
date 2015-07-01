package com.sniper.springmvc.hibernate.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ReplicationMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

import com.sniper.springmvc.hibernate.dao.BaseDao;

/**
 * 单例
 * 
 * @author laolang
 * 
 * @param <T>
 */
@SuppressWarnings("unchecked")
public abstract class BaseDaoImpl<T> implements BaseDao<T> {

	// 声明反射用，获取的类反射类型
	private Class<T> clazz;
	// 注入session
	@Resource
	private SessionFactory sessionFactory;

	public BaseDaoImpl() {
		// 得到泛型话的超类，
		Type type = this.getClass().getGenericSuperclass();
		// 如果type集成与ParameterizedType,以为内ParameterizedType可以带有参数，可以从他里面获取参数
		Type[] args = null;
		if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			// 获取参数列表
			args = parameterizedType.getActualTypeArguments();
			if (args != null && args.length > 0) {
				// 获取地一个参数
				Type arg = args[0];
				if (arg instanceof Class) {
					clazz = (Class<T>) arg;
				}
			}
		}
		// 以上简单写法
		// ParameterizedType Type = (ParameterizedType)
		// this.getClass().getGenericSuperclass();;
		// clazz = (Class<T>) Type.getActualTypeArguments()[0];
	}

	@Override
	public FullTextSession getFullTextSession() {
		if (getCurrentSession().isOpen()) {
			return Search.getFullTextSession(getCurrentSession());
		}
		return Search.getFullTextSession(getOpenSesion());

	}

	@Override
	public QueryBuilder getQueryBuilder() {

		return getFullTextSession().getSearchFactory().buildQueryBuilder()
				.forEntity(clazz).get();
	}

	/**
	 * 获取当前session
	 * 
	 * @return
	 */
	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 打开session
	 * 
	 * @return
	 */
	public Session getOpenSesion() {
		return sessionFactory.openSession();
	}

	public void saveEntiry(T t) {

		this.getCurrentSession().save(t);
	}

	public void saveOrUpdateEntiry(T t) {

		this.getCurrentSession().saveOrUpdate(t);

	}

	@Override
	public void savePersist(T t) {
		this.getCurrentSession().persist(t);

	}

	@Override
	public T updateMerge(T t) {
		return (T) this.getCurrentSession().merge(t);
	}

	@Override
	public void saveReplicata(T t, ReplicationMode obj) {
		this.getCurrentSession().replicate(t, obj);
	}

	public void updateEntiry(T t) {

		this.getCurrentSession().update(t);

	}

	public void deleteEntiry(T t) {
		this.getCurrentSession().delete(t);
	}

	public int batchEntiryByHQL(String hql, Object... objects) {

		Query query = this.getCurrentSession().createQuery(hql);
		query = this.setQueryParams(query, objects);
		return query.executeUpdate();

	}

	/*
	 * (non-Javadoc) 可以产生代理，load不存咋出异常
	 * 
	 * @see com.sniper.survey.dao.BaseDao#loadEntity(java.lang.Integer)
	 */

	public T loadEntity(Integer id) {
		return (T) this.getCurrentSession().load(clazz, id);

	}

	/**
	 * 不能产生代理，记录为空没有异常
	 */

	public T getEntity(Integer id) {
		return (T) this.getCurrentSession().get(clazz, id);
	}

	public T getEntity(String id) {
		return (T) this.getCurrentSession().get(clazz, id);
	}

	public List<T> findEntityByHQL(String hql, Object... objects) {

		Query query = this.getCurrentSession().createQuery(hql);
		query = this.setQueryParams(query, objects);
		return query.list();
	}

	public List<T> findEntityByHQLPage(String hql, int firstResult,
			int maxResult, Object... objects) {
		Query query = this.getCurrentSession().createQuery(hql);
		query = this.setQueryParams(query, objects);
		return query.setFirstResult(firstResult).setMaxResults(maxResult)
				.list();
	}

	public Query findEntityByHQLQuery(String hql, Object... objects) {
		Query query = this.getCurrentSession().createQuery(hql);
		query = this.setQueryParams(query, objects);
		return query;
	}

	@Override
	public SQLQuery findEntityBySQLQuery(String sql, Object... objects) {
		SQLQuery query = this.getOpenSesion().createSQLQuery(sql);
		query = this.setQueryParams(query, objects);
		return query;
	}

	@Override
	public int executeSQL(Class<?> clazz, String hql, Object... objects) {
		SQLQuery query = this.getCurrentSession().createSQLQuery(hql);
		if (clazz != null) {
			query.addEntity(clazz);
		}
		query = this.setQueryParams(query, objects);
		return query.executeUpdate();
	}

	/**
	 * 设置query的参数
	 * 
	 * @param query
	 * @param objects
	 * @return
	 */
	private Query setQueryParams(Query query, Object... objects) {

		if (objects != null && objects.length > 0) {
			if (objects[0] instanceof Map) {
				Map<String, Object> params = (Map<String, java.lang.Object>) objects[0];
				if (params.size() > 0) {
					for (Map.Entry<String, Object> entry : params.entrySet()) {
						query.setParameter(entry.getKey(), entry.getValue());
					}
				}
			} else {
				for (int i = 0; i < objects.length; i++) {
					query.setParameter(i, objects[i]);
				}
			}
		}
		return query;
	}

	private SQLQuery setQueryParams(SQLQuery query, Object... objects) {

		if (objects != null && objects.length > 0) {
			if (objects[0] instanceof Map) {
				Map<String, Object> params = (Map<String, java.lang.Object>) objects[0];
				if (params.size() > 0) {
					for (Map.Entry<String, Object> entry : params.entrySet()) {
						query.setParameter(entry.getKey(), entry.getValue());
					}
				}
			} else {
				for (int i = 0; i < objects.length; i++) {
					query.setParameter(i, objects[i]);
				}
			}
		}
		return query;
	}

	@Override
	public Criteria criteria() {

		Criteria criteria = this.getCurrentSession().createCriteria(clazz);
		return criteria;
	}
}
