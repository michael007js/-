package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/11/5.
 */

public class OrderEdit_GoodsData_Data implements Parcelable {
    public String id;
    public String name;
    public String num;
    public String price;
    public String sid;
    public String shop_id;
    public String master_map;
    public List<OrderEdit_GoodsData_Data_Options> options=new ArrayList<>();


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.num);
        dest.writeString(this.price);
        dest.writeString(this.sid);
        dest.writeString(this.shop_id);
        dest.writeString(this.master_map);
        dest.writeTypedList(this.options);
    }

    public OrderEdit_GoodsData_Data() {
    }

    protected OrderEdit_GoodsData_Data(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.num = in.readString();
        this.price = in.readString();
        this.sid = in.readString();
        this.shop_id = in.readString();
        this.master_map = in.readString();
        this.options = in.createTypedArrayList(OrderEdit_GoodsData_Data_Options.CREATOR);
    }

    public static final Creator<OrderEdit_GoodsData_Data> CREATOR = new Creator<OrderEdit_GoodsData_Data>() {
        @Override
        public OrderEdit_GoodsData_Data createFromParcel(Parcel source) {
            return new OrderEdit_GoodsData_Data(source);
        }

        @Override
        public OrderEdit_GoodsData_Data[] newArray(int size) {
            return new OrderEdit_GoodsData_Data[size];
        }
    };
}
