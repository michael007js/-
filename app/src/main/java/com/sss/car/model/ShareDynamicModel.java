package com.sss.car.model;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by leilei on 2017/9/9.
 */

public class ShareDynamicModel {
    public String trends_id;
    public String contents;
    public String city_path;
    public String create_time;
    public String trends_pid;
    public String member_id;
    public String looks;
    public String likes;
    public String transmit;
    public String face;
    public String username;
    public String day;
    public String month;
    public String week;
    public String goods_id;
    public String is_praise;
    public String comment_count;
    public List<String> picture=new ArrayList<>();
    public List<DymaicDetailsPraiseModel> likes_list=new ArrayList<>();
    public List<DymaicDetailsCommentModel> comment_list=new ArrayList<>();
    public List<ShareDynamic_GoodsModel>goods_data=new ArrayList<>();
}
