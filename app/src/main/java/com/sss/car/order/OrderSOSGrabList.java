package com.sss.car.order;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.JingDongCountDownView.SecondDownTimerView;
import com.blankj.utilcode.customwidget.JingDongCountDownView.base.OnCountDownTimerListener;
import com.blankj.utilcode.customwidget.Layout.SwipeMenuLayout;
import com.blankj.utilcode.dao.OnAskDialogCallBack;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedOrderModel;
import com.sss.car.EventBusModel.ChangedSOSHelperList;
import com.sss.car.EventBusModel.CloseSOSGrabList;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.gaode.LocationConfig;
import com.sss.car.model.OrderSOSGrabModel;
import com.sss.car.utils.MenuDialog;
import com.sss.car.utils.PayUtils;

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

import static com.sss.car.Config.member_id;

/**
 * 用户发布SOS订单之后弹出的抢单列表
 * Created by leilei on 2017/10/20.
 */

public class OrderSOSGrabList extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.listview_order_sos_grab_list)
    ListView listviewOrderSosGrabList;
    @BindView(R.id.order_sos_grab_list)
    LinearLayout orderSosGrabList;
    YWLoadingDialog ywLoadingDialog;
    List<OrderSOSGrabModel> list = new ArrayList<>();

    SSS_Adapter sss_adapter;
    @BindView(R.id.count_down_order_sos_accept_from_seller)
    SecondDownTimerView countDownOrderSosAcceptFromSeller;

    boolean canOperation = true;

    LocationConfig locationConfig;
    MenuDialog menuDialog;
    double sendLat;
    double sendLng;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    public void changeList(String consent_id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).consent_id.equals(consent_id)) {
                list.remove(i);
            }
        }
        if (list.size() > 0) {
            sss_adapter.setList(list);
        } else {
            listviewOrderSosGrabList.removeAllViewsInLayout();
        }
    }

    @Override
    protected void onDestroy() {
        if (list != null) {
            list.clear();
        }
        list = null;
        if (locationConfig != null) {
            locationConfig.release();
        }
        locationConfig = null;
        if (menuDialog != null) {
            menuDialog.clear();
        }
        menuDialog = null;
        if (sss_adapter != null) {
            sss_adapter.clear();
        }
        sss_adapter = null;
        if (countDownOrderSosAcceptFromSeller != null) {
            countDownOrderSosAcceptFromSeller.cancelDownTimer();
        }
        countDownOrderSosAcceptFromSeller = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        backTop = null;
        titleTop = null;
        listviewOrderSosGrabList = null;
        orderSosGrabList = null;
        super.onDestroy();
    }

    @Subscribe
    public void onMessageEvent(OrderSOSGrabModel orderSOSGrabModel) {
        LogUtils.e(list.size());
        list.add(orderSOSGrabModel);
        sss_adapter.setList(list);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CloseSOSGrabList event) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedSOSHelperList event) {
        changeList(event.consent_id);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedOrderModel changedOrderModel) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_sos_grab_list);
        ButterKnife.bind(this);
        customInit(orderSosGrabList, false, true, true);
        titleTop.setText("抢单者");
        APPOftenUtils.underLineOfTextView(rightButtonTop).setTextColor(getResources().getColor(R.color.mainColor));
        rightButtonTop.setText("取消");
        rightButtonTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APPOftenUtils.createAskDialog(getBaseActivityContext(), "是否确定取消该订单？", new OnAskDialogCallBack() {
                    @Override
                    public void onOKey(Dialog dialog) {
                        dialog.dismiss();
                        dialog = null;
                        cancel_AllSOS();
                    }

                    @Override
                    public void onCancel(Dialog dialog) {

                    }
                });
            }
        });
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
        countDownOrderSosAcceptFromSeller.setDownTimerListener(new OnCountDownTimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                canOperation = false;
                ToastUtils.showShortToast(getBaseActivityContext(), "您发布的SOS订单已过期");
                finish();
            }
        });
        consent_list();
        initAdapter();
    }

    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }

    void initAdapter() {
        sss_adapter = new SSS_Adapter<OrderSOSGrabModel>(getBaseActivityContext(), R.layout.item_sos_grab_adapter, list) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, OrderSOSGrabModel bean, SSS_Adapter instance) {
                helper.setText(R.id.name_item_sos_grab_adapter, bean.username);
                helper.setText(R.id.mobile_item_sos_grab_adapter, bean.mobile);
                helper.setText(R.id.price_item_sos_grab_adapter, bean.price);
                helper.setText(R.id.date_item_sos_grab_adapter, bean.create_time);
                helper.setText(R.id.prestige_item_sos_grab_adapter, bean.credit);
                helper.setText(R.id.distance_item_sos_grab_adapter, ConvertUtils.gpsToDistanceKM(Double.valueOf(Config.latitude), Double.valueOf(Config.longitude), Double.valueOf(bean.lat), Double.valueOf(bean.lng)) + "km");
                FrescoUtils.showImage(false, 60, 60, Uri.parse(Config.url + bean.face), ((SimpleDraweeView) helper.getView(R.id.pic_item_sos_grab_adapter)), 0f);
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
                helper.setItemChildClickListener(R.id.payment_item_sos_grab_adapter);
                helper.setItemChildClickListener(R.id.cancel_item_sos_grab_adapter);
                helper.setItemChildClickListener(R.id.click_item_sos_grab_adapter);

            }
        };
        sss_adapter.setOnItemListener(new SSS_OnItemListener() {
            @Override
            public void onItemChildClick(View view, final int position, SSS_HolderHelper holder) {
                ((SwipeMenuLayout) holder.getView(R.id.scoll_new_friend_item)).smoothClose();
                switch (view.getId()) {
                    case R.id.payment_item_sos_grab_adapter:
                        if (canOperation) {
                            if (sendLat == 0.0 || sendLng == 0.0) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "GPS获取中,请稍后...");
                                return;
                            }
                            if (menuDialog == null) {
                                menuDialog = new MenuDialog(getBaseActivity());
                            }
                            PayUtils.requestPayment(ywLoadingDialog, list.get(position).member_id, list.get(position).sos_id, 1, 0, list.get(position).price, getBaseActivity());
                        } else {
                            ToastUtils.showLongToast(getBaseActivityContext(), "该订单已过期");
                        }
                        break;
                    case R.id.cancel_item_sos_grab_adapter:
                        if (canOperation) {
                            neglect_SOS(list.get(position).consent_id);
                        } else {
                            ToastUtils.showLongToast(getBaseActivityContext(), "该订单已过期");
                        }
                        break;
                    case R.id.click_item_sos_grab_adapter:
                        if (getBaseActivityContext() != null) {
                            startActivity(new Intent(getBaseActivityContext(), OrderSOSAcceptFromBuyer.class)
                                    .putExtra("friend_id", list.get(position).member_id)
                                    .putExtra("sos_id", list.get(position).sos_id));
                        }
                        break;
                }
            }
        });
        listviewOrderSosGrabList.setAdapter(sss_adapter);

    }


    /**
     * 服务商抢SOS单列表
     */
    public void consent_list() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.consent_list(
                    new JSONObject()
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
                                    if (jsonObject.getJSONObject("data").has("consent_data")) {
                                        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("consent_data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            OrderSOSGrabModel grabModel = new OrderSOSGrabModel();
                                            grabModel.consent_id = jsonArray.getJSONObject(i).getString("consent_id");
                                            grabModel.lng = jsonArray.getJSONObject(i).getString("lng");
                                            grabModel.lat = jsonArray.getJSONObject(i).getString("lat");
                                            grabModel.damages = jsonArray.getJSONObject(i).getString("damages");
                                            grabModel.price = jsonArray.getJSONObject(i).getString("price");
                                            grabModel.money = jsonArray.getJSONObject(i).getString("money");
                                            grabModel.create_time = jsonArray.getJSONObject(i).getString("create_time");
                                            grabModel.sos_id = jsonArray.getJSONObject(i).getString("sos_id");
                                            grabModel.member_id = jsonArray.getJSONObject(i).getString("member_id");
                                            grabModel.shop_id = jsonArray.getJSONObject(i).getString("shop_id");
                                            grabModel.mobile = jsonArray.getJSONObject(i).getString("mobile");
                                            grabModel.face = jsonArray.getJSONObject(i).getString("face");
                                            grabModel.username = jsonArray.getJSONObject(i).getString("username");
                                            grabModel.credit = jsonArray.getJSONObject(i).getString("credit");
                                            list.add(grabModel);
                                        }
                                    }


                                    sss_adapter.setList(list);
                                    countDownOrderSosAcceptFromSeller.setDownTime(Long.valueOf(jsonObject.getJSONObject("data").getInt("start_time")) * 1000);
                                    countDownOrderSosAcceptFromSeller.startDownTimer();
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
     * 求助者列表中取消SOS订单
     */
    public void neglect_SOS(final String consent_id) {
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
                                    changeList(consent_id);
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

    public void cancel_AllSOS() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.cancel_AllSOS(
                    new JSONObject()
                            .put("type", "1")//1求助订单,2增援订单
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

    /**
     * 求助者同意SOS单(SOS订单建立)
     *
     * @throws JSONException
     */
    void payment_order(final String sos_id, final String price, String friend_id) {
        try {
            new RequestModel(System.currentTimeMillis() + "", RequestWeb.payment_orderSOS(
                    new JSONObject()
                            .put("member_id", member_id)
                            .put("sos_id", sos_id)
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


}
