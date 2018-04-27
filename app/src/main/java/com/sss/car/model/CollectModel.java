package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by leilei on 2018/4/19.
 */

public class CollectModel implements Parcelable {
    public String id;
    public String type;

    public CollectModel(String id, String type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public String toString() {
        return "CollectModel{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.type);
    }

    protected CollectModel(Parcel in) {
        this.id = in.readString();
        this.type = in.readString();
    }

    public static final Parcelable.Creator<CollectModel> CREATOR = new Parcelable.Creator<CollectModel>() {
        @Override
        public CollectModel createFromParcel(Parcel source) {
            return new CollectModel(source);
        }

        @Override
        public CollectModel[] newArray(int size) {
            return new CollectModel[size];
        }
    };
}
