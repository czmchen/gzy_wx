package com.yq.vo.navigation;

public class Result  implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private Location location;
	private Ad_info ad_info;
	private Address_components address_components;
	private double similarity;
	private int deviation;
	private int reliability;
	private int level;

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}

	public void setAd_info(Ad_info ad_info) {
		this.ad_info = ad_info;
	}

	public Ad_info getAd_info() {
		return ad_info;
	}

	public void setAddress_components(Address_components address_components) {
		this.address_components = address_components;
	}

	public Address_components getAddress_components() {
		return address_components;
	}

	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}

	public double getSimilarity() {
		return similarity;
	}

	public void setDeviation(int deviation) {
		this.deviation = deviation;
	}

	public int getDeviation() {
		return deviation;
	}

	public void setReliability(int reliability) {
		this.reliability = reliability;
	}

	public int getReliability() {
		return reliability;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

}