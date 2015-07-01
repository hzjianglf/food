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
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 调查属性
 * 
 * @author laolang
 * 
 */
@Entity
@Table(name = "mc_survey", indexes = { @Index(columnList = "title", name = "title") })
public class Survey extends BaseEntity {

	private static final long serialVersionUID = -7334373665483843888L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@NotNull
	@NotEmpty
	private String title;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date cTime = new Date();
	// 当前
	private Integer peopleNum;
	// 规定最大参与人数
	private Integer peopleMaxNum;
	private boolean locked = true;
	// 设置密码
	private String password;
	private boolean page = false;
	private int template = 0;
	@Column(columnDefinition = "text")
	private String note;
	private String submitName = "完成问卷";
	private String listStyle = "";
	// 设置问卷到用户的关系
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "uid")
	private AdminUser adminUser;
	// 计划开始时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date entDate;

	private Boolean verifyCode;
	private Integer verifyIpNum;
	private Integer verifyPhone;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "survey", cascade = { CascadeType.REMOVE })
	@OrderBy("sort asc")
	private List<SurveyPage> surveyPages = new ArrayList<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getcTime() {
		return cTime;
	}

	public void setcTime(Date cTime) {
		this.cTime = cTime;
	}

	public Integer getPeopleNum() {
		return peopleNum;
	}

	public void setPeopleNum(Integer peopleNum) {
		this.peopleNum = peopleNum;
	}

	public Integer getPeopleMaxNum() {
		return peopleMaxNum;
	}

	public void setPeopleMaxNum(Integer peopleMaxNum) {
		this.peopleMaxNum = peopleMaxNum;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isPage() {
		return page;
	}

	public void setPage(boolean page) {
		this.page = page;
	}

	public int getTemplate() {
		return template;
	}

	public void setTemplate(int template) {
		this.template = template;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getSubmitName() {
		return submitName;
	}

	public void setSubmitName(String submitName) {
		this.submitName = submitName;
	}

	public String getListStyle() {
		return listStyle;
	}

	public void setListStyle(String listStyle) {
		this.listStyle = listStyle;
	}

	public AdminUser getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(AdminUser adminUser) {
		this.adminUser = adminUser;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEntDate() {
		return entDate;
	}

	public void setEntDate(Date entDate) {
		this.entDate = entDate;
	}

	public Boolean getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(Boolean verifyCode) {
		this.verifyCode = verifyCode;
	}

	public Integer getVerifyIpNum() {
		return verifyIpNum;
	}

	public void setVerifyIpNum(Integer verifyIpNum) {
		this.verifyIpNum = verifyIpNum;
	}

	public Integer getVerifyPhone() {
		return verifyPhone;
	}

	public void setVerifyPhone(Integer verifyPhone) {
		this.verifyPhone = verifyPhone;
	}

	public List<SurveyPage> getSurveyPages() {
		return surveyPages;
	}

	public void setSurveyPages(List<SurveyPage> surveyPages) {
		this.surveyPages = surveyPages;
	}

}
