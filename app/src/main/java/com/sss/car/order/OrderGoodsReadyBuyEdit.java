package com.sss.car.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.EditText.NumberSelectEdit;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangeInfoModel;
import com.sss.car.EventBusModel.ChangedDraftOrder;
import com.sss.car.EventBusModel.ChangedOrderModel;
import com.sss.car.EventBusModel.DefaultAddressChanged;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.ListViewOrderEdit;
import com.sss.car.model.CouponModel3;
import com.sss.car.model.IntegrityMoneyModel;
import com.sss.car.model.OrderEdit;
import com.sss.car.model.ShoppingCart;
import com.sss.car.utils.MenuDialog;
import com.sss.car.view.ActivityChangeInfo;
import com.sss.car.view.ActivityMyDataAdress;
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
 * 实物预购单草稿箱编辑
 * Created by leilei on 2017/9/29.
 */

@SuppressWarnings("ALL")
public class OrderGoodsReadyBuyEdit extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.people_name_order_goods_ready_buy_edit)
    TextView peopleNameOrderGoodsReadyBuyEdit;
    @BindView(R.id.mobile_name_order_goods_ready_buy_edit)
    TextView mobileNameOrderGoodsReadyBuyEdit;
    @BindView(R.id.click_choose_car_order_goods_ready_buy_edit)
    LinearLayout clickChooseAddressOrderGoodsReadyBuyEdit;
    @BindView(R.id.list_order_goods_ready_buy_edit)
    ListViewOrderEdit listOrderGoodsReadyBuyEdit;
    @BindView(R.id.show_coupon_order_goods_ready_buy_edit)
    TextView showCouponOrderGoodsReadyBuyEdit;
    @BindView(R.id.click_coupon_order_goods_ready_buy_edit)
    LinearLayout clickCouponOrderGoodsReadyBuyEdit;
    @BindView(R.id.show_order_time_order_goods_ready_buy_edit)
    TextView showOrderTimeOrderGoodsReadyBuyEdit;
    @BindView(R.id.click_order_time_order_service)
    LinearLayout clickOrderTimeOrderService;
    @BindView(R.id.show_penal_sum_order_goods_ready_buy_edit)
    TextView showPenalSumOrderGoodsReadyBuyEdit;
    @BindView(R.id.click_penal_sum_order_goods_ready_buy_edit)
    LinearLayout clickPenalSumOrderGoodsReadyBuyEdit;
    @BindView(R.id.show_other_order_goods_ready_buy_edit)
    TextView showOtherOrderGoodsReadyBuyEdit;
    @BindView(R.id.click_other_sum_order_goods_ready_buy_edit)
    LinearLayout clickOtherSumOrderGoodsReadyBuyEdit;
    @BindView(R.id.tip_order_goods_ready_buy_edit)
    TextView tipOrderGoodsReadyBuyEdit;
    @BindView(R.id.total_price_order_goods_ready_buy_edit)
    TextView totalPriceOrderGoodsReadyBuyEdit;
    @BindView(R.id.click_submit_order_goods_ready_buy_edit)
    TextView clickSubmitOrderGoodsReadyBuyEdit;
    @BindView(R.id.order_goods_ready_buy_edit)
    LinearLayout OrderGoodsReadyBuyEdit;
    YWLoadingDialog ywLoadingDialog;
    OrderEdit orderEdit;

    MenuDialog menuDialog;


    List<CouponModel3> list = new ArrayList<>();
    SSS_Adapter sss_adapter;


    SSS_Adapter integrityMoneyAdapter;
    List<IntegrityMoneyModel> integrityMoneyList = new ArrayList<>();


    SSS_Adapter orderTimeAdapter;
    List<IntegrityMoneyModel> orderTimeList = new ArrayList<>();


    String number;
    String totalPrice;
    String shop_id;
    String coupon_id;
    String penalSum;
    String date;
    String other;
    String mobile;
    String recipients;
    String address;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.write_order_goods_ready_buy_edit)
    TextView writeOrderGoodsReadyBuyEdit;
    @BindView(R.id.price_order_goods_ready_buy_edit)
    NumberSelectEdit priceOrderGoodsReadyBuyEdit;
    @BindView(R.id.address_order_goods_ready_buy_edit)
    TextView addressOrderGoodsReadyBuyEdit;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (menuDialog != null) {
            menuDialog.clear();
        }
        menuDialog = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        backTop = null;
        titleTop = null;
        peopleNameOrderGoodsReadyBuyEdit = null;
        mobileNameOrderGoodsReadyBuyEdit = null;
        clickChooseAddressOrderGoodsReadyBuyEdit = null;
        listOrderGoodsReadyBuyEdit = null;
        showCouponOrderGoodsReadyBuyEdit = null;
        clickCouponOrderGoodsReadyBuyEdit = null;
        showOrderTimeOrderGoodsReadyBuyEdit = null;
        clickOrderTimeOrderService = null;
        showPenalSumOrderGoodsReadyBuyEdit = null;
        clickPenalSumOrderGoodsReadyBuyEdit = null;
        showOtherOrderGoodsReadyBuyEdit = null;
        clickOtherSumOrderGoodsReadyBuyEdit = null;
        tipOrderGoodsReadyBuyEdit = null;
        totalPriceOrderGoodsReadyBuyEdit = null;
        clickSubmitOrderGoodsReadyBuyEdit = null;
        OrderGoodsReadyBuyEdit = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_goods_ready_buy_edit);
        ButterKnife.bind(this);
        customInit(OrderGoodsReadyBuyEdit, false, true, true);
        APPOftenUtils.underLineOfTextView(writeOrderGoodsReadyBuyEdit).setText("手动填写");
        APPOftenUtils.underLineOfTextView(rightButtonTop).setText("保存");
        rightButtonTop.setTextColor(getResources().getColor(R.color.mainColor));
        listOrderGoodsReadyBuyEdit.setListener(new ListViewOrderEdit.OnListViewOrderEditCallBack() {
            @Override
            public void onPriceChanged(final int price) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        OrderGoodsReadyBuyEdit.this.totalPrice = String.valueOf(price);
                        totalPriceOrderGoodsReadyBuyEdit.setText("¥" + price);
                    }
                });

            }

            @Override
            public void onShopName(String shop_id) {
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityShopInfo.class)
                            .putExtra("shop_id", shop_id));
                }
            }


            @Override
            public void onTotalCount(int totalCount) {
                number = String.valueOf(totalCount);
            }
        });



        menuDialog = new MenuDialog(this);

        if ("edit".equals(getIntent().getExtras().getString("mode"))) {
            titleTop.setText("实物预购填写");
            priceOrderGoodsReadyBuyEdit.setVisibility(View.VISIBLE);
            priceOrderGoodsReadyBuyEdit.defaultNumber(0)
                    .isNegativeNumber(false)
                    .setCurrentNumber(0)
                    .minNumber(0)
                    .isLongClick(false)
                    .init(getBaseActivityContext(), true);
            priceOrderGoodsReadyBuyEdit.setNumberSelectEditOperationCakkBack(new NumberSelectEdit.NumberSelectEditOperationCakkBack() {
                @Override
                public void onAdd(NumberSelectEdit numberSelectEdit, int currentNumber) {
                    totalPriceOrderGoodsReadyBuyEdit.setText("¥" + currentNumber);
                    number = currentNumber + "";
                }

                @Override
                public void onSubtract(NumberSelectEdit numberSelectEdit, int currentNumber) {
                    totalPriceOrderGoodsReadyBuyEdit.setText("¥" + currentNumber);
                    number = currentNumber + "";
                }

                @Override
                public void onZero(NumberSelectEdit numberSelectEdit) {
                    totalPriceOrderGoodsReadyBuyEdit.setText("¥" + 0);
                    number = 0 + "";
                }

                @Override
                public void onEditChanged(NumberSelectEdit numberSelectEdit, int currentNumber) {
                    totalPriceOrderGoodsReadyBuyEdit.setText("¥" + currentNumber);
                    number = currentNumber + "";
                }
            });
        } else {

        }

        initAdapter();
        defaultAddressSynthesize();
        orderAttr(false);
        orderTip();
        coupon(false);
        orderAttrTime(false);
        getOrderDetailsSeller();
    }

    void initAdapter() {


        /***********************************************************************************************************/
        sss_adapter = new SSS_Adapter<CouponModel3>(getBaseActivityContext(), R.layout.item_dialog_coupons) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, CouponModel3 bean, SSS_Adapter instance) {
                switch (bean.type) {
                    case "1":
                        helper.setText(R.id.type_item_dialog_coupons, "满减券");//type 1满减券，2现金券，3折扣券
                        break;
                    case "2":
                        helper.setText(R.id.type_item_dialog_coupons, "¥" + bean.price);//type 1满减券，2现金券，3折扣券
                        break;
                    case "3":
                        helper.setText(R.id.type_item_dialog_coupons, "折扣券");//type 1满减券，2现金券，3折扣券
                        break;
                }

                helper.setText(R.id.name_item_dialog_coupons, bean.name);
                helper.setText(R.id.date_item_dialog_coupons, bean.duration);
                if ("1".equals(bean.is_check)) {
                    helper.setChecked(R.id.cb_item_dialog_coupons, true);
                } else {
                    helper.setChecked(R.id.cb_item_dialog_coupons, false);
                }
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
                helper.setItemChildClickListener(R.id.click_item_dialog_coupons);
            }
        };

        sss_adapter.setOnItemListener(new SSS_OnItemListener() {

            @Override
            public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                switch (view.getId()) {
                    case R.id.click_item_dialog_coupons:
                        for (int i = 0; i < list.size(); i++) {

                            if (i == position) {
                                list.get(i).is_check = "1";
                                coupon_id = list.get(i).coupon_id;
                                showCouponOrderGoodsReadyBuyEdit.setText(list.get(i).name);
                            } else {
                                list.get(i).is_check = "0";
                            }
                        }
                        sss_adapter.setList(list);
                        break;
                }
            }
        });

        /*******************************************************************************************************/
        integrityMoneyAdapter = new SSS_Adapter<IntegrityMoneyModel>(getBaseActivityContext(), R.layout.item_dialog_coupons) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, IntegrityMoneyModel bean, SSS_Adapter instance) {

                helper.setText(R.id.name_item_dialog_coupons, bean.name);
                if ("1".equals(bean.is_check)) {
                    helper.setChecked(R.id.cb_item_dialog_coupons, true);
                } else {
                    helper.setChecked(R.id.cb_item_dialog_coupons, false);
                }
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
                helper.setItemChildClickListener(R.id.click_item_dialog_coupons);
            }
        };

        integrityMoneyAdapter.setOnItemListener(new SSS_OnItemListener() {

            @Override
            public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                switch (view.getId()) {
                    case R.id.click_item_dialog_coupons:
                        for (int i = 0; i < integrityMoneyList.size(); i++) {

                            if (i == position) {
                                integrityMoneyList.get(i).is_check = "1";
                                penalSum = integrityMoneyList.get(i).name;
                                showPenalSumOrderGoodsReadyBuyEdit.setText(integrityMoneyList.get(i).name);
                            } else {
                                integrityMoneyList.get(i).is_check = "0";
                            }
                        }
                        integrityMoneyAdapter.setList(integrityMoneyList);
                        break;
                }
            }
        });

        /********************************************************************************************************************/
        orderTimeAdapter = new SSS_Adapter<IntegrityMoneyModel>(getBaseActivityContext(), R.layout.item_dialog_coupons) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, IntegrityMoneyModel bean, SSS_Adapter instance) {

                helper.setText(R.id.name_item_dialog_coupons, bean.name);
                if ("1".equals(bean.is_check)) {
                    helper.setChecked(R.id.cb_item_dialog_coupons, true);
                } else {
                    helper.setChecked(R.id.cb_item_dialog_coupons, false);
                }
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
                helper.setItemChildClickListener(R.id.click_item_dialog_coupons);
            }
        };

        orderTimeAdapter.setOnItemListener(new SSS_OnItemListener() {
            @Override
            public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                switch (view.getId()) {
                    case R.id.click_item_dialog_coupons:
                        for (int i = 0; i < orderTimeList.size(); i++) {
                            if (i == position) {
                                orderTimeList.get(i).is_check = "1";
                                date = orderTimeList.get(i).name;
                                showOrderTimeOrderGoodsReadyBuyEdit.setText(orderTimeList.get(i).name);
                            } else {
                                orderTimeList.get(i).is_check = "0";
                            }
                        }
                        orderTimeAdapter.setList(orderTimeList);
                        break;
                }
            }
        });
    }

    @OnClick({R.id.back_top, R.id.right_button_top, R.id.click_choose_car_order_goods_ready_buy_edit, R.id.click_coupon_order_goods_ready_buy_edit, R.id.click_order_time_order_service, R.id.click_penal_sum_order_goods_ready_buy_edit, R.id.click_other_sum_order_goods_ready_buy_edit, R.id.click_submit_order_goods_ready_buy_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                update_order("0");
                break;
            case R.id.click_choose_car_order_goods_ready_buy_edit:
                if ("edit".equals(getIntent().getExtras().getString("mode"))) {
                    if (getBaseActivityContext() != null) {
                        startActivity(new Intent(getBaseActivityContext(), ActivityMyDataAdress.class));
                    }
                }

                break;
            case R.id.click_coupon_order_goods_ready_buy_edit:
                if ("edit".equals(getIntent().getExtras().getString("mode"))) {
                    coupon(true);
                }
                break;
            case R.id.click_order_time_order_service:
                if ("edit".equals(getIntent().getExtras().getString("mode"))) {
                    orderAttrTime(true);
                }

                break;
            case R.id.click_penal_sum_order_goods_ready_buy_edit:
                if ("edit".equals(getIntent().getExtras().getString("mode"))) {
                    orderAttr(true);
                }

                break;
            case R.id.click_other_sum_order_goods_ready_buy_edit:
                if ("edit".equals(getIntent().getExtras().getString("mode"))) {
                    if (getBaseActivityContext() != null) {
                        startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                                .putExtra("type", "other")
                                .putExtra("canChange", true)
                                .putExtra("extra", ""));
                    }
                }

                break;
            case R.id.click_submit_order_goods_ready_buy_edit:
                if (StringUtils.isEmpty(mobile)||StringUtils.isEmpty(recipients)||StringUtils.isEmpty(address)){
                    ToastUtils.showShortToast(getBaseActivityContext(),"请设置您的个人信息");
                    return;
                }
                update_order("11");
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeInfoModel event) {
        if ("other".equals(event.type)) {
            other = event.msg;
            if (!StringUtils.isEmpty(other)) {
                showOtherOrderGoodsReadyBuyEdit.setText(other);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DefaultAddressChanged event) {
        defaultAddressSynthesize();
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
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getOrderDetailsSeller(
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
                                    orderEdit = new OrderEdit();
                                    Gson gson = new Gson();
                                    orderEdit = gson.fromJson(jsonObject.getJSONObject("data").toString(), OrderEdit.class);
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
     * 获取用户默认地址
     */
    public void defaultAddressSynthesize() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.defaultAddressSynthesize(
                    new JSONObject()
                            .put("member_id", Config.member_id)
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
                                    peopleNameOrderGoodsReadyBuyEdit.setText(jsonObject.getJSONObject("data").getString("recipients"));
                                    mobileNameOrderGoodsReadyBuyEdit.setText(jsonObject.getJSONObject("data").getString("mobile"));
                                    addressOrderGoodsReadyBuyEdit.setText(jsonObject.getJSONObject("data").getString("address"));
                                    mobile = jsonObject.getJSONObject("data").getString("mobile");
                                    recipients = jsonObject.getJSONObject("data").getString("recipients");
                                    address = jsonObject.getJSONObject("data").getString("address");
                                } else {
                                    mobile = null;
                                    recipients = null;
                                    address = null;
                                    peopleNameOrderGoodsReadyBuyEdit.setText("");
                                    mobileNameOrderGoodsReadyBuyEdit.setText("");
//                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
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
        if (orderEdit != null) {
            mobile = orderEdit.mobile;
            recipients = orderEdit.recipients;
            address = orderEdit.address;
            mobileNameOrderGoodsReadyBuyEdit.setText(mobile);
            peopleNameOrderGoodsReadyBuyEdit.setText(recipients);
            addressOrderGoodsReadyBuyEdit.setText(address);

            penalSum = orderEdit.damages;
            date = orderEdit.delivery_time;
            number = orderEdit.number;
            totalPrice = orderEdit.total;
            shop_id = orderEdit.shop_id;
            coupon_id = orderEdit.coupon_id;
            other = orderEdit.remark;

            if (!StringUtils.isEmpty(orderEdit.total)) {
//                totalPriceOrderGoodsReadyBuyEdit.setText(((int) Double.parseDouble(orderEdit.total)) + "");
                priceOrderGoodsReadyBuyEdit.setCurrentNumber(((int) Double.parseDouble(orderEdit.total)));
            }
            totalPriceOrderGoodsReadyBuyEdit.setText("¥" + orderEdit.total);
            showOrderTimeOrderGoodsReadyBuyEdit.setText(orderEdit.delivery_time);
            showPenalSumOrderGoodsReadyBuyEdit.setText(orderEdit.damages);
            showOtherOrderGoodsReadyBuyEdit.setText(orderEdit.remark);

            listOrderGoodsReadyBuyEdit.setList(getBaseActivityContext(), orderEdit, getIntent().getExtras().getString("type"));

        }
    }

    /**
     * 获取用户预购订单优惠券
     */
    public void coupon(final boolean showDialog) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();

        if (showDialog) {
            if (list.size() == 0) {
                try {
                    addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.coupon(
                            new JSONObject()
                                    .put("member_id", Config.member_id)
                                    .put("shop_id", getIntent().getExtras().getString("shop_id"))
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
                                            parseCoupon(jsonObject);
                                            if (list.size() > 0) {
                                                sss_adapter.setList(list);
                                                menuDialog.createCouponBottomDialog(getBaseActivityContext(), sss_adapter);
                                            } else {
                                                ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                            }
                                        } else {
                                            if (showDialog) {
                                                ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                            }
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
            } else {
                if (ywLoadingDialog != null) {
                    ywLoadingDialog.disMiss();
                }
                sss_adapter.setList(list);
                menuDialog.createCouponBottomDialog(getBaseActivityContext(), sss_adapter);
            }
        } else {
            try {
                addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.coupon(
                        new JSONObject()
                                .put("member_id", Config.member_id)
                                .put("shop_id", getIntent().getExtras().getString("shop_id"))
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
                                        parseCoupon(jsonObject);
                                        if (showDialog) {//如果要弹出提示框,说明是用户手动调用,则此处应该弹出底部选择框
                                            if (list.size() > 0) {
                                                sss_adapter.setList(list);
                                                menuDialog.createCouponBottomDialog(getBaseActivityContext(), sss_adapter);
                                            } else {
                                                ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                            }

                                        }


                                    } else {
                                        if (showDialog) {
                                            ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                        }
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

    void parseCoupon(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        if (jsonArray.length() > 0) {
            list.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                CouponModel3 couponModel3 = new CouponModel3();
                couponModel3.id = jsonArray.getJSONObject(i).getString("coupon_id");
                couponModel3.type = jsonArray.getJSONObject(i).getString("type");
                couponModel3.money = jsonArray.getJSONObject(i).getString("money");
                couponModel3.price = jsonArray.getJSONObject(i).getString("price");
                couponModel3.start_time = jsonArray.getJSONObject(i).getString("start_time");
                couponModel3.end_time = jsonArray.getJSONObject(i).getString("end_time");
                couponModel3.name = jsonArray.getJSONObject(i).getString("name");
                couponModel3.shop_id = jsonArray.getJSONObject(i).getString("shop_id");
                couponModel3.coupon_id = jsonArray.getJSONObject(i).getString("coupon_id");
                couponModel3.duration = jsonArray.getJSONObject(i).getString("duration");
                couponModel3.is_check = jsonArray.getJSONObject(i).getString("is_check");
                list.add(couponModel3);
                showCouponOrderGoodsReadyBuyEdit.setTextColor(getResources().getColor(R.color.black));
                if ("1".equals(couponModel3.is_check)) {
                    showCouponOrderGoodsReadyBuyEdit.setText(couponModel3.name);
                }
            }
        }else {
            showCouponOrderGoodsReadyBuyEdit.setText("无可用优惠券");
            showCouponOrderGoodsReadyBuyEdit.setTextColor(getResources().getColor(R.color.grayness));
        }
    }

    /**
     * 获取用户订单属性
     */
    public void orderAttr(final boolean showDialog) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();

        if (showDialog) {
            if (integrityMoneyList.size() == 0) {
                try {
                    addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.orderAttr(
                            new JSONObject()
                                    .put("member_id", Config.member_id)
                                    .put("type", "2")//1送达时效，2违约金比例，3求助类型，4服务就位
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
                                            parseToIntegrityMoneyList(jsonObject);
                                            if (list.size() > 0) {
                                                integrityMoneyAdapter.setList(integrityMoneyList);
                                                menuDialog.createIntegrityBottomDialog("违约金比例", getBaseActivityContext(), integrityMoneyAdapter);
                                            } else {
                                                ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                            }
                                        } else {
                                            if (showDialog) {
                                                ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                            }
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
            } else {
                if (ywLoadingDialog != null) {
                    ywLoadingDialog.disMiss();
                }
                integrityMoneyAdapter.setList(integrityMoneyList);
                menuDialog.createIntegrityBottomDialog("违约金比例", getBaseActivityContext(), integrityMoneyAdapter);
            }
        } else {
            try {
                addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.orderAttr(
                        new JSONObject()
                                .put("member_id", Config.member_id)
                                .put("type", "2")//1送达时效，2违约金比例，3求助类型，4服务就位
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
                                        parseToIntegrityMoneyList(jsonObject);
                                        if (showDialog) {//如果要弹出提示框,说明是用户手动调用,则此处应该弹出底部选择框
                                            if (list.size() > 0) {
                                                integrityMoneyAdapter.setList(integrityMoneyList);
                                                menuDialog.createIntegrityBottomDialog("违约金比例", getBaseActivityContext(), integrityMoneyAdapter);
                                            } else {
                                                ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                            }

                                        }


                                    } else {
                                        if (showDialog) {
                                            ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                        }
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

    void parseToIntegrityMoneyList(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        if (jsonArray.length() > 0) {
            integrityMoneyList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                IntegrityMoneyModel integrityMoneyModel = new IntegrityMoneyModel();
                integrityMoneyModel.attr_id = jsonArray.getJSONObject(i).getString("attr_id");
                integrityMoneyModel.is_check = jsonArray.getJSONObject(i).getString("is_check");
                integrityMoneyModel.name = jsonArray.getJSONObject(i).getString("name");
                if ("1".equals(integrityMoneyModel.is_check)) {
                    LogUtils.e(integrityMoneyModel.is_check);
                    penalSum = integrityMoneyModel.name;
                    showPenalSumOrderGoodsReadyBuyEdit.setText(integrityMoneyModel.name);
                }
                integrityMoneyList.add(integrityMoneyModel);
            }

        }
    }

    /**
     * 获取送达时间
     */
    public void orderAttrTime(final boolean showDialog) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();

        if (showDialog) {
            if (orderTimeList.size() == 0) {
                try {
                    addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.orderAttr(
                            new JSONObject()
                                    .put("member_id", Config.member_id)
                                    .put("type", "1")//1送达时效，2违约金比例，3求助类型，4服务就位
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
                                            parseOrderAttrTime(jsonObject);
                                            if (list.size() > 0) {
                                                orderTimeAdapter.setList(orderTimeList);
                                                menuDialog.createIntegrityBottomDialog("送达时效", getBaseActivityContext(), orderTimeAdapter);
                                            } else {
                                                ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                            }
                                        } else {
                                            if (showDialog) {
                                                ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                            }
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
            } else {
                if (ywLoadingDialog != null) {
                    ywLoadingDialog.disMiss();
                }
                orderTimeAdapter.setList(orderTimeList);
                menuDialog.createIntegrityBottomDialog("送达时效", getBaseActivityContext(), orderTimeAdapter);
            }
        } else {
            try {
                addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.orderAttr(
                        new JSONObject()
                                .put("member_id", Config.member_id)
                                .put("type", "1")//1送达时效，2违约金比例，3求助类型，4服务就位
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
                                        parseOrderAttrTime(jsonObject);
                                        if (showDialog) {//如果要弹出提示框,说明是用户手动调用,则此处应该弹出底部选择框
                                            if (list.size() > 0) {
                                                orderTimeAdapter.setList(orderTimeList);
                                                menuDialog.createIntegrityBottomDialog("送达时效", getBaseActivityContext(), orderTimeAdapter);
                                            } else {
                                                ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                            }

                                        }


                                    } else {
                                        if (showDialog) {
                                            ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                        }
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

    void parseOrderAttrTime(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        if (jsonArray.length() > 0) {
            orderTimeList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                IntegrityMoneyModel integrityMoneyModel = new IntegrityMoneyModel();
                integrityMoneyModel.attr_id = jsonArray.getJSONObject(i).getString("attr_id");
                integrityMoneyModel.is_check = jsonArray.getJSONObject(i).getString("is_check");
                integrityMoneyModel.name = jsonArray.getJSONObject(i).getString("name");
                if ("1".equals(integrityMoneyModel.is_check)) {
                    date = integrityMoneyModel.attr_id;
                    showOrderTimeOrderGoodsReadyBuyEdit.setText(integrityMoneyModel.name);
                }
                orderTimeList.add(integrityMoneyModel);
            }

        }
    }


    /**
     * 更新购物车,预购,订单内的商品
     */
    public void updateShoppingCart(List<ShoppingCart> list) throws JSONException {
        JSONArray sid = new JSONArray();
        JSONArray num = new JSONArray();
        JSONArray options = new JSONArray();
        for (int j = 0; j < list.size(); j++) {
            for (int k = 0; k < list.get(j).data.size(); k++) {
                sid.put(list.get(j).data.get(k).sid);
                num.put(list.get(j).data.get(k).num);

                JSONArray jsonArray = new JSONArray();
                for (int l = 0; l < list.get(j).data.get(k).options.size(); l++) {
                    jsonArray.put(new JSONObject().put("name", list.get(j).data.get(k).options.get(l).name)
                            .put("title", list.get(j).data.get(k).options.get(l).title));
                }
                options.put(jsonArray);
            }
        }


//        if (ywLoadingDialog != null) {
//            ywLoadingDialog.disMiss();
//        }
//        ywLoadingDialog = null;
//        if (getBaseActivityContext() != null) {
//            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
//            ywLoadingDialog.show();
//        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.UpdateShoppingCart(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("sid", sid)
                            .put("num", num)
                            .put("options", options)
                            .put("type", "order")//购物车（cart）预购（pre_order）订单（order）
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

//                            if (ywLoadingDialog != null) {
//                                ywLoadingDialog.disMiss();
//                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {

//                            if (ywLoadingDialog != null) {
//                                ywLoadingDialog.disMiss();
//                            }
//                            try {
//                                JSONObject jsonObject = new JSONObject(response);
//                                if ("1".equals(jsonObject.getString("status"))) {
//                                } else {
//                                }
//                            } catch (JSONException e) {
//                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Shopping Cart-0");
//                                e.printStackTrace();
//                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Shopping Cart-0");
            e.printStackTrace();
        }
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
                            .put("article_id", "7")//文章ID (3实物类)
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
                                    tipOrderGoodsReadyBuyEdit.setText(jsonObject.getJSONObject("data").getString("contents"));
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
     * 保存/提交订单
     */
    public void update_order(String status) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        if (listOrderGoodsReadyBuyEdit.getOrderEdit() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "订单刷新中...");
            return;
        }
        OrderEdit o = listOrderGoodsReadyBuyEdit.getOrderEdit();
        String goods_data = null;
        try {
            JSONObject j = new JSONObject(new Gson().toJson(o, OrderEdit.class));
            goods_data = j.getString("goods_data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.update_order(
                    new JSONObject()
                            .put("goods_data", goods_data)
                            .put("order_id", getIntent().getExtras().getString("order_id"))
                            .put("number", number)//商品数量
                            .put("total", totalPrice)//商品价格
                            .put("coupon_id", coupon_id)//优惠券ID
                            .put("delivery_time", date)//送达时长/预约时间
                            .put("damages", penalSum)//	违约金比例
                            .put("remark", other)//备注
                            .put("type", getIntent().getExtras().getString("type"))//1车品2车服
                            .put("shop_id", shop_id)//店铺ID
                            .put("member_id", Config.member_id)//用户Id
                            .put("mobile", mobile)
                            .put("recipients", recipients)//用户
                            .put("address", address)//备注
                            .put("status", status)//0草稿箱，1未付款，2已付款，3已完成，4已取消，5已退款，6已删除,11待预购
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
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    EventBus.getDefault().post(new ChangedDraftOrder());
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
}
