package com.blankj.utilcode.customwidget.Notification.notifyutil.builder;

import android.app.Notification;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Administrator on 2017/2/13 0013.
 */

public class BigTextBuilder extends BaseBuilder{
    CharSequence summaryText;

    @Override
    public void build() {
        super.build();
        NotificationCompat.BigTextStyle textStyle = new NotificationCompat.BigTextStyle();
        textStyle.setBigContentTitle(contentTitle).bigText(contentText).setSummaryText(summaryText);
        cBuilder.setStyle(textStyle);
    }
}
