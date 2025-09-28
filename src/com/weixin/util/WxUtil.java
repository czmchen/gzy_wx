package com.weixin.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.weixin.constants.CommonConstants;
import com.weixin.entity.Token;
import com.weixin.entity.WeixinUserInfo;
import com.weixin.entity.WxSetting;
import com.weixin.vo.SubscribeUser;
import com.weixin.vo.TemplateMessage;
import com.weixin.vo.WxTicketResponseInfo;


public class WxUtil extends StringUtil {
	// @Autowired
	// private static UserInfoService userInfoService = new UserInfoService();
	private static Logger log = Logger.getLogger(WxUtil.class);
	public final static String URL_USER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESSTOKEN&openid=OPENID&lang=zh_CN";
    public final static String URL_INFO_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
    public final static String STR_ACCESSTOKEN = "ACCESSTOKEN";
	public final static String STR_OPENID = "OPENID";
	private static final String ERR_SUBSCRIBE_GET_INFO = "请求订阅用户失败:";
	
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(HttpServletRequest request) {
		try {
			// 将解析结果存储在HashMap中
			Map<String, String> map = new HashMap<String, String>();

			// 从request中取得输入流
			InputStream inputStream = request.getInputStream();
			// 读取输入流
			SAXReader reader = new SAXReader();
			Document document = reader.read(inputStream);
			// 得到xml根元素
			Element root = document.getRootElement();
			// 得到根元素的所有子节点
			List<Element> elementList = root.elements();

			// 遍历所有子节点
			for (Element e : elementList)
				map.put(e.getName(), e.getText());

			// 释放资源
			inputStream.close();
			inputStream = null;
			return map;
		} catch (Exception e) {
			return null;
		}
	}

	
	/**
     * 远程请求userinfo
     * @author 梁嘉贺
     * @date 2018年4月3日 下午2:40:04
     * @description 远程请求token
     * @param openId 传入openId
     * @return SubscribeUser userinfo
     */
    public static SubscribeUser getUserInfoFromWeChat(String openid) {
        try{
            CommonUtil commonUtil = new CommonUtil();
    		StringUtil st = new StringUtil();
    		Token token = commonUtil.getToken(st.getSetting().getAppid(), st.getSetting().getAppsecret());
    		
            String response = HttpClientManager.loadUrl(getUserInfoUrl(token.getAccessToken(), openid));
            SubscribeUser userinfo =  com.alibaba.fastjson.JSONObject.parseObject(response, SubscribeUser.class);
            if(StringUtil.isRealEmpty(userinfo.getErrcode()) || userinfo.getErrcode().equals(String.valueOf(0)) ){
                return userinfo;
            }else{
                StringBuffer buffer = new StringBuffer();
                buffer.append(ERR_SUBSCRIBE_GET_INFO);
                buffer.append(userinfo.getErrcode());
                buffer.append(userinfo.getErrmsg());
                throw new RuntimeException(buffer.toString());
            }
        }catch(Exception e){
            throw new RuntimeException(e.getMessage(),e);
        }finally{
        	//client.releaseConnection();
        }
    }
    
    /**
     * 获取用户的url
     * @author 梁嘉贺
     * @date 2018年4月3日 上午11:18:41
     * @param token token
     * @description 获取用户的url
     * @return
     */
    public static String getUserInfoUrl(String token,String openid){
        return URL_USER_INFO.replace(STR_ACCESSTOKEN, token).replace(STR_OPENID, openid);
    }
    
	public static Map<String, Object> oppen_id(HttpServletRequest request, HttpSession session) {
		String oppen_id = "";
		String code="";
		String access_token="";
		StringUtil st = new StringUtil();
		if (session.getAttribute("oppen_id") == null) {
			code = (String) request.getParameter("code");// 获取code值
			System.out.println("------------------------------------");
			System.out.println("code=" + code);
			System.out.println("------------------------------------");
			if (code != null) {
				String token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
						+ st.getSetting().getAppid()
						+ "&secret="
						+ st.getSetting().getAppsecret()
						+ "&code="
						+ code
						+ "&grant_type=authorization_code";
				// 获取用户的openid
				net.sf.json.JSONObject json = new net.sf.json.JSONObject();
				// CommonUtil commonUtil=new CommonUtil();
				json = CommonUtil.httpsRequest(token_url, "GET", null);
				if (json != null) {
					oppen_id = json.getString("openid");
					access_token=json.getString("access_token");
					log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
					log.info(json);
				}
			}
		} else {
			oppen_id = (String) session.getAttribute("oppen_id");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("code",code);
		map.put("oppen_id",oppen_id);
		map.put("access_token",access_token);
		log.info("code=="+code);
		log.info("WXUTIL 96 --oppen_id=="+oppen_id);
		return map;
	}
	

	
	public static Map<String, Object> oppenIdInfo(HttpServletRequest request,
			HttpSession session) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			if (session.getAttribute("oppen_id") == null) {
				
				Map<String, Object> map2  = WxUtil.oppen_id(request, session);
			//	String code  = (String) map2.get("code");
				String oppen_id = (String) map2.get("oppen_id");
				String access_token  = (String) map2.get("access_token");
				AdvancedUtil advancedUtil = new AdvancedUtil();
				WeixinUserInfo wxi = new WeixinUserInfo();

				log.info("--------------------");
				log.info("oppen_id====" + oppen_id+"......accessToken====" + access_token);
				wxi = advancedUtil.getUserInfo(access_token, oppen_id);
				// username = URLEncoder.encode(wxi.getNickname(), "utf-8");
				String realname = wxi.getNickname();
				String head_img = wxi.getHeadImgUrl();
				log.info("realname==" + realname+"....head_img_url=" + head_img);
//				session.setAttribute("realname", realname);
//				session.setAttribute("head_img", head_img_url);
//				session.setAttribute("oppen_id", oppen_id);
				map.put("realname", realname);
				map.put("head_img", head_img);
				map.put("oppen_id", oppen_id);
			}
			return map;
		} catch (Exception e) {
			e.getStackTrace();
			return null;
		}

	}
	
	
	/****
     * 
     * @author 陈周敏
     * @date 2017年9月22日 上午10:01:16
     * @description 微信公众号发送模板消息通知
     * @param templateMessage
     * @return
     * @throws Exception
     */
    public static String sendNote(final TemplateMessage templateMessage) {
        String result = null;
        try {
            CommonUtil commonUtil = new CommonUtil();
    		StringUtil st = new StringUtil();
            Token token = commonUtil.getToken(st.getSetting().getAppid(), st.getSetting().getAppsecret());
          
            String url = new StringBuffer(URL_INFO_TEMPLATE).append(token.getAccessToken()).toString();
            result = HttpClientManager.postJSON(url, templateMessage.toJson());
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally{
        	//client.releaseConnection();
        }
    }
    
    
    public static WxTicketResponseInfo getTicket(WxSetting wxSetting) {
    	WxTicketResponseInfo userinfo =  null;
        try{
        	CommonUtil commonUtil = new CommonUtil();
            Token token = commonUtil.getToken(wxSetting.getAppid(), wxSetting.getAppsecret());
    		String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+token.getAccessToken()+"&type=jsapi";
            String response = HttpClientManager.loadUrl(url);
            userinfo =  com.alibaba.fastjson.JSONObject.parseObject(response, WxTicketResponseInfo.class);
            if(StringUtil.isRealEmpty(userinfo.getErrcode()) || userinfo.getErrcode().equals(String.valueOf(0)) ){
                return userinfo;
            }
        }catch(Exception e){
        	log.error(e);
            throw new RuntimeException(e.getMessage(),e);
        }finally{
        	//client.releaseConnection();
        }
        return userinfo;
    }
}

