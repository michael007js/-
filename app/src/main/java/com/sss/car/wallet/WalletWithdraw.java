package com.sss.car.wallet;

import android.content.Intent;
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
import com.blankj.utilcode.customwidget.ZhiFuBaoPasswordStyle.PassWordKeyboard;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.sss.car.Config;
import com.sss.car.EventBusModel.BindCardModel;
import com.sss.car.EventBusModel.ChangedWalletModel;
import com.sss.car.P;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.dao.OnPayPasswordVerificationCallBack;
import com.sss.car.model.BankModel;
import com.sss.car.utils.MenuDialog;
import com.sss.car.view.ActivityBangCardBind;
import com.sss.car.view.ActivityMyDataSetPassword;
import com.sss.car.view.ActivityMyDataSynthesizSettingSetPayPassword;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.sss.car.R.id.bank_name;

/**
 * 我的资金==>提现
 * Created by leilei on 2017/10/26.
 */

public class WalletWithdraw extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.input_wallet_withdraw)
    EditText inputWalletWithdraw;
    @BindView(R.id.next_wallet_withdraw)
    TextView nextWalletWithdraw;
    @BindView(R.id.wallet_withdraw)
    LinearLayout WalletWithdraw;
    @BindView(R.id.can_wallet_withdraw)
    TextView canWalletWithdraw;
    MenuDialog menuDialog;
    YWLoadingDialog ywLoadingDialog;
    BankModel bankModel;
    int expendable = 0;
    @BindView(bank_name)
    TextView bankName;
    @BindView(R.id.bank)
    LinearLayout bank;
    String content;

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
        inputWalletWithdraw = null;
        nextWalletWithdraw = null;
        canWalletWithdraw = null;
        WalletWithdraw = null;
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BankModel bankModel) {
        this.bankModel = bankModel;
    }

    @Override
    protected void onResume() {
        super.onResume();
        get_default();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BindCardModel bankModel) {
        get_default();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_withdraw);
        ButterKnife.bind(this);
        customInit(WalletWithdraw, false, true, true);
        titleTop.setText("提现");
        inputWalletWithdraw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ("0".equals(s)) {
                    inputWalletWithdraw.setText("");
                }
                if (StringUtils.isEmpty(s.toString())) {
                    canWalletWithdraw.setText("可用于提现的金额:  " + expendable + "元");
                } else {
//                    canWalletWithdraw.setText(content);
                    terrace_withdraw(inputWalletWithdraw.getText().toString());
                    if (expendable < Integer.valueOf(inputWalletWithdraw.getText().toString())) {
                        inputWalletWithdraw.setText(expendable+"");
                        inputWalletWithdraw.setSelection(s.toString().length()-1);
                        ToastUtils.showShortToast(getBaseActivityContext(),"可用于提现的金额为:  " + expendable + "元");
                    }
                }
            }
        });
        get_default();
        my_balance();

    }

    @OnClick({R.id.back_top, R.id.next_wallet_withdraw, R.id.bank})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bank:
                if (bankModel != null) {
                    if (!StringUtils.isEmpty(bankModel.card_id)) {
                        if (getBaseActivityContext() != null) {
                            startActivity(new Intent(getBaseActivityContext(), WalletBankList.class)
                            .putExtra("isHidemobile",true));
                        }
                    } else {
                        if (getBaseActivityContext() != null) {
//                            startActivity(new Intent(getBaseActivityContext(), ActivityWeb.class)
//                                    .putExtra("type",ActivityWeb.BANK_BIND));
                            startActivity(new Intent(getBaseActivityContext(), ActivityBangCardBind.class)
                            .putExtra("isHidemobile",true));
                        }
//                        if (getBaseActivityContext() != null) {
//                            startActivity(new Intent(getBaseActivityContext(), WalletAddBank.class));
//                        }
                    }
                } else {
                    ToastUtils.showLongToast(getBaseActivityContext(), "银行卡信息获取中Err-2");
                }

                break;
            case R.id.back_top:
                finish();
                break;
            case R.id.next_wallet_withdraw:
                if (expendable == 0) {
                    ToastUtils.showLongToast(getBaseActivityContext(), "您的可用余额为0...");
                    return;
                }
                if (bankModel == null) {
                    ToastUtils.showLongToast(getBaseActivityContext(), "银行卡信息获取中Err-3");
                    return;
                }
                if (StringUtils.isEmpty(bankModel.card_id)) {
                    ToastUtils.showLongToast(getBaseActivityContext(), "请绑定银行卡");
                    startActivity(new Intent(getBaseActivityContext(), ActivityBangCardBind.class)
                    .putExtra("isHidemobile",true));
                    return;
                }
                if (StringUtils.isEmpty(inputWalletWithdraw.getText().toString().trim())) {
                    ToastUtils.showLongToast(getBaseActivityContext(), "请填写提现金额");
                    return;
                }

                P.e(ywLoadingDialog, Config.member_id, getBaseActivity(), new P.p() {
                    @Override
                    public void exist() {
                        if (menuDialog == null) {
                            menuDialog = new MenuDialog(getBaseActivity());
                        }
                        menuDialog.createPasswordInputDialog("请输入您的支付密码", getBaseActivity(), new OnPayPasswordVerificationCallBack() {
                            @Override
                            public void onVerificationPassword(String password, final PassWordKeyboard passWordKeyboard, final com.rey.material.app.BottomSheetDialog bottomSheetDialog) {
                                P.r(ywLoadingDialog, Config.member_id, password, getBaseActivity(), new P.r() {
                                    @Override
                                    public void match() {
                                        bottomSheetDialog.dismiss();
                                        passWordKeyboard.setStatus(true);
                                        withdraw();
                                    }

                                    @Override
                                    public void mismatches() {
                                        passWordKeyboard.setStatus(false);
                                    }
                                });
                            }

                        });
                    }

                    @Override
                    public void nonexistence() {
                        if (getBaseActivityContext() != null) {
                            startActivity(new Intent(getBaseActivityContext(), ActivityMyDataSynthesizSettingSetPayPassword.class)
                                    .putExtra("mode",ActivityMyDataSynthesizSettingSetPayPassword.set));
                        }
                    }
                });
                break;
        }
    }

    public void terrace_withdraw(String money) {
//        if (ywLoadingDialog != null) {
//            ywLoadingDialog.disMiss();
//        }
//        ywLoadingDialog = null;
//        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
//        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.terrace_withdraw(
                    new JSONObject()
                            .put("money",money)
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
                                    content = jsonObject.getJSONObject("data").getString("contents");
                                    canWalletWithdraw.setText(content);
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
                                    if (StringUtils.isEmpty(bankModel.bank_name)) {
                                        bankName.setText("绑定银行卡");
                                    } else {
                                        bankName.setText(bankModel.bank_name);
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

    void my_balance() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.my_balance(
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
                                    expendable = jsonObject.getJSONObject("data").getInt("expendable");
                                    canWalletWithdraw.setText("可用于提现的金额:  " + expendable + "元");
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

    void withdraw() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.withdraw(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("money", inputWalletWithdraw.getText().toString().trim())
                            .put("card_id", bankModel.card_id)
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
                                    EventBus.getDefault().post(new ChangedWalletModel());
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
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
