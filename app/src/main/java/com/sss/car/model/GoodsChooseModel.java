package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/9/25.
 */

public class GoodsChooseModel implements Parcelable {
    public String goods_id;
    public String title;
    public String slogan;
    public String master_map;
    public String cost_price;
    public String price;
    public String start_time;
    public String end_time;
    public String likes;
    public String share;
    public String sell;
    public String member_id;
    public String shop_id;
    public String distance;
    public String is_like;
   public List<String > picture=new ArrayList<>();
    public List<String > photo=new ArrayList<>();
    public List<GoodsChooseSizeName> size_name=new ArrayList<>();
    public List<GoodsChooseSizeData> size_dat=new ArrayList<>();


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.goods_id);
        dest.writeString(this.title);
        dest.writeString(this.slogan);
        dest.writeString(this.master_map);
        dest.writeString(this.cost_price);
        dest.writeString(this.price);
        dest.writeString(this.start_time);
        dest.writeString(this.end_time);
        dest.writeString(this.likes);
        dest.writeString(this.share);
        dest.writeString(this.sell);
        dest.writeString(this.member_id);
        dest.writeString(this.shop_id);
        dest.writeString(this.distance);
        dest.writeString(this.is_like);
        dest.writeStringList(this.picture);
        dest.writeStringList(this.photo);
        dest.writeList(this.size_name);
        dest.writeTypedList(this.size_dat);
    }

    public GoodsChooseModel() {
    }

    protected GoodsChooseModel(Parcel in) {
        this.goods_id = in.readString();
        this.title = in.readString();
        this.slogan = in.readString();
        this.master_map = in.readString();
        this.cost_price = in.readString();
        this.price = in.readString();
        this.start_time = in.readString();
        this.end_time = in.readString();
        this.likes = in.readString();
        this.share = in.readString();
        this.sell = in.readString();
        this.member_id = in.readString();
        this.shop_id = in.readString();
        this.distance = in.readString();
        this.is_like = in.readString();
        this.picture = in.createStringArrayList();
        this.photo = in.createStringArrayList();
        this.size_name = new ArrayList<GoodsChooseSizeName>();
        in.readList(this.size_name, GoodsChooseSizeName.class.getClassLoader());
        this.size_dat = in.createTypedArrayList(GoodsChooseSizeData.CREATOR);
    }

    public static final Creator<GoodsChooseModel> CREATOR = new Creator<GoodsChooseModel>() {
        @Override
        public GoodsChooseModel createFromParcel(Parcel source) {
            return new GoodsChooseModel(source);
        }

        @Override
        public GoodsChooseModel[] newArray(int size) {
            return new GoodsChooseModel[size];
        }
    };
}
