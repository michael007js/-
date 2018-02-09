package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by leilei on 2017/9/13.
 */

public class GoodsModel implements Parcelable {
    public String goods_id ;
    public String title ;
    public String slogan;//一句话推荐
    public String master_map;//商铺图片
    public String cost_price;//原价
    public String price;//现价
    public String sell;//销量
    public String number;
    public String type;
    public String member_id;
    public String distance;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.goods_id);
        dest.writeString(this.title);
        dest.writeString(this.slogan);
        dest.writeString(this.master_map);
        dest.writeString(this.cost_price);
        dest.writeString(this.price);
        dest.writeString(this.sell);
        dest.writeString(this.number);
        dest.writeString(this.type);
        dest.writeString(this.member_id);
        dest.writeString(this.distance);
    }

    public GoodsModel() {
    }

    protected GoodsModel(Parcel in) {
        this.goods_id = in.readString();
        this.title = in.readString();
        this.slogan = in.readString();
        this.master_map = in.readString();
        this.cost_price = in.readString();
        this.price = in.readString();
        this.sell = in.readString();
        this.number = in.readString();
        this.type = in.readString();
        this.member_id = in.readString();
        this.distance = in.readString();
    }

    public static final Creator<GoodsModel> CREATOR = new Creator<GoodsModel>() {
        @Override
        public GoodsModel createFromParcel(Parcel source) {
            return new GoodsModel(source);
        }

        @Override
        public GoodsModel[] newArray(int size) {
            return new GoodsModel[size];
        }
    };
}
