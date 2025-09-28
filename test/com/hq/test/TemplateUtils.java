package com.hq.test;

import com.weixin.util.WxUtil;
import com.weixin.vo.TemplateData;
import com.weixin.vo.TemplateMessage;
import com.weixin.vo.TemplateValue;

public class TemplateUtils {
	public static void main(String[] args) {
		TemplateMessage templateMessage = new TemplateMessage();
		TemplateData data = new TemplateData();
		templateMessage.setTemplate_id("DrgTHetSXbMKk_y76bMlV3OIQzDRE05zCSMG0f5KTSs");
		templateMessage.setTouser("olSMft2Xip7Lsxc317_qeoHmxiv4");
		
		data.setFirst(new TemplateValue("尊敬的客户，您好",""));
		data.setKeyword1(new TemplateValue("我们甘竹厂家",""));
		data.setKeyword2(new TemplateValue("2021-01-09",""));
		data.setRemark(new TemplateValue("有司机过来，是否需要入货？",""));
		templateMessage.setData(data);
		System.out.println(WxUtil.sendNote(templateMessage));
	}
}
