package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by leilei on 2017/10/15.
 */

public class OrderSellerModel_Order_Goods implements Parcelable {
    public String goods_id;
    public String price;
    public String number;
    public String title;
    public String slogan;
    public String master_map;
    public String cost_price;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.goods_id);
        dest.writeString(this.price);
        dest.writeString(this.number);
        dest.writeString(this.title);
        dest.writeString(this.slogan);
        dest.writeString(this.master_map);
        dest.writeString(this.cost_price);
    }

    public OrderSellerModel_Order_Goods() {
    }

    protected OrderSellerModel_Order_Goods(Parcel in) {
        this.goods_id = in.readString();
        this.price = in.readString();
        this.number = in.readString();
        this.title = in.readString();
        this.slogan = in.readString();
        this.master_map = in.readString();
        this.cost_price = in.readString();
    }

    public static final Creator<OrderSellerModel_Order_Goods> CREATOR = new Creator<OrderSellerModel_Order_Goods>() {
        @Override
        public OrderSellerModel_Order_Goods createFromParcel(Parcel source) {
            return new OrderSellerModel_Order_Goods(source);
        }

        @Override
        public OrderSellerModel_Order_Goods[] newArray(int size) {
            return new OrderSellerModel_Order_Goods[size];
        }
    };
}
