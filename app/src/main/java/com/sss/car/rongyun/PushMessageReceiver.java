package com.sss.car.rongyun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.ActivityManagerUtils;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.BadgerUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.RequestWeb;
import com.sss.car.view.ConversationChat;
import com.sss.car.view.LoginAndRegister;
import com.sss.car.view.Main;

import org.json.JSONException;
import org.json.JSONObject;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.push.notification.PushNotificationMessage;
import okhttp3.Call;


/**
 * Created by Administrator on 2016/12/7.
 */
public class PushMessageReceiver extends io.rong.push.notification.PushMessageReceiver {

    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage message) {
        BadgerUtils.applyCount(context, 1);
        LogUtils.e("sssss", "收到推送---------------------------------------------------------------------------------" +
                "\n类型" + message.getConversationType() +
                "\n消息头" + message.getObjectName() +
                "\n内容" + new String(message.getPushContent()).toString() +
                "\n附加消息" + message.getExtra() +
                "\n发送者用户名" + message.getSenderName() +
                "\n发送者ID" + message.getSenderId() +
                "\n目标ID" + message.getTargetId());
        BadgerUtils.applyCount(context,1);
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
        BadgerUtils.removeCount(context);
        close_window(message);
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

    private String getType(String type) {
        if ("group".equals(type)) {
            return "2";
        } else if ("private".equals(type)) {
            return "1";
        } else {
            return "";
        }

    }

    /**
     * 设置消息已读
     */
    void close_window(final PushNotificationMessage conversation) {
        try {
         RequestWeb.close_window(
                    new JSONObject()
                            .put("member_pid", Config.member_id)
                            .put("window_type", getType(conversation.getConversationType().getName()))
                            .put("member_id", conversation.getTargetId()).toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {


                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}