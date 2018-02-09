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

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


/**
 * Created by leilei on 2018/1/2.
 */

public class ActivityMyDataSynthesizeSettingSetLoginPassword extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.oid_password)
    EditText oidPassword;
    @BindView(R.id.eyes_one)
    HideShowButton eyesOne;
    @BindView(R.id.new_one_password)
    EditText newOnePassword;
    @BindView(R.id.eyes_two)
    HideShowButton eyesTwo;
    @BindView(R.id.new_two_password)
    EditText newTwoPassword;
    @BindView(R.id.eyes_three)
    HideShowButton eyesThree;
    @BindView(R.id.forget)
    TextView forget;
    @BindView(R.id.sure)
    TextView sure;
    @BindView(R.id.activity_my_data_synthesize_setting_set_login_password)
    LinearLayout activityMyDataSynthesizeSettingSetLoginPassword;

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
        setContentView(R.layout.activity_my_data_synthesize_setting_set_login_password);
        ButterKnife.bind(this);
        titleTop.setText("登录密码修改");
        customInit(activityMyDataSynthesizeSettingSetLoginPassword,false,true,true);
        eyesOne.setOnHideShowButtonCallBack(new HideShowButton.OnHideShowButtonCallBack() {
            @Override
            public void onHideShowButtonClick(boolean isHide) {
                eyesOne.changed(60,60,getBaseActivityContext());
                if (isHide) {
                    oidPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    oidPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        eyesTwo.setOnHideShowButtonCallBack(new HideShowButton.OnHideShowButtonCallBack() {
            @Override
            public void onHideShowButtonClick(boolean isHide) {
                eyesTwo.changed(60,60,getBaseActivityContext());
                if (isHide) {
                    newOnePassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    newOnePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        eyesOne.setOnHideShowButtonCallBack(new HideShowButton.OnHideShowButtonCallBack() {
            @Override
            public void onHideShowButtonClick(boolean isHide) {
                eyesThree.changed(60,60,getBaseActivityContext());
                if (isHide) {
                    newTwoPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    newTwoPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedSet changedSet) {
        finish();
    }
    @OnClick({R.id.back_top, R.id.forget, R.id.sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.forget:
                startActivity(new Intent(getBaseActivityContext(), ActivityFindPassword.class)
                        .putExtra("mode",ActivityFindPassword.login));
                break;
            case R.id.sure:
                setPayPass("登录密码");
                break;
        }
    }


    /**
     * 保存支付密码
     *
     * @throws JSONException
     */
    void setPayPass(String meaning)  {

        if (StringUtils.isEmpty(oidPassword.getText().toString().trim())) {
            ToastUtils.showShortToast(getBaseActivityContext(), "请输入原密码");
            return;
        }
        if (StringUtils.isEmpty(newOnePassword.getText().toString().trim()) || StringUtils.isEmpty(newTwoPassword.getText().toString().trim())) {
            ToastUtils.showShortToast(getBaseActivityContext(), "请输入新密码");
            return;
        }
        if (!newOnePassword.getText().toString().trim().equals(newTwoPassword.getText().toString().trim())) {
            ToastUtils.showShortToast(getBaseActivityContext(), "请确认新密码");
            return;
        }
        if (newOnePassword.getText().toString().trim().length() < 6 || newTwoPassword.getText().toString().trim().length() < 6) {
            ToastUtils.showShortToast(getBaseActivityContext(), "请输入6-18位登录密码");
            return;
        }
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
                            .put("old_pass", oidPassword.getText().toString().trim())
                            .put("password", newTwoPassword.getText().toString().trim())
                            .put("member_id", Config.member_id).toString(), meaning, new StringCallback() {
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
                                        finish();
                                    } else {
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
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
            e.printStackTrace();
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:save-0");
        }
    }

}
