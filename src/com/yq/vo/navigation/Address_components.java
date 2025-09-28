package com.yq.vo.navigation;
public class Address_components  implements java.io.Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String province;
    private String city;
    private String district;
    private String street;
    private String street_number;
    public void setProvince(String province) {
         this.province = province;
     }
     public String getProvince() {
         return province;
     }

    public void setCity(String city) {
         this.city = city;
     }
     public String getCity() {
         return city;
     }

    public void setDistrict(String district) {
         this.district = district;
     }
     public String getDistrict() {
         return district;
     }

    public void setStreet(String street) {
         this.street = street;
     }
     public String getStreet() {
         return street;
     }

    public void setStreet_number(String street_number) {
         this.street_number = street_number;
     }
     public String getStreet_number() {
         return street_number;
     }

}