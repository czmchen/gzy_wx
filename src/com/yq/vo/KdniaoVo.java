/**
  * Copyright 2020 bejson.com 
  */
package com.yq.vo;
import java.io.Serializable;
import java.util.List;

/**
 * Auto-generated: 2020-02-29 7:46:36
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class KdniaoVo implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String LogisticCode;
    private String ShipperCode;
    private List<Traces> Traces;
    private String State;
    private String EBusinessID;
    private boolean Success;
    public void setLogisticCode(String LogisticCode) {
         this.LogisticCode = LogisticCode;
     }
     public String getLogisticCode() {
         return LogisticCode;
     }

    public void setShipperCode(String ShipperCode) {
         this.ShipperCode = ShipperCode;
     }
     public String getShipperCode() {
         return ShipperCode;
     }

    public void setTraces(List<Traces> Traces) {
         this.Traces = Traces;
     }
     public List<Traces> getTraces() {
         return Traces;
     }

    public void setState(String State) {
         this.State = State;
     }
     public String getState() {
         return State;
     }

    public void setEBusinessID(String EBusinessID) {
         this.EBusinessID = EBusinessID;
     }
     public String getEBusinessID() {
         return EBusinessID;
     }

    public void setSuccess(boolean Success) {
         this.Success = Success;
     }
     public boolean getSuccess() {
         return Success;
     }

}