package com.sss.car.dao;

import com.sss.car.model.MessageChatListModel;

import java.util.List;

/**
 * Created by leilei on 2017/8/23.
 */

public interface MessageChatListOperationCallBack {
    void onClickMessage(int position, MessageChatListModel messageChatListModel, List<MessageChatListModel> list);

    void onLongClickMessage(int position, MessageChatListModel messageChatListModel, List<MessageChatListModel> list);
}
