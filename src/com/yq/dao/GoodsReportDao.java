package com.yq.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yq.entity.GoodsReport;

public interface GoodsReportDao {

	public int insert(GoodsReport goodsReport);

	public int update(GoodsReport goodsReport);

	public int deleteLogic(@Param(value = "inspReports")  String[] inspReports);
	
	public int deleteLogicById(@Param(value = "id") String id);

	public List<GoodsReport> list(GoodsReport goodsReport);
	
	public List<GoodsReport> search(GoodsReport goodsReport);
	
	public List<GoodsReport> listByDiyColum(@Param(value = "inspReports") String[] inspReports);
	
}
