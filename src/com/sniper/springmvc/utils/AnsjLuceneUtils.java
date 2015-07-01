package com.sniper.springmvc.utils;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import love.cq.util.IOUtil;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.dic.LearnTool;
import org.ansj.domain.Nature;
import org.ansj.domain.NewWord;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

/**
 * nlpchina.github.io/ansj_seg
 * 
 * @author sniper
 * 
 */
public class AnsjLuceneUtils {

	private static String learnToolPath = "WebRoot/WEB-INF/library/learnTool.snap";
	private static LearnTool learnTool = new LearnTool();

	/**
	 * 最基本分次 基本就是保证了最基本的分词.词语颗粒度最非常小的..所涉及到的词大约是10万左右.
	 * 
	 * 基本分词速度非常快.在macAir上.能到每秒300w字每秒.同时准确率也很高.但是对于新词他的功能十分有限
	 * 
	 * @param key
	 * @return
	 */
	public static List<Term> baseAnalysis(String key) {

		return BaseAnalysis.parse(key);
	}

	/**
	 * 精准分词 精准分词是Ansj分词的店长推荐款
	 * 
	 * 它在易用性,稳定性.准确性.以及分词效率上.都取得了一个不错的平衡.
	 * 
	 * 如果你初次赏识Ansj如果你想开箱即用.那么就用这个分词方式是不会错的.
	 * 
	 * @param str
	 * @return
	 */
	public static List<Term> toAnalysis(String str) {
		return ToAnalysis.parse(str);
	}

	/**
	 * 
	 * 最牛叉,需要完整的jar包
	 * 
	 * nlp分词 nlp分词是总能给你惊喜的一种分词方式.
	 * 
	 * 它可以识别出未登录词.但是它也有它的缺点.速度比较慢.稳定性差.ps:我这里说的慢仅仅是和自己的其他方式比较.应该是40w字每秒的速度吧.
	 * 
	 * 个人觉得nlp的适用方式.1.语法实体名抽取.未登录词整理.只要是对文本进行发现分析等工作
	 * 
	 * @param str
	 * @return
	 */
	public static List<Term> NlpAnalysis(String str) {
		return NlpAnalysis.parse(str);
	}

	/**
	 * 面向索引的分词 面向索引的分词。故名思议就是适合在lucene等文本检索中用到的分词。 主要考虑以下两点
	 * 
	 * 召回率 召回率是对分词结果尽可能的涵盖。比如对“上海虹桥机场南路” 召回结果是[上海/ns, 上海虹桥机场/nt, 虹桥/ns, 虹桥机场/nz,
	 * 机场/n, 南路/nr] 准确率 其实这和召回本身是具有一定矛盾性的Ansj的强大之处是很巧妙的避开了这两个的冲突
	 * 。比如我们常见的歧义句“旅游和服务”->对于一般保证召回 。大家会给出的结果是“旅游 和服 服务”
	 * 对于ansj不存在跨term的分词。意思就是。召回的词只是针对精准分词之后的结果的一个细分。比较好的解决了这个问题
	 * 
	 * @param str
	 * @return
	 */
	public static List<Term> indexAnalysis(String str) {
		return IndexAnalysis.parse(str);
	}

	/**
	 * 新词发现小工具 写入本地文件
	 * 
	 * @param key
	 * @param length
	 * @return
	 */
	public static List<Entry<String, Double>> learnToolWrite(String key,
			int length) {

		NlpAnalysis.parse(key, learnTool);
		// learnTool.getTopTree(length, Nature.NR);

		List<Entry<String, Double>> topTree = learnTool.getTopTree(length,
				Nature.NW);
		StringBuilder sb = new StringBuilder();
		for (Entry<String, Double> entry : topTree) {
			sb.append(entry.getKey() + "\t" + entry.getValue() + "\n");
		}
		IOUtil.Writer(learnToolPath, IOUtil.UTF8, sb.toString());
		sb = null;
		return topTree;
	}

	/**
	 * 新词发现小工具 读取本地文件
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public static List<Entry<String, Double>> learnToolRead(int length)
			throws UnsupportedEncodingException {
		HashMap<String, Double> loadMap = IOUtil.loadMap(learnToolPath,
				IOUtil.UTF8, String.class, Double.class);
		for (Entry<String, Double> entry : loadMap.entrySet()) {
			learnTool.addTerm(new NewWord(entry.getKey(), Nature.NW, entry
					.getValue()));
			learnTool.active(entry.getKey());
		}

		return learnTool.getTopTree(length);
	}

	/**
	 * 关键词提取的例子
	 * 
	 * @param length
	 *            关键词提取的例子
	 * @param title
	 *            标题
	 * @param content
	 *            内容
	 */
	public static List<Keyword> keyWordCompuer(int length, String title,
			String content) {
		KeyWordComputer kwc = new KeyWordComputer(length);
		List<Keyword> result = kwc.computeArticleTfidf(title, content);
		return result;
	}

	public static void main(String[] args) {
		
		KeyWordComputer kwc = new KeyWordComputer(2);
		String title = "维基解密否认斯诺登接受委内瑞拉庇护";
		String content = "有俄罗斯国会议员，9号在社交网站推特表示，美国中情局前雇员斯诺登，已经接受委内瑞拉的庇护，不过推文在发布几分钟后随即删除。俄罗斯当局拒绝发表评论，而一直协助斯诺登的维基解密否认他将投靠委内瑞拉。　　俄罗斯国会国际事务委员会主席普什科夫，在个人推特率先披露斯诺登已接受委内瑞拉的庇护建议，令外界以为斯诺登的动向终于有新进展。　　不过推文在几分钟内旋即被删除，普什科夫澄清他是看到俄罗斯国营电视台的新闻才这样说，而电视台已经作出否认，称普什科夫是误解了新闻内容。　　委内瑞拉驻莫斯科大使馆、俄罗斯总统府发言人、以及外交部都拒绝发表评论。而维基解密就否认斯诺登已正式接受委内瑞拉的庇护，说会在适当时间公布有关决定。　　斯诺登相信目前还在莫斯科谢列梅捷沃机场，已滞留两个多星期。他早前向约20个国家提交庇护申请，委内瑞拉、尼加拉瓜和玻利维亚，先后表示答应，不过斯诺登还没作出决定。　　而另一场外交风波，玻利维亚总统莫拉莱斯的专机上星期被欧洲多国以怀疑斯诺登在机上为由拒绝过境事件，涉事国家之一的西班牙突然转口风，外长马加略]号表示愿意就任何误解致歉，但强调当时当局没有关闭领空或不许专机降落。";
		Collection<Keyword> result = kwc.computeArticleTfidf(title, content);
		System.out.println(result);
		System.out.println("------------");

	}
}
