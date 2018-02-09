package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.AdressDataChoose.TimePicker;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.SwitchButton.SwitchButton;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedSet;
import com.sss.car.EventBusModel.ChangedTime;
import com.sss.car.R;
import com.sss.car.RequestWeb;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static android.R.attr.value;

/**
 * 勿扰时段
 * Created by leilei on 2017/11/7.
 */

public class ActivityMyDataSynthesizeSettingDoNotDisturb extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.from_)
    TextView from;
    @BindView(R.id.to_)
    TextView to;
    @BindView(R.id.activity_my_data_synthesize_setting_do_not)
    LinearLayout activityMyDataSynthesizeSettingDoNotDisturb;
    String start, end;
    @BindView(R.id.switch_time)
    SwitchButton switchTime;
    @BindView(R.id.click_)
    LinearLayout click;
    YWLoadingDialog ywLoadingDialog;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }


    @Subscribe
    public void onMessageEvent(ChangedTime changedTime) {
        start = changedTime.startTime;
        end = changedTime.endTime;
        from.setText("从"+start);
        to.setText("至"+end);

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog!=null){
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog=null;
        backTop = null;
        titleTop = null;
        from = null;
        to = null;
        activityMyDataSynthesizeSettingDoNotDisturb = null;
        start = null;
        end = null;
        switchTime = null;
        click = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data_synthesize_setting_do_not_disturb);
        ButterKnife.bind(this);
        customInit(activityMyDataSynthesizeSettingDoNotDisturb, false, true, true);
        titleTop.setText("勿扰时段");
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递失败");
            finish();
        }
        start = getIntent().getExtras().getString("start");
        end = getIntent().getExtras().getString("end");
        from.setText("从"+start);
        to.setText("至"+end);
        if ("1".equals(getIntent().getExtras().getString("disturb"))){
            switchTime.setOpened(true);
        }else if ("0".equals(getIntent().getExtras().getString("disturb"))){
            switchTime.setOpened(false);
        }
        switchTime.setOnStateChangedListener(new SwitchButton.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchButton view) {
                if (StringUtils.isEmpty(start)||StringUtils.isEmpty(end)){
                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataSynthesizeSettingDoNotDisturbChooseTime.class)
                            .putExtra("start", start)
                            .putExtra("end", end)
                    );
                }else {
                    setUsinfo("1");
                }
            }

            @Override
            public void toggleToOff(SwitchButton view) {
                if (!StringUtils.isEmpty(start)&&!StringUtils.isEmpty(end)){
                    setUsinfo("0");
                }
            }
        });
    }

    @OnClick({R.id.back_top, R.id.click_})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.click_:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataSynthesizeSettingDoNotDisturbChooseTime.class)
                            .putExtra("start", start)
                            .putExtra("end", end)
                    );
                }
                break;
        }
    }


    /**
     * 获取用户设置资料
     */
    public void setUsinfo(final String value) {
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
                            .put("disturb",value)
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
                                    if ("1".equals(value)){
                                        switchTime.setOpened(true);
                                    }else {
                                        switchTime.setOpened(false);
                                    }
                                    EventBus.getDefault().post(new ChangedSet());
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
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
