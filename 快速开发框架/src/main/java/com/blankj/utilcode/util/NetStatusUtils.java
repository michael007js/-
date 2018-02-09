package com.blankj.utilcode.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.blankj.utilcode.dao.NetEventCallBack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 * 网络监视类
 * Created by leilei on 2017/7/31.
 */

public class NetStatusUtils {
    /**
     * 没有连接网络
     */
    public static final int NETWORK_NONE = -1;
    /**
     * 移动网络
     */
    public static final int NETWORK_MOBILE = 0;
    /**
     * 无线网络
     */
    public static final int NETWORK_WIFI = 1;

    /**
     * 网络卡顿
     */
    public static final int NETWORK_CONGESTED = 2;

    /**
     * ping网络失败
     */
    public static final int NETWORK_PING_FAIL = 3;

    /**
     * 网络通畅
     */
    public static final int NETWORK_PING_SUCCESS = 4;

    /**
     * ping异常中断
     */
    public static final int INTERRUPTED_EXCEPTION = 5;

    /**
     * IO异常
     */
    public static final int NETWORK_IO_EXCEPTION = 6;


    /**
     * 是否已经启动线程
     */
    public boolean isRunning = false;
    /**
     * ping开关
     */
    boolean pingSwitch = false;

    /**
     * 网络请求判断卡顿的时间
     */

    int congestedTime = 5000;

    /**
     * 网络卡顿次数
     */
    int networkCongestedCount = 0;
    /**
     * 线程池用于ping网络
     */
    ThreadPoolUtils threadPoolUtils = new ThreadPoolUtils(ThreadPoolUtils.FixedThread, 1);


    public int getNetWorkState(Context context) {
        // 得到连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                return NETWORK_WIFI;
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                return NETWORK_MOBILE;
            }
        } else {
            return NETWORK_NONE;
        }
        return NETWORK_NONE;
    }

    public void pingSwitch(boolean pingSwitch) {
        this.pingSwitch = pingSwitch;
    }


    Process p;
    InputStream input;
    BufferedReader in;
    StringBuffer stringBuffer;
    String content = "";

    public void ping(final Context context, final NetEventCallBack netEvevt) {
        if (isRunning == false) {
            isRunning = true;
            threadPoolUtils.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    if (pingSwitch == true) {
                        /*开始访问时间*/
                        long startTime = System.currentTimeMillis();
                        /*结束访问时间*/
                        try {
                            p = Runtime.getRuntime().exec("ping -c 1 -w 100 " + "www.baidu.com");
                            // 读取ping的内容，可不加。
                            input = p.getInputStream();
                            in = new BufferedReader(new InputStreamReader(input));
                            stringBuffer = new StringBuffer();
                            while ((content = in.readLine()) != null) {
                                stringBuffer.append(content);
                            }
                            long endTime = System.currentTimeMillis();
//                            LogUtils.e("SSS", "result content : " + stringBuffer.toString());
                            // PING的状态
                            if (p.waitFor() == 0) {
                                if (endTime-startTime > congestedTime) {
                                    if (networkCongestedCount > 6) {
                                        if (netEvevt != null) {
                                            netEvevt.onNetworkCongested(context, endTime-startTime);
                                        }
                                    }
                                } else {
                                    netEvevt.onNetworkPingSuccess(context, endTime-startTime);
                                    networkCongestedCount = 0;
                                }
                            } else {
                                if (netEvevt != null) {
                                    netEvevt.onNetworkPingFail(context, endTime-startTime);
                                }
                            }

                        } catch (IOException e) {
                            if (netEvevt != null) {
                                netEvevt.onIOException(context, e);
                            }
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            if (netEvevt != null) {
                                netEvevt.onInterruptedException(context, e);
                            }
                            e.printStackTrace();
                        } finally {
                            try {
                                in.close();
                                input.close();
                                p.destroy();
                                stringBuffer = null;
                            } catch (IOException e) {
                                if (netEvevt != null) {
                                    netEvevt.onIOException(context, e);
                                }
                                e.printStackTrace();
                            }

                        }
                    }
                }
            }, 1, 1, TimeUnit.SECONDS);
        }
    }
}






