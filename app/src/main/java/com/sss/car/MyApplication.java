package com.sss.car;


import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.application.UtilCodeApplication;
import com.blankj.utilcode.util.ActivityManagerUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ProcessUtils;
import com.blankj.utilcode.util.SPUtils;
import com.networkbench.agent.impl.NBSAppAgent;
import com.sss.car.view.LoginAndRegister;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by leilei on 2017/8/7.
 */

public class MyApplication extends UtilCodeApplication {


    @Override
    public void onCreate() {
        super.onCreate();
        String processName = ProcessUtils.getProcessName(this);
        if (AppUtils.getAppPackageName(this).equals(processName)) {
//            initOnePxAlive();
            initFresco();
            initCameraConfig();
            repairAndroidOsFileUriExposedException();
            initJiGuang();
            initRongYun(this);
            initUMeng();
            NBSAppAgent.setLicenseKey("2d36e417272e46439774db3c0914c8e7").withLocationServiceEnabled(true).startInApplication(this.getApplicationContext());
        }
    }

    /**
     * 初始化友盟
     * 参数1:上下文，不能为空
     * 参数2:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
     * 参数3:Push推送业务的secret
     */
    void initUMeng() {
        PlatformConfig.setWeixin("wxfa869f12af50587a", "742ad17e92a2e814c821d82a9670e604");
        PlatformConfig.setQQZone("1106506737", "O6JLXLeQZEQCNVBL");
        PlatformConfig.setSinaWeibo("3805527398", "170b79e0aa523e10d80ff6a9824941fa", "http://sns.whalecloud.com");
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "5a5ec4c5b27b0a108700007d");
        UMConfigure.setLogEnabled(true);
    }
    /**
     * 初始化极光
     */
    void initJiGuang() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    /**
     * 设置别名
     *
     * @param account
     */
    public static void initJiGuangUser(String account, Context context) {
        JPushInterface.setAlias(context, account, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                LogUtils.e(s);
                LogUtils.e(i);
            }
        });
    }

    /**
     * 初始化融云
     */
    void initRongYun(final Context context) {
        RongIM.init(this);
        RongIM.setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
            @Override
            public void onChanged(ConnectionStatus connectionStatus) {
                switch (connectionStatus) {
                    case CONNECTED://连接成功。
                        break;
                    case DISCONNECTED://断开连接。
                        break;
                    case CONNECTING://连接中。
                        break;
                    case NETWORK_UNAVAILABLE://网络不可用。
                        break;
                    case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
                        new SPUtils(context,Config.defaultFileName,MODE_PRIVATE).put("password","");
                        ActivityManagerUtils.getActivityManager().finishAllActivity();
                        startActivity(new Intent(context, LoginAndRegister.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                                .putExtra("isShowBack", false)
                                .putExtra("KICKED_OFFLINE_BY_OTHER_CLIENT", true));
                        break;
                }
            }
        });
    }

}



