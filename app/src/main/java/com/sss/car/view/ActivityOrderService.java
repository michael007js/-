package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.AdressDataChoose.DatePicker;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.EditText.NumberSelectEdit;
import com.blankj.utilcode.customwidget.ZhiFuBaoPasswordStyle.PassWordKeyboard;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.EventBusModel.CarName;
import com.sss.car.EventBusModel.ChangeInfoModel;
import com.sss.car.P;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.ListviewOrderServiceGoodsList;
import com.sss.car.model.CouponModel3;
import com.sss.car.model.IntegrityMoneyModel;
import com.sss.car.model.ShoppingCart;
import com.sss.car.model.ShoppingCart_Data;
import com.sss.car.model.ShoppingCart_Data_Options;
import com.sss.car.utils.MenuDialog;

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

import static com.sss.car.Config.member_id;


/**
 * 车服预购订单
 * Created by leilei on 2017/9/28.
 */

public class ActivityOrderService extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.people_name_activity_order_service)
    TextView peopleNameActivityOrderService;
    @BindView(R.id.mobile_name_activity_order_service)
    TextView mobileNameActivityOrderService;
    @BindView(R.id.car_name_activity_order_service)
    TextView carNameActivityOrderService;
    @BindView(R.id.click_choose_car_activity_order_service)
    LinearLayout clickChooseCarActivityOrderService;
    @BindView(R.id.price_activity_order_service)
    NumberSelectEdit priceActivityOrderService;
    @BindView(R.id.show_coupon_activity_order_service)
    TextView showCouponActivityOrderService;
    @BindView(R.id.click_coupon_activity_order_service)
    LinearLayout clickCouponActivityOrderService;
    @BindView(R.id.show_order_time_activity_order_service)
    TextView showOrderTimeActivityOrderService;
    @BindView(R.id.click_order_time_order_service)
    LinearLayout clickOrderTimeActivityOrderService;
    @BindView(R.id.show_penal_sum_activity_order_service)
    TextView showPenalSumActivityOrderService;
    @BindView(R.id.click_penal_sum_activity_order_service)
    LinearLayout clickPenalSumActivityOrderService;
    @BindView(R.id.show_other_activity_order_service)
    TextView showOtherActivityOrderService;
    @BindView(R.id.click_other_sum_activity_order_service)
    LinearLayout clickOtherSumActivityOrderService;
    @BindView(R.id.click_submit_activity_order_service)
    TextView clickSubmitActivityOrderService;
    @BindView(R.id.activity_order_service)
    LinearLayout activityOrderService;
    @BindView(R.id.tip_activity_order_service)
    TextView tipActivityOrderService;
    @BindView(R.id.total_price_activity_order_service)
    TextView totalPriceActivityOrderService;
    @BindView(R.id.list_activity_order_service)
    ListviewOrderServiceGoodsList listActivityOrderService;


    YWLoadingDialog ywLoadingDialog;

    MenuDialog menuDialog;
    SSS_Adapter sss_adapter;

    List<CouponModel3> list = new ArrayList<>();


    SSS_Adapter integrityMoneyAdapter;
    List<IntegrityMoneyModel> integrityMoneyList = new ArrayList<>();


    DatePicker datePicker;

    String totalPrice;
    String date;
    String other;
    String number;
    String coupon_id;
    String penalSum;
    String shop_id;
    String mobile;
    String recipients;

    List<ShoppingCart> shoppingCartOrderlist = new ArrayList<>();


    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (datePicker != null) {
            datePicker.dismiss();
        }
        datePicker = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (menuDialog != null) {
            menuDialog.clear();
        }
        menuDialog = null;
        if (list != null) {
            list.clear();
        }
        list = null;
        if (integrityMoneyList != null) {
            integrityMoneyList.clear();
        }
        integrityMoneyList = null;
        backTop = null;
        titleTop = null;
        listActivityOrderService = null;
        tipActivityOrderService = null;
        totalPriceActivityOrderService = null;
        peopleNameActivityOrderService = null;
        mobileNameActivityOrderService = null;
        carNameActivityOrderService = null;
        clickChooseCarActivityOrderService = null;
        priceActivityOrderService = null;
        showCouponActivityOrderService = null;
        clickCouponActivityOrderService = null;
        showOrderTimeActivityOrderService = null;
        clickOrderTimeActivityOrderService = null;
        showPenalSumActivityOrderService = null;
        clickPenalSumActivityOrderService = null;
        showOtherActivityOrderService = null;
        clickOtherSumActivityOrderService = null;
        clickSubmitActivityOrderService = null;
        activityOrderService = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_service);
        ButterKnife.bind(this);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传输错误!");
            finish();
        }
        customInit(activityOrderService, false, true, true);
        listActivityOrderService.setListviewOrderServiceGoodsListOperationCallBacn(new ListviewOrderServiceGoodsList.ListviewOrderServiceGoodsListOperationCallBacn() {
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
                        ActivityOrderService.this.totalPrice = String.valueOf(totalPrice);
                        totalPriceActivityOrderService.setText("¥" + totalPrice);
                        priceActivityOrderService.setCurrentNumber(totalPrice);
                    }
                });
            }

            @Override
            public void onTotalCount(int totalCount) {
                number = String.valueOf(totalCount);
            }
        });

        listActivityOrderService.setAddAndSubtractCallBack(new ListviewOrderServiceGoodsList.AddAndSubtractCallBack() {
            @Override
            public void onAddAndSubtract(String number, String sid, List<ShoppingCart> list) {
                try {
                    updateShoppingCart(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAddAndSubtractPrice(String count_price, String sid, List<ShoppingCart> shoppingCartOrderlist) {
                try {
                    updateShoppingCart(shoppingCartOrderlist);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        priceActivityOrderService
                .init(getBaseActivityContext(), true)
                .isNegativeNumber(false)
                .minNumber(0)
                .isLongClick(true)
                .setNumberSelectEditOperationCakkBack(new NumberSelectEdit.NumberSelectEditOperationCakkBack() {
                    @Override
                    public void onAdd(NumberSelectEdit numberSelectEdit, int currentNumber) {
                        totalPrice = String.valueOf(currentNumber);
                        totalPriceActivityOrderService.setText("¥" + currentNumber);
                    }

                    @Override
                    public void onSubtract(NumberSelectEdit numberSelectEdit, int currentNumber) {
                        LogUtils.e(currentNumber);
                        totalPrice = String.valueOf(currentNumber);
                        totalPriceActivityOrderService.setText("¥" + currentNumber);
                    }

                    @Override
                    public void onZero(NumberSelectEdit numberSelectEdit) {
                        totalPrice = String.valueOf(numberSelectEdit.getCurrentNumber());
                        totalPriceActivityOrderService.setText("¥" + numberSelectEdit.getCurrentNumber());
                    }

                    @Override
                    public void onEditChanged(NumberSelectEdit numberSelectEdit, int currentNumber) {
                        totalPrice = String.valueOf(numberSelectEdit.getCurrentNumber());
                        totalPriceActivityOrderService.setText("¥" + numberSelectEdit.getCurrentNumber());
                    }
                });

        if ("ActivityGoodsServiceDetails".equals(getIntent().getExtras().getString("from"))) {
//            clickSubmitActivityOrderService.setText("去支付");
        }
        menuDialog = new MenuDialog(this);
        titleTop.setText("服务预购订单");
        initAdapter();
        getShoppingCartOrder();
        defaultVehicle();
        coupon(false);
        orderAttr(false);
        orderTip();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CarName event) {
        carNameActivityOrderService.setText(event.carName);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeInfoModel event) {
        switch (event.type) {
            case "service_time":
                date = event.msg;
                showOrderTimeActivityOrderService.setText(date);
                showOrderTimeActivityOrderService.setText(date);
                break;
            case "other":
                other = event.msg;
                if (!StringUtils.isEmpty(other)) {
                    showOtherActivityOrderService.setText(other);
                }
                break;
        }
    }

    void initAdapter() {


        /***********************************************************************************************************/
        sss_adapter = new SSS_Adapter<CouponModel3>(getBaseActivityContext(), R.layout.item_dialog_coupons) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, CouponModel3 bean,SSS_Adapter instance) {
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
                                showCouponActivityOrderService.setText(list.get(i).name);
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
            protected void setView(SSS_HolderHelper helper, int position, IntegrityMoneyModel bean,SSS_Adapter instance) {

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
                                showPenalSumActivityOrderService.setText(integrityMoneyList.get(i).name);
                            } else {
                                integrityMoneyList.get(i).is_check = "0";
                            }
                        }
                        integrityMoneyAdapter.setList(integrityMoneyList);
                        break;
                }
            }
        });


    }

    @OnClick({R.id.back_top, R.id.click_choose_car_activity_order_service, R.id.click_coupon_activity_order_service,
            R.id.click_order_time_order_service, R.id.click_penal_sum_activity_order_service,
            R.id.click_other_sum_activity_order_service, R.id.click_submit_activity_order_service})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.click_choose_car_activity_order_service:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataCarGarage.class));
                }
                break;
            case R.id.click_coupon_activity_order_service:
                coupon(true);
                break;
            case R.id.click_order_time_order_service:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityOrderChooseTime.class)
                            .putExtra("type", "service_time"));
                }
//                DatePicker datePicker = new DatePicker(getBaseActivityContext());
//                datePicker.setMaxYear(Integer.valueOf(DateUtils.getNowYear()) + 1);
//                datePicker.setMinYear(Integer.valueOf(DateUtils.getNowYear()));
//                datePicker.setDateListener(new DatePicker.OnDateCListener() {
//                    @Override
//                    public void onDateSelected(String year, String month, String day) {
//                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                        try {
//                            long a = simpleDateFormat.parse(year + "-" + month + "-" + day).getTime();
//                            long b = new Date().getTime();
//                            if (a < b) {
//                                ToastUtils.showShortToast(getBaseActivityContext(), "您选择的时间不能小于当前时间");
//                                return;
//                            }
//                            date = year + "-" + month + "-" + day;
//                            showOrderTimeActivityOrderService.setText(date);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                datePicker.show();
                break;
            case R.id.click_penal_sum_activity_order_service:
                orderAttr(true);
                break;
            case R.id.click_other_sum_activity_order_service:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                            .putExtra("type", "other")
                            .putExtra("canChange", true)
                            .putExtra("extra", ""));
                }
                break;
            case R.id.click_submit_activity_order_service:
//                if ("ActivityGoodsServiceDetails".equals(getIntent().getExtras().getString("from"))) {
//
//                } else {
//
//                }
                if (StringUtils.isEmpty(date) || StringUtils.isEmpty(penalSum)) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请选择预约时间或违约金比例");
                    return;
                }
                submitOrder();
                break;
        }
    }


    /**
     * 获取订单列表
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
                            .put("member_id", member_id)
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
                                        listActivityOrderService.setList(ActivityOrderService.this, shoppingCartOrderlist, false);
                                        listActivityOrderService.totalPrice();
                                        listActivityOrderService.totalCount();
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
     * 获取用户默认车辆
     */
    public void defaultVehicle() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.defaultVehicle(
                    new JSONObject()
                            .put("member_id", member_id)
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
                                    peopleNameActivityOrderService.setText(jsonObject.getJSONObject("data").getString("username"));
                                    mobileNameActivityOrderService.setText(jsonObject.getJSONObject("data").getString("mobile"));
                                    carNameActivityOrderService.setText(jsonObject.getJSONObject("data").getString("vehicle_name"));
                                    mobile = jsonObject.getJSONObject("data").getString("mobile");
                                    recipients = jsonObject.getJSONObject("data").getString("username");
                                } else {
                                    recipients = null;
                                    mobile = null;
                                    peopleNameActivityOrderService.setText("");
                                    mobileNameActivityOrderService.setText("");
                                    carNameActivityOrderService.setText("");
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
                                    .put("member_id", member_id)
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
                                .put("member_id", member_id)
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
                if ("1".equals(couponModel3.is_check)) {
                    showCouponActivityOrderService.setText(couponModel3.name);
                    coupon_id = couponModel3.id;
                }
            }
        }
    }

    /**
     * 获取违约金比例
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
                                    .put("member_id", member_id)
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
                                .put("member_id", member_id)
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
                    showPenalSumActivityOrderService.setText(integrityMoneyModel.name);
                }
                integrityMoneyList.add(integrityMoneyModel);
                penalSum = integrityMoneyModel.name;
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
                            .put("member_id", member_id)
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
                                    tipActivityOrderService.setText(jsonObject.getJSONObject("data").getString("contents"));
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

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();


        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.submitOrder(
                    new JSONObject()
                            .put("address_id", "0")//占位符
                            .put("number", number)//商品数量
                            .put("total", totalPrice)//商品数量
                            .put("coupon_id", coupon_id)//优惠券ID
                            .put("delivery_time", date)//送达时长/预约时间
                            .put("damages", penalSum)//	违约金比例
                            .put("remark", other)//备注
                            .put("type", "2")//1车品2车服
                            .put("shop_id", shop_id)//店铺ID
                            .put("member_id", member_id)//用户Id
                            .put("mobile", mobile)
                            .put("recipients", recipients)//用户
                            .put("vehicle_name", carNameActivityOrderService.getText().toString())//备注
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
                                    final String order_id = jsonObject.getJSONObject("data").getString("order_id");


                                    menuDialog.createPaymentDialog(ywLoadingDialog, "",111,1, getBaseActivity(), new MenuDialog.OnPaymentCallBack() {
                                        @Override
                                        public void onVerificationPassword(final String password, final PassWordKeyboard passWordKeyboard, final String payMode, final String is_integral,final com.rey.material.app.BottomSheetDialog bottomSheetDialog) {
                                            P.r(ywLoadingDialog, member_id, password, getBaseActivity(), new P.r() {
                                                @Override
                                                public void match() {
                                                    bottomSheetDialog.dismiss();
                                                    passWordKeyboard.setStatus(true);
                                                    try {
                                                        payment_order(order_id, is_integral,payMode);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                @Override
                                                public void mismatches() {
                                                    passWordKeyboard.setStatus(false);
                                                }
                                            });
                                        }
                                    });

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


    void payment_order(String order_id, String is_integral, String mode) throws JSONException {
        new RequestModel(System.currentTimeMillis() + "", RequestWeb.payment_order(
                new JSONObject()
                        .put("member_id", member_id)
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
    }

}
