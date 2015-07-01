package com.sniper.springmvc.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "mc_members")
public class Members extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer uid;
	private String username;
	private String password;
	private String email;
	private Boolean checkEmail;
	private String myid;
	private String myidkey;
	private String regip;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date regdate;
	private String lastloginip;
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastlogintime;

	// 用户是否启用
	@Column(name = "ENABLED", columnDefinition = "BIT(1)  DEFAULT b'1'")
	private Boolean enabled;
	// 用户是否锁定
	@Column(name = "LOCKED", columnDefinition = "BIT(1) default b'0'")
	private Boolean locked;
	// 用户过期时间戳
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "USERNAME_EXPIRED")
	private Date usernameExpired;
	// 密码过期时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PASSWORD_EXPIRED")
	private Date passwordExpired;

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getCheckEmail() {
		return checkEmail;
	}

	public void setCheckEmail(Boolean checkEmail) {
		this.checkEmail = checkEmail;
	}

	public String getMyid() {
		return myid;
	}

	public void setMyid(String myid) {
		this.myid = myid;
	}

	public String getMyidkey() {
		return myidkey;
	}

	public void setMyidkey(String myidkey) {
		this.myidkey = myidkey;
	}

	public String getRegip() {
		return regip;
	}

	public void setRegip(String regip) {
		this.regip = regip;
	}

	public Date getRegdate() {
		return regdate;
	}

	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}

	public String getLastloginip() {
		return lastloginip;
	}

	public void setLastloginip(String lastloginip) {
		this.lastloginip = lastloginip;
	}

	public Date getLastlogintime() {
		return lastlogintime;
	}

	public void setLastlogintime(Date lastlogintime) {
		this.lastlogintime = lastlogintime;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public Date getUsernameExpired() {
		return usernameExpired;
	}

	public void setUsernameExpired(Date usernameExpired) {
		this.usernameExpired = usernameExpired;
	}

	public Date getPasswordExpired() {
		return passwordExpired;
	}

	public void setPasswordExpired(Date passwordExpired) {
		this.passwordExpired = passwordExpired;
	}

}
