package com.yq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yq.dao.DistributionMerchantsDao;
import com.yq.entity.DistributionMerchants;

@Service
public class DistributionMerchantsService {
	@Autowired
	private DistributionMerchantsDao distributionMerchantsDao;
	
	public void save(DistributionMerchants objDistributionMerchants) {
		distributionMerchantsDao.save(objDistributionMerchants);
	}
	
	public DistributionMerchants getMerchantByDistributionId(String distributionId) {
		return distributionMerchantsDao.getMerchantByDistributionId(distributionId);
	}
	
	public DistributionMerchants getMerchantByWxusercust(String distributionId) {
		return distributionMerchantsDao.getMerchantByWxusercust(distributionId);
	}
	
}
