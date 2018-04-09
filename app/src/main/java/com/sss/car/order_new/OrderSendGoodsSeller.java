package com.sss.car.order_new;

import android.content.Intent;
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
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.rey.material.app.BottomSheetDialog;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedOrderModel;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.ExpressModel;
import com.sss.car.utils.MenuDialog;
import com.sss.car.view.ActivityShopInfo;

import org.greenrobot.eventbus.EventBus;
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
 * 卖家发货
 * Created by leilei on 2018/1/8.
 */

public class OrderSendGoodsSeller extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.order_code)
    TextView orderCode;
    @BindView(R.id.listview)
    OrderGoodsList listview;
    @BindView(R.id.title_company)
    TextView titleCompany;
    @BindView(R.id.company)
    TextView company;
    @BindView(R.id.title_expressage_code)
    TextView titleExpressageCode;
    @BindView(R.id.expressage_code)
    EditText expressageCode;
    @BindView(R.id.title_reason)
    TextView titleReason;
    @BindView(R.id.reason)
    EditText reason;
    @BindView(R.id.click)
    TextView click;
    @BindView(R.id.order_send_goods_seller)
    LinearLayout orderSendGoodsSeller;
    OrderModel orderModel;
    YWLoadingDialog ywLoadingDialog;


    List<ExpressModel> expressList = new ArrayList<>();
    SSS_Adapter expressAdapter;
    Gson gson = new Gson();
    MenuDialog menuDialog;
    BottomSheetDialog bottomSheetDialog;
    String express_id;


    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;

        if (bottomSheetDialog != null) {
            bottomSheetDialog.dismiss();
        }
        bottomSheetDialog = null;
        if (expressList != null) {
            expressList.clear();
        }
        expressList = null;
        express_id = null;
        gson = null;
        if (menuDialog != null) {
            menuDialog.clear();
        }
        menuDialog = null;
        if (expressAdapter != null) {
            expressAdapter.clear();
        }
        expressAdapter = null;

        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误!");
            return;
        }
        setContentView(R.layout.order_send_goods_seller);
        ButterKnife.bind(this);
        customInit(orderSendGoodsSeller, false, true, false);
        titleTop.setText("发货填写");
        List<OrderModel> list = new ArrayList<>();
        orderModel = getIntent().getParcelableExtra("data");
        initExpressAdapter();
        express_company();
        list.add(orderModel);
        orderCode.setText("" + orderModel.order_code);
        listview.setList(getBaseActivityContext(), list, false);
        listview.setListViewOrderCallBack(new OrderGoodsList.ListViewOrderCallBack() {
            @Override
            public void onName(String targetPic, String targetName, String targetId) {
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityShopInfo.class)
                            .putExtra("shop_id", targetId));
                }
            }

            @Override
            public void onQR(String QR) {
            }
        });

        company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expressList.size() == 0) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "快递公司信息获取中...");
                    express_company();
                    return;
                }
                if (menuDialog == null) {
                    menuDialog = new MenuDialog(getBaseActivity());
                }
                if (bottomSheetDialog != null) {
                    bottomSheetDialog.dismiss();
                }
                bottomSheetDialog = null;
                bottomSheetDialog = menuDialog.createExpressBottomDialog(getBaseActivityContext(), expressAdapter);
                expressAdapter.setList(expressList);
            }
        });
    }


    void initExpressAdapter() {
        expressAdapter = new SSS_Adapter<ExpressModel>(getBaseActivityContext(), R.layout.item_express_text) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, final ExpressModel bean, SSS_Adapter instance) {
                helper.setText(R.id.text_express, bean.name);
                helper.getView(R.id.text_express).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        express_id = bean.express_id;
                        company.setText(bean.name);
                        company.setTextColor(getResources().getColor(R.color.black));
                        if (bottomSheetDialog != null) {
                            bottomSheetDialog.dismiss();
                        }
                        bottomSheetDialog = null;
                    }
                });
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };

    }


    /**
     * 发货时从服务器获取支持的快递公司
     */
    public void express_company() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.express_company(
                    new JSONObject()
                            .put("member_id", Config.member_id)
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
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        expressList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), ExpressModel.class));
                                    }
                                    expressAdapter.setList(expressList);
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

    @OnClick({R.id.back_top, R.id.click})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.click:
                if (StringUtils.isEmpty(express_id)) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请选择快递公司");
                    return;
                }
                if (StringUtils.isEmpty(expressageCode.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请输入快递单号");
                    return;
                }
                LogUtils.e(orderModel.status);
                if (8==orderModel.status) {
                    deliver_goods(orderModel.order_id);
                } else {
                    deliver(orderModel.order_id);
                }
                break;
        }
    }
    /**
     * 立即发货
     */
    public void deliver_goods(final String order_id) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        LogUtils.e(order_id);
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.deliver_goods(
                    new JSONObject()
                            .put("express_id", express_id)
                            .put("exchange_id", orderModel.exchange_id)
                            .put("waybill", expressageCode.getText().toString().trim())
                            .put("member_id", Config.member_id)
                            .put("remark", reason.getText().toString().trim())
                            .put("order_id", order_id)
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
                                    EventBus.getDefault().post(new ChangedOrderModel());
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
    /**
     * 立即发货
     */
    public void deliver(final String order_id) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        LogUtils.e(order_id);
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.deliver(
                    new JSONObject()
                            .put("express_id", express_id)
                            .put("exchange_id", orderModel.exchange_id)
                            .put("waybill", expressageCode.getText().toString().trim())
                            .put("member_id", Config.member_id)
                            .put("remark", reason.getText().toString().trim())
                            .put("order_id", order_id)
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
                                    EventBus.getDefault().post(new ChangedOrderModel());
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
