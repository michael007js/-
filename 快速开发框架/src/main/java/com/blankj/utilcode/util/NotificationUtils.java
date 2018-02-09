package com.blankj.utilcode.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.blankj.utilcode.R;

import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * 通知栏
 * 参考 http://blog.csdn.net/u012124438/article/details/53574649
 * Created by leilei on 2017/5/13.
 */

public class NotificationUtils {
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private Context context;

    public NotificationUtils(Context context) {
        this.context = context;
        mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context);
    }

    /**
     * 创建通知
     *
     * @param title   标题
     * @param content 内容
     * @param ticker  标贴
     */
    public NotificationCompat.Builder create(String title, String content, String ticker) {
        return mBuilder.setContentTitle(title)//设置通知栏标题
                .setContentText(content) //设置通知栏显示内容
                .setTicker(ticker) //通知首次出现在通知栏，带上升动画效果的
                .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL)) //设置通知栏点击意图(默认)
                .setSmallIcon(R.drawable.ic_launcher);//设置通知小ICON
    }

    /**
     * 获取一个默认的 PendingIntent 对象
     *
     * @param flags flag参数如下:
     *              Notification.FLAG_SHOW_LIGHTS              //三色灯提醒，在使用三色灯提醒时候必须加该标志符
     *              Notification.FLAG_ONGOING_EVENT            //发起正在运行事件（活动中）
     *              Notification.FLAG_INSISTENT                //让声音、振动无限循环，直到用户响应 （取消或者打开）
     *              Notification.FLAG_ONLY_ALERT_ONCE          //发起Notification后，铃声和震动均只执行一次
     *              Notification.FLAG_AUTO_CANCEL              //用户单击通知后自动消失
     *              Notification.FLAG_NO_CLEAR                 //只有全部清除时，Notification才会清除 ，不清除该通知(QQ的通知无法清除，就是用的这个)
     *              Notification.FLAG_FOREGROUND_SERVICE       //表示正在运行的服务
     * @return
     */
    public PendingIntent getDefalutIntent(int flags) {
        return PendingIntent.getActivity(context, 1, new Intent(), flags);
    }


    /**
     * 获取一个用于启动 Activity 的 PendingIntent 对象
     *
     * @param context
     * @param requestCode
     * @param intent
     * @param flags       flags参数如下
     *                    FLAG_CANCEL_CURRENT:如果当前系统中已经存在一个相同的 PendingIntent 对象，那么就将先将已有的 PendingIntent 取消，
     *                    然后重新生成一个 PendingIntent 对象。
     *                    FLAG_NO_CREATE:如果当前系统中不存在相同的 PendingIntent 对象，系统将不会创建该 PendingIntent 对象而是直接返回 null 。
     *                    FLAG_ONE_SHOT:该 PendingIntent 只作用一次。
     *                    FLAG_UPDATE_CURRENT:如果系统中已存在该 PendingIntent 对象，那么系统将保留该 PendingIntent 对象，
     *                    但是会使用新的 Intent 来更新之前 PendingIntent 中的 Intent 对象数据，例如更新 Intent 中的 Extras 。
     * @return
     */
    public PendingIntent getActivityIntent(Context context, int requestCode, Intent intent, int flags) {
        return PendingIntent.getActivity(context, requestCode,intent, flags);
    }



    /**
     * 获取一个用于启动 Service 的 PendingIntent 对象
     *
     * @param context
     * @param requestCode
     * @param intent
     * @param flags       flags参数如下
     *                    FLAG_CANCEL_CURRENT:如果当前系统中已经存在一个相同的 PendingIntent 对象，那么就将先将已有的 PendingIntent 取消，
     *                    然后重新生成一个 PendingIntent 对象。
     *                    FLAG_NO_CREATE:如果当前系统中不存在相同的 PendingIntent 对象，系统将不会创建该 PendingIntent 对象而是直接返回 null 。
     *                    FLAG_ONE_SHOT:该 PendingIntent 只作用一次。
     *                    FLAG_UPDATE_CURRENT:如果系统中已存在该 PendingIntent 对象，那么系统将保留该 PendingIntent 对象，
     *                    但是会使用新的 Intent 来更新之前 PendingIntent 中的 Intent 对象数据，例如更新 Intent 中的 Extras 。
     * @return
     */
    public PendingIntent getServiceIntent(Context context, int requestCode, Intent intent, int flags) {
        return PendingIntent.getService(context, requestCode, intent, flags);
    }


    /**
     * 获取一个用于向 BroadcastReceiver 广播的 PendingIntent 对象
     * @param context
     * @param requestCode
     * @param intent
     * @param flags       flags参数如下
     *                    FLAG_CANCEL_CURRENT:如果当前系统中已经存在一个相同的 PendingIntent 对象，那么就将先将已有的 PendingIntent 取消，
     *                    然后重新生成一个 PendingIntent 对象。
     *                    FLAG_NO_CREATE:如果当前系统中不存在相同的 PendingIntent 对象，系统将不会创建该 PendingIntent 对象而是直接返回 null 。
     *                    FLAG_ONE_SHOT:该 PendingIntent 只作用一次。
     *                    FLAG_UPDATE_CURRENT:如果系统中已存在该 PendingIntent 对象，那么系统将保留该 PendingIntent 对象，
     *                    但是会使用新的 Intent 来更新之前 PendingIntent 中的 Intent 对象数据，例如更新 Intent 中的 Extras 。
     * @return
     */
    public static PendingIntent getBroadcastIntent(Context context, int requestCode, Intent intent, int flags){
        return PendingIntent.getBroadcast(context, requestCode, intent, flags);
    }





    //*********************************************************主要常用设置************************************************************************

    /**
     * 设置通知集合的数量
     *
     * @param number
     */
    public void setNumber(int number) {
        mBuilder.setNumber(number);
    }


    /**
     * 通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
     */
    public void setWhen() {
        mBuilder.setWhen(System.currentTimeMillis());
    }


    /**
     * 设置该通知优先级
     *
     * @param type type参数如下:
     *             Notification.PRIORITY_DEFAULT(优先级为0)
     *             Notification.PRIORITY_HIGH
     *             Notification.PRIORITY_LOW
     *             Notification.PRIORITY_MAX(优先级为2)
     *             Notification.PRIORITY_MIN(优先级为-2)
     */
    public void setPriority(int type) {
        mBuilder.setPriority(type);
    }


    /**
     * 设置这个标志当用户单击面板就可以让通知将自动取消
     *
     * @param b
     */
    public void setAutoCancel(boolean b) {
        mBuilder.setAutoCancel(b);
    }


    /**
     * 设置此通知为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐,文件下载等)或以某种方式正在等待
     * 使用该标记后你的通知栏无法被用户手动进行删除，只能通过代码进行删除，慎用
     *
     * @param b
     */
    public void setOngoing(boolean b) {
        mBuilder.setOngoing(b);
    }

    /**
     * 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
     *
     * @param type type参数如下:
     *             Notification.DEFAULT_ALL所有 需要权限uses-permission android:name="android.permission.VIBRATE"
     *             Notification.DEFAULT_VIBRATE 震动,需要权限uses-permission android:name="android.permission.VIBRATE"
     *             Notification.DEFAULT_SOUND 铃声
     *             Notification.DEFAULT_LIGHTS 呼吸灯
     *             也可以同时设置,用"|"拼接
     */
    public void setDefaults(int type) {
        mBuilder.setDefaults(type);
    }

    /**
     * 设置震动的时间 需要权限uses-permission android:name="android.permission.VIBRATE"
     *
     * @param l l为毫秒数组,如:new long[] {0,300,500,700},延迟0ms，然后振动300ms，在延迟500ms，接着在振动700ms。
     */
    public void setVibrate(long[] l) {
        mBuilder.setVibrate(l);

    }


    /**
     * 三色灯提醒
     *
     * @param ledARGB  灯光颜色
     * @param ledOnMS  点亮持续时间
     * @param ledOffMS 熄灭的时间
     *                 如:0xff00eeff, 500, 200
     */
    public void setLights(int ledARGB, int ledOnMS, int ledOffMS) {
        mBuilder.setLights(ledARGB, ledOnMS, ledOffMS);
    }


    /**
     * 自定义的铃声
     *
     * @param uri 如
     *            setSound(Uri.parse("file:///sdcard/dance.mp3"))自定义
     *            setSound(Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "5"))调用系统自带的铃声
     */
    public void setSound(Uri uri) {
        mBuilder.setSound(uri);
    }


    /**
     * 设置带进度条的通知
     * 此方法在4.0及以后版本才有用，如果为早期版本：需要自定义通知布局，其中包含ProgressBar视图
     * <p>
     * 如果为确定的进度条：调用setProgress(max, progress, false)来设置通知，
     * 在更新进度的时候在此发起通知更新progress，
     * 并且在下载完成后要移除进度条，
     * 通过调用setProgress(0, 0, false)既可。
     * 如果为不确定（持续活动）的进度条，
     * 这是在处理进度无法准确获知时显示活动正在持续，
     * 所以调用setProgress(0, 0, true) ，
     * 操作结束时，
     * 调用setProgress(0, 0, false)并更新通知以移除指示条
     *
     * @param max           进度条最大数值
     * @param progress      当前进度
     * @param indeterminate 表示进度是否不确定，true为不确定，false为确定
     */
    public void setProgress(int max, int progress, boolean indeterminate) {
        mBuilder.setProgress(max, progress, indeterminate);
    }

    //*********************************************************主要常用设置************************************************************************


    //***********************************************************主要操作**************************************************************************

    /**
     * 显示通知
     * 如果之前的通知还未被取消，则会直接更新该通知相关的属性；
     * 如果之前的通知已经被取消，则会重新创建一个新通知。
     *
     * @param id      自定义的通知ID
     * @param builder
     */
    public void notify(int id, NotificationCompat.Builder builder) {
        mNotificationManager.notify(id, builder.build());
    }

    /**
     * 取消通知
     *
     * @param id 自定义的通知ID
     */
    public void cancel(int id) {
        mNotificationManager.cancel(id);
    }


    /**
     * 清除所有通知
     */
    public void cancelAll() {
        mNotificationManager.cancelAll();
    }


    //***********************************************************主要操作**************************************************************************
}
