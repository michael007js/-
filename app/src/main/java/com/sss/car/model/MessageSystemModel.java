package com.sss.car.model;

/**
 * Created by leilei on 2017/12/12.
 */

public class MessageSystemModel {
    public String id;
    public String title;
    public String type;
    public String goods_type;
    public String send_time;
    public String contents;
    public String is_read;
    public String member_id;
    public String target;
    public String is_top;
    public String friend_id;

    @Override
    public String toString() {
        return "MessageSystemModel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", goods_type='" + goods_type + '\'' +
                ", send_time='" + send_time + '\'' +
                ", contents='" + contents + '\'' +
                ", is_read='" + is_read + '\'' +
                ", member_id='" + member_id + '\'' +
                ", target='" + target + '\'' +
                ", is_top='" + is_top + '\'' +
                ", friend_id='" + friend_id + '\'' +
                '}';
    }
}
