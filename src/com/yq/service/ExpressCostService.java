package com.yq.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yq.dao.ExpressCostDao;
import com.yq.entity.ExpressCost;

@Service
public class ExpressCostService {
	@Autowired
	private ExpressCostDao expressCostDao;
	
	public List<ExpressCost> list(ExpressCost expressCost){
		return expressCostDao.list(expressCost);
	}
	
}
