package com.sss.car;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.chanjet.yqpay.util.MsgUtil;

public class WebViewActivity extends Activity {

    private static final int RESULTCODE = 200;

    private String url = "http://yqpay.chanpay.com:9708/";

    private LJWebView mLJWebView;

    public static void startActionForResult(Activity act, String url, int requestCode) {
        Intent it = new Intent(act, WebViewActivity.class);
        it.putExtra("url", url);
        act.startActivityForResult(it, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_webview);
        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent.getStringExtra("url") != null) {
            this.url = intent.getStringExtra("url");
//			this.url = "http://www.baidu.com";
        }
    }

    private void initView() {
        mLJWebView = (LJWebView) findViewById(R.id.webview);
        mLJWebView.requestFocuss();
        mLJWebView.setBarHeight(8);
        mLJWebView.setClickable(true);
        mLJWebView.setUseWideViewPort(true);
        mLJWebView.setSupportZoom(true);
        mLJWebView.setBuiltInZoomControls(true);
        mLJWebView.setJavaScriptEnabled(true);
        mLJWebView.addaddJavascriptInterface(new scriptInterface(), "windowInterface");
        mLJWebView.setJavaScriptCanOpenWindowsAutomatically(true);
        mLJWebView.setCacheMode(WebSettings.LOAD_NO_CACHE);
//		mLJWebView.setProgressStyle(LJWebView.Circle); // 设置WebView加载样式
        mLJWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtils.e("跳转的URL =" + url);
                if (url.endsWith("successful-finished")) {
                    finish();
                }

                try {
                    /*if (url.startsWith("alipays:")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        intent.addCategory("android.intent.category.BROWSABLE");
                        intent.setComponent(null);
                        startActivity(intent);
                        finish();
                        *//*return true;
                        url = "intent:" + url.substring(8);
                        MsgUtil.showDebugSOP("url is : " + url);*//*
                    }*/

                    if (url.startsWith("intent:") || url.startsWith("alipays:")) {
                        try {
                            Intent intent;
                            intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                            intent.addCategory("android.intent.category.BROWSABLE");
                            intent.setComponent(null);
                            finish();
                            startActivity(intent);
                        } catch (Exception e) {
                        }
                    } else if (url.startsWith("tel:")){
//                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));//直接拨打电话
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_DIAL);//携带号码到拨号盘，用户选择是否拨号
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }else {
                        view.loadUrl(url);
                    }
                    return true;
                } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    MsgUtil.showToastShort(WebViewActivity.this, "未找到对应应用");
                    return false;
                }
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                view.getSettings().setJavaScriptEnabled(true);
                super.onPageFinished(view, url);
                // html加载完成之后，添加监听js函数
                addImageClickListner();
            }

        });
        mLJWebView.loadUrl(url);
        ((ImageView) findViewById(R.id.img_back)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        /*if (mLJWebView.canGoBack()) {
            mLJWebView.goBack();
        } else {
            super.onBackPressed();
            setResult(RESULTCODE);
            finish();
        }*/
        super.onBackPressed();
        setResult(RESULTCODE);
        finish();
    }

    // js通信接口
    class scriptInterface {
        @android.webkit.JavascriptInterface
        public void closeWindow() {
            runOnUiThread(new Runnable() {
                public void run() {
                    setResult(RESULTCODE);
                    finish();
                }
            });
        }
    }

    // 注入js函数监听
    @android.webkit.JavascriptInterface
    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的closeme几点，并添加onclick函数
        mLJWebView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByName(\"closeme\"); " +  // closeme 是跟H5页面商定好的值，不可随意修改
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.windowInterface.closeWindow();  " +
                "    }  " +
                "}" +
                "})()");
    }
}
