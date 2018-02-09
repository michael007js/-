package com.sss.car.dao;

import android.view.View;
import android.view.ViewGroup;

import com.sss.car.model.Community_Userinfo_Posts_Model;

import java.util.List;

/**
 * Created by leilei on 2017/9/4.
 */

public interface Community_Userinfo_PostsOperationCallBack {

    void onType(Community_Userinfo_Posts_Model model, List<Community_Userinfo_Posts_Model> list, int position);

    void onClickItem(Community_Userinfo_Posts_Model model, List<Community_Userinfo_Posts_Model> list, int position);

    void onComment(Community_Userinfo_Posts_Model model, List<Community_Userinfo_Posts_Model> list, int position);

    void onShare(Community_Userinfo_Posts_Model model, List<Community_Userinfo_Posts_Model> list, int position);

    void onCollection(Community_Userinfo_Posts_Model model, List<Community_Userinfo_Posts_Model> list, int position);
}
