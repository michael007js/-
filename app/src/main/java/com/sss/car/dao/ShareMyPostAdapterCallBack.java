package com.sss.car.dao;

import com.sss.car.model.Community_Userinfo_Posts_Model;

import java.util.List;


/**
 * Created by leilei on 2017/9/5.
 */

public interface ShareMyPostAdapterCallBack {

    void onClickItem(int position, Community_Userinfo_Posts_Model model, List<Community_Userinfo_Posts_Model> list);

    void onClickType(int position, Community_Userinfo_Posts_Model model, List<Community_Userinfo_Posts_Model> list);

    void onDelete(int position, Community_Userinfo_Posts_Model model, List<Community_Userinfo_Posts_Model> list);

    void onShare(int position, Community_Userinfo_Posts_Model model, List<Community_Userinfo_Posts_Model> list);

    void onComment(int position, Community_Userinfo_Posts_Model model, List<Community_Userinfo_Posts_Model> list);

    void onCollect(int position, Community_Userinfo_Posts_Model model, List<Community_Userinfo_Posts_Model> list);


}
