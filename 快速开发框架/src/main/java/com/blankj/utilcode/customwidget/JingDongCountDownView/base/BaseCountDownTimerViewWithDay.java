package com.blankj.utilcode.customwidget.JingDongCountDownView.base;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.CountDownTimerUtils;
import com.blankj.utilcode.util.LogUtils;

public abstract class BaseCountDownTimerViewWithDay extends LinearLayout {

    private Context mContext;

    /**
     * 倒计时控制器
     */
    private CountDownTimerUtils mCountDownTimer;

    private OnCountDownTimerListener OnCountDownTimerListener;

    private long mMillis;
    /**
     * 时
     */
    private TextView mDayTextView;

    /**
     * 时
     */
    private TextView mHourTextView;

    /**
     * 分
     */
    private TextView mMinTextView;

    /**
     * 秒
     */
    private TextView mSecondTextView;

    /**
     * 获取边框颜色
     *
     * @return
     */
    protected abstract String getStrokeColor();

    /**
     * 设置背景色
     *
     * @return
     */
    protected abstract String getBackgroundColor();

    /**
     * 获取文字颜色
     *
     * @return
     */
    protected abstract String getTextColor();

    /**
     * 获取边框圆角
     *
     * @return
     */
    protected abstract int getCornerRadius();

    /**
     * 获取标签文字大小
     *
     * @return
     */
    protected abstract int getTextSize();

    public BaseCountDownTimerViewWithDay(Context context, AttributeSet attrs,
                                         int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        init();
    }

    public BaseCountDownTimerViewWithDay(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseCountDownTimerViewWithDay(Context context) {
        this(context, null);
    }

    private void init() {
        this.setOrientation(HORIZONTAL);// 设置布局排列方式
        createView();// 创造三个标签
        addLabelView();// 添加标签到容器中
    }

    /**
     * 创建时、分、秒的标签
     */
    private void createView() {
        mDayTextView= createLabel();
        mDayTextView.setTextSize(14f);
        mHourTextView = createLabel();
        mHourTextView.setTextSize(14f);
        mMinTextView = createLabel();
        mMinTextView.setTextSize(14f);
        mSecondTextView = createLabel();
        mSecondTextView.setTextSize(14f);
    }

    /**
     * 添加标签到容器中
     */
    private void addLabelView() {
        removeAllViews();
        this.addView(mDayTextView);
        this.addView(createColon());
        this.addView(mHourTextView);
        this.addView(createColon());
        this.addView(mMinTextView);
        this.addView(createColon());
        this.addView(mSecondTextView);
    }
    /**
     * 创建冒号
     *
     * @return
     */
    private TextView createColon() {
        TextView textView = new TextView(mContext);
        textView.setText(":");
        textView .setTextColor(Color.WHITE);
        return textView;
    }

    /**
     * 创建标签
     *
     * @return
     */
    private TextView createLabel() {
        TextView textView = new GradientTextView(mContext)
                .setTextColor(getTextColor()).setStrokeColor(getStrokeColor())
                .setBackgroundColor(getBackgroundColor())
                .setTextSize(getTextSize()).setStrokeRadius(getCornerRadius())
                .build();
        return textView;
    }

    /**
     * 创建倒计时
     */
    private void createCountDownTimer() {
        if (mCountDownTimer == null) {
            mCountDownTimer = new CountDownTimerUtils(mMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    setSecond(millisUntilFinished);// 设置秒
                    if (OnCountDownTimerListener != null) {
                        OnCountDownTimerListener.onTick(millisUntilFinished);
                    }
                }

                @Override
                public void onFinish() {
                    if (OnCountDownTimerListener != null) {
                        OnCountDownTimerListener.onFinish();
                    }
                    mDayTextView.setText("00");
                    mHourTextView.setText("00");
                    mMinTextView.setText("00");
                    mSecondTextView.setText("00");
                }
            };
        }

    }

    /**
     * 设置秒
     *
     * @param millis
     */
    private void setSecond(long millis) {
        long totalSeconds = millis / 1000;
        String second = (int) (totalSeconds % 60) + "";// 秒
        long totalMinutes = totalSeconds / 60;
        String minute = (int) (totalMinutes % 60) + "";// 分
        long totalHours = totalMinutes / 60;
        String hour = (int) (totalHours % 24) + "";// 时
        String day=(int) (totalSeconds/3600 % 24)+"";
//        LogUtils.e("hour:" + hour);
//        LogUtils.e("minute:" + minute);
//        LogUtils.e("second:" + second);
        if (day.length() == 1) {
            day = "0" + day;
        }
        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        if (minute.length() == 1) {
            minute = "0" + minute;
        }
        if (second.length() == 1) {
            second = "0" + second;
        }
        mDayTextView.setText(day);
        mHourTextView.setText(hour);
        mMinTextView.setText(minute);
        mSecondTextView.setText(second);
    }

    /**
     * 设置监听事件
     *
     * @param listener
     */
    public void setDownTimerListener(OnCountDownTimerListener listener) {
        this.OnCountDownTimerListener = listener;
    }

    /**
     * 设置时间值
     *
     * @param millis
     */
    public void setDownTime(Long millis) {
        this.mMillis = millis;
    }

    /**
     * 开始倒计时
     */
    public void startDownTimer() {
        createCountDownTimer();// 创建倒计时
        if (mCountDownTimer != null) {
            mCountDownTimer.start();
        }
        LogUtils.e(mCountDownTimer == null);
    }


    public void cancelDownTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

}
