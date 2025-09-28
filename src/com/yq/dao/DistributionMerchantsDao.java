package com.yq.dao;

import org.apache.ibatis.annotations.Param;

import com.yq.entity.DistributionMerchants;

public interface DistributionMerchantsDao {
	
	public void save(DistributionMerchants objDistributionMerchants);
	
	public DistributionMerchants getMerchantByDistributionId(@Param(value = "distributionId") String distributionId);
	
	public DistributionMerchants getMerchantByWxusercust(@Param(value = "custId") String custId);
}
