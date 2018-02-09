package com.sss.car.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.EventBusModel.CarSearch;
import com.sss.car.EventBusModel.CreateCarModel;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.adapter.CarSearchAdapter;
import com.sss.car.dao.CarSearchClickCallBack;
import com.sss.car.model.CarSearchModel;
import com.sss.car.model.ChildCarModel;

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
 * Created by leilei on 2017/8/18.
 */

public class ActivityMyDataCarListSearch extends BaseActivity implements CarSearchClickCallBack {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.top_parent)
    LinearLayout topParent;
    @BindView(R.id.list_activity_my_data_car_carlist_search)
    ListView listActivityMyDataCarCarlistSearch;
    @BindView(R.id.activity_my_data_car_carlist_search)
    LinearLayout activityMyDataCarCarlistSearch;
    @BindView(R.id.input_activity_my_data_car_carlist_search)
    EditText inputActivityMyDataCarCarlistSearch;
    CarSearchAdapter carSearchAdapter;
    List<CarSearchAdapter> carSearchAdapterList = new ArrayList<>();
    List<CarSearchModel> carSearchModelList = new ArrayList<>();

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        rightButtonTop = null;
        topParent = null;
        listActivityMyDataCarCarlistSearch = null;
        activityMyDataCarCarlistSearch = null;
        inputActivityMyDataCarCarlistSearch = null;
        if (carSearchModelList != null) {
            carSearchModelList.clear();
        }
        carSearchModelList = null;
        if (carSearchAdapterList != null) {
            for (int i = 0; i < carSearchAdapterList.size(); i++) {
                carSearchAdapterList.get(i).clear();
            }
            carSearchAdapterList.clear();
        }
        carSearchAdapterList = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data_car_carlist_search);
        ButterKnife.bind(this);
        customInit(activityMyDataCarCarlistSearch, false, true, true);
        titleTop.setText("品牌搜索");
        inputActivityMyDataCarCarlistSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!StringUtils.isEmpty(inputActivityMyDataCarCarlistSearch.getText().toString().trim()))
                    searchCar(inputActivityMyDataCarCarlistSearch.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CreateCarModel event) {
        finish();
    }

    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }


    /**
     * 搜索品牌
     *
     * @param keywords
     */
    void searchCar(String keywords) {
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.searchCar(
                    new JSONObject()
                            .put("keywords", keywords)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (getBaseActivityContext() != null) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "服务器访问错误");
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (StringUtils.isEmpty(response)) {
                                if (getBaseActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "服务器返回错误");
                                }
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        carSearchModelList.clear();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            carSearchModelList.add(
                                                    new CarSearchModel(jsonArray.getJSONObject(i).getString("brand_id"),
                                                            jsonArray.getJSONObject(i).getString("logo"),
                                                            jsonArray.getJSONObject(i).getString("name"))
                                            );
                                        }
                                        if (getBaseActivityContext() != null) {
                                            carSearchAdapter = new CarSearchAdapter(carSearchModelList, getBaseActivityContext(), ActivityMyDataCarListSearch.this);
                                            carSearchAdapterList.add(carSearchAdapter);
                                            listActivityMyDataCarCarlistSearch.setAdapter(carSearchAdapter);
                                        }
                                    } else {
                                        if (carSearchAdapterList != null) {
                                            listActivityMyDataCarCarlistSearch.setAdapter(null);
                                        }
                                    }
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:search car-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:search car-0");
            }
            e.printStackTrace();
        }
    }

    /**
     * 点击回调
     *
     * @param carSearchModel
     */
    @Override
    public void onClickCar(CarSearchModel carSearchModel) {
        EventBus.getDefault().post(new CarSearch(carSearchModel.brand_id, carSearchModel.logo, carSearchModel.name));
        finish();
    }
}
