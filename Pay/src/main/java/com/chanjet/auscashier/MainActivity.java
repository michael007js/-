package com.chanjet.auscashier;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.chanjet.auscashier.activities.FrontEndQuickPayActivity;
import com.chanjet.auscashier.activities.QuickPayActivity;
import com.chanjet.yqpay.IYQPayCallback;
import com.chanjet.yqpay.YQPay;
import com.chanjet.yqpay.YQPayApi;
import com.chanjet.yqpay.thread.ThreadPool;
import com.chanjet.yqpay.util.DeviceUtils;
import com.chanjet.yqpay.util.LOG;
import com.chanjet.yqpay.util.MD5;
import com.chanjet.yqpay.util.MsgUtil;
import com.chanjet.yqpay.util.StringUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends Activity {

    /**
     * 调用收银台页面结果callback---失败
     */
    public static final String CALLBACK_FAILD = "failed";
    /**
     * 调用收银台页面结果callback---成功
     */
    public static final String CALLBACK_SUCCESS = "success";
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
    public static final String CALLBACK_DATAERROR = "dataError";
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
    /**
     * 调用收银台页面结果callback---正在处理中
     */
    public static final String CALLBACK_PROCESSED_PAY = "processed_pay";

    private static final int REQUEST_CODE = 8888;

    private String outtradeno = "", tradeType = "11";

    private EditText etAmount, etSubject, etName, etEnsureAmount, etExtension, etSubMchId, etSplitList, etNodifyUrl;
    private RadioGroup rgTradeType;
    private RadioButton rbRight;

    private EditText etBuyerPayCode, etTradeMemo;
    private RadioGroup rgLimitCreditPay;
    private RadioButton rbUseCredit, rbNoCredit;
    private String limitCredit = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ThreadPool.startup();
        initData();
        initView();

    }

    private void initData() {
        Intent intent = getIntent();
        reqMap = (HashMap<String, String>) intent.getSerializableExtra("reqMap");
    }

    private void initView(){
        etSubMchId = (EditText) findViewById(R.id.et_sub_mch_id);
        etAmount = (EditText) findViewById(R.id.et_amount);
        etSubject = (EditText) findViewById(R.id.et_subject);
        etName = (EditText) findViewById(R.id.et_name);
        rgTradeType = (RadioGroup) findViewById(R.id.rg_trade_type);
        etEnsureAmount = (EditText) findViewById(R.id.et_ensure_amount);
        etSplitList = (EditText) findViewById(R.id.et_split_list);
        etNodifyUrl = (EditText) findViewById(R.id.et_notify_url);
        etExtension = (EditText) findViewById(R.id.et_extension);
        rbRight = (RadioButton) findViewById(R.id.radioButton1);
        rbRight.setChecked(true);

        rgTradeType.setOnCheckedChangeListener(listener);
        ((ImageView) findViewById(R.id.img_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        etBuyerPayCode = (EditText) findViewById(R.id.et_buyer_pay_code);
        etTradeMemo = (EditText) findViewById(R.id.et_trade_memo);
        rgLimitCreditPay = (RadioGroup) findViewById(R.id.rg_limit_credit_pay);
        rbUseCredit = (RadioButton) findViewById(R.id.rb_use_credit);
//        rbUseCredit.setSelected(true);
        rgLimitCreditPay.setOnCheckedChangeListener(limitCreditListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            tradeType = (String) rgTradeType.findViewById(checkedId).getTag();
            MsgUtil.showDebugLog("WJDemo", "trade type tag is : " + tradeType + ", type is : " + ((RadioButton) rgTradeType.findViewById(checkedId)).getText());
        }
    };

    private RadioGroup.OnCheckedChangeListener limitCreditListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            limitCredit = (String) rgLimitCreditPay.findViewById(checkedId).getTag();
            MsgUtil.showDebugLog("WJDemo", "limit credit tag is : " + limitCredit + ", type is : " + ((RadioButton) rgLimitCreditPay.findViewById(checkedId)).getText());
        }
    };


    // 支付宝支付
    public void do_alipay(View v) {
        if (checkAliWXPayParam(v)) {
            HashMap<String, String> aliMap = (HashMap<String, String>) reqMap.clone();
            if (!StringUtils.isEmpty(etSubMchId.getText().toString().trim()))
                aliMap.put("SubMchId", etSubMchId.getText().toString().trim());//子商户号
            aliMap.put("TradeAmount", etAmount.getText().toString().trim());// 金额
            aliMap.put("GoodsName", etName.getText().toString().trim());// 商品名称
            aliMap.put("Subject", etSubject.getText().toString().trim());// 订单标题
            outtradeno = StringUtils.getOrderid();
            aliMap.put("OutTradeNo", outtradeno);// 订单号
            aliMap.put("TradeType", tradeType);
            aliMap.put("DeviceInfo", DeviceUtils.getIMEI(MainActivity.this));
            if (tradeType.equals("12") && !StringUtils.isEmpty(etEnsureAmount.getText().toString().trim()))
                aliMap.put("EnsureAmount", etEnsureAmount.getText().toString().trim());
            if (!StringUtils.isEmpty(etSplitList.getText().toString().trim()))
                aliMap.put("SplitList", etSplitList.getText().toString().trim());
            if (!StringUtils.isEmpty(etExtension.getText().toString().trim()))
                aliMap.put("RoyaltyParameters", etExtension.getText().toString().trim());//扩展字段
            aliMap.put("BankCode", "ALIPAY");
            if (!StringUtils.isEmpty(etNodifyUrl.getText().toString().trim()))
                aliMap.put("NotifyUrl", etNodifyUrl.getText().toString().trim());// 服务器异步通知页面路

            dialog = ProgressDialog.show(this, "提示", "正在获取预支付订单...");
            dialog.setCancelable(true);
            YQPayApi.doPay(this, aliMap, new IYQPayCallback() {
                @Override
                public void payResult(final String status, final String message) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();
                            Log.i("RESULT", "CALLBACK: " + "status:" + status + " message:" + message);
                            if (status == null || "".equals(status)) {
                                Toast.makeText(MainActivity.this, "获取预付订单失败！", Toast.LENGTH_LONG).show();
                            } else {
                                if (CALLBACK_FAILD.equals(status)) {
                                    String msg = "";
                                    if (CALLBACK_PARAM_FREE.equals(message)) {
                                        msg = "调用收银台时没有传递参数";
                                    } else if (CALLBACK_SERVER_NO_PARAM.equals(message)) {
                                        msg = "订单生成失败";
                                    } else if (CALLBACK_HTTPSTATUSEXCEPTION.equals(message)) {
                                        msg = "网络连接异常";
                                    } else if (CALLBACK_IOEXCEPTION.equals(message)) {
                                        msg = "网络读取异常";
                                    } else if (CALLBACK_NONETWORK.equals(message)) {
                                        msg = "无访问网络";
                                    } else if (CALLBACK_DATAERROR.equals(message)) {
                                        msg = "数据解析错误";
                                    } else if (CALLBACK_CANCEL_PAY.equals(message)) {
                                        msg = "取消支付";
                                    } else if (CALLBACK_FAILED_PAY.equals(message)) {
                                        msg = "支付失败";
                                    } else if (CALLBACK_PROCESSED_PAY.equals(message)) {
                                        msg = "正在处理中";
                                    } else {
                                        msg = message;
                                    }
//                                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                                    showFildDialog(msg);
                                } else if (CALLBACK_SUCCESS.equals(status)) {
                                    Toast.makeText(MainActivity.this, "下单成功！", Toast.LENGTH_SHORT).show();
                                    HashMap<String, String> respMap = new Gson().fromJson(message, HashMap.class);
                                    outtradeno = respMap.get("OuterTradeNo");
                                    MsgUtil.showDebugLog("WJDemo", "outtradeno" + outtradeno);
                                    String payContent = respMap.get("PayInfo");
                                    WebViewActivity.startActionForResult(MainActivity.this, payContent, REQUEST_CODE);
                                } else {
                                    HashMap<String, String> respMap = new Gson().fromJson(message, HashMap.class);
                                    Toast.makeText(MainActivity.this, respMap.get("RetMap"), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
            });
        }
    }

    private boolean checkAliWXPayParam(View v) {
        if (etAmount.getText().toString().trim() == null || "".equals(etAmount.getText().toString().trim())) {
            MsgUtil.showToastShort(MainActivity.this, "请输入金额");
            return false;
        }
        if (etSubject.getText().toString().trim() == null || "".equals(etSubject.getText().toString().trim())) {
            MsgUtil.showToastShort(MainActivity.this, "请输入订单标题");
            return false;
        }
        if (etName.getText().toString().trim() == null || "".equals(etName.getText().toString().trim())) {
            MsgUtil.showToastShort(MainActivity.this, "请输入商品名称");
            return false;
        }
        if (StringUtils.isEmpty(tradeType)) {
            MsgUtil.showToastShort(MainActivity.this, "请选择交易类型");
            return false;
        }
        if (tradeType.equals("12") && StringUtils.isEmpty(etEnsureAmount.getText().toString().trim())) {
            MsgUtil.showToastShort(MainActivity.this, "请输入担保金额");
            return false;
        }
        if (v.getId() == R.id.ll_wx_public_number_pay) {
            if (StringUtils.isEmpty(etBuyerPayCode.getText().toString().trim())) {
                MsgUtil.showToastShort(MainActivity.this, "请输入付方支付ID");
                return false;
            }
        }
        return true;
    }

    // 微信支付
    public void do_wxpay(View v) {
        if (checkAliWXPayParam(v)) {
            HashMap<String, String> wxMap = (HashMap<String, String>) reqMap.clone();
            if (!StringUtils.isEmpty(etSubMchId.getText().toString().trim()))
                wxMap.put("SubMchId", etSubMchId.getText().toString().trim());//子商户号
            wxMap.put("TradeAmount", etAmount.getText().toString().trim());// 金额
            wxMap.put("GoodsName", etName.getText().toString().trim());// 商品名称
            wxMap.put("Subject", etSubject.getText().toString().trim());// 订单标题
            outtradeno = StringUtils.getOrderid();
            wxMap.put("OutTradeNo", outtradeno);// 订单号
            wxMap.put("AppId", "wxfa869f12af50587a");// 微信appid
            wxMap.put("TradeType", tradeType);
            wxMap.put("DeviceInfo", DeviceUtils.getIMEI(MainActivity.this));
            if (tradeType.equals("12") && !StringUtils.isEmpty(etEnsureAmount.getText().toString().trim()))
                wxMap.put("EnsureAmount", etEnsureAmount.getText().toString().trim());
            if (!StringUtils.isEmpty(etSplitList.getText().toString().trim()))
                wxMap.put("SplitList", etSplitList.getText().toString().trim());
            if (!StringUtils.isEmpty(etExtension.getText().toString().trim()))
                wxMap.put("RoyaltyParameters", etExtension.getText().toString().trim());//扩展字段
            wxMap.put("BankCode", "WXPAY");
            if (!StringUtils.isEmpty(etNodifyUrl.getText().toString().trim()))
                wxMap.put("NotifyUrl", etNodifyUrl.getText().toString().trim());// 服务器异步通知页面路
            dialog = ProgressDialog.show(this, "提示", "正在获取预支付订单...");
            dialog.setCancelable(true);
            YQPayApi.doPay(this, wxMap, new IYQPayCallback() {
                @Override
                public void payResult(final String status, final String message) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();
                            Log.e("RESULT", "CALLBACK: " + "status:" + status + " message:" + message);
                            if (status == null || "".equals(status)) {
                                Toast.makeText(MainActivity.this, "获取预付订单失败！", Toast.LENGTH_LONG).show();
                            } else {
                                if (CALLBACK_FAILD.equals(status)) {
                                    String msg = "";
                                    if (CALLBACK_PARAM_FREE.equals(message)) {
                                        msg = "调用收银台时没有传递参数";
                                    } else if (CALLBACK_SERVER_NO_PARAM.equals(message)) {
                                        msg = "订单生成失败";
                                    } else if (CALLBACK_HTTPSTATUSEXCEPTION.equals(message)) {
                                        msg = "网络连接异常";
                                    } else if (CALLBACK_IOEXCEPTION.equals(message)) {
                                        msg = "网络读取异常";
                                    } else if (CALLBACK_NONETWORK.equals(message)) {
                                        msg = "无访问网络";
                                    } else if (CALLBACK_DATAERROR.equals(message)) {
                                        msg = "数据解析错误";
                                    } else if (CALLBACK_CANCEL_PAY.equals(message)) {
                                        msg = "取消支付";
                                    } else if (CALLBACK_FAILED_PAY.equals(message)) {
                                        msg = "支付失败";
                                    } else {
                                        msg = message;
                                    }
//                                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                                    showFildDialog(msg);
                                } else if (CALLBACK_SUCCESS.equals(status)) {
                                    Toast.makeText(MainActivity.this, "支付成功", Toast.LENGTH_LONG).show();
                                } else if (EntryActivity.CALLBACK_PROCESS.equals(status)) {
                                    Toast.makeText(MainActivity.this, "处理中", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
            });
        }
    }

    // 畅捷支付 畅捷前台
    public void do_chanpay_wap_front_end(View v) {
        Intent intent = new Intent();
        intent.putExtra("reqMap", reqMap);
        intent.setClass(MainActivity.this, FrontEndQuickPayActivity.class);
        startActivity(intent);
    }

    // 畅捷收银台支付
    public void do_chanpay_wap(View v) {
        Intent intent = new Intent();
        intent.putExtra("reqMap", reqMap);
        intent.setClass(MainActivity.this, QuickPayActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LOG.logI("==================onActivityResult==================");
        if (REQUEST_CODE == requestCode) {
            showConfirmDialog();
        }
    }

    private void showConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("订单" + outtradeno + "\n请根据支付的情况点击下方按钮，请不要重复支付。")
                .setNegativeButton("未支付", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("支付成功", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        doQueryState();
                    }
                }).show();
    }

    private ProgressDialog dialog;
    private HashMap<String, String> reqMap;
    //    private TradeInfo info;
    private String userId;


    private void doQueryState() {
        final HashMap<String, String> queryMap = (HashMap<String, String>) reqMap.clone();
        queryMap.put("TrxId", StringUtils.getOrderid());
        queryMap.put("OrderTrxId", outtradeno);
        queryMap.put("TradeType", "pay_order");
        dialog = ProgressDialog.show(MainActivity.this, "提示", "正在获取支付结果...");
        dialog.setCancelable(true);
        YQPay pay = YQPay.getInstance(MainActivity.this);
        pay.queryState(queryMap, new IYQPayCallback() {
            @Override
            public void payResult(final String status, final String message) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        dialog.dismiss();
                        Log.i("RESULT", "CALLBACK: " + "status:" + status + " message:" + message);
                        if (CALLBACK_FAILD.equals(status)) {
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                        } else {
                            Map<String, String> respMap = new Gson().fromJson(message, HashMap.class);
                            if (CALLBACK_SUCCESS.equals(status)) {
                                Toast.makeText(MainActivity.this, "支付成功", Toast.LENGTH_LONG).show();
                            } else if (EntryActivity.CALLBACK_PROCESS.equals(status)) {
                                Toast.makeText(MainActivity.this, "处理中", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        });
    }

    private void showFildDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("提示")
//                .setMessage("获取支付结果失败，你可以稍后再查看已支付订单，请不要重复支付。")
                .setMessage(message)
                .setPositiveButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    /*public static String getKeyValue(String key, String info) {
        String dinfo = URLDecoder.decode(info);
        String[] str = dinfo.split("&");
        String result = info;
        for (int i = 0; i < str.length; i++) {
            String[] param = str[i].split("=");
            if (param.length == 2) {
                if (key.equals(param[0])) {
                    result = param[1];
                }
            }
        }
        return result;
    }*/

    // 生成用户唯一标识(该字段用商户自己定义格式)
    private String genUserId() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        }
        return super.onKeyDown(keyCode, event);
    }
}
