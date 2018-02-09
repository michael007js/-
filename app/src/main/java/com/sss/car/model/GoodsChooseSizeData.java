package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/9/25.
 */

public class GoodsChooseSizeData implements Parcelable {
    public String name;
    public String price;
    public String number;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.price);
        dest.writeString(this.number);
    }

    public GoodsChooseSizeData() {
    }

    protected GoodsChooseSizeData(Parcel in) {
        this.name = in.readString();
        this.price = in.readString();
        this.number = in.readString();
    }

    public static final Creator<GoodsChooseSizeData> CREATOR = new Creator<GoodsChooseSizeData>() {
        @Override
        public GoodsChooseSizeData createFromParcel(Parcel source) {
            return new GoodsChooseSizeData(source);
        }

        @Override
        public GoodsChooseSizeData[] newArray(int size) {
            return new GoodsChooseSizeData[size];
        }
    };
}
