package com.sss.car.view;

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
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.sss.car.R.id.get;

/**
 * Created by leilei on 2017/8/16.
 */

public class ActivityFindPasswordSetPassword extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.password_one_activity_find_password_set_password)
    EditText passwordOneActivityFindPasswordSetPassword;
    @BindView(R.id.password_two_activity_find_password_set_password)
    EditText passwordTwoActivityFindPasswordSetPassword;
    @BindView(R.id.complete_activity_find_password_set_password)
    TextView completeActivityFindPasswordSetPassword;
    @BindView(R.id.activity_find_password_set_password)
    LinearLayout activityFindPasswordSetPassword;
    YWLoadingDialog ywLoadingDialog;
    JSONObject jsonObject;
    @BindView(R.id.eyes_one)
    HideShowButton eyesOne;
    @BindView(R.id.eyes_two)
    HideShowButton eyesTwo;

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
        rightButtonTop = null;
        passwordOneActivityFindPasswordSetPassword = null;
        passwordTwoActivityFindPasswordSetPassword = null;
        completeActivityFindPasswordSetPassword = null;
        activityFindPasswordSetPassword = null;
        jsonObject = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || getIntent().getExtras() == null) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据传输错误!");
                finish();
            }
        }
        setContentView(R.layout.activity_find_password_set_password);
        ButterKnife.bind(this);
        customInit(activityFindPasswordSetPassword, false, true, false);
        titleTop.setText("重置登录密码");
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


    @OnClick({R.id.back_top, R.id.complete_activity_find_password_set_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.complete_activity_find_password_set_password:
                try {
                    change();
                } catch (JSONException e) {
                    if (getBaseActivityContext() != null) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:change-0");
                    }
                    e.printStackTrace();
                }
                break;
        }
    }


    /**
     * 修改密码
     *
     * @throws JSONException
     */
    void change() throws JSONException {
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

        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.findPassword(
                new JSONObject().put("mobile", getIntent().getExtras().getString("mobile"))
                        .put("reserved", getIntent().getExtras().getString("protect"))
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
}
