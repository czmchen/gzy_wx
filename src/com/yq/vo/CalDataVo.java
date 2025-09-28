package com.yq.vo;

public class CalDataVo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] id;
	private String[] goodsName;
	private String[] goodsImg;
	private String[] goodsCode;
	private Integer[] preference1;
	private Double[] price;
	private Double[] nbox;
	private Double[] goodsNbox;
	private Double[] goodsNum;
	private Double[] goodsWeight;
	private Double goodsNboxAmount;
	private Double goodsAmount;
	private Double calTotalAmount;
	private Double preference1TotalAmount;
	private Double calTotalPreferenceAmount;
	private String salesOne;

	public String[] getId() {
		return id;
	}

	public void setId(String[] id) {
		this.id = id;
	}

	public String[] getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String[] goodsName) {
		this.goodsName = goodsName;
	}

	public String[] getGoodsImg() {
		return goodsImg;
	}

	public void setGoodsImg(String[] goodsImg) {
		this.goodsImg = goodsImg;
	}

	public Double[] getPrice() {
		return price;
	}

	public void setPrice(Double[] price) {
		this.price = price;
	}

	public Double[] getGoodsNbox() {
		return goodsNbox;
	}

	public void setGoodsNbox(Double[] goodsNbox) {
		this.goodsNbox = goodsNbox;
	}

	public Double[] getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(Double[] goodsNum) {
		this.goodsNum = goodsNum;
	}

	public Double getGoodsAmount() {
		return goodsAmount;
	}

	public void setGoodsAmount(Double goodsAmount) {
		this.goodsAmount = goodsAmount;
	}

	public Double getCalTotalAmount() {
		return calTotalAmount;
	}

	public void setCalTotalAmount(Double calTotalAmount) {
		this.calTotalAmount = calTotalAmount;
	}

	public String[] getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String[] goodsCode) {
		this.goodsCode = goodsCode;
	}

	public Double[] getNbox() {
		return nbox;
	}

	public void setNbox(Double[] nbox) {
		this.nbox = nbox;
	}

	public Double getGoodsNboxAmount() {
		return goodsNboxAmount;
	}

	public void setGoodsNboxAmount(Double goodsNboxAmount) {
		this.goodsNboxAmount = goodsNboxAmount;
	}

	public Double getPreference1TotalAmount() {
		return preference1TotalAmount;
	}

	public void setPreference1TotalAmount(Double preference1TotalAmount) {
		this.preference1TotalAmount = preference1TotalAmount;
	}

	public Integer[] getPreference1() {
		return preference1;
	}

	public void setPreference1(Integer[] preference1) {
		this.preference1 = preference1;
	}

	public Double getCalTotalPreferenceAmount() {
		return calTotalPreferenceAmount;
	}

	public void setCalTotalPreferenceAmount(Double calTotalPreferenceAmount) {
		this.calTotalPreferenceAmount = calTotalPreferenceAmount;
	}

	public Double[] getGoodsWeight() {
		return goodsWeight;
	}

	public void setGoodsWeight(Double[] goodsWeight) {
		this.goodsWeight = goodsWeight;
	}

	public String getSalesOne() {
		return salesOne;
	}

	public void setSalesOne(String salesOne) {
		this.salesOne = salesOne;
	}

}
