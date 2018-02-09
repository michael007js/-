package com.sss.car.custom.GoodsTypeSelect.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by leilei on 2017/10/28.
 */

public class SelectDataModel {
    public String title;
    public boolean isSelect=false;
    public String type;

    public SelectDataModel(String title, boolean isSelect,String type) {
        this.title = title;
        this.isSelect = isSelect;
        this.type=type;
    }

    @Override
    public String toString() {
        return "SelectDataModel{" +
                "title='" + title + '\'' +
                ", isSelect=" + isSelect +
                ", type='" + type + '\'' +
                '}';
    }
}
