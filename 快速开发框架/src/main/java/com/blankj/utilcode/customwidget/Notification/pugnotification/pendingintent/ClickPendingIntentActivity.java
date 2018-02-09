package com.blankj.utilcode.customwidget.Notification.pugnotification.pendingintent;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import com.blankj.utilcode.customwidget.Notification.pugnotification.constants.BroadcastActions;
import com.blankj.utilcode.customwidget.Notification.pugnotification.interfaces.PendingIntentNotification;
import com.blankj.utilcode.customwidget.Notification.pugnotification.notification.PugNotification;


public class ClickPendingIntentActivity implements PendingIntentNotification {
    private final Class<?> mActivity;
    private final Bundle mBundle;
    private final int mIdentifier;

    public ClickPendingIntentActivity(Class<?> activity, Bundle bundle, int identifier) {
        this.mActivity = activity;
        this.mBundle = bundle;
        this.mIdentifier = identifier;
    }

    @Override
    public PendingIntent onSettingPendingIntent() {
        Intent clickIntentActivity = new Intent(PugNotification.mSingleton.mContext, mActivity);
        clickIntentActivity.setAction(BroadcastActions.ACTION_PUGNOTIFICATION_CLICK_INTENT);
        clickIntentActivity.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        clickIntentActivity.setPackage(PugNotification.mSingleton.mContext.getPackageName());

        if (mBundle != null) {
            clickIntentActivity.putExtras(mBundle);
        }
        return PendingIntent.getActivity(PugNotification.mSingleton.mContext, mIdentifier, clickIntentActivity,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
