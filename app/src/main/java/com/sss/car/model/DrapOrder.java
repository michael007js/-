package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/11/4.
 */

public class DrapOrder implements Parcelable {
    public String order_id;
    public String total;
    public String number;
    public String shop_id;
    public String member_id;
    public String create_time;
    public String shop_name;
    public String shop_logo;
    public String type;
    public List<DrapOrder_GoodsData>goods_data=new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.order_id);
        dest.writeString(this.total);
        dest.writeString(this.number);
        dest.writeString(this.shop_id);
        dest.writeString(this.member_id);
        dest.writeString(this.create_time);
        dest.writeString(this.shop_name);
        dest.writeString(this.shop_logo);
        dest.writeString(this.type);
        dest.writeTypedList(this.goods_data);
    }

    public DrapOrder() {
    }

    protected DrapOrder(Parcel in) {
        this.order_id = in.readString();
        this.total = in.readString();
        this.number = in.readString();
        this.shop_id = in.readString();
        this.member_id = in.readString();
        this.create_time = in.readString();
        this.shop_name = in.readString();
        this.shop_logo = in.readString();
        this.type = in.readString();
        this.goods_data = in.createTypedArrayList(DrapOrder_GoodsData.CREATOR);
    }

    public static final Creator<DrapOrder> CREATOR = new Creator<DrapOrder>() {
        @Override
        public DrapOrder createFromParcel(Parcel source) {
            return new DrapOrder(source);
        }

        @Override
        public DrapOrder[] newArray(int size) {
            return new DrapOrder[size];
        }
    };
}
