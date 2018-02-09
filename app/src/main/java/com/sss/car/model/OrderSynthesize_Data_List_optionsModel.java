package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 综合订单列表==>订单类型列表(实物订单,服务订单)==>商品列表==>规格
 * Created by leilei on 2017/9/28.
 */

public class OrderSynthesize_Data_List_optionsModel implements Parcelable {
    public String name;
    public String title;

    @Override
    public String toString() {
        return "OrderSynthesize_Data_List_optionsModel{" +
                "name='" + name + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.title);
    }

    public OrderSynthesize_Data_List_optionsModel() {
    }

    protected OrderSynthesize_Data_List_optionsModel(Parcel in) {
        this.name = in.readString();
        this.title = in.readString();
    }

    public static final Creator<OrderSynthesize_Data_List_optionsModel> CREATOR = new Creator<OrderSynthesize_Data_List_optionsModel>() {
        @Override
        public OrderSynthesize_Data_List_optionsModel createFromParcel(Parcel source) {
            return new OrderSynthesize_Data_List_optionsModel(source);
        }

        @Override
        public OrderSynthesize_Data_List_optionsModel[] newArray(int size) {
            return new OrderSynthesize_Data_List_optionsModel[size];
        }
    };
}
