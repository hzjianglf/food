package com.sniper.springmvc.hibernate.search.interceptor;

import org.hibernate.search.indexes.interceptor.EntityIndexingInterceptor;
import org.hibernate.search.indexes.interceptor.IndexingOverride;

import com.sniper.springmvc.model.PptFile;
/**
 * 限制条件执行拦截器
 * @author sniper
 *
 */
public class PptFileEnabledInterceptor implements
		EntityIndexingInterceptor<PptFile> {

	@Override
	public IndexingOverride onAdd(PptFile entity) {
		if (entity.getEnabled()) {
			// 这是最基本的操作，让Hibernate搜索更新指数如预期创造，更新或删除的文件
			return IndexingOverride.APPLY_DEFAULT;
		}
		// 跳过
		return IndexingOverride.SKIP;
	}

	@Override
	public IndexingOverride onUpdate(PptFile entity) {
		if (entity.getEnabled()) {
			return IndexingOverride.UPDATE;
		}
		return IndexingOverride.REMOVE;
	}

	@Override
	public IndexingOverride onDelete(PptFile entity) {
		return IndexingOverride.APPLY_DEFAULT;
	}

	@Override
	public IndexingOverride onCollectionUpdate(PptFile entity) {
		return onUpdate(entity);
	}

}
