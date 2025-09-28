package com.hq.test;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import javax.imageio.ImageIO;

import com.weixin.util.HttpClientManager;
import com.weixin.util.PropertiesUtils;

public class TestConnect {

	public static void main(String[] args) throws Exception {
		Map data = new HashMap();
		data.put("1", "222");
		System.out.println(data.size());
		System.out.println(2000/1000);String str = null;
		if(str!=null&&str.equals(null)) {
			System.out.println("1111111111111");
		}
//		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	    System.out.println(sd.format(new Date(1559491200000l)));
//		System.out.println(new Date().getTime());
//		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(1659767724293l)));
//		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-08-06 10:35:24").getTime());
//		UploadDetailVo objUploadDetailVo = new UploadDetailVo();
//		objUploadDetailVo.setFiledeskName("12121");
//		System.out.println(JsonUtil.obj2JSON(objUploadDetailVo));
//		System.out.println(new Date().getTime());
//		System.out.println("dasjdkajdlajl\nsadada8288\n90iiikaj".replaceAll("\n", ""));
//		
//		
//		System.out.println(MD5Util.MD5Encode("/web/mapComponents/locationPicker/v/index.html?search=1&type=1&referer=ganzhuyou&key=ZNFBZ-5CS66-NIFSJ-MZINS-FWNN7-P7BT3&coord=40.022964,116.319723IukSWyJJCAFLwX1CB10X9qxaSSpwtXu","UTF-8"));
//		System.out.println(MyMD5Util.getMD5("/web/mapComponents/locationPicker/v/index.html?search=1&type=1&referer=ganzhuyou&key=ZNFBZ-5CS66-NIFSJ-MZINS-FWNN7-P7BT3&coord=40.022964,116.319723IukSWyJJCAFLwX1CB10X9qxaSSpwtXu"));
//		System.out.println(MyMD5Util.encrypt("/web/mapComponents/locationPicker/v/index.html?search=1&type=1&referer=ganzhuyou&key=ZNFBZ-5CS66-NIFSJ-MZINS-FWNN7-P7BT3&coord=40.022964,116.319723IukSWyJJCAFLwX1CB10X9qxaSSpwtXu"));
//		System.out.println(com.hq.test.MD5Util.MD5("/web/mapComponents/locationPicker/v/index.html?search=1&type=1&referer=ganzhuyou&key=ZNFBZ-5CS66-NIFSJ-MZINS-FWNN7-P7BT3&coord=40.022964,116.319723IukSWyJJCAFLwX1CB10X9qxaSSpwtXu"));
//		
//		System.out.println(new SimpleDateFormat("yyyy-mm-dd HH:mm:ss").parse("2023-04-27 10:32:32").getTime());
//		System.out.println("250210924112839|_|100527".split("\\|\\_\\|")[0]);
//		Float floa = 0.0f;
//		System.out.println(floa.floatValue());
//		float f;
//		Double dol = 13.1;
//		Double dol1 = 13.1;
//		Double dol2 = 3.0;
//		float fol = 3.0f;
//		System.out.println(dol2 == fol);
//		System.out.println(Math.ceil(0.0));
//		System.out.println(System.currentTimeMillis());
//		
//		String s ="20210717205741429|_|205809";
//		System.out.println(s.split("\\|\\_\\|")[0]);
//		
//		System.out.println(System.currentTimeMillis());
		
		
		
//System.out.println(MyConfig.class.getResource("/")+"apiclient_cert.p12");
//System.out.println(System.currentTimeMillis());
		/*
		 * String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver"; String
		 * dbURL = "jdbc:sqlserver://202.104.28.102:3355;DatabaseName=DefWeixin"; String
		 * userName = "Dfuserwxdata"; String userPwd = "Df0757!@#123."; try {
		 * Class.forName(driverName); System.out.println("加载驱动成功！"); } catch (Exception
		 * e) { e.printStackTrace(); System.out.println("加载驱动失败！"); } try { Connection
		 * dbConn = DriverManager.getConnection(dbURL, userName, userPwd);
		 * System.out.println("连接数据库成功！" + dbConn); dbConn.close(); } catch (Exception
		 * e) { e.printStackTrace(); System.out.print("SQL Server连接失败！"); }
		 */


		/*
		 * String regEx =
		 * ".*[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\\\]+.*";
		 * Pattern pattern = Pattern.compile(regEx); Matcher matcher =
		 * pattern.matcher("/::>"); StringBuffer sb = new StringBuffer(); while
		 * (matcher.find()) { String name = matcher.group();// 键名 //
		 * matcher.appendReplacement(sb, value); System.out.println("sb = " +
		 * sb.toString()); } // 最后还得要把尾串接到已替换的内容后面去，这里尾串为“，欢迎下次光临！”
		 * matcher.appendTail(sb);
		 */
		/*
		 * System.out.println(contentEmoji2Img(str));
		 * 
		 * System.out.println(str.replaceAll("\\/::", "99999"));
		 * System.out.println("\\/::\\(+");
		 */
//		download();
//		
//		String imgName = "O4-MIBgjvUc9S03MZsNQ25JFVsUfR8XcaPfcyoXC5Y2Nr4gr82rTudxRCLrtPF7f";
//		System.out.println( "<img id='"+imgName+"' src='${pageContext.request.contextPath}/cloud/download.html?fileName="+imgName+"_abbr&fileType=images&suffix=jpg' imgOrgSrc='${pageContext.request.contextPath}/cloud/download.html?fileName="+imgName+"&fileType=images&suffix=jpg' title='点击查看原图' "
//				+ " onclick='displayOrgImg('"+imgName+"');'>");
//		imgDetail("123.png");
	}

	private static String contentEmoji2Img(String strContent) {
		Map<String, String> wxEmoji2Img = com.weixin.constants.CommonConstants.WX_EMOJI2IMG_DATA;
		for (String key : wxEmoji2Img.keySet()) {
			strContent = strContent.replaceAll("\\" + key, Matcher.quoteReplacement(wxEmoji2Img.get(key)));
		}
		return strContent;
	}

	private static void download() {
		try {
			HttpClientManager.downloadImg("http://mmbiz.qpic.cn/mmbiz_jpg/lTIomianHwbmDoibBYtRFOz5K6oFbicicxciaSbib7ibANLWE1hntEv4uzB0PgWEM4fs9YHWTSHW1JoGZaiaKj4oRfMyKw/0","O4-MIBgjvUc9S03MZsNQ25JFVsUfR8XcaPfcyoXC5Y2Nr4gr82rTudxRCLrtPF7f.jpg");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void imgDetail(String fileFullName) throws IOException {
		String fullFilePath = PropertiesUtils.read("customer.file.location") + "/images/";
		BufferedImage bi = ImageIO.read(new File(fullFilePath + fileFullName));

		int width = bi.getWidth();
		int height = bi.getHeight();

		System.out.println("width:" + width);
		System.out.println("height:" + height);

	}

	/**
	 * 实现图像的等比缩放
	 * 
	 * @param source
	 * @param targetW
	 * @param targetH
	 * @return
	 */
	private static BufferedImage resize(BufferedImage source, int targetW, int targetH) {
		// targetW，targetH分别表示目标长和宽
		int type = source.getType();
		BufferedImage target = null;
		double sx = (double) targetW / source.getWidth();
		double sy = (double) targetH / source.getHeight();
		// 这里想实现在targetW，targetH范围内实现等比缩放。如果不需要等比缩放
		// 则将下面的if else语句注释即可
		if (sx < sy) {
			sx = sy;
			targetW = (int) (sx * source.getWidth());
		} else {
			sy = sx;
			targetH = (int) (sy * source.getHeight());
		}
		if (type == BufferedImage.TYPE_CUSTOM) { // handmade
			ColorModel cm = source.getColorModel();
			WritableRaster raster = cm.createCompatibleWritableRaster(targetW, targetH);
			boolean alphaPremultiplied = cm.isAlphaPremultiplied();
			target = new BufferedImage(cm, raster, alphaPremultiplied, null);
		} else
			target = new BufferedImage(targetW, targetH, type);
		Graphics2D g = target.createGraphics();
		// smoother than exlax:
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
		g.dispose();
		return target;
	}

}
