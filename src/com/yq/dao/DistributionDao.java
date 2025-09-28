package com.yq.dao;

import java.util.List;

import com.yq.entity.Distribution;
import com.yq.vo.DistributionVo;

public interface DistributionDao {
	public List<DistributionVo> search(Distribution distribution);
	public int update(Distribution distribution);
	public int orderSign(Distribution distribution);
}
