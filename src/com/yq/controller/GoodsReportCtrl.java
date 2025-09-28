package com.yq.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.weixin.util.PropertiesUtils;
import com.weixin.util.StringUtil;
import com.yq.constants.CommonConstants;
import com.yq.entity.Goods;
import com.yq.entity.GoodsReport;
import com.yq.entity.User;
import com.yq.service.GoodsReportService;
import com.yq.service.GoodsService;
import com.yq.service.UserService;
import com.yq.util.JsonUtil;
import com.yq.util.qrcode.QRCodeUtil;
import com.yq.vo.GoodsReportVo;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.Manufacturer;
import eu.bitwalker.useragentutils.UserAgent;

@Controller
@RequestMapping
public class GoodsReportCtrl {
	@Autowired
	private RedisTemplate<String, ?> redisTemplate;
	private final static Logger logger = Logger.getLogger(GoodsReportCtrl.class);
	@Autowired
	private UserService userService;
	@Autowired
	GoodsReportService goodsReportService;
	@Autowired
	GoodsService goodsService;

	private final String key = CommonConstants.REDIS_GOODS_REPORT_UUID_DATA_CONSTANTS;

	@RequestMapping(value = "/goodsReport/go2LoginPage.html")
	public String go2LoginPage() {
		return "goodsReportManager/login";
	}

	@RequestMapping(value = "/goodsReport/go2Report.html")
	public String go2Report() {
		return "goodsReportManager/reportView";
	}

	@RequestMapping(value = "/goodsReport/index.html")
	public String go2Index() {
		return "goodsReportManager/index";
	}

	@RequestMapping(value = "/goodsReport/go2index.html")
	public ModelAndView index(String uuid, HttpSession session, Model model) {
		ModelAndView ml = new ModelAndView();
		Object value = redisTemplate.opsForHash().get(key, uuid);
		if (value == null) {
			ml.setViewName("goodsReportManager/login");
			return ml;
		}

		User objUser = JsonUtil.json2Entity((String) value, User.class);
		if (StringUtil.isRealEmpty(uuid) || objUser == null) {
			ml.setViewName("goodsReportManager/login");
			return ml;
		}

		session.setAttribute("GoodsReportUUID", uuid);
		session.setAttribute("GoodsReportUserData", objUser);
		ml.setViewName("goodsReportManager/index");
		return ml;
	}

	@RequestMapping(value = "/goodsReport/customerLogin.html")
	public ModelAndView customerLogin(String uuid, HttpServletResponse response, HttpSession session) {
		ModelAndView ml = new ModelAndView();
		try {
			if (!redisTemplate.opsForHash().hasKey(key, uuid)) {
				return null;
			}
			String openId = (String) session.getAttribute("oppen_id");
			User userData = userService.getUserByOpenId(openId);
			logger.error("customerLogin openId:"+openId+"||json:"+JsonUtil.obj2JSON(userData));
			if (userData != null && userData.getGoodsReportMg() == 1) {
				redisTemplate.opsForHash().put(key, uuid, JsonUtil.obj2JSON(userData));
				ml.setViewName("page/go2SubResult.html?operResult=1&returnURL=&operType=close&cu=");
			} else {
				ml.setViewName("page/go2SubResult.html?operResult=0&returnURL=&operType=close&errorMsg=您不是客服，登陆失败!&cu=");
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return ml;
	}

	@ResponseBody
	@RequestMapping(value = "/goodsReport/qrcode.html")
	public String customerQrcode(HttpServletRequest request, HttpSession session) {
		String qrcodeUUID = UUID.randomUUID().toString();
		try {
			redisTemplate.opsForHash().put(key, qrcodeUUID, null);

			String qrCodeLoacation = request.getSession().getServletContext().getRealPath("/") + "goodsReportManager/common/img/qrcode/";
			String text = "https://ganzhuyou.cn/goodsReport/customerLogin.html?uuid=" + qrcodeUUID;// 存放在二维码中的内容
			String destPath = qrCodeLoacation + qrcodeUUID + ".jpg";
			logger.debug(destPath);

			QRCodeUtil.encode(text, CommonConstants.QR_CODE_GANZHU_IMGPATH, destPath, true);// 生成二维码
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
		return qrcodeUUID;
	}

	@ResponseBody
	@RequestMapping(value = "/goodsReport/checkLogin.html")
	public String checkLogin(String uuid) {
		try {
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

	@ResponseBody
	@RequestMapping(value = "/goodsReport/submitForm.html", method = RequestMethod.POST)
	public String submitForm(GoodsReportVo goodsReportVo, BindingResult bindingResult, Model model, HttpServletResponse response, HttpServletRequest request) {
		try {
			goodsReportService.update(goodsReportVo);
			return "1";
		} catch (Exception e) {
			logger.error(e);
			return "0";
		}
	}

	@ResponseBody
	@RequestMapping(value = "/goodsReport/getPreviewData.html", method = RequestMethod.GET)
	public void getPreviewData(String fileDeskName, HttpServletResponse response, HttpServletRequest request) {
		if (fileDeskName == null) {
			return;
		}
		try {
			String fullFilePathAndFileName = PropertiesUtils.read("customer.file.location") + "/upload/" + fileDeskName;
			outputByNIO(request, response, fullFilePathAndFileName, fileDeskName, null);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/goodsReport/deleteById.html", method = RequestMethod.POST)
	public String deleteLogicById(String id, HttpServletResponse response, HttpServletRequest request) {
		try {
			goodsReportService.deleteLogicById(id);
			return "1";
		} catch (Exception e) {
			logger.error(e);
			return "0";
		}
	}

	@RequestMapping(value = "/goodsReport/list.html", method = RequestMethod.POST)
	public void list(GoodsReportVo goodsReportVo, BindingResult bindingResult, Model model, HttpServletResponse response, HttpServletRequest request) {
		try {
			List<GoodsReport> lstGoodsReport = goodsReportService.search(goodsReportVo);
			returnJson(response, request, JsonUtil.list2JSON(lstGoodsReport));
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	
	@RequestMapping(value = "/goodsReport/checkExistFile.html", method = RequestMethod.GET)
	public void checkExistFile(String inspReportStr , HttpServletResponse response, HttpServletRequest request) {
		try {
			String[] inspReports = inspReportStr.split(",");
			List<GoodsReport> lstGoodsReport = goodsReportService.listByDiyColum(inspReports);
			returnJson(response, request, JsonUtil.list2JSON(lstGoodsReport));
		} catch (Exception e) {
			logger.error(e);
		}
	}

	@RequestMapping(value = "/goodsReport/listGoods.html", method = RequestMethod.POST)
	public void listAllGoods(String searchKey, HttpServletResponse response, HttpServletRequest request) {
		try {
			Goods goods = new Goods();
			goods.setStatus(1);
			List<Goods> lstGoods = goodsService.listOrderJoinReport(goods);
			returnJson(response, request, JsonUtil.list2JSON(lstGoods));
		} catch (Exception e) {
			logger.error(e);
		}
	}

	@RequestMapping(value = "/goodsReport/download.html", method = { RequestMethod.GET, RequestMethod.POST })
	public void download(Long id, HttpServletResponse response, HttpServletRequest request) {
		try {
			GoodsReport goodsReport = new GoodsReport();
			goodsReport.setId(id);
			List<GoodsReport> lstGoodsReport = goodsReportService.list(goodsReport);
			if (lstGoodsReport != null && lstGoodsReport.size() > 0) {
				GoodsReport objGoodsReport = lstGoodsReport.get(0);
				String fileOrgName = objGoodsReport.getInspReport();// 原来文件名称
				String fileDeskName = objGoodsReport.getReportDeskFile();// 磁盘存储的名称
				String fullFilePathAndFileName = PropertiesUtils.read("customer.file.location") + "/upload/" + fileDeskName;
				outputByNIO(request, response, fullFilePathAndFileName, fileOrgName, null);
			}
		} catch (IOException e) {
			logger.error(e);
		}
	}
	
	
	@RequestMapping(value = "/goodsReport/downloadFileByDeskFullName.html", method = { RequestMethod.GET, RequestMethod.POST })
	public void downloadFileByDeskFullName(String fileName,String fileOrgName, HttpServletResponse response, HttpServletRequest request) {
		try {
			
			if (fileName != null) {
				String fileDeskName = fileName;// 磁盘存储的名称
				String fullFilePathAndFileName = PropertiesUtils.read("customer.file.location") + "/upload/" + fileDeskName;
				outputByNIO(request, response, fullFilePathAndFileName, fileOrgName, null);
			}
		} catch (IOException e) {
			logger.error(e);
		}
	}

	private void outputByNIO(HttpServletRequest request, HttpServletResponse response, String fullFilePathAndFileName, String fileRealName, String mimeType) throws IOException {
		File file = new File(fullFilePathAndFileName);

		// set response headers
		String filename = null;
		UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
		Browser browser = userAgent.getBrowser();
		boolean flag = browser.getManufacturer().equals(Manufacturer.MICROSOFT);
		int j = browser.compareTo(Browser.IE);

		// 判断是否为microsoft并且j是否大于0，若都满足，则为IE
		if (j >= 0 && flag) {
			filename = URLEncoder.encode(fileRealName, "UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + filename);
		} else {
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileRealName.getBytes("utf-8"), "iso8859-1"));
		}
		response.addHeader("Content-Length", "" + file.length());
		response.setContentType((mimeType != null) ? mimeType : "application/octet-stream");

		// read and write file
		ServletOutputStream op = response.getOutputStream();
		// 128 KB buffer
		int bufferSize = 131072;
		FileInputStream fileInputStream = new FileInputStream(file);
		FileChannel fileChannel = fileInputStream.getChannel();
		// 6x128 KB = 768KB byte buffer
		ByteBuffer bb = ByteBuffer.allocateDirect(786432);
		byte[] barray = new byte[bufferSize];
		int nRead, nGet;

		try {
			while ((nRead = fileChannel.read(bb)) != -1) {
				if (nRead == 0)
					continue;
				bb.position(0);
				bb.limit(nRead);
				while (bb.hasRemaining()) {
					nGet = Math.min(bb.remaining(), bufferSize);
					bb.get(barray, 0, nGet);// read bytes from disk
					op.write(barray);// write bytes to output
				}
				bb.clear();
			}
		} catch (IOException e) {
			logger.error(e);
		} finally {
			bb.clear();
			fileChannel.close();
			fileInputStream.close();
		}
	}

	public static final String CONTENTTYPE_JSON = "application/json; charset=UTF-8";
	public static final String CONTENTTYPE_HTML = "text/html; charset=UTF-8";
	public static final String ACCEPT = "accept";
	public static final String JSON = "json";

	public void returnJson(HttpServletResponse response, HttpServletRequest request, String json) {
		try {
			String contentType = CONTENTTYPE_JSON;
			if (request != null) {
				String accept = request.getHeader(ACCEPT);
				if (accept != null && !accept.contains(JSON)) {
					contentType = CONTENTTYPE_HTML;
				}
			}
			response.setContentType(contentType);
			response.getWriter().write(json);
			response.getWriter().flush();
		} catch (IOException e) {
			logger.error("returnJson is error!", e);
		}
	}
}
