package com.sss.car.dao;

import android.support.v7.widget.RecyclerView;

import com.sss.car.model.CreateGroupFriendAttentionFansRecentlyChatPublicModel;

import java.util.List;

/**
 * Created by leilei on 2017/8/29.
 */

public interface CreateGroupFriendAttentionFansRecentlyChatClickCallBack {

    void clickChanged(boolean isChoose, int position, CreateGroupFriendAttentionFansRecentlyChatPublicModel createGroupFriendAttentionFansRecentlyChatPublicModel, RecyclerView.ViewHolder holder);

}
