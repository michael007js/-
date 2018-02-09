package com.sss.car.model;

import com.sss.car.adapter.SharePostDetailsCommentReplayModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/9/6.
 */

public class SharePostDetailsCommentModel {
    public String comment_id;
    public String contents;
    public String picture;
    public String create_time;
    public String comment_pid;
    public String community_id;
    public String member_id;
    public String likes;
    public String username;
    public String face;
    public String vehicle_name;
    public String is_likes;
    public List<SharePostDetailsCommentReplayModel> reply_list =new ArrayList<>();

    @Override
    public String toString() {
        return "SharePostDetailsCommentModel{" +
                "comment_id='" + comment_id + '\'' +
                ", contents='" + contents + '\'' +
                ", picture='" + picture + '\'' +
                ", create_time='" + create_time + '\'' +
                ", comment_pid='" + comment_pid + '\'' +
                ", community_id='" + community_id + '\'' +
                ", member_id='" + member_id + '\'' +
                ", likes='" + likes + '\'' +
                ", username='" + username + '\'' +
                ", face='" + face + '\'' +
                ", vehicle_name='" + vehicle_name + '\'' +
                ", is_likes='" + is_likes + '\'' +
                '}';
    }
}
