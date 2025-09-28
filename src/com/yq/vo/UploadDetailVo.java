package com.yq.vo;

public class UploadDetailVo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String fileOrgName;
	private String filedeskName;
	private String pdf2jpgData;
	private String suffix;
	private String fileFloder;
	private String errorMsg;
	private long costTime;
	private boolean succFlag;

	public String getFileOrgName() {
		return fileOrgName;
	}

	public void setFileOrgName(String fileOrgName) {
		this.fileOrgName = fileOrgName;
	}

	public String getFiledeskName() {
		return filedeskName;
	}

	public void setFiledeskName(String filedeskName) {
		this.filedeskName = filedeskName;
	}

	public boolean isSuccFlag() {
		return succFlag;
	}

	public void setSuccFlag(boolean succFlag) {
		this.succFlag = succFlag;
	}

	public String getPdf2jpgData() {
		return pdf2jpgData;
	}

	public void setPdf2jpgData(String pdf2jpgData) {
		this.pdf2jpgData = pdf2jpgData;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public long getCostTime() {
		return costTime;
	}

	public void setCostTime(long costTime) {
		this.costTime = costTime;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getFileFloder() {
		return fileFloder;
	}

	public void setFileFloder(String fileFloder) {
		this.fileFloder = fileFloder;
	}

}
