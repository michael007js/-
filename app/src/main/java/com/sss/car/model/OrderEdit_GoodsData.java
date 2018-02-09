package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/11/5.
 */

public class OrderEdit_GoodsData implements Parcelable {
    public String shop_id;
    public String name;
    public String logo;
    public String total_rows;
    public String total;
    public String order_id;
    public  List<OrderEdit_GoodsData_Data> data=new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shop_id);
        dest.writeString(this.name);
        dest.writeString(this.logo);
        dest.writeString(this.total_rows);
        dest.writeString(this.total);
        dest.writeString(this.order_id);
        dest.writeTypedList(this.data);
    }

    public OrderEdit_GoodsData() {
    }

    protected OrderEdit_GoodsData(Parcel in) {
        this.shop_id = in.readString();
        this.name = in.readString();
        this.logo = in.readString();
        this.total_rows = in.readString();
        this.total = in.readString();
        this.order_id = in.readString();
        this.data = in.createTypedArrayList(OrderEdit_GoodsData_Data.CREATOR);
    }

    public static final Parcelable.Creator<OrderEdit_GoodsData> CREATOR = new Parcelable.Creator<OrderEdit_GoodsData>() {
        @Override
        public OrderEdit_GoodsData createFromParcel(Parcel source) {
            return new OrderEdit_GoodsData(source);
        }

        @Override
        public OrderEdit_GoodsData[] newArray(int size) {
            return new OrderEdit_GoodsData[size];
        }
    };
}
