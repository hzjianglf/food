package com.sniper.springmvc.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "mc_files")
public class Files extends BaseEntity {

	private static final long serialVersionUID = -8962752040834738451L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String oldName;
	private String newPath;
	private String hash;
	// 来源
	private String postUrl;
	@Temporal(TemporalType.TIMESTAMP)
	private Date stime = new Date();
	// 时间发生时间
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date eventTimeDate;

	private int download;
	@Column(name = "mf_size")
	private long size;
	private int width;
	private int height;
	private String fileType;
	private String tags;
	private Boolean isMain;
	private Boolean isThumb;
	private String thumbPrefix;
	private int sort = (int) (System.currentTimeMillis() / 1000);
	// 是否属于多媒体
	private Boolean album;
	private String suffix;

	@OneToOne
	@JoinColumn(name = "uid")
	private AdminUser adminUser;

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST })
	@JoinColumn(name = "c_id")
	private Channel channel;

	@ManyToOne
	@JoinColumn(name = "a_id")
	private Albums albums;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public String getNewPath() {
		return newPath;
	}

	public void setNewPath(String newPath) {
		this.newPath = newPath;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getPostUrl() {
		return postUrl;
	}

	public void setPostUrl(String postUrl) {
		this.postUrl = postUrl;
	}

	public Date getStime() {
		return stime;
	}

	public void setStime(Date stime) {
		this.stime = stime;
	}

	public Date getEventTimeDate() {
		return eventTimeDate;
	}

	public void setEventTimeDate(Date eventTimeDate) {
		this.eventTimeDate = eventTimeDate;
	}

	public int getDownload() {
		return download;
	}

	public void setDownload(int download) {
		this.download = download;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public Boolean getIsMain() {
		return isMain;
	}

	public void setIsMain(Boolean isMain) {
		this.isMain = isMain;
	}

	public Boolean getIsThumb() {
		return isThumb;
	}

	public void setIsThumb(Boolean isThumb) {
		this.isThumb = isThumb;
	}

	public String getThumbPrefix() {
		return thumbPrefix;
	}

	public void setThumbPrefix(String thumbPrefix) {
		this.thumbPrefix = thumbPrefix;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public AdminUser getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(AdminUser adminUser) {
		this.adminUser = adminUser;
	}

	public Boolean getAlbum() {
		return album;
	}

	public void setAlbum(Boolean album) {
		this.album = album;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Albums getAlbums() {
		return albums;
	}

	public void setAlbums(Albums albums) {
		this.albums = albums;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

}
