package com.weixin.customer.vo;

public class Message {

	private String touser;
	private String msgtype;
	private TextContent text;
	private MediaContent image;
	private MediaContent voice;
	private MediaContent video;
	private MusicContent music;
	private Articles news;

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public TextContent getText() {
		return text;
	}

	public void setText(TextContent text) {
		this.text = text;
	}

	public MediaContent getImage() {
		return image;
	}

	public void setImage(MediaContent image) {
		this.image = image;
	}

	public MediaContent getVoice() {
		return voice;
	}

	public void setVoice(MediaContent voice) {
		this.voice = voice;
	}

	public MediaContent getVideo() {
		return video;
	}

	public void setVideo(MediaContent video) {
		this.video = video;
	}

	public MusicContent getMusic() {
		return music;
	}

	public void setMusic(MusicContent music) {
		this.music = music;
	}

	public Articles getNews() {
		return news;
	}

	public void setNews(Articles news) {
		this.news = news;
	}

	@Override
	public String toString() {
		return "Message [touser=" + touser + ", msgtype=" + msgtype + ", text=" + text + ", image=" + image + ", voice=" + voice + ", video=" + video + ", music=" + music + ", news=" + news + "]";
	}
}