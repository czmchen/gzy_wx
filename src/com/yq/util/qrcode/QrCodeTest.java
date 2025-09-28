package com.yq.util.qrcode;

import java.util.UUID;
 
public class QrCodeTest {
 
	public static void main(String[] args) throws Exception {
		// 存放在二维码中的内容
		String text = "http://ganzhuyou.cn/page/code2Order.html";
		// 嵌入二维码的图片路径
		String imgPath = "D:/data/qrCode/ganzhu_logon.jpg";
		// 生成的二维码的路径及名称
		String destPath = "D:/data/qrCode/ganzhuyouCode2Order.jpg";
		//生成二维码
		QRCodeUtil.encode(text, imgPath, destPath, true);
		// 解析二维码
		String str = QRCodeUtil.decode(destPath);
		// 打印出解析出的内容
		System.out.println(str);
 
	}
 
}