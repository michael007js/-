package com.sss.car.order;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.ZhiFuBaoPasswordStyle.PassWordKeyboard;
import com.blankj.utilcode.dao.OnAskDialogCallBack;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.LogUtils;
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
import com.sss.car.model.ExpressModel;
import com.sss.car.model.OrderSellerModel;
import com.sss.car.model.OrderSellerModel_Order_Goods;
import com.sss.car.order_new.Constant;
import com.sss.car.order_new.OrderReturnsChangeApplyForAndCompleteDataSeller;
import com.sss.car.utils.MenuDialog;
import com.sss.car.utils.OrderUtils;
import com.sss.car.view.ActivityMyDataSetPassword;
import com.sss.car.view.ActivityShopInfo;

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
 * 我的订单==>实物类发货
 * Created by leilei on 2017/9/29.
 */

@SuppressWarnings("ALL")
public class OrderGoodsOrderSendSeller extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.people_name_order_goods_order_send_seller)
    TextView peopleNameOrderGoodsOrderSendSeller;
    @BindView(R.id.mobile_name_order_goods_order_send_seller)
    TextView mobileNameOrderGoodsOrderSendSeller;
    @BindView(R.id.click_choose_car_order_goods_order_send_seller)
    LinearLayout clickChooseAddressOrderGoodsOrderSendSeller;
    @BindView(R.id.list_order_goods_order_send_seller)
    ListViewOrderSellerDetails listOrderGoodsOrderSendSeller;
    @BindView(R.id.show_coupon_order_goods_order_send_seller)
    TextView showCouponOrderGoodsOrderSendSeller;
    @BindView(R.id.click_coupon_order_goods_order_send_seller)
    LinearLayout clickCouponOrderGoodsOrderSendSeller;
    @BindView(R.id.show_order_time_order_goods_order_send_seller)
    TextView showOrderTimeOrderGoodsOrderSendSeller;
    @BindView(R.id.click_order_time_order_service)
    LinearLayout clickOrderTimeOrderService;
    @BindView(R.id.show_penal_sum_order_goods_order_send_seller)
    TextView showPenalSumOrderGoodsOrderSendSeller;
    @BindView(R.id.click_penal_sum_order_goods_order_send_seller)
    LinearLayout clickPenalSumOrderGoodsOrderSendSeller;
    @BindView(R.id.show_other_order_goods_order_send_seller)
    TextView showOtherOrderGoodsOrderSendSeller;
    @BindView(R.id.click_other_sum_order_goods_order_send_seller)
    LinearLayout clickOtherSumOrderGoodsOrderSendSeller;
    @BindView(R.id.order_goods_order_send_seller)
    LinearLayout OrderGoodsOrderSendSeller;
    YWLoadingDialog ywLoadingDialog;
    OrderSellerModel orderSellerModel;


    @BindView(R.id.price_order_goods_order_send_seller)
    TextView priceOrderGoodsOrderSendSeller;
    @BindView(R.id.click_s)
    TextView clicks;
    @BindView(R.id.content_order_goods_order_send_seller)
    TextView contentOrderGoodsOrderSendSeller;
    @BindView(R.id.location_pic)
    SimpleDraweeView locationPic;
    @BindView(R.id.address_or_car)
    TextView addressOrCar;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.company)
    TextView company;
    @BindView(R.id.expressage_code)
    EditText expressageCode;
    @BindView(R.id.parent_company)
    LinearLayout parentCompany;
    @BindView(R.id.parent_expressage_code)
    LinearLayout parentExpressageCode;

    List<ExpressModel> expressList = new ArrayList<>();
    SSS_Adapter expressAdapter;
    Gson gson = new Gson();
    MenuDialog menuDialog;
    BottomSheetDialog bottomSheetDialog;
    String express_id;
    @BindView(R.id.click_d)
    TextView clickD;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.parent_price)
    LinearLayout parentPrice;
    @BindView(R.id.line)
    TextView line;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bottomSheetDialog != null) {
            bottomSheetDialog.dismiss();
        }
        bottomSheetDialog = null;
        if (expressList != null) {
            expressList.clear();
        }
        expressList = null;
        express_id = null;
        gson = null;
        if (menuDialog != null) {
            menuDialog.clear();
        }
        menuDialog = null;
        if (expressAdapter != null) {
            expressAdapter.clear();
        }
        expressAdapter = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        backTop = null;
        titleTop = null;
        peopleNameOrderGoodsOrderSendSeller = null;
        mobileNameOrderGoodsOrderSendSeller = null;
        clickChooseAddressOrderGoodsOrderSendSeller = null;
        listOrderGoodsOrderSendSeller = null;
        showCouponOrderGoodsOrderSendSeller = null;
        clickCouponOrderGoodsOrderSendSeller = null;
        showOrderTimeOrderGoodsOrderSendSeller = null;
        clickOrderTimeOrderService = null;
        showPenalSumOrderGoodsOrderSendSeller = null;
        clickPenalSumOrderGoodsOrderSendSeller = null;
        showOtherOrderGoodsOrderSendSeller = null;
        clickOtherSumOrderGoodsOrderSendSeller = null;
        OrderGoodsOrderSendSeller = null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.order_goods_order_send_seller);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误");
            finish();
        }
        ButterKnife.bind(this);
        customInit(OrderGoodsOrderSendSeller, false, true, true);
        listOrderGoodsOrderSendSeller.setListViewOrderSellerDetailsCallBack(new ListViewOrderSellerDetails.ListViewOrderSellerDetailsCallBack() {
            @Override
            public void onClickShopName(String shop_id) {
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityShopInfo.class)
                            .putExtra("shop_id", shop_id));
                }
            }
        });
        titleTop.setText("实物订单详情");
        initView();
        getOrderDetailsSeller();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedOrderModel changedOrderModel) {
        finish();
    }

    void initView() {

        LogUtils.e(getIntent().getExtras().getInt("status") + "---" + getIntent().getExtras().getString("exchange_status")+"---is_bargain:"+getIntent().getExtras().getString("is_bargain"));
        switch (getIntent().getExtras().getInt("status")) {
            case Constant.Have_Already_Delivery_Awating_Sign_For:
//                if ("1".equals(getIntent().getExtras().getString("is_bargain"))) {
//
//                }

                bargain();
                break;
            case Constant.Have_Already_Paid_Awating_Delivery:
                send();
                break;
            case Constant.Returns:
                if ("5".equals(getIntent().getExtras().getString("exchange_status"))) {
                    delete();
                } else {
                    returns();
                }

                break;
            case Constant.Changed:
                if ("3".equals(getIntent().getExtras().getString("exchange_status")) || "6".equals(getIntent().getExtras().getString("exchange_status"))) {
                    changed();
                } else {
                    send();
                }
                break;
            case Constant.Refunded:
                delete();
                break;
            case Constant.Awating_Dispose_Returns:
                if ("2".equals(getIntent().getExtras().getString("exchange_status"))){
                    stop();
                }else {
                    deposit();
                }
                break;
        }

    }

    private void stop() {
        clickD.setText("已拒绝");
        clickD.setVisibility(View.VISIBLE);
        clicks.setVisibility(View.GONE);
        parentPrice.setVisibility(View.VISIBLE);
        line.setVisibility(View.VISIBLE);

    }
    private void bargain() {
        clickD.setText("拒绝");
        clicks.setText("同意");
        clickD.setVisibility(View.VISIBLE);
        clicks.setVisibility(View.VISIBLE);
        parentPrice.setVisibility(View.VISIBLE);
        line.setVisibility(View.VISIBLE);
        clickD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderUtils.status_bargain(getBaseActivity(), ywLoadingDialog, true, getIntent().getExtras().getString("order_id"), "2");
            }
        });
        clicks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderUtils.status_bargain(getBaseActivity(), ywLoadingDialog, true, getIntent().getExtras().getString("order_id"), "2");
            }
        });
    }

    private void send() {
        clicks.setText("立即发货");
        expressageCode.setEnabled(true);
        parentExpressageCode.setVisibility(View.VISIBLE);
        parentCompany.setVisibility(View.VISIBLE);
        expressageCode.setHint("请填写快递单号");
        initExpressAdapter();
        express_company();
        clicks.setVisibility(View.VISIBLE);
        clicks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(express_id)) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请选择快递公司");
                    return;
                }
                if (StringUtils.isEmpty(expressageCode.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请输入快递单号");
                    return;
                }
                OrderUtils.deliver(getBaseActivity(), ywLoadingDialog, true, getIntent().getExtras().getString("order_id"), express_id, expressageCode.getText().toString().trim());
            }
        });
        company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expressList.size() == 0) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "快递公司信息获取中...");
                    express_company();
                    return;
                }
                if (menuDialog == null) {
                    menuDialog = new MenuDialog(getBaseActivity());
                }
                if (bottomSheetDialog != null) {
                    bottomSheetDialog.dismiss();
                }
                bottomSheetDialog = null;
                bottomSheetDialog = menuDialog.createExpressBottomDialog(getBaseActivityContext(), expressAdapter);
                expressAdapter.setList(expressList);
            }
        });
    }

    private void returns() {
        clicks.setText("确认收货");
        clicks.setVisibility(View.VISIBLE);
        parentExpressageCode.setVisibility(View.VISIBLE);
        parentCompany.setVisibility(View.VISIBLE);
        clicks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (orderSellerModel == null) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "订单刷新中...");
                    return;
                }
                APPOftenUtils.createAskDialog(getBaseActivityContext(), "确认要收货吗？", new OnAskDialogCallBack() {
                    @Override
                    public void onOKey(Dialog dialog) {
                        dialog.dismiss();
                        dialog = null;
                        P.e(ywLoadingDialog, Config.member_id, getBaseActivity(), new P.p() {
                            @Override
                            public void exist() {
                                if (menuDialog == null) {
                                    menuDialog = new MenuDialog(getBaseActivity());
                                }
                                menuDialog.createPasswordInputDialog("请输入您的支付密码", getBaseActivity(), new OnPayPasswordVerificationCallBack() {
                                    @Override
                                    public void onVerificationPassword(String password, final PassWordKeyboard passWordKeyboard, final BottomSheetDialog bottomSheetDialog) {
                                        P.r(ywLoadingDialog, Config.member_id, password, getBaseActivity(), new P.r() {
                                            @Override
                                            public void match() {
                                                bottomSheetDialog.dismiss();
                                                passWordKeyboard.setStatus(true);
                                                OrderUtils.confirm_goods(getBaseActivity(), ywLoadingDialog, true, getIntent().getExtras().getString("order_id"), orderSellerModel.exchange_id);
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
                                if (getBaseActivityContext() != null) {
                                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataSetPassword.class));
                                }
                            }
                        });


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

    private void changed() {
        clicks.setText("确认收货");
        clicks.setVisibility(View.VISIBLE);
        parentExpressageCode.setVisibility(View.VISIBLE);
        parentCompany.setVisibility(View.VISIBLE);
        clicks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderSellerModel == null) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "订单刷新中...");
                    return;
                }
                APPOftenUtils.createAskDialog(getBaseActivityContext(), "确认要收货吗？", new OnAskDialogCallBack() {
                    @Override
                    public void onOKey(Dialog dialog) {
                        dialog.dismiss();
                        dialog = null;

                        P.e(ywLoadingDialog, Config.member_id, getBaseActivity(), new P.p() {
                            @Override
                            public void exist() {
                                if (menuDialog == null) {
                                    menuDialog = new MenuDialog(getBaseActivity());
                                }
                                menuDialog.createPasswordInputDialog("请输入您的支付密码", getBaseActivity(), new OnPayPasswordVerificationCallBack() {
                                    @Override
                                    public void onVerificationPassword(String password, final PassWordKeyboard passWordKeyboard, final BottomSheetDialog bottomSheetDialog) {
                                        P.r(ywLoadingDialog, Config.member_id, password, getBaseActivity(), new P.r() {
                                            @Override
                                            public void match() {
                                                bottomSheetDialog.dismiss();
                                                passWordKeyboard.setStatus(true);
                                                OrderUtils.exchange_goods(getBaseActivity(), ywLoadingDialog, true, getIntent().getExtras().getString("order_id"), orderSellerModel.exchange_id);
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
                                if (getBaseActivityContext() != null) {
                                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataSetPassword.class));
                                }
                            }
                        });

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

    private void delete() {
        clicks.setText("删除订单");
        clicks.setVisibility(View.VISIBLE);
        clicks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderSellerModel == null) {
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
    }

    private void deposit() {
        clicks.setText("立即处理");
        clicks.setVisibility(View.VISIBLE);
        clicks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderSellerModel == null) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "订单刷新中...");
                    return;
                }
                if ("1".equals(orderSellerModel.type)) {
                    if (getBaseActivityContext() != null) {
                        startActivity(new Intent(getBaseActivityContext(), OrderReturnsChangeApplyForAndCompleteDataSeller.class)
                                .putExtra("order_id", orderSellerModel.order_id));
                    }
                }

            }
        });
    }

    void initExpressAdapter() {
        expressAdapter = new SSS_Adapter<ExpressModel>(getBaseActivityContext(), R.layout.item_express_text) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, final ExpressModel bean, SSS_Adapter instance) {
                helper.setText(R.id.text_express, bean.name);
                helper.getView(R.id.text_express).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        express_id = bean.express_id;
                        company.setText(bean.name);
                        if (bottomSheetDialog != null) {
                            bottomSheetDialog.dismiss();
                        }
                        bottomSheetDialog = null;
                    }
                });
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };

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
                                    orderSellerModel.bargain = jsonObject.getJSONObject("data").getString("bargain");
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
                                    orderSellerModel.exchange_id = jsonObject.getJSONObject("data").getString("exchange_id");
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


    void showData() {
        price.setText(orderSellerModel.bargain);
        express_id = orderSellerModel.express_id;
        peopleNameOrderGoodsOrderSendSeller.setText(orderSellerModel.recipients);
        contentOrderGoodsOrderSendSeller.setText(orderSellerModel.address);
        mobileNameOrderGoodsOrderSendSeller.setText(orderSellerModel.mobile);
        priceOrderGoodsOrderSendSeller.setText(orderSellerModel.total);
        showCouponOrderGoodsOrderSendSeller.setText(orderSellerModel.coupon_name);
        showOrderTimeOrderGoodsOrderSendSeller.setText(orderSellerModel.delivery_time);
        showPenalSumOrderGoodsOrderSendSeller.setText(orderSellerModel.damages);
        showOtherOrderGoodsOrderSendSeller.setText(orderSellerModel.remark);
        listOrderGoodsOrderSendSeller.setData(getBaseActivityContext(), orderSellerModel);
        expressageCode.setText(orderSellerModel.waybill);
        if (getIntent().getExtras().getInt("status") == Constant.Have_Already_Paid_Awating_Delivery) {
            company.setHint("请填写快递公司");
        } else {
            company.setText(orderSellerModel.express);
        }
    }

    /**
     * 发货时从服务器获取支持的快递公司
     */
    public void express_company() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.express_company(
                    new JSONObject()
                            .put("member_id", Config.member_id)
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
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        expressList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), ExpressModel.class));
                                    }
                                    expressAdapter.setList(expressList);
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }

}
