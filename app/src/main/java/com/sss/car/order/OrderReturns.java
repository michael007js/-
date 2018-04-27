package com.sss.car.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.GalleryHorizontalListView.GalleryHorizontalListView;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.BitmapUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.rey.material.app.BottomSheetDialog;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangeInfoModel;
import com.sss.car.EventBusModel.ChangedOrderModel;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.ExpressModel;
import com.sss.car.model.ReterunAndChangeModel;
import com.sss.car.order_new.CustomOrderReturnsListView;
import com.sss.car.order_new.OrderModel;
import com.sss.car.utils.MenuDialog;
import com.sss.car.view.ActivityChangeInfo;
import com.sss.car.view.ActivityImages;
import com.sss.car.view.ActivityShopInfo;
import com.sss.car.view.ActivityUserInfo;

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


/**
 * 买家申请和完善退换货页面
 * Created by leilei on 2017/10/16.
 */

@SuppressWarnings("ALL")
public class OrderReturns extends BaseActivity implements CustomOrderReturnsListView.OnCustomOrderReturnsListViewCallBack {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.order_code)
    TextView orderCode;
    @BindView(R.id.listview)
    CustomOrderReturnsListView listview;
    @BindView(R.id.apply_for_type)
    Spinner applyForType;
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
    GalleryHorizontalListView photo;
    @BindView(R.id.click_submit)
    TextView clickSubmit;
    @BindView(R.id.order_returns)
    LinearLayout activityOrderApplyForReturnsChangeRightTopButtonDetails;


    List<String> listSpinner = new ArrayList<>();

    SSS_Adapter sss_adapter;

    OrderModel orderModel;
    String sendReturnOrChange, sendReason, sendCode, sendCompany, sendType;

    YWLoadingDialog ywLoadingDialog;
    @BindView(R.id.code)
    TextView code;
    @BindView(R.id.click_code)
    LinearLayout clickCode;
    @BindView(R.id.show_company)
    TextView showCompany;
    @BindView(R.id.show_code)
    TextView showCode;
    @BindView(R.id.feedback)
    TextView feedback;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.top_parent)
    LinearLayout topParent;
    @BindView(R.id.feedback_parent)
    LinearLayout feedbackParent;

    List<ExpressModel> expressList = new ArrayList<>();
    SSS_Adapter expressAdapter;
    Gson gson = new Gson();
    MenuDialog menuDialog;
    BottomSheetDialog bottomSheetDialog;
    String express_id;
    List<String> temp = new ArrayList<>();
    @BindView(R.id.return_type)
    Spinner returnType;
    List<ReterunAndChangeModel> list = new ArrayList<>();
    String attr_id;
    @BindView(R.id.parent_return_type)
    LinearLayout parentReturnType;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        orderCode = null;
        listview = null;
        applyForType = null;
        titleCompany = null;
        clickCompany = null;
        titleReason = null;
        reason = null;
        clickReason = null;
        photo = null;
        clickSubmit = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_returns);
        ButterKnife.bind(this);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递失败");
            finish();
        }
        listview.setOnCustomOrderReturnsListViewCallBack(this);
        customInit(activityOrderApplyForReturnsChangeRightTopButtonDetails, false, true, true);

        if (getIntent().getExtras().getBoolean("returnOrChange_isFirst")) {//第一次打开,申请退换货
            titleTop.setText("退换货申请");
            titleCompany.setTextColor(getResources().getColor(R.color.line));
            code.setTextColor(getResources().getColor(R.color.line));
            reason.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getBaseActivityContext() != null) {
                        startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                                .putExtra("canChange", true)
                                .putExtra("type", "returnAndChangeReason").putExtra("extra", reason.getText().toString()));
                    }
                }
            });
            listSpinner.add("退货");
            listSpinner.add("换货");
            parentReturnType.setVisibility(View.VISIBLE);
            returnType.setVisibility(View.VISIBLE);
            initSpinner();
            initPhotoAdapter(true);
        } else if (getIntent().getExtras().getBoolean("returnOrChange_isFirst") == false) {//不是第一次打开,填写

            titleTop.setText("完善退换资料");
            applyForType.setEnabled(false);
            initPhotoAdapter(false);
            express_company();
            clickCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getBaseActivityContext() != null) {
                        startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                                .putExtra("canChange", true)
                                .putExtra("type", "returnAndChangeReason_Code").putExtra("extra", showCode.getText().toString()));
                    }
                }
            });
            initExpressAdapter();
            clickCompany.setOnClickListener(new View.OnClickListener() {
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

        if (true == getIntent().getExtras().getBoolean("isStop")) {
            titleTop.setText("退换货资料");
            clickSubmit.setVisibility(View.GONE);
            applyForType.setEnabled(false);
            clickCompany.setEnabled(false);
            clickCode.setEnabled(false);
            clickReason.setEnabled(false);
            photo.setEnabled(false);
        }

        getInfo();
        orderAttr();

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
                                    List<OrderModel> list = new ArrayList<>();
                                    list.add(orderModel);
                                    orderCode.setText("" + orderModel.order_code);
                                    listview.setList(getBaseActivityContext(), orderModel, getIntent().getExtras().getInt("what"));
                                    if (getIntent().getExtras().getBoolean("returnOrChange_isFirst") == false) {//不是第一次打开,填写
                                        getReturnAndChangeOrderInfo(jsonObject.getJSONObject("data").getString("exchange_id"));
                                        type.setVisibility(View.VISIBLE);
                                        applyForType.setVisibility(View.GONE);
                                        feedbackParent.setVisibility(View.VISIBLE);
                                    } else {
                                        applyForType.setVisibility(View.VISIBLE);
                                        type.setVisibility(View.GONE);
                                        feedbackParent.setVisibility(View.GONE);
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


    void initExpressAdapter() {
        expressAdapter = new SSS_Adapter<ExpressModel>(getBaseActivityContext(), R.layout.item_express_text) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, final ExpressModel bean, SSS_Adapter instance) {
                helper.setText(R.id.text_express, bean.name);
                helper.getView(R.id.text_express).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        express_id = bean.express_id;
                        showCompany.setText(bean.name);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeInfoModel event) {
        switch (event.type) {
            case "returnAndChangeReason":
                sendReason = event.msg;
                reason.setText(event.msg);
                break;
            case "returnAndChangeReason_Code":
                showCode.setText(event.msg);
                sendCode = event.msg;
                break;
            case "returnAndChangeReason_Company":
                showCompany.setText(event.msg);
                sendCompany = event.msg;
                break;
        }
    }

    @OnClick({R.id.back_top, R.id.click_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.click_submit:
                if (getIntent().getExtras().getBoolean("returnOrChange_isFirst")) {//第一次打开,申请退换货
                    applyForReturnsAndChange();
                } else if (getIntent().getExtras().getBoolean("returnOrChange_isFirst") == false) {//不是第一次打开,填写
                    if (StringUtils.isEmpty(express_id)) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "请选择快递公司");
                        return;
                    }
                    completeReturnsAndChange();
                }
                break;
        }
    }

    void initSpinner() {
        applyForType.setAdapter(new SSS_Adapter<String>(getBaseActivityContext(), R.layout.item_spinner, listSpinner) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, String bean, SSS_Adapter instance) {
                helper.setText(R.id.text_item_spinner, bean);
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        });
        applyForType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        sendReturnOrChange = "1";//1退货2换货
                        break;
                    case 1:
                        sendReturnOrChange = "2";//1退货2换货
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    void initType(){
        returnType.setAdapter(new SSS_Adapter<ReterunAndChangeModel>(getBaseActivityContext(), R.layout.item_spinner, list) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, ReterunAndChangeModel bean, SSS_Adapter instance) {
                helper.setText(R.id.text_item_spinner, bean.name);
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        });
        returnType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                attr_id = list.get(position).attr_id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void initPhotoAdapter(boolean isCanAdd) {
        if (isCanAdd) {
            photo.setCanOperation(true);
            temp.add(GalleryHorizontalListView.Holder);
            photo.setList(temp);
        } else {
            photo.setCanOperation(true);
            photo.hideClose(true);
        }

        photo.setOnGalleryHorizontalListViewCallBack(new GalleryHorizontalListView.OnGalleryHorizontalListViewCallBack() {
            @Override
            public void onClickImage(SimpleDraweeView simpleDraweeView, int position, List<String> list) {

                startActivity(new Intent(getBaseActivityContext(), ActivityImages.class)
                        .putStringArrayListExtra("data", (ArrayList<String>) list)
                        .putExtra("current", position - 1));
            }

            @Override
            public void onClose(int position, List<String> list) {

            }
        });

    }

    /**
     * 获取退换类型
     */
    public void orderAttr() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.orderAttr(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("type", "5")//1送达时效，2违约金比例，3求助类型，4服务就位，5退换货原因
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
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        ReterunAndChangeModel reterunAndChangeModel = new ReterunAndChangeModel();
                                        reterunAndChangeModel.attr_id = jsonArray.getJSONObject(i).getString("attr_id");
                                        reterunAndChangeModel.name = jsonArray.getJSONObject(i).getString("name");
                                        list.add(reterunAndChangeModel);
                                    }
                                    initType();

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
    public void getReturnAndChangeOrderInfo(String exchange_id) {

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
                            .put("exchange_id", exchange_id)
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
                                    feedback.setText(jsonObject.getJSONObject("data").getString("feedback"));
                                    if ("1".equals(jsonObject.getJSONObject("data").getString("type"))) {
                                        type.setText("退货");
                                    } else {
                                        type.setText("换货");
                                    }
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                    JSONArray jsonArray = jsonObject1.getJSONArray("picture");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        temp.add(Config.url + jsonArray.getString(i));
                                    }

                                    photo.setList(temp);
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
     * 申请退换货
     */
    public void applyForReturnsAndChange() {
        if (StringUtils.isEmpty(sendReason)) {
            ToastUtils.showShortToast(getBaseActivityContext(), " 请填写退换货理由");
            return;
        }
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();

        JSONArray goods_id = new JSONArray();
        for (int i = 0; i < orderModel.goods_data.size(); i++) {
            goods_id.put(orderModel.goods_data.get(i).goods_id);
        }

        JSONArray picture = new JSONArray();
        for (int i = 0; i < photo.getList().size(); i++) {
            if (!"default".equals(photo.getList().get(i))) {
                picture.put(ConvertUtils.bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(photo.getList().get(i),
                        getWindowManager().getDefaultDisplay().getWidth(),
                        getWindowManager().getDefaultDisplay().getHeight())));
            }
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.applyForReturnsAndChange(
                    new JSONObject()
                            .put("order_id", orderModel.order_id)
                            .put("member_id", Config.member_id)
                            .put("goods_id", goods_id)
                            .put("attr_id", attr_id)
                            .put("cause", sendReason)
                            .put("type", sendReturnOrChange)
                            .put("picture", picture)
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
                                    List<String> temp = new ArrayList<>();
                                    for (int i = 0; i < orderModel.goods_data.size(); i++) {
                                        temp.add(orderModel.goods_data.get(i).goods_id);
                                    }
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


    /**
     * 完善退换货资料
     */
    public void completeReturnsAndChange() {
        if (StringUtils.isEmpty(express_id) || StringUtils.isEmpty(sendCode)) {
            ToastUtils.showShortToast(getBaseActivityContext(), " 请填写完快递信息后再提交");
            return;
        }
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();

        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.complete(
                    new JSONObject()
                            .put("order_id", orderModel.order_id)
                            .put("exchange_id", orderModel.exchange_id)
                            .put("member_id", Config.member_id)
                            .put("express_id", express_id)
                            .put("waybill", sendCode)
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
    public void onShop(String shop_id) {
        if (getBaseActivityContext() != null) {
            startActivity(new Intent(getBaseActivityContext(), ActivityShopInfo.class)
                    .putExtra("shop_id", shop_id));
        }
    }

    @Override
    public void onUser(String user_id, String nikeName) {
        startActivity(new Intent(getBaseActivityContext(), ActivityUserInfo.class)
                .putExtra("id", user_id));
    }
}
