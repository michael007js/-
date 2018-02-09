package com.chanjet.auscashier.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.chanjet.auscashier.EntryActivity;
import com.chanjet.auscashier.R;
import com.chanjet.yqpay.Constants;
import com.chanjet.yqpay.Constants$CallBackConstants;
import com.chanjet.yqpay.IYQPayCallback;
import com.chanjet.yqpay.YQPayApi;
import com.chanjet.yqpay.filter.IDCardNumInputFilter;
import com.chanjet.yqpay.util.MsgUtil;
import com.chanjet.yqpay.util.RSA;
import com.chanjet.yqpay.util.StringUtils;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/4/15.
 */

public class AuthenticationActivity extends Activity {

    private LinearLayout llCVV2, llCardTerm, llResponse;
    private RadioGroup rgCardType;
    private EditText etBankCardNumber, etIDCardNumber, etHolderName, etPhoneNumber, etCVV2, etCardTerm,
            etMerUserId, etOrderNumber, etExpiredTime, etSendFlag, etNotifyUrl, etExtension,
            etResOrderNumber, etResReadNumber;

    private ImageView ivBack;
    private Button btOK;

    private String cardType = null;
    private ProgressDialog dialog;
    private HashMap<String, String> reqMap;
    private String mTrxId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        initData();
        initView();
    }

    private void initData() {
        reqMap = (HashMap<String, String>) getIntent().getSerializableExtra("reqMap");
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.img_back);
        rgCardType = (RadioGroup) findViewById(R.id.rg_card_type);
        llCVV2 = (LinearLayout) findViewById(R.id.ll_cvv2);
        llCardTerm = (LinearLayout) findViewById(R.id.ll_credit_term);
        rgCardType.setOnCheckedChangeListener(mListener);

        etBankCardNumber = (EditText) findViewById(R.id.et_bank_card_number);
        etIDCardNumber = (EditText) findViewById(R.id.et_id_card_number);
        etHolderName = (EditText) findViewById(R.id.et_holder_name);
        etPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
        etCVV2 = (EditText) findViewById(R.id.et_cvv2);
        etCardTerm = (EditText) findViewById(R.id.et_card_term);
        etMerUserId = (EditText) findViewById(R.id.et_mer_user_id);
        btOK = (Button) findViewById(R.id.bt_ok);

        etOrderNumber = (EditText) findViewById(R.id.et_order_number);
        mTrxId = StringUtils.getOrderid();
        etOrderNumber.setText(mTrxId);
        etExpiredTime = (EditText) findViewById(R.id.et_order_expired_time);
        etNotifyUrl = (EditText) findViewById(R.id.et_notify_url);
        etSendFlag = (EditText) findViewById(R.id.et_send_flag);
        etExtension = (EditText) findViewById(R.id.et_extension);
        llResponse = (LinearLayout) findViewById(R.id.ll_response_message);
        llResponse.setVisibility(View.INVISIBLE);
        etResOrderNumber = (EditText) findViewById(R.id.et_response_order_number);
        etResReadNumber = (EditText) findViewById(R.id.et_response_read_number);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etIDCardNumber.setFilters(new InputFilter[]{new IDCardNumInputFilter()});
        btOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput())
                    do_bindCard();
            }
        });
    }

    private RadioGroup.OnCheckedChangeListener mListener = new RadioGroup.OnCheckedChangeListener() {
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

    private boolean checkInput() {
        if (StringUtils.isEmpty(etExpiredTime.getText().toString().trim())) {
            MsgUtil.showToastShort(AuthenticationActivity.this, "请输入订单有效期");
            return false;
        }
        if (StringUtils.isEmpty(etSendFlag.getText().toString().trim())) {
            MsgUtil.showToastShort(AuthenticationActivity.this, "短信发送标识不能为空");
            return false;
        }
        if (StringUtils.isEmpty(etNotifyUrl.getText().toString().trim())) {
            MsgUtil.showToastShort(AuthenticationActivity.this, "异步通知地址不能为空");
            return false;
        }
        if (etBankCardNumber.getText().toString().trim().length() < 16) {
            MsgUtil.showToastShort(AuthenticationActivity.this, "卡号错误");
            return false;
        }
        if (cardType == null || cardType.equals("")) {
            MsgUtil.showToastShort(AuthenticationActivity.this, "请选择卡类型");
            return false;
        }
        if (cardType.equals("00")) {
            if (StringUtils.isEmpty(etCVV2.getText().toString().trim()) || etCVV2.getText().toString().trim().length() != 3) {
                MsgUtil.showToastShort(AuthenticationActivity.this, "请输入正确的CVV2码");
                return false;
            }
            if (StringUtils.isEmpty(etCardTerm.getText().toString().trim()) || etCardTerm.getText().toString().trim().length() != 4) {
                MsgUtil.showToastShort(AuthenticationActivity.this, "请输入正确的卡有效期");
                return false;
            }
        }
        if (etIDCardNumber.getText().toString().trim().length() != 18) {
            MsgUtil.showToastShort(AuthenticationActivity.this, "请输入正确的证件号");
            return false;
        }
        if (etHolderName.getText().toString().trim().equals("")) {
            MsgUtil.showToastShort(AuthenticationActivity.this, "请输入正确的姓名");
            return false;
        }
        if (etPhoneNumber.getText().toString().trim().length() != 11) {
            MsgUtil.showToastShort(AuthenticationActivity.this, "请输入正确的手机号");
            return false;
        }
        if (etMerUserId.getText().toString().trim() == null || "".equals(etMerUserId.getText().toString().trim())) {
            MsgUtil.showToastShort(AuthenticationActivity.this, "请输入正确的用户标识");
            return false;
        }
        return true;
    }

    private void do_bindCard() {
//        getBindCardPara();
        dialog = ProgressDialog.show(this, "提示", "处理中，请稍后...");
        dialog.setCancelable(true);
        YQPayApi.doPay(AuthenticationActivity.this, getBindCardPara(), new IYQPayCallback() {
            @Override
            public void payResult(String status, String message) {
                dialog.dismiss();
                MsgUtil.showDebugLog("WJDemo", "bind card result : " + message);
                if (status.equals(Constants$CallBackConstants.CALLBACK_FAILD)) {
                    Toast.makeText(AuthenticationActivity.this, message, Toast.LENGTH_LONG).show();
                } else {
                    Gson gson = new Gson();
                    HashMap<String, String> resMap = gson.fromJson(message, HashMap.class);
                    Toast.makeText(AuthenticationActivity.this, resMap.get("RetMsg"), Toast.LENGTH_LONG).show();
                    String TrxId = resMap.get("TrxId");

                    llResponse.setVisibility(View.VISIBLE);
                    etResOrderNumber.setText(TrxId);
                    etResReadNumber.setText(resMap.get("OrderTrxId"));

                    Intent intent = new Intent();
                    intent.putExtra("TrxId", TrxId);
                    intent.putExtra("TradeType", "auth_order");
                    setResult(1, intent);
                    finish();
                }
            }
        });
    }

    private HashMap<String, String> getBindCardPara() {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("Version", reqMap.get("Version"));
        maps.put("TradeDate", reqMap.get("TradeDate"));
        maps.put("TradeTime", reqMap.get("TradeTime"));

        maps.put("PartnerId", reqMap.get("PartnerId"));
        maps.put("InputCharset", reqMap.get("InputCharset"));
        maps.put("MchId", reqMap.get("MchId"));
        maps.put("PAY_KEY", reqMap.get("PAY_KEY"));

        maps.put("TrxId", mTrxId);// 订单号
        maps.put("ExpiredTime", etExpiredTime.getText().toString().trim());// 订单有效期
        maps.put("BankCode", "BindCard");
        maps.put("NotifyUrl", etNotifyUrl.getText().toString().trim());//卡类型 00-贷记卡  01-借记卡

        maps.put("BkAcctTp", cardType);//卡类型 00-贷记卡  01-借记卡
        if (cardType.equals("00")) {
            maps.put("CardCvn2", RSA.encrypt(etCVV2.getText().toString().trim(), EntryActivity.PUBLIC_KEY, maps.get("InputCharset")));//cvv2
            maps.put("CardExprDt", RSA.encrypt(getCurrentCardExprDt(etCardTerm.getText().toString().trim()), EntryActivity.PUBLIC_KEY, maps.get("InputCharset")));//卡有效期
        }
        MsgUtil.showDebugLog("WJDemo", "bank card number is : " + etBankCardNumber.getText().toString().trim());
        maps.put("BkAcctNo", RSA.encrypt(etBankCardNumber.getText().toString().trim(), EntryActivity.PUBLIC_KEY, maps.get("InputCharset")));//卡号
        maps.put("IDTp", "01");//证件类型   01：身份证
        maps.put("IDNo", RSA.encrypt(etIDCardNumber.getText().toString().trim(), EntryActivity.PUBLIC_KEY, maps.get("InputCharset")));//证件号
        maps.put("CstmrNm", RSA.encrypt(etHolderName.getText().toString().trim(), EntryActivity.PUBLIC_KEY, maps.get("InputCharset")));//持卡人姓名
        maps.put("MobNo", RSA.encrypt(etPhoneNumber.getText().toString().trim(), EntryActivity.PUBLIC_KEY, maps.get("InputCharset")));//预留手机号
        maps.put("MerUserId", etMerUserId.getText().toString().trim());
        maps.put("SmsFlag", etSendFlag.getText().toString().trim());//短信发送标识
        if (!StringUtils.isEmpty(etExtension.getText().toString().trim()))
            maps.put("Extension", etExtension.getText().toString().trim());//扩展字段
        return maps;
    }

    private String getCurrentCardExprDt(String str){
        return str.substring(0, 2) + "/" + str.substring(2);
    }
}
