package com.yq.entity;

import java.io.Serializable;

public class Freight implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer fgt_id;
	Float fgt_price;
	Float free_price;
	public Integer getFgt_id() {
		return fgt_id;
	}
	public void setFgt_id(Integer fgt_id) {
		this.fgt_id = fgt_id;
	}
	public Float getFgt_price() {
		return fgt_price;
	}
	public void setFgt_price(Float fgt_price) {
		this.fgt_price = fgt_price;
	}
	public Float getFree_price() {
		return free_price;
	}
	public void setFree_price(Float free_price) {
		this.free_price = free_price;
	}
	
}
