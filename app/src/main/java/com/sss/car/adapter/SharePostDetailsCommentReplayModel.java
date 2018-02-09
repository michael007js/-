package com.sss.car.adapter;

/**
 * Created by leilei on 2017/9/6.
 */

public class SharePostDetailsCommentReplayModel {

    public String comment_id;
    public String contents;
    public String member_id;
    public String username;
    public String create_time;

    @Override
    public String toString() {
        return "SharePostDetailsCommentReplayModel{" +
                "comment_id='" + comment_id + '\'' +
                ", contents='" + contents + '\'' +
                ", member_id='" + member_id + '\'' +
                ", username='" + username + '\'' +
                ", create_time='" + create_time + '\'' +
                '}';
    }
}
