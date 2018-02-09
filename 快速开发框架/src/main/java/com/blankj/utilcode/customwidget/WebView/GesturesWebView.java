package com.blankj.utilcode.customwidget.WebView;

/**
 * Created by leilei on 2017/5/10.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.customwidget.Dialog.FlycoDialog.dialog.listener.OnOperItemClickL;
import com.blankj.utilcode.customwidget.Dialog.FlycoDialog.dialog.widget.ActionSheetDialog;
import com.blankj.utilcode.dao.OnAskDialogCallBack;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.WebViewManagerUtils;


/**
 * 手势滑动WebView
 */
public class GesturesWebView extends WebView implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    private Activity a;
    ActionSheetDialog dialog;
    //识别手势，有一个类GestureDetector
    private GestureDetector mGestureDetector;

    private WebViewManagerUtils webViewManagerUtils;

    public GesturesWebView(Context context) {
        super(context);
        //1、实例化
        mGestureDetector = new GestureDetector(context, this);
        //2、添加双击事件的监听
        mGestureDetector.setOnDoubleTapListener(this);

        webViewManagerUtils = new WebViewManagerUtils(this);
    }

    public GesturesWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //1、实例化
        mGestureDetector = new GestureDetector(context, this);
        //2、添加双击事件的监听
        mGestureDetector.setOnDoubleTapListener(this);

        webViewManagerUtils = new WebViewManagerUtils(this);
    }

    public GesturesWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //1、实例化
        mGestureDetector = new GestureDetector(context, this);
        //2、添加双击事件的监听
        mGestureDetector.setOnDoubleTapListener(this);

        webViewManagerUtils = new WebViewManagerUtils(this);
    }

    public GesturesWebView init(Activity a) {
        this.a = a;
        return this;
    }

    public void ondestroy(){
        if (dialog!=null){
            dialog.dismiss();
            dialog=null;
        }
        a=null;
        mGestureDetector=null;
        webViewManagerUtils=null;
    }
    /**
     * 开启关联webview进度与标题
     *
     * @param seekBar
     * @return
     */
    public GesturesWebView enableProgress(final SeekBar seekBar, final TextView title) {
        webViewManagerUtils.enableProgress(seekBar, title);
        return this;
    }

    /**
     * 开启在自身内打开网页
     *
     * @return
     */
    public GesturesWebView enableOpenInWebview() {
        webViewManagerUtils.enableOpenInWebview();
        return this;
    }

    /**
     * 开启自适应功能
     */
    public GesturesWebView enableAdaptive() {
        webViewManagerUtils.enableAdaptive();
        return this;
    }

    /**
     * 禁用自适应功能
     */
    public GesturesWebView disableAdaptive() {
        webViewManagerUtils.disableAdaptive();
        return this;
    }

    /**
     * 开启缩放功能
     */
    public GesturesWebView enableZoom() {
        webViewManagerUtils.enableZoom();
        return this;

    }

    /**
     * 禁用缩放功能
     */
    public GesturesWebView disableZoom() {
        webViewManagerUtils.disableZoom();
        return this;
    }

    /**
     * 开启JavaScript
     */
    @SuppressLint("SetJavaScriptEnabled")
    public GesturesWebView enableJavaScript() {
        webViewManagerUtils.enableJavaScript();
        return this;
    }

    /**
     * 禁用JavaScript
     */
    public GesturesWebView disableJavaScript() {
        webViewManagerUtils.disableJavaScript();
        return this;
    }

    /**
     * 开启JavaScript自动弹窗
     */
    public GesturesWebView enableJavaScriptOpenWindowsAutomatically() {
        webViewManagerUtils.enableJavaScriptOpenWindowsAutomatically();
        return this;
    }

    /**
     * 禁用JavaScript自动弹窗
     */
    public GesturesWebView disableJavaScriptOpenWindowsAutomatically() {
        webViewManagerUtils.disableJavaScriptOpenWindowsAutomatically();
        return this;
    }

    /**
     * 返回
     *
     * @return true：已经返回，false：到头了没法返回了
     */
    public boolean back() {
        if (GesturesWebView.this.canGoBack()) {
            GesturesWebView.this.goBack();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 前进
     *
     * @return true：已经前进，false：到头了没法前进了
     */
    public boolean forward() {
        if (GesturesWebView.this.canGoForward()) {
            GesturesWebView.this.goForward();
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
    public Bitmap captureWebViewVisibleSize() {
        return (GesturesWebView.this.getDrawingCache());
    }

    /**
     * 截取webView的整个页面，未显示的也会被截取
     *
     * @return
     */
    public Bitmap captureWebView() {
        Picture snapShot = GesturesWebView.this.capturePicture();

        Bitmap bmp = Bitmap.createBitmap(snapShot.getWidth(), snapShot.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        snapShot.draw(canvas);
        return bmp;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);//最高人民法院
        mGestureDetector.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
//        return mGestureDetector.onTouchEvent(event);//将屏幕上的触摸事件交给侦探
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d("flag", "----------------->onDown: 手指按下");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.d("flag", "----------------->onShowPress: 手指按下长安之前触发");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d("flag", "----------------->onSingleTapUp: 手指单击抬起事件");
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d("flag", "----------------->onScroll: 手指按下并且滑动");

        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d("flag", "----------------->onLongPress: 手指长按");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d("flag", "----------------->onFling: 手指滑动，手指屏幕，屏幕惯性滑动");
        //参数一二，MotionEvent，获取触摸事件的坐标，x轴坐标
        float x1 = e1.getX();
        float x2 = e2.getX();

        if (x1 - x2 > 400) {//e1在e2右边，手指开始的地方在右边---->向左滑动
            forward();
        } else if (x2 - x1 > 400) {
            if (back()==false) {
                createDialog();
            }
        }
        return true;
    }

    public void createDialog() {
        if (a==null){
            return;
        }
        APPOftenUtils.createAskDialog(a, "您即将退出页面,确定吗?", new OnAskDialogCallBack() {
            @Override
            public void onOKey(Dialog dialog) {
                dialog.dismiss();
                dialog = null;
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            sleep(500);
                            a.finish();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }

            @Override
            public void onCancel(Dialog dialog) {
                dialog.dismiss();
                dialog = null;
            }
        });
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.d("flag", "----------------->onSingleTapConfirmed: 单击确定事件");
        //单击实现向左滑动
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d("flag", "----------------->onDoubleTap: 双击事件触发");
        //向右滑动
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.d("flag", "----------------->" +
                "onDoubleTapEvent: 双击之间发生的触摸事件：移动，抬起……");
        return true;
    }
}