package com.sniper.springmvc.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mc_system_config")
public class SystemConfig extends BaseEntity {

	private static final long serialVersionUID = -3380558646358201255L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String keyName;
	private String keyValue;
	private String keyInfo;
	private Boolean autoload = true;
	private String input;
	private String placeholder;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public String getKeyInfo() {
		return keyInfo;
	}

	public void setKeyInfo(String keyInfo) {
		this.keyInfo = keyInfo;
	}

	public Boolean getAutoload() {
		return autoload;
	}

	public void setAutoload(Boolean autoload) {
		this.autoload = autoload;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}
	
	public SystemConfig() {
		super();
	}

	public SystemConfig(String keyName, String keyValue) {
		super();
		this.keyName = keyName;
		this.keyValue = keyValue;
	}

}
