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
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.R;
import com.sss.car.RequestWeb;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 支付绑定(微信,支付宝)
 * Created by leilei on 2017/11/7.
 */

public class ActivityMyDataSynthesizeBindAccountWxZfb extends BaseActivity {
    public static final int ZFB = 0X0001;
    public static final int WX = 0X0002;
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.account)
    EditText account;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.code)
    EditText code;
    @BindView(R.id.sure)
    TextView sure;
    @BindView(R.id.get)
    CountDownButton get;
    @BindView(R.id.activity_my_data_synthesize_bind_account_wx_zfb)
    LinearLayout activityMyDataSynthesizeBindAccountWxZfb;

    String sms_code;
    YWLoadingDialog ywLoadingDialog;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        type = null;
        account = null;
        phone = null;
        code = null;
        sure = null;
        get = null;
        activityMyDataSynthesizeBindAccountWxZfb = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data_synthesize_bind_account_wx_zfb);
        ButterKnife.bind(this);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误");
            finish();
        }

        switch (getIntent().getExtras().getInt("what")) {
            case ZFB:
                titleTop.setText("支付宝绑定");
                type.setText("支付宝账号:");
                break;
            case WX:
                titleTop.setText("微信绑定");
                type.setText("微信账号:");
                break;
        }
        customInit(activityMyDataSynthesizeBindAccountWxZfb, false, true, false);

    }

    @OnClick({R.id.back_top, R.id.get, R.id.sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.sure:
                if (StringUtils.isEmpty(account.getText().toString().trim())){
                    ToastUtils.showShortToast(getBaseActivityContext(),"请获填写要绑定的账号");
                    return;
                }
                if (StringUtils.isEmpty(sms_code)){
                    ToastUtils.showShortToast(getBaseActivityContext(),"请获取验证码");
                    return;
                }
                if (StringUtils.isEmpty(code.getText().toString().trim())){
                    ToastUtils.showShortToast(getBaseActivityContext(),"请输入验证码");
                    return;
                }
                if (!code.getText().toString().trim().equals(code)){
                    ToastUtils.showShortToast(getBaseActivityContext(),"验证码不正确");
                    return;
                }
                break;
            case R.id.get:
                if (StringUtils.isEmpty(phone.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "手机号为空");
                    return;
                }
                try {
                    getSms(phone.getText().toString().trim());
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

        if (get.getmCurrentmillis() > 1000) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "请于" + (get.getmCurrentmillis() / 1000) + "秒后再试");
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
        get.start();
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
                            sms_code = jsonObject.getJSONObject("data").getString("code");
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
