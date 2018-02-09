package com.sss.car.jiguang;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.blankj.utilcode.util.ActivityManagerUtils;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedMessageOrderList;
import com.sss.car.EventBusModel.JiGuangModel;
import com.sss.car.model.OrderSOSGrabModel;
import com.sss.car.model.PushSOSHelperFromBuyerModel;
import com.sss.car.order.OrderSOSPopUpWindows;
import com.sss.car.utils.CarUtils;
import com.sss.car.view.LoginAndRegister;
import com.sss.car.view.Main;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;


/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class ReceivedMessage extends BroadcastReceiver {
    private static final String TAG = "ReceivedMessage";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            LogUtils.e(TAG, intent.getAction() + "\n[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

            LogUtils.e(bundle.getString(JPushInterface.EXTRA_EXTRA));
            add(bundle.getString(JPushInterface.EXTRA_EXTRA), context);
            EventBus.getDefault().post(new JiGuangModel());
            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                LogUtils.e(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                LogUtils.e(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//                processCustomMessage(context, bundle);

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                LogUtils.e(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                LogUtils.e(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                parse(bundle.getString(JPushInterface.EXTRA_EXTRA), context);
                LogUtils.e(TAG, "[MyReceiver] 用户点击打开了通知");
                //打开自定义的Activity
//                Intent i = new Intent(context, Launcher.class);
//                i.putExtras(bundle);
//                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                context.startActivity(i);




//                if (AppUtils.isBackground(context)) {
//                    ActivityManagerUtils.getActivityManager().finishAllActivity();
//                    context.startActivity(new Intent(context, LoginAndRegister.class)
//                            .putExtra("isShowBack", false)
//                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                } else {
//                    //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    ActivityManagerUtils.getActivityManager().finishAllActivity();
//                    context.startActivity(new Intent(context, LoginAndRegister.class)
//                            .putExtra("isShowBack", false)
//                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                }

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                LogUtils.e(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                LogUtils.e(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                LogUtils.e(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {

        }

    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    LogUtils.e(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    LogUtils.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
//    private void processCustomMessage(Context context, Bundle bundle) {
//        if (MainActivity.isForeground) {
//            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//            if (!ExampleUtil.isEmpty(extras)) {
//                try {
//                    JSONObject extraJson = new JSONObject(extras);
//                    if (extraJson.length() > 0) {
//                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//                    }
//                } catch (JSONException e) {
//
//                }
//
//            }
//            LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
//        }
//    }


    void parse(String data, Context context) throws JSONException {
        LogUtils.e("sss"+"---"+data);
        if (!StringUtils.isEmpty(data)) {
            JSONObject jsonObject = new JSONObject(data);
            switch (jsonObject.getString("type")) {
                case "sos":
                    CarUtils.orderJump(
                            context,
                            "sos",
                            jsonObject.getInt("status"),
                            jsonObject.getString("ids"),
                            false,
                            jsonObject.getString("goods_comment"),
                            jsonObject.getString("is_comment"),
                            jsonObject.getString("exchange_id"),
                            jsonObject.getString("exchange_status"));
                    break;
                case "order":
                    if ("1".equals(jsonObject.getString("order_type"))){
                        CarUtils.orderJump(
                                context,
                                "goods",
                                jsonObject.getInt("status"),
                                jsonObject.getString("ids"),
                                Config.member_id.equals(jsonObject.getString("member_pid")),
                                jsonObject.getString("goods_comment"),
                                jsonObject.getString("is_comment"),
                                jsonObject.getString("exchange_id"),
                                jsonObject.getString("exchange_status"));
                    }else    if ("2".equals(jsonObject.getString("order_type"))){
                        CarUtils.orderJump(
                                context,
                                "service",
                                jsonObject.getInt("status"),
                                jsonObject.getString("ids"),
                                Config.member_id.equals(jsonObject.getString("member_pid")),
                                jsonObject.getString("goods_comment"),
                                jsonObject.getString("is_comment"),
                                jsonObject.getString("exchange_id"),
                                jsonObject.getString("exchange_status"));
                    }
                    break;
            }
        }
    }

    void add(String data, Context context) {
        if (!StringUtils.isEmpty(data)) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                LogUtils.e(jsonObject.getString("type") + jsonObject.getString("status"));
                switch (jsonObject.getString("type")) {
                    case "sos":
                        EventBus.getDefault().post(new ChangedMessageOrderList());
                        // status:1.sos求助弹窗, 2.sos接受订单, 3.sos用户取消, 4.sos店铺取消, 5.用户确认服务
                        switch (jsonObject.getString("status")) {
                            case "1":
                                PushSOSHelperFromBuyerModel pushSOSHelperFromBuyerModel = new PushSOSHelperFromBuyerModel();
                                pushSOSHelperFromBuyerModel.status = jsonObject.getString("status");
                                pushSOSHelperFromBuyerModel.type = jsonObject.getJSONObject("data").getString("type");
                                pushSOSHelperFromBuyerModel.sos_id = jsonObject.getJSONObject("data").getString("sos_id");
                                pushSOSHelperFromBuyerModel.recipients = jsonObject.getJSONObject("data").getString("recipients");
                                pushSOSHelperFromBuyerModel.start_time = jsonObject.getJSONObject("data").getString("start_time");
                                pushSOSHelperFromBuyerModel.title = jsonObject.getJSONObject("data").getString("title");
                                pushSOSHelperFromBuyerModel.mobile = jsonObject.getJSONObject("data").getString("mobile");
                                pushSOSHelperFromBuyerModel.price = jsonObject.getJSONObject("data").getInt("price");
                                pushSOSHelperFromBuyerModel.credit = jsonObject.getJSONObject("data").getString("credit");
                                pushSOSHelperFromBuyerModel.damages = jsonObject.getJSONObject("data").getString("damages");
                                pushSOSHelperFromBuyerModel.vehicle_name = jsonObject.getJSONObject("data").getString("vehicle_name");
                                pushSOSHelperFromBuyerModel.member_id = jsonObject.getJSONObject("data").getString("member_id");
                                pushSOSHelperFromBuyerModel.lat = jsonObject.getJSONObject("data").getDouble("lat");
                                pushSOSHelperFromBuyerModel.lng = jsonObject.getJSONObject("data").getDouble("lng");
                                if (ActivityUtils.isRunning(context, Main.class)){
                                    if (ActivityManagerUtils.getActivityManager().existActivity("order.OrderSOSPopUpWindows") == null) {
                                        context.startActivity(new Intent(context, OrderSOSPopUpWindows.class)
                                                .putExtra("data", pushSOSHelperFromBuyerModel).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    }
                                }


                                break;
                            case "2":
                                OrderSOSGrabModel orderSOSGrabModel = new OrderSOSGrabModel();
                                orderSOSGrabModel.consent_id = jsonObject.getJSONObject("data").getString("consent_id");
                                orderSOSGrabModel.lng = jsonObject.getJSONObject("data").getString("lng");
                                orderSOSGrabModel.lat = jsonObject.getJSONObject("data").getString("lat");
                                orderSOSGrabModel.damages = jsonObject.getJSONObject("data").getString("damages");
                                orderSOSGrabModel.price = jsonObject.getJSONObject("data").getString("price");
                                orderSOSGrabModel.money = jsonObject.getJSONObject("data").getString("money");
                                orderSOSGrabModel.create_time = jsonObject.getJSONObject("data").getString("create_time");
                                orderSOSGrabModel.sos_id = jsonObject.getJSONObject("data").getString("sos_id");
                                orderSOSGrabModel.member_id = jsonObject.getJSONObject("data").getString("member_id");
                                orderSOSGrabModel.shop_id = jsonObject.getJSONObject("data").getString("shop_id");
                                orderSOSGrabModel.mobile = jsonObject.getJSONObject("data").getString("mobile");
                                orderSOSGrabModel.face = jsonObject.getJSONObject("data").getString("face");
                                orderSOSGrabModel.username = jsonObject.getJSONObject("data").getString("username");
                                orderSOSGrabModel.credit = jsonObject.getJSONObject("data").getString("credit");
                                EventBus.getDefault().post(orderSOSGrabModel);
                                break;
                            case "3":
                                ToastUtils.showLongToast(context, jsonObject.getJSONObject("data").getString("message"));
                                break;
                            case "4":
                                break;
                            case "5":
                                ToastUtils.showLongToast(context, jsonObject.getJSONObject("data").getString("message"));
                                break;
                        }
                        break;


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
