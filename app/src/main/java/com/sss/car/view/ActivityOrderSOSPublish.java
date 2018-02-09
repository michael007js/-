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
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.EditText.NumberSelectEdit;
import com.blankj.utilcode.customwidget.ListView.HorizontalListView;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.BitmapUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.EventBusModel.CarName;
import com.sss.car.EventBusModel.ChangeInfoModel;
import com.sss.car.EventBusModel.ChooseAdress;
import com.sss.car.MyApplication;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.CouponModel3;
import com.sss.car.model.IntegrityMoneyModel;
import com.sss.car.utils.MenuDialog;

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
 * SOS发布订单
 * Created by leilei on 2017/10/13.
 */

public class ActivityOrderSOSPublish extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.people_name_activity_order_sos_publish)
    TextView peopleNameActivityOrderSosPublish;
    @BindView(R.id.mobile_name_activity_order_sos_publish)
    TextView mobileNameActivityOrderSosPublish;
    @BindView(R.id.car_activity_order_sos_publish)
    TextView carActivityOrderSOSPublish;
    @BindView(R.id.click_choose_car_activity_order_sos_publish)
    LinearLayout clickChooseCarActivityOrderSosPublish;
    @BindView(R.id.show_type_activity_order_sos_publish)
    TextView showTypeActivityOrderSosPublish;
    @BindView(R.id.click_type_activity_order_sos_publish)
    LinearLayout clickTypeActivityOrderSosPublish;
    @BindView(R.id.show_address_activity_order_sos_publish)
    TextView showAddressActivityOrderSosPublish;
    @BindView(R.id.click_address_activity_order_sos_publish)
    LinearLayout clickAddressActivityOrderSosPublish;
    @BindView(R.id.price_activity_order_sos_publish)
    NumberSelectEdit priceActivityOrderSosPublish;
    @BindView(R.id.show_time_activity_order_sos_publish)
    TextView showTimeActivityOrderSosPublish;
    @BindView(R.id.click_time_activity_order_sos_publish)
    LinearLayout clickTimeActivityOrderSosPublish;
    @BindView(R.id.show_penal_sum_activity_order_sos_publish)
    TextView showPenalSumActivityOrderSosPublish;
    @BindView(R.id.click_penal_sum_activity_order_sos_publish)
    LinearLayout clickPenalSumActivityOrderSosPublish;
    @BindView(R.id.show_other_activity_order_sos_publish)
    TextView showOtherActivityOrderSosPublish;
    @BindView(R.id.click_other_sum_activity_order_sos_publish)
    LinearLayout clickOtherSumActivityOrderSosPublish;
    @BindView(R.id.tip_activity_order_sos_publish)
    TextView tipActivityOrderSosPublish;
    @BindView(R.id.click_submit_activity_order_sos_publish)
    TextView clickSubmitActivityOrderSosPublish;
    @BindView(R.id.activity_order_sos_publish)
    LinearLayout activityOrderSosPublish;
    @BindView(R.id.show_fault_activity_order_sos_publish)
    TextView showFaultActivityOrderSosPublish;
    @BindView(R.id.click_fault_activity_order_sos_publish)
    LinearLayout clickFaultActivityOrderSosPublish;
    @BindView(R.id.photo)
    HorizontalListView photo;


    YWLoadingDialog ywLoadingDialog;

    MenuDialog menuDialog;


    SSS_Adapter integrityMoneyAdapter;
    List<IntegrityMoneyModel> integrityMoneyList = new ArrayList<>();

    SSS_Adapter serviceTimeAdapter;
    List<IntegrityMoneyModel> serviceTimeList = new ArrayList<>();

    SSS_Adapter serviceTypeAdapter;
    List<IntegrityMoneyModel> serviceTypeList = new ArrayList<>();

    String lat, lng;
    List<CouponModel3> list = new ArrayList<>();

    SSS_Adapter sss_adapter;
    List<String> listPhoto = new ArrayList<>();


    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        rightButtonTop = null;
        peopleNameActivityOrderSosPublish = null;
        mobileNameActivityOrderSosPublish = null;
        carActivityOrderSOSPublish = null;
        clickChooseCarActivityOrderSosPublish = null;
        showTypeActivityOrderSosPublish = null;
        clickTypeActivityOrderSosPublish = null;
        showAddressActivityOrderSosPublish = null;
        clickAddressActivityOrderSosPublish = null;
        priceActivityOrderSosPublish = null;
        showTimeActivityOrderSosPublish = null;
        clickTimeActivityOrderSosPublish = null;
        showPenalSumActivityOrderSosPublish = null;
        clickPenalSumActivityOrderSosPublish = null;
        showOtherActivityOrderSosPublish = null;
        clickOtherSumActivityOrderSosPublish = null;
        tipActivityOrderSosPublish = null;
        clickSubmitActivityOrderSosPublish = null;
        activityOrderSosPublish = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_sos_publish);
        ButterKnife.bind(this);
        customInit(activityOrderSosPublish, false, true, true);
        titleTop.setText("SOS订单发布");
        initAdapter();
        initPhotoAdapter();
        menuDialog = new MenuDialog(this);
        priceActivityOrderSosPublish.init(getBaseActivityContext(), true);
        priceActivityOrderSosPublish.minNumber(0)
                .isNegativeNumber(false)
                .isLongClick(false);
        defaultVehicle();
        serviceType();
        serviceTime();
        integrityMoney();
        orderTip();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CarName event) {
        carActivityOrderSOSPublish.setText(event.carName);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeInfoModel event) {
        if ("other".equals(event.type)) {
            showOtherActivityOrderSosPublish.setText(event.msg);
        }
        if ("fault".equals(event.type)) {
            showFaultActivityOrderSosPublish.setText(event.msg);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChooseAdress event) {
        lat = event.lai;
        lng = event.lng;
        showAddressActivityOrderSosPublish.setText(event.adress);
    }


    @OnClick({R.id.back_top, R.id.right_button_top, R.id.click_fault_activity_order_sos_publish, R.id.click_choose_car_activity_order_sos_publish, R.id.click_type_activity_order_sos_publish, R.id.click_address_activity_order_sos_publish, R.id.click_time_activity_order_sos_publish, R.id.click_penal_sum_activity_order_sos_publish, R.id.click_other_sum_activity_order_sos_publish, R.id.click_submit_activity_order_sos_publish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.click_fault_activity_order_sos_publish:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                            .putExtra("type", "fault")
                            .putExtra("canChange", true)
                            .putExtra("extra", showFaultActivityOrderSosPublish.getText().toString()));
                }
                break;
            case R.id.right_button_top:
                break;
            case R.id.click_choose_car_activity_order_sos_publish:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataCarGarage.class));
                }
                break;
            case R.id.click_type_activity_order_sos_publish:
                serviceType();
                break;
            case R.id.click_address_activity_order_sos_publish:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityChooseAdress.class));
                }
                break;
            case R.id.click_time_activity_order_sos_publish:
                serviceTime();
                break;
            case R.id.click_penal_sum_activity_order_sos_publish:
                integrityMoney();
                break;
            case R.id.click_other_sum_activity_order_sos_publish:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                            .putExtra("type", "other")
                            .putExtra("canChange", true)
                            .putExtra("extra", ""));
                }
                break;
            case R.id.click_submit_activity_order_sos_publish:
                publisSOS();
                break;
        }
    }


    void initAdapter() {
        integrityMoneyAdapter = new SSS_Adapter<IntegrityMoneyModel>(getBaseActivityContext(), R.layout.item_dialog_coupons) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, IntegrityMoneyModel bean,SSS_Adapter instance) {

                helper.setText(R.id.name_item_dialog_coupons, bean.name);
                if ("1".equals(bean.is_check)) {
                    helper.setChecked(R.id.cb_item_dialog_coupons, true);
                } else {
                    helper.setChecked(R.id.cb_item_dialog_coupons, false);
                }
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
                helper.setItemChildClickListener(R.id.click_item_dialog_coupons);
            }
        };

        integrityMoneyAdapter.setOnItemListener(new SSS_OnItemListener() {

            @Override
            public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                switch (view.getId()) {
                    case R.id.click_item_dialog_coupons:
                        for (int i = 0; i < integrityMoneyList.size(); i++) {

                            if (i == position) {
                                integrityMoneyList.get(i).is_check = "1";
                                showPenalSumActivityOrderSosPublish.setText(integrityMoneyList.get(i).name);
                            } else {
                                integrityMoneyList.get(i).is_check = "0";
                            }
                        }
                        integrityMoneyAdapter.setList(integrityMoneyList);
                        break;
                }
            }
        });
        /*******************************************************************************************************/
        serviceTimeAdapter = new SSS_Adapter<IntegrityMoneyModel>(getBaseActivityContext(), R.layout.item_dialog_coupons) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, IntegrityMoneyModel bean,SSS_Adapter instance) {

                helper.setText(R.id.name_item_dialog_coupons, bean.name);
                if ("1".equals(bean.is_check)) {
                    helper.setChecked(R.id.cb_item_dialog_coupons, true);
                } else {
                    helper.setChecked(R.id.cb_item_dialog_coupons, false);
                }
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
                helper.setItemChildClickListener(R.id.click_item_dialog_coupons);
            }
        };

        serviceTimeAdapter.setOnItemListener(new SSS_OnItemListener() {

            @Override
            public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                switch (view.getId()) {
                    case R.id.click_item_dialog_coupons:
                        for (int i = 0; i < serviceTimeList.size(); i++) {

                            if (i == position) {
                                serviceTimeList.get(i).is_check = "1";
                                showTimeActivityOrderSosPublish.setText(serviceTimeList.get(i).name);
                            } else {
                                serviceTimeList.get(i).is_check = "0";
                            }
                        }
                        serviceTimeAdapter.setList(serviceTimeList);
                        break;
                }
            }
        });
        /*******************************************************************************************************/
        serviceTypeAdapter = new SSS_Adapter<IntegrityMoneyModel>(getBaseActivityContext(), R.layout.item_dialog_coupons) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, IntegrityMoneyModel bean,SSS_Adapter instance) {

                helper.setText(R.id.name_item_dialog_coupons, bean.name);
                if ("1".equals(bean.is_check)) {
                    helper.setChecked(R.id.cb_item_dialog_coupons, true);
                } else {
                    helper.setChecked(R.id.cb_item_dialog_coupons, false);
                }
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
                helper.setItemChildClickListener(R.id.click_item_dialog_coupons);
            }
        };

        serviceTypeAdapter.setOnItemListener(new SSS_OnItemListener() {

            @Override
            public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                switch (view.getId()) {
                    case R.id.click_item_dialog_coupons:
                        for (int i = 0; i < serviceTypeList.size(); i++) {

                            if (i == position) {
                                serviceTypeList.get(i).is_check = "1";
                                showTimeActivityOrderSosPublish.setText(serviceTypeList.get(i).name);
                            } else {
                                serviceTypeList.get(i).is_check = "0";
                            }
                        }
                        serviceTypeAdapter.setList(serviceTypeList);
                        break;
                }
            }
        });
    }


    void initPhotoAdapter() {
        listPhoto.add("default");

        sss_adapter = new SSS_Adapter<String>(getBaseActivityContext(), R.layout.item_image, listPhoto) {
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
                                .putExtra("current", position-1));
                    }

                }
            }
        });
        photo.setAdapter(sss_adapter);

    }

    /**
     * 发布订单
     */
    public void publisSOS() {


        if (StringUtils.isEmpty(carActivityOrderSOSPublish.getText().toString().trim())) {
            ToastUtils.showShortToast(getBaseActivityContext(), " 请添加您的车辆");
            return;
        }
        if (StringUtils.isEmpty(showTypeActivityOrderSosPublish.getText().toString().trim())) {
            ToastUtils.showShortToast(getBaseActivityContext(), " 请填写您的求助类型");
            return;
        }

        if (StringUtils.isEmpty(showFaultActivityOrderSosPublish.getText().toString().trim())) {
            ToastUtils.showShortToast(getBaseActivityContext(), " 请填写您的求助故障");
            return;
        }

        if (StringUtils.isEmpty(lat) || StringUtils.isEmpty(lng) || StringUtils.isEmpty(showAddressActivityOrderSosPublish.getText().toString().trim())) {
            ToastUtils.showShortToast(getBaseActivityContext(), " 请选择您的地址");
            return;
        }
        if (StringUtils.isEmpty(showTimeActivityOrderSosPublish.getText().toString().trim())) {
            ToastUtils.showShortToast(getBaseActivityContext(), " 请选择服务就位时间");
            return;
        }
        if (StringUtils.isEmpty(showPenalSumActivityOrderSosPublish.getText().toString().trim())) {
            ToastUtils.showShortToast(getBaseActivityContext(), " 请选择违约金比例");
            return;
        }

        if (priceActivityOrderSosPublish.getCurrentNumber()==0){
            ToastUtils.showShortToast(getBaseActivityContext(), " 请选择服务价格");
            return;
        }
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        JSONArray picture = new JSONArray();
        for (int i = 0; i < listPhoto.size(); i++) {
            if (!"default".equals(listPhoto.get(i))) {
                picture.put(ConvertUtils.bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(listPhoto.get(i),
                        getWindowManager().getDefaultDisplay().getWidth(),
                        getWindowManager().getDefaultDisplay().getHeight())));
            }
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.publisSOS(
                    new JSONObject()
                            .put("mobile", mobileNameActivityOrderSosPublish.getText().toString().trim())
                            .put("recipients", peopleNameActivityOrderSosPublish.getText().toString().trim())
                            .put("vehicle_name", carActivityOrderSOSPublish.getText().toString().trim())
                            .put("member_id", Config.member_id)
                            .put("title", showFaultActivityOrderSosPublish.getText().toString().trim())
                            .put("type", showTypeActivityOrderSosPublish.getText().toString().trim())
                            .put("gps", lat + "," + lng)
                            .put("price", priceActivityOrderSosPublish.getCurrentNumber())
                            .put("service_time", showTimeActivityOrderSosPublish.getText().toString().trim())
                            .put("damages", showPenalSumActivityOrderSosPublish.getText().toString().trim())
                            .put("address", showAddressActivityOrderSosPublish.getText().toString().trim())
                            .put("remark", showOtherActivityOrderSosPublish.getText().toString().trim())
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
                                    tipActivityOrderSosPublish.setText(jsonObject.getJSONObject("data").getString("contents"));
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
     * 获取用户默认车辆
     */
    public void defaultVehicle() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.defaultVehicle(
                    new JSONObject()
                            .put("member_id", Config.member_id)
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
                                    peopleNameActivityOrderSosPublish.setText(jsonObject.getJSONObject("data").getString("username"));
                                    mobileNameActivityOrderSosPublish.setText(jsonObject.getJSONObject("data").getString("mobile"));
                                    carActivityOrderSOSPublish.setText(jsonObject.getJSONObject("data").getString("vehicle_name"));
                                } else {
                                    peopleNameActivityOrderSosPublish.setText("");
                                    mobileNameActivityOrderSosPublish.setText("");
                                    carActivityOrderSOSPublish.setText("");
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:SOS-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:SOS-0");
            e.printStackTrace();
        }
    }


    /**
     * 获取违约金比例
     */
    public void integrityMoney() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();

        if (integrityMoneyList.size() == 0) {
            try {
                addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.orderAttr(
                        new JSONObject()
                                .put("member_id", Config.member_id)
                                .put("type", "2")//1送达时效，2违约金比例，3求助类型，4服务就位
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
                                        parseToIntegrityMoneyList(jsonObject);
                                        if (list.size() > 0) {
                                            integrityMoneyAdapter.setList(integrityMoneyList);
                                            menuDialog.createIntegrityBottomDialog("违约金比例", getBaseActivityContext(), integrityMoneyAdapter);
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
        } else {
            if (ywLoadingDialog != null) {
                ywLoadingDialog.disMiss();
            }
            integrityMoneyAdapter.setList(integrityMoneyList);
            menuDialog.createIntegrityBottomDialog("违约金比例", getBaseActivityContext(), integrityMoneyAdapter);
        }

    }

    void parseToIntegrityMoneyList(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        if (jsonArray.length() > 0) {
            integrityMoneyList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                IntegrityMoneyModel integrityMoneyModel = new IntegrityMoneyModel();
                integrityMoneyModel.attr_id = jsonArray.getJSONObject(i).getString("attr_id");
                integrityMoneyModel.is_check = jsonArray.getJSONObject(i).getString("is_check");
                integrityMoneyModel.name = jsonArray.getJSONObject(i).getString("name");
                if ("1".equals(integrityMoneyModel.is_check)) {
                    showPenalSumActivityOrderSosPublish.setText(integrityMoneyModel.name);
                }
                integrityMoneyList.add(integrityMoneyModel);
            }

        }
    }

    /**
     * 获取就位时间
     */
    public void serviceTime() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();

        if (serviceTimeList.size() == 0) {
            try {
                addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.orderAttr(
                        new JSONObject()
                                .put("member_id", Config.member_id)
                                .put("type", "4")//1送达时效，2违约金比例，3求助类型，4服务就位
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
                                        parseToServiceTimeList(jsonObject);
                                        if (list.size() > 0) {
                                            serviceTimeAdapter.setList(serviceTimeList);
                                            menuDialog.createIntegrityBottomDialog("就位时间", getBaseActivityContext(), serviceTimeAdapter);
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
        } else {
            if (ywLoadingDialog != null) {
                ywLoadingDialog.disMiss();
            }
            serviceTimeAdapter.setList(serviceTimeList);
            menuDialog.createIntegrityBottomDialog("就位时间", getBaseActivityContext(), serviceTimeAdapter);
        }

    }

    void parseToServiceTimeList(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        if (jsonArray.length() > 0) {
            serviceTimeList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                IntegrityMoneyModel integrityMoneyModel = new IntegrityMoneyModel();
                integrityMoneyModel.attr_id = jsonArray.getJSONObject(i).getString("attr_id");
                integrityMoneyModel.is_check = jsonArray.getJSONObject(i).getString("is_check");
                integrityMoneyModel.name = jsonArray.getJSONObject(i).getString("name");
                if ("1".equals(integrityMoneyModel.is_check)) {
                    showTimeActivityOrderSosPublish.setText(integrityMoneyModel.name);
                }
                serviceTimeList.add(integrityMoneyModel);
            }

        }
    }

    /**
     * 获取求助类型
     */
    public void serviceType() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();

        if (serviceTypeList.size() == 0) {
            try {
                addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.orderAttr(
                        new JSONObject()
                                .put("member_id", Config.member_id)
                                .put("type", "3")//1送达时效，2违约金比例，3求助类型，4服务就位
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
                                        parseToServiceTypeList(jsonObject);
                                        if (list.size() > 0) {
                                            serviceTypeAdapter.setList(serviceTypeList);
                                            menuDialog.createIntegrityBottomDialog("求助类型", getBaseActivityContext(), serviceTypeAdapter);
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
        } else {
            if (ywLoadingDialog != null) {
                ywLoadingDialog.disMiss();
            }
            serviceTypeAdapter.setList(serviceTypeList);
            menuDialog.createIntegrityBottomDialog("求助类型", getBaseActivityContext(), serviceTypeAdapter);
        }

    }

    void parseToServiceTypeList(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        if (jsonArray.length() > 0) {
            serviceTypeList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                IntegrityMoneyModel integrityMoneyModel = new IntegrityMoneyModel();
                integrityMoneyModel.attr_id = jsonArray.getJSONObject(i).getString("attr_id");
                integrityMoneyModel.is_check = jsonArray.getJSONObject(i).getString("is_check");
                integrityMoneyModel.name = jsonArray.getJSONObject(i).getString("name");
                if ("1".equals(integrityMoneyModel.is_check)) {
                    showTypeActivityOrderSosPublish.setText(integrityMoneyModel.name);
                }
                serviceTypeList.add(integrityMoneyModel);
            }

        }
    }
}
