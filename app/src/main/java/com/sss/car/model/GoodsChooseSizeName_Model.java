package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by leilei on 2017/9/26.
 */

public class GoodsChooseSizeName_Model implements Parcelable {
    public String name;
    public boolean isChoose;

    public GoodsChooseSizeName_Model(String name, boolean isChoose) {
        this.name = name;
        this.isChoose = isChoose;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeByte(this.isChoose ? (byte) 1 : (byte) 0);
    }

    protected GoodsChooseSizeName_Model(Parcel in) {
        this.name = in.readString();
        this.isChoose = in.readByte() != 0;
    }

    public static final Creator<GoodsChooseSizeName_Model> CREATOR = new Creator<GoodsChooseSizeName_Model>() {
        @Override
        public GoodsChooseSizeName_Model createFromParcel(Parcel source) {
            return new GoodsChooseSizeName_Model(source);
        }

        @Override
        public GoodsChooseSizeName_Model[] newArray(int size) {
            return new GoodsChooseSizeName_Model[size];
        }
    };
}
