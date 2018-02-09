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
import com.blankj.utilcode.util.LogUtils;
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
 * Created by leilei on 2017/8/17.
 */

public class ActivityMyDataCarDisplacement extends BaseActivity implements MyCarDisplacementYearTypeClickCallBack {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.list_activity_my_data_car_car_displacement)
    ListView listActivityMyDataCarCarDisplacement;
    @BindView(R.id.activity_my_data_car_car_displacement)
    LinearLayout activityMyDataCarCarDisplacement;
    YWLoadingDialog ywLoadingDialog;
    List<CarDisplacementYearTypePublicModel> list = new ArrayList<>();
    CarDisplacementYearTypePublicAdapter carDisplacementYearTypePublicAdapter;
    @BindView(R.id.logo_activity_my_data_car_car_displacement)
    ImageView logoActivityMyDataCarCarDisplacement;
    @BindView(R.id.name_activity_my_data_car_car_displacement)
    TextView nameActivityMyDataCarCarDisplacement;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backTop = null;
        titleTop = null;
        rightButtonTop = null;
        listActivityMyDataCarCarDisplacement = null;
        activityMyDataCarCarDisplacement = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (list != null) {
            list.clear();
        }
        list = null;
        if (carDisplacementYearTypePublicAdapter != null) {
            carDisplacementYearTypePublicAdapter.clear();
        }
        carDisplacementYearTypePublicAdapter = null;
        nameActivityMyDataCarCarDisplacement = null;
        logoActivityMyDataCarCarDisplacement = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data_car_car_displacement);
        ButterKnife.bind(this);
        customInit(activityMyDataCarCarDisplacement, false, true, true);
        titleTop.setText("排量选择");
        if (getIntent() == null || getIntent().getExtras() == null) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据传输错误!");
            }
            finish();
        }
        carDisplacementYearTypePublicAdapter = new CarDisplacementYearTypePublicAdapter(list, getBaseActivityContext(), this);
        listActivityMyDataCarCarDisplacement.setAdapter(carDisplacementYearTypePublicAdapter);
        logoActivityMyDataCarCarDisplacement.setTag(R.id.glide_tag, Config.url + getIntent().getExtras().getString("carLogo"));
        nameActivityMyDataCarCarDisplacement.setText(getIntent().getExtras().getString("carType") + getIntent().getExtras().getString("carLabel"));
        if (getBaseActivityContext() != null) {
            addImageViewList(GlidUtils.downLoader(false, logoActivityMyDataCarCarDisplacement, getBaseActivityContext()));
        }
        getDisplacement(getIntent().getExtras().getString("id"), getIntent().getExtras().getString("carLogo"));

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CarName event) {
        finish();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CreateCarModel event) {
        finish();
    }

    /**
     * 获取排量
     *
     * @param id
     */
    void getDisplacement(String id, final String logo) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.chooseCarDisplacement(
                    new JSONObject()
                            .put("series_id", id)
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
                                            carDisplacementYearTypePublicModel.id = jsonArray.getJSONObject(i).getString("property_id");
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
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:car type-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:car type-0");
            }
            e.printStackTrace();
        }
    }

    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }

    /**
     * 列表点击
     */
    @Override
    public void onClickCar(String id, String name, String logo) {
        if (getBaseActivityContext() != null) {
            startActivity(new Intent(getBaseActivityContext(), ActivityMyDataCarYear.class)
                    .putExtra("id", id)
                    .putExtra("ids", getIntent().getExtras().getString("ids") + "," + id)
                    .putExtra("carType", getIntent().getExtras().getString("carType"))
                    .putExtra("carLabel", getIntent().getExtras().getString("carLabel"))
                    .putExtra("carDisplacement", name)
                    .putExtra("carLogo", logo));
        }

    }
}
