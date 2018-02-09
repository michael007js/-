package com.sss.car.order_new;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/11/9.
 */

public class OrderModel_GoodsData implements Parcelable {
    public String goods_id;
    public String number;
    public String title;
    public String slogan;
    public String master_map;
    public String cost_price;
    public String price;
    public int customGrade=0;
    public String customContent="";
    public List<String> photos=new ArrayList<>();

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
        dest.writeInt(this.customGrade);
        dest.writeString(this.customContent);
        dest.writeStringList(this.photos);
    }

    public OrderModel_GoodsData() {
    }

    protected OrderModel_GoodsData(Parcel in) {
        this.goods_id = in.readString();
        this.number = in.readString();
        this.title = in.readString();
        this.slogan = in.readString();
        this.master_map = in.readString();
        this.cost_price = in.readString();
        this.price = in.readString();
        this.customGrade = in.readInt();
        this.customContent = in.readString();
        this.photos = in.createStringArrayList();
    }

    public static final Creator<OrderModel_GoodsData> CREATOR = new Creator<OrderModel_GoodsData>() {
        @Override
        public OrderModel_GoodsData createFromParcel(Parcel source) {
            return new OrderModel_GoodsData(source);
        }

        @Override
        public OrderModel_GoodsData[] newArray(int size) {
            return new OrderModel_GoodsData[size];
        }
    };
}
