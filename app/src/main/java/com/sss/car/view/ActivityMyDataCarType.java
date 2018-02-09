package com.sss.car.view;

import android.content.Intent;
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
import com.sss.car.EventBusModel.CarName;
import com.sss.car.EventBusModel.CreateCarModel;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.adapter.CarDisplacementYearTypePublicAdapter;
import com.sss.car.dao.MyCarDisplacementYearTypeClickCallBack;
import com.sss.car.model.CarDisplacementYearTypePublicModel;

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
 * Created by leilei on 2017/8/17.
 */

public class ActivityMyDataCarType extends BaseActivity implements MyCarDisplacementYearTypeClickCallBack {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.logo_activity_my_data_car_car_type)
    ImageView logoActivityMyDataCarCarType;
    @BindView(R.id.name_activity_my_data_car_car_type)
    TextView nameActivityMyDataCarCarType;
    @BindView(R.id.list_activity_my_data_car_car_type)
    ListView listActivityMyDataCarCarType;
    @BindView(R.id.activity_my_data_car_car_type)
    LinearLayout activityMyDataCarCarType;
    YWLoadingDialog ywLoadingDialog;
    CarDisplacementYearTypePublicAdapter carDisplacementYearTypePublicAdapter;
    List<CarDisplacementYearTypePublicModel> list = new ArrayList<>();

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
        logoActivityMyDataCarCarType = null;
        nameActivityMyDataCarCarType = null;
        listActivityMyDataCarCarType = null;
        activityMyDataCarCarType = null;
        if (carDisplacementYearTypePublicAdapter != null) {
            carDisplacementYearTypePublicAdapter.clear();
        }
        carDisplacementYearTypePublicAdapter = null;
        if (list != null) {
            list.clear();
        }
        list = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data_car_car_type);
        ButterKnife.bind(this);
        customInit(activityMyDataCarCarType, false, true, false);
        titleTop.setText("车款选择");
        if (getIntent() == null || getIntent().getExtras() == null) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据传输错误!");
            }
            finish();
        }
        carDisplacementYearTypePublicAdapter = new CarDisplacementYearTypePublicAdapter(list, getBaseActivityContext(), this);
        listActivityMyDataCarCarType.setAdapter(carDisplacementYearTypePublicAdapter);
        logoActivityMyDataCarCarType.setTag(R.id.glide_tag, Config.url + getIntent().getExtras().getString("carLogo"));
        if (getBaseActivityContext() != null) {
            addImageViewList(GlidUtils.downLoader(false, logoActivityMyDataCarCarType, getBaseActivityContext()));
        }
        nameActivityMyDataCarCarType.setText(getIntent().getExtras().getString("carType") + getIntent().getExtras().getString("carLabel"));
        getType(getIntent().getExtras().getString("id"), getIntent().getExtras().getString("carLogo"));
    }

    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }


    /**
     * 获取款式
     *
     * @param id
     */
    void getType(String id, final String logo) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.chooseCarType(
                    new JSONObject()
                            .put("property_id", id)
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
                                            CarDisplacementYearTypePublicModel carDisplacementYearTypePublicModel = new CarDisplacementYearTypePublicModel();
                                            carDisplacementYearTypePublicModel.id = jsonArray.getJSONObject(i).getString("style_id");
                                            carDisplacementYearTypePublicModel.name = jsonArray.getJSONObject(i).getString("name");
                                            carDisplacementYearTypePublicModel.logo = logo;
                                            list.add(carDisplacementYearTypePublicModel);
                                        }
                                        carDisplacementYearTypePublicAdapter.refresh(list);
                                    } else {
                                        if (getBaseActivityContext() != null) {
                                            ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                        }
                                    }
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:car year-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:car year-0");
            }
            e.printStackTrace();
        }
    }

    @Override
    public void onClickCar(String id, String name, String logo) {
        createCar(id, name, logo);
    }



    /**
     * 创建车辆
     *
     * @param id
     * @param name
     * @param logo
     */
    void createCar(String id, String name, String logo) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = null;
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.createCar(
                    new JSONObject()
                            .put("name", getIntent().getExtras().getString("carType"))
                            .put("brand", logo)
                            .put("type", getIntent().getExtras().getString("carLabel"))
                            .put("displacement", getIntent().getExtras().getString("carDisplacement"))
                            .put("year", getIntent().getExtras().getString("carYear"))
                            .put("ids", getIntent().getExtras().getString("ids") + "," + id)
                            .put("style",name)
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
                                        if (getBaseActivityContext() != null) {
                                            EventBus.getDefault().post(new CreateCarModel());
                                            EventBus.getDefault().post(new CarName(jsonObject.getJSONObject("data").getString("vehicle_name")));
                                            startActivity(new Intent(getBaseActivityContext(), ActivityMyDataCarGarage.class));
                                        }
                                        finish();
                                    } else {
                                        if (getBaseActivityContext() != null) {
                                            ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                        }
                                    }
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:create car-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:create car-0");
            }
            e.printStackTrace();
        }
    }
}
