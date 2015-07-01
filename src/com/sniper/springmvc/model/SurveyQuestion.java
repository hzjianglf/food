package com.sniper.springmvc.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "mc_survey_question")
@DynamicUpdate(true)
@DynamicInsert(true)
public class SurveyQuestion extends BaseEntity {

	private static final long serialVersionUID = -2897085975644254403L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	// 名称
	@NotEmpty
	private String name;
	// 填写提示
	private String title;
	private Integer type = 1;
	private Integer sort;
	// 当选择那个题的时候显示此题
	private String relation;
	// 其他样式, 文本框,;下拉框
	private Integer otherStyle;
	// 矩阵集合行标题
	private String matrixRowTitles;
	// 矩阵列标题
	private String matrixColTitles;
	// 矩阵下拉选集
	private String matrixSelectOptions;
	// optional 表示是否必须
	@ManyToOne(cascade = { CascadeType.PERSIST })
	@JoinColumn(name = "page_id")
	private SurveyPage page;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "question", cascade = { CascadeType.REMOVE })
	@OrderBy("sort asc")
	private List<SurveyQuestionOption> options = new ArrayList<>();

	@OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "rule_id")
	private SurveyQuestionRules rules;

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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public SurveyPage getPage() {
		return page;
	}

	public void setPage(SurveyPage page) {
		this.page = page;
	}

	public List<SurveyQuestionOption> getOptions() {
		return options;
	}

	public void setOptions(List<SurveyQuestionOption> options) {
		this.options = options;
	}

	public SurveyQuestionRules getRules() {
		return rules;
	}

	public void setRules(SurveyQuestionRules rules) {
		this.rules = rules;
	}

	public Integer getOtherStyle() {
		return otherStyle;
	}

	public void setOtherStyle(Integer otherStyle) {
		this.otherStyle = otherStyle;
	}

	public String getMatrixRowTitles() {
		return matrixRowTitles;
	}

	public void setMatrixRowTitles(String matrixRowTitles) {
		this.matrixRowTitles = matrixRowTitles;
	}

	public String getMatrixColTitles() {
		return matrixColTitles;
	}

	public void setMatrixColTitles(String matrixColTitles) {
		this.matrixColTitles = matrixColTitles;
	}

	public String getMatrixSelectOptions() {
		return matrixSelectOptions;
	}

	public void setMatrixSelectOptions(String matrixSelectOptions) {
		this.matrixSelectOptions = matrixSelectOptions;
	}

}
