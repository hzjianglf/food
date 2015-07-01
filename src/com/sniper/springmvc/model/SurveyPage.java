package com.sniper.springmvc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 调查页面多个页面组成一个完整调查
 * 
 * @author laolang
 * 
 */
@Entity
@Table(name = "mc_survey_page")
public class SurveyPage extends BaseEntity {

	private static final long serialVersionUID = -4119371995632333205L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@NotEmpty
	private String name;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date ctime = new Date();
	private Integer sort;
	private String note;
	// optional 导致我 的数据在service里面不用flush不能保存数据
	@ManyToOne(cascade = { CascadeType.PERSIST })
	@JoinColumn(name = "survey_id")
	private Survey survey;

	// 问题列表
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "page", cascade = { CascadeType.REMOVE })
	@OrderBy("sort asc")
	private List<SurveyQuestion> sq = new ArrayList<>();

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

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public List<SurveyQuestion> getSq() {
		return sq;
	}

	public void setSq(List<SurveyQuestion> sq) {
		this.sq = sq;
	}

}
