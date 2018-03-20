package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
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
import com.sss.car.Config;
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

import static com.sss.car.R.id.editText;

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
    @BindView(R.id.text)
    TextView text;

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
        if (login == getIntent().getExtras().getInt("mode")) {
            tip.setText("新密码由6-18位数字和字母组成");
            text.setText("登录密码");
        } else {
            titleTop.setText("重置支付密码");
            accountComplaintActivityFindPassword.setVisibility(View.GONE);
            tip.setText("支付密码由6位数字组成");
            passwordOneActivityFindPasswordSetPassword.addTextChangedListener(new TagNameTextWatch(passwordOneActivityFindPasswordSetPassword, 6));
            passwordTwoActivityFindPasswordSetPassword.addTextChangedListener(new TagNameTextWatch(passwordTwoActivityFindPasswordSetPassword, 6));
            passwordOneActivityFindPasswordSetPassword.setKeyListener(new NumberKeyListener() {
                @Override
                public int getInputType() {
                    return InputType.TYPE_NUMBER_VARIATION_PASSWORD;
                }

                @Override
                protected char[] getAcceptedChars() {
                    char[] numberChars = {'1','2','3','4','5','6','7','8','9','0'};
                    return numberChars;
                }
            });
            passwordTwoActivityFindPasswordSetPassword.setKeyListener(new NumberKeyListener() {
                @Override
                public int getInputType() {
                    return InputType.TYPE_NUMBER_VARIATION_PASSWORD;
                }

                @Override
                protected char[] getAcceptedChars() {
                    char[] numberChars = {'1','2','3','4','5','6','7','8','9','0'};
                    return numberChars;
                }
            });
            text.setText("支付密码");
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

              if (login==getIntent().getExtras().getInt("mode")){
                  if (RegexUtils.isABC(passwordOneActivityFindPasswordSetPassword.getText().toString().trim())) {
                      ToastUtils.showShortToast(getBaseActivityContext(), "密码不能为纯数字或纯字母");
                      return;
                  }
                  if (RegexUtils.isNumericString(passwordOneActivityFindPasswordSetPassword.getText().toString().trim())) {
                      ToastUtils.showShortToast(getBaseActivityContext(), "密码不能为纯数字或纯字母");
                      return;
                  }
              }
                if (!passwordOneActivityFindPasswordSetPassword.getText().toString().trim().equals(passwordTwoActivityFindPasswordSetPassword.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "您的密码两次密码输入不一致");
                    return;
                }
                if (login == getIntent().getExtras().getInt("mode")) {
                    try {
                        change();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    set_pay_pass();
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


    public void set_pay_pass() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.set_pay_pass(
                    new JSONObject()
                            .put("reserved",protectActivityFindPassword.getText().toString().trim())
                            .put("mobile",mobileActivityFindPassword.getText().toString().trim())
                            .put("pay_pass", passwordOneActivityFindPasswordSetPassword.getText().toString().trim())
                            .put("member_id", Config.member_id).toString(), "支付密码", new StringCallback() {
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
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }
}

class TagNameTextWatch implements TextWatcher {
    private EditText et;
    private int maxLenth;

    TagNameTextWatch(EditText et, int maxLenth) {
        this.maxLenth = maxLenth;
        this.et = et;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {

        Editable editable = et.getText();
        int len = editable.length();
        if (len > maxLenth) {
            int selEndIndex = Selection.getSelectionEnd(editable);
            String str = editable.toString();
            //截取新字符串
            String newStr = str.substring(0, maxLenth);
            et.setText(newStr);
            editable = et.getText();

            //新字符串的长度
            int newLen = editable.length();
            //旧光标位置超过字符串长度
            if (selEndIndex > newLen) {
                selEndIndex = editable.length();
            }
            //设置新光标所在的位置
            Selection.setSelection(editable, selEndIndex);

        }
    }
}