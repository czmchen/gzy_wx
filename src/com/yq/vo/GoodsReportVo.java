package com.yq.vo;

import com.yq.entity.GoodsReport;

public class GoodsReportVo extends GoodsReport implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String goodsCode;
	private String goodsName;
	private String goodsSpe;
	private String[] fileOrgName;
	private String[] filedeskName;
	private String[] pdf2jpgDataArray;

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsSpe() {
		return goodsSpe;
	}

	public void setGoodsSpe(String goodsSpe) {
		this.goodsSpe = goodsSpe;
	}

	public String[] getFileOrgName() {
		return fileOrgName;
	}

	public void setFileOrgName(String[] fileOrgName) {
		this.fileOrgName = fileOrgName;
	}

	public String[] getFiledeskName() {
		return filedeskName;
	}

	public void setFiledeskName(String[] filedeskName) {
		this.filedeskName = filedeskName;
	}

	public String[] getPdf2jpgDataArray() {
		return pdf2jpgDataArray;
	}

	public void setPdf2jpgDataArray(String[] pdf2jpgDataArray) {
		this.pdf2jpgDataArray = pdf2jpgDataArray;
	}

}
