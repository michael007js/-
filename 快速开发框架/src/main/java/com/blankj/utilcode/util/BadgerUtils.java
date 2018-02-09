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

    public static void addBadger(WeakReference<Context> weakReference,int badgeCount){
        if (EmptyUtils.isNotEmpty(weakReference.get())){
            ShortcutBadger.applyCount(weakReference.get(), badgeCount);
            LogUtils.e("addBadger"+badgeCount);
        }
    }

    public static void removeCount(WeakReference<Context> weakReference ){
        if (EmptyUtils.isNotEmpty(weakReference.get())){
            ShortcutBadger.removeCount(weakReference.get());
        }
    }

    public static void applyCount(WeakReference<Context> weakReference ,int badgeCount){
        if (EmptyUtils.isNotEmpty(weakReference.get())){
            ShortcutBadger.applyCount(weakReference.get(),badgeCount);
        }
    }

}
