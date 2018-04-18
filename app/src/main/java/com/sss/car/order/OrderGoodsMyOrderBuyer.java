package com.sss.car.order;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.JingDongCountDownView.SecondDownTimerView;
import com.blankj.utilcode.customwidget.JingDongCountDownView.base.OnCountDownTimerListener;
import com.blankj.utilcode.customwidget.ZhiFuBaoPasswordStyle.PassWordKeyboard;
import com.blankj.utilcode.dao.OnAskDialogCallBack;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PriceUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.rey.material.app.BottomSheetDialog;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedOrderModel;
import com.sss.car.P;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.ListViewOrderSellerDetails;
import com.sss.car.dao.OnPayPasswordVerificationCallBack;
import com.sss.car.model.OrderSellerModel;
import com.sss.car.model.OrderSellerModel_Order_Goods;
import com.sss.car.order_new.Constant;
import com.sss.car.order_new.OrderCommentBuyer;
import com.sss.car.order_new.OrderCommentSeller;
import com.sss.car.order_new.OrderModel;
import com.sss.car.utils.MenuDialog;
import com.sss.car.utils.OrderUtils;
import com.sss.car.utils.PayUtils;
import com.sss.car.view.ActivityMyDataSetPassword;
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
 * 我的订单==>支出 实物
 * Created by leilei on 2017/9/29.
 */

@SuppressWarnings("ALL")
public class OrderGoodsMyOrderBuyer extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.people_name_order_goods_my_order_buyer)
    TextView peopleNameOrderGoodsMyOrderBuyer;
    @BindView(R.id.mobile_name_order_goods_my_order_buyer)
    TextView mobileNameOrderGoodsMyOrderBuyer;
    @BindView(R.id.click_choose_car_order_goods_my_order_buyer)
    LinearLayout clickChooseAddressOrderGoodsMyOrderBuyer;
    @BindView(R.id.list_order_goods_my_order_buyer)
    ListViewOrderSellerDetails listOrderGoodsMyOrderBuyer;
    @BindView(R.id.show_coupon_order_goods_my_order_buyer)
    TextView showCouponOrderGoodsMyOrderBuyer;
    @BindView(R.id.click_coupon_order_goods_my_order_buyer)
    LinearLayout clickCouponOrderGoodsMyOrderBuyer;
    @BindView(R.id.show_order_time_order_goods_my_order_buyer)
    TextView showOrderTimeOrderGoodsMyOrderBuyer;
    @BindView(R.id.click_order_time_order_service)
    LinearLayout clickOrderTimeOrderService;
    @BindView(R.id.show_penal_sum_order_goods_my_order_buyer)
    TextView showPenalSumOrderGoodsMyOrderBuyer;
    @BindView(R.id.click_penal_sum_order_goods_my_order_buyer)
    LinearLayout clickPenalSumOrderGoodsMyOrderBuyer;
    @BindView(R.id.show_other_order_goods_my_order_buyer)
    TextView showOtherOrderGoodsMyOrderBuyer;
    @BindView(R.id.click_other_sum_order_goods_my_order_buyer)
    LinearLayout clickOtherSumOrderGoodsMyOrderBuyer;
    @BindView(R.id.total_price_order_goods_my_order_buyer)
    TextView totalPriceOrderGoodsMyOrderBuyer;
    @BindView(R.id.click_submit_order_goods_my_order_buyer)
    TextView clickSubmitOrderGoodsMyOrderBuyer;
    @BindView(R.id.order_goods_my_order_buyer)
    LinearLayout OrderGoodsMyOrderBuyer;
    YWLoadingDialog ywLoadingDialog;
    OrderSellerModel orderSellerModel;


    @BindView(R.id.price_order_goods_my_order_buyer)
    TextView priceOrderGoodsMyOrderBuyer;
    @BindView(R.id.click_s)
    TextView clicks;
    @BindView(R.id.parent)
    LinearLayout parent;
    @BindView(R.id.content_order_goods_my_order_buyer)
    TextView contentOrderGoodsMyOrderBuyer;
    @BindView(R.id.location_pic)
    SimpleDraweeView locationPic;
    @BindView(R.id.address_or_car)
    TextView addressOrCar;
    OrderModel orderModel;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.company)
    EditText company;
    @BindView(R.id.expressage_code)
    EditText expressageCode;
    MenuDialog menuDialog;
    @BindView(R.id.countdown)
    SecondDownTimerView countdown;
    @BindView(R.id.parent_expressage_code)
    LinearLayout parentExpressageCode;
    @BindView(R.id.company_parent)
    LinearLayout companyParent;
    @BindView(R.id.parent_countdown)
    LinearLayout parentCountdown;

    double price=0.00;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countdown != null) {
            countdown.cancelDownTimer();
        }
        countdown = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (menuDialog != null) {
            menuDialog.clear();
        }
        menuDialog = null;
        backTop = null;
        titleTop = null;
        peopleNameOrderGoodsMyOrderBuyer = null;
        mobileNameOrderGoodsMyOrderBuyer = null;
        clickChooseAddressOrderGoodsMyOrderBuyer = null;
        listOrderGoodsMyOrderBuyer = null;
        showCouponOrderGoodsMyOrderBuyer = null;
        clickCouponOrderGoodsMyOrderBuyer = null;
        showOrderTimeOrderGoodsMyOrderBuyer = null;
        clickOrderTimeOrderService = null;
        showPenalSumOrderGoodsMyOrderBuyer = null;
        clickPenalSumOrderGoodsMyOrderBuyer = null;
        showOtherOrderGoodsMyOrderBuyer = null;
        clickOtherSumOrderGoodsMyOrderBuyer = null;
        totalPriceOrderGoodsMyOrderBuyer = null;
        clickSubmitOrderGoodsMyOrderBuyer = null;
        OrderGoodsMyOrderBuyer = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_goods_my_order_buyer);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误");
            finish();
        }
        ButterKnife.bind(this);
        customInit(OrderGoodsMyOrderBuyer, false, true, true);
        listOrderGoodsMyOrderBuyer.setListViewOrderSellerDetailsCallBack(new ListViewOrderSellerDetails.ListViewOrderSellerDetailsCallBack() {
            @Override
            public void onClickShopName(String shop_id) {
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityShopInfo.class)
                            .putExtra("shop_id", shop_id));
                }
            }
        });
        titleTop.setText("实物订单详情");
        countdown.setDownTimerListener(new OnCountDownTimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                ToastUtils.showShortToast(getBaseActivityContext(), "该订单已经过期");
                EventBus.getDefault().post(new ChangedOrderModel());
                finish();
            }
        });

        getOrderDetailsSeller();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedOrderModel changedOrderModel) {
        finish();
    }


    void init() {
        LogUtils.e(getIntent().getExtras().getInt("status"));
        switch (getIntent().getExtras().getInt("status")) {
            case Constant.Ready_Buy:
                clicks.setText("取消订单");
                clicks.setVisibility(View.VISIBLE);
                parent.setVisibility(View.GONE);
                clicks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        APPOftenUtils.createAskDialog(getBaseActivityContext(), "是否确定要取消订单?", new OnAskDialogCallBack() {
                            @Override
                            public void onOKey(Dialog dialog) {
                                dialog.dismiss();
                                dialog = null;
                                OrderUtils.cancelOrder(getBaseActivity(), ywLoadingDialog, true, getIntent().getExtras().getString("order_id"));
                            }

                            @Override
                            public void onCancel(Dialog dialog) {
                                dialog.dismiss();
                                dialog = null;
                            }
                        });
                    }
                });
                break;
            case Constant.Non_Payment:
                clicks.setText("立即付款");
                clicks.setVisibility(View.VISIBLE);
                parent.setVisibility(View.GONE);
                clicks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (orderSellerModel == null && orderModel == null) {
                            ToastUtils.showShortToast(getBaseActivityContext(), "订单获取中，请稍候...");
                            return;
                        }
                        APPOftenUtils.createAskDialog(getBaseActivityContext(), "是否确定要立即付款?", new OnAskDialogCallBack() {
                            @Override
                            public void onOKey(Dialog dialog) {
                                dialog.dismiss();
                                dialog = null;
                                PayUtils.requestPayment(ywLoadingDialog,false, "0", orderModel.order_id, 2, 0, PriceUtils.formatBy2Scale(Double.valueOf(orderModel.total), 2), getBaseActivity(),null);
//                                if (ywLoadingDialog != null) {
//                                    ywLoadingDialog.dismiss();
//                                }
//                                ywLoadingDialog = null;
//                                ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
//                                ywLoadingDialog.show();
//                                try {
//                                    String a = orderSellerModel.total;
//                                    double b = Double.valueOf(a);
//                                    new PayMentUtils().requestPayment(ywLoadingDialog, "立即付款", (int) b, getBaseActivity(), new PayMentUtils.OnPaymentCallBack() {
//                                        @Override
//                                        public void onMatch(String payMode, String is_integral, int price) {
//                                            JSONArray jsonArray = new JSONArray();
//                                            jsonArray.put(getIntent().getExtras().getString("order_id"));
//                                            OrderUtils.payment_order(getBaseActivity(), ywLoadingDialog, is_integral, "balance", true, getIntent().getExtras().getString("order_id"));
//                                        }
//
//                                        @Override
//                                        public void onZhiFuBao(String payMode, String is_integral, int price) {
//                                            JSONArray jsonArray = new JSONArray();
//                                            jsonArray.put(getIntent().getExtras().getString("order_id"));
//                                            OrderUtils.payment_order(getBaseActivity(), ywLoadingDialog, is_integral, "ali_pay", true, getIntent().getExtras().getString("order_id"));
//                                        }
//
//                                        @Override
//                                        public void onWeiXin(String payMode, String is_integral, int price) {
//                                            JSONArray jsonArray = new JSONArray();
//                                            jsonArray.put(getIntent().getExtras().getString("order_id"));
//                                            OrderUtils.payment_order(getBaseActivity(), ywLoadingDialog, is_integral, "we_chat_pay", true, getIntent().getExtras().getString("order_id"));
//                                        }
//
//                                        @Override
//                                        public void onMismatches() {
//                                            ToastUtils.showShortToast(getBaseActivityContext(), "密码不正确");
//                                        }
//
//                                        @Override
//                                        public void onErrorMsg(String msg) {
//                                            ToastUtils.showShortToast(getBaseActivityContext(), msg);
//
//                                        }
//                                    });
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                    ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误err-0");
//                                }

                            }

                            @Override
                            public void onCancel(Dialog dialog) {
                                dialog.dismiss();
                                dialog = null;
                            }
                        });
                    }
                });
                break;
            case Constant.Awating_Comment:
                clicks.setVisibility(View.VISIBLE);
                parent.setVisibility(View.GONE);
                clicks.setText("去评价");
                clicks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getIntent().getExtras().getBoolean("isIncome")) {
                            startActivity(new Intent(getBaseActivityContext(), OrderCommentSeller.class)
                                    .putExtra("order_id", getIntent().getExtras().getString("order_id")));
                        } else {
                            startActivity(new Intent(getBaseActivityContext(), OrderCommentBuyer.class)
                                    .putExtra("order_id", getIntent().getExtras().getString("order_id")));
                        }

                    }
                });

                break;
            case -100:
                clicks.setVisibility(View.VISIBLE);
                parent.setVisibility(View.GONE);
                clicks.setText("删除订单");
                clicks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        APPOftenUtils.createAskDialog(getBaseActivityContext(), "是否确认删除订单?", new OnAskDialogCallBack() {
                            @Override
                            public void onOKey(Dialog dialog) {
                                dialog.dismiss();
                                dialog = null;
                                OrderUtils.del_expend(getBaseActivity(), ywLoadingDialog, true, getIntent().getExtras().getString("order_id"));
                            }

                            @Override
                            public void onCancel(Dialog dialog) {
                                dialog.dismiss();
                                dialog = null;
                            }
                        });
                    }
                });
                break;
            case Constant.Have_Already_Delivery_Awating_Sign_For:
                clicks.setVisibility(View.VISIBLE);
                parent.setVisibility(View.GONE);
                clicks.setText("确认收货");
                clicks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clicks.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                APPOftenUtils.createAskDialog(getBaseActivityContext(), "是否确定要收货?", new OnAskDialogCallBack() {
                                    @Override
                                    public void onOKey(Dialog dialog) {
                                        dialog.dismiss();
                                        dialog = null;
//                                        P.e(ywLoadingDialog, Config.member_id, getBaseActivity(), new P.p() {
//                                            @Override
//                                            public void exist() {
//                                                if (menuDialog == null) {
//                                                    menuDialog = new MenuDialog(getBaseActivity());
//                                                }
//                                                menuDialog.createPasswordInputDialog("请输入您的支付密码", getBaseActivity(), new OnPayPasswordVerificationCallBack() {
//                                                    @Override
//                                                    public void onVerificationPassword(String password, final PassWordKeyboard passWordKeyboard, final BottomSheetDialog bottomSheetDialog) {
//                                                        P.r(ywLoadingDialog, Config.member_id, password, getBaseActivity(), new P.r() {
//                                                            @Override
//                                                            public void match() {
//                                                                bottomSheetDialog.dismiss();
//                                                                passWordKeyboard.setStatus(true);
//                                                                if (ywLoadingDialog==null){
//                                                                    ywLoadingDialog=new YWLoadingDialog(getBaseActivityContext());
//                                                                }
//                                                                OrderUtils.start(1,menuDialog,getBaseActivity(), ywLoadingDialog, true, getIntent().getExtras().getString("order_id"),price);
//                                                            }
//
//                                                            @Override
//                                                            public void mismatches() {
//
//                                                                passWordKeyboard.setStatus(false);
//                                                            }
//                                                        });
//                                                    }
//
//                                                });
//                                            }
//
//
//                                            @Override
//                                            public void nonexistence() {
//                                                if (getBaseActivityContext() != null) {
//                                                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataSetPassword.class));
//                                                }
//                                            }
//                                        });
                                        OrderUtils.start(1,menuDialog,getBaseActivity(), ywLoadingDialog, true, getIntent().getExtras().getString("order_id"),price);

                                    }

                                    @Override
                                    public void onCancel(Dialog dialog) {
                                        dialog.dismiss();
                                        dialog = null;
                                    }
                                });

                            }
                        });

                    }
                });
                break;
            case Constant.Awating_Dispose_Returns:
                if ("2".equals(getIntent().getExtras().getString("exchange_status"))){
                    clicks.setVisibility(View.VISIBLE);
                    parent.setVisibility(View.GONE);
                    clicks.setText("查看退换货资料");
                    clicks.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (getBaseActivityContext() != null) {
                                startActivity(new Intent(getBaseActivityContext(), OrderReturns.class)
                                        .putExtra("what", getIntent().getExtras().getInt("what"))
                                        .putExtra("order_id", getIntent().getExtras().getString("order_id"))
                                        .putExtra("isStop",true)
                                        .putExtra("returnOrChange_isFirst", false)//第一次打开,申请退换货;如果不是第一次打开,则填写快递信息
                                );
                            }
                        }
                    });
                }else {
                    clicks.setVisibility(View.VISIBLE);
                    parent.setVisibility(View.GONE);
                    clicks.setText("完善资料");
                    clicks.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (getBaseActivityContext() != null) {
                                startActivity(new Intent(getBaseActivityContext(), OrderReturns.class)
                                        .putExtra("what", getIntent().getExtras().getInt("what"))
                                        .putExtra("order_id", getIntent().getExtras().getString("order_id"))
                                        .putExtra("returnOrChange_isFirst", false)//第一次打开,申请退换货;如果不是第一次打开,则填写快递信息
                                );
                            }
                        }
                    });
                }


                break;
            case Constant.Refunded:
                clicks.setText("删除订单");
                clicks.setVisibility(View.VISIBLE);
                parent.setVisibility(View.GONE);
                clicks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (orderSellerModel == null && orderModel == null) {
                            ToastUtils.showShortToast(getBaseActivityContext(), "订单刷新中...");
                            return;
                        }
                        APPOftenUtils.createAskDialog(getBaseActivityContext(), "确认要删除订单吗？", new OnAskDialogCallBack() {
                            @Override
                            public void onOKey(Dialog dialog) {
                                dialog.dismiss();
                                dialog = null;
                                OrderUtils.del_expend(getBaseActivity(), ywLoadingDialog, true, getIntent().getExtras().getString("order_id"));

                            }

                            @Override
                            public void onCancel(Dialog dialog) {
                                dialog.dismiss();
                                dialog = null;
                            }
                        });

                    }
                });
                break;
        }
    }


    @OnClick({R.id.back_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.back_top:
                finish();
                break;

        }
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
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:p-0");
                                e.printStackTrace();
                            }
                        }
                    }));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:p-0");
            e.printStackTrace();
        }
    }

    public void getInfo() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();

        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.new_expend_single(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("order_id", getIntent().getExtras().getString("order_id"))
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
                                    orderModel = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), OrderModel.class);
                                    init();
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
                                    orderSellerModel = new OrderSellerModel();
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
                                    price= jsonObject.getJSONObject("data").getDouble("total");
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
                                        orderSellerModel_order_goods.total = jsonArray.getJSONObject(i).getString("total");
                                        orderSellerModel_order_goods.price = jsonArray.getJSONObject(i).getString("price");
                                        orderSellerModel_order_goods.number = jsonArray.getJSONObject(i).getString("number");
                                        orderSellerModel_order_goods.goods_id = jsonArray.getJSONObject(i).getString("goods_id");
                                        orderSellerModel_order_goods.title = jsonArray.getJSONObject(i).getString("title");
                                        list.add(orderSellerModel_order_goods);
                                    }
                                    orderSellerModel.goods_data = list;
                                    int start_time = jsonObject.getJSONObject("data").getInt("start_time");
                                    if (start_time > 0) {
                                        countdown.setDownTime(Long.valueOf(start_time*1000));
                                        countdown.startDownTimer();
                                        parentCountdown.setVisibility(View.VISIBLE);
                                    }
                                    getInfo();


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


    void showData() {
        if (orderSellerModel != null) {
            peopleNameOrderGoodsMyOrderBuyer.setText(orderSellerModel.recipients);


            mobileNameOrderGoodsMyOrderBuyer.setText(orderSellerModel.mobile);
            totalPriceOrderGoodsMyOrderBuyer.setText("¥" + orderSellerModel.total);
            if (!StringUtils.isEmpty(orderSellerModel.total)) {
                totalPriceOrderGoodsMyOrderBuyer.setText(((int) Double.parseDouble(orderSellerModel.total)) + "");
                priceOrderGoodsMyOrderBuyer.setText(orderSellerModel.total);

            }
            contentOrderGoodsMyOrderBuyer.setText(orderSellerModel.address);

            showOrderTimeOrderGoodsMyOrderBuyer.setText(orderSellerModel.delivery_time);
            showPenalSumOrderGoodsMyOrderBuyer.setText(orderSellerModel.damages);
            showOtherOrderGoodsMyOrderBuyer.setText(orderSellerModel.remark);
            showCouponOrderGoodsMyOrderBuyer.setText(orderSellerModel.coupon_name);
            listOrderGoodsMyOrderBuyer.setData(getBaseActivityContext(), orderSellerModel);

            showCompany();
        }
    }

    void showCompany() {
        LogUtils.e(getIntent().getExtras().getInt("status"));
        if (Constant.Awating_Comment == getIntent().getExtras().getInt("status") || Constant.Have_Already_Delivery_Awating_Sign_For == getIntent().getExtras().getInt("status")) {
            companyParent.setVisibility(View.VISIBLE);
            company.setText(orderSellerModel.express);
            expressageCode.setText(orderSellerModel.waybill);
        }
    }


}
