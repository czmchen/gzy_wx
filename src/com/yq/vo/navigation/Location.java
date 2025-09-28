package com.yq.vo.navigation;
public class Location  implements java.io.Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double lng;
    private double lat;
    public void setLng(double lng) {
         this.lng = lng;
     }
     public double getLng() {
         return lng;
     }

    public void setLat(double lat) {
         this.lat = lat;
     }
     public double getLat() {
         return lat;
     }

}