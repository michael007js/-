package com.sss.car.EventBusModel;

import com.sss.car.model.CreateGroupFriendAttentionFansRecentlyChatPublicModel;

/**
 * Created by leilei on 2017/8/30.
 */

public class ChooseCreateGroupFriendAttentionFansRecentlyChatPublicModel {
    public String type;
    public CreateGroupFriendAttentionFansRecentlyChatPublicModel model;
    public boolean isChoose;

    public ChooseCreateGroupFriendAttentionFansRecentlyChatPublicModel(String type, CreateGroupFriendAttentionFansRecentlyChatPublicModel model, boolean isChoose) {
        this.type = type;
        this.model = model;
        this.isChoose = isChoose;
    }
}
