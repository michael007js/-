package com.sss.car.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/9/2.
 */

public class DymaicDetailsModel {
    public String trends_id;
    public String contents;
    public String city_path;
    public String create_time;
    public String member_id;
    public String looks;
    public String likes;
    public String username;
    public String comment_count;
    public String face;
    public String transmit ;
    public String is_praise;
    public List<String> picture=new ArrayList<>();
    public List<DymaicDetailsPraiseModel> likes_list =new ArrayList<>();
    public List<DymaicDetailsCommentModel>comment_list =new ArrayList<>();

}
