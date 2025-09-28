package com.weixin.customer.service;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;

import com.weixin.customer.vo.Article;
import com.weixin.customer.vo.Articles;
import com.weixin.customer.vo.Message;
import com.weixin.customer.vo.WeixinResponse;

public class CrmSendMessageService {
	private Logger log = Logger.getLogger(this.getClass());
	private RestTemplate restTemplate;

	private String serviceHost = "https://api.weixin.qq.com";

	/*public CrmSendMessageServiceImpl() {
		restTemplate = RestTemplateFactory.makeRestTemplate();
	}

	public WeixinResponse sendMessage(String accessToken, Message message) {
		WeixinResponse weixinResponse = null;
		String url = new StringBuffer(serviceHost).append("/cgi-bin/message/custom/send?access_token=").append(accessToken).toString();
		weixinResponse = restTemplate.postForObject(url, message, WeixinResponse.class);
		return weixinResponse;
	}*/

	/**
	 * 发送客服消息
	 * 
	 * @param openId
	 *            要发给的用户
	 * @param accessToken
	 *            微信公众号token
	 * @param weixinAppId
	 *            微信公众号APPID
	 */
	private void sendCustomMessage(String openId, String accessToken, String weixinAppId) {
		try {
			RestTemplate rest = new RestTemplate();
			String postUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + accessToken;
			// 推送图文消息
			Message message = new Message();
			message.setTouser(openId);// 普通用户openid
			message.setMsgtype("news");// 图文消息（点击跳转到外链）为news
			Articles news = new Articles();
			Article article = new Article();
			article.setDescription("客服消息图文描述");// 图文消息/视频消息/音乐消息的描述
			article.setPicurl("http://mmbiz.qpic.cn/mmbiz_jpg/CDW6Ticice130g6RcXCkNNDWic4dEaAHQDia2OG5atHBqSvsPuCfuqoyeeLWENia4ciaKt3KHWQ9t2LRPDpUo5AkOyyA/0");// 图文消息的图片链接，支持JPG、PNG格式，较好的效果为大图640*320，小图80*80
			article.setTitle("客服消息图文标题");// 图文消息/视频消息/音乐消息的标题
			// 图文推送链接
			String url = "https://www.baidu.com";
			article.setUrl(url);// 图文消息被点击后跳转的链接
			Article[] articles = { article };
			news.setArticles(articles);
			message.setNews(news);
			int i = 1;
			while (i <= 3) {// 循环发送3次
				WeixinResponse response = rest.postForObject(postUrl, message, WeixinResponse.class, new HashMap<String, String>());
				log.info("发送客服消息返回信息:" + response.toString());
				if (response.getErrcode() == 0) {// 发送成功-退出循环发送
					i = 4;
					break;
				} else {
					i++;// 发送失败-继续循环发送
				}
			}
		} catch (Exception e) {
			log.error("发送客服消息失败,openId=" + openId, e);
		}
	}
	
	public static void main(String[] args) {
		new CrmSendMessageService().sendCustomMessage("olSMfty-Izl7sqsQM493M4YjmEsI", "31_tSZBXtObr1Lqy4DWQXx-a680iZqjbyAhrYfZ-0XXLpvrT2OuyO0AgJPWKct9acgaw28IVKuNHW2QgmVXY46d-CyFQdmMRuYk4qCjYm399GFbnTpOcBVTrnj3xuzgO0jhbzUfZYRnw0wBTpH6ODQgAJAAPC", "wx0ae96c7d8a5d7e50");
	}

}
