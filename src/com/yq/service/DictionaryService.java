package com.yq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yq.dao.DictionaryDao;
import com.yq.entity.Dictionary;

@Service
public class DictionaryService {
	@Autowired
	DictionaryDao dictionaryDao;

	public Dictionary getDataByKey(String dicKey) {
		return dictionaryDao.getDataByKey(dicKey);
	}
	
	public int update(Dictionary dictionary) {
		return dictionaryDao.update(dictionary);
	}
	
}
