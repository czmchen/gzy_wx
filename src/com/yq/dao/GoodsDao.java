package com.yq.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yq.entity.Goods;

public interface GoodsDao {
	public int insert(Map<String, Object> map);

	public int update(Map<String, Object> map);

	public int upstatus(Map<String, Object> map);
	
	public int updateGoodsReport(Map<String, Object> map);

	public int upisrec(Map<String, Object> map);
	
	public List<Goods> list(Goods goods);
	
	public List<Goods> listOrderByName(Goods goods);
	
	public List<Goods> listOrderJoinReport(Goods goods);

	public int count(Goods goods);

	public List<Goods> listById(Goods goods);

	public List<Goods> listByIds(@Param(value = "ids") String[] ids);
	
	public List<Goods> searchByPar(@Param(value = "searchKey") String searchKey);
	
	public List<Goods> listAllGoodsIdAndImg();
	
	public List<Goods> getBuildInMemberGoodsCar(@Param(value = "custID") String custID);
	
	public List<Goods> getGoodsCar();
}
