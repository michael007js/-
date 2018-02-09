package com.sss.car.model;

/**
 * Created by leilei on 2017/8/31.
 */

public class SearchAddFriendModel {
    public String member_id;
    public String username;
    public String face;
    public String account;
    public String mobile;

    public SearchAddFriendModel(String member_id, String username, String face, String account, String mobile) {
        this.member_id = member_id;
        this.username = username;
        this.face = face;
        this.account = account;
        this.mobile = mobile;
    }
}
