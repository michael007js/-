package com.chanjet.auscashier;

import java.io.Serializable;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.chanjet.yqpay.IYQPayCallback;
import com.chanjet.yqpay.YQPay;
import com.chanjet.yqpay.util.MD5;
import com.chanjet.yqpay.util.MsgUtil;
import com.chanjet.yqpay.util.StringUtils;
import com.google.gson.Gson;

public class EntryActivity extends Activity {

    public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDv0rdsn5FYPn0EjsCPqDyIsYRawNWGJDRHJBcdCldodjM5bpve+XYb4Rgm36F6iDjxDbEQbp/HhVPj0XgGlCRKpbluyJJt8ga5qkqIhWoOd/Cma1fCtviMUep21hIlg1ZFcWKgHQoGoNX7xMT8/0bEsldaKdwxOlv3qGxWfqNV5QIDAQAB";

    public static final String PAY_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMpdQYluaQtZBwFr4tdF4eBUgeGM5//PCsQZJo2yiF/f0oiG7TpFqLEs4WoW9EG/jzcHNtqoGglrySN5vVnOILb49lLgfYuHwc85ya1gBrR6y41JOMFiFFh/19BaRojox1MF4CdHomv3jigp5eb6SZj3IFNUkTUAR2WLNfVsjDehAgMBAAECgYACJvXAeW0iksLRUA1sjrC1SxDKjk4VWWVa6o2PajurEV1S2GSDfyQpJjoLf8z/OgNS6X+62ZjVStEr0GqkHt4YLzwj0UUJVeZUIkZSuBOmdV2eEaQ5846eXIM6i9CVb2Gj/xR42kf6t6Axlx/VQ7PrqfoPwEJhcvkSH0zu5SaIMQJBAObJDrrLllaBwpKmMthMTYEZudKJrNOBOIg05zm5w6Y4KO0iSttNQ0ySmmOQHz/sbWjWR/yPndktvltiLXBBT60CQQDgeUaRXUJYrGMcgMWtbFctFk2s7yNT92yuslCvvqFHTmIG8JJSUR6HY2rD+dC2n4O+IzYy3Jn2D1WDLm34GHZFAkB5L+7qK+n/9xejRl7AsiCowEeRxPXaAqsa6xzYdTHF4QusqitAyWujlAA6c/3U5WP2fz9B4nBzENA60G0n20PpAkEAhoFk/ZmqNcQmJ2AqEz7GBehFZwhsXNX3s755hrVtonKfXpUsuFKPPoUd5ox8udFfecFTqqpxPvX1QYPCtVTDGQJABCmk7p4NuE6j9AbvU6B9r4GNpiL5lGeYxZaYHWXkuOTyKgQojMIK1P8EMXbUwYhRLKc/5+JYcCXGQX5Ar2LxMA=="; // 商户支付密钥    畅捷前台  C环境 对应200000400059商户
    public static final String MERID = "200001340029"; // 商户号     正式

    /**
     * 调用收银台页面结果callback---失败
     */
    public static final String CALLBACK_FAILD = "failed";
    /**
     * 调用收银台页面结果callback---成功
     */
    public static final String CALLBACK_SUCCESS = "success";
    /**
     * 调用收银台页面结果callback---处理中
     */
    public static final String CALLBACK_PROCESS = "process";
    /**
     * 调用收银台页面结果---调用收银台时没有传递参数
     */
    public static final String CALLBACK_PARAM_FREE = "no_param";
    /**
     * 调用收银台页面结果--订单生成失败（服务器返回解析错误或为空）
     */
    public static final String CALLBACK_SERVER_NO_PARAM = "server_no_param";
    /**
     * 调用收银台页面结果callback---网络连接异常
     */
    public static final String CALLBACK_HTTPSTATUSEXCEPTION = "HttpStatusException";
    /**
     * 调用收银台页面结果callback---读取数据异常
     */
    public static final String CALLBACK_IOEXCEPTION = "IOException";
    /**
     * 调用收银台页面结果callback---无访问网络
     */
    public static final String CALLBACK_NONETWORK = "noNetwork";
    /**
     * 调用收银台页面结果callback---解析出错
     */
    public static final String CALLBACK_JSONERROR = "jsonError";
    /**
     * 调用收银台页面结果callback---取消支付
     */
    public static final String CALLBACK_CANCEL_PAY = "cancel_pay";
    /**
     * 调用收银台页面结果callback---支付失败
     */
    public static final String CALLBACK_FAILED_PAY = "failed_pay";
    /**
     * 调用收银台页面结果--订单已存在
     */
    public static final String CALLBACK_SERVER_ORDER_EXIITS = "server_order_exitis";
    /**
     * 调用收银台页面结果--订单不存在
     */
    public static final String CALLBACK_SERVER_ORDER_NOEXIITS = "server_order_no_exitis";

    private ProgressDialog dialog;
    private Map<String, String> tradeInfo;
    private Spinner spPayType;
    private ArrayAdapter adapterPayType;
    private String paymethod = "auth_order"; //auth_order：鉴权订单； pay_order：支付订单； refund_order：退款订单；

    private EditText etPartnerId;
    private EditText mEditQueryOrderId;
    private EditText etOriOrderNo, etRefundAmount, etRefundEnsureAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        etPartnerId = (EditText) findViewById(R.id.et_partner_id);
        mEditQueryOrderId = (EditText) findViewById(R.id.edt_query_orderid);
        spPayType = (Spinner) findViewById(R.id.sp_paytype);
        adapterPayType = ArrayAdapter.createFromResource(this, R.array.order_type, android.R.layout.simple_spinner_item);
        // 设置下拉框样式
        adapterPayType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPayType.setAdapter(adapterPayType);
        spPayType.setOnItemSelectedListener(new SpinnerSelectedListener());
        etOriOrderNo = (EditText) findViewById(R.id.et_ori_order_no);
        etRefundAmount = (EditText) findViewById(R.id.et_refund_amount);
        etRefundEnsureAmount = (EditText) findViewById(R.id.et_refund_ensure_amount);

        etPartnerId.setText(MERID);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void toPay(View v) {
        if (TextUtils.isEmpty(etPartnerId.getText().toString().trim())) {
            Toast.makeText(this, "请输入合作者ID！", Toast.LENGTH_SHORT).show();
            return;
        }
        waitDilog("正在创建订单...", "提示", "创建成功！");
    }

    public void toQuery(View v) {
        if (TextUtils.isEmpty(etPartnerId.getText().toString().trim())) {
            Toast.makeText(this, "请输入合作者ID！", Toast.LENGTH_SHORT).show();
            return;
        }
        final String orderid = mEditQueryOrderId.getText().toString().trim();
        if (TextUtils.isEmpty(orderid)) {
            Toast.makeText(this, "请输入原订单号！", Toast.LENGTH_SHORT).show();
            return;
        }
        final HashMap<String, String> queryMap = (HashMap<String, String>) getBaseParam();
        queryMap.put("TrxId", StringUtils.getOrderid());
        queryMap.put("OrderTrxId", orderid);
        queryMap.put("TradeType", paymethod);
        dialog = ProgressDialog.show(EntryActivity.this, "提示", "正在获取支付结果...");
        dialog.setCancelable(true);
        final YQPay pay = YQPay.getInstance(EntryActivity.this);
        /**
         *@param orderid 订单号
         *@param MERID 商户号
         *@param PAY_KEY 商户密钥
         */
        pay.queryState(queryMap, new IYQPayCallback() {
            @Override
            public void payResult(final String status, final String message) {
                dialog.dismiss();
                Log.i("RESULT", "CALLBACK:   " + "status:" + status + " message:" + message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("RESULT", "CALLBACK: " + "status:" + status + " message:" + message);
                        if (CALLBACK_FAILD.equals(status)) {
                            Toast.makeText(EntryActivity.this, message, Toast.LENGTH_LONG).show();
                        } else {
                            Map<String, String> respMap = new Gson().fromJson(message, HashMap.class);
                            if (CALLBACK_SUCCESS.equals(status)) {
                                Toast.makeText(EntryActivity.this, "支付成功", Toast.LENGTH_LONG).show();
                            } else if (EntryActivity.CALLBACK_PROCESS.equals(status)) {
                                Toast.makeText(EntryActivity.this, "处理中", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        });
    }

    private boolean checkRefundParam() {
        if (TextUtils.isEmpty(etPartnerId.getText().toString().trim())) {
            Toast.makeText(this, "请输入合作者ID！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(etOriOrderNo.getText().toString().trim())) {
            MsgUtil.showToastShort(EntryActivity.this, "请输入原始订单号");
            return false;
        }
        if (TextUtils.isEmpty(etRefundAmount.getText().toString().trim())) {
            MsgUtil.showToastShort(EntryActivity.this, "请输入退款金额");
            return false;
        }
        return true;
    }

    /**
     * 退款
     *
     * @param v
     */
    public void toRefund(View v) {
        if (checkRefundParam()) {
            final Map<String, String> reqMap = getBaseParam();
            reqMap.put("TrxId", StringUtils.getOrderid());
            reqMap.put("OriTrxId", etOriOrderNo.getText().toString().trim());
            reqMap.put("TrxAmt", etRefundAmount.getText().toString().trim());
            String ensureAmount = etRefundEnsureAmount.getText().toString().trim();
            if (ensureAmount != null && !"".equals(ensureAmount))
                reqMap.put("RefundEnsureAmount", ensureAmount);
            reqMap.put("NotifyUrl", "http://123.103.9.203:9708/online/ChanNotify.do");
            dialog = ProgressDialog.show(this, "提示", "正在退款...");
            dialog.setCancelable(true);
            final YQPay pay = YQPay.getInstance(EntryActivity.this);
            pay.refundOrder(reqMap, new IYQPayCallback() {
                @Override
                public void payResult(final String status, final String message) {
                    Log.i("RESULT", "CALLBACK:   " + "status:" + status + " message:" + message);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (CALLBACK_FAILD.equals(status)) {
                                Toast.makeText(EntryActivity.this, message, Toast.LENGTH_LONG).show();
                            } else {
                                Map<String, String> respMap = new Gson().fromJson(message, HashMap.class);
                                if (CALLBACK_SUCCESS.equals(status)) {
                                    Toast.makeText(EntryActivity.this, respMap.get("RetMsg"), Toast.LENGTH_LONG).show();
                                } else if (CALLBACK_PROCESS.equals(status)) {
                                    Toast.makeText(EntryActivity.this, respMap.get("RetMsg"), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
            });
        }
    }

    private void waitDilog(String message, String title, final String toastStr) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(message);
        dialog.setTitle(title);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
        final Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(500);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Intent it = new Intent(EntryActivity.this, MainActivity.class);
                            tradeInfo = getBaseParam();
                            it.putExtra("reqMap", (Serializable) tradeInfo);
                            startActivityForResult(it, 1);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                toast.setText(toastStr);
                toast.show();
            }
        }.start();
    }


    class SpinnerSelectedListener implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int position,
                                   long index) {
            Log.e("xxxxx", "position : " + position);
            if (position == 0) {
                paymethod = "auth_order";
            } else if (position == 1) {
                paymethod = "pay_order";
            } else if (position == 1) {
                paymethod = "refund_order";
            }
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    private Map<String, String> getBaseParam() {
        Map<String, String> reqMaps = new HashMap<String, String>();
        // 基本参数
        reqMaps.put("Version", "1.0");//接口版本，目前只有固定值1.0
        reqMaps.put("TradeDate", StringUtils.getCurrentTime("yyyyMMdd"));// 交易日期yyyyMMdd
        reqMaps.put("TradeTime", StringUtils.getCurrentTime("HHmmss"));// 交易时间HHmmss
        reqMaps.put("PartnerId", etPartnerId.getText().toString().trim()); // 签约合作方的唯一用户号
        reqMaps.put("InputCharset", "utf-8");// 商户网站使用的编码格式，如utf-8、gbk、gb2312等
        reqMaps.put("MchId", etPartnerId.getText().toString().trim());// 商户标示ID
        reqMaps.put("PAY_KEY", PAY_KEY);// 支付密钥
        reqMaps.put("AppId", "2017110709782800");// 微信/支付宝给商户开通的appid

        return reqMaps;
    }

    // 生成用户唯一标识(该字段用商户自己定义格式)
    private String genUserId() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    public static String getKeyValue(String key, String info) {
        String dinfo = URLDecoder.decode(info);
        String[] str = dinfo.split("&");
        for (int i = 0; i < str.length; i++) {
            String[] param = str[i].split("=");
            if (param.length == 2) {
                if (key.equals(param[0])) {
                    return param[1];
                }
            }
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (resultCode == 2) {
            outtradeno = data.getStringExtra("outtradeno");
            if (outtradeno != null && !"".equals(outtradeno)) {
                mEditQueryOrderId.setText(orderId);
                mEditRefundOrderId.setText(outtradeno);
            }
        }*/



    }
}
