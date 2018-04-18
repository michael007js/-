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
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.rongyun.RongYunUtils;
import com.sss.car.utils.CarUtils;
import com.sss.car.utils.OrderUtils;
import com.sss.car.view.ActivityImages;
import com.sss.car.view.ActivityShopInfo;

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

//ActivityOrderDisposeSellerRightTopButtonDetails

/**
 * 收入 代付款,待收货,已完成,退换货公用Fragment
 * Created by leilei on 2017/11/9.
 */
@SuppressLint("ValidFragment")
public class NewOrderFragmentSeller extends BaseFragment implements CustomListViewOrderSeller.OnCustomListViewCallBack {
    public static final int Type_Payment = 1;//待付款
    public static final int Type_Reveived = 2;//待收货
    public static final int Type_Complete = 3;//已完成
    public static final int Type_Returns = 4;//退换货
    int type;
    YWLoadingDialog ywLoadingDialog;
    List<OrderModel> list = new ArrayList<>();
    @BindView(R.id.list_new_order_fragment)
    CustomListViewOrderSeller listNewOrderFragment;
    Unbinder unbinder;
    BaseActivity baseActivity;
    @BindView(R.id.refresh)
    PullToRefreshScrollView refresh;
    int p = 1;
    @BindView(R.id.empty_view)
    SimpleDraweeView emptyView;


    public NewOrderFragmentSeller(int type, BaseActivity baseActivity) {
        this.type = type;
        this.baseActivity = baseActivity;
    }

    @Override
    protected int setContentView() {
        return R.layout.new_order_fragment_seller;
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
                                listNewOrderFragment.setonCustomListViewCallBack(NewOrderFragmentSeller.this);
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
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getOrderinfo(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("type", type)
                            .put("p", p)
                            .toString(), "", new StringCallback() {
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
                                        listNewOrderFragment.setList(getBaseFragmentActivityContext(), list);
                                    }
                                    if (list.size() > 0) {
                                        emptyView.setVisibility(View.GONE);
                                        listNewOrderFragment.setVisibility(View.VISIBLE);
                                    }else {
                                        emptyView.setVisibility(View.VISIBLE);
                                        listNewOrderFragment.setVisibility(View.GONE);

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
     * 列表项被点击
     */
    @Override
    public void onItem(String types, String order_id, String shop_id, int status, OrderModel orderModel) {
        LogUtils.e(orderModel.status + "---" + types);
//        switch (Integer.valueOf(type)) {
//            case Type_Payment:
//
//                if (orderModel.status == Constant.Ready_Buy) {
//                    if ("1".equals(types)) {
//                        if (getBaseFragmentActivityContext() != null) {
//                            startActivity(new Intent(getBaseFragmentActivityContext(), OrderGoodsReadyBuyList.class)
//                                    .putExtra("order_id", order_id));
//                        }
//                    } else if ("2".equals(types)) {
//                        if (getBaseFragmentActivityContext() != null) {
//                            startActivity(new Intent(getBaseFragmentActivityContext(), OrderServiceReadyBuyList.class)
//                                    .putExtra("order_id", order_id));
//                        }
//                    }
//
//                } else if (orderModel.status == Constant.Non_Payment) {
//                    if ("2".equals(types)) {
//                        startActivity(new Intent(getBaseFragmentActivityContext(), OrderServiceOrderTip.class)
//                                .putExtra("order_id", order_id)
//                                .putExtra("buttonTitle", "待买家支付"));
//                    } else {
//                        startActivity(new Intent(getBaseFragmentActivityContext(), OrderGoodsOrderTip.class)
//                                .putExtra("order_id", order_id)
//                                .putExtra("buttonTitle", "待买家支付"));
//
//                    }
//                }
//                break;
//            case Type_Reveived:
//                if (orderModel.status == Constant.Have_Already_Paid_Awating_Delivery) {
//                    if ("1".equals(types)) {
//                        if (getBaseFragmentActivityContext() != null) {
//                            startActivity(new Intent(getBaseFragmentActivityContext(), OrderGoodsOrderSendSeller.class)
//                                    .putExtra("order_id", order_id)
//                                    .putExtra("status", orderModel.status));
//                        }
//                    } else {
//                        startActivity(new Intent(getBaseFragmentActivityContext(), OrderGoodsOrderTip.class)
//                                .putExtra("order_id", order_id)
//                                .putExtra("status", orderModel.status)
//                                .putExtra("buttonTitle", "待服务"));
//                    }
//                } else if (orderModel.status == Constant.Have_Already_Delivery_Awating_Sign_For) {
//                    startActivity(new Intent(getBaseFragmentActivityContext(), OrderGoodsOrderTip.class)
//                            .putExtra("order_id", order_id)
//                            .putExtra("status", orderModel.status)
//                            .putExtra("buttonTitle", "待签收"));
//                }
//                break;
//            case Type_Complete:
//                if ("0".equals(orderModel.goods_comment)) {
//                    if ("1".equals(types)) {
//                        startActivity(new Intent(getBaseFragmentActivityContext(), OrderGoodsMyOrderBuyer.class)
//                                .putExtra("order_id", order_id)
//                                .putExtra("status", orderModel.status));
//
//                    } else {
//                        startActivity(new Intent(getBaseFragmentActivityContext(), OrderServiceMyOrderBuyer.class)
//                                .putExtra("order_id", order_id)
//                                .putExtra("status", orderModel.status));
//
//                    }
//                } else {
//                    if ("1".equals(types)) {
//                        startActivity(new Intent(getBaseFragmentActivityContext(), OrderGoodsMyOrderBuyer.class)
//                                .putExtra("order_id", order_id)
//                                .putExtra("status", -100));/*传-100为删除订单*/
//                    } else {
//                        startActivity(new Intent(getBaseFragmentActivityContext(), OrderServiceMyOrderBuyer.class)
//                                .putExtra("order_id", order_id)
//                                .putExtra("status", -100));/*传-100为删除订单*/
//                    }
//
//                }
//
//                break;
//            case Type_Returns:
//                if (orderModel.status == Constant.Awating_Dispose_Returns) {
//                    if ("1".equals(types)) {
//                        if (Constant.Awating_Dispose_Returns == orderModel.status) {
//                            if ("1".equals(orderModel.exchange_status)){
//                                startActivity(new Intent(getBaseFragmentActivityContext(), OrderGoodsOrderTip.class)
//                                        .putExtra("order_id", order_id)
//                                        .putExtra("grayness", true)
//                                        .putExtra("buttonTitle", "已同意"));
//                            }else if ("2".equals(orderModel.exchange_status)){
//                                startActivity(new Intent(getBaseFragmentActivityContext(), OrderGoodsOrderTip.class)
//                                        .putExtra("order_id", order_id)
//                                        .putExtra("grayness", true)
//                                        .putExtra("buttonTitle", "未同意"));
//                            }else {
//                                startActivity(new Intent(getBaseFragmentActivityContext(), OrderGoodsOrderSendSeller.class)
//                                        .putExtra("order_id", order_id)
//                                        .putExtra("status", orderModel.status));
//                            }
//
//                        }else if (orderModel.status == Constant.Returns) {
//                            startActivity(new Intent(getBaseFragmentActivityContext(), OrderGoodsOrderSendSeller.class)
//                                    .putExtra("order_id", order_id)
//                                    .putExtra("status", orderModel.status));
//                        }
//                    }
//                } else if (orderModel.status == Constant.Returns || orderModel.status == Constant.Changed) {
//                    startActivity(new Intent(getBaseFragmentActivityContext(), OrderGoodsOrderSendSeller.class)
//                            .putExtra("order_id", order_id)
//                            .putExtra("status", orderModel.status));
//                } else if (orderModel.status == Constant.Refunded) {
//                    startActivity(new Intent(getBaseFragmentActivityContext(), OrderGoodsOrderSendSeller.class)
//                            .putExtra("order_id", order_id)
//                            .putExtra("status", orderModel.status));
//                }
//                break;
//        }
        if ("1".equals(types)) {
            CarUtils.orderJump(getBaseFragmentActivityContext(), "goods", status, orderModel.order_id, true, orderModel.goods_comment, orderModel.is_comment, orderModel.exchange_id, orderModel.exchange_status);
        } else {
            CarUtils.orderJump(getBaseFragmentActivityContext(), "service", status, orderModel.order_id, true, orderModel.goods_comment, orderModel.is_comment, orderModel.exchange_id, orderModel.exchange_status);

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

    /**
     * 待付款
     */
    @Override
    public void onWaitForPayment() {

    }

    /**
     * 联系买家
     */
    @Override
    public void onConnectBuyer(String member_id, String name) {
        if (StringUtils.isEmpty(member_id)) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器数据错误！");
            return;
        }
        RongYunUtils.startConversation(getBaseFragmentActivityContext(), Conversation.ConversationType.PRIVATE, member_id, name);
    }


    /**
     * 待发货
     *
     * @param orderModel
     */
    @Override
    public void onWaitForSend(OrderModel orderModel) {
        if (getBaseFragmentActivityContext() != null) {
            startActivity(new Intent(getBaseFragmentActivityContext(), OrderSendGoodsSeller.class).putExtra("data", orderModel));
        }
    }

    /**
     * 待服务
     */
    @Override
    public void onWaitForService() {

    }

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
     *
     * @param orderModel
     */
    @Override
    public void onImmediateComment(OrderModel orderModel) {
        startActivity(new Intent(getBaseFragmentActivityContext(), OrderCommentSeller.class)
                .putExtra("order_id", orderModel.order_id));
    }

    /**
     * 删除订单
     *
     * @param order_id
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
     * 未处理
     */
    @Override
    public void onNoDispose() {

    }

    /**
     * 已同意
     */
    @Override
    public void onAlreadyAgree() {

    }

    /**
     * 已拒绝
     */
    @Override
    public void onAlreadyReject() {

    }

    /**
     * 确认收货
     *
     * @param orderModel
     */
    @Override
    public void onConfirmReceipt(final OrderModel orderModel) {
        APPOftenUtils.createAskDialog(getBaseFragmentActivityContext(), "确认要收货吗？", new OnAskDialogCallBack() {
            @Override
            public void onOKey(Dialog dialog) {
                dialog.dismiss();
                dialog = null;
                if (Constant.Changed == orderModel.status) {
                    OrderUtils.exchange_goods(baseActivity, ywLoadingDialog, false, orderModel.order_id, orderModel.exchange_id);
                } else if (Constant.Returns == orderModel.status) {
                    OrderUtils.confirm_goods(baseActivity, ywLoadingDialog, false, orderModel.order_id, orderModel.exchange_id);
                }
            }

            @Override
            public void onCancel(Dialog dialog) {
                dialog.dismiss();
                dialog = null;
            }
        });
    }

    /**
     * 立即发货
     * @param orderModel
     */
    @Override
    public void onImmediateDelivery(OrderModel orderModel) {
        if ("1".equals(orderModel.type)) {
            CarUtils.orderJump(getBaseFragmentActivityContext(), "goods", orderModel.status, orderModel.order_id, true, orderModel.goods_comment, orderModel.is_comment, orderModel.exchange_id, orderModel.exchange_status);
        } else {
            CarUtils.orderJump(getBaseFragmentActivityContext(), "service", orderModel.status, orderModel.order_id, true, orderModel.goods_comment, orderModel.is_comment, orderModel.exchange_id, orderModel.exchange_status);

        }
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
    public void onCancelOrder(String order_id) {
        OrderUtils.cancelOrder(baseActivity, ywLoadingDialog, true, order_id);
    }

    /**
     * 立即处理（退换货）
     */
    @Override
    public void onImmediateProcessing(OrderModel orderModel) {
        if (getBaseFragmentActivityContext() != null) {
            startActivity(new Intent(getBaseFragmentActivityContext(), OrderReturnsChangeApplyForAndCompleteDataSeller.class)
                    .putExtra("order_id", orderModel.order_id));
        }

    }

    /**
     * 立即处理(预购)
     */
    @Override
    public void onImmediateProcessingReadyBuy(OrderModel orderModel) {
        if ("1".equals(orderModel.type)) {
            if (getBaseFragmentActivityContext() != null) {
                startActivity(new Intent(getBaseFragmentActivityContext(), OrderGoodsReadyBuyList.class)
                        .putExtra("order_id", orderModel.order_id));
            }
        } else if ("2".equals(orderModel.type)) {
            if (getBaseFragmentActivityContext() != null) {
                startActivity(new Intent(getBaseFragmentActivityContext(), OrderServiceReadyBuyList.class)
                        .putExtra("order_id", orderModel.order_id));
            }

        }
    }

    @Override
    public void onDepositChangedPrice(OrderModel orderModel) {
        if ("1".equals(orderModel.type)) {
            CarUtils.orderJump(
                    getBaseFragmentActivityContext(),
                    "goods",
                    Integer.valueOf(orderModel.status),
                    orderModel.order_id,
                    true,
                    orderModel.goods_comment,
                    orderModel.is_comment,
                    orderModel.exchange_id,
                    orderModel.exchange_status,
                    orderModel.is_bargain);
        } else if ("2".equals(orderModel.type)) {
            CarUtils.orderJump(
                    getBaseFragmentActivityContext(),
                    "service",
                    Integer.valueOf(orderModel.status),
                    orderModel.order_id,
                    true,
                    orderModel.goods_comment,
                    orderModel.is_comment,
                    orderModel.exchange_id,
                    orderModel.exchange_status,
                    orderModel.is_bargain);

        }

    }
}
