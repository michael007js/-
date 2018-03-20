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
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedSet;
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

/**
 * Created by leilei on 2018/1/2.
 */

public class ActivityResetProtectInfo extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.mobile)
    EditText mobile;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.eyes_one)
    HideShowButton eyesOne;
    @BindView(R.id.protect_info)
    EditText protectInfo;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.activity_reset_protect_info)
    LinearLayout activityResetProtectInfo;
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
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_protect_info);
        ButterKnife.bind(this);
        titleTop.setText("重置密保信息");
        customInit(activityResetProtectInfo,false,true,false);
        eyesOne.setOnHideShowButtonCallBack(new HideShowButton.OnHideShowButtonCallBack() {
            @Override
            public void onHideShowButtonClick(boolean isHide) {
                eyesOne.changed(20, 20, getBaseActivityContext());
                if (isHide) {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    @OnClick({R.id.back_top, R.id.save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.save:
                if (StringUtils.isEmpty(mobile.getText().toString().trim())){
                    ToastUtils.showShortToast(getBaseActivityContext(),"请输入注册手机号码");
                    return;
                }
                if (StringUtils.isEmpty(password.getText().toString().trim())){
                    ToastUtils.showShortToast(getBaseActivityContext(),"请输入登录密码");
                    return;
                }
                if (StringUtils.isEmpty(protectInfo.getText().toString().trim())){
                    ToastUtils.showShortToast(getBaseActivityContext(),"请输入新密保信息");
                    return;
                }
                set_reserved();
                break;
        }
    }
    /**
     * 获取用户设置资料
     */
    public void set_reserved() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.set_reserved(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("mobile",mobile.getText().toString().trim())
                            .put("password",password.getText().toString().trim())
                            .put("reserved",protectInfo.getText().toString().trim())
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
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    EventBus.getDefault().post(new ChangedSet());
                                    finish();
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }



}
