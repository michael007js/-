package com.blankj.utilcode.HttpRequestLib.biz;


import com.blankj.utilcode.HttpRequestLib.dao.IHttpListener;
import com.blankj.utilcode.HttpRequestLib.dao.IHttpService;

import java.util.Map;

/**
 * Http请求任务类
 * Created by 61642 on 2018/4/16.
 */

public class HttpTask implements Runnable {

    private IHttpService iHttpService;
    int  httpRequestType;


    /**
     * POST String
     * @param requestInfo
     * @param timeOut
     * @param httpRequestType
     * @param url
     * @param iHttpService
     * @param iHttpListener
     */
    public HttpTask(String requestInfo,long timeOut,int httpRequestType, String url, IHttpService iHttpService, IHttpListener iHttpListener) {
        this.iHttpService = iHttpService;
        this.httpRequestType=httpRequestType;
        iHttpService.setUrl(url);
        iHttpService.setTimeOut(timeOut);
        iHttpService.setIHttpListener(iHttpListener);
        iHttpService.setRequestData(requestInfo);
    }

    /**
     * Get
     * @param timeOut
     * @param httpRequestType
     * @param url
     * @param iHttpService
     * @param iHttpListener
     */
    public HttpTask(long timeOut,int httpRequestType, String url, IHttpService iHttpService, IHttpListener iHttpListener) {
        this.iHttpService = iHttpService;
        this.httpRequestType=httpRequestType;
        iHttpService.setUrl(url);
        iHttpService.setTimeOut(timeOut);
        iHttpService.setIHttpListener(iHttpListener);
    }

    /**
     * POST KV
     * @param requestInfo
     * @param timeOut
     * @param httpRequestType
     * @param url
     * @param iHttpService
     * @param iHttpListener
     */
    public HttpTask(Map<String ,String> requestInfo, long timeOut, int httpRequestType, String url, IHttpService iHttpService, IHttpListener iHttpListener) {
        this.iHttpService = iHttpService;
        this.httpRequestType=httpRequestType;
        iHttpService.setUrl(url);
        iHttpService.setTimeOut(timeOut);
        iHttpService.setIHttpListener(iHttpListener);
        iHttpService.setRequestData(requestInfo);
    }



    @Override
    public void run() {
        iHttpService.execute(httpRequestType);
    }
}
