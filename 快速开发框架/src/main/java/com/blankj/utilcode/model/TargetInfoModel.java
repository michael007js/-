package com.blankj.utilcode.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.poisearch.IndoorData;
import com.amap.api.services.poisearch.Photo;
import com.amap.api.services.poisearch.PoiItemExtension;
import com.amap.api.services.poisearch.SubPoiItem;

import java.util.List;

/**
 * POI兴趣点模型
 * Created by leilei on 2017/4/20.
 */

public class TargetInfoModel implements Parcelable {
    public String provinceName;
    public String provinceCode;
    public String cityName;
    public int distance;
    public String cityCode;
    public String typeDes;
    public String typeCode;
    public String parkingType;
    public String businessArea;
    public String email;
    public LatLonPoint enter;
    public LatLonPoint exit;
    public IndoorData indoorData;
    public LatLonPoint latLonPoint;
    public List<Photo> photo;
    public PoiItemExtension poiExtension;
    public String postCode;
    public  List<SubPoiItem> subPois;
    public String shopId;
    public String snippet;
    public String tel;
    public String title;
    public String website;




    @Override
    public String toString() {
        return "TargetInfoModel{" +
                "\nprovinceName='" + provinceName + '\'' +
                "\ndistance='" + distance + '\'' +
                "\n provinceCode='" + provinceCode + '\'' +
                "\n cityName='" + cityName + '\'' +
                "\n cityCode='" + cityCode + '\'' +
                "\n typeDes='" + typeDes + '\'' +
                "\n typeCode='" + typeCode + '\'' +
                "\n parkingType='" + parkingType + '\'' +
                "\n businessArea='" + businessArea + '\'' +
                "\n email='" + email + '\'' +
                "\n enter=" + enter +
                "\n exit=" + exit +
                "\n indoorData=" + indoorData +
                "\n latLonPoint=" + latLonPoint +
                "\n photo=" + photo +
                "\n poiExtension=" + poiExtension +
                "\n postCode='" + postCode + '\'' +
                "\n subPois=" + subPois +
                "\n shopId='" + shopId + '\'' +
                "\n snippet='" + snippet + '\'' +
                "\n tel='" + tel + '\'' +
                "\n title='" + title + '\'' +
                "\n website='" + website + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.provinceName);
        dest.writeString(this.provinceCode);
        dest.writeString(this.cityName);
        dest.writeInt(this.distance);
        dest.writeString(this.cityCode);
        dest.writeString(this.typeDes);
        dest.writeString(this.typeCode);
        dest.writeString(this.parkingType);
        dest.writeString(this.businessArea);
        dest.writeString(this.email);
        dest.writeParcelable(this.enter, flags);
        dest.writeParcelable(this.exit, flags);
        dest.writeParcelable(this.indoorData, flags);
        dest.writeParcelable(this.latLonPoint, flags);
        dest.writeTypedList(this.photo);
        dest.writeParcelable(this.poiExtension, flags);
        dest.writeString(this.postCode);
        dest.writeTypedList(this.subPois);
        dest.writeString(this.shopId);
        dest.writeString(this.snippet);
        dest.writeString(this.tel);
        dest.writeString(this.title);
        dest.writeString(this.website);
    }

    public TargetInfoModel() {
    }

    protected TargetInfoModel(Parcel in) {
        this.provinceName = in.readString();
        this.provinceCode = in.readString();
        this.cityName = in.readString();
        this.distance = in.readInt();
        this.cityCode = in.readString();
        this.typeDes = in.readString();
        this.typeCode = in.readString();
        this.parkingType = in.readString();
        this.businessArea = in.readString();
        this.email = in.readString();
        this.enter = in.readParcelable(LatLonPoint.class.getClassLoader());
        this.exit = in.readParcelable(LatLonPoint.class.getClassLoader());
        this.indoorData = in.readParcelable(IndoorData.class.getClassLoader());
        this.latLonPoint = in.readParcelable(LatLonPoint.class.getClassLoader());
        this.photo = in.createTypedArrayList(Photo.CREATOR);
        this.poiExtension = in.readParcelable(PoiItemExtension.class.getClassLoader());
        this.postCode = in.readString();
        this.subPois = in.createTypedArrayList(SubPoiItem.CREATOR);
        this.shopId = in.readString();
        this.snippet = in.readString();
        this.tel = in.readString();
        this.title = in.readString();
        this.website = in.readString();
    }

    public static final Creator<TargetInfoModel> CREATOR = new Creator<TargetInfoModel>() {
        @Override
        public TargetInfoModel createFromParcel(Parcel source) {
            return new TargetInfoModel(source);
        }

        @Override
        public TargetInfoModel[] newArray(int size) {
            return new TargetInfoModel[size];
        }
    };
}
