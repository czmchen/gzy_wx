package com.yq.vo;

import java.util.ArrayList;
import java.util.List;

public class UploadVo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String token;
	private boolean uploadFinish;
	private long costTime;
	private List<UploadDetailVo> lstUploadDetailVo = new ArrayList<UploadDetailVo>();
	private String def1;
	private String def2;
	private String def3;
	private String def4;
	private String def5;
	private String def6;
	private String def7;
	private String def8;
	private String def9;
	private String def10;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isUploadFinish() {
		return uploadFinish;
	}

	public void setUploadFinish(boolean uploadFinish) {
		this.uploadFinish = uploadFinish;
	}

	public List<UploadDetailVo> getLstUploadDetailVo() {
		return lstUploadDetailVo;
	}

	public void setLstUploadDetailVo(List<UploadDetailVo> lstUploadDetailVo) {
		this.lstUploadDetailVo = lstUploadDetailVo;
	}

	public long getCostTime() {
		return costTime;
	}

	public void setCostTime(long costTime) {
		this.costTime = costTime;
	}

	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}

	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}

	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}

	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}

	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}

	public String getDef6() {
		return def6;
	}

	public void setDef6(String def6) {
		this.def6 = def6;
	}

	public String getDef7() {
		return def7;
	}

	public void setDef7(String def7) {
		this.def7 = def7;
	}

	public String getDef8() {
		return def8;
	}

	public void setDef8(String def8) {
		this.def8 = def8;
	}

	public String getDef9() {
		return def9;
	}

	public void setDef9(String def9) {
		this.def9 = def9;
	}

	public String getDef10() {
		return def10;
	}

	public void setDef10(String def10) {
		this.def10 = def10;
	}

}
