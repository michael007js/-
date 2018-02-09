package com.sss.car.custom.GoodsTypeSelect.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;


/**
 * 点击后更多与收起变换的TextView
 * Created by leilei on 2017/10/31.
 */

public class MoreTextView extends TextView {
    private OnMoreTextViewCallBack onMoreTextViewCallBack;
    private boolean moreOpen = false;


    public void setOnMoreTextViewCallBack(OnMoreTextViewCallBack onMoreTextViewCallBack) {
        this.onMoreTextViewCallBack = onMoreTextViewCallBack;
    }

    public MoreTextView(Context context) {
        super(context);
        init();
    }

    public MoreTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MoreTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public boolean isMoreOpen() {
        return moreOpen;
    }

    public MoreTextView moreOpen(boolean isMoreMode) {
        this.moreOpen = isMoreMode;
        showText();
        return this;
    }

    private void init() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e(moreOpen);
                if (moreOpen){
                    moreOpen=false;
                }else {
                    moreOpen=true;
                }
                showText();
                if (onMoreTextViewCallBack != null) {
                    onMoreTextViewCallBack.onMoreTextViewClick(moreOpen);

                }
            }
        });
    }

    private void showText() {
        if (moreOpen) {
            MoreTextView.this.setText("收起");
        } else {
            MoreTextView.this.setText("更多");
        }
        this.setGravity(Gravity.CENTER);
        this.setPadding(10, 10, 10, 10);
    }

    public interface OnMoreTextViewCallBack {
        void onMoreTextViewClick(boolean isModeMode);
    }

}
