package lucene;

import java.awt.print.Book;
import java.lang.annotation.ElementType;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.de.GermanStemFilterFactory;
import org.apache.lucene.analysis.en.EnglishPossessiveFilterFactory;
import org.apache.lucene.analysis.ngram.NGramFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.cfg.ConcatStringBridge;
import org.hibernate.search.cfg.Environment;
import org.hibernate.search.cfg.SearchMapping;
import org.hibernate.search.engine.ProjectionConstants;
import org.hibernate.search.query.DatabaseRetrievalMethod;
import org.hibernate.search.query.ObjectLookupMethod;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.Unit;

import com.sniper.springmvc.hibernate.service.impl.PostService;
import com.sniper.springmvc.model.Post;
import com.sniper.springmvc.model.PostValue;

public class SearchMappingTest {

	PostService postService;

	public void name1() {

		SearchMapping mapping = new SearchMapping();
		Configuration configuration = new Configuration();
		configuration.getProperties().put(Environment.MODEL_MAPPING, mapping);

		SessionFactory sf = configuration.buildSessionFactory();

		mapping.analyzerDef("ngram", StandardTokenizerFactory.class)
				.filter(LowerCaseFilterFactory.class)
				.filter(NGramFilterFactory.class).param("minGramsize", "3")
				.param("maxGramSize", "3");
	}

	public void name2WithJpa() {

		SearchMapping mapping = new SearchMapping();

		Map<Object, Object> props = new HashMap<>();
		props.put(Environment.MODEL_MAPPING, mapping);

		EntityManagerFactory emf = Persistence.createEntityManagerFactory(
				"userPU", props);
	}

	/**
	 * 操作实体类添加索引,相当于直接在实体类上面枷锁因
	 * 
	 * @Entity
	 * @Indexed(index="Address_Index", 
	 *                                 interceptor=IndexWhenPublishedInterceptor.
	 *                                 class) public class Address { // ... }
	 * @Id
	 * @GeneratedValue
	 * @DocumentId(name="id") private Long addressId;
	 */
	public void name3WithIndex() {
		SearchMapping mapping = new SearchMapping();
		mapping.entity(Post.class).indexed().indexName("Post_Index")
				.interceptor(IndexWhenPublishedInterceptor.class)
				// 修饰的字段
				.property("id", ElementType.FIELD).documentId().name("id");

		Configuration cfg = new Configuration();

		cfg.getProperties().put(Environment.MODEL_MAPPING, mapping);

	}

	/**
	 * @Indexed
	 * @Entity
	 * @AnalyzerDefs({
	 * @AnalyzerDef(name = "ngram", tokenizer = @TokenizerDef(factory =
	 *                   StandardTokenizerFactory.class), filters = {
	 * @TokenFilterDef(factory = LowerCaseFilterFactory.class),
	 * @TokenFilterDef(factory = NGramFilterFactory.class, params = {
	 * @Parameter(name = "minGramSize",value = "3"),
	 * @Parameter(name = "maxGramSize",value = "3") }) }),
	 * @AnalyzerDef(name = "en", tokenizer = @TokenizerDef(factory =
	 *                   StandardTokenizerFactory.class), filters = {
	 * @TokenFilterDef(factory = LowerCaseFilterFactory.class),
	 * @TokenFilterDef(factory = EnglishPorterFilterFactory.class) }),
	 * @AnalyzerDef(name = "de", tokenizer = @TokenizerDef(factory =
	 *                   StandardTokenizerFactory.class), filters = {
	 * @TokenFilterDef(factory = LowerCaseFilterFactory.class),
	 * @TokenFilterDef(factory = GermanStemFilterFactory.class) })
	 * 
	 *                         }) public class Address { // ... }
	 */
	public void name3Model() {
		SearchMapping mapping = new SearchMapping();
		mapping.analyzerDef("ngram", StandardTokenizerFactory.class)
				.filter(LowerCaseFilterFactory.class)
				.filter(NGramFilterFactory.class).param("minGramsize", "3")
				.param("maxGramSize", "3")
				.analyzerDef("en", StandardTokenizerFactory.class)
				.filter(LowerCaseFilterFactory.class)
				.filter(EnglishPossessiveFilterFactory.class)
				.analyzerDef("de", StandardTokenizerFactory.class)
				.filter(LowerCaseFilterFactory.class)
				.filter(GermanStemFilterFactory.class).entity(Post.class)
				.indexed().property("id", ElementType.METHOD).documentId()
				.name("id");
		Configuration cfg = new Configuration();

		cfg.getProperties().put(Environment.MODEL_MAPPING, mapping);

	}

	/**
	 * 全文以编程方式定义的定义
	 * 
	 * @Entity
	 * @Indexed
	 * @AnalyzerDefs({
	 * @AnalyzerDef(name = "en", tokenizer = @TokenizerDef(factory =
	 *                   StandardTokenizerFactory.class), filters = {
	 * @TokenFilterDef(factory = LowerCaseFilterFactory.class),
	 * @TokenFilterDef(factory = EnglishPorterFilterFactory.class) }) }) public
	 *                         class Address {
	 * @Id
	 * @GeneratedValue
	 * @DocumentId(name="id") private Long getAddressId() {...};
	 * @Fields({
	 * @Field(store=Store.YES, analyzer=@Analyzer(definition="en")),
	 * @Field(name="address_data", analyzer=@Analyzer(definition="en")) })
	 *                             public String getAddress1() {...}
	 * 
	 *                             // ... }
	 */
	public void name4Model() {
		SearchMapping mapping = new SearchMapping();

		mapping.analyzerDef("en", StandardTokenizerFactory.class)
				.filter(LowerCaseFilterFactory.class)
				.filter(EnglishPossessiveFilterFactory.class)
				// .fullTextFilterDef("security", SecurityFilterChain.class)
				// .cache(FilterCacheModeType.INSTANCE_ONLY)
				.entity(Post.class)
				.indexed()
				// 一个 property 是一个字段
				.property("id", ElementType.METHOD).documentId().name("id")
				.property("note", ElementType.FIELD)
				/**
				 * @Fields({
				 * @Field(store=Store.YES, analyzer=@Analyzer(definition="en")),
				 * @Field(name="address_data", 
				 *                             analyzer=@Analyzer(definition="en"
				 *                             )) })
				 */
				.field().analyzer("en").store(Store.YES).field().name("source")
				.analyzer("en").store(Store.NO);

		Configuration cfg = new Configuration();
		cfg.getProperties().put("hibernate.search.model_mapping", mapping);

	}

	/**
	 * @Entity
	 * @Indexed public class ProductCatalog {
	 * @Id
	 * @GeneratedValue
	 * @DocumentId(name="id") public Long getCatalogId() {...}
	 * @Field public String getTitle() {...}
	 * @Field public String getDescription();
	 * @OneToMany(fetch = FetchType.LAZY)
	 * @IndexColumn(name = "list_position")
	 * @Cascade(org.hibernate.annotations.CascadeType.ALL)
	 * @IndexedEmbedded(prefix="catalog.items") public List<Item> getItems()
	 *                                          {...}
	 * 
	 *                                          // ... }
	 */
	public void name5Entity() {
		SearchMapping mapping = new SearchMapping();
		mapping.entity(Post.class).indexed().property("id", ElementType.METHOD)
				.documentId().name("id").property("name", ElementType.METHOD)
				.field().index(Index.YES).store(Store.NO)
				.property("note", ElementType.METHOD).field().index(Index.YES)
				.store(Store.NO)
				/**
				 * @OneToMany(fetch = FetchType.LAZY)
				 * @IndexColumn(name = "list_position")
				 * @Cascade(org.hibernate.annotations.CascadeType.ALL)
				 * @IndexedEmbedded(prefix="catalog.items") public List<Item>
				 *                                          getItems() {...}
				 */
				.property("postValue", ElementType.METHOD).indexEmbedded()
				.prefix("post.postValue");
	}

	public void name6Entity() {
		SearchMapping mapping = new SearchMapping();
		mapping.entity(Post.class).indexed().property("id", ElementType.METHOD)
				.documentId().property("name", ElementType.METHOD).field()
				.property("note", ElementType.METHOD).field()
				.property("postValue", ElementType.METHOD).indexEmbedded()

				.entity(PostValue.class).property("id", ElementType.METHOD)
				.field().property("value", ElementType.METHOD).containedIn();
	}

	/**
	 * 关于日期的方法
	 */
	public void name6Date() {
		SearchMapping mapping = new SearchMapping();

		mapping.entity(Post.class).indexed().property("id", ElementType.FIELD)
				.documentId().property("name", ElementType.FIELD).field()
				.property("stime", ElementType.FIELD).field()
				.dateBridge(Resolution.DAY)
				.property("letime", ElementType.FIELD)
				.calendarBridge(Resolution.DAY);

		// cfg.getProperties().put( "hibernate.search.model_mapping", mapping );
	}

	/**
	 * 传递参数
	 */
	public void name6Param() {
		SearchMapping mapping = new SearchMapping();

		mapping.entity(Post.class).indexed().property("id", ElementType.FIELD)
				.documentId().property("name", ElementType.FIELD).field()
				.field().name("name_abridged").bridge(ConcatStringBridge.class)
				.param("size", "4");

	}

/**
	 * @Entity
		@Indexed
		@ClassBridges ( {
		  @ClassBridge(name="branchnetwork",
		     store= Store.YES,
		     impl = CatDeptsFieldsClassBridge.class,
		     params = @Parameter( name="sepChar", value=" " ) ),
		  @ClassBridge(name="equiptype",
		     store= Store.YES,
		     impl = EquipmentType.class,
		     params = {@Parameter( name="C", value="Cisco" ),
		        @Parameter( name="D", value="D-Link" ),
		        @Parameter( name="K", value="Kingston" ),
		        @Parameter( name="3", value="3Com" )
		   })
		})
	 */
	public void name7Bridges() {
		SearchMapping mapping = new SearchMapping();

		mapping.entity(Post.class).classBridge(PaddedIntegerBridge.class)
				.name("branchnetwork")
				.index(Index.YES)
				.store(Store.YES)
				.param("sepChar", " ")
				// 多参数传递
				.classBridge(ElementType.class).name("equiptype")
				.index(Index.YES).store(Store.YES).param("C", "Cisco")
				.param("D", "D-Link").param("K", "Kingston").param("3", "3Com")
				.indexed();

	}

	/**
	 * @Entity
	 * @Indexed
	 * @DynamicBoost(impl = CustomBoostStrategy.class) public class
	 *                    DynamicBoostedDescriptionLibrary {
	 * @Id
	 * @GeneratedValue
	 * @DocumentId private int id;
	 * 
	 *             private float dynScore;
	 * @Field(store = Store.YES)
	 * @DynamicBoost(impl = CustomFieldBoostStrategy.class) private String name;
	 * 
	 *                    public DynamicBoostedDescriptionLibrary() { dynScore =
	 *                    1.0f; }
	 * 
	 *                    // ... }
	 */
	public void name8Boost() {
		SearchMapping mapping = new SearchMapping();
		mapping.entity(Post.class).indexed()
				.dynamicBoost(CustomBoostStrategy.class)
				.property("libraryId", ElementType.FIELD).documentId()
				.name("id").property("name", ElementType.FIELD)
				.dynamicBoost(CustomFieldBoostStrategy.class).field()
				.store(Store.YES);
	}

	public void name9Search() {
		Query luceneQuery = postService.getQueryBuilder().keyword()
				.onFields("history", "description", "name").matching("storm")
				.createQuery();

	}

	/**
	 * 多搜索使用统一处理
	 */
	public void name10Search() {
		Query luceneQuery = postService.getQueryBuilder().keyword()
				.onField("history").andField("name").boostedTo(5)
				.andField("description").matching("storm").createQuery();
	}

	/**
	 * 模糊查询
	 */
	public void name11Search() {
		Query luceneQuery = postService.getQueryBuilder().keyword().fuzzy()
				.withThreshold(0.8f).withPrefixLength(1).onField("history")
				.matching("starm").createQuery();
	}

	/**
	 * 通配符查询 不建议使用
	 */
	public void name12Search() {
		Query luceneQuery = postService.getQueryBuilder().keyword().wildcard()
				.onField("history").matching("sto*").createQuery();
	}

	/**
	 * 句子搜索
	 */
	public void name13Search() {
		Query luceneQuery = postService.getQueryBuilder().phrase()
				.onField("history").sentence("Thou shalt not kill")
				.createQuery();
	}

	/**
	 * 设置一句话有破的句子 可能是
	 */
	public void name14Search() {
		Query luceneQuery = postService.getQueryBuilder().phrase().withSlop(3)
				.onField("history").sentence("Thou kill").createQuery();
	}

	/**
	 * 范围查询
	 */
	public void name15Search() {
		// look for 0 <= starred < 3
		Query luceneQuery = postService.getQueryBuilder().range()
				.onField("starred").from(0).to(3).excludeLimit().createQuery();

		// look for myths strictly BC
		Date beforeChrist = new Date();
		Query luceneQuery1 = postService.getQueryBuilder().range()
				.onField("creationDate").below(beforeChrist).excludeLimit()
				.createQuery();
	}

	/**
	 * MoreLikeThis，相似检索。找出某篇文档的相似文档，常见于“类似新闻”、“相关文章”等，这里完全是基于内容的分析。
	 */
	public void name16Search() {

		Query mltQuery = postService.getQueryBuilder().moreLikeThis()
				.comparingAllFields().toEntityWithId(12).createQuery();
		postService.getFullTextSession()
				.createFullTextQuery(mltQuery, Post.class).list();

		List<Post> results = postService
				.getFullTextSession()
				.createFullTextQuery(mltQuery, Post.class)
				.setProjection(ProjectionConstants.THIS,
						ProjectionConstants.SCORE).list();
	}

	public void name17Search() {
		double centerLatitude = 24.0d;
		double centerLongitude = 32.0d;

		QueryBuilder builder = postService.getQueryBuilder();
		org.apache.lucene.search.Query luceneQuery = builder.spatial()
				.onField("location").within(100, Unit.KM)
				.ofLatitude(centerLatitude).andLongitude(centerLongitude)
				.createQuery();

		FullTextQuery hibQuery = postService.getFullTextSession()
				.createFullTextQuery(luceneQuery, Post.class);
		hibQuery.setProjection(FullTextQuery.SPATIAL_DISTANCE,
				FullTextQuery.THIS);
		hibQuery.setSpatialParameters(centerLatitude, centerLongitude,
				"location");
		List<Post> results = hibQuery.list();
	}

	/**
	 * 提高字段的重要性
	 */
	public void name18SearchBoosted() {
		Query mltQuery = postService.getQueryBuilder().moreLikeThis()
				.comparingField("summary").boostedTo(10f)
				.andField("description").toEntityWithId(12).createQuery();
	}

	/**
	 * 这个例子和上面的例子结果一样,不一样的写法
	 */
	public void name19Search() {
		Post coffee = new Post(); // managed entity from somewhere

		Query mltQuery = postService.getQueryBuilder().moreLikeThis()
				.comparingField("summary").boostedTo(10f)
				.andField("description").toEntity(coffee).createQuery();
	}

	/**
	 * 
	 */
	public void name20Search() {
		Post coffee = new Post(); // managed entity from somewhere
		Query mltQuery = postService.getQueryBuilder()
				.moreLikeThis()
				// 排除用于比较实体
				.excludeEntityUsedForComparison().comparingField("summary")
				.boostedTo(10f).andField("description").toEntity(coffee)
				.createQuery();
	}

	public void name21Search() {
		Post coffee = new Post(); // managed entity from somewhere
		Query mltQuery = postService.getQueryBuilder()
				.moreLikeThis()
				// 有显著的因素
				.favorSignificantTermsWithFactor(1f).comparingField("summary")
				.boostedTo(10f).andField("description").toEntity(coffee)
				.createQuery();
	}

	/**
	 * 组合查询
	 */
	public void name22SearchCombining() {
		// look for popular modern myths that are not urban
		Date twentiethCentury = new Date();
		QueryBuilder mythQB = postService.getQueryBuilder();
		Query luceneQuery = mythQB
				.bool()
				// 不一定：查询不包含子查询匹配的元素
				.must(mythQB.keyword().onField("description").matching("urban")
						.createQuery())
				.not()
				// 必须：查询必须包含子查询匹配的元素
				// range 范围查询
				.must(mythQB.range().onField("starred").above(4).createQuery())
				.must(mythQB.range().onField("creationDate")
						.above(twentiethCentury).createQuery()).createQuery();

		// look for popular myths that are preferably urban
		Query luceneQuery1 = mythQB
				.bool()
				// 应：查询查询应包含子查询匹配的元素
				.should(mythQB.keyword().onField("description")
						.matching("urban").createQuery())
				.must(mythQB.range().onField("starred").above(4).createQuery())
				.createQuery();

		// look for all myths except religious ones
		Query luceneQuery2 = mythQB
				.all()
				.except(mythQB.keyword().onField("description_stem")
						.matching("religion").createQuery()).createQuery();
	}

	public void name23Search() {

		QueryBuilder mythQB = postService.getQueryBuilder();
		// keyword 关键词搜索
		Query luceneQuery = mythQB
				.bool()
				.should(mythQB.keyword().onField("description")
						.matching("urban").createQuery())
				.should(mythQB.keyword().onField("name")
				// 提高整个查询或特定领域的特定因素
						.boostedTo(3)
						// 在处理这场无视分析仪
						.ignoreAnalyzer()
						// 忽略场桥时处理这一领域
						.ignoreFieldBridge()

						.matching("urban").createQuery())
				.must(mythQB
						.range()
						.boostedTo(5)
						// 过滤查询结果使用滤波器实例
						// .filteredBy(new ChainedFilter())
						// 所有的结果匹配的查询有一个恒定的得分等于增加
						.withConstantScore().onField("starred").above(4)
						.createQuery()).createQuery();
	}

	/**
	 * 排序查询
	 */
	public void name24Sort() {

		Query arg0 = postService.getQueryBuilder().keyword().onField("name")
				.matching("shh").createQuery();
		FullTextQuery fullTextQuery = postService.getFullTextSession()
				.createFullTextQuery(arg0, Post.class);
		Sort sort = new Sort(new SortField("name", Type.STRING));
		fullTextQuery.setSort(sort);

		List<Post> list = fullTextQuery.list();
	}

	/**
	 * 当你限制返回类型为一类负荷，Hibernate搜索使用一个单一的查询对象。它也考虑静态的抓取策略在你的域模型的定义。
	 * 它往往是有用的，但是，细化的抓取策略具体的使用情况。
	 */
	public void name24Query() {

		FullTextSession s = postService.getFullTextSession();

		Query luceneQuery = postService.getQueryBuilder().keyword()
				.onField("name").matching("shh").createQuery();

		Criteria criteria = s.createCriteria(Post.class).setFetchMode(
				"authors", FetchMode.JOIN);
		s.createFullTextQuery(luceneQuery).setCriteriaQuery(criteria);
	}

	public void name25Projection() {
		FullTextSession s = postService.getFullTextSession();
		Query luceneQuery = postService.getQueryBuilder().keyword()
				.onField("name").matching("shh").createQuery();

		FullTextQuery query = s.createFullTextQuery(luceneQuery, Book.class);
		query.setProjection("id", "name", "note", "postValue.value");
		List<Object[]> results = query.list();
		Object[] firstResult = (Object[]) results.get(0);
		Integer id = (Integer) firstResult[0];
		String name = (String) firstResult[1];
		String note = (String) firstResult[2];
		String postValue = (String) firstResult[3];
	}

	/**
	 * fulltextquery.this：返回初始化和管理的实体（如非预期查询会做）。
	 * fulltextquery.document：返回对象投影相关Lucene文档。
	 * fulltextquery.object_class：返回索引实体类。
	 * fulltextquery.score：返回文档评分查询。分数的比较结果方便对其他的对于一个给定的查询，但都无用时，比较不同的查询结果。
	 * fulltextquery.id：对预测对象的id属性值。 fulltextquery.document_id：Lucene文档ID。小心，
	 * Lucene文档ID可以改变两个IndexReader开口之间的加班。
	 * fulltextquery.explanation：返回匹配对象/文件在给定查询的Lucene的解释对象
	 * 。不要使用如果您检索大量数据。运行的解释通常是运行每个匹配的元素，整个Lucene的查询代价。确保你使用的投影！
	 */
	public void name26Projection() {
		FullTextSession s = postService.getFullTextSession();
		Query luceneQuery = postService.getQueryBuilder().keyword()
				.onField("name").matching("shh").createQuery();

		FullTextQuery query = s.createFullTextQuery(luceneQuery, Post.class);

		query.setProjection(FullTextQuery.SCORE, FullTextQuery.THIS,
				"mainAuthor.name");
		List results = query.list();
		Object[] firstResult = (Object[]) results.get(0);
		float score = (float) firstResult[0];
		Post book = (Post) firstResult[1];
		String authorName = (String) firstResult[2];
	}

	public void name27Projection() {
		FullTextSession s = postService.getFullTextSession();
		Query luceneQuery = postService.getQueryBuilder().keyword()
				.onField("name").matching("shh").createQuery();
		/**
		 * objectlookupmethod.persistence_context：如果最匹配的实体已经在持久化上下文（
		 * IE加载在会话或EntityManager）
		 * objectlookupmethod.second_level_cache：首先检查存储上下文，然后第二级缓存。
		 */
		FullTextQuery query = s.createFullTextQuery(luceneQuery, Post.class);
		query.initializeObjectsWith(ObjectLookupMethod.SECOND_LEVEL_CACHE,
				DatabaseRetrievalMethod.QUERY);
	}
}
