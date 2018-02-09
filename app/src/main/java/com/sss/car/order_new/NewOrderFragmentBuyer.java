package com.sss.car.order_new;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.dao.OnAskDialogCallBack;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshBase;
import com.blankj.utilcode.pullToRefresh.PullToRefreshScrollView;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.PriceUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedOrderModel;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.order.OrderReturns;
import com.sss.car.rongyun.RongYunUtils;
import com.sss.car.utils.CarUtils;
import com.sss.car.utils.MenuDialog;
import com.sss.car.utils.OrderUtils;
import com.sss.car.utils.PayUtils;
import com.sss.car.view.ActivityImages;
import com.sss.car.view.ActivityShopInfo;
import com.sss.car.view.ActivityUserInfo;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.rong.imlib.model.Conversation;
import okhttp3.Call;


//ActivityOrderCompleteDataBuyerRightTopButtonDetails

/**
 * 支出 代付款,待收货,已完成,退换货公用Fragment
 * Created by leilei on 2017/11/9.
 */
@SuppressLint("ValidFragment")
public class NewOrderFragmentBuyer extends BaseFragment implements CustomListViewOrderBuyer.OnListViewOrderCallBack {
    public static final int Type_Payment = 1;//待付款
    public static final int Type_Reveived = 2;//待收货
    public static final int Type_Complete = 3;//已完成
    public static final int Type_Returns = 4;//退换货
    int type;
    YWLoadingDialog ywLoadingDialog;
    List<OrderModel> list = new ArrayList<>();
    @BindView(R.id.list_new_order_fragment)
    CustomListViewOrderBuyer listNewOrderFragment;
    Unbinder unbinder;
    BaseActivity baseActivity;
    MenuDialog menuDialog;
    int what = 2;
    int p = 1;
    @BindView(R.id.refresh)
    PullToRefreshScrollView refresh;
    @BindView(R.id.empty_view)
    SimpleDraweeView emptyView;


    public NewOrderFragmentBuyer(int type, BaseActivity baseActivity) {
        this.type = type;
        this.baseActivity = baseActivity;
    }

    @Override
    protected int setContentView() {
        return R.layout.new_order_fragment_buyer;
    }

    @Override
    protected void lazyLoad() {
        if (!isLoad) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(100);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listNewOrderFragment.setOnListViewOrderCallBack(NewOrderFragmentBuyer.this);
                                listNewOrderFragment.removeAllViews();
                                list.clear();
                                new_expend();
                                refresh.setMode(PullToRefreshBase.Mode.BOTH);
                                refresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
                                    @Override
                                    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                                        p = 1;
                                        new_expend();
                                    }

                                    @Override
                                    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                                        new_expend();
                                    }
                                });
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

    }

    @Override
    protected void stopLoad() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        listNewOrderFragment = null;
        if (list != null) {
            list.clear();
        }
        list = null;
    }

    public void refresh() {
        listNewOrderFragment.removeAllViews();
        list.clear();
        p = 1;
        new_expend();
    }

    public void new_expend() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();

        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.new_expend(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("type", type)
                            .put("p", p)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (refresh != null) {
                                refresh.onRefreshComplete();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (refresh != null) {
                                refresh.onRefreshComplete();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {

                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (p == 1) {
                                        list.clear();
                                    }
                                    if (jsonArray.length() > 0) {
                                        p++;
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            list.add(new Gson().fromJson(jsonArray.getJSONObject(i).toString(), OrderModel.class));
                                        }
                                        listNewOrderFragment.setList(getBaseFragmentActivityContext(), list, what);
                                    }
                                    if (list.size() > 0) {
                                        emptyView.setVisibility(View.GONE);
                                    }else {
                                        emptyView.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onQRCode(String qr) {
        if (getBaseFragmentActivityContext() != null) {
            List<String> temp = new ArrayList<>();
            temp.add(qr);
            startActivity(new Intent(getBaseFragmentActivityContext(), ActivityImages.class)
                    .putStringArrayListExtra("data", (ArrayList<String>) temp)
                    .putExtra("current", 0));
        }
    }

    /**
     * 商品item被点击
     *
     * @param types
     * @param order_id
     * @param shop_id
     * @param orderModel
     */
    @Override
    public void onItem(String types, String order_id, String shop_id, int status, OrderModel orderModel) {
//        LogUtils.e(orderModel.status + "---" + types);
//        switch (Integer.valueOf(type)) {
//
//            case Type_Payment:
//                if ("2".equals(types)) {
//                    startActivity(new Intent(getBaseFragmentActivityContext(), OrderServiceMyOrderBuyer.class)
//                            .putExtra("order_id", order_id)
//                            .putExtra("status", orderModel.status));
//                } else if ("1".equals(types)) {
//                    startActivity(new Intent(getBaseFragmentActivityContext(), OrderGoodsMyOrderBuyer.class)
//                            .putExtra("order_id", order_id)
//                            .putExtra("status", orderModel.status));
//                }
//                break;
//            case Type_Reveived:
//                if ("2".equals(types)) {
//                    if (orderModel.status == Constant.Have_Already_Paid_Awating_Delivery || orderModel.status == Constant.Ready_Buy) {
//                        startActivity(new Intent(getBaseFragmentActivityContext(), OrderServiceMyOrderBuyer.class)
//                                .putExtra("order_id", order_id)
//                                .putExtra("status", orderModel.status));
//                    }
//                } else if ("1".equals(types)) {
//                    if (orderModel.status == Constant.Have_Already_Paid_Awating_Delivery || orderModel.status == Constant.Ready_Buy) {
//                        startActivity(new Intent(getBaseFragmentActivityContext(), OrderGoodsOrderTip.class)
//                                .putExtra("order_id", order_id)
//                                .putExtra("buttonTitle", "待发货"));
//                    } else {
//                        startActivity(new Intent(getBaseFragmentActivityContext(), OrderGoodsMyOrderBuyer.class)
//                                .putExtra("order_id", order_id)
//                                .putExtra("status", orderModel.status));
//                    }
//                }
//
//                break;
//            case Type_Complete:
//                if ("2".equals(types)) {
//                    if ("0".equals(orderModel.is_comment)) {
//                        startActivity(new Intent(getBaseFragmentActivityContext(), OrderServiceMyOrderBuyer.class)
//                                .putExtra("order_id", order_id)
//                                .putExtra("status", orderModel.status));
//                    } else {
//                        startActivity(new Intent(getBaseFragmentActivityContext(), OrderServiceMyOrderBuyer.class)
//                                .putExtra("order_id", order_id)
//                                .putExtra("status", -100));/*传-100为删除订单*/
//
//                    }
//                } else if ("1".equals(types)) {
//                    if ("0".equals(orderModel.is_comment)) {
//                        startActivity(new Intent(getBaseFragmentActivityContext(), OrderGoodsMyOrderBuyer.class)
//                                .putExtra("order_id", order_id)
//                                .putExtra("status", orderModel.status));
//                    } else {
//                        startActivity(new Intent(getBaseFragmentActivityContext(), OrderGoodsMyOrderBuyer.class)
//                                .putExtra("order_id", order_id)
//                                .putExtra("status", -100));/*传-100为删除订单*/
//                    }
//                }
//
//                break;
//            case Type_Returns:
//                if (orderModel.status == Constant.Awating_Dispose_Returns) {
//                    if ("1".equals(types)) {
//                        if ("0".equals(orderModel.exchange_status)) {
//                            startActivity(new Intent(getBaseFragmentActivityContext(), OrderGoodsOrderTip.class)
//                                    .putExtra("order_id", order_id)
//                                    .putExtra("grayness", true)
//                                    .putExtra("buttonTitle", "未处理"));
//                        } else if ("1".equals(orderModel.exchange_status)) {
//                            startActivity(new Intent(getBaseFragmentActivityContext(), OrderGoodsMyOrderBuyer.class)
//                                    .putExtra("order_id", order_id)
//                                    .putExtra("what", what)
//                                    .putExtra("status", orderModel.status));
//                        } else if ("2".equals(orderModel.exchange_status)) {
//                            startActivity(new Intent(getBaseFragmentActivityContext(), OrderGoodsOrderTip.class)
//                                    .putExtra("order_id", order_id)
//                                    .putExtra("grayness", true)
//                                    .putExtra("buttonTitle", "未同意"));
//                        }
//
//                    }
//                } else if (orderModel.status == Constant.Returns || orderModel.status == Constant.Changed) {
//                    startActivity(new Intent(getBaseFragmentActivityContext(), OrderGoodsOrderTip.class)
//                            .putExtra("order_id", order_id)
//                            .putExtra("grayness", true)
//                            .putExtra("buttonTitle", "待签收"));
//                } else if (orderModel.status == Constant.Refunded) {
//                    startActivity(new Intent(getBaseFragmentActivityContext(), OrderGoodsMyOrderBuyer.class)
//                            .putExtra("order_id", order_id)
//                            .putExtra("status", orderModel.status));
//                } else if (orderModel.status == Constant.Delete) {
//                    if ("1".equals(types)) {
//                        startActivity(new Intent(getBaseFragmentActivityContext(), OrderGoodsOrderTip.class)
//                                .putExtra("order_id", order_id)
//                                .putExtra("hideButton", true));
//                    } else if ("2".equals(types)) {
//                        startActivity(new Intent(getBaseFragmentActivityContext(), OrderServiceOrderTip.class)
//                                .putExtra("order_id", order_id)
//                                .putExtra("hideButton", true));
//                    }
//                }
//                break;
//        }
        if ("1".equals(types)) {
            CarUtils.orderJump(getBaseFragmentActivityContext(), "goods", status, orderModel.order_id, false, orderModel.goods_comment, orderModel.is_comment, orderModel.exchange_id, orderModel.exchange_status);
        } else {
            CarUtils.orderJump(getBaseFragmentActivityContext(), "service", status, orderModel.order_id, false, orderModel.goods_comment, orderModel.is_comment, orderModel.exchange_id, orderModel.exchange_status);

        }
    }

    /**
     * 店铺被点击
     *
     * @param shop_id
     */
    @Override
    public void onShop(String shop_id) {
        if (getBaseFragmentActivityContext() != null) {
            startActivity(new Intent(getBaseFragmentActivityContext(), ActivityShopInfo.class)
                    .putExtra("shop_id", shop_id));
        }

    }

    @Override
    public void onUser(String user_id, String target_name) {
        startActivity(new Intent(getBaseFragmentActivityContext(), ActivityUserInfo.class)
                .putExtra("id", user_id));
    }

    @Override
    public void onPayment(final OrderModel orderModel) {
        PayUtils.requestPayment(ywLoadingDialog, "0", orderModel.order_id, 2, 0, PriceUtils.formatBy2Scale(Double.valueOf(orderModel.total), 2), getActivity());


//        try {
//            String a = orderModel.total;
//            double b = Double.valueOf(a);
//            new PayMentUtils().requestPayment(ywLoadingDialog, "立即付款", (int) b, baseActivity, new PayMentUtils.OnPaymentCallBack() {
//                @Override
//                public void onMatch(String payMode, String is_integral, int price) {
//                    JSONArray jsonArray = new JSONArray();
//                    jsonArray.put(orderModel.order_id);
//                    payment_order(jsonArray, is_integral, "balance");
//                }
//
//                @Override
//                public void onZhiFuBao(String payMode, String is_integral, int price) {
//                    JSONArray jsonArray = new JSONArray();
//                    jsonArray.put(orderModel.order_id);
//                    payment_order(jsonArray, is_integral, "ali_pay");
//                }
//
//                @Override
//                public void onWeiXin(String payMode, String is_integral, int price) {
//                    JSONArray jsonArray = new JSONArray();
//                    jsonArray.put(orderModel.order_id);
//                    payment_order(jsonArray, is_integral, "we_chat_pay");
//                }
//
//                @Override
//                public void onMismatches() {
//                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), "密码不正确");
//                }
//
//                @Override
//                public void onErrorMsg(String msg) {
//                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), msg);
//
//                }
//            });
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误err-0");
//        }

    }


    /**
     * 付款
     *
     * @param order_id
     * @param is_integral
     * @param mode        支付方式：balance余额，ali_pay支付，we_chat_pay微信支付
     * @throws JSONException
     */
    void payment_order(JSONArray order_id, String is_integral, String mode) {
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
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
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
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:p-0");
                                e.printStackTrace();
                            }
                        }
                    }));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:p-0");
            e.printStackTrace();
        }
    }
//    /**
//     * 待付款
//     *
//     * @param type
//     * @param order_id
//     */
//    String integral = "0";
//    String mode="balance";
//    @Override
//    public void onWaitForPayment(String type, String totalPrice, final String order_id) {
//        if (StringUtils.isEmpty(totalPrice)) {
//            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "获取支付价格出错");
//            return;
//        }
//        if (menuDialog == null) {
//            menuDialog = new MenuDialog(getActivity());
//        }
//
//        menuDialog.createPaymentTOrderDialog(ywLoadingDialog, "支付", totalPrice, getActivity(), new OnIntegralCallBack() {
//            @Override
//            public void onIntegralCallBack(String integral, String mode) {
//                NewOrderFragmentBuyer.this.integral = integral;
//                NewOrderFragmentBuyer.this. mode=mode;
//                if ("ali_pay".equals(mode)) {
//                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), "支付宝支付" + "使用积分" + integral);
//                } else if ("we_chat_pay".equals(mode)) {
//                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), "微信支付" + "使用积分" + integral);
//                } else if ("balance".equals(mode)) {
//                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), "钱包支付" + "使用积分" + integral);
//                }
//
//            }
//        }, new OnPayPasswordVerificationCallBack() {
//            @Override
//            public void onVerificationPassword(String password, PassWordKeyboard passWordKeyboard, BottomSheetDialog bottomSheetDialog) {
//                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "密码正确");
//                bottomSheetDialog.dismiss();
//                String a = "0";
//                if ("0".equals(integral)) {
//                    a = "0";
//                } else {
//                    a = "1";
//                }
//                OrderUtils.payment_order(baseActivity, ywLoadingDialog, a,mode, order_id);
//            }
//        });
//    }

    /**
     * 联系卖家
     *
     * @param member_id
     */
    @Override
    public void onConnectSeller(String member_id, String name) {
        if (StringUtils.isEmpty(member_id)) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器数据错误！");
            return;
        }
        RongYunUtils.startConversation(getBaseFragmentActivityContext(), Conversation.ConversationType.PRIVATE, member_id, name);
    }

    /**
     * 待发货
     */
    @Override
    public void onWaitForSend() {

    }

//    /**
//     * 待服务
//     */
//    @Override
//    public void onWaitForService() {
//
//    }

    /**
     * 查看物流
     */
    @Override
    public void onLogistics(String order_id) {
        OrderUtils.Logistics(baseActivity, order_id);
    }

    /**
     * 待签收
     */
    @Override
    public void onWaitForSign() {

    }

    /**
     * 立即评论
     */
    @Override
    public void onImmediateComment(OrderModel orderModel) {
        startActivity(new Intent(getBaseFragmentActivityContext(), OrderCommentBuyer.class)
                .putExtra("order_id", orderModel.order_id));
    }

    /**
     * 删除订单
     */
    @Override
    public void onDeleteOrder(final String order_id) {
        APPOftenUtils.createAskDialog(getBaseFragmentActivityContext(), "是否确定要删除该订单?", new OnAskDialogCallBack() {
            @Override
            public void onOKey(Dialog dialog) {
                dialog.dismiss();
                dialog = null;
                OrderUtils.del_expend(baseActivity, ywLoadingDialog, false, order_id);

            }

            @Override
            public void onCancel(Dialog dialog) {
                dialog.dismiss();
                dialog = null;
            }
        });
    }

    /**
     * 确认收货
     */
    @Override
    public void onConfirmReceipt(final String order_id) {
        APPOftenUtils.createAskDialog(getBaseFragmentActivityContext(), "是否确定要收货?", new OnAskDialogCallBack() {
            @Override
            public void onOKey(Dialog dialog) {
                dialog.dismiss();
                dialog = null;
                OrderUtils.sureOrderGoods(baseActivity, ywLoadingDialog, false, order_id);
            }

            @Override
            public void onCancel(Dialog dialog) {
                dialog.dismiss();
                dialog = null;
            }
        });
    }

    /**
     * 已签收
     */
    @Override
    public void onHaveBeenSigned() {

    }

    /**
     * 已退款
     */
    @Override
    public void onRefunded() {

    }

    /**
     * 取消订单
     */
    @Override
    public void onCancelOrder(final String order_id) {
        APPOftenUtils.createAskDialog(getActivity(), "是否确定要取消订单?", new OnAskDialogCallBack() {
            @Override
            public void onOKey(Dialog dialog) {
                dialog.dismiss();
                dialog = null;
                OrderUtils.cancelOrder(baseActivity, ywLoadingDialog, false, order_id);
            }

            @Override
            public void onCancel(Dialog dialog) {
                dialog.dismiss();
                dialog = null;
            }
        });

    }

    /**
     * 确认服务
     *
     * @param order_id
     */
    @Override
    public void onConfirmedService(final String order_id) {
        APPOftenUtils.createAskDialog(getBaseFragmentActivityContext(), "是否确认服务?", new OnAskDialogCallBack() {
            @Override
            public void onOKey(Dialog dialog) {
                dialog.dismiss();
                dialog = null;
                OrderUtils.service_goods(baseActivity, ywLoadingDialog, false, order_id);
            }

            @Override
            public void onCancel(Dialog dialog) {
                dialog.dismiss();
                dialog = null;
            }
        });
    }

    /**
     * 立即收货
     */
    @Override
    public void onGoodsReceived() {

    }

    /**
     * 退换货
     */
    @Override
    public void onReturnsAndChanged(OrderModel orderModel) {
        if (getBaseFragmentActivityContext() != null) {
            startActivity(new Intent(getBaseFragmentActivityContext(), OrderReturns.class)
                    .putExtra("what", what)
                    .putExtra("returnOrChange_isFirst", true)//第一次打开,申请退换货;如果不是第一次打开,则填写快递信息
                    .putExtra("order_id", orderModel.order_id));
        }
    }

    /**
     * 完善资料
     */
    @Override
    public void onCompleteTheInformation(OrderModel orderModel) {
        if (getBaseFragmentActivityContext() != null) {
            startActivity(new Intent(getBaseFragmentActivityContext(), OrderReturns.class)
                    .putExtra("what", what)
                    .putExtra("returnOrChange_isFirst", false)//第一次打开,申请退换货;如果不是第一次打开,则填写快递信息
                    .putExtra("order_id", orderModel.order_id));
        }
    }

//    /**
//     * 已发货
//     */
//    @Override
//    public void onDelivered() {
//
//    }

    /**
     * 已完成
     */
    @Override
    public void omComplete() {

    }

}
