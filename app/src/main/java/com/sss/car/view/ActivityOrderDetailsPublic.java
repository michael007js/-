package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.ListView.HorizontalListView;
import com.blankj.utilcode.customwidget.ZhiFuBaoPasswordStyle.PassWordKeyboard;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangeInfoModel;
import com.sss.car.EventBusModel.OrderCommentListChanged;
import com.sss.car.P;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.ListViewOrder;
import com.sss.car.custom.ListViewOrderSellerDetails;
import com.sss.car.dao.OnPayPasswordVerificationCallBack;
import com.sss.car.model.OrderSellerModel;
import com.sss.car.model.OrderSellerModel_Order_Goods;
import com.sss.car.utils.MenuDialog;

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

import static com.sss.car.R.id.company;
import static com.sss.car.R.id.reason;
import static com.sss.car.R.id.result;

/**
 * 订单详情公用界面
 * Created by leilei on 2017/10/15.
 */

public class ActivityOrderDetailsPublic extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.people_name_activity_order_details_public)
    TextView peopleNameActivityOrderDetailsPublic;
    @BindView(R.id.mobile_name_activity_order_details_public)
    TextView mobileNameActivityOrderDetailsPublic;
    @BindView(R.id.show_car_or_name_activity_order_details_public)
    TextView showCarOrNameActivityOrderDetailsPublic;
    @BindView(R.id.show_address_activity_order_details_public)
    TextView showAddressActivityOrderDetailsPublic;
    @BindView(R.id.price_activity_order_details_public)
    TextView priceActivityOrderDetailsPublic;
    @BindView(R.id.show_time_activity_order_details_public)
    TextView showTimeActivityOrderDetailsPublic;
    @BindView(R.id.show_penal_sum_activity_order_details_public)
    TextView showPenalSumActivityOrderDetailsPublic;
    @BindView(R.id.show_other_activity_order_details_public)
    TextView showOtherActivityOrderDetailsPublic;
    @BindView(R.id.photo)
    HorizontalListView photo;

    @BindView(R.id.activity_order_details_public)
    LinearLayout ActivityOrderDetailsPublic;
    @BindView(R.id.ListViewOrderSellerDetails_activity_order_details_public)
    ListViewOrderSellerDetails ListViewOrderSellerDetailsActivityOrderDetailsPublic;
    /**
     * @param status         0---支出:预购+收入:无，
     * 1---支出:待付款+收入:无，
     * 2---支出:待服务+收入:已付款(待发货)，
     * 3---支出:待收货+收入:已发货，
     * 4---支出:待评价+收入:待评价，
     * 5---支出:已完成+收入:已完成，
     * 6---支出:待处理（已申请退货）+收入:待处理（已申请退货），
     * 7---支出:退货+收入:退货，
     * 8---支出:换货+收入:换货，
     * 9---支出:已退款+收入:已退款,
     * 10---支出:待服务+收入:待服务
     * @param mode           TYPE_WAITING_FOR_DISPOSE_RETURN_CHANGE_FROM_BUY = 0X0001;//买家版待处理(退换货状态下--支出)
     * TYPE_WAITING_FOR_DISPOSE_RETURN_CHANGE_FROM_public = 0X0002;//卖家版待处理(退换货状态下--收入)
     * TYPE_WAITING_FOR_SEND = 0X0003;//待发货(收入)
     * TYPE_WAITING_FOR_SERVICE_FROM_BUY = 0X0004;//买家版待服务(支出)
     * TYPE_WAITING_FOR_SERVICE_FROMSELLER = 0X0005;//卖家版待服务(收入)
     * TYPE_WAITING_FOR_COMMENT_FROM_BUY = 0X0006;//买家版待评价(支出)
     * TYPE_WAITING_FOR_COMMENT_FROMSELLER = 0X0007;//卖家版待评价(收入)
     * TYPE_WAITING_FOR_READY_BUY = 0X0008;//待预购(支出)
     * TYPE_WAITING_FOR_PAYMENT = 0X0009;//待付款(支出)
     * TYPE_WAITING_FOR_RECEIVING = 0X0010;//待收货(支出)
     * TYPE_WAITING_FOR_RETURNS_FROM_BUY = 0X0011;//买家版待退货(支出)
     * TYPE_WAITING_FOR_RETURNS_FROMSELLER = 0X0012;//卖家版待退货(收入)
     * TYPE_WAITING_FOR_CHANGE_FROM_BUY = 0X0013;//买家版待换换货(支出)
     * TYPE_WAITING_FOR_CHANGE_FROMSELLER = 0X0014;//卖家版待换换货(收入)
     * TYPE_WAITING_FOR_DISPOSE_NORMAL_FROM_BUY = 0X00015;//买家版待处理(正常状态下--支出)
     * TYPE_WAITING_FOR_COMPLETE_RETURN_CHANGE_FROM_BUY = 0X00017;//买家版已完成(退换货状态下--支出)
     * TYPE_WAITING_FOR_COMPLETE_RETURN_CHANGE_FROM_public = 0X0018;//卖家版已完成(退换货状态下--收入)
     * TYPE_WAITING_FOR_COMPLETE_NORMAL_FROM_BUY = 0X00019;//买家版已完成(正常状态下--支出)
     * TYPE_WAITING_FOR_COMPLETE_NORMAL_FROM_public = 0X00020;//卖家版已完成(正常状态下--收入)
     * @param what           FragmentOrder.INCOME//收入
     * FragmentOrder.EXPEND//支出
     * @param isShowCheckBox 实否要显示CheckBox
     * @param isShowQR       实否要显示二维码
     */
    int mode;


    YWLoadingDialog ywLoadingDialog;

    OrderSellerModel orderSellerModel;

    MenuDialog menuDialog;


    @BindView(R.id.adress_or_car_name_activity_order_details_public)
    TextView adressOrCarNameActivityOrderDetailsPublic;
    @BindView(R.id.location_name_activity_order_details_public)
    ImageView locationNameActivityOrderDetailsPublic;
    @BindView(R.id.show_company_activity_order_details_public)
    TextView showCompanyActivityOrderDetailsPublic;
    @BindView(R.id.show_code_activity_order_details_public)
    TextView showCodeActivityOrderDetailsPublic;


    @BindView(R.id.click_cancel_activity_order_details_public)
    TextView clickCancelActivityOrderDetailsPublic;
    @BindView(R.id.click_send_activity_order_details_public)
    TextView clickSendActivityOrderDetailsPublic;
    @BindView(R.id.click_comment_activity_order_details_public)
    TextView clickCommentActivityOrderDetailsPublic;
    @BindView(R.id.click_company_activity_order_details_public)
    LinearLayout clickCompanyActivityOrderDetailsPublic;
    @BindView(R.id.click_code_activity_order_details_public)
    LinearLayout clickCodeActivityOrderDetailsPublic;


    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (menuDialog != null) {
            menuDialog.clear();
        }
        menuDialog = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        orderSellerModel = null;
        backTop = null;
        titleTop = null;
        rightButtonTop = null;
        clickCommentActivityOrderDetailsPublic = null;
        peopleNameActivityOrderDetailsPublic = null;
        mobileNameActivityOrderDetailsPublic = null;
        showCarOrNameActivityOrderDetailsPublic = null;
        showAddressActivityOrderDetailsPublic = null;
        priceActivityOrderDetailsPublic = null;
        showTimeActivityOrderDetailsPublic = null;
        showPenalSumActivityOrderDetailsPublic = null;
        ListViewOrderSellerDetailsActivityOrderDetailsPublic = null;
        showOtherActivityOrderDetailsPublic = null;
        photo = null;
        clickCancelActivityOrderDetailsPublic = null;
        ActivityOrderDetailsPublic = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_public);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传输错误");
            finish();
        }
        mode = getIntent().getExtras().getInt("mode");
        ButterKnife.bind(this);
        customInit(ActivityOrderDetailsPublic,false,true,true);


        init();
        ListViewOrderSellerDetailsActivityOrderDetailsPublic.setListViewOrderSellerDetailsCallBack(new ListViewOrderSellerDetails.ListViewOrderSellerDetailsCallBack() {

            @Override
            public void onClickShopName(String shop_id) {
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityShopInfo.class)
                            .putExtra("shop_id", shop_id));
                }

            }
        });
        getOrderDetailsSeller();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeInfoModel event) {
        switch (event.type) {
            case "expressageCompany":
                showCompanyActivityOrderDetailsPublic.setText(event.msg);
                break;
            case "expressageCode":
                showCodeActivityOrderDetailsPublic.setText(event.msg);
                break;
        }
    }

    void init() {
        LogUtils.e(mode);
        switch (mode) {
            case ListViewOrder.TYPE_WAITING_FOR_COMPLETE_NORMAL_FROM_SELLER://卖家版已完成(正常状态下--收入)129
                break;
            case ListViewOrder.TYPE_WAITING_FOR_COMPLETE_NORMAL_FROM_BUY://买家版已完成(正常状态下--支出)128
                break;
            case ListViewOrder.TYPE_WAITING_FOR_COMPLETE_RETURN_CHANGE_FROM_SELLER://卖家版已完成(退换货状态下--收入)127
                break;
            case ListViewOrder.TYPE_WAITING_FOR_COMPLETE_RETURN_CHANGE_FROM_BUY://买家版已完成(退换货状态下--支出)126
                break;
            case ListViewOrder.TYPE_WAITING_FOR_DISPOSE_RETURN_CHANGE_FROM_BUY://买家版待处理(退换货状态下--支出)111
                break;
            case ListViewOrder.TYPE_WAITING_FOR_DISPOSE_NORMAL_FROM_BUY://买家版待处理(正常状态下--支出)125
                break;
            case ListViewOrder.TYPE_WAITING_FOR_DISPOSE_RETURN_CHANGE_FROM_SELLER://卖家版待处理(退换货状态下--收入)112
                break;
            case ListViewOrder.TYPE_WAITING_FOR_SEND://待发货(卖家版)113
                titleTop.setText("立即发货");
                clickSendActivityOrderDetailsPublic.setVisibility(View.VISIBLE);
                clickSendActivityOrderDetailsPublic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        send();
                    }
                });
                clickCompanyActivityOrderDetailsPublic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getBaseActivityContext() != null) {
                            startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                                    .putExtra("canChange", true)
                                    .putExtra("type", "expressageCompany").putExtra("extra", showCompanyActivityOrderDetailsPublic.getText().toString()));
                        }
                    }
                });
                clickCodeActivityOrderDetailsPublic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getBaseActivityContext() != null) {
                            startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                                    .putExtra("canChange", true)
                                    .putExtra("type", "expressageCode").putExtra("extra", showCodeActivityOrderDetailsPublic.getText().toString()));
                        }
                    }
                });

                break;
            case ListViewOrder.TYPE_WAITING_FOR_SERVICE_FROM_BUY://待服务(买家版)114
                break;
            case ListViewOrder.TYPE_WAITING_FOR_SERVICE_FROM_SELLER://待服务(卖家版)115
                break;
            case ListViewOrder.TYPE_WAITING_FOR_RECEIVING://待收货(买家版)120
                break;
            case ListViewOrder.TYPE_WAITING_FOR_COMMENT_FROM_BUY://待评价(买家版)116
                break;
            case ListViewOrder.TYPE_WAITING_FOR_COMMENT_FROM_SELLER://待评价(卖家版)117
                break;
            case ListViewOrder.TYPE_WAITING_FOR_READY_BUY://待预购(买家版)118
                break;
            case ListViewOrder.TYPE_WAITING_FOR_PAYMENT://待付款(买家版)119
                break;
            case ListViewOrder.TYPE_WAITING_FOR_RETURNS_FROM_BUY://待退货(买家版)121
                break;
            case ListViewOrder.TYPE_WAITING_FOR_RETURNS_FROMSELLER://待退货(卖家版)122
                break;
            case ListViewOrder.TYPE_WAITING_FOR_CHANGE_FROM_BUY://待换货(买家版)123
                break;
            case ListViewOrder.TYPE_WAITING_FOR_CHANGE_FROMSELLER://待换货(卖家版)124
                break;
        }
    }

    @OnClick({R.id.back_top, R.id.click_comment_activity_order_details_public})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.click_comment_activity_order_details_public:
                if (getBaseActivityContext() != null) {
//                    startActivity(new Intent(getBaseActivityContext(), ActivityOrderCommentSeller.class)
//                            .putExtra("targetPic", targetPic)
//                            .putExtra("targetOrderId", targetOrderId));
                }
                break;

        }
    }


    /**
     * 立即发货输入密码
     */
    public void send() {
        if (StringUtils.isEmpty(showCompanyActivityOrderDetailsPublic.getText().toString().trim()) || StringUtils.isEmpty(showCodeActivityOrderDetailsPublic.getText().toString().trim())) {
            ToastUtils.showShortToast(getBaseActivityContext(), "请填写您的物流信息");
            return;
        }
        P.e(ywLoadingDialog, Config.member_id, getBaseActivity(), new P.p() {
            @Override
            public void exist() {
                if (menuDialog == null) {
                    menuDialog = new MenuDialog(getBaseActivity());
                }
                menuDialog.createPasswordInputDialog("请输入您的支付密码", getBaseActivity(), new OnPayPasswordVerificationCallBack() {
                    @Override
                    public void onVerificationPassword(final String password, final PassWordKeyboard passWordKeyboard,final com.rey.material.app.BottomSheetDialog bottomSheetDialog) {
                        P.r(ywLoadingDialog, Config.member_id, password, getBaseActivity(), new P.r() {
                            @Override
                            public void match() {
                                bottomSheetDialog.dismiss();
                                passWordKeyboard.setStatus(true);
                                deliver();
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


    /**
     * 获取订单详情(卖家版)
     */
    public void getOrderDetailsSeller() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getOrderDetailsSeller(
                    new JSONObject()
                            .put("member_id",Config.member_id)
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
//                                    orderSellerModel.order_id = jsonObject.getJSONObject("data").getString("order_id");
//                                    orderSellerModel.mobile = jsonObject.getJSONObject("data").getString("mobile");
//                                    orderSellerModel.recipients = jsonObject.getJSONObject("data").getString("recipients");
//                                    orderSellerModel.address = jsonObject.getJSONObject("data").getString("address");
//                                    orderSellerModel.vehicle_name = jsonObject.getJSONObject("data").getString("vehicle_name");
//                                    orderSellerModel.remark = jsonObject.getJSONObject("data").getString("remark");
//                                    orderSellerModel.order_code = jsonObject.getJSONObject("data").getString("order_code");
//                                    orderSellerModel.delivery_time = jsonObject.getJSONObject("data").getString("delivery_time");
//                                    orderSellerModel.damages = jsonObject.getJSONObject("data").getString("damages");
//                                    orderSellerModel.total = jsonObject.getJSONObject("data").getString("total");
//                                    orderSellerModel.deduct_price = jsonObject.getJSONObject("data").getString("deduct_price");
//                                    orderSellerModel.coupon_price = jsonObject.getJSONObject("data").getString("coupon_price");
//                                    orderSellerModel.number = jsonObject.getJSONObject("data").getString("number");
//                                    orderSellerModel.integral = jsonObject.getJSONObject("data").getString("integral");
//                                    orderSellerModel.coupon_id = jsonObject.getJSONObject("data").getString("coupon_id");
//                                    orderSellerModel.create_time = jsonObject.getJSONObject("data").getString("create_time");
//                                    orderSellerModel.shop_id = jsonObject.getJSONObject("data").getString("shop_id");
//                                    orderSellerModel.member_id = jsonObject.getJSONObject("data").getString("member_id");
//                                    orderSellerModel.mode_payment = jsonObject.getJSONObject("data").getString("mode_payment");
//                                    orderSellerModel.type = jsonObject.getJSONObject("data").getString("type");
//                                    orderSellerModel.status = jsonObject.getJSONObject("data").getString("status");
//                                    orderSellerModel.shop_name = jsonObject.getJSONObject("data").getString("shop_name");
//                                    orderSellerModel.shop_logo = jsonObject.getJSONObject("data").getString("shop_logo");
//                                    orderSellerModel.start_time = jsonObject.getJSONObject("data").getString("start_time");
//                                    orderSellerModel.express = jsonObject.getJSONObject("data").getString("express");
//                                    orderSellerModel.waybill = jsonObject.getJSONObject("data").getString("waybill");
//                                    orderSellerModel.coupon_name = jsonObject.getJSONObject("data").getString("coupon_name");
//                                    JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("order_goods");
//                                    List<OrderSellerModel_Order_Goods> list = new ArrayList<>();
//                                    for (int i = 0; i < jsonArray.length(); i++) {
//                                        OrderSellerModel_Order_Goods orderSellerModel_order_goods = new OrderSellerModel_Order_Goods();
//                                        orderSellerModel_order_goods.id = jsonArray.getJSONObject(i).getString("id");
//                                        orderSellerModel_order_goods.price = jsonArray.getJSONObject(i).getString("price");
//                                        orderSellerModel_order_goods.number = jsonArray.getJSONObject(i).getString("number");
//                                        orderSellerModel_order_goods.goods_id = jsonArray.getJSONObject(i).getString("goods_id");
//                                        orderSellerModel_order_goods.title = jsonArray.getJSONObject(i).getString("title");
//                                        list.add(orderSellerModel_order_goods);
//                                    }
//                                    orderSellerModel.goods_data = list;

                                    Gson gson=new Gson();
                                    gson.fromJson(jsonObject.toString(),OrderSellerModel.class);

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
        if (orderSellerModel != null) {
            if ("service".equals(getIntent().getExtras().getString("goods_or_service"))) {
                adressOrCarNameActivityOrderDetailsPublic.setText("车型:");
                showCarOrNameActivityOrderDetailsPublic.setText(orderSellerModel.vehicle_name);
            } else if ("goods".equals(getIntent().getExtras().getString("goods_or_service"))) {
                adressOrCarNameActivityOrderDetailsPublic.setText("收货地址:");
                showCarOrNameActivityOrderDetailsPublic.setText(orderSellerModel.address);
                locationNameActivityOrderDetailsPublic.setVisibility(View.VISIBLE);
            }
            ListViewOrderSellerDetailsActivityOrderDetailsPublic.setData(getBaseActivityContext(), orderSellerModel);
            peopleNameActivityOrderDetailsPublic.setText(orderSellerModel.recipients);
            mobileNameActivityOrderDetailsPublic.setText(orderSellerModel.mobile);
            showAddressActivityOrderDetailsPublic.setText(orderSellerModel.address);
            priceActivityOrderDetailsPublic.setText(orderSellerModel.total);
            showTimeActivityOrderDetailsPublic.setText(orderSellerModel.delivery_time);
            showPenalSumActivityOrderDetailsPublic.setText(orderSellerModel.damages);
            showOtherActivityOrderDetailsPublic.setText(orderSellerModel.remark);
            showCodeActivityOrderDetailsPublic.setText(orderSellerModel.waybill);
            showCompanyActivityOrderDetailsPublic.setText(orderSellerModel.express);
        }

    }


    /**
     * 立即发货
     */
    public void deliver() {

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        LogUtils.e(getIntent().getExtras().getString("order_id"));
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.deliver(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("express", showCompanyActivityOrderDetailsPublic.getText().toString().trim())
                            .put("waybill", showCodeActivityOrderDetailsPublic.getText().toString().trim())
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
                                    EventBus.getDefault().post(new OrderCommentListChanged(getIntent().getExtras().getString("order_id")));
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
