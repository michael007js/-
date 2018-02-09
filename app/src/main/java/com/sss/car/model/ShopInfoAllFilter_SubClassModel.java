package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by leilei on 2017/9/19.
 */

public class ShopInfoAllFilter_SubClassModel implements Parcelable {
    public String classify_id;
    public String name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.classify_id);
        dest.writeString(this.name);
    }

    public ShopInfoAllFilter_SubClassModel() {
    }

    protected ShopInfoAllFilter_SubClassModel(Parcel in) {
        this.classify_id = in.readString();
        this.name = in.readString();
    }

    public static final Creator<ShopInfoAllFilter_SubClassModel> CREATOR = new Creator<ShopInfoAllFilter_SubClassModel>() {
        @Override
        public ShopInfoAllFilter_SubClassModel createFromParcel(Parcel source) {
            return new ShopInfoAllFilter_SubClassModel(source);
        }

        @Override
        public ShopInfoAllFilter_SubClassModel[] newArray(int size) {
            return new ShopInfoAllFilter_SubClassModel[size];
        }
    };
}
