package com.weixin.vo;

import com.weixin.constants.CommonConstants;
import com.weixin.util.PropertiesUtils;

public enum WxEmoji {
	SMILE("/::)", "100.gif", "微笑"), SAD("/::~", "101.gif", "伤心"), BEAUTY("/::B", "102.gif", "美女"),
	INADAZE2("/::|", "103.gif", "发呆"), SUNGLASSES("/:8-)", "104.gif", "墨镜"), CRY("/::<", "105.gif", "哭"),
	SHAME("/::$", "106.gif", "羞"), DUMB("/::X", "107.gif", "哑"), SLEEP("/::Z", "108.gif", "睡"),
	CRY2("/::'(", "109.gif", "哭"), EMBARRASSED("/::-|", "110.gif", "囧"), ANGER("/::@", "111.gif", "怒"),
	NAUGHTY("/::P", "112.gif", "调皮"), LAUGH("/::D", "113.gif", "笑"), SURPRISED("/::O", "114.gif", "惊讶"),
	SORRY("/::(", "115.gif", "难过"), COOL("/::+", "116.gif", "酷"), SWEAT2("/:--b", "117.gif", "汗"),
	CRAZY2("/::Q", "118.gif", "抓狂"), SPIT("/::T", "119.gif", "吐"), LAUGH2("/:,@P", "120.gif", "笑"),
	HAPPY("/:,@-D", "121.gif", "快乐"), ODD("/::d", "122.gif", "奇"), PRIDE("/:,@o", "123.gif", "傲"),
	HUNGRY("/::g", "124.gif", "饿"), TIRED("/:|-)", "125.gif", "累"), SCARE2("/::!", "126.gif", "吓"),
	SWEAT3("/::L", "127.gif", "汗"), HAPPY2("/::>", "128.gif", "高兴"), LEISURE("/::,@", "129.gif", "闲"),
	STRIVE("/:,@f", "130.gif", "努力"), SCOLD("/::-S", "131.gif", "骂"), DOUBT("/:?", "132.gif", "疑问"),
	SECRET("/:,@x", "133.gif", "秘密"), CHAOS("/:,@@", "134.gif", "乱"), CRAZY("/::8", "135.gif", "疯"),
	MOURNING("/:,@!", "136.gif", "哀"), GHOST("/:!!!", "137.gif", "鬼"), STRIKE("/:xx", "138.gif", "打击"),
	BYE("/:bye", "139.gif", "bye"), SWEAT("/:wipe", "140.gif", "汗"), DIG("/:dig", "141.gif", "抠"),
	APPLAUSE("/:handclap", "142.gif", "鼓掌"), TOOBAD("/:&-(", "143.gif", "糟糕"), SPOOF("/:B-)", "144.gif", "恶搞"),
	WHAT("/:<@", "145.gif", "什么"), WHAT2("/:@>", "146.gif", "什么"), TIRED2("/::-O", "147.gif", "累"),
	SEE("/:>-|", "148.gif", "看"), SORRY2("/:P-(", "149.gif", "难过"), SORRY3("/::'|", "150.gif", "难过"),
	BAD("/:X-)", "151.gif", "坏"), DEAR("/::*", "152.gif", "亲"), SCARE("/:@x", "153.gif", "吓"),
	POOR("/:8*", "154.gif", "可怜"), KNIFE2("/:pd", "155.gif", "刀"), FRUITS("/:<W>", "156.gif", "水果"),
	ALCOHOL("/:beer", "157.gif", "酒"), BASKETBALL("/:basketb", "158.gif", "篮球"), TABLETENNIS("/:oo", "159.gif", "乒乓"),
	COFFEE("/:coffee", "160.gif", "咖啡"), DELICIOUSFOOD("/:eat", "161.gif", "美食"), ANIMAL("/:pig", "162.gif", "动物"),
	FLOWER("/:rose", "163.gif", "鲜花"), WITHERED("/:fade", "164.gif", "枯"), LIP("/:showlove", "165.gif", "唇"),
	LOVE2("/:heart", "166.gif", "爱"), BREAKUP("/:break", "167.gif", "分手"), BIRTHDAY("/:cake", "168.gif", "生日"),
	ELECTRIC("/:li", "169.gif", "电"), BOMB("/:bome", "170.gif", "炸弹"), KNIFE("/:kn", "171.gif", "刀子"),
	FOOTBALL("/:footb", "172.gif", "足球"), LADYBUG("/:ladybug", "173.gif", "瓢虫"), FLYING("/:shit", "174.gif", "翔"),
	MOON("/:moon", "175.gif", "月亮"), SUNLIGHT("/:sun", "176.gif", "太阳"), GIFT("/:gift", "177.gif", "礼物"),
	HUG("/:hug", "178.gif", "抱抱"), THUMB("/:strong", "179.gif", "拇指"), BELITTLE("/:weak", "180.gif", "贬低"),
	HANDSHAKE("/:share", "181.gif", "握手"), SCISSORHANDS("/:v", "182.gif", "剪刀手 "), BOXING("/:@)", "183.gif", "抱拳"),
	SEDUCE("/:jj", "184.gif", "勾引"), FIST("/:@@", "185.gif", "拳头"), LITTLEFINGER("/:bad", "186.gif", "小拇指 "),
	THUMBEIGHT("/:lvu", "187.gif", "拇指八 "), INDEXFINGER("/:no", "188.gif", "食指"), OK("/:ok", "189.gif", "ok"),
	LOVERS("/:love", "190.gif", "情侣"), LOVE("/:<L>", "191.gif", "爱心"), BOUNCING("/:jump", "192.gif", "蹦哒"),
	TREMBLE("/:shake", "193.gif", "颤抖"), QI("/:<O>", "194.gif", "怄气"), DANCE("/:circle", "195.gif", "跳舞"),
	INADAZE("/:kotow", "196.gif", "发呆"), CARRYONONESBACK("/:turn", "197.gif", "背着"),
	REACHOUT("/:skip", "198.gif", "伸手"), PLAYHANDSOME("/:oY", "199.gif", "耍帅"),
	FIRECRACKER("[爆竹]", "200_firecracker.png", "爆竹"), FIREWORKS("[Fireworks]", "Fireworks.png", "烟花"),
	BLESSING("[Blessing]", "Blessing.png", "福"), PACKET("[Packet]", "202_Packet.png", "红包"),
	PARTY("[Party]", "203_Party.png", "庆祝"), RICH("[Rich]", "204_Rich.png", "發"), WORSHIP("[Worship]", "214.png", "合十"),
	Broken("[Broken]", "215_Broken.png", "裂开"), HURT("[Hurt]", "Hurt.png", "苦涩"), SIGH("[Sigh]", "Sigh.png", "叹气"),
	LETMESEE("[LetMeSee]", "LetMeSee.png", "让我看看"), AWESOME("[Awesome]", "Awesome.png", "666"),
	BORING("[Boring]", "Boring.png", "翻白眼"), WOW("[Wow]", "Wow.png", "哇"), MYBAD("[MyBad]", "MyBad.png", "打脸"),
	NOPROB("[NoProb]", "NoProb.png", "好的"), DOGE("[Doge]", "Doge.png", "旺柴"),
	RESPECT("[Respect]", "Respect.png", "社会社会"), EMM("[Emm]", "Emm.png", "Emm"), OMG("[OMG]", "OMG.png", "天啊"),
	SWEATS("[Sweats]", "Sweats.png", "汗"), GOFORIT("[GoForIt]", "GoForIt.png", "加油"),
	ONLOOKER("[Onlooker]", "Onlooker.png", "吃瓜"), YEAH("[Yeah!]", "Yeah.png", "耶"),
	CONCERNED("[Concerned]", "Concerned.png", "邹眉"), SMART("[Smart]", "Smart.png", "机智"),
	SMIRK("[Smirk]", "Smirk.png", "奸笑"), FACEPALM("[Facepalm]", "Facepalm.png", "捂脸"), HEY("[Hey]", "Hey.png", "嘿哈"),
	DUH("[Duh]", "Duh.png", "无语"), LETDOWN("[LetDown]", "LetDown.png", "失望"), TERROR("[Terror]", "Terror.png", "恐惧"),
	LOL("[Lol]", "Lol.png", "破涕为笑"), FLUSHED("[Flushed]", "Flushed.png", "脸红"), SICK("[Sick]", "Sick.png", "生病"),
	HAPPY3("[Happy]", "Happy.png", "笑脸");

	// 成员变量
	private String wxEmoji;
	private String img;
	private String name;

	// 构造方法
	WxEmoji(String wxEmoji, String img, String name) {
		this.wxEmoji = wxEmoji;
		this.img = img;
		this.name = name;
	}

	// 普通方法
	public static void initWXEmojiData() {
		String customerEmoji2imgDomainUrl = PropertiesUtils.read(CommonConstants.CUSTOMER_EMOJI2IMG_DOMAIN_URL);
		String customerDomainUrlFirst = PropertiesUtils.read(CommonConstants.CUSTOMER_DOMAIN_URL_FIRST);
		String customerDomainUrlSecond = PropertiesUtils.read(CommonConstants.CUSTOMER_DOMAIN_URL_SECOND);
		String customerDomainUrlThird = PropertiesUtils.read(CommonConstants.CUSTOMER_DOMAIN_URL_THIRD);
		for (WxEmoji c : WxEmoji.values()) {
			CommonConstants.WX_EMOJI2IMG_DATA.put(escapeExprSpecialWord(c.getWxEmoji()), "<img src=\""
					+ customerEmoji2imgDomainUrl + "/customerservice/common/img/wxface/" + c.getImg() + "\">");
			CommonConstants.WX_IMG2EMOJI_DATA.put(
					"<img src=\"" + customerDomainUrlFirst + "/customerservice/common/img/wxface/" + c.getImg() + "\">",
					c.getWxEmoji());
			CommonConstants.WX_IMG2EMOJI_DATA.put("<img src=\"" + customerDomainUrlSecond
					+ "/customerservice/common/img/wxface/" + c.getImg() + "\">", c.getWxEmoji());
			CommonConstants.WX_IMG2EMOJI_DATA.put(
					"<img src=\"" + customerDomainUrlThird + "/customerservice/common/img/wxface/" + c.getImg() + "\">",
					c.getWxEmoji());
		}
	}

	/**
	 * 转义正则特殊字符 （$()*+.[]?\^{},|）
	 * 
	 * @param keyword
	 * @return
	 */
	public static String escapeExprSpecialWord(String keyword) {
		if (keyword != null) {
			String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "?", "^", "{", "}", "|" };
			for (String key : fbsArr) {
				if (keyword.contains(key)) {
					keyword = keyword.replace(key, "\\" + key);
				}
			}
		}
		return keyword;
	}

	public String getWxEmoji() {
		return wxEmoji;
	}

	public void setWxEmoji(String wxEmoji) {
		this.wxEmoji = wxEmoji;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
