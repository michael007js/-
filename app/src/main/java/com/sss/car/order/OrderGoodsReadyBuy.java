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
import com.blankj.utilcode.util.ThreadPoolUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangeInfoModel;
import com.sss.car.EventBusModel.DefaultAddressChanged;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.ListviewOrderServiceGoodsList;
import com.sss.car.model.CouponModel3;
import com.sss.car.model.IntegrityMoneyModel;
import com.sss.car.model.ShoppingCart;
import com.sss.car.model.ShoppingCart_Data;
import com.sss.car.model.ShoppingCart_Data_Options;
import com.sss.car.order_new.NewOrderBuyer;
import com.sss.car.utils.C;
import com.sss.car.utils.MenuDialog;
import com.sss.car.view.ActivityChangeInfo;
import com.sss.car.view.ActivityMyDataAdress;
import com.sss.car.view.ActivityShopInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.channels.UnsupportedAddressTypeException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 实物预购单
 * Created by leilei on 2017/9/29.
 */

public class OrderGoodsReadyBuy extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.people_name_order_goods_ready_buy)
    TextView peopleNameOrderGoodsReadyBuy;
    @BindView(R.id.mobile_name_order_goods_ready_buy)
    TextView mobileNameOrderGoodsReadyBuy;
    @BindView(R.id.address_order_goods_ready_buy)
    TextView addressOrderGoodsReadyBuy;
    @BindView(R.id.click_choose_car_order_goods_ready_buy)
    LinearLayout clickChooseAddressOrderGoodsReadyBuy;
    @BindView(R.id.list_order_goods_ready_buy)
    ListviewOrderServiceGoodsList listOrderGoodsReadyBuy;
    @BindView(R.id.price_order_goods_ready_buy)
    NumberSelectEdit priceOrderGoodsReadyBuy;
    @BindView(R.id.show_coupon_order_goods_ready_buy)
    TextView showCouponOrderGoodsReadyBuy;
    @BindView(R.id.click_coupon_order_goods_ready_buy)
    LinearLayout clickCouponOrderGoodsReadyBuy;
    @BindView(R.id.show_order_time_order_goods_ready_buy)
    TextView showOrderTimeOrderGoodsReadyBuy;
    @BindView(R.id.click_order_time_order_service)
    LinearLayout clickOrderTimeOrderService;
    @BindView(R.id.show_penal_sum_order_goods_ready_buy)
    TextView showPenalSumOrderGoodsReadyBuy;
    @BindView(R.id.click_penal_sum_order_goods_ready_buy)
    LinearLayout clickPenalSumOrderGoodsReadyBuy;
    @BindView(R.id.show_other_order_goods_ready_buy)
    TextView showOtherOrderGoodsReadyBuy;
    @BindView(R.id.click_other_sum_order_goods_ready_buy)
    LinearLayout clickOtherSumOrderGoodsReadyBuy;
    @BindView(R.id.tip_order_goods_ready_buy)
    TextView tipOrderGoodsReadyBuy;
    @BindView(R.id.total_price_order_goods_ready_buy)
    TextView totalPriceOrderGoodsReadyBuy;
    @BindView(R.id.click_submit_order_goods_ready_buy)
    TextView clickSubmitOrderGoodsReadyBuy;
    @BindView(R.id.order_goods_ready_buy)
    LinearLayout OrderGoodsReadyBuy;
    YWLoadingDialog ywLoadingDialog;


    MenuDialog menuDialog;


    List<ShoppingCart> shoppingCartOrderlist = new ArrayList<>();

    List<CouponModel3> list = new ArrayList<>();
    SSS_Adapter sss_adapter;


    SSS_Adapter integrityMoneyAdapter;
    List<IntegrityMoneyModel> integrityMoneyList = new ArrayList<>();


    SSS_Adapter orderTimeAdapter;
    List<IntegrityMoneyModel> orderTimeList = new ArrayList<>();

    String number;
    String totalPrice = "0";
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
    @BindView(R.id.write_order_goods_ready_buy)
    TextView writeOrderGoodsReadyBuy;


    ThreadPoolUtils threadPoolUtils = new ThreadPoolUtils(ThreadPoolUtils.FixedThread, 1);

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (threadPoolUtils != null) {
            threadPoolUtils.shutDownNow();
        }
        threadPoolUtils = null;
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
        peopleNameOrderGoodsReadyBuy = null;
        mobileNameOrderGoodsReadyBuy = null;
        addressOrderGoodsReadyBuy = null;
        clickChooseAddressOrderGoodsReadyBuy = null;
        listOrderGoodsReadyBuy = null;
        priceOrderGoodsReadyBuy = null;
        showCouponOrderGoodsReadyBuy = null;
        clickCouponOrderGoodsReadyBuy = null;
        showOrderTimeOrderGoodsReadyBuy = null;
        clickOrderTimeOrderService = null;
        showPenalSumOrderGoodsReadyBuy = null;
        clickPenalSumOrderGoodsReadyBuy = null;
        showOtherOrderGoodsReadyBuy = null;
        clickOtherSumOrderGoodsReadyBuy = null;
        tipOrderGoodsReadyBuy = null;
        totalPriceOrderGoodsReadyBuy = null;
        clickSubmitOrderGoodsReadyBuy = null;
        OrderGoodsReadyBuy = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_goods_ready_buy);
        ButterKnife.bind(this);
        customInit(OrderGoodsReadyBuy, false, true, true);
        APPOftenUtils.underLineOfTextView(writeOrderGoodsReadyBuy).setText("手动填写");
        APPOftenUtils.underLineOfTextView(rightButtonTop).setText("保存");
        rightButtonTop.setTextColor(getResources().getColor(R.color.mainColor));
        listOrderGoodsReadyBuy.setListviewOrderServiceGoodsListOperationCallBacn(new ListviewOrderServiceGoodsList.ListviewOrderServiceGoodsListOperationCallBacn() {
            @Override
            public void onClickFromShopName(String shop_id) {
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityShopInfo.class)
                            .putExtra("shop_id", shop_id));
                }
            }

            @Override
            public void onTotalPrice(final int totalPrice) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        OrderGoodsReadyBuy.this.totalPrice = String.valueOf(totalPrice);
                        priceOrderGoodsReadyBuy.setCurrentNumber(totalPrice);
                    }
                });
            }

            @Override
            public void onTotalCount(int totalCount) {
                number = String.valueOf(totalCount);

            }
        });

        listOrderGoodsReadyBuy.setAddAndSubtractCallBack(new ListviewOrderServiceGoodsList.AddAndSubtractCallBack() {
            @Override
            public void onAddAndSubtract(String number, String sid, List<ShoppingCart> list) {
                try {
                    updateShoppingCart(list);
                    setClick(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAddAndSubtractPrice(String count_price, String sid, List<ShoppingCart> shoppingCartOrderlist) {
                try {
                    updateShoppingCart(shoppingCartOrderlist);
                    setClick(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        priceOrderGoodsReadyBuy
                .init(getBaseActivityContext(), false)
                .isHideChangeButton(true)
                .isNegativeNumber(false)
                .minNumber(0)
                .isLongClick(true)
                .setNumberSelectEditOperationCakkBack(new NumberSelectEdit.NumberSelectEditOperationCakkBack() {
                    @Override
                    public void onAdd(NumberSelectEdit numberSelectEdit, int currentNumber) {
                        totalPrice = String.valueOf(currentNumber);
                        coupon_id = null;
                        setClick(false);

                    }

                    @Override
                    public void onSubtract(NumberSelectEdit numberSelectEdit, int currentNumber) {
                        LogUtils.e(currentNumber);
                        totalPrice = String.valueOf(currentNumber);
                        coupon_id = null;
                        setClick(false);
                    }

                    @Override
                    public void onZero(NumberSelectEdit numberSelectEdit) {
                        totalPrice = String.valueOf(numberSelectEdit.getCurrentNumber());
                    }

                    @Override
                    public void onEditChanged(NumberSelectEdit numberSelectEdit, int currentNumber) {
                        totalPrice = String.valueOf(numberSelectEdit.getCurrentNumber());

                    }
                });


//        if ("ActivityGoodsServiceDetails".equals(getIntent().getExtras().getString("from"))) {
//            clickSubmitOrderGoodsReadyBuy.setText("去支付");
//        }

        menuDialog = new MenuDialog(this);
        titleTop.setText("实物预购订单");
        initAdapter();
        defaultAddressSynthesize();
        getShoppingCartOrder();
        orderAttr(false);
        orderTip();
        orderAttrTime(false);
        setClick(true);
    }

    void initAdapter() {


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
                    default:
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
                                showCouponOrderGoodsReadyBuy.setText(list.get(i).name);
                            } else {
                                list.get(i).is_check = "0";
                            }
                        }
                        sss_adapter.setList(list);
                        try {
                            updateShoppingCart(listOrderGoodsReadyBuy.getShoppingCartOrderlist());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
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
                                showPenalSumOrderGoodsReadyBuy.setText(integrityMoneyList.get(i).name);
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
                                showOrderTimeOrderGoodsReadyBuy.setText(orderTimeList.get(i).name);
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

    @OnClick({R.id.back_top, R.id.right_button_top, R.id.click_choose_car_order_goods_ready_buy, R.id.click_order_time_order_service, R.id.click_penal_sum_order_goods_ready_buy, R.id.click_other_sum_order_goods_ready_buy, R.id.click_submit_order_goods_ready_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                save_drafts();
                break;
            case R.id.click_choose_car_order_goods_ready_buy:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataAdress.class));
                }
                break;
            case R.id.click_order_time_order_service:
                orderAttrTime(true);
                break;
            case R.id.click_penal_sum_order_goods_ready_buy:
                orderAttr(true);
                break;
            case R.id.click_other_sum_order_goods_ready_buy:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                            .putExtra("type", "other")
                            .putExtra("canChange", true)
                            .putExtra("extra", ""));
                }
                break;
            case R.id.click_submit_order_goods_ready_buy:
//                if ("ActivityGoodsServiceDetails".equals(getIntent().getExtras().getString("from"))) {
//                    menuDialog.createPaymentDialog(getBaseActivityContext());
//                } else {
//                    submitOrder();
//                }
                if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(recipients) || StringUtils.isEmpty(address)) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请设置您的个人信息");
                    return;
                }

                if (StringUtils.isEmpty(date) || StringUtils.isEmpty(penalSum)) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请选择预约时间或违约金比例");
                    return;
                }
                submitOrder();


                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeInfoModel event) {
        if ("other".equals(event.type)) {
            other = event.msg;
            if (!StringUtils.isEmpty(other)) {
                showOtherOrderGoodsReadyBuy.setText(other);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DefaultAddressChanged event) {
        defaultAddressSynthesize();
    }


    private void setClick(boolean enable) {
        if (enable) {
            coupon(false);
            clickCouponOrderGoodsReadyBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (StringUtils.isEmpty(shop_id)) {
                        ToastUtils.showLongToast(getBaseActivityContext(), "信息获取中");
                    } else {
                        coupon(true);
                    }
                }
            });
        } else {
            coupon_id = null;
            showCouponOrderGoodsReadyBuy.setTextColor(getResources().getColor(R.color.grayness));
            showCouponOrderGoodsReadyBuy.setText("优惠券不可用");
            clickCouponOrderGoodsReadyBuy.setOnClickListener(null);
        }
    }

    private void requestPrice() {
        threadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.coupon_price(
                            new JSONObject()
                                    .put("money", totalPrice)
                                    .put("member_id", Config.member_id)
                                    .put("coupon_id", coupon_id)
                                    .put("shop_id", shop_id)
                                    .toString(), new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if ("1".equals(jsonObject.getString("status"))) {
                                            if (totalPriceOrderGoodsReadyBuy != null) {
                                                String a = "¥" + jsonObject.getJSONObject("data").getString("total");
                                                totalPriceOrderGoodsReadyBuy.setText(a);
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
                                    peopleNameOrderGoodsReadyBuy.setText(jsonObject.getJSONObject("data").getString("recipients"));
                                    mobileNameOrderGoodsReadyBuy.setText(jsonObject.getJSONObject("data").getString("mobile"));
                                    addressOrderGoodsReadyBuy.setText(jsonObject.getJSONObject("data").getString("address"));
                                    mobile = jsonObject.getJSONObject("data").getString("mobile");
                                    recipients = jsonObject.getJSONObject("data").getString("recipients");
                                    address = jsonObject.getJSONObject("data").getString("address");
                                } else {
                                    mobile = null;
                                    recipients = null;
                                    address = null;
                                    peopleNameOrderGoodsReadyBuy.setText("");
                                    mobileNameOrderGoodsReadyBuy.setText("");
                                    addressOrderGoodsReadyBuy.setText("");
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

    /**
     * 获取购物车列表/订单列表
     */
    public void getShoppingCartOrder() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getShoppingCartOrder(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("type", "order")//购物车type=cart，订单type=order
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
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (jsonArray.length() > 0) {
                                        shoppingCartOrderlist.clear();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            ShoppingCart shoppingCart = new ShoppingCart();
                                            shoppingCart.shop_id = jsonArray.getJSONObject(i).getString("shop_id");
                                            shoppingCart.name = jsonArray.getJSONObject(i).getString("name");
                                            shoppingCart.logo = jsonArray.getJSONObject(i).getString("logo");
                                            shoppingCart.total_rows = jsonArray.getJSONObject(i).getString("total_rows");
                                            shoppingCart.total = jsonArray.getJSONObject(i).getString("total");
                                            JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("data");
                                            List<ShoppingCart_Data> ShoppingCart_Data = new ArrayList<>();
                                            for (int j = 0; j < jsonArray1.length(); j++) {
                                                ShoppingCart_Data shoppingCart_data = new ShoppingCart_Data();
                                                shoppingCart_data.id = jsonArray1.getJSONObject(j).getString("id");
                                                shoppingCart_data.name = jsonArray1.getJSONObject(j).getString("name");
                                                shoppingCart_data.num = jsonArray1.getJSONObject(j).getString("num");
                                                shoppingCart_data.price = jsonArray1.getJSONObject(j).getString("price");
                                                shoppingCart_data.count_price = jsonArray1.getJSONObject(j).getString("price");
                                                shoppingCart_data.shop_id = jsonArray1.getJSONObject(j).getString("shop_id");
                                                shoppingCart_data.sid = jsonArray1.getJSONObject(j).getString("sid");
                                                shoppingCart_data.master_map = jsonArray1.getJSONObject(j).getString("master_map");
                                                shoppingCart_data.total = jsonArray1.getJSONObject(j).getString("total");
                                                shoppingCart_data.is_collect = jsonArray1.getJSONObject(j).getString("is_collect");
                                                shoppingCart_data.size_name = jsonArray1.getJSONObject(j).getString("size_name");
                                                shoppingCart_data.type = jsonArray1.getJSONObject(j).getString("type");
                                                shop_id = shoppingCart_data.shop_id;
                                                List<ShoppingCart_Data_Options> options = new ArrayList<>();
//                                                if (jsonArray1.getJSONObject(j).has("options")){
                                                JSONArray jsonArray2 = jsonArray1.getJSONObject(j).getJSONArray("options");
                                                for (int k = 0; k < jsonArray2.length(); k++) {
                                                    ShoppingCart_Data_Options shoppingCart_data_options = new ShoppingCart_Data_Options();
                                                    shoppingCart_data_options.name = jsonArray2.getJSONObject(k).getString("name");
                                                    shoppingCart_data_options.title = jsonArray2.getJSONObject(k).getString("title");
                                                    options.add(shoppingCart_data_options);
                                                }
//                                                }

                                                shoppingCart_data.options = options;
                                                ShoppingCart_Data.add(shoppingCart_data);
                                            }

                                            shoppingCart.data = ShoppingCart_Data;
                                            shoppingCartOrderlist.add(shoppingCart);
                                        }
                                        listOrderGoodsReadyBuy.setList(OrderGoodsReadyBuy.this, shoppingCartOrderlist, true);
//                                        listOrderGoodsReadyBuy.totalPrice();
//                                        listOrderGoodsReadyBuy.totalCount();
                                        coupon(false);
                                    }

                                } else {
                                    shoppingCartOrderlist.clear();
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:goods-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:goods-0");
            e.printStackTrace();
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
                                    .put("shop_id", shop_id)
                                    .put("money", totalPrice)
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
                                                coupon_id = list.get(0).coupon_id;
                                            } else {
                                                ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                            }
                                        } else {
                                            if (showDialog) {
                                                ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                            }
                                        }
                                        try {
                                            updateShoppingCart(listOrderGoodsReadyBuy.getShoppingCartOrderlist());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
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
                                .put("money", totalPrice)
                                .put("shop_id", shop_id)
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
                                                coupon_id = list.get(0).coupon_id;
                                            } else {
                                                ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                            }

                                        }
                                    } else {
                                        if (showDialog) {
                                            ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                        }
                                    }

                                    try {
                                        updateShoppingCart(listOrderGoodsReadyBuy.getShoppingCartOrderlist());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
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
                LogUtils.e(couponModel3.is_check);
                showCouponOrderGoodsReadyBuy.setTextColor(getResources().getColor(R.color.black));
                showCouponOrderGoodsReadyBuy.setText("优惠券可用");
//                if ("1".equals(couponModel3.is_check)) {
//                    showCouponOrderGoodsReadyBuy.setText(couponModel3.name);
//                    coupon_id = couponModel3.coupon_id;
//                }
            }
        } else {
            showCouponOrderGoodsReadyBuy.setText("无可用优惠券");
            showCouponOrderGoodsReadyBuy.setTextColor(getResources().getColor(R.color.grayness));
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
                    penalSum = integrityMoneyModel.name;
                    if (showPenalSumOrderGoodsReadyBuy!=null) {
                        showPenalSumOrderGoodsReadyBuy.setText(integrityMoneyModel.name);
                    }
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
                    date = /*integrityMoneyModel.attr_id*/integrityMoneyModel.name;
                    showOrderTimeOrderGoodsReadyBuy.setText(integrityMoneyModel.name);
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
        JSONArray price = new JSONArray();
        JSONArray num = new JSONArray();
        JSONArray total = new JSONArray();
        JSONArray options = new JSONArray();
        for (int j = 0; j < list.size(); j++) {
            for (int k = 0; k < list.get(j).data.size(); k++) {
                sid.put(list.get(j).data.get(k).sid);
                num.put(list.get(j).data.get(k).num);
                price.put(list.get(j).data.get(k).price);
                total.put(list.get(j).data.get(k).count_price);
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
                            .put("total",total)
                            .put("coupon_id", coupon_id)
                            .put("price", price)
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
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    if (totalPriceOrderGoodsReadyBuy != null) {
                                        String a = "¥" + jsonObject.getJSONObject("data").getString("total");
                                        totalPriceOrderGoodsReadyBuy.setText(a);
                                        priceOrderGoodsReadyBuy.setCurrentNumber(jsonObject.getJSONObject("data").getInt("total"));
                                    }
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Shopping Cart-0");
                                e.printStackTrace();
                            }
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
                                    tipOrderGoodsReadyBuy.setText(jsonObject.getJSONObject("data").getString("contents"));
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
     * 提交订单
     */
    public void submitOrder() {
        if (StringUtils.isEmpty(date) || StringUtils.isEmpty(penalSum)) {
            ToastUtils.showShortToast(getBaseActivityContext(), "请选择送达时效或违约金比例");
            return;
        }
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();

        JSONArray price = new JSONArray();
        for (int j = 0; j < listOrderGoodsReadyBuy.getShoppingCartOrderlist().size(); j++) {
            for (int k = 0; k < listOrderGoodsReadyBuy.getShoppingCartOrderlist().get(j).data.size(); k++) {
                price.put(listOrderGoodsReadyBuy.getShoppingCartOrderlist().get(j).data.get(k).price);
            }
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.submitOrder(
                    new JSONObject()
                            .put("number", number)//商品数量
                            .put("total", totalPrice)//商品价格
                            .put("coupon_id", coupon_id)//优惠券ID
                            .put("delivery_time", date)//送达时长/预约时间
                            .put("damages", penalSum)//	违约金比例
                            .put("remark", other)//备注
                            .put("type", "1")//1车品2车服
                            .put("price", price)
                            .put("shop_id", shop_id)//店铺ID
                            .put("member_id", Config.member_id)//用户Id
                            .put("mobile", mobile)
                            .put("recipients", recipients)//用户
                            .put("address", address)//备注
                            .put("status", "1")//0草稿箱，1未付款，2已付款，3已完成，4已取消，5已退款，6已删除
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
                                    if (getIntent() == null || getIntent().getExtras() == null) {
                                        startActivity(new Intent(getBaseActivityContext(), NewOrderBuyer.class));
                                    } else {
                                        if (!"fromActivityOrderSynthesize".equals(getIntent().getExtras().getString("where"))) {
                                            startActivity(new Intent(getBaseActivityContext(), NewOrderBuyer.class));
                                        }
                                    }

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
     * 保存
     */
    public void save_drafts() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();


        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.save_drafts(
                    new JSONObject()
                            .put("number", number)//商品数量
                            .put("total", totalPrice)//商品价格
                            .put("coupon_id", coupon_id)//优惠券ID
                            .put("delivery_time", date)//送达时长/预约时间
                            .put("damages", penalSum)//	违约金比例
                            .put("remark", other)//备注
                            .put("type", "1")//1车品2车服
                            .put("shop_id", shop_id)//店铺ID
                            .put("member_id", Config.member_id)//用户Id
                            .put("mobile", mobile)
                            .put("recipients", recipients)//用户
                            .put("address", address)//备注
                            .put("status", "1")//0草稿箱，1未付款，2已付款，3已完成，4已取消，5已退款，6已删除
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
