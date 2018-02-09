package com.sss.car.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/9/4.
 */

public class Community_Userinfo_Posts_Model {

    public List<String> picture=new ArrayList<>();
    public String community_id;  //文章ID
    public String title;
    public String create_time;
    public String cate_id;  //分类ID
    public String member_id;
    public String share; //  分享number
    public String is_top; // 是否推荐
    public String is_hot;  //  是否热门
    public String is_essence;//是否精华
    public String username;
    public String face;
    public String day;
    public String month;
    public String week; // 星期几，不同颜色
    public String vehicle_name;// 车名称
    public String cate_name;
    public String is_collect;// 是否收藏
    public String collect_count;// 收藏数量
    public String comment_count; // 评论数量

    @Override
    public String toString() {
        return "Community_Userinfo_Posts_Model{" +
                "picture=" + picture +
                ", community_id='" + community_id + '\'' +
                ", title='" + title + '\'' +
                ", create_time='" + create_time + '\'' +
                ", cate_id='" + cate_id + '\'' +
                ", member_id='" + member_id + '\'' +
                ", share='" + share + '\'' +
                ", is_top='" + is_top + '\'' +
                ", is_hot='" + is_hot + '\'' +
                ", is_essence='" + is_essence + '\'' +
                ", username='" + username + '\'' +
                ", face='" + face + '\'' +
                ", day='" + day + '\'' +
                ", month='" + month + '\'' +
                ", week='" + week + '\'' +
                ", vehicle_name='" + vehicle_name + '\'' +
                ", cate_name='" + cate_name + '\'' +
                ", is_collect='" + is_collect + '\'' +
                ", collect_count='" + collect_count + '\'' +
                ", comment_count='" + comment_count + '\'' +
                '}';
    }
}
