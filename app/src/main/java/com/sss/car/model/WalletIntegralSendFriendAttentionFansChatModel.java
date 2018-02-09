package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by leilei on 2017/10/25.
 */

public class WalletIntegralSendFriendAttentionFansChatModel implements Parcelable {
    public String member_id;
    public String face;
    public String username;
    public boolean isChoose=false;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.member_id);
        dest.writeString(this.face);
        dest.writeString(this.username);
        dest.writeByte(this.isChoose ? (byte) 1 : (byte) 0);
    }

    public WalletIntegralSendFriendAttentionFansChatModel() {
    }

    protected WalletIntegralSendFriendAttentionFansChatModel(Parcel in) {
        this.member_id = in.readString();
        this.face = in.readString();
        this.username = in.readString();
        this.isChoose = in.readByte() != 0;
    }

    public static final Creator<WalletIntegralSendFriendAttentionFansChatModel> CREATOR = new Creator<WalletIntegralSendFriendAttentionFansChatModel>() {
        @Override
        public WalletIntegralSendFriendAttentionFansChatModel createFromParcel(Parcel source) {
            return new WalletIntegralSendFriendAttentionFansChatModel(source);
        }

        @Override
        public WalletIntegralSendFriendAttentionFansChatModel[] newArray(int size) {
            return new WalletIntegralSendFriendAttentionFansChatModel[size];
        }
    };
}
