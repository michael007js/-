package com.blankj.utilcode.HttpRequestLib;


import com.blankj.utilcode.HttpRequestLib.biz.HttpTask;
import com.blankj.utilcode.HttpRequestLib.biz.HttpThreadChange;
import com.blankj.utilcode.HttpRequestLib.dao.IDataListener;
import com.blankj.utilcode.HttpRequestLib.dao.IHttpListener;
import com.blankj.utilcode.HttpRequestLib.dao.IHttpService;
import com.blankj.utilcode.HttpRequestLib.engline.HttpService;

import java.util.Map;

/**
 * Http请求工具类
 * Created by 61642 on 2018/4/17.
 */

public class HttpRequestUtils {

    /**
     * Post发送String
     *
     * @param timeOut       超时时间
     * @param requestInfo   请求参数
     * @param url           请求地址
     * @param iDataListener 返回接口
     */
    public static void doPostString(long timeOut, String requestInfo, String url, IDataListener iDataListener) {
        IHttpService iHttpService = new HttpService();
        IHttpListener iHttpListener = new HttpThreadChange(iDataListener);
        HttpTask httpTask = new HttpTask(requestInfo, timeOut, HttpRequestType.Post_String, url, iHttpService, iHttpListener);
        ThreadPoolManage.getInstance().setTimeOut(timeOut).execute(httpTask);
    }

    /**
     * Post发送键值对
     *
     * @param timeOut       超时时间
     * @param requestInfo   请求参数
     * @param url           请求地址
     * @param iDataListener 返回接口
     */
    public static void doPostKeyValue(long timeOut, Map<String, String> requestInfo, String url, IDataListener iDataListener) {
        IHttpService iHttpService = new HttpService();
        IHttpListener iHttpListener = new HttpThreadChange(iDataListener);
        HttpTask httpTask = new HttpTask(requestInfo, timeOut, HttpRequestType.Post_Key_Value, url, iHttpService, iHttpListener);
        ThreadPoolManage.getInstance().setTimeOut(timeOut).execute(httpTask);
    }

    /**
     * get请求
     *
     * @param timeOut       超时时间
     * @param url           请求地址
     * @param iDataListener 返回接口
     */
    public static void doGet(long timeOut, String url, IDataListener iDataListener) {
        IHttpService iHttpService = new HttpService();
        IHttpListener iHttpListener = new HttpThreadChange(iDataListener);
        HttpTask httpTask = new HttpTask(timeOut, HttpRequestType.Get, url, iHttpService, iHttpListener);
        ThreadPoolManage.getInstance().setTimeOut(timeOut).execute(httpTask);
    }

}
