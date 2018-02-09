package com.chanjet.auscashier.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.chanjet.auscashier.R;
import com.chanjet.yqpay.Constants$CallBackConstants;
import com.chanjet.yqpay.IYQPayCallback;
import com.chanjet.yqpay.YQPayApi;
import com.chanjet.yqpay.util.MsgUtil;
import com.chanjet.yqpay.util.StringUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/18.
 */

public class RequestPayActivity extends Activity implements View.OnClickListener {

    private LinearLayout llResponse;
    private EditText etGoodsName, etGoodsDetail, etMerUserId, etSellerId, etSubMerchantNo, etEnsureAmount, etCardBegin, etCardEnd, etAmount,
            etOrderNumber, etExpiredTime, etRoyaltyParameters, etNotifyUrl, etSendFlag, etExtension,
            etResOrderNumber, etResReadNumber;
    private RadioGroup rgTradeType;
    private RadioButton rbRight;
    private Button btOK;

    private String tradeType = "11", mOrderNumber = "";
    private ProgressDialog dialog;
    private HashMap<String, String> baseMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_pay);
        initData();
        initView();
    }

    private void initData() {
        baseMap = (HashMap<String, String>) getIntent().getSerializableExtra("reqMap");
    }

    private void initView() {
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etGoodsName = (EditText) findViewById(R.id.et_goods_name);
        etGoodsDetail = (EditText) findViewById(R.id.et_goods_detail);
        etMerUserId = (EditText) findViewById(R.id.et_mer_user_id);
        etSellerId = (EditText) findViewById(R.id.et_seller_id);
        etSubMerchantNo = (EditText) findViewById(R.id.et_sub_merchant_no);
        rgTradeType = (RadioGroup) findViewById(R.id.rg_trade_type);
        etEnsureAmount = (EditText) findViewById(R.id.et_ensure_amount);
        etCardBegin = (EditText) findViewById(R.id.et_card_begin);
        etCardEnd = (EditText) findViewById(R.id.et_card_end);
        etAmount = (EditText) findViewById(R.id.et_trx_amt);
        btOK = (Button) findViewById(R.id.bt_ok);

        rgTradeType.setOnCheckedChangeListener(listener);
        rbRight = (RadioButton) findViewById(R.id.radioButton1);
        rbRight.setChecked(true);
        btOK.setOnClickListener(this);

        etOrderNumber = (EditText) findViewById(R.id.et_order_number);
        mOrderNumber = StringUtils.getOrderid();
        etOrderNumber.setText(mOrderNumber);
        etExpiredTime = (EditText) findViewById(R.id.et_expired_time);
        etRoyaltyParameters = (EditText) findViewById(R.id.et_royalty_parameters);
        etNotifyUrl = (EditText) findViewById(R.id.et_notify_url);
        etSendFlag = (EditText) findViewById(R.id.et_send_flag);
        etExtension = (EditText) findViewById(R.id.et_extension);

        llResponse = (LinearLayout) findViewById(R.id.ll_response_message);
        llResponse.setVisibility(View.INVISIBLE);
        etResOrderNumber = (EditText) findViewById(R.id.et_response_order_number);
        etResReadNumber = (EditText) findViewById(R.id.et_response_read_number);
    }

    private RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            tradeType = (String) rgTradeType.findViewById(checkedId).getTag();
            MsgUtil.showDebugLog("WJDemo", "trade type tag is : " + tradeType + ", type is : " + ((RadioButton) rgTradeType.findViewById(checkedId)).getText());
        }
    };

    private boolean checkInput() {
        if (StringUtils.isEmpty(etExpiredTime.getText().toString().trim())) {
            MsgUtil.showToastShort(RequestPayActivity.this, "订单有效期错误");
            return false;
        }
        if (StringUtils.isEmpty(etSendFlag.getText().toString().trim())) {
            MsgUtil.showToastShort(RequestPayActivity.this, "请输入短信发送标识");
            return false;
        }
        if (etGoodsName.getText().toString().trim() == null || "".equals(etGoodsName.getText().toString().trim())) {
            MsgUtil.showToastShort(RequestPayActivity.this, "请输入正确的商品名");
            return false;
        }
        if (etMerUserId.getText().toString().trim() == null || "".equals(etMerUserId.getText().toString().trim())) {
            MsgUtil.showToastShort(RequestPayActivity.this, "请输入正确的用户标识");
            return false;
        }
        if (etSellerId.getText().toString().trim() == null || "".equals(etSellerId.getText().toString().trim())) {
            MsgUtil.showToastShort(RequestPayActivity.this, "请输入正确的卖家ID");
            return false;
        }
        if (tradeType == null || "".equals(tradeType)) {
            MsgUtil.showToastShort(RequestPayActivity.this, "请选择交易类型");
            return false;
        }
        if (tradeType.equals("12") && (etEnsureAmount.getText().toString().trim() == null || "".equals(etEnsureAmount.getText().toString().trim()))) {
            MsgUtil.showToastShort(RequestPayActivity.this, "请输入担保金额");
            return false;
        }
        if (etGoodsName.getText().toString().trim() == null || "".equals(etGoodsName.getText().toString().trim())) {
            MsgUtil.showToastShort(RequestPayActivity.this, "请输入正确的商品名");
            return false;
        }
        if (etCardBegin.getText().toString().trim() == null || etCardBegin.getText().toString().trim().length() != 6) {
            MsgUtil.showToastShort(RequestPayActivity.this, "请输入卡号前六位");
            return false;
        }
        if (etCardEnd.getText().toString().trim() == null || etCardEnd.getText().toString().trim().length() != 4) {
            MsgUtil.showToastShort(RequestPayActivity.this, "请输入卡号后四位");
            return false;
        }
        if (etAmount.getText().toString().trim() == null || "".equals(etAmount.getText().toString().trim())) {
            MsgUtil.showToastShort(RequestPayActivity.this, "请输入卡号后四位");
            return false;
        }
        return true;
    }

    private void do_request_pay() {
        dialog = ProgressDialog.show(this, "提示", "处理中，请稍后...");
        dialog.setCancelable(true);
        YQPayApi.doPay(RequestPayActivity.this, getRequestPayParam(), new IYQPayCallback() {
            @Override
            public void payResult(String status, String message) {
                dialog.dismiss();
                MsgUtil.showDebugLog("WJDemo", "bind card result : " + message);
                if (status.equals(Constants$CallBackConstants.CALLBACK_FAILD)) {
                    Toast.makeText(RequestPayActivity.this, message, Toast.LENGTH_LONG).show();
                } else {
                    Gson gson = new Gson();
                    HashMap<String, String> resMap = gson.fromJson(message, HashMap.class);
                    Toast.makeText(RequestPayActivity.this, resMap.get("RetMsg"), Toast.LENGTH_LONG).show();
                    String TrxId = resMap.get("TrxId");
                    Intent intent = new Intent();
                    intent.putExtra("TrxId", TrxId);
                    intent.putExtra("TradeType", "pay_order");
                    llResponse.setVisibility(View.VISIBLE);
                    etResOrderNumber.setText(TrxId);
                    etResReadNumber.setText(resMap.get("OrderTrxId"));
                    setResult(1, intent);
                    finish();
                }
            }
        });
    }

    private Map<String, String> getRequestPayParam() {
        HashMap<String, String> reqMap;
        if (baseMap == null || baseMap.size() == 0) {
            showConfirmDialog();
        }
        reqMap = (HashMap<String, String>) baseMap.clone();
        reqMap.put("TrxId", mOrderNumber);
        reqMap.put("OrdrName", etGoodsName.getText().toString().trim());
        if (!StringUtils.isEmpty(etGoodsDetail.getText().toString().trim()))
            reqMap.put("OrdrDesc", etGoodsDetail.getText().toString().trim());
        reqMap.put("MerUserId", etMerUserId.getText().toString().trim());
        reqMap.put("SellerId", etSellerId.getText().toString().trim());
        if (!StringUtils.isEmpty(etSubMerchantNo.getText().toString().trim()))
            reqMap.put("SubMerchantNo", etSubMerchantNo.getText().toString().trim());
        if (!StringUtils.isEmpty(etEnsureAmount.getText().toString().trim()))
            reqMap.put("EnsureAmount", etEnsureAmount.getText().toString().trim());
        reqMap.put("TradeType", tradeType);
        reqMap.put("CardBegin", etCardBegin.getText().toString().trim());
        reqMap.put("CardEnd", etCardEnd.getText().toString().trim());
        reqMap.put("TrxAmt", etAmount.getText().toString().trim());
        reqMap.put("BankCode", "RequestPay");
        reqMap.put("ExpiredTime", etExpiredTime.getText().toString().trim());
        reqMap.put("SmsFlag", etSendFlag.getText().toString().trim());
        if (!StringUtils.isEmpty(etNotifyUrl.getText().toString().trim()))
            reqMap.put("NotifyUrl", etNotifyUrl.getText().toString().trim());
        if (!StringUtils.isEmpty(etRoyaltyParameters.getText().toString().trim()))
            reqMap.put("RoyaltyParameters", etRoyaltyParameters.getText().toString().trim());//交易金额分润账号集
        if (!StringUtils.isEmpty(etExtension.getText().toString().trim()))
            reqMap.put("Extension", etExtension.getText().toString().trim());//扩展字段
        return reqMap;
    }

    private void showConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("订单数据异常，请重试！")
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        setResult(2);
                        finish();
                    }
                })
                .show();
    }

    @Override
    public void onClick(View v) {
        if (v == btOK) {
            if (checkInput()) {
                do_request_pay();
            }
        }
    }
}
