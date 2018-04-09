package com.blankj.utilcode.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.provider.Settings;

/**
 * 国内手机厂商白名单跳转工具类
 * Created by leilei on 2018/4/9.
 */

public class SettingUtils {
    public static void enterWhiteListSetting(Context context) {
        try {
            context.startActivity(getSettingIntent(context));
        } catch (Exception e) {
            context.startActivity(new Intent(Settings.ACTION_SETTINGS));
        }
    }

    private static Intent getSettingIntent(Context context) {
        ComponentName componentName = null;
        String brand = android.os.Build.BRAND;
        LogUtils.e(brand.toLowerCase());
        switch (brand.toLowerCase()) {
            case "samsung":
                if ( ActivityUtils.isActivityExists(context,"com.samsung.android.sm","com.samsung.android.sm.app.dashboard.SmartManagerDashBoardActivity")){
                    componentName = new ComponentName("com.samsung.android.sm","com.samsung.android.sm.app.dashboard.SmartManagerDashBoardActivity");
                }

                break;
            case "huawei":
                if ( ActivityUtils.isActivityExists(context,"com.huawei.systemmanager","com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity")){
                    componentName = new ComponentName("com.huawei.systemmanager","com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity");
                }
                if ( ActivityUtils.isActivityExists(context,"com.huawei.systemmanager","com.huawei.systemmanager.optimize.process.ProtectActivity")){
                    componentName = new ComponentName("com.huawei.systemmanager","com.huawei.systemmanager.optimize.process.ProtectActivity");
                }
                break;
            case "xiaomi":
                if ( ActivityUtils.isActivityExists(context,"com.miui.securitycenter","com.miui.permcenter.autostart.AutoStartManagementActivity")){
                    componentName = new ComponentName("com.miui.securitycenter","com.miui.permcenter.autostart.AutoStartManagementActivity");
                }
                break;
            case "vivo":
                if ( ActivityUtils.isActivityExists(context,"com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")){
                    componentName = new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity");
                }
                break;
            case "oppo":
                if ( ActivityUtils.isActivityExists(context,"com.coloros.oppoguardelf", "com.coloros.powermanager.fuelgaue.PowerUsageModelActivity")){
                    componentName = new ComponentName("com.coloros.oppoguardelf", "com.coloros.powermanager.fuelgaue.PowerUsageModelActivity");
                }
                break;
            case "360":
                if ( ActivityUtils.isActivityExists(context,"com.yulong.android.coolsafe", "com.yulong.android.coolsafe.ui.activity.autorun.AutoRunListActivity")){
                    componentName = new ComponentName("com.yulong.android.coolsafe", "com.yulong.android.coolsafe.ui.activity.autorun.AutoRunListActivity");
                }
                break;
            case "meizu":
                if ( ActivityUtils.isActivityExists(context,"com.meizu.safe", "com.meizu.safe.permission.SmartBGActivity")){
                    componentName = new ComponentName("com.meizu.safe", "com.meizu.safe.permission.SmartBGActivity");
                }
                break;
            case "oneplus":
                if ( ActivityUtils.isActivityExists(context,"com.oneplus.security", "com.oneplus.security.chainlaunch.view.ChainLaunchAppListActivity")){
                    componentName = new ComponentName("com.oneplus.security", "com.oneplus.security.chainlaunch.view.ChainLaunchAppListActivity");
                }
                break;
            default:
                break;
        }
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (componentName != null) {
            intent.setComponent(componentName);
        } else {
            intent.setAction(Settings.ACTION_SETTINGS);
        }
        return intent;
    }
}
