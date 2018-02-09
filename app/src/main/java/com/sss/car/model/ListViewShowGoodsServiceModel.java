package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/9/13.
 */

public class ListViewShowGoodsServiceModel implements Parcelable {
    public String classify_id;
    public String name;
    public int count;
    public int number;
    public int page;
    public List<GoodsModel> list=new ArrayList<>();


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.classify_id);
        dest.writeString(this.name);
        dest.writeInt(this.count);
        dest.writeInt(this.number);
        dest.writeInt(this.page);
        dest.writeTypedList(this.list);
    }

    public ListViewShowGoodsServiceModel() {
    }

    protected ListViewShowGoodsServiceModel(Parcel in) {
        this.classify_id = in.readString();
        this.name = in.readString();
        this.count = in.readInt();
        this.number = in.readInt();
        this.page = in.readInt();
        this.list = in.createTypedArrayList(GoodsModel.CREATOR);
    }

    public static final Creator<ListViewShowGoodsServiceModel> CREATOR = new Creator<ListViewShowGoodsServiceModel>() {
        @Override
        public ListViewShowGoodsServiceModel createFromParcel(Parcel source) {
            return new ListViewShowGoodsServiceModel(source);
        }

        @Override
        public ListViewShowGoodsServiceModel[] newArray(int size) {
            return new ListViewShowGoodsServiceModel[size];
        }
    };
}
