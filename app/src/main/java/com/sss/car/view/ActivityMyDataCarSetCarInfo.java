package com.sss.car.view;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.adapter.CarSetCarinfoAdapter;
import com.sss.car.dao.CarSearchClickCallBack;
import com.sss.car.dao.CarSetCarinfoCallBack;
import com.sss.car.model.CarSearchModel;
import com.sss.car.model.CarSetCarInfoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static android.R.attr.type;

/**
 * Created by leilei on 2017/8/18.
 */

public class ActivityMyDataCarSetCarInfo extends BaseActivity implements CarSetCarinfoCallBack {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.top_parent)
    LinearLayout topParent;
    @BindView(R.id.logo_activity_my_data_car_set_car_info)
    ImageView logoActivityMyDataCarSetCarInfo;
    @BindView(R.id.name_activity_my_data_car_set_car_info)
    TextView nameActivityMyDataCarSetCarInfo;
    @BindView(R.id.text_activity_my_data_car_set_car_info)
    TextView textActivityMyDataCarSetCarInfo;
    @BindView(R.id.list_activity_my_data_car_set_car_info)
    ListView listActivityMyDataCarSetCarInfo;
    @BindView(R.id.activity_my_data_car_set_car_info)
    LinearLayout activityMyDataCarSetCarInfo;
    YWLoadingDialog ywLoadingDialog;
    CarSetCarinfoAdapter carSetCarinfoAdapter;
    List<CarSetCarInfoModel> list = new ArrayList<>();
    String jumpMode;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data_car_set_car_info);
        ButterKnife.bind(this);
        customInit(activityMyDataCarSetCarInfo, false, true, false);
        if (getIntent() == null || getIntent().getExtras() == null) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据传输错误!");
            }
            finish();
        }
        logoActivityMyDataCarSetCarInfo.setTag(R.id.glide_tag, Config.url + getIntent().getExtras().getString("logo"));
        nameActivityMyDataCarSetCarInfo.setText(getIntent().getExtras().getString("name") + getIntent().getExtras().getString("type"));
        if (getBaseActivityContext() != null) {
            addImageViewList(GlidUtils.downLoader(false, logoActivityMyDataCarSetCarInfo, getBaseActivityContext()));
        }

        switch (getIntent().getExtras().getString("jumpMode")) {
            case "1":
                textActivityMyDataCarSetCarInfo.setText("请选择发动机排量");
                titleTop.setText("排量选择");
                jumpMode = "1";
                break;
            case "2":
                textActivityMyDataCarSetCarInfo.setText("请选择年份");
                titleTop.setText("年份选择");
                jumpMode = "2";
                break;
            case "3":
                textActivityMyDataCarSetCarInfo.setText("请选择车款");
                titleTop.setText("选择款式");
                jumpMode = "3";
                break;
        }
        getChangeCarInfo();
    }

    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        rightButtonTop = null;
        topParent = null;
        logoActivityMyDataCarSetCarInfo = null;
        nameActivityMyDataCarSetCarInfo = null;
        textActivityMyDataCarSetCarInfo = null;
        listActivityMyDataCarSetCarInfo = null;
        activityMyDataCarSetCarInfo = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (carSetCarinfoAdapter != null) {
            carSetCarinfoAdapter.clear();
        }
        if (list != null) {
            list.clear();
        }
        list = null;
        super.onDestroy();
    }


    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }


    /**
     * 请求我的默认车辆信息
     */
    public void getChangeCarInfo() {

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getChangeCarInfo(
                    new JSONObject()
                            .put("ids", getIntent().getExtras().getString("ids"))
                            .put("type", getIntent().getExtras().getString("jumpMode"))
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
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            CarSetCarInfoModel carSetCarInfoModel = new CarSetCarInfoModel();
                                            carSetCarInfoModel.name = jsonArray.getJSONObject(i).getString("name");
                                            carSetCarInfoModel.id = jsonArray.getJSONObject(i).getString("id");
                                            list.add(carSetCarInfoModel);
                                        }
                                        if (getBaseActivityContext() != null) {
                                            carSetCarinfoAdapter = new CarSetCarinfoAdapter(list, getBaseActivityContext(), ActivityMyDataCarSetCarInfo.this);
                                            listActivityMyDataCarSetCarInfo.setAdapter(carSetCarinfoAdapter);
                                        }
                                    } else {
                                        if (getBaseActivityContext() != null) {
                                            ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                        }
                                    }
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: get car info-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: get car info-0");
            }
            e.printStackTrace();
        }
    }


    /**
     * 请求我的默认车辆信息
     */
    public void updateCurrentCarInfo(CarSetCarInfoModel carSetCarInfoModel) {

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        }
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.updateCurrentCarInfo(
                    new JSONObject()
                            .put("ids", getIntent().getExtras().getString("ids"))
                            .put("vehicle_id", getIntent().getExtras().getString("id"))
                            .put("name", carSetCarInfoModel.name)
                            .put("id", carSetCarInfoModel.id)
                            .put("type", jumpMode)
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
                                        finish();
                                    } else {
                                        if (getBaseActivityContext() != null) {
                                            ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                        }
                                    }
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: change car info-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: change car info-0");
            }
            e.printStackTrace();
        }
    }

    /**
     * 属性点击回调
     *
     * @param carSetCarInfoModel
     */
    @Override
    public void onClickCarinfo(CarSetCarInfoModel carSetCarInfoModel) {
        updateCurrentCarInfo(carSetCarInfoModel);
    }
}
