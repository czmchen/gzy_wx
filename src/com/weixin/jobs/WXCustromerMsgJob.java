package com.weixin.jobs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.weixin.entity.Token;
import com.weixin.entity.WxMsg;
import com.weixin.service.WxMsgService;
import com.weixin.util.ChangeAudioFormat;
import com.weixin.util.CommonUtil;
import com.weixin.util.DateUtils;
import com.weixin.util.PropertiesUtils;
import com.weixin.util.SpringUtil;
import com.weixin.util.StringUtil;
import com.weixin.util.WXCustomerManagerUtil;
@DisallowConcurrentExecution
public class WXCustromerMsgJob implements Job {
	private final static Logger logger = Logger.getLogger(WXCustromerMsgJob.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			List<WxMsg> lstWxMsg = ((WxMsgService) SpringUtil.getBean("wxMsgService")).getNeed2Download();
			if (lstWxMsg.size() > 0) {
				List<WxMsg> lstResultWxMsg = downloadMsg2Disk(lstWxMsg);// 先下载消息文件到磁盘
				updateResultWxMsg(lstResultWxMsg); // 更新结果
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	private void updateResultWxMsg(List<WxMsg> lstResultWxMsg) {
		for (WxMsg wxMsg : lstResultWxMsg) {
			try {
				((WxMsgService) SpringUtil.getBean("wxMsgService")).updateDownloadResult(wxMsg);
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	private List<WxMsg> downloadMsg2Disk(List<WxMsg> lstWxMsg) {
		List<WxMsg> lstResultWxMsg = new ArrayList<WxMsg>();

		for (WxMsg objWxMsg : lstWxMsg) {
			if ("voice".equals(objWxMsg.getMessageType())) {
				this.downloadVoice(lstResultWxMsg, objWxMsg);// 开始下载语音
			}
			if ("video".equals(objWxMsg.getMessageType())) {
				this.downloadVideo(lstResultWxMsg, objWxMsg);// 开始下载流媒体
			}

		}
		return lstResultWxMsg;
	}

	@SuppressWarnings("static-access")
	private void downloadVoice(final List<WxMsg> lstResultWxMsg, final WxMsg objWxMsg) {
		WxMsg resultWxMsg = new WxMsg();
		lstResultWxMsg.add(resultWxMsg);
		String mediaId = objWxMsg.getMediaId();
		String result = "";
		int status = 0;
		resultWxMsg.setMediaId(mediaId);

		try {
			String fileFullLocation = PropertiesUtils.read("customer.file.location") + "/voice/";
			String armVoiceFileFullName = mediaId + ".amr";
			String amrVoiceFullLocation = (fileFullLocation + armVoiceFileFullName);
			String mp3VoiceFullLocation = (fileFullLocation + mediaId + ".mp3");
			if (!(new File(amrVoiceFullLocation).exists())) {
				StringUtil st = new StringUtil();
				Token token = new CommonUtil().getToken(st.getSetting().getAppid(), st.getSetting().getAppsecret());
				WXCustomerManagerUtil.handleVoice(token.getAccessToken(), mediaId, armVoiceFileFullName,
						fileFullLocation);
			}
			if (new File(amrVoiceFullLocation).length() <= 68) {
				result = DateUtils.nowDate(DateUtils.DATETIME_FORMATE) + "  状态码:10001,语音已超过3天未下载，已超时！" + "\r\n";
				status = -1;
				resultWxMsg.setDownloadResult(result);
				resultWxMsg.setIsDownload(status);
				return;
			}
			File file = new File(mp3VoiceFullLocation);
			if (!file.exists()) {
				try {
					ChangeAudioFormat.changeToMp3(amrVoiceFullLocation, mp3VoiceFullLocation);
				} catch (Exception e) {
					result = DateUtils.nowDate(DateUtils.DATETIME_FORMATE) + "  状态码:10002,语音转换失败，请重试！" + e.getMessage()
							+ "\r\n";
					status = -1;
					resultWxMsg.setDownloadResult(result);
					resultWxMsg.setIsDownload(status);
					return;
				}
			}
			result = DateUtils.nowDate(DateUtils.DATETIME_FORMATE) + "  下载成功！" + "\r\n";
			status = 1;
			resultWxMsg.setDownloadResult(result);
			resultWxMsg.setIsDownload(status);
		} catch (Exception e) {
			logger.error(e);
			status = -1;
			resultWxMsg.setDownloadResult(result);
			resultWxMsg.setIsDownload(status);
		}
	}

	@SuppressWarnings("static-access")
	private void downloadVideo(final List<WxMsg> lstResultWxMsg, final WxMsg objWxMsg) {
		WxMsg resultWxMsg = new WxMsg();
		lstResultWxMsg.add(resultWxMsg);
		String mediaId = objWxMsg.getMediaId();
		String result = "";
		int status = 0;
		resultWxMsg.setMediaId(mediaId);

		try {
			StringUtil st = new StringUtil();
			Token token = new CommonUtil().getToken(st.getSetting().getAppid(), st.getSetting().getAppsecret());
			String videoFileFullLocation = PropertiesUtils.read("customer.file.location") + "/video/" + mediaId
					+ ".mp4";
			String videoImgFileFullLocation = PropertiesUtils.read("customer.file.location") + "/video/"
					+ objWxMsg.getThumbMediaId() + ".jpg";
			if (!(new File(videoFileFullLocation).exists())) {// 先下载video文件
				WXCustomerManagerUtil.downloadVideo(token.getAccessToken(), mediaId);
			}
			if (!(new File(videoImgFileFullLocation).exists())) {// 下载video的图片
				WXCustomerManagerUtil.downloadVideoThumbMediaImg(token.getAccessToken(), objWxMsg.getThumbMediaId());
			}
			if (new File(videoFileFullLocation).length() <= 68) {
				result = DateUtils.nowDate(DateUtils.DATETIME_FORMATE) + "  状态码:10001,文件已超过3天未下载，已超时！" + "\r\n";
				status = -1;
				resultWxMsg.setDownloadResult(result);
				resultWxMsg.setIsDownload(status);
				return;
			}
			result = DateUtils.nowDate(DateUtils.DATETIME_FORMATE) + "  下载成功！" + "\r\n";
			status = 1;
			resultWxMsg.setDownloadResult(result);
			resultWxMsg.setIsDownload(status);
		} catch (Exception e) {
			logger.error(e);
			status = -1;
			resultWxMsg.setDownloadResult(result);
			resultWxMsg.setIsDownload(status);
		}
	}

}
