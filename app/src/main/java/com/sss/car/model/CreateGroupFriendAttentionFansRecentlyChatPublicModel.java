package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by leilei on 2017/8/29.
 */

public class CreateGroupFriendAttentionFansRecentlyChatPublicModel implements Parcelable {
    public String member_id;
    public String face;
    public String username;
    public String title;
    public boolean isChoose;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.member_id);
        dest.writeString(this.face);
        dest.writeString(this.username);
        dest.writeString(this.title);
        dest.writeByte(this.isChoose ? (byte) 1 : (byte) 0);
    }

    public CreateGroupFriendAttentionFansRecentlyChatPublicModel() {
    }

    protected CreateGroupFriendAttentionFansRecentlyChatPublicModel(Parcel in) {
        this.member_id = in.readString();
        this.face = in.readString();
        this.username = in.readString();
        this.title = in.readString();
        this.isChoose = in.readByte() != 0;
    }

    public static final Creator<CreateGroupFriendAttentionFansRecentlyChatPublicModel> CREATOR = new Creator<CreateGroupFriendAttentionFansRecentlyChatPublicModel>() {
        @Override
        public CreateGroupFriendAttentionFansRecentlyChatPublicModel createFromParcel(Parcel source) {
            return new CreateGroupFriendAttentionFansRecentlyChatPublicModel(source);
        }

        @Override
        public CreateGroupFriendAttentionFansRecentlyChatPublicModel[] newArray(int size) {
            return new CreateGroupFriendAttentionFansRecentlyChatPublicModel[size];
        }
    };

    @Override
    public String toString() {
        return "CreateGroupFriendAttentionFansRecentlyChatPublicModel{" +
                "member_id='" + member_id + '\'' +
                ", face='" + face + '\'' +
                ", username='" + username + '\'' +
                ", title='" + title + '\'' +
                ", isChoose=" + isChoose +
                '}';
    }
}
