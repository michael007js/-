package com.sss.car.order_new;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.BitmapUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedOrderModel;
import com.sss.car.EventBusModel.OrderCommentListChanged;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.ListViewOrderRetuenOrChange;
import com.sss.car.fragment.FragmentOrder;
import com.sss.car.view.ActivityShopInfo;
import com.sss.car.view.ActivityUserInfo;

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
 * 退换货申请与补充退换货资料公用界面
 * Created by leilei on 2017/10/12.
 */

@SuppressWarnings("ALL")
public class OrderReturnsChangeApplyForAndCompleteDataSeller extends BaseActivity implements ListViewOrderRetuenOrChange.ListViewOrderRetuenOrChangeCallBack {

    OrderModel orderModel;
    ListViewOrderRetuenOrChange listViewOrderRetuenOrChange;


    List<String> listSpinner = new ArrayList<>();
    List<String> listPhoto = new ArrayList<>();

    SSS_Adapter sss_adapter;

    boolean isFromBusy = false;
    YWLoadingDialog ywLoadingDialog;


    String sendCompany, sendCode, sendReason, sendResult, sendReturnOrChange/*1退货2换货*/;

    String sendFeedback;
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.order_code)
    TextView orderCode;
    @BindView(R.id.listview)
    ListViewOrderRetuenOrChange listview;
    @BindView(R.id.apply_for_type)
    TextView applyForType;
    @BindView(R.id.title_company)
    TextView titleCompany;
    @BindView(R.id.click_company)
    LinearLayout clickCompany;
    @BindView(R.id.title_reason)
    TextView titleReason;
    @BindView(R.id.reason)
    TextView reason;
    @BindView(R.id.click_reason)
    LinearLayout clickReason;
    @BindView(R.id.photo)
    HorizontalListView photo;
    @BindView(R.id.click_yes)
    TextView clickYes;
    @BindView(R.id.click_no)
    TextView clickNo;
    @BindView(R.id.order_apply_for_returns_change_right_top_button_details)
    LinearLayout orderApplyForReturnsChangeRightTopButtonDetails;
    @BindView(R.id.feedback)
    EditText feedback;


    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;

        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_apply_for_returns_change_right_top_button_details_seller);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误");
            finish();
        }
        customInit(orderApplyForReturnsChangeRightTopButtonDetails, false, true, false);
        listViewOrderRetuenOrChange = $.f(this, R.id.listview);
        listViewOrderRetuenOrChange.setListViewOrderCallBack(this);
        ButterKnife.bind(this);
        customInit(orderApplyForReturnsChangeRightTopButtonDetails, false, true, false);
        listview.setListViewOrderCallBack(new ListViewOrderRetuenOrChange.ListViewOrderRetuenOrChangeCallBack() {
            @Override
            public void onName(String targetPic, String targetName, String targetId) {
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityShopInfo.class)
                            .putExtra("shop_id", targetId));
                }
            }
        });
        initPhotoAdapter();
        getInfo();
        titleTop.setText("立即处理");
    }


    void initPhotoAdapter() {
        sss_adapter = new SSS_Adapter<String>(getBaseActivityContext(), R.layout.item_image) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, String bean, SSS_Adapter instance) {
                if ("default".equals(bean)) {
                    FrescoUtils.showImage(false, 80, 80, Uri.parse("res://" + getBaseActivityContext().getPackageName() + "/" + R.mipmap.logo_add_image), ((SimpleDraweeView) helper.getView(R.id.pic_item_image)), 0f);
                } else {
                    if (bean.startsWith("/storage/")) {
                        FrescoUtils.showImage(false, 80, 80, Uri.fromFile(new File(bean)), ((SimpleDraweeView) helper.getView(R.id.pic_item_image)), 0f);
                    } else {
                        FrescoUtils.showImage(false, 80, 80, Uri.parse( bean), ((SimpleDraweeView) helper.getView(R.id.pic_item_image)), 0f);
                    }
                }
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };
        sss_adapter.setList(listPhoto);
        photo.setAdapter(sss_adapter);

    }

    public void getInfo() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();

        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.new_expend_single(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("order_id", getIntent().getExtras().getString("order_id"))
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
                                    orderModel = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), OrderModel.class);
                                    orderCode.setText("" + orderModel.order_code);
                                    List<OrderModel> list = new ArrayList<OrderModel>();
                                    list.add(orderModel);
                                    listview.setList(getBaseActivityContext(), list);
                                    getReturnAndChangeOrderInfo();
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
     * 获取退换货详情
     */
    public void getReturnAndChangeOrderInfo() {


        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();

        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getReturnAndChangeOrderInfo(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("exchange_id", orderModel.exchange_id)
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
                                    if ("1".equals(jsonObject.getJSONObject("data").getString("type"))) {
                                        applyForType.setText("退货");
                                    } else if ("2".equals(jsonObject.getJSONObject("data").getString("type"))) {
                                        applyForType.setText("换货");
                                    }

                                    JSONObject jsonObject1=jsonObject.getJSONObject("data");
                                    JSONArray jsonArray=jsonObject1.getJSONArray("picture");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        listPhoto.add(Config.url+jsonArray.getString(i));
                                    }

                                    sss_adapter.setList(listPhoto);
                                    reason.setText(jsonObject.getJSONObject("data").getString("cause"));
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
     * 同意和拒绝订单信息
     *
     * @param status 1同意，2拒绝
     */
    public void status_exchange(String status) {
        if (StringUtils.isEmpty(feedback.getText().toString().trim())) {
            ToastUtils.showShortToast(getBaseActivityContext(), " 请填写反馈信息");
            return;
        }
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();

        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.status_exchange(
                    new JSONObject()
                            .put("exchange_id", orderModel.exchange_id)
                            .put("feedback",feedback.getText().toString().trim())
                            .put("member_id", Config.member_id)
                            .put("status", status)
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
                                    EventBus.getDefault().post(new ChangedOrderModel());
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


    @Override
    public void onName(String targetPic, String targetName, String targetId) {
        switch (getIntent().getExtras().getInt("what")) {
            case FragmentOrder.INCOME:
                startActivity(new Intent(getBaseActivityContext(), ActivityUserInfo.class)
                        .putExtra("id", targetId));
                break;

            case FragmentOrder.EXPEND:
                if (getBaseActivityContext() != null) {
                    if (getBaseActivityContext() != null) {
                        startActivity(new Intent(getBaseActivityContext(), ActivityShopInfo.class)
                                .putExtra("shop_id", targetId));
                    }
                }
                break;
        }
    }

    @OnClick({R.id.back_top, R.id.click_yes, R.id.click_no})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.click_yes:
                status_exchange("1");
                break;
            case R.id.click_no:
                status_exchange("2");
                break;
        }
    }
}
