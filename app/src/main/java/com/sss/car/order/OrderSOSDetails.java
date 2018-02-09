package com.sss.car.order;

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
import com.blankj.utilcode.customwidget.ListView.HorizontalListView;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.utils.MenuDialog;
import com.sss.car.view.ActivityImages;

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
 * SOS已完成/已取消页面
 * Created by leilei on 2017/10/19.
 */

public class OrderSOSDetails extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.people_name_order_sos_details)
    TextView peopleNameOrderSOSAcceptFromSeller;
    @BindView(R.id.mobile_name_order_sos_details)
    TextView mobileNameOrderSOSAcceptFromSeller;
    @BindView(R.id.car_order_sos_details)
    TextView car_order_sos_details;
    @BindView(R.id.help_type_order_sos_details)
    TextView helpTypeOrderSOSAcceptFromSeller;
    @BindView(R.id.show_address_order_sos_details)
    TextView showAddressOrderSOSAcceptFromSeller;
    @BindView(R.id.price_order_sos_details)
    TextView priceOrderSOSDetails;
    @BindView(R.id.show_service_time_order_sos_details)
    TextView showServiceTimeOrderSOSAcceptFromSeller;
    @BindView(R.id.show_penal_sum_order_sos_details)
    TextView penalSumOrderSOSAcceptFromSeller;
    @BindView(R.id.show_other_order_sos_details)
    TextView showOtherOrderSOSAcceptFromSeller;
    @BindView(R.id.photo_order_sos_details)
    HorizontalListView photoOrderSOSAcceptFromSeller;
    @BindView(R.id.tip_order_sos_details)
    TextView tipOrderSOSAcceptFromSeller;
    @BindView(R.id.order_sos_details)
    LinearLayout OrderSOSAcceptFromSeller;

    YWLoadingDialog ywLoadingDialog;
    @BindView(R.id.fault_order_sos_details)
    TextView faultOrderSosAcceptFromSeller;
    MenuDialog menuDialog;

    List<String> list = new ArrayList<>();

    SSS_Adapter sss_adapter;

    String friend_id;

    boolean canClick = false;



    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        backTop = null;
        titleTop = null;
        rightButtonTop = null;
        faultOrderSosAcceptFromSeller = null;
        if (menuDialog != null) {
            menuDialog.clear();
        }
        menuDialog = null;
        peopleNameOrderSOSAcceptFromSeller = null;
        mobileNameOrderSOSAcceptFromSeller = null;
        car_order_sos_details = null;
        helpTypeOrderSOSAcceptFromSeller = null;
        showAddressOrderSOSAcceptFromSeller = null;
        priceOrderSOSDetails = null;
        showServiceTimeOrderSOSAcceptFromSeller = null;
        penalSumOrderSOSAcceptFromSeller = null;
        showOtherOrderSOSAcceptFromSeller = null;
        photoOrderSOSAcceptFromSeller = null;
        tipOrderSOSAcceptFromSeller = null;
        OrderSOSAcceptFromSeller = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_sos_details);
        ButterKnife.bind(this);
        customInit(OrderSOSAcceptFromSeller, false, true, false);
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

    @OnClick({R.id.back_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
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
                                    mobileNameOrderSOSAcceptFromSeller.setText(jsonObject1.getString("mobile"));
                                    car_order_sos_details.setText(jsonObject1.getString("vehicle_name"));
                                    penalSumOrderSOSAcceptFromSeller.setText(jsonObject1.getString("vehicle_name"));
                                    helpTypeOrderSOSAcceptFromSeller.setText(jsonObject1.getString("type"));
                                    showAddressOrderSOSAcceptFromSeller.setText(jsonObject1.getString("address"));
                                    priceOrderSOSDetails.setText(jsonObject1.getString("price"));
                                    showServiceTimeOrderSOSAcceptFromSeller.setText(jsonObject1.getString("service_time"));
                                    showOtherOrderSOSAcceptFromSeller.setText(jsonObject1.getString("remark"));
                                    faultOrderSosAcceptFromSeller.setText(jsonObject1.getString("title"));

                                    JSONArray jsonArray = jsonObject1.getJSONArray("picture");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        list.add(jsonArray.getString(i));
                                    }
                                    if (jsonArray.length() > 0) {
                                        photoOrderSOSAcceptFromSeller.setVisibility(View.VISIBLE);
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

}
