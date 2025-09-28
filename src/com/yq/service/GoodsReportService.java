package com.yq.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yq.dao.GoodsDao;
import com.yq.dao.GoodsReportDao;
import com.yq.entity.GoodsReport;
import com.yq.vo.GoodsReportVo;

@Service
public class GoodsReportService {
	@Autowired
	GoodsReportDao goodsReportDao;
	@Autowired
	private GoodsDao goodsDao;

	public int insert(GoodsReport goodsReport) {
		return goodsReportDao.insert(goodsReport);
	}

	public void insert(GoodsReportVo goodsReportVo) throws Exception {
		try {
			GoodsReport goodsReport = new GoodsReport();
			Map<String, Object> updateGoodsPar = new HashMap<String, Object>();
			updateGoodsPar.put("bar_num", goodsReportVo.getBarNum());
			updateGoodsPar.put("goods_code", goodsReportVo.getGoodsCode());

			BeanUtils.copyProperties(goodsReport, goodsReportVo);

			goodsDao.updateGoodsReport(updateGoodsPar);
			for (int i = 0; i < goodsReportVo.getFileOrgName().length; i++) {
				String fileOrgName = goodsReportVo.getFileOrgName()[i];
				String filedeskName = goodsReportVo.getFiledeskName()[i];
				String pdf2jpgData = "";
				if (goodsReportVo.getFileOrgName().length == 1) {
					for (int j = 0; j < goodsReportVo.getPdf2jpgDataArray().length; j++) {
						pdf2jpgData += goodsReportVo.getPdf2jpgDataArray()[j];
						if (j < (goodsReportVo.getPdf2jpgDataArray().length - 1)) {
							pdf2jpgData += ",";
						}
					}
				} else {
					pdf2jpgData = goodsReportVo.getPdf2jpgDataArray()[i];
				}
				String fileOrgNameFull = fileOrgName.substring(0, fileOrgName.lastIndexOf("."));

				String[] fileSplits = fileOrgNameFull.split("-");
				String batchNum = fileSplits[1].substring(0, fileSplits[1].length()-1);
				String barNum = fileSplits[1].substring(fileSplits[1].length()-1);
				goodsReport.setLotNum(fileSplits[0]);// 分解批号
				goodsReport.setBatchNum(batchNum);// 分解批次
				goodsReport.setBarNum(barNum);// 分解条码后三位
				goodsReport.setReportDeskFile(filedeskName);
				goodsReport.setInspReport(fileOrgName);
				goodsReport.setPdf2jpgData(pdf2jpgData);
				goodsReportDao.insert(goodsReport);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public void update(GoodsReportVo goodsReportVo) throws Exception {
		goodsReportDao.deleteLogic(goodsReportVo.getFileOrgName());// 先逻辑删除掉
		this.insert(goodsReportVo);
	}

	public int update(GoodsReport goodsReport) {
		return goodsReportDao.update(goodsReport);
	}

	public List<GoodsReport> list(GoodsReport goodsReport) {
		return goodsReportDao.list(goodsReport);
	}

	public List<GoodsReport> search(GoodsReportVo goodsReportVo) {
		return goodsReportDao.search(goodsReportVo);
	}

	public List<GoodsReport> listByDiyColum(String[] inspReports) {
		return goodsReportDao.listByDiyColum(inspReports);
	}

	public int deleteLogicById(String id) {
		return goodsReportDao.deleteLogicById(id);
	}

}
