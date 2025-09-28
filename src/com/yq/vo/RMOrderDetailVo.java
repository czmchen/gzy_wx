package com.yq.vo;

public class RMOrderDetailVo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String fid;
	private String invID;
	private String invName;
	private String price;
	private String Qty;
	private String nQty;
	private String amount;
	private String goodsQty;

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getQty() {
		return Qty;
	}

	public void setQty(String qty) {
		Qty = qty;
	}

	public String getnQty() {
		return nQty;
	}

	public void setnQty(String nQty) {
		this.nQty = nQty;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getGoodsQty() {
		return goodsQty;
	}

	public void setGoodsQty(String goodsQty) {
		this.goodsQty = goodsQty;
	}

}
