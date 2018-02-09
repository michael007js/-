package com.blankj.utilcode.dao;

import android.content.Context;

import java.io.IOException;

/**
 * Created by leilei on 2017/7/31.
 */

public interface NetEventCallBack {
    /**
     * netMobile:-1没有连接网络,0移动网络, 1无线网络
     * @param context
     * @param netMobile
     */
     void onNetChange(Context context, int netMobile);

    /**
     * 网络卡顿
     * @param millionTime
     */
    void onNetworkCongested(Context context,long millionTime);

    /**
     * ping网络失败
     * @param millionTime
     */
    void onNetworkPingFail(Context context,long millionTime);

    /**
     * 网络通畅
     */
    void onNetworkPingSuccess(Context context,long millionTime);

    /**
     * ping异常中断
     * @param e
     */
    void onInterruptedException(Context context,InterruptedException e);

    /**
     * IO异常
     * @param e
     */
    void onIOException(Context context,IOException e);
}