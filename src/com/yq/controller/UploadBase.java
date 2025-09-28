package com.yq.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.weixin.util.PropertiesUtils;
import com.weixin.util.StringUtil;
import com.yq.constants.CommonConstants;
import com.yq.util.JsonUtil;
import com.yq.vo.UploadDetailVo;
import com.yq.vo.UploadVo;

public class UploadBase  extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger logger = Logger.getLogger(UploadBase.class);

	protected UploadVo upload(String checkContentType,String fileFolder,HttpServletRequest req, HttpServletResponse resp) {
		long start_time = System.currentTimeMillis();
		String tempPath = PropertiesUtils.read("customer.file.location") + "/upload/"+fileFolder;// 获取上传文件临时保存路径
		File file = new File(tempPath);
		if (!file.exists() && !file.isDirectory()) {// 目录不存在或不是一个目录
			file.mkdirs();
		}
		if (!ServletFileUpload.isMultipartContent(req)) {// 普通表单
			return null;
		}
		DiskFileItemFactory factory = new DiskFileItemFactory();// 创建DiskFileItemFactory工厂类
		ServletFileUpload upload = new ServletFileUpload(factory);// 创建一个文件上传解析器
		String uploadToken = null;
		List<FileItem> list = null;
		UploadVo objUploadVo = new UploadVo();
		try {
			list = upload.parseRequest(req);// 利用文件上传解析器解析数据并存放到list中
			for (FileItem item : list) {
				if (item.isFormField()) {// 普通输入框中的数据
					String name = item.getFieldName();// 输入框的name值
					String value = item.getString("UTF-8"); // 输入框的值
					if("uploadToken".equals(name)) {
						uploadToken = value;
						objUploadVo.setToken(uploadToken);
					}if("def1".equals(name)) {
						objUploadVo.setDef1(value);
					}if("def2".equals(name)) {
						objUploadVo.setDef2(value);
					}if("def3".equals(name)) {
						objUploadVo.setDef3(value);
					}if("def4".equals(name)) {
						objUploadVo.setDef4(value);
					}if("def5".equals(name)) {
						objUploadVo.setDef5(value);
					}if("def6".equals(name)) {
						objUploadVo.setDef6(value);
					}if("def7".equals(name)) {
						objUploadVo.setDef7(value);
					}if("def8".equals(name)) {
						objUploadVo.setDef8(value);
					}if("def9".equals(name)) {
						objUploadVo.setDef9(value);
					}if("def10".equals(name)) {
						objUploadVo.setDef10(value);
					}
					System.out.println(name + " : " + value);
				}
			}
		} catch (Exception e) {
		}
		List<UploadDetailVo> lstUploadDetailVo = new ArrayList<UploadDetailVo>();
		objUploadVo.setLstUploadDetailVo(lstUploadDetailVo);
		CommonConstants.UPLOAD_FILE_STATUS_DATA.put(uploadToken, objUploadVo);
		try {
			// 解决上传文件的中文乱码
			upload.setHeaderEncoding("UTF-8");

			// 循环所有数据
			for (FileItem item : list) {
				InputStream is = null;
				FileOutputStream fos = null;
				UploadDetailVo objUploadDetailVo = new UploadDetailVo();
				try {
					// 判断数据来源
					if (!item.isFormField()) {// 文件 获取上传文件的文件路径(不同浏览器不同，有的是文件名，有的是全路径)
						long uploadStartTime = System.currentTimeMillis();
						lstUploadDetailVo.add(objUploadDetailVo);
						String fileName = item.getName();
						if (fileName == null || "".equals(fileName.trim())) {
							continue;
						}
						
						if(!StringUtil.isRealEmpty(checkContentType)&&item.getContentType().indexOf(checkContentType)==-1){//非普通输入框中的数据,并且非在上传的文件类型内
							throw new Exception("只能上传"+checkContentType+"文件类型");
						}
						
						// 获取文件名
						fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
						String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
						String uuid = UUID.randomUUID().toString();
						String fileDiskName = uuid + "." + suffix;
						objUploadDetailVo.setFileOrgName(fileName);
						objUploadDetailVo.setSuffix(suffix);
						objUploadDetailVo.setFiledeskName(fileDiskName);
						// 获取上传文件的输入流
						is = item.getInputStream();
						// 创建一个文件输出流
						fos = new FileOutputStream(tempPath + "/" + fileDiskName);
						// 创建一个缓存区
						byte[] buff = new byte[1024];

						int len = 0;
						while ((len = is.read(buff)) > 0) {// 循环读取数据
							fos.write(buff, 0, len);// 使用文件输出流将缓存区的数据写入到指定的目录中
						}
						objUploadDetailVo.setSuccFlag(true);
						long uploadEndTime = System.currentTimeMillis();
						objUploadDetailVo.setCostTime(uploadEndTime - uploadStartTime);
					}
				} catch (Exception e) {
					objUploadDetailVo.setErrorMsg(e.getMessage());
					logger.error(e);
				} finally {
					if (fos != null) {
						fos.close();// 关闭输出流
						if (item != null) {
							item.delete();// 删除临时文件
						}
					}
					if (is != null) {
						is.close();// 关闭输出流
					}
				}
			}
			long end_time = System.currentTimeMillis();
			System.out.println("文件上传成功,共用时" + (end_time - start_time) + "ms");
			objUploadVo.setCostTime(end_time - start_time);
//			req.setAttribute("time", lt);
//			req.getRequestDispatcher("goodsReportManager/upload.jsp").forward(req, resp);
		} catch (Exception e) {
			logger.error(e);
		}
		objUploadVo.setUploadFinish(true);
		return objUploadVo;
	}

	protected void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 获取进度数据
		String uploadToken = req.getParameter("uploadToken");
		// 返回
		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter pw = resp.getWriter();
		pw.write(JsonUtil.obj2JSON(CommonConstants.UPLOAD_FILE_STATUS_DATA.get(uploadToken)) + "");
		pw.flush();
		pw.close();
		returnJson(resp, req, JsonUtil.obj2JSON(CommonConstants.UPLOAD_FILE_STATUS_DATA.get(uploadToken)) + "");
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
