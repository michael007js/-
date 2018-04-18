package com.sss.car.view;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Button.CountDownButton;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.dao.OnAskDialogCallBack;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.BitmapUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.RegisterModel;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.HideShowButton;
import com.sss.car.custom.ResultIma;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


/**
 * Created by leilei on 2017/8/16.
 */

@SuppressWarnings("ALL")
public class ActivityRegister extends BaseActivity implements View.OnFocusChangeListener {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.mobile_activity_register)
    EditText mobileActivityRegister;
    @BindView(R.id.code_activity_register)
    EditText codeActivityRegister;
    @BindView(R.id.get_code_activity_register)
    CountDownButton getCodeActivityRegister;
    @BindView(R.id.password_one_activity_register)
    EditText passwordOneActivityRegister;
    @BindView(R.id.password_two_activity_register)
    EditText passwordTwoActivityRegister;
    @BindView(R.id.invitation_activity_register)
    EditText invitationActivityRegister;
    @BindView(R.id.register_activity_register)
    TextView registerActivityRegister;
    @BindView(R.id.protocol_activity_register)
    TextView protocolActivityRegister;
    @BindView(R.id.qr_activity_register)
    ImageView qrActivityRegister;
    @BindView(R.id.activity_register)
    LinearLayout activityRegister;
    String code = "";
    YWLoadingDialog ywLoadingDialog;
    @BindView(R.id.show_phone)
    ResultIma showPhone;
    @BindView(R.id.show_code)
    ResultIma showCode;
    @BindView(R.id.eyes_one)
    HideShowButton eyesOne;
    @BindView(R.id.show_password_one)
    ResultIma showPasswordOne;
    @BindView(R.id.eyes_two)
    HideShowButton eyesTwo;
    @BindView(R.id.show_password_two)
    ResultIma showPasswordTwo;
    @BindView(R.id.show_invitation)
    ResultIma showInvitation;
    @BindView(R.id.protect_activity_register)
    EditText protectActivityRegister;
    @BindView(R.id.show_protect)
    ResultIma showProtect;


    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        customInit(activityRegister, false, true, false);
        titleTop.setText("快速注册");
        APPOftenUtils.underLineOfTextView(protocolActivityRegister);
        init();
        if (getBaseActivityContext() != null) {
            qr_pic();
//            addImageViewList(GlidUtils.glideLoad(false, qrActivityRegister, getBaseActivityContext(), R.mipmap.logo_register_code_qr));
        }
        getCodeActivityRegister.createCountTimer();
        getCodeActivityRegister.setOnOperationCallBack(new CountDownButton.CountDownButtonOperationCallBack() {
            @Override
            public void onFinish() {
                getCodeActivityRegister.setText("获取动态码");
            }

            @Override
            public void onTick(long millisUntilFinished) {
                getCodeActivityRegister.setText("剩余" + millisUntilFinished / 1000 + "秒");
            }

            @Override
            public void onClickFromUser(boolean isRunning, long millisUntilFinished) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        rightButtonTop = null;
        mobileActivityRegister = null;
        codeActivityRegister = null;
        passwordOneActivityRegister = null;
        passwordTwoActivityRegister = null;
        invitationActivityRegister = null;
        registerActivityRegister = null;
        protocolActivityRegister = null;
        qrActivityRegister = null;
        activityRegister = null;
        code = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getCodeActivityRegister != null) {
            getCodeActivityRegister.destroy();
        }
        getCodeActivityRegister = null;
        super.onDestroy();

    }

    private void init() {
        mobileActivityRegister.setOnFocusChangeListener(this);
        codeActivityRegister.setOnFocusChangeListener(this);
        passwordOneActivityRegister.setOnFocusChangeListener(this);
        passwordTwoActivityRegister.setOnFocusChangeListener(this);
        invitationActivityRegister.setOnFocusChangeListener(this);
        protectActivityRegister.setOnFocusChangeListener(this);
        mobileActivityRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobileActivityRegister.setFocusable(true);
                mobileActivityRegister.setFocusableInTouchMode(true);
                mobileActivityRegister.requestFocus();
            }
        });
        codeActivityRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeActivityRegister.setFocusable(true);
                codeActivityRegister.setFocusableInTouchMode(true);
                codeActivityRegister.requestFocus();
            }
        });
        passwordOneActivityRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordOneActivityRegister.setFocusable(true);
                passwordOneActivityRegister.setFocusableInTouchMode(true);
                passwordOneActivityRegister.requestFocus();
            }
        });
        passwordTwoActivityRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordTwoActivityRegister.setFocusable(true);
                passwordTwoActivityRegister.setFocusableInTouchMode(true);
                passwordTwoActivityRegister.requestFocus();
            }
        });
        invitationActivityRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invitationActivityRegister.setFocusable(true);
                invitationActivityRegister.setFocusableInTouchMode(true);
                invitationActivityRegister.requestFocus();
            }
        });
        protectActivityRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                protectActivityRegister.setFocusable(true);
                protectActivityRegister.setFocusableInTouchMode(true);
                protectActivityRegister.requestFocus();
            }
        });
        eyesOne.setOnHideShowButtonCallBack(new HideShowButton.OnHideShowButtonCallBack() {
            @Override
            public void onHideShowButtonClick(boolean isHide) {
                eyesOne.changed(60, 60, getBaseActivityContext());
                if (isHide) {
                    passwordOneActivityRegister.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    passwordOneActivityRegister.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        eyesTwo.setOnHideShowButtonCallBack(new HideShowButton.OnHideShowButtonCallBack() {
            @Override
            public void onHideShowButtonClick(boolean isHide) {
                eyesTwo.changed(60, 60, getBaseActivityContext());
                if (isHide) {
                    passwordTwoActivityRegister.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    passwordTwoActivityRegister.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }


    @OnClick({R.id.back_top, R.id.get_code_activity_register, R.id.register_activity_register, R.id.protocol_activity_register, R.id.qr_activity_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.qr_activity_register:
                APPOftenUtils.createAskDialog(getBaseActivityContext(), "是否保存到相册?", new OnAskDialogCallBack() {
                    @Override
                    public void onOKey(Dialog dialog) {
                        dialog.dismiss();
                        dialog = null;
                        if (APPOftenUtils.saveImageToGallery(getBaseActivityContext(), BitmapUtils.getBitmapFromImageView(qrActivityRegister))) {
                            ToastUtils.showShortToast(getBaseActivityContext(), "保存成功");
                        } else {
                            ToastUtils.showShortToast(getBaseActivityContext(), "保存失败");
                        }
                    }

                    @Override
                    public void onCancel(Dialog dialog) {
                        dialog.dismiss();
                        dialog = null;

                    }
                });
                break;
            case R.id.back_top:
                finish();
                break;
            case R.id.get_code_activity_register:
                if (StringUtils.isEmpty(mobileActivityRegister.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "填写手机号");
                    return;
                }
                mobile_isset();
                break;
            case R.id.register_activity_register:
                mobileActivityRegister.setFocusable(false);
                mobileActivityRegister.clearFocus();
                codeActivityRegister.setFocusable(false);
                codeActivityRegister.clearFocus();
                passwordOneActivityRegister.setFocusable(false);
                passwordOneActivityRegister.clearFocus();
                passwordTwoActivityRegister.setFocusable(false);
                passwordTwoActivityRegister.clearFocus();
                invitationActivityRegister.setFocusable(false);
                invitationActivityRegister.clearFocus();
                protectActivityRegister.setFocusable(false);
                protectActivityRegister.clearFocus();
                activityRegister.setFocusable(true);
                activityRegister.setFocusableInTouchMode(true);
                activityRegister.requestFocus();
                if (StringUtils.isEmpty(mobileActivityRegister.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请输入手机号码");
                    showPhone.wrong(60, 60, getBaseActivityContext());
                    showPhone.setVisibility(View.VISIBLE);
                    return;
                }

                if (StringUtils.isEmpty(codeActivityRegister.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请输入验证码");
                    showCode.wrong(60, 60, getBaseActivityContext());
                    showCode.setVisibility(View.VISIBLE);
                    return;
                }

                if (StringUtils.isEmpty(passwordOneActivityRegister.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请输入密码");
                    showPasswordOne.wrong(60, 60, getBaseActivityContext());
                    showPasswordOne.setVisibility(View.VISIBLE);
                    return;
                }

                if (StringUtils.isEmpty(passwordTwoActivityRegister.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请确认密码");
                    showPasswordTwo.wrong(60, 60, getBaseActivityContext());
                    showPasswordTwo.setVisibility(View.VISIBLE);
                    return;
                }
                if (RegexUtils.isABC(passwordOneActivityRegister.getText().toString().trim()) || RegexUtils.isNumericString(passwordOneActivityRegister.getText().toString().trim()) || passwordOneActivityRegister.getText().length() < 6) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请输入6-18位的字母和数字组合");
                    showPasswordOne.wrong(60, 60, getBaseActivityContext());
                    showPasswordOne.setVisibility(View.VISIBLE);
                    return;
                }

                if (StringUtils.isEmpty(invitationActivityRegister.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请输入注册码");
                    showInvitation.wrong(60, 60, getBaseActivityContext());
                    showInvitation.setVisibility(View.VISIBLE);
                    return;
                }
                if (StringUtils.isEmpty(protectActivityRegister.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请输入预留密保信息");
                    showProtect.wrong(60, 60, getBaseActivityContext());
                    showProtect.setVisibility(View.VISIBLE);
                    return;
                }


                if (showPhone.res && showCode.res && showPasswordOne.res && showPasswordTwo.res && showInvitation.res && showProtect.res) {
                    try {
                        register(mobileActivityRegister.getText().toString().trim(),
                                passwordOneActivityRegister.getText().toString().trim(),
                                passwordTwoActivityRegister.getText().toString().trim(),
                                invitationActivityRegister.getText().toString().trim());
                    } catch (JSONException e) {
                        if (getBaseActivityContext() != null) {
                            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:register-0");
                        }
                        e.printStackTrace();
                    }
                }


                break;
            case R.id.protocol_activity_register:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityProtocol.class));
                }
                break;
        }
    }

    public void qr_pic() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.dismiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.qr_pic(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.dismiss();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.dismiss();
                            }
                            try {
                                final JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    qrActivityRegister.setTag(R.id.glide_tag,Config.url+jsonObject.getJSONObject("data").getString("qr_code"));
                                    addImageViewList(GlidUtils.downLoader(false,qrActivityRegister,getBaseActivityContext()));
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-2");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }

    /**
     * 获取该账号是否注册
     */
    public void mobile_isset() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.dismiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.mobile_isset(
                    new JSONObject()
                            .put("mobile", mobileActivityRegister.getText().toString().trim())
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.dismiss();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.dismiss();
                            }
                            try {
                                final JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    getSms(mobileActivityRegister.getText().toString().trim());
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-2");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }

    private void register(final String mobile, final String passwordOne, String passwordTwo, String invitation) throws JSONException {


        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.register(
                new JSONObject()
                        .put("mobile", mobile)
                        .put("verify_code", code)
                        .put("register_code", invitationActivityRegister.getText().toString().trim())
                        .put("reserved", protectActivityRegister.getText().toString().trim())
                        .put("password", passwordOne).toString(),
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
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    EventBus.getDefault().post(new RegisterModel(mobile));
                                    finish();
                                } else {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                        if ("验证码有误".equals(jsonObject.getString("message"))) {
                                            showCode.wrong(60, 60, getBaseActivityContext());
                                            showCode.setVisibility(View.VISIBLE);
                                        }
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

        if (getCodeActivityRegister.getmCurrentmillis() > 6000) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "请于" + (getCodeActivityRegister.getmCurrentmillis() / 1000) + "秒后再试");
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
            getCodeActivityRegister.start();
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

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.mobile_activity_register:
                if (hasFocus == false) {
                    if (StringUtils.isEmpty(mobileActivityRegister.getText().toString().trim()) || mobileActivityRegister.getText().toString().length() < 11) {
                        showPhone.setVisibility(View.VISIBLE);
                        showPhone.wrong(60, 60, getBaseActivityContext());
                    } else {
                        showPhone.setVisibility(View.VISIBLE);
                        showPhone.right(60, 60, getBaseActivityContext());
                    }
                } else {
                    showPhone.setVisibility(View.INVISIBLE);
                    showPhone.right(60, 60, getBaseActivityContext());
                }
                break;
            case R.id.code_activity_register:
                if (hasFocus == false) {
                    if (StringUtils.isEmpty(codeActivityRegister.getText().toString().trim()) ||
                            StringUtils.isEmpty(code) ||
                            !code.equals(codeActivityRegister.getText().toString().trim()) || codeActivityRegister.getText().toString().trim().length() < 6) {
                        showCode.setVisibility(View.VISIBLE);
                        showCode.wrong(60, 60, getBaseActivityContext());
                    } else {
                        showCode.setVisibility(View.VISIBLE);
                        showCode.right(60, 60, getBaseActivityContext());
                    }
                } else {
                    showCode.setVisibility(View.INVISIBLE);
                    showCode.right(60, 60, getBaseActivityContext());
                }
                break;
            case R.id.password_one_activity_register:
                if (hasFocus == false) {
                    if (StringUtils.isEmpty(passwordOneActivityRegister.getText().toString().trim()) || passwordOneActivityRegister.getText().length() < 6 || passwordOneActivityRegister.getText().length() > 18) {
                        showPasswordOne.setVisibility(View.VISIBLE);
                        showPasswordOne.wrong(60, 60, getBaseActivityContext());
                        return;
                    }
                    if (RegexUtils.isABC(passwordOneActivityRegister.getText().toString().trim())) {
                        showPasswordOne.setVisibility(View.VISIBLE);
                        showPasswordOne.wrong(60, 60, getBaseActivityContext());
                        return;
                    }
                    if (RegexUtils.isNumericString(passwordOneActivityRegister.getText().toString().trim())) {
                        showPasswordOne.setVisibility(View.VISIBLE);
                        showPasswordOne.wrong(60, 60, getBaseActivityContext());
                        return;
                    }
                    showPasswordOne.setVisibility(View.VISIBLE);
                    showPasswordOne.right(60, 60, getBaseActivityContext());
                } else {
                    showPasswordOne.setVisibility(View.INVISIBLE);
                    showPasswordOne.right(60, 60, getBaseActivityContext());
                }
                break;
            case R.id.password_two_activity_register:
                if (hasFocus == false) {
                    if (StringUtils.isEmpty(passwordTwoActivityRegister.getText().toString().trim())) {
                        showPasswordTwo.setVisibility(View.VISIBLE);
                        showPasswordTwo.wrong(60, 60, getBaseActivityContext());
                        return;
                    }
                    if (!passwordTwoActivityRegister.getText().toString().trim().equals(passwordOneActivityRegister.getText().toString().trim())) {
                        showPasswordTwo.setVisibility(View.VISIBLE);
                        showPasswordTwo.wrong(60, 60, getBaseActivityContext());
                        return;
                    }
                    showPasswordTwo.setVisibility(View.VISIBLE);
                    showPasswordTwo.right(60, 60, getBaseActivityContext());

                } else {
                    showPasswordTwo.setVisibility(View.INVISIBLE);
                    showPasswordTwo.right(60, 60, getBaseActivityContext());
                }
                break;
            case R.id.invitation_activity_register:
                if (hasFocus == false) {

                    if (StringUtils.isEmpty(invitationActivityRegister.getText().toString().trim())) {
                        showInvitation.setVisibility(View.VISIBLE);
                        showInvitation.wrong(60, 60, getBaseActivityContext());
                        return;
                    }
                    if (invitationActivityRegister.getText().toString().trim().length() < 6) {
                        showInvitation.setVisibility(View.VISIBLE);
                        showInvitation.wrong(60, 60, getBaseActivityContext());
                        return;
                    }
                    String temp = invitationActivityRegister.getText().toString().trim();
                    if (RegexUtils.isABC(temp.substring(0, 2)) == false) {
                        showInvitation.setVisibility(View.VISIBLE);
                        showInvitation.wrong(60, 60, getBaseActivityContext());
                        return;
                    }

                    showInvitation.setVisibility(View.VISIBLE);
                    showInvitation.right(60, 60, getBaseActivityContext());


                } else {
                    showInvitation.setVisibility(View.INVISIBLE);
                    showInvitation.right(60, 60, getBaseActivityContext());
                }
                break;
            case R.id.protect_activity_register:
                if (hasFocus == false) {
                    if (StringUtils.isEmpty(protectActivityRegister.getText().toString().trim())) {
                        showProtect.setVisibility(View.VISIBLE);
                        showProtect.wrong(60, 60, getBaseActivityContext());
                        return;
                    }
                    showProtect.setVisibility(View.VISIBLE);
                    showProtect.right(60, 60, getBaseActivityContext());
                } else {
                    showProtect.setVisibility(View.INVISIBLE);
                    showProtect.right(60, 60, getBaseActivityContext());
                }
                break;
        }
    }
}