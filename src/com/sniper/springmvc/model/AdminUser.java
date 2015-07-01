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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "mc_admin_user")
@DynamicUpdate(true)
public class AdminUser extends BaseEntity {

	private static final long serialVersionUID = -1749860151352757711L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Column(columnDefinition = "text")
	private String unit;
	@Column(unique = true, updatable = false)
	private String name;
	@Column(name = "password", nullable = true)
	private String password;
	private String nickName;
	@Email
	private String email;
	// 用户是否启用
	@Column(name = "ENABLED", columnDefinition = "BIT(1)  DEFAULT b'1'")
	private Boolean enabled;
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
	// 用户是否锁定
	@Column(name = "LOCKED", columnDefinition = "BIT(1) default b'0'")
	private Boolean locked;
	// 用户加密密钥结果
	private String signCode;
	// 创建时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ctime", updatable = false)
	private Date ctime = new Date();

	// 对应用户组
	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST })
	@JoinTable(name = "mc_admin_user_group", joinColumns = @JoinColumn(name = "uid"), inverseJoinColumns = @JoinColumn(name = "gid"))
	private Set<AdminGroup> adminGroup = new HashSet<>();

	// 权限总和
	@Transient
	private long[] rightSum;
	// 设置为超级管理员
	@Transient
	private Boolean superAdmin = false;

	@Transient
	private Boolean auth;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
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

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public String getSignCode() {
		return signCode;
	}

	public void setSignCode(String signCode) {
		this.signCode = signCode;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Set<AdminGroup> getAdminGroup() {
		return adminGroup;
	}

	public void setAdminGroup(Set<AdminGroup> adminGroup) {
		this.adminGroup = adminGroup;
	}

	public long[] getRightSum() {
		return rightSum;
	}

	public void setRightSum(long[] rightSum) {
		this.rightSum = rightSum;
	}

	public Boolean getSuperAdmin() {
		return superAdmin;
	}

	public void setSuperAdmin(Boolean superAdmin) {
		this.superAdmin = superAdmin;
	}

	public Boolean getAuth() {
		return auth;
	}

	public void setAuth(Boolean auth) {
		this.auth = auth;
	}

	/**
	 * 计算用户权限总和
	 */
	public void calucateRightSum() {
		int pos = 0;
		long code = 0;
		for (AdminGroup g : adminGroup) {
			// 判断超级管理员
			if ("administrator".equals(g.getValue())) {
				this.superAdmin = true;
				adminGroup = null;
				return;
			}
			for (AdminRight r : g.getAdminRight()) {
				pos = r.getPos();
				code = r.getCode();
				rightSum[pos] = rightSum[pos] | code;
			}
		}
		// 释放起源在计算权限总和之后,adminGroup就是没用了
		adminGroup = null;
	}

	/**
	 * 判断用户是否具有指定权限
	 * 
	 * @param right
	 * @return
	 */
	public boolean hasRight(AdminRight right) {
		int pos = right.getPos();
		long code = right.getCode();

		return !((rightSum[pos] & code) == 0);
	}

}
