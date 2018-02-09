package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by leilei on 2017/12/12.
 */

public class ActivityMyDataCarAdd extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.click_database)
    TextView clickDatabase;
    @BindView(R.id.et_one)
    EditText etOne;
    @BindView(R.id.et_two)
    EditText etTwo;
    @BindView(R.id.et_three)
    EditText etThree;
    @BindView(R.id.et_four)
    EditText etFour;
    @BindView(R.id.et_five)
    EditText etFive;
    @BindView(R.id.click_save)
    TextView clickSave;
    @BindView(R.id.activity_my_data_car_add)
    LinearLayout activityMyDataCarAdd;
    YWLoadingDialog ywLoadingDialog;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }


    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        clickDatabase = null;
        etOne = null;
        etTwo = null;
        etThree = null;
        etFour = null;
        etFive = null;
        clickSave = null;
        activityMyDataCarAdd = null;

        super.onDestroy();

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CarName event) {
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data_car_add);
        ButterKnife.bind(this);
        customInit(activityMyDataCarAdd, false, true, true);
        titleTop.setText("添加爱车");

    }

    @OnClick({R.id.back_top, R.id.click_database, R.id.click_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.click_database:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataCarCarList.class));
                }
                break;
            case R.id.click_save:
                add_vehicle();
                break;
        }
    }

    public void add_vehicle() {
        if (StringUtils.isEmpty(etOne.getText().toString().trim()) ||
                StringUtils.isEmpty(etTwo.getText().toString().trim()) ||
                StringUtils.isEmpty(etThree.getText().toString().trim()) ||
                StringUtils.isEmpty(etFour.getText().toString().trim()) ||
                StringUtils.isEmpty(etFive.getText().toString().trim())){
            ToastUtils.showShortToast(getBaseActivityContext(),"请填写完整");
            return;
        }
            if (ywLoadingDialog != null) {
                ywLoadingDialog.dismiss();
            }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.add_vehicle(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("name", etOne.getText().toString().trim())
                            .put("type", etTwo.getText().toString().trim())
                            .put("displacement", etThree.getText().toString().trim())
                            .put("year", etFour.getText().toString().trim())
                            .put("style", etFive.getText().toString().trim())
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.dismiss();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.dismiss();
                            }
                            try {
                                final JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    EventBus.getDefault().post(new CreateCarModel());
                                    EventBus.getDefault().post(new CarName(jsonObject.getJSONObject("data").getString("vehicle_name")));
                                    finish();
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-2");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }
}
