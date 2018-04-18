package com.blankj.utilcode.HttpRequestLib.dao;

/**
 * 用于回调给调用层
 * Created by 61642 on 2018/4/16.
 */

public interface IDataListener {
    /**
     * 返回成功
     * @param response
     */
    void onSuccess(String response);

    /**
     * 返回失败
     */
    void onFail(int responseCode, String responseMessage);
}
