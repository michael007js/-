package com.chanjet.auscashier.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.chanjet.yqpay.util.LOG;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import com.chanjet.yqpay.wxpay.WXPay;

/**
 * LePaySDK回调类
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    /**
     * 微信APP_ID 替换为你的应用从官方网站申请到的合法appId
     */
//    private static final String APP_ID = "wx9f295dec5e92fbe5";
    private static final String APP_ID = "wx2a5538052969956e";

    private IWXAPI api;
    private WXPay wxPay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wxPay = WXPay.getInstance(this);
        api = WXAPIFactory.createWXAPI(this, APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        wxPay.setResp(resp);
        LOG.logE("resp:" + resp.errCode);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}