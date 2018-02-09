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
import com.sss.car.EventBusModel.FindPassword;
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
 * Created by leilei on 2017/12/3.
 */

public class AccountComplaint extends BaseActivity {

    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.top_parent)
    LinearLayout topParent;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.mobile_account_complaint)
    EditText mobileAccountComplaint;
    @BindView(R.id.id_account_complaint)
    EditText idAccountComplaint;
    @BindView(R.id.password_account_complaint)
    EditText passwordAccountComplaint;
    @BindView(R.id.connect_mobile_account_complaint)
    EditText connectMobileAccountComplaint;
    @BindView(R.id.friend_account_account_complaint)
    EditText friendAccountAccountComplaint;
    @BindView(R.id.business_account_complaint)
    EditText businessAccountComplaint;
    @BindView(R.id.submit_account_complaint)
    TextView submitAccountComplaint;
    @BindView(R.id.account_complaint)
    LinearLayout accountComplaint;
YWLoadingDialog ywLoadingDialog;


    @Override
    protected void onDestroy() {
        if (ywLoadingDialog!=null){
            ywLoadingDialog.dismiss();
        }
        ywLoadingDialog=null;
        super.onDestroy();
    }

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_complaint);
        ButterKnife.bind(this);
        customInit(accountComplaint,false,true,false);
        titleTop.setText("账号申诉");
    }

    @OnClick({R.id.back_top, R.id.submit_account_complaint})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.submit_account_complaint:
                if (StringUtils.isEmpty(mobileAccountComplaint.getText().toString().trim())){
                    ToastUtils.showLongToast(getBaseActivityContext(),"注册手机号不能为空");
                    return;
                }
                if (StringUtils.isEmpty(connectMobileAccountComplaint.getText().toString().trim())){
                    ToastUtils.showLongToast(getBaseActivityContext(),"联系手机号不能为空");
                    return;
                }
                try {
                    addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.accountComplaint(
                            new JSONObject().put("mobile", connectMobileAccountComplaint.getText().toString().trim())
                                    .put("old_mobile",mobileAccountComplaint.getText().toString().trim())
                                    .put("friend_name",friendAccountAccountComplaint.getText().toString().trim())
                                    .put("goods_name",businessAccountComplaint.getText().toString().trim())
                                    .put("account",idAccountComplaint.getText().toString().trim()).toString(),
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
                                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
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
                                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
                                            }
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            })));
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
                }
                break;
        }
    }
}
