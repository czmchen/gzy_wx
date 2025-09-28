package com.weixin.vo;

public class WxOrder implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String wxOrderNO;
	private String OrderNO;
	private String CustID;
	private String CustMC;
	private String CustWXnum;
	private String creater;
	private String creatdate;
	private String sysreaddate;
	private String wxsenddate;
	private Integer IsRead;
	private Integer IsSend;
	private String invID;
	private String invName;
	private Float nQty;
	private Float Qty;
	private Float Amount;
	private Integer IsVerified;
	private String ordAddress;
	private String VerifyDate;
	private String Verifyer;
	private Float Price;
	private String Note;
	private String receive;
	private Integer receiveType;
	private String fid;
	private Integer Iszf;
	private String zfdate;
	private Float zfamount;
	private String YJFHRQ;
	private String VerifyNote;
	private Integer isSys;
	private Integer orderStatus;
	private String OrderWLDH;
	private Float receiveCost;
	private String zfTransactionId;
	private String sendDataText;
	private Float orgAmount;
	private Float preference1;
	private Integer IsPD;
	private Integer IsYLZF;
	private String ordphone;
	private String ordRecipient;
	private Integer addrId;
	private Float expressCost;
	private Integer signFlag;
	private String signDatetime;
	private String signSendMsg;
	private Integer signSendFlag;
	private String latitude;
	private String longitude;

	public String getWxOrderNO() {
		return wxOrderNO;
	}

	public void setWxOrderNO(String wxOrderNO) {
		this.wxOrderNO = wxOrderNO;
	}

	public String getOrderNO() {
		return OrderNO;
	}

	public void setOrderNO(String orderNO) {
		OrderNO = orderNO;
	}

	public String getCustID() {
		return CustID;
	}

	public void setCustID(String custID) {
		CustID = custID;
	}

	public String getCustMC() {
		return CustMC;
	}

	public void setCustMC(String custMC) {
		CustMC = custMC;
	}

	public String getCustWXnum() {
		return CustWXnum;
	}

	public void setCustWXnum(String custWXnum) {
		CustWXnum = custWXnum;
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

	public String getSysreaddate() {
		return sysreaddate;
	}

	public void setSysreaddate(String sysreaddate) {
		this.sysreaddate = sysreaddate;
	}

	public String getWxsenddate() {
		return wxsenddate;
	}

	public void setWxsenddate(String wxsenddate) {
		this.wxsenddate = wxsenddate;
	}

	public Integer getIsRead() {
		return IsRead;
	}

	public void setIsRead(Integer isRead) {
		IsRead = isRead;
	}

	public Integer getIsSend() {
		return IsSend;
	}

	public void setIsSend(Integer isSend) {
		IsSend = isSend;
	}

	public String getInvID() {
		return invID;
	}

	public void setInvID(String invID) {
		this.invID = invID;
	}

	public String getInvName() {
		return invName;
	}

	public void setInvName(String invName) {
		this.invName = invName;
	}

	public Float getnQty() {
		return nQty;
	}

	public void setnQty(Float nQty) {
		this.nQty = nQty;
	}

	public Float getQty() {
		return Qty;
	}

	public void setQty(Float qty) {
		Qty = qty;
	}

	public Float getAmount() {
		return Amount;
	}

	public void setAmount(Float amount) {
		Amount = amount;
	}

	public Integer getIsVerified() {
		return IsVerified;
	}

	public void setIsVerified(Integer isVerified) {
		IsVerified = isVerified;
	}

	public String getOrdAddress() {
		return ordAddress;
	}

	public void setOrdAddress(String ordAddress) {
		this.ordAddress = ordAddress;
	}

	public String getVerifyDate() {
		return VerifyDate;
	}

	public void setVerifyDate(String verifyDate) {
		VerifyDate = verifyDate;
	}

	public String getVerifyer() {
		return Verifyer;
	}

	public void setVerifyer(String verifyer) {
		Verifyer = verifyer;
	}

	public Float getPrice() {
		return Price;
	}

	public void setPrice(Float price) {
		Price = price;
	}

	public String getNote() {
		return Note;
	}

	public void setNote(String note) {
		Note = note;
	}

	public String getReceive() {
		return receive;
	}

	public void setReceive(String receive) {
		this.receive = receive;
	}

	public Integer getReceiveType() {
		return receiveType;
	}

	public void setReceiveType(Integer receiveType) {
		this.receiveType = receiveType;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public Integer getIszf() {
		return Iszf;
	}

	public void setIszf(Integer iszf) {
		Iszf = iszf;
	}

	public String getZfdate() {
		return zfdate;
	}

	public void setZfdate(String zfdate) {
		this.zfdate = zfdate;
	}

	public Float getZfamount() {
		return zfamount;
	}

	public void setZfamount(Float zfamount) {
		this.zfamount = zfamount;
	}

	public String getYJFHRQ() {
		return YJFHRQ;
	}

	public void setYJFHRQ(String yJFHRQ) {
		YJFHRQ = yJFHRQ;
	}

	public String getVerifyNote() {
		return VerifyNote;
	}

	public void setVerifyNote(String verifyNote) {
		VerifyNote = verifyNote;
	}

	public Integer getIsSys() {
		return isSys;
	}

	public void setIsSys(Integer isSys) {
		this.isSys = isSys;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderWLDH() {
		return OrderWLDH;
	}

	public void setOrderWLDH(String orderWLDH) {
		OrderWLDH = orderWLDH;
	}

	public Float getReceiveCost() {
		return receiveCost;
	}

	public void setReceiveCost(Float receiveCost) {
		this.receiveCost = receiveCost;
	}

	public String getZfTransactionId() {
		return zfTransactionId;
	}

	public void setZfTransactionId(String zfTransactionId) {
		this.zfTransactionId = zfTransactionId;
	}

	public String getSendDataText() {
		return sendDataText;
	}

	public void setSendDataText(String sendDataText) {
		this.sendDataText = sendDataText;
	}

	public Float getOrgAmount() {
		return orgAmount;
	}

	public void setOrgAmount(Float orgAmount) {
		this.orgAmount = orgAmount;
	}

	public Float getPreference1() {
		return preference1;
	}

	public void setPreference1(Float preference1) {
		this.preference1 = preference1;
	}

	public Integer getIsPD() {
		return IsPD;
	}

	public void setIsPD(Integer isPD) {
		IsPD = isPD;
	}

	public Integer getIsYLZF() {
		return IsYLZF;
	}

	public void setIsYLZF(Integer isYLZF) {
		IsYLZF = isYLZF;
	}

	public String getOrdphone() {
		return ordphone;
	}

	public void setOrdphone(String ordphone) {
		this.ordphone = ordphone;
	}

	public String getOrdRecipient() {
		return ordRecipient;
	}

	public void setOrdRecipient(String ordRecipient) {
		this.ordRecipient = ordRecipient;
	}

	public Integer getAddrId() {
		return addrId;
	}

	public void setAddrId(Integer addrId) {
		this.addrId = addrId;
	}

	public Float getExpressCost() {
		return expressCost;
	}

	public void setExpressCost(Float expressCost) {
		this.expressCost = expressCost;
	}

	public Integer getSignFlag() {
		return signFlag;
	}

	public void setSignFlag(Integer signFlag) {
		this.signFlag = signFlag;
	}

	public String getSignDatetime() {
		return signDatetime;
	}

	public void setSignDatetime(String signDatetime) {
		this.signDatetime = signDatetime;
	}

	public String getSignSendMsg() {
		return signSendMsg;
	}

	public void setSignSendMsg(String signSendMsg) {
		this.signSendMsg = signSendMsg;
	}

	public Integer getSignSendFlag() {
		return signSendFlag;
	}

	public void setSignSendFlag(Integer signSendFlag) {
		this.signSendFlag = signSendFlag;
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

}
