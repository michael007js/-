package com.blankj.utilcode.HttpRequestLib.dao;


/**
 * 处理响应结果
 * Created by 61642 on 2018/4/16.
 */

public interface IHttpListener {

    /**
     * 成功回调
     * @param s
     */
    void onSuccess(String s);

    /**
     * 失败回调
     * @param responseCode
     * @param responseMessage
     */
    void onFail(int responseCode, String responseMessage);

}
