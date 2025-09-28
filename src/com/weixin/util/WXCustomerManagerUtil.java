package com.weixin.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.weixin.entity.Token;
import com.yq.util.JsonUtil;

public class WXCustomerManagerUtil {
	public static final Logger log = Logger.getLogger(WXCustomerManagerUtil.class);

	public static void main(String[] args) throws Exception {
		// 元泓星辰
//		String appId = "wx0ae96c7d8a5d7e50";
//		String appSecret = "14dbe87ced1b559449003b144c553292";
//		String access_token = "37_lo7aCu4QYCXBwEFDE2q5zt1lHMGl-5rCfW3qdzOhGNpZ6CvpskyBFF_Oe3rwDOUCqMgQn-E8P-SJbKs7g5PHNFd8OsKtqa8heCb1Rq6NKzoY7_FVywSgUYq7xLcqq7VovM_Ju-8kecKDq1k9QGSgAEADTF";
//		handleVoice(access_token, "0UqP8FLzl7Gg7LtATT6Q_gr6xIbsqvCVy6NYs1hZODWo-OP66Ky2bW7TRjb07nST","0UqP8FLzl7Gg7LtATT6Q_gr6xIbsqvCVy6NYs1hZODWo-OP66Ky2bW7TRjb07nST.amr",
//				"d:/data/video/0UqP8FLzl7Gg7LtATT6Q_gr6xIbsqvCVy6NYs1hZODWo-OP66Ky2bW7TRjb07nST.amr");
//		System.err.println("Tocken：" + access_token);
//		ChangeAudioFormat.changeToMp3(
//				"d:/data/video/0UqP8FLzl7Gg7LtATT6Q_gr6xIbsqvCVy6NYs1hZODWo-OP66Ky2bW7TRjb07nST.amr",
//				"d:/data/video/0UqP8FLzl7Gg7LtATT6Q_gr6xIbsqvCVy6NYs1hZODWo-OP66Ky2bW7TRjb07nST.mp3");
//		// 播放一个 mp3 音频文件, 代码很简单
//		File file = new File("d:/data/video/0UqP8FLzl7Gg7LtATT6Q_gr6xIbsqvCVy6NYs1hZODWo-OP66Ky2bW7TRjb07nST.mp3");
//		Player player = new Player(new FileInputStream(file));
//		player.play();

		// 发送消息
		/*
		 * JSONObject text = new JSONObject(); text.put("content", "内容"); JSONObject
		 * json = new JSONObject(); json.put("touser", "olSMfty-Izl7sqsQM493M4YjmEsI");
		 * json.put("text", text); json.put("msgtype", "text");
		 * 
		 * customSend(json);
		 */

		// 发送图片
		/*
		 * JSONObject media_id = new JSONObject(); String filePath = "d:\\payImg.png";
		 * media_id.put("media_id", upload(filePath, access_token, "image"));
		 * 
		 * JSONObject json1 = new JSONObject(); json1.put("touser",
		 * "olSMfty-Izl7sqsQM493M4YjmEsI"); json1.put("image", media_id);
		 * json1.put("msgtype", "image"); customSend(json1, access_token);
		 */
		
		
//		JSONObject text = new JSONObject();
//		text.put("media_id", "fMpU9kibr_dagTUaaCgVj2UszG0ot-IraJAA90QObWtqFjd7bMB0ix8SKb4QAsuO");
//		JSONObject json = new JSONObject();
//		json.put("touser", "olSMfty-Izl7sqsQM493M4YjmEsI");
//		json.put("image", text);
//		json.put("msgtype", "image");
//		String sendResult = WXCustomerManagerUtil.customMsgSend(json);
//		System.out.println(sendResult);
//		String access_token = "50_lQJOttb8M-n0ddDNtRTpcqwcg-L7IP6jCsIEsWszim583712gp83wqfSsug0ZV5UkVFPTFlAFQE5TlWzNASIFAimfcSucfrNpx-lQbiMP271aGnfH0R2pHPHUHxNW0akvuqBcRcokMQkXexkKIVhAGAZXK";
//		String media_id = "jZreHdVVB8g8xNPhAmvcsP4AWOWaoZm_mR1YEF1p8VJueAMC0lqLdkeX421DaxXc";
//		String url = "https://api.weixin.qq.com/cgi-bin/media/get?access_token="+access_token+"&media_id="+media_id;
//		
//		
//		String realURL = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=51_lHhRUVMYd51UME4u9Xa0FFiUXZ7ZEqBnY1j8der3Z6bd9w8r-ekzafE1A8xhxxiMwTwKFz8BCyGaequ9l6cYGwF67nbB7VPLgE5EFc409VBtdgYAj0jsuQpsrz23MMWEFKZ_QmjghdKbPD9cNMSfABAVUA&media_id=jZreHdVVB8g8xNPhAmvcsP4AWOWaoZm_mR1YEF1p8VJueAMC0lqLdkeX421DaxXc";
//		String fileFullLocation = PropertiesUtils.read("customer.file.location") + "/video/";
//		
//		
		/*
		 * HttpClientManager objHttpClientManager = new HttpClientManager(); Map<String,
		 * String> header = new HashMap<String, String>(); header.put("Cache-Control",
		 * "no-cache, must-revalidate"); header.put("Connection", "keep-alive");
		 * header.put("Content-Type", "video/mpeg4");
		 * objHttpClientManager.initHttpsClient(); objHttpClientManager.download(header,
		 * "video", realURL, access_token+".mp4");
		 * objHttpClientManager.releaseConnection();
		 */
		
//		downloadVideo("51_ontd3tLZNI9X6Q7hzmd69ebqMaLolWNklSv9vdM2Sko4VFWkUeiwSnaUDiIU8dggYfb2qG7eYzYPSZw18impFQHi3aCXvuSDT3uYjfBHSu_GVZpm4npHZ5h-auIlIMa-GJVhQlTwRw3YHs2MRTYdAJAXZE", "jZreHdVVB8g8xNPhAmvcsP4AWOWaoZm_mR1YEF1p8VJueAMC0lqLdkeX421DaxXc");
//		MyX509TrustManager.downLoadFromUrlHttps(realURL, access_token+".mp4", fileFullLocation);
		
		System.out.println(DateUtils.nowDate(DateUtils.TIME_FORMATE));
	}
	
	/**
	 * 获取code
	 */
	// https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=redirect_uri&response_type=code&scope=snsapi_base&state=1&connect_redirect=1#wechat_redirect

	/**
	 * 获取code后，根据code获取openid
	 */
	// https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=secret&code=$code&grant_type=authorization_code

	public static void uploadVoice(final String base64Audio) {
		String fullFlieLocaltion = PropertiesUtils.read("customer.file.location") + "/upload/voice/" + UUID.randomUUID()
				+ ".amr";
		try {
			saveMedia2LocalDisk(base64Audio, fullFlieLocaltion);// 保存多媒体到本地先
			String result = uploadMedia(fullFlieLocaltion, "voice");// 上传到腾讯
		} catch (Exception e) {
			log.error(e);
		}
	}

	public static void saveMedia2LocalDisk(final String base64Audio, final String fullFlieLocaltion) {
		FileOutputStream fos = null;
		try {
//			Decoder decoder = Base64.getDecoder();
//			byte[] decodedByte = decoder.decode(base64Audio.split(",")[1].replaceAll(" +", "+"));
			fos = new FileOutputStream(new File(fullFlieLocaltion));
			fos.write(base64Audio.getBytes());
		} catch (Exception e) {
			log.error(e);
		} finally {
			try {
				if (fos != null) {
					fos.close();
					fos = null;
				}
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 上传多媒体图片、小视频、语音
	 * 
	 * @param filePath
	 * @param accessToken
	 * @param type
	 * @return
	 * @throws IOException
	 */
	public static String uploadMedia(String filePath, String type) throws IOException {
		String result = "";
		try {
			CommonUtil commonUtil = new CommonUtil();
			StringUtil st = new StringUtil();
			Token token = commonUtil.getToken(st.getSetting().getAppid(), st.getSetting().getAppsecret());
			String targetURL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=" + token.getAccessToken()
					+ "&type=" + type;
			result = HttpClientManager.upload(filePath, targetURL);
			Map resultData = JsonUtil.json2Map(result);
			log.info("客服消息result：" + result);
			if (resultData != null && resultData.get("media_id") != null) {
				return (String) resultData.get("media_id");
			}
		} catch (Exception e) {
			log.error("向客服发送 POST 请求出现异常！" + e);
		} finally {
			//httpClientManager.releaseConnection();
		}
		return null;
	}
	

	/**
	 * 发送图片
	 * 
	 * @param filePath
	 * @param accessToken
	 * @param type
	 * @return
	 * @throws IOException
	 */
	public static Object upload(String filePath, String accessToken, String type) throws IOException {
		File file = new File(filePath);
		if (!file.exists() || !file.isFile()) {
			throw new IOException("文件不存在");
		}
		// String url = UPLOAD_URL.replace("ACCESS_TOKEN",
		// accessToken).replace("TYPE", type);
		String url = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=" + accessToken + "&type=" + type;
		URL urlobj = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) urlobj.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);

		// 设置头信息
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Charset", "UTF-8");

		// 设置边界
		String BOUNFARY = "----------" + System.currentTimeMillis();
		conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNFARY);

		StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(BOUNFARY);
		sb.append("\r\n");
		sb.append("Content-Disposition:from-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
		sb.append("Content-Type:application/actet-stream\r\n\r\n");

		byte[] head = sb.toString().getBytes("utf-8");
		// 获得输出流
		OutputStream out = new DataOutputStream(conn.getOutputStream());
		// 输出表头
		out.write(head);

		// 文件正文部分
		// 把文件以流的方式 推入到url
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while ((bytes = in.read(bufferOut)) != -1) {
			out.write(bufferOut, 0, bytes);
		}
		in.close();
		// 结尾部分
		byte[] foot = ("\r\n--" + BOUNFARY + "--\r\n").getBytes("utf-8");
		out.write(foot);
		out.flush();
		out.close();

		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		String result = null;
		reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line = null;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		if (result == null) {
			result = buffer.toString();
		}
		if (result != null) {
			reader.close();
		}
		Map jsonObject = JsonUtil.json2Map(result);
		System.out.println(jsonObject);
		String typeName = "media_id";
		if (!"image".equals(type) && !"voice".equals(type) && !"video".equals(type)) {
			typeName = type + "_media_id";
		}
		Object mediaid = jsonObject.get(typeName);
		return mediaid;
	}

	/**
	 * post请求,发送消息
	 */
	public static String customSend(JSONObject json) {

		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			CommonUtil commonUtil = new CommonUtil();
			StringUtil st = new StringUtil();
			Token token = commonUtil.getToken(st.getSetting().getAppid(), st.getSetting().getAppsecret());

			URL realUrl = new URL(
					"https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + token.getAccessToken());
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "text/html; charset=utf-8");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Charset", "UTF-8");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(json.toString());
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			System.out.println("客服消息result：" + result);
		} catch (Exception e) {
			System.out.println("向客服发送 POST 请求出现异常！" + e);
			log.error("向客服发送 POST 请求出现异常！" + e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				log.error(ex);
			}
		}
		return result;
	}

	/**
	 * post请求,发送消息
	 */
	public static String customMsgSend(JSONObject json) {
		String result = "";
		try {
			CommonUtil commonUtil = new CommonUtil();
			StringUtil st = new StringUtil();
			Token token = commonUtil.getToken(st.getSetting().getAppid(), st.getSetting().getAppsecret());
			result = HttpClientManager.postJSON(
					"https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + token.getAccessToken(),
					json.toString());
			log.info("客服消息result：" + result);
		} catch (Exception e) {
			log.error("向客服发送 POST 请求出现异常！" + e);
		} finally {
			//httpClientManager.releaseConnection();
		}
		return result;
	}

	/**
	 * 从微信下载语音，并存储到硬盘
	 * 
	 * @Title: handleVoice @Description: TODO(从微信下载语音，并存储到硬盘) @author
	 *         pll @param @param userid 用户ID @param @param mediaId
	 *         从微信下载所需语音ID @return void 返回类型 @throws
	 */
	public static void handleVoice(final String accesstoken, final String mediaId, final String fileName,
			final String fileFullLocation) {
		InputStream is = null;
		FileOutputStream fileOutputStream = null;
		String url = "https://file.api.weixin.qq.com/cgi-bin/media/get?access_token=" + accesstoken + "&media_id="
				+ mediaId;
		try {
//			URL urlGet = new URL(url);
//			HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
//			http.setRequestMethod("GET"); // 必须是get方式请求
//			http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//			http.setDoOutput(true);
//			http.setDoInput(true);
//
//			System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
//			System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
//			http.connect();
//
//			is = http.getInputStream();// 获取文件转化为byte流
//
//			byte[] data = new byte[8 * 1024];// 存储到硬盘，原本音频格式为amr
//			int len = 0;
//			fileOutputStream = new FileOutputStream(fileFullLocation);
//			while ((len = is.read(data)) != -1) {
//				fileOutputStream.write(data, 0, len);
//			}

			MyX509TrustManager.downLoadFromUrlHttps(url, fileName, fileFullLocation);

		} catch (Exception e) {
			log.error(e);
		} finally {
//			if (is != null) {
//				try {
//					is.close();
//				} catch (IOException e) {
//					log.error(e);
//				}
//			}
//			if (fileOutputStream != null) {
//				try {
//					fileOutputStream.close();
//				} catch (IOException e) {
//					log.error(e);
//				}
		}
	}
	
	public static void downloadVideo(final String accesstoken, final String mediaId) throws Exception {
		String realURL = "https://api.weixin.qq.com/cgi-bin/media/get?access_token="+accesstoken+"&media_id="+mediaId;
		try {
			Map<String, String> header = new HashMap<String, String>();
			header.put("Cache-Control", "no-cache, must-revalidate");
			header.put("Connection", "keep-alive");
			header.put("Content-Type", "video/mpeg4");
			HttpClientManager.download(header, "video", realURL, mediaId+".mp4");
		}catch(Exception e) {
			log.error(e);
			throw e;
		}
	}
	
	public static void downloadVideoThumbMediaImg(final String accesstoken, final String mediaId) throws Exception {
		String realURL = "https://api.weixin.qq.com/cgi-bin/media/get?access_token="+accesstoken+"&media_id="+mediaId;
		try {
			Map<String, String> header = new HashMap<String, String>();
			header.put("Cache-Control", "no-cache, must-revalidate");
			header.put("Connection", "keep-alive");
			header.put("Content-Type", "image/jpeg");
			HttpClientManager.download(header, "video", realURL, mediaId+".jpg");
		}catch(Exception e) {
			log.error(e);
			throw e;
		}
	}
}
class UserInfo {

	private List<User_info_list> user_info_list;

	public void setUser_info_list(List<User_info_list> user_info_list) {
		this.user_info_list = user_info_list;
	}

	public List<User_info_list> getUser_info_list() {
		return user_info_list;
	}

}

class User_info_list {
	private int subscribe;
	private String openid;
	private String nickname;
	private int sex;
	private String language;
	private String city;
	private String province;
	private String country;
	private String headimgurl;
	private long subscribe_time;
	private String unionid;
	private String remark;
	private int groupid;
	private Date tagid_list;
	private String subscribe_scene;
	private long qr_scene;
	private String qr_scene_str;

	public void setSubscribe(int subscribe) {
		this.subscribe = subscribe;
	}

	public int getSubscribe() {
		return subscribe;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getOpenid() {
		return openid;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getNickname() {
		return nickname;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getSex() {
		return sex;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLanguage() {
		return language;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCity() {
		return city;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getProvince() {
		return province;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountry() {
		return country;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setSubscribe_time(long subscribe_time) {
		this.subscribe_time = subscribe_time;
	}

	public long getSubscribe_time() {
		return subscribe_time;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return remark;
	}

	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}

	public int getGroupid() {
		return groupid;
	}

	public void setTagid_list(Date tagid_list) {
		this.tagid_list = tagid_list;
	}

	public Date getTagid_list() {
		return tagid_list;
	}

	public void setSubscribe_scene(String subscribe_scene) {
		this.subscribe_scene = subscribe_scene;
	}

	public String getSubscribe_scene() {
		return subscribe_scene;
	}

	public void setQr_scene(long qr_scene) {
		this.qr_scene = qr_scene;
	}

	public long getQr_scene() {
		return qr_scene;
	}

	public void setQr_scene_str(String qr_scene_str) {
		this.qr_scene_str = qr_scene_str;
	}

	public String getQr_scene_str() {
		return qr_scene_str;
	}

}