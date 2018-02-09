package com.blankj.utilcode.util;

/**
 * Created by leilei on 2017/5/5.
 */

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * WebView管理器，提供常用设置
 */
public class WebViewManagerUtils {
    private WebView webView;
    private WebSettings webSettings;

    public WebViewManagerUtils(WebView webView) {
        this.webView = webView;
        webSettings = webView.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
    }


    /**
     * 开启关联webview进度与标题
     *
     * @param seekBar
     * @return
     */
    public WebViewManagerUtils enableProgress(final SeekBar seekBar, final TextView title) {
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    if (seekBar != null)
                        seekBar.setVisibility(View.INVISIBLE);
                    if (title != null)
                        title.setText(webView.getTitle());
                } else {
                    if (View.INVISIBLE == seekBar.getVisibility()) {
                        seekBar.setVisibility(View.VISIBLE);
                    }
                    if (seekBar != null)
                        seekBar.setProgress(newProgress);
                    if (title != null)
                        title.setText("加载中...");
                }
                super.onProgressChanged(view, newProgress);
            }

        });
        return this;
    }


    /**
     * 开启在自身内打开网页
     *
     * @return
     */
    public WebViewManagerUtils enableOpenInWebview() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        return this;
    }

    /**
     * 开启自适应功能
     */
    public WebViewManagerUtils enableAdaptive() {
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        return this;
    }

    /**
     * 禁用自适应功能
     */
    public WebViewManagerUtils disableAdaptive() {
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        return this;
    }

    /**
     * 开启缩放功能
     */
    public WebViewManagerUtils enableZoom() {
        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        return this;
    }

    /**
     * 禁用缩放功能
     */
    public WebViewManagerUtils disableZoom() {
        webSettings.setSupportZoom(false);
        webSettings.setUseWideViewPort(false);
        webSettings.setBuiltInZoomControls(false);
        return this;
    }

    /**
     * 开启JavaScript
     */
    @SuppressLint("SetJavaScriptEnabled")
    public WebViewManagerUtils enableJavaScript() {
        webSettings.setJavaScriptEnabled(true);
        return this;
    }

    /**
     * 禁用JavaScript
     */
    public WebViewManagerUtils disableJavaScript() {
        webSettings.setJavaScriptEnabled(false);
        return this;
    }

    /**
     * 开启JavaScript自动弹窗
     */
    public WebViewManagerUtils enableJavaScriptOpenWindowsAutomatically() {
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        return this;
    }

    /**
     * 禁用JavaScript自动弹窗
     */
    public WebViewManagerUtils disableJavaScriptOpenWindowsAutomatically() {
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
        return this;
    }

    /**
     * 返回
     *
     * @return true：已经返回，false：到头了没法返回了
     */
    public boolean goBack() {
        if (webView.canGoBack()) {
            webView.goBack();
            return true;
        } else {
            return false;
        }
    }


    /**
     * 只截取屏幕中显示出来部分的webView画面，未显示的部分不会被截取
     * 前提：WebView要设置webView.setDrawingCacheEnabled(true);
     *
     * @return
     */
    private Bitmap captureWebViewVisibleSize() {
        return webView.getDrawingCache();
    }


    /**
     * 截取webView的整个页面，未显示的也会被截取
     *
     * @return
     */
    private Bitmap captureWebView() {
        Picture snapShot = webView.capturePicture();

        Bitmap bmp = Bitmap.createBitmap(snapShot.getWidth(), snapShot.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        snapShot.draw(canvas);
        return bmp;
    }
}