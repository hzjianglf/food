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

@Entity
@Table(name = "mc_set_passowrd", indexes = { @Index(columnList = "sign", name = "sign") })
public class SetPassword extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String email;
	private Integer uid;
	private String keyCode;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date sendTime = new Date();
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;
	@Column(columnDefinition = "text", updatable = false)
	private String publicKey;
	@Column(columnDefinition = "text", updatable = false)
	private String privateKey;
	@Column(updatable = false)
	private String sign;
	private boolean signaTrue = false;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(String keyCode) {
		this.keyCode = keyCode;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public void setSignaTrue(boolean signaTrue) {
		this.signaTrue = signaTrue;
	}

	public boolean isSignaTrue() {
		return signaTrue;
	}

}
