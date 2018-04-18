package com.blankj.utilcode.HttpRequestLib.biz;

import android.os.Handler;
import android.os.Looper;

import com.blankj.utilcode.HttpRequestLib.dao.IDataListener;
import com.blankj.utilcode.HttpRequestLib.dao.IHttpListener;

/**
 * 负责从子线程转换到UI线程
 * Created by 61642 on 2018/4/17.
 */

public class HttpThreadChange implements IHttpListener {


    /**
     * 数据回传接口
     */
    IDataListener iDataListener;


    /**
     * 用于转换线程，操作UI
     */
    private Handler handler=new Handler(Looper.getMainLooper());

    public HttpThreadChange(IDataListener iDataListener) {
        this.iDataListener = iDataListener;
    }

    @Override
    public void onSuccess(final String s) {
        if (iDataListener != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    iDataListener.onSuccess(s);
                    handler.removeCallbacksAndMessages(null);
                }
            });

        }
    }

    @Override
    public void onFail(final int responseCode, final String responseMessage) {
        if (iDataListener != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    iDataListener.onFail(responseCode, responseMessage);
                    handler.removeCallbacksAndMessages(null);
                }
            });

        }
    }



}
