package com.blankj.utilcode.util;

/**
 * Created by leilei on 2017/8/8.
 */

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import static android.R.attr.delay;


public abstract class CountDownTimerUtils {

    private final long mMillisInFuture;

    private final long mCountdownInterval;

    private long mStopTimeInFuture;

    private boolean mCancelled = false;

    private boolean mComplete = false;

    private boolean mIsPause = false;

    public CountDownTimerUtils(long millisInFuture, long countDownInterval) {
        mMillisInFuture = millisInFuture;
        mCountdownInterval = countDownInterval;
    }

    public long getmStopTimeInFuture() {
        return mStopTimeInFuture;
    }

    public synchronized final void cancel() {
        mComplete = true;
        mCancelled = true;
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        mHandler = null;
        LogUtils.e("cancel");
    }

    public void onPause() {
        if (mComplete = false && mCancelled == false) {
            mIsPause = true;
        }

    }

    public void onContinue() {
        if (mComplete = false && mCancelled == false) {
            mIsPause = false;
        }

    }

    public synchronized final CountDownTimerUtils start() {
        mComplete = false;
        mCancelled = false;
        if (mMillisInFuture <= 0) {
            if (!mComplete) {
                onFinish();
            }

            return this;
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
        if (mHandler != null) {
            mHandler.sendMessage(mHandler.obtainMessage(MSG));
        }
        return this;
    }


    public abstract void onTick(long millisUntilFinished);

    public abstract void onFinish();


    public void onComplete() {
        this.mComplete = true;
    }

    private static final int MSG = 1;


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            synchronized (this) {
                if (mCancelled) {
                    return;
                }
                if (mIsPause) {
                    return;
                }
                final long millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime();

                if (millisLeft <= 0) {
                    if (!mComplete) {
                        onFinish();
                    }

                } else if (millisLeft < mCountdownInterval) {
                    sendMessageDelayed(obtainMessage(MSG), millisLeft);
                } else {
                    long lastTickStart = SystemClock.elapsedRealtime();
                    onTick(millisLeft);
                    long delay = lastTickStart + mCountdownInterval - SystemClock.elapsedRealtime();
                    while (delay < 0) {

                        delay += mCountdownInterval;
                    }
                    sendMessageDelayed(obtainMessage(MSG), delay);
                }
            }
        }
    };
}

