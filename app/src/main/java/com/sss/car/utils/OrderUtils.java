package com.sss.car.utils;

import android.content.Intent;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.ZhiFuBaoPasswordStyle.PassWordKeyboard;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PriceUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.rey.material.app.BottomSheetDialog;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedMessageOrderList;
import com.sss.car.EventBusModel.ChangedOrderModel;
import com.sss.car.P;
import com.sss.car.RequestWeb;
import com.sss.car.dao.OnPayPasswordVerificationCallBack;
import com.sss.car.dao.OnPriceCallBack;
import com.sss.car.dao.OnPriceGoodsServiceCallBack;
import com.sss.car.model.OrderPayModel;
import com.sss.car.view.ActivityMyDataSetPassword;
import com.sss.car.view.ActivityWeb;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.widget.AsyncImageView;
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


    public static void start(int where, MenuDialog menuDialog, final BaseActivity activity, final YWLoadingDialog ywLoadingDialog, final boolean isFinish, final String order_id, final double total) {

        if (menuDialog == null) {
            menuDialog = new MenuDialog(activity);
        }
        if (where == 1||where==2) {
            pay_goods_list(where,menuDialog, activity, ywLoadingDialog, isFinish, order_id);
        } else if (where == 3) {
            sureOrderGoodsSOS(menuDialog, activity, ywLoadingDialog, isFinish, order_id, total);
        }

    }
AsyncImageView asyncImageView;
    /**
     * 获取商品订单商品列表
     */
    private static void pay_goods_list(final int where, final MenuDialog menuDialog, final BaseActivity activity, final YWLoadingDialog ywLoadingDialog, final boolean isFinish, final String order_id) {
        try {
            if (ywLoadingDialog != null) {
                ywLoadingDialog.show();
            }
            activity.addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.pay_goods_list(
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
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                List<OrderPayModel> list = new ArrayList<>();
                                Gson gson = new Gson();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    list.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), OrderPayModel.class));
                                }
                                if (1 == where) {
                                    sureOrderGoods(where,menuDialog, activity, ywLoadingDialog, isFinish, order_id, list);
                                } else {
                                    service_goods(where,menuDialog, activity, ywLoadingDialog, isFinish, order_id, list);
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
     * 确认SOS价格调节框
     */
    public static void sureOrderGoodsSOS(final MenuDialog menuDialog, final BaseActivity activity, final YWLoadingDialog ywLoadingDialog, final boolean isFinish, final String order_id, final double total) {
        P.e(ywLoadingDialog, Config.member_id, activity, new P.p() {
            @Override
            public void exist() {
                menuDialog.createPasswordInputDialog("请输入您的支付密码", activity, new OnPayPasswordVerificationCallBack() {
                    @Override
                    public void onVerificationPassword(String password, final PassWordKeyboard passWordKeyboard, final com.rey.material.app.BottomSheetDialog bottomSheetDialog) {
                        P.r(ywLoadingDialog, Config.member_id, password, activity, new P.r() {
                            @Override
                            public void match() {
                                bottomSheetDialog.dismiss();
                                passWordKeyboard.setStatus(true);
                                menuDialog.createPriceDialog_SOS("确认SOS服务", total, activity, new OnPriceCallBack() {
                                    @Override
                                    public void onPriceCallBack(double doublePrice, BottomSheetDialog bottomSheetDialog) {

                                        if (doublePrice < 1) {
                                            ToastUtils.showShortToast(activity, "最低不得小于一元！");
                                        } else {
                                            if (doublePrice > total) {
                                                PayUtils.requestPayment(ywLoadingDialog, true, "0", Config.member_id, 3,
                                                        0, PriceUtils.formatBy2Scale(doublePrice - total, 2), activity, "1","0");
                                            } else {
                                                bargain("sos", activity, ywLoadingDialog, isFinish, order_id, doublePrice);

                                            }
                                        }
                                        bottomSheetDialog.dismiss();

                                    }
                                });
                            }

                            @Override
                            public void mismatches() {

                                passWordKeyboard.setStatus(false);
                            }
                        });
                    }

                });
            }


            @Override
            public void nonexistence() {
                if (activity != null) {
                    activity.startActivity(new Intent(activity, ActivityMyDataSetPassword.class));
                }
            }
        });


    }

    /**
     * 实物——确认收货价格调节框
     */
    public static void sureOrderGoods(final int where, final MenuDialog menuDialog, final BaseActivity activity, final YWLoadingDialog ywLoadingDialog, final boolean isFinish, final String order_id, final List<OrderPayModel> list) {
        P.e(ywLoadingDialog, Config.member_id, activity, new P.p() {
            @Override
            public void exist() {
                menuDialog.createPasswordInputDialog("请输入您的支付密码", activity, new OnPayPasswordVerificationCallBack() {
                    @Override
                    public void onVerificationPassword(String password, final PassWordKeyboard passWordKeyboard, final com.rey.material.app.BottomSheetDialog bottomSheetDialog) {
                        P.r(ywLoadingDialog, Config.member_id, password, activity, new P.r() {
                            @Override
                            public void match() {
                                bottomSheetDialog.dismiss();
                                passWordKeyboard.setStatus(true);
                                menuDialog.createPriceDialog_GoodsService("确认收货", list, activity, new OnPriceGoodsServiceCallBack() {
                                    @Override
                                    public void onPriceCallBack(List<OrderPayModel> list, BottomSheetDialog bottomSheetDialog) {
                                        JSONArray jsonArray=new JSONArray();
                                        for (int i = 0; i < list.size(); i++) {
                                            try {
                                                jsonArray.put(new JSONObject()
                                                        .put("goods_id",list.get(i).goods_id)
                                                        .put("title",list.get(i).title)
                                                        .put("total",list.get(i).total)
                                                );
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        pay_order_goods(where,menuDialog,activity,ywLoadingDialog,isFinish,order_id,jsonArray);

                                    }
                                });
                            }

                            @Override
                            public void mismatches() {

                                passWordKeyboard.setStatus(false);
                            }
                        });
                    }

                });
            }


            @Override
            public void nonexistence() {
                if (activity != null) {
                    activity.startActivity(new Intent(activity, ActivityMyDataSetPassword.class));
                }
            }
        });


    }

    /**
     * 服务——确认收货价格调节框
     */
    public static void service_goods(final int where, final MenuDialog menuDialog, final BaseActivity activity, final YWLoadingDialog ywLoadingDialog, final boolean isFinish, final String order_id, final List<OrderPayModel> list) {

        P.e(ywLoadingDialog, Config.member_id, activity, new P.p() {
            @Override
            public void exist() {
                menuDialog.createPasswordInputDialog("请输入您的支付密码", activity, new OnPayPasswordVerificationCallBack() {
                    @Override
                    public void onVerificationPassword(String password, final PassWordKeyboard passWordKeyboard, final com.rey.material.app.BottomSheetDialog bottomSheetDialog) {
                        P.r(ywLoadingDialog, Config.member_id, password, activity, new P.r() {
                            @Override
                            public void match() {
                                bottomSheetDialog.dismiss();
                                passWordKeyboard.setStatus(true);
                                menuDialog.createPriceDialog_GoodsService("确认服务", list, activity, new OnPriceGoodsServiceCallBack() {
                                    @Override
                                    public void onPriceCallBack(List<OrderPayModel> list, BottomSheetDialog bottomSheetDialog) {
                                        JSONArray jsonArray=new JSONArray();
                                        for (int i = 0; i < list.size(); i++) {
                                            try {
                                                jsonArray.put(new JSONObject()
                                                        .put("goods_id",list.get(i).goods_id)
                                                        .put("title",list.get(i).title)
                                                        .put("total",list.get(i).total)
                                                );
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        pay_order_goods(where,menuDialog,activity,ywLoadingDialog,isFinish,order_id,jsonArray);
                                    }
                                });
                            }

                            @Override
                            public void mismatches() {

                                passWordKeyboard.setStatus(false);
                            }
                        });
                    }

                });
            }


            @Override
            public void nonexistence() {
                if (activity != null) {
                    activity.startActivity(new Intent(activity, ActivityMyDataSetPassword.class));
                }
            }
        });


    }

    /**
     * 更新商品价格
     */
    private static void pay_order_goods(final int where, final MenuDialog menuDialog, final BaseActivity activity, final YWLoadingDialog ywLoadingDialog, final boolean isFinish,
                                        final String order_id,JSONArray jsonArray) {
        try {
            if (ywLoadingDialog != null) {
                ywLoadingDialog.show();
            }
            activity.addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.pay_order_goods(
                    new JSONObject()
                            .put("order_id", order_id)
                            .put("goods_data",jsonArray)
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
                                    double money=jsonObject.getJSONObject("data").getDouble("money");
                                    if (money==0){
                                        if (where==1) {
                                            goods(activity, ywLoadingDialog, isFinish, order_id);
                                        } else if (where==2) {
                                            service(activity, ywLoadingDialog, isFinish, order_id);
                                        }
                                    }else   if (money>0){
                                        PayUtils.requestPayment(ywLoadingDialog, false, "0", Config.member_id, 5, 0,
                                                PriceUtils.formatBy2Scale(jsonObject.getJSONObject("data").getDouble("money"), 2), activity, "1","1");
                                    }else {
                                        EventBus.getDefault().post(new ChangedOrderModel());
                                        if (isFinish) {
                                            activity.finish();
                                        }
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
     * 用户订单议价
     */
    public static void bargain(final String where, final BaseActivity activity, final YWLoadingDialog ywLoadingDialog, final boolean isFinish, final String order_id, double money) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.show();
        }
        try {
            RequestWeb.bargain(
                    new JSONObject()
                            .put("money", money)
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
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    if ("0".equals(jsonObject.getJSONObject("data").getString("money"))) {
                                        if ("sos".equals(where)) {
                                            accomplish(activity, ywLoadingDialog, isFinish, order_id);
                                        } else if ("goods".equals(where)) {
                                            goods(activity, ywLoadingDialog, isFinish, order_id);
                                        } else if ("service".equals(where)) {
                                            service(activity, ywLoadingDialog, isFinish, order_id);
                                        }
                                    } else {
                                        PayUtils.requestPayment(ywLoadingDialog, true, "0", Config.member_id, 3, 0,
                                                PriceUtils.formatBy2Scale(jsonObject.getJSONObject("data").getDouble("money"), 2), activity, "1","1");
                                    }
                                } else {
                                    ToastUtils.showShortToast(activity, jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(activity, "数据解析错误Err:-0");
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (JSONException e) {
            ToastUtils.showShortToast(activity, "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }

    /**
     * 完成SOS订单
     */
    public static void accomplish(final BaseActivity activity, final YWLoadingDialog ywLoadingDialog, final boolean isFinish, final String order_id) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.show();
        }
        try {
            RequestWeb.accomplish(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("sos_id", order_id)
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
                                    ToastUtils.showShortToast(activity, jsonObject.getString("message"));
                                    EventBus.getDefault().post(new ChangedMessageOrderList());
                                    if (isFinish) {
                                        activity.finish();
                                    }
                                } else {
                                    ToastUtils.showShortToast(activity, jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(activity, "数据解析错误Err:-0");
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (JSONException e) {
            ToastUtils.showShortToast(activity, "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }

    /**
     * 确认收货
     */
    public static void goods(final BaseActivity activity, final YWLoadingDialog ywLoadingDialog, final boolean isFinish, final String order_id) {
        try {
            if (ywLoadingDialog != null) {
                ywLoadingDialog.show();
            }
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
    private static void service(final BaseActivity activity, final YWLoadingDialog ywLoadingDialog, final boolean isFinish, final String order_id) {
        try {
            if (ywLoadingDialog != null) {
                ywLoadingDialog.show();
            }
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
     * 同意拒绝
     */
    public static void status_bargain(final BaseActivity activity, final YWLoadingDialog ywLoadingDialog, final boolean isFinish, final String order_id, String status) {
        try {
            activity.addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.status_bargain(
                    new JSONObject()
                            .put("status", status)
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
