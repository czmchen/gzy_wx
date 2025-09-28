package com.weixin.vo;

import com.alibaba.fastjson.JSONObject;

/****
 * 
 * @author 陈周敏
 * @date 2017年9月21日 下午1:48:34
 * @Description 整理陈文韬写的微信消息类
 */

public class TemplateMessage {

	private String touser;
	private String template_id;
	private String url;
	private String topcolor;
	private TemplateData data = new TemplateData();

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTopcolor() {
		return topcolor;
	}

	public void setTopColor(String topcolor) {
		this.topcolor = topcolor;
	}

	public TemplateData getData() {
		return data;
	}

	public void setData(TemplateData data) {
		this.data = data;
	}

	public String toJson() {
		return JSONObject.toJSONString(this);
	}
}
