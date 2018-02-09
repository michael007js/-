package com.blankj.utilcode.util;

/**
 * Created by leilei on 2017/5/19.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 发送广播简易工具类
 * Created by Liam on 2015/4/2.
 */
public class BroadcastUtils {

    /**
     * 构造函数设为private，防止外部实例化
     */
    private void BroadcastManager() {
    }

    /**
     * 注册广播接收器
     *
     * @param ctx       Context(不可空)
     * @param iReceiver IReceiver(不可空)
     * @param action    Intent.Action(不可空)
     */
    public static void registerReceiver(Context ctx, final IReceiver iReceiver, final String action) {
        if (ctx == null || iReceiver == null || action == null) throw new IllegalArgumentException("使用registerReceiver()注册广播时，参数Context、IReceiver和Action不能为空！");

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                iReceiver.onReceive(context, intent);
            }
        };
        IntentFilter filter = new IntentFilter(action);
        ctx.registerReceiver(receiver, filter);
    }

    /**
     * 发送广播
     *
     * @param ctx    Context(不可空)
     * @param i      Intent（可为null），不需要设置Action，请把Action作为参数传入
     * @param action Intent.Action（不可空）
     */
    public static void send(Context ctx, Intent i, final String action) {
        if (ctx == null || action == null) throw new IllegalArgumentException("使用send()发送广播时，参数Context和Action不能为空！");

        if (i == null) i = new Intent();
        i.setAction(action);
        ctx.sendBroadcast(i);
    }


    /**
     * 接收广播的页面需要实现，把接收后的操作覆写在OnReceive()方法里
     */
    public interface IReceiver {
        void onReceive(Context ctx, Intent intent);
    }

}