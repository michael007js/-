package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/10/7.
 */

public class OrderModel_goods_data implements Parcelable {
    public String goods_id;
    public String number;
    public String title;
    public String slogan;
    public String master_map;
    public String cost_price;
    public String price;
    public boolean isChoose=false;
    public String customContent="";
    public String customGrade;
    public List<String> photo=new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.goods_id);
        dest.writeString(this.number);
        dest.writeString(this.title);
        dest.writeString(this.slogan);
        dest.writeString(this.master_map);
        dest.writeString(this.cost_price);
        dest.writeString(this.price);
        dest.writeByte(this.isChoose ? (byte) 1 : (byte) 0);
        dest.writeString(this.customContent);
        dest.writeString(this.customGrade);
        dest.writeStringList(this.photo);
    }

    public OrderModel_goods_data() {
    }

    protected OrderModel_goods_data(Parcel in) {
        this.goods_id = in.readString();
        this.number = in.readString();
        this.title = in.readString();
        this.slogan = in.readString();
        this.master_map = in.readString();
        this.cost_price = in.readString();
        this.price = in.readString();
        this.isChoose = in.readByte() != 0;
        this.customContent = in.readString();
        this.customGrade = in.readString();
        this.photo = in.createStringArrayList();
    }

    public static final Creator<OrderModel_goods_data> CREATOR = new Creator<OrderModel_goods_data>() {
        @Override
        public OrderModel_goods_data createFromParcel(Parcel source) {
            return new OrderModel_goods_data(source);
        }

        @Override
        public OrderModel_goods_data[] newArray(int size) {
            return new OrderModel_goods_data[size];
        }
    };
}
