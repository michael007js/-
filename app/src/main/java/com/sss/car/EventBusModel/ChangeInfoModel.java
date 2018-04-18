package com.sss.car.EventBusModel;

/**
 * Created by leilei on 2017/8/15.
 */

public class ChangeInfoModel {
    public String msg;
    public String province;
    public String city;
    public String district;
    public String lat;
    public String lng;
    public String type;
    public String startTime;
    public String endTime;

    @Override
    public String toString() {
        return "ChangeInfoModel{" +
                "msg='" + msg + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
