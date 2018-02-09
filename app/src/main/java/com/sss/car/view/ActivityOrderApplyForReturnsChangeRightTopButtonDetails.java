package com.sss.car.view;

import android.content.Intent;
import android.net.Uri;
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
import com.blankj.utilcode.customwidget.ListView.HorizontalListView;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.BitmapUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangeInfoModel;
import com.sss.car.EventBusModel.OrderCommentListChanged;
import com.sss.car.MyApplication;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.ListViewOrderRetuenOrChange;
import com.sss.car.fragment.FragmentOrder;
import com.sss.car.model.OrderModel;

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
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import okhttp3.Call;


/**
 * 申请退换货页面
 * 作废
 * Created by leilei on 2017/10/16.
 */

public class ActivityOrderApplyForReturnsChangeRightTopButtonDetails extends BaseActivity implements ListViewOrderRetuenOrChange.ListViewOrderRetuenOrChangeCallBack{
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.order_code)
    TextView orderCode;
    @BindView(R.id.listview)
    ListViewOrderRetuenOrChange listview;
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
    HorizontalListView photo;
    @BindView(R.id.click_submit)
    TextView clickSubmit;
    @BindView(R.id.activity_order_apply_for_returns_change_right_top_button_details)
    LinearLayout activityOrderApplyForReturnsChangeRightTopButtonDetails;

    ListViewOrderRetuenOrChange listViewOrderRetuenOrChange;

    List<String> listSpinner = new ArrayList<>();
    List<String> listPhoto = new ArrayList<>();

    SSS_Adapter sss_adapter;

    OrderModel orderModel;
    String sendReturnOrChange, sendReason;

    YWLoadingDialog ywLoadingDialog;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_apply_for_returns_change_right_top_button_details);
        ButterKnife.bind(this);
        if (getIntent()==null||getIntent().getExtras()==null){
            ToastUtils.showShortToast(getBaseActivityContext(),"数据传递失败");
            finish();
        }
        titleTop.setText("退换货申请");
        customInit(activityOrderApplyForReturnsChangeRightTopButtonDetails, false, true, true);
        listViewOrderRetuenOrChange = $.f(this, R.id.listview);
        listViewOrderRetuenOrChange.setListViewOrderCallBack(this);
        List<OrderModel> list = new ArrayList<>();
        orderModel = getIntent().getParcelableExtra("data");

        for (int i = 0; i < orderModel.data.size(); i++) {
            if (!orderModel.data.get(i).isChoose){
                orderModel.data.remove(i);
            }
        }
        list.add(orderModel);
        orderCode.setText("" + orderModel.order_code);
//        listViewOrderRetuenOrChange.setList(getBaseActivityContext(), list);

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
        initSpinner();
        initPhotoAdapter();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeInfoModel event) {
        switch (event.type) {
            case "returnAndChangeReason":
                sendReason = event.msg;
                reason.setText(event.msg);
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
                applyForReturnsAndChange();
                break;
        }
    }

    void initSpinner() {
        listSpinner.add("退货");
        listSpinner.add("换货");
        applyForType.setAdapter(new SSS_Adapter<String>(getBaseActivityContext(), R.layout.item_spinner, listSpinner) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, String bean,SSS_Adapter instance) {
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

    void initPhotoAdapter() {
        listPhoto.add("default");
        sss_adapter = new SSS_Adapter<String>(getBaseActivityContext(), R.layout.item_image) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, String bean,SSS_Adapter instance) {
                if ("default".equals(bean)) {
                    FrescoUtils.showImage(false, 80, 80, Uri.parse("res://" + getBaseActivityContext().getPackageName() + "/" + R.mipmap.logo_add_image), ((SimpleDraweeView) helper.getView(R.id.pic_item_image)), 0f);
                } else {
                    if (bean.startsWith("/storage/")) {
                        FrescoUtils.showImage(false, 80, 80, Uri.fromFile(new File(bean)), ((SimpleDraweeView) helper.getView(R.id.pic_item_image)), 0f);
                    } else {
                        FrescoUtils.showImage(false, 80, 80, Uri.parse(Config.url + bean), ((SimpleDraweeView) helper.getView(R.id.pic_item_image)), 0f);
                    }
                }
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };
        photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ("default".equals(listPhoto.get(position))) {
                    APPOftenUtils.createPhotoChooseDialog(0, 9, weakReference, MyApplication.getFunctionConfig(), new GalleryFinal.OnHanlderResultCallback() {
                        @Override
                        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                            if (resultList == null || resultList.size() == 0) {
                                return;
                            }
                            for (int i = 0; i < resultList.size(); i++) {
                                listPhoto.add(resultList.get(i).getPhotoPath());
                                sss_adapter.setList(listPhoto);
                            }
                        }

                        @Override
                        public void onHanlderFailure(int requestCode, String errorMsg) {

                        }
                    });
                } else {
                    if (getBaseActivityContext() != null) {
                        List<String> temp = new ArrayList<>();
                        for (int j = 0; j < listPhoto.size(); j++) {
                            if (!"default".equals(listPhoto.get(j))) {
                                if (listPhoto.get(j).startsWith("/storage/")) {
                                    temp.add(listPhoto.get(j));
                                } else {
                                    temp.add(Config.url + listPhoto.get(j));
                                }
                            }
                        }
                        startActivity(new Intent(getBaseActivityContext(), ActivityImages.class)
                                .putStringArrayListExtra("data", (ArrayList<String>) temp)
                                .putExtra("current", position));
                    }

                }
            }
        });
        sss_adapter.setList(listPhoto);
        photo.setAdapter(sss_adapter);

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
//        for (int i = 0; i < listViewOrderRetuenOrChange.getList().get(0).data.size(); i++) {
//            goods_id.put(listViewOrderRetuenOrChange.getList().get(0).data.get(i).goods_id);
//        }

        JSONArray picture = new JSONArray();
        for (int i = 0; i < listPhoto.size(); i++) {
            if (!"default".equals(listPhoto.get(i))) {
                picture.put(ConvertUtils.bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(listPhoto.get(i),
                        getWindowManager().getDefaultDisplay().getWidth(),
                        getWindowManager().getDefaultDisplay().getHeight())));
            }
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.applyForReturnsAndChange(
                    new JSONObject()
                            .put("order_id", listViewOrderRetuenOrChange.getList().get(0).order_id)
                            .put("member_id", Config.member_id)
                            .put("goods_id", goods_id)
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
                                    for (int i = 0; i < orderModel.data.size(); i++) {
                                        temp.add(orderModel.data.get(i).goods_id);
                                    }
                                    EventBus.getDefault().post(new OrderCommentListChanged(listViewOrderRetuenOrChange.getList().get(0).order_id, temp));
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
    @Override
    public void onName(String targetPic, String targetName, String targetId) {
        switch (getIntent().getExtras().getInt("what")) {
            case FragmentOrder.INCOME:
                startActivity(new Intent(getBaseActivityContext(), ActivityUserInfo.class)
                        .putExtra("id", targetId));
                break;

            case FragmentOrder.EXPEND:

                    if (getBaseActivityContext() != null) {
                        startActivity(new Intent(getBaseActivityContext(), ActivityShopInfo.class)
                                .putExtra("shop_id", targetId));
                    }
                break;
        }
    }
}
