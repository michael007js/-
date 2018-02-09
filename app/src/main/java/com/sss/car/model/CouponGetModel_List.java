package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by leilei on 2017/11/2.
 */

public class CouponGetModel_List implements Parcelable {
    public String id;
    public String coupon_id;
    public String classify_id;
    public String name;
    public String describe;
    public String type;            /*1满减券，2现金券，3折扣券*/
    public String money;
    public String price;
    public String sell_price;
    public String sell;
    public String number;
    public String create_time;
    public String start_time;
    public String end_time;
    public String state;
    public String is_join;
    public String duration;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.coupon_id);
        dest.writeString(this.classify_id);
        dest.writeString(this.name);
        dest.writeString(this.describe);
        dest.writeString(this.type);
        dest.writeString(this.money);
        dest.writeString(this.price);
        dest.writeString(this.sell_price);
        dest.writeString(this.sell);
        dest.writeString(this.number);
        dest.writeString(this.create_time);
        dest.writeString(this.start_time);
        dest.writeString(this.end_time);
        dest.writeString(this.state);
        dest.writeString(this.is_join);
        dest.writeString(this.duration);
    }

    public CouponGetModel_List() {
    }

    protected CouponGetModel_List(Parcel in) {
        this.id = in.readString();
        this.coupon_id = in.readString();
        this.classify_id = in.readString();
        this.name = in.readString();
        this.describe = in.readString();
        this.type = in.readString();
        this.money = in.readString();
        this.price = in.readString();
        this.sell_price = in.readString();
        this.sell = in.readString();
        this.number = in.readString();
        this.create_time = in.readString();
        this.start_time = in.readString();
        this.end_time = in.readString();
        this.state = in.readString();
        this.is_join = in.readString();
        this.duration = in.readString();
    }

    public static final Creator<CouponGetModel_List> CREATOR = new Creator<CouponGetModel_List>() {
        @Override
        public CouponGetModel_List createFromParcel(Parcel source) {
            return new CouponGetModel_List(source);
        }

        @Override
        public CouponGetModel_List[] newArray(int size) {
            return new CouponGetModel_List[size];
        }
    };
}
