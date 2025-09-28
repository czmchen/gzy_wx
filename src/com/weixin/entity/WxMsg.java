package com.weixin.entity;

public class WxMsg implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String msgId;
	private String messageType;
	private String fromUserName;
	private String toUserName;
	private String createTime;
	private String content;
	private String picUrl;
	private String mediaId;
	private String format;
	private String fileMd5;
	private String fileTotalLen;
	private String fileKey;
	private String description;
	private String title;
	private String location_X;
	private String location_Y;
	private String scale;
	private String label;
	private String thumbMediaId;
	private int isMsgNew;
	private String fileFullLocation;
	private int msgSendOrReceive;
	private String head_img;
	private String sendResult;
	private String fromHead_img;
	private String fromName;
	private int isDownload;
	private String downloadResult;
	private String contentBak;// 202105251714 add

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getFileMd5() {
		return fileMd5;
	}

	public void setFileMd5(String fileMd5) {
		this.fileMd5 = fileMd5;
	}

	public String getFileTotalLen() {
		return fileTotalLen;
	}

	public void setFileTotalLen(String fileTotalLen) {
		this.fileTotalLen = fileTotalLen;
	}

	public String getFileKey() {
		return fileKey;
	}

	public void setFileKey(String fileKey) {
		this.fileKey = fileKey;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation_X() {
		return location_X;
	}

	public void setLocation_X(String location_X) {
		this.location_X = location_X;
	}

	public String getLocation_Y() {
		return location_Y;
	}

	public void setLocation_Y(String location_Y) {
		this.location_Y = location_Y;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	public int getIsMsgNew() {
		return isMsgNew;
	}

	public void setIsMsgNew(int isMsgNew) {
		this.isMsgNew = isMsgNew;
	}

	public String getFileFullLocation() {
		return fileFullLocation;
	}

	public void setFileFullLocation(String fileFullLocation) {
		this.fileFullLocation = fileFullLocation;
	}

	public int getMsgSendOrReceive() {
		return msgSendOrReceive;
	}

	public void setMsgSendOrReceive(int msgSendOrReceive) {
		this.msgSendOrReceive = msgSendOrReceive;
	}

	public String getHead_img() {
		return head_img;
	}

	public void setHead_img(String head_img) {
		this.head_img = head_img;
	}

	public String getSendResult() {
		return sendResult;
	}

	public void setSendResult(String sendResult) {
		this.sendResult = sendResult;
	}

	public String getFromHead_img() {
		return fromHead_img;
	}

	public void setFromHead_img(String fromHead_img) {
		this.fromHead_img = fromHead_img;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public int getIsDownload() {
		return isDownload;
	}

	public void setIsDownload(int isDownload) {
		this.isDownload = isDownload;
	}

	public String getDownloadResult() {
		return downloadResult;
	}

	public void setDownloadResult(String downloadResult) {
		this.downloadResult = downloadResult;
	}

	public String getContentBak() {
		return contentBak;
	}

	public void setContentBak(String contentBak) {
		this.contentBak = contentBak;
	}

}