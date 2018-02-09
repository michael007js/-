package com.sss.car.view;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.ZhiFuBaoPasswordStyle.PassWordKeyboard;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.rey.material.app.BottomSheetDialog;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedScoreModel;
import com.sss.car.P;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.dao.OnPayPasswordVerificationCallBack;
import com.sss.car.utils.MenuDialog;

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
 * 钱包==>积分==>选择赠予人==>输入积分
 * Created by leilei on 2017/10/25.
 */

public class ActivityWalletIntegralSendInput extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.pic_activity_wallet_integral_send_input)
    SimpleDraweeView picActivityWalletIntegralSendInput;
    @BindView(R.id.text_activity_wallet_integral_send_input)
    TextView textActivityWalletIntegralSendInput;
    @BindView(R.id.click_activity_wallet_integral_send_input)
    LinearLayout clickActivityWalletIntegralSendInput;
    @BindView(R.id.input_activity_wallet_integral_send_input)
    EditText inputActivityWalletIntegralSendInput;
    @BindView(R.id.give_activity_wallet_integral_send_input)
    TextView giveActivityWalletIntegralSendInput;
    @BindView(R.id.activity_wallet_integral_send_input)
    LinearLayout activityWalletIntegralSendInput;
    YWLoadingDialog ywLoadingDialog;
    MenuDialog menuDialog;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (menuDialog!=null){
            menuDialog.clear();
        }
        menuDialog=null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        backTop = null;
        titleTop = null;
        picActivityWalletIntegralSendInput = null;
        textActivityWalletIntegralSendInput = null;
        clickActivityWalletIntegralSendInput = null;
        inputActivityWalletIntegralSendInput = null;
        giveActivityWalletIntegralSendInput = null;
        activityWalletIntegralSendInput = null;
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_integral_send_input);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showLongToast(getBaseActivityContext(), "数据传递错误!");
            finish();
        }
        ButterKnife.bind(this);
        customInit(activityWalletIntegralSendInput, false, true, false);
        titleTop.setText("积分赠予");
        addImageViewList(FrescoUtils.showImage(false, 50, 50,
                Uri.parse(Config.url + getIntent().getExtras().getString("face")),
                picActivityWalletIntegralSendInput, 100f));
        textActivityWalletIntegralSendInput.setText(getIntent().getExtras().getString("username"));
    }

    @OnClick({R.id.back_top, R.id.give_activity_wallet_integral_send_input})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.give_activity_wallet_integral_send_input:
                if (StringUtils.isEmpty(inputActivityWalletIntegralSendInput.getText().toString().trim()) || "0".equals(inputActivityWalletIntegralSendInput.getText().toString().trim())) {
                    ToastUtils.showLongToast(getBaseActivityContext(), "请输入您要赠予的积分");
                    return;
                }
                if (menuDialog == null) {
                    menuDialog = new MenuDialog(getBaseActivity());
                }

                menuDialog.createPasswordInputDialog("确认支付密码", getBaseActivity(), new OnPayPasswordVerificationCallBack() {
                    @Override
                    public void onVerificationPassword(final String password, final PassWordKeyboard passWordKeyboard, final BottomSheetDialog bottomSheetDialog) {
                        P.r(ywLoadingDialog, Config.member_id, password, getBaseActivity(), new P.r() {
                            @Override
                            public void match() {
                                bottomSheetDialog.dismiss();
                                passWordKeyboard.setStatus(true);
                                give_integral(password);
                            }

                            @Override
                            public void mismatches() {
                                passWordKeyboard.setStatus(false);
                            }
                        });
                    }
                });
                break;
        }
    }

    /**
     * 我的钱包==>积分==>赠送
     */
    void give_integral(String password) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.give_integral(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("friend_id", getIntent().getExtras().getString("member_id"))
                            .put("integral",inputActivityWalletIntegralSendInput.getText().toString().trim())
                            .put("password",password)
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
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    EventBus.getDefault().post(new ChangedScoreModel());
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
