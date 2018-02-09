package com.sss.car.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/11/4.
 */

public class OrderEdit {
    public String order_id;
    public String mobile;
    public String recipients;
    public String address;
    public String vehicle_name;
    public String remark;
    public String order_code;
    public String delivery_time;
    public String damages;
    public String express;
    public String waybill;
    public String total;
    public String deduct_price;
    public String coupon_price;
    public String number;
    public String integral;
    public String coupon_id;
    public String create_time;
    public String shop_id;
    public String member_id;
    public String mode_payment;
    public String type;
    public String status;
    public String shop_name;
    public String shop_logo;
    public String start_time;
    public  List<OrderEdit_GoodsData> goods_data=new ArrayList<>();
}
