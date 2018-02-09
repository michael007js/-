package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by leilei on 2017/11/5.
 */

public class OrderEdit_GoodsData_Data_Options implements Parcelable {
    public String title;
    public String name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.name);
    }

    public OrderEdit_GoodsData_Data_Options() {
    }

    protected OrderEdit_GoodsData_Data_Options(Parcel in) {
        this.title = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<OrderEdit_GoodsData_Data_Options> CREATOR = new Parcelable.Creator<OrderEdit_GoodsData_Data_Options>() {
        @Override
        public OrderEdit_GoodsData_Data_Options createFromParcel(Parcel source) {
            return new OrderEdit_GoodsData_Data_Options(source);
        }

        @Override
        public OrderEdit_GoodsData_Data_Options[] newArray(int size) {
            return new OrderEdit_GoodsData_Data_Options[size];
        }
    };
}
