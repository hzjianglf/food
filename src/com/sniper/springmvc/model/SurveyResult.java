package com.sniper.springmvc.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 此页面是保存每个问题及其答案
 * 
 * @author sniper
 * 
 */
@Entity
@Table(name = "mc_survey_result", indexes = { @Index(columnList = "quetion", name = "quetion") })
public class SurveyResult extends BaseEntity {

	private static final long serialVersionUID = -945541332137035362L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	// 被投票id或者人
	private int surveyId = 0;
	// 投票人可为空
	private int uid = 0;
	// 得票人数
	private int num = 0;
	// 问题id,因为问题的标题都是保存在数据库中的所以直接使用id即可
	private int quetion = 0;
	// 答案就不一样了,因为答案可以自己填写所以不能使用int
	@Column(columnDefinition = "text")
	private String answers;

	@ManyToOne(cascade = { CascadeType.REMOVE })
	@JoinColumn(name = "data_id")
	private SurveyResultData resultData;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(int surveyId) {
		this.surveyId = surveyId;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getQuetion() {
		return quetion;
	}

	public void setQuetion(int quetion) {
		this.quetion = quetion;
	}

	public String getAnswers() {
		return answers;
	}

	public void setAnswer(String answers) {
		this.answers = answers;
	}

	public SurveyResultData getResultData() {
		return resultData;
	}

	public void setResultData(SurveyResultData resultData) {
		this.resultData = resultData;
	}

}
