package com.yq.vo;

public class WXMsgListVo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String openId;

	private String head_img;

	private String wxName;

	private String lastMsgContext;

	private int isNewMsg;

	private String lastMsgDateTime;
	
	private String thumbMediaId;//add by chenzhoumin 2032112061120
	
	private String format;//add by chenzhoumin 2032112061120

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getHead_img() {
		return head_img;
	}

	public void setHead_img(String head_img) {
		this.head_img = head_img;
	}

	public String getWxName() {
		return wxName;
	}

	public void setWxName(String wxName) {
		this.wxName = wxName;
	}

	public String getLastMsgContext() {
		return lastMsgContext;
	}

	public void setLastMsgContext(String lastMsgContext) {
		this.lastMsgContext = lastMsgContext;
	}

	public int getIsNewMsg() {
		return isNewMsg;
	}

	public void setIsNewMsg(int isNewMsg) {
		this.isNewMsg = isNewMsg;
	}

	public String getLastMsgDateTime() {
		return lastMsgDateTime;
	}

	public void setLastMsgDateTime(String lastMsgDateTime) {
		this.lastMsgDateTime = lastMsgDateTime;
	}

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

}
