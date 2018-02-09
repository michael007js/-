package com.sss.car.view;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.AdressDataChoose.TimePicker;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedSet;
import com.sss.car.EventBusModel.ChangedTime;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.rongyun.RongYunUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 勿扰时段选择时间
 * Created by leilei on 2017/11/7.
 */

public class ActivityMyDataSynthesizeSettingDoNotDisturbChooseTime extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.from_)
    TextView from;
    @BindView(R.id.to_)
    TextView to;
    @BindView(R.id.activity_my_data_synthesize_setting_do_not_disturb_time)
    LinearLayout activityMyDataSynthesizeSettingDoNotDisturbTime;
    TimePicker timePicker;
    String start, end;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    YWLoadingDialog ywLoadingDialog;

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
        from = null;
        to = null;
        activityMyDataSynthesizeSettingDoNotDisturbTime = null;
        timePicker = null;
        start = null;
        end = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data_synthesize_setting_do_not_disturb_choose_time);
        ButterKnife.bind(this);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递失败");
            finish();
        }
        customInit(activityMyDataSynthesizeSettingDoNotDisturbTime, false, true, false);
        titleTop.setText("勿扰时段");
        start = getIntent().getExtras().getString("start");
        end = getIntent().getExtras().getString("end");
        from.setText("从" + start);
        to.setText("至" + end);
        rightButtonTop.setText("保存");
    }

    @OnClick({R.id.back_top, R.id.from_, R.id.right_button_top, R.id.to_})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.from_:
                createDialog(1);
                break;
            case R.id.to_:
                createDialog(2);
                break;
            case R.id.right_button_top:
                if (StringUtils.isEmpty(start) || StringUtils.isEmpty(end)) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请选择起始时间");
                    return;

                }
                setUsinfo();
                break;
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
                            .put("start_time", start)
                            .put("end_time", end)
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
                                    ChangedTime changedTime = new ChangedTime(start, end);
                                    EventBus.getDefault().post(changedTime);
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

    /**
     * 创建时间选择器
     *
     * @param from
     */
    void createDialog(final int from) {
        if (timePicker != null) {
            timePicker.clear();
        }
        timePicker = null;
        timePicker = new TimePicker(getBaseActivityContext(), false);
        timePicker.setTimListener(new TimePicker.OnTimeCListener() {
            @Override
            public void onDateSelected(String hour, String min, String sec) {
                timePicker.dismiss();
            }

            @Override
            public void onDateSelected(String hour, String min) {
                timePicker.dismiss();
                if (from == 1) {

                    if (!StringUtils.isEmpty(end)) {
                        if (TimeUtils.string2Millis(end, "HH:mm") > TimeUtils.string2Millis(hour + ":" + min, "HH:mm")) {
                            start = hour + ":" + min;
                        } else {
                            ToastUtils.showShortToast(getBaseActivityContext(), "结束时间必须大于开始时间");
                            return;
                        }
                    }
                    start = hour + ":" + min;
                    ActivityMyDataSynthesizeSettingDoNotDisturbChooseTime.this.from.setText("从" + start);
                } else {
                    if (!StringUtils.isEmpty(start)) {
                        if (TimeUtils.string2Millis(hour + ":" + min, "HH:mm") > TimeUtils.string2Millis(start, "HH:mm")) {
                            end = hour + ":" + min;
                        } else {
                            ToastUtils.showShortToast(getBaseActivityContext(), "结束时间必须大于开始时间");
                            return;
                        }
                    }
                    end = hour + ":" + min;
                    to.setText("至" + end);
                }
            }
        });
        timePicker.show();
    }
}
