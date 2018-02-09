package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
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
 * 密码修改
 * Created by leilei on 2017/11/7.
 */

public class ActivityMyDataSynthesizeSettingPasswordManager extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.click_login_password)
    LinearLayout clickLoginPassword;
    @BindView(R.id.click_pay_password)
    LinearLayout clickPayPassword;
    @BindView(R.id.activity_my_data_synthesize_setting_password_manager)
    LinearLayout activityMyDataSynthesizeSettingPasswordManager;
    YWLoadingDialog ywLoadingDialog;
    String isExits;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        clickLoginPassword = null;
        clickPayPassword = null;
        activityMyDataSynthesizeSettingPasswordManager = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data_synthesize_setting_password_manager);

        ButterKnife.bind(this);
        titleTop.setText("密码修改");
        customInit(activityMyDataSynthesizeSettingPasswordManager, false, true, false);
        isExits();
    }

    @OnClick({R.id.back_top, R.id.click_login_password, R.id.click_pay_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.click_login_password:
                if (getBaseActivityContext()!=null){
                    startActivity(new Intent(getBaseActivityContext(),ActivityMyDataSynthesizSettingChangeLoginPassword.class));
                }
                break;
            case R.id.click_pay_password:
                if ("1".equals(isExits)){
                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataSynthesizSettingSetPayPassword.class)
                    .putExtra("mode",ActivityMyDataSynthesizSettingSetPayPassword.changed));
                }else  if ("0".equals(isExits)){
                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataSynthesizSettingSetPayPassword.class)
                            .putExtra("mode",ActivityMyDataSynthesizSettingSetPayPassword.set));
                }else {
                    ToastUtils.showShortToast(getBaseActivityContext(), "信息获取中");
                }
                    break;
        }
    }

    void isExits()   {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog=null;
        ywLoadingDialog=new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            new RequestModel(System.currentTimeMillis() + "", RequestWeb.exits_pass(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .toString()
                    , new StringCallback() {
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
                                    isExits=jsonObject.getJSONObject("data").getString("code");
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:p-0");
                                e.printStackTrace();
                            }
                        }
                    }));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:p-0");
            e.printStackTrace();
        }
    }
}
