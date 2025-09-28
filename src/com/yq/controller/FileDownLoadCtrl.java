package com.yq.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.weixin.util.PropertiesUtils;
import com.weixin.util.StringUtil;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.Manufacturer;
import eu.bitwalker.useragentutils.UserAgent;

@Controller
@RequestMapping()
public class FileDownLoadCtrl {
	
	private final static Logger logger = Logger.getLogger(FileDownLoadCtrl.class);

	@RequestMapping(value = "cloud/download.html", method = { RequestMethod.GET, RequestMethod.POST })
	public void download(HttpServletResponse response, HttpServletRequest request){
		String fileName = request.getParameter("fileName");
		String fileType = request.getParameter("fileType");
		String suffix = request.getParameter("suffix");
		
		StringBuffer fileNameBuffer = new StringBuffer();
		StringBuffer fullFilePathAndFileName = new StringBuffer(PropertiesUtils.read("customer.file.location"));
		fullFilePathAndFileName.append("/");
		if(!StringUtil.isRealEmpty(fileType)) {
			fullFilePathAndFileName.append(fileType).append("/");
		}
		
		if(!StringUtil.isRealEmpty(fileName)) {
			fileNameBuffer.append(fileName);
		}
		if(!StringUtil.isRealEmpty(suffix)) {
			fileNameBuffer.append(".").append(suffix);
		}
		fullFilePathAndFileName.append(fileNameBuffer);
		
		try {
			outputByNIO(request, response, fullFilePathAndFileName.toString(), fileNameBuffer.toString(), null);
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

}
