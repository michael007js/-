package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/9/26.
 */

public class GoodsChooseSizeName implements Parcelable {
    public String title;
    public List<GoodsChooseSizeName_Model> data=new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeList(this.data);
    }

    public GoodsChooseSizeName() {
    }

    protected GoodsChooseSizeName(Parcel in) {
        this.title = in.readString();
        this.data = new ArrayList<GoodsChooseSizeName_Model>();
        in.readList(this.data, GoodsChooseSizeName_Model.class.getClassLoader());
    }

    public static final Creator<GoodsChooseSizeName> CREATOR = new Creator<GoodsChooseSizeName>() {
        @Override
        public GoodsChooseSizeName createFromParcel(Parcel source) {
            return new GoodsChooseSizeName(source);
        }

        @Override
        public GoodsChooseSizeName[] newArray(int size) {
            return new GoodsChooseSizeName[size];
        }
    };
}
