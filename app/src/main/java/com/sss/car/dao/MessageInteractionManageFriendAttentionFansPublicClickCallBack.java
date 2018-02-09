package com.sss.car.dao;

import com.sss.car.model.MessageInteractionManageFriendAttentionFansPublicModel;

import java.util.List;

/**
 * Created by leilei on 2017/8/28.
 */

public interface MessageInteractionManageFriendAttentionFansPublicClickCallBack {
    void onDelete(int poistion, MessageInteractionManageFriendAttentionFansPublicModel messageInteractionManageFriendAttentionFansPublicModel, List<MessageInteractionManageFriendAttentionFansPublicModel> list);

    void onClick(int poistion, MessageInteractionManageFriendAttentionFansPublicModel messageInteractionManageFriendAttentionFansPublicModel, List<MessageInteractionManageFriendAttentionFansPublicModel> list);


}
