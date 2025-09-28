package com.yq.vo;

import com.yq.entity.Distribution;

public class DistributionVo extends Distribution {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String custStoreImg;

	public String getCustStoreImg() {
		return custStoreImg;
	}

	public void setCustStoreImg(String custStoreImg) {
		this.custStoreImg = custStoreImg;
	}
	
}
