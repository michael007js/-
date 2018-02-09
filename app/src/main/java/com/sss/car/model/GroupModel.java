package com.sss.car.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import static io.rong.imlib.statistics.UserData.picture;

/**
 * Created by leilei on 2017/8/29.
 */

public class GroupModel {

    public String group_id;
    public String account;
    public String name;
    public String code;
    public String picture;
    public String city_path;
    public String notice;//公告
    public String create_time;
    public String records; //保存到通讯录
    public String name_show;//是否显示群成员昵称  0是   1不是

    public String member_id;//群主
    public String shield;//消息免打扰  0正常  1免打扰
    public String state;//0正常  1被封禁
    public String status; //0自动加入  1需申请才能加入  2关闭加入
    public String remark;//我在本群的昵称

    @Override
    public String toString() {
        return "GroupModel{" +
                "group_id='" + group_id + '\'' +
                ", account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", picture='" + picture + '\'' +
                ", city_path='" + city_path + '\'' +
                ", notice='" + notice + '\'' +
                ", create_time='" + create_time + '\'' +
                ", records='" + records + '\'' +
                ", name_show='" + name_show + '\'' +
                ", member_id='" + member_id + '\'' +
                ", shield='" + shield + '\'' +
                ", state='" + state + '\'' +
                ", status='" + status + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
