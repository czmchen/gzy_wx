package com.yq.vo;

import java.io.Serializable;
import java.util.List;

import com.yq.entity.Order;

public class OrderVO extends Order implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fid;
	private int ispd;
	private List<RMOrderDetailVo> objRMOrderDetailVo;
	private KdniaoVo objKdniaoVo = null;
	private String orderNO;
	private String wxOrderNO;
	private int IsYLZF;
	private int signFlag;

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public List<RMOrderDetailVo> getObjRMOrderDetailVo() {
		return objRMOrderDetailVo;
	}

	public void setObjRMOrderDetailVo(List<RMOrderDetailVo> objRMOrderDetailVo) {
		this.objRMOrderDetailVo = objRMOrderDetailVo;
	}

	public KdniaoVo getObjKdniaoVo() {
		return objKdniaoVo;
	}

	public void setObjKdniaoVo(KdniaoVo objKdniaoVo) {
		this.objKdniaoVo = objKdniaoVo;
	}

	public String getOrderNO() {
		return orderNO;
	}

	public void setOrderNO(String orderNO) {
		this.orderNO = orderNO;
	}

	public String getWxOrderNO() {
		return wxOrderNO;
	}

	public void setWxOrderNO(String wxOrderNO) {
		this.wxOrderNO = wxOrderNO;
	}

	public int getIspd() {
		return ispd;
	}

	public void setIspd(int ispd) {
		this.ispd = ispd;
	}

	public int getIsYLZF() {
		return IsYLZF;
	}

	public void setIsYLZF(int isYLZF) {
		IsYLZF = isYLZF;
	}

	public int getSignFlag() {
		return signFlag;
	}

	public void setSignFlag(int signFlag) {
		this.signFlag = signFlag;
	}

}
