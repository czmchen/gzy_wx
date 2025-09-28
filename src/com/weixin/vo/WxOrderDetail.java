package com.weixin.vo;

public class WxOrderDetail implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String fid;
	private String InvID;
	private String InvName;
	private Float Qty;
	private Float NQty;
	private Float Price;
	private Float Amount;
	private String Note;
	private Integer goodsQty;
	private Float orgPrice;
	private Float orgAmount;
	private Float preference1;

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getInvID() {
		return InvID;
	}

	public void setInvID(String invID) {
		InvID = invID;
	}

	public String getInvName() {
		return InvName;
	}

	public void setInvName(String invName) {
		InvName = invName;
	}

	public Float getQty() {
		return Qty;
	}

	public void setQty(Float qty) {
		Qty = qty;
	}

	public Float getNQty() {
		return NQty;
	}

	public void setNQty(Float nQty) {
		NQty = nQty;
	}

	public Float getPrice() {
		return Price;
	}

	public void setPrice(Float price) {
		Price = price;
	}

	public Float getAmount() {
		return Amount;
	}

	public void setAmount(Float amount) {
		Amount = amount;
	}

	public String getNote() {
		return Note;
	}

	public void setNote(String note) {
		Note = note;
	}

	public Integer getGoodsQty() {
		return goodsQty;
	}

	public void setGoodsQty(Integer goodsQty) {
		this.goodsQty = goodsQty;
	}

	public Float getOrgPrice() {
		return orgPrice;
	}

	public void setOrgPrice(Float orgPrice) {
		this.orgPrice = orgPrice;
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

}
