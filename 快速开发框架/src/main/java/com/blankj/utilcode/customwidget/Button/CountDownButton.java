package com.blankj.utilcode.customwidget.Button;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;


/**
 * 倒计时按钮
 * Created by leilei on 2017/8/1.
 */

public class CountDownButton extends TextView implements View.OnClickListener {

    public interface CountDownButtonOperationCallBack {
        void onFinish();

        void onTick(long millisUntilFinished);

        void onClickFromUser(boolean isRunning, long millisUntilFinished);
    }


    /**
     * 操作回调接口
     */
    CountDownButtonOperationCallBack mCountDownButtonOperationCallBack;
    boolean isRunning = false;
    /**
     * 倒计时总时间
     */
    long mMillisInFuture = 60100;
    /**
     * 间隔
     */
    long mCountDownInterval = 1000;


    long mCurrentmillis = 0;
    CountDownTimer mCountDownTimer;


    public CountDownButton(Context context) {
        super(context);
        setOnClickListener(this);
    }

    public CountDownButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    public CountDownButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(this);
    }

    public void setOnOperationCallBack(CountDownButtonOperationCallBack countDownButtonOperationCallBack) {
        this.mCountDownButtonOperationCallBack = countDownButtonOperationCallBack;
    }

    public void start() {
        if (mCountDownTimer == null) {
            createCountTimer();
        }
        mCountDownTimer.start();
    }

    public void finish() {
        mCountDownTimer.onFinish();
    }

    public void cancel() {
        mCountDownTimer.cancel();
    }

    public void destroy() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mCountDownTimer = null;
        mCountDownButtonOperationCallBack = null;
    }

    public long getmCurrentmillis() {
        return mCurrentmillis;
    }

    /**
     * 倒计时时间间隔(毫秒)
     *
     * @param countDownInterval
     */
    public CountDownButton countDownInterval(long countDownInterval) {
        this.mCountDownInterval = countDownInterval + 100;
        return this;
    }

    /**
     * 倒计时总时间(毫秒)
     *
     * @param millisInFuture
     */
    public CountDownButton millisInFuture(long millisInFuture) {
        this.mMillisInFuture = millisInFuture;
        return this;
    }

    public CountDownButton createCountTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        mCountDownTimer = new CountDownTimer(mMillisInFuture, mCountDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                isRunning = true;
                CountDownButton.this.mCurrentmillis = millisUntilFinished;
                if (mCountDownButtonOperationCallBack != null) {
                    mCountDownButtonOperationCallBack.onTick(mCurrentmillis);
//                    LogUtils.e(mCurrentmillis);
                }
            }

            @Override
            public void onFinish() {
                CountDownButton.this.mMillisInFuture = 0;
                isRunning = false;
                if (mCountDownButtonOperationCallBack != null) {
                    mCountDownButtonOperationCallBack.onFinish();
                }
            }
        };
        return this;
    }


    @Override
    public void onClick(View v) {
        if (mCountDownButtonOperationCallBack != null) {
            mCountDownButtonOperationCallBack.onClickFromUser(isRunning, mCurrentmillis);
        }
    }
}
