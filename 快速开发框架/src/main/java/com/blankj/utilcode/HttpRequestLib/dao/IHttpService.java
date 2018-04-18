package com.blankj.utilcode.HttpRequestLib.dao;


import java.util.Map;

/**
 * 处理请求
 * Created by 61642 on 2018/4/16.
 */

public interface IHttpService {

    /**
     * 设置超时时间
     * @param time
     */
    void setTimeOut(long time);

    /**
     * 设置请求地址
     *
     * @param url
     */
    void setUrl(String url);

    /**
     * 设置请求数据
     *
     * @param requestData
     */
    void setRequestData(String requestData);

    /**
     * 设置请求数据
     *
     * @param requestData
     */
    void setRequestData(Map<String ,String> requestData);

    /**
     * 提交请求
     */
    void execute(int httpRequestType);

    /**
     * 设置数据处理回调监听
     *
     * @param iHttpListener
     */
    void setIHttpListener(IHttpListener iHttpListener);
}
