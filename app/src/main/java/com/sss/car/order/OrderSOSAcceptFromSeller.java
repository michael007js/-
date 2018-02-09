package com.sss.car.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.EditText.NumberSelectEdit;
import com.blankj.utilcode.customwidget.JingDongCountDownView.SecondDownTimerView;
import com.blankj.utilcode.customwidget.JingDongCountDownView.base.OnCountDownTimerListener;
import com.blankj.utilcode.customwidget.ListView.HorizontalListView;
import com.blankj.utilcode.customwidget.ZhiFuBaoPasswordStyle.PassWordKeyboard;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.rey.material.app.BottomSheetDialog;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedOrderModel;
import com.sss.car.P;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.dao.OnIntegralCallBack;
import com.sss.car.dao.OnPayPasswordVerificationCallBack;
import com.sss.car.gaode.LocationConfig;
import com.sss.car.utils.MenuDialog;
import com.sss.car.utils.PayUtils;
import com.sss.car.view.ActivityImages;
import com.sss.car.view.ActivityMyDataSetPassword;
import com.sss.car.view.ActivityUserInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


/**
 * SOS订单卖家接单页面
 * Created by leilei on 2017/10/19.
 */

public class OrderSOSAcceptFromSeller extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.people_name_order_sos_accept_from_seller)
    TextView peopleNameOrderSOSAcceptFromSeller;
    @BindView(R.id.mobile_name_order_sos_accept_from_seller)
    TextView mobileNameOrderSOSAcceptFromSeller;
    @BindView(R.id.car_order_sos_accept_from_seller)
    TextView car_order_sos_accept_from_seller;
    @BindView(R.id.help_type_order_sos_accept_from_seller)
    TextView helpTypeOrderSOSAcceptFromSeller;
    @BindView(R.id.show_address_order_sos_accept_from_seller)
    TextView showAddressOrderSOSAcceptFromSeller;
    @BindView(R.id.NumberSelectEdit_order_sos_accept_from_seller)
    NumberSelectEdit NumberSelectEditOrderSOSAcceptFromSeller;
    @BindView(R.id.show_service_time_order_sos_accept_from_seller)
    TextView showServiceTimeOrderSOSAcceptFromSeller;
    @BindView(R.id.click_service_time_order_sos_accept_from_seller)
    LinearLayout clickServiceTimeOrderSOSAcceptFromSeller;
    @BindView(R.id.show_penal_sum_order_sos_accept_from_seller)
    TextView showPenalSumOrderSOSAcceptFromSeller;
    @BindView(R.id.click_penal_sum_order_sos_accept_from_seller)
    LinearLayout clickPenalSumOrderSOSAcceptFromSeller;
    @BindView(R.id.show_other_order_sos_accept_from_seller)
    TextView showOtherOrderSOSAcceptFromSeller;
    @BindView(R.id.click_other_sum_order_sos_accept_from_seller)
    LinearLayout clickOtherSumOrderSOSAcceptFromSeller;
    @BindView(R.id.photo_order_sos_accept_from_seller)
    HorizontalListView photoOrderSOSAcceptFromSeller;
    @BindView(R.id.tip_order_sos_accept_from_seller)
    TextView tipOrderSOSAcceptFromSeller;
    @BindView(R.id.total_price_order_sos_accept_from_seller)
    TextView totalPriceOrderSOSAcceptFromSeller;
    @BindView(R.id.click_submit_order_sos_accept_from_seller)
    TextView clickSubmitOrderSOSAcceptFromSeller;
    @BindView(R.id.order_sos_accept_from_seller)
    LinearLayout OrderSOSAcceptFromSeller;

    YWLoadingDialog ywLoadingDialog;
    @BindView(R.id.fault_order_sos_accept_from_seller)
    TextView faultOrderSosAcceptFromSeller;
    @BindView(R.id.count_down_order_sos_accept_from_seller)
    SecondDownTimerView countDownOrderSosAcceptFromSeller;
    MenuDialog menuDialog;

    List<String> list = new ArrayList<>();

    SSS_Adapter sss_adapter;

    String friend_id, nikename;

    boolean canClick = false;

    LocationConfig locationConfig;

    Double sendLai = 0.0, sendLng = 0.0;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (locationConfig != null) {
            locationConfig.release();
        }
        locationConfig = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        backTop = null;
        titleTop = null;
        rightButtonTop = null;
        faultOrderSosAcceptFromSeller = null;
        if (countDownOrderSosAcceptFromSeller != null) {
            countDownOrderSosAcceptFromSeller.cancelDownTimer();
        }
        countDownOrderSosAcceptFromSeller = null;
        if (menuDialog != null) {
            menuDialog.clear();
        }
        menuDialog = null;
        peopleNameOrderSOSAcceptFromSeller = null;
        mobileNameOrderSOSAcceptFromSeller = null;
        car_order_sos_accept_from_seller = null;
        helpTypeOrderSOSAcceptFromSeller = null;
        showAddressOrderSOSAcceptFromSeller = null;
        NumberSelectEditOrderSOSAcceptFromSeller = null;
        showServiceTimeOrderSOSAcceptFromSeller = null;
        clickServiceTimeOrderSOSAcceptFromSeller = null;
        showPenalSumOrderSOSAcceptFromSeller = null;
        clickPenalSumOrderSOSAcceptFromSeller = null;
        showOtherOrderSOSAcceptFromSeller = null;
        clickOtherSumOrderSOSAcceptFromSeller = null;
        photoOrderSOSAcceptFromSeller = null;
        tipOrderSOSAcceptFromSeller = null;
        totalPriceOrderSOSAcceptFromSeller = null;
        clickSubmitOrderSOSAcceptFromSeller = null;
        OrderSOSAcceptFromSeller = null;
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedOrderModel changedOrderModel){
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_sos_accept_from_seller);
        ButterKnife.bind(this);
        customInit(OrderSOSAcceptFromSeller, false, true, true);
        titleTop.setText("sos订单");
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传输错误");
            finish();
        }
        APPOftenUtils.underLineOfTextView(rightButtonTop).setTextColor(getResources().getColor(R.color.mainColor));


        if (locationConfig == null) {
            locationConfig = new LocationConfig(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {
                    if (aMapLocation.getErrorCode() == 0) {
                        sendLai = aMapLocation.getLatitude();
                        sendLng = aMapLocation.getLongitude();
                    }
                }
            }, getBaseActivityContext(), LocationConfig.LocationType_Continuous_Positioning);
        }
        locationConfig.start();


        countDownOrderSosAcceptFromSeller.setDownTimerListener(new OnCountDownTimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                ToastUtils.showShortToast(getBaseActivityContext(), "该订单已过期");
                finish();
            }
        });
        NumberSelectEditOrderSOSAcceptFromSeller.init(getBaseActivityContext(), true)
                .defaultNumber(0)
                .maxWidth(100)
                .setNumberSelectEditOperationCakkBack(new NumberSelectEdit.NumberSelectEditOperationCakkBack() {
                    @Override
                    public void onAdd(NumberSelectEdit numberSelectEdit, int currentNumber) {
                        temp=currentNumber;
                        int a = initTotalPrice();
                        if (a != -1) {
                            totalPriceOrderSOSAcceptFromSeller.setText(a + "");
                        }
                    }

                    @Override
                    public void onSubtract(NumberSelectEdit numberSelectEdit, int currentNumber) {
                        temp=currentNumber;
                        int a = initTotalPrice();
                        if (a != -1) {
                            totalPriceOrderSOSAcceptFromSeller.setText(a + "");
                        }
                    }

                    @Override
                    public void onZero(NumberSelectEdit numberSelectEdit) {
                        temp=0;
                        totalPriceOrderSOSAcceptFromSeller.setText(0 + "");
                    }

                    @Override
                    public void onEditChanged(NumberSelectEdit numberSelectEdit, int currentNumber) {
                        temp=currentNumber;
                        int a = initTotalPrice();
                        if (a != -1) {
                            totalPriceOrderSOSAcceptFromSeller.setText(a + "");
                        }
                    }
                });

        initAdapter();
        orderTip();
        get_drafts_SOS();
    }

    @OnClick({R.id.back_top, R.id.right_button_top, R.id.click_submit_order_sos_accept_from_seller})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                if (!StringUtils.isEmpty(rightButtonTop.getText().toString())) {
                    if (getBaseActivityContext() != null) {
                        startActivity(new Intent(getBaseActivityContext(), ActivityUserInfo.class)
                                .putExtra("id", friend_id));
                    }
                }
                break;
            case R.id.click_submit_order_sos_accept_from_seller:

                if (!canClick) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "订单刷新中,请稍后...");
                    return;
                }
                if (showPenalSumOrderSOSAcceptFromSeller.getText().toString().trim().indexOf("%") != -1) {
                    int a = initTotalPrice();
                    if (a != -1) {
                        PayUtils.requestPayment(ywLoadingDialog,friend_id,getIntent().getExtras().getString("sos_id"),1,0, String.valueOf(a),getBaseActivity());
//                        showDialog(initTotalPrice() + "");
                    } else {
                        ToastUtils.showLongToast(getBaseActivityContext(), "服务器数据解析异常-1");
                    }

                } else {
                    consent();

                }

                break;
        }
    }

    String integral = "0";
    String mode = "balance";//支付方式：balance余额，ali_pay支付，we_chat_pay微信支付

    void showDialog(String price) {
        if (menuDialog == null) {
            menuDialog = new MenuDialog(getBaseActivity());
        }
        menuDialog.createPaymentOrderDialog(ywLoadingDialog, "缴纳保证金", price, getBaseActivity(), new OnIntegralCallBack() {
            @Override
            public void onIntegralCallBack(String integral, String mode) {
                OrderSOSAcceptFromSeller.this.integral = integral;
                OrderSOSAcceptFromSeller.this.mode = mode;
            }
        }, new OnPayPasswordVerificationCallBack() {
            @Override
            public void onVerificationPassword(final String password, final PassWordKeyboard passWordKeyboard, final BottomSheetDialog bottomSheetDialog) {

                P.e(ywLoadingDialog, Config.member_id, getBaseActivity(), new P.p() {
                    @Override
                    public void exist() {
                        P.r(ywLoadingDialog, Config.member_id, password, getBaseActivity(), new P.r() {
                            @Override
                            public void match() {
                                passWordKeyboard.setStatus(true);
                                consent();
                            }

                            @Override
                            public void mismatches() {
                                passWordKeyboard.setStatus(false);
                            }
                        });
                    }

                    @Override
                    public void nonexistence() {
                        if (getBaseActivityContext() != null) {
                            getBaseActivityContext().startActivity(new Intent(getBaseActivityContext(), ActivityMyDataSetPassword.class));
                        }
                    }
                });


            }
        });
    }

    int total = 0;
    int temp=0;
//    int payPrice() {
//        if (canClick) {
//            if (showPenalSumOrderSOSAcceptFromSeller.getText().toString().trim().indexOf("%") != -1) {
//                String[] temp = showPenalSumOrderSOSAcceptFromSeller.getText().toString().trim().split("%");
//                try {
//                    if (temp.length > 0) {
//                        String b = temp[0];
//                        int d = Integer.valueOf(b);
//
//                        LogUtils.e(total + "---" + b + "---" + d);
//                        return total * d / 100;
//                    } else {
//                        ToastUtils.showLongToast(getBaseActivityContext(), "服务器数据解析异常-1");
//                        return -1;
//                    }
//                } catch (NumberFormatException e) {
//                    e.printStackTrace();
//                    ToastUtils.showLongToast(getBaseActivityContext(), "服务器数据解析异常-2");
//                    return -1;
//                }
//
//            } else {
//                return Integer.valueOf(NumberSelectEditOrderSOSAcceptFromSeller.getCurrentNumber());
//            }
//
//        } else {
//            return -1;
//        }
//    }

    int initTotalPrice() {
        if (canClick) {
//            if (showPenalSumOrderSOSAcceptFromSeller.getText().toString().trim().indexOf("%") != -1) {
//                String[] temp = showPenalSumOrderSOSAcceptFromSeller.getText().toString().trim().split("%");
//                try {
//                    if (temp.length > 0) {
//                        return Integer.valueOf(temp[0]) * NumberSelectEditOrderSOSAcceptFromSeller.getCurrentNumber() / 100 + NumberSelectEditOrderSOSAcceptFromSeller.getCurrentNumber();
//                    } else {
//
//                        ToastUtils.showLongToast(getBaseActivityContext(), "服务器数据解析异常-1");
//                        return -1;
//                    }
//                } catch (NumberFormatException e) {
//                    ToastUtils.showLongToast(getBaseActivityContext(), "服务器数据解析异常-2");
//                    return -1;
//                }
//
//            } else {
//                return Integer.valueOf(NumberSelectEditOrderSOSAcceptFromSeller.getCurrentNumber());
//            }
            if (showPenalSumOrderSOSAcceptFromSeller.getText().toString().trim().indexOf("%") != -1) {
                String[] temp = showPenalSumOrderSOSAcceptFromSeller.getText().toString().trim().split("%");
                try {
                    if (temp.length > 0) {
                        String b = temp[0];
                        int d = Integer.valueOf(b);

                        LogUtils.e(temp + "---" + b + "---" + d);
                        return this.temp * d / 100;
                    } else {
                        ToastUtils.showLongToast(getBaseActivityContext(), "服务器数据解析异常-1");
                        return -1;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    ToastUtils.showLongToast(getBaseActivityContext(), "服务器数据解析异常-2");
                    return -1;
                }

            } else {
                return Integer.valueOf(NumberSelectEditOrderSOSAcceptFromSeller.getCurrentNumber());
            }

        } else {
            return -1;
        }
    }


    void initAdapter() {
        sss_adapter = new SSS_Adapter<String>(getBaseActivityContext(), R.layout.item_image, list) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, String bean, SSS_Adapter instance) {
                if (bean.startsWith("/storage/")) {
                    LogUtils.e(bean);
                    FrescoUtils.showImage(false, 80, 80, Uri.fromFile(new File(bean)), ((SimpleDraweeView) helper.getView(R.id.pic_item_image)), 0f);
                } else {
                    LogUtils.e(Config.url + bean);
                    FrescoUtils.showImage(false, 80, 80, Uri.parse(Config.url + bean), ((SimpleDraweeView) helper.getView(R.id.pic_item_image)), 0f);
                }


            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };
        photoOrderSOSAcceptFromSeller.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (getBaseActivityContext() != null) {
                    List<String> temp = new ArrayList<>();
                    for (int j = 0; j < list.size(); j++) {
                        if (!"default".equals(list.get(j))) {
                            if (list.get(j).startsWith("/storage/")) {
                                temp.add(list.get(j));
                            } else {
                                temp.add(Config.url + list.get(j));
                            }
                        }
                    }
                    startActivity(new Intent(getBaseActivityContext(), ActivityImages.class)
                            .putStringArrayListExtra("data", (ArrayList<String>) temp)
                            .putExtra("current", position));
                }
            }
        });
        photoOrderSOSAcceptFromSeller.setAdapter(sss_adapter);
    }


    /**
     * 获取订单信息
     */
    public void get_drafts_SOS() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.get_drafts_SOS(
                    new JSONObject()
                            .put("sos_id", getIntent().getExtras().getString("sos_id"))
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
                                    canClick = true;
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                    friend_id = jsonObject1.getString("member_id");
                                    peopleNameOrderSOSAcceptFromSeller.setText(jsonObject1.getString("recipients"));
                                    nikename = jsonObject1.getString("recipients");
                                    mobileNameOrderSOSAcceptFromSeller.setText(jsonObject1.getString("mobile"));
                                    car_order_sos_accept_from_seller.setText(jsonObject1.getString("vehicle_name"));
                                    helpTypeOrderSOSAcceptFromSeller.setText(jsonObject1.getString("type"));
                                    showAddressOrderSOSAcceptFromSeller.setText(jsonObject1.getString("address"));
                                    NumberSelectEditOrderSOSAcceptFromSeller.setCurrentNumber(jsonObject1.getInt("price"));
                                    showServiceTimeOrderSOSAcceptFromSeller.setText(jsonObject1.getString("service_time"));
                                    showPenalSumOrderSOSAcceptFromSeller.setText(jsonObject1.getString("damages"));
                                    showOtherOrderSOSAcceptFromSeller.setText(jsonObject1.getString("remark"));
                                    faultOrderSosAcceptFromSeller.setText(jsonObject1.getString("title"));
                                    totalPriceOrderSOSAcceptFromSeller.setText("¥" + jsonObject1.getInt("price"));
                                    temp=jsonObject1.getInt("price");;
                                    total = jsonObject1.getInt("price");
                                    countDownOrderSosAcceptFromSeller.setDownTime(Long.valueOf(jsonObject1.getInt("start_time")) * 1000);
                                    countDownOrderSosAcceptFromSeller.startDownTimer();

                                    JSONArray jsonArray = jsonObject1.getJSONArray("picture");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        list.add(jsonArray.getString(i));
                                    }
                                    if (jsonArray.length() > 0) {
                                        photoOrderSOSAcceptFromSeller.setVisibility(View.VISIBLE);
                                    }
                                    sss_adapter.setList(list);
                                    APPOftenUtils.underLineOfTextView(rightButtonTop).setTextColor(getResources().getColor(R.color.mainColor));
                                    rightButtonTop.setText(jsonObject1.getString("recipients"));
                                    int a = initTotalPrice();
                                    if (a != -1) {
                                        totalPriceOrderSOSAcceptFromSeller.setText(a + "");
                                    }
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
                            .put("article_id", "9")//文章ID (3实物类)
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
                                    tipOrderSOSAcceptFromSeller.setText(jsonObject.getJSONObject("data").getString("contents"));
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
     * 服务商抢SOS单
     */
    public void consent() {
        if (sendLai == 0.0 || sendLng == 0.0) {
            ToastUtils.showShortToast(getBaseActivityContext(), "GPS获取中,请稍后...");
            return;
        }
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.consent(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("price", NumberSelectEditOrderSOSAcceptFromSeller.getCurrentNumber())
                            .put("sos_id", getIntent().getExtras().getString("sos_id"))
                            .put("lat", sendLai)
                            .put("lng", sendLng)
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
//                                    if (getBaseActivityContext() != null) {
//                                        startActivity(new Intent(getBaseActivityContext(), OrderSOSGrabList.class)
//                                                .putExtra("sos_id", getIntent().getExtras().getString("sos_id")));
//                                    }
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
