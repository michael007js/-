package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by leilei on 2017/10/20.
 */

public class PushSOSHelperFromBuyerModel implements Parcelable {
    public String status;
    public String type;
    public String sos_id;
    public String recipients;
    public String start_time;
    public String title;
    public String mobile;
    public int price;
    public String credit;
    public String damages;
    public String vehicle_name;
    public String member_id;
    public double lat;
    public double lng;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.status);
        dest.writeString(this.type);
        dest.writeString(this.sos_id);
        dest.writeString(this.recipients);
        dest.writeString(this.start_time);
        dest.writeString(this.title);
        dest.writeString(this.mobile);
        dest.writeInt(this.price);
        dest.writeString(this.credit);
        dest.writeString(this.damages);
        dest.writeString(this.vehicle_name);
        dest.writeString(this.member_id);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
    }

    public PushSOSHelperFromBuyerModel() {
    }

    protected PushSOSHelperFromBuyerModel(Parcel in) {
        this.status = in.readString();
        this.type = in.readString();
        this.sos_id = in.readString();
        this.recipients = in.readString();
        this.start_time = in.readString();
        this.title = in.readString();
        this.mobile = in.readString();
        this.price = in.readInt();
        this.credit = in.readString();
        this.damages = in.readString();
        this.vehicle_name = in.readString();
        this.member_id = in.readString();
        this.lat = in.readDouble();
        this.lng = in.readDouble();
    }

    public static final Creator<PushSOSHelperFromBuyerModel> CREATOR = new Creator<PushSOSHelperFromBuyerModel>() {
        @Override
        public PushSOSHelperFromBuyerModel createFromParcel(Parcel source) {
            return new PushSOSHelperFromBuyerModel(source);
        }

        @Override
        public PushSOSHelperFromBuyerModel[] newArray(int size) {
            return new PushSOSHelperFromBuyerModel[size];
        }
    };
}
