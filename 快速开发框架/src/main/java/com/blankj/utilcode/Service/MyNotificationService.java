package com.blankj.utilcode.Service;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;

import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.LogUtils;

/**
 <service android:name="com.blankj.utilcode.Service.MyNotificationService"
 android:label="@string/app_name"
 android:permission="android.permission.BIND_NOTIFICATION_LISTENER_SERVICE">
 <intent-filter>
 <action android:name="android.service.notification.NotificationListenerService" />
 </intent-filter>
 </service>
 *
 *
 * Created by leilei on 2017/8/10.
 */

@SuppressLint("OverrideAbstract")
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MyNotificationService extends NotificationListenerService {
    @Override
    //当有新通知到来时会回调
    public void onNotificationPosted(StatusBarNotification sbn) {
        LogUtils.e(sbn.toString());
        APPOftenUtils.initRongYunPushService(this);
    }

    //当有通知移除时会回调
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        APPOftenUtils.initRongYunPushService(this);
    }

    //可用的并且和通知管理器连接成功时回调。
    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
    }


    private void toggleNotificationListenerService() {
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(this, MyNotificationService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(this, MyNotificationService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
}
