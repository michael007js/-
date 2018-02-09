package com.blankj.utilcode.util;

/**
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑
 * 　　　　┃　　　┃代码无BUG！
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━神兽出没━━━━━━
 * Created by leilei on 2017/5/5.
 */

import android.app.Activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * Desction:应用程序Activity管理类：用于Activity管理和应用程序退出
 * Author:pengjianbo
 * Date:15/9/17 下午4:48
 */
public class ActivityManagerUtils {

    private static List<Activity> activityStack;
    private static ActivityManagerUtils instance;

    private ActivityManagerUtils() {
    }

    /**
     * 单一实例
     */
    public static ActivityManagerUtils getActivityManager() {
        if (instance == null) {
            instance = new ActivityManagerUtils();
        }
        return instance;
    }

    public static List<Activity> getActivityStack() {
        return activityStack;
    }

    //判断列表中是否存在某个activity
    public Activity existActivity(String activityName) {
        for (int i = 0; i < activityStack.size(); i++) {
            if (activityName.equals(activityStack.get(i).getLocalClassName())) {
                return activityStack.get(i);
            }
        }
        return null;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new ArrayList<>();
        }
        activityStack.add(activity);
        LogUtils.e("sssss", activity.getLocalClassName().toString() + "++被加入到队列");
    }

    /**
     * 结束所有activity并重启指定的activity
     * @param in
     * @param context
     */
    public void finishAllActivityAndStartAssignActivity(Intent in, Context context) {
        for (int i = 0; i < activityStack.size(); i++) {
            activityStack.get(i).finish();
        }
        context.startActivity(in);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        if (activityStack == null) {
            return null;
        }
        if (activityStack.size() == 0) {
            return null;
        }
        Activity activity = activityStack.get(activityStack.size() - 1);
        LogUtils.e("sssss", activity.getLocalClassName().toString() + "==>被获取实例");
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        if (activityStack == null) {
            return;
        }
        if (activityStack.size() == 0) {
            return;
        }
        Activity activity = activityStack.get(activityStack.size() - 1);
        LogUtils.e("sssss", activity.getLocalClassName().toString() + "--被销毁");
        finishActivity(activity);
        activity = null;
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activityStack == null) {
            return;
        }
        if (activity != null) {
            activityStack.remove(activity);
            LogUtils.e("sssss", activity.getLocalClassName().toString() + "--被销毁");
            activity.finish();
            activity = null;
        }
    }


    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        if (activityStack == null) {
            return;
        }
        Iterator<Activity> iterator = activityStack.iterator();
        for (int i = 0; i < activityStack.size(); i++) {
            if (cls.equals(activityStack.get(i).getClass())) {
                LogUtils.e("sssss", activityStack.get(i).getLocalClassName().toString() + "--被销毁");
                activityStack.get(i).finish();
                activityStack.remove(i);

            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityStack == null) {
            return;
        }
        for (int i = 0; i < activityStack.size(); i++) {
            LogUtils.e("sssss", activityStack.get(i).getLocalClassName().toString() + "--被销毁");
            activityStack.get(i).finish();
        }
        activityStack.clear();
    }


    /**
     * 根据ActivityName获取堆中Activity实例
     * @param activityName
     * @return
     */
    public Activity getActivity(String activityName) {
        Iterator<Activity> iterator = activityStack.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (activity != null && TextUtils.equals(activity.getClass().getName(), activityName)) {
                LogUtils.e("sssss", activity.getLocalClassName().toString() + "==>被获取实例");
                return activity;
            }
        }
        return null;
    }

    /**
     * 退出应用程序
     */
    public void appExit() {
        try {
            finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
        }
    }
}