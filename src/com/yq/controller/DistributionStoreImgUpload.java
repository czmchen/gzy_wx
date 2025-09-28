package com.yq.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.weixin.util.SpringUtil;
import com.yq.service.DistributionService;
import com.yq.vo.UploadDetailVo;
import com.yq.vo.UploadVo;

@WebServlet("/distribution/storeImgUpload")
public class DistributionStoreImgUpload extends UploadBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger logger = Logger.getLogger(DistributionStoreImgUpload.class);
		
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			this.getProcess(req, resp);
		} catch (Exception e) {
			logger.error(e);
		}
	}


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			UploadVo objUploadVo = this.upload("image","distribution/images", req, resp);
			List<UploadDetailVo> lstUploadDetailVo = objUploadVo.getLstUploadDetailVo();
			if(lstUploadDetailVo!=null&&lstUploadDetailVo.size()!=0) {
				UploadDetailVo objUploadDetailVo = lstUploadDetailVo.get(0);//因为只有一个，直接获取
				objUploadDetailVo.setFileFloder("upload/distribution/images");
				String saveStoreImg2RMWXUserCust = ((DistributionService) SpringUtil.getBean("distributionService")).saveStoreImg2RMWXUserCust(objUploadVo);
				returnJson(resp, req, saveStoreImg2RMWXUserCust);
			}else {
				return ;
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
