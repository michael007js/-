package com.chanjet.auscashier.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
 * Created by Administrator on 2017/4/17.
 */

public class ConfirmTradeActivity extends Activity implements View.OnClickListener {
    private LinearLayout llOriInfo;
    private LinearLayout llResend, llResponse;
    private RadioGroup rgTradeType;
    private RadioButton rbAuth, rbPay;
    private EditText etMessage, etOriNo, etExtension, etResOrderNumber, etResReadNumber, etStatus;
    private Button btResend, btOK;
    private ImageView ivBack;
    private TextView tvTitle;

    private TimeCount mTime;
    private ProgressDialog dialog;
    private HashMap<String, String> reqMap;
    private String oriNo = "", oriType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_trade);
        initData();
        initView();
    }

    private void initData() {
        reqMap = (HashMap<String, String>) getIntent().getSerializableExtra("reqMap");
        oriNo = reqMap.get("oriNo");
        oriType = reqMap.get("oriType");
        mTime = new TimeCount(60000, 1000);
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.img_back);
        ivBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        llOriInfo = (LinearLayout) findViewById(R.id.ll_ori_info);
        llResend = (LinearLayout) findViewById(R.id.ll_resend_message);
        etOriNo = (EditText) findViewById(R.id.et_ori);
        rgTradeType = (RadioGroup) findViewById(R.id.rg_card_type);
        rbAuth = (RadioButton) findViewById(R.id.rb_auth);
        rbPay = (RadioButton) findViewById(R.id.rb_pay);
        rgTradeType.setOnCheckedChangeListener(listener);
        etMessage = (EditText) findViewById(R.id.et_message);
        btResend = (Button) findViewById(R.id.bt_resend);
        btOK = (Button) findViewById(R.id.bt_ok);
        etExtension = (EditText) findViewById(R.id.et_extension);

        llResponse = (LinearLayout) findViewById(R.id.ll_response_message);
        llResponse.setVisibility(View.INVISIBLE);
        etResOrderNumber = (EditText) findViewById(R.id.et_response_order_number);
        etResReadNumber = (EditText) findViewById(R.id.et_response_read_number);
        etStatus = (EditText) findViewById(R.id.et_trade_status);

        if (oriNo != null && !"".equals(oriNo))
            etOriNo.setText(oriNo);
        if (oriType.equals("confirm_receipt")) {
            llOriInfo.setVisibility(View.GONE);
        } else {
            if (oriType.equals("auth_order"))
                rbAuth.setChecked(true);
            else if (oriType.equals("pay_order"))
                rbPay.setChecked(true);
            mTime.start();
        }

        btResend.setOnClickListener(this);
        btOK.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == ivBack) {
            finish();
        } else if (view == btResend) {
            if (checkParam(true)) {
                mTime.start();
                do_request(getResendMessageParams());
            }
        } else if (view == btOK) {
            if (checkParam(false)) {
                do_request(getCheckParams());
            }
        }
    }

    private RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            oriType = (String) rgTradeType.findViewById(checkedId).getTag();
            MsgUtil.showDebugLog("WJDemo", "card type is : " + oriType);
        }
    };

    private boolean checkParam(boolean isResend) {
        oriNo = etOriNo.getText().toString().trim();
        if (oriNo == null || oriNo.equals("")) {
            MsgUtil.showToastShort(ConfirmTradeActivity.this, "请输入原订单号");
            return false;
        }
        if (llOriInfo.isShown()) {
            if (oriType == null || "".equals(oriType)) {
                MsgUtil.showToastShort(ConfirmTradeActivity.this, "请选择原订单类型");
                return false;
            }
            if (!isResend && (etMessage.getText().toString().trim().equals("") || etMessage.getText().toString().trim() == null || etMessage.getText().toString().trim().length() != 6)) {
                MsgUtil.showToastShort(ConfirmTradeActivity.this, "请输入正确的验证码");
                return false;
            }
        }
        return true;
    }

    // 发送验证码
    private class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            btResend.setText("重新获取");
            btResend.setClickable(true);
            btResend.setBackgroundResource(R.drawable.common_button_bg);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            btResend.setClickable(false);
            btResend.setBackgroundColor(getResources().getColor(R.color.colorLightestGray));
            btResend.setText(millisUntilFinished / 1000 + "秒");
        }
    }

    private void do_request(final Map<String, String> reqMap) {
        dialog = ProgressDialog.show(this, "提示", "处理中，请稍后...");
        dialog.setCancelable(true);
        YQPayApi.doPay(ConfirmTradeActivity.this, reqMap, new IYQPayCallback() {
            @Override
            public void payResult(String status, String message) {
                dialog.dismiss();
                MsgUtil.showDebugLog("WJDemo", reqMap.get("BankCode") + "response is : " + message);
                if (status.equals(Constants$CallBackConstants.CALLBACK_FAILD)) {
                    Toast.makeText(ConfirmTradeActivity.this, message, Toast.LENGTH_LONG).show();
                } else {
                    HashMap<String, String> respMap = new Gson().fromJson(message, HashMap.class);
                    Toast.makeText(ConfirmTradeActivity.this, respMap.get("RetMsg"), Toast.LENGTH_LONG).show();
                    if (!"ResendMessage".equals(reqMap.get("BankCode"))) {
                        llResponse.setVisibility(View.VISIBLE);
                        etResOrderNumber.setText(respMap.get("TrxId"));
                        etResReadNumber.setText(respMap.get("OrderTrxId"));
                        etStatus.setText(respMap.get("Status"));
                    }
                }
            }
        });
    }

    private Map<String, String> getCheckParams() {
        Map<String, String> checkMap = new HashMap<>();
        setBaseParam(checkMap);

        checkMap.put("TrxId", StringUtils.getOrderid());
        if ("auth_order".equals(oriType)) {
            checkMap.put("OriAuthTrxId", oriNo);
            checkMap.put("BankCode", "AuthConfirm");
        } else if ("pay_order".equals(oriType)) {
            checkMap.put("OriPayTrxId", oriNo);
            checkMap.put("BankCode", "PayConfirm");
        } else if ("confirm_receipt".equals(oriType)) {
            checkMap.put("OrderTrxId", oriNo);
            checkMap.put("BankCode", "ConfirmReceipt");
        }
        checkMap.put("SmsCode", etMessage.getText().toString().trim());
        if (!StringUtils.isEmpty(etExtension.getText().toString().trim()))
            checkMap.put("Extension", etExtension.getText().toString().trim());

        return checkMap;
    }

    private Map<String, String> getResendMessageParams() {
        Map<String, String> checkMap = new HashMap<>();
        setBaseParam(checkMap);

        checkMap.put("TrxId", StringUtils.getOrderid());
        checkMap.put("OriTrxId", oriNo);
        checkMap.put("TradeType", oriType);//auth_order：鉴权订单；pay _order：支付订单；
        checkMap.put("BankCode", "ResendMessage");
        if (!StringUtils.isEmpty(etExtension.getText().toString().trim()))
            checkMap.put("Extension", etExtension.getText().toString().trim());
        return checkMap;
    }

    private void setBaseParam(Map<String, String> checkMap) {
        checkMap.put("Version", reqMap.get("Version"));
        checkMap.put("TradeDate", reqMap.get("TradeDate"));
        checkMap.put("TradeTime", reqMap.get("TradeTime"));

        checkMap.put("PartnerId", reqMap.get("PartnerId"));
        checkMap.put("InputCharset", reqMap.get("InputCharset"));
        checkMap.put("MchId", reqMap.get("MchId"));
        checkMap.put("PAY_KEY", reqMap.get("PAY_KEY"));
    }
}
