package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by leilei on 2017/9/27.
 */

public class ShoppingCart_Data_Options implements Parcelable {
    public String name;
    public String title;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.title);
    }

    public ShoppingCart_Data_Options() {
    }

    protected ShoppingCart_Data_Options(Parcel in) {
        this.name = in.readString();
        this.title = in.readString();
    }

    public static final Creator<ShoppingCart_Data_Options> CREATOR = new Creator<ShoppingCart_Data_Options>() {
        @Override
        public ShoppingCart_Data_Options createFromParcel(Parcel source) {
            return new ShoppingCart_Data_Options(source);
        }

        @Override
        public ShoppingCart_Data_Options[] newArray(int size) {
            return new ShoppingCart_Data_Options[size];
        }
    };
}
