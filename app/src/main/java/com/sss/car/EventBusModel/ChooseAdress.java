package com.sss.car.EventBusModel;

/**
 * Created by leilei on 2017/10/14.
 */

public class ChooseAdress {
    public String adress;
    public String lai;
    public String lng;
    public String province;
    public String city;
    public String district;


    public ChooseAdress(String adress, String lai, String lng, String province, String city, String district) {
        this.adress = adress;
        this.lai = lai;
        this.lng = lng;
        this.province = province;
        this.city = city;
        this.district = district;
    }
}
