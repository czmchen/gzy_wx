package com.yq.entity;

import java.io.Serializable;

public class User extends Page implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int user_id;// int(11) NOT NULL AUTO_INCREMENT,
	String oppen_id;// varchar(255) NOT NULL,
	String username;// varchar(255) DEFAULT NULL COMMENT '账号手机号',
	String realname;// varchar(255) DEFAULT NULL COMMENT '昵称',
	String password;// varchar(255) DEFAULT NULL,
	String head_img;// varchar(255) DEFAULT NULL,
	Integer area_id;
	String area_name;
	String add_time;
	String member_time;
	int status;
	String mphone;
	String custRemark;
	String custid;
	String custKHWXH;
	String custName;
	private String KHLXR;
	private String KHMC;
	private String crm_addr_name;
	private String crm_area;
	private String crm_city;
	private String crm_town;
	private String crm_province;
	private String cityPickerCN;
	private int isCustomer;
	private int isSalesMan;
	private int goodsReportMg;
	private int isWXFocus;
	private Integer sex;
	private String city;
	private String province;
	private String country;
	private String latitude;
	private String longitude;
	private String lastUpdateTime;
	private String salesman;
	private int custStatus;
	private int salesPriceTimes;
	private String gaodeId;
	private Long merchantsId;

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getOppen_id() {
		return oppen_id;
	}

	public void setOppen_id(String oppen_id) {
		this.oppen_id = oppen_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHead_img() {
		return head_img;
	}

	public void setHead_img(String head_img) {
		this.head_img = head_img;
	}

	public Integer getArea_id() {
		return area_id;
	}

	public void setArea_id(Integer area_id) {
		this.area_id = area_id;
	}

	public String getArea_name() {
		return area_name;
	}

	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}

	public String getAdd_time() {
		return add_time;
	}

	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMember_time() {
		return member_time;
	}

	public void setMember_time(String member_time) {
		this.member_time = member_time;
	}

	public String getMphone() {
		return mphone;
	}

	public void setMphone(String mphone) {
		this.mphone = mphone;
	}

	public String getCustRemark() {
		return custRemark;
	}

	public void setCustRemark(String custRemark) {
		this.custRemark = custRemark;
	}

	public String getCustid() {
		return custid;
	}

	public void setCustid(String custid) {
		this.custid = custid;
	}

	public String getCustKHWXH() {
		return custKHWXH;
	}

	public void setCustKHWXH(String custKHWXH) {
		this.custKHWXH = custKHWXH;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getKHLXR() {
		return KHLXR;
	}

	public void setKHLXR(String kHLXR) {
		KHLXR = kHLXR;
	}

	public String getKHMC() {
		return KHMC;
	}

	public void setKHMC(String kHMC) {
		KHMC = kHMC;
	}

	public String getCrm_addr_name() {
		return crm_addr_name;
	}

	public void setCrm_addr_name(String crm_addr_name) {
		this.crm_addr_name = crm_addr_name;
	}

	public String getCrm_area() {
		return crm_area;
	}

	public void setCrm_area(String crm_area) {
		this.crm_area = crm_area;
	}

	public String getCrm_city() {
		return crm_city;
	}

	public void setCrm_city(String crm_city) {
		this.crm_city = crm_city;
	}

	public String getCrm_province() {
		return crm_province;
	}

	public void setCrm_province(String crm_province) {
		this.crm_province = crm_province;
	}

	public String getCityPickerCN() {
		return cityPickerCN;
	}

	public void setCityPickerCN(String cityPickerCN) {
		this.cityPickerCN = cityPickerCN;
	}

	public String getCrm_town() {
		return crm_town;
	}

	public void setCrm_town(String crm_town) {
		this.crm_town = crm_town;
	}

	public int getIsCustomer() {
		return isCustomer;
	}

	public void setIsCustomer(int isCustomer) {
		this.isCustomer = isCustomer;
	}

	public int getGoodsReportMg() {
		return goodsReportMg;
	}

	public void setGoodsReportMg(int goodsReportMg) {
		this.goodsReportMg = goodsReportMg;
	}

	public int getIsWXFocus() {
		return isWXFocus;
	}

	public void setIsWXFocus(int isWXFocus) {
		this.isWXFocus = isWXFocus;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getSalesman() {
		return salesman;
	}

	public void setSalesman(String salesman) {
		this.salesman = salesman;
	}

	public int getCustStatus() {
		return custStatus;
	}

	public void setCustStatus(int custStatus) {
		this.custStatus = custStatus;
	}

	public int getSalesPriceTimes() {
		return salesPriceTimes;
	}

	public void setSalesPriceTimes(int salesPriceTimes) {
		this.salesPriceTimes = salesPriceTimes;
	}

	public int getIsSalesMan() {
		return isSalesMan;
	}

	public void setIsSalesMan(int isSalesMan) {
		this.isSalesMan = isSalesMan;
	}

	public String getGaodeId() {
		return gaodeId;
	}

	public void setGaodeId(String gaodeId) {
		this.gaodeId = gaodeId;
	}

	public Long getMerchantsId() {
		return merchantsId;
	}

	public void setMerchantsId(Long merchantsId) {
		this.merchantsId = merchantsId;
	}

}
