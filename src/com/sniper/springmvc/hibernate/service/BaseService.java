package com.sniper.springmvc.hibernate.service;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.query.dsl.QueryBuilder;

import com.sniper.springmvc.datasource.DataSource;
import com.sniper.springmvc.datasource.DataSourceValue;
import com.sniper.springmvc.utils.HqlUtils;

public interface BaseService<T> {

	public FullTextSession getFullTextSession();

	public QueryBuilder getQueryBuilder();

	public Session getOpenSession();

	public Session getCurrentSession();

	// 写操作
	@DataSource
	public void saveEntiry(T t);

	@DataSource
	public void saveOrUpdateEntiry(T t);

	@DataSource
	public void updateEntiry(T t);

	@DataSource
	public void deleteEntiry(T t);

	@DataSource
	public void deleteEntirys(Object[] ids);

	@DataSource
	public int batchEntiryByHQL(String hql, Object... objects);

	@DataSource
	public boolean deleteBatchEntityById(Integer[] id);

	@DataSource
	public boolean deleteBatchEntityById(String[] id);

	/**
	 * 单个字段的修改,批量修改
	 * 
	 * @param hql
	 * @param Object
	 */
	@DataSource
	public int batchFiledChange(String filedName, Object changeValue,
			Integer[] id);

	@DataSource
	public int executeSQL(Class<?> clazz, String hql, Object... objects);

	// 级联关系保存
	@DataSource
	public void savePersist(T t);

	@DataSource
	public T updateMerge(T t);

	// 读操作
	@DataSource(DataSourceValue.SLAVE)
	public T loadEntity(Integer id);

	@DataSource(DataSourceValue.SLAVE)
	public T loadCEntity(Integer id);

	@DataSource(DataSourceValue.SLAVE)
	public T getEntity(Integer id);

	@DataSource(DataSourceValue.SLAVE)
	public T getEntity(String id);

	@DataSource(DataSourceValue.SLAVE)
	public List<T> getEntitys(String[] id);

	@DataSource
	public List<T> getEntitys(Integer[] id);

	@DataSource(DataSourceValue.SLAVE)
	public T getCEntity(Integer id);

	@DataSource(DataSourceValue.SLAVE)
	public List<T> findEntityByHQL(String hql, Object... objects);

	@DataSource(DataSourceValue.SLAVE)
	public List<T> findCEntityByHQL(String hql, Object... objects);

	public Query findEntityByHQLQuery(String hql, Object... objects);

	public SQLQuery findEntityBySQLQuery(String sql, Object... objects);

	// 获取唯一的值
	@DataSource(DataSourceValue.SLAVE)
	public Object uniqueResult(String hql, Object... objects);

	// 查询所有实体
	@DataSource(DataSourceValue.SLAVE)
	public List<T> findAllEntitles();

	@DataSource(DataSourceValue.SLAVE)
	public List<T> findCAllEntitles();

	@DataSource(DataSourceValue.SLAVE)
	public List<T> findEntityByHQLPage(String hql, int firstResult,
			int maxResult, Object... objects);

	@DataSource(DataSourceValue.SLAVE)
	public int pageCountList(HqlUtils hqlUtils, Object... objects);

	@DataSource(DataSourceValue.SLAVE)
	public int pageCountList(HqlUtils hqlUtils);

	@DataSource(DataSourceValue.SLAVE)
	public List<T> pageList(HqlUtils hqlUtils, int fristRow, int listRow,
			Object... Object);

	@DataSource(DataSourceValue.SLAVE)
	public List<T> pageList(HqlUtils hqlUtils, int fristRow, int listRow);

	@DataSource(DataSourceValue.SLAVE)
	public List<T> pageList(HqlUtils hqlUtils);

	public Criteria criteria();

}
