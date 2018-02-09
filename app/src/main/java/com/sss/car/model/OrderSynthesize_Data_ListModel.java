package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 综合订单列表==>订单类型列表(实物订单,服务订单)==>商品列表
 * Created by leilei on 2017/9/28.
 */

public class OrderSynthesize_Data_ListModel implements Parcelable {
    public String id;
    public String name;
    public String num;
    public String price;
    public String shop_id;
    public String master_map;
    public List<OrderSynthesize_Data_List_optionsModel> options=new ArrayList<>();

    @Override
    public String toString() {
        return "OrderSynthesize_Data_ListModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", num='" + num + '\'' +
                ", price='" + price + '\'' +
                ", shop_id='" + shop_id + '\'' +
                ", master_map='" + master_map + '\'' +
                ", options=" + options +
                '}';
    }

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
        dest.writeString(this.shop_id);
        dest.writeString(this.master_map);
        dest.writeTypedList(this.options);
    }

    public OrderSynthesize_Data_ListModel() {
    }

    protected OrderSynthesize_Data_ListModel(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.num = in.readString();
        this.price = in.readString();
        this.shop_id = in.readString();
        this.master_map = in.readString();
        this.options = in.createTypedArrayList(OrderSynthesize_Data_List_optionsModel.CREATOR);
    }

    public static final Creator<OrderSynthesize_Data_ListModel> CREATOR = new Creator<OrderSynthesize_Data_ListModel>() {
        @Override
        public OrderSynthesize_Data_ListModel createFromParcel(Parcel source) {
            return new OrderSynthesize_Data_ListModel(source);
        }

        @Override
        public OrderSynthesize_Data_ListModel[] newArray(int size) {
            return new OrderSynthesize_Data_ListModel[size];
        }
    };
}
