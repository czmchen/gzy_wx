package com.yq.entity;

import java.io.Serializable;

public class Address implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer addr_id;
	String addr_user;
	String addr_name;
	String addr_tel;
	String oppen_id;
	String sort;
	String province;
	String city;
	String area;
	String town;
	String cityPickerCN;
	int isCustomerRegist;

	public Integer getAddr_id() {
		return addr_id;
	}

	public void setAddr_id(Integer addr_id) {
		this.addr_id = addr_id;
	}

	public String getAddr_user() {
		return addr_user;
	}

	public void setAddr_user(String addr_user) {
		this.addr_user = addr_user;
	}

	public String getAddr_name() {
		return addr_name;
	}

	public void setAddr_name(String addr_name) {
		this.addr_name = addr_name;
	}

	public String getAddr_tel() {
		return addr_tel;
	}

	public void setAddr_tel(String addr_tel) {
		this.addr_tel = addr_tel;
	}

	public String getOppen_id() {
		return oppen_id;
	}

	public void setOppen_id(String oppen_id) {
		this.oppen_id = oppen_id;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCityPickerCN() {
		return cityPickerCN;
	}

	public void setCityPickerCN(String cityPickerCN) {
		this.cityPickerCN = cityPickerCN;
	}

	public int getIsCustomerRegist() {
		return isCustomerRegist;
	}

	public void setIsCustomerRegist(int isCustomerRegist) {
		this.isCustomerRegist = isCustomerRegist;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

}
