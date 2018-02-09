package com.sss.car.wallet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.sss.car.Config;
import com.sss.car.EventBusModel.BindCardModel;
import com.sss.car.EventBusModel.ChangedWalletModel;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.BankModel;
import com.sss.car.utils.PayUtils;
import com.sss.car.view.ActivityWeb;
import com.sss.car.view.WalletAddBank;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 我的资金==>充值
 * Created by leilei on 2017/10/26.
 */

public class WalletRecharge extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.input_wallet_recharge)
    EditText inputWalletRecharge;
    @BindView(R.id.next_wallet_recharge)
    TextView nextWalletRecharge;
    @BindView(R.id.wallet_recharge)
    LinearLayout walletRecharge;
    @BindView(R.id.logo)
    SimpleDraweeView logo;
    @BindView(R.id.name)
    TextView name;
    YWLoadingDialog ywLoadingDialog;
    BankModel bankModel;
    @BindView(R.id.click)
    LinearLayout click;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        backTop = null;
        titleTop = null;
        inputWalletRecharge = null;
        nextWalletRecharge = null;
        walletRecharge = null;
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedWalletModel changedWalletModel) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BindCardModel bindCardModel) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ("bank".equals(getIntent().getExtras().getString("type"))) {
            get_default();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误！");
            finish();
        }
        setContentView(R.layout.wallet_recharge);
        ButterKnife.bind(this);
        customInit(walletRecharge, false, true, true);
        titleTop.setText("充值");
        if ("bank".equals(getIntent().getExtras().getString("type"))) {
//            addImageViewList(FrescoUtils.showImage(false, 40, 40, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseActivityContext()) + "/" + R.mipmap.logo_bank), logo, 0f));
            name.setText("绑定银行卡");
        } else if ("wx".equals(getIntent().getExtras().getString("type"))) {
            addImageViewList(FrescoUtils.showImage(false, 80, 80, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseActivityContext()) + "/" + R.mipmap.logo_wx), logo, 0f));
            name.setText("微信");
        } else if ("zfb".equals(getIntent().getExtras().getString("type"))) {
            addImageViewList(FrescoUtils.showImage(false, 80, 80, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseActivityContext()) + "/" + R.mipmap.logo_zfb), logo, 0f));
            name.setText("支付宝");
        }
        inputWalletRecharge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ("0".equals(s.toString())) {
                    APPOftenUtils.setBackgroundOfVersion(nextWalletRecharge, getResources().getDrawable(R.drawable.bg_button_small_ra1));
                    nextWalletRecharge.setTextColor(getResources().getColor(R.color.textColor));
                } else {
                    APPOftenUtils.setBackgroundOfVersion(nextWalletRecharge, getResources().getDrawable(R.drawable.bg_button_small_ra));
                    nextWalletRecharge.setTextColor(getResources().getColor(R.color.white));
                }
            }
        });
    }

    @OnClick({R.id.back_top, R.id.next_wallet_recharge,R.id.click})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.click:
                if (bankModel != null) {
                    if ("bank".equals(getIntent().getExtras().getString("type"))) {
                        if (!StringUtils.isEmpty(bankModel.card_id)) {
                            if (getBaseActivityContext() != null) {
                                startActivity(new Intent(getBaseActivityContext(), WalletBankList.class));
                            }
                        } else {
                            if (getBaseActivityContext() != null) {
                                startActivity(new Intent(getBaseActivityContext(), ActivityWeb.class)
                                        .putExtra("type", ActivityWeb.BANK_BIND)
                                .putExtra("card_id",bankModel.card_id));
                            }
//                        if (getBaseActivityContext() != null) {
//                            startActivity(new Intent(getBaseActivityContext(), WalletAddBank.class));
//                        }
                        }
                    } else {
                        ToastUtils.showLongToast(getBaseActivityContext(), "银行卡信息获取中Err-2");
                    }
                }
                break;
            case R.id.back_top:
                finish();
                break;
            case R.id.next_wallet_recharge:
                if (StringUtils.isEmpty(inputWalletRecharge.getText().toString().trim()) || "0".equals(inputWalletRecharge.getText().toString().trim())) {
//                    ToastUtils.showLongToast(getBaseActivityContext(), "请填写充值金额");
                    return;
                }


                if ("bank".equals(getIntent().getExtras().getString("type"))) {
                    if (bankModel == null) {
                        ToastUtils.showLongToast(getBaseActivityContext(), "银行卡信息获取中Err-3");
                        return;
                    }
                    if (StringUtils.isEmpty(bankModel.card_id)) {
                        ToastUtils.showLongToast(getBaseActivityContext(), "银行卡信息获取中Err-4");
                        return;
                    }
                    if (getBaseActivityContext() != null) {
                        startActivity(new Intent(getBaseActivityContext(), ActivityWeb.class)
                                .putExtra("type", ActivityWeb.BANK_RECHANGE)
                                .putExtra("money",inputWalletRecharge.getText().toString().trim())
                                .putExtra("card_id",bankModel.card_id));
                    }

                } else if ("wx".equals(getIntent().getExtras().getString("type"))) {
                    PayUtils.payment_into(ywLoadingDialog, "0", getBaseActivity(), inputWalletRecharge.getText().toString().trim(), 0, 5, Config.member_id, "2",0);
                } else if ("zfb".equals(getIntent().getExtras().getString("type"))) {
                    PayUtils.payment_into(ywLoadingDialog, "0", getBaseActivity(), inputWalletRecharge.getText().toString().trim(), 0, 5, Config.member_id, "3",0);
                }
                break;
        }
    }
    public void get_default() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.get_default(
                    new JSONObject()
                            .put("member_id", Config.member_id)
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
                                    bankModel = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), BankModel.class);
                                    if (StringUtils.isEmpty(bankModel.bank_name)){
                                        name.setText("绑定银行卡");
                                    }else {
                                        name.setText(bankModel.bank_name);
                                    }

                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }
}
