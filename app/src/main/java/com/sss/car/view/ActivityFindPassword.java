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
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.EventBusModel.FindPassword;
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
 * Created by leilei on 2017/12/3.
 */

public class ActivityFindPassword extends BaseActivity {
    public static final int login = 0x0001;
    public static final int pay = 0x0002;
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.top_parent)
    LinearLayout topParent;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.mobile_activity_find_password)
    EditText mobileActivityFindPassword;
    @BindView(R.id.protect_activity_find_password)
    EditText protectActivityFindPassword;
    @BindView(R.id.next_activity_find_password)
    TextView nextActivityFindPassword;
    @BindView(R.id.account_complaint_activity_find_password)
    TextView accountComplaintActivityFindPassword;
    @BindView(R.id.activity_find_password)
    LinearLayout activityFindPassword;
    YWLoadingDialog ywLoadingDialog;
    @BindView(R.id.password_one_activity_find_password_set_password)
    EditText passwordOneActivityFindPasswordSetPassword;
    @BindView(R.id.eyes_one)
    HideShowButton eyesOne;
    @BindView(R.id.password_two_activity_find_password_set_password)
    EditText passwordTwoActivityFindPasswordSetPassword;
    @BindView(R.id.eyes_two)
    HideShowButton eyesTwo;
    @BindView(R.id.tip)
    TextView tip;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.dismiss();
        }
        ywLoadingDialog = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        ButterKnife.bind(this);
        customInit(activityFindPassword, false, true, true);
        titleTop.setText("重置登录密码");
        if (login==getIntent().getExtras().getInt("mode")){
            tip.setText("新密码由6-18位数字和字母组成");
        }else {
            titleTop.setText("重置支付密码");
            accountComplaintActivityFindPassword.setVisibility(View.GONE);
            tip.setText("支付密码由6位数字组成");
        }
        eyesOne.setOnHideShowButtonCallBack(new HideShowButton.OnHideShowButtonCallBack() {
            @Override
            public void onHideShowButtonClick(boolean isHide) {
                eyesOne.changed(20, 20, getBaseActivityContext());
                if (isHide) {
                    passwordOneActivityFindPasswordSetPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    passwordOneActivityFindPasswordSetPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        eyesTwo.setOnHideShowButtonCallBack(new HideShowButton.OnHideShowButtonCallBack() {
            @Override
            public void onHideShowButtonClick(boolean isHide) {
                eyesTwo.changed(20, 20, getBaseActivityContext());
                if (isHide) {
                    passwordTwoActivityFindPasswordSetPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    passwordTwoActivityFindPasswordSetPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    @OnClick({R.id.back_top, R.id.next_activity_find_password, R.id.account_complaint_activity_find_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.next_activity_find_password:
                if (StringUtils.isEmpty(mobileActivityFindPassword.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "手机号为空");
                    return;
                }

                if (StringUtils.isEmpty(protectActivityFindPassword.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "密码保护信息为空");
                    return;
                }
                if (StringUtils.isEmpty(passwordOneActivityFindPasswordSetPassword.getText().toString().trim()) || StringUtils.isEmpty(passwordTwoActivityFindPasswordSetPassword.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "您的密码为空");
                    return;
                }
                if (passwordOneActivityFindPasswordSetPassword.getText().toString().trim().length() < 6) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "您的密码过短");
                    return;
                }

                if (RegexUtils.isABC(passwordOneActivityFindPasswordSetPassword.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "密码不能为纯数字或纯字母");
                    return;
                }
                if (RegexUtils.isNumericString(passwordOneActivityFindPasswordSetPassword.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "密码不能为纯数字或纯字母");
                    return;
                }
                if (!passwordOneActivityFindPasswordSetPassword.getText().toString().trim().equals(passwordTwoActivityFindPasswordSetPassword.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "您的密码两次密码输入不一致");
                    return;
                }
                if (login==getIntent().getExtras().getInt("mode")){
                    try {
                        change();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {

                }

                break;
            case R.id.account_complaint_activity_find_password:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), AccountComplaint.class));
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FindPassword event) {
        finish();
    }


    /**
     * 修改密码
     *
     * @throws JSONException
     */
    void change() throws JSONException {
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.findPassword(
                new JSONObject().put("mobile", mobileActivityFindPassword.getText().toString().trim())
                        .put("reserved", protectActivityFindPassword.getText().toString().trim())
                        .put("password", passwordOneActivityFindPasswordSetPassword.getText().toString().trim()).toString(),
                new StringCallback() {
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
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "找回密码成功!");
                                    }
                                    EventBus.getDefault().post(new FindPassword());
                                    finish();
                                } else {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    }
                                }
                            } catch (JSONException e) {
                                if (getBaseActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:register-0");
                                }
                                e.printStackTrace();
                            }
                        }
                    }
                })));
    }


    /**
     * 验证预留的密码保护信息
     */
    public void reserved() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.reserved(
                    new JSONObject()
                            .put("mobile", mobileActivityFindPassword.getText().toString().trim())
                            .put("reserved", protectActivityFindPassword.getText().toString().trim())
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
                                    ToastUtils.showShortToast(getBaseActivityContext(), "验证成功");
                                    if (getBaseActivityContext() != null) {
                                        startActivity(new Intent(getBaseActivityContext(), ActivityFindPasswordSetPassword.class)
                                                .putExtra("mobile", mobileActivityFindPassword.getText().toString().trim())
                                                .putExtra("protect", protectActivityFindPassword.getText().toString().trim()));
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
}
