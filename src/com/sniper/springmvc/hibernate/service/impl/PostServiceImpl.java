package com.sniper.springmvc.hibernate.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.sniper.springmvc.hibernate.dao.BaseDao;
import com.sniper.springmvc.model.Post;
import com.sniper.springmvc.utils.HqlUtils;

@Service("postService")
public class PostServiceImpl extends BaseServiceImpl<Post> implements
		PostService {

	@Resource(name = "postDao")
	public void setDao(BaseDao<Post> dao) {

		super.setDao(dao);
	}

	/**
	 * 防止懒加载所有的懒加载都必须在service里面处理
	 */
	@Override
	public Post getAllEntity(Integer id) {
		Post post = super.getEntity(id);
		if (post != null) {
			try {
				post.getPostValue().getValue();
				post.getChannels().size();
				post.getFiles().size();
			} catch (Exception e) {
				
			}
			
		}

		return post;
	}

	@Override
	public Post getPostFiles(Integer id) {
		Post post = super.getEntity(id);
		if (post != null) {
			post.getFiles().size();
		}

		return post;
	}

	/**
	 * 根据栏目id获取新闻
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Post> getCListByChannelID(Integer[] channelId, Integer limit) {

		String hql = "select DISTINCT p from Post as p join p.channels as c  ";
		if (channelId.length != 0) {
			hql += " where c.id in( " + StringUtils.join(channelId, ",") + ")";
		}

		hql += " order by id desc";
		List<Post> list = this.findEntityByHQLQuery(hql).setMaxResults(limit)
				.list();
		// 去除重复,视频里面可以我这有错
		// list = new ArrayList<>(new LinkedHashMap(list));
		return list;
	}

	/**
	 * 读取带有内容的文章列表
	 */
	@Override
	public List<Post> getListsWithPostValue(HqlUtils hqlUtils, int maxResult,
			Object... Object) {
		List<Post> lists = this.pageList(hqlUtils, 0, maxResult, Object);
		for (Post post : lists) {
			post.getPostValue().getValue();
		}
		return lists;
	}

	@Override
	public List<Post> getListsWithFiles(HqlUtils hqlUtils, int maxResult,
			Object... Object) {
		List<Post> lists = this.pageList(hqlUtils, 0, maxResult, Object);
		for (Post post : lists) {
			post.getFiles().size();
		}
		return lists;
	}
}
