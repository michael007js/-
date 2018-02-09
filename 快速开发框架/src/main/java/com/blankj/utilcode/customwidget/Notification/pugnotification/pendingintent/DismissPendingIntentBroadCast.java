package com.blankj.utilcode.customwidget.Notification.pugnotification.pendingintent;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import com.blankj.utilcode.customwidget.Notification.pugnotification.constants.BroadcastActions;
import com.blankj.utilcode.customwidget.Notification.pugnotification.interfaces.PendingIntentNotification;
import com.blankj.utilcode.customwidget.Notification.pugnotification.notification.PugNotification;


public class DismissPendingIntentBroadCast implements PendingIntentNotification {
    private final Bundle mBundle;
    private final int mIdentifier;

    public DismissPendingIntentBroadCast(Bundle bundle, int identifier) {
        this.mBundle = bundle;
        this.mIdentifier = identifier;
    }

    @Override
    public PendingIntent onSettingPendingIntent() {
        Intent clickIntentBroadcast = new Intent(BroadcastActions.ACTION_PUGNOTIFICATION_DIMISS_INTENT);
        clickIntentBroadcast.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        clickIntentBroadcast.setPackage(PugNotification.mSingleton.mContext.getPackageName());
        if (mBundle != null) {
            clickIntentBroadcast.putExtras(mBundle);
        }

        return PendingIntent.getBroadcast(PugNotification.mSingleton.mContext, mIdentifier, clickIntentBroadcast,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
