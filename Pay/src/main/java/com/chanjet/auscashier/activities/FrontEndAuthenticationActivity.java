package com.chanjet.auscashier.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chanjet.auscashier.R;
import com.chanjet.auscashier.WebViewActivity;
import com.chanjet.yqpay.Constants$CallBackConstants;
import com.chanjet.yqpay.IYQPayCallback;
import com.chanjet.yqpay.YQPayApi;
import com.chanjet.yqpay.util.MsgUtil;
import com.chanjet.yqpay.util.StringUtils;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/4/15.
 */

public class FrontEndAuthenticationActivity extends Activity {

    private LinearLayout llResponse;
    private EditText etMerUserId, etOrderNumber, etExpiredTime, etNotifyUrl, etReturnUrl, etExtension,
            etSellerId;

    private ImageView ivBack;
    private Button btGenerateOrder, btOK;

    private ProgressDialog dialog;
    private HashMap<String, String> reqMap;
    private String mTrxId;

    private static final int REQUEST_CODE = 8888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.front_end_activity_authentication);
        initData();
        initView();
    }

    private void initData() {
        reqMap = (HashMap<String, String>) getIntent().getSerializableExtra("reqMap");
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.img_back);
        btOK = (Button) findViewById(R.id.bt_ok);

        etOrderNumber = (EditText) findViewById(R.id.et_order_number);
        btGenerateOrder = (Button) findViewById(R.id.bt_generate_order);
        etSellerId = (EditText) findViewById(R.id.et_seller_id);
        etExpiredTime = (EditText) findViewById(R.id.et_order_expired_time);
        etNotifyUrl = (EditText) findViewById(R.id.et_notify_url);
        etReturnUrl = (EditText) findViewById(R.id.et_return_url);
        etMerUserId = (EditText) findViewById(R.id.et_mer_user_id);
        etExtension = (EditText) findViewById(R.id.et_extension);

        mTrxId = StringUtils.getOrderid();
        etOrderNumber.setText(mTrxId);
        etSellerId.setText(reqMap.get("MchId"));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btGenerateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTrxId = StringUtils.getOrderid();
                etOrderNumber.setText(mTrxId);
            }
        });
        btOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput())
                    do_bindCard();
            }
        });
    }

    private boolean checkInput() {
        if (StringUtils.isEmpty(etExpiredTime.getText().toString().trim())) {
            MsgUtil.showToastShort(FrontEndAuthenticationActivity.this, "请输入订单有效期");
            return false;
        }
        if (StringUtils.isEmpty(etNotifyUrl.getText().toString().trim())) {
            MsgUtil.showToastShort(FrontEndAuthenticationActivity.this, "异步通知地址不能为空");
            return false;
        }
        if (etMerUserId.getText().toString().trim() == null || "".equals(etMerUserId.getText().toString().trim())) {
            MsgUtil.showToastShort(FrontEndAuthenticationActivity.this, "请输入正确的用户标识");
            return false;
        }
        return true;
    }

    private void do_bindCard() {
//        getBindCardPara();
        dialog = ProgressDialog.show(this, "提示", "处理中，请稍后...");
        dialog.setCancelable(true);
        YQPayApi.doPay(FrontEndAuthenticationActivity.this, getBindCardPara(), new IYQPayCallback() {
            @Override
            public void payResult(String status, String message) {
                dialog.dismiss();
                MsgUtil.showDebugLog("WJDemo", "bind card result : " + message);
                if (status.equals(Constants$CallBackConstants.CALLBACK_FAILD)) {
                    Toast.makeText(FrontEndAuthenticationActivity.this, message, Toast.LENGTH_LONG).show();
                } else {
                    WebViewActivity.startActionForResult(FrontEndAuthenticationActivity.this, message, REQUEST_CODE);
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
        if (!StringUtils.isEmpty(etExtension.getText().toString().trim()))
            maps.put("MerchantNo", etSellerId.getText().toString().trim());
        maps.put("ExpiredTime", etExpiredTime.getText().toString().trim());// 订单有效期
        maps.put("BankCode", "FrontEndBindCard");
        maps.put("NotifyUrl", etNotifyUrl.getText().toString().trim());
        maps.put("ReturnUrl", etReturnUrl.getText().toString().trim());
        maps.put("MerUserId", etMerUserId.getText().toString().trim());
        if (!StringUtils.isEmpty(etExtension.getText().toString().trim()))
            maps.put("Extension", etExtension.getText().toString().trim());//扩展字段
        return maps;
    }
}
