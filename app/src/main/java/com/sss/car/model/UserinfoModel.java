package com.sss.car.model;

/**
 * Created by leilei on 2017/8/31.
 */

public class UserinfoModel {
    public String member_id;
    public String face;
    public String username;
    public String is_auth;
    public String credit;
    public String last_time;
    public String auth_type;
    public String friend;
    public String shop_id;
    public String account;
    public String is_special;
    public String remark;
    public String status;
    public String sex;


    public UserinfoModel(String member_id, String face, String username, String is_auth, String credit, String last_time, String auth_type, String friend, String shop_id,String account,String is_special,String remark,String status,String sex) {
        this.member_id = member_id;
        this.face = face;
        this.username = username;
        this.is_auth = is_auth;
        this.credit = credit;
        this.last_time = last_time;
        this.auth_type = auth_type;
        this.friend = friend;
        this.shop_id = shop_id;
        this.account=account;
        this.is_special=is_special;
        this.remark=remark;
        this.status=status;
        this.sex=sex;
    }

    @Override
    public String toString() {
        return "UserinfoModel{" +
                "member_id='" + member_id + '\'' +
                ", face='" + face + '\'' +
                ", username='" + username + '\'' +
                ", is_auth='" + is_auth + '\'' +
                ", credit='" + credit + '\'' +
                ", last_time='" + last_time + '\'' +
                ", auth_type='" + auth_type + '\'' +
                ", friend='" + friend + '\'' +
                ", shop_id='" + shop_id + '\'' +
                ", account='" + account + '\'' +
                ", is_special='" + is_special + '\'' +
                ", remark='" + remark + '\'' +
                ", status='" + status + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
