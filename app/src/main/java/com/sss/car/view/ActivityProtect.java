package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Tab.tab.ScrollTab;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.SettingModel;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by leilei on 2017/12/30.
 */

public class ActivityProtect extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.show_login_password)
    TextView showLoginPassword;
    @BindView(R.id.click_login_password)
    LinearLayout clickLoginPassword;
    @BindView(R.id.show_pay_password)
    TextView showPayPassword;
    @BindView(R.id.click_pay_password)
    LinearLayout clickPayPassword;
    @BindView(R.id.show_password_protect)
    TextView showPasswordProtect;
    @BindView(R.id.click_password_protect)
    LinearLayout clickPasswordProtect;
    @BindView(R.id.activity_protect)
    LinearLayout activityProtect;
    YWLoadingDialog ywLoadingDialog;
    @BindView(R.id.parent)
    LinearLayout parent;
    String code;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {
    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog!=null){
            ywLoadingDialog.disMiss();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protect);
        ButterKnife.bind(this);
        customInit(activityProtect, false, true, false);
        titleTop.setText("密保设置");
    }

    @OnClick({R.id.back_top, R.id.click_login_password, R.id.click_pay_password, R.id.click_password_protect})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.click_login_password:
                startActivity(new Intent(getBaseActivityContext(),ActivityMyDataSynthesizeSettingSetLoginPassword.class));
                break;
            case R.id.click_pay_password:
                if ("1".equals(code)) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataSynthesizSettingSetPayPassword.class)
                            .putExtra("mode",ActivityMyDataSynthesizSettingSetPayPassword.changed));
                } else if ("0".equals(code)) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataSynthesizSettingSetPayPassword.class)
                            .putExtra("mode",ActivityMyDataSynthesizSettingSetPayPassword.set));
                } else {
                    ToastUtils.showShortToast(getBaseActivityContext(), "信息获取中");
                    isExits();
                }
                break;
            case R.id.click_password_protect:
                startActivity(new Intent(getBaseActivityContext(),ActivityMyDataSynthesizeSettingSetProtectInfo.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isExits();

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
                                parent.setVisibility(View.VISIBLE);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    code=jsonObject.getJSONObject("data").getString("code");
                                    if ("1".equals(code)) {
                                        showPayPassword.setText("去修改");
                                    } else {
                                        showPayPassword.setText("去设置");
                                    }
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
