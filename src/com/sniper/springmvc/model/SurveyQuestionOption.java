package com.sniper.springmvc.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "mc_survey_question_option")
@DynamicInsert(true)
@DynamicUpdate(true)
public class SurveyQuestionOption extends BaseEntity {

	private static final long serialVersionUID = 7320570737861553596L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@NotEmpty
	private String name;
	private String title;
	// 可填写
	@Column(columnDefinition = " BIT(1) NOT NULL DEFAULT b'0'")
	private boolean writed;

	private Integer sort;
	@Column(columnDefinition = " BIT(1) NOT NULL DEFAULT b'0'")
	private boolean checked;
	// 填写之后跳到那个提
	private Integer goid;
	// 默认值
	@Column(columnDefinition = "text")
	private String defaultvalue;
	// 答案
	private String answer;

	@ManyToOne(cascade = { CascadeType.PERSIST })
	@JoinColumn(name = "question_id")
	private SurveyQuestion question;

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isWrited() {
		return writed;
	}

	public void setWrited(boolean writed) {
		this.writed = writed;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Integer getGoid() {
		return goid;
	}

	public void setGoid(Integer goid) {
		this.goid = goid;
	}

	public String getDefaultvalue() {
		return defaultvalue;
	}

	public void setDefaultvalue(String defaultvalue) {
		this.defaultvalue = defaultvalue;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public SurveyQuestion getQuestion() {
		return question;
	}

	public void setQuestion(SurveyQuestion question) {
		this.question = question;
	}

}
