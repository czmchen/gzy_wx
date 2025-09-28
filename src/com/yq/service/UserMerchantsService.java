package com.yq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yq.dao.UserMerchantsDao;
import com.yq.entity.UserMerchants;

@Service
public class UserMerchantsService {
	@Autowired
	private UserMerchantsDao userMerchantsDao;
	
	public void save(UserMerchants objUserMerchants) {
		userMerchantsDao.save(objUserMerchants);
	}
	
}
