package com.blankj.utilcode.database.table;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * 经纬度表
 * Created by leilei on 2017/5/18.
 */



public class SSS_latlng extends DataSupport implements Parcelable {
    private Long id;
    private double lai;
    private double lng;

    public SSS_latlng(Long id, double lai, double lng) {
        this.id = id;
        this.lai = lai;
        this.lng = lng;
    }

    public SSS_latlng() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLai() {
        return lai;
    }

    public void setLai(double lai) {
        this.lai = lai;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "SSS_latlng{" +
                "id=" + id +
                ", lai=" + lai +
                ", lng=" + lng +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeDouble(this.lai);
        dest.writeDouble(this.lng);
    }

    protected SSS_latlng(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.lai = in.readDouble();
        this.lng = in.readDouble();
    }

    public static final Creator<SSS_latlng> CREATOR = new Creator<SSS_latlng>() {
        @Override
        public SSS_latlng createFromParcel(Parcel source) {
            return new SSS_latlng(source);
        }

        @Override
        public SSS_latlng[] newArray(int size) {
            return new SSS_latlng[size];
        }
    };
}