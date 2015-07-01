package lucene;

import org.hibernate.search.indexes.interceptor.EntityIndexingInterceptor;
import org.hibernate.search.indexes.interceptor.IndexingOverride;

import com.sniper.springmvc.model.Post;

/**
 * 设置功能 这个官网说是测试功能 作用:当文章满足一定条件后在被索引 作用级别 类 Indexed
 * 
 * @author sniper
 * 
 */
public class IndexWhenPublishedInterceptor implements
		EntityIndexingInterceptor<Post> {
	/**
	 * 当一个实体被添加到您的数据库
	 */
	@Override
	public IndexingOverride onAdd(Post entity) {
		if (entity.getStatus() == 1) {
			// 这是最基本的操作，让Hibernate搜索更新指数如预期创造，更新或删除的文件
			return IndexingOverride.APPLY_DEFAULT;
		}
		// 跳过
		return IndexingOverride.SKIP;
	}

	/**
	 * 当一个实体在数据库中更新你的
	 */
	@Override
	public IndexingOverride onUpdate(Post entity) {
		if (entity.getStatus() == 1) {
			return IndexingOverride.UPDATE;
		}
		return IndexingOverride.REMOVE;
	}

	/**
	 * 当一个实体从您的数据库中删除
	 */
	@Override
	public IndexingOverride onDelete(Post entity) {
		return IndexingOverride.APPLY_DEFAULT;
	}

	/**
	 * 当集合自己的实体在你的数据库中更新
	 */
	@Override
	public IndexingOverride onCollectionUpdate(Post entity) {
		return onUpdate(entity);
	}

}
