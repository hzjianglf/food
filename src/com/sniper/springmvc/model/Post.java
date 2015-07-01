package com.sniper.springmvc.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lucene.PaddedIntegerBridge;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.Boost;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;
import org.hibernate.validator.constraints.NotEmpty;

//表明类的实体名称默认类名
@Entity
@Indexed
@Cacheable
@Table(name = "mc_post", indexes = {
		@Index(columnList = "name", name = "nameindex", unique = false),
		@Index(columnList = "tags", name = "tags"),
		@Index(columnList = "sort", name = "sort") })
// 添加2个分析其
@AnalyzerDef(name = "postName", tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class), filters = {
		@TokenFilterDef(factory = LowerCaseFilterFactory.class),
		@TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = { @Parameter(name = "language", value = "English") }) })
public class Post extends BaseEntity {

	private static final long serialVersionUID = -8153642939179018327L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@DocumentId
	private Integer id;
	@NotEmpty
	@Analyzer(definition = "postName")
	// boost 权重
	@Field(index = org.hibernate.search.annotations.Index.YES, analyze = Analyze.YES, store = Store.NO, boost = @Boost(2f))
	private String name;
	@Analyzer(definition = "postName")
	@Field(index = org.hibernate.search.annotations.Index.YES, analyze = Analyze.YES, store = Store.NO)
	@Column(columnDefinition = "text")
	private String note;
	@Field(index = org.hibernate.search.annotations.Index.YES, analyze = Analyze.YES, store = Store.NO)
	private String source;
	@Field(index = org.hibernate.search.annotations.Index.YES, analyze = Analyze.YES, store = Store.NO)
	private int status;
	@Column(insertable = false)
	private String lastEditIp;
	@Field(index = org.hibernate.search.annotations.Index.YES, analyze = Analyze.YES, store = Store.NO)
	//自定义 桥数据处理
	@FieldBridge(impl = PaddedIntegerBridge.class)
	private int sort;
	@Field(index = org.hibernate.search.annotations.Index.YES, analyze = Analyze.YES, store = Store.NO)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(insertable = false)
	@DateBridge(resolution = Resolution.SECOND)
	private Date letime = new Date();
	@Temporal(TemporalType.TIMESTAMP)
	@Field(index = org.hibernate.search.annotations.Index.YES, analyze = Analyze.NO, store = Store.NO)
	@Column(updatable = false)
	@DateBridge(resolution = Resolution.SECOND)
	private Date stime = new Date();
	private String attachment;
	@Field(index = org.hibernate.search.annotations.Index.YES, analyze = Analyze.YES, store = Store.NO)
	private String url;
	// int 初始化为0,integer初始化为null
	private Integer commentCount;
	@Field(index = org.hibernate.search.annotations.Index.YES, analyze = Analyze.YES, store = Store.NO)
	private String language = "zh_CN";

	@IndexedEmbedded
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "postValueId")
	// 默认为延迟加载,由于这里是主键关联，在住表删除时，次表没变化，是个bug
	private PostValue postValue;

	@IndexedEmbedded
	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH })
	// referencedColumnName为每个表关联列，主键不用写
	@JoinTable(name = "mc_post_node", joinColumns = @JoinColumn(name = "pn_pid"), inverseJoinColumns = @JoinColumn(name = "pn_cid"))
	private Set<Channel> channels = new HashSet<>();

	@ManyToOne(cascade = { CascadeType.PERSIST }, fetch = FetchType.EAGER)
	@JoinColumn(name = "uid")
	private AdminUser adminUser;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH,
			CascadeType.REMOVE })
	@JoinTable(name = "mc_post_files", joinColumns = @JoinColumn(name = "post_id"), inverseJoinColumns = @JoinColumn(name = "file_id"))
	private Set<Files> files = new HashSet<>();

	private String target = "_self";
	@Field(index = org.hibernate.search.annotations.Index.YES, analyze = Analyze.YES, store = Store.NO)
	private String tags;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getLastEditIp() {
		return lastEditIp;
	}

	public void setLastEditIp(String lastEditIp) {
		this.lastEditIp = lastEditIp;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Date getLetime() {
		return letime;
	}

	public void setLetime(Date letime) {
		this.letime = letime;
	}

	public Date getStime() {
		return stime;
	}

	public void setStime(Date stime) {
		this.stime = stime;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public PostValue getPostValue() {
		return postValue;
	}

	public void setPostValue(PostValue postValue) {
		this.postValue = postValue;
	}

	public Set<Channel> getChannels() {
		return channels;
	}

	public void setChannels(Set<Channel> channels) {
		this.channels = channels;
	}

	/**
	 * 配合查询使用的构造器
	 * 
	 * @param id
	 * @param name
	 */
	public Post(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Post() {

	}

	public void setAdminUser(AdminUser adminUser) {
		this.adminUser = adminUser;
	}

	public AdminUser getAdminUser() {
		return adminUser;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setFiles(Set<Files> files) {
		this.files = files;
	}

	public Set<Files> getFiles() {
		return files;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

}
