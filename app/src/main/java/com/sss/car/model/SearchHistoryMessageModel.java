package com.sss.car.model;

import io.rong.imlib.model.Message;

/**
 * Created by leilei on 2017/8/30.
 */

public class SearchHistoryMessageModel {
    public Message message;
    public String content;


    public SearchHistoryMessageModel(Message message, String content) {
        this.message = message;
        this.content = content;
    }
}
