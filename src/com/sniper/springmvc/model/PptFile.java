package com.sniper.springmvc.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Boost;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.NotEmpty;

@Indexed
@Entity
@Table(name = "mc_pptfile")
public class PptFile extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@DocumentId
	private Integer id;
	@NotEmpty
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO, boost = @Boost(2f))
	private String name;
	@Column(columnDefinition = "text")
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO, boost = @Boost(2f))
	private String note;
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO, boost = @Boost(2f))
	private String tags;
	private Boolean original;
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO, boost = @Boost(2f))
	private String source;
	private int sort;
	private Boolean enabled;
	// 积分
	private int integral;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate = new Date();
	private int download;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "channel_id")
	private Channel channel;

	@ManyToOne(cascade = { CascadeType.PERSIST }, fetch = FetchType.EAGER)
	@JoinColumn(name = "uid")
	private AdminUser adminUser;

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE,
			CascadeType.REFRESH })
	@JoinColumns(value = { @JoinColumn(name = "ppt_file_id", referencedColumnName = "id") })
	@OrderBy("sort asc")
	private Set<Files> files = new HashSet<>();

	@OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE })
	@JoinColumn(name = "ppt_id")
	private Files ppt;

	private String flash;

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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getDownload() {
		return download;
	}

	public void setDownload(int download) {
		this.download = download;
	}

	public AdminUser getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(AdminUser adminUser) {
		this.adminUser = adminUser;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Set<Files> getFiles() {
		return files;
	}

	public void setFiles(Set<Files> files) {
		this.files = files;
	}

	public Files getPpt() {
		return ppt;
	}

	public void setPpt(Files ppt) {
		this.ppt = ppt;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public Boolean getOriginal() {
		return original;
	}

	public void setOriginal(Boolean original) {
		this.original = original;
	}

	public String getFlash() {
		return flash;
	}

	public void setFlash(String flash) {
		this.flash = flash;
	}

}
