package com.sss.car.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.ZhiFuBaoPasswordStyle.PassWordKeyboard;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PriceUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chanjet.yqpay.IYQPayCallback;
import com.chanjet.yqpay.YQPayApi;
import com.chanjet.yqpay.util.DeviceUtils;
import com.google.gson.Gson;
import com.rey.material.app.BottomSheetDialog;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedCouponModel;
import com.sss.car.EventBusModel.ChangedOrderModel;
import com.sss.car.EventBusModel.ChangedPopularizeModel;
import com.sss.car.P;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.view.ActivityMyDataSynthesizSettingSetPayPassword;
import com.sss.car.view.ActivityPayInfo;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by leilei on 2018/1/16.
 */

@SuppressWarnings("ALL")
public class PayUtils {
    /**
     * 调用收银台页面结果callback---失败
     */
    private static final String CALLBACK_FAILD = "failed";
    /**
     * 调用收银台页面结果callback---成功
     */
    private static final String CALLBACK_SUCCESS = "success";
    /**
     * 调用收银台页面结果---调用收银台时没有传递参数
     */
    private static final String CALLBACK_PARAM_FREE = "no_param";
    /**
     * 调用收银台页面结果--订单生成失败（服务器返回解析错误或为空）
     */
    private static final String CALLBACK_SERVER_NO_PARAM = "server_no_param";
    /**
     * 调用收银台页面结果callback---网络连接异常
     */
    private static final String CALLBACK_HTTPSTATUSEXCEPTION = "HttpStatusException";
    /**
     * 调用收银台页面结果callback---读取数据异常
     */
    private static final String CALLBACK_IOEXCEPTION = "IOException";
    /**
     * 调用收银台页面结果callback---无访问网络
     */
    private static final String CALLBACK_NONETWORK = "noNetwork";
    /**
     * 调用收银台页面结果callback---解析出错
     */
    private static final String CALLBACK_DATAERROR = "dataError";
    /**
     * 调用收银台页面结果callback---取消支付
     */
    private static final String CALLBACK_CANCEL_PAY = "cancel_pay";
    /**
     * 调用收银台页面结果callback---支付失败
     */
    private static final String CALLBACK_FAILED_PAY = "failed_pay";
    /**
     * 调用收银台页面结果callback---正在处理中
     */
    private static final String CALLBACK_PROCESSED_PAY = "processed_pay";

    /**
     * 请求可用积分
     *
     * @param ywLoadingDialog
     * @param friend_id       如果是SOS订单支付时才有用，其他支付可以无视
     * @param ids             商品类型ID
     * @param title_type      1SOS订单支付，2订单支付，3优惠券支付，4推广订单支付，5账户充值
     * @param is_deposit      是否是缴纳保证金  1是  0否
     * @param money           支付金额
     * @param activity
     */
    public static void requestPayment(final YWLoadingDialog ywLoadingDialog, final String friend_id, final String ids, final int title_type, final int is_deposit, final String money, final Activity activity) {
        LogUtils.e("ids:" + ids + "    title_type:" + title_type + "    money:" + money + "    is_deposit:"+is_deposit);
        try {
            RequestWeb.payment_integral(
                    new JSONObject()
                            .put("is_deposit", is_deposit)
                            .put("member_id", Config.member_id)
                            .put("money", money)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(activity, e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            String a = "0";
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    a = jsonObject.getJSONObject("data").getString("deduction");
                                }

                                if (ywLoadingDialog != null) {
                                    ywLoadingDialog.dismiss();
                                }
                                double b = Double.valueOf(a);
                                if ("0".equals(money) || "0.0".equals(money) || "0.00".equals(money)) {
                                    createPasswordInputDialog(ywLoadingDialog, friend_id, activity, money, 0, title_type, ids, "1", is_deposit);
                                } else {
                                    createPaymentDialog(ywLoadingDialog, friend_id, ids, title_type, is_deposit, money, (float) b, activity);
                                }

                            } catch (JSONException e) {
                                ToastUtils.showShortToast(activity, "积分数据请求失败Err -0");
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (JSONException e) {
            ToastUtils.showShortToast(activity, "积分数据请求失败Err -1");
            e.printStackTrace();
        }
    }

    /**
     * 创建弹窗
     *
     * @param ywLoadingDialog
     * @param friend_id       如果是SOS订单支付时才有用，其他支付可以无视
     * @param ids             商品类型ID
     * @param title_type      1SOS订单支付，2订单支付，3优惠券支付，4推广订单支付，5账户充值
     * @param is_deposit      是否是缴纳保证金 1是  0否
     * @param money           支付金额
     * @param score           积分抵兑金额
     * @param activity
     */
    private static void createPaymentDialog(final YWLoadingDialog ywLoadingDialog, final String friend_id, final String ids, final int title_type, final int is_deposit, final String money, final float score, final Activity activity) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_payment_bottom, null);
        TextView close_dialog_payment_bottom = $.f(view, R.id.close_dialog_payment_bottom);
        final TextView money_dialog_payment_bottom = $.f(view, R.id.money_dialog_payment_bottom);
        TextView click_dialog_payment_bottom = $.f(view, R.id.click_dialog_payment_bottom);
        TextView score_dialog_payment_bottom_score = $.f(view, R.id.score_dialog_payment_bottom_score);
        final TextView cancel_dialog_payment_bottom_score = $.f(view, R.id.cancel_dialog_payment_bottom_score);
        TextView click_next_dialog_payment_bottom = $.f(view, R.id.click_next_dialog_payment_bottom);
        final LinearLayout click_balance_dialog_payment_bottom = $.f(view, R.id.click_balance_dialog_payment_bottom);
        final LinearLayout click_wx_dialog_payment_bottom = $.f(view, R.id.click_wx_dialog_payment_bottom);
        final LinearLayout click_zfb_dialog_payment_bottom = $.f(view, R.id.click_zfb_dialog_payment_bottom);
        final CheckBox cb_balance_dialog_payment_bottom = $.f(view, R.id.cb_balance_dialog_payment_bottom);
        final CheckBox cb_wx_dialog_payment_bottom = $.f(view, R.id.cb_wx_dialog_payment_bottom);
        final CheckBox cb_zfb_dialog_payment_bottom = $.f(view, R.id.cb_zfb_dialog_payment_bottom);
        final CheckBox cb_score_dialog_payment_bottom_score = $.f(view, R.id.cb_score_dialog_payment_bottom_score);
        final LinearLayout parent_dialog_payment_bottom_score = $.f(view, R.id.parent_dialog_payment_bottom_score);
        final TextView zfb_name=$.f(view,R.id.zfb_name);
        final TextView wx_name=$.f(view,R.id.wx_name);
        if (is_deposit == 0) {//
            click_dialog_payment_bottom.setVisibility(View.GONE);
        } else if (is_deposit == 1) {
            click_dialog_payment_bottom.setVisibility(View.VISIBLE);
        }
        LinearLayout click_score = $.f(view, R.id.click_score);
        money_dialog_payment_bottom.setText(money + "");
        score_dialog_payment_bottom_score.setText(score + "");
        click_dialog_payment_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent_dialog_payment_bottom_score.setVisibility(View.VISIBLE);
            }
        });


        click_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_score_dialog_payment_bottom_score.isChecked()) {
                    cb_score_dialog_payment_bottom_score.setChecked(false);

                } else {
                    cb_score_dialog_payment_bottom_score.setChecked(true);
                }

                if (cb_score_dialog_payment_bottom_score.isChecked()) {
                    double a = (PriceUtils.subtract(Double.valueOf(money), Double.valueOf(score), 2));
                    if (a <= 0) {
                        click_wx_dialog_payment_bottom.setEnabled(false);
                        click_zfb_dialog_payment_bottom.setEnabled(false);
                        wx_name.setTextColor(Color.parseColor("#787878"));
                        zfb_name.setTextColor(Color.parseColor("#787878"));
                    } else {
                        click_wx_dialog_payment_bottom.setEnabled(true);
                        click_zfb_dialog_payment_bottom.setEnabled(true);
                        wx_name.setTextColor(Color.parseColor("#000000"));
                        zfb_name.setTextColor(Color.parseColor("#000000"));
                    }
                    money_dialog_payment_bottom.setText(a + "");
                    cancel_dialog_payment_bottom_score.setText("确定");
                } else {

                    money_dialog_payment_bottom.setText(money + "");
                    cancel_dialog_payment_bottom_score.setText("取消");
                }
            }
        });
        cb_score_dialog_payment_bottom_score.setEnabled(false);
//        cb_score_dialog_payment_bottom_score.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (cb_score_dialog_payment_bottom_score.isChecked()) {
//                    cb_score_dialog_payment_bottom_score.setChecked(false);
//
//                } else {
//                    cb_score_dialog_payment_bottom_score.setChecked(true);
//                }
//
//                if (cb_score_dialog_payment_bottom_score.isChecked()) {
//                    money_dialog_payment_bottom.setText((PriceUtils.subtract(Double.valueOf(money), Double.valueOf(score), 2)) + "");
//                    cancel_dialog_payment_bottom_score.setText("确定");
//                } else {
//                    money_dialog_payment_bottom.setText(money + "");
//                    cancel_dialog_payment_bottom_score.setText("取消");
//                }
//            }
//        });


        cancel_dialog_payment_bottom_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("确定".equals(cancel_dialog_payment_bottom_score.getText().toString().trim())) {
                    parent_dialog_payment_bottom_score.setVisibility(View.GONE);
                } else {
                    money_dialog_payment_bottom.setText(money + "");
                    parent_dialog_payment_bottom_score.setVisibility(View.GONE);
                }
            }
        });
        click_balance_dialog_payment_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_balance_dialog_payment_bottom.setChecked(true);
                cb_wx_dialog_payment_bottom.setChecked(false);
                cb_zfb_dialog_payment_bottom.setChecked(false);
            }
        });
        click_wx_dialog_payment_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_balance_dialog_payment_bottom.setChecked(false);
                cb_wx_dialog_payment_bottom.setChecked(true);
                cb_zfb_dialog_payment_bottom.setChecked(false);
            }
        });
        click_zfb_dialog_payment_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_balance_dialog_payment_bottom.setChecked(false);
                cb_wx_dialog_payment_bottom.setChecked(false);
                cb_zfb_dialog_payment_bottom.setChecked(true);
            }
        });
        cb_balance_dialog_payment_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_balance_dialog_payment_bottom.setChecked(true);
                cb_wx_dialog_payment_bottom.setChecked(false);
                cb_zfb_dialog_payment_bottom.setChecked(false);
            }
        });
        cb_wx_dialog_payment_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_balance_dialog_payment_bottom.setChecked(false);
                cb_wx_dialog_payment_bottom.setChecked(true);
                cb_zfb_dialog_payment_bottom.setChecked(false);
            }
        });
        cb_zfb_dialog_payment_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_balance_dialog_payment_bottom.setChecked(false);
                cb_wx_dialog_payment_bottom.setChecked(false);
                cb_zfb_dialog_payment_bottom.setChecked(true);
            }
        });

        close_dialog_payment_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        click_next_dialog_payment_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cb_balance_dialog_payment_bottom.isChecked() && !cb_wx_dialog_payment_bottom.isChecked() && !cb_zfb_dialog_payment_bottom.isChecked()) {
                    ToastUtils.showShortToast(activity, "请选择支付方式");
                    return;
                }
                int is_integral = 0;
                if (cb_score_dialog_payment_bottom_score.isChecked()) {
                    is_integral = 1;
                } else {
                    is_integral = 0;
                }
                if (cb_balance_dialog_payment_bottom.isChecked()) {
                    createPasswordInputDialog(ywLoadingDialog, friend_id, activity, money_dialog_payment_bottom.getText().toString().trim(), is_integral, title_type, ids, "1", is_deposit);
                } else if (cb_wx_dialog_payment_bottom.isChecked()) {
                    payment_into(ywLoadingDialog, friend_id, activity, money_dialog_payment_bottom.getText().toString().trim(), is_integral, title_type, ids, "2", is_deposit);
                } else if (cb_zfb_dialog_payment_bottom.isChecked()) {
                    payment_into(ywLoadingDialog, friend_id, activity, money_dialog_payment_bottom.getText().toString().trim(), is_integral, title_type, ids, "3", is_deposit);
                }
                bottomSheetDialog.dismiss();

            }
        });

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.show();
    }

    /**
     * 弹出密码输入框
     *
     * @param ywLoadingDialog
     * @param friend_id       如果是SOS订单支付时才有用，其他支付可以无视
     * @param activity
     * @param money           金额
     * @param is_integral     是否使用积分：1使用。0不使用
     * @param title_type      1SOS订单支付，2订单支付，3优惠券支付，4推广订单支付，5账户充值
     * @param ids             商品类型ID
     * @param payment_mode    1余额，2，支付宝，3微信
     * @param is_deposit      是否是缴纳保证金  1是  0否
     */
    private static void createPasswordInputDialog(final YWLoadingDialog ywLoadingDialog, final String friend_id, final Activity activity, final String money,
                                                  final int is_integral, final int title_type, final String ids, final String payment_mode, final int is_deposit) {

        P.e(ywLoadingDialog, Config.member_id, activity, new P.p() {
            @Override
            public void exist() {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
                View view = LayoutInflater.from(activity).inflate(R.layout.dialog_password_input, null);
                final PassWordKeyboard PassWordKeyboard = $.f(view, R.id.PassWordKeyboard);
                PassWordKeyboard
                        .title("验证支付密码")
                        .titleColor(activity.getResources().getColor(R.color.mainColor))
                        .setColor(activity.getResources().getColor(R.color.mainColor))
                        .setLoadingDraw(activity, R.mipmap.logo_loading)
                        .overridePendingTransition(activity)
                        .customFunction("")
                        .setOnPassWordKeyboardCallBack(new PassWordKeyboard.OnPassWordKeyboardCallBack() {
                            @Override
                            public void onPassword(final String pasword) {
                                P.r(ywLoadingDialog, Config.member_id, pasword, activity, new P.r() {
                                    @Override
                                    public void match() {
                                        bottomSheetDialog.dismiss();
                                        PassWordKeyboard.setStatus(true);
                                        payment_into(ywLoadingDialog, friend_id, activity, money, is_integral, title_type, ids, payment_mode, is_deposit);
                                    }

                                    @Override
                                    public void mismatches() {
                                        PassWordKeyboard.setStatus(false);
                                    }
                                });
                            }

                            @Override
                            public void onFinish() {
                                bottomSheetDialog.dismiss();
                            }

                            @Override
                            public void onCustomFunction() {
                            }
                        });
                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.show();
            }

            @Override
            public void nonexistence() {
                if (activity != null) {
                    activity.startActivity(new Intent(activity, ActivityMyDataSynthesizSettingSetPayPassword.class)
                            .putExtra("mode", ActivityMyDataSynthesizSettingSetPayPassword.set));
                }
            }
        });

    }


    /**
     * @param ywLoadingDialog
     * @param friend_id       如果是SOS订单支付时才有用，其他支付可以无视
     * @param activity
     * @param money           金额
     * @param is_integral     是否使用积分：1使用。0不使用
     * @param title_type      1SOS订单支付，2订单支付，3优惠券支付，4推广订单支付，5账户充值
     * @param ids             商品类型ID
     * @param payment_mode    1余额，2，支付宝，3微信       @throws JSONException
     * @param is_deposit      是否是缴纳保证金  1是  0否
     */
    public static void payment_into(
            final YWLoadingDialog ywLoadingDialog,
            String friend_id,
            final Activity activity,
            final String money,
            int is_integral,
            final int title_type,
            String ids,
            final String payment_mode,
            final int is_deposit) {
        final YWLoadingDialog ywLoadingDialog1 = showToast(ywLoadingDialog, activity, "正在生成订单");
        try {
            RequestWeb.payment_into(
                    new JSONObject()
                            .put("money", money)
                            .put("type", title_type)
                            .put("ids", ids)
                            .put("lat",Config.latitude)
                            .put("lng",Config.longitude)
                            .put("is_deposit", is_deposit)
                            .put("friend_id", friend_id)
                            .put("is_integral", is_integral)
                            .put("payment_mode", payment_mode)
                            .put("member_id", Config.member_id)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(activity, e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    if ("1".equals(payment_mode)) {
                                        if (ywLoadingDialog1 != null) {
                                            ywLoadingDialog1.disMiss();
                                        }
                                        if (title_type == 1 || title_type == 2) {
                                            ToastUtils.showShortToast(activity, jsonObject.getString("message"));
                                            EventBus.getDefault().post(new ChangedOrderModel());
                                        } else if (title_type == 3) {
                                            ToastUtils.showShortToast(activity, jsonObject.getString("message"));
                                            EventBus.getDefault().post(new ChangedCouponModel());
                                        } else if (title_type == 4) {
                                            ToastUtils.showShortToast(activity, jsonObject.getString("message"));
                                            EventBus.getDefault().post(new ChangedPopularizeModel());
                                        }
                                    } else if ("2".equals(payment_mode)) {
                                        wxPay(ywLoadingDialog1, title_type, jsonObject, activity);
                                    } else if ("3".equals(payment_mode)) {
                                        aliPay(ywLoadingDialog1, title_type, jsonObject, activity);
                                    }

                                } else {
                                    if (ywLoadingDialog != null) {
                                        ywLoadingDialog.disMiss();
                                    }
                                    if (ywLoadingDialog1 != null) {
                                        ywLoadingDialog1.disMiss();
                                    }
                                    ToastUtils.showShortToast(activity, jsonObject.getString("message"));
                                }

                            } catch (JSONException e) {
                                if (ywLoadingDialog1 != null) {
                                    ywLoadingDialog1.disMiss();
                                }
                                if (ywLoadingDialog != null) {
                                    ywLoadingDialog.disMiss();
                                }
                                ToastUtils.showShortToast(activity, "统一订单数据解析错误Err -0");
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (JSONException e) {
            if (ywLoadingDialog != null) {
                ywLoadingDialog.disMiss();
            }
            if (ywLoadingDialog1 != null) {
                ywLoadingDialog1.disMiss();
            }
            ToastUtils.showShortToast(activity, "统一订单数据解析错误Err -1");
            e.printStackTrace();
        }
    }

    /**
     * @param ywLoadingDialog
     * @param title_type      1SOS订单支付，2订单支付，3优惠券支付，4推广订单支付，5账户充值
     * @param jsonObject
     * @param activity
     * @throws JSONException
     */
    private static void aliPay(final YWLoadingDialog ywLoadingDialog, final int title_type, final JSONObject jsonObject, final Activity activity) throws JSONException {
        final HashMap<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("Version", jsonObject.getJSONObject("data").getString("Version"));//接口版本，目前只有固定值1.0
        requestMap.put("TradeDate", jsonObject.getJSONObject("data").getString("TradeDate"));// 交易日期yyyyMMdd
        requestMap.put("TradeTime", jsonObject.getJSONObject("data").getString("TradeTime"));// 交易时间HHmmss
        requestMap.put("PartnerId", jsonObject.getJSONObject("data").getString("PartnerId")); // 签约合作方的唯一用户号
        requestMap.put("InputCharset", jsonObject.getJSONObject("data").getString("InputCharset"));// 商户网站使用的编码格式，如utf-8、gbk、gb2312等
        requestMap.put("MchId", jsonObject.getJSONObject("data").getString("MchId"));// 商户标示ID
        requestMap.put("PAY_KEY", jsonObject.getJSONObject("data").getString("PAY_KEY"));// 支付密钥
        requestMap.put("AppId", jsonObject.getJSONObject("data").getString("AppId"));// 微信/支付宝给商户开通的appid


        requestMap.put("TradeAmount", jsonObject.getJSONObject("data").getString("TradeAmount"));// 金额
        requestMap.put("GoodsName", jsonObject.getJSONObject("data").getString("GoodsName"));// 商品名称
        requestMap.put("Subject", jsonObject.getJSONObject("data").getString("Subject"));// 订单标题
        requestMap.put("OutTradeNo", jsonObject.getJSONObject("data").getString("OutTradeNo"));// 订单号
        requestMap.put("TradeType", jsonObject.getJSONObject("data").getString("TradeType"));
        requestMap.put("DeviceInfo", DeviceUtils.getIMEI(activity));
//        requestMap.put("EnsureAmount", jsonObject.getJSONObject("data").getString("EnsureAmount"));//担保金额
        requestMap.put("BankCode", jsonObject.getJSONObject("data").getString("BankCode"));
        requestMap.put("NotifyUrl", jsonObject.getJSONObject("data").getString("NotifyUrl"));// 服务器异步通知页面路
        YQPayApi.doPay(activity, requestMap, new IYQPayCallback() {
            @Override
            public void payResult(final String status, final String message) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        if (ywLoadingDialog != null) {
                            ywLoadingDialog.disMiss();
                        }
//                        dialog.dismiss();
                        LogUtils.e("RESULT", "CALLBACK: " + "status:" + status + " message:" + message);
                        if (status == null || "".equals(status)) {
                            Toast.makeText(activity, "获取预付订单失败！", Toast.LENGTH_LONG).show();
                        } else {
                            if (CALLBACK_FAILD.equals(status)) {
                                String msg = "";
                                if (CALLBACK_PARAM_FREE.equals(message)) {
                                    msg = "调用收银台时没有传递参数";
                                } else if (CALLBACK_SERVER_NO_PARAM.equals(message)) {
                                    msg = "订单生成失败";
                                } else if (CALLBACK_HTTPSTATUSEXCEPTION.equals(message)) {
                                    msg = "网络连接异常";
                                } else if (CALLBACK_IOEXCEPTION.equals(message)) {
                                    msg = "网络读取异常";
                                } else if (CALLBACK_NONETWORK.equals(message)) {
                                    msg = "无访问网络";
                                } else if (CALLBACK_DATAERROR.equals(message)) {
                                    msg = "数据解析错误";
                                } else if (CALLBACK_CANCEL_PAY.equals(message)) {
                                    msg = "取消支付";
                                } else if (CALLBACK_FAILED_PAY.equals(message)) {
                                    msg = "支付失败";
                                } else if (CALLBACK_PROCESSED_PAY.equals(message)) {
                                    msg = "正在处理中";
                                } else {
                                    msg = message;
                                }
//                                    Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
                                ToastUtils.showShortToast(activity, msg);
                            } else if (CALLBACK_SUCCESS.equals(status)) {
                                ToastUtils.showShortToast(activity, "下单成功！");
                                activity.startActivity(new Intent(activity, ActivityPayInfo.class).putExtra("requestMap", (Serializable) requestMap)
                                                .putExtra("respMap", message)
                                                .putExtra("title_type", title_type)
                                /*.putExtra("orderID",jsonObject.getJSONObject("data").getString("OutTradeNo"))*/);
                            } else {
                                HashMap<String, String> respMap = new Gson().fromJson(message, HashMap.class);
                                ToastUtils.showLongToast(activity, respMap.get("RetMap"));
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * @param ywLoadingDialog
     * @param title_type      1SOS订单支付，2订单支付，3优惠券支付，4推广订单支付，5账户充值
     * @param jsonObject
     * @param activity
     * @throws JSONException
     */
    private static void wxPay(final YWLoadingDialog ywLoadingDialog, final int title_type, final JSONObject jsonObject, final Activity activity) throws JSONException {
        final HashMap<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("Version", jsonObject.getJSONObject("data").getString("Version"));//接口版本，目前只有固定值1.0
        requestMap.put("TradeDate", jsonObject.getJSONObject("data").getString("TradeDate"));// 交易日期yyyyMMdd
        requestMap.put("TradeTime", jsonObject.getJSONObject("data").getString("TradeTime"));// 交易时间HHmmss
        requestMap.put("PartnerId", jsonObject.getJSONObject("data").getString("PartnerId")); // 签约合作方的唯一用户号
        requestMap.put("InputCharset", jsonObject.getJSONObject("data").getString("InputCharset"));// 商户网站使用的编码格式，如utf-8、gbk、gb2312等
        requestMap.put("MchId", jsonObject.getJSONObject("data").getString("MchId"));// 商户标示ID
        requestMap.put("PAY_KEY", jsonObject.getJSONObject("data").getString("PAY_KEY"));// 支付密钥
        requestMap.put("AppId", jsonObject.getJSONObject("data").getString("AppId"));// 微信/支付宝给商户开通的appid


        requestMap.put("TradeAmount", jsonObject.getJSONObject("data").getString("TradeAmount"));// 金额
        requestMap.put("GoodsName", jsonObject.getJSONObject("data").getString("GoodsName"));// 商品名称
        requestMap.put("Subject", jsonObject.getJSONObject("data").getString("Subject"));// 订单标题
        requestMap.put("OutTradeNo", jsonObject.getJSONObject("data").getString("OutTradeNo"));// 订单号
        requestMap.put("TradeType", jsonObject.getJSONObject("data").getString("TradeType"));
        requestMap.put("DeviceInfo", DeviceUtils.getIMEI(activity));
//        requestMap.put("EnsureAmount", jsonObject.getJSONObject("data").getString("EnsureAmount"));//担保金额
        requestMap.put("BankCode", jsonObject.getJSONObject("data").getString("BankCode"));
        requestMap.put("NotifyUrl", jsonObject.getJSONObject("data").getString("NotifyUrl"));// 服务器异步通知页面路
        YQPayApi.doPay(activity, requestMap, new IYQPayCallback() {
            @Override
            public void payResult(final String status, final String message) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        if (ywLoadingDialog != null) {
                            ywLoadingDialog.disMiss();
                        }
//                        dialog.dismiss();
                        LogUtils.e("RESULT", "CALLBACK: " + "status:" + status + " message:" + message);
                        if (status == null || "".equals(status)) {
                            Toast.makeText(activity, "获取预付订单失败！", Toast.LENGTH_LONG).show();
                        } else {
                            if (CALLBACK_FAILD.equals(status)) {
                                String msg = "";
                                if (CALLBACK_PARAM_FREE.equals(message)) {
                                    msg = "调用收银台时没有传递参数";
                                } else if (CALLBACK_SERVER_NO_PARAM.equals(message)) {
                                    msg = "订单生成失败";
                                } else if (CALLBACK_HTTPSTATUSEXCEPTION.equals(message)) {
                                    msg = "网络连接异常";
                                } else if (CALLBACK_IOEXCEPTION.equals(message)) {
                                    msg = "网络读取异常";
                                } else if (CALLBACK_NONETWORK.equals(message)) {
                                    msg = "无访问网络";
                                } else if (CALLBACK_DATAERROR.equals(message)) {
                                    msg = "数据解析错误";
                                } else if (CALLBACK_CANCEL_PAY.equals(message)) {
                                    msg = "取消支付";
                                } else if (CALLBACK_FAILED_PAY.equals(message)) {
                                    msg = "支付失败";
                                } else if (CALLBACK_PROCESSED_PAY.equals(message)) {
                                    msg = "正在处理中";
                                } else {
                                    msg = message;
                                }
//                                    Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
                                ToastUtils.showShortToast(activity, msg);
                            } else if (CALLBACK_SUCCESS.equals(status)) {
                                ToastUtils.showShortToast(activity, "下单成功！");
                                activity.startActivity(new Intent(activity, ActivityPayInfo.class).putExtra("requestMap", (Serializable) requestMap)
                                                .putExtra("respMap", message)
                                                .putExtra("title_type", title_type)
                                /*.putExtra("orderID",jsonObject.getJSONObject("data").getString("OutTradeNo"))*/);
                            } else {
                                HashMap<String, String> respMap = new Gson().fromJson(message, HashMap.class);
                                ToastUtils.showLongToast(activity, respMap.get("RetMap"));
                            }
                        }
                    }
                });
            }
        });
    }

    private static YWLoadingDialog showToast(YWLoadingDialog ywLoadingDialog, Activity activity, String str) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(activity);
        ywLoadingDialog.show();
        ywLoadingDialog.setTitle(str);
        return ywLoadingDialog;
    }


}
