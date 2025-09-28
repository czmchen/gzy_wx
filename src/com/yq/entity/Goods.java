package com.yq.entity;

import java.io.Serializable;

public class Goods extends Page implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer goods_id;
	String goods_name;
	String goods_code;
	String goods_img;
	float goods_price;
	String goods_detail;
	String add_time;
	Integer ctg_id;
	Integer status;
	Integer is_recommend;
	String goods_spe;
	float goods_weight;
	Integer type;
	Integer goods_num;
	Integer nbox;
	float wholesalePrice;
	Integer preference1;
	String bar_num;

	float def1 = 0.0f;
	float def2 = 0.0f;
	float def3 = 0.0f;
	float salesPrice1;
	int salesAmount1;

	public Integer getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getGoods_code() {
		return goods_code;
	}

	public void setGoods_code(String goods_code) {
		this.goods_code = goods_code;
	}

	public String getGoods_img() {
		return goods_img;
	}

	public void setGoods_img(String goods_img) {
		this.goods_img = goods_img;
	}

	public float getGoods_price() {
		return goods_price;
	}

	public void setGoods_price(float goods_price) {
		this.goods_price = goods_price;
	}

	public String getGoods_detail() {
		return goods_detail;
	}

	public void setGoods_detail(String goods_detail) {
		this.goods_detail = goods_detail;
	}

	public Integer getCtg_id() {
		return ctg_id;
	}

	public void setCtg_id(Integer ctg_id) {
		this.ctg_id = ctg_id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAdd_time() {
		return add_time;
	}

	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}

	public Integer getIs_recommend() {
		return is_recommend;
	}

	public void setIs_recommend(Integer is_recommend) {
		this.is_recommend = is_recommend;
	}

	public String getGoods_spe() {
		return goods_spe;
	}

	public void setGoods_spe(String goods_spe) {
		this.goods_spe = goods_spe;
	}

	public float getGoods_weight() {
		return goods_weight;
	}

	public void setGoods_weight(float goods_weight) {
		this.goods_weight = goods_weight;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getGoods_num() {
		return goods_num;
	}

	public void setGoods_num(Integer goods_num) {
		this.goods_num = goods_num;
	}

	public Integer getNbox() {
		return nbox;
	}

	public void setNbox(Integer nbox) {
		this.nbox = nbox;
	}

	public float getWholesalePrice() {
		return wholesalePrice;
	}

	public void setWholesalePrice(float wholesalePrice) {
		this.wholesalePrice = wholesalePrice;
	}

	public Integer getPreference1() {
		return preference1;
	}

	public void setPreference1(Integer preference1) {
		this.preference1 = preference1;
	}

	public float getDef1() {
		return def1;
	}

	public void setDef1(float def1) {
		this.def1 = def1;
	}

	public float getDef2() {
		return def2;
	}

	public void setDef2(float def2) {
		this.def2 = def2;
	}

	public float getDef3() {
		return def3;
	}

	public void setDef3(float def3) {
		this.def3 = def3;
	}

	public String getBar_num() {
		return bar_num;
	}

	public void setBar_num(String bar_num) {
		this.bar_num = bar_num;
	}

	public float getSalesPrice1() {
		return salesPrice1;
	}

	public void setSalesPrice1(float salesPrice1) {
		this.salesPrice1 = salesPrice1;
	}

	public int getSalesAmount1() {
		return salesAmount1;
	}

	public void setSalesAmount1(int salesAmount1) {
		this.salesAmount1 = salesAmount1;
	}

}
