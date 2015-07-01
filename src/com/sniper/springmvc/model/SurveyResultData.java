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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 本表存放的是用户所有数据的集合
 * 
 * @author sniper
 * 
 */
@Entity
@Table(name = "mc_survey_result_data")
public class SurveyResultData extends BaseEntity {

	private static final long serialVersionUID = -200497905494303535L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	// 答案合集json形式
	@Column(columnDefinition = "longtext")
	private String resultData;
	// 答题时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date cTime = new Date();
	// ip
	@Column(length = 15)
	private String ip;
	// cookie
	private String cookie;
	// 用户浏览器信息
	private String agent;
	private String accept;
	private String locale;
	private String navigator;
	private String os;
	// 是否是登录用户
	private int uid;
	private String sessionid;

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, mappedBy = "resultData")
	private Set<SurveyResult> results = new HashSet<>();

	@ManyToOne(cascade = { CascadeType.REMOVE })
	@JoinColumn(name = "survey_id")
	private Survey survey;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getResultData() {
		return resultData;
	}

	public void setResultData(String resultData) {
		this.resultData = resultData;
	}

	public Date getcTime() {
		return cTime;
	}

	public void setcTime(Date cTime) {
		this.cTime = cTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getAgent() {
		return agent;
	}

	public String getAccept() {
		return accept;
	}

	public void setAccept(String accept) {
		this.accept = accept;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getNavigator() {
		return navigator;
	}

	public void setNavigator(String navigator) {
		this.navigator = navigator;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public Set<SurveyResult> getResults() {
		return results;
	}

	public void setResults(Set<SurveyResult> results) {
		this.results = results;
	}

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

}
