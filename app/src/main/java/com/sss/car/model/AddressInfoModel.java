package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by leilei on 2017/8/19.
 */

public class AddressInfoModel implements Parcelable {
    public String address_id;
    public String mobile;
    public String province;
    public String city;
    public String county;
    public String address;
    public String recipients;
    public String create_time;
    public String member_id;
    public String is_default;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.address_id);
        dest.writeString(this.mobile);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.county);
        dest.writeString(this.address);
        dest.writeString(this.recipients);
        dest.writeString(this.create_time);
        dest.writeString(this.member_id);
        dest.writeString(this.is_default);
    }

    public AddressInfoModel() {
    }

    protected AddressInfoModel(Parcel in) {
        this.address_id = in.readString();
        this.mobile = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.county = in.readString();
        this.address = in.readString();
        this.recipients = in.readString();
        this.create_time = in.readString();
        this.member_id = in.readString();
        this.is_default = in.readString();
    }

    public static final Creator<AddressInfoModel> CREATOR = new Creator<AddressInfoModel>() {
        @Override
        public AddressInfoModel createFromParcel(Parcel source) {
            return new AddressInfoModel(source);
        }

        @Override
        public AddressInfoModel[] newArray(int size) {
            return new AddressInfoModel[size];
        }
    };
}
