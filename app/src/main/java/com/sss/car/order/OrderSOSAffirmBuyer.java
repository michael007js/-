package com.sss.car.order;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.ListView.HorizontalListView;
import com.blankj.utilcode.dao.OnAskDialogCallBack;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedMessageOrderList;
import com.sss.car.EventBusModel.ChangedSOSList;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.utils.MenuDialog;
import com.sss.car.view.ActivityImages;

import org.greenrobot.eventbus.EventBus;
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
 * SOS发布者确认服务商完成服务页
 * Created by leilei on 2017/10/19.
 */

public class OrderSOSAffirmBuyer extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.people_name_order_sos_affirm_buyer)
    TextView peopleNameOrderOrderSOSAffirmBuyer;
    @BindView(R.id.mobile_name_order_sos_affirm_buyer)
    TextView mobileNameOrderOrderSOSAffirmBuyer;
    @BindView(R.id.car_order_sos_affirm_buyer)
    TextView car_order_sos_affirm_buyer;
    @BindView(R.id.help_type_order_sos_affirm_buyer)
    TextView helpTypeOrderOrderSOSAffirmBuyer;
    @BindView(R.id.show_address_order_sos_affirm_buyer)
    TextView showAddressOrderOrderSOSAffirmBuyer;
    @BindView(R.id.price_order_sos_affirm_buyer)
    TextView priceOrderSOSDetails;
    @BindView(R.id.show_service_time_order_sos_affirm_buyer)
    TextView showServiceTimeOrderOrderSOSAffirmBuyer;
    @BindView(R.id.show_penal_sum_order_sos_affirm_buyer)
    TextView penalSumOrderOrderSOSAffirmBuyer;
    @BindView(R.id.show_other_order_sos_affirm_buyer)
    TextView showOtherOrderOrderSOSAffirmBuyer;
    @BindView(R.id.photo_order_sos_affirm_buyer)
    HorizontalListView photoOrderOrderSOSAffirmBuyer;
    @BindView(R.id.tip_order_sos_affirm_buyer)
    TextView tipOrderOrderSOSAffirmBuyer;
    @BindView(R.id.order_sos_affirm_buyer)
    LinearLayout OrderOrderSOSAffirmBuyer;

    YWLoadingDialog ywLoadingDialog;
    @BindView(R.id.fault_order_sos_affirm_buyer)
    TextView faultOrderOrderSOSAffirmBuyer;
    MenuDialog menuDialog;

    List<String> list = new ArrayList<>();

    SSS_Adapter sss_adapter;

    String friend_id;

    boolean canClick = false;
    @BindView(R.id.total_price_order_order_sos_affirm_buyer)
    TextView totalPriceOrderOrderSosAffirmBuyer;
    @BindView(R.id.click_submit_order_order_sos_affirm_buyer)
    TextView clickSubmitOrderOrderSosAffirmBuyer;


    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        totalPriceOrderOrderSosAffirmBuyer = null;
        clickSubmitOrderOrderSosAffirmBuyer = null;
        backTop = null;
        titleTop = null;
        rightButtonTop = null;
        faultOrderOrderSOSAffirmBuyer = null;
        if (menuDialog != null) {
            menuDialog.clear();
        }
        menuDialog = null;
        peopleNameOrderOrderSOSAffirmBuyer = null;
        mobileNameOrderOrderSOSAffirmBuyer = null;
        car_order_sos_affirm_buyer = null;
        helpTypeOrderOrderSOSAffirmBuyer = null;
        showAddressOrderOrderSOSAffirmBuyer = null;
        priceOrderSOSDetails = null;
        showServiceTimeOrderOrderSOSAffirmBuyer = null;
        penalSumOrderOrderSOSAffirmBuyer = null;
        showOtherOrderOrderSOSAffirmBuyer = null;
        photoOrderOrderSOSAffirmBuyer = null;
        tipOrderOrderSOSAffirmBuyer = null;
        OrderOrderSOSAffirmBuyer = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_sos_affirm_buyer);
        ButterKnife.bind(this);
        customInit(OrderOrderSOSAffirmBuyer, false, true, false);
        titleTop.setText("sos订单");
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传输错误");
            finish();
        }
        APPOftenUtils.underLineOfTextView(rightButtonTop).setTextColor(getResources().getColor(R.color.mainColor));


        initAdapter();
        orderTip();
        get_drafts_SOS();
    }

    @OnClick({R.id.back_top, R.id.click_submit_order_order_sos_affirm_buyer,R.id.click_cancel_order_order_sos_affirm_buyer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.click_submit_order_order_sos_affirm_buyer:
                APPOftenUtils.createAskDialog(getBaseActivityContext(), "是否要确认完成？", new OnAskDialogCallBack() {
                    @Override
                    public void onOKey(Dialog dialog) {
                        dialog.dismiss();
                        dialog=null;
                        accomplish();
                    }

                    @Override
                    public void onCancel(Dialog dialog) {
                        dialog.dismiss();
                        dialog=null;
                    }
                });

                break;
            case R.id.click_cancel_order_order_sos_affirm_buyer:
                APPOftenUtils.createAskDialog(getBaseActivityContext(), "是否要取消订单？", new OnAskDialogCallBack() {
                    @Override
                    public void onOKey(Dialog dialog) {
                        dialog.dismiss();
                        dialog=null;
                        cancel_sos();
                    }

                    @Override
                    public void onCancel(Dialog dialog) {
                        dialog.dismiss();
                        dialog=null;
                    }
                });
                break;
        }
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
        photoOrderOrderSOSAffirmBuyer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        photoOrderOrderSOSAffirmBuyer.setAdapter(sss_adapter);
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
                                    peopleNameOrderOrderSOSAffirmBuyer.setText(jsonObject1.getString("recipients"));
                                    mobileNameOrderOrderSOSAffirmBuyer.setText(jsonObject1.getString("mobile"));
                                    car_order_sos_affirm_buyer.setText(jsonObject1.getString("vehicle_name"));
                                    penalSumOrderOrderSOSAffirmBuyer.setText(jsonObject1.getString("vehicle_name"));
                                    helpTypeOrderOrderSOSAffirmBuyer.setText(jsonObject1.getString("type"));
                                    showAddressOrderOrderSOSAffirmBuyer.setText(jsonObject1.getString("address"));
                                    priceOrderSOSDetails.setText(jsonObject1.getString("price"));
                                    totalPriceOrderOrderSosAffirmBuyer.setText("¥" + jsonObject1.getString("price"));
                                    showServiceTimeOrderOrderSOSAffirmBuyer.setText(jsonObject1.getString("service_time"));
                                    showOtherOrderOrderSOSAffirmBuyer.setText(jsonObject1.getString("remark"));
                                    faultOrderOrderSOSAffirmBuyer.setText(jsonObject1.getString("title"));

                                    JSONArray jsonArray = jsonObject1.getJSONArray("picture");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        list.add(jsonArray.getString(i));
                                    }
                                    if (jsonArray.length() > 0) {
                                        photoOrderOrderSOSAffirmBuyer.setVisibility(View.VISIBLE);
                                    }
                                    sss_adapter.setList(list);
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
                                    tipOrderOrderSOSAffirmBuyer.setText(jsonObject.getJSONObject("data").getString("contents"));
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
     * 完成SOS订单
     */
    public void accomplish() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.accomplish(
                    new JSONObject()
                            .put("member_id",Config.member_id)
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
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    EventBus.getDefault().post(new ChangedMessageOrderList());
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
     * 取消SOS订单信息(卖家版)
     */
    public void cancel_sos() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.cancel_sos(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("type", "1")//1求助者2援助者
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
                                    EventBus.getDefault().post(new ChangedMessageOrderList());
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
