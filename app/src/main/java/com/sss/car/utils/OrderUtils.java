package com.sss.car.utils;

import android.content.Intent;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedOrderModel;
import com.sss.car.RequestWeb;
import com.sss.car.view.ActivityWeb;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by leilei on 2017/11/10.
 */

@SuppressWarnings("ALL")
public class OrderUtils {


    /**
     * 取消订单
     *
     * @param activity
     * @param ywLoadingDialog
     * @param isFinish
     * @param order_id
     */
    public static void cancelOrder(final BaseActivity activity, YWLoadingDialog ywLoadingDialog, final boolean isFinish, String order_id) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(activity);
        ywLoadingDialog.show();

        try {
            final YWLoadingDialog finalYwLoadingDialog = ywLoadingDialog;
            activity.addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.cancelOrder(
                    new JSONObject()
                            .put("order_id", order_id)
                            .put("member_id", Config.member_id)
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (finalYwLoadingDialog != null) {
                                finalYwLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(activity, e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (finalYwLoadingDialog != null) {
                                finalYwLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    if (isFinish) {
                                        activity.finish();
                                    }
                                    EventBus.getDefault().post(new ChangedOrderModel());
                                } else {
                                    ToastUtils.showShortToast(activity, jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(activity, "数据解析错误Err:-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(activity, "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }


    /**
     * 支付
     *
     * @param activity
     * @param ywLoadingDialog
     * @param is_integral
     * @param mode            支付方式：balance余额，ali_pay支付，we_chat_pay微信支付
     * @param isFinish
     * @param order_id
     */
    public static void payment_order(final BaseActivity activity, final YWLoadingDialog ywLoadingDialog, final String is_integral, String mode, final boolean isFinish, String order_id) {
        try {
            new RequestModel(System.currentTimeMillis() + "", RequestWeb.payment_order(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("order_id", order_id)
                            .put("is_integral", is_integral)
                            .put("mode", mode)
                            .toString()
                    , new StringCallback() {
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
                                    EventBus.getDefault().post(new ChangedOrderModel());
                                    if (isFinish) {
                                        activity.finish();
                                    }
                                } else {
                                    ToastUtils.showShortToast(activity, jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(activity, "数据解析错误Err:p-0");
                                e.printStackTrace();
                            }
                        }
                    }));
        } catch (JSONException e) {
            ToastUtils.showShortToast(activity, "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }

    /**
     * 确认收货
     */
    public static void sureOrderGoods(final BaseActivity activity, final YWLoadingDialog ywLoadingDialog, final boolean isFinish, final String order_id) {
        try {
            activity.addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.sureOrderGoods(
                    new JSONObject()
                            .put("order_id", order_id)
                            .put("member_id", Config.member_id)
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(activity, e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    EventBus.getDefault().post(new ChangedOrderModel());
                                    if (isFinish) {
                                        activity.finish();
                                    }
                                } else {
                                    ToastUtils.showShortToast(activity, jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(activity, "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(activity, "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }

    /**
     * 确认服务
     */
    public static void service_goods(final BaseActivity activity, final YWLoadingDialog ywLoadingDialog, final boolean isFinish, final String order_id) {
        try {
            activity.addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.service_goods(
                    new JSONObject()
                            .put("order_id", order_id)
                            .put("member_id", Config.member_id)
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(activity, e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    EventBus.getDefault().post(new ChangedOrderModel());
                                    if (isFinish) {
                                        activity.finish();
                                    }
                                } else {
                                    ToastUtils.showShortToast(activity, jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(activity, "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(activity, "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }

    /**
     * 删除支出订单
     */
    public static void del_expend(final BaseActivity activity, final YWLoadingDialog ywLoadingDialog, final boolean isFinish, final String order_id) {
        try {
            activity.addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.del_expend(
                    new JSONObject()
                            .put("order_id", order_id)
                            .put("member_id", Config.member_id)
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(activity, e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    EventBus.getDefault().post(new ChangedOrderModel());

                                    if (isFinish) {
                                        activity.finish();
                                    }
                                } else {
                                    ToastUtils.showShortToast(activity, jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(activity, "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(activity, "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }

    /**
     * 立即发货
     */
    public static void deliver(final BaseActivity activity, final YWLoadingDialog ywLoadingDialog, final boolean isFinish, final String order_id, String express_id, String waybill) {
        try {
            activity.addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.deliver(
                    new JSONObject()
                            .put("express_id", express_id)
                            .put("waybill", waybill)
                            .put("member_id", Config.member_id)
                            .put("order_id", order_id)
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(activity, e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    EventBus.getDefault().post(new ChangedOrderModel());
                                    if (isFinish) {
                                        activity.finish();
                                    }
                                } else {
                                    ToastUtils.showShortToast(activity, jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(activity, "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(activity, "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }

    /**
     * 商家确认收货（退货）
     */
    public static void confirm_goods(final BaseActivity activity, final YWLoadingDialog ywLoadingDialog, final boolean isFinish, final String order_id, String exchange_id) {
        try {
            activity.addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.confirm_goods(
                    new JSONObject()
                            .put("order_id", order_id)
                            .put("exchange_id", exchange_id)
                            .put("member_id", Config.member_id)
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(activity, e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    EventBus.getDefault().post(new ChangedOrderModel());
                                    if (isFinish) {
                                        activity.finish();
                                    }
                                } else {
                                    ToastUtils.showShortToast(activity, jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(activity, "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(activity, "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }

    /**
     * 商家确认收货（换货）
     */
    public static void exchange_goods(final BaseActivity activity, final YWLoadingDialog ywLoadingDialog, final boolean isFinish, final String order_id, String exchange_id) {
        try {
            activity.addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.exchange_goods(
                    new JSONObject()
                            .put("order_id", order_id)
                            .put("exchange_id", exchange_id)
                            .put("member_id", Config.member_id)
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(activity, e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    EventBus.getDefault().post(new ChangedOrderModel());
                                    if (isFinish) {
                                        activity.finish();
                                    }
                                } else {
                                    ToastUtils.showShortToast(activity, jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(activity, "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(activity, "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }

    /**
     * 查看物流
     */
    public static void Logistics(final BaseActivity activity, String order_id) {
        if (activity != null) {
            activity.startActivity(new Intent(activity, ActivityWeb.class)
                    .putExtra("type", ActivityWeb.LOGISTICS)
                    .putExtra("order_id", order_id));
        }


    }
}
