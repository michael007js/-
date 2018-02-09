package com.sss.car.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.FlycoDialog.dialog.listener.OnOperItemClickL;
import com.blankj.utilcode.customwidget.Dialog.FlycoDialog.dialog.widget.ActionSheetDialog;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.CarDelete;
import com.sss.car.EventBusModel.CarModel;
import com.sss.car.EventBusModel.CarName;
import com.sss.car.EventBusModel.CreateCarModel;
import com.sss.car.EventBusModel.ShowNullCarModel;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.adapter.CarGarageAdapter;
import com.sss.car.dao.CarGarageCallBack;
import com.sss.car.model.CarGarageModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


/**
 * Created by leilei on 2017/8/17.
 */

public class ActivityMyDataCarGarage extends BaseActivity implements CarGarageCallBack, LoadImageCallBack {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.list_activity_my_data_car_garage)
    InnerListview listActivityMyDataCarGarage;
    @BindView(R.id.activity_my_data_car_garage)
    LinearLayout activityMyDataCarGarage;
    YWLoadingDialog ywLoadingDialog;
    List<CarGarageModel> list = new ArrayList<>();
    CarGarageAdapter carGarageAdapter;
    @BindView(R.id.add)
    TextView add;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        listActivityMyDataCarGarage = null;
        activityMyDataCarGarage = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (list != null) {
            list.clear();
        }
        list = null;
        if (carGarageAdapter != null) {
            carGarageAdapter.clear();
        }
        carGarageAdapter = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data_car_garage);
        ButterKnife.bind(this);
        customInit(activityMyDataCarGarage, false, true, true);
        carGarageAdapter = new CarGarageAdapter(list, getBaseActivityContext(), this, this);
        listActivityMyDataCarGarage.setAdapter(carGarageAdapter);
        titleTop.setText("我的车库");
        try {
            requestmyCar(1);
        } catch (JSONException e) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:my car-0");
            }
            e.printStackTrace();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CarName event) {
        if (event.close){
            finish();
        }
    }


    @OnClick({R.id.back_top,R.id.add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataCarAdd.class));
                }
                break;
            case R.id.back_top:
                finish();
                break;

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CreateCarModel event) {
        finish();
    }

    /**
     * 请求我的车辆信息
     *
     * @param mode 1默认与用户点击设为默认后调用,2用户点击删除后调用
     * @throws JSONException
     */
    void requestmyCar(final int mode) throws JSONException {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.myCar(
                new JSONObject()
                        .put("member_id", Config.member_id).toString(), new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (getBaseActivityContext() != null) {
                            ToastUtils.showShortToast(getBaseActivityContext(), "服务器访问错误");
                        }
                        if (ywLoadingDialog != null) {
                            ywLoadingDialog.disMiss();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (ywLoadingDialog != null) {
                            ywLoadingDialog.disMiss();
                        }
                        if (StringUtils.isEmpty(response)) {
                            if (getBaseActivityContext() != null) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "服务器返回错误");
                            }
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    list.clear();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        CarGarageModel carGarageModel = new CarGarageModel();
                                        carGarageModel.vehicle_id = jsonArray.getJSONObject(i).getString("vehicle_id");
                                        carGarageModel.name = jsonArray.getJSONObject(i).getString("name");
                                        carGarageModel.brand = jsonArray.getJSONObject(i).getString("brand");
                                        carGarageModel.type = jsonArray.getJSONObject(i).getString("type");
                                        carGarageModel.displacement = jsonArray.getJSONObject(i).getString("displacement");
                                        carGarageModel.year = jsonArray.getJSONObject(i).getString("year");
                                        carGarageModel.ids = jsonArray.getJSONObject(i).getString("ids");
                                        carGarageModel.create_time = jsonArray.getJSONObject(i).getString("create_time");
                                        carGarageModel.is_default = jsonArray.getJSONObject(i).getString("is_default");
                                        carGarageModel.member_id = jsonArray.getJSONObject(i).getString("member_id");
                                        list.add(carGarageModel);
                                    }
                                    carGarageAdapter.refresh(list);
                                    if (mode == 2) {
                                        if (list.size() < 1) {
                                            EventBus.getDefault().post(new ShowNullCarModel());
                                            finish();
                                        }
                                    }


                                } else {
                                    listActivityMyDataCarGarage.setAdapter(null);
                                    EventBus.getDefault().post(new ShowNullCarModel());
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    }
                                }
                            } catch (JSONException e) {
                                if (getBaseActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:my car-0");
                                }
                                e.printStackTrace();
                            }
                        }
                    }

                })));
    }

    /**
     * 删除车辆回调
     *
     * @param id
     */
    @Override
    public void onDeleteCar(String id) {
        if (getBaseActivityContext() != null) {
            createDeleteDialog(id, weakReference);
        }
    }


    /**
     * 设为默认回调
     *
     * @param carGarageModel
     */
    @Override
    public void onDefault(CarGarageModel carGarageModel) {
        if ("1".equals(carGarageModel.is_default)) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "您选择的车辆已经设为默认!");
            }
        } else {
            chooseCar(carGarageModel.vehicle_id);
        }
    }

    @Override
    public void onClick(CarGarageModel carGarageModel) {

        if (getIntent()!=null){
            if (getIntent().getExtras()!=null){
                if ("fromSOS".equals(getIntent().getExtras().getString("where"))){
                    EventBus.getDefault().post(new CarModel(carGarageModel.name+carGarageModel.type));
                 LogUtils.e(carGarageModel.toString());
                    finish();
                }
            }
        }
    }

    /**
     * 图片显示回调
     *
     * @param imageView
     */
    @Override
    public void onLoad(ImageView imageView) {
        addImageViewList(imageView);
    }


    /**
     * 创建删除对话框
     *
     * @param weakReference
     */
    void createDeleteDialog(final String carid, final WeakReference<Context> weakReference) {
        if (getBaseActivityContext() == null) {
            return;
        }
        String[] stringItems = {"删除"};
        if (getBaseActivityContext() != null) {
            final ActionSheetDialog dialog = new ActionSheetDialog(getBaseActivityContext(), stringItems, null)
                    .isTitleShow(true)
                    .itemTextColor(Color.parseColor("#e83e41"))
                    .setmCancelBgColor(Color.parseColor("#e83e41"))
                    .cancelText(Color.WHITE);
            dialog.title("是否要删除您的爱车");
            dialog.titleTextSize_SP(14.5f).show();
            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            dialog.dismiss();
                            delete(carid);
                            break;
                    }
                }
            });
        }
    }

    /**
     * 删除爱车
     *
     * @param id
     */
    void delete(String id) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.deleteCar(
                    new JSONObject()
                            .put("vehicle_id", id)
                            .put("member_id", Config.member_id)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (getBaseActivityContext() != null) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "服务器访问错误");
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (StringUtils.isEmpty(response)) {
                                if (getBaseActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "服务器返回错误");
                                }
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        requestmyCar(2);
                                        EventBus.getDefault().post(new CarDelete());
                                    } else {
                                        if (getBaseActivityContext() != null) {
                                            ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                        }
                                    }
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:choose car-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:choose car-0");
            }
            e.printStackTrace();
        }
    }


    /**
     * 选择车辆(设为默认)
     *
     * @param id
     */
    void chooseCar(String id) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.chooseCar(
                    new JSONObject()
                            .put("vehicle_id", id)
                            .put("member_id", Config.member_id)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (getBaseActivityContext() != null) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "服务器访问错误");
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (StringUtils.isEmpty(response)) {
                                if (getBaseActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "服务器返回错误");
                                }
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        EventBus.getDefault().post(new CarName(jsonObject.getJSONObject("data").getString("vehicle_name"),false));
                                        requestmyCar(1);
                                    } else {
                                        if (getBaseActivityContext() != null) {
                                            ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                        }
                                    }
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:choose car-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:choose car-0");
            }
            e.printStackTrace();
        }
    }


}
