package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by leilei on 2017/8/19.
 */

public class DataEntitiesCertificationPicModel implements Parcelable {

    public String id;
    public String path;
    public String type;
    public String picture_id;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.path);
        dest.writeString(this.type);
        dest.writeString(this.picture_id);
    }

    public DataEntitiesCertificationPicModel() {
    }

    public DataEntitiesCertificationPicModel(String id, String path, String type, String picture_id) {
        this.id = id;
        this.path = path;
        this.type = type;
        this.picture_id = picture_id;
    }

    protected DataEntitiesCertificationPicModel(Parcel in) {
        this.id = in.readString();
        this.path = in.readString();
        this.type = in.readString();
        this.picture_id = in.readString();
    }

    public static final Creator<DataEntitiesCertificationPicModel> CREATOR = new Creator<DataEntitiesCertificationPicModel>() {
        @Override
        public DataEntitiesCertificationPicModel createFromParcel(Parcel source) {
            return new DataEntitiesCertificationPicModel(source);
        }

        @Override
        public DataEntitiesCertificationPicModel[] newArray(int size) {
            return new DataEntitiesCertificationPicModel[size];
        }
    };
}
