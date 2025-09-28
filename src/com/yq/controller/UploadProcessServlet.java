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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.weixin.util.PropertiesUtils;
import com.yq.constants.CommonConstants;
import com.yq.util.JsonUtil;
import com.yq.util.Pdf2JpgUtil;
import com.yq.vo.UploadDetailVo;
import com.yq.vo.UploadVo;

@WebServlet("/uploadFile")
public class UploadProcessServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final static Logger logger = Logger.getLogger(UploadProcessServlet.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
		long start_time = System.currentTimeMillis();
		String tempPath = PropertiesUtils.read("customer.file.location") + "/upload";// 获取上传文件临时保存路径
		File file = new File(tempPath);
		if (!file.exists() && !file.isDirectory()) {// 目录不存在或不是一个目录
			file.mkdirs();
		}
		if (!ServletFileUpload.isMultipartContent(req)) {// 普通表单
			return;
		}
		DiskFileItemFactory factory = new DiskFileItemFactory();// 创建DiskFileItemFactory工厂类
		ServletFileUpload upload = new ServletFileUpload(factory);// 创建一个文件上传解析器
		String uploadToken = null;
		List<FileItem> list = null;
		try {
			list = upload.parseRequest(req);// 利用文件上传解析器解析数据并存放到list中
			for (FileItem item : list) {
				if (item.isFormField()) {// 普通输入框中的数据
					String name = item.getFieldName();// 输入框的name值
					String value = item.getString("UTF-8"); // 输入框的值
					if("uploadToken".equals(name)) {
						uploadToken = value;
					}
					System.out.println(name + " : " + value);
				}
			}
		} catch (Exception e) {
		}
		List<UploadDetailVo> lstUploadDetailVo = new ArrayList<UploadDetailVo>();
		UploadVo objUploadVo = new UploadVo();
		objUploadVo.setLstUploadDetailVo(lstUploadDetailVo);
		objUploadVo.setToken(uploadToken);
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
						// 获取文件名
						fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
						String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
						if(!"pdf".equals(suffix)) {
							throw new Exception("只能上传pdf文件类型");
						}
						String uuid = UUID.randomUUID().toString();
						String fileDiskName = uuid + "." + suffix;
						objUploadDetailVo.setFileOrgName(fileName);
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

						List<String> pdf2jpgData = Pdf2JpgUtil.convert(fileDiskName);//pdf转换为jpg
						objUploadDetailVo.setPdf2jpgData(JsonUtil.list2JSON(pdf2jpgData));
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
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// 获取进度数据
		String uploadToken = req.getParameter("uploadToken");
		// 返回
		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter pw = resp.getWriter();
		pw.write(JsonUtil.obj2JSON(CommonConstants.UPLOAD_FILE_STATUS_DATA.get(uploadToken)) + "");
		pw.flush();
		pw.close();

	}
}
