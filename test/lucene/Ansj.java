package lucene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;

import love.cq.util.IOUtil;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.dic.LearnTool;
import org.ansj.domain.Nature;
import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.ansj.lucene4.AnsjAnalysis;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.nlpcn.commons.lang.tire.GetWord;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.library.Library;
import org.tartarus.snowball.ext.PorterStemmer;

/**
 * http://nlpchina.github.io/ansj_seg/
 * http://www.cnblogs.com/xing901022/p/3933675.html
 * 
 * @author sniper
 * 
 */
public class Ansj {

	public void text1() {
		// 基本分次
		List<Term> parse = BaseAnalysis.parse("让战士们过一个欢乐祥和的新春佳节。");
		System.out.println(parse);
		parse = ToAnalysis.parse("让战士们过一个欢乐祥和的新春佳节。");
		System.out.println(parse);

		parse = NlpAnalysis
				.parse("洁面仪配合洁面深层清洁毛孔 清洁鼻孔面膜碎觉使劲挤才能出一点点皱纹 脸颊毛孔修复的看不见啦 草莓鼻历史遗留问题没辙 脸和脖子差不多颜色的皮肤才是健康的 长期使用安全健康的比同龄人显小五到十岁 28岁的妹子看看你们的鱼尾纹");
		System.out.println(parse);
		// 索引分次
		parse = IndexAnalysis.parse("主副食品");
		System.out.println(parse);
	}

	/**
	 * 添加辞典 了分词默认把大写都转换为小写了.所以添加新词的时候要求必须是小写.
	 */
	public void add() {
		// 增加新词,中间按照'\t'隔开
		UserDefineLibrary.insertWord("ansj中文分词", "userDefine", 1000);
		List<Term> terms = ToAnalysis.parse("我觉得Ansj中文分词是一个不错的系统!我是王婆!");
		System.out.println("增加新词例子:" + terms);
		// 删除词语,只能删除.用户自定义的词典.
		UserDefineLibrary.removeWord("ansj中文分词");
		terms = ToAnalysis.parse("我觉得ansj中文分词是一个不错的系统!我是王婆!");
		System.out.println("删除用户自定义词典例子:" + terms);
	}

	/**
	 * 新词小工具
	 */

	public void newworld() {
		// 构建一个新词学习的工具类。这个对象。保存了所有分词中出现的新词。出现次数越多。相对权重越大。
		LearnTool learnTool = new LearnTool();

		// 进行词语分词。也就是nlp方式分词，这里可以分多篇文章
		NlpAnalysis.parse("说过，社交软件也是打着沟通的平台，让无数寂寞男女有了肉体与精神的寄托。", learnTool);
		NlpAnalysis.parse(
				"其实可以打着这个需求点去运作的互联网公司不应只是社交类软件与可穿戴设备，还有携程网，去哪儿网等等，订房订酒店多好的寓意",
				learnTool);
		NlpAnalysis.parse("张艺谋的卡宴，马明哲的戏", learnTool);

		// 取得学习到的topn新词,返回前10个。这里如果设置为0则返回全部
		System.out.println(learnTool.getTopTree(10));

		// 只取得词性为Nature.NR的新词
		System.out.println(learnTool.getTopTree(10, Nature.NR));
	}

	/**
	 * 关键词提取
	 */

	public void keyworld() {
		KeyWordComputer kwc = new KeyWordComputer(5);
		String title = "维基解密否认斯诺登接受委内瑞拉庇护";
		String content = "有俄罗斯国会议员，9号在社交网站推特表示，美国中情局前雇员斯诺登，已经接受委内瑞拉的庇护，不过推文在发布几分钟后随即删除。俄罗斯当局拒绝发表评论，而一直协助斯诺登的维基解密否认他将投靠委内瑞拉。　　俄罗斯国会国际事务委员会主席普什科夫，在个人推特率先披露斯诺登已接受委内瑞拉的庇护建议，令外界以为斯诺登的动向终于有新进展。　　不过推文在几分钟内旋即被删除，普什科夫澄清他是看到俄罗斯国营电视台的新闻才这样说，而电视台已经作出否认，称普什科夫是误解了新闻内容。　　委内瑞拉驻莫斯科大使馆、俄罗斯总统府发言人、以及外交部都拒绝发表评论。而维基解密就否认斯诺登已正式接受委内瑞拉的庇护，说会在适当时间公布有关决定。　　斯诺登相信目前还在莫斯科谢列梅捷沃机场，已滞留两个多星期。他早前向约20个国家提交庇护申请，委内瑞拉、尼加拉瓜和玻利维亚，先后表示答应，不过斯诺登还没作出决定。　　而另一场外交风波，玻利维亚总统莫拉莱斯的专机上星期被欧洲多国以怀疑斯诺登在机上为由拒绝过境事件，涉事国家之一的西班牙突然转口风，外长马加略]号表示愿意就任何误解致歉，但强调当时当局没有关闭领空或不许专机降落。";
		Collection<Keyword> result = kwc.computeArticleTfidf(title, content);
		System.out.println(result);
	}

	/**
	 * 词性标注
	 */

	public void NatureDemo() {
		List<Term> terms = ToAnalysis
				.parse("Ansj中文分词是一个真正的ict的实现.并且加入了自己的一些数据结构和算法的分词.实现了高效率和高准确率的完美结合!");
		new NatureRecognition(terms).recognition(); // 词性标注
		System.out.println(terms);
	}

	/**
	 * 对非ansj的分词结果进行词性标注
	 */

	public void NatureTagDemo() {
		String[] strs = { "对", "非", "ansj", "的", "分词", "结果", "进行", "词性", "标注" };
		List<String> lists = Arrays.asList(strs);
		System.out.println(lists);
		List<Term> terms = ToAnalysis
				.parse("Ansj中文分词是一个真正的ict的实现.并且加入了自己的一些数据结构和算法的分词.实现了高效率和高准确率的完美结合!");
		new NatureRecognition(terms).recognition();

		List<Term> recognition = NatureRecognition.recognition(lists, 0);

		System.out.println(recognition);
	}

	public void treesplit() throws Exception {
		/**
		 * 词典的构造.一行一个词后面是参数.可以从文件读取.可以是read流.
		 */
		String dic = "中国\t1\tzg\n人名\t2\n中国人民\t4\n人民\t3\n孙健\t5\nCSDN\t6\njava\t7\njava学习\t10\n";
		Forest forest = Library.makeForest(new BufferedReader(new StringReader(
				dic)));

		/**
		 * 删除一个单词
		 */
		Library.removeWord(forest, "中国");
		/**
		 * 增加一个新词
		 */
		Library.insertWord(forest, "中国人");
		String content = "中国人名识别是中国人民的一个骄傲.孙健人民在CSDN中学到了很多最早iteye是java学习笔记叫javaeye但是java123只是一部分";
		GetWord udg = forest.getWord(content);

		String temp = null;
		while ((temp = udg.getFrontWords()) != null)
			System.out.println(temp + "\t\t" + udg.getParam(1) + "\t\t"
					+ udg.getParam(2));
	}

	/**
	 * Ansj In Lucene
	 * 
	 * @throws IOException
	 */
	// @Test
	public void test1() throws IOException {
		// Token nt = new Token();

		Analyzer analyzer2 = new WhitespaceAnalyzer(); // 空格分词
		// Analyzer analyzer3 = new StandardAnalyzer(); // 单字分中文
		Analyzer analyzer4 = new AnsjAnalysis();

		StringReader reader = new StringReader(
				"\n\n\n\n\n\n\n我从小就不由自主地认为自己长大以后一定得成为一个象我父亲一样的画家, 可能是父母潜移默化的影响。其实我根本不知道作为画家意味着什么，我是否喜欢，最重要的是否适合我，我是否有这个才华。其实人到中年的我还是不确定我最喜欢什么，最想做的是什么？我相信很多人和我一样有同样的烦恼。毕竟不是每个人都能成为作文里的宇航员，科学家和大教授。知道自己适合做什么，喜欢做什么，能做好什么其实是个非常困难的问题。"
						+ "幸运的是，我想我的孩子不会为这个太过烦恼。通过老大，我慢慢发现美国高中的一个重要功能就是帮助学生分析他们的专长和兴趣，从而帮助他们选择大学的专业和未来的职业。我觉得帮助一个未成形的孩子找到她未来成长的方向是个非常重要的过程。"
						+ "美国高中都有专门的职业顾问，通过接触不同的课程，和各种心理，个性，兴趣很多方面的问答来帮助每个学生找到最感兴趣的专业。这样的教育一般是要到高年级才开始， 可老大因为今年上计算机的课程就是研究一个职业走向的软件项目，所以她提前做了这些考试和面试。看来以后这样的教育会慢慢由电脑来测试了。老大带回家了一些试卷，我挑出一些给大家看看。这门课她花了2个多月才做完，这里只是很小的一部分。"
						+ "在测试里有这样的一些问题："
						+ "你是个喜欢动手的人吗？ 你喜欢修东西吗？你喜欢体育运动吗？你喜欢在室外工作吗？你是个喜欢思考的人吗？你喜欢数学和科学课吗？你喜欢一个人工作吗？你对自己的智力自信吗？你的创造能力很强吗？你喜欢艺术，音乐和戏剧吗？  你喜欢自由自在的工作环境吗？你喜欢尝试新的东西吗？ 你喜欢帮助别人吗？你喜欢教别人吗？你喜欢和机器和工具打交道吗？你喜欢当领导吗？你喜欢组织活动吗？你什么和数字打交道吗？");
		TokenStream ts = analyzer2.tokenStream("sentence", reader);
		ts.reset();
		System.out.println("start: " + (new Date()));
		long before = System.currentTimeMillis();
		while (ts.incrementToken()) {
			System.out.println(ts.getAttribute(CharTermAttribute.class));
		}

		ts.close();
		analyzer4.close();
		analyzer2.close();
		// analyzer3.close();
		long now = System.currentTimeMillis();
		System.out.println("time: " + (now - before) / 1000.0 + " s");
	}

	@Test
	public void indexTest() throws CorruptIndexException,
			LockObtainFailedException, IOException, ParseException {
		HashSet<String> hs = new HashSet<>();

		System.out.println(ResourceBundle.getBundle("i18n").getString(
				"i18n.username"));

		BufferedReader reader2 = IOUtil.getReader("s", "UTF-8");
		String word = null;
		while ((word = reader2.readLine()) != null) {
			hs.add(word);
		}

		Analyzer analyzer1 = new AnsjAnalysis(hs, false);
		Directory directory = null;
		IndexWriter iwriter = null;

		BufferedReader reader = IOUtil.getReader(
				"/approot/lucene/a/indextest.txt", "UTF-8");
		String temp = null;
		StringBuilder sb = new StringBuilder();
		while ((temp = reader.readLine()) != null) {
			sb.append(temp);
			sb.append("\n");
		}
		reader.close();
		String text = sb.toString();

		text = "开源项目管理你喜欢在室外工作吗？你是个喜欢思考的人吗？你喜欢数学和科学课吗？你喜欢一个人工作吗？你对自己的智力自信吗？你的创造能力很强吗？你喜欢艺术，音乐和戏剧吗？  你喜欢自由自在的工作环境吗？你喜欢尝试新的东西吗？ 你喜欢帮助别人吗？你喜欢教别人吗？你喜欢和机器和工具打交道吗？你喜欢当领导吗？你喜欢组织活动吗？你什么和数字打交道吗？";

		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(Version.LATEST,
				analyzer);
		// 本地储存
		// Directory directory = FSDirectory.open("/tmp/testindex");
		// 建立内存索引对象
		directory = new RAMDirectory();
		iwriter = new IndexWriter(directory, config);

		// BufferedReader reader =
		// IOUtil.getReader("/Users/ansj/Documents/快盘/分词/语料/1998年人民日报分词语料_未区分.txt",
		// "GBK");
		// String temp = null;
		// while ((temp = reader.readLine()) != null) {
		// addContent(iwriter, temp);
		// }
		addContent(iwriter, text);
		addContent(iwriter, text);
		addContent(iwriter, text);
		addContent(iwriter, text);

		iwriter.commit();
		iwriter.close();

		System.out.println("索引建立完毕");

		search(analyzer, directory, "室外工作");
		directory.close();
	}

	private void search(Analyzer analyzer, Directory directory, String queryStr)
			throws CorruptIndexException, IOException, ParseException,
			org.apache.lucene.queryparser.classic.ParseException {
		// 查询索引
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher isearcher = new IndexSearcher(indexReader);
		QueryParser tq = new QueryParser("text", analyzer);
		Query query = tq.parse(queryStr);
		System.out.println(query);
		TopDocs hits = isearcher.search(query, 5);
		System.out.println(queryStr + ":共找到" + hits.totalHits + "条记录!");
		for (int i = 0; i < hits.scoreDocs.length; i++) {
			int docId = hits.scoreDocs[i].doc;
			Document document = isearcher.doc(docId);
			System.out.println(toHighlighter(analyzer, query, document));
		}
		indexReader.close();
	}

	/**
	 * 高亮设置
	 * 
	 * @param query
	 * @param doc
	 * @param field
	 * @return
	 */
	private String toHighlighter(Analyzer analyzer, Query query, Document doc) {
		String field = "text";
		try {
			SimpleHTMLFormatter simpleHtmlFormatter = new SimpleHTMLFormatter(
					"<font color=\"red\">", "</font>");

			Highlighter highlighter = new Highlighter(simpleHtmlFormatter,
					new QueryScorer(query));
			TokenStream tokenStream1 = analyzer.tokenStream("text",
					new StringReader(doc.get(field)));
			String highlighterStr = highlighter.getBestFragment(tokenStream1,
					doc.get(field));
			return highlighterStr == null ? doc.get(field) : highlighterStr;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidTokenOffsetsException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void addContent(IndexWriter iwriter, String text)
			throws CorruptIndexException, IOException {
		Document doc = new Document();
		doc.add(new Field("text", text, TextField.TYPE_STORED));
		iwriter.addDocument(doc);
	}

	@Test
	public void poreterTest() {
		PorterStemmer ps = new PorterStemmer();
		ps.setCurrent("apache");
		ps.stem();

		// System.out.println(ps.stem());
	}

}
