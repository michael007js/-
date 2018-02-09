package com.sss.car.dao;

import com.sss.car.model.MessageInteractionManageGroupModel;

import java.util.List;

/**
 * Created by leilei on 2017/8/28.
 */

public interface MessageInteractionManageGroupClickCallBack {
    void onClickItem(int poistion, MessageInteractionManageGroupModel messageInteractionManageGroupModel, List<MessageInteractionManageGroupModel> list);
}
