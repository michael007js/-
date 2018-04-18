package com.sss.car.order_new;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by leilei on 2017/11/9.
 */

public class OrderModel implements Parcelable {
    public String order_id;
    public String qr_code;
    public String order_code;
    public String create_time;
    public String shop_id;
    public String member_id;
    public String is_comment;
    public String goods_comment;
    public String is_bargain;
    public String type;
    public String total;
    public int status;
    public String exchange_status;
    public String name;
    public String picture;
    public String lng;
    public String damages;
    public String lat;
    public String id;
    public String status_name;
    public String target_id;
    public String target_name;
    public String exchange_id;
    public List<OrderModel_GoodsData> goods_data=new ArrayList<>();

    @Override
    public String toString() {
        return "OrderModel{" +
                "order_id='" + order_id + '\'' +
                ", qr_code='" + qr_code + '\'' +
                ", order_code='" + order_code + '\'' +
                ", create_time='" + create_time + '\'' +
                ", shop_id='" + shop_id + '\'' +
                ", member_id='" + member_id + '\'' +
                ", is_comment='" + is_comment + '\'' +
                ", goods_comment='" + goods_comment + '\'' +
                ", is_bargain='" + is_bargain + '\'' +
                ", type='" + type + '\'' +
                ", total='" + total + '\'' +
                ", status=" + status +
                ", exchange_status='" + exchange_status + '\'' +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", lng='" + lng + '\'' +
                ", damages='" + damages + '\'' +
                ", lat='" + lat + '\'' +
                ", id='" + id + '\'' +
                ", status_name='" + status_name + '\'' +
                ", target_id='" + target_id + '\'' +
                ", target_name='" + target_name + '\'' +
                ", exchange_id='" + exchange_id + '\'' +
                ", goods_data=" + goods_data +
                '}';
    }

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
        dest.writeString(this.shop_id);
        dest.writeString(this.member_id);
        dest.writeString(this.is_comment);
        dest.writeString(this.goods_comment);
        dest.writeString(this.is_bargain);
        dest.writeString(this.type);
        dest.writeString(this.total);
        dest.writeInt(this.status);
        dest.writeString(this.exchange_status);
        dest.writeString(this.name);
        dest.writeString(this.picture);
        dest.writeString(this.lng);
        dest.writeString(this.damages);
        dest.writeString(this.lat);
        dest.writeString(this.id);
        dest.writeString(this.status_name);
        dest.writeString(this.target_id);
        dest.writeString(this.target_name);
        dest.writeString(this.exchange_id);
        dest.writeTypedList(this.goods_data);
    }

    public OrderModel() {
    }

    protected OrderModel(Parcel in) {
        this.order_id = in.readString();
        this.qr_code = in.readString();
        this.order_code = in.readString();
        this.create_time = in.readString();
        this.shop_id = in.readString();
        this.member_id = in.readString();
        this.is_comment = in.readString();
        this.goods_comment = in.readString();
        this.is_bargain = in.readString();
        this.type = in.readString();
        this.total = in.readString();
        this.status = in.readInt();
        this.exchange_status = in.readString();
        this.name = in.readString();
        this.picture = in.readString();
        this.lng = in.readString();
        this.damages = in.readString();
        this.lat = in.readString();
        this.id = in.readString();
        this.status_name = in.readString();
        this.target_id = in.readString();
        this.target_name = in.readString();
        this.exchange_id = in.readString();
        this.goods_data = in.createTypedArrayList(OrderModel_GoodsData.CREATOR);
    }

    public static final Parcelable.Creator<OrderModel> CREATOR = new Parcelable.Creator<OrderModel>() {
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
