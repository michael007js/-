package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 综合订单列表==>订单类型列表(实物订单,服务订单)
 * Created by leilei on 2017/9/28.
 */

public class OrderSynthesize_DataModel implements Parcelable {
    public String title;
    public String type;
    public String order_id;
    public List<OrderSynthesize_Data_ListModel> list=new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.type);
        dest.writeString(this.order_id);
        dest.writeTypedList(this.list);
    }

    public OrderSynthesize_DataModel() {
    }

    protected OrderSynthesize_DataModel(Parcel in) {
        this.title = in.readString();
        this.type = in.readString();
        this.order_id = in.readString();
        this.list = in.createTypedArrayList(OrderSynthesize_Data_ListModel.CREATOR);
    }

    public static final Creator<OrderSynthesize_DataModel> CREATOR = new Creator<OrderSynthesize_DataModel>() {
        @Override
        public OrderSynthesize_DataModel createFromParcel(Parcel source) {
            return new OrderSynthesize_DataModel(source);
        }

        @Override
        public OrderSynthesize_DataModel[] newArray(int size) {
            return new OrderSynthesize_DataModel[size];
        }
    };
}
