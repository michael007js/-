package com.sss.car.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.blankj.utilcode.customwidget.ListView.HorizontalListView;
import com.blankj.utilcode.customwidget.ZhiFuBaoPasswordStyle.PassWordKeyboard;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.rey.material.app.BottomSheetDialog;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedOrderModel;
import com.sss.car.EventBusModel.ChangedSOSHelperList;
import com.sss.car.EventBusModel.CloseSOSGrabList;
import com.sss.car.P;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.dao.OnIntegralCallBack;
import com.sss.car.dao.OnPayPasswordVerificationCallBack;
import com.sss.car.gaode.LocationConfig;
import com.sss.car.utils.MenuDialog;
import com.sss.car.utils.PayUtils;
import com.sss.car.view.ActivityImages;

import org.greenrobot.eventbus.EventBus;
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

import static com.sss.car.Config.member_id;


/**
 * SOS订单买家付款页面
 * Created by leilei on 2017/10/21.
 */

public class OrderSOSAcceptFromBuyer extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.people_name_order_sos_accept_from_buyer)
    TextView peopleNameOrderSosAcceptFromBuyer;
    @BindView(R.id.mobile_name_order_sos_accept_from_buyer)
    TextView mobileNameOrderSosAcceptFromBuyer;
    @BindView(R.id.car_order_sos_accept_from_buyer)
    TextView carOrderSosAcceptFromBuyer;
    @BindView(R.id.fault_order_sos_accept_from_buyer)
    TextView faultOrderSosAcceptFromBuyer;
    @BindView(R.id.count_down_order_sos_accept_from_buyer)
    SecondDownTimerView countDownOrderSosAcceptFromBuyer;
    @BindView(R.id.help_type_order_sos_accept_from_buyer)
    TextView helpTypeOrderSosAcceptFromBuyer;
    @BindView(R.id.show_address_order_sos_accept_from_buyer)
    TextView showAddressOrderSosAcceptFromBuyer;
    @BindView(R.id.NumberSelectEdit_order_sos_accept_from_buyer)
    NumberSelectEdit NumberSelectEditOrderSosAcceptFromBuyer;
    @BindView(R.id.show_service_time_order_sos_accept_from_buyer)
    TextView showServiceTimeOrderSosAcceptFromBuyer;
    @BindView(R.id.click_service_time_order_sos_accept_from_buyer)
    LinearLayout clickServiceTimeOrderSosAcceptFromBuyer;
    @BindView(R.id.show_penal_sum_order_sos_accept_from_buyer)
    TextView showPenalSumOrderSosAcceptFromBuyer;
    @BindView(R.id.click_penal_sum_order_sos_accept_from_buyer)
    LinearLayout clickPenalSumOrderSosAcceptFromBuyer;
    @BindView(R.id.show_other_order_sos_accept_from_buyer)
    TextView showOtherOrderSosAcceptFromBuyer;
    @BindView(R.id.click_other_sum_order_sos_accept_from_buyer)
    LinearLayout clickOtherSumOrderSosAcceptFromBuyer;
    @BindView(R.id.photo_order_sos_accept_from_buyer)
    HorizontalListView photoOrderSosAcceptFromBuyer;
    @BindView(R.id.tip_order_sos_accept_from_buyer)
    TextView tipOrderSosAcceptFromBuyer;
    @BindView(R.id.total_price_order_sos_accept_from_buyer)
    TextView totalPriceOrderSosAcceptFromBuyer;
    @BindView(R.id.cancel_submit_order_sos_accept_from_buyer)
    TextView cancelSubmitOrderSosAcceptFromBuyer;
    @BindView(R.id.click_submit_order_sos_accept_from_buyer)
    TextView clickSubmitOrderSosAcceptFromBuyer;
    @BindView(R.id.order_sos_accept_from_buyer)
    LinearLayout orderSosAcceptFromBuyer;

    YWLoadingDialog ywLoadingDialog;

    SSS_Adapter sss_adapter;
    List<String> list = new ArrayList<>();

    MenuDialog menuDialog;
    LocationConfig locationConfig;

    double sendLat;
    double sendLng;
    String consent_id;
    boolean can;
int total=0;
    String friend_id;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (locationConfig != null) {
            locationConfig.release();
        }
        locationConfig = null;
        backTop = null;
        titleTop = null;
        rightButtonTop = null;
        peopleNameOrderSosAcceptFromBuyer = null;
        mobileNameOrderSosAcceptFromBuyer = null;
        carOrderSosAcceptFromBuyer = null;
        faultOrderSosAcceptFromBuyer = null;
        countDownOrderSosAcceptFromBuyer = null;
        helpTypeOrderSosAcceptFromBuyer = null;
        showAddressOrderSosAcceptFromBuyer = null;
        NumberSelectEditOrderSosAcceptFromBuyer = null;
        showServiceTimeOrderSosAcceptFromBuyer = null;
        clickServiceTimeOrderSosAcceptFromBuyer = null;
        showPenalSumOrderSosAcceptFromBuyer = null;
        clickPenalSumOrderSosAcceptFromBuyer = null;
        showOtherOrderSosAcceptFromBuyer = null;
        clickOtherSumOrderSosAcceptFromBuyer = null;
        photoOrderSosAcceptFromBuyer = null;
        tipOrderSosAcceptFromBuyer = null;
        totalPriceOrderSosAcceptFromBuyer = null;
        cancelSubmitOrderSosAcceptFromBuyer = null;
        clickSubmitOrderSosAcceptFromBuyer = null;
        orderSosAcceptFromBuyer = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_sos_accept_from_buyer);
        ButterKnife.bind(this);
        customInit(orderSosAcceptFromBuyer, false, true, true);
        NumberSelectEditOrderSosAcceptFromBuyer.init(getBaseActivityContext(), false)
                .maxWidth(100)
                .defaultNumber(0)
                .isNegativeNumber(false);
        if (locationConfig == null) {
            locationConfig = new LocationConfig(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {
                    if (aMapLocation.getErrorCode() == 0) {
                        sendLat = aMapLocation.getLatitude();
                        sendLng = aMapLocation.getLongitude();
                    }
                }
            }, getBaseActivityContext(), LocationConfig.LocationType_Continuous_Positioning);
        }
        locationConfig.start();
        titleTop.setText("sos订单反馈");
        initAdapter();
        orderTip();
        getSOSSellerDetails();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedOrderModel changedOrderModel){
        finish();
    }

    void initAdapter() {
        sss_adapter = new SSS_Adapter<String>(getBaseActivityContext(), R.layout.item_image, list) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, String bean,SSS_Adapter instance) {
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
        photoOrderSosAcceptFromBuyer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        photoOrderSosAcceptFromBuyer.setAdapter(sss_adapter);
    }

    @OnClick({R.id.back_top, R.id.right_button_top, R.id.cancel_submit_order_sos_accept_from_buyer, R.id.click_submit_order_sos_accept_from_buyer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                break;
            case R.id.click_submit_order_sos_accept_from_buyer:
                if (StringUtils.isEmpty(showPenalSumOrderSosAcceptFromBuyer.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "订单刷新中,请稍后...");
                    return;
                }
//                showDialog(NumberSelectEditOrderSosAcceptFromBuyer.getCurrentNumber() + "");

                PayUtils.requestPayment(ywLoadingDialog,false,friend_id,getIntent().getExtras().getString("sos_id"),1,0,total+"",getBaseActivity(),null,"0");
                break;
            case R.id.cancel_submit_order_sos_accept_from_buyer:
                if (can) {
                    neglect_SOS();
                } else {
                    ToastUtils.showLongToast(getBaseActivityContext(), "订单刷新中,请稍后...");
                }
                break;
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
                                    tipOrderSosAcceptFromBuyer.setText(jsonObject.getJSONObject("data").getString("contents"));
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
     * 获取SOS信息
     */
    public void getSOSSellerDetails() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getSOSSellerDetails(
                    new JSONObject()
                            .put("friend_id", getIntent().getExtras().getString("friend_id"))
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
                                    friend_id=jsonObject.getJSONObject("data").getString("friend_id");
                                    peopleNameOrderSosAcceptFromBuyer.setText(jsonObject.getJSONObject("data").getString("friend_name"));
                                    mobileNameOrderSosAcceptFromBuyer.setText(jsonObject.getJSONObject("data").getString("mobile"));
                                    carOrderSosAcceptFromBuyer.setText(jsonObject.getJSONObject("data").getString("vehicle_name"));
                                    showAddressOrderSosAcceptFromBuyer.setText(jsonObject.getJSONObject("data").getString("address"));
                                    helpTypeOrderSosAcceptFromBuyer.setText(jsonObject.getJSONObject("data").getString("type"));
                                    faultOrderSosAcceptFromBuyer.setText(jsonObject.getJSONObject("data").getString("title"));
                                    totalPriceOrderSosAcceptFromBuyer.setText("¥" + jsonObject.getJSONObject("data").getInt("price"));
                                    total=jsonObject.getJSONObject("data").getInt("price");
                                    NumberSelectEditOrderSosAcceptFromBuyer.setCurrentNumber(jsonObject.getJSONObject("data").getInt("price"));
                                    showServiceTimeOrderSosAcceptFromBuyer.setText(jsonObject.getJSONObject("data").getString("service_time"));
                                    showPenalSumOrderSosAcceptFromBuyer.setText(jsonObject.getJSONObject("data").getString("damages"));
                                    showOtherOrderSosAcceptFromBuyer.setText(jsonObject.getJSONObject("data").getString("remark"));
                                    consent_id = jsonObject.getJSONObject("data").getString("consent_id");
                                    can = true;
                                    JSONArray picture = jsonObject.getJSONObject("data").getJSONArray("picture");

                                    for (int i = 0; i < picture.length(); i++) {
                                        list.add(picture.getString(i));
                                    }
                                    if (list.size() > 0) {
                                        photoOrderSosAcceptFromBuyer.setVisibility(View.VISIBLE);
                                        sss_adapter.setList(list);
                                    }
                                    countDownOrderSosAcceptFromBuyer.setDownTime(Long.valueOf(jsonObject.getJSONObject("data").getInt("start_time")) * 1000);
                                    countDownOrderSosAcceptFromBuyer.startDownTimer();
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
     * 求助者同意SOS单(SOS订单建立)
     *
     * @throws JSONException
     */
    void payment_order(final String sos_id, final String price, final String friend_id) {
        try {
            new RequestModel(System.currentTimeMillis() + "", RequestWeb.payment_orderSOS(
                    new JSONObject()
                            .put("member_id", member_id)
                            .put("sos_id",sos_id)
                            .put("friend_id", friend_id)
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
                                    PayUtils.requestPayment(ywLoadingDialog,false,friend_id,sos_id,1,0,price,getBaseActivity(),null,"0");
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
            e.printStackTrace();
        }
    }
    /**
     * 忽略增援的SOS订单
     */
    public void neglect_SOS() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.neglect_SOS(
                    new JSONObject()
                            .put("consent_id", consent_id)
                            .put("member_id", Config.member_id)
                            .put("sos_id", getIntent().getExtras().getString("sos_id"))
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
                                    EventBus.getDefault().post(new ChangedSOSHelperList(consent_id));
                                    finish();
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
