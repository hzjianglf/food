package com.sniper.springmvc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "mc_mail", indexes = { @Index(columnList = "subject", name = "subject") })
public class Mail extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;
	private String subject;
	@Basic(fetch = FetchType.LAZY)
	@Column(columnDefinition = "LONGTEXT")
	private String content;

	private String mailFrom;
	private String mailTo;
	// 抄送人
	private String mailCc;
	// 暗送人
	private String mailBcc;
	// 是否是超文本内容电子邮件
	private boolean mimeMessage;
	private boolean send;
	private String errorMessage;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(unique = false)
	private Date sendTime = new Date();

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinTable(name = "mc_mail_files", joinColumns = @JoinColumn(name = "mail_id"), inverseJoinColumns = @JoinColumn(name = "file_id"))
	private List<Files> files = new ArrayList<>();

	public String getId() {
		if ("".equals(this.id)) {
			this.id = UUID.randomUUID().toString();
		}
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSubject() {
		if(subject == null){
			return "主题";
		}
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMailFrom() {
		return mailFrom;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	public String getMailTo() {
		return mailTo;
	}

	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	public String getMailCc() {
		return mailCc;
	}

	public void setMailCc(String mailCc) {
		this.mailCc = mailCc;
	}

	public String getMailBcc() {
		return mailBcc;
	}

	public void setMailBcc(String mailBcc) {
		this.mailBcc = mailBcc;
	}

	public boolean isMimeMessage() {
		return mimeMessage;
	}

	public void setMimeMessage(boolean mimeMessage) {
		this.mimeMessage = mimeMessage;
	}

	public boolean isSend() {
		return send;
	}

	public void setSend(boolean send) {
		this.send = send;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public List<Files> getFiles() {
		return files;
	}

	public void setFiles(List<Files> files) {
		this.files = files;
	}

}
