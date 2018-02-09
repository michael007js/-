package com.sss.car.view;

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
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.utils.MenuDialog;

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
 * SOS卖家版未完成订单详情
 * Created by leilei on 2017/10/14.
 */

public class ActivityOrderSOSCompleteDetails extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.people_name_activity_order_sos_complete_details)
    TextView peopleNameActivityOrderSosCompleteDetails;
    @BindView(R.id.mobile_name_activity_order_sos_complete_details)
    TextView mobileNameActivityOrderSosCompleteDetails;
    @BindView(R.id.car_activity_order_sos_complete_details)
    TextView carActivityOrderSosCompleteDetails;
    @BindView(R.id.show_type_activity_order_sos_complete_details)
    TextView showTypeActivityOrderSosCompleteDetails;
    @BindView(R.id.show_fault_activity_order_sos_complete_details)
    TextView showFaultActivityOrderSosCompleteDetails;
    @BindView(R.id.show_address_activity_order_sos_complete_details)
    TextView showAddressActivityOrderSosCompleteDetails;
    @BindView(R.id.price_activity_order_sos_complete_details)
    TextView priceActivityOrderSosCompleteDetails;
    @BindView(R.id.show_time_activity_order_sos_complete_details)
    TextView showTimeActivityOrderSosCompleteDetails;
    @BindView(R.id.show_penal_sum_activity_order_sos_complete_details)
    TextView showPenalSumActivityOrderSosCompleteDetails;
    @BindView(R.id.show_other_activity_order_sos_complete_details)
    TextView showOtherActivityOrderSosCompleteDetails;
    @BindView(R.id.photo)
    HorizontalListView photo;
    @BindView(R.id.tip_activity_order_sos_complete_details)
    TextView tipActivityOrderSosCompleteDetails;
    @BindView(R.id.activity_order_sos_complete_details)
    LinearLayout activityOrderSosCompleteDetails;
    @BindView(R.id.content_activity_order_sos_complete_details)
    TextView contentActivityOrderSosCompleteDetails;
    YWLoadingDialog ywLoadingDialog;

    List<String> list = new ArrayList<>();

    SSS_Adapter sss_adapter;

    MenuDialog menuDialog;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (menuDialog!=null){
            menuDialog.clear();
        }
        menuDialog=null;
        backTop = null;
        titleTop = null;
        contentActivityOrderSosCompleteDetails = null;
        peopleNameActivityOrderSosCompleteDetails = null;
        mobileNameActivityOrderSosCompleteDetails = null;
        carActivityOrderSosCompleteDetails = null;
        showTypeActivityOrderSosCompleteDetails = null;
        showFaultActivityOrderSosCompleteDetails = null;
        showAddressActivityOrderSosCompleteDetails = null;
        priceActivityOrderSosCompleteDetails = null;
        showTimeActivityOrderSosCompleteDetails = null;
        showPenalSumActivityOrderSosCompleteDetails = null;
        showOtherActivityOrderSosCompleteDetails = null;
        photo = null;
        tipActivityOrderSosCompleteDetails = null;
        activityOrderSosCompleteDetails = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_sos_complete_details);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误");
            finish();
        }
        ButterKnife.bind(this);
        customInit(activityOrderSosCompleteDetails, false, true, false);
        titleTop.setText("订单详情");
        initAdapter();
        getSOSSellerDetails();
        orderTip();
    }

    void initAdapter() {
        sss_adapter = new SSS_Adapter<String>(getBaseActivityContext(), R.layout.item_image, list) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, String bean,SSS_Adapter instance) {

                if (bean.startsWith("/storage/")) {
                    FrescoUtils.showImage(false, 80, 80, Uri.fromFile(new File(bean)), ((SimpleDraweeView) helper.getView(R.id.pic_item_image)), 0f);
                } else {
                    FrescoUtils.showImage(false, 80, 80, Uri.parse(Config.url + bean), ((SimpleDraweeView) helper.getView(R.id.pic_item_image)), 0f);
                }

            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };
        photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        photo.setAdapter(sss_adapter);
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
     * 获取SOS信息(卖家版)
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
                                    peopleNameActivityOrderSosCompleteDetails.setText(jsonObject.getJSONObject("data").getString("friend_name"));
                                    mobileNameActivityOrderSosCompleteDetails.setText(jsonObject.getJSONObject("data").getString("mobile"));
                                    carActivityOrderSosCompleteDetails.setText(jsonObject.getJSONObject("data").getString("vehicle_name"));
                                    showAddressActivityOrderSosCompleteDetails.setText(jsonObject.getJSONObject("data").getString("address"));
                                    showTypeActivityOrderSosCompleteDetails.setText(jsonObject.getJSONObject("data").getString("type"));
                                    contentActivityOrderSosCompleteDetails.setText(jsonObject.getJSONObject("data").getString("title"));
                                    priceActivityOrderSosCompleteDetails.setText("¥"+jsonObject.getJSONObject("data").getString("price"));
                                    showTimeActivityOrderSosCompleteDetails.setText(jsonObject.getJSONObject("data").getString("service_time"));
                                    showPenalSumActivityOrderSosCompleteDetails.setText(jsonObject.getJSONObject("data").getString("damages"));
                                    showOtherActivityOrderSosCompleteDetails.setText(jsonObject.getJSONObject("data").getString("remark"));
                                    JSONArray picture = jsonObject.getJSONObject("data").getJSONArray("picture");
                                    for (int i = 0; i < picture.length(); i++) {
                                        list.add(picture.getString(i));
                                    }
                                    if (list.size()>0){
                                        photo.setVisibility(View.VISIBLE);
                                        sss_adapter.setList(list);
                                    }

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
                                    tipActivityOrderSosCompleteDetails.setText(jsonObject.getJSONObject("data").getString("contents"));
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
