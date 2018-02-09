package com.sss.car.view;

import android.os.Bundle;
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

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 设置登录密码
 * Created by leilei on 2017/11/7.
 */

public class ActivityMyDataSynthesizSettingChangeLoginPassword extends BaseActivity {
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

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog!=null){
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog=null;
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
        setContentView(R.layout.activity_my_data_synthesize_setting_set_pay_password);
        ButterKnife.bind(this);
        customInit(activityMyDataSynthesizeSettingSetPayPassword, false, true, false);
        titleTop.setText("登录密码");
    }

    @OnClick({R.id.back_top, R.id.sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.sure:
                if (StringUtils.isEmpty(oidPassword.getText().toString().trim())){
                    ToastUtils.showShortToast(getBaseActivityContext(),"请输入旧密码");
                    return;
                }
                if (StringUtils.isEmpty(newOnePassword.getText().toString().trim())||StringUtils.isEmpty(newTwoPassword.getText().toString().trim())){
                    ToastUtils.showShortToast(getBaseActivityContext(),"请输入新密码");
                    return;
                }
                if (!newOnePassword.getText().toString().trim().equals(newTwoPassword.getText().toString().trim())){
                    ToastUtils.showShortToast(getBaseActivityContext(),"新密码不一致");
                    return;
                }
                try {
                    save();
                } catch (JSONException e) {
                    e.printStackTrace();
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
                        .put("old_pass",oidPassword.getText().toString().trim())
                        .put("password",newTwoPassword.getText().toString().trim())
                        .put("member_id", Config.member_id).toString(), "设置登录密码", new StringCallback() {
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
}
