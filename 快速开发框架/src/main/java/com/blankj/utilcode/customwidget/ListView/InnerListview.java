package com.blankj.utilcode.customwidget.ListView;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ListView;

import com.blankj.utilcode.util.LogUtils;

/**
 * Created by leilei on 2017/3/18.
 */

public class InnerListview extends ListView{
    public boolean isMeasure=false;
    int expandSpec;
    public InnerListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public InnerListview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InnerListview(Context context) {
        super(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.e("log", "onFinishInflate");
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e("log", "onAttachedToWindow");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        isMeasure=true;
        expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        isMeasure=false;
            super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }




}
