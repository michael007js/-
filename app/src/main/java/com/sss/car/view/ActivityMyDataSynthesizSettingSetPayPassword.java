package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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
import com.sss.car.EventBusModel.ChangedSet;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.HideShowButton;

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
 * 设置支付密码
 * Created by leilei on 2017/11/7.
 */

public class ActivityMyDataSynthesizSettingSetPayPassword extends BaseActivity {
    public static final int changed = 0x0001;
    public static final int set = 0x0002;
    @BindView(R.id.oid_password)
    EditText oidPassword;
    @BindView(R.id.new_one_password)
    EditText newOnePassword;
    @BindView(R.id.new_two_password)
    EditText newTwoPassword;
    @BindView(R.id.activity_my_data_synthesize_setting_set_pay_password)
    LinearLayout activityMyDataSynthesizeSettingSetPayPassword;
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.sure)
    TextView sure;
    YWLoadingDialog ywLoadingDialog;
    @BindView(R.id.eyes_one)
    HideShowButton eyesOne;
    @BindView(R.id.eyes_two)
    HideShowButton eyesTwo;
    @BindView(R.id.old_password)
    EditText oldPassword;
    @BindView(R.id.eyes_old)
    HideShowButton eyesOld;
    @BindView(R.id.parent_old)
    LinearLayout parentOld;
    @BindView(R.id.forget)
    TextView forget;
    @BindView(R.id.old_parent)
    LinearLayout oldParent;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        oidPassword = null;
        newOnePassword = null;
        newTwoPassword = null;
        activityMyDataSynthesizeSettingSetPayPassword = null;
        backTop = null;
        titleTop = null;
        sure = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误");
            finish();
        }
        setContentView(R.layout.activity_my_data_synthesize_setting_set_pay_password);
        ButterKnife.bind(this);
        customInit(activityMyDataSynthesizeSettingSetPayPassword, false, true, true);
        if (set == getIntent().getExtras().getInt("mode")) {
            parentOld.setVisibility(View.VISIBLE);
            titleTop.setText("设置支付密码");
            oldParent.setVisibility(View.GONE);
        } else {
            if (changed == getIntent().getExtras().getInt("mode")) {
                parentOld.setVisibility(View.GONE);
                titleTop.setText("支付密码修改");
                sure.setText("确认修改");

            }
        }


        eyesOne.setOnHideShowButtonCallBack(new HideShowButton.OnHideShowButtonCallBack() {
            @Override
            public void onHideShowButtonClick(boolean isHide) {
                eyesOne.changed(20, 20, getBaseActivityContext());
                if (isHide) {
                    newOnePassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    newOnePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        eyesTwo.setOnHideShowButtonCallBack(new HideShowButton.OnHideShowButtonCallBack() {
            @Override
            public void onHideShowButtonClick(boolean isHide) {
                eyesTwo.changed(20, 20, getBaseActivityContext());
                if (isHide) {
                    newTwoPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    newTwoPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        eyesOld.setOnHideShowButtonCallBack(new HideShowButton.OnHideShowButtonCallBack() {
            @Override
            public void onHideShowButtonClick(boolean isHide) {
                eyesOld.changed(20, 20, getBaseActivityContext());
                if (isHide) {
                    oldPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    oldPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedSet changedSet) {
        finish();
    }

    @OnClick({R.id.back_top, R.id.sure, R.id.forget})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forget:
                startActivity(new Intent(this, ActivityFindPassword.class)
                        .putExtra("mode", ActivityFindPassword.pay));
                break;
            case R.id.back_top:
                finish();
                break;
            case R.id.sure:
                if (set == getIntent().getExtras().getInt("mode")) {
                    if (StringUtils.isEmpty(oidPassword.getText().toString().trim())) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "请输入密保信息");
                        return;
                    }
                    if (StringUtils.isEmpty(newOnePassword.getText().toString().trim()) || StringUtils.isEmpty(newTwoPassword.getText().toString().trim())) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "请输入支付密码");
                        return;
                    }
                    if (newOnePassword.getText().toString().trim().length() < 6 || newTwoPassword.getText().toString().trim().length() < 6) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "请输入6位数支付密码");
                        return;
                    }
                    if (!newOnePassword.getText().toString().trim().equals(newTwoPassword.getText().toString().trim())) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "请确认支付密码");
                        return;
                    }
                    try {
                        save();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (StringUtils.isEmpty(oldPassword.getText().toString().trim())) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "请输入原密码");
                        return;
                    }
                    if (StringUtils.isEmpty(newOnePassword.getText().toString().trim()) || StringUtils.isEmpty(newTwoPassword.getText().toString().trim())) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "请输入登录密码");
                        return;
                    }
                    if (newOnePassword.getText().toString().trim().length() < 6 || newTwoPassword.getText().toString().trim().length() < 6) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "请输入6-18位登录密码");
                        return;
                    }
                    if (!newOnePassword.getText().toString().trim().equals(newTwoPassword.getText().toString().trim())) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "请确认登录密码");
                        return;
                    }
                    set_reserved();
                }

                break;
        }
    }


    /**
     * 保存
     *
     * @throws JSONException
     */
    void save() throws JSONException {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.setUserInfo(
                new JSONObject()
                        .put("reserved", oidPassword.getText().toString().trim())
                        .put("pay_pass", newTwoPassword.getText().toString().trim())
                        .put("member_id", Config.member_id).toString(), "设置支付密码", new StringCallback() {
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
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    EventBus.getDefault().post(new ChangedSet());
                                    finish();
                                } else {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    }
                                }
                            } catch (JSONException e) {
                                if (getBaseActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:save-0");
                                }
                                e.printStackTrace();
                            }
                        }
                    }

                })));
    }

    /**
     * 重置支付密码
     *
     * @throws JSONException
     */
    void set_reserved() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.setUserInfo(
                    new JSONObject()
                            .put("old_pay", oldPassword.getText().toString().trim())
                            .put("pay_pass", newTwoPassword.getText().toString().trim())
                            .put("member_id", Config.member_id).toString(), "重置支付密码", new StringCallback() {
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
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                        EventBus.getDefault().post(new ChangedSet());
                                        finish();
                                    } else {
                                        if (getBaseActivityContext() != null) {
                                            ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                        }
                                    }
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:save-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:save-0");
            e.printStackTrace();
        }
    }

}
