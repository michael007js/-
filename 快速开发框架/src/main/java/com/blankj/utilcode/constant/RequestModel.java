package com.blankj.utilcode.constant;

import okhttp3.Call;

/**
 * Created by leilei on 2017/8/8.
 */

public class RequestModel {
    public String string;
    public Call call;

    public RequestModel(String string, Call call) {
        this.string = string;
        this.call = call;
    }
}
