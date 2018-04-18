package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/10/15.
 */

public class OrderSellerModel implements Parcelable {
    public String express_id;
    public String order_id;
    public String mobile;
    public String recipients;
    public String address;
    public String vehicle_name;
    public String remark;
    public String order_code;
    public String delivery_time;
    public String damages;
    public String total;
    public String bargain;
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
    public String coupon_name;
    public String express;
    public String waybill;
    public String exchange_id;
    public String lat;
    public String lng;
    public  List<OrderSellerModel_Order_Goods> goods_data=new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.express_id);
        dest.writeString(this.order_id);
        dest.writeString(this.mobile);
        dest.writeString(this.recipients);
        dest.writeString(this.address);
        dest.writeString(this.vehicle_name);
        dest.writeString(this.remark);
        dest.writeString(this.order_code);
        dest.writeString(this.delivery_time);
        dest.writeString(this.damages);
        dest.writeString(this.total);
        dest.writeString(this.bargain);
        dest.writeString(this.deduct_price);
        dest.writeString(this.coupon_price);
        dest.writeString(this.number);
        dest.writeString(this.integral);
        dest.writeString(this.coupon_id);
        dest.writeString(this.create_time);
        dest.writeString(this.shop_id);
        dest.writeString(this.member_id);
        dest.writeString(this.mode_payment);
        dest.writeString(this.type);
        dest.writeString(this.status);
        dest.writeString(this.shop_name);
        dest.writeString(this.shop_logo);
        dest.writeString(this.start_time);
        dest.writeString(this.coupon_name);
        dest.writeString(this.express);
        dest.writeString(this.waybill);
        dest.writeString(this.exchange_id);
        dest.writeString(this.lat);
        dest.writeString(this.lng);
        dest.writeTypedList(this.goods_data);
    }

    public OrderSellerModel() {
    }

    protected OrderSellerModel(Parcel in) {
        this.express_id = in.readString();
        this.order_id = in.readString();
        this.mobile = in.readString();
        this.recipients = in.readString();
        this.address = in.readString();
        this.vehicle_name = in.readString();
        this.remark = in.readString();
        this.order_code = in.readString();
        this.delivery_time = in.readString();
        this.damages = in.readString();
        this.total = in.readString();
        this.bargain = in.readString();
        this.deduct_price = in.readString();
        this.coupon_price = in.readString();
        this.number = in.readString();
        this.integral = in.readString();
        this.coupon_id = in.readString();
        this.create_time = in.readString();
        this.shop_id = in.readString();
        this.member_id = in.readString();
        this.mode_payment = in.readString();
        this.type = in.readString();
        this.status = in.readString();
        this.shop_name = in.readString();
        this.shop_logo = in.readString();
        this.start_time = in.readString();
        this.coupon_name = in.readString();
        this.express = in.readString();
        this.waybill = in.readString();
        this.exchange_id = in.readString();
        this.lat = in.readString();
        this.lng = in.readString();
        this.goods_data = in.createTypedArrayList(OrderSellerModel_Order_Goods.CREATOR);
    }

    public static final Creator<OrderSellerModel> CREATOR = new Creator<OrderSellerModel>() {
        @Override
        public OrderSellerModel createFromParcel(Parcel source) {
            return new OrderSellerModel(source);
        }

        @Override
        public OrderSellerModel[] newArray(int size) {
            return new OrderSellerModel[size];
        }
    };
}
