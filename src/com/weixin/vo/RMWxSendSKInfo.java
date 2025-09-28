package com.weixin.vo;

public class RMWxSendSKInfo implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String custID;
	private String custMC;
	private String custWXnum;
	private String payway;
	private String orderNo;
	private String orderDate;
	private Double ordernum;
	private Double orderJE;
	private String orderWLDH;
	private String orderInfo;
	private String wxOrderNO;
	private String creater;
	private String creatdate;
	private String wxreaddate;
	private String wxsenddate;
	private int IsRead;
	private int IsSend;
	private String bz;
	private String iszf;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustID() {
		return custID;
	}

	public void setCustID(String custID) {
		this.custID = custID;
	}

	public String getCustMC() {
		return custMC;
	}

	public void setCustMC(String custMC) {
		this.custMC = custMC;
	}

	public String getCustWXnum() {
		return custWXnum;
	}

	public void setCustWXnum(String custWXnum) {
		this.custWXnum = custWXnum;
	}

	public String getPayway() {
		return payway;
	}

	public void setPayway(String payway) {
		this.payway = payway;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public Double getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(Double ordernum) {
		this.ordernum = ordernum;
	}

	public Double getOrderJE() {
		return orderJE;
	}

	public void setOrderJE(Double orderJE) {
		this.orderJE = orderJE;
	}

	public String getOrderWLDH() {
		return orderWLDH;
	}

	public void setOrderWLDH(String orderWLDH) {
		this.orderWLDH = orderWLDH;
	}

	public String getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(String orderInfo) {
		this.orderInfo = orderInfo;
	}

	public String getWxOrderNO() {
		return wxOrderNO;
	}

	public void setWxOrderNO(String wxOrderNO) {
		this.wxOrderNO = wxOrderNO;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getCreatdate() {
		return creatdate;
	}

	public void setCreatdate(String creatdate) {
		this.creatdate = creatdate;
	}

	public String getWxreaddate() {
		return wxreaddate;
	}

	public void setWxreaddate(String wxreaddate) {
		this.wxreaddate = wxreaddate;
	}

	public String getWxsenddate() {
		return wxsenddate;
	}

	public void setWxsenddate(String wxsenddate) {
		this.wxsenddate = wxsenddate;
	}

	public int getIsRead() {
		return IsRead;
	}

	public void setIsRead(int isRead) {
		IsRead = isRead;
	}

	public int getIsSend() {
		return IsSend;
	}

	public void setIsSend(int isSend) {
		IsSend = isSend;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getIszf() {
		return iszf;
	}

	public void setIszf(String iszf) {
		this.iszf = iszf;
	}
}
