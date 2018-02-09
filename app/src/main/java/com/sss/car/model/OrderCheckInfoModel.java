package com.sss.car.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2018/1/10.
 */

public class OrderCheckInfoModel {
    public String order_id;
    public String qr_code;
    public String order_code;
    public String total;
    public String create_time;
    public String shop_id;
    public String member_id;
    public String is_comment;
    public String goods_comment;
    public String type;//1车品2车服3SOS
    public int status;
    public String name;
    public String picture;
    public String lng;
    public String lat;
    public String exchange_id;
    public String exchange_status;
    public String status_name;
    public List<OrderCheckInfoModel_Goods_Data> goods_data=new ArrayList<>();


}
