package com.sss.car.view;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedSet;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.SettingModel;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


/**
 * 设置视频自动播放
 * Created by leilei on 2017/11/7.
 */

public class ActivityMyDataSynthesizeSettingAutoVideo extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.never)
    CheckBox never;
    @BindView(R.id.click_never)
    LinearLayout clickNever;
    @BindView(R.id.wifi)
    CheckBox wifi;
    @BindView(R.id.click_wifi)
    LinearLayout clickWifi;
    @BindView(R.id.activity_my_data_synthesize_setting_auto_video)
    LinearLayout activityMyDataSynthesizeSettingAutoVideo;
    String request;

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
        setContentView(R.layout.activity_my_data_synthesize_setting_auto_video);
        ButterKnife.bind(this);
        customInit(activityMyDataSynthesizeSettingAutoVideo, false, true, false);
        titleTop.setText("视频自动播放");
        rightButtonTop.setText("保存");
        never.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                never.setChecked(true);
                wifi.setChecked(false);
                request = "0";
            }
        });
        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                never.setChecked(false);
                wifi.setChecked(true);
                request = "1";
            }
        });


        getUsinfo();
    }

    @OnClick({R.id.back_top, R.id.right_button_top, R.id.click_never, R.id.click_wifi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                if (StringUtils.isEmpty(request)){
                    ToastUtils.showShortToast(getBaseActivityContext(),"请选择类别");
                    return;
                }
                setUsinfo();
                break;
            case R.id.click_never:
                never.setChecked(true);
                wifi.setChecked(false);
                request = "0";
                break;
            case R.id.click_wifi:
                never.setChecked(false);
                wifi.setChecked(true);
                request = "1";
                break;
        }
    }

    /**
     * 获取用户设置资料
     */
    public void getUsinfo() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getUsinfo(
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
                                    SettingModel settingModel = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), SettingModel.class);
                                    request=settingModel.video;
                                    if ("1".equals(settingModel.video)) {
                                        wifi.setChecked(true);
                                        never.setChecked(false);
                                    } else if ("0".equals(settingModel.video)) {
                                        never.setChecked(true);
                                        wifi.setChecked(false);
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


    /**
     * 获取用户设置资料
     */
    public void setUsinfo() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.setUsinfo(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("video", request)
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
                                    EventBus.getDefault().post(new ChangedSet());
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
