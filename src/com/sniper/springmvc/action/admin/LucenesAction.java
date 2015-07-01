package com.sniper.springmvc.action.admin;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sniper.springmvc.hibernate.service.impl.PostService;
import com.sniper.springmvc.model.Post;

@RequestMapping("/nsm/lucene")
@Controller
public class LucenesAction extends BaseAction {

	@Resource
	private PostService postService;

	private FullTextSession fullTextSession;

	@RequestMapping("/")
	@ResponseBody
	public String index() throws InterruptedException {

		fullTextSession = postService.getFullTextSession();
		// 同步创建索引
		fullTextSession.createIndexer().startAndWait();
		// 异步创建索引
		// fullTextSession.createIndexer().start();

		Transaction tx = fullTextSession.beginTransaction();

		QueryBuilder qb = fullTextSession.getSearchFactory()
				.buildQueryBuilder().forEntity(Post.class).get();

		Query query = qb.keyword().onField("name").matching("阿").createQuery();

		FullTextQuery query2 = fullTextSession.createFullTextQuery(query,
				Post.class);
		// 设置排序字段
		Sort sort = new Sort(new SortField("sort", SortField.Type.INT, true));

		query2.setSort(sort);
		query2.setFirstResult(0);
		query2.setMaxResults(100);

		List<Post> list = query2.list();

		System.out.println(list);

		tx.commit();

		postService.getCurrentSession().close();
		System.out.println("---");

		return "---------";
	}

	@RequestMapping("/list")
	@ResponseBody
	public List<Post> list() throws InterruptedException, IOException,
			InvalidTokenOffsetsException {

		fullTextSession = postService.getFullTextSession();

		// Transaction tx = fullTextSession.beginTransaction();
		
		QueryBuilder qb = postService.getQueryBuilder();
		// ignoreFieldBridge 强制忽略
		// 下面的注视好像可以提取元数据,提取一个mp3
		// @TikaBridge(metadataProcessor = Mp3TikaMetadataProcessor.class)
		qb.keyword().onField("")
		.ignoreFieldBridge()
		.matching("").createQuery();

		Query query = qb.keyword().onField("name").matching("添加").createQuery();

		org.hibernate.Query query2 = fullTextSession.createFullTextQuery(query,
				Post.class);

		List<Post> list = query2.list();

		SimpleHTMLFormatter formatter = new SimpleHTMLFormatter(
				"<font color=\"red\">", "</font>");

		QueryScorer queryScorer = new QueryScorer(query);

		Highlighter highlighter = new Highlighter(formatter, queryScorer);

		String[] fieldNames = { "name" };

		for (Post post : list) {
			String name = highlighter.getBestFragment(new StandardAnalyzer(),
					"name", post.getName());
			post.setName(name);

		}

		System.out.println(list.size());
		System.out.println(list);

		return list;
	}

	@RequestMapping("/add")
	@ResponseBody
	public String add() {

		Post post = new Post();
		post.setName("添加添加测试");

		postService.saveEntiry(post);

		return "---------";
	}
}
