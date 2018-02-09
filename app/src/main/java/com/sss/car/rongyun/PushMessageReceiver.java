package com.sss.car.rongyun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.blankj.utilcode.util.ActivityManagerUtils;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.sss.car.view.ConversationChat;
import com.sss.car.view.LoginAndRegister;
import com.sss.car.view.Main;

import io.rong.push.notification.PushNotificationMessage;


/**
 * Created by Administrator on 2016/12/7.
 */
public class PushMessageReceiver extends io.rong.push.notification.PushMessageReceiver {

    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage message) {
        LogUtils.e("sssss", "收到推送---------------------------------------------------------------------------------" +
                "\n类型" + message.getConversationType() +
                "\n消息头" + message.getObjectName() +
                "\n内容" + new String(message.getPushContent()).toString() +
                "\n附加消息" + message.getExtra() +
                "\n发送者用户名" + message.getSenderName() +
                "\n发送者ID" + message.getSenderId() +
                "\n目标ID" + message.getTargetId());
        return false;
    }


    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage message) {
        LogUtils.e("sssss", "点击推送---------------------------------------------------------------------------------" +
                "\n类型" + message.getConversationType() +
                "\n消息头" + message.getObjectName() +
                "\n内容" + new String(message.getPushContent()).toString() +
                "\n附加消息" + message.getExtra() +
                "\n发送者用户名" + message.getSenderName() +
                "\n发送者ID" + message.getSenderId() +
                "\n目标ID" + message.getTargetId());

//        if (ActivityUtils.isActivityExistsInStack(Main.class)){
//            if (!ActivityUtils.isActivityExistsInStack(ConversationChat.class)){
//                Uri uri = Uri.parse("rong://" + context.getApplicationInfo().processName).buildUpon().appendPath("conversation").appendPath(message.getConversationType().getName().toLowerCase()).appendQueryParameter("targetId", message.getSenderId()).appendQueryParameter("title", message.getSenderName()).build();
//                context.startActivity(new Intent(context, LoginAndRegister.class)
//                        .putExtra("isShowBack",false)
//                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                context.startActivity(new Intent("android.intent.action.VIEW", uri).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
//            }
//        }else {
//            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            ActivityManagerUtils.getActivityManager().finishAllActivity();
//            context.startActivity(new Intent(context, LoginAndRegister.class)
//                    .putExtra("isShowBack",false)
//                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
//
//        }

        return false;
    }
}