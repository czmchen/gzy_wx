package com.yq.dao;

import org.apache.ibatis.annotations.Param;

import com.yq.entity.Dictionary;


public interface DictionaryDao {
	
	public Dictionary getDataByKey(@Param(value = "dicKey")String dicKey);
	
	public int update(Dictionary dictionary);
	
}
