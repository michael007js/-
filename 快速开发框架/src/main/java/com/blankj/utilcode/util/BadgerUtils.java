package com.blankj.utilcode.util;

import android.app.Activity;
import android.content.Context;

import java.lang.ref.WeakReference;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * 角标
 * https://github.com/leolin310148/ShortcutBadger
 *
 * https://github.com/lixiangers/BadgeUtil
 *
 * Created by leilei on 2017/8/8.
 */

public class BadgerUtils {

    public static void addBadger(Context context,int badgeCount){
            ShortcutBadger.applyCount(context, badgeCount);
            LogUtils.e("addBadger"+badgeCount);
    }

    public static void removeCount(Context context ){
            ShortcutBadger.removeCount(context);
    }

    public static void applyCount(Context context ,int badgeCount){
            ShortcutBadger.applyCount(context,badgeCount);
    }

}
