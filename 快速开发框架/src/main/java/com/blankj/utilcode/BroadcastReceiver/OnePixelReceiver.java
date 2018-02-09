package com.blankj.utilcode.BroadcastReceiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.blankj.utilcode.activity.OnePiexlActivity;
import com.blankj.utilcode.util.LogUtils;

/**
 *
 <receiver android:name="com.blankj.utilcode.BroadcastReceiver.OnePixelReceiver" >
 <intent-filter>
 <action android:name="android.intent.action.BOOT_COMPLETED" />
 <action android:name="android.intent.action.SCREEN_ON" />
 <action android:name="android.intent.action.SCREEN_OFF" />
 <category android:name="android.intent.category.LAUNCHER" />
 </intent-filter>
 </receiver>
 * Created by Administrator on 2017/7/10.
 * 监听屏幕状态与开机自启的广播
 */
public class OnePixelReceiver extends BroadcastReceiver {
    private static OnePixelReceiver receiver;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Intent it = new Intent(context, OnePiexlActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(it);
            LogUtils.i("1px--screen off-");
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            context.sendBroadcast(new Intent("finish activity"));
            LogUtils.i("1px--screen on-");
        }
    }

    public static void register1pxReceiver(Context context) {
        if (receiver == null) {
            receiver = new OnePixelReceiver();
        }
        context.registerReceiver(receiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        context.registerReceiver(receiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
    }

    public static void unregister1pxReceiver(Context context) {
        context.unregisterReceiver(receiver);
    }
}