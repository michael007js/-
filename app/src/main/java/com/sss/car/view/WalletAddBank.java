package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chanjet.yqpay.Constants$CallBackConstants;
import com.chanjet.yqpay.IYQPayCallback;
import com.chanjet.yqpay.YQPayApi;
import com.chanjet.yqpay.util.MsgUtil;
import com.google.gson.Gson;
import com.sss.car.Config;
import com.sss.car.EventBusModel.BindCardModel;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.BankModel;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by leilei on 2018/1/28.
 */

public class WalletAddBank extends BaseActivity {
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.holder)
    EditText holder;
    @BindView(R.id.card)
    EditText card;
    @BindView(R.id.idcard)
    EditText idcard;
    @BindView(R.id.mobile)
    EditText mobile;
    @BindView(R.id.wallet_add_bank)
    LinearLayout walletAddBank;
    YWLoadingDialog ywLoadingDialog;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_add_bank);
        ButterKnife.bind(this);
        customInit(walletAddBank, false, true, false);
        titleTop.setText("绑定银行卡");
    }

    @OnClick({R.id.back_top, R.id.complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.complete:
                if (StringUtils.isEmpty(holder.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请输入持卡人");
                    return;
                }
                if (StringUtils.isEmpty(card.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请输入身份证号");
                    return;
                }
                if (StringUtils.isEmpty(idcard.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请输入卡号");
                    return;
                }
                if (StringUtils.isEmpty(mobile.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请输入手机号");
                    return;
                }
                auth_card();
                break;
        }
    }

    void auth_card() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.auth_card(
                    new JSONObject()
                            .put("cardholder", holder.getText().toString().trim())
                            .put("card_num", card.getText().toString().trim())
                            .put("id_card", idcard.getText().toString().trim())
                            .put("mobile", mobile.getText().toString().trim())
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
                                    format(jsonObject.getJSONObject("data"));

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

    void format(JSONObject jsonObject) throws JSONException {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("Version", jsonObject.getString("Version"));
        maps.put("TradeDate", jsonObject.getString("TradeDate"));
        maps.put("TradeTime", jsonObject.getString("TradeTime"));
        maps.put("PartnerId", jsonObject.getString("PartnerId"));
        maps.put("InputCharset", jsonObject.getString("InputCharset"));
        maps.put("MchId", jsonObject.getString("MchId"));
        maps.put("PAY_KEY", jsonObject.getString("PAY_KEY"));
        maps.put("TrxId", jsonObject.getString("TrxId"));
        maps.put("ExpiredTime", jsonObject.getString("ExpiredTime"));
        maps.put("BankCode", jsonObject.getString("BankCode"));
        maps.put("BkAcctTp", jsonObject.getString("BkAcctTp"));
        maps.put("BkAcctNo", card.getText().toString().trim());
        maps.put("IDTp", jsonObject.getString("IDTp"));
        maps.put("IDNo", jsonObject.getString("TradeDate"));
        maps.put("TradeDate", idcard.getText().toString().trim());
        maps.put("CstmrNm", holder.getText().toString().trim());
        maps.put("MobNo", mobile.getText().toString().trim());
        maps.put("MerUserId", jsonObject.getString("MerUserId"));
        maps.put("SmsFlag", jsonObject.getString("SmsFlag"));

        YQPayApi.doPay(this, maps, new IYQPayCallback() {
            @Override
            public void payResult(String status, String message) {
                MsgUtil.showDebugLog("WJDemo", "bind card result : " + message);
                if (status.equals("failed")) {
                    Toast.makeText(getBaseActivityContext(), message, Toast.LENGTH_LONG).show();
                } else {
                    Gson gson = new Gson();
                    HashMap<String, String> resMap = gson.fromJson(message, HashMap.class);
                    Toast.makeText(getBaseActivityContext(), resMap.get("RetMsg"), Toast.LENGTH_LONG).show();
                    String TrxId = resMap.get("TrxId");

//                    llResponse.setVisibility(View.VISIBLE);
//                    etResOrderNumber.setText(TrxId);
//                    etResReadNumber.setText(resMap.get("OrderTrxId"));
                    insert_card();
                    LogUtils.e(TrxId);
                }
            }
        });
    }


    void insert_card() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.insert_card(
                    new JSONObject()
                            .put("cardholder", holder.getText().toString().trim())
                            .put("card_num", card.getText().toString().trim())
                            .put("id_card", idcard.getText().toString().trim())
                            .put("mobile", mobile.getText().toString().trim())
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
                                    EventBus.getDefault().post(new BindCardModel());
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
