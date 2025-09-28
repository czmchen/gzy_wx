package com.yq.controller;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hq.test.ImageHelper;
import com.weixin.entity.Token;
import com.weixin.entity.WxMsg;
import com.weixin.service.WxMsgService;
import com.weixin.util.ChangeAudioFormat;
import com.weixin.util.CommonUtil;
import com.weixin.util.DateUtils;
import com.weixin.util.HttpClientManager;
import com.weixin.util.PropertiesUtils;
import com.weixin.util.SpringUtil;
import com.weixin.util.StringUtil;
import com.weixin.util.WXCustomerManagerUtil;
import com.yq.constants.CommonConstants;
import com.yq.entity.User;
import com.yq.service.UserService;
import com.yq.util.CreateImage;
import com.yq.util.JsonUtil;
import com.yq.util.Pinyin4jUtil;
import com.yq.util.qrcode.QRCodeUtil;
import com.yq.vo.WXMsgListVo;

@Controller
@RequestMapping
public class CustromerCtrl {
	@Autowired
	private UserService userService;
	@SuppressWarnings("rawtypes")
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private WxMsgService wxMsgService;
	private final static Logger logger = Logger.getLogger(CustromerCtrl.class);
	


	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/customerservice/go2index.html")
	public ModelAndView index(String uuid, HttpSession session, Model model) {
		ModelAndView ml = new ModelAndView();

		String key = CommonConstants.REDIS_CUSTOMER_SERVER_UUID_DATA_CONSTANTS;
		Object value = redisTemplate.opsForHash().get(key, uuid);
		if (value == null) {
			ml.setViewName("customerservice/login");
			return ml;
		}

		User objUser = JsonUtil.json2Entity((String) value, User.class);
		if (StringUtil.isRealEmpty(uuid) || objUser == null) {
			ml.setViewName("customerservice/login");
			return ml;
		}

		session.setAttribute("uuid", uuid);
		session.setAttribute("customerLoginUserData", objUser);
		ml.setViewName("customerservice/index");
		return ml;
	}

	@ResponseBody
	@RequestMapping(value = "/customerservice/allUserList.html")
	public void allUserList(String wxName, HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
		try {
			Map<String, List<User>> data = new HashMap<String, List<User>>();
			List<User> userData = userService.allUser(wxName);
			for (User user : userData) {
				String firstChar = Pinyin4jUtil.converterToFirstSpell(
						(StringUtil.isRealEmpty(user.getCustRemark()) ? user.getRealname() : user.getCustRemark())
								.substring(0, 1))
						.toUpperCase();
				List<User> dataVal = data.get(firstChar);
				if (dataVal == null) {
					dataVal = new ArrayList<User>();
				}
				dataVal.add(user);
				if (firstChar.matches("^[A-Z]+$")) {
					data.put(firstChar, dataVal);
				} else {
					data.put("#", dataVal);
				}
			}
			data.entrySet().stream().sorted(java.util.Map.Entry.comparingByKey());
			pw.write(JsonUtil.obj2JSON(data));
			pw.flush();
		} catch (Exception e) {
			logger.error(e);
		} finally {
			pw.close();
		}
	}

	@ResponseBody
	@RequestMapping(value = "/customerservice/getUserMsgList.html")
	public void getUserMsgList(String wxName, Integer page, HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
		int recordAmount = 50;
		try {
			if (page == null) {
				page = 1;
			}
			recordAmount = 50 * page;
			List<WXMsgListVo> lstNewMsg = wxMsgService.getNewMsgList(wxName, recordAmount);
			pw.write(JsonUtil.list2JSON(lstNewMsg));
			pw.flush();
		} catch (Exception e) {
			logger.error(e);
		} finally {
			pw.close();
		}
	}

	@ResponseBody
	@RequestMapping(value = "/customerservice/updateNewMsg2Read.html")
	public void updateNewMsg2Read(String openId, HttpServletResponse response) throws Exception {
		logger.debug("执行updateNewMsg2Read");
		try {
			wxMsgService.updateNewMsg2Read(openId);
		} catch (Exception e) {
			logger.error(e);
		} finally {
		}
	}

	@ResponseBody
	@RequestMapping(value = "/customerservice/getUserMsgDetail.html")
	public void getUserMsgDetail(String openId, HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
		try {
			List<WxMsg> lstNewMsg = wxMsgService.getAllMsg(openId,null);
			pw.write(JsonUtil.list2JSON(lstNewMsg));
			pw.flush();
		} catch (Exception e) {
			logger.error(e);
		} finally {
			pw.close();
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/customerservice/getUserNewMsgDetail.html")
	public void getUserNewMsgDetail(String openId, HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
		try {
			List<WxMsg> lstNewMsg = wxMsgService.getAllMsg(openId,1);
			if(lstNewMsg!=null&&lstNewMsg.size()>0) {
				List<Long> ids = new ArrayList<Long>();
				for(WxMsg objWxMsg : lstNewMsg) {
					ids.add(Long.valueOf(objWxMsg.getId()));
				}
				wxMsgService.updateNewMsg2ReadByIds(openId, ids);
			}
			pw.write(JsonUtil.list2JSON(lstNewMsg));
			pw.flush();
		} catch (Exception e) {
			logger.error(e);
		} finally {
			pw.close();
		}
	}

	@ResponseBody
	@RequestMapping(value = "/customerservice/sendMsg.html")
	public void sendMsg(String openId, String textContet, HttpServletResponse response, HttpSession session)
			throws Exception {
		try {
			String strContentImg2Emoji = contentImg2Emoji(textContet);
			JSONObject text = new JSONObject();
			text.put("content", strContentImg2Emoji);// 202105251715 转化的发出的内容为微信端可显示的内容
			JSONObject json = new JSONObject();
			json.put("touser", openId);
			json.put("text", text);
			json.put("msgtype", "text");
			WxMsgService WxMsgService = (WxMsgService) SpringUtil.getBean("wxMsgService");
			String result = WXCustomerManagerUtil.customMsgSend(json);

			WxMsg wxMsg = new WxMsg();
			wxMsg.setFromUserName(((User) session.getAttribute("customerLoginUserData")).getOppen_id());
			wxMsg.setToUserName(openId);
			wxMsg.setMessageType("text");
			wxMsg.setContent(textContet);// 页面展示的内容
			wxMsg.setContentBak(strContentImg2Emoji);// 原来的内容作为处理前的备份
			wxMsg.setMsgSendOrReceive(1);
			wxMsg.setCreateTime(DateUtils.nowDate(DateUtils.DATETIME_FORMATE));
			wxMsg.setSendResult(result);

			WxMsgService.insert(wxMsg);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	private String contentImg2Emoji(String strContent) {
		Map<String, String> wxImg2emojiData = com.weixin.constants.CommonConstants.WX_IMG2EMOJI_DATA;
		for (String key : wxImg2emojiData.keySet()) {
			strContent = strContent.replaceAll("\\" + key, Matcher.quoteReplacement(wxImg2emojiData.get(key)));
		}
		return strContent;
	}

	@RequestMapping(value = "/customerservice/go2LoginPage.html")
	public String go2LoginPage() {
		return "customerservice/login";
	}

	@RequestMapping(value = "/customerservice/go2Friends.html")
	public String go2Friends() {
		return "customerservice/friends";
	}

	@RequestMapping(value = "/customerservice/sendNewMsg.html")
	public ModelAndView sendNewMsg(String openId, Model model) {
		ModelAndView ml = new ModelAndView();
		try {
			wxMsgService.insertNewMsg(openId);
			ml.addObject("default_check_user_openId", openId);
		} catch (Exception e) {
			logger.error(e);
		}
		ml.setViewName("customerservice/index");
		return ml;
	}

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/customerservice/checkLogin.html")
	public String checkLogin(String uuid) {
		try {
			String key = CommonConstants.REDIS_CUSTOMER_SERVER_UUID_DATA_CONSTANTS;
			Object value = redisTemplate.opsForHash().get(key, uuid);
			if (value != null) {
				return "1";
			}
		} catch (Exception e) {
			logger.error(e);
			return "0";
		}
		return "0";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/customer/customerLogin.html")
	public ModelAndView customerLogin(String uuid, HttpServletResponse response, HttpSession session) {
		ModelAndView ml = new ModelAndView();
		try {
			String key = CommonConstants.REDIS_CUSTOMER_SERVER_UUID_DATA_CONSTANTS;
			if (!redisTemplate.opsForHash().hasKey(key, uuid)) {
				return null;
			}
			String openId = (String) session.getAttribute("oppen_id");
			User userData = userService.getUserByOpenId(openId);
			if (userData != null && userData.getIsCustomer() == 1) {
				redisTemplate.opsForHash().put(key, uuid, JsonUtil.obj2JSON(userData));
				ml.setViewName("page/go2SubResult.html?operResult=1&returnURL=&operType=close&cu=");
			} else {
				ml.setViewName(
						"page/go2SubResult.html?operResult=0&returnURL=&operType=close&errorMsg=您不是客服，登陆失败!&cu=");
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return ml;
	}

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/customerservice/qrcode.html")
	public String customerQrcode(HttpServletRequest request, HttpSession session) {
		String qrcodeUUID = UUID.randomUUID().toString();
		try {
			String key = CommonConstants.REDIS_CUSTOMER_SERVER_UUID_DATA_CONSTANTS;
			redisTemplate.opsForHash().put(key, qrcodeUUID, null);

			String qrCodeLoacation = request.getSession().getServletContext().getRealPath("/")
					+ "customerservice/common/img/qrcode/";
			String text = "https://ganzhuyou.cn/customer/customerLogin.html?uuid=" + qrcodeUUID;// 存放在二维码中的内容
			String destPath = qrCodeLoacation + qrcodeUUID + ".jpg";
			logger.debug(destPath);

			QRCodeUtil.encode(text, CommonConstants.QR_CODE_GANZHU_IMGPATH, destPath, true);// 生成二维码
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
		return qrcodeUUID;
	}

	@SuppressWarnings("static-access")
	@ResponseBody
	@RequestMapping(value = "/customerservice/playVoice.html")
	public String playVoice(String mediaId, HttpServletRequest request, HttpSession session) {
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
			if (new File(amrVoiceFullLocation).length() == 68) {
				return "10001";// 语音已超过3天未下载，已超时！
			}
			File file = new File(mp3VoiceFullLocation);
			if (!file.exists()) {
				try {
					ChangeAudioFormat.changeToMp3(amrVoiceFullLocation, mp3VoiceFullLocation);
				} catch (Exception e) {
					logger.error(e);
					return "10002";// 语音转换失败，请重试！
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return mediaId;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/customerservice/updateVoiceFile.html")
	public void upload(MultipartFile file, String uuid, String openId, HttpServletRequest request) {
		String fullWavFlieLocaltion = PropertiesUtils.read("customer.file.location") + "/upload/voice/"
				+ UUID.randomUUID() + ".wav";
		String fullAmrFlieLocaltion = PropertiesUtils.read("customer.file.location") + "/upload/voice/"
				+ UUID.randomUUID() + ".amr";
		try {
			File file2Local = new File(fullWavFlieLocaltion);
			file.transferTo(file2Local);
			ChangeAudioFormat.changeToAMR(fullWavFlieLocaltion, fullAmrFlieLocaltion);
			String mediaId = WXCustomerManagerUtil.uploadMedia(fullAmrFlieLocaltion, "voice");// 上传到腾讯
			JSONObject text = new JSONObject();
			text.put("media_id", mediaId);
			JSONObject json = new JSONObject();
			json.put("touser", openId);
			json.put("voice", text);
			json.put("msgtype", "voice");
			String sendResult = WXCustomerManagerUtil.customMsgSend(json);

			WxMsg wxMsg = new WxMsg();
			String key = CommonConstants.REDIS_CUSTOMER_SERVER_UUID_DATA_CONSTANTS;
			Object value = redisTemplate.opsForHash().get(key, uuid);
			if (value != null) {// 通过redis获取登陆用户的openId
				User objUser = JsonUtil.json2Entity((String) value, User.class);
				wxMsg.setFromUserName(objUser.getOppen_id());
			}
			wxMsg.setToUserName(openId);
			wxMsg.setMessageType("voice");
			wxMsg.setContent("[语音]");
			wxMsg.setMsgSendOrReceive(1);
			wxMsg.setMediaId(mediaId);
			wxMsg.setFormat("amr");
			wxMsg.setCreateTime(DateUtils.nowDate(DateUtils.DATETIME_FORMATE));
			wxMsg.setSendResult(sendResult);
			WxMsgService WxMsgService = (WxMsgService) SpringUtil.getBean("wxMsgService");
			WxMsgService.insert(wxMsg);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/customerservice/reDownloadImg.html")
	private Integer reDownloadImg(String imgName) {
		String imgFullName = imgName + ".jpg";// 原图全图
		String abbrImgFullName = imgName + "_abbr.jpg";// 缩略图
		int isDownload = -1;
		String downloadResult = "成功!";
		WxMsg objWxMsg = null;
		try {
			objWxMsg = wxMsgService.search(imgName).get(0);
			HttpClientManager.downloadImg(objWxMsg.getPicUrl(), imgFullName);// 按照原图下载
			ImageHelper.processImg(imgFullName, abbrImgFullName, 300);
			isDownload = 1;
		} catch (Exception e) {
			downloadResult = "失败!" + e.getMessage();
			logger.error(e);
		} finally {
			WxMsg wxMsg = new WxMsg();
			wxMsg.setMediaId(imgName);
			wxMsg.setIsDownload(isDownload);
			wxMsg.setDownloadResult(wxMsg.getDownloadResult() + "\r\n" + DateUtils.nowDate(DateUtils.DATETIME_FORMATE)
					+ " 重新下载结果：" + downloadResult);
			wxMsgService.updateDownloadResult(wxMsg);
		}
		return isDownload;
	}

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/customerservice/uploadImg.html")
	private Integer uploadImg(MultipartFile file, String uuid, String openId, HttpServletRequest request) {
		int successFlag = 0;
		try {
			String suffix = "";
			String uploadType = "";
			List<String> wxServerType = com.weixin.constants.CommonConstants.WX_SERVER_TYPE;
			for(String type : wxServerType) {
				if(file.getContentType().toUpperCase().indexOf(type)!=-1) {//也就是存在该业务类型
					suffix = type.toLowerCase();
					uploadType = "image";
				}
			}
			if(file.getContentType().toUpperCase().indexOf(com.weixin.constants.CommonConstants.VIDEO_TYPE_MP4)!=-1) {
				suffix = com.weixin.constants.CommonConstants.VIDEO_TYPE_MP4.toLowerCase();
				uploadType = "video";
			}
			if(StringUtil.isRealEmpty(suffix)) {
				return null;
			}
			String fileName = UUID.randomUUID().toString();
			String uploadFileLocation = PropertiesUtils.read("customer.file.location") + "/upload/"+uploadType+"/"+ fileName+"."+suffix;
			File uploadFile = new File(uploadFileLocation);
			file.transferTo(uploadFile);
			
			String mediaId = WXCustomerManagerUtil.uploadMedia(uploadFileLocation, uploadType);// 上传到腾讯
			JSONObject text = new JSONObject();
			text.put("media_id", mediaId);
			JSONObject json = new JSONObject( );
			json.put("touser", openId);
			json.put(uploadType, text);
			json.put("msgtype", uploadType);
			String sendResult = WXCustomerManagerUtil.customMsgSend(json);
			
			WxMsg wxMsg = new WxMsg();
			
			if("image".equals(uploadType)) {
				ImageHelper.moveFile(uploadFileLocation, PropertiesUtils.read("customer.file.location") + "/images/"+ mediaId+"."+suffix);//迁移图片，再删除
				ImageHelper.processImg(mediaId+"."+suffix, mediaId+"_abbr."+suffix, 300);
			}else if("video".equals(uploadType)) {
				String targetVideoLocation = PropertiesUtils.read("customer.file.location") + "/video/"+ mediaId+"."+suffix;
				String thumbMediaId = mediaId+"_sysMade_thumb";
				ImageHelper.moveFile(uploadFileLocation, targetVideoLocation);//迁移视频，再删除
				CreateImage.getVideoPicture(targetVideoLocation, PropertiesUtils.read("customer.file.location") + "/video/"+ thumbMediaId+".jpg");
				wxMsg.setThumbMediaId(thumbMediaId);
				wxMsg.setIsDownload(1);//通知不要下载了
			}

			String key = CommonConstants.REDIS_CUSTOMER_SERVER_UUID_DATA_CONSTANTS;
			Object value = redisTemplate.opsForHash().get(key, uuid);
			if (value != null) {// 通过redis获取登陆用户的openId
				User objUser = JsonUtil.json2Entity((String) value, User.class);
				wxMsg.setFromUserName(objUser.getOppen_id());
			}
			wxMsg.setToUserName(openId);
			wxMsg.setMessageType(uploadType);
			wxMsg.setContent(uploadType.equals("image")?"[图片]":"[视频]");
			wxMsg.setMsgSendOrReceive(1);
			wxMsg.setMediaId(mediaId);
			wxMsg.setFormat(suffix);
			wxMsg.setPicUrl(PropertiesUtils.read(com.weixin.constants.CommonConstants.CUSTOMER_EMOJI2IMG_DOMAIN_URL)+"/cloud/download.html?fileType="+(uploadType.equals("image") ?"images":"video")+"&fileName="+mediaId+"&suffix="+suffix);
			wxMsg.setCreateTime(DateUtils.nowDate(DateUtils.DATETIME_FORMATE));
			wxMsg.setSendResult(sendResult);
			WxMsgService WxMsgService = (WxMsgService) SpringUtil.getBean("wxMsgService");
			WxMsgService.insert(wxMsg);
			successFlag = 1;
		} catch (Exception e) {
			logger.error(e);
		}
		return successFlag;
	}
	
}
