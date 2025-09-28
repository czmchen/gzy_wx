package com.yq.entity;

public class GoodsReport implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String barNum;
	private String lotNum;
	private String batchNum;
	private String inspReport;
	private String reportDeskFile;
	private String pdf2jpgData;
	private String modifyTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBarNum() {
		return barNum;
	}

	public void setBarNum(String barNum) {
		this.barNum = barNum;
	}

	public String getLotNum() {
		return lotNum;
	}

	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}

	public String getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}

	public String getInspReport() {
		return inspReport;
	}

	public void setInspReport(String inspReport) {
		this.inspReport = inspReport;
	}

	public String getReportDeskFile() {
		return reportDeskFile;
	}

	public void setReportDeskFile(String reportDeskFile) {
		this.reportDeskFile = reportDeskFile;
	}

	public String getPdf2jpgData() {
		return pdf2jpgData;
	}

	public void setPdf2jpgData(String pdf2jpgData) {
		this.pdf2jpgData = pdf2jpgData;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

}
