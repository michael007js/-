package com.sss.car.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Button.CountDownButton;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 设置支付密码
 * Created by leilei on 2017/10/15.
 */

public class ActivityMyDataSetPassword extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.mobile_activity_my_data_set_password)
    EditText mobileActivityMyDataSetPassword;
    @BindView(R.id.code_activity_my_data_set_password)
    EditText codeActivityMyDataSetPassword;
    @BindView(R.id.get_activity_my_data_set_password)
    CountDownButton getActivityMyDataSetPassword;
    @BindView(R.id.password_one_activity_my_data_set_password)
    EditText passwordOneActivityMyDataSetPassword;
    @BindView(R.id.password_two_activity_my_data_set_password)
    EditText passwordTwoActivityMyDataSetPassword;
    @BindView(R.id.complete_activity_my_data_set_password)
    TextView completeActivityMyDataSetPassword;
    @BindView(R.id.activity_my_data_set_password)
    LinearLayout activityMyDataSetPassword;

    YWLoadingDialog ywLoadingDialog;
    String code;

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
        mobileActivityMyDataSetPassword = null;
        codeActivityMyDataSetPassword = null;
        if (getActivityMyDataSetPassword!=null){
            getActivityMyDataSetPassword.destroy();
        }
        getActivityMyDataSetPassword = null;
        passwordOneActivityMyDataSetPassword = null;
        passwordTwoActivityMyDataSetPassword = null;
        completeActivityMyDataSetPassword = null;
        activityMyDataSetPassword = null;
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data_set_password);
        ButterKnife.bind(this);
        customInit(activityMyDataSetPassword, false, true, false);
        getActivityMyDataSetPassword.setText("获取验证码");
        titleTop.setText("修改支付密码");
        getActivityMyDataSetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getSms(mobileActivityMyDataSetPassword.getText().toString().trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        getActivityMyDataSetPassword.setOnOperationCallBack(new CountDownButton.CountDownButtonOperationCallBack() {
            @Override
            public void onFinish() {
                getActivityMyDataSetPassword.setText("获取验证码");
            }

            @Override
            public void onTick(long millisUntilFinished) {
                if (getActivityMyDataSetPassword!=null){
                    getActivityMyDataSetPassword.setText("剩余" + millisUntilFinished / 1000 + "秒");
                }

            }

            @Override
            public void onClickFromUser(boolean isRunning, long millisUntilFinished) {
                if (isRunning) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请于" + millisUntilFinished / 1000 + "秒后再试");
                }

            }
        });
    }

    @OnClick({R.id.back_top, R.id.complete_activity_my_data_set_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.complete_activity_my_data_set_password:
                try {
                    setPayPass("设置支付密码");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 获取验证码
     *
     * @param mobile
     * @throws JSONException
     */
    void getSms(String mobile) throws JSONException {
        if (StringUtils.isEmpty(mobile)) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "手机号为空");
            }
            return;
        }

        if (mobile.length() < 11) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "手机号不正确");
            }
            return;
        }

        if (getActivityMyDataSetPassword.getmCurrentmillis() > 1000) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "请于" + (getActivityMyDataSetPassword.getmCurrentmillis() / 1000) + "秒后再试");
            }
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
        addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getSMS(new JSONObject().put("mobile", mobile).toString(), new StringCallback() {
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
                            code = jsonObject.getJSONObject("data").getString("code");
                            getActivityMyDataSetPassword.start();
                        } else {
                            if (getBaseActivityContext() != null) {
                                ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                            }
                        }
                    } catch (JSONException e) {
                        if (getBaseActivityContext() != null) {
                            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:SMS-0");
                        }
                        e.printStackTrace();
                    }
                }
            }
        })));
    }

    /**
     * 保存支付密码
     *
     * @throws JSONException
     */
    void setPayPass(String meaning) throws JSONException {
        if (StringUtils.isEmpty(code)) {
            ToastUtils.showShortToast(getBaseActivityContext(), "请先获取验证码");
            return;
        }
        if (!code.equals(codeActivityMyDataSetPassword.getText().toString().trim())){
            ToastUtils.showShortToast(getBaseActivityContext(), "验证码不正确");
            return;
        }
        if (StringUtils.isEmpty(passwordOneActivityMyDataSetPassword.getText().toString().trim())) {
            ToastUtils.showShortToast(getBaseActivityContext(), "请输入您的密码");
            return;
        }
        if (StringUtils.isEmpty(passwordTwoActivityMyDataSetPassword.getText().toString().trim())) {
            ToastUtils.showShortToast(getBaseActivityContext(), "请确认您的密码");
            return;
        }
        if (!passwordTwoActivityMyDataSetPassword.getText().toString().trim().equals(passwordTwoActivityMyDataSetPassword.getText().toString().trim())) {
            ToastUtils.showShortToast(getBaseActivityContext(), "两次输入的密码不正确");
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
        addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.setUserInfo(
                new JSONObject()
                        .put("pay_pass", passwordTwoActivityMyDataSetPassword.getText().toString().trim())
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
    }
}
