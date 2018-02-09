package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/11/4.
 */

public class DrapOrder_GoodsData implements Parcelable {
    public String id;
    public String price;
    public String number;
    public String goods_id;
    public String title;
    public String shop_id;
    public String master_map;
    public String options;
    public String sid;
    public String total;
    public String size_name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.price);
        dest.writeString(this.number);
        dest.writeString(this.goods_id);
        dest.writeString(this.title);
        dest.writeString(this.shop_id);
        dest.writeString(this.master_map);
        dest.writeString(this.options);
        dest.writeString(this.sid);
        dest.writeString(this.total);
        dest.writeString(this.size_name);
    }

    public DrapOrder_GoodsData() {
    }

    protected DrapOrder_GoodsData(Parcel in) {
        this.id = in.readString();
        this.price = in.readString();
        this.number = in.readString();
        this.goods_id = in.readString();
        this.title = in.readString();
        this.shop_id = in.readString();
        this.master_map = in.readString();
        this.options = in.readString();
        this.sid = in.readString();
        this.total = in.readString();
        this.size_name = in.readString();
    }

    public static final Creator<DrapOrder_GoodsData> CREATOR = new Creator<DrapOrder_GoodsData>() {
        @Override
        public DrapOrder_GoodsData createFromParcel(Parcel source) {
            return new DrapOrder_GoodsData(source);
        }

        @Override
        public DrapOrder_GoodsData[] newArray(int size) {
            return new DrapOrder_GoodsData[size];
        }
    };
}
