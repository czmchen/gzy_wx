package com.weixin.vo;

public class TemplateValue {
	String value;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	String color;
	public TemplateValue(String value,String color) {
		// TODO Auto-generated constructor stub
		this.color=color;
		this.value=value;
	}
}
