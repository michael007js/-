package com.chanjet.auscashier.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.chanjet.auscashier.EntryActivity;
import com.chanjet.auscashier.R;
import com.chanjet.auscashier.WebViewActivity;
import com.chanjet.yqpay.Constants$CallBackConstants;
import com.chanjet.yqpay.IYQPayCallback;
import com.chanjet.yqpay.YQPayApi;
import com.chanjet.yqpay.util.MsgUtil;
import com.chanjet.yqpay.util.RSA;
import com.chanjet.yqpay.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/18.
 */

public class FrontEndDirectRequestPayActivity extends Activity implements View.OnClickListener {

    private LinearLayout llCVV2, llCardTerm;
    private EditText etGoodsName, etGoodsDetail, etMerUserId, etSellerId, etSubMerchantNo, etEnsureAmount, etAmount,
            etOrderNumber, etExpiredTime, etRoyaltyParameters, etNotifyUrl, etReturnUrl, etExtension;
    private EditText etBankCardNumber, etIDCardNumber, etHolderName, etPhoneNumber, etCVV2, etCardTerm;
    private RadioGroup rgTradeType;
    private RadioButton rbRight;
    private Button btGenerateOrder, btOK;

    private String tradeType = "11", mOrderNumber = "";
    private ProgressDialog dialog;
    private HashMap<String, String> baseMap;
    private String cardType = null;
    private RadioGroup rgCardType;

    private static final int REQUEST_CODE = 8888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.front_end_activity_direct_request_pay);
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
        etAmount = (EditText) findViewById(R.id.et_trx_amt);
        btOK = (Button) findViewById(R.id.bt_ok);

        etOrderNumber = (EditText) findViewById(R.id.et_order_number);
        btGenerateOrder = (Button) findViewById(R.id.bt_generate_order);
        etExpiredTime = (EditText) findViewById(R.id.et_expired_time);
        etRoyaltyParameters = (EditText) findViewById(R.id.et_royalty_parameters);
        etNotifyUrl = (EditText) findViewById(R.id.et_notify_url);
        etReturnUrl = (EditText) findViewById(R.id.et_return_url);
        etExtension = (EditText) findViewById(R.id.et_extension);

        etBankCardNumber = (EditText) findViewById(R.id.et_bank_card_number);
        etIDCardNumber = (EditText) findViewById(R.id.et_id_card_number);
        etHolderName = (EditText) findViewById(R.id.et_holder_name);
        etPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
        etCVV2 = (EditText) findViewById(R.id.et_cvv2);
        etCardTerm = (EditText) findViewById(R.id.et_card_term);
        llCVV2 = (LinearLayout) findViewById(R.id.ll_cvv2);
        llCardTerm = (LinearLayout) findViewById(R.id.ll_credit_term);
        rgCardType = (RadioGroup) findViewById(R.id.rg_card_type);

        mOrderNumber = StringUtils.getOrderid();
        etOrderNumber.setText(mOrderNumber);
        btGenerateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrderNumber = StringUtils.getOrderid();
                etOrderNumber.setText(mOrderNumber);
            }
        });
        etSellerId.setText(baseMap.get("MchId"));
        rgTradeType.setOnCheckedChangeListener(listener);
        rbRight = (RadioButton) findViewById(R.id.radioButton1);
        rbRight.setChecked(true);
        rgCardType.setOnCheckedChangeListener(cardTypeListener);
        btOK.setOnClickListener(this);
    }

    private RadioGroup.OnCheckedChangeListener cardTypeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            cardType = (String) rgCardType.findViewById(checkedId).getTag();
            MsgUtil.showDebugLog("WJDemo", "card type is : " + rgCardType.findViewById(checkedId).getTag());
            changeViewState(cardType);
        }
    };

    private void changeViewState(String str) {
        if (str.equals("00")) {
            llCVV2.setVisibility(View.VISIBLE);
            llCardTerm.setVisibility(View.VISIBLE);
        } else if (str.equals("01")) {
            llCVV2.setVisibility(View.GONE);
            llCardTerm.setVisibility(View.GONE);
        }
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
            MsgUtil.showToastShort(FrontEndDirectRequestPayActivity.this, "订单有效期错误");
            return false;
        }
        if (etGoodsName.getText().toString().trim() == null || "".equals(etGoodsName.getText().toString().trim())) {
            MsgUtil.showToastShort(FrontEndDirectRequestPayActivity.this, "请输入正确的商品名");
            return false;
        }
        if (etMerUserId.getText().toString().trim() == null || "".equals(etMerUserId.getText().toString().trim())) {
            MsgUtil.showToastShort(FrontEndDirectRequestPayActivity.this, "请输入正确的用户标识");
            return false;
        }
        if (etSellerId.getText().toString().trim() == null || "".equals(etSellerId.getText().toString().trim())) {
            MsgUtil.showToastShort(FrontEndDirectRequestPayActivity.this, "请输入正确的卖家ID");
            return false;
        }
        if (tradeType == null || "".equals(tradeType)) {
            MsgUtil.showToastShort(FrontEndDirectRequestPayActivity.this, "请选择交易类型");
            return false;
        }
        if (tradeType.equals("12") && (etEnsureAmount.getText().toString().trim() == null || "".equals(etEnsureAmount.getText().toString().trim()))) {
            MsgUtil.showToastShort(FrontEndDirectRequestPayActivity.this, "请输入担保金额");
            return false;
        }
        if (etGoodsName.getText().toString().trim() == null || "".equals(etGoodsName.getText().toString().trim())) {
            MsgUtil.showToastShort(FrontEndDirectRequestPayActivity.this, "请输入正确的商品名");
            return false;
        }

        if (!(StringUtils.isEmpty(etBankCardNumber.getText().toString().trim()) && StringUtils.isEmpty(etIDCardNumber.getText().toString().trim())
                && StringUtils.isEmpty(etIDCardNumber.getText().toString().trim()) && StringUtils.isEmpty(etHolderName.getText().toString().trim())
                && StringUtils.isEmpty(etPhoneNumber.getText().toString().trim()))) {
            if (etBankCardNumber.getText().toString().trim().length() < 16) {
                MsgUtil.showToastShort(FrontEndDirectRequestPayActivity.this, "卡号错误");
                return false;
            }
            if (cardType == null || cardType.equals("")) {
                MsgUtil.showToastShort(FrontEndDirectRequestPayActivity.this, "请选择卡类型");
                return false;
            }
            if (cardType.equals("00")) {
                if (StringUtils.isEmpty(etCVV2.getText().toString().trim()) || etCVV2.getText().toString().trim().length() != 3) {
                    MsgUtil.showToastShort(FrontEndDirectRequestPayActivity.this, "请输入正确的CVV2码");
                    return false;
                }
                if (StringUtils.isEmpty(etCardTerm.getText().toString().trim())) {
                    MsgUtil.showToastShort(FrontEndDirectRequestPayActivity.this, "请输入卡有效期");
                    return false;
                }
            }
            if (etIDCardNumber.getText().toString().trim().length() != 18) {
                MsgUtil.showToastShort(FrontEndDirectRequestPayActivity.this, "请输入正确的证件号");
                return false;
            }
            if (etHolderName.getText().toString().trim().equals("")) {
                MsgUtil.showToastShort(FrontEndDirectRequestPayActivity.this, "请输入正确的姓名");
                return false;
            }
            if (etPhoneNumber.getText().toString().trim().length() != 11) {
                MsgUtil.showToastShort(FrontEndDirectRequestPayActivity.this, "请输入正确的手机号");
                return false;
            }
        }

        if (etAmount.getText().toString().trim() == null || "".equals(etAmount.getText().toString().trim())) {
            MsgUtil.showToastShort(FrontEndDirectRequestPayActivity.this, "请输入订单金额");
            return false;
        }
        return true;
    }

    private void do_request_pay() {
        dialog = ProgressDialog.show(this, "提示", "处理中，请稍后...");
        dialog.setCancelable(true);
        YQPayApi.doPay(FrontEndDirectRequestPayActivity.this, getRequestPayParam(), new IYQPayCallback() {
            @Override
            public void payResult(String status, String message) {
                dialog.dismiss();
                MsgUtil.showDebugLog("WJDemo", "bind card result : " + message);
                if (status.equals(Constants$CallBackConstants.CALLBACK_FAILD)) {
                    Toast.makeText(FrontEndDirectRequestPayActivity.this, message, Toast.LENGTH_LONG).show();
                } else {
                    WebViewActivity.startActionForResult(FrontEndDirectRequestPayActivity.this, message, REQUEST_CODE);
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

        if (!(StringUtils.isEmpty(etBankCardNumber.getText().toString().trim()) && StringUtils.isEmpty(etIDCardNumber.getText().toString().trim())
                && StringUtils.isEmpty(etIDCardNumber.getText().toString().trim()) && StringUtils.isEmpty(etHolderName.getText().toString().trim())
                && StringUtils.isEmpty(etPhoneNumber.getText().toString().trim()))) {
            reqMap.put("BkAcctTp", cardType);//卡类型 00-贷记卡  01-借记卡
            if (cardType.equals("00")) {
                reqMap.put("CardCvn2", RSA.encrypt(etCVV2.getText().toString().trim(), EntryActivity.PUBLIC_KEY, reqMap.get("InputCharset")));//cvv2
                reqMap.put("CardExprDt", RSA.encrypt(getCurrentCardExprDt(etCardTerm.getText().toString().trim()), EntryActivity.PUBLIC_KEY, reqMap.get("InputCharset")));//卡有效期
            }
            MsgUtil.showDebugLog("WJDemo", "bank card number is : " + etBankCardNumber.getText().toString().trim());
            reqMap.put("BkAcctNo", RSA.encrypt(etBankCardNumber.getText().toString().trim(), EntryActivity.PUBLIC_KEY, reqMap.get("InputCharset")));//卡号
            reqMap.put("IDTp", "01");//证件类型   01：身份证
            reqMap.put("IDNo", RSA.encrypt(etIDCardNumber.getText().toString().trim(), EntryActivity.PUBLIC_KEY, reqMap.get("InputCharset")));//证件号
            reqMap.put("CstmrNm", RSA.encrypt(etHolderName.getText().toString().trim(), EntryActivity.PUBLIC_KEY, reqMap.get("InputCharset")));//持卡人姓名
            reqMap.put("MobNo", RSA.encrypt(etPhoneNumber.getText().toString().trim(), EntryActivity.PUBLIC_KEY, reqMap.get("InputCharset")));//预留手机号
        }

        if (!StringUtils.isEmpty(etSubMerchantNo.getText().toString().trim()))
            reqMap.put("SubMerchantNo", etSubMerchantNo.getText().toString().trim());
        if (!StringUtils.isEmpty(etEnsureAmount.getText().toString().trim()))
            reqMap.put("EnsureAmount", etEnsureAmount.getText().toString().trim());
        reqMap.put("TradeType", tradeType);
        reqMap.put("TrxAmt", etAmount.getText().toString().trim());
        reqMap.put("BankCode", "FrontEndDirectRequestPay");
        reqMap.put("ExpiredTime", etExpiredTime.getText().toString().trim());
        reqMap.put("AccessChannel", "wap");
        if (!StringUtils.isEmpty(etNotifyUrl.getText().toString().trim()))
            reqMap.put("ReturnUrl", etReturnUrl.getText().toString().trim());
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

    private String getCurrentCardExprDt(String str) {
        return str.substring(0, 2) + "/" + str.substring(2);
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
