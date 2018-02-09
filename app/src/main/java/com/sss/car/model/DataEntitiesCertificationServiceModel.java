package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by leilei on 2017/8/19.
 */

public class DataEntitiesCertificationServiceModel implements Parcelable {
    public String service;
    public String id;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.service);
        dest.writeString(this.id);
    }

    public DataEntitiesCertificationServiceModel(String service, String id) {
        this.service = service;
        this.id = id;
    }

    public DataEntitiesCertificationServiceModel() {
    }

    protected DataEntitiesCertificationServiceModel(Parcel in) {
        this.service = in.readString();
        this.id = in.readString();
    }

    public static final Creator<DataEntitiesCertificationServiceModel> CREATOR = new Creator<DataEntitiesCertificationServiceModel>() {
        @Override
        public DataEntitiesCertificationServiceModel createFromParcel(Parcel source) {
            return new DataEntitiesCertificationServiceModel(source);
        }

        @Override
        public DataEntitiesCertificationServiceModel[] newArray(int size) {
            return new DataEntitiesCertificationServiceModel[size];
        }
    };
}
