package com.sss.car.view;

import android.content.Intent;
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
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.EventBusModel.FindPassword;
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
 * Created by leilei on 2017/8/16.
 */

public class ActivityFindPasswordBySMS extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.mobile_find_password_by_sms)
    EditText mobileFindPasswordBySms;
    @BindView(R.id.code_find_password_by_sms)
    EditText codeFindPasswordBySms;
    @BindView(R.id.get_code_find_password_by_sms)
    CountDownButton getCodeFindPasswordBySms;
    @BindView(R.id.next_find_password_by_sms)
    TextView nextFindPasswordBySms;
    @BindView(R.id.can_not_get_sms_find_password_by_sms)
    TextView canNotGetSmsFindPasswordBySms;
    @BindView(R.id.find_password_by_sms)
    LinearLayout findPasswordBySms;
    String code = "";
    YWLoadingDialog ywLoadingDialog;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }


    @Override
    protected void onDestroy() {
        if (getCodeFindPasswordBySms != null) {
            getCodeFindPasswordBySms.destroy();
        }
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        code = null;
        getCodeFindPasswordBySms = null;
        backTop = null;
        titleTop = null;
        rightButtonTop = null;
        mobileFindPasswordBySms = null;
        codeFindPasswordBySms = null;
        nextFindPasswordBySms = null;
        canNotGetSmsFindPasswordBySms = null;
        findPasswordBySms = null;
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password_by_sms);
        ButterKnife.bind(this);
        customInit(findPasswordBySms, false, true, true);
        titleTop.setText("重置登录密码");
        getCodeFindPasswordBySms.setOnOperationCallBack(new CountDownButton.CountDownButtonOperationCallBack() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onTick(long millisUntilFinished) {
                getCodeFindPasswordBySms.setText("剩余" + millisUntilFinished / 1000 + "秒");
            }

            @Override
            public void onClickFromUser(boolean isRunning, long millisUntilFinished) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FindPassword event) {
        finish();
    }


    @OnClick({R.id.back_top, R.id.get_code_find_password_by_sms, R.id.next_find_password_by_sms, R.id.can_not_get_sms_find_password_by_sms})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.get_code_find_password_by_sms:
                try {
                    getSms(mobileFindPasswordBySms.getText().toString().trim());
                } catch (JSONException e) {
                    if (getBaseActivityContext() != null) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:sms-0");
                    }
                    e.printStackTrace();
                }
                break;
            case R.id.next_find_password_by_sms:
                next();
                break;
            case R.id.can_not_get_sms_find_password_by_sms:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityFindPasswordByAccount.class));
                }
                break;
        }
    }


    /**
     * 下一步
     */
    void next() {
        if (StringUtils.isEmpty(mobileFindPasswordBySms.getText().toString().trim())) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "手机号为空");
            }
            return;
        }
        if (mobileFindPasswordBySms.getText().toString().trim().length() < 11) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "手机号不正确");
            }
            return;
        }
        if (StringUtils.isEmpty(code)) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "请先获取动态码");
            }
            return;
        }
        if (!code.equals(codeFindPasswordBySms.getText().toString().trim())) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "动态码不正确");
            }
            return;
        }
        if (getBaseActivityContext() != null) {
            startActivity(new Intent(getBaseActivityContext(), ActivityFindPasswordSetPassword.class)
                    .putExtra("type", "sms")
                    .putExtra("extra", mobileFindPasswordBySms.getText().toString().trim()));
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

        if (getCodeFindPasswordBySms.getmCurrentmillis() > 1000) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "请于" + (getCodeFindPasswordBySms.getmCurrentmillis() / 1000) + "秒后再试");
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
        getCodeFindPasswordBySms.start();
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
}
