/**
  * Copyright 2020 bejson.com 
  */
package com.yq.vo;
import java.io.Serializable;
import java.util.Date;

/**
 * Auto-generated: 2020-02-29 7:46:36
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Traces implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String AcceptStation;
    private String AcceptTime;
    public void setAcceptStation(String AcceptStation) {
         this.AcceptStation = AcceptStation;
     }
     public String getAcceptStation() {
         return AcceptStation;
     }

    public void setAcceptTime(String AcceptTime) {
         this.AcceptTime = AcceptTime;
     }
     public String getAcceptTime() {
         return AcceptTime;
     }

}