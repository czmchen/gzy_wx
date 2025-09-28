package com.weixin.service;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.test.ImageHelper;
import com.weixin.dao.WxMsgMapper;
import com.weixin.entity.Token;
import com.weixin.entity.WxMsg;
import com.weixin.util.C3P0DBManager;
import com.weixin.util.CommonUtil;
import com.weixin.util.DateUtils;
import com.weixin.util.HttpClientManager;
import com.weixin.util.PropertiesUtils;
import com.weixin.util.StringUtil;
import com.weixin.util.WXCustomerManagerUtil;
import com.yq.vo.WXMsgListVo;

@Service
public class WxMsgService {
	@Autowired
	private WxMsgMapper wxMsgMapper;
	Logger log = Logger.getLogger(WxMsgService.class);

	public int insert(Map<String, String> wxMsgMap) {
		WxMsg wxMsg = new WxMsg();

		// 正常的微信处理流程
		// 发送方帐号（open_id）也是回复消息的接收者
		String to = wxMsgMap.get("FromUserName");
		// 公众帐号 也是回复消息的发送者
		String from = wxMsgMap.get("ToUserName");
		// 消息类型
		String msgType = wxMsgMap.get("MsgType");
		// 消息ID
		String msgId = wxMsgMap.get("MsgId");
		// 消息ID
		String createTime = wxMsgMap.get("CreateTime");

		wxMsg.setToUserName(to);
		wxMsg.setFromUserName(from);
		wxMsg.setMessageType(msgType);
		wxMsg.setMsgId(msgId);
		wxMsg.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.parseLong(createTime) * 1000L));

		if (msgType.equals("text")) {// 用户发送的文本信息
			wxMsg.setContent(contentEmoji2Img(wxMsgMap.get("Content")));
			wxMsg.setContentBak(wxMsgMap.get("Content"));
		} else if (msgType.equals("image")) {
			wxMsg.setPicUrl(wxMsgMap.get("PicUrl"));
			wxMsg.setMediaId(wxMsgMap.get("MediaId"));
			wxMsg.setContent("[图片]");
			downloadImg(wxMsg);
		} else if (msgType.equals("link")) {
			wxMsg.setContent("[链接]");
		} else if (msgType.equals("voice")) {
			wxMsg.setFormat(wxMsgMap.get("Format"));
			wxMsg.setMediaId(wxMsgMap.get("MediaId"));
			wxMsg.setContent("[语音]");
		} else if (msgType.equals("video")) {
			wxMsg.setThumbMediaId(wxMsgMap.get("ThumbMediaId"));
			wxMsg.setMediaId(wxMsgMap.get("MediaId"));
			wxMsg.setContent("[视频]");
			downloadVideo(wxMsg);
		} else if (msgType.equals("location")) {
			String location_x = wxMsgMap.get("Location_X");
			String location_y = wxMsgMap.get("Location_Y");
			String label = wxMsgMap.get("Label");
			String content = "您的当前位置：X(" + location_x + "),Y(" + location_y + ")" + label;
			wxMsg.setLocation_X(location_x);
			wxMsg.setLocation_Y(location_y);
			wxMsg.setContent(content);
			wxMsg.setLabel(label);
		} else if (msgType.equals("file")) {
			wxMsg.setFileMd5(wxMsgMap.get("FileMd5"));
			wxMsg.setFileKey(wxMsgMap.get("FileKey"));
			wxMsg.setFileTotalLen(wxMsgMap.get("FileTotalLen"));
			wxMsg.setTitle(wxMsgMap.get("Title"));
			wxMsg.setContent("[文件]");
		} else {
			String content = "暂时无相应服务。。。";
			wxMsg.setContent(content);
		}
		updateUserMsgNewNote(wxMsg.getToUserName());
		return wxMsgMapper.insert(wxMsg);
	}
	
	private String contentEmoji2Img(String strContent) {
		Map<String, String> wxEmoji2Img = com.weixin.constants.CommonConstants.WX_EMOJI2IMG_DATA;
		for(String key : wxEmoji2Img.keySet()) {
			strContent = strContent.replaceAll("\\"+key, Matcher.quoteReplacement(wxEmoji2Img.get(key)));
		}
		return strContent;
	}
	
	private int downloadImg(WxMsg wxMsg) {
		String imgFullName = wxMsg.getMediaId()+".jpg";//原图全图
		String abbrImgFullName = wxMsg.getMediaId()+"_abbr.jpg";//缩略图
		int isDownload = -1;
		String downloadResult = "成功!";
		try {
			HttpClientManager.downloadImg(wxMsg.getPicUrl(), imgFullName);//按照原图下载
			ImageHelper.processImg(imgFullName, abbrImgFullName, 300);
			isDownload = 1;
		} catch (Exception e) {
			downloadResult = "失败!"+e.getMessage();
			log.error(e);
		}finally {
			wxMsg.setIsDownload(isDownload);
			wxMsg.setDownloadResult(DateUtils.nowDate(DateUtils.DATETIME_FORMATE)+" 下载结果："+downloadResult);
		}
		return isDownload;
	}
	

	@SuppressWarnings("static-access")
	private void downloadVideo(WxMsg objWxMsg) {
		String mediaId = objWxMsg.getMediaId();
		String result = "";
		int status = 0;

		try {
			StringUtil st = new StringUtil();
			Token token = new CommonUtil().getToken(st.getSetting().getAppid(), st.getSetting().getAppsecret());
			String videoFileFullLocation = PropertiesUtils.read("customer.file.location") + "/video/" + mediaId + ".mp4";
			String videoImgFileFullLocation = PropertiesUtils.read("customer.file.location") + "/video/" + objWxMsg.getThumbMediaId() + ".jpg";
			if (!(new File(videoFileFullLocation).exists())) {//先下载video文件
				WXCustomerManagerUtil.downloadVideo(token.getAccessToken(), mediaId);
			}if (!(new File(videoImgFileFullLocation).exists())) {//下载video的图片
				WXCustomerManagerUtil.downloadVideoThumbMediaImg(token.getAccessToken(), objWxMsg.getThumbMediaId());
			}
			result = DateUtils.nowDate(DateUtils.DATETIME_FORMATE) + "  下载成功！" + "\r\n";
			status = 1;
			objWxMsg.setDownloadResult(result);
			objWxMsg.setIsDownload(status);
		} catch (Exception e) {
			log.error(e);
			objWxMsg.setDownloadResult(result);
		}
	}
	
	private void updateUserMsgNewNote(String openId) {
		Connection conn = null;
		Statement st = null;
		try {
			conn = C3P0DBManager.getConnection();
			st = conn.createStatement();
			st.executeUpdate("update WxUserCust set isMsgNew = 1 where openid = '" + openId + "'");
		} catch (SQLException e) {
			log.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, st, null);
		}
	}

	public int insert(WxMsg wxMsg) {
		return wxMsgMapper.insert(wxMsg);
	}
	
	public int insertNewMsg(String openId) {
		return wxMsgMapper.insertNewMsg(openId);
	}

	public List<WxMsg> getAllMsg(String openId,Integer isMsgNew) {
		return wxMsgMapper.getAllMsg(openId,isMsgNew);
	}
	
	public List<WXMsgListVo> getNewMsgList(String wxName,int recordAmount) {
		return wxMsgMapper.getNewMsgList(wxName,recordAmount);
	}

	public List<WxMsg> search(String mediaId) {
		return wxMsgMapper.search(mediaId);
	}

	public void updateNewMsg2Read(String openId) {
		wxMsgMapper.updateNewMsg2Read(openId);
	}
	
	public void updateNewMsg2ReadByIds(String openId,List<Long> ids) {
		wxMsgMapper.updateNewMsg2ReadByIds(openId,ids);
	}

	public List<WxMsg> getNeed2Download() {
		return wxMsgMapper.getNeed2Download();
	}

	public int updateDownloadResult(WxMsg wxMsg) {
		return wxMsgMapper.updateDownloadResult(wxMsg);
	}
	
	public int updatePicUrlByMediaId(String mediaId,String picUrl) {
		return wxMsgMapper.updatePicUrlByMediaId(mediaId,picUrl);
	}
}
