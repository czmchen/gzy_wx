package com.yq.entity;

import java.io.Serializable;

public class ExpressCost implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String provinceName;
	private String provinceCode;
	private Float baseWeight;
	private Float continuationWeight;
	private String expressName;
	private String createDateTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public Float getBaseWeight() {
		return baseWeight;
	}

	public void setBaseWeight(Float baseWeight) {
		this.baseWeight = baseWeight;
	}

	public Float getContinuationWeight() {
		return continuationWeight;
	}

	public void setContinuationWeight(Float continuationWeight) {
		this.continuationWeight = continuationWeight;
	}

	public String getExpressName() {
		return expressName;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}

	public String getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(String createDateTime) {
		this.createDateTime = createDateTime;
	}

}
