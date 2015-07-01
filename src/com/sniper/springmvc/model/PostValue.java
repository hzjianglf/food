package com.sniper.springmvc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;

@Entity
@Table(name = "mc_post_value")
public class PostValue extends BaseEntity {

	private static final long serialVersionUID = 8437710606525667425L;
	@Id
	// @GenericGenerator(name = "generator", strategy = "native", parameters = {
	// @Parameter(name = "property", value = "post") })
	// @GenericGenerator(name = "generator", strategy = GenerationType.AUTO)
	// @GeneratedValue(generator = "generator")
	@GeneratedValue(strategy = GenerationType.AUTO)
	// 不能使用子曾
	private Integer id;
	@Field
	@Column(columnDefinition = "longtext")
	private String value;

	public PostValue() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
