package com.sss.car.model;

import java.util.List;

/**
 * Created by leilei on 2017/8/25.
 */

public class DymaicModel {
    public String trends_pid;//动态转发,被转发者的id

    public String trends_id;
    public String contents;
    public List<String> picture;
    public String city_path;
    public String create_time;
    public String member_id;
    public String looks;
    public String transmit;
    public String likes;
    public String face;
    public String username;
    public String is_praise;
    public String comment_count;
    public String day;
    public String month;
    public String week;
    public List<ShareDymaicCommentListModel> comment_list;
    public List<String> likes_list;
}
