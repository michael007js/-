package com.sss.car.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Button.CountDownButton;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangeInfoModel;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.rongyun.RongYunUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static android.R.attr.key;
import static android.R.attr.value;

/**
 * Created by leilei on 2017/8/16.
 */

public class ActivityMyDataChangeMobile extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.old_activity_my_data_change_mobile)
    EditText oldActivityMyDataChangeMobile;
    @BindView(R.id.new_activity_my_data_change_mobile)
    EditText newActivityMyDataChangeMobile;
    @BindView(R.id.code_activity_my_data_change_mobile)
    EditText codeActivityMyDataChangeMobile;
    @BindView(R.id.get_code_activity_my_data_change_mobile)
    CountDownButton getCodeActivityMyDataChangeMobile;
    @BindView(R.id.sure_activity_my_data_change_mobile)
    TextView sureActivityMyDataChangeMobile;
    @BindView(R.id.activity_my_data_change_mobile)
    LinearLayout activityMyDataChangeMobile;
    String code = "";
    YWLoadingDialog ywLoadingDialog;
    ChangeInfoModel changeUserInfoModel;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data_change_mobile);
        ButterKnife.bind(this);
        customInit(activityMyDataChangeMobile, false, true, false);
        oldActivityMyDataChangeMobile.setText(Config.mobile);
        getCodeActivityMyDataChangeMobile.setOnOperationCallBack(new CountDownButton.CountDownButtonOperationCallBack() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished>2000) {
                    getCodeActivityMyDataChangeMobile.setText("剩余" + millisUntilFinished / 1000 + "秒");
                }else {
                    getCodeActivityMyDataChangeMobile.setText("获取验证码");
                }
            }

            @Override
            public void onClickFromUser(boolean isRunning, long millisUntilFinished) {

            }
        });
        titleTop.setText("修改绑定手机");
    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        if (getCodeActivityMyDataChangeMobile != null) {
            getCodeActivityMyDataChangeMobile.destroy();
        }
        ywLoadingDialog = null;
        changeUserInfoModel = null;
        backTop = null;
        titleTop = null;
        rightButtonTop = null;
        oldActivityMyDataChangeMobile = null;
        newActivityMyDataChangeMobile = null;
        codeActivityMyDataChangeMobile = null;
        getCodeActivityMyDataChangeMobile = null;
        sureActivityMyDataChangeMobile = null;
        activityMyDataChangeMobile = null;
        code = null;
        super.onDestroy();
    }


    @OnClick({R.id.back_top, R.id.get_code_activity_my_data_change_mobile, R.id.sure_activity_my_data_change_mobile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.get_code_activity_my_data_change_mobile:
                if (StringUtils.isEmpty(oldActivityMyDataChangeMobile.getText().toString().trim())){
                    ToastUtils.showShortToast(getBaseActivityContext(),"新手机号不能为空");
                    return;
                }
                try {
                    getSms(newActivityMyDataChangeMobile.getText().toString().trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.sure_activity_my_data_change_mobile:
                if (StringUtils.isEmpty(newActivityMyDataChangeMobile.getText().toString().trim())){
                    ToastUtils.showShortToast(getBaseActivityContext(),"新手机号不能为空");
                    return;
                }
                mobile_isset(newActivityMyDataChangeMobile.getText().toString().trim());

                break;
        }
    }


    /**
     * 确定
     */
    void sure(final String mobile) {
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
        if (mobile.equals(oldActivityMyDataChangeMobile.getText().toString().trim())) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "新号码不能与旧号码相同!");
            }
            return;
        }
        if (StringUtils.isEmpty(code)) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "请先获取动态码");
            }
            return;
        }
        if (!code.equals(codeActivityMyDataChangeMobile.getText().toString().trim())) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "验证码不正确");
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
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.setUserInfo(
                    new JSONObject()
                            .put("mobile", mobile)
                            .put("verify_code",code)
                            .put("old_mobile",oldActivityMyDataChangeMobile.getText().toString().trim())
                            .put("member_id", Config.member_id).toString(), "保存手机号", new StringCallback() {
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
                                        Config.mobile = jsonObject.getJSONObject("data").getString("value");
                                        new SPUtils(getBaseActivityContext(),Config.defaultFileName, Context.MODE_PRIVATE).put("account",Config.mobile);

                                        changeUserInfoModel = new ChangeInfoModel();
                                        changeUserInfoModel.msg = mobile;
                                        changeUserInfoModel.type = getIntent().getExtras().getString("type");
                                        EventBus.getDefault().post(changeUserInfoModel);
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
    /**
     * 获取该账号是否注册
     */
    public void mobile_isset(final String mobile) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.dismiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.mobile_isset(
                    new JSONObject()
                            .put("mobile", mobile)
                            .put("member_id",Config.member_id)
                            .put("verify_code",code)
                            .put("old_mobile",oldActivityMyDataChangeMobile.getText().toString().trim())
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
                                    sure(newActivityMyDataChangeMobile.getText().toString().trim());
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

        if (getCodeActivityMyDataChangeMobile.getmCurrentmillis() > 2000) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "剩余" + (getCodeActivityMyDataChangeMobile.getmCurrentmillis() / 1000) + "秒后再试");
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
            getCodeActivityMyDataChangeMobile.start();
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

}
