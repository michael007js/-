package com.sss.car.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.SwitchButton.SwitchButton;
import com.blankj.utilcode.dao.OnAskDialogCallBack;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedSet;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.SettingModel;
import com.sss.car.rongyun.RongYunUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imlib.RongIMClient;
import okhttp3.Call;


/**
 * 综合设置
 * Created by leilei on 2017/8/22.
 */

@SuppressWarnings("ALL")
public class ActivityMyDataSynthesizeSetting extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.activity_my_data_synthesize_setting)
    LinearLayout activityMyDataSynthesizeSetting;
    @BindView(R.id.click_protect_password)
    LinearLayout clickProtectPassword;
    @BindView(R.id.click_pay_password)
    LinearLayout clickPayPassword;
    @BindView(R.id.click_changed_password)
    LinearLayout clickChangedPassword;
    @BindView(R.id.click_bind_pay)
    LinearLayout clickBindPay;
    @BindView(R.id.switch_login_protect)
    SwitchButton switchLoginProtect;
    @BindView(R.id.switch_sound)
    SwitchButton switchSound;
    @BindView(R.id.switch_shake)
    SwitchButton switchShake;
    @BindView(R.id.click_do_not_disturb)
    LinearLayout clickDoNotDisturb;
    @BindView(R.id.click_message_received)
    LinearLayout clickMessageReceived;
    @BindView(R.id.click_auto_play)
    LinearLayout clickAutoPlay;
    @BindView(R.id.click_clear)
    LinearLayout clickClear;
    @BindView(R.id.click_system_received)
    LinearLayout clickSystemReceived;
    YWLoadingDialog ywLoadingDialog;
    SettingModel settingModel = new SettingModel();
    @BindView(R.id.cache)
    TextView cache;
    @BindView(R.id.parent)
    LinearLayout parent;
    @BindView(R.id.click_protect)
    LinearLayout clickProtect;

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
        titleTop = null;clickProtect=null;
        activityMyDataSynthesizeSetting = null;
        clickProtectPassword = null;
        clickPayPassword = null;
        clickChangedPassword = null;
        clickBindPay = null;
        switchLoginProtect = null;
        switchSound = null;
        switchShake = null;
        clickDoNotDisturb = null;
        clickMessageReceived = null;
        clickAutoPlay = null;
        clickClear = null;
        clickSystemReceived = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data_synthesize_setting);
        ButterKnife.bind(this);
        customInit(activityMyDataSynthesizeSetting, false, true, true);
        titleTop.setText("综合设置");
        cache.setText(FrescoUtils.getCacheSize());
        getUsinfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedSet changedSet) {
        getUsinfo();
    }

    @OnClick({R.id.back_top,R.id.click_protect, R.id.click_protect_password, R.id.click_pay_password, R.id.click_changed_password, R.id.click_bind_pay, R.id.click_do_not_disturb, R.id.click_message_received, R.id.click_auto_play, R.id.click_clear, R.id.click_system_received})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.click_protect:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityProtect.class));
                }
                break;
            case R.id.back_top:
                finish();
                break;
            case R.id.click_protect_password:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataSynthesizeSettingProtectPassword.class));
                }
                break;
            case R.id.click_pay_password:
                if ("1".equals(settingModel.isset_pass)) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataSynthesizSettingSetPayPassword.class));
                } else if ("0".equals(settingModel.isset_pass)) {
                    if (getBaseActivityContext() != null) {
                        startActivity(new Intent(getBaseActivityContext(), ActivityMyDataSetPassword.class));
                    }
                } else {
                    ToastUtils.showShortToast(getBaseActivityContext(), "信息获取中");
                    getUsinfo();
                }
                break;
            case R.id.click_changed_password:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataSynthesizeSettingPasswordManager.class));
                }
                break;
            case R.id.click_bind_pay:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataSynthesizePayBindManager.class));
                }
                break;
            case R.id.click_do_not_disturb:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataSynthesizeSettingDoNotDisturb.class)
                            .putExtra("start", settingModel.start_time)
                            .putExtra("disturb", settingModel.disturb)
                            .putExtra("end", settingModel.end_time));
                }
                break;
            case R.id.click_message_received:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataSynthesizeSettingMessageReceive.class));
                }
                break;
            case R.id.click_auto_play:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataSynthesizeSettingAutoVideo.class));
                }
                break;
            case R.id.click_clear:
                APPOftenUtils.createAskDialog(getBaseActivityContext(), "是否要清理缓存？", new OnAskDialogCallBack() {
                    @Override
                    public void onOKey(Dialog dialog) {
                        dialog.dismiss();
                        dialog=null;
                        FrescoUtils.clearDiskCache();
                        cache.setText(FrescoUtils.getCacheSize());
                    }

                    @Override
                    public void onCancel(Dialog dialog) {
                        dialog.dismiss();
                        dialog=null;
                    }
                });
                break;
            case R.id.click_system_received:
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
                                    parent.setVisibility(View.VISIBLE);
                                    settingModel = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), SettingModel.class);
                                    showData();
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
     * 设置用户设置资料
     */
    public void setUsinfo(String key, String value) {
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
                            .put(key, value)
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

    void showData() {
        if ("0".equals(settingModel.login_protect)) {
            switchLoginProtect.setOpened(true);
        }
        if ("0".equals(settingModel.voice)) {
            switchSound.setOpened(true);
        }
        if ("0".equals(settingModel.shake)) {
            switchShake.setOpened(true);
        }
        initSwitchButton();
    }

    void initSwitchButton() {
        switchLoginProtect.setOnStateChangedListener(new SwitchButton.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchButton view) {
                view.setOpened(true);
                setUsinfo("login_protect", "0");
            }

            @Override
            public void toggleToOff(SwitchButton view) {
                view.setOpened(false);
                setUsinfo("login_protect", "1");
            }
        });
        switchSound.setOnStateChangedListener(new SwitchButton.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchButton view) {
                view.setOpened(true);
                RongYunUtils.setNotificationQuietHours(new RongIMClient.OperationCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
                setUsinfo("voice", "0");

            }

            @Override
            public void toggleToOff(SwitchButton view) {
                view.setOpened(false);
                RongYunUtils.removeNotificationQuietHours(new RongIMClient.OperationCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
                setUsinfo("voice", "1");
            }
        });
        switchShake.setOnStateChangedListener(new SwitchButton.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchButton view) {
                view.setOpened(true);
                setUsinfo("shake", "0");
            }

            @Override
            public void toggleToOff(SwitchButton view) {
                view.setOpened(false);
                setUsinfo("shake", "1");
            }
        });
    }


}
