package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 综合订单列表
 * Created by leilei on 2017/9/28.
 */

public class OrderSynthesizeModel implements Parcelable {
    public String shop_id;
    public String name;
    public String logo;
    public String total_rows;
    public String total;
    public String order_id;
    public List<OrderSynthesize_DataModel> data=new ArrayList<>();

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

    public OrderSynthesizeModel() {
    }

    protected OrderSynthesizeModel(Parcel in) {
        this.shop_id = in.readString();
        this.name = in.readString();
        this.logo = in.readString();
        this.total_rows = in.readString();
        this.total = in.readString();
        this.order_id = in.readString();
        this.data = in.createTypedArrayList(OrderSynthesize_DataModel.CREATOR);
    }

    public static final Creator<OrderSynthesizeModel> CREATOR = new Creator<OrderSynthesizeModel>() {
        @Override
        public OrderSynthesizeModel createFromParcel(Parcel source) {
            return new OrderSynthesizeModel(source);
        }

        @Override
        public OrderSynthesizeModel[] newArray(int size) {
            return new OrderSynthesizeModel[size];
        }
    };
}
