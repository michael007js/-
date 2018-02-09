package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by leilei on 2017/8/19.
 */

public class DataEntitiesCertificationModel implements Parcelable {
    public String auth_id;
    public String member_id;
    public String possessor;
    public String company_name;
    public List<DataEntitiesCertificationServiceModel> scope_service;
    public String manage_path;
    public String business_license;
    public List<DataEntitiesCertificationPicModel> shop_picture;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.auth_id);
        dest.writeString(this.member_id);
        dest.writeString(this.possessor);
        dest.writeString(this.company_name);
        dest.writeTypedList(this.scope_service);
        dest.writeString(this.manage_path);
        dest.writeString(this.business_license);
        dest.writeTypedList(this.shop_picture);
    }

    public DataEntitiesCertificationModel() {
    }

    protected DataEntitiesCertificationModel(Parcel in) {
        this.auth_id = in.readString();
        this.member_id = in.readString();
        this.possessor = in.readString();
        this.company_name = in.readString();
        this.scope_service = in.createTypedArrayList(DataEntitiesCertificationServiceModel.CREATOR);
        this.manage_path = in.readString();
        this.business_license = in.readString();
        this.shop_picture = in.createTypedArrayList(DataEntitiesCertificationPicModel.CREATOR);
    }

    public static final Creator<DataEntitiesCertificationModel> CREATOR = new Creator<DataEntitiesCertificationModel>() {
        @Override
        public DataEntitiesCertificationModel createFromParcel(Parcel source) {
            return new DataEntitiesCertificationModel(source);
        }

        @Override
        public DataEntitiesCertificationModel[] newArray(int size) {
            return new DataEntitiesCertificationModel[size];
        }
    };
}
