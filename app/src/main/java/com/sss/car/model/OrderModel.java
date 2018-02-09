package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/10/7.
 */

public class OrderModel implements Parcelable {
    public String order_id;
    public String qr_code;
    public String order_code;
    public String create_time;
    public String id;
    public String name;
    public String exchange_id;
    public String picture;
    public String lng;
    public String exchange_status;
    public String lat;
    public String target_id;
    public String target_name;
    public List<OrderModel_goods_data> data=new ArrayList<>();
    public boolean isChoose=false;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.order_id);
        dest.writeString(this.qr_code);
        dest.writeString(this.order_code);
        dest.writeString(this.create_time);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.exchange_id);
        dest.writeString(this.picture);
        dest.writeString(this.lng);
        dest.writeString(this.exchange_status);
        dest.writeString(this.lat);
        dest.writeString(this.target_id);
        dest.writeString(this.target_name);
        dest.writeTypedList(this.data);
        dest.writeByte(this.isChoose ? (byte) 1 : (byte) 0);
    }

    public OrderModel() {
    }

    protected OrderModel(Parcel in) {
        this.order_id = in.readString();
        this.qr_code = in.readString();
        this.order_code = in.readString();
        this.create_time = in.readString();
        this.id = in.readString();
        this.name = in.readString();
        this.exchange_id = in.readString();
        this.picture = in.readString();
        this.lng = in.readString();
        this.exchange_status = in.readString();
        this.lat = in.readString();
        this.target_id = in.readString();
        this.target_name = in.readString();
        this.data = in.createTypedArrayList(OrderModel_goods_data.CREATOR);
        this.isChoose = in.readByte() != 0;
    }

    public static final Creator<OrderModel> CREATOR = new Creator<OrderModel>() {
        @Override
        public OrderModel createFromParcel(Parcel source) {
            return new OrderModel(source);
        }

        @Override
        public OrderModel[] newArray(int size) {
            return new OrderModel[size];
        }
    };
}
