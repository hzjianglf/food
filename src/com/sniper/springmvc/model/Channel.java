package com.sniper.springmvc.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "mc_channel", indexes = {
		@Index(columnList = "name", name = "name"),
		@Index(columnList = "showType", name = "showType"),
		@Index(columnList = "sort", name = "sort") })
public class Channel extends BaseEntity {

	private static final long serialVersionUID = -331296954351916696L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;
	@NotNull
	@Size(message = "名称不合法", max = 200, min = 2)
	@Column(name = "name")
	private String name;
	private int fid = 0;
	private int sort = 0;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "stime", updatable = false)
	private Date stime = new Date();
	@Temporal(TemporalType.TIMESTAMP)
	private Date letime = new Date();
	private Integer uid = 0;
	@Column(columnDefinition = "BIT(1) default b'1'")
	private Boolean status;
	private String url;
	// 页面显示方式
	private int showType;
	@Column(columnDefinition = "BIT(1) default b'0'")
	private Boolean showHome;

	private String attachement;

	private String note;

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

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public Date getStime() {
		return stime;
	}

	public void setStime(Date stime) {
		this.stime = stime;
	}

	public Date getLetime() {
		return letime;
	}

	public void setLetime(Date letime) {
		this.letime = letime;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getShowType() {
		return showType;
	}

	public void setShowType(int showType) {
		this.showType = showType;
	}

	public Boolean getShowHome() {
		return showHome;
	}

	public void setShowHome(Boolean showHome) {
		this.showHome = showHome;
	}

	public String getAttachement() {
		return attachement;
	}

	public void setAttachement(String attachement) {
		this.attachement = attachement;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Channel(Integer id) {
		super();
		this.id = id;
	}

	public Channel() {
	}

}