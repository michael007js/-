package com.blankj.utilcode.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.util.ArrayMap;
import android.view.View;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/9/23
 *     desc  : Activity相关工具类
 * </pre>
 */
public final class ActivityUtils {

    private ActivityUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断App是否存在指定的Activity文件
     *
     * @param packageName 包名
     * @param className   activity全路径类名
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isActivityExists(Context context, String packageName, String className) {
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        return !(context.getPackageManager().resolveActivity(intent, 0) == null ||
                intent.resolveActivity(context.getPackageManager()) == null ||
                context.getPackageManager().queryIntentActivities(intent, 0).size() == 0);
    }

    /**
     * 判断App指定的Activity是否已经运行
     * @param context
     * @param cls
     * @return
     */
    public static boolean isRunning(Context context, Class<?> cls) {

        Intent intent = new Intent(context, cls);
        ComponentName cmpName = intent.resolveActivity(context.getPackageManager());
        boolean bIsExist = false;
        if (cmpName != null) { // 说明系统中存在这个activity
            ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(10);
            for (ActivityManager.RunningTaskInfo runningTask : runningTasks) {
                if (runningTask.baseActivity.equals(cmpName)) { // 说明它已经启动了
                    bIsExist = true;
                    break;
                }
            }
        }
        return bIsExist;
    }


    /**
     * 打开Activity
     *
     * @param packageName 包名
     * @param className   全类名
     */
    public static void launchActivity(Context context, String packageName, String className) {
        launchActivity(context, packageName, className, null);
    }

    /**
     * 打开Activity
     *
     * @param packageName 包名
     * @param className   全类名
     * @param bundle      bundle
     */
    public static void launchActivity(Context context, String packageName, String className, Bundle bundle) {
        context.startActivity(getComponentIntent(packageName, className, bundle));
    }



    /**
     * 获取Activity栈链表
     *
     * @return Activity栈链表
     */
    public static List<Activity> getActivityList() {
        return ActivityManagerUtils.getActivityManager().getActivityStack();
    }


    /**
     * 获取启动项Activity
     *
     * @param packageName 包名
     * @return 启动项Activity
     */
    public static String getLauncherActivity(Context context,@NonNull final String packageName) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PackageManager pm =context.getPackageManager();
        List<ResolveInfo> info = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo aInfo : info) {
            if (aInfo.activityInfo.packageName.equals(packageName)) {
                return aInfo.activityInfo.name;
            }
        }
        return "no " + packageName;
    }


    /**
     * 判断Activity是否存在栈中
     *
     * @param activity activity
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public static boolean isActivityExistsInStack(@NonNull final Activity activity) {
        List<Activity> activities = ActivityManagerUtils.getActivityManager().getActivityStack();
        for (Activity aActivity : activities) {
            if (aActivity.equals(activity)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断Activity是否存在栈中
     *
     * @param clz activity类
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public static boolean isActivityExistsInStack(@NonNull final Class<?> clz) {
        List<Activity> activities = ActivityManagerUtils.getActivityManager().getActivityStack();
        for (Activity aActivity : activities) {
            if (aActivity.getClass().equals(clz)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 结束Activity
     *
     * @param activity activity
     */
    public static void finishActivity(@NonNull final Activity activity) {
        finishActivity(activity, false);
    }

    /**
     * 结束Activity
     *
     * @param activity   activity
     * @param isLoadAnim 是否启动动画
     */
    public static void finishActivity(@NonNull final Activity activity, final boolean isLoadAnim) {
        activity.finish();
        if (!isLoadAnim) {
            activity.overridePendingTransition(0, 0);
        }
    }

    /**
     * 结束Activity
     *
     * @param activity  activity
     * @param enterAnim 入场动画
     * @param exitAnim  出场动画
     */
    public static void finishActivity(@NonNull final Activity activity,
                                      @AnimRes final int enterAnim,
                                      @AnimRes final int exitAnim) {
        activity.finish();
        activity.overridePendingTransition(enterAnim, exitAnim);
    }

    /**
     * 结束Activity
     *
     * @param clz activity类
     */
    public static void finishActivity(@NonNull final Class<?> clz) {
        finishActivity(clz, false);
    }

    /**
     * 结束Activity
     *
     * @param clz        activity类
     * @param isLoadAnim 是否启动动画
     */
    public static void finishActivity(@NonNull final Class<?> clz, final boolean isLoadAnim) {
        List<Activity> activities = ActivityManagerUtils.getActivityManager().getActivityStack();
        for (Activity activity : activities) {
            if (activity.getClass().equals(clz)) {
                activity.finish();
                if (!isLoadAnim) {
                    activity.overridePendingTransition(0, 0);
                }
            }
        }
    }

    /**
     * 结束Activity
     *
     * @param clz       activity类
     * @param enterAnim 入场动画
     * @param exitAnim  出场动画
     */
    public static void finishActivity(@NonNull final Class<?> clz,
                                      @AnimRes final int enterAnim,
                                      @AnimRes final int exitAnim) {
        List<Activity> activities = ActivityManagerUtils.getActivityManager().getActivityStack();
        for (Activity activity : activities) {
            if (activity.getClass().equals(clz)) {
                activity.finish();
                activity.overridePendingTransition(enterAnim, exitAnim);
            }
        }
    }

    /**
     * 结束到指定Activity
     *
     * @param activity      activity
     * @param isIncludeSelf 是否结束该activity自己
     */
    public static boolean finishToActivity(@NonNull final Activity activity,
                                           final boolean isIncludeSelf) {
        return finishToActivity(activity, isIncludeSelf, false);
    }

    /**
     * 结束到指定Activity
     *
     * @param activity      activity
     * @param isIncludeSelf 是否结束该activity自己
     * @param isLoadAnim    是否启动动画
     */
    public static boolean finishToActivity(@NonNull final Activity activity,
                                           final boolean isIncludeSelf,
                                           final boolean isLoadAnim) {
        List<Activity> activities = ActivityManagerUtils.getActivityManager().getActivityStack();
        for (int i = activities.size() - 1; i >= 0; --i) {
            Activity aActivity = activities.get(i);
            if (aActivity.equals(activity)) {
                if (isIncludeSelf) {
                    finishActivity(aActivity, isLoadAnim);
                }
                return true;
            }
            finishActivity(aActivity, isLoadAnim);
        }
        return false;
    }

    /**
     * 结束到指定Activity
     *
     * @param activity      activity
     * @param isIncludeSelf 是否结束该activity自己
     * @param enterAnim     入场动画
     * @param exitAnim      出场动画
     */
    public static boolean finishToActivity(@NonNull final Activity activity,
                                           final boolean isIncludeSelf,
                                           @AnimRes final int enterAnim,
                                           @AnimRes final int exitAnim) {
        List<Activity> activities = ActivityManagerUtils.getActivityManager().getActivityStack();
        for (int i = activities.size() - 1; i >= 0; --i) {
            Activity aActivity = activities.get(i);
            if (aActivity.equals(activity)) {
                if (isIncludeSelf) {
                    finishActivity(aActivity, enterAnim, exitAnim);
                }
                return true;
            }
            finishActivity(aActivity, enterAnim, exitAnim);
        }
        return false;
    }

    /**
     * 结束到指定Activity
     *
     * @param clz           activity类
     * @param isIncludeSelf 是否结束该activity自己
     */
    public static boolean finishToActivity(@NonNull final Class<?> clz,
                                           final boolean isIncludeSelf) {
        return finishToActivity(clz, isIncludeSelf, false);
    }

    /**
     * 结束到指定Activity
     *
     * @param clz           activity类
     * @param isIncludeSelf 是否结束该activity自己
     * @param isLoadAnim    是否启动动画
     */
    public static boolean finishToActivity(@NonNull final Class<?> clz,
                                           final boolean isIncludeSelf,
                                           final boolean isLoadAnim) {
        List<Activity> activities = ActivityManagerUtils.getActivityManager().getActivityStack();
        for (int i = activities.size() - 1; i >= 0; --i) {
            Activity aActivity = activities.get(i);
            if (aActivity.getClass().equals(clz)) {
                if (isIncludeSelf) {
                    finishActivity(aActivity, isLoadAnim);
                }
                return true;
            }
            finishActivity(aActivity, isLoadAnim);
        }
        return false;
    }

    /**
     * 结束到指定Activity
     *
     * @param clz           activity类
     * @param isIncludeSelf 是否结束该activity自己
     * @param enterAnim     入场动画
     * @param exitAnim      出场动画
     */
    public static boolean finishToActivity(@NonNull final Class<?> clz,
                                           final boolean isIncludeSelf,
                                           @AnimRes final int enterAnim,
                                           @AnimRes final int exitAnim) {
        List<Activity> activities = ActivityManagerUtils.getActivityManager().getActivityStack();
        for (int i = activities.size() - 1; i >= 0; --i) {
            Activity aActivity = activities.get(i);
            if (aActivity.getClass().equals(clz)) {
                if (isIncludeSelf) {
                    finishActivity(aActivity, enterAnim, exitAnim);
                }
                return true;
            }
            finishActivity(aActivity, enterAnim, exitAnim);
        }
        return false;
    }

    /**
     * 结束除最新之外的同类型Activity
     * <p>也就是让栈中最多只剩下一种类型的Activity</p>
     *
     * @param clz activity类
     */
    public static void finishOtherActivitiesExceptNewest(@NonNull final Class<?> clz) {
        finishOtherActivitiesExceptNewest(clz, false);
    }

    /**
     * 结束除最新之外的同类型Activity
     * <p>也就是让栈中最多只剩下一种类型的Activity</p>
     *
     * @param clz        activity类
     * @param isLoadAnim 是否启动动画
     */
    public static void finishOtherActivitiesExceptNewest(@NonNull final Class<?> clz,
                                                         final boolean isLoadAnim) {
        List<Activity> activities = ActivityManagerUtils.getActivityManager().getActivityStack();
        boolean flag = false;
        for (int i = activities.size() - 1; i >= 0; i--) {
            Activity activity = activities.get(i);
            if (activity.getClass().equals(clz)) {
                if (flag) {
                    finishActivity(activity, isLoadAnim);
                } else {
                    flag = true;
                }
            }
        }
    }

    /**
     * 结束除最新之外的同类型Activity
     * <p>也就是让栈中最多只剩下一种类型的Activity</p>
     *
     * @param clz       activity类
     * @param enterAnim 入场动画
     * @param exitAnim  出场动画
     */
    public static void finishOtherActivitiesExceptNewest(@NonNull final Class<?> clz,
                                                         @AnimRes final int enterAnim,
                                                         @AnimRes final int exitAnim) {
        List<Activity> activities = ActivityManagerUtils.getActivityManager().getActivityStack();
        boolean flag = false;
        for (int i = activities.size() - 1; i >= 0; i--) {
            Activity activity = activities.get(i);
            if (activity.getClass().equals(clz)) {
                if (flag) {
                    finishActivity(activity, enterAnim, exitAnim);
                } else {
                    flag = true;
                }
            }
        }
    }

    /**
     * 结束所有activity
     */
    public static void finishAllActivities() {
        finishAllActivities(false);
    }

    /**
     * 结束所有activity
     *
     * @param isLoadAnim 是否启动动画
     */
    public static void finishAllActivities(final boolean isLoadAnim) {
        List<Activity> activityList = ActivityManagerUtils.getActivityManager().getActivityStack();
        for (int i = activityList.size() - 1; i >= 0; --i) {// 从栈顶开始移除
            Activity activity = activityList.get(i);
            activity.finish();// 在onActivityDestroyed发生remove
            if (!isLoadAnim) {
                activity.overridePendingTransition(0, 0);
            }
        }
    }





    /**
     * 是否在后台
     *
     * @param context
     * @return
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                return appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND;
            }
        }
        return false;
    }


    private static Intent getComponentIntent(String packageName, String className, Bundle bundle) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (bundle != null) intent.putExtras(bundle);
        ComponentName cn = new ComponentName(packageName, className);
        intent.setComponent(cn);
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

}
