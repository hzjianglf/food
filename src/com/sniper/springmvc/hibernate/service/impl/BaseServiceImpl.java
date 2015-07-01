package com.sniper.springmvc.hibernate.service.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sniper.springmvc.hibernate.dao.BaseDao;
import com.sniper.springmvc.hibernate.service.BaseService;
import com.sniper.springmvc.utils.HqlUtils;

/**
 * @Resource写法不能乱写，写在字段上和set上可以用子类覆盖，而字段不能被覆盖
 * @author laolang
 * 
 * @param <T>
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {

	protected final static Logger LOGGER = LoggerFactory
			.getLogger("com.sniper.springmvc.hibernate.service.impl");

	private BaseDao<T> dao;
	//
	private Class<T> clazz;

	public BaseDao<T> getDao() {
		return dao;
	}

	@SuppressWarnings("unchecked")
	public BaseServiceImpl() {

		ParameterizedType type = (ParameterizedType) this.getClass()
				.getGenericSuperclass();
		clazz = (Class<T>) type.getActualTypeArguments()[0];

	}

	public BaseServiceImpl(BaseDao<T> dao, Class<T> clazz) {
		super();
		this.dao = dao;
		this.clazz = clazz;
	}

	// 注入
	@Resource
	public void setDao(BaseDao<T> dao) {
		this.dao = dao;
	}

	public Class<T> getClazz() {
		return clazz;
	}

	@Override
	public FullTextSession getFullTextSession() {
		return dao.getFullTextSession();
	}

	@Override
	public QueryBuilder getQueryBuilder() {
		return dao.getQueryBuilder();
	}

	@Override
	public Session getOpenSession() {
		return dao.getOpenSesion();
	}

	@Override
	public Session getCurrentSession() {
		return dao.getCurrentSession();
	}

	public void saveEntiry(T t) {
		dao.saveEntiry(t);
	}

	public void saveOrUpdateEntiry(T t) {
		dao.saveOrUpdateEntiry(t);
	}

	@Override
	public void savePersist(T t) {
		dao.savePersist(t);

	}

	@Override
	public T updateMerge(T t) {
		return dao.updateMerge(t);
	}

	public void updateEntiry(T t) {
		dao.updateEntiry(t);
	}

	public void deleteEntiry(T t) {
		dao.deleteEntiry(t);
	}

	@Override
	public void deleteEntirys(Object[] ids) {

		if (ids.length > 0) {
			for (int i = 0; i < ids.length; i++) {

				if (ids[i].getClass() == String.class) {
					this.deleteEntiry(this.getEntity((String) ids[i]));
				} else {
					this.deleteEntiry(this.getEntity((int) ids[i]));
				}
			}
		}
	}

	public int batchEntiryByHQL(String hql, Object... objects) {
		return dao.batchEntiryByHQL(hql, objects);
	}

	@Override
	public boolean deleteBatchEntityById(Integer[] id) {
		if (id.length == 0) {
			return false;
		}

		for (int i = 0; i < id.length; i++) {
			this.deleteEntiry(this.getEntity(id[i]));
		}

		String hql = "DELETE FROM " + clazz.getSimpleName() + " where id in("
				+ StringUtils.join(id, ",") + ")";
		try {
			if (this.batchEntiryByHQL(hql) > 0) {
				return true;
			}

		} catch (Exception e) {

		}
		return false;
	}

	@Override
	public boolean deleteBatchEntityById(String[] id) {
		if (id.length == 0) {
			return false;
		}

		String hql = "DELETE FROM " + clazz.getSimpleName() + " where id in(\'"
				+ StringUtils.join(id, "\',\'") + "\')";
		try {
			if (this.batchEntiryByHQL(hql) > 0) {
				return true;
			}
		} catch (Exception e) {

		}
		return false;
	}

	/**
	 * 根据id,批量修改一个字段的值
	 */
	@Override
	public int batchFiledChange(String filedName, Object changeValue,
			Integer[] id) {
		String hql = "UPDATE " + clazz.getSimpleName() + " SET " + filedName
				+ "=? WHERE id in(" + StringUtils.join(id, ",") + ") ";
		return dao.batchEntiryByHQL(hql, changeValue);

	}

	public T loadEntity(Integer id) {
		return dao.loadEntity(id);
	}

	@Override
	public T loadCEntity(Integer id) {
		return dao.loadEntity(id);
	}

	public T getEntity(Integer id) {
		return dao.getEntity(id);
	}

	public T getEntity(String id) {
		return dao.getEntity(id);
	}

	public List<T> getEntitys(String[] id) {
		String hql = "FROM " + clazz.getSimpleName() + " where id in("
				+ StringUtils.join(id, ",") + ") ";

		return this.findCEntityByHQL(hql);
	}

	@Override
	public List<T> getEntitys(Integer[] id) {
		String hql = "FROM " + clazz.getSimpleName() + " where id in("
				+ StringUtils.join(id, ",") + ") ";

		return this.findCEntityByHQL(hql);
	}

	@Override
	public T getCEntity(Integer id) {
		return dao.getEntity(id);
	}

	public List<T> findEntityByHQL(String hql, Object... objects) {
		return dao.findEntityByHQL(hql, objects);
	}

	@Override
	public List<T> findCEntityByHQL(String hql, Object... objects) {
		return dao.findEntityByHQL(hql, objects);
	}

	@Override
	public Query findEntityByHQLQuery(String hql, Object... objects) {
		return dao.findEntityByHQLQuery(hql, objects);
	}

	@Override
	public SQLQuery findEntityBySQLQuery(String sql, Object... objects) {
		return dao.findEntityBySQLQuery(sql, objects);
	}

	@Override
	public Object uniqueResult(String hql, Object... objects) {
		return this.findEntityByHQLQuery(hql, objects).uniqueResult();
	}

	@Override
	public List<T> findAllEntitles() {
		String hql = "from " + clazz.getSimpleName();
		return this.findEntityByHQL(hql);
	}

	@Override
	public List<T> findCAllEntitles() {
		return this.findAllEntitles();
	}

	@Override
	public int executeSQL(Class<?> clazz, String hql, Object... objects) {
		return dao.executeSQL(clazz, hql, objects);
	}

	@Override
	public List<T> findEntityByHQLPage(String hql, int firstResult,
			int maxResult, Object... objects) {
		return dao.findEntityByHQLPage(hql, firstResult, maxResult, objects);
	}
	/**
	 * hibernate 索引搜索
	 * @return
	 */
	public List<T> findSearch() {
		QueryBuilder builder = getQueryBuilder();
		org.apache.lucene.search.Query query = builder.keyword()
				.onField("name").matching("name").createQuery();

		@SuppressWarnings("unchecked")
		List<T> list = getFullTextSession().createFullTextQuery(query, clazz)
				.list();

		return list;
	}

	@Override
	public int pageCountList(HqlUtils hqlUtils, Object... objects) {
		String hql = hqlUtils.getHqlCount();
		long l = (long) this.uniqueResult(hql, objects);
		int totalNum = new Long(l).intValue();
		return totalNum;
	}

	public int pageCountList(HqlUtils hqlUtils) {
		String hql = hqlUtils.getHqlCount();
		long l = (long) this.uniqueResult(hql, hqlUtils.getParams());
		int totalNum = new Long(l).intValue();
		return totalNum;
	}

	/**
	 * 数据苦分页使用能完成基本的需求 简单的分页程序
	 */
	@Override
	public List<T> pageList(HqlUtils hqlUtils, int fristRow, int listRow,
			Object... objects) {

		String hql2 = hqlUtils.getHql();
		List<T> lists = this.findEntityByHQLPage(hql2, fristRow, listRow,
				objects);
		return lists;

	}

	public List<T> pageList(HqlUtils hqlUtils, int fristRow, int listRow) {

		String hql = hqlUtils.getHql();
		List<T> lists = this.findEntityByHQLPage(hql, fristRow, listRow,
				hqlUtils.getParams());
		return lists;

	}

	@Override
	public List<T> pageList(HqlUtils hqlUtils) {
		String hql2 = hqlUtils.getHql();
		List<T> lists = this.findCEntityByHQL(hql2);
		return lists;
	}

	@Override
	public Criteria criteria() {
		Criteria criteria = dao.criteria();
		return criteria;
	}

}
