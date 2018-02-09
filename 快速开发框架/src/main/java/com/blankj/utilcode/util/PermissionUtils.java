package com.blankj.utilcode.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.blankj.utilcode.activity.PermissionsActivity;
import com.blankj.utilcode.dao.PermissionListener;

import java.util.List;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;


/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2017/04/16
 *     desc  : 权限相关工具类
 * </pre>
 */
public final class PermissionUtils {
    //访问登记属性，读取或写入登记check-in数据库属性表的权限
    public static String ACCESS_CHECKIN_PROPERTIES = Manifest.permission.ACCESS_CHECKIN_PROPERTIES;

    //获取错略位置，通过WiFi或移动基站的方式获取用户错略的经纬度信息，定位精度大概误差在30~1500米
    public static String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    //获取精确位置,通过GPS芯片接收卫星的定位信息，定位精度达10米以内
    public static String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;

    //用于申请调用A-GPS模块
    public static String ACCESS_LOCATION_EXTRA_COMMANDS = Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS;

    //获取网络状态，获取网络信息状态，如当前的网络连接是否有效
    public static String ACCESS_NETWORK_STATE = Manifest.permission.ACCESS_NETWORK_STATE;

    //访问用户的指纹信息
    public static String USE_FINGERPRINT = Manifest.permission.USE_FINGERPRINT;

    //获取WiFi状态，获取当前WiFi接入的状态以及WLAN热点的信息
    public static String ACCESS_WIFI_STATE = Manifest.permission.ACCESS_WIFI_STATE;

    //获取账户验证信息，主要为GMail账户信息，只有系统级进程才能访问的权限
    public static String ACCOUNT_MANAGER = Manifest.permission.ACCOUNT_MANAGER;

    //获取电池电量统计信息
    public static String BATTERY_STATS = Manifest.permission.BATTERY_STATS;

    //绑定小插件,允许一个程序告诉appWidget服务需要访问小插件的数据库，只有非常少的应用才用到此权限
    public static String BIND_APPWIDGET = Manifest.permission.BIND_APPWIDGET;

    //绑定设备管理,请求系统管理员接收者receiver，只有系统才能使用
    public static String BIND_DEVICE_ADMIN = Manifest.permission.BIND_DEVICE_ADMIN;

    //绑定输入法,请求InputMethodService服务，只有系统才能使用
    public static String BIND_INPUT_METHOD = Manifest.permission.BIND_INPUT_METHOD;

    //绑定RemoteView,必须通过RemoteViewsService服务来请求，只有系统才能用
    public static String BIND_REMOTEVIEWS = Manifest.permission.BIND_REMOTEVIEWS;

    //绑定壁纸，必须通过WallpaperService服务来请求，只有系统才能用
    public static String BIND_WALLPAPER = Manifest.permission.BIND_WALLPAPER;

    //使用蓝牙，允许程序连接配对过的蓝牙设备
    public static String BLUETOOTH = Manifest.permission.BLUETOOTH;

    //蓝牙管理，允许程序进行发现和配对新的蓝牙设备
    public static String BLUETOOTH_ADMIN = Manifest.permission.BLUETOOTH_ADMIN;

    //应用删除时广播，当一个应用在删除时触发一个广播
    public static String BROADCAST_PACKAGE_REMOVED = Manifest.permission.BROADCAST_PACKAGE_REMOVED;

    //收到短信时广播，当收到短信时触发一个广播
    public static String BROADCAST_SMS = Manifest.permission.BROADCAST_SMS;

    //连续广播，允许一个程序收到广播后快速收到下一个广播
    public static String BROADCAST_STICKY = Manifest.permission.BROADCAST_STICKY;

    //WAP PUSH广播，WAP PUSH服务收到后触发一个广播
    public static String BROADCAST_WAP_PUSH = Manifest.permission.BROADCAST_WAP_PUSH;

    //拨打电话，允许程序从非系统拨号器里输入电话号码
    public static String CALL_PHONE = Manifest.permission.CALL_PHONE;

    //通话权限，允许程序拨打电话，替换系统的拨号器界面
    public static String CALL_PRIVILEGED = Manifest.permission.CALL_PRIVILEGED;

    //拍照权限，允许访问摄像头进行拍照
    public static String CAMERA = Manifest.permission.CAMERA;

    //改变组件状态，改变组件是否启用状态
    public static String CHANGE_COMPONENT_ENABLED_STATE = Manifest.permission.CHANGE_COMPONENT_ENABLED_STATE;

    //改变配置，允许当前应用改变配置，如定位
    public static String CHANGE_CONFIGURATION = Manifest.permission.CHANGE_CONFIGURATION;

    //改变网络状态，改变网络状态如是否能联网
    public static String CHANGE_NETWORK_STATE = Manifest.permission.CHANGE_NETWORK_STATE;

    //改变WiFi多播状态，改变WiFi多播状态
    public static String CHANGE_WIFI_MULTICAST_STATE = Manifest.permission.CHANGE_WIFI_MULTICAST_STATE;

    //改变WiFi状态，改变WiFi状态
    public static String CHANGE_WIFI_STATE = Manifest.permission.CHANGE_WIFI_STATE;

    //清除应用缓存
    public static String CLEAR_APP_CACHE = Manifest.permission.CLEAR_APP_CACHE;

    //控制定位更新，允许获得移动网络定位信息改变
    public static String CONTROL_LOCATION_UPDATES = Manifest.permission.CONTROL_LOCATION_UPDATES;

    //删除缓存文件，允许应用删除缓存文件
    public static String DELETE_CACHE_FILES = Manifest.permission.DELETE_CACHE_FILES;

    //删除应用，允许程序删除应用
    public static String DELETE_PACKAGES = Manifest.permission.DELETE_PACKAGES;

    //应用诊断，允许程序到RW到诊断资源
    public static String DIAGNOSTIC = Manifest.permission.DIAGNOSTIC;

    //禁用键盘锁，允许程序禁用键盘锁
    public static String DISABLE_KEYGUARD = Manifest.permission.DISABLE_KEYGUARD;

    //转存系统信息，允许程序获取系统dump信息从系统服务
    public static String DUMP = Manifest.permission.DUMP;

    //状态栏控制，允许程序扩展或收缩状态栏
    public static String EXPAND_STATUS_BAR = Manifest.permission.EXPAND_STATUS_BAR;

    //工厂测试模式，允许程序运行工厂测试模式
    public static String FACTORY_TEST = Manifest.permission.FACTORY_TEST;

    //允许访问GMAIL账户列表
    public static String GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;

    //获取应用大小
    public static String GET_PACKAGE_SIZE = Manifest.permission.GET_PACKAGE_SIZE;

    //允许程序使用全局搜索功能
    public static String GLOBAL_SEARCH = Manifest.permission.GLOBAL_SEARCH;

    //安装定位提供
    public static String INSTALL_LOCATION_PROVIDER = Manifest.permission.INSTALL_LOCATION_PROVIDER;

    //允许程序安装应用
    public static String INSTALL_PACKAGES = Manifest.permission.INSTALL_PACKAGES;

    //访问网络连接
    public static String INTERNET = Manifest.permission.INTERNET;

    //允许程序调用killBackgroundProcesses(String).方法结束后台进程
    public static String KILL_BACKGROUND_PROCESSES = Manifest.permission.KILL_BACKGROUND_PROCESSES;

    //允许程序执行软格式化，删除系统配置信息
    public static String MASTER_CLEAR = Manifest.permission.MASTER_CLEAR;

    //修改声音设置信息
    public static String MODIFY_AUDIO_SETTINGS = Manifest.permission.MODIFY_AUDIO_SETTINGS;

    //修改电话状态，如飞行模式，但不包含替换系统拨号器界面
    public static String MODIFY_PHONE_STATE = Manifest.permission.MODIFY_PHONE_STATE;

    //格式化可移动文件系统，比如格式化清空SD卡
    public static String MOUNT_FORMAT_FILESYSTEMS = Manifest.permission.MOUNT_FORMAT_FILESYSTEMS;

    //挂载、反挂载外部文件系统
    public static String MOUNT_UNMOUNT_FILESYSTEMS = Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS;

    //允许程序执行NFC近距离通讯操作，用于移动支持
    public static String NFC = Manifest.permission.NFC;

    //创建一个永久的Activity，该功能标记为将来将被移除
    public static String PERSISTENT_ACTIVITY = Manifest.permission.PERSISTENT_ACTIVITY;

    //处理拨出电话，允许程序监视，修改或放弃播出电话
    public static String PROCESS_OUTGOING_CALLS = Manifest.permission.PROCESS_OUTGOING_CALLS;

    //允许程序读取用户的日程信息
    public static String READ_CALENDAR = Manifest.permission.READ_CALENDAR;

    //允许应用访问联系人通讯录信息
    public static String READ_CONTACTS = Manifest.permission.READ_CONTACTS;

    //允许程序读取外部存储，如SD卡上写文件
    public static String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;

    //屏幕截图,读取帧缓存用于屏幕截图
    public static String READ_FRAME_BUFFER = Manifest.permission.READ_FRAME_BUFFER;

    //读取当前键的输入状态，仅用于系统
    public static String READ_INPUT_STATE = Manifest.permission.READ_INPUT_STATE;

    //读取系统底层日志
    public static String READ_LOGS = Manifest.permission.READ_LOGS;

    //访问电话状态
    public static String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;

    //读取短信内容
    public static String READ_SMS = Manifest.permission.READ_SMS;

    //读取同步设置，读取Google在线同步设置
    public static String READ_SYNC_SETTINGS = Manifest.permission.READ_SYNC_SETTINGS;

    //读取同步状态，获得Google在线同步状态
    public static String READ_SYNC_STATS = Manifest.permission.READ_SYNC_STATS;

    //允许程序重新启动设备
    public static String REBOOT = Manifest.permission.REBOOT;

    //允许程序开机自动运行
    public static String RECEIVE_BOOT_COMPLETED = Manifest.permission.RECEIVE_BOOT_COMPLETED;

    //接收彩信
    public static String RECEIVE_MMS = Manifest.permission.RECEIVE_MMS;

    //接收短信
    public static String RECEIVE_SMS = Manifest.permission.RECEIVE_SMS;

    //接收WAP PUSH信息
    public static String RECEIVE_WAP_PUSH = Manifest.permission.RECEIVE_WAP_PUSH;

    //录制声音通过手机或耳机的麦克
    public static String RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;

    //排序系统任务,重新排序系统Z轴运行中的任务
    public static String REORDER_TASKS = Manifest.permission.REORDER_TASKS;

    //结束任务通过restartPackage(String)方法，该方式将在未来放弃
    public static String RESTART_PACKAGES = Manifest.permission.RESTART_PACKAGES;

    //发送短信
    public static String SEND_SMS = Manifest.permission.SEND_SMS;

    //设置闹铃提醒
    public static String SET_ALARM = Manifest.permission.SET_ALARM;

    //设置程序在后台是否总是退出
    public static String SET_ALWAYS_FINISH = Manifest.permission.SET_ALWAYS_FINISH;

    //设置全局动画缩放
    public static String SET_ANIMATION_SCALE = Manifest.permission.SET_ANIMATION_SCALE;

    //设置调试程序，一般用于开发
    public static String SET_DEBUG_APP = Manifest.permission.SET_DEBUG_APP;

    //设置应用的参数，已不再工作具体查看addPackageToPreferred(String) 介绍
    public static String SET_PREFERRED_APPLICATIONS = Manifest.permission.SET_PREFERRED_APPLICATIONS;

    //允许程序设置最大的进程数量的限制
    public static String SET_PROCESS_LIMIT = Manifest.permission.SET_PROCESS_LIMIT;

    //设置系统时间
    public static String SET_TIME = Manifest.permission.SET_TIME;

    //设置系统时区
    public static String SET_TIME_ZONE = Manifest.permission.SET_TIME_ZONE;

    //设置桌面壁纸
    public static String SET_WALLPAPER = Manifest.permission.SET_WALLPAPER;

    //设置壁纸建议
    public static String SET_WALLPAPER_HINTS = Manifest.permission.SET_WALLPAPER_HINTS;

    //发送一个永久的进程信号
    public static String SIGNAL_PERSISTENT_PROCESSES = Manifest.permission.SIGNAL_PERSISTENT_PROCESSES;

    //允许程序打开、关闭、禁用状态栏
    public static String STATUS_BAR = Manifest.permission.STATUS_BAR;

    //显示系统窗口
    public static String SYSTEM_ALERT_WINDOW = Manifest.permission.SYSTEM_ALERT_WINDOW;

    //更新设备状态
    public static String UPDATE_DEVICE_STATS = Manifest.permission.UPDATE_DEVICE_STATS;

    //允许程序使用SIP视频服务
    public static String USE_SIP = Manifest.permission.USE_SIP;

    //允许振动
    public static String VIBRATE = Manifest.permission.VIBRATE;

    //允许程序在手机屏幕关闭后后台进程仍然运行
    public static String WAKE_LOCK = Manifest.permission.WAKE_LOCK;

    //写入网络GPRS接入点设置
    public static String WRITE_APN_SETTINGS = Manifest.permission.WRITE_APN_SETTINGS;

    //写入日程，但不可读取
    public static String WRITE_CALENDAR = Manifest.permission.WRITE_CALENDAR;

    //写入联系人，但不可读取
    public static String WRITE_CONTACTS = Manifest.permission.WRITE_CONTACTS;

    //允许程序写入外部存储，如SD卡上写文件
    public static String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    //允许程序写入Google Map服务数据
    public static String WRITE_GSERVICES = Manifest.permission.WRITE_GSERVICES;

    //允许程序读写系统安全敏感的设置项
    public static String WRITE_SECURE_SETTINGS = Manifest.permission.WRITE_SECURE_SETTINGS;

    //允许读写系统设置项
    public static String WRITE_SETTINGS = Manifest.permission.WRITE_SETTINGS;

    //写入Google在线同步设置
    public static String WRITE_SYNC_SETTINGS = Manifest.permission.WRITE_SYNC_SETTINGS;


    /**
     * 请求权限(前提必须启动PermissionsActivity)
     *
     * @param permissions
     * @param listener
     */
    public static void requestRunTimePermission(Context c, final String[] permissions, final PermissionListener listener) {
        //启动该Activity是用于检测并获取权限
        c.startActivity(new Intent(c, PermissionsActivity.class));
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(1000);
                    PermissionsActivity p = (PermissionsActivity) ActivityManagerUtils.getActivityManager().existActivity("com.blankj.utilcode.activity.PermissionsActivity");
                    if (p != null) {
                        p.requestRunTimePermission(permissions, listener);
                    }
                    p = null;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    /**
     * 申请运行时自定义权限
     *
     * @param context
     * @param permissionItems
     * @param callback
     */
    public static void requestRunTimePermission(Context context, List<PermissionItem> permissionItems, PermissionCallback callback) {
        HiPermission.create(context).permissions(permissionItems).checkMutiPermission(callback);
    }

    /**
     * 申请必备的三个权限(sd卡权限、定位权限、拍照权限)
     *
     * @param context
     * @param callback
     */
    public static void requestNecessaryPermission(Context context, PermissionCallback callback) {
        HiPermission.create(context).checkMutiPermission(callback);
    }
    /**
     * 判断是否有某个权限
     *
     * @param context
     * @param str_permission 某个权限,如android.permission.RECORD_AUDIO
     */
    public static boolean estimateIsExistPermission(Context context, String str_permission) {
        return (PackageManager.PERMISSION_GRANTED == context.getPackageManager().checkPermission(str_permission, "packageName"));
    }
}