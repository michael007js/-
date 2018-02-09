package com.sss.car.order_new;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PriceUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedOrderModel;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.ListViewOrderSellerDetails;
import com.sss.car.model.OrderSellerModel;
import com.sss.car.model.OrderSellerModel_Order_Goods;
import com.sss.car.model.ShoppingCart;
import com.sss.car.utils.CarUtils;
import com.sss.car.utils.PayUtils;
import com.sss.car.view.ActivityShopInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


/**
 * 卖家车服预购订单同意或取消
 * Created by leilei on 2017/9/28.
 */

@SuppressWarnings("ALL")
public class OrderServiceReadyBuyList extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.people_name_order_service_ready_buy_list)
    TextView peopleNameOrderServiceReadyBuy;
    @BindView(R.id.mobile_name_order_service_ready_buy_list)
    TextView mobileNameOrderServiceReadyBuy;
    @BindView(R.id.car_name_order_service_ready_buy_list)
    TextView carNameOrderServiceReadyBuy;
    @BindView(R.id.click_choose_car_order_service_ready_buy_list)
    LinearLayout clickChooseCarOrderServiceReadyBuy;
    @BindView(R.id.price_order_service_ready_buy_list)
    TextView priceOrderServiceReadyBuy;
    @BindView(R.id.show_coupon_order_service_ready_buy_list)
    TextView showCouponOrderServiceReadyBuy;
    @BindView(R.id.click_coupon_order_service_ready_buy_list)
    LinearLayout clickCouponOrderServiceReadyBuy;
    @BindView(R.id.show_order_time_order_service_ready_buy_list)
    TextView showOrderTimeOrderServiceReadyBuy;
    @BindView(R.id.click_order_time_order_service)
    LinearLayout clickOrderTimeOrderServiceReadyBuy;
    @BindView(R.id.show_penal_sum_order_service_ready_buy_list)
    TextView showPenalSumOrderServiceReadyBuy;
    @BindView(R.id.click_penal_sum_order_service_ready_buy_list)
    LinearLayout clickPenalSumOrderServiceReadyBuy;
    @BindView(R.id.show_other_order_service_ready_buy_list)
    TextView showOtherOrderServiceReadyBuy;
    @BindView(R.id.click_other_sum_order_service_ready_buy_list)
    LinearLayout clickOtherSumOrderServiceReadyBuy;

    @BindView(R.id.tip_order_service_ready_buy_list)
    TextView tipOrderServiceReadyBuy;
    @BindView(R.id.total_price_order_service_ready_buy_list)
    TextView totalPriceOrderServiceReadyBuy;
    @BindView(R.id.list_order_service_ready_buy_list)
    ListViewOrderSellerDetails listOrderServiceReadyBuy;


    YWLoadingDialog ywLoadingDialog;

    String totalPrice;
    String number;

    List<ShoppingCart> shoppingCartOrderlist = new ArrayList<>();
    @BindView(R.id.click_no_order_service_ready_buy_list)
    TextView clickNoOrderServiceReadyBuyList;
    @BindView(R.id.click_yes_order_service_ready_buy_list)
    TextView clickYesOrderServiceReadyBuyList;
    @BindView(R.id.order_service_ready_buy_list_list)
    LinearLayout orderServiceReadyBuyListList;


    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        backTop = null;
        titleTop = null;
        listOrderServiceReadyBuy = null;
        tipOrderServiceReadyBuy = null;
        totalPriceOrderServiceReadyBuy = null;
        peopleNameOrderServiceReadyBuy = null;
        mobileNameOrderServiceReadyBuy = null;
        carNameOrderServiceReadyBuy = null;
        clickChooseCarOrderServiceReadyBuy = null;
        priceOrderServiceReadyBuy = null;
        showCouponOrderServiceReadyBuy = null;
        clickCouponOrderServiceReadyBuy = null;
        showOrderTimeOrderServiceReadyBuy = null;
        clickOrderTimeOrderServiceReadyBuy = null;
        showPenalSumOrderServiceReadyBuy = null;
        clickPenalSumOrderServiceReadyBuy = null;
        showOtherOrderServiceReadyBuy = null;
        clickOtherSumOrderServiceReadyBuy = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误");
            finish();
        }
        setContentView(R.layout.order_service_ready_buy_list);
        ButterKnife.bind(this);
        customInit(orderServiceReadyBuyListList, false, true, true);

        listOrderServiceReadyBuy.setListViewOrderSellerDetailsCallBack(new ListViewOrderSellerDetails.ListViewOrderSellerDetailsCallBack() {
            @Override
            public void onClickShopName(String shop_id) {
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityShopInfo.class)
                            .putExtra("shop_id", shop_id)
                    );
                }
            }
        });
        titleTop.setText("服务订单详情");
        getOrderDetailsSeller();
        orderTip();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedOrderModel changedOrderModel) {
        finish();
    }

    @OnClick({R.id.back_top, R.id.click_yes_order_service_ready_buy_list, R.id.click_no_order_service_ready_buy_list})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.click_yes_order_service_ready_buy_list:
                if (orderSellerModel == null) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "订单获取中，请稍后...");
                    getOrderDetailsSeller();
                    return;
                }
                if (!StringUtils.isEmpty(orderSellerModel.damages)) {
                    if (CarUtils.isDeposit(orderSellerModel.damages) == 0) {
                        operation(1);
                    } else {
                        try {

                            LogUtils.e(Double.valueOf(orderSellerModel.damages.substring(0, orderSellerModel.damages.length() - 1)));
                            String pen = PriceUtils.formatBy2Scale(Double.valueOf(orderSellerModel.damages.substring(0, orderSellerModel.damages.length() - 1)) / 100, 1);
                            LogUtils.e(pen);
                            double price = PriceUtils.multiply(Double.valueOf(pen), Double.valueOf(orderSellerModel.total), 2);
                            LogUtils.e(price);

                            PayUtils.requestPayment(ywLoadingDialog,"0", orderSellerModel.order_id, 2, 1, String.valueOf(price), getBaseActivity());
                        } catch (IndexOutOfBoundsException e) {
                            ToastUtils.showShortToast(getBaseActivityContext(), "违约金解析错误");
                        }

                    }
                } else {
                    operation(1);
                }
                break;
            case R.id.click_no_order_service_ready_buy_list:
                operation(12);
                break;
        }
    }

    /**
     * 支出和收入同意或者拒绝
     *
     * @param status 1同意，12拒绝
     */
    public void operation(int status) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.operation(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("status", status)
                            .put("order_id", getIntent().getExtras().getString("order_id"))
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
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
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    finish();
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }

    /**
     * 获取订单详情
     */
    public void getOrderDetailsSeller() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getOrderDetailsSeller_detail(
                    new JSONObject()
                            .put("order_id",getIntent().getExtras().getString("order_id"))
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    orderSellerModel = new OrderSellerModel();
                                    orderSellerModel.order_id = jsonObject.getJSONObject("data").getString("order_id");
                                    orderSellerModel.mobile = jsonObject.getJSONObject("data").getString("mobile");
                                    orderSellerModel.recipients = jsonObject.getJSONObject("data").getString("recipients");
                                    orderSellerModel.address = jsonObject.getJSONObject("data").getString("address");
                                    orderSellerModel.vehicle_name = jsonObject.getJSONObject("data").getString("vehicle_name");
                                    orderSellerModel.remark = jsonObject.getJSONObject("data").getString("remark");
                                    orderSellerModel.order_code = jsonObject.getJSONObject("data").getString("order_code");
                                    orderSellerModel.delivery_time = jsonObject.getJSONObject("data").getString("delivery_time");
                                    orderSellerModel.damages = jsonObject.getJSONObject("data").getString("damages");
                                    orderSellerModel.total = jsonObject.getJSONObject("data").getString("total");
                                    orderSellerModel.deduct_price = jsonObject.getJSONObject("data").getString("deduct_price");
                                    orderSellerModel.coupon_price = jsonObject.getJSONObject("data").getString("coupon_price");
                                    orderSellerModel.number = jsonObject.getJSONObject("data").getString("number");
                                    orderSellerModel.integral = jsonObject.getJSONObject("data").getString("integral");
                                    orderSellerModel.coupon_id = jsonObject.getJSONObject("data").getString("coupon_id");
                                    orderSellerModel.create_time = jsonObject.getJSONObject("data").getString("create_time");
                                    orderSellerModel.shop_id = jsonObject.getJSONObject("data").getString("shop_id");
                                    orderSellerModel.member_id = jsonObject.getJSONObject("data").getString("member_id");
                                    orderSellerModel.mode_payment = jsonObject.getJSONObject("data").getString("mode_payment");
                                    orderSellerModel.type = jsonObject.getJSONObject("data").getString("type");
                                    orderSellerModel.status = jsonObject.getJSONObject("data").getString("status");
                                    orderSellerModel.shop_name = jsonObject.getJSONObject("data").getString("shop_name");
                                    orderSellerModel.shop_logo = jsonObject.getJSONObject("data").getString("shop_logo");
                                    orderSellerModel.start_time = jsonObject.getJSONObject("data").getString("start_time");
                                    orderSellerModel.express = jsonObject.getJSONObject("data").getString("express");
                                    orderSellerModel.waybill = jsonObject.getJSONObject("data").getString("waybill");
                                    orderSellerModel.coupon_name = jsonObject.getJSONObject("data").getString("coupon_name");
                                    JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("goods_data");
                                    List<OrderSellerModel_Order_Goods> list = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        OrderSellerModel_Order_Goods orderSellerModel_order_goods = new OrderSellerModel_Order_Goods();
                                        orderSellerModel_order_goods.goods_id = jsonArray.getJSONObject(i).getString("goods_id");
                                        orderSellerModel_order_goods.price = jsonArray.getJSONObject(i).getString("price");
                                        orderSellerModel_order_goods.number = jsonArray.getJSONObject(i).getString("number");
                                        orderSellerModel_order_goods.goods_id = jsonArray.getJSONObject(i).getString("goods_id");
                                        orderSellerModel_order_goods.title = jsonArray.getJSONObject(i).getString("title");
                                        list.add(orderSellerModel_order_goods);
                                    }
                                    orderSellerModel.goods_data = list;
                                    showData();
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }

    OrderSellerModel orderSellerModel;

    void showData() {
        peopleNameOrderServiceReadyBuy.setText(orderSellerModel.recipients);
        peopleNameOrderServiceReadyBuy.setText(orderSellerModel.recipients);
        carNameOrderServiceReadyBuy.setText(orderSellerModel.vehicle_name);
        mobileNameOrderServiceReadyBuy.setText(orderSellerModel.mobile);
        priceOrderServiceReadyBuy.setText(orderSellerModel.total);
        showCouponOrderServiceReadyBuy.setText(orderSellerModel.coupon_name);
        showOrderTimeOrderServiceReadyBuy.setText(orderSellerModel.delivery_time);
        showPenalSumOrderServiceReadyBuy.setText(orderSellerModel.damages);
        showOtherOrderServiceReadyBuy.setText(orderSellerModel.remark);
        listOrderServiceReadyBuy.setData(getBaseActivityContext(), orderSellerModel);
        totalPriceOrderServiceReadyBuy.setText("¥"+orderSellerModel.total);
    }


    /**
     * 获取订单提示
     */
    public void orderTip() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.orderTip(
                    new JSONObject()
                            .put("article_id", "8")//文章ID (3实物类)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    tipOrderServiceReadyBuy.setText(jsonObject.getJSONObject("data").getString("contents"));
                                    tipOrderServiceReadyBuy.setVisibility(View.VISIBLE);
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }


}
